package MenuBar;

import java.io.File;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class MenuBar_About 
{
	static WebDriver driver;
	static String Screenshotpath;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
	}
	
	//Login page
	@Test
	@Parameters({"sDriverPath", "sDriver" })
	public void Login(String sDriverPath, String sDriver) throws Exception {
		
		Settings.read();
		String sURL = Settings.getsURL();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverPath);
			driver= new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.gecko.driver"))
		{
			System.setProperty(sDriver, sDriverPath);
			
			//FirefoxOptions options = new FirefoxOptions();
		//	options.setCapability("marionette", false);
			driver = new FirefoxDriver();
			
		}
		else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		{
		  System.setProperty(sDriver, sDriverPath);
		  driver= new InternetExplorerDriver();
		}
		else
		{
		  System.setProperty(sDriver, sDriverPath);
		  driver= new EdgeDriver();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		//Login Credentials
		driver.get(sURL);
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(12000);
		
        //Check Landing page 
		if(driver.getPageSource().contains("Go to Dashboard"))
		{
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(15000);
		}
		else
		{
			System.out.println("Landing page is not present");
			Thread.sleep(15000);
		}
		Thread.sleep(3000);
	}
	
	@TestRail(testCaseId=509)
	@Test(priority=1)
	public void AboutVerification(ITestContext context) throws InterruptedException
	{
		//Click on Admin settings
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//div[@id='main-menu-icon']/div")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//li[8]")).click();
		Thread.sleep(5000);
		
		//Store the Contact us page
		String AboutLinkTitle=driver.findElement(By.xpath("//dialog[@id='version-info-popup']/section/header")).getText();
		System.out.println(AboutLinkTitle);
		
		if(AboutLinkTitle.equalsIgnoreCase("About"))
		{
			System.out.println("About page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("About page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//dialog[@id='version-info-popup']/button/i")).click();
			driver.findElement(By.id("About page")).click();
		}
		driver.findElement(By.xpath("//dialog[@id='version-info-popup']/button/i")).click();
		Thread.sleep(1000);
	}
	
	@Parameters({"Collectorsurl"})
	@TestRail(testCaseId=415)
	@Test(priority=2)
	public void GetCollectorsFromSettings(String Collectorsurl, ITestContext context) throws InterruptedException
	{

		//Click on Admin settings
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//div[@id='main-menu-icon']/div")).click();
		driver.findElement(By.xpath("//li[8]")).click();
		Thread.sleep(6000);
		
		//Click on Get Collector
		driver.findElement(By.linkText("Get Collectors")).click();
		Thread.sleep(15000);
		 
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//swich to another tab
		driver.switchTo().window(handle[1]);
		
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the page:" +CurrentUrl);
		
		/*
		 * //Store the license page title into string String
		 * GetCollectorspage=driver.findElement(By.cssSelector("h2:nth-child(1)")).
		 * getText(); System.out.println(GetCollectorspage);
		 */
		
		if(Collectorsurl.equalsIgnoreCase(CurrentUrl))
		{
			System.out.println("Get collectors page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Listener viewlet is created successfully");
		}
		else
		{
			System.out.println("Get collectors page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed to create Listener viewlet");
			driver.close();
			
			//Back to jkool
			driver.switchTo().window(handle[0]);
			Thread.sleep(1000);
			
			//Close the settings page
			driver.findElement(By.xpath("//dialog[@id='version-info-popup']/button/i")).click();
			Thread.sleep(2000);
			
			driver.findElement(By.xpath("Not created")).click();
		}
		
		driver.close();
		
		//Back to jkool
		driver.switchTo().window(handle[0]);
		Thread.sleep(1000);
		
		//Close the settings page
		driver.findElement(By.xpath("//dialog[@id='version-info-popup']/button/i")).click();
		Thread.sleep(2000);
	}
	
	@Parameters({"Licensesurl"})
	@TestRail(testCaseId=416)
	@Test(priority=3)
	public void LicenseFromSettings(String Licensesurl,ITestContext context) throws InterruptedException
	{

		//Click on Admin settings
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//div[@id='main-menu-icon']/div")).click();
		driver.findElement(By.xpath("//li[8]")).click();
		Thread.sleep(5000);
		
		//Click on License
		driver.findElement(By.linkText("License")).click();
		Thread.sleep(15000);
		 
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//swich to another tab
		driver.switchTo().window(handle[1]);
		
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the page:" +CurrentUrl);
		
		
		/*
		 * //Store the license page title into string String
		 * LicensepageTitle=driver.findElement(By.xpath("//h1")).getText();
		 * System.out.println(LicensepageTitle);
		 */
		
		if(Licensesurl.equalsIgnoreCase(CurrentUrl))
		{
		
			System.out.println("License page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("License page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.close();
			
			//Back to jkool
			driver.switchTo().window(handle[0]);
			Thread.sleep(1000);
			
			//Close the settings page
			driver.findElement(By.xpath("//dialog[@id='version-info-popup']/button/i")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("License failed")).click();
			
			
		}
		
		driver.close();
		
		//Back to jkool
		driver.switchTo().window(handle[0]);
		Thread.sleep(1000);
		
		//Close the settings page
		driver.findElement(By.xpath("//dialog[@id='version-info-popup']/button/i")).click();
		Thread.sleep(1000);
	}
	
	
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		//System.out.println("dir: " + dir);
		if (!result.getMethod().getMethodName().contains("Logout")) {
			if (ITestResult.FAILURE == result.getStatus()) {
				this.capturescreen(driver, result.getMethod().getMethodName(), "FAILURE");
				Reporter.setCurrentTestResult(result);

				Reporter.log("<br/>Failed to execute method: " + result.getMethod().getMethodName() + "<br/>");
				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsFailure/"
						+ result.getMethod().getMethodName() + ".png";

			} else {
				this.capturescreen(driver, result.getMethod().getMethodName(), "SUCCESS");
				Reporter.setCurrentTestResult(result);

				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsSuccess/"
						+ result.getMethod().getMethodName() + ".png";

			}

			String path = "<img src=\" " + screenshotPath + "\" alt=\"\"\"/\" />";
			// To add it in the report
			Reporter.log("<br/>");
			Reporter.log(path);
			
			try {
				//Update attachment to testrail server
				int testCaseID=0;
				//int status=(int) result.getTestContext().getAttribute("Status");
				//String comment=(String) result.getTestContext().getAttribute("Comment");
				  if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class))
					{
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();
					
					
					
					TestRailAPI api=new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());
					
					}
				}catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
				}
		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {
			
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (status.equals("FAILURE")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				Reporter.log(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png");
			} else if (status.equals("SUCCESS")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "./ScreenshotsSuccess/" + screenShotName + ".png"));

			}

			System.out.println("Printing screen shot taken for className " + screenShotName);

		} catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
		}
	}

}
