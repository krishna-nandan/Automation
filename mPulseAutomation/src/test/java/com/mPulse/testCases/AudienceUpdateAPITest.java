package com.mPulse.testCases;

import static io.restassured.RestAssured.expect;

import java.util.Map;

import org.apache.log4j.Logger;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.utility.AudienceApi;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.RandomeUtility;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AudienceUpdateAPITest extends BaseTest {
	
	String baseUrlAudience = "https://" + ConfigReader.getConfig("API_URL") + "/accounts/"
			+ ConfigReader.getConfig("account_id") + "/members";
	String basicAuth = UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"),
			ConfigReader.getConfig("api_password"));
	
	String listId = ConfigReader.getConfig("audienceApiTest_id");
	String accountId = ConfigReader.getConfig("account_id");
	Logger logger = Logger.getLogger(AudienceUpdateAPITest.class);
	
	@Test(groups = { "sanity", "uat", "updateapi", "testapi" })
	public void Verify_API_request_fails_if_no_auth_credentials_are_given() throws Exception {
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update").body(xmlBody).log().all().when().put();
		System.out.println("The status code is "+ r.statusCode());
		Assert.assertEquals(r.statusCode(), 401);
		String body = r.getBody().asString();
		System.out.println(body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.error.text()").toString(), "The request has not been applied because it lacks valid authentication credentials for the target resource");
	}

	@Test(groups = { "sanity", "uat", "updateapi"})
	public void Verify_API_request_should_only_accessible_through_valid_basic_authorization_for_not_valid_authorization_response_code_should_be_403()
			throws Exception {
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().put();
		System.out.println("The status code is "+ r.statusCode());
		Assert.assertEquals(r.statusCode(), 403);
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(body, "Something went wrong. Please try again. If the error recurs, please contact support@mpulsemobile.com.");
	}

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_all_the_fields_of_member_including_identifiers_are_updated_when_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_fields_not_included_in_the_request_are_updated_to_null_when_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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
	
	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_fields_not_included_in_the_request_should_not_update_to_null_when_using_X_Ms_Update_included_fields_only_header(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi", "unsubtestupdate" })
	public void Verify_that_member_is_unsubscribed_from_sms_channel_when_mobilephone_is_nulled_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi", "unsubtestupdate" })
	public void Verify_that_member_is_unsubscribed_from_email_channel_when_email_is_nulled_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_standard_fields_of_a_member_could_be_updated_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_custom_fields_of_a_member_could_be_updated_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_encrypted_custom_fields_of_a_member_could_be_updated_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_both_standard_and_custom_fields_of_a_member_could_be_updated_in_a_single_request_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_for_invalid_email_id_in_request_body_and_response_code_should_be_400_and_and_error_message_Invalid_parameter_value_when_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_for_invalid_mobilephone_in_request_body_and_response_code_should_be_400_and_and_error_message_Invalid_parameter_value_when_using_X_Ms_Update_header_with_value_update(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

	@Test(groups = { "sanity", "updateapi" })
	public void Verify_that_using_all_identifiers_in_a_single_request_and_if_one_or_more_identifiers_are_invalid_and_then_request_marked_as_failed_and_response_code_is_400(
			) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Update API");
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
				.header("X-Ms-Audience-Update", "update")
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

}
