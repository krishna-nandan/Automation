package com.mPulse.testCases;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

public class GridDemo {
	
	@Test
	public void testdemo() throws MalformedURLException, InterruptedException {
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setPlatform(Platform.LINUX);
		cap.setBrowserName("chrome");
		
		RemoteWebDriver driver = new RemoteWebDriver(new URL("http://172.17.0.1:5566/wd/hub"), cap);
        driver.get("https://www.google.com");
        Thread.sleep(5000);
        driver.quit();
	}

}
