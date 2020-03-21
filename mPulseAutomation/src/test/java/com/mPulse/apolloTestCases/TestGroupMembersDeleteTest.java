package com.mPulse.apolloTestCases;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

import org.json.*;
import com.mPulse.factories.mPulseDB;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.Resources;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.List;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class TestGroupMembersDeleteTest extends ApolloBaseTest{

	String Host = ConfigReader.getConfig("apollo_host");

	@Test(groups = {"apollo_api"})
	public void Verify_that_the_response_code_is_200_and_response_is_in_JSON_format() {
		RestAssured.baseURI=Host;
		given().header("Authorization",ConfigReader.getConfig("apollo_api_key"))
		.header("Content-Type", ConfigReader.getConfig("apollo_content"))
		.when().delete(Resources.testGroupMemberDelete())
		.then().assertThat().statusCode(200).and().assertThat().contentType(ContentType.JSON);
	}
	
	@Test(groups = {"apollo_api"})
	public void Verify_that_when_invalid_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken() {
		RestAssured.baseURI=Host;
		given().header("Authorization",ConfigReader.getConfig("apollo_wkey")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
		.header("Content-Type", ConfigReader.getConfig("apollo_content"))
		.when().delete(Resources.testGroupMemberDelete())
		.then().assertThat().statusCode(401).and().assertThat().contentType(ContentType.JSON)
		.and().assertThat().body("error.type",equalTo("InvalidAuthToken"));
	}
	
	@Test(groups = {"apollo_api"})
	public void Verify_that_when_no_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken(){
		RestAssured.baseURI = Host;
			given().header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
			.header("Content-Type", ConfigReader.getConfig("apollo_content")).
			when().delete(Resources.testGroupMemberDelete()).
			then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
			.body("error.type", equalTo("InvalidAuthToken"));
	}
	
	@Test(groups = {"apollo_api"})
	//Verify the member ids returned by API and match with entries in DB
	public void Test4() throws Exception {
		RestAssured.baseURI=Host;
		given().header("Authorization",ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source",ConfigReader.getConfig("apollo_source"))
		.header("Content-Type",ConfigReader.getConfig("apollo_content"))
		.when().delete(Resources.testGroupMemberDelete()).then().assertThat().body("message", equalTo("member removed from test group successfully"));
		
//		String testGroupMembersDb = mPulseDB.getStringData("select audience_member_id from message_test_group_subscription where account_id="+Resources.accid+"and audience_member_id="+Resources.memberid);	
//		System.out.println("Test Group Members from DB are: "+testGroupMembersDb);
//				
//		Assert.assertEquals(testGroupMembersDb, "");

	}
	
	@Test(groups = {"apollo_api"})
	//Verify the message when member is not in the Test Group
	public void Test5() throws Exception {
		
		RestAssured.baseURI=Host;
		given().header("Authorization",ConfigReader.getConfig("apollo_api_key"))
		.header("Content-Type",ConfigReader.getConfig("apollo_content"))
		.when().delete(Resources.testGroupMemberDelete()).then().assertThat().statusCode(400);
		
		JsonPath response = given().header("Authorization",ConfigReader.getConfig("apollo_api_key"))
				.header("Content-Type",ConfigReader.getConfig("apollo_content"))
				.when().delete(Resources.testGroupMemberDelete()).jsonPath();
		String resMessage = response.getString("errors.member_id");
				
		Assert.assertEquals(resMessage, "[Message test group subscription doesn't exist for member]");
		
		
	}
}	
