package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.AuthSchema;

public class AuthSchemaTest {

	@Test
	public void allChildren() throws NodeAlreadyExist {
		AuthSchema p = new AuthSchema();
		Node node1 = new Node("A");
		Node node2 = new Node("B");
		Node node3 = new Node("C");
		Node node4 = new Node("D");
		Node node5 = new Node("E");
		Node node6 = new Node("F");
		p.addChild(node1);
		p.addChild(node6);
		p.addChild(node1, node2);
		p.addChild(node2, node3);
		p.addChild(node2, node4);
		p.addChild(node4, node5);

		Set<Node> result = new HashSet<>(Arrays.asList(node2, node3, node4, node5));
		Assert.assertEquals(result, p.getAllChildren(node1));
	}
}
