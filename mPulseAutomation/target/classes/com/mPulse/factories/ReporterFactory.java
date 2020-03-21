package com.mPulse.factories;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ReporterFactory {
	public static ExtentReports reporter;
	public static Map<Long, String> threadToExtentTestMap = new HashMap<Long, String>();
	public static Map<String, ExtentTest> nameToTestMap = new HashMap<String, ExtentTest>();

	private synchronized static ExtentReports getReport() {
		if (reporter == null) {
			reporter = new ExtentReports("testReports" + File.separator + getFileName() +".html", true, DisplayOrder.NEWEST_FIRST);
		}
		return reporter;
	}

	public synchronized static ExtentTest getTest(String testName, String testDescription) {
		if (!nameToTestMap.containsKey(testName)) {
			Long threadID = Thread.currentThread().getId();
			ExtentTest test = getReport().startTest(testName.replace("_", " "), testDescription);
			nameToTestMap.put(testName, test);
			threadToExtentTestMap.put(threadID, testName);
		}
		return nameToTestMap.get(testName);
	}

	public synchronized static ExtentTest getTest(String testName) {
		return getTest(testName, "");
	}
	
	public synchronized static ExtentTest getTest() {
		Long threadID = Thread.currentThread().getId();

		if (threadToExtentTestMap.containsKey(threadID)) {
			String testName = threadToExtentTestMap.get(threadID);
			return nameToTestMap.get(testName);
		}
		return null;
	}

	public synchronized static void closeTest(String testName) {

		if (!testName.isEmpty()) {
			ExtentTest test = getTest(testName);
			getReport().endTest(test);
		}
	}

	public synchronized static void closeTest(ExtentTest test) {
		if (test != null) {
			getReport().endTest(test);
		}
	}

	public synchronized static void closeTest() {
		ExtentTest test = getTest();
		closeTest(test);
	}

	public synchronized static void closeReport() {
		if (reporter != null) {
			reporter.flush();
			reporter.close();
		}
	}
	
	private static String getFileName() {
		String fileName = (new Date()).toString().replace(" ", "_").replace(":", "-").trim();
		return "TestReport_"+fileName;
	}
}
