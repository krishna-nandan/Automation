package com.mPulse.objectRepository;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;

public class DefineSegmentPage {
	
	WebDriver driver;
	ExtentTest testReporter;
	
	@FindBy (id = "name")
	private WebElement segmentNameTextBox;
	
	@FindBy (id = "memberTab")
	private WebElement memberTab;
	
	@FindBy (id= "listTab")
	private WebElement listTab;
	
	@FindBy (xpath = "//div[@id='segment_rules']//select[@name='field_name']")
	private WebElement fieldName;
	
	@FindBy (xpath = "//div[@id='segment_rules']//div[@class='and_template' and contains(text(), 'AND')]/following-sibling::select[@name='field_name']")
	private WebElement fieldName2nd;
	
	@FindBy (xpath = "//div[@id='segment_rules']//select[@name='field_name']")
	private List<WebElement> allFieldName;
	
	@FindBy (xpath = "//div[@id='segment_rules']//select[@name='field_predicate']")
	private WebElement fieldRuleSelectBox;
	
	@FindBy (xpath = "//div[@id='segment_rules']//div[@class='and_template' and contains(text(), 'AND')]/following-sibling::select[@name='field_predicate']")
	private WebElement fieldRule2ndSelectBox;
	
	@FindBy (xpath = "//div[@id='segment_rules']//select[@name='field_predicate']")
	private List<WebElement> allFieldRuleSelectBox;
	
	@FindBy (xpath = "//div[@id='segment_rules']//input[@data-name='value']")
	private WebElement valueInputTextBox;
	
	@FindBy (xpath = "//div[@id='segment_rules']//div[@class='and_template' and contains(text(), 'AND')]/following-sibling::input")
	private WebElement valueInput2ndTextBox;
	
	@FindBy (xpath = "//div[@id='segment_rules']//input[@data-name='value']")
	private List<WebElement> allValueInputTextBox;
	
	@FindBy (xpath = "//*[@id='segment_rules']//a[@class='add_criteria']")
	private WebElement addCriteriaLink;
	
	@FindBy (xpath = "//*[@id='segment_form']/a[@class='add_additional_rule']")
	private WebElement addAdditionalRuleLink;
	
	@FindBy (id = "segment-form-submit")
	private WebElement segmentFormSubmitButton;
	
	public DefineSegmentPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isSegmentNameTextBoxPresent() {
		if(this.segmentNameTextBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isMemberTabPresent() {
		if(this.memberTab.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isListTabPresent() {
		if(this.listTab.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isFieldNamePresent() {
		if(this.fieldName.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isFieldRuleSelectBoxPresent() {
		if(this.fieldRuleSelectBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isValueInputTextBoxPresent() {
		if(this.valueInputTextBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isFieldName2ndPresent() {
		if(this.fieldName2nd.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isFieldRule2ndSelectBoxPresent() {
		if(this.fieldRule2ndSelectBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isValueInput2ndTextBoxPresent() {
		if(this.valueInput2ndTextBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isAddCriteriaLinkPresent() {
		if(this.addCriteriaLink.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isSegmentFormSubmitButtonPresent() {
		if(this.segmentFormSubmitButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public DefineSegmentPage enterSegmentName(String name) {
		if (this.isSegmentNameTextBoxPresent()) {
			this.segmentNameTextBox.sendKeys(name);
		}
		else {
			System.out.println("Error : Not able to enter segment name, segment text box not displayed");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public DefineSegmentPage clickOnMemberTab() {
		if (this.isMemberTabPresent()) {
			this.memberTab.click();
		}
		else {
			System.out.println("Error : Not able to click on member tab, member tab displayed");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public DefineSegmentPage selectFieldName(String name) {
		if (this.isFieldNamePresent()) {
			Select sc = new Select(this.fieldName);
			sc.selectByVisibleText(name);
		}
		else {
			System.out.println("Error : Not able to select field name");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public DefineSegmentPage selectRuleByVisibleText(String name) {
		if (this.isFieldRuleSelectBoxPresent()) {
			Select sc = new Select(this.fieldRuleSelectBox);
			sc.selectByVisibleText(name);
		}
		else {
			System.out.println("Error : Not able to select field Rule");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public DefineSegmentPage enterValueInputTextBox(String name) {
		if (this.isValueInputTextBoxPresent()) {
			this.valueInputTextBox.sendKeys(name);
		}
		else {
			System.out.println("Error : Not able to enter value in value text box");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public DefineSegmentPage selectSecondFieldName(String name) {
		if (this.isFieldName2ndPresent()) {
			Select sc = new Select(this.fieldName2nd);
			sc.selectByVisibleText(name);
		}
		else {
			System.out.println("Error : Not able to select 2nd field name");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public DefineSegmentPage selectSecondRuleByVisibleText(String name) {
		if (this.isFieldRule2ndSelectBoxPresent()) {
			Select sc = new Select(this.fieldRule2ndSelectBox);
			sc.selectByVisibleText(name);
		}
		else {
			System.out.println("Error : Not able to select 2nd field Rule");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public DefineSegmentPage enterSecondValueInputTextBox(String name) {
		if (this.isValueInput2ndTextBoxPresent()) {
			this.valueInput2ndTextBox.sendKeys(name);
		}
		else {
			System.out.println("Error : Not able to enter 2nd value in value text box");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public AudienceSegmentsPage clickOnSubmitButton() {
		if(this.isSegmentFormSubmitButtonPresent()) {
			this.segmentFormSubmitButton.click();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Error : Not able to click on submit button");
		}
		return new AudienceSegmentsPage(this.driver);
	}
	
	public DefineSegmentPage clickOnAddCriteriaLink() {
		if(this.isAddCriteriaLinkPresent()) {
			this.addCriteriaLink.click();
		}
		else {
			System.out.println("Error : Not able to click on addCriteria button");
		}
		return new DefineSegmentPage(this.driver);
	}

}
