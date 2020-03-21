package com.mPulse.objectRepository;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;

public class SMSMessageReportPage {
	
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	JavascriptExecutor js;
	
	@FindBy(xpath = "//div[@id='sms_link_performance']/center/ul[@class='boxes ']/li[4]")
	private WebElement clickCount;

	public SMSMessageReportPage(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public int getLinkClickCount() {
		String count = clickCount.getText();
		return Integer.parseInt(count);
	}

}
