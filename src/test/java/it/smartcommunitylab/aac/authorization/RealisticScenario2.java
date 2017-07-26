package it.smartcommunitylab.aac.authorization;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import it.smartcommunitylab.aac.authorization.config.MongoConfig;
import it.smartcommunitylab.aac.authorization.model.AccountAttribute;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;
import it.smartcommunitylab.aac.authorization.model.FQname;
import it.smartcommunitylab.aac.authorization.model.RequestedAuthorization;
import it.smartcommunitylab.aac.authorization.model.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfig.class,
		RealisticScenarioTestConfig.class }, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = { "mongo.dbname=aac-authorization-db-test" })
public class RealisticScenario2 {

	@Autowired
	private AuthorizationSchemaHelper schemaHelper;

	@Autowired
	private AuthorizationStorage storage;

	@Autowired
	private AuthorizationHelper authHelper;

	@Autowired
	private MongoTemplate mongo;

	private static final String DOMAIN = "cartella";

	@Before
	public void clean() {
		mongo.dropCollection("authorizationGranted");
	}

	@Test
	public void scenario() throws Exception {
		String jsonSchemaContent = Files
				.asCharSource(new File(Thread.currentThread().getContextClassLoader()
						.getResource("authorizationSamples/auth_schema.json").toURI()),
						Charsets.UTF_8)
				.read();
		schemaHelper.loadJson(jsonSchemaContent);
		
		AccountAttribute account1 = new AccountAttribute("google+", "fiscal_code", "XXX");
		AuthorizationUser sub = new AuthorizationUser(account1, "user");
		
		AccountAttribute account2 = new AccountAttribute("google+", "fiscal_code", "YYY");
		AuthorizationUser entity = new AuthorizationUser(account2, "user");
		
		Resource resource= new Resource(new FQname("cartellastudente", "student"));
		resource.addNodeValue(new AuthorizationNodeValue("student","studentId", "84f01dc1-694d-40eb-9296-01ca5014ef5d"));
		Authorization auth = new Authorization(sub, "ALL", resource, entity);
		authHelper.insert(auth);

		Resource resource1 = new Resource(new FQname("cartellastudente", "student-data-attr"));
		resource1.addNodeValue(new AuthorizationNodeValue("student-data", "dataType", "Authorization"));
		resource1.addNodeValue(
				new AuthorizationNodeValue("student", "studentId", "84f01dc1-694d-40eb-9296-01ca5014ef5d"));

		RequestedAuthorization requested = new RequestedAuthorization("ALL", resource1, entity);

		Assert.assertEquals(true, authHelper.validate(requested));

		Resource resource2 = new Resource(new FQname("cartellastudente", "student-data-attr"));
		resource2.addNodeValue(new AuthorizationNodeValue("student-data", "dataType", "Registration"));
		resource2.addNodeValue(new AuthorizationNodeValue("student-data-attr", "registrationId", "111111"));


		requested = new RequestedAuthorization("ALL", resource2, entity);
		Assert.assertEquals(false, authHelper.validate(requested));



	}

}
