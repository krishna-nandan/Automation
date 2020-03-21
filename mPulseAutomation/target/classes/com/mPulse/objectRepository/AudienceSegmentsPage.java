package com.mPulse.objectRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.mPulse.factories.ReporterFactory;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class AudienceSegmentsPage {

	WebDriver driver;
	ExtentTest testReporter;
	private boolean flag = false;

	@FindBy(id = "add_segment")
	private WebElement newSegmentButton;
	
	@FindBy(xpath = "//a[@class='home']")
	private WebElement homePageLink;

	@FindBy(id = "import_segment")
	private WebElement importSegmentButton;
	
	@FindBy(xpath = "//div[@class='segment_name']/a")
	private List<WebElement> allSegments;
	
	@FindBy(xpath = "//li[@class='next_page']/a")
	private WebElement nextPageNavigationButton;
	
	@FindBy (xpath = "//a[@class='segment_count']/span[@class='number']")
	private WebElement segmentedMemberCount;
	
	@FindBy (xpath = "//a[@class='segment_count']/span[@class='ratio']")
	private WebElement segmentPercentageCount;
	
	@FindBy (xpath = "//a[@class='segment_count']/span[1]")
	private WebElement memberCount;
	
	@FindBy (xpath = "//a[@class='segment_count']/span[2]")
	private WebElement memberPercentage;
	
	@FindBy (xpath = "//div[@class='unique_sub_count_div' and @style='']/div[@class='loader-div']")
	private WebElement segmentLoader;

	public AudienceSegmentsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		testReporter = ReporterFactory.getTest();
	}

	public boolean isAddSegmentButtonPresent() {
		if (this.newSegmentButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isHomeButtonPresent() {
		if (this.homePageLink.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isImportSegmentButtonPresent() {
		if (this.importSegmentButton.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	private boolean clickOnSegmentByName(String segmentName) {
		String segmentXpath = "//*[@id='segments']//a[contains(text(),'"+segmentName+"')]";
		if(this.driver.findElement(By.xpath(segmentXpath)).isDisplayed()) {
			this.driver.findElement(By.xpath(segmentXpath)).click();
			testReporter.log(LogStatus.INFO, "Requested segment with name '"+segmentName+"' found and clicked");
			return true;
		}
		else {
			for (WebElement element : allSegments) {
				if (element.getText().equals(segmentName)) {
					element.click();
					testReporter.log(LogStatus.INFO, "Requested segment with name '"+segmentName+"' found and clicked");
					return true;
				}
			}
		}
		testReporter.log(LogStatus.ERROR, "Requested segment with name '"+segmentName+"' not found");
		return false;
	}
	
	public boolean isSegmentPresentOnFirstPage(String segmentName) {
		String segmentXpath = "//*[@id='segments']//a[contains(text(),'"+segmentName+"')]";
		if(this.driver.findElement(By.xpath(segmentXpath)).isDisplayed()) {
			testReporter.log(LogStatus.INFO, "Requested segment with name '"+segmentName+"' found");
			return true;
		}
		testReporter.log(LogStatus.INFO, "Requested segment with name '"+segmentName+"' not found on first page");
		return false;
	}
	
	public boolean isMemberCountPresent() {
		if (this.memberCount.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isMemberPercentagePresent() {
		if (this.memberPercentage.isDisplayed()) {
			return true;
		}
		return false;
	}
	
	public boolean isNextPageNavigationButtonPresent() {
		if (this.nextPageNavigationButton.isDisplayed()) {
			return true;
		} else
			return false;
	}
	
	private void clickOnNextPageNavigationButton() {
		if (isNextPageNavigationButtonPresent()) {
			this.nextPageNavigationButton.click();
		} else System.out.println("Next page button is not present");
	}

	public DefineSegmentPage goToDefineSegmentPage() {
		if (isAddSegmentButtonPresent()) {
			this.newSegmentButton.click();
		} else {
			System.out.println("Error: Define Segment page link not present");
		}
		return new DefineSegmentPage(this.driver);
	}
	
	public HomePage goToHomePage() {
		if (this.isHomeButtonPresent()) {
			this.homePageLink.click();
		} else {
			System.out.println("Error: Home page link not present");
		}
		return new HomePage(this.driver);
	}
	
	public ImportSegmentPage goToImportSegmentPage() {
		if (isImportSegmentButtonPresent()) {
			this.importSegmentButton.click();
		} else {
			System.out.println("Error: Import Segment page link not present");
		}
		return new ImportSegmentPage(this.driver);
	}
	
	public SegmentMemberListPage goToSegmentMemberListPage(String segmentName) {
		do {
			flag = this.clickOnSegmentByName(segmentName);
			if(flag == false)
				this.clickOnNextPageNavigationButton();
		}while(flag == false);
		testReporter.log(LogStatus.INFO, "Navigated to segment member list page '"+segmentName+"'");
		return new SegmentMemberListPage(this.driver);
	}
	
	public AudienceSegmentsPage clickOnShowSegmentPercentageBySegmentName(String segmentName) throws InterruptedException {
		String id=getSegmentIDBySegmentName(segmentName);
		this.driver.findElement(By.id(id)).click();
		try {
			for (int i=0; i<=12; i++) {
				if(!this.segmentLoader.isDisplayed()) {
					break;
				}
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			
		}
		return new AudienceSegmentsPage(this.driver);
	}
	
	public int getSegmentedMemberCount() {
			return Integer.parseInt(this.memberCount.getText().trim().replaceAll(",", ""));
	}
	
	public Double getSegmentPercentage() {
		return Double.valueOf(this.segmentPercentageCount.getText().trim().replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("%", ""));
	}
	
	private String getSegmentIDBySegmentName(String segmentName) {
		String segmentXpath = "//*[@id='segments']//a[contains(text(),'"+segmentName+"')]";
		if(this.driver.findElement(By.xpath(segmentXpath)).isDisplayed()) {
			String attribute = this.driver.findElement(By.xpath(segmentXpath)).getAttribute("href");
			attribute = attribute.substring(52, 56);
			return attribute;
		}
		else {
			for (WebElement element : allSegments) {
				if (element.getText().equals(segmentName)) {
					String attribute = element.getAttribute("href");
					return attribute.substring(52, 56);
				}
			}
		}
		return "";
	}

}
