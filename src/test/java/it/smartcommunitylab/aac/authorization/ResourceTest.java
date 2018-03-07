package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.FQname;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class ResourceTest {

	@Test
	public void validOneParam() {
		Resource r = new Resource(new FQname("domain", "a"),
				Arrays.asList(new AuthorizationNodeValue("a", "a1", "a_value")));
		AuthorizationNode a = new AuthorizationNode(new FQname("domain", "a"));
		a.addParameter("a1");
		Assert.assertEquals(true, r.isInstanceOf(a));
	}

	@Test
	public void validTwoParamSameOrder() {
		Resource r = new Resource(new FQname("domain", "a"),
				Arrays.asList(new AuthorizationNodeValue("a", "a1", "a_value"),
						new AuthorizationNodeValue("a", "b1", "b_value")));
		AuthorizationNode a = new AuthorizationNode(new FQname("domain", "a"));
		a.addParameter("a1");
		a.addParameter("b1");
		Assert.assertEquals(true, r.isInstanceOf(a));
	}

	@Test
	public void validTwoParamNotSameOrder() {
		Resource r = new Resource(new FQname("domain", "a"),
				Arrays.asList(new AuthorizationNodeValue("a", "b1", "b_value"),
						new AuthorizationNodeValue("a", "a1", "a_value")));
		AuthorizationNode a = new AuthorizationNode(new FQname("domain", "a"));
		a.addParameter("a1");
		a.addParameter("b1");
		Assert.assertEquals(true, r.isInstanceOf(a));
	}

	@Test
	public void invalidParamListSubSetThatNodeOne() {
		Resource r = new Resource(new FQname("domain", "a"),
				Arrays.asList(new AuthorizationNodeValue("a", "b1", "b_value")));
		AuthorizationNode a = new AuthorizationNode(new FQname("domain", "a"));
		a.addParameter("a1");
		a.addParameter("b1");
		Assert.assertEquals(false, r.isInstanceOf(a));
	}

	@Test
	public void invalidParamNotPresent() {
		Resource r = new Resource(new FQname("domain", "a"),
				Arrays.asList(new AuthorizationNodeValue("a", "b1", "b_value")));
		AuthorizationNode a = new AuthorizationNode(new FQname("domain", "a"));
		a.addParameter("a1");
		Assert.assertEquals(false, r.isInstanceOf(a));
	}

	@Test
	public void invalidParamListSuperSetThatNodeOne() {
		Resource r = new Resource(new FQname("domain", "a"),
				Arrays.asList(new AuthorizationNodeValue("a", "b1", "b_value"),
						new AuthorizationNodeValue("a", "c1", "c_value")));
		AuthorizationNode a = new AuthorizationNode(new FQname("domain", "a"));
		a.addParameter("a1");
		a.addParameter("b1");
		Assert.assertEquals(false, r.isInstanceOf(a));
	}
}
