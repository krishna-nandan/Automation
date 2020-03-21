package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;

public class NewCustomFieldPage {

	WebDriver driver;
	ExtentTest testReporter;
	Actions act;

	@FindBy(id = "name")
	private WebElement customFieldName;

	@FindBy(id = "fieldType")
	private WebElement customFieldType;

	@FindBy(xpath = "//button[@id='submit']")
	private WebElement submitButton;

	public NewCustomFieldPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
		act = new Actions(driver);
	}

	public boolean isCustomFieldNamePresent() {

		if (this.customFieldName.isDisplayed()) {
			return true;
		} else
			return false;
	}

	public boolean isCustomFieldTypePresent() {

		if (this.customFieldType.isDisplayed()) {
			return true;
		} else
			return false;
	}

	public boolean issubmitButtonButtonPresent() {

		if (this.submitButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	private void enterCustomFieldName(String fieldName) {
		if (this.isCustomFieldNamePresent()) {
			this.customFieldName.sendKeys(fieldName);
		} else {
			System.out.println("Error: Custom field name text box not present");
		}
	}
	
	private void clickOnSubmitButton() {
		if (this.issubmitButtonButtonPresent()) {
			act.moveToElement(this.submitButton).build().perform();
			act.click(this.submitButton).build().perform();
		} else {
			System.out.println("Error: Submit button not present");
		}
	}
	
	private void selectCustomFieldByValue(String value) {
		Select sc = new Select(this.customFieldType);
		sc.selectByValue(value);
	}
	
	public CustomFieldsPage createNewParagraphCustomField(String fieldName) throws InterruptedException {
		this.enterCustomFieldName(fieldName);
		this.selectCustomFieldByValue("PARAGRAPH");
		this.clickOnSubmitButton();
		Thread.sleep(2000);
		return new CustomFieldsPage(this.driver);
	}

}
