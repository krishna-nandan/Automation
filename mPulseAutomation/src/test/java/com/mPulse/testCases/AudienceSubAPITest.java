package com.mPulse.testCases;

import static io.restassured.RestAssured.expect;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

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

public class AudienceSubAPITest extends BaseTest {
	String baseUrlAudience = "https://" + ConfigReader.getConfig("API_URL") + "/accounts/"
			+ ConfigReader.getConfig("account_id") + "/members";
	String basicAuth = UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"),
			ConfigReader.getConfig("api_password"));

	String listId = ConfigReader.getConfig("audienceApiTest_id");
	String accountId = ConfigReader.getConfig("account_id");
	String sc = ConfigReader.getConfig("audienceApiTest_sc");
	Logger logger = Logger.getLogger(AudienceSubAPITest.class);

	@Test(groups = { "sanity", "subapi" })
	public void Verify_API_request_fails_if_no_auth_credentials_are_given_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		String xmlBody = "<body>\n" + "   <member>\n" + "    <mobilephone>" + phoneNumber + "</mobilephone>\n"
				+ "    <firstname>hello</firstname>\n" + "  </member>\n" + "</body>";
		System.out.println("XML Body is " + xmlBody);
		RestAssured.baseURI = baseUrlAudience;
		Response r = expect().given().contentType("application/xml").header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when()
				.put();
		System.out.println("The status code is " + r.statusCode());
		Assert.assertEquals(r.statusCode(), 401);
		String body = r.getBody().asString();
		System.out.println(body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.error.text()").toString(),
				"The request has not been applied because it lacks valid authentication credentials for the target resource");
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_API_request_should_only_accessible_through_valid_basic_authorization_for_not_valid_authorization_response_code_should_be_403()
			throws Exception {
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		String xmlBody = "<body>\n" + "   <member>\n" + "    <mobilephone>" + phoneNumber + "</mobilephone>\n"
				+ "    <firstname>hello</firstname>\n" + "  </member>\n" + "</body>";
		System.out.println("XML Body is " + xmlBody);
		RestAssured.baseURI = baseUrlAudience;
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", UtilityMethods.getBasicAuth("screwed", "data")).header("X-Ms-Source", "api")
				.header("X-Ms-Audience-Update", "sub").header("X-Ms-Format", "xml").body(xmlBody).log().all().when()
				.put();
		System.out.println("The status code is " + r.statusCode());
		Assert.assertEquals(r.statusCode(), 403);
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(body,
				"Something went wrong. Please try again. If the error recurs, please contact support@mpulsemobile.com.");
	}

	@Test(groups = { "sanity", "subapi"})
	public void Verify_that_user_can_subscribe_existing_member_in_new_list_in_sms_channel_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <mobilephone>"+phoneNumber+"</mobilephone>\n" +
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
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from sms_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and mobile_phone='"+phoneNumber+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "1");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_user_can_subscribe_existing_member_in_new_list_in_email_channel_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(6);
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <email>"+email+"</email>\n" + 
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
		
		xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"    <email>"+email+"</email>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from email_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and email='"+email+"'";
		Map<String, String> email_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(email_sub.get("status"), "1");
		Assert.assertEquals(email_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_user_can_subscribe_existing_member_in_new_list_in_appmail_channel_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;
		
		String appmemberId = RandomeUtility.getRandomAppMemberID();
		String xmlBody = "<body>\n" + 
				"   <member>\n" + 
				"    <appmemberid>"+appmemberId+"</appmemberid>\n" + 
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
		
		xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"    <appmemberid>"+appmemberId+"</appmemberid>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appmemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.appmemberid.status.text()").toString(), "SUBSCRIBED");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from appmail_list_subscriber where audience_member_id ="+memberid+" and list_id="+this.listId;
		Map<String, String> appmail_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(appmail_sub.get("status"), "1");
		Assert.assertEquals(appmail_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_user_can_subscribe_existing_member_in_all_channel_in_one_request_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		String appmemberId = RandomeUtility.getRandomAppMemberID();
		String xmlBody = "<body>\n" + 
				"  <member>\n" + 
				"  	<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"  	<email>"+email+"</email>\n" + 
				"	<appmemberid>"+appmemberId+"</appmemberid>\n" + 
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
		
		xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"  	<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"  	<email>"+email+"</email>\n" + 
				"	<appmemberid>"+appmemberId+"</appmemberid>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		body = r.getBody().asString();
		xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appmemberId);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.appmemberid.status.text()").toString(), "SUBSCRIBED");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from appmail_list_subscriber where audience_member_id ="+memberid+" and list_id="+this.listId;
		Map<String, String> appmail_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(appmail_sub.get("status"), "1");
		Assert.assertEquals(appmail_sub.get("account_id"), this.accountId);
		query = "select * from email_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and email='"+email+"'";
		Map<String, String> email_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(email_sub.get("status"), "1");
		Assert.assertEquals(email_sub.get("account_id"), this.accountId);
		query = "select * from sms_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and mobile_phone='"+phoneNumber+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "1");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_user_can_not_re_subscribe_already_unsubscribed_member_in_a_list_in_sms_channel_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Map<String, String> member = new HashMap<String, String>();
		member.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);
		
		AudienceApi.unSubMember(member, this.listId);
		
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
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Cannot subscribe member to the sms channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "unsubscribed_sms");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from sms_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and mobile_phone='"+phoneNumber+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "-1");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_user_can_not_re_subscribe_already_unsubscribed_member_in_a_list_in_email_channel_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String email = RandomeUtility.getRandomEmails(6);
		Map<String, String> member = new HashMap<String, String>();
		member.put("email", email);
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);
		
		AudienceApi.unSubMember(member, this.listId);
		
		
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
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Cannot subscribe member to the email channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "unsubscribed_email");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from email_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and email='"+email+"'";
		Map<String, String> email_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(email_sub.get("status"), "-1");
		Assert.assertEquals(email_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_user_can_not_re_subscribe_already_unsubscribed_member_in_a_list_in_appmail_channel_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String appmemberid = RandomeUtility.getRandomAppMemberID();
		Map<String, String> member = new HashMap<String, String>();
		member.put("appmemberid", appmemberid);
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);
		
		AudienceApi.unSubMember(member, this.listId);
		
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<appmemberid>"+appmemberid+"</appmemberid>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appmemberid);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.appmemberid.status.text()").toString(), "UNSUBSCRIBED");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.message.text()").toString(), "Cannot subscribe member to the Secure Messaging channel of the list.");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "unsubscribed_appmemberid");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from appmail_list_subscriber where audience_member_id ="+memberid+" and list_id="+this.listId;
		Map<String, String> appmail_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(appmail_sub.get("status"), "-1");
		Assert.assertEquals(appmail_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_user_get_error_message_if_trying_to_subscribe_existing_subscribed_member_in_same_list_using_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String appmemberid = RandomeUtility.getRandomAppMemberID();
		Map<String, String> member = new HashMap<String, String>();
		member.put("appmemberid", appmemberid);
		member.put("email", email);
		member.put("mobilephone", phoneNumber);
		
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);		
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<email>"+email+"</email>\n" +
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"		<appmemberid>"+appmemberid+"</appmemberid>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appmemberid);
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.appmemberid.status.text()").toString(), "SUBSCRIBED");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[0].message.text()").toString(), "Cannot subscribe member to the email channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[0].detail.reason.text()").toString(), "already_subscribed");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[1].message.text()").toString(), "Cannot subscribe member to the sms channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[2].message.text()").toString(), "Cannot subscribe member to the Secure Messaging channel of the list.  The member is already subscribed to the list");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_member_must_get_in_pending_status_for_sms_channel_if_using_X_Ms_List_Confirm_true_and_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Map<String, String> member = new HashMap<String, String>();
		member.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMember(member);
		
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
				.header("X-Ms-Format", "xml")
				.header("X-Ms-List-Confirm", "true")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "PENDING");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from sms_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and mobile_phone='"+phoneNumber+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "0");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_member_must_get_in_pending_status_for_email_channel_if_using_X_Ms_List_Confirm_true_and_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String email = RandomeUtility.getRandomEmails(6);
		Map<String, String> member = new HashMap<String, String>();
		member.put("email", email);
		String memberid = AudienceApi.createNewMember(member);		
		
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
				.header("X-Ms-Format", "xml")
				.header("X-Ms-List-Confirm", "true")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "PENDING");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from email_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and email='"+email+"'";
		Map<String, String> email_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(email_sub.get("status"), "0");
		Assert.assertEquals(email_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_X_Ms_Force_Subscribe_Member_header_should_not_force_subscribe_alredy_unsubscribed_member_in_list()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;
		
		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String appmemberid = RandomeUtility.getRandomAppMemberID();
		Map<String, String> member = new HashMap<String, String>();
		member.put("appmemberid", appmemberid);
		member.put("email", email);
		member.put("mobilephone", phoneNumber);
		
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);		
		
		AudienceApi.unSubMember(member, this.listId);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<email>"+email+"</email>\n" +
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" +
				"		<appmemberid>"+appmemberid+"</appmemberid>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "sub")
				.header("X-Ms-Force-Subscribe-Member", "true").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("Response body "+body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appmemberid);
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.appmemberid.status.text()").toString(), "UNSUBSCRIBED");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[0].message.text()").toString(), "Cannot subscribe member to the email channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[0].detail.reason.text()").toString(), "unsubscribed_email");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[1].message.text()").toString(), "Cannot subscribe member to the sms channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[2].message.text()").toString(), "Cannot subscribe member to the Secure Messaging channel of the list.");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[2].detail.reason.text()").toString(), "unsubscribed_appmemberid");
		
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_member_must_get_welcome_message_for_sms_channel_if_using_Send_Welcome_Message_true_and_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Map<String, String> member = new HashMap<String, String>();
		member.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMember(member);
		
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
				.header("X-Ms-Format", "xml")
				.header("Send-Welcome-Message", "true")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from sms_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and mobile_phone='"+phoneNumber+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "1");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

	@Test(groups = { "sanity", "subapi" })
	public void Verify_that_member_must_get_welcome_message_for_email_channel_if_using_Send_Welcome_Message_true_and_X_Ms_Audience_Update_header_with_value_sub()
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience Sub API");
		RestAssured.baseURI = baseUrlAudience;

		String email = RandomeUtility.getRandomEmails(6);
		Map<String, String> member = new HashMap<String, String>();
		member.put("email", email);
		String memberid = AudienceApi.createNewMember(member);		
		
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
				.header("X-Ms-Format", "xml")
				.header("Send-Welcome-Message", "true")
				.header("X-Ms-Audience-Update", "sub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "ACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from email_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and email='"+email+"'";
		Map<String, String> email_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(email_sub.get("status"), "1");
		Assert.assertEquals(email_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}

}
