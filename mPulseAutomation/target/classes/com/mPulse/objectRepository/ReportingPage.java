package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;

public class ReportingPage {
	
	WebDriver driver;
	ExtentTest testReporter;
	
	@FindBy(xpath = "//tbody[@id='messages_report']/tr[@class='channel-sms']/td[2]/div/a")
	private WebElement smsReportLink;

	public ReportingPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isSmsReportLinkPresent() {
		if(this.smsReportLink.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public SMSMessageReportPage clickOnSMSReportLink() {
		if(this.isSmsReportLinkPresent()) {
			this.smsReportLink.click();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Not able to click on new Campaign button, new Campaign button is not enabled");
		}
		return new SMSMessageReportPage(this.driver);
	}
}
