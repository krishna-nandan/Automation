package com.mPulse.objectRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class EventDefinitionsPage extends HomePage {
	
	WebDriver driver;
	ExtentTest testReporter;
	Actions act;
	JavascriptExecutor js;
	private boolean flag = false;
	
	@FindBy (id = "add_event")
	private WebElement newEventButton;
	
	@FindBy (xpath = "//div[@class='ui-dialog-buttonset']//span[text()='Confirm Deletion']/ancestor::button")
	private WebElement confirmDeletionButton;
	
	@FindBy (xpath = "//div[@class='ui-dialog-buttonset']//span[text()='OK']/ancestor::button")
	private WebElement canNotDeleteOKButton;
	
	@FindBy (xpath = "//form[@id='paginationForm']//li[@class='next_page']/a")
	private WebElement nextPageNavigationButton;
	
	@FindBy (xpath = "//table[@id='users']//tbody/tr/td[1]")
	private List<WebElement> allEventName;
	
	public boolean isNewEventButtonPresent() {
		if (this.newEventButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isConfirmDeletionButtonPresent() {
		if (this.confirmDeletionButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isNextPageNavigationButtonPresent() {
		if (this.nextPageNavigationButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public boolean isCanNotDeleteOKButtonPresent() {
		if (this.canNotDeleteOKButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	public EventDefinitionsPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		act = new Actions(driver);
		js = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}
	
	public NewEventPage goToCreateNewEventPage() {
		
		if (this.isNewEventButtonPresent()) {
			this.newEventButton.click();
		} else {
			System.out.println("Error: New Event page Button not present");
		}
		return new NewEventPage(this.driver);
	}
	
	public EventDefinitionsPage clickOnConfirmDeletionButton() {
		
		if (this.isConfirmDeletionButtonPresent()) {
			this.confirmDeletionButton.click();
		} else {
			System.out.println("Error: Confirm Deletion Button not present");
		}
		return new EventDefinitionsPage(this.driver);
	}
	
	public EventDefinitionsPage clickOnNextPageNavigationButton() {
		
		if (this.isNextPageNavigationButtonPresent()) {
			act.moveToElement(this.nextPageNavigationButton).build().perform();
			this.nextPageNavigationButton.click();
		} else {
			System.out.println("Error: Next page Navigation Button not present");
		}
		return new EventDefinitionsPage(this.driver);
	}
	
	public EventDefinitionsPage clickOnCanNotDeleteOKButton() {
		
		if (this.isCanNotDeleteOKButtonPresent()) {
			this.canNotDeleteOKButton.click();
		} else {
			System.out.println("Error: OK Button not present");
		}
		return new EventDefinitionsPage(this.driver);
	}
	
	public NewEventPage clickOnEditEventButtonByEventName(String eventName) {
		boolean flag = false;
		do {
			flag = this.clickEditEventButton(eventName);
			if(flag == false)
				this.clickOnNextPageNavigationButton();
		}while(flag == false);
		System.out.println("Navigated to list SubscribersPage of list '"+eventName+"'");
		return new NewEventPage(this.driver);
	}
	
	private boolean clickEditEventButton(String eventName) {
		String xpath = "//table[@id='users']//tbody/tr/td[text()='"+ eventName +"']/following-sibling::td[last()]/a[@class='edit_event']";
		if (this.isEventPresentByName(eventName)) {
			WebElement element = this.driver.findElement(By.xpath(xpath));
			if (element.isDisplayed()) {
				act.moveToElement(element).build().perform();
				act.click(element).build().perform();
				return true;
			}
			System.out.println("Requested Event with name '"+eventName+"' not found");
			return false;
		}
		return false;
	}
	
	private boolean isEventNameDisplayOnPage(String eventName) {
		for (WebElement element : this.allEventName) {
			if (element.getText().equals(eventName)) {
				return element.isDisplayed();
			}
		}
		System.out.println("Requested Event with name '"+eventName+"' not found on Event Page");
		return false;
	}
	
	public boolean isEventPresentByName(String eventName) {
		boolean flag = false;
		do {
			flag = this.isEventNameDisplayOnPage(eventName);
			if(flag == false) {
				this.clickOnNextPageNavigationButton();
			} else {
				return true;
			}
		}while(flag == false);
		return false;
	}
	
	private EventDefinitionsPage clickOnDeleteEventButtonByEventName(String eventName) {
		String xpath = "//table[@id='users']//tbody/tr/td[text()='"+ eventName +"']/following-sibling::td[last()]/a[@class='delete_event']";
		if (this.isEventPresentByName(eventName)) {
			WebElement element = this.driver.findElement(By.xpath(xpath));
			if (element.isDisplayed()) {
				act.moveToElement(element).build().perform();
				act.click(element).build().perform();
				return new EventDefinitionsPage(this.driver);
			}
			System.out.println("Requested Event with name '"+eventName+"' not found");
			return new EventDefinitionsPage(this.driver);
		}
		System.out.println("Requested Event with name '"+eventName+"' not found");
		return new EventDefinitionsPage(this.driver);
	}
	
	public EventDefinitionsPage deleteEventByName(String eventName) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		this.clickOnDeleteEventButtonByEventName(eventName);
		System.out.println("Clicked on delete event button");
		this.clickOnConfirmDeletionButton();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		System.out.println("Clicked on confirm deletion button");
		return new EventDefinitionsPage(this.driver);
	}

}
