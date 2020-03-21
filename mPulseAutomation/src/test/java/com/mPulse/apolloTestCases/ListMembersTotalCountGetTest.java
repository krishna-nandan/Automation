package com.mPulse.apolloTestCases;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.json.JSONException;
import org.json.JSONObject;
import com.mPulse.factories.mPulseDB;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.MatcherAssert.*;

import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.Resources;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.List;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class ListMembersTotalCountGetTest extends ApolloBaseTest{

	int memCount;
	int memCountw;

	ListMembersTotalCountGetTest() {
	}

	String Host = ConfigReader.getConfig("apollo_host");
	
	@Test(groups = {"apollo_api"})

	public void Verify_that_the_response_code_is_200_and_response_is_in_JSON_format() {

		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.totalListMemberCountGet()).

				then().assertThat().statusCode(200).and().contentType(ContentType.JSON);

	}

	@Test(groups = {"apollo_api"})

	public void Verify_that_when_invalid_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken() {
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_wkey")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.totalListMemberCountGet()).

				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})
	public void Verify_that_when_no_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken()

	{

		RestAssured.baseURI = Host;
		given()

				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.totalListMemberCountGet()).

				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})
	public void Test4()throws Exception 
	{
	
		String memberCount = mPulseDB.getStringData("select count(*) from member_subscription where account_id="
					+ Resources.accid + " and list_id=" + Resources.listid);
		String memberCountW = mPulseDB.getStringData("select count(*) from member_subscription where account_id=" + Resources.accid
							+ " and list_id=" + Resources.listidw);
		memCount = Integer.parseInt(memberCount);
		memCountw = Integer.parseInt(memberCountW);
		
		RestAssured.baseURI = Host;
		Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key"))
				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).
				when().get(Resources.totalListMemberCountGet()).then().contentType(ContentType.JSON).extract().response();

		String res1 = res.asString();
		int resCount = Integer.parseInt(res1.trim());

		Assert.assertEquals(resCount, memCount);

	}

	@Test(groups = {"apollo_api"})
	public void Test5()

	{
		RestAssured.baseURI = Host;
		Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key"))

				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.getMemberCountWrong()).then().contentType(ContentType.JSON).extract().response();

		String res1 = res.asString();

		int resCount = Integer.parseInt(res1.trim());

		Assert.assertEquals(resCount, memCountw);

	}

}
