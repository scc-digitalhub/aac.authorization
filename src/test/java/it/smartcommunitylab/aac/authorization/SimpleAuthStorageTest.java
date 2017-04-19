package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.AuthSchema;
import it.smartcommunitylab.aac.authorization.model.AuthUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class SimpleAuthStorageTest {

	@Test
	public void insertFirstAuthorization() {
		AuthStorage storage = new SimpleAuthStorage(new AuthSchema());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		storage.insert(new Authorization("subject", "action", res, entity));

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");

		Assert.assertEquals(storage.search(new Authorization("subject", "action", res1, entity1)), true);

	}

	@Test
	public void searchNotExistentAuthWithEmptyStorage() {
		AuthStorage storage = new SimpleAuthStorage(new AuthSchema());

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");

		Assert.assertEquals(storage.search(new Authorization("subject", "action", res1, entity1)), false);

	}

	@Test
	public void searchNotExistentAuthWithPopulatedStorage() {
		AuthStorage storage = new SimpleAuthStorage(new AuthSchema());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		storage.insert(new Authorization("subject", "action", res, entity));

		Resource res1 = new Resource("B", Arrays.asList(new NodeValue("B", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");

		Assert.assertEquals(storage.search(new Authorization("subject", "action", res1, entity1)), false);

	}

	@Test
	public void removePresentAuth() {
		AuthStorage storage = new SimpleAuthStorage(new AuthSchema());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		Authorization auth1 = new Authorization("subject", "action", res, entity);
		storage.insert(auth1);

		Assert.assertTrue(storage.search(auth1));
		storage.remove(auth1);
		Assert.assertFalse(storage.search(auth1));
	}

	@Test
	public void removeNotPresentAuth() {
		AuthStorage storage = new SimpleAuthStorage(new AuthSchema());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity = new AuthUser("e1", "type1");
		Authorization auth1 = new Authorization("subject", "action", res, entity);
		storage.insert(auth1);

		Authorization dummy = new Authorization("s2", "action", null, new AuthUser("id2", "type"));

		Assert.assertTrue(storage.search(auth1));
		storage.remove(dummy);
		Assert.assertTrue(storage.search(auth1));
	}

	@Test
	public void searchOnWildcardAuthrorization() {
		AuthStorage storage = new SimpleAuthStorage(new AuthSchema());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", NodeValue.ALL_VALUE)));
		AuthUser entity = new AuthUser("e1", "type1");
		Authorization auth1 = new Authorization("subject", "action", res, entity);
		storage.insert(auth1);

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthUser entity1 = new AuthUser("e1", "type1");
		Authorization authToFind = new Authorization("subject", "action", res1, entity1);

		Assert.assertTrue(storage.search(authToFind));

	}

}
