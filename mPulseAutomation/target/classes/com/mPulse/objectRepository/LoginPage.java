package com.mPulse.objectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class LoginPage {

	WebDriver driver;
	ExtentTest testReporter;

	@FindBy(id = "email")
	@CacheLookup
	private WebElement userName;

	@FindBy(id = "password")
	@CacheLookup
	private WebElement password;

	@FindBy(id = "login_button")
	@CacheLookup
	private WebElement loginButton;

	@FindBy(xpath = "//*[@id='remmeber_user_password']/label")
	@CacheLookup
	private WebElement rememberMe;

	@FindBy(linkText = "Forgot Password")
	@CacheLookup
	private WebElement forgotPassword;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}

	public void enterUserName(String userName) {
		if (this.userName.isDisplayed() && this.userName.isEnabled()) {
			testReporter.log(LogStatus.INFO, "UserName textbox found and going to fill user name");
			this.userName.clear();
			this.userName.sendKeys(userName);
			testReporter.log(LogStatus.INFO, "user name '"+userName+"' entered in username textbox");
		} else
			System.out.println("Error: Element 'user name' not Displayed or not Enabled");
	}

	public void enterPassword(String password) {
		if (this.password.isDisplayed() && this.password.isEnabled()) {
			testReporter.log(LogStatus.INFO, "password textbox found and going to fill password");
			this.password.clear();
			this.password.sendKeys(password);
			testReporter.log(LogStatus.INFO, "password '"+password+"' entered in password textbox");
		} else {
			System.out.println("Error: Element 'password' not Displayed or not Enabled");
		}
	}

	public void clickOnLoginButton() {
		if (this.loginButton.isDisplayed() && this.loginButton.isEnabled()) {
			testReporter.log(LogStatus.INFO, "Login button found and going to click login button");
			loginButton.click();
			testReporter.log(LogStatus.INFO, "Login button clicked");
		} else {
			System.out.println("Error: Element 'login button' not Displayed or not Enabled");
		}
	}

	public HomePage login(String userName, String password) {
		enterUserName(userName);
		enterPassword(password);
		clickOnLoginButton();
		testReporter.log(LogStatus.INFO, "Login successfully with user name '"+userName+"' and password '"+password+"'");
		return new HomePage(driver);
	}

	public boolean isRememberMePresent() {
		if (this.rememberMe.isDisplayed() && this.rememberMe.isEnabled()) {
			return true;
		} else {
			System.out.println("Error: Remember me text not present on login page");
			return false;
		}
	}

	public boolean isForgotPasswordPresent() {
		if (this.forgotPassword.isDisplayed() && this.forgotPassword.isEnabled()) {
			return true;
		}
		return false;
	}

}
