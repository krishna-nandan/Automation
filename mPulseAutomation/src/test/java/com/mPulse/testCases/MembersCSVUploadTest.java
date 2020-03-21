package com.mPulse.testCases;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mPulse.factories.BrowserFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.objectRepository.MappingColumnsPage;
import com.mPulse.utility.AudienceApi;
import com.mPulse.utility.CSVFileWriter;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.Member;
import com.mPulse.utility.RandomeUtility;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MembersCSVUploadTest extends BaseTest {

	private WebDriver driver;
	private MappingColumnsPage mapping;

	Logger logger = Logger.getLogger(MembersCSVUploadTest.class);
	String account_id = ConfigReader.getConfig("account_id");
	String listId = ConfigReader.getConfig("csvuploadList_id");
	String shortCode = ConfigReader.getConfig("csvuploadList_sc");
	String listName = ConfigReader.getConfig("csvuploadList_name");
	String listkeyword = ConfigReader.getConfig("csvuploadList_keyword");

	@Test(groups = { "csvupload" , "demo2" , "firefox"})
	@Parameters({ "browser" })
	public void verify_that_User_Can_Mapp_All_Fields_using_comma_delimited_CSV(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");
		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String appMemberID = RandomeUtility.getRandomAppMemberID();
		String clientMemberID = RandomeUtility.getRandomClientMemberID();
		Member member = new Member("test", "last", email, phoneNumber, appMemberID, clientMemberID, "63");
		String csvFileName = UtilityMethods.getCsvFilePath("verifyUserCanMappAllFields");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappAndClickOnUpload();
		testReporter.log(LogStatus.INFO, "All fields are mapped and clicked on upload button");
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		System.out.println("Result is " + result);
		mapping.clickOnOKButton();
		testReporter.log(LogStatus.INFO, "Clicked on OK button on final result pop up");
		mPulseDB.getStringData("select email from audience_member where account_id="+this.account_id+" and mobile_phone='" + phoneNumber + "'");
		Thread.sleep(5000);
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_confirm_opt_in_message_is_received_on_CSV_import_for_SMS(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Member member = new Member("test", "last", "", phoneNumber, "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("opt_in_message_is_received_on_CSV_import_for_SMS");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and  mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		Boolean ask = mPulseDB.isMemberGetSMSConfirmMessage(this.account_id, phoneNumber);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Confirm Message");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_confirm_opt_in_message_is_received_on_CSV_import_for_Email(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("confirm_opt_in_message_is_received_on_CSV_import_for_Email");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(account_id, email);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB.getStringData("select status from email_subscription where account_id="+this.account_id+" and email ='" + email + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from email_subscription table");
		Boolean ask = mPulseDB.isMemberGetEmailConfirmMessage(account_id, email);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Confirm Message");
		AudienceApi.deleteMemberByEmail(email);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_is_received_for_EMAIL_on_joining_the_list_from_CSV_import(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods.getCsvFilePath(
				"welcome_message_is_received_for_EMAIL_on_joining_the_list_from_CSV_import");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(account_id, email);
		testReporter.log(LogStatus.INFO, "new uploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB.getStringData("select status from email_subscription where account_id="+this.account_id+" and email ='" + email + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from email_subscription table");
		Boolean ask = mPulseDB.isMemberGetEmailWelcomeMessage(account_id, email);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		AudienceApi.deleteMemberByEmail(email);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_is_received_for_SMS_on_joining_the_list_from_CSV_import(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Member member = new Member("test", "last", "", phoneNumber, "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("welcome_message_is_received_for_SMS_on_joining_the_list");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getSMSSubscriptionStatus(account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		Boolean ask = mPulseDB.isMemberGetSMSWelcomeMessage(account_id, phoneNumber);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_is_received_for_SMS_on_joining_the_list_from_CSV_import_after_confirm_Opt_in(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Member member = new Member("test", "last", "", phoneNumber, "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("welcome_message_is_received_for_SMS_on_joining_after_confirm_Opt_in");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		testReporter.log(LogStatus.INFO, "sending yes mo on " + phoneNumber + " mobile phone");
		UtilityMethods.hitMoApi(this.shortCode, phoneNumber, "yes");
		String ask = mPulseDB.getStringSMSWelcomeMessage(this.account_id, phoneNumber);
		Assert.assertTrue(ask.contains("Thanks for joining"));
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload", "retest30thjuly" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_with_footer_message_is_received_for_EMAIL_on_joining_the_list_from_CSV_import_after_confirm_Opt_in(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("opt_in_message_is_received_on_CSV_import_for_Email");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(account_id, email);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB.getStringData("select status from email_subscription where account_id="+this.account_id+" and email ='" + email + "'");
		Assert.assertEquals(status, "0");
		UtilityMethods.clickOnConfirmLinkInEmailMailinator(driver, email);
		// Thread.sleep(15000);
		String ask = mPulseDB.getStringEmailWelcomeMessage(email);
		Assert.assertTrue(ask.contains("Thank you for signing up and welcome"));
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		Assert.assertTrue(ask.contains("Our contact address is"),
				"Footer message is not appended in Email welcome message");
		Assert.assertTrue(ask.contains("##LIST.OPT_OUT##"), "Footer message is not appended in Email welcome message");
		testReporter.log(LogStatus.INFO, "Verified that footer is appended in email Welcome Message");
		AudienceApi.deleteMemberByEmail(email);
	}

//	@Test(groups = { "csvupload" })
//	@Parameters({ "browser" })
//	public void verify_that_reporting_of_member_status_by_import_in_the_CSV_Import_Subscribe_Members_tab_in_my_account(
//			String browser) {
//
//	}

	@Test(groups = { "csvupload" , "retest30thjuly"})
	@Parameters({ "browser" })
	public void verify_that_Unsubscribe_Link_in_Email_is_working(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods.getCsvFilePath("Unsubscribe_Link_in_Email_is_working");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(this.account_id, email);
		testReporter.log(LogStatus.INFO, "new uploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB.getStringData("select status from email_subscription where account_id="+this.account_id+" and email ='" + email + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from email_subscription table");
		Boolean ask = mPulseDB.isMemberGetEmailWelcomeMessage(this.account_id, email);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		UtilityMethods.clickOnUnsubscribeLinkInEmailMailinator(driver, email);
		testReporter.log(LogStatus.INFO, "Verified - Unsubscribe link in welcome email is working");
		AudienceApi.deleteMemberByEmail(email);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_the_new_valid_phone_number_merge_with_existing_email_member(String browser)
			throws Exception {

		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("new_valid_phone_number_merge_with_existing_email");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(account_id, email);
		testReporter.log(LogStatus.INFO, "new uploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		member = new Member("test", "last", email, phoneNumber, "", "", "63");
		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));
		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappFirstName();
		mapping.mappLastName();
		mapping.mappEmail();
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		subStatus = mPulseDB.getSMSSubscriptionStatus(account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		Boolean ask = mPulseDB.isMemberGetSMSWelcomeMessage(account_id, phoneNumber);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		testReporter.log(LogStatus.INFO,
				"Verified that the new valid phone number can merge with existing email member");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_the_new_valid_phone_number_merge_with_existing_appmail_member(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String appmail = RandomeUtility.getRandomAppMemberID();
		Member member = new Member("test", "last", "", "", appmail, "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("new_valid_phone_number_merge_with_existing_appmail");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappAppMemberID();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getAppmailSubscriptionStatus(account_id, appmail);
		testReporter.log(LogStatus.INFO, "new uploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		member = new Member("test", "last", "", phoneNumber, appmail, "", "63");
		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));
		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappFirstName();
		mapping.mappLastName();
		mapping.mappAppMemberID();
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		subStatus = mPulseDB.getSMSSubscriptionStatus(account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		Boolean ask = mPulseDB.isMemberGetSMSWelcomeMessage(account_id, phoneNumber);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		testReporter.log(LogStatus.INFO,
				"Verified that the new valid phone number can merge with existing appmail member");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_the_new_valid_email_merge_with_existing_phone_number_member(String browser)
			throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Member member = new Member("test", "last", "", phoneNumber, "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("new_valid_email_merge_with_existing_phone_number_member");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String email = RandomeUtility.getRandomEmails(6);
		member = new Member("test", "last", email, phoneNumber, "", "", "63");
		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox()
				.clickOnUploadButton(CSVFileWriter.writeCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		subStatus = mPulseDB.getEmailSubscriptionStatus(this.account_id, email);
		testReporter.log(LogStatus.INFO, "new uploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		testReporter.log(LogStatus.INFO, "Verified that the new valid email merge with existing phone number member");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload" })
	@Parameters({ "browser" })
	public void verify_that_User_Can_Mapp_All_Fields_using_with_pipe_delimiter_CSV(String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");
		String email = RandomeUtility.getRandomEmails(6);
		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		String appMemberID = RandomeUtility.getRandomAppMemberID();
		String clientMemberID = RandomeUtility.getRandomClientMemberID();
		Member member = new Member("test", "last", email, phoneNumber, appMemberID, clientMemberID, "63");
		String csvFileName = UtilityMethods.getCsvFilePath("User_Can_Mapp_All_Fields_using_with_pipe_delimiter");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectAllowPipeDelimiterCheckBox()
				.clickOnUploadButton(CSVFileWriter.writePipeDelimiterCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappAndClickOnUpload();
		testReporter.log(LogStatus.INFO, "All fields are mapped and clicked on upload button");
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		System.out.println("Result is " + result);
		mapping.clickOnOKButton();
		testReporter.log(LogStatus.INFO, "Clicked on OK button on final result pop up");
		mPulseDB.getStringData("select email from audience_member where account_id="+this.account_id+" and mobile_phone='" + phoneNumber + "'");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload", "pipedelimetercsv" })
	@Parameters({ "browser" })
	public void verify_that_confirm_opt_in_message_is_received_on_CSV_import_for_SMS_using_pipe_delimiter_CSV(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Member member = new Member("test", "last", "", phoneNumber, "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("opt_in_message_is_received_on_CSV_import_using_pipe_delimiter");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox().selectAllowPipeDelimiterCheckBox()
				.clickOnUploadButton(CSVFileWriter.writePipeDelimiterCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		Boolean ask = mPulseDB.isMemberGetSMSConfirmMessage(this.account_id, phoneNumber);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Confirm Message");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload", "pipedelimetercsv" })
	@Parameters({ "browser" })
	public void verify_that_confirm_opt_in_message_is_received_on_CSV_import_for_Email_using_pipe_delimiter_CSV(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("opt_in_message_is_received_on_CSV_import_using_pipe_delimiter");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox().selectAllowPipeDelimiterCheckBox()
				.clickOnUploadButton(CSVFileWriter.writePipeDelimiterCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(this.account_id, email);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB.getStringData("select status from email_subscription where account_id="+this.account_id+" and email ='" + email + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from email_subscription table");
		Boolean ask = mPulseDB.isMemberGetEmailConfirmMessage(this.account_id, email);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Confirm Message");
		AudienceApi.deleteMemberByEmail(email);
	}

	@Test(groups = { "csvupload", "pipedelimetercsv" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_is_received_for_EMAIL_on_joining_the_list_from_CSV_import_using_pipe_delimiter_CSV(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("message_is_received_for_EMAIL_on_joining_the_list_using_pipe_delimiter");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox().selectAllowPipeDelimiterCheckBox()
				.clickOnUploadButton(CSVFileWriter.writePipeDelimiterCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(this.account_id, email);
		testReporter.log(LogStatus.INFO, "new uploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB.getStringData("select status from email_subscription where account_id="+this.account_id+" and email ='" + email + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from email_subscription table");
		Boolean ask = mPulseDB.isMemberGetEmailWelcomeMessage(this.account_id, email);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		AudienceApi.deleteMemberByEmail(email);
	}

	@Test(groups = { "csvupload", "pipedelimetercsv", "test17oct" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_is_received_for_SMS_on_joining_the_list_from_CSV_import_using_pipe_delimiter_CSV(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Member member = new Member("test", "last", "", phoneNumber, "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("welcome_message_is_received_for_SMS_on_joining_the_list_using_pipe_delimiter");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectsendWelcomeMessageCheckBox().selectAllowPipeDelimiterCheckBox()
				.clickOnUploadButton(CSVFileWriter.writePipeDelimiterCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "ACTIVE");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "1");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		Boolean ask = mPulseDB.isMemberGetSMSWelcomeMessage(this.account_id, phoneNumber);
		Assert.assertTrue(ask);
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload", "pipedelimetercsv" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_is_received_for_SMS_on_joining_the_list_from_CSV_import_after_confirm_Opt_in_using_pipe_delimiter_CSV(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String phoneNumber = RandomeUtility.getRandomPhoneNumber();
		Member member = new Member("test", "last", "", phoneNumber, "", "", "63");
		String csvFileName = UtilityMethods.getCsvFilePath(
				"welcome_message_is_received_for_SMS_on_joining_after_confirm_Opt_in_using_pipe_delimiter");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox().selectAllowPipeDelimiterCheckBox()
				.clickOnUploadButton(CSVFileWriter.writePipeDelimiterCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappMobilePhone();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getSMSSubscriptionStatus(this.account_id, phoneNumber);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB
				.getStringData("select status from sms_subscription where account_id="+this.account_id+" and mobile_phone ='" + phoneNumber + "'");
		Assert.assertEquals(status, "0");
		testReporter.log(LogStatus.INFO, "verified status from sms_subscription table");
		testReporter.log(LogStatus.INFO, "sending yes mo on " + phoneNumber + " mobile phone");
		UtilityMethods.hitMoApi(this.shortCode, phoneNumber, "yes");
		String ask = mPulseDB.getStringSMSWelcomeMessage(this.account_id, phoneNumber);
		Assert.assertTrue(ask.contains("Thanks for joining"));
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		AudienceApi.deleteMemberByPhoneNumber(phoneNumber);
	}

	@Test(groups = { "csvupload", "pipedelimetercsv", "retest30thjuly" })
	@Parameters({ "browser" })
	public void verify_that_welcome_message_with_footer_message_is_received_for_EMAIL_on_joining_the_list_from_CSV_import_after_confirm_Opt_in_using_pipe_delimiter_CSV(
			String browser) throws Exception {
		driver = BrowserFactory.getBrowser(browser);
		ExtentTest testReporter = ReporterFactory.getTest();
		testReporter.log(LogStatus.INFO, "Test started on browser " + browser);
		testReporter.assignCategory("Members CSV Upload");
		logger.info("******** Browser " + browser + " Started ********");

		String email = RandomeUtility.getRandomEmails(6);
		Member member = new Member("test", "last", email, "", "", "", "63");
		String csvFileName = UtilityMethods
				.getCsvFilePath("opt_in_message_is_received_on_CSV_import_using_pipe_delimiter");

		mapping = loginTo_mPulse_cp(driver).goToAudienceListPage().goToListSubscribersPage(this.listName)
				.clickOnImportSubscribersButton().selectNeedConfirmationCheckBox().selectAllowPipeDelimiterCheckBox()
				.clickOnUploadButton(CSVFileWriter.writePipeDelimiterCsvFileForSingleMember(csvFileName, member));

		testReporter.log(LogStatus.INFO, "clicked on csv upload button");
		mapping.mappEmail();
		mapping.clickOnUploadButton();
		mapping.acceptWarningMessage();
		String result = mapping.getTotalMemberImportResult();
		testReporter.log(LogStatus.INFO, "Csv upload result is - " + result);
		mapping.clickOnOKButton();
		String subStatus = mPulseDB.getEmailSubscriptionStatus(account_id, email);
		testReporter.log(LogStatus.INFO, "new ploaded member's subscription status is " + subStatus);
		Assert.assertEquals(subStatus, "PENDING");
		String status = mPulseDB.getStringData("select status from email_subscription where account_id="+this.account_id+" and email ='" + email + "'");
		Assert.assertEquals(status, "0");
		UtilityMethods.clickOnConfirmLinkInEmailMailinator(driver, email);
		String ask = mPulseDB.getStringEmailWelcomeMessage(email);
		Assert.assertTrue(ask.contains("Thank you for signing up and welcome"));
		testReporter.log(LogStatus.INFO, "Verified that member get Welcome Message");
		Assert.assertTrue(ask.contains("Our contact address is"),
				"Footer message is not appended in Email welcome message");
		Assert.assertTrue(ask.contains("##LIST.OPT_OUT##"), "Footer message is not appended in Email welcome message");
		testReporter.log(LogStatus.INFO, "Verified that footer is appended in email Welcome Message");
		AudienceApi.deleteMemberByEmail(email);
	}

}
