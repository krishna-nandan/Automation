package com.mPulse.testCases;

import static io.restassured.RestAssured.expect;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.utility.AudienceApi;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.RandomeUtility;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class AudienceUnSubAPITest extends BaseTest {
	
	String baseUrlAudience = "https://" + ConfigReader.getConfig("API_URL") + "/accounts/"
			+ ConfigReader.getConfig("account_id") + "/members";
	String basicAuth = UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"),
			ConfigReader.getConfig("api_password"));

	String listId = ConfigReader.getConfig("audienceApiTest_id");
	String accountId = ConfigReader.getConfig("account_id");
	String sc = ConfigReader.getConfig("audienceApiTest_sc");
	Logger logger = Logger.getLogger(AudienceUnSubAPITest.class);
	
	@Test(groups = { "sanity", "unsubapi" })
	public void Verify_that_user_can_unsubscribe_existing_subscribed_member_in_sms_channel_using_X_Ms_Audience_Update_header_with_value_unsub() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience UnSub API");
		RestAssured.baseURI = baseUrlAudience;

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Map<String, String> member = new HashMap<String, String>();
		member.put("mobilephone", phoneNumber);
		
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);
		
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
				.header("X-Ms-Audience-Update", "unsub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from sms_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and mobile_phone='"+phoneNumber+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "-1");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "unsubapi" })
	public void Verify_that_user_can_unsubscribe_existing_subscribed_member_in_email_channel_using_X_Ms_Audience_Update_header_with_value_unsub() throws Exception{
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience UnSub API");
		RestAssured.baseURI = baseUrlAudience;

		String email = RandomeUtility.getRandomEmails(6);
		Map<String, String> member = new HashMap<String, String>();
		member.put("email", email);
		
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);
		
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
				.header("X-Ms-Audience-Update", "unsub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from email_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and email='"+email+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "-1");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "unsubapi" })
	public void Verify_that_user_can_unsubscribe_existing_subscribed_member_in_appmail_channel_using_X_Ms_Audience_Update_header_with_value_unsub() throws Exception{
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience UnSub API");
		RestAssured.baseURI = baseUrlAudience;

		String appmemberid = RandomeUtility.getRandomAppMemberID();
		Map<String, String> member = new HashMap<String, String>();
		member.put("appmemberid", appmemberid);
		
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);
		
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
				.header("X-Ms-Audience-Update", "unsub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appmemberid);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.appmemberid.status.text()").toString(), "UNSUBSCRIBED");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from appmail_list_subscriber where audience_member_id ="+memberid+" and list_id="+this.listId;
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "-1");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "unsubapi" })
	public void Verify_that_user_can_unsubscribe_existing_subscribed_member_in_all_channels_in_single_request_using_X_Ms_Audience_Update_header_with_value_unsub() throws Exception{
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience UnSub API");
		RestAssured.baseURI = baseUrlAudience;

		String appmemberid = RandomeUtility.getRandomAppMemberID();
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		Map<String, String> member = new HashMap<String, String>();
		member.put("appmemberid", appmemberid);
		member.put("email", email);
		member.put("mobilephone", phoneNumber);
		
		String memberid = AudienceApi.createNewMemberAndSub(member, this.listId);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<appmemberid>"+appmemberid+"</appmemberid>\n" + 
				"		<email>"+email+"</email>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "unsub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 200);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("******* Response body is - "+body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.members.member.id.appmemberid.text()").toString(), appmemberid);
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.appmemberid.status.text()").toString(), "UNSUBSCRIBED");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "INACTIVE");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "success");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "unsubapi" })
	public void Verify_that_user_can_not_unsubscribe_existing_pending_member_in_sms_and_email_channel_using_X_Ms_Audience_Update_header_with_value_unsub() throws Exception{
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("Audience UnSub API");
		RestAssured.baseURI = baseUrlAudience;

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		Map<String, String> member = new HashMap<String, String>();
		member.put("mobilephone", phoneNumber);
		member.put("email", email);
		
		String memberid = AudienceApi.addMemberInPending(member, this.listId);
		
		String xmlBody = "<body>\n" + 
				"	<member>\n" + 
				"		<mobilephone>"+phoneNumber+"</mobilephone>\n" + 
				"		<email>"+email+"</email>\n" + 
				"	</member>\n" + 
				"	<listid>"+this.listId+"</listid>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		Response r = expect().given().contentType("application/xml")
				.header("Authorization", basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml")
				.header("X-Ms-Audience-Update", "unsub").body(xmlBody).log().all().when().put();
		Assert.assertEquals(r.statusCode(), 400);
		testReporter.log(LogStatus.INFO, "Response code "+r.statusCode());
		String body = r.getBody().asString();
		System.out.println("******* Response body is - "+body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "0");
		Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
		Assert.assertEquals(xmlPath.get("body.members.member.id.mobilephone.text()").toString(), phoneNumber);
		Assert.assertEquals(xmlPath.get("body.members.member.id.email.text()").toString(), email);
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.sms.status.text()").toString(), "PENDING");
		Assert.assertEquals(xmlPath.get("body.members.member.subscriptions.email.status.text()").toString(), "PENDING");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[0].message.text()").toString(), "Cannot unsubscribe this member from the email channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[1].message.text()").toString(), "Cannot unsubscribe member from the sms channel of the list");
		Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error[0].detail.reason.text()").toString(),"subscription_not_active");
		Assert.assertEquals(xmlPath.get("body.members.member.result.text()").toString(), "failure");
		Assert.assertEquals(xmlPath.get("body.members.member.id.memberid.text()").toString(), memberid);
		String query = "select * from sms_subscription where audience_member_id ="+memberid+" and list_id="+this.listId+" and mobile_phone='"+phoneNumber+"'";
		Map<String, String> sms_sub = mPulseDB.getResultMap(query);		
		Assert.assertEquals(sms_sub.get("status"), "0");
		Assert.assertEquals(sms_sub.get("account_id"), this.accountId);
		AudienceApi.deleteMemberById(memberid);
	}
}
