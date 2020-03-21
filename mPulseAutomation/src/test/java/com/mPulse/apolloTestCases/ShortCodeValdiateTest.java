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
	
public class ShortCodeValdiateTest extends ApolloBaseTest{

	int shortcode = 149;
	String keyWord;
	
	String Host = ConfigReader.getConfig("apollo_host");
	
	@Test(groups = {"apollo_api"})

	public void Verify_that_the_response_code_is_200_and_response_is_in_JSON_format() throws Exception {
		
		keyWord = mPulseDB.getStringData(
				"select keyword from list where account_id ="+Resources.accid+" and shortcode_id="+Resources.shortcode_id+"order by created_on desc limit 1");

		RestAssured.baseURI = Host;
		given().queryParam("shortcode_id", shortcode).
		queryParam("keyword", keyWord).header("Authorization", ConfigReader.getConfig("apollo_api_key"))
		.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).
		when().get(Resources.shortcodeValidation()).
		then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}

	@Test(groups = {"apollo_api"})

	public void Verify_that_when_invalid_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken() {

		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_wkey")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).
		when().get(Resources.shortcodeValidation()).
		then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
		.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})
	public void Verify_that_when_no_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken()
	{
		RestAssured.baseURI = Host;
		given().header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).
		when().get(Resources.shortcodeValidation()).
		then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
		.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})
	public void Test4() throws Exception {
		
		keyWord = mPulseDB.getStringData(
				"select keyword from list where account_id ="+Resources.accid+" and shortcode_id=149 order by created_on desc limit 1");

		String expected = "false";

		RestAssured.baseURI = Host;
		Response res = given().queryParam("shortcode_id", shortcode).

				queryParam("keyword", keyWord).header("Authorization", ConfigReader.getConfig("apollo_api_key"))
				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.shortcodeValidation()).then().contentType(ContentType.JSON).extract().response();

		// then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
		// body("error.type", equalTo("false"));

		String res1 = res.asString();

		Assert.assertEquals(res1.trim(), expected);
	}

	@Test(groups = {"apollo_api"})
	public void Test5()throws Exception {
		
		keyWord = mPulseDB.getStringData(
				"select keyword from list where account_id ="+Resources.accid+" and shortcode_id=149 order by created_on desc limit 1");

		String expected = "true";
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

		RestAssured.baseURI = Host;
		Response res = given().queryParam("shortcode_id", shortcode).

				queryParam("keyword", "a" + timeStamp).header("Authorization", ConfigReader.getConfig("apollo_api_key"))
				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.shortcodeValidation()).then().contentType(ContentType.JSON).extract().response();

		String res1 = res.asString();

		Assert.assertEquals(res1.trim(), expected);
	}

}
