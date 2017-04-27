package it.smartcommunitylab.aac.authorization.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AuthSchema {
	private Node root;

	private Set<String> nid = new HashSet<>();

	private Map<String, Node> index = new HashMap<>();

	public AuthSchema() {
		root = new Node(Node.ROOT_NODE_ATTRIBUTE);
	}

	/**
	 * Add a child to given {@link Node}
	 * 
	 * @param parent
	 * @param child
	 * @return
	 * @throws NodeAlreadyExist
	 */
	public AuthSchema addChild(Node parent, Node child) throws NodeAlreadyExist {
		if (!index.containsKey(child.getQname())) {
			parent.addNode(child);
			index.put(child.getQname(), child);
		} else {
			throw new NodeAlreadyExist();
		}
		return this;
	}

	/**
	 * Add a child to root node
	 * 
	 * @param child
	 * @return
	 * @throws NodeAlreadyExist
	 */
	public AuthSchema addChild(Node child) throws NodeAlreadyExist {
		return addChild(root, child);
	}

	public boolean isValid(Resource res) {
		if (res == null) {
			throw new NullPointerException("resource cannot be null");
		}
		Node ref = getNode(res.getQnameRef());
		return ref != null && res.isInstanceOf(ref);
	}

	public Set<Node> getChildren(Node node) {
		return node.getChildren();
	}

	public Set<Node> getAllChildren(Node node) {
		return node.getAllChildren();
	}

	public Node getNode(String qname) {
		return index.get(qname);
	}
}
