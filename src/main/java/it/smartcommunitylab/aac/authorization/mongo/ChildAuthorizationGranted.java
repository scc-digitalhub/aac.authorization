package it.smartcommunitylab.aac.authorization.mongo;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;

import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;

@TypeAlias("childAuthGranted")
class ChildAuthorizationGranted extends AuthorizationGranted {

	private String mainAuthGrantedId;

	public ChildAuthorizationGranted(Authorization auth) {
		super(auth);
	}

	private ChildAuthorizationGranted(String id, AuthorizationUser subject, List<String> actions,
			AuthorizationUser entity, ResourceDocument resource,
			String mainAuthGrantedId) {
		super(id, subject, actions, entity, resource);
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
