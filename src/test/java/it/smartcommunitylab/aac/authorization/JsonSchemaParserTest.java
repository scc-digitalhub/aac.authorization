package it.smartcommunitylab.aac.authorization;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.CharStreams;

import it.smartcommunitylab.aac.authorization.parser.JsonSchemaNode;
import it.smartcommunitylab.aac.authorization.parser.JsonSchemaParser;
import it.smartcommunitylab.aac.authorization.parser.ParseResult;

public class JsonSchemaParserTest {

	@Test
	public void completeCase() throws UnsupportedEncodingException, IOException {
		
		String jsonContent = CharStreams.toString(new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("loadSchema1.json"), "UTF-8"));
		JsonSchemaParser parser = new JsonSchemaParser();
		ParseResult result = parser.parse(jsonContent);
		Assert.assertNotNull(result);
		Assert.assertEquals("cartella-studente", result.getDomain());
		Assert.assertEquals(7, result.getJsonSchemaNodes().size());
		JsonSchemaNode dataCertier = result.getJsonSchemaNodes().get(5);
		Assert.assertEquals("data-certifier", dataCertier.getQname());
		Assert.assertEquals(2, dataCertier.getParams().size());
	}


	@Test(expected = IllegalArgumentException.class)
	public void domain() throws UnsupportedEncodingException, IOException {

		String jsonContent = CharStreams.toString(new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("loadSchema2.json"), "UTF-8"));
		JsonSchemaParser parser = new JsonSchemaParser();
		ParseResult result = parser.parse(jsonContent);
		Assert.assertNotNull(result);
		Assert.assertEquals("cartella-studente", result.getDomain());
		Assert.assertEquals(7, result.getJsonSchemaNodes().size());
		JsonSchemaNode dataCertier = result.getJsonSchemaNodes().get(1);
		Assert.assertEquals(null, dataCertier.getQname());
		Assert.assertEquals(1, dataCertier.getParams().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void domainNotPresent() throws UnsupportedEncodingException, IOException {

		String jsonContent = CharStreams.toString(new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("loadSchema3.json"), "UTF-8"));
		JsonSchemaParser parser = new JsonSchemaParser();
		ParseResult result = parser.parse(jsonContent);
	}

	public void domainInConstructor() throws UnsupportedEncodingException, IOException {

		String jsonContent = CharStreams.toString(new InputStreamReader(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("loadSchema3.json"), "UTF-8"));
		JsonSchemaParser parser = new JsonSchemaParser("cartella-studente");
		ParseResult result = parser.parse(jsonContent);
		Assert.assertEquals("cartella-studente", result.getDomain());
	}

}
