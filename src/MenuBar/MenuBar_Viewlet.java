package MenuBar;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
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

import Common.CommonForAll;
import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class MenuBar_Viewlet 
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
	@Parameters({"sDriverPath", "sDriver", "DashboardName" })
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
		
		obj.CreateDashboard(driver, DashboardName);
	}
	
	@TestRail(testCaseId=521)
	@Parameters({"Query", "ViewletName", "DashboardName"})
	@Test(priority=1)
	public static void CreateViewletWithQueryFromMenubar(String Query, String ViewletName, String DashboardName, ITestContext context) throws InterruptedException
	{
		//Mouse hour 
		Thread.sleep(5000);
		CommonElementsofMenu.MenubarIcon(driver);
		
		//Click on viewlet and select Create 
		CommonElementsofMenu.Viewlet(driver);
		Thread.sleep(2000);
		CommonElementsofMenu.CreateViewlet(driver);
		Thread.sleep(1000);
		
		//Check the Create viewlet with JKQL check box 
		boolean Jkql=driver.findElement(By.name("create-viewlet-open-close-option")).isSelected();
		
		if(Jkql == true)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.name("create-viewlet-open-close-option")).click();
			Thread.sleep(1000);
		}
		Thread.holdsLock(3000);
		
		//Click on Create viewlet  
		driver.findElement(By.id("createViewletBtn")).click();
		
		//Enter the Viewlet query
		driver.findElement(By.xpath("//main/div/jkql-input/textarea")).clear();
		driver.findElement(By.xpath("//main/div/jkql-input/textarea")).sendKeys(Query);
		Thread.sleep(2000);
		
		//Give viewlet name
		driver.findElement(By.cssSelector("#create-jkql-viewlet .viewlet-name")).clear();
		driver.findElement(By.cssSelector("#create-jkql-viewlet .viewlet-name")).sendKeys(ViewletName);
		
		//Click on Create button     
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet']/section/footer/button[2]")).click();
		Thread.sleep(6000);
		
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		
		StringBuilder buffer=new StringBuilder();
		for (WebElement e : myElements) {

		// boolean str= e.getAttribute("aria-hidden");
		if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
		List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
		System.out.println("Viewlet size:" + ee.size());
		String GetViewletName="";
		for(WebElement innerele: ee)
		{
			GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
			//System.out.println("Viewlet name:" + GetViewletName);
			buffer.append(GetViewletName);
			buffer.append(",");
		}
		
		String Viewletnames=buffer.toString();
		System.out.println("List of viewlets are: " +Viewletnames);

		// verification
		if (Viewletnames.contains(ViewletName)) {
		System.out.println("Viewlet is created successfully");
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "working fine");
		} else {
		System.out.println("Viewlet is not created");
		context.setAttribute("Status", 5);
		context.setAttribute("Comment", "Failed");
		driver.findElement(By.id("Viewlet Creation failed")).click();
		}
		Thread.sleep(2000);
		}
		}
	}
	
	@TestRail(testCaseId=522)
	@Test(priority=2)
	public void CreateViewletWithAFormFromMenubar(ITestContext context) throws InterruptedException
	{
		//Mouse hour
		Thread.sleep(2000);
		CommonElementsofMenu.MenubarIcon(driver);
		
		//Click on viewlet and select Create 
		//CommonElementsofMenu.Viewlet(driver);
		Thread.sleep(2000);
		CommonElementsofMenu.CreateViewlet(driver);
		Thread.sleep(2000);
		
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
		Thread.holdsLock(3000);
		driver.findElement(By.id("createViewletBtn")).click();
		Thread.sleep(5000);
		
		//Give the viewlet name
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).sendKeys("ViewletForm");
		
		//Store the default viewlet name into string
		String DefaultViewletName=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).getAttribute("value");
		System.out.println("Get viewlet name is" +DefaultViewletName);
		
		//select some graph
		driver.findElement(By.cssSelector("label:nth-child(1) > .color-icon > svg")).click();
		Thread.sleep(1000);
		
		//Click on Create viewlet button
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[11]/div/button")).click();
		Thread.sleep(4000);	
		
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		
		StringBuilder buffer=new StringBuilder();
		for (WebElement e : myElements) {

		// boolean str= e.getAttribute("aria-hidden");
		if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
		List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
		System.out.println("Viewlet size:" + ee.size());
		String GetViewletName="";
		for(WebElement innerele: ee)
		{
			GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
			//System.out.println("Viewlet name:" + GetViewletName);
			buffer.append(GetViewletName);
			buffer.append(",");
		}
		
		String ViewletNames=buffer.toString();
		System.out.println("List of vielets are: " +ViewletNames);

		// verification
		if (ViewletNames.contains(DefaultViewletName)) {
		System.out.println("Viewlet is created successfully");
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "working fine");
		} else {
		System.out.println("Viewlet is not created");
		context.setAttribute("Status", 5);
		context.setAttribute("Comment", "Failed");
		driver.findElement(By.id("Viewlet Creation failed")).click();
		}
		Thread.sleep(2000);
		}
		}
		
	}
	
	@TestRail(testCaseId=523)
	@Parameters({"Viewlet", "DashboardName"})
	@Test(priority=3)
	public void OpenExistingViewletFromMenubar(String Viewlet, String DashboardName, ITestContext context) throws InterruptedException
	{
		//Mouse hour
		Thread.sleep(2000);
		CommonElementsofMenu.MenubarIcon(driver);
				
		//Click on viewlet and select Create 
		//CommonElementsofMenu.Viewlet(driver);
		Thread.sleep(2000);
		CommonElementsofMenu.CreateViewlet(driver);
		Thread.sleep(2000);
		
		//Check the Create viewlet with JKQL check box 
		boolean Existing=driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[3]/label/input")).isSelected();
		
		if(Existing == true)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[3]/label/input")).click();
			Thread.sleep(1000);
		}
		
		Thread.holdsLock(3000);
		
		//Click on Create button
		driver.findElement(By.id("createViewletBtn")).click();
		
		//Search with viewlet name 
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).sendKeys(Viewlet);
		Thread.sleep(4000);
		
		//Click on Viewlet
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul/li/img")).click();
		
		//Get the Chart name into string
		String chart=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul/li/label")).getText();
		System.out.println("Chart name is: " +chart);
		
		String ViewletName=chart+"-Copy";
		System.out.println("Existing viewlet name is:" +ViewletName);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/footer/div[2]/button")).click();
		Thread.sleep(8000);
		
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		StringBuilder buffer=new StringBuilder();
		for (WebElement e : myElements) 
		{
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
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
		Thread.sleep(2000);
		String Viewlets=buffer.toString();
		System.out.println("List of viewlets are: " +Viewlets);

		// verification
		if (Viewlets.contains(chart)) 
		{
			System.out.println("Viewlet is created successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
		else 
		{
			System.out.println("Viewlet is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			//Click on Cancel button
			driver.findElement(By.cssSelector("#open-widget .alert-btn")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("Viewlet Creation failed")).click();
		}
		
	
		
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=524)
	@Test(priority=4)
	public void VerifyOpenViewletOptionsFromMenubar(ITestContext context) throws InterruptedException
	{
		//Mouse hour
		Thread.sleep(3000);
		CommonElementsofMenu.MenubarIcon(driver);
						
		//Click on viewlet and select Create 
		//CommonElementsofMenu.Viewlet(driver);
		Thread.sleep(2000);
		CommonElementsofMenu.OpenOption(driver);
		Thread.sleep(2000);
		       
		
		//View By dropdown
		Boolean ViewBy=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div/select")).isEnabled();
		
		if(ViewBy)
		{
			System.out.println("ViewBy dropdown is Enabled");
		}
		else
		{
			System.out.println("ViewBy dropdown is not Enabled");
			driver.findElement(By.id("View by is Disable")).click();
		}
		Thread.sleep(1000);
		
		//Sort By dropdown
		Boolean SortBy=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[2]/select")).isEnabled();
		
		if(SortBy)
		{
			System.out.println("Sort By dropdown is Enabled");
		}
		else
		{
			System.out.println("Sort By dropdown is not Enabled");
			driver.findElement(By.id("Sort by is Disable")).click();
		}
		Thread.sleep(1000);
		
		//Search field
		Boolean SearchField=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).isEnabled();
		
		if(SearchField)
		{
			System.out.println("Search field is Enabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Search field is not Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Search field is Disable")).click();
		}
		Thread.sleep(3000);
	}
	
	@TestRail(testCaseId=525)
	@Parameters({"DashboardName", "ViewletFromOption"})
	@Test(priority=5)
	public void CreateviewletFromOpenOption(String DashboardName, String ViewletFromOption, ITestContext context) throws InterruptedException
	{
		//Search with viewlet name
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).sendKeys(ViewletFromOption);
		
		//Click on Viewlet
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul/li/img")).click();
		
		//Get the Chart name into string
		String chart=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul/li/label")).getText();
		System.out.println("Chart name is: " +chart);
				
		String ViewletName=chart+"-Copy";
		System.out.println("Existing viewlet name is:" +ViewletName);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/footer/div[2]/button")).click();
		Thread.sleep(6000);
		
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		StringBuilder buffer=new StringBuilder();
		for (WebElement e : myElements) 
		{
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
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
		Thread.sleep(2000);
		String Viewlets=buffer.toString();
		System.out.println("List of viewlets are: " +Viewlets);

		// verification
		if (Viewlets.contains(chart)) 
		{
			System.out.println("Viewlet is created successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
		else 
		{
			System.out.println("Viewlet is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			//Click on Cancel button
			driver.findElement(By.cssSelector("#open-widget .alert-btn")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("Viewlet Creation failed")).click();
		}
	}
	
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	public void CreateDashboard(String DashboardName) throws InterruptedException
	{
		// Click on Plus Icon
		driver.findElement(By.xpath("//div[@id='pageContainer-tabs-add']/div/div/span")).click();

		// Give the dashboard Name
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/input")).sendKeys("EditViewlet");

		// uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);

		// select two columns 
		//driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();

		// Click on Create button
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
