package it.smartcommunitylab.aac.authorization.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import it.smartcommunitylab.aac.authorization.model.AuthUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;

@Document(collection = "authGranted")
@TypeAlias("mainAuthGranted")
public class MainAuthGranted extends AuthGranted {

	private List<String> childAuths = new ArrayList<>();

	public MainAuthGranted(Authorization auth) {
		super(auth);
	}

	@PersistenceConstructor
	private MainAuthGranted(String id, String subject, String action, AuthUser entity, ResourceDocument resource,
			List<String> childAuths) {
		super(id, subject, action, entity, resource);
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
