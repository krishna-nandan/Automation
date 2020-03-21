package com.mPulse.apolloTestCases;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.mPulse.factories.mPulseDB;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.Resources;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.*;
import java.util.Properties;

public class ListMembersCountSecureMessageGetTest extends ApolloBaseTest{

	String Host = ConfigReader.getConfig("apollo_host");
	
	@Test(groups = {"apollo_api"})
	public void Verify_that_the_response_code_is_200_and_response_is_in_JSON_format() 
	{
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).
				when().get(Resources.listMemberByChannelSecureMessageGet()).
				then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().assertThat().body("size()",equalTo(4));
	}

	@Test(groups = {"apollo_api"})
	public void Verify_that_when_invalid_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken() 
	{
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_wkey")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).
				when().get(Resources.listMemberByChannelSecureMessageGet()).
				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));
	}

	@Test(groups = {"apollo_api"})
	public void Verify_that_when_no_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken()
	{
		RestAssured.baseURI = Host;
		given()
				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).
				when().get(Resources.listMemberByChannelSecureMessageGet()).
				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));
	}

	@Test(groups = {"apollo_api"})
// Verify that when correct authorization key and headers are passed, count of subscribed/unsubscribed/pending/not_subscribed member is showing correct in response.
	public void Test4() throws Exception
	{		
			String subMemberCount = mPulseDB.getStringData("select count(*) from member_subscription where account_id="
					+ Resources.accid + " and list_id=" + Resources.listid + "and secure_message_status='ACTIVE'");
			System.out.println("Count of members subscribed to Secure Message from DB in string "+subMemberCount);
			String unSubMemberCount = mPulseDB.getStringData("select count(*) from member_subscription where account_id="
					+ Resources.accid + " and list_id=" + Resources.listid + "and secure_message_status= 'INACTIVE'");
			String pendingMemberCount = mPulseDB.getStringData("select count(*) from member_subscription where account_id="
					+ Resources.accid + " and list_id=" + Resources.listid + "and secure_message_status='PENDING'");
			String notSubMemberCount = mPulseDB.getStringData("select count(*) from member_subscription where account_id="
					+ Resources.accid + " and list_id=" + Resources.listid + "and secure_message_status is null");
			int	subMemCount = Integer.parseInt(subMemberCount);
			int unSubMemCount = Integer.parseInt(unSubMemberCount);
			int pendingMemCount = Integer.parseInt(pendingMemberCount);
			int notSubMemCount = Integer.parseInt(notSubMemberCount);
			System.out.println("Count of members subscribed to Secure Message from DB is "+subMemCount);
		
			RestAssured.baseURI = Host;
			Response response = given().header("Authorization", ConfigReader.getConfig("apollo_api_key"))
					.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).
					when().get(Resources.listMemberByChannelSecureMessageGet()).then().contentType(ContentType.JSON).extract().response();

			System.out.println("Checking if count of subscribed member is showing correct in response");
			int resSubMemCount = response.jsonPath().getInt("subscribed");
			System.out.println("Count of members subscribed to SecureMessage from DB is "+subMemCount);
			System.out.println("Count of members subscribed to SecureMessage from response is "+resSubMemCount);
			Assert.assertEquals(resSubMemCount, subMemCount);

			System.out.println("Checking if count of unsubscribed member is showing correct in response");
			int resUnSubMemCount = response.jsonPath().getInt("unsubscribed");
			System.out.println("Count of members unsubscribed to SecureMessage from DB is "+unSubMemCount);
			System.out.println("Count of members unsubscribed to SecureMessage from response is "+resUnSubMemCount);
			Assert.assertEquals(resUnSubMemCount, unSubMemCount);
			
			System.out.println("Checking if count of pending member is showing correct in response");
			int resPendingMemCount = response.jsonPath().getInt("pending");
			System.out.println("Count of members pending to SecureMessage from DB is "+pendingMemCount);
			System.out.println("Count of members pending to SecureMessage from response is "+resPendingMemCount);
			Assert.assertEquals(resPendingMemCount, pendingMemCount);
			
			System.out.println("Checking if count of not_subscribed member is showing correct in response");
			int resNotSubMemCount = response.jsonPath().getInt("not_subscribed");
			System.out.println("Count of members pending to SecureMessage from DB is "+notSubMemCount);
			System.out.println("Count of members pending to SecureMessage from response is "+resNotSubMemCount);
			Assert.assertEquals(resNotSubMemCount, notSubMemCount);
				
	}

//	@Test(groups = {"apollo_api"})
// Verify the error when correct authorization key and headers are passed but list_id of different account is passed.
//
//	public void Test5()
//	{
//		RestAssured.baseURI = Host
//		Response response = given().header("Authorization", ConfigReader.getConfig("apollo_api_key"))
//				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).
//				when().get(Resouces.getMemberCountWrong()).then().contentType(ContentType.JSON).extract().response();
//
//		String errorMessage = response.jsonPath().getString("list_id");
//		Assert.assertEquals(errorMessage, "Invalid list_id");
//	}
}
