package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeAlreadyExist;

public class AuthSchemaHelperTest {


	@Test
	public void allChildren() throws NodeAlreadyExist {
		AuthSchemaHelper p = new SimpleAuthSchemaHelper();
		Node node1 = new Node("A");
		Node node2 = new Node("B");
		Node node3 = new Node("C");
		Node node4 = new Node("D");
		Node node5 = new Node("E");
		Node node6 = new Node("F");
		p.addRootChild(node1);
		p.addRootChild(node6);
		p.addChild(node1, node2);
		p.addChild(node2, node3);
		p.addChild(node2, node4);
		p.addChild(node4, node5);

		Set<Node> result = new HashSet<>(Arrays.asList(node2, node3, node4, node5));
		Assert.assertEquals(result, p.getAllChildren(node1));
	}

	@Test
	public void getAllChildren() throws NodeAlreadyExist {
		AuthSchemaHelper authSchema = new SimpleAuthSchemaHelper();
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

	@Test(expected = NodeAlreadyExist.class)
	public void nodeAlreadyExist() throws NodeAlreadyExist {
		AuthSchemaHelper authSchema = new SimpleAuthSchemaHelper();
		Node nodeA = new Node("A");
		authSchema.addRootChild(nodeA);
		authSchema.addChild(nodeA, nodeA);
	}

	@Test
	public void addRootChild() throws NodeAlreadyExist {
		AuthSchemaHelper authSchema = new SimpleAuthSchemaHelper();
		Node node = new Node("A");
		Assert.assertNull(authSchema.getNode("A"));
		authSchema.addRootChild(node);
		Assert.assertNotNull(authSchema.getNode("A"));
	}

	@Test
	public void addSecondLevelChild() throws NodeAlreadyExist {
		AuthSchemaHelper authSchema = new SimpleAuthSchemaHelper();
		Node nodeA = new Node("A");
		authSchema.addRootChild(nodeA);
		Node nodeB = new Node("B");
		authSchema.addChild(nodeA, nodeB);

		Assert.assertNotNull(authSchema.getNode("B"));
		Set<Node> children = authSchema.getChildren(nodeA);
		Assert.assertEquals(1, children.size());
		Assert.assertEquals("B", children.iterator().next().getQname());
	}



}
