package com.example.JsonArray2CSV;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.commons.lang.StringUtils;
import org.json.CDL;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;
import net.sf.json.util.JSONUtils;

@SpringBootApplication
public class JsonArray2CsvApplication {

	static String test1 = 
		"{"+
			"\"name\": \"bob\","+
			"\"id\": 1,"+
			"\"reviewers\": ["+
				"{"+
					"\"name\": \"sally\","+
					"\"id\": 2"+
				"},"+
				"{"+
					"\"name\": \"steve\","+
					"\"id\": 3"+
				"}"+
			"]"+
		"}";

	public static void main(String[] args) {
		SpringApplication.run(JsonArray2CsvApplication.class, args);
		String output = "";
		System.out.println(test1);
		if(!StringUtils.isBlank(test1) && JSONUtils.mayBeJSON(test1)) {
			Object json = new JSONTokener(test1).nextValue();
			if (json instanceof JSONObject){
				JSONArrayToCSV((JSONObject)json);
				output = json.toString();
			}
			else if (json instanceof JSONArray){
				JSONArrayToCSV((JSONArray)json);
				output = sanitizeJSONArray((JSONArray)json);
			}
		}
		System.out.println(output);
	}



	public static void JSONArrayToCSV(JSONObject jsonInput) {
		for(Object jsonKey : jsonInput.keySet()) {
			
			String keyStr = (String)jsonKey;
			Object value = jsonInput.get(keyStr);
	
			if(value instanceof JSONObject) {
				JsonArray2CsvApplication.JSONArrayToCSV((JSONObject)value);
			}
			else if(value instanceof JSONArray) {
				JsonArray2CsvApplication.JSONArrayToCSV((JSONArray)value);
				String csv = JsonArray2CsvApplication.sanitizeJSONArray((JSONArray)value);
				jsonInput.put(keyStr, csv);
			}
		}
	}
	
	public static void JSONArrayToCSV(JSONArray jsonInput) {
		for (int i = 0; i <jsonInput.length(); i++) {
			Object value = jsonInput.get(i);

			if(value instanceof JSONObject) {
				JsonArray2CsvApplication.JSONArrayToCSV((JSONObject)value);
			}
			else if(value instanceof JSONArray) {
				JsonArray2CsvApplication.JSONArrayToCSV((JSONArray)value);
				String csv = JsonArray2CsvApplication.sanitizeJSONArray((JSONArray)value);
				jsonInput.put(i, csv);
			}
		}
	}

	public static String sanitizeJSONArray(JSONArray jsonArray){
		String csv = CDL.toString(jsonArray);
		//csv = csv.replaceAll("\",\"",",");
		//csv = csv.replaceAll("\n","");
		System.out.println(csv);
		return csv;
	}
}
