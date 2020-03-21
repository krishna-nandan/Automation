package com.mPulse.apolloTestCases;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import com.mPulse.factories.mPulseDB;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import com.mPulse.utility.Resources;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.Payload;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;

public class ListPostTest extends ApolloBaseTest{

	String list_name;
	String value_api = new String();
	String key_api = new String();
	String value_db = new String();
	String key_db = new String();
	String Host = ConfigReader.getConfig("apollo_host");
	
	@Test(groups = {"apollo_api"})
	public void Verify_that_the_response_code_is_200_and_response_is_in_JSON_format() {
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
		.header("Content-Type", ConfigReader.getConfig("apollo_content")).body(Payload.listPostDataAllTrue()).
		when().post(Resources.listPost()).
		then().assertThat().statusCode(201).and().contentType(ContentType.JSON).extract().response();
	}

	@Test(groups = {"apollo_api"})

	public void Verify_that_when_invalid_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken() {

		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_wkey")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.listPost()).

				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})
	public void Verify_that_when_no_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken()

	{

		RestAssured.baseURI = Host;
		given().header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.listPost()).

				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})

	public void Test4() throws Exception {

		RestAssured.baseURI = Host;
		Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key"))
				.header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content"))
				.body(Payload.listPostDataAllTrue()).

				when().post(Resources.listPost()).

				then().

				assertThat().statusCode(201).and().contentType(ContentType.JSON).extract().response();
		
	

		Map<String, String> new_list = new HashMap<String, String>();
		new_list = res.jsonPath().getMap("$");
		list_name = res.jsonPath().getString("name");
		new_list.remove("_members_count");
		new_list.remove("_url");
		new_list.remove("shortcode");
		new_list.remove("account");

		Map<String, String> new_list_1 = new HashMap<String, String>();

		Map<String, String> db_content = new HashMap<String, String>();
		db_content = mPulseDB.getResultMap("select * from list where name =" + "'" + list_name + "'");
		db_content.remove("shortcode");
		db_content.remove("allow_txnl_campaigns");
		db_content.remove("shortcode_id");
		Map<String, String> db_content_1 = new HashMap<String, String>();
		// for (int i = 0; i < new_list.size(); i++) {

		for (Map.Entry mapElement : new_list.entrySet()) {
			key_api =  mapElement.getKey().toString();
			if (mapElement.getValue() != null)
				value_api = mapElement.getValue().toString();

			if (value_api != null && value_api == "true")
				new_list_1.put(key_api, "t");
			
			else if (value_api != null && value_api == "false")
				new_list_1.put(key_api, "f");

			else
				new_list_1.put("key_api", "value_api");

			// else if (value != null && key == "enabled_channels")

		}

		for (Map.Entry mapElement : new_list.entrySet()) {
			key_db =  mapElement.getKey().toString();
			if (mapElement.getValue() != null)
				value_db = mapElement.getValue().toString();

			db_content_1.put("key_db", "value_db");

		}

		Assert.assertEquals(new_list_1, db_content_1);

		// System.out.println("api value" + new_list);
		// System.out.println("db value " + db_content);
		//
		// System.out.println(new ArrayList<>(new_list.values()).equals(new
		// ArrayList<>(db_content.values())));
		//
		// // -- Code to print the content of the hash
		//
		// Set<Entry<String, String>> hashSet = new_list.entrySet();
		// for (@SuppressWarnings("rawtypes")
		// Entry entry : hashSet) {
		//
		// System.out.println("Key=" + entry.getKey() + ", Value=" + entry.getValue());
		// }
	}

}
