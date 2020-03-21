package com.mPulse.objectRepository;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.utility.DriverMethods;
import com.relevantcodes.extentreports.ExtentTest;

public class CampaignSchedulePage {
	
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	JavascriptExecutor js;
	
	@FindBy (xpath = "//a[@href='/home' and contains(text(), 'mPulse Mobile')]")
	private WebElement homePageLink;
	
	@FindBy (xpath = "//div[@id='common_workarea']/div[@class='progress-bar']/div[@class='loadingmessage']/span")
	private WebElement progressBarLoader;
	
	@FindBy (id = "campaign_submit")
	private WebElement launchCampaignButton;
	
	@FindBy (id = "campaign_save")
	private WebElement saveAsDraftButton;
	
	@FindBy (xpath = "//input[@name='endType' and @value='SCHEDULED']")
	private WebElement endDateRadioButton;
	
	@FindBy (id = "sms_endDate")
	private WebElement endDateInputBox;
	
	@FindBy (id = "sms_endTime")
	private WebElement endTimeInputBox;
	
	@FindBy (xpath = "//li[@id='schedule_step']//a[@class='common_form_cancel']")
	private WebElement backButton;
	
	public CampaignSchedulePage(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isLaunchCampaignButtonPresent() {
		if(this.launchCampaignButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isSaveAsDraftButtonPresent() {
		if(this.saveAsDraftButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEndDateRadioButtonPresent() {
		if(this.endDateRadioButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isBackButtonPresent() {
		if(this.backButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEndDateInputBoxPresent() {
		if(this.endDateInputBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEndTimeInputBoxPresent() {
		if(this.backButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public CommunicationPage clickOnLaunchCampaignButton() throws InterruptedException {
		Thread.sleep(6000);
		if(this.isLaunchCampaignButtonPresent()) {
			act.moveToElement(this.launchCampaignButton).build().perform();
			this.launchCampaignButton.click();
			Thread.sleep(15000);
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
			return new CommunicationPage(this.driver);
		}
		return new CommunicationPage(this.driver);
		
	}
	
	public CommunicationPage clickOnSaveAsDraftButton() throws InterruptedException {
		DriverMethods.waitForElementUntillDisappear(this.driver, this.progressBarLoader);
		if(this.isSaveAsDraftButtonPresent()) {
			act.moveToElement(this.saveAsDraftButton).build().perform();
			this.saveAsDraftButton.click();
			Thread.sleep(2000);
			DriverMethods.waitForElementUntillDisappear(this.driver, this.progressBarLoader);
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
			return new CommunicationPage(this.driver);
		}
		return new CommunicationPage(this.driver);
		
	}
	
	public CampaignComposePage clickOnBackButton() {
		if(this.isLaunchCampaignButtonPresent()) {
			act.moveToElement(this.backButton).build().perform();
			this.backButton.click();
			return new CampaignComposePage(this.driver);
		}
		return new CampaignComposePage(this.driver);
		
	}
	
	public CampaignSchedulePage checkEndDateRadioButton() {
		if(this.isEndDateRadioButtonPresent()) {
			act.moveToElement(this.endDateRadioButton).build().perform();
			this.endDateRadioButton.click();
			return new CampaignSchedulePage(this.driver);
		}
		return new CampaignSchedulePage(this.driver);
		
	}
	
	public CampaignSchedulePage enterEndDate(String date) {
		if(this.isEndDateInputBoxPresent()) {
			act.moveToElement(this.endDateInputBox).build().perform();
			this.endDateInputBox.sendKeys(date);
			return new CampaignSchedulePage(this.driver);
		}
		return new CampaignSchedulePage(this.driver);		
	}
	
	public CampaignSchedulePage enterEndTime(String time) {
		if(this.isEndDateInputBoxPresent()) {
			act.moveToElement(this.endTimeInputBox).build().perform();
			this.endTimeInputBox.sendKeys(time);
			return new CampaignSchedulePage(this.driver);
		}
		return new CampaignSchedulePage(this.driver);		
	}
	
}
