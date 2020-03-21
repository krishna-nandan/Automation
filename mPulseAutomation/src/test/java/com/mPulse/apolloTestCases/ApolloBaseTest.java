package com.mPulse.apolloTestCases;

import com.mPulse.factories.DatabaseFactory;
import com.mPulse.factories.ReporterFactory;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class ApolloBaseTest {

    @BeforeSuite(alwaysRun = true)
    public synchronized void startSuite() {
        System.out.println(" ##### Apollo Test Suite Started ##### ");
        System.out.println("Inside BeforeSuite");
    }

    @AfterSuite(alwaysRun = true)
    public synchronized void endSuite() {
        System.out.println(" ##### Apollo Test Suite Ended ##### ");
        DatabaseFactory.closeConnection();
        ReporterFactory.closeReport();
    }

    @BeforeTest(alwaysRun = true)
    public synchronized void startTest(final ITestContext testContext) {

        System.out.println("----- Test '" + testContext.getName() + "' Started -----");
    }

    @AfterTest(alwaysRun = true)
    public synchronized void endTest(final ITestContext testContext) {

        System.out.println("----- Test '" + testContext.getName() + "' Ended -----");

    }
    
	@BeforeMethod(alwaysRun = true)
	public synchronized void startMethod(Method method) {
		ReporterFactory.getTest(method.getName(), "Test Method '" + method.getName() + "' Started");
		System.out.println("**** Test Method '" + method.getName() + "' Started ****");
	}

	@AfterMethod(alwaysRun = true)
	public synchronized void endMethod(ITestResult result) {

		ExtentTest testReporter = ReporterFactory.getTest();
		if (ITestResult.FAILURE == result.getStatus()) {
			System.out.println(">>>>>>>> Error :- Test Method '" + result.getName() + "' Failed <<<<<<<<");
			testReporter.log(LogStatus.FAIL, "**** Test Method '" + result.getMethod().getMethodName().replace("_", " ") + "' Failed ****");
		} if(ITestResult.SUCCESS == result.getStatus()){
			testReporter.log(LogStatus.PASS, "**** Test Method '" + result.getMethod().getMethodName().replace("_", " ") + "' Passed ****");
		}if(ITestResult.SKIP == result.getStatus()) {
			testReporter.log(LogStatus.INFO, "**** Test Method '" + result.getMethod().getMethodName().replace("_", " ") + "' Skipped ****");
		}
		ReporterFactory.closeTest(result.getMethod().getMethodName());
		System.out.println("**** Test Method '" + result.getName() + "' Ended ****");

	}
}
