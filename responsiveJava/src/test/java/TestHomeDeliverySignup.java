package test.java;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class TestHomeDeliverySignup {

	public  String SELENIUM_HUB_URL;
	public  String TARGET_SERVER_URL;
	public  WebDriver driver;
	public WebDriverWait wait; 
	public boolean device; 
	

	@Parameters({ "targetEnvironment" })
	@BeforeTest
	public void beforeTest(String targetEnvironment) throws UnsupportedEncodingException, MalformedURLException {
		
		DesiredCapabilities capabilities = new DesiredCapabilities();

		switch (targetEnvironment) {
		case "Galaxy S5":
			device = true;
			capabilities.setCapability("platformName", "Android");
			//capabilities.setCapability("description", "Patrick");
			capabilities.setCapability("browserName", "mobileChrome");
			break;

		case "iPhone 6":
			device = true;
			capabilities.setCapability("platformName", "iOS");
			//capabilities.setCapability("description", "Patrick");
			capabilities.setCapability("browserName", "mobileSafari");
			break;

		case "Internet Explorer 11":
			device = false;
			DesiredCapabilities.internetExplorer();
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "internet explorer");
			capabilities.setCapability("version", "11");
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true); 
			break;

		case "Internet Explorer 10":
			device = false;
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "internet explorer");
			capabilities.setCapability("version", "10");
			break;	

		case "Firefox 34":
			device = false;
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "firefox");
			capabilities.setCapability("version", "34.0");
			break;

		case "Firefox 35":
			device = false;
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "firefox");
			capabilities.setCapability("version", "35.0");
			break;

		case "Chrome":
			device = false;
			capabilities.setCapability("platform", Platform.ANY);
			capabilities.setCapability("browserName", "chrome");
			capabilities.setCapability("version", "");
			break;
		}
		
		
		TARGET_SERVER_URL = getConfigurationProperty("TARGET_SERVER_URL",
				"test.target.server.url", "http://homedelivery.bostonglobe.com/");
		/*
		String user = System.getProperty("PerfectoUsername");
		String password = System.getProperty("PerfectoPassword");
		String host = System.getProperty("PerfectoCloud");
		*/
		String user = "davidd@perfectomobile.com";
		String password = "Demo1234";
		String host = "partners.perfectomobile.com";
		
		if (device) {

			System.out.println(targetEnvironment + ": device");
			
			user = URLEncoder.encode(user,"UTF-8");
			password = URLEncoder.encode(password, "UTF-8");
			URL gridURL = new URL("https://" + user + ':' + password + '@'
					+ host + "/nexperience/wd/hub");

			SELENIUM_HUB_URL = getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url", gridURL.toString());

		} else {
			System.out.println(targetEnvironment + ": desktop");;
			SELENIUM_HUB_URL = getConfigurationProperty("SELENIUM_HUB_URL",
					"test.selenium.hub.url",
					"http://seleniumgrid.perfectomobilelab.net:4444/wd/hub"); 
			
		}
		
		
		driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), capabilities);

		//  test starts in Codes entity list page
		driver.get(TARGET_SERVER_URL);
		System.out.println(SELENIUM_HUB_URL + " " + capabilities.getPlatform());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

		//driver.findElement(By.xpath("//a[text()='Home Delivery']")).click();
		wait = new WebDriverWait(driver, 20);
	}

    @Attachment
    public byte[] takeScreenshot() {
        System.out.println("Taking screenshot");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
	
	
	@Test
	public void openHomepage() {
		System.out.println("### Opening homepage ###");
		driver.get("http://homedelivery.bostonglobe.com/");
	
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='txtZip']")));
		takeScreenshot();	
		
	}	
	
	@Test(dependsOnMethods = { "openHomepage" })
	public void enterZipCode() {
		System.out.println("### Entering zipcode ###");
		driver.findElement(By.xpath("//input[@name='txtZip']")).sendKeys("02116");

		driver.findElement(By.xpath("//div[@name='cmdSubmit']")).click();
		takeScreenshot();
	}
	
	@Test (dependsOnMethods = { "enterZipCode" })
	public void selectLength () {
		System.out.println("### Selecting subscription length ###");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@for='rdSubscription1']")));

		driver.findElement(By.xpath("//label[@for='rdSubscription1']")).click();

		driver.findElement(By.xpath("//div[@id='continue_btn']")).click();
		takeScreenshot();
	}
	
	@Test(dependsOnMethods = { "selectLength" })
	public void enterDetails() {
		System.out.println("### Entering subscription details ###");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='txtDeliveryFirstName']")));
		driver.findElement(By.xpath("//input[@id='txtDeliveryFirstName']")).sendKeys("Patrick");
		driver.findElement(By.xpath("//input[@id='txtDeliveryLastName']")).sendKeys("McCartney");
		driver.findElement(By.xpath("//input[@id='txtDeliveryAddress1']")).sendKeys("28 Main St");
		driver.findElement(By.xpath("//input[@id='txtDeliveryAddress2']")).sendKeys("Apt. 2");
		driver.findElement(By.xpath("//input[@id='txtDeliveryAreaCode']")).sendKeys("781");
		driver.findElement(By.xpath("//input[@id='txtDeliveryPhone3']")).sendKeys("847");
		driver.findElement(By.xpath("//input[@id='txtDeliveryPhone4']")).sendKeys("4433");		
		driver.findElement(By.xpath("//input[@id='txtDeliveryEMail']")).sendKeys("patrickm@perfectomobile.com");			
		takeScreenshot();
	}
	
	@Test(dependsOnMethods = { "enterDetails" })
	public void testTearDown() throws Exception {
		if (driver != null) {
			driver.close();
			
			if (device) { downloadReport("html"); }
			
			driver.quit();	
		}
	}

	@Attachment
	private byte[] downloadReport(String type) throws IOException
	{	
		String command = "mobile:report:download";
		Map<String, String> params = new HashMap<>();
		params.put("type", type);
		String report = (String)((RemoteWebDriver) driver).executeScript(command, params);
		byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
		return reportBytes;
	}
	
	private static String getConfigurationProperty(String envKey,
			String sysKey, String defValue) {
		String retValue = defValue;
		String envValue = System.getenv(envKey);
		String sysValue = System.getProperty(sysKey);
		// system property prevails over environment variable
		if (sysValue != null) {
			retValue = sysValue;
		} else if (envValue != null) {
			retValue = envValue;
		}
		return retValue;
	}
	
	@AfterTest
	public void closeWebDriver () throws SessionNotFoundException {
		// make sure web driver is closed
		try{
			if ( ((RemoteWebDriver) driver).getSessionId() != null) {
				driver.close();
				driver.quit();
			}	
		}
		catch (SessionNotFoundException e) {}
			
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	

	@BeforeMethod
	public void setUp() throws Exception {
	}

	@AfterMethod
	public void tearDown() throws Exception {
	}

}
