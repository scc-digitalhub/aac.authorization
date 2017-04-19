package it.smartcommunitylab.aac.authorization.model;

public class InstanceParam {
	private String qname;
	private String name;
	private String value;

	public InstanceParam(String qname, String name, String value) {
		super();
		this.qname = qname;
		this.name = name;
		this.value = value;
	}

	public String getQname() {
		return qname;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
