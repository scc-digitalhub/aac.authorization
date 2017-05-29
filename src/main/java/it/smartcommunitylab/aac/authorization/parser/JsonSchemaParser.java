package it.smartcommunitylab.aac.authorization.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class JsonSchemaParser {
	private String domainReference;

	public interface Domain {
		public String getName();

		public List<NodeInternal> getNodes();
	}

	public interface Node {
		public String getQname();

		public List<String> getParams();

		public List<String> getChildrenQnames();
	}

	private static class NodeInternal implements Node {

		private String qname;
		private List<String> params = new ArrayList<>();
		private List<String> childrenQnames = new ArrayList<>();

		public NodeInternal() {
		}

		@JsonCreator
		public NodeInternal(@JsonProperty(value = "qname", required = true) String qname,
				@JsonProperty(value = "params") List<String> params,
				@JsonProperty(value = "childrenQnames") List<String> childrenQnames) {
			this.qname = qname;
			this.params = params == null ? new ArrayList<>() : params;
			this.childrenQnames = childrenQnames == null ? new ArrayList<>() : childrenQnames;
		}

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


		public void setChildrenQnames(List<String> childrenQnames) {
			this.childrenQnames = childrenQnames;
		}

		@Override
		public List<String> getChildrenQnames() {
			return childrenQnames;
		}

	}

	private static class DomainInternal implements Domain {
		private String name;
		private List<NodeInternal> nodes;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<NodeInternal> getNodes() {
			return nodes;
		}

		public void setNodes(List<NodeInternal> nodes) {
			this.nodes = nodes;
		}

	}

	public JsonSchemaParser() {

	}

	public JsonSchemaParser(String domain) {
		domainReference = domain;
	}

	public ParseResult parse(String jsonSchema) {
		ObjectMapper mapper = new ObjectMapper();
		ParseResult result = new ParseResult();
		Domain structure;
		try {
			structure = mapper.readValue(jsonSchema, DomainInternal.class);
			result.setDomain(domainReference != null ? domainReference : structure.getName());

			if (StringUtils.isBlank(result.getDomain())) {
				throw new IllegalArgumentException("domain name is required");
			}

			MultiValuedMap<String, String> parentsNodes = new HashSetValuedHashMap<>();
			List<JsonSchemaNode> jsonNodes = new ArrayList<>();
			structure.getNodes().stream().forEach(n -> {
				JsonSchemaNode jsonNode = new JsonSchemaNode();
				jsonNode.setQname(n.qname);
				jsonNode.setParams(n.params);
				n.childrenQnames.stream().forEach(childQname -> parentsNodes.put(childQname, n.qname));
				jsonNodes.add(jsonNode);
			});

			jsonNodes.stream().forEach(jsonNode -> {
				jsonNode.setParents(new ArrayList<>(parentsNodes.get(jsonNode.getQname())));
			});
			result.setJsonSchemaNodes(jsonNodes);
		} catch (MismatchedInputException e) {
			throw new IllegalArgumentException("qname field is required");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
