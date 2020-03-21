package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class HomePage {
	
	WebDriver driver;
	ExtentTest testReporter;

	@FindBy(xpath = "//nav[@id='navigation']/ul[@class='sub_navigation']/li[@class='selected ']/a")
	@CacheLookup
	private WebElement homePageIcon;

	@FindBy(id = "userLogout")
	@CacheLookup
	private WebElement logOutLink;
	
	@FindBy(xpath = "//div[@class='user_actions']/ul/li[1]/a")
	private WebElement mySettingLink;
	
	@FindBy(xpath = "//a[@class='audience']")
	private WebElement audienceListsPageLink;
	
	@FindBy (className = "communication")
	private WebElement communicationPageLink;
	
	@FindBy (xpath = "//a[@class='my_account']")
	private WebElement myAccountPageLink;
	
	@FindBy (className = "reporting")
	private WebElement reportingPageLink;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}

	public boolean isHomePageIconPresent() {
		if (this.homePageIcon.isDisplayed()) {
			return true;
		} else
			return false;
	}

	public boolean isLogOutLinkPresent() {
		if (this.logOutLink.isDisplayed()) {
			return true;
		} else
			return false;
	}

	public LoginPage logOut() {
		if (isLogOutLinkPresent()) {
			this.logOutLink.click();
		} else {
			System.out.println("Error: Element 'log out link' not Displayed or not Enabled");
		}
		return new LoginPage(this.driver);
	}
	
	public boolean isAudienceListsPageLinkPresent() {
		if (this.audienceListsPageLink.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isCommunicationPageLinkPresent() {
		if (this.communicationPageLink.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isMyAccountPageLinkPresent() {
		if (this.myAccountPageLink.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isReportingPageLinkPresent() {
		if (this.reportingPageLink.isDisplayed()) {
			return true;
		}
		else return false;
	}
	
	public AudienceListsPage goToAudienceListPage() {
		if (isAudienceListsPageLinkPresent()) {
			this.audienceListsPageLink.click();
			testReporter.log(LogStatus.INFO, "Clicked on audience page link");
		} else {
			System.out.println("Error: Audience List page link not present");
			testReporter.log(LogStatus.ERROR, "Error: Audience List page link not present");
		}
		return new AudienceListsPage(this.driver);
	}
	
	public MyAccountPage goToMyAccountPage() {
		if (this.isMyAccountPageLinkPresent()) {
			this.myAccountPageLink.click();
			testReporter.log(LogStatus.INFO, "Clicked on my account page link");
		} else {
			System.out.println("Error: My Account page link not present");
			testReporter.log(LogStatus.ERROR, "Error: My Account page link not present");
		}
		return new MyAccountPage(this.driver);
	}
	
	public CommunicationPage goToCommunicationPage() {
		if (isCommunicationPageLinkPresent()) {
			this.communicationPageLink.click();
			testReporter.log(LogStatus.INFO, "Clicked on communication page link");
		} else {
			System.out.println("Error: Communication page link not present");
			testReporter.log(LogStatus.ERROR, "Error: Communication page link not present");
		}
		return new CommunicationPage(this.driver);
	}
	
	public ReportingPage goToReportingPage() {
		if (isReportingPageLinkPresent()) {
			this.reportingPageLink.click();
			testReporter.log(LogStatus.INFO, "Clicked on reporting page link");
		} else {
			System.out.println("Error: Reporting page link not present");
			testReporter.log(LogStatus.ERROR, "Error: Reporting page link not present");
		}
		return new ReportingPage(this.driver);
	}

}
