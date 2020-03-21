package com.mPulse.objectRepository;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class AudienceListsPage {
	WebDriver driver;
	ExtentTest testReporter;
	private boolean flag = false;
	
	@FindBy(xpath = "//a[@class='list_name']")
	private List<WebElement> allList;

	@FindBy(xpath = "//form[@id='paginationForm']/nav/ul/li[@class='next_page']/a")
	private WebElement nextPageNavigationButton;
	
	@FindBy(xpath = "//nav[@id='navigation']//a[@class='segments']")
	private WebElement segmentsLinkButton;
	
	@FindBy(xpath = "//nav[@id='navigation']//a[@class='Email_DNC_List']")
	private WebElement emailDNCListPageLink;
	
	@FindBy (xpath = "//nav[@id='navigation']//a[@class='members']")
	private WebElement audienceMemberPageLink;

	public AudienceListsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}

	public boolean isNextPageNavigationButtonPresent() {
		if (this.nextPageNavigationButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public void clickOnNextPageNavigationButton() {
		if (isNextPageNavigationButtonPresent()) {
			this.nextPageNavigationButton.click();
		} else System.out.println("Next page button is not present");
	}
	
	public boolean isSegmentPageLinkPresent() {
		if (this.segmentsLinkButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isAudienceMemberPageLinkPresent() {
		if (this.audienceMemberPageLink.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isEmailDNCLPageLinkPresent() {
		if (this.emailDNCListPageLink.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public AudienceSegmentsPage goToSegmentsPage() {
		if (this.isSegmentPageLinkPresent()) {
			this.segmentsLinkButton.click();
		} else {
			System.out.println("Error: Audience Segment page link not present");
		}
		return new AudienceSegmentsPage(driver);
	}
	
	public DoNotContactEmailPage goDoNotContactEmailPage() {
		if (this.isEmailDNCLPageLinkPresent()) {
			this.emailDNCListPageLink.click();
		} else {
			System.out.println("Error: Do not contact email page link not present");
		}
		return new DoNotContactEmailPage(driver);
	}
	
	public AudienceMembersPage goAudienceMemberPage() {
		if (this.isAudienceMemberPageLinkPresent()) {
			this.audienceMemberPageLink.click();
		} else {
			System.out.println("Error: Audience Member page link not present");
		}
		return new AudienceMembersPage(driver);
	}

	private boolean clickOnListByName(String listName) {
		for (WebElement element : allList) {
			if (element.getText().equals(listName)) {
				element.click();
				testReporter.log(LogStatus.INFO, "Requested list with name '"+listName+"' found and clicked");
				return true;
			}
		}
		testReporter.log(LogStatus.ERROR, "Requested list with name '"+listName+"' not found");
		return false;
	}
	
	public ListSubscribersPage goToListSubscribersPage(String listName) {
		do {
			flag = this.clickOnListByName(listName);
			if(flag == false)
				this.clickOnNextPageNavigationButton();
		}while(flag == false);
		testReporter.log(LogStatus.INFO, "Navigated to list SubscribersPage of list '"+listName+"'");
		return new ListSubscribersPage(driver);
	}

}
