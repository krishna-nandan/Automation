package com.mPulse.utility;

import static io.restassured.RestAssured.expect;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.XML;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class EventUploadApi {

	public static synchronized HashMap<String, String> eventUploadAPI_JSON_V2(Map<String, String> eventDataInMap,
			String eventName, String listId, String scheduledOn, String timeZone, String evaluationScope,
			String verbosity, int expectedStatusCode) {
		RestAssured.baseURI = ConfigReader.getConfig("eventUploadApiUrl");
		eventDataInMap.put("scheduled_on", scheduledOn);
		eventDataInMap.put("timezone", timeZone);
		eventDataInMap.put("evaluation_scope", evaluationScope);

		JSONObject xy = new JSONObject(eventDataInMap);

		String jsonBody = "{ \"body\": { \"events\": { \"name\": \"" + eventName + "\", \"event\": " + xy.toString()
				+ " }, \"listid\": \"" + listId + "\" } }";
		if (verbosity.equalsIgnoreCase("full")) {
			Response r = expect().given().contentType("application/json")
					.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
					.header("X-Ms-API-Version", "2.0")
					.header("X-Ms-Format", "json").header("X-Ms-Source", "api").header("X-Ms-Verbosity", "full")
					.body(jsonBody).log().all().when().post();
			String body = r.getBody().asString();
			System.out.println(body);
			if (r.statusCode() == 400) {
				Assert.assertEquals(r.statusCode(), expectedStatusCode);
				Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
				JsonPath jsonPathEvaluator = r.jsonPath();
				HashMap<String, String> resultset = new HashMap<String, String>();
				resultset.put("processed_event_instances",
						jsonPathEvaluator.getString("body.results.processed_event_instances"));
				resultset.put("scheduled_messages", jsonPathEvaluator.getString("body.results.scheduled_messages"));
				resultset.put("errors", jsonPathEvaluator.getString("body.results.errors"));
				resultset.put("statusCode", String.valueOf(r.statusCode()));
				if (jsonPathEvaluator.getInt("body.results.processed_event_instances") == 0) {
					resultset.put("error_message",
							jsonPathEvaluator.getString("body.results.details.events_error.error_message"));
				} else {
					resultset.put("error_message",
							jsonPathEvaluator.getString("body.results.details.events.event.errors.error.error_message"));
					resultset.put("memberid", jsonPathEvaluator.getString("body.results.details.events.event.memberid"));
					resultset.put("event_definition_name",
							jsonPathEvaluator.getString("body.results.details.events.event_definition_name"));
					if (eventDataInMap.containsKey("mobilephone")) {
						resultset.put("mobilephone", jsonPathEvaluator
								.getString("body.results.details.events.event.event_instance_body.body.event.mobilephone"));
					}
					if (eventDataInMap.containsKey("email")) {
						resultset.put("email", jsonPathEvaluator
								.getString("body.results.details.events.event.event_instance_body.body.event.email"));
					}
					if (eventDataInMap.containsKey("correlationid")) {
						resultset.put("email", jsonPathEvaluator
								.getString("body.results.details.events.event.event_instance_body.body.event.correlationid"));
					}
				}
				return resultset;
			} else {
				Assert.assertEquals(r.statusCode(), expectedStatusCode);
				Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
				JsonPath jsonPathEvaluator = r.jsonPath();
				Assert.assertEquals(jsonPathEvaluator.get("body.results.details.listid").toString(),
						String.valueOf(listId));
				HashMap<String, String> resultset = new HashMap<String, String>();
				resultset.put("processed_event_instances",
						jsonPathEvaluator.getString("body.results.processed_event_instances"));
				resultset.put("scheduled_messages", jsonPathEvaluator.getString("body.results.scheduled_messages"));
				resultset.put("errors", jsonPathEvaluator.getString("body.results.errors"));
				resultset.put("event_definition_name",
						jsonPathEvaluator.getString("body.results.details.events.event_definition_name"));
				resultset.put("memberid", jsonPathEvaluator.getString("body.results.details.events.event.memberid"));
				if (jsonPathEvaluator.getString(
						"body.results.details.events.event.scheduled_messages") != null) {
					resultset.put("message_id", jsonPathEvaluator.getString(
							"body.results.details.events.event.scheduled_messages.scheduled_message.message_id"));
				}
				
				if (jsonPathEvaluator.getString(
						"body.results.details.events.event.errors") != null) {
					resultset.put("error_message", jsonPathEvaluator.getString(
							"body.results.details.events.event.errors.error.error_message"));
				}
				
				resultset.put("scheduled_on", jsonPathEvaluator
						.getString("body.results.details.events.event.event_instance_body.body.event.scheduled_on"));
				resultset.put("evaluation_scope", jsonPathEvaluator.getString(
						"body.results.details.events.event.event_instance_body.body.event.evaluation_scope"));
				resultset.put("statusCode", String.valueOf(r.statusCode()));
				if (eventDataInMap.containsKey("mobilephone")) {
					resultset.put("mobilephone", jsonPathEvaluator
							.getString("body.results.details.events.event.event_instance_body.body.event.mobilephone"));
				}
				if (eventDataInMap.containsKey("email")) {
					resultset.put("email", jsonPathEvaluator
							.getString("body.results.details.events.event.event_instance_body.body.event.email"));
				}
				if (eventDataInMap.containsKey("correlationid")) {
					resultset.put("email", jsonPathEvaluator
							.getString("body.results.details.events.event.event_instance_body.body.event.correlationid"));
				}
				return resultset;
			}
		} else {
			Response r = expect().given().contentType("application/json")
					.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
					.header("X-Ms-API-Version", "2.0")
					.header("X-Ms-Format", "json").header("X-Ms-Source", "api").body(jsonBody).log().all().when()
					.post();
			String body = r.getBody().asString();
			System.out.println(body);
			if (r.statusCode() == 400) {
				Assert.assertEquals(r.statusCode(), expectedStatusCode);
				Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
				JsonPath jsonPathEvaluator = r.jsonPath();
				HashMap<String, String> resultset = new HashMap<String, String>();
				resultset.put("processed_event_instances",
						jsonPathEvaluator.getString("body.results.processed_event_instances"));
				resultset.put("scheduled_messages", jsonPathEvaluator.getString("body.results.scheduled_messages"));
				resultset.put("errors", jsonPathEvaluator.getString("body.results.errors"));
				resultset.put("statusCode", String.valueOf(r.statusCode()));
				if (jsonPathEvaluator.getInt("body.results.processed_event_instances") == 0) {
					resultset.put("error_message",
							jsonPathEvaluator.getString("body.results.details.events_error.error_message"));
				} else {
					resultset.put("error_message",
							jsonPathEvaluator.getString("body.results.details.events.event.errors.error.error_message"));
				}
				return resultset;
			} else {
				Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
				Assert.assertEquals(r.statusCode(), expectedStatusCode);
				JsonPath jsonPathEvaluator = r.jsonPath();
				Assert.assertEquals(jsonPathEvaluator.get("body.results.details.listid").toString(),
						String.valueOf(listId));
				HashMap<String, String> resultset = new HashMap<String, String>();
				resultset.put("processed_event_instances",
						jsonPathEvaluator.getString("body.results.processed_event_instances"));
				resultset.put("scheduled_messages", jsonPathEvaluator.getString("body.results.scheduled_messages"));
				resultset.put("errors", jsonPathEvaluator.getString("body.results.errors"));
				resultset.put("event_definition_name",
						jsonPathEvaluator.getString("body.results.details.events.event_definition_name"));
				resultset.put("memberid", jsonPathEvaluator.getString("body.results.details.events.event.memberid"));
				if (jsonPathEvaluator.getString(
						"body.results.details.events.event.scheduled_messages") != null) {
					resultset.put("message_id", jsonPathEvaluator.getString(
							"body.results.details.events.event.scheduled_messages.scheduled_message.message_id"));
				}
				if (jsonPathEvaluator.getString(
						"body.results.details.events.event.errors") != null) {
					resultset.put("error_message", jsonPathEvaluator.getString(
							"body.results.details.events.event.errors.error.error_message"));
				}
				resultset.put("statusCode", String.valueOf(r.statusCode()));
				return resultset;
			}
		}
	}

	public static synchronized HashMap<String, String> eventUploadAPI_XML_V2(Map<String, String> eventDataInMap, String eventName,
			String listId, String scheduledOn, String timeZone, String evaluationScope, String verbosity) {
		RestAssured.baseURI = ConfigReader.getConfig("eventUploadApiUrl");

		JSONObject xy = new JSONObject(eventDataInMap);
		String xml = XML.toString(xy);
		String xmlBody = "<body> <events name='" + eventName + "'> <event scheduled_on='" + scheduledOn + "' timezone='"
				+ timeZone + "' evaluation_scope='" + evaluationScope + "'> " + xml + " </event> </events> <listid>"
				+ listId + "</listid> </body>";

		System.out.println(xmlBody);
		if (verbosity.equalsIgnoreCase("full")) {
			Response r = expect().given().contentType("application/xml")
					.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
					.header("X-Ms-API-Version", "2.0")
					.header("X-Ms-Format", "xml").header("X-Ms-Source", "api").header("X-Ms-Verbosity", "full")
					.body(xmlBody).log().all().when().post();
			String body = r.getBody().asString();
			System.out.println(body);
			Assert.assertEquals(r.header("Content-Type"), "text/xml;charset=utf-8");
			XmlPath xmlPath = new XmlPath(body);
			Assert.assertEquals(xmlPath.get("body.results.details.listid.text()").toString(), String.valueOf(listId));
			HashMap<String, String> resultset = new HashMap<String, String>();
			resultset.put("event_definition_name", xmlPath.get("body.results.details.events.@event_definition_name"));
			resultset.put("list_id", xmlPath.get("body.results.details.listid.text()"));
			resultset.put("status_code", String.valueOf(r.statusCode()));
			return resultset;
		} else {
			Response r = expect().statusCode(200).given().contentType("application/xml")
					.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
					.header("X-Ms-API-Version", "2.0")
					.header("X-Ms-Format", "xml").header("X-Ms-Source", "api").body(xmlBody).log().all().when().post();
			String body = r.getBody().asString();
			System.out.println(body);
			Assert.assertEquals(r.header("Content-Type"), "text/xml;charset=utf-8");
			XmlPath xmlPath = new XmlPath(body);
			Assert.assertEquals(xmlPath.get("body.results.details.listid.text()").toString(), String.valueOf(listId));
			HashMap<String, String> resultset = new HashMap<String, String>();
			resultset.put("event_definition_name", xmlPath.get("body.results.details.events.@event_definition_name"));
			resultset.put("list_id", xmlPath.get("body.results.details.listid.text()"));
			resultset.put("status_code", String.valueOf(r.statusCode()));
			return resultset;
		}

	}

	public static synchronized String eventUploadAPI_XML_V1(Map<String, String> eventDataInMap, String eventName,
			String listId, String scheduledOn, String timeZone, String evaluationScope) {
		RestAssured.baseURI = ConfigReader.getConfig("eventUploadApiUrl");
		eventDataInMap.put("scheduled_on", scheduledOn);
		eventDataInMap.put("timezone", timeZone);
		eventDataInMap.put("evaluation_scope", evaluationScope);

		JSONObject xy = new JSONObject(eventDataInMap);
		String xml = XML.toString(xy);
		String jsonBody = "<body> <events> <name>" + eventName + "</name> <event> " + xml
				+ "</event> </events> <listid>" + listId + "</listid> </body>";

		System.out.println(jsonBody);
		Response r = expect().statusCode(200).given().contentType("application/xml")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-API-Version", "1.0")
				.header("X-Ms-Format", "xml").header("X-Ms-Source", "api").header("X-Ms-Verbosity", "full")
				.body(jsonBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "text/xml;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.details.listid.text()").toString(), String.valueOf(listId));
		return xmlPath.getString("body.results.processed_event_instances.text()");
	}
	
	public static synchronized void eventUploadAPI_V2_Bulk_JSON( String eventName) throws Exception {
		RestAssured.baseURI = ConfigReader.getConfig("eventUploadApiUrl");
		String listId="5006"; 
		String scheduledOn="immediate";
		String timeZone="Asia/Kolkata";
		String evaluationScope="all";
		
		String[] phones = new String[7]; 
		for (int x= 0; x<=6; x++) {
			String phoneNumber = RandomeUtility.getRandomPhoneNumber();
			MoAPI.subscribeMemberInSMSListByListName("57889", phoneNumber, "segment test list");
			phones[x] = phoneNumber;
		}
		
		String testdata = "{ \"body\": { \"events\": { \"name\": \""+eventName+"\", \"event\": [ "
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[0]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[1]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[2]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[3]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[4]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[5]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[6]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" }"
				+" ] }, \"listid\": \""+listId+"\" } }";
		
		Response r = expect().given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-API-Version", "2.0")
				.header("X-Ms-Format", "json").header("X-Ms-Source", "api").header("X-Ms-Verbosity", "full")
				.body(testdata).log().all().when().post();
		Thread.sleep(6000);
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.statusCode(), 200);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		JsonPath jsonPathEvaluator = r.jsonPath();
		Assert.assertEquals(jsonPathEvaluator.getString("body.results.processed_event_instances"), "7");
		Assert.assertEquals(jsonPathEvaluator.getString("body.results.scheduled_messages"), "7");
		Assert.assertEquals(jsonPathEvaluator.getString("body.results.details.events.event_definition_name"), eventName);
		
	}
}
