package com.mPulse.testCases;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.mPulse.utility.ExcelDataProvider;
import com.mPulse.utility.ThreadSafeNumber;

public class Test {

	@DataProvider
	public Object[][] Authentication() throws Exception {

		Object[][] testObjArray = ExcelDataProvider.getCellData("audience_api_test_data.xlsx",
				"Sheet1");
		return (testObjArray);
	}
	
	@org.testng.annotations.Test(dataProvider="Authentication")
	 
    public void Registration_data(String sUserName,String sPassword, String testcases, String i, String xy)throws  Exception{			
 
        System.out.println(sUserName);
        System.out.println(sPassword);
        System.out.println(testcases);
        System.out.println(i);
        System.out.println(xy);
	}
}