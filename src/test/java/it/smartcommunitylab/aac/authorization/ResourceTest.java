package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class ResourceTest {

	@Test
	public void validOneParam() {
		Resource r = new Resource("a", Arrays.asList(new NodeValue("a", "a1", "a_value")));
		Node a = new Node("a", Arrays.asList("a1"));
		Assert.assertEquals(true, r.isInstanceOf(a));
	}

	@Test
	public void validTwoParamSameOrder() {
		Resource r = new Resource("a",
				Arrays.asList(new NodeValue("a", "a1", "a_value"), new NodeValue("a", "b1", "b_value")));
		Node a = new Node("a", Arrays.asList("a1", "b1"));
		// Assert.assertEquals(a.isValid(Arrays.asList(new NodeParameter("a",
		// "a1"), new NodeParameter("a", "b1"))), true);
		Assert.assertEquals(true, r.isInstanceOf(a));
	}

	@Test
	public void validTwoParamNotSameOrder() {
		Resource r = new Resource("a",
				Arrays.asList(new NodeValue("a", "b1", "b_value"), new NodeValue("a", "a1", "a_value")));
		Node a = new Node("a", Arrays.asList("a1", "b1"));
		// Assert.assertEquals(a.isValid(Arrays.asList(new NodeParameter("a",
		// "b1"), new NodeParameter("a", "a1"))), true);
		Assert.assertEquals(true, r.isInstanceOf(a));
	}

	// @Test
	// public void validParamListSubSetThatNodeOne() {
	// Resource r = new Resource("a", Arrays.asList(new NodeValue("a", "b1",
	// "b_value")));
	// Node a = new Node("a", Arrays.asList("a1", "b1"));
	// Assert.assertEquals(true, a.isValid(Arrays.asList(new NodeParameter("a",
	// "b1"))));
	// }

	@Test
	public void invalidParamNotPresent() {
		Resource r = new Resource("a", Arrays.asList(new NodeValue("a", "b1", "b_value")));
		Node a = new Node("a", Arrays.asList("a1"));
		Assert.assertEquals(false, r.isInstanceOf(a));
	}

	@Test
	public void invalidParamListSuperSetThatNodeOne() {
		Resource r = new Resource("a",
				Arrays.asList(new NodeValue("a", "b1", "b_value"), new NodeValue("a", "c1", "c_value")));
		Node a = new Node("a", Arrays.asList("a1", "b1"));
		Assert.assertEquals(false, r.isInstanceOf(a));
	}
}
