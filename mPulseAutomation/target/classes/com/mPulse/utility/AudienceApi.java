package com.mPulse.utility;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import com.mPulse.factories.mPulseDB;
import org.json.JSONObject;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.expect;

public class AudienceApi {

	public static synchronized String createNewMember(Map<String, String> memberDataInMap) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		JSONObject xy = new JSONObject(memberDataInMap);
		String jsonBody = "{\"body\": { \"member\": [ " + xy.toString() + " ] } }";
		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Ignore-Error-For-Existing-Members", "true").header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Update","included_fields_only")
				.body(jsonBody).log().all().when().put();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
		JsonPath jsonPathEvaluator = r.jsonPath();
		return jsonPathEvaluator.get("body.members[0].member.id.memberid").toString();
	}

	public static synchronized String createNewMemberAndSub(Map<String, String> memberDataInMap, String list_id) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		JSONObject xy = new JSONObject(memberDataInMap);
		String jsonBody = "{\"body\": { \"member\": [ " + xy.toString() + " ], \"listid\": \"" + list_id + "\" } }";
		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Ignore-Error-For-Existing-Members", "true").header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Update","included_fields_only")
				.body(jsonBody).log().all().when().put();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
		JsonPath jsonPathEvaluator = r.jsonPath();
		Assert.assertEquals(jsonPathEvaluator.get("body.members[0].member.result").toString(), "success");
		return jsonPathEvaluator.get("body.members[0].member.id.memberid").toString();
	}
	
	public static synchronized String unSubMember(Map<String, String> memberDataInMap, String list_id) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		JSONObject xy = new JSONObject(memberDataInMap);
		String jsonBody = "{\"body\": { \"member\": [ " + xy.toString() + " ], \"listid\": \"" + list_id + "\" } }";
		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Ignore-Error-For-Existing-Members", "true").header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "unsub")
				.header("X-Ms-Update","included_fields_only")
				.body(jsonBody).log().all().when().put();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
		JsonPath jsonPathEvaluator = r.jsonPath();
		Assert.assertEquals(jsonPathEvaluator.get("body.members[0].member.result").toString(), "success");
		return jsonPathEvaluator.get("body.members[0].member.id.memberid").toString();
	}
	
	public static synchronized String addMemberInPending(Map<String, String> memberDataInMap, String list_id) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		JSONObject xy = new JSONObject(memberDataInMap);
		String jsonBody = "{\"body\": { \"member\": [ " + xy.toString() + " ], \"listid\": \"" + list_id + "\" } }";
		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Ignore-Error-For-Existing-Members", "true").header("X-Ms-Format", "json")
				.header("X-Ms-List-Confirm", "true")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Update","included_fields_only")
				.body(jsonBody).log().all().when().put();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
		JsonPath jsonPathEvaluator = r.jsonPath();
		return jsonPathEvaluator.get("body.members[0].member.id.memberid").toString();
	}
	
	public static synchronized String upsertMemberRecord(Map<String, String> memberDataInMap) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		JSONObject xy = new JSONObject(memberDataInMap);
		String jsonBody = "{\"body\": { \"member\": [ " + xy.toString() + " ] } }";
		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Ignore-Error-For-Existing-Members", "true").header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Update","included_fields_only")
				.body(jsonBody).log().all().when().put();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
		JsonPath jsonPathEvaluator = r.jsonPath();
		return jsonPathEvaluator.get("body.members[0].member.id.memberid").toString();
	}
	
	public static synchronized void deleteMemberById(String memberId) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		String jsonBody = "{ \"body\": { \"member\": { \"memberid\":\""+memberId+"\" } } }";
		Response r = expect().given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.body(jsonBody).log().all().when().delete();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
	}
	
	public static synchronized void deleteMemberByPhoneNumber(String phoneNumber) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		String jsonBody = "{ \"body\": { \"member\": { \"mobilephone\":\""+phoneNumber+"\" } } }";
		Response r = expect().given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.body(jsonBody).log().all().when().delete();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
	}
	
	public static synchronized void deleteMemberByPhoneNumber2(String phoneNumber) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		String jsonBody = "{ \"body\": { \"member\": { \"mobilephone\":\""+phoneNumber+"\" } } }";
		Response r = expect().given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.body(jsonBody).log().all().when().delete();
	}
	
	public static synchronized void deleteMemberByEmail(String email) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		String jsonBody = "{ \"body\": { \"member\": { \"email\":\""+email+"\" } } }";
		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Format", "json")
				.header("X-Ms-Source", "api")
				.body(jsonBody).log().all().when().delete();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
	}

 	//Delete API Call where XML Body passing as a parameter
	public static synchronized String deleteMemberGeneral(String xmlBody, int statusCode) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");
		Response r = expect().statusCode(statusCode).given().contentType("application/xml")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Source", "api")
				.body(xmlBody).log().all().when().delete();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "text/xml;charset=utf-8");
		Assert.assertEquals(r.statusCode(), statusCode);
		return body;
	}
	public static synchronized void verifyMemberDeletedInDataBase(String memberId, String deleteFlag) throws Exception{
		String query = "select * from audience_member where id="+ memberId;
		Map<String, String> memberData = mPulseDB.getResultMap(query);
		Assert.assertEquals(memberData.get("deleted"), deleteFlag);
		if (deleteFlag.equalsIgnoreCase("t")) {
			Assert.assertEquals(memberData.get("mobile_phone"), null);
			Assert.assertEquals(memberData.get("app_member_id"), null);
			Assert.assertEquals(memberData.get("email"), null);
			Assert.assertEquals(memberData.get("client_member_id"), null);
		}
	}
	public static synchronized void verifyEmailIsNullInDataBase(String memberId) throws Exception{
		String query = "select email from audience_member where id="+ memberId;
		String deleteFlag = mPulseDB.getResultInString(query);
		Assert.assertEquals(deleteFlag, "null");
	}

	public static synchronized void deleteAllMemberById(ArrayList<String> audience_ids) {
		RestAssured.baseURI = ConfigReader.getConfig("audienceApiUrl");

		StringBuilder str = new StringBuilder("<body>	");
		for (String audience_id : audience_ids) {
			str.append("<member> <memberid>"+audience_id+"</memberid> </member> ");
		}
		str.append(" </body>");

		String xmlBody = str.toString();

		Response r = expect().given().contentType("application/xml")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Source", "api")
				.body(xmlBody).log().all().when().delete();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "text/xml;charset=utf-8");
		Assert.assertEquals(r.statusCode(), 200);
	}

	public static synchronized void deleteAllMemberById(String accountId) throws Exception {
		int maxLoopCount = 0;
		ArrayList<String> audience_ids = mPulseDB.getAllActiveAudienceMemberIdByLimit(accountId, "50");

		while (!audience_ids.isEmpty() && maxLoopCount <=20) {
			AudienceApi.deleteAllMemberById(audience_ids);
			audience_ids = mPulseDB.getAllActiveAudienceMemberIdByLimit(accountId, "50");
			maxLoopCount = maxLoopCount + 1;
		}
	}

}
