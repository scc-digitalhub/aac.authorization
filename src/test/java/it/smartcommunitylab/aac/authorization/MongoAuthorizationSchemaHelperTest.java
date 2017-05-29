package it.smartcommunitylab.aac.authorization;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.google.common.io.CharStreams;

import it.smartcommunitylab.aac.authorization.config.MongoConfig;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeParam;
import it.smartcommunitylab.aac.authorization.model.FQname;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthorizationStorage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfig.class,
		MongoAuthSchemaConfig.class }, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = { "mongo.dbname=aac-authorization-db-test" })
public class MongoAuthorizationSchemaHelperTest {

	@Autowired
	private AuthorizationSchemaHelper authSchema;

	@Autowired
	private MongoTemplate mongo;

	@Before
	public void clean() {
		Query q = new Query(Criteria.where("fqname").ne(AuthorizationNode.ROOT_NODE_ATTRIBUTE));
		mongo.remove(q, AuthorizationNode.class);
	}

	@Test
	public void addRootChild() throws AuthorizationNodeAlreadyExist {
		AuthorizationNode node = new AuthorizationNode(new FQname("domain", "A"));
		Assert.assertNull(authSchema.getNode(new FQname("domain", "A")));
		authSchema.addRootChild(node);
		Assert.assertNotNull(authSchema.getNode(new FQname("domain", "A")));
	}

	@Test
	public void addSecondLevelChild() throws AuthorizationNodeAlreadyExist {
		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		authSchema.addRootChild(nodeA);
		AuthorizationNode nodeB = new AuthorizationNode(new FQname("domain", "B"));
		authSchema.addChild(nodeA, nodeB);

		Assert.assertNotNull(authSchema.getNode(new FQname("domain", "B")));
		Set<AuthorizationNode> children = authSchema.getChildren(nodeA);
		Assert.assertEquals(1, children.size());
		Assert.assertEquals(new FQname("domain", "B"), children.iterator().next().getFqname());
	}

	@Test(expected = AuthorizationNodeAlreadyExist.class)
	public void nodeAlreadyExist() throws AuthorizationNodeAlreadyExist {
		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		authSchema.addRootChild(nodeA);
		authSchema.addChild(nodeA, nodeA);
	}

	@Test
	public void nodeWithParameters() throws AuthorizationNodeAlreadyExist {
		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		nodeA.addParameter("a");
		nodeA.addParameter("n");
		authSchema.addRootChild(nodeA);
		Assert.assertEquals(
				Arrays.asList(new AuthorizationNodeParam("A", "a"), new AuthorizationNodeParam("A", "n")),
				authSchema.getNode(new FQname("domain", "A")).getParameters());

	}

	@Test
	public void allChildren() throws AuthorizationNodeAlreadyExist {
		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		AuthorizationNode nodeB = new AuthorizationNode(new FQname("domain", "B"));
		AuthorizationNode nodeC = new AuthorizationNode(new FQname("domain", "C"));
		AuthorizationNode nodeD = new AuthorizationNode(new FQname("domain", "D"));
		AuthorizationNode nodeE = new AuthorizationNode(new FQname("domain", "E"));
		AuthorizationNode nodeF = new AuthorizationNode(new FQname("domain", "F"));
		authSchema.addRootChild(nodeA);
		authSchema.addRootChild(nodeF);
		authSchema.addChild(nodeA, nodeB);
		authSchema.addChild(nodeB, nodeC);
		authSchema.addChild(nodeB, nodeD);
		authSchema.addChild(nodeD, nodeE);

		Set<AuthorizationNode> result = new HashSet<>(Arrays.asList(nodeB, nodeC, nodeD, nodeE));
		Assert.assertEquals(result, authSchema.getAllChildren(nodeA));
	}

	@Test
	public void getAllChildren() throws AuthorizationNodeAlreadyExist {
		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		authSchema.addRootChild(nodeA);
		AuthorizationNode nodeB = new AuthorizationNode(new FQname("domain", "B"));
		authSchema.addChild(nodeA, nodeB);

		AuthorizationNode nodeC = new AuthorizationNode(new FQname("domain", "C"));
		AuthorizationNode nodeD = new AuthorizationNode(new FQname("domain", "D"));
		AuthorizationNode nodeE = new AuthorizationNode(new FQname("domain", "E"));

		authSchema.addChild(nodeB, nodeC).addChild(nodeB, nodeD).addChild(nodeC, nodeE);

		Set<AuthorizationNode> children = authSchema.getAllChildren(nodeB);
		Assert.assertEquals(3, children.size());
	}

	@Test
	public void importSchema() throws UnsupportedEncodingException, IOException {
		String jsonContent = CharStreams.toString(new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("loadSchema1.json"), "UTF-8"));
		authSchema.loadJson(jsonContent);
		Assert.assertNotNull(authSchema.getNode(new FQname("cartella-studente", "data")));

	}

}

class MongoAuthSchemaConfig {

	@Bean
	public AuthorizationStorage authStorage() {
		return new MongoAuthorizationStorage();
	}

	@Bean
	public AuthorizationSchemaHelper authSchema() {
		return new MongoAuthorizationSchemaHelper();
	}

}
