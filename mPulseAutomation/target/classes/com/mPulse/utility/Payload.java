package com.mPulse.utility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;

import static io.restassured.RestAssured.given;

public class Payload {
	
	static String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
	
	public static String listPostDataAllTrue() {
		String listName = "abc" + timeStamp;
		String keyWord = "k" + timeStamp;
		String data = "{" + "\"name\":" + "\"" + listName + "\",\n"

				+ "	\"description\": \"some description goes here\",\n"
				+ "	\"enabled_channels\": [true, true, true, true],\n" + "	\"send_from\": \"xyz@yopmail.com\",\n"
				+ "	\"reply_to\": \"xyz@yopmail.com\",\n" + "	\"from_name\": \"Nakul Sharma\",\n"
				+ "	\"contact_address\": \"d9, sector-3 Noida\",\n"
				+ "	\"permission_reminder\": \"some permission reminder goes here\",\n"
				+ "	\"send_welcome_message\": true,\n" + "	\"need_double_optin\": true,\n" + "	\"keyword\":" + "\""
				+ keyWord + "\",\n" + "	\"shortcode\": {\n" + "		\"id\": 1\n" + "	},\n"
				+ "	\"need_confirm_sms_opt_in\": true,\n" + "	\"send_sms_welcome_message\": true,\n"
				+ "	\"mark_for_appmail_subscription\": true\n" + "}";

		return data;
	}
	
	public static String testGroupMemberPostData() {
		String data = "{"+"\"member_id\":"+ Resources.memberid;
		return data;
	}
	
	public static String testGroupCampaignLaunch() {
		String data = "{" +"\"list_id\":" +Resources.listid+ "\"channel\":\"sms\", \n"
				+ "\"track_links\": false, \n"
				+ "\"text_content\": \"testGroupcamptext\", \n"
				+ "\"members\":[" +Resources.memberid+","+Resources.memberid+","+Resources.memberid+"], \n"
				+ "\"encoding\": 0,"
				+ "\"subject\": \"ManeeshaTestGroupTest says hello\"}";
		return data;
	}

	public static String customFieldPostData(String name, String fieldType, String description) {
		String data = "{"+ "\"description\":" +description+ ", \n"
				+ "\"field_type\":" +fieldType+ ", \n"
				+ "\"name\":\"" +name+ "\"}";
		return data;
	}

	public static String customFieldPostDataWithValues(String name, String fieldType, String description, String valueOne, String valueTwo) {
		String data = "{"+ "\"description\": " +description+ ", \n"
				+ "\"field_type\":" +fieldType+ ", \n"
				+ "\"name\":\"" +name+ "\", \n "
				+ "\"values\": [\"" +valueOne+ "\",\"" +valueTwo+ "\"] }";
		System.out.println(data);
		return data;
	}

	public static String customFieldUpdateDataWithValues(String name, String fieldType, String description, String valueOne, String valueTwo) {
		String data = "{" + "\"description\": " + description + ", \n"
				+ "\"field_type\":" + fieldType + ", \n"
				+ "\"name\":\"" + name + "\", \n "
				+ "\"values\": [\"" + valueOne + "\",\"" + valueTwo + "\"] }";
		System.out.println(data);
		return data;
	}

	public static JsonPath sendPostRequest(String customFieldName, String type, String description, int statusCode) {
		RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
		Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).
						body(Payload.customFieldPostData(customFieldName, type, description)).
						when().log().all().
						post(Resources.customFieldPost()).
						then().statusCode(statusCode).and().contentType(ContentType.JSON).extract().response();
		JsonPath jsonRespond = res.jsonPath();
		return jsonRespond;
	}

	public static JsonPath sendPostRequestWithValues(String customFieldName, String type, String description, int statusCode, String valueOne, String valueTwo) {
		RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
		Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).
						body(Payload.customFieldPostDataWithValues(customFieldName, type, description, valueOne, valueTwo)).
						when().log().all().
						post(Resources.customFieldPost()).
						then().statusCode(statusCode).and().contentType(ContentType.JSON).
						extract().response();
		JsonPath jsonRespond = res.jsonPath();
		return jsonRespond;
	}

}
