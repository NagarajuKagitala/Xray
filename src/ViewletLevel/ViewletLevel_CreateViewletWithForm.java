package ViewletLevel;

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

import Common.CommonForAll;
import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ViewletLevel_CreateViewletWithForm 
{
	static WebDriver driver;
	static String Screenshotpath;
	CommonForAll obj=new CommonForAll();
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
	}
	
	//Login page
	@Test
	@Parameters({"sDriverPath", "sDriver", "DashboardName"})
	public void Login(String sDriverPath, String sDriver, String DashboardName) throws Exception {
		
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
			Thread.sleep(10000);
		}
		
		obj.CreateDashboard(driver, DashboardName);
		
	}
	
	@Parameters({"ViewletName"})
	@TestRail(testCaseId=454)
	@Test(priority=1)
	public void CreateViewletWithAForm(String ViewletName, ITestContext context) throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.xpath("//div[@id='app-top-sidebar']/div[3]")).click();
				
		//Check the Create viewlet with JKQL check box
		boolean Form=driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[2]/label/input")).isSelected();
		
		if(Form == true)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[2]/label/input")).click();
			Thread.sleep(1000);
		}
		
		driver.findElement(By.id("createViewletBtn")).click();
		Thread.sleep(5000);
		
		//Give the viewlet name
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).sendKeys(ViewletName);
		
		/*String ViewletName=driver.findElement(By.cssSelector(".row > .input-field:nth-child(1)")).getAttribute("value");
		System.out.println("Get viewlet name is: " +ViewletName);*/
		
		//select some graph
		driver.findElement(By.cssSelector("label:nth-child(1) > .color-icon > svg")).click();
		Thread.sleep(1000);
		
		//Click on Create viewlet button
		CommonElementsofViewlet.CreateButton(driver);
		Thread.sleep(4000);
		
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		StringBuilder buffer=new StringBuilder();
		for (WebElement e : myElements) 
		{
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//List<WebElement> ee = e.findElements(By.className("viewlet-title-name-div"));
				List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
				System.out.println("Viewlet size:" + ee.size());
				String GetViewletName="";
				for(WebElement innerele: ee)
				{
					GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
					//System.out.println("Viewlet name:" + GetViewletName);
					buffer.append(GetViewletName);
					buffer.append(',');
				}

			}
		}
		
		String ListOfViewletnames=buffer.toString();
		System.out.println("List of viewlets are: " +ListOfViewletnames);
		
		if(ListOfViewletnames.contains(ViewletName))
		{
			System.out.println("Viewlet is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Viewlet Creation failed from menu")).click();
		}
		Thread.sleep(8000);		
	}
	
	@TestRail(testCaseId=444)
	@Test(priority=2)
	public void ButtonsAndStatus(ITestContext context) throws InterruptedException
	{
//		//Click on Menu and select Edit viewlet option
//		driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[9]/i")).click();
//		Thread.sleep(3000);  
//		driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
//		Thread.sleep(3000);
		try {
		int i=0;

		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {
			i++;
			System.out.println("i: "+ i);
			// boolean str= e.getAttribute("aria-hidden"); 
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {

				String viewleteditxpath = "//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i";
				int tabindex = i;
				// Click on Viewlet Menu icon
				// Thread.sleep(4000);
				driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
				Thread.sleep(2000);
				// Select Edit option
				driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
				Thread.sleep(4000);
		
		//Close button                 
				Boolean Close=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[11]/button")).isEnabled();
				
				if(Close)
				{
					System.out.println("Close button is Enabled");
				}
				else
				{
					System.out.println("Close button is not Enabled");
					driver.findElement(By.id("Close button disable")).click();
				}
				Thread.sleep(1000);
				
				//Create button
				Boolean Create=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[11]/div/button")).isEnabled();
				
				if(Create)
				{
					System.out.println("Create button is Enabled");
				}
				else
				{
					System.out.println("Create button is not Enabled");
					driver.findElement(By.id("Create button disable")).click();
				}
				Thread.sleep(1000);
				
				//Apply button
				Boolean Apply=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[11]/div/div/div/button")).isEnabled();
				
				if(Apply)
				{
					System.out.println("Apply button is Enabled");
				}
				else
				{
					System.out.println("Apply button is not Enabled");
					driver.findElement(By.id("Apply button disable")).click();
				}
				
				Thread.sleep(4000);
			}
		}
		}
			catch(Exception e)
			{
				//e.printStackTrace();
			}
		
	}
	
	@TestRail(testCaseId=445)
	@Test(priority=3)
	public void RightSidePannel(ITestContext context) throws InterruptedException
	{
		
		//Viewlet Name field
				Boolean Viewlet=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).isEnabled();
				
				if(Viewlet)
				{
					System.out.println("Viewlet Name field is Enabled");
				}
				else
				{
					System.out.println("ViewletName field is not Enabled");
					driver.findElement(By.id("Viewlet name field disable")).click();
				}
				Thread.sleep(1000);
				
				//Data type field
				Boolean DataType=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[2]/div/span/span/span/span")).isEnabled();
				
				if(DataType)
				{
					System.out.println("DataType field is Enabled");
				}
				else
				{
					System.out.println("DataType field is not Enabled");
					driver.findElement(By.id("Data type disable")).click();
				}
				Thread.sleep(1000);		
				
				//Time Period field
				Boolean TimePeriod=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[3]/div/div/div/div/div/span/span/span/span")).isEnabled();
				
				if(TimePeriod)
				{
					System.out.println("Time Period field is Enabled");
				}
				else
				{
					System.out.println("Time Period field is not Enabled");
					driver.findElement(By.id("Time period disable")).click();
				}
				Thread.sleep(1000);
				
				//Fields
				Boolean Fields=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[4]/h3/span")).isEnabled();
				
				if(Fields)
				{
					System.out.println("Fields is Enabled");
				}
				else
				{
					System.out.println("Fields is not Enabled");
					driver.findElement(By.id("Fields disable")).click();
				}
				Thread.sleep(1000);
				
				//Group By
				Boolean GroupBy=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/h3/span")).isEnabled();
				
				if(GroupBy)
				{
					System.out.println("Group By is Enabled");
				}
				else
				{
					System.out.println("Group By is not Enabled");
					driver.findElement(By.id("Group by disable")).click();
				}
				Thread.sleep(1000);
				
				//Filters
				Boolean Filters=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[6]/h3/span")).isEnabled();
				
				if(Filters)
				{
					System.out.println("Filters is Enabled");
				}
				else
				{
					System.out.println("Filters is not Enabled");
					driver.findElement(By.id("Filters disable")).click();
				}
				Thread.sleep(1000);
				
				//View let Type
				Boolean ViewletType=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[7]/h3/span")).isEnabled();
				
				if(ViewletType)
				{
					System.out.println("Viewlet Type is Enabled");
				}
				else
				{
					System.out.println("Viewlet Type is not Enabled");
				}
				Thread.sleep(1000);
				//click on close btn            
				driver.findElement(By.cssSelector(".bottom-buttons-wp > .alert-btn")).click();
				Thread.sleep(3000);
				}
	
	
	@Parameters({"NewViewletName"})
	@TestRail(testCaseId=446)
	@Test(priority=4)
	public void CreateViewletButton(String NewViewletName,ITestContext context) throws InterruptedException
	{
//		Thread.sleep(6000);
//		driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[2]/ul/li/div[2]/div[1]/div[2]/div[9]/i")).click();
//		Thread.sleep(3000);  
//		driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
//		Thread.sleep(3000);
		int i=0;

		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {
			i++;
			System.out.println("i: "+ i);
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {

		String 		viewleteditxpath="//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i";
		int		tabindex=i;
				// Click on Viewlet Menu icon
				// Thread.sleep(4000);//div[8]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[9]/i
				driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
				Thread.sleep(2000);
				// Select Edit option
				driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
				Thread.sleep(4000);
		//Enter the viewlet name
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).sendKeys(NewViewletName);
		Thread.sleep(2000);
		
		/*
		 * //Click on somewhere
		 * driver.findElement(By.xpath("//div[2]/h3/span")).click(); Thread.sleep(2000);
		 */
		
		//Click on Create button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(6000);                
		
		List<WebElement> myElements2 = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		StringBuilder buffer=new StringBuilder();
		for (WebElement e2 : myElements) 
		{
			// boolean str= e.getAttribute("aria-hidden");
			if (e2.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				List<WebElement> ee = e2.findElements(By.className("viewlet-name-wrapper"));
				System.out.println("Viewlet size:" + ee.size());
				String GetViewletName="";
				for(WebElement innerele: ee)
				{
					GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
					//System.out.println("Viewlet name:" + GetViewletName);
					buffer.append(GetViewletName);
					buffer.append(',');
				}

			}
		}
		
		String ListOfViewletnames=buffer.toString();
		System.out.println("List of viewlets are: " +ListOfViewletnames);
		
		if(ListOfViewletnames.contains(NewViewletName))
		{
			System.out.println("Viewlet is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Viewlet Creation failed from menu")).click();
		}
		Thread.sleep(2000);
		}
		}
		}
	
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	
	public void CreateDashboard() throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
				
		//Give the dashboard Name
		driver.findElement(By.cssSelector("#createDashboard .input-field")).sendKeys("Create viewlet with form");
				
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);
		
		//select two columns
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();
				
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
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



