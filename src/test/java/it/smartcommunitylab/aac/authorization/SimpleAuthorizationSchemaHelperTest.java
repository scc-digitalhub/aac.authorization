package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.FQname;

public class SimpleAuthorizationSchemaHelperTest {


	@Test
	public void allChildren() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper p = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode node1 = new AuthorizationNode(new FQname("domain", "A"));
		AuthorizationNode node2 = new AuthorizationNode(new FQname("domain", "B"));
		AuthorizationNode node3 = new AuthorizationNode(new FQname("domain", "C"));
		AuthorizationNode node4 = new AuthorizationNode(new FQname("domain", "D"));
		AuthorizationNode node5 = new AuthorizationNode(new FQname("domain", "E"));
		AuthorizationNode node6 = new AuthorizationNode(new FQname("domain", "F"));
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

	@Test(expected = AuthorizationNodeAlreadyExist.class)
	public void nodeAlreadyExist() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper authSchema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		authSchema.addRootChild(nodeA);
		authSchema.addChild(nodeA, nodeA);
	}

	@Test
	public void addRootChild() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper authSchema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode node = new AuthorizationNode(new FQname("domain", "A"));
		Assert.assertNull(authSchema.getNode(new FQname("domain", "A")));
		authSchema.addRootChild(node);
		Assert.assertNotNull(authSchema.getNode(new FQname("domain", "A")));
	}

	@Test
	public void addSecondLevelChild() throws AuthorizationNodeAlreadyExist {
		AuthorizationSchemaHelper authSchema = new SimpleAuthorizationSchemaHelper();
		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		authSchema.addRootChild(nodeA);
		AuthorizationNode nodeB = new AuthorizationNode(new FQname("domain", "B"));
		authSchema.addChild(nodeA, nodeB);

		Assert.assertNotNull(authSchema.getNode(new FQname("domain", "B")));
		Set<AuthorizationNode> children = authSchema.getChildren(nodeA);
		Assert.assertEquals(1, children.size());
		Assert.assertEquals(new FQname("domain", "B"), children.iterator().next().getFqname());
	}



}
