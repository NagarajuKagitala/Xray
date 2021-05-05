package ViewletLevel;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
public class ViewletLevel_ExistingViewlet 
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
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=447)
	@Test(priority=1)
	public void OpenExistingViewletPopup(ITestContext context) throws InterruptedException
	{
		try
		{
		//click on viewlet option
			driver.findElement(By.xpath("//div[@id='app-top-sidebar']/div[3]")).click();
		
		//Check the Create viewlet with JKQL check box
		boolean Existing=driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[3]/label/input")).isSelected();
		System.out.println(Existing);

		if(Existing==true)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[3]/label/input")).click();
			Thread.sleep(1000);
		}
		//Click on Create button
		driver.findElement(By.id("createViewletBtn")).click();
		Thread.sleep(5000);
		
		//Get the open existing viewlet page title 
		String Name=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div")).getText();
		System.out.println("Open existing viewlet page title is: "+Name);
		
		if(Name.contains("Open Existing"))
		{
			System.out.println("Open existing viewlet page is opened");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Open existing viewlet page is not opened");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Open Existing viewlet")).click();
		}
		}
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Open Existing viewlet")).click();
		}
	}
	
	@TestRail(testCaseId=448)
	@Test(priority=2)
	public void AllOptionsStatus(ITestContext context) throws InterruptedException
	{
		
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
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Sort By dropdown is not Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
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
		Thread.sleep(4000);
		
	}
	
	@Parameters({"ViewbyValue"})
	@TestRail(testCaseId=449)
	@Test(priority=3)
	public void ViewByFiletrDetailsOption(String ViewbyValue, ITestContext context)
	{
		//Select Details option	
		Select Details=new Select(driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div/select")));
		Details.selectByVisibleText(ViewbyValue);             
		
		WebElement q2=driver.findElement(By.className("open-viewlet-detailed"));
		List<WebElement> e=q2.findElements(By.tagName("th"));
		System.out.println(e.size());
		
		for(WebElement s : e)
		{
			System.out.println(s.getText());
			String columnnames=s.getText();
			if(columnnames.equalsIgnoreCase("Widget Name") || columnnames.equalsIgnoreCase("Type") || columnnames.equalsIgnoreCase("Dashboard Name"))
			{
				System.out.println("Columns names are verified");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
			}
		}
		
	}
	
	
	@Parameters({"SortOption", "ViewbyValueBack"})
	@TestRail(testCaseId=450)
	@Test(priority=4)
	public void SortByAlphabetically(String SortOption, String ViewbyValueBack,ITestContext context) throws InterruptedException
	{
		//Select Alphabetically option
		Select SortBy=new Select(driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[2]/select")));
		SortBy.selectByVisibleText(SortOption);
		
		//Select Details option	
		Select Details=new Select(driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div/select")));
		Details.selectByVisibleText(ViewbyValueBack);
		Thread.sleep(3000);
				
		WebElement List=driver.findElement(By.className("open-viewlet-icons"));
		List<WebElement> names=List.findElements(By.tagName("li"));
		System.out.println(names.size());
		
		String Array[]=new String[names.size()];
		
		 int k=0;		
		for(WebElement Sort : names)
		{
			
			Array[k]=Sort.getText().toLowerCase();
			k++;
			
		}
		
		//System.out.println("Array1: " + Arrays.toString(Array));
		
		
		String[] arrSorted = Array;	
		Arrays.sort(arrSorted); 
	   // System.out.printf("Array2: " +Arrays.toString(arrSorted)); 
		
		 for(int i = 0; i <  Array.length ;i++){
			 
			 if(Array[i].equals(arrSorted[i]))
			 {
				 System.out.println("Sorted");
				 context.setAttribute("Status",1);
				 context.setAttribute("Comment", "working fine");
			 }
			 else
			 {
				 System.out.println("Unsorted");
				 context.setAttribute("Status",5);
				 context.setAttribute("Comment", "Failed");
				 driver.findElement(By.id("Sorting failed")).click();
			 }
		    }
			 		
	}
	
	
	@Parameters({"ValidChartName"})
	@TestRail(testCaseId=451)
	@Test(priority=5)
	public void ValidSearch(String ValidChartName,ITestContext context) throws InterruptedException
	{
		//Get the existing viewlet name
		String Viewletname=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul/li/label")).getText();
		System.out.println("Existing viewlet name is: " +Viewletname);
		
		//Enter the chart name into search field
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).sendKeys(Viewletname);
		Thread.sleep(1000);
		
		//Get the data and store into string
		String SearchResults=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul")).getText();
		System.out.println("Results are: " +SearchResults);
		
		if(SearchResults.contains(Viewletname))
		{
			System.out.println("Search is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Search is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Search field is Failed")).click();
		}
		Thread.sleep(1000);
		
	}
	
	@Parameters({"InValidChartName"})
	@TestRail(testCaseId=452)
	@Test(priority=6)
	public void InValidSearch(String InValidChartName,ITestContext context) throws InterruptedException
	{
		//Enter the chart name into search field
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).sendKeys(InValidChartName);
		Thread.sleep(1000);
		
		//Get the data and store into string
		String SearchResults=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/div")).getText();
		System.out.println("Invalid search results data: " +SearchResults);
		
		String NoRecordsMessage="No viewlets available for "+'\''+InValidChartName+'\'';
		System.out.println("No records found data: " +NoRecordsMessage);
		
		for(int i=0; i<=InValidChartName.length(); i++)
		{
			driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification
		if(SearchResults.contains(NoRecordsMessage))
		{
			System.out.println("Invalid Search is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Invalid Search is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Invalid search field is failed")).click();
		}
		Thread.sleep(1000);
		
	}
	@TestRail(testCaseId=453)
	@Test(priority=7)
	public void ButtonsStatus(ITestContext context) throws InterruptedException
	{
		//Cancel Button
		Boolean Cancel=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/footer/div/button")).isEnabled();
		
		if(Cancel==true)
		{
			System.out.println("Cancel button is Enabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Cancel button is not Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Cancecl is Disable")).click();
		}
		
		//Open Button
		Boolean Open=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/footer/div[2]/button")).isEnabled();
		
		if(Open==false)
		{
			System.out.println("Open button is not Enabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Open button is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Open is Disable")).click();
		}
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=753)
	@Test(priority=8)
	public void OpenExistingViewlet(ITestContext context) throws InterruptedException
	{
		//Enter the chart name into search field
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/header/div[2]/div[3]/input")).sendKeys("ViewletName");
		Thread.sleep(2000);
		
		//Get the existing viewlet name 
		String Viewletname=driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul/li/label")).getText();
		System.out.println("Existing viewlet name is: " +Viewletname);
		
		//Select viewlet
		driver.findElement(By.xpath("//dialog[@id='open-viewlet']/section/main/div/ul/li/img")).click();
		
		//Click on Open button
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
		if (Viewlets.contains(Viewletname)) 
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
		
		//Click on Cancel button
//		driver.findElement(By.cssSelector("#open-widget .alert-btn")).click();
//		Thread.sleep(1000);
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
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();

		// Give the dashboard Name
		driver.findElement(By.cssSelector("#createDashboard .input-field")).sendKeys("OpenExistingViewlet");

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
