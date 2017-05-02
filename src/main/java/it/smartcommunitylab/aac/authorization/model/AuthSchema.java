package it.smartcommunitylab.aac.authorization.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import it.smartcommunitylab.aac.authorization.IAuthSchema;

public class AuthSchema implements IAuthSchema {
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
	@Override
	public IAuthSchema addChild(Node parent, Node child) throws NodeAlreadyExist {
		if (!index.containsKey(child.getQname())) {
			parent = parent.addChild(child);
			child = child.addParent(parent);
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
	@Override
	public IAuthSchema addChild(Node child) throws NodeAlreadyExist {
		return addChild(root, child);
	}

	@Override
	public boolean isValid(Resource res) {
		if (res == null) {
			throw new NullPointerException("resource cannot be null");
		}
		Node ref = getNode(res.getQnameRef());
		return ref != null && res.isInstanceOf(ref);
	}

	@Override
	public Set<Node> getChildren(Node node) {
		Set<Node> children = new HashSet<>();
		node.getChildren().stream().forEach(nodeNs -> {
			Node child = index.get(nodeNs);
			if (child != null) {
				children.add(child);
			}
		});
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
		return index.get(qname);
	}
}
