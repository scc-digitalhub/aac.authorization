package it.smartcommunitylab.aac.authorization.mongo;

import org.springframework.data.annotation.TypeAlias;

import it.smartcommunitylab.aac.authorization.model.AuthUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;

@TypeAlias("childAuthGranted")
public class ChildAuthGranted extends AuthGranted {

	private String mainAuthGrantedId;

	public ChildAuthGranted(Authorization auth) {
		super(auth);
	}

	private ChildAuthGranted(String id, String subject, String action, AuthUser entity, ResourceDocument resource,
			String mainAuthGrantedId) {
		super(id, subject, action, entity, resource);
		this.mainAuthGrantedId = mainAuthGrantedId;
	}

	public void setMainAuthGranted(String mainAuthGrantedId) {
		this.mainAuthGrantedId = mainAuthGrantedId;
	}

	@Override
	public boolean isChildAuth() {
		return true;
	}

}
