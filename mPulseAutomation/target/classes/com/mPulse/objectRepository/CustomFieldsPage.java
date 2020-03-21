package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;

public class CustomFieldsPage {
	
	WebDriver driver;
	ExtentTest testReporter;
	
	@FindBy(id="add_custom_field")
	private WebElement addCustomFieldButton;

	public CustomFieldsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isAddCustomFieldButtonPresent() {
		
		if (this.addCustomFieldButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public NewCustomFieldPage goToNewCustomFieldPage() {
		
		if (this.isAddCustomFieldButtonPresent()) {
			this.addCustomFieldButton.click();
		} else {
			System.out.println("Error: New Custom Field page link not present");
		}
		return new NewCustomFieldPage(this.driver);
	}

}
