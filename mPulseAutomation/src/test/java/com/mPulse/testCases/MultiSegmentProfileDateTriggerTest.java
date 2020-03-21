package com.mPulse.testCases;

import java.util.HashMap;
import java.util.Map;

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
import com.mPulse.utility.RandomeUtility;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MultiSegmentProfileDateTriggerTest extends BaseTest {
	
	private WebDriver driver;
	AudienceSegmentsPage audienceSegment;
	String segmentName;
	String memberid;

	Logger logger = Logger.getLogger(MultiSegmentProfileDateTriggerTest.class);
	
	String account_id = ConfigReader.getConfig("account_id");
	String listId = ConfigReader.getConfig("segmentTestList_id");
	String shortCode = ConfigReader.getConfig("segmentTestList_sc");
	String listName = ConfigReader.getConfig("segmentTestList_name");
	String listkeyword = ConfigReader.getConfig("segmentTestList_keyword");
	
	@Test(groups = { "sanity", "MultiSegmentProfileDateTriggerTest" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_compose_campaign_with_having_multi_segment_and_apply_profile_trigger(
			String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Multisegment on Profile and Date Triggers");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName(this.listName)
		.clickOnApplySegmentsButton()
		.selectSegmentByName("address1 is noida")
		.selectSegmentByName("custom number is 729")
		.enterCompositeSegmentName("multi_Seg_"+campaignName)
		.clickOnSelectSegmentOKButton()
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is profile trigger message with Composite segment. Automation test!")
		.clickOnAddTriggerButton()
		.clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address1")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnSaveAsDraftButton();
		
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		communication.retireCampaignByName(campaignName);

	}

	@Test(groups = { "sanity", "MultiSegmentProfileDateTriggerTest" })
	@Parameters({ "browser" })
	public void Verify_that_message_must_fire_when_user_add_member_to_the_composite_segment_and_fire_profile_trigger(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Multisegment on Profile and Date Triggers");
		logger.info("******** Browser " + browser + " Started ********");
		segmentName = UtilityMethods.getNewSegmentName();
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName(this.listName)
		.clickOnApplySegmentsButton()
		.selectSegmentByName("address1 is noida")
		.selectSegmentByName("custom number is 729")
		.enterCompositeSegmentName("multi_Seg_"+campaignName)
		.clickOnSelectSegmentOKButton()
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is profile trigger message with Composite segment. Automation test!")
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
		
		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String randomeString = RandomeUtility.getRandomString();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("email", email);
		data.put("CUSTOM_NUMBER", "729");
		data.put("ADDRESS1", "noida");

		String memberid = AudienceApi.createNewMemberAndSub(data, this.listId);
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("email", email);
		newdata.put("ADDRESS2", randomeString);
		Thread.sleep(12000);
		AudienceApi.createNewMember(newdata);
		Thread.sleep(12000);
		Boolean result = mPulseDB.isMessageEntryInMT_Tracking(account_id, memberid, campaignName);
		Assert.assertEquals(result.booleanValue(), true);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberById(memberid);

	}

	@Test(groups = { "sanity" , "MultiSegmentProfileDateTriggerTest"})
	@Parameters({ "browser" })
	public void Verify_that_message_must_not_fire_when_user_remove_existing_member_from_composite_segment_and_fire_profile_trigger(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Multisegment on Profile and Date Triggers");
		logger.info("******** Browser " + browser + " Started ********");
		segmentName = UtilityMethods.getNewSegmentName();
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName(this.listName)
		.clickOnApplySegmentsButton()
		.selectSegmentByName("address1 is noida")
		.selectSegmentByName("custom number is 729")
		.enterCompositeSegmentName("multi_Seg_"+campaignName)
		.clickOnSelectSegmentOKButton()
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is profile trigger message with segment. Automation test!")
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
		
		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String randomeString = RandomeUtility.getRandomString();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("email", email);
		data.put("CUSTOM_NUMBER", "729");
		data.put("ADDRESS1", "noida");

		String memberid = AudienceApi.createNewMemberAndSub(data, this.listId);
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("email", email);
		newdata.put("ADDRESS2", randomeString);
		newdata.put("ADDRESS1", "krishnaXX");
		newdata.put("CUSTOM_NUMBER", "357");
		
		AudienceApi.upsertMemberRecord(newdata);
		Boolean result = mPulseDB.isMessageEntryInMT_Tracking(account_id, memberid, campaignName);
		Assert.assertEquals(result.booleanValue(), false);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberById(memberid);

	}

	@Test(groups = { "sanity", "MultiSegmentProfileDateTriggerTest" })
	@Parameters({ "browser" })
	public void Verify_that_message_must_not_fire_to_member_not_in_the_composite_segment_but_in_the_list(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Multisegment on Profile and Date Triggers");
		logger.info("******** Browser " + browser + " Started ********");
		segmentName = UtilityMethods.getNewSegmentName();
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName(this.listName)
		.clickOnApplySegmentsButton()
		.selectSegmentByName("address1 is noida")
		.selectSegmentByName("custom number is 729")
		.enterCompositeSegmentName("multi_Seg_"+campaignName)
		.clickOnSelectSegmentOKButton()
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is profile trigger message with segment. Automation test!")
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
		
		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String randomeString = RandomeUtility.getRandomString();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("email", email);
		data.put("CUSTOM_NUMBER", "3298");

		String memberid = AudienceApi.createNewMemberAndSub(data, this.listId);
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("email", email);
		newdata.put("ADDRESS2", randomeString);
		
		AudienceApi.upsertMemberRecord(newdata);
		Boolean result = mPulseDB.isMessageEntryInMT_Tracking(account_id, memberid, campaignName);
		Assert.assertEquals(result.booleanValue(), false);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberById(memberid);

	}

	@Test(groups = { "sanity", "MultiSegmentProfileDateTriggerTest" })
	@Parameters({ "browser" })
	public void Verify_that_user_can_compose_campaign_with_having_composite_segment_and_apply_date_trigger(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Multisegment on Profile and Date Triggers");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName(this.listName)
		.clickOnApplySegmentsButton()
		.selectSegmentByName("address1 is noida")
		.selectSegmentByName("custom number is 729")
		.enterCompositeSegmentName("multi_Seg_"+campaignName)
		.clickOnSelectSegmentOKButton()
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is Date trigger message with segment. Automation test!")
		.clickOnAddTriggerButton()
		.clickOnDateTrigger()
		.enterDelyAmountOfTime("02")		
		.selectTimeUnitDropDownByVisibleText("Minutes")
		.selectTriggerDateFieldNameByVisibleText("Custom Date")
		.selectTriggerDateCondition("After")
		.selectMemberTimeFieldByVisibleText("Custom Time")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnSaveAsDraftButton();
		
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		communication.retireCampaignByName(campaignName);

	}

	@Test(groups = { "sanity", "MultiSegmentProfileDateTriggerTest"})
	@Parameters({ "browser" })
	public void Verify_that_message_must_not_fire_when_user_remove_existing_member_from_the_composite_segment_and_fire_date_trigger(
			String browser) throws Exception {
		
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Multisegment on Profile and Date Triggers");
		logger.info("******** Browser " + browser + " Started ********");
		segmentName = UtilityMethods.getNewSegmentName();
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication = 
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName(this.listName)
		.clickOnApplySegmentsButton()
		.selectSegmentByName("address1 is noida")
		.selectSegmentByName("custom number is 729")
		.enterCompositeSegmentName("multi_Seg_"+campaignName)
		.clickOnSelectSegmentOKButton()
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is Date trigger message with segment. Automation test!")
		.clickOnAddTriggerButton()
		.clickOnDateTrigger()
		.enterDelyAmountOfTime("02")
		.selectTimeUnitDropDownByVisibleText("Minutes")
		.selectTriggerDateFieldNameByVisibleText("Custom Date")
		.selectTriggerDateCondition("After")
		.selectMemberTimeFieldByVisibleText("Custom Time")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);

		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("email", email);
		data.put("CUSTOM_NUMBER", "729");
		data.put("ADDRESS1", "noida");

		String memberid = AudienceApi.createNewMemberAndSub(data, this.listId);
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("email", email);
		newdata.put("CUSTOM_DATE", UtilityMethods.getDate_YYYY_MM_DD(0));
		newdata.put("CUSTOM_TIME", UtilityMethods.getTimeWithDelay(0));
		newdata.put("ADDRESS1", "krishnaXX");
		newdata.put("CUSTOM_NUMBER", "9787");
		
		AudienceApi.upsertMemberRecord(newdata);
		Thread.sleep(160000); // waiting for two minutes
		Boolean result = mPulseDB.isMessageEntryInMT_Tracking(account_id, memberid, campaignName);
		Assert.assertEquals(result.booleanValue(), false);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberById(memberid);

	}

	@Test(groups = { "sanity", "MultiSegmentProfileDateTriggerTest", "re-test"})
	@Parameters({ "browser" })
	public void Verify_that_message_must_fire_when_user_add_member_to_the_composite_segment_and_fire_date_trigger(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Multisegment on Profile and Date Triggers");
		logger.info("******** Browser " + browser + " Started ********");
		segmentName = UtilityMethods.getNewSegmentName();
		String campaignName = UtilityMethods.getNewCampaignName();
		CommunicationPage communication = 
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName(this.listName)
		.clickOnApplySegmentsButton()
		.selectSegmentByName("address1 is noida")
		.selectSegmentByName("custom number is 729")
		.enterCompositeSegmentName("multi_Seg_"+campaignName)
		.clickOnSelectSegmentOKButton()
		.checkSmsChannelsCheckBox()
		.clickOnSaveAndNextButton()
		.goToSmsComposePage()
		.entersmsText("Hi, this is Date trigger message with segment. Automation test!")
		.clickOnAddTriggerButton()
		.clickOnDateTrigger()
		.enterDelyAmountOfTime("02")
		.selectTimeUnitDropDownByVisibleText("Minutes")
		.selectTriggerDateFieldNameByVisibleText("Custom Date")
		.selectTriggerDateCondition("After")
		.selectMemberTimeFieldByVisibleText("Custom Time")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);

		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("email", email);
		data.put("CUSTOM_NUMBER", "729");
		data.put("ADDRESS1", "noida");

		String memberid = AudienceApi.createNewMemberAndSub(data, this.listId);
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("email", email);
		newdata.put("CUSTOM_DATE", UtilityMethods.getDate_YYYY_MM_DD(0));
		newdata.put("CUSTOM_TIME", UtilityMethods.getTimeWithDelay(0));
		
		AudienceApi.upsertMemberRecord(newdata);
		Thread.sleep(160000); // waiting for two minutes
		Boolean result = mPulseDB.isMessageEntryInMT_Tracking(account_id, memberid, campaignName);
		Assert.assertEquals(result.booleanValue(), true);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberById(memberid);

	} 

}
