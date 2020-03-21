package com.mPulse.testCases;

import static io.restassured.RestAssured.expect;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import com.mPulse.factories.BrowserFactory;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.objectRepository.AudienceSegmentsPage;
import com.mPulse.objectRepository.CommunicationPage;
import com.mPulse.objectRepository.EventDefinitionsPage;
import com.mPulse.utility.AudienceApi;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.EventUploadApi;
import com.mPulse.utility.MoAPI;
import com.mPulse.utility.RandomeUtility;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class EventUploadAPI_V2Test extends BaseTest {

	private WebDriver driver;
	AudienceSegmentsPage audienceSegment;
	String segmentName;
	String memberid;
	public String account_id = ConfigReader.getConfig("account_id");
	String listId = ConfigReader.getConfig("eventApiList_id");
	String shortCode = ConfigReader.getConfig("eventApiList_sc");
	String listName = ConfigReader.getConfig("eventApiList_name");
	Logger logger = Logger.getLogger(EventUploadAPI_V2Test.class);
	String listkeyword = ConfigReader.getConfig("eventApiList_keyword");

	@Test(groups = { "event" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_create_new_event_with_attributes_having_default_values(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickToAddEventAttribute()
				.enterAttributeName("string_attri").clickOnAttributeRequiredCheckBox()
				.enterAttributeDefaultValue("this is default value").clickToAddEventAttribute()
				.enterAttributeName("number_attri").clickOnAttributeRequiredCheckBox().selectAttributeType("number")
				.enterAttributeDefaultValue("578").clickToAddEventAttribute().enterAttributeName("datetime_attri")
				.clickOnAttributeRequiredCheckBox().selectAttributeType("datetime")
				.enterAttributeDefaultValue("2019-10-01").clickOnCreateEventButton();

		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		Map<String, String> stringAttri = mPulseDB.getResultMap(
				"select * from event_attribute where event_id in (select id from event_definition where account_id = "+this.account_id+" and event_name='"
						+ eventName + "') and attribute_name = 'string_attri'");
		Assert.assertEquals(stringAttri.get("data_type"), "0");
		Assert.assertEquals(stringAttri.get("required"), "t");
		Assert.assertEquals(stringAttri.get("default_value"), "this is default value");
		Map<String, String> numAttri = mPulseDB.getResultMap(
				"select * from event_attribute where event_id in (select id from event_definition where account_id = "+this.account_id+" and event_name='"
						+ eventName + "') and attribute_name = 'number_attri'");
		Assert.assertEquals(numAttri.get("data_type"), "1");
		Assert.assertEquals(numAttri.get("required"), "t");
		Assert.assertEquals(numAttri.get("default_value"), "578");
		Map<String, String> dateAttri = mPulseDB.getResultMap(
				"select * from event_attribute where event_id in (select id from event_definition where account_id = "+this.account_id+" and event_name='"
						+ eventName + "') and attribute_name = 'datetime_attri'");
		Assert.assertEquals(dateAttri.get("data_type"), "2");
		Assert.assertEquals(dateAttri.get("required"), "t");
		Assert.assertEquals(dateAttri.get("default_value"), "2019-10-01");
		eventPage.deleteEventByName(eventName);
	}

	@Test(groups = { "event" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_create_new_event_without_attributes(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickOnCreateEventButton();

		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		Map<String, String> stringAttri = mPulseDB.getResultMap(
				"select count(*) from event_attribute where event_id in (select id from event_definition where account_id = "+this.account_id+" and event_name='"
						+ eventName + "')");
		Assert.assertEquals(stringAttri.get("count"), "0");
		Thread.sleep(3000);
		eventPage.deleteEventByName(eventName);
	}

	@Test(groups = { "event" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_create_new_event_with_attribute_having_dynamic_value(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickToAddEventAttribute()
				.enterAttributeName("dynamic").clickOnDynamicCheckBox()
				.enterDynamicExpression("date.today() - ##MEMBER.birthdate##").clickOnCreateEventButton();

		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		Map<String, String> stringAttri = mPulseDB.getResultMap(
				"select * from event_attribute where event_id in (select id from event_definition where account_id = "+this.account_id+" and event_name='"
						+ eventName + "') and attribute_name = 'dynamic'");
		Assert.assertEquals(stringAttri.get("is_dynamic"), "t");
		Assert.assertEquals(stringAttri.get("required"), "f");
		Assert.assertEquals(stringAttri.get("expression"), "date.today() - ##MEMBER.birthdate##");
		Thread.sleep(3000);
		eventPage.deleteEventByName(eventName);
	}

	@Test(groups = { "event" ,"cannotdelete" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_not_delete_event_if_used_in_any_active_campaign(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		String campName = RandomeUtility.getRandomString();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickOnCreateEventButton();
		
		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		eventPage.goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName(eventName).clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnSaveAsDraftButton();
		Thread.sleep(3000);
		
		Assert.assertTrue(eventPage.goToMyAccountPage().goToEventDefinitionPage()
				.deleteEventByName(eventName).isCanNotDeleteOKButtonPresent(), 
				"Can not delete pop up with OK button is not present");
		eventPage.clickOnCanNotDeleteOKButton();
		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is deleted and not present on event definition page");
		eventPage.goToCommunicationPage().retireCampaignByName(campName);
		System.out.println("Campaign "+campName+" is retired");
		Thread.sleep(3000);
		eventPage.goToMyAccountPage().goToEventDefinitionPage()
				.deleteEventByName(eventName);
		Map<String, String> stringAttri = mPulseDB.getResultMap(
				"select count(*) from event_definition where account_id ="+this.account_id+" and event_name = '"+eventName+"'");
		Assert.assertEquals(stringAttri.get("count"), "0");
	}

	@Test(groups = { "event" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_use_newly_created_event_in_setting_custom_event_for_member_engagement_trigger_in_a_campaign(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		String campName = RandomeUtility.getRandomString();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickOnCreateEventButton();
		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		eventPage.goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName(eventName).clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnSaveAsDraftButton();
		eventPage.goToCommunicationPage().retireCampaignByName(campName);
		eventPage.goToMyAccountPage().goToEventDefinitionPage()
		.deleteEventByName(eventName);
	}

	@Test(groups = { "event" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_select_event_attribute_and_give_value_while_setting_custom_event_for_member_engagement_trigger_in_a_campaign(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name").selectCustomEventAttributeName("defaultattri").enterCustomEventAttributeValue("test")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnSaveAsDraftButton().retireCampaignByName(campName);
	}

	@Test(groups = { "event", "" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_select_event_callback_in_XML(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).selectCallbackFormat("XML")
				.selectCallbackURL_Default().clickOnCreateEventButton();
		eventPage.deleteEventByName(eventName);
	}
	
	@Test(groups = { "event" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_select_event_callback_in_JSON(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).selectCallbackFormat("JSON")
				.selectCallbackURL_Default().clickOnCreateEventButton();
		eventPage.deleteEventByName(eventName);
	}

	@Test(groups = { "event", "event_retest" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_update_event_callback_from_JSON_to_XML_or_XML_to_JSON(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = "zz"+RandomeUtility.getRandomEventName();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).selectCallbackFormat("JSON")
				.selectCallbackURL_Default().clickOnCreateEventButton();
		
		eventPage.clickOnEditEventButtonByEventName(eventName).selectCallbackFormat("XML").selectCallbackURL_Default().clickOnCreateEventButton();
		eventPage.clickOnEditEventButtonByEventName(eventName).selectCallbackFormat("JSON").selectCallbackURL_Default().clickOnCreateEventButton();
	}

	@Test(groups = { "event", "failtest" })
	@Parameters({ "browser" })
	public void Verify_that_subscribed_member_get_message_for_valid_event_fired_from_event_upload_api_v2(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		eventData.put("defaultattri", "anyvalue");
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default name", this.listId, "immediate", "Asia/Kolkata", "no_rule", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default name");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(12000);
		Assert.assertTrue(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));
	}

	@Test(groups = { "event", "sanity" })
	@Parameters({ "browser" })
	public void Verify_that_pending_or_unsubscribe_member_does_not_get_message_for_valid_event_fired_from_event_upload_api_v2(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		
		MoAPI.hitMoApi(this.shortCode, phoneNumber, this.listkeyword);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		eventData.put("defaultattri", "anyvalue");
		String accountId = ConfigReader.getConfig("account_id");
		
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default name", this.listId, "immediate", "Asia/Kolkata", "no_rule", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default name");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		Assert.assertFalse(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member get event message !");
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));
	}

	@Test(groups = { "event", "failtest" })
	@Parameters({ "browser" })
	public void Verify_that_if_custom_event_trigger_is_used_for_sms_message_and_fire_event_using_member_email_where_member_subscribe_in_sms_channel_message_should_fire_for_member(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String email = RandomeUtility.getRandomEmails(6);
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> memberData = new HashMap<String, String>();
		memberData.put("mobilephone", phoneNumber);
		memberData.put("email", email);
		AudienceApi.upsertMemberRecord(memberData);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("email", email);
		eventData.put("defaultattri", "anyvalue");
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default name", this.listId, "immediate", "Asia/Kolkata", "no_rule", "full", 200);
		Assert.assertEquals(result.get("email"), email);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default name");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		Assert.assertTrue(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));
	}
	
	@Test(groups = { "event" })
	@Parameters({ "browser" })
	public void Verify_that_Custom_event_trigger_should_fire_for_evaluation_scope_with_rule(String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name").selectCustomEventAttributeName("defaultattri")
		.enterCustomEventAttributeValue("testme")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		eventData.put("defaultattri", "testme");
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default name", this.listId, "immediate", "Asia/Kolkata", "with_rule", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default name");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		Assert.assertTrue(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));		
	}
	
	@Test(groups = { "event", "21retestpass2" })
	@Parameters({ "browser" })
	public void Verify_that_Custom_event_trigger_should_not_fire_when_attribute_value_not_same_as_in_event_trigger_using_evaluation_scope_with_rule(String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name").selectCustomEventAttributeName("defaultattri")
		.enterCustomEventAttributeValue("testme")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		eventData.put("defaultattri", "notsame");
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default name", this.listId, "immediate", "Asia/Kolkata", "with_rule", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default name");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		Assert.assertFalse(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));		
	}
	
	@Test(groups = { "event" , "16thapr"})
	@Parameters({ "browser" })
	public void Verify_that_Custom_event_trigger_should_fire_for_evaluation_scope_all(String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name").selectCustomEventAttributeName("defaultattri")
		.enterCustomEventAttributeValue("testme")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		eventData.put("defaultattri", "testme");
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default name", this.listId, "immediate", "Asia/Kolkata", "all", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default name");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		Assert.assertTrue(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));
		
	}
	
	@Test(groups = { "event", "21retestpass3" })
	@Parameters({ "browser" })
	public void Verify_that_Custom_event_trigger_should_not_fire_if_required_attribute_is_missing(String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message for creating custom event trigger")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default name")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default name", this.listId, "immediate", "Asia/Kolkata", "with_rule", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default name");
		Assert.assertEquals(result.get("error_message"), "One or more required Event Attributes are missing.");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		Assert.assertFalse(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));
		
	}
	
	@Test(groups = { "event" , "16thapr2"})
	@Parameters({ "browser" })
	public void Verify_that_Custom_event_trigger_should_work_for_dynamic_event_attribute(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("Test dynamic attribute value - ##Event.dynamic## Thanks!")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("dynamic event")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		eventData.put("attry1", "78");
		eventData.put("attry2", "2");
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "dynamic event", this.listId, "immediate", "Asia/Kolkata", "all", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "dynamic event");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(12000);
		Assert.assertTrue(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		String message = mPulseDB.getMessageSentContentFromMemberActivity(account_id, result.get("memberid"), campName);
		System.out.println("Message is "+message);
		Assert.assertTrue(message.contains("Test dynamic attribute value - 156 Thanks!"));
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));		
	}
	
	@Test(groups = { "event" , "jsoncallback"})
	@Parameters({ "browser" })
	public void Verify_that_user_get_External_MO_callback_in_json_when_user_reply_on_event_message(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("Test json callback Thanks!")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default json")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String dateTime = mPulseDB.getStringData("select now()");
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default json", this.listId, "immediate", "Asia/Kolkata", "all", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default json");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		System.out.println("checking entry in mt_tracking");
		Thread.sleep(5000);
		Assert.assertTrue(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		UtilityMethods.hitMoApi(this.shortCode, phoneNumber, "sure i am going");
		Thread.sleep(5000);
		Assert.assertTrue(mPulseDB.isRecoredPresent("select * from callback_message where action ilike 'External_MO' and account_id= "+this.account_id+" and created_on > '"+ dateTime +"'"));
		Map<String, String> resultDB = mPulseDB.getResultMap("select * from callback_message where action ilike 'External_MO' and account_id= "+this.account_id+" and created_on > '"+ dateTime+
				"' order by id desc limit 1");
		System.out.println("Callback message is " + resultDB.get("message"));
		JSONObject jsonObj = new JSONObject(resultDB.get("message"));
		Assert.assertEquals(result.get("memberid"), String.valueOf(jsonObj.getJSONObject("MEMBER").getInt("member_id")) );
		Assert.assertEquals("sure i am going", jsonObj.getString("MO") );
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));				
	}
	
	@Test(groups = { "event" , "xmlcallback"})
	@Parameters({ "browser" })
	public void Verify_that_user_get_External_MO_callback_in_xml_when_user_reply_on_event_message(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String campName = RandomeUtility.getRandomString();
		CommunicationPage communicationPage = loginTo_mPulse_cp(driver).goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("Test json callback Thanks!")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName("default xml")
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		String dateTime = mPulseDB.getStringData("select now()");
		
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_JSON_V2(eventData, "default xml", this.listId, "immediate", "Asia/Kolkata", "all", "full", 200);
		Assert.assertEquals(result.get("mobilephone"), phoneNumber);
		Assert.assertEquals(result.get("statusCode"), "200");
		Assert.assertEquals(result.get("event_definition_name"), "default xml");
		System.out.println("Member id is - " +result.get("memberid"));
		Thread.sleep(5000);
		System.out.println("checking entry in mt_tracking");
		Thread.sleep(5000);
		Assert.assertTrue(mPulseDB.isMessageEntryInMT_Tracking(account_id, result.get("memberid"), campName), "Error : Member not get event message !");
		UtilityMethods.hitMoApi(this.shortCode, phoneNumber, "sure i am going");
		Thread.sleep(5000);
		Assert.assertTrue(mPulseDB.isRecoredPresent("select * from callback_message where action ilike 'External_MO' and account_id= "+this.account_id+" and created_on > '"+ dateTime +"'")
				, "ERROR: Member not received XML callback");
		Map<String, String> resultDB = mPulseDB.getResultMap("select * from callback_message where action ilike 'External_MO' and account_id= "+this.account_id+" and created_on > '"+ dateTime+
				"' order by id desc limit 1");
		System.out.println("Callback message is " + resultDB.get("message"));
		XmlPath xmlPath = new XmlPath(resultDB.get("message"));
		Assert.assertEquals(result.get("memberid"), xmlPath.get("body.MEMBER.member_id.text()"));
		Assert.assertEquals("sure i am going", xmlPath.get("body.MO.text()"));
		communicationPage.retireCampaignByName(campName);
		AudienceApi.deleteMemberById(result.get("memberid"));	
	}
	
	@Test(groups = { "event" , "21retestxml"})
	@Parameters({ "browser" })
	public void Verify_that_event_upload_API_working_for_xml(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = "01aa"+RandomeUtility.getRandomEventName();
		String campName = "xml_"+RandomeUtility.getRandomString();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickToAddEventAttribute()
				.enterAttributeName("string_attri").clickOnAttributeRequiredCheckBox()
				.clickOnCreateEventButton();
		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		eventPage.goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message to verify event upload api is working for xml")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName(eventName)
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
		Map<String, String> eventData = new HashMap<String, String>();
		eventData.put("mobilephone", phoneNumber);
		eventData.put("string_attri", "any value");
		HashMap<String, String> result = EventUploadApi.eventUploadAPI_XML_V2(eventData, eventName, this.listId, "immediate", "Asia/Kolkata", "all", "full");
		Assert.assertEquals(result.get("status_code"), "200");
		Assert.assertEquals(result.get("event_definition_name"), eventName);
		eventPage.goToCommunicationPage().retireCampaignByName(campName);
		eventPage.goToMyAccountPage().goToEventDefinitionPage().deleteEventByName(eventName);
	}
	
	@Test(groups = { })
	@Parameters({ "browser" })
	public void Verify_that_event_upload_API_is_working_for_more_than_5_members_in_single_request(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		String campName = RandomeUtility.getRandomString();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickToAddEventAttribute()
				.enterAttributeName("string_attri").clickOnAttributeRequiredCheckBox()
				.clickOnCreateEventButton();
		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		eventPage.goToCommunicationPage().clickOnCreateNewCampaignButton().enterCampaignName(campName)
		.selectListByListName(this.listName).checkSmsChannelsCheckBox().clickOnSaveAndNextButton()
		.goToSmsComposePage().entersmsText("This is sms text message to verify event upload api is working for xml")
		.clickOnAddTriggerButton().clickOnMemberEngagemantTrigger().clickOnCustomEventButton()
		.selectCustomEventName(eventName)
		.clickOnSubmitTriggerButton().clickOnSaveButton()
		.clickOnNextButton().clickOnLaunchCampaignButton();
		
		RestAssured.baseURI = ConfigReader.getConfig("eventUploadApiUrl");
		String listId=this.listId; 
		String scheduledOn="immediate";
		String timeZone="Asia/Kolkata";
		String evaluationScope="all";
		
		String[] phones = new String[7]; 
		for (int x= 0; x<=6; x++) {
			String phoneNumber = RandomeUtility.getRandomPhoneNumber();
			MoAPI.subscribeMemberInSMSListByListName(this.shortCode, phoneNumber, this.listName);
			phones[x] = phoneNumber;
		}
		String testdata = "{ \"body\": { \"events\": { \"name\": \""+eventName+"\", \"event\": [ "
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[0]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[1]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[2]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[3]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[4]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[5]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" },"
			+" { \"scheduled_on\": \""+scheduledOn+"\", \"timezone\": \""+timeZone+"\", \"evaluation_scope\": \""+evaluationScope+"\", \"mobilephone\": \""+phones[6]+"\", \"correlationid\":\"jkh67\", \"string_attri\": \"jkhgkjg\" }"
				+" ] }, \"listid\": \""+listId+"\" } }";
		
		Response r = expect().given().contentType("application/json")
				.header("Authorization", UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"), ConfigReader.getConfig("api_password")))
				.header("X-Ms-API-Version", "2.0")
				.header("X-Ms-Format", "json").header("X-Ms-Source", "api").header("X-Ms-Verbosity", "full")
				.body(testdata).log().all().when().post();
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.statusCode(), 200);
		Assert.assertEquals(r.header("Content-Type"), "application/json;charset=utf-8");
		JsonPath jsonPathEvaluator = r.jsonPath();
		Assert.assertEquals(jsonPathEvaluator.getString("body.results.processed_event_instances"), "7");
		Assert.assertEquals(jsonPathEvaluator.getString("body.results.scheduled_messages"), "7");
		Assert.assertEquals(jsonPathEvaluator.getString("body.results.details.events.event_definition_name"), "event_mebes7");
		
	}
	
	@Test(groups = { "event" , "testevantv2"})
	@Parameters({ "browser" })
	public void Verify_that_user_can_edit_existing_event_can_add_attributes_or_change_default_value(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Event Upload API Version 2");
		logger.info("******** Browser " + browser + " Started ********");
		String eventName = RandomeUtility.getRandomEventName();
		EventDefinitionsPage eventPage = loginTo_mPulse_cp(driver).goToMyAccountPage().goToEventDefinitionPage()
				.goToCreateNewEventPage().enterEventName(eventName).clickToAddEventAttribute()
				.enterAttributeName("attri1").clickOnAttributeRequiredCheckBox()
				.clickOnCreateEventButton();
		Thread.sleep(3000);
		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		eventPage.clickOnEditEventButtonByEventName(eventName).selectAttributeType("Number").clickToAddEventAttribute()
		.enterAttributeName("attri2").clickOnAttributeRequiredCheckBox()
		.clickOnCreateEventButton();
		Thread.sleep(3000);
		Assert.assertTrue(eventPage.isEventPresentByName(eventName), "Event "+eventName+" is not present on event definition page");
		eventPage.deleteEventByName(eventName);
	}
	
//	@Test(groups = { "event" })
//	@Parameters({ "browser" })
//	public void Verify_that_if_custom_event_trigger_is_used_for_email_message_and_fire_event_using_member_phone_number_where_member_subscribe_in_email_channel_message_should_fire_for_member(
//			String browser) {
//	}

}
