package com.mPulse.apolloTestCases;

import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import com.mPulse.factories.mPulseDB;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.Resources;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.*;
import java.util.Properties;

public class ListGetTest extends ApolloBaseTest {

	Response Res;
	int count;
	int case5;
	JSONObject value = new JSONObject();
	String Host = ConfigReader.getConfig("apollo_host");

	@Test(groups = {"apollo_api"})
	public void Verify_that_the_response_code_is_200_and_response_is_in_JSON_format() {
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
		.header("Content-Type", ConfigReader.getConfig("apollo_content")).
		when().get(Resources.accountWiseList()).
		then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}

	@Test(groups = {"apollo_api"})

	public void Verify_that_when_invalid_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken() {

		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_wkey")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.accountWiseList()).

				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})
	public void Verify_that_when_no_authorization_key_is_passed_response_code_is_401_and_error_type_is_InvalidAuthToken()

	{

		RestAssured.baseURI = Host;
		given().header("X-Ms-Source", ConfigReader.getConfig("apollo_source")).header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.accountWiseList()).

				then().assertThat().statusCode(401).and().contentType(ContentType.JSON).and()
				.body("error.type", equalTo("InvalidAuthToken"));

	}

	@Test(groups = {"apollo_api"})

	public void Test4() {

		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).

				when().get(Resources.accountWiseList()).

				then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("list_ids.size()", equalTo(count));

	}

	@Test(groups = {"apollo_api"})

	public void Test5() throws Exception {
		String totalListCount = mPulseDB
				.getStringData("select count(*) from list where account_id="+Resources.accid+" and mark_for_delete='f'");
		String topValue = mPulseDB.getStringData(
				"select id from list where account_id="+Resources.accid+"and mark_for_delete='f' order by created_on desc limit 1");

		case5 = Integer.parseInt(topValue);
		count = Integer.parseInt(totalListCount);
	
		RestAssured.baseURI = Host;
		given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
				.header("Content-Type", ConfigReader.getConfig("apollo_content")).
				when().get(Resources.accountWiseList()).
				then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("lists." + case5, hasKey("_members_count")).and().body("lists." + case5, hasKey("_url")).and()
				.body("lists." + case5, hasKey("account")).and().body("lists." + case5, hasKey("account_id")).and()
				.body("lists." + case5, hasKey("age_limit")).and().body("lists." + case5, hasKey("contact_address"))
				.and().body("lists." + case5, hasKey("created_on")).and().body("lists." + case5, hasKey("description"))
				.and().body("lists." + case5, hasKey("enable_click_to_join")).and()
				.body("lists." + case5, hasKey("enable_for_location_smart")).and()
				.body("lists." + case5, hasKey("enabled_channels")).and().body("lists." + case5, hasKey("from_name"))
				.and().body("lists." + case5, hasKey("id")).and().body("lists." + case5, hasKey("keyword")).and()
				.body("lists." + case5, hasKey("last_modified_on")).and()
				.body("lists." + case5, hasKey("location_smart_client_id")).and()
				.body("lists." + case5, hasKey("location_smart_group_id")).and()
				.body("lists." + case5, hasKey("location_smart_service_id")).and()
				.body("lists." + case5, hasKey("make_public")).and()
				.body("lists." + case5, hasKey("mark_for_appmail_subscription")).and()
				.body("lists." + case5, hasKey("mark_for_delete")).and().body("lists." + case5, hasKey("name")).and()
				.body("lists." + case5, hasKey("need_confirm_sms_opt_in")).and()
				.body("lists." + case5, hasKey("need_double_opt_in")).and()
				.body("lists." + case5, hasKey("permission_reminder")).and().body("lists." + case5, hasKey("reply_to"))
				.and().body("lists." + case5, hasKey("send_from")).and()
				.body("lists." + case5, hasKey("send_sms_welcome_message")).and()
				.body("lists." + case5, hasKey("send_welcome_message")).and()
				.body("lists." + case5, hasKey("shortcode")).and()
				.body("lists." + case5, hasKey("use_different_messages_for_resub"));

		// then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and()

	}

}
