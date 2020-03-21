package com.mPulse.objectRepository;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.DatabaseFactory;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.utility.DriverMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class CommunicationPage {

	WebDriver driver;
	ExtentTest testReporter;
	
	@FindBy (xpath = "//a[@href='/home' and contains(text(), 'mPulse Mobile')]")
	private WebElement homePageLink;
	
	@FindBy (id = "list_campaign_new")
	private WebElement newCampaignButton;
	
	@FindBy (xpath = "//form[@id='form']/button")
	private WebElement campaignSearchButton;
	
	@FindBy (id = "list_campaign_name")
	private WebElement campaignNameTextBox;
	
	@FindBy (id = "list_campaign_start_date")
	private WebElement campaignStartDate;
	
	@FindBy (id = "list_campaign_end_date")
	private WebElement campaignEndDate;
	
	@FindBy (xpath = "//div[@class='ui-dialog-buttonset']/button[contains(@class, 'submit')]")
	private WebElement uiDialogYesButton;
	
	@FindBy (xpath = "//div[@class='ui-dialog-buttonset']/button")
	private WebElement campaignCloneOkButton;

	public CommunicationPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isCampaignSearchButtonPresent() {
		if(this.campaignSearchButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isNewCampaignButtonPresent() {
		if(this.newCampaignButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignNameTextBoxPresent() {
		if(this.campaignNameTextBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignStartDatePresent() {
		if(this.campaignStartDate.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignEndDatePresent() {
		if(this.campaignEndDate.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignCloneOkButtonPresent() {
		if(this.campaignCloneOkButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public CommunicationPage clickOnCampaignSearchButton() {
		if(this.isCampaignSearchButtonPresent()) {
			this.campaignSearchButton.click();
			testReporter.log(LogStatus.INFO, "Clicked on campaign search button");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Campaign search button not present");
		}
		return new CommunicationPage(this.driver);
	}
	
	public CommunicationPage clickOnCampaignCloneOkButton() {
		if(this.isCampaignCloneOkButtonPresent()) {
			this.campaignCloneOkButton.click();
			testReporter.log(LogStatus.INFO, "Clicked on campaign clone OK button");
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Campaign Clone OK button not present");
		}
		return new CommunicationPage(this.driver);
	}
	
	public CommunicationPage enterCampaignNameInSearchTextBox(String campaignName) {
		if(this.isCampaignNameTextBoxPresent()) {
			this.campaignNameTextBox.clear();
			this.campaignNameTextBox.sendKeys(campaignName);
			testReporter.log(LogStatus.INFO, "campaign name entered in text box name - "+campaignName);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Not able to click on Campaign search button, Campaign search button is not enabled");
		}
		return new CommunicationPage(this.driver);
	}
	
	public CommunicationPage enterCampaignStartDate(String startDate) {
		if(this.isCampaignStartDatePresent()) {
			this.campaignStartDate.clear();
			this.campaignStartDate.sendKeys(startDate);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Not able to enter Campaign Start date");
		}
		return new CommunicationPage(this.driver);
	}
	
	public CommunicationPage enterCampaignEndDate(String endDate) {
		if(this.isCampaignEndDatePresent()) {
			this.campaignEndDate.clear();
			this.campaignEndDate.sendKeys(endDate);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Not able to enter Campaign End date");
		}
		return new CommunicationPage(this.driver);
	}
	
	public boolean isCampaignPresentByName(String campName) {
		String xpath = "//*[@id='list_campaign_data']/tbody//a[contains(text(), '" + campName + "')]";
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DriverMethods.isElementPresent(this.driver, this.homePageLink);
		if (this.driver.findElement(By.xpath(xpath)).isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isUiDialogYesButton() {
		if (this.uiDialogYesButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public CommunicationPage clickOnCampaignRetireLink(String campName) {
		if (this.isCampaignPresentByName(campName)) {
			String campId = this.getCampaignIdByName(campName);
			String xpath = "//*[@id='list_campaign_data']/tbody//a[contains(text(), '"+campName+"')]/following::td/a[@data-id='"+campId+"']";
			this.driver.findElement(By.xpath(xpath)).click();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return new CommunicationPage(this.driver);
		}
		else {
			System.out.println("Error : Not able to click on new Campaign Retire button");
		}
		return new CommunicationPage(this.driver);
	}
	
	public CommunicationPage clickOnCloneLink(String campName) {
		if (this.isCampaignPresentByName(campName)) {
			String campId = this.getCampaignIdByName(campName);
			String xpath = "//*[@id='list_campaign_data']/tbody//a[contains(text(), '"+campName+"')]/following::td/a[@id='"+campId+"' and @class='campaign_action clone tooltip']";
			this.driver.findElement(By.xpath(xpath)).click();
			try {
				Thread.sleep(12000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return new CommunicationPage(this.driver);
		}
		else {
			System.out.println("Error : Not able to click on clone Campaign button");
		}
		return new CommunicationPage(this.driver);
	}
	
	public CampaignPlanPage clickOnCampaignEditLink(String campName) {
		if (this.isCampaignPresentByName(campName)) {
			String campId = this.getCampaignIdByName(campName);
			String xpath = "//a[@id='"+campId+"' and contains (@class, 'edit')]";
			this.driver.findElement(By.xpath(xpath)).click();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return new CampaignPlanPage(this.driver);
		}
		else {
			System.out.println("Error : Not able to click on new Campaign Retire button");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public ReportingPage clickOnCampaignReportLink(String campName) {
		if (this.isCampaignPresentByName(campName)) {
			String campId = this.getCampaignIdByName(campName);
			String xpath = "//a[@href='/reports/campaign/"+campId+"/performance']";
			this.driver.findElement(By.xpath(xpath)).click();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			testReporter.log(LogStatus.INFO, "Navigating to campaign report page");
			return new ReportingPage(this.driver);
		}
		else {
			System.out.println("Error : Not able to click on new Campaign Retire button");
		}
		return new ReportingPage(this.driver);
	}
	
	public CommunicationPage clickOnRetireYesButton(String campName) {
		if (this.isUiDialogYesButton()) {
			this.uiDialogYesButton.click();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
			return new CommunicationPage(this.driver);
		}
		else {
			System.out.println("Error : Not able to click on Campaign Retire yes button");
		}
		return new CommunicationPage(this.driver);
	}
	
	private void waitTillCampaignMarkForDeletePresent(String campName) throws Exception {	
		Map<String, String> stringAttri;
		int maxCount = 10;
		do {
			Thread.sleep(5000);
			stringAttri = mPulseDB.getResultMap("select count(*) from campaign where account_id = 1580 and mark_for_delete =true and name ilike '"+campName+"%'");
			maxCount--;
		} while (stringAttri.get("count") != "0" && maxCount != 0);
	}
	
	public CommunicationPage retireCampaignByName(String campName) throws Exception {
		if (this.isCampaignPresentByName(campName)) {
			this.clickOnCampaignRetireLink(campName).clickOnRetireYesButton(campName);
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
			this.waitTillCampaignMarkForDeletePresent(campName);
			return new CommunicationPage(this.driver);
		}
		else {
			System.out.println("Error : Not able to Retire Campaign, or campaign not Present");
		}
		return new CommunicationPage(this.driver);
	}
	
	private String getCampaignIdByName(String campName) {
		String xpath = "//*[@id='list_campaign_data']/tbody//a[contains(text(), '" + campName + "')]";
			String pattern1 = "campaigns/";
			String pattern2 = "/goal";
			String text = this.driver.findElement(By.xpath(xpath)).getAttribute("href");
			Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
			Matcher m = p.matcher(text);
			while (m.find()) {
			  return m.group(1);
			}
			return null;
	}
	
	public CampaignPlanPage clickOnCreateNewCampaignButton() {
		if(this.isNewCampaignButtonPresent()) {
			this.newCampaignButton.click();
			testReporter.log(LogStatus.INFO, "Clicked on create new campaign button");
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Not able to click on new Campaign button, new Campaign button is not enabled");
		}
		return new CampaignPlanPage(this.driver);
	}
}
