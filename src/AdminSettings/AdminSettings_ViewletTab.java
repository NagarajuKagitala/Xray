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
public class AdminSettings_ViewletTab 
{
	static WebDriver driver;
	static String Screenshotpath;
	int Dashboardscount=0;
	
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
	}
	
	//Login page
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
			
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", false);
			driver = new FirefoxDriver(options);
			
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
		Thread.sleep(4000);
		
        //Check Landing page 
		if(driver.getPageSource().contains("Go to Dashboard"))
		{
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(15000);
		}
		else
		{
			System.out.println("Landing page is not present");
			Thread.sleep(6000);
		}
		
		//Click on Admin settings
		CommonElements.MenubarIcon(driver);
		CommonElements.AdminSettingsOption(driver);
		//driver.findElement(By.cssSelector(".icon")).click();
		//driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);
	}
	
	@Parameters({"DashboardName", "Query"})
	@TestRail(testCaseId=397)
	@Test(priority=1)
	public void DataPointsforViewlet(String DashboardName, String Query,ITestContext context) throws InterruptedException
	{
		//click on Viewlet Tab
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li[5]")).click();
		Thread.sleep(1000);
		
		//Get the Data points max value
		String Maxvalue=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div/input")).getAttribute("value");
		int MaximumInputRecordsPerPage=Integer.parseInt(Maxvalue);
		System.out.println("Maximun input records are: " +MaximumInputRecordsPerPage);
		
		//close the Popup 
		CommonElements.AdminCancelButton(driver);
		//driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
		Thread.sleep(1000);
		
		this.Createdashboard(DashboardName);
		
		WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		
		//--- Create a viewlet --------
		
		CreateViewlet(Query);
		
		System.out.println("Dashboard no is: " +Dashboardscount);
		//Click on Events count                          
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div[2]/div/div/div[3]/div[3]/div/table/tbody/tr[2]/td[2]")).click();
		
		//Get the total events into string    
		String Records=driver.findElement(By.xpath("//div/div[2]/div[2]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
		System.out.println("Records are: " +Records);
		Thread.sleep(4000);
		try {
			System.out.println("Split records");
		String[] part = Records.split(" of ");
		
		int TotalRecords=0;
		if(part[1].contains(","))
		{
			TotalRecords=Integer.parseInt(part[1].replace(",", ""));
		}
		else
		{
			TotalRecords=Integer.parseInt(part[1]);
		}
		
		System.out.println("Maximum records are: " +TotalRecords);
		
		System.out.println(part[0]);
		String[] RecodsforPage=part[0].split(" - ");
				
		//System.out.println(part[1]);
		int MaxRecordsPerPage = Integer.parseInt(RecodsforPage[1]);
		
		System.out.println("Final value:"+MaxRecordsPerPage);
		
		if(MaximumInputRecordsPerPage > TotalRecords)
		{
			System.out.println("Total records are below input value");
			if(TotalRecords == MaxRecordsPerPage)
			{
				System.out.println("Data pointers for viewlet is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Data pointers for viewlet is not working fine");
				//Close the Events popup 
				driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
				Thread.sleep(2000);
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.id("Data pointers for viewlet failed")).click();
				
			}
		}
		else if(MaximumInputRecordsPerPage == TotalRecords)
		{
			System.out.println("Total records and input records are same");
			if(TotalRecords == MaxRecordsPerPage)
			{
				System.out.println("Data pointers for viewlet is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Data pointers for viewlet is not working fine");
				//Close the Events popup
				driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
				Thread.sleep(2000);
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.id("Data pointers for viewlet failed")).click();
			}
			
		}
		
		else
		{
			System.out.println("Total records are more than input records");
			if(MaximumInputRecordsPerPage == MaxRecordsPerPage)
			{
				System.out.println("Data pointers for viewlet is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Data pointers for viewlet is not working fine");
				//Close the Events popup
				driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
				Thread.sleep(2000);
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.id("Data pointers for viewlet failed")).click();
			}
			
		}
		Thread.sleep(1000);
		}
		catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Data pointers for viewlet failed")).click();
		}
		
		//Close the Events popup
		driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
		Thread.sleep(2000);
		
	}
	
	@Parameters({"SummaryQuery"})
	@TestRail(testCaseId=398)
	@Test(priority=2)
	public void DataGroupsforSummaryViewlet(String SummaryQuery,ITestContext context) throws InterruptedException
	{

		//Click on Admin settings
		CommonElements.MenubarIcon(driver);
		CommonElements.AdminSettingsOption(driver);
		//driver.findElement(By.cssSelector(".icon")).click();
		//driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(3000);
		
		//click on Viewlet Tab
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Get the Data points max value
		String Maxvalue=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div[2]/input")).getAttribute("value");
		System.out.println(Maxvalue);
		
		int SummaryDataMax = Integer.parseInt(Maxvalue);			
		System.out.println(SummaryDataMax);
		
		//close the Popup 
		CommonElements.AdminCancelButton(driver);
		//driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
		Thread.sleep(2000);
		
		//Click on summary Viewlet +icon  
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[4]/div[2]/button")).click(); 
		Thread.sleep(1000);
		
		driver.findElement(By.cssSelector("#create-summary-viewlet #createViewletBtn")).click();
		 
		//Enter query                    
		driver.findElement(By.cssSelector("#create-jkql-viewlet-summary .query-input")).sendKeys(SummaryQuery);
		
		//click on Create button     
		driver.findElement(By.cssSelector("#create-jkql-viewlet-summary .primary-btn")).click();
		Thread.sleep(2000);
		
		WebElement ele=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).findElement(By.tagName("ul"));
		List<WebElement> divs=ele.findElements(By.tagName("li"));
		System.out.println("Final size is: " +divs.size());
		
		for(WebElement ti : divs)
		{
			List<WebElement> header=ti.findElements(By.tagName("div"));
			for(WebElement test:header)
			{
				if(test.getAttribute("class").contains("viewlet-body"))
				{
					List<WebElement> test1=test.findElements(By.tagName("div"));
					
					for(WebElement test2 : test1)
					{
						if(test2.getAttribute("class").contains("viewlet-content-wrapper"))
						{
							WebElement test3=test2.findElement(By.tagName("div")).findElement(By.tagName("ul"));
							//System.out.println("Classes list is: " +test3.getAttribute("class"));
			
				List<WebElement> Final=test3.findElements(By.tagName("li"));
				System.out.println("Final size: " +Final.size());
				
				int i=0;
				int fin=0;
				for(WebElement el : Final)
				{
					//System.out.println("Classes are: " +el.getAttribute("class"));
					if(el.getAttribute("class").equalsIgnoreCase("summary-block"))
					{
						i++;
						fin=i;
						System.out.println("Size of summary blocks: " +i);
					}
				}
				System.out.println("Final count is:" +fin);
				if(fin <= SummaryDataMax) 
				{
					 System.out.println("Data groups for summary viewlet is working fine");
					 context.setAttribute("Status",1); context.setAttribute("Comment", "working fine");
				} 
				else 
				{
					  System.out.println("Data groups for summary viewlet is not working");
					 context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
					 driver.findElement(By.id("Data groups for summary failed")).click(); 
				 }
				}
				}
					
			}
				
			}
		}
	}
	
	@Test(priority=3)
	@TestRail(testCaseId=399)
	public void Reset(ITestContext context) throws InterruptedException
	{

		//Click on Admin settings
		CommonElements.MenubarIcon(driver);
		CommonElements.AdminSettingsOption(driver);
		//driver.findElement(By.cssSelector(".icon")).click();
		//driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);
		
		//click on Viewlet Tab
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li[5]")).click();
		Thread.sleep(1000);
		
		//Get the Data points max value
		String MaxDataPointsvalue=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div/input")).getAttribute("value");
		//System.out.println("Max points 100 values are: " +MaxDataPointsvalue);
		
		//Get the Data points max value(Maximum Data Groups in Summary Viewlet) 
		String MaxDataGroupsvalue=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div[2]/input")).getAttribute("value");
		//System.out.println("Max Groups 10 values are: " +MaxDataGroupsvalue);
		
		//Enter the data points value
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div/input")).sendKeys("1000");
		
		//Enter the data set value
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div[2]/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div[2]/input")).sendKeys("60");
		
		//Click on Reset button
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/button")).click();
		Thread.sleep(1000);
		
		//Get the Data points max value
		String MaxDataPointsvalueAfter=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div/input")).getAttribute("value");
		//System.out.println("Max Points 100 values are: " +MaxDataPointsvalueAfter);
		
		//Get the Data points max value
		String MaxDataGroupsvalueAfter=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[3]/span/div[2]/input")).getAttribute("value");
		//System.out.println("Max groups 10 values are: " +MaxDataGroupsvalueAfter);
		Thread.sleep(2000);
		
		//Verification
		
		if(MaxDataPointsvalue.equalsIgnoreCase(MaxDataPointsvalueAfter) && MaxDataGroupsvalue.equalsIgnoreCase(MaxDataGroupsvalueAfter))
		{
			System.out.println("Reset button is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Reset button is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			CommonElements.AdminCancelButton(driver);
			driver.findElement(By.id("Reset button failed")).click();
		}
		
		//close the Popup 
		CommonElements.AdminCancelButton(driver);
		//driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click(); 
		Thread.sleep(1000);	
	}
	
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	public void Createdashboard(String DashboardName) throws InterruptedException
	{
		// ---  Create dashboard ------------
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
		
		//Give the dashboard Name
		driver.findElement(By.cssSelector(".edit-name")).sendKeys(DashboardName);
				
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//label[2]/input")).click();
		Thread.sleep(1000);
		
		//select two columns
		driver.findElement(By.xpath("//main/div/ul/li[2]/div")).click();
				
		//Click on Create button   
		driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
		
	}
	
	public void CreateViewlet(String Query) throws InterruptedException
	{	
		driver.findElement(By.xpath("//div[@id='app-top-sidebar']/div[3]")).click();
		//Click on Create viewlet  
		driver.findElement(By.id("createViewletBtn")).click();
		
		//Enter the Viewlet query  
		driver.findElement(By.xpath("//main/div/jkql-input/textarea")).sendKeys(Query);
		
		//Click on Create button     
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet']/section/footer/button[2]")).click();
		Thread.sleep(6000);
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
