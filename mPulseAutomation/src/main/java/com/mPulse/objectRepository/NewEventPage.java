package com.mPulse.objectRepository;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;

public class NewEventPage {
	
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	JavascriptExecutor js;
	
	@FindBy (id = "eventName")
	private WebElement eventNameInputBox;
	
	@FindBy (id= "callbackFormat")
	private WebElement callbackFormatSelectBox;
	
	@FindBy (id = "callbackSyncUrl")
	private WebElement callbackSyncUrlSelectBox;
	
	@FindBy (id = "add_event_attribute")
	private WebElement add_event_attributeButton;
	
	@FindBy (xpath = "//a[@id='add_event_attribute']/ancestor::li/preceding-sibling::li[1]/input[1]")
	private WebElement attributeNameInputBox;
	
	@FindBy (xpath = "//a[@id='add_event_attribute']/ancestor::li/preceding-sibling::li[1]//input[@type='checkbox' and text() = 'Required']")
	private WebElement attributeRequiredCheckBox;
	
	@FindBy (xpath = "//a[@id='add_event_attribute']/ancestor::li/preceding-sibling::li[1]//select")
	private WebElement attributeTypeSelectBox;
	
	@FindBy (xpath = "//a[@id='add_event_attribute']/ancestor::li/preceding-sibling::li[1]//input[@placeholder='Default Value']")
	private WebElement attributeDefaultValueInputBox;
	
	@FindBy (xpath = "//a[@id='add_event_attribute']/ancestor::li/preceding-sibling::li[1]//input[@type='checkbox' and text() = 'Dynamic']")
	private WebElement dynamicCheckBox;
	
	@FindBy (xpath = "//a[@id='add_event_attribute']/ancestor::li/preceding-sibling::li[1]//input[@placeholder='Write expression here']")
	private WebElement dynamicExpressionInputBox;
	
	@FindBy (xpath = "//button[@name='create' and @class='common_form_submit icon_button']")
	private WebElement createButton;
	
	@FindBy (id = "delay")
	private WebElement addDelayInputBox;
	
	@FindBy (id = "moSessionTtl")
	private WebElement eventSessionValiditySelectBox;
	
	@FindBy (xpath = "//div[@class='ui-dialog-buttonset']//span[text()='Yes']/ancestor::button")
	private WebElement delayWarningYesButton;
	
