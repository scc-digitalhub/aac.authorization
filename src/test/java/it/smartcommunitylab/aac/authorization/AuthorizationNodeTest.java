package it.smartcommunitylab.aac.authorization;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.FQname;

public class AuthorizationNodeTest {

	@Test
	public void createNode() {
		AuthorizationNode first = new AuthorizationNode(new FQname("domain", "istituto"));
		Assert.assertEquals(new FQname("domain", "istituto"), first.getFqname());
	}

	@Test
	public void addNode() {
		AuthorizationNode parent = new AuthorizationNode(new FQname("domain", "istituto"));
		AuthorizationNode child = new AuthorizationNode(new FQname("domain", "anno"));
		Assert.assertEquals(parent, parent.addChild(child));
	}

}
