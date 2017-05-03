package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class SimpleAuthorizationStorageTest {

	@Test
	public void insertFirstAuthorization() {
		AuthorizationStorage storage = new SimpleAuthorizationStorage(new SimpleAuthorizationSchemaHelper());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		storage.insert(new Authorization(subject, "action", res, entity));

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");

		Assert.assertEquals(storage.search(new Authorization(subject, "action", res1, entity1)), true);

	}

	@Test
	public void searchNotExistentAuthWithEmptyStorage() {
		AuthorizationStorage storage = new SimpleAuthorizationStorage(new SimpleAuthorizationSchemaHelper());

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");

		Assert.assertEquals(storage.search(new Authorization(subject, "action", res1, entity1)), false);

	}

	@Test
	public void searchNotExistentAuthWithPopulatedStorage() {
		AuthorizationStorage storage = new SimpleAuthorizationStorage(new SimpleAuthorizationSchemaHelper());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		storage.insert(new Authorization(subject, "action", res, entity));

		Resource res1 = new Resource("B", Arrays.asList(new NodeValue("B", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");

		Assert.assertEquals(storage.search(new Authorization(subject, "action", res1, entity1)), false);

	}

	@Test
	public void removePresentAuth() {
		AuthorizationStorage storage = new SimpleAuthorizationStorage(new SimpleAuthorizationSchemaHelper());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth1 = new Authorization(subject, "action", res, entity);
		storage.insert(auth1);

		Assert.assertTrue(storage.search(auth1));
		storage.remove(auth1);
		Assert.assertFalse(storage.search(auth1));
	}

	@Test
	public void removeNotPresentAuth() {
		AuthorizationStorage storage = new SimpleAuthorizationStorage(new SimpleAuthorizationSchemaHelper());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth1 = new Authorization(subject, "action", res, entity);
		storage.insert(auth1);

		Authorization dummy = new Authorization(new AuthorizationUser("dummy", "dummy"), "action", null,
				new AuthorizationUser("id2", "type"));

		Assert.assertTrue(storage.search(auth1));
		storage.remove(dummy);
		Assert.assertTrue(storage.search(auth1));
	}

	@Test
	public void searchOnWildcardAuthorization() {
		AuthorizationStorage storage = new SimpleAuthorizationStorage(new SimpleAuthorizationSchemaHelper());

		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", NodeValue.ALL_VALUE)));
		AuthorizationUser entity = new AuthorizationUser("e1", "type1");
		AuthorizationUser subject = new AuthorizationUser("sub", "type");
		Authorization auth1 = new Authorization(subject, "action", res, entity);
		storage.insert(auth1);

		Resource res1 = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_value")));
		AuthorizationUser entity1 = new AuthorizationUser("e1", "type1");
		Authorization authToFind = new Authorization(subject, "action", res1, entity1);

		Assert.assertTrue(storage.search(authToFind));

	}

}
