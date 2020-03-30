package com.mPulse.testCases;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.mPulse.factories.BrowserFactory;

public class UnitTest extends BaseTest{
	
	@Test(groups = { "sanity_krishna" })
	@Parameters({ "browser" })
	public void test_one(String browser) {
		WebDriver driver = BrowserFactory.getBrowser(browser);
		loginTo_mPulse_cp(driver);
		String title = driver.getTitle();
		String url = driver.getCurrentUrl();
		
		System.out.println("**************** Current url is - "+url);
		System.out.println("**************** Current title is - "+title);
	}
	
	@Test(groups = { "sanity_krishna_headless" })
	@Parameters({ "browser" })
	public void test_one_2(String browser) {
		WebDriver driver = BrowserFactory.getBrowser(browser);
		loginTo_mPulse_cp(driver);
		String title = driver.getTitle();
		String url = driver.getCurrentUrl();
		
		System.out.println("**************** Current url is - "+url);
		System.out.println("**************** Current title is - "+title);
	}
}
