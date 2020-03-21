package com.mPulse.testCases;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mPulse.factories.BrowserFactory;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.objectRepository.AudienceSegmentsPage;
import com.mPulse.objectRepository.CommunicationPage;
import com.mPulse.utility.AudienceApi;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.MoAPI;
import com.mPulse.utility.RandomeUtility;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MOandARsTest extends BaseTest {

	private WebDriver driver;
	AudienceSegmentsPage audienceSegment;
	String memberid;
	
	Logger logger = Logger.getLogger(MOandARsTest.class);
	String account_id = ConfigReader.getConfig("account_id");
	String listId = ConfigReader.getConfig("moAndAr_id");
	String shortCode = ConfigReader.getConfig("moAndAr_sc");
	String listName = ConfigReader.getConfig("moAndAr_name");
	String listkeyword = ConfigReader.getConfig("moAndAr_keyword");

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_send_help_MO_and_user_receive_shortcode_help_AR(String browser) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "help");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "shortcode_message");
		Assert.assertEquals(mtValue.get("message_id"), "7417");
	}
	
	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_send_help_MO_after_subscription_in_list_and_user_receive_list_help_AR(String browser) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "help");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "list_sms_message");
		Assert.assertEquals(mtValue.get("message_id"), "225486");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_receive_MT_on_profile_update_by_OM_if_have_profile_trigger(String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName("mo and ar")
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is profile trigger message, profile updated from MO !")
		.clickOnAddTriggerButton()
		.clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address2")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword+" Address2 newvalue");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "sms_message");
		//Assert.assertEquals(mtValue.get("message_id"), "225486");	
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_send_list_keyword_and_user_receive_confirmed_opt_in_AR(String browser) throws Exception { 
		
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "list_sms_message");
		Assert.assertEquals(mtValue.get("message_id"), "225483");
		Thread.sleep(5000);
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where original_id = "
				+ mtValue.get("id");
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("Reply YES to continue."), "user not received confirmed message containg text 'Reply YES to continue.'");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_send_yes_user_gets_subscribed_and_receive_welcome_AR(String browser) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "list_sms_message");
		Assert.assertEquals(mtValue.get("message_id"), "225481");
		Thread.sleep(5000);
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where original_id = "
				+ mtValue.get("id");
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("Thanks for joining our mo and ar!"), "user not received welcome message containg text 'Thanks for joining our mo and ar!'");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_recieved_stopall_AR_and_gets_unsubscribed_from_all_lists_and_added_to_GPRL(
			String browser) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "EVENTTEST");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "STOP ALL");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "shortcode_message");
		Assert.assertEquals(mtValue.get("message_id"), "7413");
		Thread.sleep(5000);
		String query = "select count(*) from sms_subscription where account_id = "+ this.account_id +" and status in (1, 0) and mobile_phone = '"+ phoneNumber +"'";
		int status= mPulseDB.getResultInInteger(query);
		Assert.assertEquals(String.valueOf(status), "0");
		HashMap<String, String> blockstatus= (HashMap<String, String>) mPulseDB.getResultMap("select * from shortcode_blocked_phone_number where phone_number = '"+phoneNumber+"'");
		Assert.assertEquals(blockstatus.get("shortcode_id"), "1");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_recieved_stop_AR_of_list_and_gets_unsubscribed_from_that_list_from_which_he_or_she_recieved_last_mt(
			String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "EVENTTEST");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(5000);
		
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName("mo and ar")
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is profile trigger message, profile updated from MO and unsub from last mt message !")
		.clickOnAddTriggerButton()
		.clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address2")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "TESTMOAR Address2 newvalue");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "sms_message");
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "stop");
		Thread.sleep(8000);
		mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "list_sms_message");
		Assert.assertEquals(mtValue.get("message_id"), "225489");
		Thread.sleep(5000);
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where original_id = "
				+ mtValue.get("id");
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("TESTMOAR Alerts: You have been removed from the service"), 
				"user not received last mt stop message containg text 'TESTMOAR Alerts: You have been removed from the service'");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
		
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_gets_spanish_MO_on_sending_spanish_Help_MO_keyword_AYUDA(String browser) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "Yes");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "AYUDA");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "list_sms_message");
		Assert.assertEquals(mtValue.get("message_id"), "225486");
		Thread.sleep(5000);
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where original_id = "
				+ mtValue.get("id");
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("Spanish message help -"), "user not received spanish help message containg text 'Spanish message help -'");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_get_Opt_in_message_by_sending_spanish_keyword_SI(String browser) throws Exception { 
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "SPTESTMOAR");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "SI");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "list_sms_message");
		Assert.assertEquals(mtValue.get("message_id"), "225481");
		Thread.sleep(5000);
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where original_id = "
				+ mtValue.get("id");
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("Spanish welcome message -"), "user not received spanish welcome message containg text 'Spanish welcome message -'");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "moandar"})
	@Parameters({ "browser" })
	public void verify_that_user_get_Opt_out_message_by_sending_spanish_keyword_ALTO_from_which_he_or_she_recieved_last_mt(String browser) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("MO and ARs");
		logger.info("******** Browser " + browser + " Started ********");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "SPTESTMOAR");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "SI");
		Thread.sleep(5000);
		MoAPI.hitMoApi(this.shortCode, phoneNumber, "ALTO");
		Thread.sleep(8000);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap("select * from mt_tracking where phone_number = '"+phoneNumber+"' order by id desc limit 1");
		Assert.assertEquals(mtValue.get("shortcode"), this.shortCode);
		Assert.assertEquals(mtValue.get("message_type"), "list_sms_message");
		Assert.assertEquals(mtValue.get("message_id"), "225489");
		Thread.sleep(5000);
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where original_id = "
				+ mtValue.get("id");
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("Spanish stop message -"), "user not received spanish stop message containg text 'Spanish stop message -'");
		String statusQuery = "select count(*) from sms_subscription where account_id = "+ ConfigReader.getConfig("account_id") +" and status in (1, 0) and mobile_phone = '"+ phoneNumber +"'";
		int status= mPulseDB.getResultInInteger(statusQuery);
		Assert.assertEquals(String.valueOf(status), "0");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

}
