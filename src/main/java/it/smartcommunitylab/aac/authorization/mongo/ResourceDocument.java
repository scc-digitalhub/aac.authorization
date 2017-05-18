package it.smartcommunitylab.aac.authorization.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.FQname;
import it.smartcommunitylab.aac.authorization.model.Resource;

class ResourceDocument {
	private FQname fqname;

	@Field("attrs")
	private Map<String, String> attributes = new HashMap<>();

	private static final String NAMESPACE_SEPARATOR = "#";

	public ResourceDocument(FQname fqname) {
		this.fqname = fqname;
	}

	public ResourceDocument(Resource resource) {
		if (resource != null) {
			fqname = resource.getFqnameRef();
			addAllAttributes(resource.getValues());
		}
	}

	@PersistenceConstructor
	private ResourceDocument(FQname fqname, Map<String, String> attributes) {
		this.fqname = fqname;
		this.attributes = attributes;
	}

	interface AttributeDocument {
		String getName();

		String getValue();
	}

	public void addAttribute(String name, String value) {
		attributes.put(name, value);
	}

	public void addAllAttributes(Collection<AuthorizationNodeValue> attributes) {
		for (AuthorizationNodeValue attr : attributes) {
			addAttribute(getAttrName(attr.getDefinition().getFQname(), attr.getDefinition().getName()),
					attr.getValue());
		}
	}

	private String getAttrName(FQname fqname, String name) {
		if (fqname == null || name == null) {
			throw new NullPointerException("attribute name parts cannot be null");
		}
		return String.format("%s%s%s", fqname.getQname(), NAMESPACE_SEPARATOR, name);
	}

	private List<AuthorizationNodeValue> convertAttributes() {
		List<AuthorizationNodeValue> values = new ArrayList<>();
		for (String key : attributes.keySet()) {
			String value = attributes.get(key);
			values.add(convertAttribute(key, value));
		}

		return values;
	}

	private AuthorizationNodeValue convertAttribute(String attrName, String value) {
		if (attrName == null) {
			throw new NullPointerException("attrName cannot be null");
		}
		String[] attrParts = attrName.split(NAMESPACE_SEPARATOR);
		return new AuthorizationNodeValue(new FQname(fqname.getDomain(), attrParts[0]), attrParts[1], value);

	}

	public final FQname getFqname() {
		return fqname;
	}

	public final Collection<AttributeDocument> getAttributes() {
		List<AttributeDocument> result = new ArrayList<>();
		for (Entry<String, String> entry : attributes.entrySet()) {
			result.add(new AttributeDocumentImpl(entry));
		}
		return result;
	}

	public Resource toResource() {
		return new Resource(fqname, convertAttributes());

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
