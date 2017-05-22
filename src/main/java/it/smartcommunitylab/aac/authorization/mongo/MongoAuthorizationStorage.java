package it.smartcommunitylab.aac.authorization.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import it.smartcommunitylab.aac.authorization.AuthorizationSchemaHelper;
import it.smartcommunitylab.aac.authorization.AuthorizationStorage;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeParam;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;
import it.smartcommunitylab.aac.authorization.mongo.ResourceDocument.AttributeDocument;

public class MongoAuthorizationStorage implements AuthorizationStorage {

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private AuthorizationSchemaHelper authSchema;

	@Override
	public Authorization insert(Authorization auth) {
		return insertMainAuth(auth).toAuthorization();
	}

	private AuthorizationGranted insertMainAuth(Authorization auth) {
		MainAuthorizationGranted authGranted = new MainAuthorizationGranted(auth);
		mongo.insert(authGranted);

		AuthorizationNode nodeDefinition = authSchema.getNode(auth.getResource().getFqnameRef());
		if (nodeDefinition != null) {
			Set<AuthorizationNode> allChildren = authSchema.getAllChildren(nodeDefinition);
			allChildren.stream().forEach(childNodeDefinition -> {
				AuthorizationGranted child = insertChildAuth(generateChildAuthorization(childNodeDefinition, auth),
						authGranted.getId());
				authGranted.addChildAuth(child.getId());
			});

			// update child refs
			Query q = new Query(Criteria.where("id").is(authGranted.getId()));
			Update ops = new Update().set("childAuths", authGranted.getChildAuths());
			mongo.findAndModify(q, ops, MainAuthorizationGranted.class);
		}

		return authGranted;
	}

	private AuthorizationGranted insertChildAuth(Authorization auth, String mainAuthId) {
		ChildAuthorizationGranted authGranted = new ChildAuthorizationGranted(auth);
		authGranted.setMainAuthGranted(mainAuthId);
		mongo.insert(authGranted);
		return authGranted;
	}

	private Authorization generateChildAuthorization(AuthorizationNode childNodeDefinition, Authorization parentAuthorization) {
		return new Authorization(parentAuthorization.getSubject(), parentAuthorization.getAction(),
				generateChildResource(childNodeDefinition, parentAuthorization.getResource()),
				parentAuthorization.getEntity());
	}

	private Resource generateChildResource(AuthorizationNode childNodeDefinition, Resource parentResource) {
		List<AuthorizationNodeValue> childValues = new ArrayList<>(parentResource.getValues());
		List<AuthorizationNodeParam> definitions = childValues.stream().map(childValue -> {
			return childValue.getDefinition();
		}).collect(Collectors.toList());
		List<AuthorizationNodeParam> toAssign = (List<AuthorizationNodeParam>) CollectionUtils
				.subtract(childNodeDefinition.getParameters(), definitions);
		toAssign.stream().forEach(assign -> {
			childValues.add(
					new AuthorizationNodeValue(assign.getQname(), assign.getName(), AuthorizationNodeValue.ALL_VALUE));
		});
		return new Resource(childNodeDefinition.getFqname(), childValues);

	}

	@Override
	public void remove(Authorization auth) {
		remove(auth.getId());
	}

	@Override
	public boolean search(Authorization auth) {
		AuthorizationGranted authGranted = new MainAuthorizationGranted(auth);
		Criteria crit = Criteria.where("subject");
		crit.is(auth.getSubject());
		crit.and("action").is(authGranted.getAction());
		crit.and("entity").is(authGranted.getEntity());
		crit.and("resource.fqname").is(authGranted.getResource().getFqname());
		for (AttributeDocument attr : authGranted.getResource().getAttributes()) {
			crit.and("resource.attrs." + attr.getName()).in(Arrays.asList(attr.getValue(), AuthorizationNodeValue.ALL_VALUE));
		}
		Query query = new Query(crit);
		return mongo.exists(query, MainAuthorizationGranted.class);
	}

	@Override
	public void remove(String authorizationId) {
		Query query = new Query(Criteria.where("id").is(authorizationId));
		MainAuthorizationGranted authGranted = mongo.findOne(query, MainAuthorizationGranted.class);
		if (authGranted != null) {
			// try to remove child auths
			authGranted.getChildAuths().stream().forEach(child -> {
				Query q = new Query(Criteria.where("id").is(child));
				mongo.remove(q, ChildAuthorizationGranted.class);
			});

			mongo.remove(query, MainAuthorizationGranted.class);
		}

	}

}
