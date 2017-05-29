package it.smartcommunitylab.aac.authorization.parser;

import java.util.List;

public class JsonSchemaNode {
	private String qname;
	private List<String> params;
	private List<String> parents;

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public List<String> getParents() {
		return parents;
	}

	public void setParents(List<String> parents) {
		this.parents = parents;
	}

}
