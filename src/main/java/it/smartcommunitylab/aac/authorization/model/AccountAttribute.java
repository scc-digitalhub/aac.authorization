package it.smartcommunitylab.aac.authorization.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AccountAttribute {
	private String accountName;
	private String attributeName;
	private String attributeValue;

	public AccountAttribute(String accountName, String attributeName, String attributeValue) {
		super();
		this.accountName = accountName;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
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
		AccountAttribute rhs = (AccountAttribute) obj;
		return new EqualsBuilder().append(accountName, rhs.accountName).append(attributeName, rhs.attributeName)
				.append(attributeValue, rhs.attributeValue).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(5, 3).append(accountName).append(attributeName).append(attributeValue).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("accountName", accountName)
				.append("attributeName", attributeName).append("attributeValue", attributeValue).build();
	}

}
