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
import org.springframework.stereotype.Component;

import it.smartcommunitylab.aac.authorization.AuthStorage;
import it.smartcommunitylab.aac.authorization.AuthSchemaHelper;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeParameter;
import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;
import it.smartcommunitylab.aac.authorization.mongo.ResourceDocument.AttributeDocument;

@Component
public class MongoAuthStorage implements AuthStorage {

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private AuthSchemaHelper authSchema;

	@Override
	public Authorization insert(Authorization auth) {
		return insertMainAuth(auth).toAuthorization();
	}

	private AuthGranted insertMainAuth(Authorization auth) {
		MainAuthGranted authGranted = new MainAuthGranted(auth);
		mongo.insert(authGranted);

		Node nodeDefinition = authSchema.getNode(auth.getResource().getQnameRef());
		if (nodeDefinition != null) {
			Set<Node> allChildren = authSchema.getAllChildren(nodeDefinition);
			allChildren.stream().forEach(childNodeDefinition -> {
				AuthGranted child = insertChildAuth(generateChildAuthorization(childNodeDefinition, auth),
						authGranted.getId());
				authGranted.addChildAuth(child.getId());
			});

			// update child refs
			Query q = new Query(Criteria.where("id").is(authGranted.getId()));
			Update ops = new Update().set("childAuths", authGranted.getChildAuths());
			mongo.findAndModify(q, ops, MainAuthGranted.class);
		}

		return authGranted;
	}

	private AuthGranted insertChildAuth(Authorization auth, String mainAuthId) {
		ChildAuthGranted authGranted = new ChildAuthGranted(auth);
		authGranted.setMainAuthGranted(mainAuthId);
		mongo.insert(authGranted);
		return authGranted;
	}

	private Authorization generateChildAuthorization(Node childNodeDefinition, Authorization parentAuthorization) {
		return new Authorization(parentAuthorization.getSubject(), parentAuthorization.getAction(),
				generateChildResource(childNodeDefinition, parentAuthorization.getResource()),
				parentAuthorization.getEntity());
	}

	private Resource generateChildResource(Node childNodeDefinition, Resource parentResource) {
		List<NodeValue> childValues = new ArrayList<>(parentResource.getValues());
		List<NodeParameter> definitions = childValues.stream().map(childValue -> {
			return childValue.getDefinition();
		}).collect(Collectors.toList());
		List<NodeParameter> toAssign = (List<NodeParameter>) CollectionUtils
				.subtract(childNodeDefinition.getParameters(), definitions);
		toAssign.stream().forEach(assign -> {
			childValues.add(new NodeValue(assign.getQname(), assign.getName(), NodeValue.ALL_VALUE));
		});
		return new Resource(childNodeDefinition.getQname(), childValues);

	}

	@Override
	public void remove(Authorization auth) {
		Query query = new Query(Criteria.where("id").is(auth.getId()));
		MainAuthGranted authGranted = mongo.findOne(query, MainAuthGranted.class);
		if (authGranted != null) {
			// try to remove child auths
			authGranted.getChildAuths().stream().forEach(child -> {
				Query q = new Query(Criteria.where("id").is(child));
				mongo.remove(q, ChildAuthGranted.class);
			});

			mongo.remove(query, MainAuthGranted.class);
		}
	}

	@Override
	public boolean search(Authorization auth) {
		AuthGranted authGranted = new MainAuthGranted(auth);
		Criteria crit = Criteria.where("subject");
		crit.is(auth.getSubject());
		crit.and("action").is(authGranted.getAction());
		crit.and("entity.id").is(authGranted.getEntity().getId());
		crit.and("entity.type").is(authGranted.getEntity().getType());
		crit.and("resource.qname").is(authGranted.getResource().getQname());
		for (AttributeDocument attr : authGranted.getResource().getAttributes()) {
			crit.and("resource.attrs." + attr.getName()).in(Arrays.asList(attr.getValue(), NodeValue.ALL_VALUE));
		}
		Query query = new Query(crit);
		return mongo.exists(query, MainAuthGranted.class);
	}

}
