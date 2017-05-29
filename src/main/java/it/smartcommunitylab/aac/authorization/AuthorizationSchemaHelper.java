package it.smartcommunitylab.aac.authorization;

import java.util.Set;

import org.springframework.stereotype.Service;

import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.FQname;
import it.smartcommunitylab.aac.authorization.model.Resource;

@Service
public interface AuthorizationSchemaHelper {

	/**
	 * Add a child to given {@link AuthorizationNode}
	 * 
	 * @param parent
	 * @param child
	 * @return
	 * @throws AuthorizationNodeAlreadyExist
	 */
	AuthorizationSchemaHelper addChild(AuthorizationNode parent, AuthorizationNode child) throws AuthorizationNodeAlreadyExist;

	AuthorizationSchemaHelper addChild(FQname parentQname, AuthorizationNode child)
			throws AuthorizationNodeAlreadyExist;

	/**
	 * Add a child to root node
	 * 
	 * @param child
	 * @return
	 * @throws AuthorizationNodeAlreadyExist
	 */
	AuthorizationSchemaHelper addRootChild(AuthorizationNode child) throws AuthorizationNodeAlreadyExist;

	boolean isValid(Resource res);

	Set<AuthorizationNode> getChildren(AuthorizationNode node);

	Set<AuthorizationNode> getAllChildren(AuthorizationNode node);

	Set<AuthorizationNode> getChildren(FQname qName);

	Set<AuthorizationNode> getAllChildren(FQname qname);

	AuthorizationNode getNode(FQname qname);

	void loadJson(String jsonString);

}