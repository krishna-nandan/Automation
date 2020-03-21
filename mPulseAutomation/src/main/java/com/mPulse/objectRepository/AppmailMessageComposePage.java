package com.mPulse.objectRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.utility.DriverMethods;
import com.relevantcodes.extentreports.ExtentTest;

public class AppmailMessageComposePage {
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	JavascriptExecutor js;
	
	@FindBy (xpath = "//a[@href='/home' and contains(text(), 'mPulse Mobile')]")
	private WebElement homePageLink;
	
	@FindBy(xpath = "//div[@id='appmail-message-accordion']//form[@id='event_appmail_compose_0']//textarea[contains(@placeholder, 'enter your Secure Messaging subject')]")
	private WebElement secureMessageSubject;
	
	@FindBy (xpath = "//div[@id='common_workarea']/div[@class='saving-bar']/div[@class='loadingmessage']/span")
	private WebElement loaderSavingBar;
	
	@FindBy (xpath = "//div[@id='appmail-message-accordion']//form[@id='event_appmail_compose_0']//span[@id='cke_message[0].body']//iframe[@tabindex='0' and @allowtransparency='true']")
	private WebElement iframe;
	
	@FindBy (xpath = "html/body")
	private WebElement secureMessageTextBody;
	
	@FindBy (xpath = "//div[@id='appmail-message-accordion']//form[@id='event_appmail_compose_0']//div[@id='add_trigger']")
	private WebElement appmailAddTriggerButton;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Next')]/ancestor::button[@id='message_submit']")
	private WebElement campaignNextButton;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Save')]/ancestor::button[@class='icon_button only_save shift_right']")
	private WebElement campaignSaveButton;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Next')]/ancestor::button[@id='message_submit']/following-sibling::a[@class='common_form_cancel']")
	private WebElement campaignBackButton;

