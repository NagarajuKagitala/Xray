package EditViewletFilterOptions;

import java.io.File;
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
public class EditViewletPage_EditViewletFilterOptions 
{
	static WebDriver driver;
	static String Screenshotpath;
	static int Dashboardscount=0;
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
			
			//FirefoxOptions options = new FirefoxOptions();
			//options.setCapability("marionette", false);
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
	}
	
	@TestRail(testCaseId=500)
	@Parameters({"Query", "ViewletName"})
	@Test(priority=1)
	public void CreateViewletWithQuery(String Query, String ViewletName, ITestContext context) throws InterruptedException
	{	
		//Create viewlet
		obj.CreateViewlet(driver, Query, ViewletName);
		
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
		
		String Viewlets=buffer.toString();
		System.out.println("List of viewlets are: " +Viewlets);

		// verification
		if (Viewlets.contains(ViewletName)) 
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
			driver.findElement(By.id("Viewlet Creation failed")).click();
		}
		Thread.sleep(2000);
				
	}
	
	@TestRail(testCaseId=501)
	@Test(priority=2)
	public void Event(ITestContext context) throws InterruptedException
	{
		System.out.println("Started event method");
		Thread.sleep(4000);
		//Click on menu bar and select Edit viewlet option 
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		Thread.sleep(2000);
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);       
		
		this.SelectingEvent();
		Thread.sleep(2000);
		
		String[] Group= {"MsgLength", "ElapsedTime", "ThreadID", "ProcessID", "ReasonCode"};
		
		//Click on Group by + Icon
		//CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		//driver.findElement(By.cssSelector("label:nth-child(13) .viewlet-icon")).click();
		//Select x axis value
		//driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span")).click();
		//driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i]);
		//driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		//driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(12000);
		
		/*//Click on menu bar and select Edit viewlet option 
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[9]/i")).click();
		Thread.sleep(2000);
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);*/ 
		
		try
		{
			//String Xaxis=fin.getAttribute("value");
			//String Xaxis=driver.findElement(By.cssSelector(".amcharts-axis-title:nth-child(28) > tspan")).getText();
			//String Xaxis=driver.findElement(By.className("amcharts-axis-title")).findElement(By.tagName("tspan")).getAttribute("innerHTML");
			
			/*WebElement ele=driver.findElement(By.className("amcharts-axis-title"));
			List<WebElement> ts=driver.findElements(By.className("amcharts-axis-title"));
			System.out.println("Size is: " +ts.size());
			
			for(WebElement tt:ts)
			{
				System.out.println("values are: " +tt.getText());
			}
			System.out.println("X-Axis data:" +Xaxis); */
			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.contains(Group[i]) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
				//driver.findElement(By.xpath("Not working as expected")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		Thread.sleep(4000);
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
	}
	
	@TestRail(testCaseId=502)
	//@Parameters({"Name"})
	@Test(priority=3)
	public void Activity(ITestContext context) throws InterruptedException
	{
		//Click on menu bar and select Edit viewlet option
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);
		
		this.SelectingActivity();
		Thread.sleep(1000);
		//"ReportTime"
		String[] Group= {"ElapsedTime", "ReasonCode", "SnapShotCount", "ProcessID", "ThreadID"};
		
		//Click on Group by + Icon         
		CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		Thread.sleep(2000);
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		//Select x axis value
		//driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span")).click();
		//driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i]);
		//driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		
		//Select y- axis
		//driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div[2]/div/span/span/span/span")).click();
		//driver.findElement(By.cssSelector(".select2-search__field")).sendKeys("Activities Count");
		//driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		Thread.sleep(8000);
				
		try
		{			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.toLowerCase().contains(Group[i].toLowerCase()) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		Thread.sleep(4000);
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
	}
	
	@TestRail(testCaseId=503)
	@Test(priority=4)
	public void Snapshot(ITestContext context) throws InterruptedException
	{
		//Click on menu bar and select Edit viewlet option
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);
		
		this.SelectingSnapshot();
		Thread.sleep(2000);
		//"SnapshotTime", "ReportTime"
		String[] Group= {"ThreadID", "ReasonCode", "ProcessID"};
		
		//Click on Group by + Icon
		CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		/*
		 * //Select x axis value driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span"
		 * )).click();
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i
		 * ]);
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 * 
		 * //Select y- axis driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div[2]/div/span/span/span/span"
		 * )).click(); driver.findElement(By.cssSelector(".select2-search__field")).
		 * sendKeys("Snapshots Count");
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 */
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		Thread.sleep(8000);
				
		try
		{			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.contains(Group[i]) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
		
	}
	
	@TestRail(testCaseId=504)
	@Test(priority=5)
	public void Job(ITestContext context) throws InterruptedException
	{
		//Click on menu bar and select Edit viewlet option
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);
		
		this.SelectingJob();
		Thread.sleep(2000);
		
		String[] Group= {"ElapsedTime", "WaitTime", "ProcessID", "Percent",};
		
		//Click on Group by + Icon
		CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		/*
		 * //Select x axis value driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span"
		 * )).click();
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i
		 * ]);
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 * 
		 * //Select y- axis driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div[2]/div/span/span/span/span"
		 * )).click(); driver.findElement(By.cssSelector(".select2-search__field")).
		 * sendKeys("Jobs Count");
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 */
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		Thread.sleep(8000);
				
		try
		{			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.contains(Group[i]) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
		
	}
	
	@TestRail(testCaseId=505)
	@Test(priority=6)
	public void Log(ITestContext context) throws InterruptedException
	{
		//Click on menu bar and select Edit viewlet option
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);
		
		this.SelectingLog();
		Thread.sleep(2000);
		
		String[] Group= {"ElapsedTime", "WaitTime", "MsgLength"};
		
		//Click on Group by + Icon
		CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		/*
		 * //Select x axis value driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span"
		 * )).click();
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i
		 * ]);
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 * 
		 * //Select y- axis driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div[2]/div/span/span/span/span"
		 * )).click(); driver.findElement(By.cssSelector(".select2-search__field")).
		 * sendKeys("Logs Count");
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 */
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		Thread.sleep(8000);
				
		try
		{			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.contains(Group[i]) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
	}
	
	@TestRail(testCaseId=506)
	@Test(priority=7)
	public void Relative(ITestContext context) throws InterruptedException
	{
		//Click on menu bar and select Edit viewlet option
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);
		
		this.SelectingRelative();
		Thread.sleep(2000);
		
		String[] Group= {"ChildLatitude", "ChildLongitude", "ParentLatitude", "ParentLongitude",};
		
		//Click on Group by + Icon
		CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		//Select x axis value
		/*
		 * driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span"
		 * )).click();
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i
		 * ]);
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 * 
		 * //Select y- axis driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div[2]/div/span/span/span/span"
		 * )).click(); driver.findElement(By.cssSelector(".select2-search__field")).
		 * sendKeys("Relatives Count");
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 */
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		Thread.sleep(8000);
				
		try
		{			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.contains(Group[i]) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
	}
	
	@TestRail(testCaseId=507)
	@Test(priority=8)
	public void Resource(ITestContext context) throws InterruptedException
	{
		//Click on menu bar and select Edit viewlet option
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);
		
		this.SelectingResource();
		Thread.sleep(2000);
		
		String[] Group= {"Latitude", "Longitude"};
		
		//Click on Group by + Icon
		CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		/*
		 * //Select x axis value driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span"
		 * )).click();
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i
		 * ]);
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 * 
		 * //Select y- axis driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div[2]/div/span/span/span/span"
		 * )).click(); driver.findElement(By.cssSelector(".select2-search__field")).
		 * sendKeys("Resources Count");
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 */
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		Thread.sleep(8000);
				
		try
		{			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.contains(Group[i]) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
	}
	
	@TestRail(testCaseId=508)
	@Test(priority=9)
	public void Source(ITestContext context) throws InterruptedException
	{
		//Click on menu bar and select Edit viewlet option
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
		CommonElementsofEditViewlet.Editoption(driver);
		Thread.sleep(4000);
		
		this.SelectingSource();
		Thread.sleep(2000);
		
		String[] Group= {"Latitude", "Longitude"};
		
		//Click on Group by + Icon 
		CommonElementsofEditViewlet.GroupByPlusIcon(driver);
		
		for(int i=0; i<Group.length; i++) 
		{
		CommonElementsofEditViewlet.GroupByOption(driver);
		
		//Change Option             
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		//System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			//System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase(Group[i]))
			{
				list.click();
				break;
			}
		}
		
		WebElement bucket=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/label/input"));
		if(bucket.isSelected())
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			bucket.click();
			Thread.sleep(2000);
		}
		Thread.sleep(4000);
		
		/*
		 * //Select x axis value driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div/div/span/span/span/span"
		 * )).click();
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Group[i
		 * ]);
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 * 
		 * //Select y- axis driver.findElement(By.xpath(
		 * "//div[@id='viewlet-form-settings-wp']/div[8]/div/div/div[2]/div/span/span/span/span"
		 * )).click(); driver.findElement(By.cssSelector(".select2-search__field")).
		 * sendKeys("Sources Count");
		 * driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(Keys.
		 * ENTER); Thread.sleep(2000);
		 */
		
		driver.manage().deleteAllCookies();
		
		CommonElementsofEditViewlet.PreviewButton(driver);
		Thread.sleep(8000);
				
		try
		{			
			//Total data
			String Total=driver.findElement(By.id("viewlet-form-preview-wp")).getText();
			System.out.println("Total data is: " +Total);
			
			System.out.println("Selected x axis values is: " +Group[i]);
			
			//Verification
			if(Total.contains(Group[i]) || Total.contains("No record found"))
			{
				System.out.println("Working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Not Working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				CommonElementsofEditViewlet.CancelButton(driver);
				driver.findElement(By.id("failed")).click();
			}
		
		}
		catch (Exception e)
		{
			System.out.println("X- axis element is not found");
		}
		}
		
		//Click on Cancel button
		CommonElementsofEditViewlet.CancelButton(driver);
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
		Thread.sleep(2000);
						
		//Give the dashboard Name 
		driver.findElement(By.cssSelector("#createDashboard .input-field")).sendKeys("EditViewletFilters");
		Thread.sleep(1000);			
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);	
		//select two columns
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();
		Thread.sleep(1000);			
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
		
	}
	
	public void SelectingEvent() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		//CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Event"))
			{
				list.click();
				break;
			}
		}
	}
	
	public void SelectingActivity() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity  
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Activity"))
			{
				list.click();
				break;
			}
		}
	}
	
	public void SelectingSnapshot() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Snapshot"))
			{
				list.click();
				break;
			}
		}
	}
	
	public void SelectingJob() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Job"))
			{
				list.click();
				break;
			}
		}
	}
	
	public void SelectingLog() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Log"))
			{
				list.click();
				break;
			}
		}
	}
	
	public void SelectingRelative() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Relative"))
			{
				list.click();
				break;
			}
		}
	}
	
	public void SelectingResource() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Resource"))
			{
				list.click();
				break;
			}
		}
	}
	
	public void SelectingSource() throws InterruptedException
	{
		CommonElementsofEditViewlet.SelectDatatype(driver);
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Source"))
			{
				list.click();
				break;
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
