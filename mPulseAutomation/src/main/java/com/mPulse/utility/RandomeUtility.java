package com.mPulse.utility;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomeUtility {
	
	public static String getRandomEmails(int length) {
		return RandomStringUtils.randomAlphanumeric(length).toLowerCase()+"@mailinator.com";
	}
	
	public static String getRandomString() {
		return "kn"+RandomStringUtils.randomAlphanumeric(6).toLowerCase();
	}
	
	public static String getRandomEventName() {
		return "event_"+RandomStringUtils.randomAlphanumeric(6).toLowerCase();
	}
	
	public static String getRandomEventAttributeName() {
		return "attri_"+RandomStringUtils.randomAlphanumeric(6).toLowerCase();
	}
	
	public static String getRandomPhoneNumber() {
		if (ConfigReader.getConfig("allowRandomPhone").equalsIgnoreCase("true")) {
			Random rand = new Random();
			int num = rand.nextInt(90000000) + 10000000;
			String finalnumber = "199"+num;
			return finalnumber;
		}
		else {
			return ThreadSafeNumber.getRandomPhone();
		}
	}
	
	public static String getRandomNumber() {
		Random rand = new Random();
		int num = rand.nextInt(900) + 100;
		String finalnumber = "1"+num;
		return finalnumber;
	}
	
	public static String getRandomAppMemberID() {
		return RandomStringUtils.randomAlphanumeric(6).toLowerCase()+"app";
	}
	
	public static String getRandomClientMemberID() {
		return RandomStringUtils.randomAlphanumeric(6).toLowerCase()+"client";
	}
}