	public AppmailMessageComposePage(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isSecureMessageSubjectTextBoxPresent() {
		if(this.secureMessageSubject.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isAppmailAddTriggerButtonPresent() {
		if (this.appmailAddTriggerButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignSaveButtonPresent() {
		if(this.campaignSaveButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public AppmailMessageComposePage enterSecureMessageSubject(String subject) throws Exception {
		Thread.sleep(10000);
		DriverMethods.waitForElementUntillDisappear(this.driver, this.loaderSavingBar);
		act.moveToElement(this.secureMessageSubject).build().perform();
		this.secureMessageSubject.click();
		this.secureMessageSubject.clear();
		this.secureMessageSubject.sendKeys(subject);
		Thread.sleep(3000);
		System.out.println("Info : secure message subject text box is filled with value "+subject);
		return new AppmailMessageComposePage(this.driver);
	}
	
	public CampaignComposePage clickOnSaveButton() throws InterruptedException {
		if(this.isCampaignSaveButtonPresent()) {
			this.campaignSaveButton.click();
			Thread.sleep(10000);
			DriverMethods.waitForElementUntillDisappear(this.driver, this.loaderSavingBar);
			DriverMethods.isElementPresent(driver, homePageLink);
		}
		return new CampaignComposePage(driver);
	}
	
	public AppmailMessageComposePage enterSecureMessageText(String text) {
		int size = driver.findElements(By.tagName("iframe")).size();
		System.out.println("total number of iframe - "+size);
		this.driver.switchTo().frame(0);
		this.secureMessageTextBody.sendKeys(text);
		this.driver.switchTo().defaultContent();
		return new AppmailMessageComposePage(this.driver);
	}
	
	public AppmailTriggers clickOnAddTriggerButton() throws InterruptedException {
		if (this.isAppmailAddTriggerButtonPresent()) {
			act.moveToElement(this.appmailAddTriggerButton).build().perform();
			this.appmailAddTriggerButton.click();
			DriverMethods.isElementPresent(driver, homePageLink);
			Thread.sleep(3000);
		} else {
			System.out.println("Error : Appmail Trigger are not present");
		}
		return new AppmailTriggers();
	}
	
	public class AppmailTriggers {
		@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='4']")
		private WebElement appmailOneTimeTrigger;

		@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='3']")
		private WebElement appmailRecurrenceTrigger;

		@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='0']")
		private WebElement appmailDateTrigger;

		@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='2']")
		private WebElement appmailMemberEngagementTrigger;

		@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='1']")
		private WebElement appmailMemberProfileTrigger;

		@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='5']")
		private WebElement appmailGetMemberInfoTrigger;

		@FindBy(xpath = "//div[@id='appmail_trigger_0']/following-sibling::div/div[@class='ui-dialog-buttonset']/button/span[contains(text(), 'Add Trigger')]/ancestor::button")
		private WebElement addAppmailTriggerButton;

		@FindBy(xpath = "//div[@id='appmail_trigger_0']/following-sibling::div/div[@class='ui-dialog-buttonset']/button/span[contains(text(), 'Cancel')]/ancestor::button")
		private WebElement cancelTriggerButton;

		public AppmailTriggers() {
			PageFactory.initElements(driver, this);
			testReporter = ReporterFactory.getTest();
		}

		public boolean isCancelTriggerButtonPresent() {
			if (this.cancelTriggerButton.isDisplayed()) {
				return true;
			}
			return false;
		}

		public boolean isAppmailMemberProfileTriggerPresent() {
			if (this.appmailMemberProfileTrigger.isDisplayed()) {
				return true;
			}
			else {
				return DriverMethods.isElementPresent(driver, this.appmailMemberProfileTrigger);
			}
		}
		
		public boolean isAppmailDateTriggerPresent() {
			if (this.appmailDateTrigger.isDisplayed()) {
				return true;
			}
			return false;
		}

		public boolean isAddTriggerButtonPresent() {
			if (this.addAppmailTriggerButton.isDisplayed()) {
				return true;
			}
			return false;
		}
		
		public ProfileTrigger clickOnMemberProfileTrigger() throws InterruptedException {
			if(this.isAppmailMemberProfileTriggerPresent()) {
				act.moveToElement(this.appmailMemberProfileTrigger).build().perform();
				this.appmailMemberProfileTrigger.click();
				Thread.sleep(3000);
			}
			return new ProfileTrigger();
		}
//		
//		public DateTrigger clickOnDateTrigger() throws InterruptedException {
//			if(this.isappmailDateTriggerPresent()) {
//				act.moveToElement(this.appmailDateTrigger).build().perform();
//				this.appmailDateTrigger.click();
//				Thread.sleep(3000);
//			}
//			return new DateTrigger();
//		}
		
		public class ProfileTrigger {

			@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='1']/following-sibling::div//div[@id='profile_trigger_div']//div/input[@class='textbox sm-textbox inlineComponent']")
			private WebElement appmailProfileTriggerTimeDelayInlineInputTextBox;

			@FindBy(xpath = "//div[@id='appmail_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='1']/following-sibling::div//div[@id='profile_trigger_div']//div/input[@id='msg_field_value_appmail_0']")
			private WebElement appmailProfiletriggerFieldValueTextBox;

			public ProfileTrigger() {
				PageFactory.initElements(driver, this);
				testReporter = ReporterFactory.getTest();
			}

			public boolean isAppmailProfileTriggerTimeDelayInlineInputTextBoxPresent() {
				if (this.appmailProfileTriggerTimeDelayInlineInputTextBox.isDisplayed()) {
					return true;
				}
				return false;
			}

			public boolean isAppmailProfiletriggerFieldValueTextBoxPresent() {
				if (this.appmailProfiletriggerFieldValueTextBox.isDisplayed()) {
					return true;
				}
				return false;
			}
			
			public AppmailMessageComposePage clickOnSubmitTriggerButton() throws InterruptedException {
				System.out.println("Inside method appmail clickOnSubmitTriggerButton");
				if(isAddTriggerButtonPresent()) {
					System.out.println("Inside if condition is addtrigger button ");
					act.moveToElement(addAppmailTriggerButton).build().perform();
					System.out.println("moved to element clickOnSubmitTriggerButton");
					addAppmailTriggerButton.click();
					System.out.println("clicked on add trigger button");
					Thread.sleep(10000);
				}
				else {
					System.out.println("Error : add trigger button not present");
				}
				return new AppmailMessageComposePage(driver);
			}
			
			public ProfileTrigger selectTimeUnitDropDownByVisibleText(String visibleText) throws InterruptedException {
				if(visibleText.equalsIgnoreCase("Minutes")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='0']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Hours")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='1']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Days")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='2']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Weeks")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='3']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Months")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='4']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Immediately")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='5']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Years")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='6']\")).trigger('click');");
				} else {
					System.out.println("Error : Not able to select dropdown, given value '"+visibleText+"' may be incorrect");
				}
				Thread.sleep(3000);
				return new ProfileTrigger();
			}
			
			public ProfileTrigger selectFieldNameDropDownByValue(String value) throws InterruptedException {
				String javascript = "$($(\"#dk_container_appmail_message_0_trigger_profile_fieldName .dk_options a[data-dk-dropdown-value='"+value+"']\")).trigger('click');";
				js.executeScript(javascript);
				Thread.sleep(3000);
				return new ProfileTrigger();
			}
			
			public ProfileTrigger selectProfileChangeDropDownByValue(String value) throws InterruptedException {
				if(value.equalsIgnoreCase("Any Value")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='0']\")).trigger('click');");
				} else if(value.equalsIgnoreCase("Specific Value")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='1']\")).trigger('click');");
				} else if(value.equalsIgnoreCase("New Value Only")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='2']\")).trigger('click');");
				} else if(value.equalsIgnoreCase("Any Specific Value")) {
					js.executeScript("$($(\"#dk_container_appmail_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='3']\")).trigger('click');");
				} else {
					System.out.println("Error : Not able to select dropdown, given value '"+value+"' may be incorrect");
				}
				Thread.sleep(2000);
				return new ProfileTrigger();
			}
			
		}
	}
}
