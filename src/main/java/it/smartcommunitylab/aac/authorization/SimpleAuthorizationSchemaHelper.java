package it.smartcommunitylab.aac.authorization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class SimpleAuthorizationSchemaHelper implements AuthorizationSchemaHelper {
	private AuthorizationNode root;

	private Set<String> nid = new HashSet<>();

	private Map<String, AuthorizationNode> index = new HashMap<>();

	public SimpleAuthorizationSchemaHelper() {
		root = new AuthorizationNode(AuthorizationNode.ROOT_NODE_ATTRIBUTE);
	}

	/**
	 * Add a child to given {@link AuthorizationNode}
	 * 
	 * @param parent
	 * @param child
	 * @return
	 * @throws AuthorizationNodeAlreadyExist
	 */
	@Override
	public AuthorizationSchemaHelper addChild(AuthorizationNode parent, AuthorizationNode child) throws AuthorizationNodeAlreadyExist {
		if (!index.containsKey(child.getQname())) {
			parent = parent.addChild(child);
			child = child.addParent(parent);
			index.put(child.getQname(), child);
		} else {
			throw new AuthorizationNodeAlreadyExist();
		}
		return this;
	}

	/**
	 * Add a child to root node
	 * 
	 * @param child
	 * @return
	 * @throws AuthorizationNodeAlreadyExist
	 */
	@Override
	public AuthorizationSchemaHelper addRootChild(AuthorizationNode child) throws AuthorizationNodeAlreadyExist {
		return addChild(root, child);
	}

	@Override
	public boolean isValid(Resource res) {
		if (res == null) {
			throw new NullPointerException("resource cannot be null");
		}
		AuthorizationNode ref = getNode(res.getQnameRef());
		return ref != null && res.isInstanceOf(ref);
	}

	@Override
	public Set<AuthorizationNode> getChildren(AuthorizationNode node) {
		Set<AuthorizationNode> children = new HashSet<>();
		node.getChildren().stream().forEach(nodeNs -> {
			AuthorizationNode child = index.get(nodeNs);
			if (child != null) {
				children.add(child);
			}
		});
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
		return index.get(qname);
	}
}
