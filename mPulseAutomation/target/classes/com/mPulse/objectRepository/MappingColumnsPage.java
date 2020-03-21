package com.mPulse.objectRepository;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.utility.DriverMethods;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MappingColumnsPage {

	public WebDriver driver;
	ExtentTest testReporter;
	Actions act;

	@FindBy(id = "FIRSTNAME0")
	private WebElement firstName;

	@FindBy(id = "LASTNAME0")
	private WebElement lastName;

	@FindBy(id = "EMAIL0")
	private WebElement email;

	@FindBy(id = "MOBILEPHONE0")
	private WebElement mobilePhone;

	@FindBy(id = "APPMEMBERID0")
	private WebElement appMemberId;

	@FindBy(id = "CLIENTMEMBERID0")
	private WebElement clientMemberId;

	@FindBy(xpath = "//li[@data-tag='CUSTOM_NUMBER']")
	private WebElement customNumber;

	@FindBy(id = "upload")
	private WebElement uploadButton;

	@FindBy(xpath = "//button[@type='button' and @aria-disabled='true']")
	private WebElement okButton;

	@FindBy(xpath = "//span[@class='import_result step_detail']")
	private List<WebElement> totalMembersToImport;

	@FindBy(xpath = "//ul[@id='system_field_tab_header']/li[2]/a")
	private WebElement customFieldsTab;

	@FindBy(xpath = "//ul[@id='import_field_content']/li[1]/div")
	private WebElement csvFirstName;

	@FindBy(xpath = "//ul[@id='import_field_content']/li[2]/div")
	private WebElement csvLastName;

	@FindBy(xpath = "//ul[@id='import_field_content']/li[3]/div")
	private WebElement csvEmail;

	@FindBy(xpath = "//ul[@id='import_field_content']/li[4]/div")
	private WebElement csvMobilePhone;

	@FindBy(xpath = "//ul[@id='import_field_content']/li[5]/div")
	private WebElement csvAppMemberID;

	@FindBy(xpath = "//ul[@id='import_field_content']/li[6]/div")
	private WebElement csvClientMemberID;

	@FindBy(xpath = "//ul[@id='import_field_content']/li[7]/div")
	private WebElement csvCustomNumber;
	
	@FindBy (xpath = "//div[@class='ui-dialog-buttonset']/button[1]")
	private WebElement warningYesButton;
	
	@FindBy (xpath = "//div[@class='ui-dialog-buttonset']/button[2]")
	private WebElement warningNoButton;

	public MappingColumnsPage(WebDriver driver) {
		this.driver = driver;
		System.out.println("driver instance inside MappingColumnsPage " + driver.toString());
		act = new Actions(driver);
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}

	public void mappFirstName() {
		act.moveToElement(csvFirstName).build().perform();
		act.dragAndDrop(firstName, csvFirstName).build().perform();
		testReporter.log(LogStatus.INFO, "First name mapped");
	}

	public void mappLastName() {
		act.moveToElement(csvLastName).build().perform();
		act.dragAndDrop(lastName, csvLastName).build().perform();
		testReporter.log(LogStatus.INFO, "Last name mapped");
	}

	public void mappEmail() {
		act.moveToElement(csvEmail).build().perform();
		act.dragAndDrop(email, csvEmail).build().perform();
		testReporter.log(LogStatus.INFO, "Email column mapped");
	}

	public void mappMobilePhone() {
		act.moveToElement(csvMobilePhone).build().perform();
		act.dragAndDrop(mobilePhone, csvMobilePhone).build().perform();
		testReporter.log(LogStatus.INFO, "Mobile phone column mapped");
	}

	public void mappAppMemberID() {
		act.moveToElement(csvAppMemberID).build().perform();
		act.dragAndDrop(appMemberId, csvAppMemberID).build().perform();
		testReporter.log(LogStatus.INFO, "AppMemberID column mapped");
	}

	public void mappClientMemberID() {
		act.moveToElement(csvClientMemberID).build().perform();
		act.dragAndDrop(clientMemberId, csvClientMemberID).build().perform();
		testReporter.log(LogStatus.INFO, "ClientMemberID column mapped");
	}

	public void clickOnCustomFields() {
		if (this.customFieldsTab.isEnabled() && this.customFieldsTab.isDisplayed()) {
			this.customFieldsTab.click();
			testReporter.log(LogStatus.INFO, "Clicked on custom field tab");
		} else {
			System.out.println("Error: Element 'Custom Fields tab' not Displayed or not Enabled");
		}
	}

	public void mappCustomNumber() {
		this.clickOnCustomFields();
		act.moveToElement(csvCustomNumber).build().perform();
		act.dragAndDrop(customNumber, csvCustomNumber).build().perform();
		testReporter.log(LogStatus.INFO, "Custom Number column mapped");
	}

	public void mappAllFields() {
		this.mappFirstName();
		this.mappLastName();
		this.mappEmail();
		this.mappMobilePhone();
		this.mappAppMemberID();
		this.mappClientMemberID();
		this.mappCustomNumber();
	}
	
	public void clickOnUploadButton() {
		act.moveToElement(this.uploadButton).build().perform();
		this.uploadButton.click();
		testReporter.log(LogStatus.INFO, "Upload button clicked after mapping");
	}

	public void mappAndClickOnUpload() {
		this.mappAllFields();
		act.moveToElement(this.uploadButton).build().perform();
		this.uploadButton.click();
	}

	public String getTotalMemberImportResult() throws InterruptedException {
		String text = "";
		if (DriverMethods.isElementPresent(driver, okButton)) {
			for (WebElement ele : this.totalMembersToImport) {
				text = text + ele.getText();
			}
			return text;
		}
		return text;

	}

	public ListSubscribersPage clickOnOKButton() {
		if (DriverMethods.isElementPresent(driver, okButton)) {
			this.okButton.click();
			testReporter.log(LogStatus.INFO, "Clicked OK button on result pop-up");
		}
		else {
			System.out.println("Ok Button not found");
			testReporter.log(LogStatus.ERROR, "OK button not appears on result pop-up");
		}
		return new ListSubscribersPage(driver);
	}
	
	public void acceptWarningMessage() {
		if (DriverMethods.isElementPresent(this.driver, this.warningYesButton)) {
			this.warningYesButton.click();
			testReporter.log(LogStatus.INFO, "Accepted warning message by clicking yes button");
		}
	}

}
