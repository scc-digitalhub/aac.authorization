package it.smartcommunitylab.aac.authorization;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;

public class AuthorizationNodeTest {

	@Test
	public void createNode() {
		AuthorizationNode first = new AuthorizationNode("istituto");
		Assert.assertEquals("istituto", first.getQname());
	}

	@Test
	public void addNode() {
		AuthorizationNode parent = new AuthorizationNode("istituto");
		AuthorizationNode child = new AuthorizationNode("anno");
		Assert.assertEquals(parent, parent.addChild(child));
	}

}
