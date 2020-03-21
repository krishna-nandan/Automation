package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class UploadAudiencePage {
	
	WebDriver driver;
	ExtentTest testReporter;
	
	@FindBy (id= "needConfirm")
	private WebElement needConfirmationCheckBox;
	
	@FindBy (id= "sendWelcomeMessage")
	private WebElement sendWelcomeMessageCheckBox;
	
	@FindBy (id= "allowPipeDelimiter")
	private WebElement allowPipeDelimiterCheckBox;
	
	@FindBy (id= "firstRowIsHeader")
	private WebElement firstRowIsHeaderCheckBox;
	
	@FindBy (id= "file")
	private WebElement browseFileButton;
	
	@FindBy (id ="upload")
	private WebElement uploadButton;
	
	public UploadAudiencePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isNeedConfirmCheckBoxPresent() {
		if (this.needConfirmationCheckBox.isDisplayed()) {
			return true;
		} else return false;
	}
	
	public boolean isSendWelcomeMessageCheckBoxPresent() {
		if (this.sendWelcomeMessageCheckBox.isDisplayed()) {
			return true;
		} else return false;
	}
	
	public boolean isAllowPipeDelimiterCheckBoxPresent() {
		if (this.allowPipeDelimiterCheckBox.isDisplayed()) {
			return true;
		} else return false;
	}
	
	public boolean isFirstRowIsHeaderCheckBoxPresent() {
		if (this.firstRowIsHeaderCheckBox.isDisplayed()) {
			return true;
		} else return false;
	}
	
	public boolean isBrowseFileButtonPresent() {
		if (this.browseFileButton.isDisplayed()) {
			return true;
		} else return false;
	}
	
	public boolean isUploadButtonPresent() {
		if (this.uploadButton.isDisplayed()) {
			return true;
		} else return false;
	}
	
	public UploadAudiencePage selectNeedConfirmationCheckBox() {
		if(this.isNeedConfirmCheckBoxPresent()) {
			this.needConfirmationCheckBox.click();
			testReporter.log(LogStatus.INFO, "Clicked on need confirmation check box");
			return new UploadAudiencePage(driver);
		}
		else Assert.assertTrue(true, "Error : need Confirmation check box not present");
		return new UploadAudiencePage(driver);
	}
	
	public UploadAudiencePage selectsendWelcomeMessageCheckBox() {
		if(this.isSendWelcomeMessageCheckBoxPresent()) {
			this.sendWelcomeMessageCheckBox.click();
			testReporter.log(LogStatus.INFO, "Clicked on send welcome message check box");
			return new UploadAudiencePage(driver);
		}
		else Assert.assertTrue(true, "Error : sendWelcomeMessageCheckBox not present");
		return new UploadAudiencePage(driver);
	}
	
	public UploadAudiencePage selectAllowPipeDelimiterCheckBox() {
		if(this.isAllowPipeDelimiterCheckBoxPresent()) {
			this.allowPipeDelimiterCheckBox.click();
			testReporter.log(LogStatus.INFO, "Clicked on allow pipe delimiter check box");
			return new UploadAudiencePage(driver);
		}
		else Assert.assertTrue(true, "Error : Allow Pipe Delimiter Check Box not present");
		return new UploadAudiencePage(driver);
	}
	
	public MappingColumnsPage clickOnUploadButton(String filePath) throws InterruptedException {
		this.browseFileButton.sendKeys(filePath);
		Thread.sleep(5000);
		testReporter.log(LogStatus.INFO, "CSV file from path "+filePath+" is selected");
		this.uploadButton.click();
		Thread.sleep(5000);
		testReporter.log(LogStatus.INFO, "Clicked on upload CSV button");
		return new MappingColumnsPage(driver);
	}

}
