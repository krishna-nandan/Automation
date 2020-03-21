package com.mPulse.testCases;

import java.util.ArrayList;

import com.mPulse.factories.mPulseDB;
import com.mPulse.utility.AudienceApi;

public class UnitTest {
	
	public static void main(String[] args) throws Exception {
		ArrayList<String> audience_ids = mPulseDB.getAllRowData("select id from audience_member where deleted = false and mobile_phone is not null and account_id=1580 limit 5");
		AudienceApi.deleteAllMemberById(audience_ids);
		System.out.println("data is - "+audience_ids);
	}
}
