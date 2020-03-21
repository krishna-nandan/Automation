package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.mPulse.utility.DriverMethods;

public class ListSubscribersPage {

	WebDriver driver;

	@FindBy(xpath = "//a[@id='import_audience' and contains(@href, 'uploads/new')]")
	private WebElement importSubscribers;
	
	@FindBy (xpath = "//table[@id='members']/tbody[1]/tr[1]/td[@class='phone']/ul/li")
	private WebElement firstMemberWithPhone;
	
	@FindBy (xpath = "//table[@id='members']/tbody[1]/tr[1]/td[@class='email']/ul/li")
	private WebElement firstMemberWithEmail;

	public boolean isImportSubscribersButtonPresent() {
		if (this.importSubscribers.isDisplayed()) {
			return true;
		}
		return false;
	}

	public UploadAudiencePage clickOnImportSubscribersButton() {
		if (isImportSubscribersButtonPresent()) {
			this.importSubscribers.click();
			return new UploadAudiencePage(driver);
		} else {
			Assert.assertTrue(false, "Error : Import Subscribers button not present");
			return new UploadAudiencePage(driver);
		}
	}
	
	public boolean isFirstMemberWithPhonePresent() {
		if (DriverMethods.isElementPresent(driver, firstMemberWithPhone)) {
			return true;
		}
		else return false;
	}
	
	public boolean isFirstMemberWithEmailPresent() {
		if (DriverMethods.isElementPresent(driver, this.firstMemberWithEmail)) {
			return true;
		}
		else return false;
	}
	
	public String getFirstMemberPhoneNumber() {
		if (isFirstMemberWithPhonePresent()) {
			return "1"+this.firstMemberWithPhone.getText().replace("-", "");
		}
		else return "";
	}
	
	public String getFirstMemberEmail() {
		if (isFirstMemberWithEmailPresent()) {
			return this.firstMemberWithEmail.getAttribute("title");
		}
		else return "";
	}

	public ListSubscribersPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

}
