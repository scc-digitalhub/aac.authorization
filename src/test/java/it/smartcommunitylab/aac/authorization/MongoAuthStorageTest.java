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
import it.smartcommunitylab.aac.authorization.model.AuthUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthStorage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfig.class,
		TestAppConfig.class }, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = { "mongo.dbname=aac-authorization-db-test" })
public class MongoAuthStorageTest {

	@Autowired
	private AuthStorage storage;

	@Autowired
	private MongoTemplate mongo;

	@Before
	public void clean() {
		mongo.getDb().dropDatabase();
	}

	@Test
	public void saveAuthorization() {
		Resource r = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("id", "type");
		Authorization auth = new Authorization("sub", "action", r, entity);
		storage.insert(auth);
	}

	@Test
	public void insertFirstAuthorization() {

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		storage.insert(new Authorization("subject", "action", res, entity));

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");

		Assert.assertEquals(true, storage.search(new Authorization("subject", "action", res1, entity1)));

	}

	@Test
	public void searchNotExistentAuthWithEmptyStorage() {

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");

		Assert.assertEquals(false, storage.search(new Authorization("subject", "action", res1, entity1)));

	}

	@Test
	public void searchNotExistentAuthWithPopulatedStorage() {

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		storage.insert(new Authorization("subject", "action", res, entity));

		Resource res1 = new Resource("B", Arrays.asList(new NodeValue("B", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");

		Assert.assertEquals(false, storage.search(new Authorization("subject", "action", res1, entity1)));

	}

	@Test
	public void removePresentAuth() {

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		Authorization auth1 = new Authorization("subject", "action", res, entity);
		auth1 = storage.insert(auth1);

		Assert.assertTrue(storage.search(auth1));
		storage.remove(auth1);
		Assert.assertFalse(storage.search(auth1));
	}

	@Test
	public void removeNotPresentAuth() {

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		Authorization auth1 = new Authorization("subject", "action", res, entity);
		storage.insert(auth1);

		Authorization dummy = new Authorization("s2", "action", null, new AuthUser("id2", "type"));

		Assert.assertTrue(storage.search(auth1));
		storage.remove(dummy);
		Assert.assertTrue(storage.search(auth1));
	}

	@Test
	public void searchOnWildcardAuthorization() {

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", NodeValue.ALL_VALUE)));
		AuthUser entity = new AuthUser("e1", "type1");
		Authorization auth1 = new Authorization("subject", "action", res, entity);
		storage.insert(auth1);

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");
		Authorization authToFind = new Authorization("subject", "action", res1, entity1);

		Assert.assertTrue(storage.search(authToFind));

	}

	@Test
	public void searchForAChildAuthorization() {
		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", NodeValue.ALL_VALUE)));
		AuthUser entity = new AuthUser("e1", "type1");
		Authorization auth1 = new Authorization("subject", "action", res, entity);
		storage.insert(auth1);

		Resource res1 = new Resource("E", Arrays.asList(new NodeValue("A", "a", "a_value"),
				new NodeValue("C", "c", "c_value"), new NodeValue("E", "e", "e_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");
		Authorization authToFind = new Authorization("subject", "action", res1, entity1);

		Assert.assertTrue(storage.search(authToFind));
	}
}

class TestAppConfig {

	@Bean
	public AuthStorage authStorage() {
		return new MongoAuthStorage();
	}

	/*
	 * 
	 * A (a) -- B (a b)
	 * 		 -- C (a c) -- D ( a c d)
	 * 		            -- E ( a c e)
	 *  		     
	 */

	@Bean
	public AuthSchemaHelper authSchema() {
		AuthSchemaHelper schema = new SimpleAuthSchemaHelper();
		Node node = new Node("A");
		node.addParameter("a");

		Node nodeB = new Node("B");
		nodeB.addParameter("b");
		Node nodeC = new Node("C");
		nodeC.addParameter("c");
		Node nodeD = new Node("D");
		nodeD.addParameter("d");
		Node nodeE = new Node("E");
		nodeE.addParameter("e");

		try {
			schema.addRootChild(node);
			schema.addChild(node, nodeC);
			schema.addChild(nodeC, nodeD);
			schema.addChild(nodeC, nodeE);
			schema.addChild(node, nodeB);
		} catch (NodeAlreadyExist e) {
			e.printStackTrace();
		}
		return schema;
	}
}
