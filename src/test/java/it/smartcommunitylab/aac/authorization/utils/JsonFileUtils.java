package it.smartcommunitylab.aac.authorization.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class JsonFileUtils {

	public static Map<String, Object> jsonToMap(InputStream in) {
		Gson gson = new Gson();
		return gson.fromJson(new JsonReader(new InputStreamReader(in)), new LinkedHashMap<String, Object>().getClass());
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new LinkedHashMap<String, Object>().getClass());
	}

	public static Map<String, Object> jsonResourceFileToMap(String resourceClasspathName) {
		return jsonToMap(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceClasspathName));
	}

}
