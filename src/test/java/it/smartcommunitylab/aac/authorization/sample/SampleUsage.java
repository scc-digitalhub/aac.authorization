package it.smartcommunitylab.aac.authorization.sample;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.smartcommunitylab.aac.authorization.AuthorizationHelper;
import it.smartcommunitylab.aac.authorization.AuthorizationHelperImpl;
import it.smartcommunitylab.aac.authorization.AuthorizationSchemaHelper;
import it.smartcommunitylab.aac.authorization.NotValidResourceException;
import it.smartcommunitylab.aac.authorization.config.Config;
import it.smartcommunitylab.aac.authorization.config.MongoConfig;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;
import it.smartcommunitylab.aac.authorization.model.FQname;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class SampleUsage {

	private final static Logger logger = LoggerFactory.getLogger(SampleUsage.class);

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class);
		ctx.register(MongoConfig.class);
		ctx.refresh();

		AuthorizationSchemaHelper schemaHelper = ctx.getBean(AuthorizationSchemaHelper.class);

		AuthorizationNode nodeA = new AuthorizationNode(new FQname("domain", "A"));
		nodeA.addParameter("a");
		try {
			schemaHelper.addRootChild(nodeA);
		} catch (AuthorizationNodeAlreadyExist e) {
			logger.error("authorization node already exists", e);
		}
		AuthorizationHelper authHelper = ctx.getBean(AuthorizationHelperImpl.class);
		ctx.close();
		Resource res = new Resource(new FQname("domain", "A"),
				Arrays.asList(new AuthorizationNodeValue("A", "a", "a_Value")));
		Authorization auth = new Authorization(new AuthorizationUser("sub", "type"), "act", res, new AuthorizationUser("id", "type"));

		try {
			authHelper.insert(auth);
		} catch (NotValidResourceException e) {
			logger.error("not valid resource", e);
		}
		logger.info("simple usage end");
	}

}
