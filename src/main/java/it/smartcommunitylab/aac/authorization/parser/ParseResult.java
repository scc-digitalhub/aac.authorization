package it.smartcommunitylab.aac.authorization.parser;

import java.util.ArrayList;
import java.util.List;

public class ParseResult {
	private String domain;
	private List<JsonSchemaNode> jsonSchemaNodes = new ArrayList<>();

	public void addNode(JsonSchemaNode node) {
		jsonSchemaNodes.add(node);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<JsonSchemaNode> getJsonSchemaNodes() {
		return jsonSchemaNodes;
	}

	public void setJsonSchemaNodes(List<JsonSchemaNode> jsonSchemaNodes) {
		this.jsonSchemaNodes = jsonSchemaNodes;
	}

}
