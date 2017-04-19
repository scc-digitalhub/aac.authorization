package it.smartcommunitylab.aac.authorization.model;

import java.util.List;

public class NodeInstance {
	private String qname;
	private List<String> params;

	public NodeInstance(String qname, List<String> params) {
		this.qname = qname;
		this.params = params;
	}

	public String getQname() {
		return qname;
	}

	public List<String> getParams() {
		return params;
	}

}
