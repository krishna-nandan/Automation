package com.mPulse.testCases;

import static io.restassured.RestAssured.expect;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import com.mPulse.factories.BrowserFactory;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.objectRepository.CommunicationPage;
import com.mPulse.utility.AudienceApi;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.MoAPI;
import com.mPulse.utility.RandomeUtility;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class EnglishAndSpanishMTTest extends BaseTest {
	
	private WebDriver driver;
	String baseUrlNCM = "https://"+ConfigReader.getConfig("API_URL")+"/account/"+ConfigReader.getConfig("account_id")+"/non-campaign-message";
	String baseUrlDMA = "https://"+ConfigReader.getConfig("API_URL")+"/account/"+ConfigReader.getConfig("account_id")+"/direct-message";
	String basicAuth = UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password"));

	Logger logger = Logger.getLogger(EnglishAndSpanishMTTest.class);
	
	String accountId = ConfigReader.getConfig("account_id");
	String listId = ConfigReader.getConfig("audienceApiTest_id");
	String sc = ConfigReader.getConfig("audienceApiTest_sc");
	
	@Test(groups = { "sanity", "englishspanishmt"})
	@Parameters({ "browser" })
	public void Verify_sending_MO_with_160_and_more_characters_from_mo_hit_api(String browser)
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("English and Spanish MT");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String longMo = "hey, I am sending MO from my phone to verify mo API accepts more than 160 characters - yufjytfytfhtfgyfjytdhtrgdjghmtfjgtdhgrdjgtdjgdjgdjgfrdcjgfrdjgrdjtrdjtrdjtfjtfjytfhb";
		MoAPI.hitMoApi("57889", phoneNumber, longMo);
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mo_log where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), "57889");
		Assert.assertEquals(mtValue.get("original_message"), longMo);
	}
	
	@Test(groups = { "sanity", "englishspanishmt"})
	@Parameters({ "browser" })
	public void Ensure_user_is_receiving_MT_with_spanish_characters(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("English and Spanish MT");
		logger.info("******** Browser " + browser + " Started ********");
		String spanishText = "À Á Â Ã Ä Ç È É Ê Ë Ì Í Î Ï Ñ Ò Ó Ô Ö Ù Ú Û Ü Ý à á â ã ä æ ç è é ê ë ì í î ï ñ ò ó ô õ ö ù ú û ü ý ÿ";
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName("sms link tracking")
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.selectSpanishTab()
		.entersmsText(spanishText)
		.clickOnAddTriggerButton()
		.clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Last Name")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String randomeString = RandomeUtility.getRandomString();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMemberAndSub(data, ConfigReader.getConfig("smsLinkTrackingList_id"));
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("LASTNAME", randomeString);
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		Thread.sleep(10000);
		Boolean result = mPulseDB.isMessageEntryInMT_Tracking(this.accountId, memberid, campaignName);
		Assert.assertEquals(result.booleanValue(), true);
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where "
				+ "audience_member_id = "+memberid+" and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains(spanishText), "Error: user not received spanish message on profile trigger");
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberById(memberid);
		
	}
	
	@Test(groups = { "sanity", "englishspanishmt", "rerun06aug"})
	@Parameters({ "browser" })
	public void Ensure_Message_API_is_working_with_english_content(String browser)
			throws Exception {
		String englishMessageId = "229965";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("English and Spanish MT");
		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMemberAndSub(data, ConfigReader.getConfig("smsLinkTrackingList_id"));
		String xmlBody = "<body>\n" + 
				"  <message>\n" + 
				"    <shortcode>57889</shortcode>\n" + 
				"    <MESSAGEID>"+englishMessageId+"</MESSAGEID>\n" + 
				"  </message>\n" + 
				"  <member>\n" + 
				"    <memberid>"+memberid+"</memberid>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		RestAssured.baseURI = baseUrlNCM;
		Response r = expect().statusCode(200).given().contentType("application/xml")
				.header("Authorization", this.basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		String body = r.getBody().asString();
		System.out.println(body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), String.valueOf("1"));
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where "
				+ "audience_member_id = "+memberid+" and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("This is English message template ! here goes to your message body."), "Error: user not received non campaign message");
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "englishspanishmt", "rerun06aug"})
	@Parameters({ "browser" })
	public void Ensure_Message_API_is_working_with_dynamic_tag_for_english_content(String browser)
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("English and Spanish MT");
		String englishMessageId = "229967";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String tagValue = "successful";
		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMemberAndSub(data, ConfigReader.getConfig("smsLinkTrackingList_id"));
		String xmlBody = "<body>\n" + 
				"  <message>\n" + 
				"    <shortcode>57889</shortcode>\n" + 
				"    <MESSAGEID>"+englishMessageId+"</MESSAGEID>\n" + 
				"  </message>\n" + 
				"  <member>\n" + 
				"    <memberid>"+memberid+"</memberid>\n" + 
				"    <TEST>"+tagValue+"</TEST>\n" +
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		RestAssured.baseURI = baseUrlNCM;
		Response r = expect().statusCode(200).given().contentType("application/xml")
				.header("Authorization", this.basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		String body = r.getBody().asString();
		System.out.println(body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), String.valueOf("1"));
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where "
				+ "audience_member_id = "+memberid+" and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("This is english message having tag test. tag value - "+tagValue+"."), "Error: user not received non campaign message");
		AudienceApi.deleteMemberById(memberid);
	}
	
	@Test(groups = { "sanity", "englishspanishmt", "rerun06aug"})
	@Parameters({ "browser" })
	public void Ensure_Message_API_with_spanish_content_is_working(String browser)
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("English and Spanish MT");
		String spanishMessageId = "229966";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMemberAndSub(data, ConfigReader.getConfig("smsLinkTrackingList_id"));
		String xmlBody = "<body>\n" + 
				"  <message>\n" + 
				"    <shortcode>57889</shortcode>\n" + 
				"    <MESSAGEID>"+spanishMessageId+"</MESSAGEID>\n" + 
				"  </message>\n" + 
				"  <member>\n" + 
				"    <memberid>"+memberid+"</memberid>\n" + 
				"  </member>\n" + 
				"</body>";
		System.out.println("XML Body is " +xmlBody);
		RestAssured.baseURI = baseUrlNCM;
		Response r = expect().statusCode(200).given().contentType("application/xml")
				.header("Authorization", this.basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		String body = r.getBody().asString();
		System.out.println(body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), String.valueOf("1"));
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where "
				+ "audience_member_id = "+memberid+" and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("À Á Â Ã Ä Ç È É Ê Ë Ì\nÍ Î Ï Ñ Ò Ó Ô Ö Ù Ú Û \nÜ Ý à á â ã ä æ ç è é\nê ë ì í î ï ñ ò ó ô õ\nö ù ú û ü ý ÿ\nmore then 250 À Á Â Ã Ä Ç È É Ê Ë Ì Í Î Ï Ñ Ò Ó Ô Ö Ù Ú Û Ü Ý à á â ã ä æ ç è é ê ë ì í î ï ñ ò ó ô õ ö ù ú û ü ý ÿ À Á Â Ã Ä Ç È É Ê Ë Ì Í Î Ï Ñ Ò Ó Ô Ö Ù Ú Û Ü Ý à á â ã ä æ ç è é ê ë ì í î ï ñ ò ó ô õ ö ù ú û ü ý ÿÀ Á Â Ã Ä Ç È É Ê Ë Ì Í Î Ï Ñ Ò Ó Ô Ö Ù Ú Û Ü Ý à á â ã ä æ ç è é ê ë ì í î ï ñ ò ó ô õ ö ù ú û ü ý ÿÀ Á Â Ã Ä Ç È É Ê Ë Ì Í Î Ï Ñ"), "Error: user not received non campaign message");
		AudienceApi.deleteMemberById(memberid);

	}
	
	@Test(groups = { "sanity", "englishspanishmt", "rerun06aug"})
	@Parameters({ "browser" })
	public void Ensure_Direct_message_API_is_working(String browser)
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Api test case started");
		testReporter.assignCategory("English and Spanish MT");
		String message = "this is Direct message api test message !";
		String listId = ConfigReader.getConfig("smsLinkTrackingList_id");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		String memberid = AudienceApi.createNewMemberAndSub(data, listId);
		String xmlBody = "<body>\n" + 
				"	<message>\n" + 
				"		<messagecontent>"+message+"</messagecontent>\n" + 
				"		<listid>"+listId+"</listid>\n" + 
				"		<tags>mymessage, krishna</tags>\n" + 
				"		<requested_by>krishna</requested_by>\n" + 
				"   </message>\n" + 
				"   <member>\n" + 
				"   		<memberid>"+memberid+"</memberid>\n" + 
				"   </member>\n" + 
				"</body>";
		RestAssured.baseURI = baseUrlDMA;
		Response r = expect().statusCode(200).given().contentType("application/xml")
				.header("Authorization", this.basicAuth)
				.header("X-Ms-Source", "api")
				.header("X-Ms-Format", "xml").body(xmlBody).log().all().when().post();
		Assert.assertEquals(r.statusCode(), 200);
		String body = r.getBody().asString();
		System.out.println(body);
		XmlPath xmlPath = new XmlPath(body);
		Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), String.valueOf("1"));
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where "
				+ "audience_member_id = "+memberid+" and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains(message), "Error: user not received non campaign message");
		AudienceApi.deleteMemberById(memberid);
	}
}
