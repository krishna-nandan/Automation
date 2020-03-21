package com.mPulse.objectRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.utility.DriverMethods;
import com.relevantcodes.extentreports.ExtentTest;

public class CampaignPlanPage {
	
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	
	@FindBy (xpath = "//a[@href='/home' and contains(text(), 'mPulse Mobile')]")
	private WebElement homePageLink;
	
	@FindBy (xpath = "//div[@id='common_workarea']/div[@class='progress-bar']/div[@class='loadingmessage']/span")
	private WebElement progressBarLoader;
	
	@FindBy (id = "name")
	private WebElement campaignNameTextBox;
	
	@FindBy (id= "listId")
	private WebElement listSelectDropDown;
	
	@FindBy (xpath = "//div[@class='sending-sms-list-wrapper']//span/button")
	private WebElement applySegmentsButton;
	
	@FindBy (id = "emailChannels")
	private WebElement emailChannelsCheckBox;
	
	@FindBy (id = "smsChannels")
	private WebElement smsChannelsCheckBox;
	
	@FindBy (id = "appMailChannels")
	private WebElement appMailChannelsCheckBox;
	
	@FindBy (id = "pnChannels")
	private WebElement pnChannelsCheckBox;
	
	@FindBy (id = "goal_submit")
	private WebElement saveAndNextButton;
	
	@FindBy (xpath = "//a[@href='/campaigns' and @class='common_form_cancel back_to_campaign']")
	private WebElement backToCampaignsLink;
	
	@FindBy (xpath = "//button[@type='button' and @role='button' and contains(@class,'submit')]")
	private WebElement selectSegmentOKButton;
	
	@FindBy (xpath = "//button[@type='button' and @role='button' and contains(@class,'cancel')]")
	private WebElement selectSegmentCancelButton;
	
	@FindBy (id = "segmentName")
	private WebElement compositeSegmentNameTextBox;

	public CampaignPlanPage(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isCampaignNameTextBoxPresent() {
		if(this.campaignNameTextBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isListSelectDropDownPresent() {
		if(this.listSelectDropDown.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isApplySegmentsButtonPresent() {
		if(this.applySegmentsButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEmailChannelsCheckBoxPresent() {
		if(this.emailChannelsCheckBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isSmsChannelsCheckBoxPresent() {
		if(this.smsChannelsCheckBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isAppMailChannelsCheckBoxPresent() {
		if(this.appMailChannelsCheckBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean ispnChannelsCheckBoxPresent() {
		if(this.pnChannelsCheckBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isSaveAndNextButtonPresent() {
		if(this.saveAndNextButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isBackToCampaignsLinkPresent() {
		if(this.backToCampaignsLink.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isSelectSegmentOKButtonPresent() {
		if(this.selectSegmentOKButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isSelectSegmentCancelButtonPresent() {
		if(this.selectSegmentCancelButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCompositeSegmentNameTextBoxPresent() {
		if(this.compositeSegmentNameTextBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public CampaignPlanPage enterCampaignName(String name) {
		if(this.isCampaignNameTextBoxPresent()) {
			this.campaignNameTextBox.clear();
			this.campaignNameTextBox.sendKeys(name);
		}
		else {
			System.out.println("Error : Not able to enter Campaign name");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage checkEmailChannelsCheckBox() {
		if(this.isEmailChannelsCheckBoxPresent()) {
			this.emailChannelsCheckBox.click();
		}
		else {
			System.out.println("Error : Not able to select Email check box");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage checkSmsChannelsCheckBox() {
		if(this.isSmsChannelsCheckBoxPresent()) {
			act.moveToElement(this.smsChannelsCheckBox).build().perform();
			this.smsChannelsCheckBox.click();
		}
		else {
			System.out.println("Error : Not able to select SMS check box");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage checkAppMailChannelsCheckBox() {
		if(this.isAppMailChannelsCheckBoxPresent()) {
			this.appMailChannelsCheckBox.click();
		}
		else {
			System.out.println("Error : Not able to select Appmail check box");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage checkPnChannelsCheckBox() {
		if(this.ispnChannelsCheckBoxPresent()) {
			this.pnChannelsCheckBox.click();
		}
		else {
			System.out.println("Error : Not able to select PN check box");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignComposePage clickOnSaveAndNextButton() throws InterruptedException {
		if(this.isSaveAndNextButtonPresent()) {
			act.moveToElement(this.saveAndNextButton).build().perform();
			this.saveAndNextButton.click();
			DriverMethods.waitForElementUntillDisappear(this.driver, this.progressBarLoader);
			//Thread.sleep(10000);
			DriverMethods.isElementPresent(this.driver, this.homePageLink);
		}
		else {
			System.out.println("Error : Not able to click on save and next button");
		}
		return new CampaignComposePage(this.driver);
	}
	
	public CampaignPlanPage clickOnSelectSegmentOKButton() {
		if(this.isSelectSegmentOKButtonPresent()) {
			this.selectSegmentOKButton.click();
		}
		else {
			System.out.println("Error : Not able to click on select segment OK button");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CommunicationPage clickOnBackToCampaignsLink() {
		if(this.isBackToCampaignsLinkPresent()) {
			this.backToCampaignsLink.click();
		}
		else {
			System.out.println("Error : Not able to click on Back to campaign link");
		}
		return new CommunicationPage(this.driver);
	}
	
	public CampaignPlanPage clickOnSelectSegmentCancelButton() {
		if(this.isSelectSegmentCancelButtonPresent()) {
			this.selectSegmentCancelButton.click();
		}
		else {
			System.out.println("Error : Not able to click on select segment Cancel button");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage clickOnApplySegmentsButton() {
		if(this.isApplySegmentsButtonPresent()) {
			this.applySegmentsButton.click();
		}
		else {
			System.out.println("Error : Not able to click on apply segment button");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage enterCompositeSegmentName(String segmentName) {
		if(this.isCompositeSegmentNameTextBoxPresent()) {
			this.compositeSegmentNameTextBox.sendKeys(segmentName);
		}
		else {
			System.out.println("Error : Not able to enter composite segment name");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage selectSegmentByName(String segmentName) {
		String xpath = "//label[contains(text(),'"+segmentName+"')]/preceding-sibling::input";
		WebElement element = this.driver.findElement(By.xpath(xpath));
		if (element.isDisplayed()) {
			element.click();
		}
		else {
			System.out.println("Error : Not able to select segment by name");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage selectSegmentBySegmentID(int segmentID) {
		String xpath = "//input[@type='checkbox' and @value='"+segmentID+"']";
		WebElement element = this.driver.findElement(By.xpath(xpath));
		if (element.isDisplayed()) {
			element.click();
		}
		else {
			System.out.println("Error : Not able to select segment by ID");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage selectListByListName(String listName) {
		if (this.isListSelectDropDownPresent()) {
			Select sc = new Select(this.listSelectDropDown);
			sc.selectByVisibleText(listName);
		}
		else {
			System.out.println("Error : Not able to select List by List name");
		}
		return new CampaignPlanPage(this.driver);
	}
	
	public CampaignPlanPage selectListByListID(int listID) {
		if (this.isListSelectDropDownPresent()) {
			Select sc = new Select(this.listSelectDropDown);
			sc.selectByValue(Integer.toString(listID));
		}
		else {
			System.out.println("Error : Not able to select List by ID");
		}
		return new CampaignPlanPage(this.driver);
	}

}
