package com.mPulse.testCases;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.mPulse.factories.BrowserFactory;
import com.mPulse.factories.DatabaseFactory;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.objectRepository.HomePage;
import com.mPulse.objectRepository.LoginPage;
import com.mPulse.utility.AudienceApi;
import com.mPulse.utility.ConfigReader;
import com.mPulse.utility.ThreadSafeNumber;
import com.mPulse.utility.UtilityMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseTest {
	
	private WebDriver driver;
	Logger logger = Logger.getLogger(BaseTest.class);
	
	@BeforeSuite(alwaysRun = true)
	public synchronized void startSuite() throws Exception {
		
		System.out.println(" ##### Test Suite Started ##### ");
		System.out.println("Inside BeforeSuite");
		if(ConfigReader.getConfig("allowAllMemberToDelete").equalsIgnoreCase("true")) {
			String accountId = ConfigReader.getConfig("account_id");
			System.out.println("Deleting All audience member by audience api for account id "+accountId);
			AudienceApi.deleteAllMemberById(accountId);
		}		
	}

	@AfterSuite(alwaysRun = true)
	public synchronized void endSuite() {

		System.out.println(" ##### Test Suite Ended ##### ");
		DatabaseFactory.closeConnection();
		ReporterFactory.closeReport();

	}

	@BeforeTest(alwaysRun = true)
	@Parameters({ "browser" })
	public synchronized void startTest(String browserName, final ITestContext testContext) {

		System.out.println("----- Test '" + testContext.getName() + "' Started -----");
		System.out.println("----- Test on Browser '" + browserName + "' Started -----");

	}

	@AfterTest(alwaysRun = true)
	@Parameters({ "browser" })
	public synchronized void endTest(String browserName, final ITestContext testContext) {

		System.out.println("----- Test '" + testContext.getName() + "' Ended -----");
		BrowserFactory.closeWebDriver(browserName);
		System.out.println("----- Test on Browser '" + browserName + "' Closed -----");

	}

	@BeforeMethod(alwaysRun = true)
	public synchronized void startMethod(Method method) {
		ReporterFactory.getTest(method.getName(), "Test Method '" + method.getName() + "' Started");
		System.out.println("**** Test Method '" + method.getName() + "' Started ****");
		if (ConfigReader.getConfig("allowRandomPhone").equalsIgnoreCase("false")) {
			for (int i=0; i < ThreadSafeNumber.getAllPhone().length; i++) {
				AudienceApi.deleteMemberByPhoneNumber2(ThreadSafeNumber.getAllPhone()[i]);
			}
		}
	}

	@AfterMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public synchronized void endMethod(ITestResult result, String browserName) {

		ExtentTest testReporter = ReporterFactory.getTest();
		if (ITestResult.FAILURE == result.getStatus()) {
			System.out.println(">>>>>>>> Error :- Test Method '" + result.getName() + "' Failed <<<<<<<<");
			driver = BrowserFactory.getBrowser(browserName);
			String screenshot_path = UtilityMethods.getScreenShot(driver, "Method_" + result.getName() + "_onFailure");
			String image = testReporter.addScreenCapture(screenshot_path);
			testReporter.log(LogStatus.FAIL, "**** Test Method '" + result.getMethod().getMethodName().replace("_", " ") + "' Failed ****");
			testReporter.log(LogStatus.FAIL, result.getMethod().getMethodName(), image);
			System.out.println("ScreenShort taken on Browser - " + browserName);
		} if(ITestResult.SUCCESS == result.getStatus()){
			testReporter.log(LogStatus.PASS, "**** Test Method '" + result.getMethod().getMethodName().replace("_", " ") + "' Passed ****");
		}if(ITestResult.SKIP == result.getStatus()) {
			testReporter.log(LogStatus.INFO, "**** Test Method '" + result.getMethod().getMethodName().replace("_", " ") + "' Skipped ****");
		}
		ReporterFactory.closeTest(result.getMethod().getMethodName());
		System.out.println("**** Test Method '" + result.getName() + "' Ended ****");

	}

	public static synchronized HomePage loginTo_mPulse_cp(WebDriver driver) {
		LoginPage login;
		String cp_username = ConfigReader.getConfig("cp_username");
		String cp_password = ConfigReader.getConfig("cp_password");
		try {
			goTo_mPulse_homePage(driver, ConfigReader.getConfig("ENV"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String url = driver.getCurrentUrl();
		
		if (url.contains("apps.mpulsemobile.com/home")) {
			return new HomePage(driver);
		}
		else if (url.contains("apps.mpulsemobile.com/login")) {
			login = new LoginPage(driver);
			return login.login(cp_username, cp_password);
		}
		
		logOut(driver);
		login = new LoginPage(driver);
		return login.login(cp_username, cp_password);
	}
	
	public static synchronized HomePage loginTo_prod_mPulse_cp(WebDriver driver) {
		LoginPage login;
		String prod_cp_username = ConfigReader.getConfig("prod_cp_username");
		String prod_cp_password = ConfigReader.getConfig("prod_cp_password");
		try {
			goTo_prod_mPulse_homePage(driver, ConfigReader.getConfig("PROD_BASEURL"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logOut(driver);
		login = new LoginPage(driver);
		System.out.println("user name "+prod_cp_username);
		System.out.println("password "+prod_cp_password);
		return login.login(prod_cp_username, prod_cp_password);
	}

	public static synchronized void loginTo_mPulse_cc(WebDriver driver) {
		// driver.get(BASEURL_CC);
		// LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		// login.login(cp_username, cp_password);
	}

	public static synchronized void goTo_mPulse_homePage(WebDriver driver, String BASEURL) throws Exception {
		try {

			System.out.println("Opening mPulse Home Page");
			driver.navigate().to(ConfigReader.getConfig("mPulse_homePage"));
			Thread.sleep(3000);
			System.out.println("on mPulse Home Page");
		} catch (Exception e) {
			if (e.toString().contains("Timed out") || e.toString().contains("Permission"))
				try {
					Thread.sleep(2000);
				} catch (Exception e2) {
				}
		}

	}
	
	public static synchronized void goTo_prod_mPulse_homePage(WebDriver driver, String BASEURL) throws Exception {
		try {

			System.out.println("Opening mPulse Home Page");
			driver.navigate().to(ConfigReader.getConfig("prod_homePage"));
			System.out.println("on mPulse Home Page");
		} catch (Exception e) {
			if (e.toString().contains("Timed out") || e.toString().contains("Permission"))
				try {
					Thread.sleep(2000);
				} catch (Exception e2) {
				}
		}

	}

	public static synchronized void logOut(WebDriver driver) {
		HomePage home;
		try {
			home = new HomePage(driver);
			if (home.isLogOutLinkPresent()) {
				home.logOut();
			}
		} catch (Exception e) {
			System.out.println("Log out link not Present");
		}
	}

}
