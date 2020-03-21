package com.mPulse.utility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class RetryAnalyzer implements IRetryAnalyzer {

	int counter = 0;
	int retryLimit = 1;
	/*
	 * This method decides how many times a test needs to be rerun.
	 * TestNg will call this method every time a test fails. So we 
	 * can put some code in here to decide when to rerun the test.
	 * 
	 * Note: This method will return true if a tests needs to be retried
	 * and false it not.
	 *
	 */

	@Override
	public boolean retry(ITestResult result) {
		ExtentTest testReporter = ReporterFactory.getTest();
		
		if(counter < retryLimit)
		{
			counter++;
			testReporter.log(LogStatus.INFO, "Inside retry method : retry count "+counter);
			return true;
		}
		return false;
	}
}