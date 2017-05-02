package it.smartcommunitylab.aac.authorization;

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

import it.smartcommunitylab.aac.authorization.config.MongoConfig;
import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.NodeParameter;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthStorage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfig.class,
		MongoAuthSchemaConfig.class }, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = { "mongo.dbname=aac-authorization-db-test" })
public class MongoAuthSchemaHelperTest {

	@Autowired
	private AuthSchemaHelper authSchema;

	@Autowired
	private MongoTemplate mongo;

	@Before
	public void clean() {
		Query q = new Query(Criteria.where("qname").ne(Node.ROOT_NODE_ATTRIBUTE));
		mongo.remove(q, Node.class);
	}

	@Test
	public void addRootChild() throws NodeAlreadyExist {
		Node node = new Node("A");
		Assert.assertNull(authSchema.getNode("A"));
		authSchema.addRootChild(node);
		Assert.assertNotNull(authSchema.getNode("A"));
	}

	@Test
	public void addSecondLevelChild() throws NodeAlreadyExist {
		Node nodeA = new Node("A");
		authSchema.addRootChild(nodeA);
		Node nodeB = new Node("B");
		authSchema.addChild(nodeA, nodeB);

		Assert.assertNotNull(authSchema.getNode("B"));
		Set<Node> children = authSchema.getChildren(nodeA);
		Assert.assertEquals(1, children.size());
		Assert.assertEquals("B", children.iterator().next().getQname());
	}

	@Test(expected = NodeAlreadyExist.class)
	public void nodeAlreadyExist() throws NodeAlreadyExist {
		Node nodeA = new Node("A");
		authSchema.addRootChild(nodeA);
		authSchema.addChild(nodeA, nodeA);
	}

	@Test
	public void nodeWithParameters() throws NodeAlreadyExist {
		Node nodeA = new Node("A");
		nodeA.addParameter("a");
		nodeA.addParameter("n");
		authSchema.addRootChild(nodeA);
		Assert.assertEquals(Arrays.asList(new NodeParameter("A", "a"), new NodeParameter("A", "n")),
				authSchema.getNode("A").getParameters());

	}

	@Test
	public void allChildren() throws NodeAlreadyExist {
		Node nodeA = new Node("A");
		Node nodeB = new Node("B");
		Node nodeC = new Node("C");
		Node nodeD = new Node("D");
		Node nodeE = new Node("E");
		Node nodeF = new Node("F");
		authSchema.addRootChild(nodeA);
		authSchema.addRootChild(nodeF);
		authSchema.addChild(nodeA, nodeB);
		authSchema.addChild(nodeB, nodeC);
		authSchema.addChild(nodeB, nodeD);
		authSchema.addChild(nodeD, nodeE);

		Set<Node> result = new HashSet<>(Arrays.asList(nodeB, nodeC, nodeD, nodeE));
		Assert.assertEquals(result, authSchema.getAllChildren(nodeA));
	}

	@Test
	public void getAllChildren() throws NodeAlreadyExist {
		Node nodeA = new Node("A");
		authSchema.addRootChild(nodeA);
		Node nodeB = new Node("B");
		authSchema.addChild(nodeA, nodeB);

		Node nodeC = new Node("C");
		Node nodeD = new Node("D");
		Node nodeE = new Node("E");

		authSchema.addChild(nodeB, nodeC).addChild(nodeB, nodeD).addChild(nodeC, nodeE);

		Set<Node> children = authSchema.getAllChildren(nodeB);
		Assert.assertEquals(3, children.size());
	}

}

class MongoAuthSchemaConfig {

	@Bean
	public AuthStorage authStorage() {
		return new MongoAuthStorage();
	}

	@Bean
	public AuthSchemaHelper authSchema() {
		return new MongoAuthSchemaHelper();
	}

}
