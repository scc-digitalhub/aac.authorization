package it.smartcommunitylab.aac.authorization;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class MongoAuthorizationSchemaHelper implements AuthorizationSchemaHelper {

	@Autowired
	private MongoTemplate mongo;

	protected AuthorizationNode root;

	@PostConstruct
	private void init() {
		root = mongo.findOne(new Query(Criteria.where("qname").is(AuthorizationNode.ROOT_NODE_ATTRIBUTE)), AuthorizationNode.class);
		if (root == null) {
			root = createRootNode();
			mongo.insert(root);
		}
	}

	protected AuthorizationNode createRootNode() {
		return new AuthorizationNode(AuthorizationNode.ROOT_NODE_ATTRIBUTE);
	}

	@Override
	public AuthorizationSchemaHelper addChild(AuthorizationNode parent, AuthorizationNode child) throws AuthorizationNodeAlreadyExist {
		Query q = new Query(Criteria.where("qname").is(parent.getQname()));
		AuthorizationNode p = mongo.findOne(q, AuthorizationNode.class);
		if (p != null) {
			p = p.addChild(child);
			child = child.addParent(p);
			try {
				mongo.insert(child);
			} catch (DuplicateKeyException e) {
				throw new AuthorizationNodeAlreadyExist();
			}
			Update ops = new Update();
			ops.set("childrenNs", p.getChildren());
			mongo.updateFirst(q, ops, AuthorizationNode.class);
		}
		return this;
	}

	@Override
	public AuthorizationSchemaHelper addRootChild(AuthorizationNode child) throws AuthorizationNodeAlreadyExist {
		return addChild(root, child);
	}

	@Override
	public boolean isValid(Resource res) {
		AuthorizationNode n = getNode(res.getQnameRef());
		if (n != null) {
			return res.isInstanceOf(n);
		}

		return false;
	}

	@Override
	public Set<AuthorizationNode> getChildren(AuthorizationNode node) {
		AuthorizationNode n = getNode(node.getQname());
		Set<AuthorizationNode> children = new HashSet<>();
		if (n != null) {
		n.getChildren().stream().forEach(childNs -> {
			AuthorizationNode child = getNode(childNs);
			if (child != null) {
				children.add(child);
			}
		});
		}
		return children;
	}

	@Override
	public Set<AuthorizationNode> getAllChildren(AuthorizationNode node) {
		Set<AuthorizationNode> allChildren = new LinkedHashSet<>();
		Set<AuthorizationNode> directChildren = getChildren(node);
		allChildren.addAll(directChildren);
		Queue<AuthorizationNode> queue = new LinkedList<>();
		queue.addAll(directChildren);
		AuthorizationNode visit = null;
		while ((visit = queue.poll()) != null) {
			allChildren.addAll(getAllChildren(visit));
		}
		return allChildren;
	}

	@Override
	public AuthorizationNode getNode(String qname) {
		Query q = new Query(Criteria.where("qname").is(qname));
		return mongo.findOne(q, AuthorizationNode.class);
	}

}
