package AdminSettings;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
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
public class AdminSettings_Set {
	
	static WebDriver driver;
	static String Screenshotpath;
    By Menubar=By.cssSelector(".fa-bars");
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
	}
	
	@Test
	@Parameters({"sDriverPath", "sDriver" })
	public static void Login(String sDriverPath, String sDriver) throws Exception {
		
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
			
		//	FirefoxOptions options = new FirefoxOptions();
		//	options.setCapability("marionette", false);
			driver = new FirefoxDriver();
			
		}
		else
		{
		  System.setProperty(sDriver, sDriverPath);
		  driver= new InternetExplorerDriver();
		}
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(sURL);
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(15000);
		
		if(driver.getPageSource().contains("Go to Dashboard"))
		{
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(15000);
			//dashboardnotpresent(sRepositories);
			System.out.println("Go to Dashboard page is present");
		}
		else
		{
			System.out.println("Go to Dashboard page is not present");
			Thread.sleep(12000);
			//dashboardnotpresent(sRepositories);	
		}
	}
	
	
	
	@Test(priority=1)
	@Parameters ({"SetName", "Criteria"})
	@TestRail(testCaseId=363)
	public void CreateSet(String SetName,String Criteria, ITestContext context) throws Throwable {
		try
		{
		
		/*
		 * Actions action = new Actions(driver); WebElement element =
		 * driver.findElement(By.xpath("//div[@id='topMenu']/div"));
		 * action.moveToElement(element).perform();
		 */
		
		
		//Click on Admin settings
		CommonElements.MenubarIcon(driver);
		//driver.findElement(Menubar).click();
		CommonElements.AdminSettingsOption(driver);
		//driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);
				
		//click on sets btn
		driver.findElement(By.cssSelector(".adminStep:nth-child(2)")).click();
		
		//click on create btn
		driver.findElement(By.xpath("//input[@value='Create']")).click();
		
		//click on advanced btn
		driver.findElement(By.xpath("//button[contains(.,'Advanced')]")).click();
		
		//Give set name 
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/main/aside[2]/div/table/tbody/tr/td[2]/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/main/aside[2]/div/table/tbody/tr/td[2]/input")).sendKeys(SetName);
		Thread.sleep(3000);
		
		//Enter criteria
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/main/aside[2]/div/table/tbody/tr[3]/td[2]/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/main/aside[2]/div/table/tbody/tr[3]/td[2]/input")).sendKeys(Criteria);
		Thread.sleep(3000);
		
		//click on finish btn
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/footer/div[2]/button[3]")).click();
		Thread.sleep(8000);
		
		/*//Close the cancel button
		driver.findElement(By.xpath("//footer[2]/div/button")).click();	
		
		//Click on Admin settings
		driver.findElement(By.cssSelector(".icon")).click();
		driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);
						
		//click on sets btn
		driver.findElement(By.cssSelector(".adminStep:nth-child(2)")).click();
		Thread.sleep(3000);*/
		
		//verify Set click on edit btn
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[2]/div[2]/input[2]")).click();
		Thread.sleep(6000);
		
		//Search with set name
		driver.findElement(By.xpath("//dialog[@id='editSet']/section/main/input")).sendKeys(SetName);
		Thread.sleep(3000);
		
		//click on all string names
		String GetSetNames=driver.findElement(By.id("setTable")).getText();
		System.out.println(GetSetNames);
		
		//Clear search data
		driver.findElement(By.xpath("//dialog[@id='editSet']/section/main/input")).clear();
		
		if(GetSetNames.contains(SetName)) {
			System.out.println("Created set Verified");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		//click on cancel btn
		CommonElements.cancel(driver);
		//driver.findElement(By.cssSelector("#editSet .alert-btn")).click();
		Thread.sleep(3000);	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Test(priority=2)
	@Parameters ({"SetName","ObjectiveName","Objective"})
	@TestRail(testCaseId=364)
	public static void EditSet(String SetName, String ObjectiveName, String Objective,ITestContext context) throws Throwable {
		/*Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//div[@id='topMenu']/div"));
		action.moveToElement(element).perform();

		//Click on Admin settings
		driver.findElement(By.xpath("//li[contains(.,'Admin Settings')]")).click();
		Thread.sleep(2000);
		
		//click on sets btn
		driver.findElement(By.cssSelector(".adminStep:nth-child(2)")).click();
		//click on edit btn
		driver.findElement(By.xpath("//input[@value='Edit']")).click();
		Thread.sleep(1000);*/
		
				
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[2]/div[2]/input[2]")).click();
		Thread.sleep(2000);
		
		//click on search here
		driver.findElement(By.xpath("//dialog[@id='editSet']/section/main/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='editSet']/section/main/input")).sendKeys(SetName);
		Thread.sleep(2000);
		
		WebElement table=driver.findElement(By.id("setDataTable"));
		List<WebElement> lst_ele= table.findElements(By.tagName("tr"));
		
		for(WebElement web_ele:lst_ele)
		{
			String attr=web_ele.getAttribute("style");
			
			if(attr.isEmpty())
			{
				//System.out.println(web_ele.getAttribute("innerHTML"));
				web_ele.findElement(By.tagName("td")).click();
				
			}
			
		}
		//EDIT A SET		
		//click on edit btn
		driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
		//click on + 
		driver.findElement(By.xpath("//input[@value='+']")).click();
		Thread.sleep(4000);
		
		//enter objectivename
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/main/aside[2]/div/table[2]/tbody/tr[2]/td[2]/input")).sendKeys(ObjectiveName);
		//enter objective
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/main/aside[2]/div/table[2]/tbody/tr[2]/td[4]/input")).sendKeys(Objective);
		Thread.sleep(10000);
		
		//click on save and close btn
		driver.findElement(By.xpath("//dialog[@id='setWizard']/section/footer/div[2]/button[4]")).click();
		Thread.sleep(10000);
		
		//click on Edit button
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[2]/div[2]/input[2]")).click();
		Thread.sleep(2000);
		
		//search
		driver.findElement(By.xpath("//dialog[@id='editSet']/section/main/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='editSet']/section/main/input")).sendKeys(SetName);
		Thread.sleep(2000);
		
		//Verification
		WebElement tableAfterEdit=driver.findElement(By.id("setDataTable"));
		List<WebElement> lst_ele2= tableAfterEdit.findElements(By.tagName("tr"));
		
		for(WebElement web_ele:lst_ele2)
		{
			String attr=web_ele.getAttribute("style");
			
			if(attr.isEmpty())
			{
				System.out.println(web_ele.getText());
				if(web_ele.getText().contains(Objective))
				{
					System.out.println(SetName + " Edited ");
					context.setAttribute("Status",1);
					context.setAttribute("Comment", "working fine");
				}
				else
				{
					System.out.println(SetName + "not Edited ");
					context.setAttribute("Status",5);
					context.setAttribute("Comment", "Failed");
					CommonElements.cancel(driver);
					//driver.findElement(By.cssSelector("#editSet .alert-btn")).click();
					Thread.sleep(1000);
					//driver.findElement(By.xpath("//footer[2]/div/button")).click();
					driver.findElement(By.xpath("Not created")).click();
				}
			}
		}
		Thread.sleep(3000);
		CommonElements.cancel(driver);
		//driver.findElement(By.cssSelector("#editSet .alert-btn")).click();
		Thread.sleep(1000);
		//driver.findElement(By.xpath("//footer[2]/div/button")).click();
	}
	
	
	@Test(priority=3)
	@Parameters ({"SetName"})
	@TestRail(testCaseId=365)
	public static void DeleteSet(String SetName,ITestContext context) throws Throwable {
		/*Actions action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath("//div[@id='topMenu']/div"));
		action.moveToElement(element).perform();
		//Click on Admin settings
		driver.findElement(By.xpath("//li[contains(.,'Admin Settings')]")).click();
		Thread.sleep(2000);
		//click on sets btn
		driver.findElement(By.cssSelector(".adminStep:nth-child(2)")).click();*/
		/*
		 * //Click on Admin settings
		 * driver.findElement(By.cssSelector(".icon")).click();
		 * driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		 * Thread.sleep(2000);
		 * 
		 * //click on sets btn
		 * driver.findElement(By.cssSelector(".adminStep:nth-child(2)")).click();
		 */
		
		//click on delete btn
		driver.findElement(By.xpath("//input[@value='Delete']")).click();
		Thread.sleep(5000);
		
		//click on live search box here
		driver.findElement(By.cssSelector(".live-search-box")).clear();
		
		//Click on Admin settings
		driver.findElement(By.cssSelector(".live-search-box")).sendKeys(SetName);
		//driver.findElement(By.cssSelector("#deleteSets main")).click();
		Thread.sleep(2000);
		//driver.findElement(By.name("setCheckBox")).click();
		
		
		//WebElement table=driver.findElement(By.id("deleteSetsTable")).findElement(By.tagName("thead")).findElement(By.tagName("tbody"));
//		WebElement table=driver.findElement(By.id("setTable"));
//		List<WebElement> lst_ele= table.findElements(By.tagName("tr"));
//		
//		for(WebElement web_ele:lst_ele)
//		{
//			String attr=web_ele.getAttribute("style");
//			
//			if(attr.isEmpty())
//			{
//				//System.out.println(web_ele.getAttribute("innerHTML"));
//				web_ele.findElement(By.tagName("td")).click();
//				
//			}
//			
//		}
//		
//		table.click();
		driver.findElement(By.xpath("//tbody[@id='setDataTable']/tr/td/input")).click();
		Thread.sleep(5000);
		//click on delete btn
		driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(2000);
		
		/*//after select,click on delete btn
		driver.findElement(By.cssSelector("#deleteSets .primary-btn")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@value='Edit']")).click();*/
		
		String GetSetNames=driver.findElement(By.xpath("//div[@id='setTable']/div")).getText();
		System.out.println("list of sets are: " +GetSetNames);
		
		if(!GetSetNames.contains(SetName)) {
			System.out.println("Deleted set Verified");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			//CommonElements.cancel(driver);
			driver.findElement(By.cssSelector("#deleteSets .alert-btn")).click();
			Thread.sleep(5000);
			CommonElements.AdminCancelButton(driver);
			//driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer/div/button")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(3000);
		//CommonElements.cancel(driver);
		driver.findElement(By.cssSelector("#deleteSets .alert-btn")).click();
		Thread.sleep(2000); 
		CommonElements.AdminCancelButton(driver);
		//driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer/div/button")).click();	
	}
	
	@Test(priority=3)
	public static void Logout() throws Throwable
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
