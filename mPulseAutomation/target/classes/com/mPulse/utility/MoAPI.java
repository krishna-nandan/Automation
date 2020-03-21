package com.mPulse.utility;

import static io.restassured.RestAssured.expect;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.mPulse.factories.mPulseDB;

public class MoAPI {
	
	public static synchronized void hitMoApi(String shortcode, String phoneNumber, String message) {
		String myJson = String.format(
				"{\"shortcode\":\"%s\", \"phone_number\":\"%s\",  \"carrier\":\"Boost\",  \"message\":\"%s\"}",
				shortcode, phoneNumber, message);
		RestAssured.baseURI = ConfigReader.getConfig("moApiUrl");

		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", ConfigReader.getConfig("moApiBasicAuth")).header("X-Ms-Source", "sms")
				.header("X-Ms-Format", "json").header("X-Ms-Async", "false").body(myJson).log().all().when().post();

		//String body = r.getBody().asString();
		Assert.assertEquals(r.statusCode(), 200);
	}
	
	public static synchronized void subscribeMemberInSMSListByListName(String shortcode, String phoneNumber, String listName) throws Exception {
		String listKeyword = mPulseDB.getResultInString("select keyword from list where name ='"+listName+"' and account_id="+ConfigReader.getConfig("account_id"));
		hitMoApi(shortcode, phoneNumber, listKeyword);
		Thread.sleep(5000);
		hitMoApi(shortcode, phoneNumber, "yes");
	}
	
	public static synchronized void subscribeMemberInSMSListByListID(String shortcode, String phoneNumber, String listID) throws Exception {
		String listKeyword = mPulseDB.getResultInString("select keyword from list where id ='"+listID+"' and account_id="+ConfigReader.getConfig("account_id"));
		hitMoApi(shortcode, phoneNumber, listKeyword);
		Thread.sleep(5000);
		hitMoApi(shortcode, phoneNumber, "yes");
	}

}
