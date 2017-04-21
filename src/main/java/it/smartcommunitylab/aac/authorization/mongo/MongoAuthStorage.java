package it.smartcommunitylab.aac.authorization.mongo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.aac.authorization.AuthStorage;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.mongo.ResourceDocument.AttributeDocument;

@Component
public class MongoAuthStorage implements AuthStorage {

	@Autowired
	private MongoTemplate mongo;

	@Override
	public Authorization insert(Authorization auth) {
		AuthGranted authGranted = new AuthGranted(auth);
		mongo.insert(authGranted);
		return authGranted.toAuthorization();

	}

	@Override
	public void remove(Authorization auth) {
		AuthGranted authGranted = new AuthGranted(auth);
		mongo.remove(authGranted);

	}

	@Override
	public boolean search(Authorization auth) {
		AuthGranted authGranted = new AuthGranted(auth);
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
		return mongo.exists(query, AuthGranted.class);
	}

}
