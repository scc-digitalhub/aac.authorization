package it.smartcommunitylab.aac.authorization;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import it.smartcommunitylab.aac.authorization.config.MongoConfig;
import it.smartcommunitylab.aac.authorization.model.AccountAttribute;
import it.smartcommunitylab.aac.authorization.model.Authorization;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNode;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeAlreadyExist;
import it.smartcommunitylab.aac.authorization.model.AuthorizationNodeValue;
import it.smartcommunitylab.aac.authorization.model.AuthorizationUser;
import it.smartcommunitylab.aac.authorization.model.FQname;
import it.smartcommunitylab.aac.authorization.model.Resource;
import it.smartcommunitylab.aac.authorization.mongo.MongoAuthorizationStorage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MongoConfig.class,
		RealisticScenarioTestConfig.class }, loader = AnnotationConfigContextLoader.class)
@TestPropertySource(properties = { "mongo.dbname=aac-authorization-db-test" })
public class RealisticScenario {

	@Autowired
	private AuthorizationSchemaHelper schemaHelper;

	@Autowired
	private AuthorizationStorage storage;

	@Autowired
	private AuthorizationHelper authHelper;

	@Autowired
	private MongoTemplate mongo;

	private static final String DOMAIN = "cartella";

	@PostConstruct
	public void setup() {
		try {
			AuthorizationNode student = new AuthorizationNode(new FQname(DOMAIN, "student"));
			student.addParameter("studentId");
			AuthorizationNode institute = new AuthorizationNode(new FQname(DOMAIN, "institute"));
			institute.addParameter("instituteId");
			AuthorizationNode certifier = new AuthorizationNode(new FQname(DOMAIN, "certifier"));
			institute.addParameter("certifierId");

			schemaHelper.addRootChild(student);
			schemaHelper.addRootChild(institute);
			schemaHelper.addRootChild(certifier);

			AuthorizationNode data = new AuthorizationNode(new FQname(DOMAIN, "data"));
			data.addParameter("data");
			schemaHelper.addChild(student.getFqname(), data);

			AuthorizationNode dataDetail = new AuthorizationNode(new FQname(DOMAIN, "data-detail"));
			dataDetail.addParameter("type");
			dataDetail.addParameter("id");

			schemaHelper.addChild(data.getFqname(), dataDetail);

			AuthorizationNode dataInstitute = new AuthorizationNode(new FQname(DOMAIN, "data-institute"));
			dataInstitute.addParameter("data");
			schemaHelper.addChild(institute.getFqname(), dataInstitute);

			AuthorizationNode studentCertifier = new AuthorizationNode(new FQname(DOMAIN, "student-certifier"));
			studentCertifier.addParameter("student");
			AuthorizationNode dataCertifier = new AuthorizationNode(new FQname(DOMAIN, "data-certifier"));
			dataCertifier.addParameter("data");

			schemaHelper.addChild(certifier.getFqname(), dataCertifier);
			schemaHelper.addChild(certifier.getFqname(), studentCertifier);

		} catch (AuthorizationNodeAlreadyExist e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Before
	public void clean() {
		mongo.dropCollection("authorizationGranted");
	}

	@Test
	public void scenarioFirstLevelAuthorization() throws NotValidResourceException {
		AuthorizationUser subject = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		AuthorizationUser entity = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		Resource resStudent1 = new Resource(new FQname(DOMAIN, "student"));
		resStudent1.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		Authorization authorization = new Authorization(subject, "action", resStudent1, entity);
		authHelper.insert(authorization);

		Resource resStudent2 = new Resource(new FQname(DOMAIN, "student"));
		resStudent2.addNodeValue(new AuthorizationNodeValue("student", "studentId", "he-man-id"));
		Authorization authorization1 = new Authorization(subject, "action", resStudent2, entity);

		Assert.assertFalse(authHelper.validate(authorization1));

		Assert.assertTrue(authHelper.validate(authorization));

		// try validate a second level authorization

		Resource resStudent3 = new Resource(new FQname(DOMAIN, "data"));
		resStudent3.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		resStudent3.addNodeValue(new AuthorizationNodeValue("data", "data", "Experience"));
		Authorization authorization2 = new Authorization(subject, "action", resStudent3, entity);

		Assert.assertTrue(authHelper.validate(authorization2));

		Resource resStudent4 = new Resource(new FQname(DOMAIN, "data"));
		resStudent4.addNodeValue(new AuthorizationNodeValue("student", "studentId", "he-man-id"));
		resStudent4.addNodeValue(new AuthorizationNodeValue("data", "data", "Experience"));
		Authorization authorization3 = new Authorization(subject, "action", resStudent4, entity);

		Assert.assertFalse(authHelper.validate(authorization3));
	}

	@Test
	public void scenarioSecondLevelAuthorization() throws NotValidResourceException {
		AuthorizationUser subject = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		AuthorizationUser entity = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		Resource resStudent1 = new Resource(new FQname(DOMAIN, "data"));
		resStudent1.addNodeValue(new AuthorizationNodeValue("data", "data", "Experience"));
		resStudent1.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		Authorization authorization = new Authorization(subject, "action", resStudent1, entity);
		authHelper.insert(authorization);

		Resource resStudent2 = new Resource(new FQname(DOMAIN, "student"));
		resStudent2.addNodeValue(new AuthorizationNodeValue("student", "studentId", "he-man"));
		Authorization authorization1 = new Authorization(subject, "action", resStudent2, entity);

		Assert.assertFalse(authHelper.validate(authorization1));
		Assert.assertTrue(authHelper.validate(authorization));

	}

	@Test
	public void scenarioThirdLevelAuthorization() throws NotValidResourceException {
		AuthorizationUser subject = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		AuthorizationUser entity = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		Resource resStudent1 = new Resource(new FQname(DOMAIN, "data"));
		resStudent1.addNodeValue(new AuthorizationNodeValue("data", "data", "Experience"));
		resStudent1.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		Authorization authorization = new Authorization(subject, "action", resStudent1, entity);
		authHelper.insert(authorization);

		AuthorizationUser subject1 = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		AuthorizationUser entity1 = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		Resource resStudent2 = new Resource(new FQname(DOMAIN, "data-detail"));
		resStudent2.addNodeValue(new AuthorizationNodeValue("data", "data", "Experience"));
		resStudent2.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		resStudent2.addNodeValue(new AuthorizationNodeValue("data-detail", "type", "expType"));
		resStudent2.addNodeValue(new AuthorizationNodeValue("data-detail", "id", "expId"));
		Authorization authorization1 = new Authorization(subject1, "action", resStudent2, entity1);
		Assert.assertTrue(authHelper.validate(authorization1));

		Resource resStudent3 = new Resource(new FQname(DOMAIN, "data"));
		resStudent3.addNodeValue(new AuthorizationNodeValue("data", "data", "Registration"));
		resStudent3.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		Authorization authorization2 = new Authorization(subject, "action", resStudent3, entity);
		Assert.assertFalse(authHelper.validate(authorization2));

		Resource resStudent4 = new Resource(new FQname(DOMAIN, "data"));
		resStudent3.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		Authorization authorization3 = new Authorization(subject, "action", resStudent4, entity);
		Assert.assertFalse(authHelper.validate(authorization3));
	}


	@Test
	public void incompleteResource() throws NotValidResourceException {
		AuthorizationUser subject = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		AuthorizationUser entity = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		Resource resStudent1 = new Resource(new FQname(DOMAIN, "student"));
		resStudent1.addNodeValue(new AuthorizationNodeValue("student", "studentId", "my-id"));
		Authorization authorization = new Authorization(subject, "action", resStudent1, entity);
		authHelper.insert(authorization);

		Resource resStudent3 = new Resource(new FQname(DOMAIN, "data"));
		resStudent3.addNodeValue(new AuthorizationNodeValue("data", "data", "Experience"));
		Authorization authorization2 = new Authorization(subject, "action", resStudent3, entity);

		Assert.assertFalse(authHelper.validate(authorization2));
	}

	@Test
	public void authorizeAllValue() throws NotValidResourceException {
		AuthorizationUser subject = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		AuthorizationUser entity = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		Resource resStudent1 = new Resource(new FQname(DOMAIN, "student"),
				Arrays.asList(new AuthorizationNodeValue("student", "studentId", AuthorizationNodeValue.ALL_VALUE)));
		Authorization authorization = new Authorization(subject, "action", resStudent1, entity);
		authHelper.insert(authorization);

		Resource resStudent2 = new Resource(new FQname(DOMAIN, "student"),
				Arrays.asList(new AuthorizationNodeValue("student", "studentId", "eddie-brock")));
		Authorization authorization1 = new Authorization(subject, "action", resStudent2, entity);
		Assert.assertTrue(authHelper.validate(authorization1));
	}

	@Test
	public void validateForAnotherEntity() throws NotValidResourceException {
		AuthorizationUser subject = new AuthorizationUser(new AccountAttribute("account", "name", "my-id"), "user");
		AuthorizationUser entity = new AuthorizationUser(new AccountAttribute("account", "surname", "my-id"), "user");
		Resource resStudent1 = new Resource(new FQname(DOMAIN, "student"),
				Arrays.asList(new AuthorizationNodeValue("student", "studentId", AuthorizationNodeValue.ALL_VALUE)));
		Authorization authorization = new Authorization(subject, "action", resStudent1, entity);
		authHelper.insert(authorization);

		AuthorizationUser entity1 = new AuthorizationUser(new AccountAttribute("google+", "surname", "my-id"), "user");
		Resource resStudent2 = new Resource(new FQname(DOMAIN, "student"),
				Arrays.asList(new AuthorizationNodeValue("student", "studentId", "eddie-brock")));
		Authorization authorization1 = new Authorization(subject, "action", resStudent2, entity1);
		Assert.assertFalse(authHelper.validate(authorization1));
	}

}


class RealisticScenarioTestConfig {

	@Bean
	public AuthorizationStorage authStorage() {
		return new MongoAuthorizationStorage();
	}

	@Bean
	public AuthorizationSchemaHelper authSchema() {
		return new MongoAuthorizationSchemaHelper();
	}

	@Bean
	public AuthorizationHelper authHelper() {
		return new AuthorizationHelperImpl();
	}
}
