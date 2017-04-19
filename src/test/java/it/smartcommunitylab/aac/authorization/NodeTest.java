package it.smartcommunitylab.aac.authorization;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.Node;

public class NodeTest {

	@Test
	public void createNode() {
		Node first = new Node("istituto");
		Assert.assertEquals("istituto", first.getQname());
	}

	@Test
	public void addNode() {
		Node parent = new Node("istituto");
		Node child = new Node("anno");
		Assert.assertEquals(parent, parent.addNode(child));
	}

}
