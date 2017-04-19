package it.smartcommunitylab.aac.authorization.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Node {

	public static final String ROOT_NODE_ATTRIBUTE = "root";

	private String qname;
	private List<NodeParameter> parameters = new ArrayList<>();

	private Set<Node> parentNs = new HashSet<>();
	private Set<Node> siblingNs = new HashSet<>();

	public Node(String qname) {
		this.qname = qname;
	}

	public Node(String qname, final List<String> parameters) {
		this(qname);
		this.parameters.addAll(parameters.stream().map(n -> {
			return new NodeParameter(qname, n);
		}).collect(Collectors.toList()));

	}

	public Node(String qname, String parameter) {
		this(qname);
		this.parameters.add(new NodeParameter(qname, parameter));
	}

	public Node addNode(Node node) {
		node.parentNs.add(this);
		siblingNs.add(node);
		node.parameters.addAll(parameters);
		return this;
	}

	public boolean isRoot() {
		return ROOT_NODE_ATTRIBUTE.equals(qname);
	}

	public Set<Node> getChildren() {
		return siblingNs;
	}

	public Set<Node> getAllChildren() {
		Set<Node> children = new LinkedHashSet<>();
		children.addAll(siblingNs);
		Queue<Node> queue = new LinkedList<>();
		queue.addAll(siblingNs);
		Node visit = null;
		while ((visit = queue.poll()) != null) {
			children.addAll(visit.getAllChildren());
		}
		return children;
	}

	public String getQname() {
		return qname;
	}

	public List<NodeParameter> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return qname;
	}

}
