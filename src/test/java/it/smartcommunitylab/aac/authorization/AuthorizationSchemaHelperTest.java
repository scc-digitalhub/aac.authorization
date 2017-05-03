package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;

public class AuthorizationSchemaHelperTest {


	@Test
	public void allChildren() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper p = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode node1 = new AuthorizationNode("A");
		AuthorizationNode node2 = new AuthorizationNode("B");
		AuthorizationNode node3 = new AuthorizationNode("C");
		AuthorizationNode node4 = new AuthorizationNode("D");
		AuthorizationNode node5 = new AuthorizationNode("E");
		AuthorizationNode node6 = new AuthorizationNode("F");
		p.addRootChild(node1);
		p.addRootChild(node6);
		p.addChild(node1, node2);
		p.addChild(node2, node3);
		p.addChild(node2, node4);
		p.addChild(node4, node5);

		Set<AuthorizationNode> result = new HashSet<>(Arrays.asList(node2, node3, node4, node5));
		Assert.assertEquals(result, p.getAllChildren(node1));
	}

	@Test
	public void getAllChildren() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper authSchema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode nodeA = new AuthorizationNode("A");
		authSchema.addRootChild(nodeA);
		AuthorizationNode nodeB = new AuthorizationNode("B");
		authSchema.addChild(nodeA, nodeB);

		AuthorizationNode nodeC = new AuthorizationNode("C");
		AuthorizationNode nodeD = new AuthorizationNode("D");
		AuthorizationNode nodeE = new AuthorizationNode("E");

		authSchema.addChild(nodeB, nodeC).addChild(nodeB, nodeD).addChild(nodeC, nodeE);

		Set<AuthorizationNode> children = authSchema.getAllChildren(nodeB);
		Assert.assertEquals(3, children.size());
	}

	@Test(expected = AuthorizationNodeAlreadyExist.class)
	public void nodeAlreadyExist() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper authSchema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode nodeA = new AuthorizationNode("A");
		authSchema.addRootChild(nodeA);
		authSchema.addChild(nodeA, nodeA);
	}

	@Test
	public void addRootChild() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper authSchema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode node = new AuthorizationNode("A");
		Assert.assertNull(authSchema.getNode("A"));
		authSchema.addRootChild(node);
		Assert.assertNotNull(authSchema.getNode("A"));
	}

	@Test
	public void addSecondLevelChild() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper authSchema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode nodeA = new AuthorizationNode("A");
		authSchema.addRootChild(nodeA);
		AuthorizationNode nodeB = new AuthorizationNode("B");
		authSchema.addChild(nodeA, nodeB);

		Assert.assertNotNull(authSchema.getNode("B"));
		Set<AuthorizationNode> children = authSchema.getChildren(nodeA);
		Assert.assertEquals(1, children.size());
		Assert.assertEquals("B", children.iterator().next().getQname());
	}



}
