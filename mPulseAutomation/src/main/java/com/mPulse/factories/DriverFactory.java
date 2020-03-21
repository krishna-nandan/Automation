package com.mPulse.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.mPulse.listeners.WebActionListeners;

public class DriverFactory {
	private static WebDriver firefoxdriver;

	public static Map<Long, WebDriver> nameToTestMap = new HashMap<Long, WebDriver>();

	public synchronized static WebDriver createDriver() {
		Long threadID = Thread.currentThread().getId();
		if (!nameToTestMap.containsKey(threadID)) {
			WebDriver driver = getBrowser();
			nameToTestMap.put(threadID, driver);
		}
		return nameToTestMap.get(threadID);
	}

	public synchronized static WebDriver getDriver() {
		Long threadID = Thread.currentThread().getId();

		if (nameToTestMap.containsKey(threadID)) {
			return nameToTestMap.get(threadID);
		}
		return createDriver();
	}

	public synchronized static void closeDriver() {
		Long threadID = Thread.currentThread().getId();
		getDriver().quit();
		nameToTestMap.remove(threadID);
	}

	private synchronized static WebDriver getBrowser() {

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.proxy.type", ProxyType.AUTODETECT.ordinal());
		firefoxdriver = new FirefoxDriver(profile);
		firefoxdriver.manage().window().maximize();
		firefoxdriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		firefoxdriver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
		EventFiringWebDriver eventDriver = new EventFiringWebDriver(firefoxdriver);
		WebActionListeners webListeners = new WebActionListeners();
		eventDriver.register(webListeners);
		firefoxdriver = eventDriver;
		return firefoxdriver;
	}

}
