package com.mPulse.utility;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;

public class DriverMethods {
	public static boolean isElementPresent(WebDriver driver, WebElement element) {

		if (element.isDisplayed() && element.isEnabled()) {
			System.out.println("Element found in first time");
			return true;
		} else {

			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
			wait.pollingEvery(5, TimeUnit.SECONDS);
			wait.withTimeout(90, TimeUnit.SECONDS);
			wait.ignoring(NoSuchElementException.class);

			Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver arg0) {

					if (element.isDisplayed() && element.isEnabled()) {
						System.out.println("Element found after wait");
						return true;
					}
					System.out.println("Waiting for element to be enabled and polling in every 5 SECONDS Max to 90 Secounds");
					return false;
				}

			};

			wait.until(function);
			return true;
		}
	}
	
	public static boolean waitForElementUntillDisappear(WebDriver driver, WebElement element) {

		if (!(element.isDisplayed() && element.isEnabled())) {
			System.out.println("waiting Element is not enabled and displayed");
			return true;
		} else {

			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
			wait.pollingEvery(5, TimeUnit.SECONDS);
			wait.withTimeout(90, TimeUnit.SECONDS);
			wait.ignoring(NoSuchElementException.class);

			Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver arg0) {

					if (!(element.isDisplayed() && element.isEnabled())) {
						System.out.println("Waiting Element is not enabled and displayed");
						return true;
					}
					System.out.println("Waiting for element to disappear and polling in every 5 SECONDS Max to 90 Secounds");
					return false;
				}

			};

			wait.until(function);
			return true;
		}
	}
}
