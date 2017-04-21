package it.smartcommunitylab.aac.authorization.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.mongodb.core.mapping.Field;

import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;

class ResourceDocument {
	private String qname;

	@Field("attrs")
	private Map<String, String> attributes = new HashMap<>();

	private static final String NAMESPACE_SEPARATOR = "#";

	public ResourceDocument(String qname) {
		this.qname = qname;
	}

	public ResourceDocument(Resource resource) {
		if (resource != null) {
			qname = resource.getQnameRef();
			addAllAttributes(resource.getValues());
		}
	}

	interface AttributeDocument {
		String getName();

		String getValue();
	}

	public void addAttribute(String name, String value) {
		attributes.put(name, value);
	}

	public void addAllAttributes(Collection<NodeValue> attributes) {
		for (NodeValue attr : attributes) {
			addAttribute(getAttrName(attr.getQname(), attr.getName()), attr.getValue());
		}
	}

	private String getAttrName(String qname, String name) {
		if (qname == null || name == null) {
			throw new NullPointerException("attribute name parts cannot be null");
		}
		return String.format("%s%s%s", qname, NAMESPACE_SEPARATOR, name);
	}

	private List<NodeValue> convertAttributes() {
		List<NodeValue> values = new ArrayList<>();
		for (String key : attributes.keySet()) {
			String value = attributes.get(key);
			values.add(convertAttribute(key, value));
		}

		return values;
	}

	private NodeValue convertAttribute(String attrName, String value) {
		if (attrName == null) {
			throw new NullPointerException("attrName cannot be null");
		}
		String[] attrParts = attrName.split(NAMESPACE_SEPARATOR);
		return new NodeValue(attrParts[0], attrParts[1], value);

	}

	public final String getQname() {
		return qname;
	}

	public final Collection<AttributeDocument> getAttributes() {
		List<AttributeDocument> result = new ArrayList<>();
		for (Entry<String, String> entry : attributes.entrySet()) {
			result.add(new AttributeDocumentImpl(entry));
		}
		return result;
	}

	public Resource toResource() {
		return new Resource(qname, convertAttributes());

	}

	private static class AttributeDocumentImpl implements AttributeDocument {

		private String name;
		private String value;

		public AttributeDocumentImpl(Entry<String, String> entry) {
			name = entry.getKey();
			value = entry.getValue();
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			return value;
		}

	}
}
