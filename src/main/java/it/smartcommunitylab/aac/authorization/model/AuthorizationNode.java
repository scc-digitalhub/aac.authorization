package it.smartcommunitylab.aac.authorization.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authorizationSchema")
public class AuthorizationNode {

	public static final FQname ROOT_NODE_ATTRIBUTE = new FQname("*", "root");

	private String id;

	private FQname fqname;

	private List<AuthorizationNodeParam> parameters = new ArrayList<>();

	private Set<FQname> parentNs = new HashSet<>();
	private Set<FQname> childrenNs = new HashSet<>();

	public AuthorizationNode(FQname fqname) {
		this.fqname = fqname;
	}


	@PersistenceConstructor
	private AuthorizationNode(FQname fqname, final List<AuthorizationNodeParam> parameters) {
		this(fqname);
		this.parameters.addAll(parameters);
	}

	public AuthorizationNode(FQname fqname, String parameter) {
		this(fqname);
		this.parameters.add(new AuthorizationNodeParam(fqname.getQname(), parameter));
	}

	public AuthorizationNode addChild(AuthorizationNode node) {
		childrenNs.add(node.getFqname());
		return this;
	}

	public AuthorizationNode addParent(AuthorizationNode node) {
		parentNs.add(node.getFqname());
		parameters.addAll(node.parameters);
		return this;
	}

	public AuthorizationNode addParameter(String param) {
		parameters.add(new AuthorizationNodeParam(fqname.getQname(), param));
		return this;
	}

	public boolean isRoot() {
		return ROOT_NODE_ATTRIBUTE.equals(fqname);
	}

	public final Set<FQname> getChildren() {
		return childrenNs;
	}


	public final FQname getFqname() {
		return fqname;
	}

	public final List<AuthorizationNodeParam> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return fqname.toString();
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
		AuthorizationNode rhs = (AuthorizationNode) obj;
		return new EqualsBuilder().append(fqname, rhs.fqname).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(77, 3).append(fqname).hashCode();
	}


}
