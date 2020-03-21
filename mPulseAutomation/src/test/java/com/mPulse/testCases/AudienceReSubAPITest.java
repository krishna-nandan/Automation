package com.mPulse.testCases;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;

public class AudienceReSubAPITest extends BaseTest {
	
	String baseUrlAudience = "https://" + ConfigReader.getConfig("API_URL") + "/accounts/"
			+ ConfigReader.getConfig("account_id") + "/members";
	String basicAuth = UtilityMethods.getBasicAuth(ConfigReader.getConfig("api_username"),
			ConfigReader.getConfig("api_password"));

	String listId = ConfigReader.getConfig("audienceApiTest_id");
	String accountId = ConfigReader.getConfig("account_id");
	String sc = ConfigReader.getConfig("audienceApiTest_sc");
	Logger logger = Logger.getLogger(AudienceReSubAPITest.class);

	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_member_must_get_in_pending_status_when_trying_to_re_subscribe_existing_unsubscription_member_for_email_channel_if_using_X_Ms_List_Confirm_true_and_X_Ms_Audience_Update_header_with_value_resub() throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
	}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_member_must_get_in_pending_status_when_trying_to_re_subscribe_existing_unsubscription_member_for_sms_channel_if_using_X_Ms_List_Confirm_true_and_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_member_must_get_welcome_message_when_trying_to_re_subscribe_existing_unsubscription_member_for_email_channel_if_using_Send_Welcome_Message_true_and_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_member_must_get_welcome_message_when_trying_to_re_subscribe_existing_unsubscription_member_for_sms_channel_if_using_Send_Welcome_Message_true_and_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_can_re_subscribe_already_unsubscribed_member_in_a_list_in_appmail_channel_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_can_re_subscribe_already_unsubscribed_member_in_a_list_in_email_channel_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_can_re_subscribe_already_unsubscribed_member_in_a_list_in_sms_channel_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_can_re_subscribe_existing_unsubscribed_member_in_all_channel_in_one_request_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_can_not_subscribe_existing_member_for_first_time_in_a_list_in_appmail_channel_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_can_not_subscribe_existing_member_for_first_time_in_a_list_in_email_channel_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_can_not_subscribe_existing_member_for_first_time_in_a_list_in_sms_channel_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_get_error_message_if_trying_to_re_subscribe_existing_subscribed_member_in_same_list_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}
	
	@Test(groups = { "sanity", "resubapi" })
	public void Verify_that_user_get_error_message_if_trying_to_re_subscribe_existing_subscription_pending_member_in_same_list_using_X_Ms_Audience_Update_header_with_value_resub() throws Exception {}

}
