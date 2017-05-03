package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import it.smartcommunitylab.aac.authorization.config.MongoConfig;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthorizationStorage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfig.class,
		TestAppConfig.class }, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = { "mongo.dbname=aac-authorization-db-test" })
public class MongoAuthorizationStorageTest {

	@Autowired
	private AuthorizationStorage storage;

	@Autowired
	private MongoTemplate mongo;

	@Before
	public void clean() {
		mongo.getDb().dropDatabase();
	}

	@Test
	public void saveAuthorization() {
		Resource r = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("id", "type");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth = new Authorization(subject, "action", r, entity);
		storage.insert(auth);
	}

	@Test
	public void insertFirstAuthorization() {

		Resource res = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		storage.insert(new Authorization(subject, "action", res, entity));

		Resource res1 = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");

		Assert.assertEquals(true, storage.search(new Authorization(subject, "action", res1, entity1)));

	}

	@Test
	public void searchNotExistentAuthWithEmptyStorage() {

		Resource res1 = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Assert.assertEquals(false, storage.search(new Authorization(subject, "action", res1, entity1)));

	}

	@Test
	public void searchNotExistentAuthWithPopulatedStorage() {

		Resource res = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		storage.insert(new Authorization(subject, "action", res, entity));

		Resource res1 = new Resource("B", Arrays.asList(new AuthorizationNodeValue("B", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");

		Assert.assertEquals(false, storage.search(new Authorization(subject, "action", res1, entity1)));

	}

	@Test
	public void removePresentAuth() {

		Resource res = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth1 = new Authorization(subject, "action", res, entity);
		auth1 = storage.insert(auth1);

		Assert.assertTrue(storage.search(auth1));
		storage.remove(auth1);
		Assert.assertFalse(storage.search(auth1));
	}

	@Test
	public void removeNotPresentAuth() {

		Resource res = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth1 = new Authorization(subject, "action", res, entity);
		storage.insert(auth1);

		Authorization dummy = new Authorization(new AuthorizationUser("dummy", "dummy"), "action", null,
				new AuthorizationUser("id2", "type"));

		Assert.assertTrue(storage.search(auth1));
		storage.remove(dummy);
		Assert.assertTrue(storage.search(auth1));
	}

	@Test
	public void searchOnWildcardAuthorization() {

		Resource res = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", AuthorizationNodeValue.ALL_VALUE)));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth1 = new Authorization(subject, "action", res, entity);
		storage.insert(auth1);

		Resource res1 = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");
		Authorization authToFind = new Authorization(subject, "action", res1, entity1);

		Assert.assertTrue(storage.search(authToFind));

	}

	@Test
	public void searchForAChildAuthorization() {
		Resource res = new Resource("A", Arrays.asList(new AuthorizationNodeValue("A", "a", AuthorizationNodeValue.ALL_VALUE)));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth1 = new Authorization(subject, "action", res, entity);
		storage.insert(auth1);

		Resource res1 = new Resource("E", Arrays.asList(new AuthorizationNodeValue("A", "a", "a_value"),
				new AuthorizationNodeValue("C", "c", "c_value"), new AuthorizationNodeValue("E", "e", "e_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");
		Authorization authToFind = new Authorization(subject, "action", res1, entity1);

		Assert.assertTrue(storage.search(authToFind));
	}
}

class TestAppConfig {

	@Bean
	public AuthorizationStorage authStorage() {
		return new MongoAuthorizationStorage();
	}

	/*
	 * 
	 * A (a) -- B (a b)
	 * 		 -- C (a c) -- D ( a c d)
	 * 		            -- E ( a c e)
	 *  		     
	 */

	@Bean
	public AuthorizationSchemaHelper authSchema() {
		AuthorizationSchemaHelper schema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode node = new AuthorizationNode("A");
		node.addParameter("a");

		AuthorizationNode nodeB = new AuthorizationNode("B");
		nodeB.addParameter("b");
		AuthorizationNode nodeC = new AuthorizationNode("C");
		nodeC.addParameter("c");
		AuthorizationNode nodeD = new AuthorizationNode("D");
		nodeD.addParameter("d");
		AuthorizationNode nodeE = new AuthorizationNode("E");
		nodeE.addParameter("e");

		try {
			schema.addRootChild(node);
			schema.addChild(node, nodeC);
			schema.addChild(nodeC, nodeD);
			schema.addChild(nodeC, nodeE);
			schema.addChild(node, nodeB);
		} catch (AuthorizationNodeAlreadyExist e) {
			e.printStackTrace();
		}
		return schema;
	}
}
