package com.mPulse.factories;

import com.mPulse.listeners.WebActionListeners;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BrowserFactory {

	private static WebDriver firefoxdriver;
	private static WebDriver firefoxdriver2;
	private static WebDriver chromedriver;
	private static WebDriver chromedriver2;
	private static WebDriver chromedriver3;
	private static WebDriver browserstack;
	private static WebDriver chromegrid;
	private static WebDriver firefoxgrid;
	private static String chromedriverPath = getChromedriverPath();

	public static final String USERNAME = "quovantistechnol1";
	public static final String AUTOMATE_KEY = "aqhz9iTMPoscXye2xTCv";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
	
	private BrowserFactory() {}
	public synchronized static WebDriver getBrowser(String browserName) {

		if (browserName.equalsIgnoreCase("firefox")) {
			if (firefoxdriver == null) {
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("network.proxy.type", ProxyType.AUTODETECT.ordinal());
				// WebDriver driver = new FirefoxDriver(profile);
				firefoxdriver = new FirefoxDriver(profile);
				firefoxdriver.manage().window().maximize();
				firefoxdriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				firefoxdriver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(firefoxdriver);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				firefoxdriver = eventDriver;
				return firefoxdriver;

			} else {
				return firefoxdriver;
			}
		}

		if (browserName.equalsIgnoreCase("firefox2")) {
			if (firefoxdriver2 == null) {
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("network.proxy.type", ProxyType.AUTODETECT.ordinal());
				// WebDriver driver = new FirefoxDriver(profile);
				firefoxdriver2 = new FirefoxDriver(profile);
				firefoxdriver2.manage().window().maximize();
				firefoxdriver2.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				firefoxdriver2.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(firefoxdriver2);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				firefoxdriver2 = eventDriver;
				return firefoxdriver2;
			} else {
				return firefoxdriver2;
			}
		}

		if (browserName.equalsIgnoreCase("chrome")) {
			if (chromedriver == null) {
				System.setProperty("webdriver.chrome.driver", chromedriverPath);
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-proxy-server=socks5");
				chromedriver = new ChromeDriver(options);
				chromedriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				chromedriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				chromedriver.manage().window().maximize();
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(chromedriver);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				chromedriver = eventDriver;
				return chromedriver;
			} else {
				return chromedriver;
			}
		}
		
		if (browserName.equalsIgnoreCase("chrome2")) {
			if (chromedriver2 == null) {
				System.setProperty("webdriver.chrome.driver", chromedriverPath);
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-proxy-server=socks5");
				chromedriver2 = new ChromeDriver(options);
				chromedriver2.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				chromedriver2.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				chromedriver2.manage().window().maximize();
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(chromedriver2);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				chromedriver2 = eventDriver;
				return chromedriver2;
			} else {
				return chromedriver2;
			}
		}
		
		if (browserName.equalsIgnoreCase("chrome3")) {
			if (chromedriver3 == null) {
				System.setProperty("webdriver.chrome.driver", chromedriverPath);
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-proxy-server=socks5");
				chromedriver3 = new ChromeDriver(options);
				chromedriver3.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				chromedriver3.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				chromedriver3.manage().window().maximize();
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(chromedriver3);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				chromedriver3 = eventDriver;
				return chromedriver3;
			} else {
				return chromedriver3;
			}
		}
		
		if (browserName.equalsIgnoreCase("browserstack")) {
			if (browserstack == null) {
				DesiredCapabilities caps = new DesiredCapabilities();
			    caps.setCapability("browser", "IE");
			    caps.setCapability("browser_version", "11.0");
			    caps.setCapability("os", "Windows");
			    caps.setCapability("os_version", "10");
			    caps.setCapability("resolution", "1024x768");

			    try {
					browserstack = new RemoteWebDriver(new URL(URL), caps);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			    browserstack.manage().window().maximize();
			    browserstack.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			    browserstack.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(browserstack);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				browserstack = eventDriver;
				return browserstack;

			} else {
				return browserstack;
			}
		}
		
		if (browserName.equalsIgnoreCase("chromegrid")) {
			if (chromegrid == null) {
				String hubURL = "http://172.17.0.2:4444/wd/hub";
				DesiredCapabilities capability = DesiredCapabilities.chrome();
		        capability.setBrowserName("chrome");
		        capability.setPlatform(Platform.LINUX);

			    try {
			    	chromegrid = new RemoteWebDriver(new URL(hubURL), capability);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			    chromegrid.manage().window().maximize();
			    chromegrid.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			    chromegrid.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(chromegrid);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				chromegrid = eventDriver;
				return chromegrid;

			} else {
				return chromegrid;
			}
		}
		
		if (browserName.equalsIgnoreCase("firefoxgrid")) {
			if (firefoxgrid == null) {
				String hubURL = "http://172.17.0.2:4444/wd/hub";
				DesiredCapabilities capability = DesiredCapabilities.firefox();
		        capability.setBrowserName("firefox");
		        capability.setVersion("64.0");
		        capability.setPlatform(Platform.LINUX);

			    try {
			    	firefoxgrid = new RemoteWebDriver(new URL(hubURL), capability);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			    firefoxgrid.manage().window().maximize();
			    firefoxgrid.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			    firefoxgrid.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(firefoxgrid);
				WebActionListeners webListeners = new WebActionListeners();
				eventDriver.register(webListeners);
				firefoxgrid = eventDriver;
				return firefoxgrid;

			} else {
				return firefoxgrid;
			}
		}

		return null;
	}

	public synchronized static WebDriver getDriver(String browserName) {
		if (browserName.equalsIgnoreCase("firefox")) {
			return getBrowser(browserName);
		}

		if (browserName.equalsIgnoreCase("firefox2")) {
			return getBrowser(browserName);
		}

		if (browserName.equalsIgnoreCase("chromeL")) {
			return getBrowser(browserName);
		}
		if (browserName.equalsIgnoreCase("chromeL1")) {
			return getBrowser(browserName);
		}

		if (browserName.equalsIgnoreCase("chromeL")) {
			return getBrowser(browserName);
		}

		if (browserName.equalsIgnoreCase("chromeL2")) {
			return getBrowser(browserName);
		}
		
		if (browserName.equalsIgnoreCase("chromegrid")) {
			return getBrowser(browserName);
		}
		
		if (browserName.equalsIgnoreCase("firefoxgrid")) {
			return getBrowser(browserName);
		}

		return null;
	}

	public synchronized static void closeWebDriver(String browserName) {
		if (browserName.equalsIgnoreCase("firefox")) {
			if (firefoxdriver != null) {
				getBrowser(browserName).quit();
			}			
		}

		if (browserName.equalsIgnoreCase("firefox2")) {
			if (firefoxdriver2 != null) {
				getBrowser(browserName).quit();
			}
		}

		if (browserName.equalsIgnoreCase("firefox3")) {
			if (firefoxdriver2 != null) {
				getBrowser(browserName).quit();
			}
		}

		if (browserName.equalsIgnoreCase("chrome")) {
			if (chromedriver == null) {
				
			}
			else {
				getBrowser(browserName).quit();
			}
		}
		
		if (browserName.equalsIgnoreCase("chrome2")) {
			if (chromedriver2 != null) {
				getBrowser(browserName).quit();
			}
		}
		
		if (browserName.equalsIgnoreCase("chrome3")) {
			if (chromedriver3 != null) {
				getBrowser(browserName).quit();
			}
		}

	}
	
	public synchronized static String getChromedriverPath() {
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("linux")) {
			return System.getProperty("user.dir") + "/src/test/resources/chromedriver_linux";
		}
		else if (os.toLowerCase().contains("mac")) {
			return System.getProperty("user.dir") + "/src/test/resources/chromedriver_mac";
		}
		else if (os.toLowerCase().contains("window")) {
			return System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe";
		}
		else {
			System.out.println("ERROR: Operating System is not in the defined range - "+os);
		}
		return "";
	}

}
