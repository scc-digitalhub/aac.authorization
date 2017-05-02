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

import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class MongoAuthSchemaHelper implements AuthSchemaHelper {

	@Autowired
	private MongoTemplate mongo;

	protected Node root;

	@PostConstruct
	private void init() {
		root = mongo.findOne(new Query(Criteria.where("qname").is(Node.ROOT_NODE_ATTRIBUTE)), Node.class);
		if (root == null) {
			root = createRootNode();
			mongo.insert(root);
		}
	}

	protected Node createRootNode() {
		return new Node(Node.ROOT_NODE_ATTRIBUTE);
	}

	@Override
	public AuthSchemaHelper addChild(Node parent, Node child) throws NodeAlreadyExist {
		Query q = new Query(Criteria.where("qname").is(parent.getQname()));
		Node p = mongo.findOne(q, Node.class);
		if (p != null) {
			p = p.addChild(child);
			child = child.addParent(p);
			try {
				mongo.insert(child);
			} catch (DuplicateKeyException e) {
				throw new NodeAlreadyExist();
			}
			Update ops = new Update();
			ops.set("childrenNs", p.getChildren());
			mongo.updateFirst(q, ops, Node.class);
		}
		return this;
	}

	@Override
	public AuthSchemaHelper addRootChild(Node child) throws NodeAlreadyExist {
		return addChild(root, child);
	}

	@Override
	public boolean isValid(Resource res) {
		Node n = getNode(res.getQnameRef());
		if (n != null) {
			return res.isInstanceOf(n);
		}

		return false;
	}

	@Override
	public Set<Node> getChildren(Node node) {
		Node n = getNode(node.getQname());
		Set<Node> children = new HashSet<>();
		if (n != null) {
		n.getChildren().stream().forEach(childNs -> {
			Node child = getNode(childNs);
			if (child != null) {
				children.add(child);
			}
		});
		}
		return children;
	}

	@Override
	public Set<Node> getAllChildren(Node node) {
		Set<Node> allChildren = new LinkedHashSet<>();
		Set<Node> directChildren = getChildren(node);
		allChildren.addAll(directChildren);
		Queue<Node> queue = new LinkedList<>();
		queue.addAll(directChildren);
		Node visit = null;
		while ((visit = queue.poll()) != null) {
			allChildren.addAll(getAllChildren(visit));
		}
		return allChildren;
	}

	@Override
	public Node getNode(String qname) {
		Query q = new Query(Criteria.where("qname").is(qname));
		return mongo.findOne(q, Node.class);
	}

}
