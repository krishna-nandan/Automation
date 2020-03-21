package com.mPulse.utility;

public class Resources {

	public static String accid = ConfigReader.getConfig("apollo_account_id");
	public static String accid2 = ConfigReader.getConfig("apollo_account_id2");
	public static String listid = ConfigReader.getConfig("apollo_list_id");
	public static String listidw = ConfigReader.getConfig("apollo_list_idw");
	public static String customfieldid = ConfigReader.getConfig("apollo_custom_field_id");
	public static String memberid = ConfigReader.getConfig("apollo_member_id");
	public static String segmentid = ConfigReader.getConfig("apollo_segment_id");
	public static String segment_name = ConfigReader.getConfig("apollo_segment_name");
	public static String customfieldtype = ConfigReader.getConfig("apollo_custom_field_type");
	public static String shortcode_id = ConfigReader.getConfig("apollo_shortcode_id");


	public static String accountWiseList() {
		String res = "api/accounts/" + accid + "/lists?limit=100&offset=0";

		return res;
	}

	public static String shortcodeValidation() {
		String res = "api/accounts/" + accid + "/lists/validate/keyword";

		return res;
	}

	public static String totalListMemberCountGet() {
		String res = "api/accounts/" + accid + "/lists/" + listid + "/members/count";

		return res;
	}

	public static String getMemberCountWrong() {
		String res = "api/accounts/" + accid + "/lists/" + listidw + "/members/count";

		return res;
		
	}
	
	
	public static String listSmsGet() {
		String res = "api/accounts/" + accid + "/lists/" + listid + "/list_sms_messages";

		return res;
		
	}
	
	
	public static String listPost() {
		String res = "api/accounts/" + accid + "/lists/new";

		return res;
		
	}
	
	public static String customFieldSingleGet() {
		String res = "api/accounts/" + accid + "/custom_fields/" + customfieldid;

		return res;
		
	}
	
	public static String customFieldAllGet() {
		String res = "api/accounts/" + accid + "/custom_fields";

		return res;
		
	}
	
	public static String customFieldPost() {
		String res = "api/accounts/" + accid + "/custom_fields";

		return res;
		
	}
	
	public static String customFieldPut() {
		String res = "api/accounts/" + accid + "/custom_fields/" + customfieldid;

		return res;
		
	}
	
	public static String listMemberByChannelSmsGet() {
		String res = "api/accounts/" + accid + "/lists/" + listid + "/members/sms/count";

		return res;
		
	}
	
	public static String listMemberByChannelEmailGet() {
		String res = "api/accounts/" + accid + "/lists/" + listid + "/members/email/count";

		return res;
		
	}
	
	public static String listMemberByChannelSecureMessageGet() {
		String res = "api/accounts/" + accid + "/lists/" + listid + "/members/secure_message/count";

		return res;
		
	}
	
	public static String listMemberByChannelPNGet() {
		String res = "api/accounts/" + accid + "/lists/" + listid + "/members/pn/count";

		return res;
		
	}
	
	public static String testGroupMembers() {
		String res = "api/accounts/" + accid + "/message-test-group";

		return res;
		
	}
	
	public static String testGroupMemberDelete() {
		String res = "api/accounts/" + accid + "/message-test-group/"+memberid;

		return res;
		
	}
	
	public static String testGroupLaunch() {
		String res = "api/accounts/" + accid + "/message-test-group/launch";

		return res;
		
	}
	
	public static String segments() {
		String res = "api/accounts/" + accid + "/segments";

		return res;
	}
	
	public static String segmentValidateName() {
		String res = "api/accounts/" + accid + "/segments/validate/name?name="+segment_name;

		return res;
	}
	
	public static String segmentSearchName() {
		String res = "api/accounts/" + accid + "/segments/search?name="+segment_name;

		return res;
	}
	
	public static String segmentId() {
		String res = "api/accounts/" + accid + "/segments/"+segmentid;

		return res;
	}
	
}
