package ViewletLevel;

import java.io.File;
import java.util.HashMap;
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
import org.openqa.selenium.chrome.ChromeOptions;
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
public class ViewletLevel_ConsolePage {
	static WebDriver driver;
	static String Screenshotpath;
	int Dashboardscount=0;
	CommonForAll obj=new CommonForAll();

	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();

		Screenshotpath = Settings.getScreenshotPath();
	}

	// Login page
	@Test
	@Parameters({ "sDriverPath", "sDriver", "DownloadPath", "DashboardName"})
	public void Login(String sDriverPath, String sDriver, String DownloadPath, String DashboardName) throws Exception {

		Settings.read();
		String sURL = Settings.getsURL();
		String sUsername = Settings.getsUsername();
		String sPassword = Settings.getsPassword();

		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverPath);
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", DownloadPath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(options);
		} else if (sDriver.equalsIgnoreCase("webdriver.gecko.driver")) {
			System.setProperty(sDriver, sDriverPath);

		//	FirefoxOptions options = new FirefoxOptions();
		//	options.setCapability("marionette", false);
			driver = new FirefoxDriver();

		} else if (sDriver.equalsIgnoreCase("webdriver.ie.driver")) {
			System.setProperty(sDriver, sDriverPath);
			driver = new InternetExplorerDriver();
		} else {
			System.setProperty(sDriver, sDriverPath);
			driver = new EdgeDriver();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// Login Credentials
		driver.get(sURL);
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(4000);

		// Check Landing page
		if (driver.getPageSource().contains("Go to Dashboard")) {
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(15000);
		} else {
			System.out.println("Landing page is not present");
			Thread.sleep(6000);
		}

		obj.CreateDashboard(driver, DashboardName);
		Thread.sleep(3000);
		
		WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		System.out.println(Dashboardscount);
	}

	@Parameters({"Query", "ViewletName"})
	@TestRail(testCaseId = 465)
	@Test(priority = 1)
	public void CreateViewletforconsole(String Query, String ViewletName, ITestContext context) throws InterruptedException 
	{
		obj.CreateViewlet(driver, Query, ViewletName);	
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {

		// boolean str= e.getAttribute("aria-hidden");
		if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
		List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
		System.out.println("Viewlet size:" + ee.size());
		String GetViewletName="";
		for(WebElement innerele: ee)
		{
		GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
		System.out.println("Viewlet name:" + GetViewletName);
		}

		// verification
		if (GetViewletName.equalsIgnoreCase(ViewletName)) {
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

	@Parameters({"ConsoleQuery"})
	@TestRail(testCaseId = 466)
	@Test(priority = 2)
	public void Console(String ConsoleQuery, ITestContext context) throws InterruptedException 
	{		
		Thread.sleep(3000);
		try
		{
			// Click on The histogram icon                   			
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div[2]/div/div/div[3]/div[3]/div/table/tbody/tr[2]/td[2]")).click();
			Thread.sleep(5000);
		}
		catch (Exception e)
		{
			//Click on + icon in the console 
			  driver.findElement(By.xpath("//div[2]/div[5]/div[2]/button")).click();
			  Thread.sleep(2000);
			  
			  //Click on query field and enter the query 
			  driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).click();
			  driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(ConsoleQuery);
			  driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).
			  sendKeys(Keys.ENTER); Thread.sleep(4000);
			 
		}
		
		// Store the Event details
		String Event = driver.findElement(By.xpath("//span[contains(.,'Event Details')]")).getText();
		// System.out.println(Event);

		if (Event.equalsIgnoreCase("Event Details")||(Event.equalsIgnoreCase("Temporary viewlet"))) {
			System.out.println("Console page is opened");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Console page is not opened");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Console page open failed")).click();
		}
		Thread.sleep(2000);
	}

	@TestRail(testCaseId = 467)
	@Test(priority = 3)
	public void ConsoleViewletEditOption(ITestContext context) throws InterruptedException {
		// Click on Viewlet Menu    
		driver.findElement(By.xpath("//div/div/div/div[2]/button[5]/i")).click();
		Thread.sleep(3000);
		
		WebElement Editoption=driver.findElement(By.className("modal-popup"));
		WebElement ul=Editoption.findElement(By.tagName("ul"));
		
		List<WebElement> li=ul.findElements(By.tagName("li"));
		
		for(WebElement clas: li)
		{
				System.out.println("Text value is: " +clas.getText());
				String title=clas.getText();
				
				if(title.equalsIgnoreCase("Edit Viewlet"))
				{
					if(!clas.getAttribute("class").equalsIgnoreCase("disabled"))
					{
						clas.click();
						Thread.sleep(4000);
						
						String ViewletNameTitle=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/h3/span")).getText();
						System.out.println("Edit viewlet header name is :" +ViewletNameTitle );
					
						// Click on close button in Edit viewlet
						driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[9]/button")).click();
						Thread.sleep(2000);
						
						if(ViewletNameTitle.equalsIgnoreCase("Viewlet Name"))
						{
							System.out.println("Edit option is working");
							context.setAttribute("Status", 1);
							context.setAttribute("Comment", "working fine");
							break;
						}
						else
						{
							context.setAttribute("Status", 5);
							context.setAttribute("Comment", "Failed");
							driver.findElement(By.id("Edip option failed")).click();
						}
					}
					else
					{
						System.out.println("Edit option is disabled");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");
						break;
					}
				
				}
		}
		Thread.sleep(2000);
	}

	@TestRail(testCaseId = 468)
	@Test(priority = 4)
	public void ConsoleViewletExportOption(ITestContext context) throws InterruptedException {
		// Click on Viewlet Menu
		driver.findElement(By.xpath("//div/div/div/div[2]/button[5]/i")).click();
		
		WebElement Editoption=driver.findElement(By.className("modal-popup"));
		WebElement ul=Editoption.findElement(By.tagName("ul"));
		
		List<WebElement> li=ul.findElements(By.tagName("li"));
		
		for(WebElement clas: li)
		{
				System.out.println("Class value is: " +clas.getText());
				String title=clas.getText();
				
				if(title.equalsIgnoreCase("Export to CSV"))
				{
				System.out.println(clas.getAttribute("class"));	
					if(clas.getAttribute("class").equalsIgnoreCase("enabled"))
					{
						clas.click();
						Thread.sleep(5000);
						
						String ViewletNameTitle=driver.findElement(By.xpath("//a[@id='ui-id-22']/span")).getText();
						System.out.println("Edit viewlet header name is :" +ViewletNameTitle );
						Thread.sleep(3000);
					
						
						if(ViewletNameTitle.equalsIgnoreCase("Event Details"))
						{
							System.out.println("Edit option is working");
							context.setAttribute("Status", 1);
							context.setAttribute("Comment", "working fine");
						}
						else
						{
							context.setAttribute("Status", 5);
							context.setAttribute("Comment", "Failed");
							driver.findElement(By.id("Edit option failed")).click();
						}
					}
					else
					{
						System.out.println("Export option is disabled");
						driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
						Thread.sleep(2000);
						driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[9]/button")).click();
						Thread.sleep(5000);
//						driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
//						Thread.sleep(2000);	
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");
						
//						driver.findElement(By.cssSelector(".setting-icon:nth-child(6) > .fa")).click();
//						driver.findElement(By.cssSelector(".modal-popup-overlay")).click();
//						Thread.sleep(1000);
					}
					
					
				}
				
		}
		
		// Click on close button in Edit viewlet
//		driver.findElement(By.cssSelector(".ui-state-hover > .ui-icon")).click();
//		Thread.sleep(2000);	
		}

	@TestRail(testCaseId = 469)
	@Test(priority = 5)
	public void CheckOptionsStatusWithOutQueryforTemporaryViewlet(ITestContext context) throws InterruptedException 
	{
		// Click on + icon 
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[5]/div[2]/button")).click();
		Thread.sleep(2000);
		
		/*WebElement start=driver.findElement(By.xpath("//li[contains(@class, 'ui-corner-bottom')]")).findElement(By.tagName("div")).findElement(By.tagName("div")).findElement(By.tagName("div"));
		WebElement icons=start.findElement(By.className("controls"));
		
		List<WebElement> divs=icons.findElements(By.tagName("div"));
		System.out.println("Divs size is: " +divs.size());

		StringBuilder buffer = new StringBuilder();
		for(WebElement enable:divs)
		{
			System.out.println("class are: " +enable.getAttribute("class"));
			if(enable.getAttribute("class").contains("disabled"))
			{
				System.out.println(enable.getAttribute("title"));
				String Values=enable.getAttribute("title").toString();
				buffer.append(Values);
				buffer.append(',');
				Thread.sleep(2000);
			}
		}
		
		String ListOfDisableoptions=buffer.toString();
		System.out.println("List of options are: " +ListOfDisableoptions);
		
		if(ListOfDisableoptions.contains("Change chart type") && ListOfDisableoptions.contains("Set the date and time range for this viewlet"))
		{
			System.out.println("Options are disabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Options status failed")).click();
		}*/
		
		/*// Rest Query
		Boolean ResetQuery = driver.findElement(By.xpath("//div[3]/i")).isSelected();
		System.out.println(ResetQuery);

		if (ResetQuery == true) {
			System.out.println("Reset Query is Enabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
			driver.findElement(By.id("Reset query Enabled")).click();
		} else {
			System.out.println("Reset Query is Disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");

		}*/

		// Chart type                                 
		Boolean ChartType = driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div/div/div[2]/button[2]/i")).isEnabled();
		 System.out.println(ChartType);

		if (ChartType==false) {
			System.out.println("Chart Type is Enabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Chart type Option Enabled")).click();
			
		} else {
			System.out.println("Chart Type is Disabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}

		// Date and Time Query
		Boolean DateTime = driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div/div/div[2]/button[3]/i")).isEnabled();
		System.out.println(DateTime);

		if (DateTime==false) {
			System.out.println("DateTime is Enabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Date time is Enabled")).click();
		} else {
			System.out.println("DateTime is Disabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}

		/*// Refresh Query
		Boolean RefreshQuery = driver.findElement(By.xpath("//li/div[2]/div/div[2]/div[7]/i")).isSelected();
		// System.out.println(RefreshQuery);

		if (RefreshQuery == true) {
			System.out.println("Refresh Query is Enabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
			driver.findElement(By.id("Refresh option Enabled")).click();
		} else {
			System.out.println("Refresh Query is Disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");

		}

		// Viewlet Menu
		Boolean ViewletMenu = driver.findElement(By.xpath("//li[2]/div[2]/div/div[2]/div[9]]")).isSelected();
		// System.out.println(ViewletMenu);

		if (ViewletMenu == true) {
			System.out.println("Viewlet Menu is Enabled");
			driver.findElement(By.id("Viewlet menu is Enabled")).click();
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Viewlet Menu is Disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");

		}*/
		Thread.sleep(3000);

	}

	@Parameters({ "NQuery" })
	@TestRail(testCaseId = 470)
	@Test(priority = 6)
	public void EnterQueryIntoTemporaryViewletInTheConsolePage(String NQuery, ITestContext context) throws InterruptedException 
	{
		// Enter the query into field 
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(NQuery);
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);		
		
		//Get events name                              
		String Columns=driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[3]")).getText();
		System.out.println("Column name is: " +Columns);

		/*
		 * StringBuilder buffer=new StringBuilder(); try { List<WebElement> myElements =
		 * driver.findElements(By.cssSelector("*[class^='tab-div']")); //
		 * System.out.println(myElements.size()); for (WebElement e : myElements) {
		 * 
		 * // boolean str= e.getAttribute("aria-hidden"); if
		 * (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
		 * 
		 * WebElement
		 * con=e.findElement(By.className("console-panel")).findElement(By.className(
		 * "tabs-panels")); List<WebElement> lis=con.findElements(By.tagName("div"));
		 * System.out.println("Size are: " +lis.size());
		 * 
		 * for(WebElement f:lis) { System.out.println("Class status are: "
		 * +f.getAttribute("aria-hidden"));
		 * if(f.getAttribute("aria-hidden").equalsIgnoreCase("false")) { WebElement
		 * tab=f.findElement(By.tagName("table")).findElement(By.tagName("thead")).
		 * findElement(By.tagName("tr")); List<WebElement>
		 * th=tab.findElements(By.tagName("th")); System.out.println("Th  size is: "
		 * +th.size()); for(WebElement fi:th) { WebElement
		 * fdiv=fi.findElement(By.tagName("div")); String value=fdiv.getText();
		 * buffer.append(value); buffer.append(','); //System.out.println("Values are: "
		 * +value); } } } } } } catch (Exception e) { e.printStackTrace(); }
		 * 
		 * String Columns=buffer.toString(); System.out.println("Columns values are: "
		 * +Columns);
		 */
		
		if(Columns.contains("EventID"))
		{
			System.out.println("Query is executed");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Query is not executed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query execution failed")).click();
		}
		Thread.sleep(4000);
	}

	@TestRail(testCaseId = 471)
	@Test(priority = 7)
	public void CheckTemporaryViewletOptionsStatusWithQueryIntheConsolePage(ITestContext context) throws InterruptedException 
	{		
		/*
		 * // Rest Query Boolean ResetQuery =
		 * driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[2]/button/i")).
		 * isEnabled(); System.out.println(ResetQuery);
		 * 
		 * if (ResetQuery == true) { System.out.println("Reset Query is Enabled");
		 * context.setAttribute("Status", 1); context.setAttribute("Comment",
		 * "working fine"); } else { System.out.println("Reset Query is Disabled");
		 * context.setAttribute("Status", 5); context.setAttribute("Comment", "Failed");
		 * driver.findElement(By.id("Reset query diasbled")).click(); }
		 */

		// Chart type
		Boolean ChartType = driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[2]/button[2]/i")).isEnabled();
		System.out.println(ChartType);                   

		if (ChartType == true) {
			System.out.println("Chart Type is Enabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Chart Type is Disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Chart type disable")).click();
		}

		// Date and Time Query
		Boolean DateTime = driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[2]/button[3]/i")).isEnabled();
		System.out.println(DateTime);

		if (DateTime == true) {
			System.out.println("DateTime is Enabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("DateTime is Disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("CDate time disable")).click();
		}

		// Refresh Query
		Boolean RefreshQuery = driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/button[4]/i")).isEnabled();
		System.out.println(RefreshQuery);

		if (RefreshQuery == true) {
			System.out.println("Refresh Query is Enabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Refresh Query is Disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Refresh query disable")).click();
		}

		// Viewlet Menu
		Boolean ViewletMenu = driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/button[5]/i")).isEnabled();
	    System.out.println(ViewletMenu);

		if (ViewletMenu == true) {
			System.out.println("Viewlet Menu is Enabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Viewlet Menu is Disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Viewlet Menu disable")).click();
		}
		Thread.sleep(2000);
	}

	@Parameters({ "NewQuery" })
	@TestRail(testCaseId = 472)
	@Test(priority = 8)
	public void EditTempoararyViewletQueryIntheConsolePage(String NewQuery, ITestContext context) throws InterruptedException 
	{
		// Click on Edit Query
		driver.findElement(By.xpath("//div[2]/div[2]/div/div/div/div[2]/button/i")).click();
		Thread.sleep(2000);          

		// Edit the query
		WebElement ele=driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea"));
		ele.sendKeys(Keys.CONTROL + "a");           
		ele.sendKeys(Keys.DELETE);
		Thread.sleep(2000);

		// Enter the new query
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(NewQuery);
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(6000);
		
		/*
		 * StringBuilder buffer=new StringBuilder(); try { List<WebElement> myElements =
		 * driver.findElements(By.cssSelector("*[class^='tab-div']")); //
		 * System.out.println(myElements.size()); for (WebElement e : myElements) {
		 * 
		 * // boolean str= e.getAttribute("aria-hidden"); if
		 * (e.getAttribute("aria-hidden").toLowerCase().matches("false")) { WebElement
		 * con=e.findElement(By.className("console-panel")).findElement(By.className(
		 * "tabs-panels")); List<WebElement> lis=con.findElements(By.tagName("li"));
		 * System.out.println("Size are: " +lis.size());
		 * 
		 * for(WebElement f:lis) { System.out.println("Class status are: "
		 * +f.getAttribute("aria-hidden"));
		 * if(f.getAttribute("aria-hidden").equalsIgnoreCase("false")) { WebElement
		 * tab=f.findElement(By.tagName("table")).findElement(By.tagName("thead")).
		 * findElement(By.tagName("tr")); List<WebElement>
		 * th=tab.findElements(By.tagName("th")); System.out.println("Th  size is: "
		 * +th.size()); for(WebElement fi:th) { WebElement
		 * fdiv=fi.findElement(By.tagName("div")); String value=fdiv.getText();
		 * buffer.append(value); buffer.append(','); System.out.println("Values are: "
		 * +value); } } } } } } catch (Exception e) { e.printStackTrace(); }
		 * 
		 * if(buffer.toString().contains("ActivityID")) {
		 * System.out.println("Query is updated"); context.setAttribute("Status", 1);
		 * context.setAttribute("Comment", "working fine"); } else {
		 * System.out.println("Query is not updated"); context.setAttribute("Status",
		 * 5); context.setAttribute("Comment", "Failed");
		 * driver.findElement(By.id("Query Update failed")).click(); }
		 */
		//Store the new query into string
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).click();
		Thread.sleep(4000);
		//String UpdatedQuery=driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).getText();
		String Updated=driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).getAttribute("value");
		//System.out.println("Updated query is: " +UpdatedQuery); 
		System.out.println("Updated query value: " +Updated); 
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(Keys.ENTER);
		
		//Get the Activity column name
		//String Columnid=driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[3]")).getText();
		//System.out.println("Column name is: " +Columnid);
		
		if(Updated.equalsIgnoreCase(NewQuery))
		{
			System.out.println("Query is updated");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
		else 
		{
			System.out.println("Query is not updated");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query Update failed")).click();
		}
		Thread.sleep(1000);
	}

	@TestRail(testCaseId = 473)
	@Test(priority = 9)
	public void TemporaryViewletRefreshOptionIntheConsolePage(ITestContext context) throws InterruptedException 
	{
		try
		{
		// click on Refresh icon
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/button[4]/i")).click();
		Thread.sleep(2000);
		context.setAttribute("Status", 1);
		context.setAttribute("Comment", "working fine");
		}
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Refresh failed")).click();
		}
		Thread.sleep(1000);
	}

	@TestRail(testCaseId = 474)
	@Test(priority = 10)
	public void TemporaryViewletEditOptionFromViewletMenuIntheConsolepage() throws InterruptedException {
		// Click on Viewlet Menu and Select Edit option
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/button[5]/i")).click();
		driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
		Thread.sleep(4000);

		// Verifying the right side panel buttons 
		this.RightSidePannel();
		this.ButtonsAndStatus();
		Thread.sleep(1000);
	}

	@TestRail(testCaseId = 475)
	@Test(priority = 11)
	public void CloseButtonFuncationalityinTemporaryviewletEditPage(ITestContext context) throws InterruptedException {
		// Click on Close button
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[9]/button")).click();
		Thread.sleep(3000);           
		
		//Get temporary viewlet name into string   
		String Temp=driver.findElement(By.xpath("//div[3]/div/div/ul/li[2]/a/span")).getText();
		System.out.println("Temp viewlet name is: " +Temp);

		if (Temp.equalsIgnoreCase("Temporary viewlet")) 
		{
			System.out.println("The close button is working");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("The close button is not working fine");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Close button failed")).click();
		}
	}

	@TestRail(testCaseId = 476)
	@Test(priority = 12)
	public void PreviewButtonFuncationalityinTemporaryviewletEditPage() throws InterruptedException {
		
		// Click on Viewlet Menu and Select Edit option 
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/button[5]/i")).click();
		driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
		Thread.sleep(4000);

		this.PreviewButton1();
		Thread.sleep(3000);
	}

	@TestRail(testCaseId = 477)
	@Test(priority = 13)
	public void ApplyButtonFuncationalityinTemporaryviewletEditPage(ITestContext context) throws InterruptedException {
		// Click on Apply button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(6000);

		/*
		 * StringBuilder buffer=new StringBuilder(); try { List<WebElement> myElements =
		 * driver.findElements(By.cssSelector("*[class^='tab-div']")); //
		 * System.out.println(myElements.size()); for (WebElement e : myElements) {
		 * 
		 * // boolean str= e.getAttribute("aria-hidden"); if
		 * (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
		 * 
		 * 
		 * WebElement
		 * con=e.findElement(By.className("console-panel")).findElement(By.className(
		 * "tabs-panels")); List<WebElement> lis=con.findElements(By.tagName("li"));
		 * System.out.println("Size are: " +lis.size());
		 * 
		 * for(WebElement f:lis) { System.out.println("Class status are: "
		 * +f.getAttribute("aria-hidden"));
		 * if(f.getAttribute("aria-hidden").equalsIgnoreCase("false")) { WebElement
		 * tab=f.findElement(By.className("viewlet-body")).findElement(By.tagName(
		 * "table")).findElement(By.tagName("thead")).findElement(By.tagName("tr"));
		 * List<WebElement> th=tab.findElements(By.tagName("th"));
		 * System.out.println("Th  size is: " +th.size()); for(WebElement fi:th) {
		 * WebElement fdiv=fi.findElement(By.tagName("div")); String
		 * value=fdiv.getText(); buffer.append(value); buffer.append(',');
		 * //System.out.println("Values are: " +value); } } } } } } catch (Exception e)
		 * { e.printStackTrace(); }
		 * 
		 * System.out.println("buffer values are: " +buffer.toString());
		
		
		
		if(buffer.toString().contains("EventName"))
		{
			System.out.println("Query is executed");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Query is not executed");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query execution failed")).click(); 
		}  */
		
	    // Store the Activity name into string
		String Ver = driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[5]")).getText();
		System.out.println(Ver);

		if (Ver.equalsIgnoreCase(" EventName")) 
		{
			System.out.println("Apply button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Apply button is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Apply button disable")).click();
		}
		Thread.sleep(1000);
	}

	@TestRail(testCaseId = 478)
	@Test(priority = 14)
	public void TemporaryViewletExportoptionFromMenubar(ITestContext context) throws InterruptedException 
	{
		try
		{
		// Click on Viewlet Menu and Select Edit option
	   //driver.findElement(By.xpath("//div[2]/li[2]/div/div/div[2]/div[6]/i")).click();
			//driver.findElement(By.xpath("//div[2]/li/div/div/div[2]/div[6]/i")).click();
			
		//driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/button[5]/i")).click();
	   
		WebElement Editoption=driver.findElement(By.className("modal-popup"));
		WebElement ul=Editoption.findElement(By.tagName("ul"));
		
		List<WebElement> li=ul.findElements(By.tagName("li"));
		
		for(WebElement clas: li)
		{
				System.out.println("Class value is: " +clas.getText());
				String title=clas.getText();
				
				if(title.equalsIgnoreCase("Export to CSV"))
				{
					if(!clas.getAttribute("class").equalsIgnoreCase("disabled"))
					{
						clas.click();
						Thread.sleep(4000);
						System.out.println("Export option is working");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");
					}
				
					else
					{
						System.out.println("Export option is disabled");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");
						Thread.sleep(3000);
						//driver.findElement(By.cssSelector(".modal-popup-overlay:nth-child(22)")).click();
					}
				
				}
		}
		
		}
		catch (Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Temporary viewlet export failed")).click();
		}

	}
	@TestRail(testCaseId = 799)
	@Test(priority=20)
	public void movetoDashboard(ITestContext context) throws InterruptedException
	{
		//get viewlet name
		String ViewletName=driver.findElement(By.xpath("//span[contains(.,'Temporary viewlet')]")).getText();
		System.out.println("ViewletName:"+ViewletName);
		Thread.sleep(2000);
		//click on move to dashboard icon
		driver.findElement(By.xpath("//div[2]/div/div/div/div[2]/button[6]/i")).click();
		Thread.sleep(3000);
		//verification of viewlet
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
		
		String ListOfViewletnames=buffer.toString();
		System.out.println("List of viewlets are: " +ListOfViewletnames);
		
		if(ListOfViewletnames.contains(ViewletName))
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
			driver.findElement(By.id("move to dashboard failed")).click();
			
		}
		Thread.sleep(2000);
	}
	@TestRail(testCaseId = 800)
	@Test(priority=21)
	public void TearOff(ITestContext context) throws InterruptedException
	{
		//click on tear off icon on created viewlet 
		driver.findElement(By.xpath("//div/div/div/div[2]/button[7]/i")).click();
		Thread.sleep(15000);     
		
		java.util.Set<String> h=driver.getWindowHandles();
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
		//switch from viewlet to dockViewlet
		driver.switchTo().window(handle[1]);
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the Dock page:" +CurrentUrl);
		
		if(CurrentUrl.contains("dock.jsp"))
		{
			System.out.println("Tear off button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Tear off button is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("Tear off button failed")).click();
			
			//Back to jkool
			driver.switchTo().window(handle[0]);
			Thread.sleep(2000);	
		}
		driver.close();
		//Back to jkool
		driver.switchTo().window(handle[0]);
		Thread.sleep(2000);		
		
	}

	@Test(priority = 22)
	public void Logout() throws InterruptedException 
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);

	}

	public void CreateDashboard() throws InterruptedException {
		//Click on Plus Icon
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
								
		//Give the dashboard Name
		driver.findElement(By.cssSelector("#createDashboard .input-field")).sendKeys("QueryViewlet");
								
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);
						
		//select two columns
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();
								
		//Click on Create button
	    driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);

	}

	public void ButtonsAndStatus() throws InterruptedException {
		// Close button
		Boolean Close = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[9]/button")).isEnabled();

		if (Close) {
			System.out.println("Close button is Enabled");
		} else {
			System.out.println("Close button is not Enabled");
			driver.findElement(By.id("Close button disable")).click();
		}
		Thread.sleep(1000);

		// Preview button
		Boolean Preview = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[9]/div/button[2]")).isEnabled();

		if (Preview) {
			System.out.println("Preview button is Enabled");
		} else {
			System.out.println("Preview button is not Enabled");
			driver.findElement(By.id("Preview button disable")).click();
		}
		Thread.sleep(1000);

		// Apply button
		Boolean Apply = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[9]/div/button")).isEnabled();

		if (Apply) {
			System.out.println("Apply button is Enabled");
		} else {
			System.out.println("Apply button is not Enabled");
			driver.findElement(By.id("Apply button disable")).click();
		}
		Thread.sleep(2000);

	}

	public void RightSidePannel() throws InterruptedException {
		// Viewlet Name field
		Boolean Viewlet = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/h3/span")).isEnabled();

		if (Viewlet) {
			System.out.println("Viewlet Name field is Enabled");
		} else {
			System.out.println("ViewletName field is not Enabled");
			driver.findElement(By.id("Viewlet name field disable")).click();
		}
		Thread.sleep(1000);

		// Data type field
		Boolean DataType = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[2]/h3/span")).isEnabled();

		if (DataType) {
			System.out.println("DataType field is Enabled");
		} else {
			System.out.println("DataType field is not Enabled");
			driver.findElement(By.id("Data type disable")).click();
		}
		Thread.sleep(1000);

		// Time Period field
		Boolean TimePeriod = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[3]/h3/span")).isEnabled();

		if (TimePeriod) {
			System.out.println("Time Period field is Enabled");
		} else {
			System.out.println("Time Period field is not Enabled");
			driver.findElement(By.id("Time period disable")).click();
		}
		Thread.sleep(1000);

		// Fields
		Boolean Fields = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[4]/h3/span")).isEnabled();

		if (Fields) {
			System.out.println("Fields is Enabled");
		} else {
			System.out.println("Fields is not Enabled");
			driver.findElement(By.id("Fields disable")).click();
		}
		Thread.sleep(1000);

		// Group By
		Boolean GroupBy = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/h3/span")).isEnabled();

		if (GroupBy) {
			System.out.println("Group By is Enabled");
		} else {
			System.out.println("Group By is not Enabled");
			driver.findElement(By.id("Group by disable")).click();
		}
		Thread.sleep(1000);

		// Filters
		Boolean Filters = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[6]/h3/span")).isEnabled();

		if (Filters) {
			System.out.println("Filters is Enabled");
		} else {
			System.out.println("Filters is not Enabled");
			driver.findElement(By.id("Filters disable")).click();
		}
		Thread.sleep(1000);

		// View let Type
		Boolean ViewletType = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[7]/h3/span")).isEnabled();

		if (ViewletType) {
			System.out.println("Viewlet Type is Enabled");
		} else {
			System.out.println("Viewlet Type is not Enabled");
		}
		Thread.sleep(1000);
	}

	public void PreviewButton1() throws InterruptedException 
	{
		// Change Activity to  event
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[2]/div/span/span/span/span")).click();
		
		//Change Activity to Event
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
				System.out.println("Selecting Event");
				list.click();
				break;
			}
		}

		// Click on Preview
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[9]/div/button[2]")).click();
		Thread.sleep(8000);
		
		WebElement list=driver.findElement(By.id("viewlet-form-preview-wp")).findElement(By.className("ui-jqgrid-view"));
		List<WebElement> tds=list.findElements(By.tagName("div"));
		System.out.println("Number of divs are: " +tds.size());
		
		StringBuffer buffer=new StringBuffer();
		for(WebElement divs: tds)
		{
			if(divs.getAttribute("class").contains("ui-state-default")) 
			{
				WebElement listofth=divs.findElement(By.className("ui-jqgrid-hbox")).findElement(By.tagName("table")).findElement(By.tagName("thead")).findElement(By.tagName("tr"));
				List<WebElement> ths=listofth.findElements(By.tagName("th"));
				System.out.println("Number of colums are: " +ths.size());

				for(WebElement col: ths)
				{
						WebElement fin=col.findElement(By.tagName("div"));
						System.out.println("Value is: " +fin.getText());
						buffer.append(fin.getText());
				}
			}
		}
		
		System.out.println("buffer values are: " +buffer.toString());
		
		if (buffer.toString().contains("EventName")) {
			System.out.println("Preview button is working fine");
		} else {
			System.out.println("Preview button is not working");
			driver.findElement(By.id("Preview button disable")).click();
		}
		Thread.sleep(4000);
	}

	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		// System.out.println("dir: " + dir);
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
				// Update attachment to testrail server
				int testCaseID = 0;
				// int status=(int) result.getTestContext().getAttribute("Status");
				// String comment=(String) result.getTestContext().getAttribute("Comment");
				if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class)) {
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod()
							.getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();

					TestRailAPI api = new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());

				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
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
