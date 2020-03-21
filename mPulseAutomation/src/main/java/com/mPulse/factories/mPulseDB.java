package com.mPulse.factories;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mPulseDB {

	private static Connection conn;

	public static synchronized String getStringData(String query) throws Exception {

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}

		return value;
	}

	public static synchronized int updateData (String query) throws Exception {

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
			int r = stmt.executeUpdate(query);
			Thread.sleep(3000);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) { }
		return r;
	}
	
	public static synchronized String selectNow() throws Exception {

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery("select now()");
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}

		return value;
	}

	public static synchronized String getSMSSubscriptionStatus(String accountID, String phoneNumber) throws Exception {

		String query = "select sms_status from member_subscription where account_id=" + accountID
				+ " and audience_member_id in (select id from audience_member where account_id=" + accountID
				+ " and mobile_phone='" + phoneNumber + "')";

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}

	public static synchronized String getEmailSubscriptionStatus(String accountID, String emailID) throws Exception {

		String query = "select email_status from member_subscription where account_id=" + accountID
				+ " and audience_member_id in (select id from audience_member where account_id=" + accountID
				+ " and email='" + emailID + "')";

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	public static synchronized String getAppmailSubscriptionStatus(String accountID, String appMemberId) throws Exception {

		String query = "select secure_message_status from member_subscription where account_id=" + accountID
				+ " and audience_member_id in (select id from audience_member where account_id=" + accountID
				+ " and app_member_id='" + appMemberId + "')";

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}

	public static synchronized String getEmailUploadWelcomeMessageCount(String accountID, String emailID)
			throws Exception {

		String query = "select email_status from member_subscription where account_id=" + accountID
				+ " and audience_member_id in (select id from audience_member where account_id=" + accountID
				+ " and email='" + emailID + "')";

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}

	public static synchronized Boolean isMemberGetEmailConfirmMessage(String accountID, String emailID)
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		String query = "select count(*) from email_upload_confirm_message ecm join email_sent es on ecm.id=es.message_id and ecm.account_id=es.account_id where es.account_id="
				+ accountID + " and es.email='" + emailID + "'";
		testReporter.log(LogStatus.INFO, "Looking for entry in email_upload_confirm_message with query - " + query);
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		testReporter.log(LogStatus.INFO, "Query result is - " + value);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		if (value.equals("1"))
			return true;
		return false;
	}

	public static synchronized Boolean isMemberGetSMSConfirmMessage(String accountID, String phoneNumber)
			throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		
		String query = "select count(*) from sms_upload_confirm_message scm join mt_tracking mt on scm.id=mt.message_id and scm.account_id=mt.account_id where mt.account_id="
		 + accountID + " and mt.phone_number='" + phoneNumber + "' and mt.audience_member_id in (select id from audience_member where account_id=" + accountID + " and mobile_phone='" + phoneNumber + "')";
		
		testReporter.log(LogStatus.INFO, "Looking for entry in sms_upload_confirm_message with query - " + query);
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		testReporter.log(LogStatus.INFO, "Query result is - " + value);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return (value.equals("1"));
	}

	public static synchronized Boolean isMemberGetSMSWelcomeMessage(String accountID, String phoneNumber)
			throws Exception {

		String query = "select count(*) from sms_upload_welcome_message swm join mt_tracking mt on swm.id=mt.message_id and swm.account_id=mt.account_id where mt.account_id="	
		+ accountID + " and mt.phone_number='" + phoneNumber + "' and mt.audience_member_id in (select id from audience_member where account_id=" + accountID + " and mobile_phone='" + phoneNumber + "')";

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return (value.equals("1"));
	}

	public static synchronized Boolean isMemberGetEmailWelcomeMessage(String accountID, String emailID)
			throws Exception {

		String query = "select count(*) from email_upload_welcome_message ewm join email_sent es on ewm.id=es.message_id and ewm.account_id=es.account_id where es.account_id="
				+ accountID + " and es.email='" + emailID + "'";

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		if (value.equals("1"))
			return true;
		return false;
	}

	public static String getStringSMSWelcomeMessage(String accountID, String phoneNumber) throws Exception {
		String query = "select text from mt_tracking mt join list_sms_message lsm on mt.message_id=lsm.id and mt.account_id=lsm.account_id where mt.phone_number ='"
				+phoneNumber+"' and mt.message_type='list_sms_message' and mt.account_id="+accountID;
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	public static synchronized Boolean isMessageEntryInMT_Tracking(String accountID, String member_id, String campName)
			throws Exception {
		
		ExtentTest testReporter = ReporterFactory.getTest();
		String query = "select count(id) from mt_tracking where account_id="+accountID+" and audience_member_id="+member_id+" and message_id in "
				+ "(select id from message where campaign_id in(select id from campaign where name ilike '"+campName+"'))";
		testReporter.log(LogStatus.INFO, "Looking for entry in mt_tracking with query - " + query);
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		Thread.sleep(4000);
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(10000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		testReporter.log(LogStatus.INFO, "Query result is - " + value);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		if (Integer.parseInt(value) == 0)
			return false;
		return true;
	}
	
	public static synchronized String getMessageSentContentFromMemberActivity(String accountID, String member_id, String campName) throws Exception {
		
		String query = "select content->> 'content' from member_activity where original_id ="
		+getMtTrackingId(getMessageId(accountID, getCampaignIdByName(accountID, campName)), member_id);

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	private static synchronized String getCampaignIdByName(String accountID, String campName) throws Exception {
		
		String query = "select id from campaign where name ilike '"+campName+"' and account_id ="+accountID;
		System.out.println(" String query "+ query);
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	private static synchronized String getMessageId(String accountID, String campID) throws Exception {
		
		String query = "select id from message where campaign_id = "+campID+" and account_id ="+accountID;

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	private static synchronized String getMtTrackingId(String messageID, String MemberID) throws Exception {
		
		String query = "select id from mt_tracking where message_id = "+messageID+" and audience_member_id ="+MemberID+" limit 1";
		System.out.println("String query 2 "+ query);
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	public static String getStringEmailWelcomeMessage(String email) throws Exception {
		String query = "select text_body from email_sent es join list_email_welcome_message lsm on es.message_id=lsm.id and es.account_id=lsm.account_id where es.email ='"+email+"'";
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		System.out.println("Email Text Value is - "+value);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	public static int getTotalActiveMemberCount(String accountId) throws Exception {
		ExtentTest testReporter = ReporterFactory.getTest();
		String query = "select count(id) from audience_member where deleted = 'false' and account_id ="+accountId;
		testReporter.log(LogStatus.INFO, "Looking for entry in total active member count with query - " + query);
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		testReporter.log(LogStatus.INFO, "Query result is - " + query);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return Integer.parseInt(value);
	}

	public static ResultSet getResultSet(String query) throws Exception {

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet r = stmt.executeQuery(query);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return r;
	}
	
	public static String getResultInString(String query) throws Exception {
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return value;
	}
	
	public static int getResultInInteger(String query) throws Exception {
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return Integer.parseInt(value);
	}
	
	public static int getCountInAPIRequestLog(String query) throws Exception {
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(12000);
			System.out.println("Loop count "+x+1);
			x=x+1;
		} while (r.next() == false && x<=20);
		String value = r.getString(1);
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return Integer.parseInt(value);
	}
	
	public static Map<String, String> getResultMap(String query) throws Exception {
		
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		Map<String, String> result = new HashMap<String, String>();
		ResultSetMetaData rsmd = r.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		for (int i = 1; i <= columnCount; i++ ) {
		  String name = rsmd.getColumnName(i);
		  result.put(name, r.getString(i));
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return result;
	}
	
	public static synchronized Boolean isRecoredPresent(String query)
			throws Exception {

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
		} while (r.next() == false && x<=20);
		int value = r.getRow();
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		if (value >= 1)
			return true;
		return false;
	}
	
	public static synchronized ArrayList<String> getAllRowData(String query)
			throws Exception {

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		ArrayList<String> resultList = new ArrayList<String>();
		boolean istrue = false;
		
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
			istrue = r.next();
			if (istrue) {
				resultList.add(r.getString(1));
			}
		} while (istrue == false && x<=20);
		
		String result;
		while (r.next()) {
		        result = r.getString(1); 
		        resultList.add(result);
		    }
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return resultList;
	}
	
	public static synchronized ArrayList<String> getAllRowData2(String query)
			throws Exception {

		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		ArrayList<String> resultList = new ArrayList<String>();
		
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
			
		} while (r.next() == false && x<=20);
		
		String result;
		
		do {
			result = r.getString(1); 
	        resultList.add(result);
		} while(r.next());

		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		
		return resultList;
	}
	
	public static synchronized ArrayList<String> getAllActiveAudienceMemberIdByLimit(String accountId, String limit)
			throws Exception {
		String query = "select id from audience_member where deleted = false and account_id="+accountId+" and encrypted_data is null order by id desc limit "+limit;
		conn = DatabaseFactory.getConnection();
		Statement stmt;
		stmt = conn.createStatement();
		int x=0;
		ResultSet r;
		ArrayList<String> resultList = new ArrayList<String>();
		boolean istrue = false;
		
		do {
			r = stmt.executeQuery(query);
			Thread.sleep(3000);
			x=x+1;
			istrue = r.next();
			if (istrue) {
				resultList.add(r.getString(1));
			}
		} while (istrue == false && x<=20);
		
		String result;
		while (r.next()) {
		        result = r.getString(1); 
		        resultList.add(result);
		    }
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
		}
		return resultList;
	}

}
