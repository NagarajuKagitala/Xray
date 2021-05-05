package SummaryViewlets;

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
import org.openqa.selenium.support.ui.Select;
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
public class SummaryViewlet_CreateSummaryBasedOnObjectives 
{
	static WebDriver driver;
	static String Screenshotpath;
	int Dashboardscount=0;
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
			Thread.sleep(6000);
		}
		
		//Create a Dashboard
		obj.CreateDashboard(driver, DashboardName);
		Thread.sleep(2000);
		
		WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		System.out.println("Dashboard count is: " +Dashboardscount);
	}
	
	@TestRail(testCaseId=492)
	@Parameters({"DataType", "DashboardName"})
	@Test(priority=1)
	public void CreateSummaryViewletBasedOnObjectives(String DataType, String DashboardName, ITestContext context) throws InterruptedException
	{
		try {
		//Click on summary + button 
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[4]/div[2]/button")).click();
		                                                       
		boolean SummaryOnObjectives=driver.findElement(By.xpath("//dialog[@id='create-summary-viewlet']/section/main/div[2]/label/input")).isSelected();
		
		if(SummaryOnObjectives)
		{
			System.out.println("Basic summary Radio button is already selected");
		}
		else
		{
			driver.findElement(By.xpath("//dialog[@id='create-summary-viewlet']/section/main/div[2]/label/input")).click();
		}
		Thread.sleep(1000);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='create-summary-viewlet']/section/footer/div[2]/button")).click();
		Thread.sleep(5000);          
		
		//select set name 
		driver.findElement(By.cssSelector("tr:nth-child(1) .error")).click();
		
		//Click on next after selecting the set name 
		driver.findElement(By.xpath("//dialog[@id='it-ops-wizard']/section/footer/div[2]/button[2]")).click();
			
		//Click on next button after selecting the data type
		//driver.findElement(By.xpath("//dialog[@id='it-ops-wizard']/section/footer/div[2]/button[2]")).click();
		//Thread.sleep(2000);
		
		//Click on next after selecting the time limit
		driver.findElement(By.xpath("//dialog[@id='it-ops-wizard']/section/footer/div[2]/button[2]")).click();
		Thread.sleep(2000);         
		
		/*
		 * if(DataType.equalsIgnoreCase("event")) { //Click on next button after
		 * selecting data type driver.findElement(By.xpath(
		 * "//dialog[@id='queryWizard']/section/footer/div[2]/button[2]")).click();
		 * Thread.sleep(2000); } else {
		 * driver.findElement(By.id("itops-source-activities")).click();
		 * Thread.sleep(1000);
		 * 
		 * //Click on next button after selecting data type driver.findElement(By.xpath(
		 * "//dialog[@id='queryWizard']/section/footer/div[2]/button[2]")).click();
		 * Thread.sleep(2000); }
		 */
		
		//Select Time limit value
//		driver.findElement(By.xpath("//button[2]")).click();
//		Thread.sleep(2000);
		
		//store the viewlet name into string
		String DefaultviewletName=driver.findElement(By.id("itops-viewlet-name")).getAttribute("value");
		System.out.println("Default viewlet name:" +DefaultviewletName);
		Thread.sleep(3000);
		
		//select Dashboard
		Select dashboard=new Select(driver.findElement(By.id("itops-dashboards")));
		dashboard.selectByVisibleText(DashboardName);
	
		//Click on save button
		driver.findElement(By.xpath("//button[contains(.,'Save')]")).click();
		Thread.sleep(2000);
				
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		StringBuilder buffer = new StringBuilder();
		for (WebElement k : myElements) 
		{
			// boolean str= e.getAttribute("aria-hidden");
			if (k.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
		
		
		WebElement z=k.findElement(By.id("mCSB_"+ Dashboardscount +"_container"));
		WebElement summary=z.findElement(By.tagName("ul"));
		
		List<WebElement> Divs=summary.findElements(By.tagName("li"));
				
		for(WebElement e : Divs)
		{
			List<WebElement> header=e.findElements(By.tagName("div"));
			//System.out.println("no of dives: " +header.size());
			for(WebElement e1: header)
			{
				//System.out.println("class:"+e1.getAttribute("class"));
				if(e1.getAttribute("class").contains("viewlet-head"))
				{
					List<WebElement> header1=e1.findElements(By.tagName("div"));
					//System.out.println("no of divs are: " +header.size());
					
					for(WebElement ee :header1)
					{
						if(ee.getAttribute("class").contains("viewlet-head-row"))
						{
							System.out.println("classlast:"+ee.getAttribute("class"));
							//System.out.println("html: " +ee.getAttribute("innerHTML"));
							List<WebElement> fi=ee.findElements(By.tagName("div"));
									for(WebElement na: fi)
									{
										if(na.getAttribute("class").contains("viewlet-name-wrapper"))
										{
											String Values=na.getAttribute("title").toString();
											System.out.println("values are: " +Values);
											buffer.append(Values);
											buffer.append(',');
										}
									}
									
									
						}
					}
						
						}
			}
			
			
			
					
				}
			}
		}
		
		String Data=buffer.toString();
		//String Data=summary.getText();
		//Store the viewlets data 
		//String Data=driver.findElement(By.xpath("//div[3]/div/ul/li/div")).getText();
		System.out.println("Summary viewlet data is: " +Data);
		
		
		//verification
		if(Data.contains(DefaultviewletName))
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
			driver.findElement(By.id("Viewlet Creation failed")).click();
		}
		}
	
		catch(Exception e)
		{
			e.printStackTrace();
			}
		Thread.sleep(2000);
	}
	
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
		
	}
		
	public void CreateDashboard() throws InterruptedException
	{
		// Click on Plus Icon
		driver.findElement(By.xpath("//div[@id='pageContainer-tabs-add']/div/div/span")).click();

		// Give the dashboard Name
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/input")).sendKeys("SummaryObjective");

		// uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);

		// select two columns 
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();

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