	public NewEventPage(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isEventNameInputBoxPresent() {
		if (this.eventNameInputBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isCallbackFormatSelectBoxPresent() {
		if (this.callbackFormatSelectBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isCallbackSyncUrlSelectBoxPresent() {
		if (this.callbackSyncUrlSelectBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isAddEventAttributeButtonPresent() {
		if (this.add_event_attributeButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isAttributeNameInputBoxPresent() {
		if (this.attributeNameInputBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isAttributeRequiredCheckBoxPresent() {
		if (this.attributeRequiredCheckBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isAttributeTypeSelectBoxPresent() {
		if (this.attributeTypeSelectBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isAttributeDefaultValueInputBoxPresent() {
		if (this.attributeDefaultValueInputBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isDynamicCheckBoxPresent() {
		if (this.dynamicCheckBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isDynamicExpressionInputBoxPresent() {
		if (this.dynamicExpressionInputBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isCreateButtonPresent() {
		if (this.createButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isaddDelayInputBoxPresent() {
		if (this.addDelayInputBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isEventSessionValiditySelectBoxPresent() {
		if (this.eventSessionValiditySelectBox.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isDelayWarningYesButtonPresent() {
		if (this.delayWarningYesButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public NewEventPage enterEventName(String eventName) {
		
		if (this.isEventNameInputBoxPresent()) {
			this.eventNameInputBox.clear();
			this.eventNameInputBox.sendKeys(eventName);
		} else {
			System.out.println("Error: Event name input box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage selectCallbackFormat(String callbackFormat) throws Exception {

		if (this.isCallbackFormatSelectBoxPresent()) {
			Select sc = new Select(this.callbackFormatSelectBox);
			if (callbackFormat.equalsIgnoreCase("xml")) {
				sc.selectByVisibleText("XML");
			}
			if (callbackFormat.equalsIgnoreCase("json")) {
				sc.selectByVisibleText("JSON");
			} else {
				System.out.println("Error: Callback format not valid");
			}
		} else {
			System.out.println("Error: Callback format select box not present");
		}
		Thread.sleep(3000);
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage selectCallbackURL_Default() throws Exception {
		if (this.isCallbackSyncUrlSelectBoxPresent()) {
			Thread.sleep(3000);
			this.callbackSyncUrlSelectBox.click();
			Select sc = new Select(this.callbackSyncUrlSelectBox);
				sc.selectByIndex(1);
		} else {
			System.out.println("Error: Callback url select box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage clickToAddEventAttribute() {
		
		if (this.isAddEventAttributeButtonPresent()) {
			this.add_event_attributeButton.click();
		} else {
			System.out.println("Error: Add Event attribute button not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage enterAttributeName(String attributeName) {
		
		if (this.isAttributeNameInputBoxPresent()) {
			this.attributeNameInputBox.clear();
			this.attributeNameInputBox.sendKeys(attributeName);
		} else {
			System.out.println("Error: Event attribute name input box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage clickOnAttributeRequiredCheckBox() {
		
		if (this.isAttributeRequiredCheckBoxPresent()) {
			this.attributeRequiredCheckBox.click();
		} else {
			System.out.println("Error: attribute Required Check Box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage enterAttributeDefaultValue(String defaultValue) {
		
		if (this.isAttributeDefaultValueInputBoxPresent()) {
			this.attributeDefaultValueInputBox.clear();
			this.attributeDefaultValueInputBox.sendKeys(defaultValue);
		} else {
			System.out.println("Error: attribute default value input box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage selectAttributeType(String attributeType) {

		if (this.isAttributeTypeSelectBoxPresent()) {
			Select sc = new Select(this.attributeTypeSelectBox);
			if (attributeType.equalsIgnoreCase("String")) {
				sc.selectByValue("0");
			}
			if (attributeType.equalsIgnoreCase("Number")) {
				sc.selectByValue("1");
			}
			if (attributeType.equalsIgnoreCase("DateTime")) {
				sc.selectByValue("2");
			} else {
				System.out.println("Error: Attribute type format not valid");
			}
		} else {
			System.out.println("Error: Attribute type select box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage clickOnDynamicCheckBox() {
		
		if (this.isDynamicCheckBoxPresent()) {
			this.dynamicCheckBox.click();
		} else {
			System.out.println("Error: dynamic Check Box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage clickOnDelayWarningYesButton() {
		
		if (this.isDelayWarningYesButtonPresent()) {
			this.delayWarningYesButton.click();
		} else {
			System.out.println("Error: Delay Warning Yes Button not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public EventDefinitionsPage clickOnCreateEventButton() throws InterruptedException {
		
		if (this.isCreateButtonPresent()) {
			this.createButton.click();
			Thread.sleep(3000);
		} else {
			System.out.println("Error: create Event Button not present");
		}
		return new EventDefinitionsPage(this.driver);
	}
	
	public NewEventPage enterDynamicExpression(String dynamicExpression) {
		
		if (this.isDynamicExpressionInputBoxPresent()) {
			this.dynamicExpressionInputBox.clear();
			this.dynamicExpressionInputBox.sendKeys(dynamicExpression);
		} else {
			System.out.println("Error: dynamic Expression Input Box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage enterAddDelayInputBox(String delayTime) {
		
		if (this.isaddDelayInputBoxPresent()) {
			this.addDelayInputBox.clear();
			this.addDelayInputBox.sendKeys(delayTime);
		} else {
			System.out.println("Error: add Delay Input Box not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public NewEventPage selectEventSessionValidity(String sessionValidity) {

		if (this.isEventSessionValiditySelectBoxPresent()) {
			Select sc = new Select(this.eventSessionValiditySelectBox);
			if (sessionValidity.equalsIgnoreCase("24 Hours")) {
				sc.selectByValue("1");
			}
			if (sessionValidity.equalsIgnoreCase("48 Hours")) {
				sc.selectByValue("2");
			}
			if (sessionValidity.equalsIgnoreCase("72 Hours")) {
				sc.selectByValue("3");
			}
			if (sessionValidity.equalsIgnoreCase("96 Hours")) {
				sc.selectByValue("4");
			} else {
				System.out.println("Error: session Validity type not valid");
			}
		} else {
			System.out.println("Error: event Session Validity Select Box not present");
		}
		return new NewEventPage(this.driver);
	}

}
