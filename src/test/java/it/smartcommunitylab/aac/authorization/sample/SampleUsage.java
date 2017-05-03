package it.smartcommunitylab.aac.authorization.sample;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.smartcommunitylab.aac.authorization.AuthHelper;
import it.smartcommunitylab.aac.authorization.AuthSchemaHelper;
import it.smartcommunitylab.aac.authorization.config.Config;
import it.smartcommunitylab.aac.authorization.config.MongoConfig;
import it.smartcommunitylab.aac.authorization.model.AuthUser;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.Node;
import it.smartcommunitylab.aac.authorization.model.NodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.NodeValue;
import it.smartcommunitylab.aac.authorization.model.Resource;

public class SampleUsage {

	private final static Logger logger = LoggerFactory.getLogger(SampleUsage.class);

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class);
		ctx.register(MongoConfig.class);
		ctx.refresh();

		AuthSchemaHelper schemaHelper = ctx.getBean(AuthSchemaHelper.class);

		Node nodeA = new Node("A");
		nodeA.addParameter("a");
		try {
			schemaHelper.addRootChild(nodeA);
		} catch (NodeAlreadyExist e) {
			// silence exception
		}
		AuthHelper authHelper = ctx.getBean(AuthHelper.class);
		ctx.close();
		Resource res = new Resource("A", Arrays.asList(new NodeValue("A", "a", "a_Value")));
		Authorization auth = new Authorization(new AuthUser("sub", "type"), "act", res, new AuthUser("id", "type"));

		authHelper.insert(auth);
		logger.info("simple usage end");
	}

}
