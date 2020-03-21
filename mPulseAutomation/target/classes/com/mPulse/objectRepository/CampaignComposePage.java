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

public class CampaignComposePage {
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	JavascriptExecutor js;
	
	@FindBy (xpath = "//a[@href='/home' and contains(text(), 'mPulse Mobile')]")
	private WebElement homePageLink;
	
	@FindBy (xpath = "//div[@id='common_workarea']/div[@class='progress-bar']/div[@class='loadingmessage']/span")
	private WebElement progressBarLoader;
	
	@FindBy(xpath = "//li[@id='email_tab_header' and contains(@style, 'list-item')]")
	private WebElement emailTab;

	@FindBy(xpath = "//li[@id='sms_tab_header' and contains(@style, 'list-item')]")
	private WebElement smsTab;

	@FindBy(xpath = "//li[@id='appmail_tab_header' and contains(@style, 'list-item')]")
	private WebElement appmailTab;

	@FindBy(xpath = "//li[@id='pn_tab_header' and contains(@style, 'list-item')]")
	private WebElement pnTab;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Next')]/ancestor::button[@id='message_submit']")
	private WebElement campaignNextButton;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Save')]/ancestor::button[@class='icon_button only_save shift_right']")
	private WebElement campaignSaveButton;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Next')]/ancestor::button[@id='message_submit']/following-sibling::a[@class='common_form_cancel']")
	private WebElement campaignBackButton;

	public CampaignComposePage(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isEmailTabPresent() {
		if(this.emailTab.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isSmsTabPresent() {
		if(this.smsTab.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isAppmailTabPresent() {
		if(this.appmailTab.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isPnTabPresent() {
		if(this.pnTab.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignSaveButtonPresent() {
		if(this.campaignSaveButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignBackButtonPresent() {
		if(this.campaignBackButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignNextButtonPresent() {
		if(this.campaignNextButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public CampaignSchedulePage clickOnNextButton() throws InterruptedException {
		if(this.isCampaignNextButtonPresent()) {
			this.campaignNextButton.click();
			Thread.sleep(16000);
			DriverMethods.waitForElementUntillDisappear(this.driver, this.progressBarLoader);
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
		}
		return new CampaignSchedulePage(this.driver);
	}
	
	public CampaignComposePage clickForEmailBug() throws InterruptedException {
		if(this.isCampaignNextButtonPresent()) {
			this.campaignNextButton.click();
			Thread.sleep(10000);
			DriverMethods.waitForElementUntillDisappear(this.driver, this.progressBarLoader);
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
		}
		return new CampaignComposePage(this.driver);
	}
	
	public EmailMessageComposePage goToEmailComposePage() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new EmailMessageComposePage(this.driver);
	}
	
	public SmsMessageComposePage goToSmsComposePage() throws InterruptedException {
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new SmsMessageComposePage(this.driver);
	}
	
	public AppmailMessageComposePage goToAppmailComposePage() {
		return new AppmailMessageComposePage(this.driver);
	}
	
	public EmailMessageComposePage clickOnEmailTab() throws InterruptedException {
		if(this.isEmailTabPresent()) {
			this.emailTab.click();
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
			Thread.sleep(5000);
		}
		else {
			System.out.println("Error : Not able to click on SMS Tab");
		}
		return new EmailMessageComposePage(this.driver);
	}
	
	public SmsMessageComposePage clickOnSmsTab() throws InterruptedException {
		Thread.sleep(5000);
		if(this.isSmsTabPresent()) {
			act.moveToElement(this.smsTab).build().perform();
			this.smsTab.click();
			DriverMethods.isElementPresent(driver, homePageLink);
			Thread.sleep(12000);
		}
		else {
			System.out.println("Error : Not able to click on SMS Tab");
		}
		return new SmsMessageComposePage(this.driver);
	}
	
	public AppmailMessageComposePage clickOnAppmailTab() throws InterruptedException {
		if(this.isAppmailTabPresent()) {
			act.moveToElement(this.appmailTab).build().perform();
			this.appmailTab.click();
			DriverMethods.isElementPresent(driver, homePageLink);
			Thread.sleep(12000);
		}
		else {
			System.out.println("Error : Not able to click on appmail Tab");
		}
		return new AppmailMessageComposePage(this.driver);
	}
	
	class AppmailCampaignComposePage{
		public AppmailCampaignComposePage() {
			PageFactory.initElements(driver, this);
			testReporter = ReporterFactory.getTest();
		}
		class AppmailTriggers{
			public AppmailTriggers() {
				PageFactory.initElements(driver, this);
				testReporter = ReporterFactory.getTest();
			}
		}
	}
	
	class PNCampaignComposePage{
		public PNCampaignComposePage() {
			PageFactory.initElements(driver, this);
			testReporter = ReporterFactory.getTest();
		}
		class PNTriggers{
			public PNTriggers() {
				PageFactory.initElements(driver, this);
				testReporter = ReporterFactory.getTest();
			}
		}
	}
}
