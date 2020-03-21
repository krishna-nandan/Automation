package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;

public class MyAccountPage {
	WebDriver driver;
	ExtentTest testReporter;
	
	@FindBy (xpath = "//a[@class='custom_fields']")
	private WebElement customFieldsPageLink;
	
	@FindBy (xpath = "//a[@class = 'event_definitions']")
	private WebElement eventDefinitionPageLink;

	public MyAccountPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isCustomFieldsPageButtonPresent() {
		if (this.customFieldsPageLink.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isEventDefinitionPageButtonPresent() {
		if (this.eventDefinitionPageLink.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public CustomFieldsPage goToCustomFieldsPage() {
		
		if (this.isCustomFieldsPageButtonPresent()) {
			this.customFieldsPageLink.click();
		} else {
			System.out.println("Error: Custom Fields page link not present");
		}
		return new CustomFieldsPage(this.driver);
	}
	
	public EventDefinitionsPage goToEventDefinitionPage() {
		
		if (this.isEventDefinitionPageButtonPresent()) {
			this.eventDefinitionPageLink.click();
		} else {
			System.out.println("Error: Event definition page link not present");
		}
		return new EventDefinitionsPage(this.driver);
	}
}
