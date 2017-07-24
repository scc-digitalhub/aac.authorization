package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class SimpleAuthorizationStorage implements AuthorizationStorage {

	private Map<Authorization, int[]> storage = new HashMap<>();

	private AuthorizationSchemaHelper schema;

	public SimpleAuthorizationStorage(AuthorizationSchemaHelper schema) {
		this.schema = schema;
	}

	@Override
	public Authorization insert(Authorization auth) {
		storage.put(auth, new int[0]);
		return auth;

	}

	@Override
	public void remove(Authorization auth) {
		storage.remove(auth);

	}

	@Override
	public boolean search(Authorization auth) {
		// check if resource is valid

		boolean checked = storage.containsKey(auth);
		if (checked) {
			return true;
		} else { // check if there is a WILDCARD
			for (Resource dep : createDependentResources(auth.getResource())) {
				Authorization depAuth = new Authorization(auth.getSubject(), auth.getActions(), dep, auth.getEntity());
				checked = storage.containsKey(depAuth);
				if (checked) {
					break;
				}
			}
		}
		return checked;
	}

	private List<Resource> createDependentResources(Resource res) {
		List<AuthorizationNodeValue> values = res.getValues();
		AuthorizationNodeValue removed = values.remove(values.size() - 1);
		values.add(createNodeValueAll(removed));
		Resource depRes = new Resource(res.getFqnameRef(), values);
		return Arrays.asList(depRes);

	}

	public AuthorizationNodeValue createNodeValueAll(AuthorizationNodeValue value) {
		return new AuthorizationNodeValue(value.getDefinition().getQname(), value.getDefinition().getName(),
				AuthorizationNodeValue.ALL_VALUE);
	}

	@Override
	public void remove(String authorizationId) {

	}

}
