package com.mPulse.objectRepository;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.utility.DriverMethods;
import com.relevantcodes.extentreports.ExtentTest;

public class EmailMessageComposePage {
	
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	JavascriptExecutor js;
	
	@FindBy (xpath = "//a[@href='/home' and contains(text(), 'mPulse Mobile')]")
	private WebElement homePageLink;
	
	@FindBy (xpath = "//div[@id='common_workarea']/div[@class='saving-bar']/div[@class='loadingmessage']/span")
	private WebElement loaderSavingBar;
	
	@FindBy (xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//textarea[contains(@placeholder, 'enter your email subject')]")
	private WebElement emailSubjectTextBox;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//input[@id='trackClickThrough']")
	private WebElement emailLinkShorteningCheckBox;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//input[@id='enableDeeplinkForSubPreferences']")
	private WebElement emailEnableSubPreferencesCheckBox;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//input[@id='enableDeeplinkWithSecuremessage' and contains(@name, 'Securemessage')]")
	private WebElement emailSecureMessageCheckBox;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@class='allow-redirect']/preceding-sibling::div/span/select[@id='selectedAppmailExpire_0']")
	private WebElement emailDeeplinkValidSelectBox;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@class='allow-redirect']/input[@type='checkbox']")
	private WebElement redirectUrlCheckBox;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@class='allow-redirect']/input[@type='text']")
	private WebElement redirectUrlTextBox;
	
	@FindBy (xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@class='nav-pill-item email_design_btn']")
	private WebElement designEmailButton;
	
	@FindBy (xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@id='email_tabs']//a[contains(text (), 'Edit Email')]")
	private WebElement editEmailButton;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@class='template-view new_template_view']//div[@class='template-type']/input[@class='btn plainEmail']")
	private WebElement plainEmailSelectButton;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//input[@id='enableDeeplinkWithSecuremessage' and contains(@name, 'Frequency')]")
	private WebElement emailFrequencyControlCheckBox;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@id='plainTextEditor_0']//div[@class='ace_layer ace_text-layer']/div[1]/div")
	private WebElement emailTextBoxFirstLine;
	
	@FindBy(xpath = "//form[@id='event_email_compose_0']//div[@id='email_tabs']//div[@class='left-button-wrap']/span[contains(text(), 'Back to Templates')]")
	private WebElement emailBackToTemplates;
	
	@FindBy (xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div[@class='right-button-wrap']/a[contains(@class, 'design-save')]")
	private WebElement emailComposeSaveButton;
	
	@FindBy (xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']/div/div/div[@class='popup container-popup']/div/div[@class='popup-header clearfix']/span")
	private WebElement closeComposeEmailButton;

	@FindBy(xpath = "//div[@id='email-message-accordion']//form[@id='event_email_compose_0']//div/div[@id='add_trigger']")
	private WebElement emailAddTrigger;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Next')]/ancestor::button[@id='message_submit']")
	private WebElement campaignNextButton;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Save')]/ancestor::button[@class='icon_button only_save shift_right']")
	private WebElement campaignSaveButton;
	
	@FindBy (xpath = "//div[@class='common_form_control campaign_details_control']/button/span[contains(text(), 'Next')]/ancestor::button[@id='message_submit']/following-sibling::a[@class='common_form_cancel']")
	private WebElement campaignBackButton;

	public EmailMessageComposePage(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
		js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public boolean isCampaignSaveButtonPresent() {
		if(this.campaignSaveButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignBackButtonPresent() {
		if(this.campaignBackButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isCampaignNextButtonPresent() {
		if(this.campaignBackButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEmailSubjectTextBoxPresent() {
		if(this.emailSubjectTextBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEmailLinkShorteningCheckBoxPresent() {
		if(this.emailLinkShorteningCheckBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEmailEnableSubPreferencesCheckBoxPresent() {
		if(this.emailEnableSubPreferencesCheckBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEmailSecureMessageCheckBoxPresent() {
		if(this.emailSecureMessageCheckBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isemailDeeplinkValidSelectBoxPresent() {
		if (this.emailDeeplinkValidSelectBox.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isDesignEmailButtonPresent() {
		if(this.designEmailButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEditEmailButtonPresent() {
		if(this.editEmailButton.isDisplayed()) {
			return true;
		}
		return false;
	}

	public boolean isPlainEmailSelectButtonPresent() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		if (this.plainEmailSelectButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isEmailComposeSaveButtonPresent() {
		if (this.emailComposeSaveButton.isDisplayed()) {
			return true;
		}
		return false;
	}

	public boolean isEmailAddTriggerPresent() {
		if (this.emailAddTrigger.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isComposeEmailCloseButtonPresent() {
		if (this.closeComposeEmailButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public EmailMessageComposePage enterEmailSubject(String emailSubject) throws InterruptedException {
		DriverMethods.waitForElementUntillDisappear(this.driver, this.loaderSavingBar);
		act.moveToElement(this.emailSubjectTextBox).build().perform();
		this.emailSubjectTextBox.click();
		this.emailSubjectTextBox.clear();
		this.emailSubjectTextBox.sendKeys(emailSubject);
		Thread.sleep(3000);
		System.out.println("Info : Email subject text box is filled with value "+emailSubject);
		return new EmailMessageComposePage(this.driver);
	}
	
	public EmailMessageComposePage selectLinkTrackingCheckBox() throws InterruptedException {
		if(this.isEmailLinkShorteningCheckBoxPresent()) {
			act.moveToElement(this.emailLinkShorteningCheckBox).click().build().perform();
			//this.emailLinkShorteningCheckBox.click();
			DriverMethods.isElementPresent(driver, homePageLink);
		}
		return new EmailMessageComposePage(driver);
	}

	public EmailMessageComposePage clickOnDesignEmail() throws InterruptedException {
		if(this.isDesignEmailButtonPresent()) {
			act.moveToElement(this.designEmailButton).click(this.designEmailButton).build().perform();
			Thread.sleep(5000);
		} else {
			System.out.println("Error : Design Email Button are not present or not display");
		}
		return new EmailMessageComposePage(this.driver);
	}
	
	public EmailMessageComposePage clickOnEditEmail() throws InterruptedException {
		if(this.isEditEmailButtonPresent()) {
			act.moveToElement(this.editEmailButton).click(this.editEmailButton).build().perform();
			Thread.sleep(5000);
		} else {
			System.out.println("Error : Edit Email Button are not present or not display");
		}
		return new EmailMessageComposePage(this.driver);
	}
	
	public EmailMessageComposePage clickOnPlainEmailSelectButton() throws InterruptedException {
		if(this.isPlainEmailSelectButtonPresent()) {
			act.moveToElement(this.plainEmailSelectButton).click(this.plainEmailSelectButton).build().perform();
			Thread.sleep(5000);
		} else {
			System.out.println("Error : Plain Email Button are not present or not display");
		}
		return new EmailMessageComposePage(this.driver);
	}
	
	public CampaignComposePage clickOnSaveButton() throws InterruptedException {
		if(this.isCampaignSaveButtonPresent()) {
			this.campaignSaveButton.click();
			Thread.sleep(5000);
			DriverMethods.waitForElementUntillDisappear(this.driver, this.loaderSavingBar);
			DriverMethods.isElementPresent(driver, homePageLink);
		}
		return new CampaignComposePage(driver);
	}
	
	public EmailMessageComposePage clickOnEmailComposeSaveButton() throws InterruptedException {
		if(this.isEmailComposeSaveButtonPresent()) {
			this.emailComposeSaveButton.click();
			//Thread.sleep(6000);
			DriverMethods.waitForElementUntillDisappear(this.driver, this.loaderSavingBar);
			DriverMethods.isElementPresent(driver, this.closeComposeEmailButton);
		}
		return new EmailMessageComposePage(driver);
	}
	
	public EmailMessageComposePage enterEmailText(String emailText) throws InterruptedException {
		js.executeScript("jQuery('div.ace_content div.ace_text-layer div.ace_line_group div.ace_line').first().text('Hi, ##FIRSTNAME## ! " + emailText +
				" ##LIST.PERMISSION_REMINDER##');");
		return new EmailMessageComposePage(driver);
	}
	
	public EmailMessageComposePage clickOnEmailComposeCloseButton() throws InterruptedException {
		if(this.isComposeEmailCloseButtonPresent()) {
			this.closeComposeEmailButton.click();
//			act.moveToElement(this.closeComposeEmailButton).build().perform();
//			act.doubleClick().build().perform();
			//js.executeAsyncScript("jQuery('span.close_bee_design').first().click();");
			Thread.sleep(10000);
			act.keyDown(Keys.CONTROL).sendKeys("-").keyUp(Keys.CONTROL).perform();
			act.keyDown(Keys.CONTROL).sendKeys("+").keyUp(Keys.CONTROL).perform();
			DriverMethods.isElementPresent(driver, homePageLink);
		}
		return new EmailMessageComposePage(driver);
	}
	
	public CampaignPlanPage clickOnBackButton() {
		if(this.isCampaignBackButtonPresent()) {
			this.campaignBackButton.click();
			DriverMethods.isElementPresent(driver, homePageLink);
		}
		return new CampaignPlanPage(driver);
	}

	public EmailTriggers clickOnAddTriggerButton() throws InterruptedException {
		if (this.isEmailAddTriggerPresent()) {
			act.moveToElement(this.emailAddTrigger).build().perform();
			this.emailAddTrigger.click();
			DriverMethods.isElementPresent(driver, homePageLink);
			Thread.sleep(8000);
		} else {
			System.out.println("Error : Email Trigger are not present");
		}
		return new EmailTriggers();
	}
	
	public class EmailTriggers {

		@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='4']")
		private WebElement emailOneTimeTrigger;

		@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='3']")
		private WebElement emailRecurrenceTrigger;

		@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='0']")
		private WebElement emailDateTrigger;

		@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='2']")
		private WebElement emailMemberEngagementTrigger;

		@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='1']")
		private WebElement emailMemberProfileTrigger;

		@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='5']")
		private WebElement emailGetMemberInfoTrigger;

		@FindBy(xpath = "//div[@class='ui-dialog-buttonset']/button/span[contains(text(), 'Add Trigger')]/ancestor::button")
		private WebElement addTriggerButton;

		@FindBy(xpath = "//div[@class='ui-dialog-buttonset']/button/span[contains(text(), 'Cancel')]/ancestor::button")
		private WebElement cancelTriggerButton;

		public EmailTriggers() {
			PageFactory.initElements(driver, this);
			testReporter = ReporterFactory.getTest();
		}

		public boolean isCancelTriggerButtonPresent() {
			if (this.cancelTriggerButton.isDisplayed()) {
				return true;
			}
			return false;
		}

		public boolean isEmailMemberProfileTriggerPresent() {
			if (this.emailMemberProfileTrigger.isDisplayed()) {
				return true;
			}
			else {
				return DriverMethods.isElementPresent(driver, this.emailMemberProfileTrigger);
			}
		}
		
		public boolean isEmailDateTriggerPresent() {
			if (this.emailDateTrigger.isDisplayed()) {
				return true;
			}
			return false;
		}

		public boolean isAddTriggerButtonPresent() {
			if (this.addTriggerButton.isDisplayed()) {
				return true;
			}
			return false;
		}
		
		public ProfileTrigger clickOnMemberProfileTrigger() throws InterruptedException {
			if(this.isEmailMemberProfileTriggerPresent()) {
				act.moveToElement(this.emailMemberProfileTrigger).build().perform();
				this.emailMemberProfileTrigger.click();
				Thread.sleep(3000);
			}
			return new ProfileTrigger();
		}
		
		public DateTrigger clickOnDateTrigger() throws InterruptedException {
			if(this.isEmailDateTriggerPresent()) {
				act.moveToElement(this.emailDateTrigger).build().perform();
				this.emailDateTrigger.click();
				Thread.sleep(3000);
			}
			return new DateTrigger();
		}

		public class ProfileTrigger {

			@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='1']/following-sibling::div//div[@id='profile_trigger_div']//div/input[@class='textbox sm-textbox inlineComponent']")
			private WebElement emailProfileTriggerTimeDelayInlineInputTextBox;

			@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='1']/following-sibling::div//div[@id='profile_trigger_div']//div/input[@id='msg_field_value_email_0']")
			private WebElement emailProfiletriggerFieldValueTextBox;

			public ProfileTrigger() {
				PageFactory.initElements(driver, this);
				testReporter = ReporterFactory.getTest();
			}

			public boolean isEmailProfileTriggerTimeDelayInlineInputTextBoxPresent() {
				if (this.emailProfileTriggerTimeDelayInlineInputTextBox.isDisplayed()) {
					return true;
				}
				return false;
			}

			public boolean isSmsProfiletriggerFieldValueTextBoxPresent() {
				if (this.emailProfiletriggerFieldValueTextBox.isDisplayed()) {
					return true;
				}
				return false;
			}
			
			public EmailMessageComposePage clickOnSubmitTriggerButton() throws InterruptedException {
				if(isAddTriggerButtonPresent()) {
					act.moveToElement(addTriggerButton).build().perform();
					addTriggerButton.click();
					Thread.sleep(3000);
				}
				else {
					System.out.println("Error : add trigger button not present");
				}
				return new EmailMessageComposePage(driver);
			}
			
			public ProfileTrigger selectTimeUnitDropDownByVisibleText(String visibleText) throws InterruptedException {
				if(visibleText.equalsIgnoreCase("Minutes")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='0']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Hours")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='1']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Days")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='2']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Weeks")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='3']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Months")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='4']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Immediately")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='5']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Years")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_timeUnits .dk_options a[data-dk-dropdown-value='6']\")).trigger('click');");
				} else {
					System.out.println("Error : Not able to select dropdown, given value '"+visibleText+"' may be incorrect");
				}
				Thread.sleep(3000);
				return new ProfileTrigger();
			}
			
			public ProfileTrigger selectFieldNameDropDownByValue(String value) throws InterruptedException {
				String javascript = "$($(\"#dk_container_email_message_0_trigger_profile_fieldName .dk_options a[data-dk-dropdown-value='"+value+"']\")).trigger('click');";
				js.executeScript(javascript);
				Thread.sleep(3000);
				return new ProfileTrigger();
			}
			
			public ProfileTrigger selectProfileChangeDropDownByValue(String value) throws InterruptedException {
				if(value.equalsIgnoreCase("Any Value")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='0']\")).trigger('click');");
				} else if(value.equalsIgnoreCase("Specific Value")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='1']\")).trigger('click');");
				} else if(value.equalsIgnoreCase("New Value Only")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='2']\")).trigger('click');");
				} else if(value.equalsIgnoreCase("Any Specific Value")) {
					js.executeScript("$($(\"#dk_container_email_message_0_trigger_profile_profileChangeType .dk_options a[data-dk-dropdown-value='3']\")).trigger('click');");
				} else {
					System.out.println("Error : Not able to select dropdown, given value '"+value+"' may be incorrect");
				}
				Thread.sleep(2000);
				return new ProfileTrigger();
			}
			
		}

		public class DateTrigger {

			@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='0']/following-sibling::div//div[@id='date_trigger_div']//input[@class='textbox sm-textbox inlineComponent']")
			private WebElement emailDateTriggerTimeTextBox;

			@FindBy(xpath = "//div[@id='email_trigger_0' and contains(@style, 'block')]//fieldset/div/h3[@triggertype='0']/following-sibling::div//div[@id='date_trigger_div']/div/input[@class='textbox sm-textbox inlineComponent']")
			private WebElement emailDateTriggerTimePickerTextBox;

			public DateTrigger() {
				PageFactory.initElements(driver, this);
				testReporter = ReporterFactory.getTest();
			}
			
			public boolean isSmsDateTriggerTimeTextBoxPresent() {
				if (this.emailDateTriggerTimeTextBox.isDisplayed()) {
					return true;
				}
				return false;
			}
			
			public DateTrigger enterDelyAmountOfTime(String number) {
				if(this.isSmsDateTriggerTimeTextBoxPresent()) {
					this.emailDateTriggerTimePickerTextBox.sendKeys(number);
					//this.smsDateTriggerTimePickerTextBox.sendKeys(Keys.TAB);
				}
				return new DateTrigger();
			}
			
			public DateTrigger selectTimeUnitDropDownByVisibleText(String visibleText) throws InterruptedException {
				if(visibleText.equalsIgnoreCase("Minutes")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_timeUnits .dk_options a[data-dk-dropdown-value='0']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Hours")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_timeUnits .dk_options a[data-dk-dropdown-value='1']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Days")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_timeUnits .dk_options a[data-dk-dropdown-value='2']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Weeks")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_timeUnits .dk_options a[data-dk-dropdown-value='3']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Months")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_timeUnits .dk_options a[data-dk-dropdown-value='4']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Immediately")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_timeUnits .dk_options a[data-dk-dropdown-value='5']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("Years")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_timeUnits .dk_options a[data-dk-dropdown-value='6']\")).trigger('click');");
				} else {
					System.out.println("Error : Not able to select dropdown, given value '"+visibleText+"' may be incorrect");
				}
				Thread.sleep(3000);
				return new DateTrigger();
			}
			
			public DateTrigger selectTriggerDateFieldNameByVisibleText(String visibleText) throws InterruptedException {
				String javascript = "$($(\"#dk_container_sms_message_0_trigger_date_fieldName .dk_options a[data-dk-dropdown-value='"+visibleText+"']\")).trigger('click');";
				js.executeScript(javascript);
				Thread.sleep(3000);
				return new DateTrigger();
			}
			
			public DateTrigger selectMemberTimeFieldByVisibleText(String visibleText) throws InterruptedException {
				String javascript = "$($(\"#dk_container_sms_message_0_trigger_date_memberTimeField .dk_options a[data-dk-dropdown-value='"+visibleText+"']\")).trigger('click');";
				js.executeScript(javascript);
				Thread.sleep(3000);
				return new DateTrigger();
			}
			
			public DateTrigger selectTriggerDateCondition(String visibleText) throws InterruptedException {
				if(visibleText.equalsIgnoreCase("Before")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_when .dk_options a[data-dk-dropdown-value='0']\")).trigger('click');");
				} else if(visibleText.equalsIgnoreCase("After")) {
					js.executeScript("$($(\"#dk_container_sms_message_0_trigger_date_when .dk_options a[data-dk-dropdown-value='1']\")).trigger('click');");
				} else {
					System.out.println("Error : Not able to select dropdown, given value '"+visibleText+"' may be incorrect");
				}
				Thread.sleep(3000);
				return new DateTrigger();
			}
			
			public SmsMessageComposePage clickOnSubmitTriggerButton() throws InterruptedException {
				if(isAddTriggerButtonPresent()) {
					act.moveToElement(addTriggerButton).build().perform();
					addTriggerButton.click();
					Thread.sleep(3000);
				}
				else {
					System.out.println("Error : add trigger button not present");
				}
				return new SmsMessageComposePage(driver);
			}
			
		}

	}

}
