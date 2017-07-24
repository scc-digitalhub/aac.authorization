package it.smartcommunitylab.aac.authorization.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;

import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;

@TypeAlias("mainAuthGranted")
class MainAuthorizationGranted extends AuthorizationGranted {

	private List<String> childAuths = new ArrayList<>();

	public MainAuthorizationGranted(Authorization auth) {
		super(auth);
	}

	@PersistenceConstructor
	private MainAuthorizationGranted(String id, AuthorizationUser subject, List<String> actions,
			AuthorizationUser entity, ResourceDocument resource,
			List<String> childAuths) {
		super(id, subject, actions, entity, resource);
		this.childAuths = childAuths;
	}

	public final void setChildAuths(List<String> childAuths) {
		this.childAuths = childAuths;
	}

	public void addChildAuth(String childAuthId) {
		childAuths.add(childAuthId);
	}

	public final List<String> getChildAuths() {
		return childAuths;
	}

	@Override
	public boolean isChildAuth() {
		return false;
	}

}
