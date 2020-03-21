package com.mPulse.testCases;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
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
import com.mPulse.utility.ThreadSafeNumber;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class SMSLinkTrackingTest extends BaseTest {

	private WebDriver driver;
	AudienceSegmentsPage audienceSegment;
	String memberid;

	Logger logger = Logger.getLogger(SMSLinkTrackingTest.class);
	String account_id = ConfigReader.getConfig("account_id");
	String listId = ConfigReader.getConfig("smsLinkTrackingList_id");
	String shortCode = ConfigReader.getConfig("smsLinkTrackingList_sc");
	String listName = ConfigReader.getConfig("smsLinkTrackingList_name");
	String listkeyword = ConfigReader.getConfig("smsLinkTrackingList_keyword");

	@Test(groups = { "sanity", "linktracking",})
	@Parameters({ "browser" })
	public void Verify_user_can_create_sms_message_with_link_shortening_option(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, this is test compose message to test link shortening")
		.clickOnSmsLinkShorteningCheckBox()
		.clickOnAddTriggerButton()
		.clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address2")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton()
		.clickOnNextButton()
		.clickOnSaveAsDraftButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap(
				"select track_click_through from sms_message where id in (select id from message where campaign_id in (select id from campaign where name ilike '"+campaignName+"'))");
		Assert.assertEquals(mtValue.get("track_click_through"), "t");

	}
	
	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_user_can_create_email_message_with_link_shortening_option(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage()
		.clickOnCreateNewCampaignButton()
		.enterCampaignName(campaignName)
		.selectListByListName("sms link tracking").checkEmailChannelsCheckBox().clickOnSaveAndNextButton()
		.goToEmailComposePage().enterEmailSubject("test email link tracking").selectLinkTrackingCheckBox()
		.clickOnDesignEmail().clickOnPlainEmailSelectButton()
		.enterEmailText("url1 - http://www.google.com/search?q=SampleIdent+filetype%3Audf url2 - http://mysite.du.edu/~balzar/lebailbr.dbw url3 - http://sdpd.univ-lemans.fr/DU-SDPD/semaine-4/na5.rit url4 - https://en.wikipedia.org/wiki/Internet#Terminology Thanks!")
		.clickOnEmailComposeSaveButton().clickOnEmailComposeCloseButton()
		.clickOnAddTriggerButton().clickOnMemberProfileTrigger().selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address2").selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton().clickOnSaveButton().clickOnNextButton()
		.clickOnSaveAsDraftButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		HashMap<String, String> mtValue= (HashMap<String, String>) mPulseDB.getResultMap(
				"select track_click_through from email_message where id in (select id from message where campaign_id in (select id from campaign where name ilike '"+campaignName+"'))");
		Assert.assertEquals(mtValue.get("track_click_through"), "t");

	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_user_can_click_on_links_recieved_at_sms_devices(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, url one - https://www.google.com/search?q=SampleIdent+filetype%3Audf end!")
		.clickOnSmsLinkShorteningCheckBox()
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
		Thread.sleep(8000);
		String link = "https://www.google.com/search?q=SampleIdent+filetype%3Audf";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("url one -"),"Error: not received shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("link is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(link));
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_links_get_opened_and_count_increase_under_link_click_section_on_reporting_page(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, url one - https://www.google.com/search?q=SampleIdent+filetype%3Audf end!")
		.clickOnSmsLinkShorteningCheckBox()
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
		Thread.sleep(8000);
		String link = "https://www.google.com/search?q=SampleIdent+filetype%3Audf";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("url one -"),"Error: not received shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("link is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(link));
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);

	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_secure_message_deeplink_feature_for_Email(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage().enterCampaignStartDate("06/21/2019")
		.enterCampaignEndDate("06/21/2019")
		.enterCampaignNameInSearchTextBox("default email and secure message")
		.clickOnCampaignSearchButton().clickOnCloneLink("default email and secure message").clickOnCampaignCloneOkButton()
		.clickOnCampaignEditLink("Clone default email and secure message _")
		.enterCampaignName(campaignName).clickOnSaveAndNextButton()
		.goToEmailComposePage().clickOnAddTriggerButton().clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address1")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton().clickOnAppmailTab()
		.clickOnAddTriggerButton().clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address1")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton().clickOnNextButton()
		.clickOnLaunchCampaignButton();
		
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		Thread.sleep(8000);
		String xpath = "html/body/header/label/strong";
		String email = RandomeUtility.getRandomEmails(6);
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("email", email);
		data.put("appmemberid", RandomeUtility.getRandomString());

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("email", email);
		newdata.put("ADDRESS1", "newkanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		Thread.sleep(12000);
		String beforeLink = driver.getCurrentUrl();
		
		String actualLink = UtilityMethods.getSecureMessageLinkFromMailinatorEmail(driver, email);
		driver.navigate().to(actualLink);
		System.out.println("current link is - "+ driver.getCurrentUrl());
		System.out.println("title is - "+ driver.getTitle());
		Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
		driver.navigate().to(beforeLink);
		Thread.sleep(5000);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByEmail(email);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_secure_message_deeplink_feature_for_SMS(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		
		CommunicationPage communication =
				loginTo_mPulse_cp(driver)
				.goToCommunicationPage()
				.clickOnCreateNewCampaignButton()
				.enterCampaignName(campaignName)
				.selectListByListName("sms link tracking")
				.checkSmsChannelsCheckBox()
				.checkAppMailChannelsCheckBox()
				.clickOnSaveAndNextButton()
				.goToSmsComposePage()
				.entersmsText("Hi, deeplink - ##SECUREMESSAGE_LINK## end!")
				.clickOnAddTriggerButton()
				.clickOnMemberProfileTrigger()
				.selectTimeUnitDropDownByVisibleText("Immediately")
				.selectFieldNameDropDownByValue("Address1")
				.selectProfileChangeDropDownByValue("New Value Only")
				.clickOnSubmitTriggerButton()
				.clickOnSaveButton()
				.clickOnAppmailTab().enterSecureMessageSubject("this is test subject")
				.enterSecureMessageText("Hi User, this is your message!").clickOnAddTriggerButton()
				.clickOnMemberProfileTrigger()
				.selectTimeUnitDropDownByVisibleText("Immediately")
				.selectFieldNameDropDownByValue("Address1")
				.selectProfileChangeDropDownByValue("New Value Only")
				.clickOnSubmitTriggerButton()
				.clickOnSaveButton()
				.clickOnSmsTab().clickOnSmsSecureMessageCheckBox()
				.clickOnSaveButton().clickOnNextButton().clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		Thread.sleep(8000);
		String xpath = "html/body/header/label/strong";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("appmemberid", RandomeUtility.getRandomString());

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS1", "newkanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(15000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("deeplink -"),"Error: not received secure message shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "deeplink - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to(actualLink);
		System.out.println("current link is - "+ driver.getCurrentUrl());
		System.out.println("title is - "+ driver.getTitle());
		Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_subscription_preferences_deeplink_feature_for_Email(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		
		CommunicationPage communication =
		loginTo_mPulse_cp(driver)
		.goToCommunicationPage().enterCampaignStartDate("06/24/2019")
		.enterCampaignEndDate("06/24/2019")
		.enterCampaignNameInSearchTextBox("default email sub preference")
		.clickOnCampaignSearchButton().clickOnCloneLink("default email sub preference").clickOnCampaignCloneOkButton()
		.clickOnCampaignEditLink("Clone default email sub preference _")
		.enterCampaignName(campaignName).clickOnSaveAndNextButton()
		.goToEmailComposePage().clickOnAddTriggerButton().clickOnMemberProfileTrigger()
		.selectTimeUnitDropDownByVisibleText("Immediately")
		.selectFieldNameDropDownByValue("Address2")
		.selectProfileChangeDropDownByValue("New Value Only")
		.clickOnSubmitTriggerButton()
		.clickOnSaveButton().clickOnNextButton()
		.clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		Thread.sleep(8000);
		String xpath = "//input[@type='Submit' and @value='Submit']";
		String email = RandomeUtility.getRandomEmails(6);

		Map<String, String> data = new HashMap<String, String>();
		data.put("email", email);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("email", email);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		Thread.sleep(12000);
		String beforeLink = driver.getCurrentUrl();
		String actualLink = UtilityMethods.getSubPrefLinkFromMailinatorEmail(driver, email);
		driver.navigate().to(actualLink);
		System.out.println("current link is - "+ driver.getCurrentUrl());
		System.out.println("title is - "+ driver.getTitle());
		Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByEmail(email);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_subscription_preferences_deeplink_feature_for_SMS(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, sublink - ##SUBPREFERENCES## end!")
		.clickOnSmsLinkShorteningCheckBox()
		.clickOnSmsEnableSubPreferencesCheckBox()
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
		Thread.sleep(8000);
		String xpath = "//input[@type='Submit' and @value='Submit']";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("sublink - "),"Error: not received sub pref shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "sublink - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("current link is - "+ driver.getCurrentUrl());
		System.out.println("title is - "+ driver.getTitle());
		Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_redirectional_URL_end_to_end(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
		String campaignName = UtilityMethods.getNewCampaignName();
		String redirectURL = "https://www.google.com/";
		CommunicationPage communication =
				loginTo_mPulse_cp(driver)
				.goToCommunicationPage()
				.clickOnCreateNewCampaignButton()
				.enterCampaignName(campaignName)
				.selectListByListName("sms link tracking")
				.checkSmsChannelsCheckBox()
				.checkAppMailChannelsCheckBox()
				.clickOnSaveAndNextButton()
				.goToSmsComposePage()
				.entersmsText("Hi, deeplink - ##SECUREMESSAGE_LINK## end!")
				.clickOnAddTriggerButton()
				.clickOnMemberProfileTrigger()
				.selectTimeUnitDropDownByVisibleText("Immediately")
				.selectFieldNameDropDownByValue("Address1")
				.selectProfileChangeDropDownByValue("New Value Only")
				.clickOnSubmitTriggerButton()
				.clickOnSaveButton()
				.clickOnAppmailTab().enterSecureMessageSubject("this is test subject")
				.enterSecureMessageText("Hi User, this is your message!").clickOnAddTriggerButton()
				.clickOnMemberProfileTrigger()
				.selectTimeUnitDropDownByVisibleText("Immediately")
				.selectFieldNameDropDownByValue("Address1")
				.selectProfileChangeDropDownByValue("New Value Only")
				.clickOnSubmitTriggerButton()
				.clickOnSaveButton()
				.clickOnSmsTab().clickOnSmsSecureMessageCheckBox()
				.clickOnAllowRedirectUrlCheckBox().enterRedirectUrl(redirectURL)
				.clickOnSaveButton().clickOnNextButton().clickOnLaunchCampaignButton();
		Assert.assertEquals(communication.isCampaignPresentByName(campaignName), true);
		Thread.sleep(8000);
		String xpath = "html/body/header/label/strong";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("appmemberid", RandomeUtility.getRandomString());

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS1", "newkanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(15000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("deeplink -"),"Error: not received secure message shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "deeplink - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to(actualLink);
		System.out.println("current link is - "+ driver.getCurrentUrl());
		System.out.println("title is - "+ driver.getTitle());
		Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
		Thread.sleep(300000);
		driver.navigate().to(actualLink);
		Assert.assertEquals(driver.getCurrentUrl(), redirectURL);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);

	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_VCF_Card_Feature(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, vcflink - ##VCFCARD## end!")
		.clickOnSmsLinkShorteningCheckBox()
		.clickOnSmsEnableVCFCheckBox()
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
		Thread.sleep(8000);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("vcflink - "),"Error: not received vcflink shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "vcflink - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("current link is - "+ driver.getCurrentUrl());
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_Branding_code_feature(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, sublink - ##SUBPREFERENCES## end!")
		.clickOnSmsLinkShorteningCheckBox()
		.clickOnSmsEnableSubPreferencesCheckBox()
		.clickOnAddBrnadingCodeCheckBox()
		.enterBrandingCode("auto").selectAppendBrandingCheckBox()
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
		Thread.sleep(8000);
		String xpath = "//input[@type='Submit' and @value='Submit']";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("sublink - "),"Error: not received sub pref shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "sublink - "," end!");
		Assert.assertTrue(actualLink.contains("auto"));
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("current link is - "+ driver.getCurrentUrl());
		System.out.println("title is - "+ driver.getTitle());
		Assert.assertTrue(driver.findElement(By.xpath(xpath)).isDisplayed());
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_that_URLs_containing_hash_sign_should_work_properly_when_shortened(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, hash url one - https://en.wikipedia.org/wiki/Internet#Terminology end!")
		.clickOnSmsLinkShorteningCheckBox()
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
		Thread.sleep(8000);
		String link = "https://en.wikipedia.org/wiki/Internet#Terminology";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("hash url one -"),"Error: not received link with hash shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("link is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(link));
		Assert.assertTrue(driver.findElement(By.id("Terminology")).isDisplayed());
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);

	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_that_trailing_slashes_should_work_properly_in_URLs_when_shortened(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, trailing slashe url one - https://krypted.com/utilities/html-encoding-reference/ end!")
		.clickOnSmsLinkShorteningCheckBox()
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
		Thread.sleep(8000);
		String link = "https://krypted.com/utilities/html-encoding-reference/";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("trailing slashe url one"),"Error: not received link with hash shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("link is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(link));
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id='post-5773']/header/h1")).isDisplayed());
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_links_with_http_and_https_types(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, http url one - http://www.yopmail.com/en/ end! url two https://www.google.com/ end url two !")
		.clickOnSmsLinkShorteningCheckBox()
		.clickOnSmsPrependHttpCheckBox()
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
		Thread.sleep(8000);
		String linkone = "http://www.yopmail.com/en/";
		String linktwo = "https://www.google.com/";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("http url one -"),"Error: not received http and https shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String actualLinkTwo = StringUtils.substringBetween(content.get("content"), "url two "," end url");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to(actualLink);
		System.out.println("link one with http is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(linkone));
		driver.navigate().to(actualLinkTwo);
		System.out.println("link one with https is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(linktwo));
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 2);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);

	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_that_reserved_characters_should_work_when_used_properly_in_link_when_shortened(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, reserved character url one - https://www.google.com/search?source=hp&ei=aXXmXN79GZaz0PEPj7mPkAM&q=sachin&oq=sachin&gs_l=psy-ab.12..0i131j0j0i131j0l2j0i131j0l4.133.780..1050...0.0..0.294.1745.2-6......0....1..gws-wiz.....0.dwpKZzZoum8 end!")
		.clickOnSmsLinkShorteningCheckBox()
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
		Thread.sleep(8000);
		String link = "https://www.google.com/search?source=hp&ei=aXXmXN79GZaz0PEPj7mPkAM&q=sachin&oq=sachin&gs_l=psy-ab.12..0i131j0j0i131j0l2j0i131j0l4.133.780..1050...0.0..0.294.1745.2-6......0....1..gws-wiz.....0.dwpKZzZoum8";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("reserved character url one"),"Error: not received reserved character shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("link is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(link));
		Assert.assertTrue(driver.findElement(By.id("dimg_27")).isDisplayed());
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verify_that_URLs_containing_percentile_sign_should_work_properly_when_shortened(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, percentile sign url one - https://www.go365.com/direct-connect-logon?dcURL=HJI/wmxeVnDoUiiS5ezP9zKJEw35Kg/yTQhqrJOtfUPZycqNyXdfG/v3kY1EzJzsgRNLfElzYoBD%2BulVxglhET5reBnSxbeHydYK1YYhFfKGVZ/Uj8c8PMmyIrH%2BymAE4nFPT7nR4jCFaUy/IkIz5z9QqIiQnVzZdiv4eEE2DfrciTIZaEW9uvVxWQ==&dcAKA=MqcA9ztAOBTe&dcPO=54&dcEA=1&cm_mmc=Email_GO365-_-National_Step_Challenge_2019-_-Email1_ChallengeCTA-_-1710313619&cm_mmc=SMS-Go365-Member-_-EnrollmentAnnouncement-_-NationalStepChallenge-_-Join end!")
		.clickOnSmsLinkShorteningCheckBox()
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
		Thread.sleep(8000);
		String link = "https://www.go365.com/direct-connect-logon?dcURL=HJI/wmxeVnDoUiiS5ezP9zKJEw35Kg/yTQhqrJOtfUPZycqNyXdfG/v3kY1EzJzsgRNLfElzYoBD%2BulVxglhET5reBnSxbeHydYK1YYhFfKGVZ/Uj8c8PMmyIrH%2BymAE4nFPT7nR4jCFaUy/IkIz5z9QqIiQnVzZdiv4eEE2DfrciTIZaEW9uvVxWQ==&dcAKA=MqcA9ztAOBTe&dcPO=54&dcEA=1&cm_mmc=Email_GO365-_-National_Step_Challenge_2019-_-Email1_ChallengeCTA-_-1710313619&cm_mmc=SMS-Go365-Member-_-EnrollmentAnnouncement-_-NationalStepChallenge-_-Join";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("percentile sign url one"),"Error: not received link with hash shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("link is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(link));
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "sanity", "linktracking"})
	@Parameters({ "browser" })
	public void Verfiy_if_custom_field_has_a_link_in_it_and_used_as_token(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("SMS and Email LinkTracking");
		logger.info("******** Browser " + browser + " Started ********");
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
		.entersmsText("Hi, custom field url one - ##URL## end!")
		.clickOnSmsLinkShorteningCheckBox()
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
		Thread.sleep(8000);
		String link = "https://krypted.com/utilities/html-encoding-reference/";
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();

		Map<String, String> data = new HashMap<String, String>();
		data.put("mobilephone", phoneNumber);
		data.put("URL", link);

		this.memberid = AudienceApi.createNewMemberAndSub(data, "5438");
		
		Map<String, String> newdata = new HashMap<String, String>();
		newdata.put("mobilephone", phoneNumber);
		newdata.put("ADDRESS2", "kanpurnagar");
		Thread.sleep(12000);
		AudienceApi.upsertMemberRecord(newdata);
		
		String query = "SELECT jsonb_extract_path_text(content, 'content') as content from member_activity where audience_member_id = "
				+ memberid + " and content->>'channel'= 'SMS' and activity_type = 'MESSAGE_SENT' order by id desc limit 1";
		Thread.sleep(12000);
		HashMap<String, String> content= (HashMap<String, String>) mPulseDB.getResultMap(query);
		System.out.println("Content is - " + content.get("content"));
		Assert.assertTrue(content.get("content").contains("custom field url one"),"Error: not received link with hash shorten link message");
		String actualLink = StringUtils.substringBetween(content.get("content"), "url one - "," end!");
		String beforeLink = driver.getCurrentUrl();
		driver.navigate().to("http://"+actualLink);
		System.out.println("link is - "+ driver.getCurrentUrl());
		Assert.assertTrue(driver.getCurrentUrl().contains(link));
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id='post-5773']/header/h1")).isDisplayed());
		driver.navigate().to(beforeLink);
		driver.navigate().refresh();
		int linkcount = communication.clickOnCampaignReportLink(campaignName).clickOnSMSReportLink().getLinkClickCount();
		Assert.assertEquals(linkcount, 1);
		driver.navigate().to(beforeLink);
		communication.retireCampaignByName(campaignName);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}
}
