package com.mPulse.utility;

import static io.restassured.RestAssured.expect;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.mPulse.objectRepository.HomePage;

public class UtilityMethods {

	public static synchronized String getScreenShot(WebDriver driver, String ScreenShotName) {
		String fileName = (new Date()).toString().replace(" ", "_").replace(":", "-").trim() + ".png";
		String destinationFilePath = "./ScreenShots/" + ScreenShotName + "_" + fileName;
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(source, new File(destinationFilePath));
		} catch (Exception e) {
			System.out.println("Exception while taking screen shot " + e.getMessage());
		}
		System.out.println("Screen shot taken");
		return destinationFilePath;
	}

	public static synchronized String getCsvFilePath(String fileName) {
		return System.getProperty("user.dir") + "/src/test/resources/testData" + "/" + fileName + ".csv";
	}
	
	public static synchronized Double getPercentage(int count, int total) {
		Double toBeTruncated = ((count*100.0)/(total*1.0));
		System.out.println("un truncated "+toBeTruncated);
	    Double truncatedDouble=new BigDecimal(toBeTruncated ).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		return truncatedDouble;
	}
	
	public static HomePage createSegment(WebDriver driver, String segmentName) {
		
		HomePage home = new HomePage(driver);
		home.goToAudienceListPage()
		.goToSegmentsPage()
		.goToDefineSegmentPage()
		.enterSegmentName(segmentName)
		.clickOnMemberTab()
		.selectFieldName("Address2")
		.selectRuleByVisibleText("Equal To")
		.enterValueInputTextBox("kanpur")
		.clickOnAddCriteriaLink()
		.selectSecondFieldName("custom number")
		.selectSecondRuleByVisibleText("Equal To")
		.enterSecondValueInputTextBox("789")
		.clickOnSubmitButton()
		.goToHomePage();
		return new HomePage(driver);
	}
	
	public static synchronized String getNewSegmentName() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		return (new Date()).toString().replace(" ", "").replace(":", "").trim().substring(0, 14)+"_seg";
	}
	
	public static synchronized String getDate_YYYY_MM_DD(int delaydate) {
		Date dNow = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dNow);
		cal.add(Calendar.DAY_OF_MONTH, delaydate);
		dNow = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = sdf.format(dNow);
		return strDate;
	}
	
	public static synchronized String getTimeWithDelay(int delayTimeInMinutes) {
		Date dNow = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dNow);
		cal.add(Calendar.MINUTE, delayTimeInMinutes);
		dNow = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String strDate = sdf.format(dNow);
		return strDate;
	}
	
	public static synchronized String getNewCampaignName() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		return "Camp_"+(new Date()).toString().replace(" ", "_").replace(":", "").trim().substring(0, 17);
	}

	public static synchronized void hitMoApi(String shortcode, String phoneNumber, String message) {
		String myJson = String.format(
				"{\"shortcode\":\"%s\", \"phone_number\":\"%s\",  \"carrier\":\"Boost\",  \"message\":\"%s\"}",
				shortcode, phoneNumber, message);
		RestAssured.baseURI = ConfigReader.getConfig("moApiUrl");

		Response r = expect().statusCode(200).given().contentType("application/json")
				.header("Authorization", ConfigReader.getConfig("moApiBasicAuth")).header("X-Ms-Source", "sms")
				.header("X-Ms-Format", "json").header("X-Ms-Async", "false").body(myJson).log().all().when().post();

		//String body = r.getBody().asString();
		Assert.assertEquals(r.statusCode(), 200);
	}
	
	public static synchronized void clickOnConfirmLinkInEmailMailinator(WebDriver driver, String email) throws InterruptedException {
		
		email = email.substring(0, email.indexOf('@'));
		System.out.println("Email is -- "+email);
		driver.navigate().to("https://www.mailinator.com/v3/index.jsp?zone=public&query="+email+"#/#inboxpane");
		Thread.sleep(5000);
		try {
			driver.findElement(By.xpath("//*[@id='InboxCtrl']/section/div/div[2]/ul/li[1]/ul/li/div")).click();
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println("Email inbox not present, clicking on email message");
		}
		driver.findElement(By.xpath("//div[@id='inboxpane']//table[@class='table table-striped jambo_table']//td/a[contains(text(), 'Your confirmation')]")).click();	
		driver.switchTo().frame("msg_body");
		Thread.sleep(5000);
		String text = driver.findElement(By.xpath("html/body/p/a[1]")).getAttribute("href");
		System.out.println("The url is - "+text);
		driver.navigate().to(text);
		
	}
	
	public static synchronized void clickOnUnsubscribeLinkInEmailMailinator(WebDriver driver, String email) throws InterruptedException {
		
		email = email.substring(0, email.indexOf('@'));
		System.out.println("Email is -- "+email);
		driver.navigate().to("https://www.mailinator.com/v3/index.jsp?zone=public&query="+email+"#/#inboxpane");
		Thread.sleep(5000);
		try {
			driver.findElement(By.xpath("//*[@id='InboxCtrl']/section/div/div[2]/ul/li[1]/ul/li/div")).click();
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println("Email inbox not present, clicking on email message");
		}
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id='inboxpane']//table[@class='table table-striped jambo_table']//td/a[contains(text(), 'Welcome')]")).click();	
		driver.switchTo().frame("msg_body");
		Thread.sleep(5000);
		String text = driver.findElement(By.xpath("html/body/a[contains(text(), 'Unsubscribe')]")).getAttribute("href");
		System.out.println("The url is - "+text);
		driver.navigate().to(text);
		
	}
	
	public static synchronized String getSecureMessageLinkFromMailinatorEmail(WebDriver driver, String email) throws InterruptedException {
		
		email = email.substring(0, email.indexOf('@'));
		System.out.println("Email is -- "+email);
		driver.navigate().to("https://www.mailinator.com/v3/index.jsp?zone=public&query="+email+"#/#inboxpane");
		Thread.sleep(5000);
		try {
			driver.findElement(By.xpath("//*[@id='InboxCtrl']/section/div/div[2]/ul/li[1]/ul/li/div")).click();
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println("Email inbox not present, clicking on email message");
		}
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id='inboxpane']//table[@class='table table-striped jambo_table']//td/a[contains(text(), 'email with secure message link')]")).click();	
		driver.switchTo().frame("msg_body");
		Thread.sleep(5000);
		String text = driver.findElement(By.xpath("html/body/a[1]")).getAttribute("href").trim();
		System.out.println("The secure message url is - "+text);
		return text;
	}
	
	public static synchronized String getSubPrefLinkFromMailinatorEmail(WebDriver driver, String email) throws InterruptedException {
		
		email = email.substring(0, email.indexOf('@'));
		System.out.println("Email is -- "+email);
		driver.navigate().to("https://www.mailinator.com/v3/index.jsp?zone=public&query="+email+"#/#inboxpane");
		Thread.sleep(5000);
		try {
			driver.findElement(By.xpath("//*[@id='InboxCtrl']/section/div/div[2]/ul/li[1]/ul/li/div")).click();
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println("Email inbox not present, clicking on email message");
		}
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id='inboxpane']//table[@class='table table-striped jambo_table']//td/a[contains(text(), 'default email message with sub pref link')]")).click();	
		driver.switchTo().frame("msg_body");
		Thread.sleep(5000);
		String text = driver.findElement(By.xpath("html/body/a[1]")).getAttribute("href").trim();
		System.out.println("The sub preference url is - "+text);
		return text;
	}
	
	public static synchronized String getBasicAuth(String username, String password) {
		return "Basic "+ Base64.getEncoder().encodeToString((username +":"+ password).getBytes());
	}

}
