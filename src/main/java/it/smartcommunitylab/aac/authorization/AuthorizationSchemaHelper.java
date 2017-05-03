package it.smartcommunitylab.aac.authorization;

import java.util.Set;

import org.springframework.stereotype.Service;

import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.Resource;

@Service
public interface AuthorizationSchemaHelper {

	/**
	 * Add a child to given {@link Node}
	 * 
	 * @param parent
	 * @param child
	 * @return
	 * @throws NodeAlreadyExist
	 */
	AuthorizationSchemaHelper addChild(Node parent, Node child) throws NodeAlreadyExist;

	/**
	 * Add a child to root node
	 * 
	 * @param child
	 * @return
	 * @throws NodeAlreadyExist
	 */
	AuthorizationSchemaHelper addRootChild(Node child) throws NodeAlreadyExist;

	boolean isValid(Resource res);

	Set<Node> getChildren(Node node);

	Set<Node> getAllChildren(Node node);

	Node getNode(String qname);

}