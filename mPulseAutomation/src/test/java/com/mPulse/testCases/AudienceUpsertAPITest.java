package com.mPulse.testCases;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.utility.*;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.expect;

public class AudienceUpsertAPITest extends BaseTest {

	String baseUrlAudience = "https://" + ConfigReader.getConfig("API_URL") + "/accounts/"
			+ ConfigReader.getConfig("account_id") + "/members";
	String basicAuth = UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"),
			ConfigReader.getConfig("api_password"));

	String listId = ConfigReader.getConfig("audienceApiTest_id");
	String accountId = ConfigReader.getConfig("account_id");
	String sc = ConfigReader.getConfig("audienceApiTest_sc");
	Logger logger = Logger.getLogger(AudienceUpsertAPITest.class);

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_API_request_fails_if_no_auth_credentials_are_given_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		RestAssured.baseURI = baseUrlAudience;
		Response r = expect().given().contentType("application/xml")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "upsert").body(xmlBody).log().all().when().put();
		System.out.println("The status code is "+ r.statusCode());
		Assert.assertEquals(r.statusCode(), 401);
		String body = r.getBody().asString();
		System.out.println(body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.error.text()").toString(), "The request has not been applied because it lacks valid authentication credentials for the target resource");
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_API_request_should_only_accessible_through_valid_basic_authorization_for_not_valid_authorization_response_code_should_be_403()
			throws Exception {
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		RestAssured.baseURI = baseUrlAudience;
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", UtilityMethods.getBasicAuth("screwed", "data"))
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		System.out.println("The status code is "+ r.statusCode());
		Assert.assertEquals(r.statusCode(), 403);
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(body, "Something went wrong. Please try again. If the error recurs, please contact support@mpulsemobile.com.");
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_all_the_fields_of_member_including_identifiers_are_updated_when_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		testReporter.log(LogStatus.INFO, "Update all fields of a member including identifiers ");
		
		String email = RandomeUtility.getRandomEmails(6);
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		xmlBody = "<body>\n" + 
				"  <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <appmemberid>"+appMemberId+"</appmemberid>\n" + 
				"    <email>"+email+"</email>\n" +
				"    <USERAGENT>member api</USERAGENT>\n" + 
				"    <PREFERREDLANGUAGE>spanish</PREFERREDLANGUAGE>\n" + 
				"    <FAX>16758009987</FAX>\n" + 
				"    <BUSINESSphone>16758009987</BUSINESSphone>\n" + 
				"    <landline>19092100078</landline>\n" + 
				"    <ZIPCODE>10023</ZIPCODE>\n" + 
				"    <FIRSTNAME>fjtyf</FIRSTNAME>\n" + 
				"    <GENDER>Male</GENDER>\n" + 
				"    <BIRTHDATE>08/22/2010</BIRTHDATE>\n" + 
				"    <LASTNAME>last</LASTNAME>\n" + 
				"    <COUNTRY>United States</COUNTRY>\n" + 
				"    <STATE>New York</STATE>\n" + 
				"    <COMPANY>Quovantis</COMPANY>\n" + 
				"    <ADDRESS2>Noida</ADDRESS2>\n" + 
				"    <ADDRESS1>sector 3</ADDRESS1>\n" + 
				"    <JOB>Automation</JOB>\n" + 
				"    <CITY>delhi</CITY>\n" + 
				"    <RMENABLED>True</RMENABLED>\n" + 
				"    <CUSTOM_NUMBER>3556</CUSTOM_NUMBER>\n" + 
				"    <SINGLE_LINE>this is single line</SINGLE_LINE>\n" + 
				"    <TIME>05:30pm</TIME>\n" + 
				"    <DATE>07/25/2019</DATE>\n" + 
				"    <MULTI_PICKLIST>hey | bye</MULTI_PICKLIST>\n" + 
				"    <PARAGRAPH>paragraph line 0ne\n" + 
				"line two\n" + 
				"line three</PARAGRAPH>\n" + 
				"    <PICKLIST>two</PICKLIST>\n" + 
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.APPMEMBERID.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CITY.text()").toString(), "delhi");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FAX.text()").toString(), "16758009987");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FIRSTNAME.text()").toString(), "fjtyf");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.BUSINESS.text()").toString(), "16758009987");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.RMENABLED.text()").toString(), "True");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PREFERREDLANGUAGE.text()").toString(), "Spanish");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.GENDER.text()").toString(), "Male");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ADDRESS2.text()").toString(), "Noida");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ZIP.text()").toString(), "10023");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.JOB.text()").toString(), "Automation");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.BIRTHDATE.text()").toString(), "2010-08-22");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.STATE.text()").toString(), "New York");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.COUNTRY.text()").toString(), "United States");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.LASTNAME.text()").toString(), "last");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ADDRESS1.text()").toString(), "sector 3");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.COMPANY.text()").toString(), "Quovantis");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.HOMEPHONE.text()").toString(), "19092100078");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CUSTOM_NUMBER.text()").toString(), "3556");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.SINGLE_LINE.text()").toString(), "this is single line");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.TIME.text()").toString(), "17:30:00");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.DATE.text()").toString(), "2019-07-25");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.MULTI_PICKLIST.text()").toString(), "hey | bye");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PARAGRAPH.text()").toString(), "paragraph line 0ne\n" + 
				"line two\n" + 
				"line three");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PICKLIST.text()").toString(), "two");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appMemberId);
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_both_standard_and_custom_fields_of_a_member_could_be_updated_in_a_single_request_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		testReporter.log(LogStatus.INFO, "Update all fields of a member including identifiers ");
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <ENC_NUMBER>3556</ENC_NUMBER>\n" + 
				"    <CUSTOM_NUMBER>5678</CUSTOM_NUMBER>\n" + 
				"    <SINGLE_LINE>old single line</SINGLE_LINE>\n" +
				"    <LASTNAME>last</LASTNAME>\n" +
				"    <COMPANY>Quovantis</COMPANY>\n" +
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_NUMBER.text()").toString(), "3556");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CUSTOM_NUMBER.text()").toString(), "5678");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.SINGLE_LINE.text()").toString(), "old single line");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.LASTNAME.text()").toString(), "last");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.COMPANY.text()").toString(), "Quovantis");
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_custom_fields_of_a_member_could_be_updated_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <CUSTOM_NUMBER>5678</CUSTOM_NUMBER>\n" + 
				"    <SINGLE_LINE>old single line</SINGLE_LINE>\n" + 
				"    <TIME>08:30pm</TIME>\n" + 
				"    <DATE>07/20/2019</DATE>\n" + 
				"    <MULTI_PICKLIST>bye</MULTI_PICKLIST>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		testReporter.log(LogStatus.INFO, "Update all fields of a member including identifiers ");
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <CUSTOM_NUMBER>3556</CUSTOM_NUMBER>\n" + 
				"    <SINGLE_LINE>this is single line</SINGLE_LINE>\n" + 
				"    <TIME>05:30pm</TIME>\n" + 
				"    <DATE>07/25/2019</DATE>\n" + 
				"    <MULTI_PICKLIST>hey | bye</MULTI_PICKLIST>\n" +
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CUSTOM_NUMBER.text()").toString(), "3556");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.SINGLE_LINE.text()").toString(), "this is single line");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.TIME.text()").toString(), "17:30:00");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.DATE.text()").toString(), "2019-07-25");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.MULTI_PICKLIST.text()").toString(), "hey | bye");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_encrypted_custom_fields_of_a_member_could_be_updated_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <ENC_NUMBER>5678</ENC_NUMBER>\n" + 
				"    <ENC_SINGLE_LINE>old single line</ENC_SINGLE_LINE>\n" + 
				"    <ENC_TIME>08:30pm</ENC_TIME>\n" + 
				"    <ENC_DATE>07/20/2019</ENC_DATE>\n" + 
				"    <ENC_MULTI_PICKLIST>one</ENC_MULTI_PICKLIST>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		testReporter.log(LogStatus.INFO, "Update all fields of a member including identifiers ");
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <ENC_NUMBER>3556</ENC_NUMBER>\n" + 
				"    <ENC_SINGLE_LINE>this is single line</ENC_SINGLE_LINE>\n" + 
				"    <ENC_TIME>05:30pm</ENC_TIME>\n" + 
				"    <ENC_DATE>07/25/2019</ENC_DATE>\n" + 
				"    <ENC_MULTI_PICKLIST>one | three</ENC_MULTI_PICKLIST>\n" +
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_NUMBER.text()").toString(), "3556");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_SINGLE_LINE.text()").toString(), "this is single line");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_TIME.text()").toString(), "17:30:00");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_DATE.text()").toString(), "2019-07-25");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_MULTI_PICKLIST.text()").toString(), "one | three");
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_fields_not_included_in_the_request_are_updated_to_null_when_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Adding member with valid phone number identifier and first & last name " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello</firstname>\n" + 
				"    <lastname>lastme</lastname>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		testReporter.log(LogStatus.INFO, "Updating member without giving lastname in the request ");
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String query = "select * from audience_member where id= "+memberid;
		
		testReporter.log(LogStatus.INFO, "Verifying entry in audience_member table with query " + query);
		Map<String, String> member = mPulseDB.getResultMap(query);
		
		Assert.assertEquals(member.get("last_name"), null);
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_fields_not_included_in_the_request_should_not_update_to_null_when_using_X_Ms_Update_included_fields_only_and_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Adding member with valid phone number identifier and first & last name " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello</firstname>\n" + 
				"    <lastname>lastme</lastname>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		testReporter.log(LogStatus.INFO, "Updating member without giving lastname in the request with included_fields_only header ");
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello23</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Update", "included_fields_only")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.LASTNAME.text()").toString(), "lastme");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FIRSTNAME.text()").toString(), "hello23");
		String query = "select * from audience_member where id= "+memberid;
		
		testReporter.log(LogStatus.INFO, "Verifying entry in audience_member table with query " + query);
		Map<String, String> member = mPulseDB.getResultMap(query);
		
		Assert.assertEquals(member.get("last_name"), "lastme");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_for_invalid_email_id_in_request_body_and_response_code_should_be_400_and_and_error_message_Invalid_parameter_value_when_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String invalid_email = "emailaddress@email";
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+invalid_email+"</email>\n" +
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "EMAIL");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.value.text()").toString(), invalid_email);
	
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_for_invalid_mobilephone_in_request_body_and_response_code_should_be_400_and_and_error_message_Invalid_parameter_value_when_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Adding member with valid email identifier " +email);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String invalidPhoneNumber = "197116578";
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+invalidPhoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" +
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "MOBILEPHONE");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.value.text()").toString(), invalidPhoneNumber);

	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_member_is_unsubscribed_from_email_channel_when_email_is_nulled_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Adding member with valid email id and phone number identifier " +email+ " "+phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" + 
				"    <lastname>lastme</lastname>\n" +
				"  </member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" +
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Force-Subscribe-Member", "true")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		testReporter.log(LogStatus.INFO, "Updating member without including email identifier");
		Thread.sleep(5000);
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello23</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		String query = "select * from audience_member where id= "+memberid;
		
		testReporter.log(LogStatus.INFO, "Verifying entry in audience_member table with query " + query);
		Map<String, String> member = mPulseDB.getResultMap(query);
		testReporter.log(LogStatus.INFO, "Verifying that email id should get nulled in audience_member table");
		Assert.assertEquals(member.get("email"), null);
		testReporter.log(LogStatus.INFO, "Successfully verified - email id found null in audience_member table");
		query = "select status from email_subscription where audience_member_id ="+memberid;
		
		testReporter.log(LogStatus.INFO, "Verifying subscription status from email_subscription table " + query);
		Map<String, String> subscription = mPulseDB.getResultMap(query);
		testReporter.log(LogStatus.INFO, "Verifying that member should get unsubscribed in email_subscription table");
		Assert.assertEquals(subscription.get("status"), "-1");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_member_is_unsubscribed_from_sms_channel_when_mobilephone_is_nulled_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Adding member with valid phone number and email identifier " +phoneNumber+ " "+email);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" + 
				"    <lastname>lastme</lastname>\n" +
				"  </member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" +
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Force-Subscribe-Member", "true")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		testReporter.log(LogStatus.INFO, "Updating member without giving phone number in the request ");
		Thread.sleep(5000);
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" +
				"    <firstname>hello23</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		String query = "select * from audience_member where id= "+memberid;
		
		testReporter.log(LogStatus.INFO, "Verifying entry in audience_member table with query " + query);
		Map<String, String> member = mPulseDB.getResultMap(query);
		
		Assert.assertEquals(member.get("mobile_phone"), null);
		
		query = "select status from sms_subscription where audience_member_id ="+memberid;
		
		testReporter.log(LogStatus.INFO, "Verifying subscription status from sms_subscription table " + query);
		Map<String, String> subscription = mPulseDB.getResultMap(query);
		
		Assert.assertEquals(subscription.get("status"), "-1");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_standard_fields_of_a_member_could_be_updated_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello</firstname>\n" + 
				"    <LASTNAME>last</LASTNAME>\n" +
				"    <COMPANY>Quovantis</COMPANY>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		testReporter.log(LogStatus.INFO, "Update all fields of a member including identifiers ");
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>Krishna</firstname>\n" + 
				"    <LASTNAME>Nandan</LASTNAME>\n" +
				"    <COMPANY>mPulse</COMPANY>\n" +
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FIRSTNAME.text()").toString(), "Krishna");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.LASTNAME.text()").toString(), "Nandan");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.COMPANY.text()").toString(), "mPulse");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_using_all_identifiers_in_a_single_request_and_if_one_or_more_identifiers_are_invalid_and_then_request_marked_as_failed_and_response_code_is_400()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Adding member with valid email identifier " +email);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" +
				"  </member>\n" + 
				"</body>";
		
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String invalidPhoneNumber = "197116578";
		String appmemberid = "testputapp";
		
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+invalidPhoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" +
				"    <appmemberid>"+appmemberid+"</appmemberid>\n" +
				"    <clientmemberid>"+appmemberid+"</clientmemberid>\n" +
				"  </member>\n" + 
				"</body>";
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response body "+body);
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "MOBILEPHONE");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.value.text()").toString(), invalidPhoneNumber);
	}
	
	@Test(groups = { "sanity", "uat", "upsertapi"})
	public void Verify_that_user_should_create_member_by_only_giving_the_valid_phone_number_in_the_request_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with invalid mobile number - 8 digit");
		String phoneNumber = "87956704";
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "MOBILEPHONE");
		
		testReporter.log(LogStatus.INFO, "Trying to add member with invalid mobile number - 12 digit");
		phoneNumber = "998795670467";
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "MOBILEPHONE");
		
		testReporter.log(LogStatus.INFO, "Trying to add member with blank mobile phone");
		phoneNumber = "";
		String email = RandomeUtility.getRandomEmails(4);
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"    <email>"+email+"</email>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "MOBILEPHONE");
		
		testReporter.log(LogStatus.INFO, "Trying to add member with valid mobile phone");
		phoneNumber = RandomeUtility.getRandomPhoneNumber();
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi"})
	public void Verify_that_user_should_create_member_by_only_giving_valid_email_in_the_request_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with invalid email id (having space) - test mail@yopmail.com");
		String email = "test mail@yopmail.com";
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "EMAIL");
		
		testReporter.log(LogStatus.INFO, "Trying to add member with invalid email id - @yopmail.com");
		email = "998795670467";
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "EMAIL");
		
		testReporter.log(LogStatus.INFO, "Trying to add member with blank email id");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		email = "";
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"    <email>"+email+"</email>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "EMAIL");
		
		testReporter.log(LogStatus.INFO, "Trying to add member with valid email id");
		email = RandomeUtility.getRandomEmails(4);
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_user_should_create_new_member_by_giving_client_member_id_with_app_member_id_in_a_request_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with app member id and client member id");
		String clientMemberId = RandomeUtility.getRandomClientMemberID();
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <clientmemberid>"+clientMemberId+"</clientmemberid>\n" +
				"    <appmemberid>"+appMemberId+"</appmemberid>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CLIENTMEMBERID.text()").toString(), clientMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.APPMEMBERID.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appMemberId);
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
		
	}

	@Test(groups = { "sanity", "uat", "upsertapi"})
	public void Verify_that_member_should_not_create_without_giving_any_identifier_using_X_Ms_Audience_Update_header_with_value_upsert() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member without giving any identifier");
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <lastname>lastname</lastname>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "A required field is missing");
		testReporter.log(LogStatus.INFO, "Verified that user should not create member without giving any identifier");
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void verify_that_member_should_create_by_giving_all_valid_identifiers_at_once_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with all valid identifiers given in one request");
		String clientMemberId = RandomeUtility.getRandomClientMemberID();
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" +
				"    <clientmemberid>"+clientMemberId+"</clientmemberid>\n" +
				"    <appmemberid>"+appMemberId+"</appmemberid>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.MOBILEPHONE.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CLIENTMEMBERID.text()").toString(), clientMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.APPMEMBERID.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.EMAIL.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "upsertapi"})
	public void verify_that_member_can_be_created_with_all_standard_field_using_X_Ms_Audience_Update_header_with_value_upsert() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with unique app member id");
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<appmemberid>"+appMemberId+"</appmemberid>\n" + 
				"		<USERAGENT>member api</USERAGENT>\n" + 
				"		<PREFERREDLANGUAGE>spanish</PREFERREDLANGUAGE>\n" + 
				"		<FAX>16758009987</FAX>\n" + 
				"		<BUSINESSphone>16758009987</BUSINESSphone>\n" + 
				"		<landline>19092100078</landline>\n" + 
				"		<ZIPCODE>10023</ZIPCODE>\n" + 
				"		<FIRSTNAME>fjtyf</FIRSTNAME>\n" + 
				"		<GENDER>Male</GENDER>\n" + 
				"		<BIRTHDATE>08/22/2010</BIRTHDATE>\n" + 
				"		<LASTNAME>last</LASTNAME>\n" + 
				"		<COUNTRY>United States</COUNTRY>\n" + 
				"		<STATE>New York</STATE>\n" + 
				"		<COMPANY>Quovantis</COMPANY>\n" + 
				"		<ADDRESS2>Noida</ADDRESS2>\n" + 
				"		<ADDRESS1>sector 3</ADDRESS1>\n" + 
				"		<JOB>Automation</JOB>\n" + 
				"		<CITY>delhi</CITY>\n" + 
				"		<RMENABLED>True</RMENABLED>\n" + 
				"	</member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.APPMEMBERID.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CITY.text()").toString(), "delhi");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FAX.text()").toString(), "16758009987");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FIRSTNAME.text()").toString(), "fjtyf");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.BUSINESS.text()").toString(), "16758009987");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.RMENABLED.text()").toString(), "True");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PREFERREDLANGUAGE.text()").toString(), "Spanish");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.GENDER.text()").toString(), "Male");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ADDRESS2.text()").toString(), "Noida");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ZIP.text()").toString(), "10023");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.JOB.text()").toString(), "Automation");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.BIRTHDATE.text()").toString(), "2010-08-22");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.STATE.text()").toString(), "New York");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.COUNTRY.text()").toString(), "United States");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.LASTNAME.text()").toString(), "last");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ADDRESS1.text()").toString(), "sector 3");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.COMPANY.text()").toString(), "Quovantis");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.HOMEPHONE.text()").toString(), "19092100078");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appMemberId);
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		String query = "select * from audience_member where id= "+memberid;
		
		testReporter.log(LogStatus.INFO, "Verifying entry in audience_member table with query " + query);
		Map<String, String> member = mPulseDB.getResultMap(query);
		System.out.println("Member object in map - " + member);
		
		Assert.assertEquals(member.get("app_member_id"), appMemberId);
		Assert.assertEquals(member.get("first_name"), "fjtyf");
		Assert.assertEquals(member.get("last_name"), "last");
		Assert.assertEquals(member.get("land_line"), "19092100078");
		Assert.assertEquals(member.get("business_phone"), "16758009987");
		Assert.assertEquals(member.get("zip_code"), "10023");
		Assert.assertEquals(member.get("city"), "delhi");
		Assert.assertEquals(member.get("country"), ConfigReader.getConfig("country"));
		Assert.assertEquals(member.get("state"), ConfigReader.getConfig("state"));
		Assert.assertEquals(member.get("fax"), "16758009987");
		Assert.assertEquals(member.get("preferred_language"), ConfigReader.getConfig("preferred_language"));
		Assert.assertEquals(member.get("rm_enabled"), "t");
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void verify_that_member_can_be_created_with_all_types_of_custom_field_using_X_Ms_Audience_Update_header_with_value_upsert() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with all types of custom fields ");
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<appmemberid>"+appMemberId+"</appmemberid>\n" + 
				"		<CUSTOM_NUMBER>3556</CUSTOM_NUMBER>\n" + 
				"		<SINGLE_LINE>this is single line</SINGLE_LINE>\n" + 
				"		<TIME>05:30pm</TIME>\n" + 
				"		<DATE>07/25/2019</DATE>\n" + 
				"		<MULTI_PICKLIST>hey | bye</MULTI_PICKLIST>\n" + 
				"		<PARAGRAPH>paragraph line 0ne\n" + 
				"		line two\n" + 
				"		line three</PARAGRAPH>\n" + 
				"		<PICKLIST>two</PICKLIST>\n" + 
				"	</member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.APPMEMBERID.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CUSTOM_NUMBER.text()").toString(), "3556");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.SINGLE_LINE.text()").toString(), "this is single line");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.TIME.text()").toString(), "17:30:00");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.DATE.text()").toString(), "2019-07-25");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.MULTI_PICKLIST.text()").toString(), "hey | bye");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PARAGRAPH.text()").toString(), "paragraph line 0ne\n" + 
				"		line two\n" + 
				"		line three");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PICKLIST.text()").toString(), "two");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appMemberId);
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void verify_that_member_can_be_created_with_standard_and_custom_field_using_X_Ms_Audience_Update_header_with_value_upsert() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with standard and custom field");
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<appmemberid>"+appMemberId+"</appmemberid>\n" + 
				"		<FIRSTNAME>Auto test</FIRSTNAME>\n" + 
				"		<ADDRESS1>new city noida</ADDRESS1>\n" + 
				"		<GENDER>Male</GENDER>\n" + 
				"		<CUSTOM_NUMBER>3556</CUSTOM_NUMBER>\n" + 
				"		<SINGLE_LINE>this is single line</SINGLE_LINE>\n" + 
				"		<TIME>05:30pm</TIME>\n" + 
				"		<DATE>07/25/2019</DATE>\n" + 
				"		<MULTI_PICKLIST>hey | bye</MULTI_PICKLIST>\n" + 
				"		<PARAGRAPH>paragraph line 0ne\n" + 
				"		line two\n" + 
				"		line three</PARAGRAPH>\n" + 
				"		<PICKLIST>two</PICKLIST>\n" + 
				"	</member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.APPMEMBERID.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.CUSTOM_NUMBER.text()").toString(), "3556");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FIRSTNAME.text()").toString(), "Auto test");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ADDRESS1.text()").toString(), "new city noida");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.GENDER.text()").toString(), "Male");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.SINGLE_LINE.text()").toString(), "this is single line");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.TIME.text()").toString(), "17:30:00");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.DATE.text()").toString(), "2019-07-25");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.MULTI_PICKLIST.text()").toString(), "hey | bye");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PARAGRAPH.text()").toString(), "paragraph line 0ne\n" + 
				"		line two\n" + 
				"		line three");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.PICKLIST.text()").toString(), "two");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appMemberId);
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		String query = "select custom_data from audience_member where id="+memberid;
		String customData = mPulseDB.getResultInString(query);
		
		JSONObject jsonObject = new JSONObject(customData);
		Assert.assertEquals(jsonObject.getString("date1"), "2019-07-25");
		Assert.assertEquals(jsonObject.getString("time1"), "17:30:00");
		Assert.assertEquals(jsonObject.getNumber("number1"), 3556);
		Assert.assertEquals(jsonObject.getNumber("picklist1"), Integer.parseInt(ConfigReader.getConfig("picklistvalue")));
		Assert.assertEquals(jsonObject.getString("singleline1"), "this is single line");
		Assert.assertTrue(jsonObject.getString("paragraph1").contains("paragraph line 0ne"));
		Assert.assertEquals(jsonObject.getJSONArray("multipicklist1").toString(), ConfigReader.getConfig("multipicklistvalue"));
		System.out.println("value is "+jsonObject.getString("date1"));
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void verify_that_member_can_be_created_with_all_7_types_of_encrypted_custom_fields_using_X_Ms_Audience_Update_header_with_value_upsert()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with all 7 types of encrypted custom fields ");
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<appmemberid>"+appMemberId+"</appmemberid>\n" + 
				"		<ENC_NUMBER>3556</ENC_NUMBER>\n" + 
				"		<ENC_SINGLE_LINE>this is single line</ENC_SINGLE_LINE>\n" + 
				"		<ENC_TIME>05:30pm</ENC_TIME>\n" + 
				"		<ENC_DATE>07/25/2019</ENC_DATE>\n" + 
				"		<ENC_MULTI_PICKLIST>one | three</ENC_MULTI_PICKLIST>\n" + 
				"		<ENC_PARAGRAPH>paragraph line 0ne\n" + 
				"		line two\n" + 
				"		line three</ENC_PARAGRAPH>\n" + 
				"		<ENC_PICKLIST>bike</ENC_PICKLIST>\n" + 
				"	</member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.APPMEMBERID.text()").toString(), appMemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_NUMBER.text()").toString(), "3556");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_SINGLE_LINE.text()").toString(), "this is single line");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_TIME.text()").toString(), "17:30:00");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_DATE.text()").toString(), "2019-07-25");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_MULTI_PICKLIST.text()").toString(), "one | three");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_PARAGRAPH.text()").toString(), "paragraph line 0ne\n" + 
				"		line two\n" + 
				"		line three");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.ENC_PICKLIST.text()").toString(), "bike");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appMemberId);
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_for_invalid_email_id_in_the_request_body_the_response_code_should_be_400_and_error_message_Invalid_parameter_value_using_X_Ms_Audience_Update_header_with_value_upsert () throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with invalid email id (having space) - test mail@yopmail.com");
		String email = "test mail@yopmail.com";
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" + 
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "EMAIL");
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_when_using_all_identifiers_in_a_single_request_if_one_or_more_identifiers_are_invalid_then_request_marked_as_failed_and_the_response_code_should_be_400_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		testReporter.log(LogStatus.INFO, "Trying to add member with one invalid identifiers given in request");
		String clientMemberId = RandomeUtility.getRandomClientMemberID();
		String appMemberId = RandomeUtility.getRandomAppMemberID();
		String phoneNumber = RandomeUtility.getRandomPhoneNumber()+"789";
		String email = RandomeUtility.getRandomEmails(6);
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" +
				"    <clientmemberid>"+clientMemberId+"</clientmemberid>\n" +
				"    <appmemberid>"+appMemberId+"</appmemberid>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "invalid_format");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "MOBILEPHONE");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.value.text()").toString(), phoneNumber);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_audience_API_data_writes_on_api_request_log_table_when_using_X_Ms_Audience_Update_header_with_value_upsert() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String query = "select count(id) from api_request_log where api_type = 'AUDIENCE_API' and account_id = "+this.accountId+" "
				+ "and request_method = 'PUT' and request_body ilike '%"+phoneNumber+"%'";
		testReporter.log(LogStatus.INFO, "Verifying entry in api_request_log table with query " + query);
		int requestCount = mPulseDB.getCountInAPIRequestLog(query);
		System.out.println("Request Count is " + requestCount);
		Assert.assertTrue(requestCount >= 1);
		testReporter.log(LogStatus.INFO, "Successfully verified entry in api_request_log table ");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_user_can_update_identifiers_and_profile_field_when_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Updating email identifier for an existing member using X-Ms-Audience-Update header with value upsert , email" +email);
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		testReporter.log(LogStatus.INFO, "Updating first name for an existing member using X-Ms-Audience-Update header with value upsert");
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <firstname>updatedname</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.profile.FIRSTNAME.text()").toString(), "updatedname");
		Assert.assertNotEquals(xmlPath.get("body.members.member.profile.FIRSTNAME.text()").toString(), "hello");
		testReporter.log(LogStatus.INFO, "As expected, First name not updated for an existing member using X-Ms-Ignore-Error-For-Existing-Members true ");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_user_can_update_existing_member_identifier_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Trying to add member with valid phone number and valid email identifier " +phoneNumber+ " email "+email);
		
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		email = RandomeUtility.getRandomEmails(6);
		xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"    <email>"+email+"</email>\n" +
				"    <firstname>hello</firstname>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_member_should_be_added_in_sms_pending_state_in_the_respective_list_and_get_confirm_opt_in_message_when_using_X_Ms_List_Confirm_header_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to add member in sms pending state with phone number " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-List-Confirm", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "PENDING");
		
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.accountId, phoneNumber);
		testReporter.log(LogStatus.INFO, "newly added member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where audience_member_id ='" + memberid + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		
		String query = "select count(id) from mt_tracking where account_id ="+this.accountId+" and audience_member_id = '"+memberid+"' and message_type = 'list_sms_message'";
		int count = mPulseDB.getResultInInteger(query);
		Assert.assertTrue(count == 1);
		testReporter.log(LogStatus.INFO, "Verified that member get Confirm opt-in Message");
		
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "uat", "upsertapi" })
	public void Verify_that_member_should_be_added_in_email_pending_state_in_the_respective_list_and_get_confirm_opt_in_message_when_using_X_Ms_List_Confirm_header_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Trying to add member in email pending state with email "+email);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<email>"+email+"</email>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-List-Confirm", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "PENDING");
		
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		String subStatus = mPulseDB.getEmailSubscriptionStatus(this.accountId, email);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB.getStringData("select status from email_subscription where email ='" + email + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from email_subscription table");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "upsertapi" })
	public void Verify_that_member_should_be_added_and_subscribed_in_sms_channel_in_the_respective_list_and_get_welcome_message_when_using_Send_Welcome_Message_header_in_the_respective_list_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to add member in sms active state with phone number " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("Send-Welcome-Message", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "ACTIVE");
		
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.accountId, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where audience_member_id ='" + memberid + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		String query = "select count(id) from mt_tracking where account_id ="+this.accountId+" and audience_member_id = '"+memberid+"' and message_type = 'sms_welcome_message'";
		int count = mPulseDB.getResultInInteger(query);
		Assert.assertTrue(count == 1);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");

		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "upsertapi" })
	public void Verify_that_member_should_be_added_and_subscribed_in_email_channel_in_the_respective_list_and_get_welcome_message_when_using_Send_Welcome_Message_header_in_the_respective_list_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(6);
		
		testReporter.log(LogStatus.INFO, "Trying to add member in email active state with email "+email);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<email>"+email+"</email>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("Send-Welcome-Message", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "ACTIVE");
		
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		
		String subStatus = mPulseDB.getEmailSubscriptionStatus(this.accountId, email);
		testReporter.log(LogStatus.INFO, "newly added member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB.getStringData("select status from email_subscription where email ='" + email + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from email_subscription table");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "upsertapi" })
	public void Verify_that_member_should_get_subscribed_in_the_respective_list_when_using_Force_Subscribe_Member_header_irrespective_of_there_current_subscription_status_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-List-Confirm", "true")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		MoAPI.hitMoApi(this.sc, phoneNumber, "stop");
		Thread.sleep(8000);
		
		RestAssured.baseURI = baseUrlAudience;
		
		xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Force-Subscribe-Member", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "ACTIVE");
		
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "upsertapi" })
	public void Verify_that_user_can_update_existing_member_subscription_using_Ignore_Error_For_Existing_Members_and_X_Ms_Force_Subscribe_Member_header_at_a_time_in_same_request_using_X_Ms_Audience_Update_header_with_value_upsert (
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to add member with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-List-Confirm", "true")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		
		xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Force-Subscribe-Member", "true")
				.header("X-Ms-Ignore-Error-For-Existing-Members", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "ACTIVE");
		
		String memberid = xmlPath.get("body.members.member.id.memberid.text()").toString();
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "upsertapi"})
	public void Verify_error_response_when_user_try_to_subscribe_member_in_sms_channel_with_phone_number_and_list_not_support_sms_channel() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to subscribe member in a list that not support sms channel with valid phone number identifier " +phoneNumber);
		String listId = ConfigReader.getConfig("emailList_id");
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Force-Subscribe-Member", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "sms_channel_not_supported");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "listid");
	}
	
	@Test(groups = { "sanity", "upsertapi"})
	public void Verify_error_response_when_user_try_to_add_member_with_email_and_list_not_support_email_channel() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(4);
		
		testReporter.log(LogStatus.INFO, "Trying to subscribe member in a list that not support email channel with valid email identifier " +email);
		String listId = ConfigReader.getConfig("moAndAr_id");
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<email>"+email+"</email>\n" + 
				"	</member>\n" + 
				"	<listid>"+listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Force-Subscribe-Member", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Invalid parameter value");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "email_channel_not_supported");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.parameter.text()").toString(), "listid");
	}
	
	@Test(groups = { "sanity", "upsertapi"})
	public void Verify_error_response_when_list_id_missing_and_user_try_to_subscribe_member_in_sms_channel() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Upsert API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		testReporter.log(LogStatus.INFO, "Trying to subscribe member in a list that not support sms channel with valid phone number identifier " +phoneNumber);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Force-Subscribe-Member", "true")
				.header("X-Ms-Audience-Update", "upsert")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response Body is " +body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.xml_errors.xml_error.message.text()").toString(), "A required field is missing");
		Assert.assertEquals(xmlPath.get("body.xml_errors.xml_error.detail.parameter.text()").toString(), "list_id");
	}

}
