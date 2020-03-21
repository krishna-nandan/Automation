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
import java.util.*;

public class ListSmsGetTest extends ApolloBaseTest{

	int count, case5;

	Response res;

	String Host = ConfigReader.getConfig("apollo_host");
	
	@Test(groups = {"apollo_api"})
	public void Verify_that_the_response_code_is_200_and_response_is_in_JSON_format() {
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
		.header("Content-Type", ConfigReader.getConfig("apollo_content")).
		when().get(Resources.listSmsGet()).
		then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}

	@Test(groups = {"apollo_api"})

	public void Verify_that_when_invalid_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken() {
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_wkey")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
		.header("Content-Type", ConfigReader.getConfig("apollo_content")).
		when().get(Resources.listSmsGet()).
		then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
		.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})
	public void Verify_that_when_no_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken()

	{

		RestAssured.baseURI = Host;
		given().header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.listSmsGet()).

				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})

	public void Test4() {

		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).
				when().get(Resources.listSmsGet()).
				then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("list_sms_message_ids.size()", equalTo(count));
	}

	@Test(groups = {"apollo_api"})

	public void Test5() throws Exception {
		String totalListCount = mPulseDB
				.getStringData("select count(*) from list_sms_message where list_id=" + Resources.listid);

		String topValue = mPulseDB.getStringData(
				"select id from list_sms_message where list_id=" + Resources.listid + " order by id desc limit 1");

		count = Integer.parseInt(totalListCount);
		case5 = Integer.parseInt(topValue);
		RestAssured.baseURI = Host;

		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.listSmsGet()).

				then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("list_sms_messages." + case5, hasKey("account_id")).and()
				.body("list_sms_messages." + case5, hasKey("created_on")).and()
				.body("list_sms_messages." + case5, hasKey("disable")).and()
				.body("list_sms_messages." + case5, hasKey("enable_deeplink_for_sub_preferences")).and()
				.body("list_sms_messages." + case5, hasKey("id")).and()
				.body("list_sms_messages." + case5, hasKey("last_modified_on")).and()
				.body("list_sms_messages." + case5, hasKey("list_id")).and()
				.body("list_sms_messages." + case5, hasKey("message_type")).and()
				.body("list_sms_messages." + case5, hasKey("mo_keywords"));
	}

	@Test(groups = {"apollo_api"})

	public void Test6() {

		RestAssured.baseURI = Host;

		res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.listSmsGet());

		ArrayList<Object> jsResponse = (ArrayList<Object>) res.jsonPath().getList("list_sms_message_ids");

		String[] actual_messages = new String[jsResponse.size()];

		for (int i = 0; i < jsResponse.size(); i++) {

			actual_messages[i] = res.jsonPath().getString("list_sms_messages." + jsResponse.get(i) + ".message_type");

		}

		String[] expected_messages = { "welcome", "unknown", "confirm", "confirm_extend", "replied_no",
				"help_for_both_channel", "email_subscribed_from_sms", "age_gate_rejection",
				"bye_bye_list_keyword_both_channel", "duplicate_join", "resub_confirm", "resub_welcome" };

		Assert.assertEquals(actual_messages, expected_messages);

	}

}
