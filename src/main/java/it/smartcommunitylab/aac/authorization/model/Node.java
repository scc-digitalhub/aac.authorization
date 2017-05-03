package it.smartcommunitylab.aac.authorization.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

public class Node {

	public static final String ROOT_NODE_ATTRIBUTE = "root";

	@Id
	private String qname;

	private List<NodeParameter> parameters = new ArrayList<>();

	private Set<String> parentNs = new HashSet<>();
	private Set<String> childrenNs = new HashSet<>();

	public Node(String qname) {
		this.qname = qname;
	}



	@PersistenceConstructor
	private Node(String qname, final List<NodeParameter> parameters) {
		this(qname);
		this.parameters.addAll(parameters);
	}

	public Node(String qname, String parameter) {
		this(qname);
		this.parameters.add(new NodeParameter(qname, parameter));
	}

	public Node addChild(Node node) {
		childrenNs.add(node.getQname());
		return this;
	}

	public Node addParent(Node node) {
		parentNs.add(node.getQname());
		parameters.addAll(node.parameters);
		return this;
	}

	public Node addParameter(String param) {
		parameters.add(new NodeParameter(qname, param));
		return this;
	}

	public boolean isRoot() {
		return ROOT_NODE_ATTRIBUTE.equals(qname);
	}

	public Set<String> getChildren() {
		return childrenNs;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Node rhs = (Node) obj;
		return new EqualsBuilder().append(qname, rhs.qname).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(77, 3).append(qname).hashCode();
	}

}
