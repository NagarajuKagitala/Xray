package MenuBar;

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
public class MenuBar_Dashboard 
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
			Thread.sleep(6000);
		}
	}
	
	@TestRail(testCaseId=510)
	@Parameters({"DashboardName"})
	@Test(priority=1)
	public void CreateDashboardFromMenubar(String DashboardName, ITestContext context) throws InterruptedException
	{
		try
		{
		//Mouse hour 
		CommonElementsofMenu.MenubarIcon(driver);
		Thread.sleep(2000);
		
		//Click on Dashboard and Select Save button
		CommonElementsofMenu.DashboardLink(driver);
		Thread.sleep(4000);
		
		driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[2]/ul/li/span")).click();
		Thread.sleep(2000);
		
		//Give the dashboard Name
		driver.findElement(By.cssSelector(".edit-name")).sendKeys(DashboardName);
		
		// uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//label[2]/input")).click();
		Thread.sleep(1000);

		// select two columns 
		driver.findElement(By.xpath("//main/div/ul/li[2]/div")).click();
				
							
		//Click on Create button
		driver.findElement(By.cssSelector("#create-dashboard .primary-btn")).click();
		Thread.sleep(4000);
		
		List<WebElement> list=driver.findElements(By.className("tabs-title"));
		System.out.println(list.size());
		
		for(WebElement e : list)
		{
			System.out.println(e.getText());
			if(e.getText().equalsIgnoreCase(DashboardName))
			{
				System.out.println("Dashboard is created successfully");
				break;
			}	
		}
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "working fine");
		}
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("failed")).click();
		}
	}
	
	@TestRail(testCaseId=511)
	@Test(priority=2)
	public void SaveDashboardFromMenu(ITestContext context) throws InterruptedException
	{
		//Mouse hour 
		Thread.sleep(2000);
		CommonElementsofMenu.MenubarIcon(driver);
		
		//Click on Dashboard and Select Save button
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[2]")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath("//li[2]/ul/li[3]")).click();
		Thread.sleep(3000);
		
		String Success=driver.findElement(By.cssSelector(".message-main")).getText();
		System.out.println(Success);
		Thread.sleep(2000);
		
		//Click on OK button
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		
		if(Success.contains("successfully"))
		{
			System.out.println("Dashoard is saved successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Dashoard is not saved");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Save option failed")).click();
		}
	}
	
	@TestRail(testCaseId=512)
	@Parameters({"SaveasDashboardName"})
	@Test(priority=3)
	public void SaveasDashboardFromMenu(String SaveasDashboardName, ITestContext context) throws InterruptedException
	{
		try
		{
		//Mouse hour 
		CommonElementsofMenu.MenubarIcon(driver);
		
		//Click on Dashboard and Select Save button
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[2]")).click();
		//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		driver.findElement(By.xpath("//span[contains(.,'Save As')]")).click();
		Thread.sleep(1000);
		
		//Enter the dashboard name
		driver.findElement(By.xpath("//dialog[@id='save-as-dashboard']/section/main/div/input")).sendKeys(SaveasDashboardName);
		
		//Click on OK button
		driver.findElement(By.id("saveAsDashboardBtn")).click();
		Thread.sleep(5000);		
		
		List<WebElement> list=driver.findElements(By.className("tabs-title"));
		System.out.println(list.size());
		
		for(WebElement e : list)
		{
			System.out.println(e.getText());
			if(e.getText().equalsIgnoreCase(SaveasDashboardName))
			{
				System.out.println("Dashboard is saved using save as option");
				break;
			}	
		}
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "working fine");
		}
		
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("failed")).click();
			
		}
		
		//Click on OK button
	//	driver.findElement(By.id("updatesDone")).click();
		
	}
	
	@TestRail(testCaseId=513)
	@Test(priority=4)
	public void ChangeDashboardLayoutFromMenubar(ITestContext context) throws InterruptedException
	{
		//Mouse hour 
		Thread.sleep(3000);
		CommonElementsofMenu.MenubarIcon(driver);
		
		//Click on Dashboard and Select Save button
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[2]")).click();
		//driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//span[contains(.,'Change Layout')]")).click();
		Thread.sleep(4000);
		
        WebElement ele=driver.findElement(By.className("colType"));
		
		List<WebElement> fin=ele.findElements(By.tagName("li"));
	    System.out.println(fin.size());
			int i=0;
		
		    for(WebElement ss : fin)
		    {
		    	//System.out.println("text is: " +ss.getText());
		    	//System.out.println("Inner html: " +ss.getAttribute("innerHTML"));
			try
			{
			List<WebElement> div=ss.findElements(By.tagName("div"));
			
			for(WebElement col: div)
			{
				 System.out.println("Existing selected column is: " +col.getAttribute("class"));
				if(col.getAttribute("class").equalsIgnoreCase("thumb selected"))
				{
					System.out.println("Div tag title is: " +col.getAttribute("title"));
					if(col.getAttribute("title").equalsIgnoreCase("Two Columns"))
					{
						System.out.println("Initial is Two");
					//System.out.println(fin.get(i+1));
					fin.get(i+1).click();
					String Clickablevalue=fin.get(i+1).getText();
					System.out.println("Selected value is:" +Clickablevalue);
					
					//Click on Apply button  
					driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
					Thread.sleep(3000);
					
					this.Verify(Clickablevalue, context);
					Thread.sleep(4000);
					
					}
					
					else if(col.getAttribute("title").equalsIgnoreCase("Three Columns"))
					{
						//System.out.println(fin.get(i+1));
						fin.get(i-1).click();
						Thread.sleep(2000);
						
						String Clickablevalue=fin.get(i+1).getText();
						System.out.println("Selected value is:" +Clickablevalue);
						
						//Click on Apply button  
						driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();						
						Thread.sleep(2000);
						
						this.Verify(Clickablevalue, context);
					
					}
					
					else
					{
						System.out.println("Initial is single");
						fin.get(i+1).click();
						Thread.sleep(2000);
						
						String Clickablevalue=fin.get(i+1).getText();
						System.out.println("Selected value is:" +Clickablevalue);
						
						//Click on Apply button  
						driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
						Thread.sleep(3000);
						
						
						this.Verify(Clickablevalue, context);
						
					}
					
								
				}else
					
				{
					System.out.println("Not clicked");
				}
			}	
				
		
	}
		
		catch(Exception e){
		    e.printStackTrace();
		  }
			
		   i++;
	}	
		    
		
	}

	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	
	public void Verify(String clickablevalue, ITestContext context) throws InterruptedException
	{
		//Mouse hour 
		Thread.sleep(3000);
		CommonElementsofMenu.MenubarIcon(driver);
		Thread.sleep(3000);

		//Click on Dashboard and Select Save button
		driver.findElement(By.xpath("//li[2]/ul/li[3]")).click();
		Thread.sleep(3000);
		
		String Success=driver.findElement(By.cssSelector(".message-main")).getText();
		System.out.println(Success);
		Thread.sleep(2000);
		
		//Click on OK button
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		
		CommonElementsofMenu.MenubarIcon(driver);
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//span[contains(.,'Change Layout')]")).click();
		Thread.sleep(4000);
		
        WebElement ele1=driver.findElement(By.className("colType"));
		
		List<WebElement> fin1=ele1.findElements(By.tagName("li"));
		System.out.println(fin1.size());
		
		for(WebElement ss1 : fin1)
		{
			List<WebElement> div1=ss1.findElements(By.tagName("div"));
			
			for(WebElement col1: div1)
			if(col1.getAttribute("class").equalsIgnoreCase("thumb selected"))
			{
				String Selected=col1.getAttribute("title");
				System.out.println("Output values is:"+Selected);
				
				if(clickablevalue.equalsIgnoreCase(Selected))
				{
					System.out.println("Change Layout is working fine");
					context.setAttribute("Status",1);
					context.setAttribute("Comment", "working fine");
				}
				else
				{
					System.out.println("Change layout not working");
					context.setAttribute("Status",5);
					context.setAttribute("Comment", "Failed");
					driver.findElement(By.cssSelector("#change-dashboard-layout .alert-btn")).click();
					driver.findElement(By.id("Layout failed")).click();
				}
				//Close the popup page
				Thread.sleep(2000);  
				driver.findElement(By.cssSelector("#change-dashboard-layout .alert-btn")).click();
				
			}
		}
		
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
