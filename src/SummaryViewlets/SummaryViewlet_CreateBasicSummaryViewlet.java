package SummaryViewlets;

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
public class SummaryViewlet_CreateBasicSummaryViewlet 
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
		//   options.setCapability("marionette", false);
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
		Thread.sleep(20000);
		
        //Check Landing page 
		if(driver.getPageSource().contains("Go to Dashboard"))
		{
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(25000);
		}
		else
		{
			System.out.println("Landing page is not present");
			Thread.sleep(25000);
		}
		
		//Create a Dashboard
		obj.CreateDashboard(driver, DashboardName);
		Thread.sleep(2000);
		
		WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		
	}
	
	@TestRail(testCaseId=479)
	@Parameters({"Query"})
	@Test(priority=1)
	public void CreateSummaryViewlet(String Query, ITestContext context) throws InterruptedException
	{         
		System.out.println("Count is: " +Dashboardscount);
		
		//Click on + button 
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[4]/div[2]/button")).click();
		
		boolean BasicSummary=driver.findElement(By.name("create-summary-viewlet-open-close-option")).isSelected();
		
		if(BasicSummary)
		{
			System.out.println("Basic summary Radio button is already selected");
		}
		else
		{
			driver.findElement(By.name("create-summary-viewlet-open-close-option")).click();
		}
		Thread.sleep(1000);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='create-summary-viewlet']/section/footer/div[2]/button")).click();
		
		//Enter the query into Define query field 
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet-summary']/section/main/div/jkql-input/textarea")).sendKeys(Query);
		Thread.sleep(2000);
		
		//Get viewlet name
		String ViewletName=driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet-summary']/section/main/div[2]/input")).getAttribute("value");
		System.out.println("ViewletName:"+ViewletName);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet-summary']/section/footer/button[2]")).click();
		Thread.sleep(2000);
		
		String GetViewletName=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div/div")).getAttribute("title");
		System.out.println("Viewlet name from option:" +GetViewletName);      
		
		//verification
		if(GetViewletName.equalsIgnoreCase(ViewletName))
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
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=480)
	@Test(priority=2)
	public void VerifyRecordsCount(ITestContext context) throws InterruptedException
	{
		//Store the records into string                          
		//String Records=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div[2]/div[2]/div/ul/li/ul/li/div[2]/div[2]")).getText();
		//System.out.println("Count at viewlet: " +Records);       
		//Thread.sleep(4000);
		
		String All=driver.findElement(By.xpath("//div[@id='mCSB_"+ Dashboardscount +"_container']/ul/li")).getText();
		System.out.println("total data: " +All);
		
		int i;
		for(i = 0; i < All.length(); i++){
		    char c = All.charAt(i);
		    if( '0' <= c && c <= '9' )
		        break;
		}
		String alphaPart = All.substring(0, i);
		String numberPart = All.substring(i);
		
		//System.out.println("Values o: "+alphaPart);
		System.out.println("number o: "+numberPart);
		
		driver.findElement(By.xpath("//div[2]/div/div/div/div/div/div/ul/li/div[2]/div[2]/div/ul/li/div/div")).click();
		Thread.sleep(5000);
		
		System.out.println("Dashboard count is: " +Dashboardscount);
		//Click on Count
		//driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div[2]/div[2]/div/ul/li/ul/li/div[2]/div[2]")).click();
		//Thread.sleep(6000);
		
		//Get the count in the console page
		String ConsoleCount=driver.findElement(By.xpath("//div/div[2]/div[2]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
		System.out.println("Count at console: " +ConsoleCount);
		
		if(ConsoleCount.contains(numberPart))
		{
			System.out.println("Records count is matched");
			 context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Records count is not matched");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Records count not matched")).click();
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=481)
	@Parameters({"ConsoleQuery"})
	@Test(priority=3)
	public void ObservationOfQuery(String ConsoleQuery, ITestContext context) throws InterruptedException
	{
		//Get the Query data and store into string 
		String Query=driver.findElement(By.xpath("//div/div/div/div/jkql-input/textarea")).getAttribute("value");
		System.out.println("Get query from console: " +Query);
		
		if(Query.equalsIgnoreCase(ConsoleQuery))
		{
			System.out.println("Console Query is fine");
			 context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Console Query is not fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query Mismatch")).click();
		}
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=482)
	@Parameters({"TempVielwetName"})
	@Test(priority=4)
	public void TemporaryViewlet(String TempVielwetName, ITestContext context) throws InterruptedException
	{
		//Click on + button
		driver.findElement(By.cssSelector(".ui-layout-resizer-south-open .primary-btn")).click();
		Thread.sleep(1000);
		
		//Store the Temporary viewlet name into string
		String TempVielwet=driver.findElement(By.xpath("//span[contains(.,'Temporary viewlet')]")).getText();
		System.out.println("Temp viewlet name is: " +TempVielwet);
		
		if(TempVielwet.equalsIgnoreCase(TempVielwetName))
		{
			System.out.println("Temporary viewlet created");
			context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Temporary viewlet is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Temp viewlet failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=483)
	@Parameters({"TemporaryQuery"})
	@Test(priority=5)
	public void QueryInTemporaryViewlet(String TemporaryQuery, ITestContext context) throws InterruptedException
	{
		//Enter the Query in Temporary viewlet
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(TemporaryQuery);
		driver.findElement(By.xpath("//div[2]/div/div/div/div/jkql-input/textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(6000);
		
		//Store the Entered query into string
		String Eventid=driver.findElement(By.xpath("//div[2]/div/div[2]/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[3]")).getText();
		System.out.println("Field name is: " +Eventid);
		
		if(Eventid.equalsIgnoreCase(" EventID"))
		{
			System.out.println("Query is entered to temporary viewlet");
			context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Query is not entered to temporary viewlet");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query entered failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=484)
	@Test(priority=6)
	public void ConsoleTabFunctionality(ITestContext context) throws InterruptedException
	{
		//Click on Console tab
		driver.findElement(By.cssSelector(".ui-layout-toggler-south-open")).click();
		Thread.sleep(2000);
		
		boolean Temp=driver.findElement(By.xpath("//a[contains(.,'Temporary viewlet')]")).isDisplayed();
		System.out.println(Temp);
		
		if(Temp)
		{
			System.out.println("Console page is not closed");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Console button failed")).click();
		}
		else
		{
			System.out.println("Console page is closed");
			context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=485)
	@Parameters({"NewQuery"})
	@Test(priority=7)
	public void EditSummaryViewletQuery(String NewQuery, ITestContext context) throws InterruptedException
	{
		//Click on Edit Query button  
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[2]/i")).click();
		Thread.sleep(3000);

		//Edit the Query
		WebElement toClear = driver.findElement(By.xpath("//dialog[@id='summary-query-edit-popup']/section/div/jkql-input/textarea"));
		toClear.sendKeys(Keys.CONTROL + "a");
		toClear.sendKeys(Keys.DELETE);
	//	driver.findElement(By.cssSelector(".inputWithjKQL")).sendKeys(Keys.BACK_SPACE);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//dialog[@id='summary-query-edit-popup']/section/div/jkql-input/textarea")).sendKeys("Get count of Event Show as summary");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//dialog[@id='summary-query-edit-popup']/section/div/jkql-input/textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		//Store the summary viewlet data
		String SummaryName=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div[2]/div[2]/div/ul/li/div/div")).getText();
		System.out.println("Summary viewlet query name: " +SummaryName);
		
		if(SummaryName.equalsIgnoreCase("Events Count"))
		{
			System.out.println("Summary query is Edited");
			context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Summary query is not Edited");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Edit summary query failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=486)
	@Parameters({"NewViewletName"})
	@Test(priority=8)
	public void EditOptionInSummaryViewlet(String NewViewletName, ITestContext context) throws InterruptedException
	{
		//Click on Viewlet Menu
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
		//driver.findElement(By.xpath("//div/div/div/div[2]/div[7]/i")).click();
		Thread.sleep(2000);          
		
		//Select Edit viewlet option
		driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
		Thread.sleep(4000);
		
		this.ButtonsAndStatus();
		this.RightSidePannel();
		
		//Update the viewlet name  
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).sendKeys(NewViewletName);
		
		//Click on Apply button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(3000);
		System.out.println("Viewlet name updated");
		WebElement ele=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).findElement(By.tagName("div")).findElement(By.tagName("div")).findElement(By.tagName("div"));
		//Get the viewlet name and stored into string
		List<WebElement> NewName=ele.findElements(By.tagName("div"));
		
		for(WebElement ss : NewName)
		{
			/*System.out.println(ss.getText());*/
			System.out.println("Attribute list: " +ss.getAttribute("class"));
			if(ss.getAttribute("class").equalsIgnoreCase("summary-viewlet-title"))
			{
				String ss1=driver.findElement(By.className("summary-viewlet-title")).getText();
				System.out.println(ss1);
				break;
			}
		}
	}
	
	@TestRail(testCaseId=487)
	@Test(priority=9)
	public void SaveSummaryViewlet(ITestContext context) throws InterruptedException
	{
		//Click on Viewlet Menu
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
		Thread.sleep(3000);
		
		WebElement Save=driver.findElement(By.xpath("//li[contains(.,'Save Viewlet')]"));
		
		String isEnabled = Save.getAttribute("class");
		//System.out.println(isEnabled);
		
		if (!isEnabled.equals("disabled"))
		{
			System.out.println("Save option is Enabled");
			Save.click();
			Thread.sleep(2000);
			
			//Store the success message into string
			String Msg=driver.findElement(By.cssSelector(".message-main")).getText();
			System.out.println("Save popup data is: " +Msg);
			
			//Click on Confirmation OK button
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
			
			if(Msg.contains("successfully"))
			{
				System.out.println("Save option is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Save option is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.id("Save Option failed")).click();
			}
			
			//Click on ViewletMenu
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
			Thread.sleep(2000);
		}
		else
		{
			System.out.println("Save option is Disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
	}
	
	@TestRail(testCaseId=488)
	@Parameters({"SaveAsViewletName"})
	@Test(priority=10) //dependsOnMethods = { "SaveSummaryViewlet" })
	public void SaveASSummaryViewlet(String SaveAsViewletName, ITestContext context) throws InterruptedException
	{		
		WebElement SaveAs=driver.findElement(By.xpath("//li[contains(.,'Save As Viewlet')]"));
		
		String isEnabled = SaveAs.getAttribute("class");
		//System.out.println(isEnabled);
		
		if (!isEnabled.equals("disabled"))
		{
			System.out.println("Save As option is Enabled");
			SaveAs.click();
			Thread.sleep(1000);
			
			//Enter the save as viewlet name
			driver.findElement(By.xpath("//dialog[@id='save-as-viewlet']/section/main/div/input")).clear();
			driver.findElement(By.xpath("//dialog[@id='save-as-viewlet']/section/main/div/input")).sendKeys(SaveAsViewletName);
			
			//Click on Confirmation ok button
			driver.findElement(By.xpath("//dialog[@id='save-as-viewlet']/section/footer/div[2]/button")).click();
			Thread.sleep(6000);
			
			//WebElement summary=driver.findElement(By.className("summary-panel")).findElement(By.tagName("ul")).findElement(By.tagName("li"));
			
			WebElement summary=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).findElement(By.tagName("ul"));
			
			List<WebElement> Divs=summary.findElements(By.tagName("li"));
			//System.out.println("No of divs are: " +Divs.size());
			StringBuilder buffer = new StringBuilder();
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
			
			String Data=buffer.toString();
			//String Data=summary.getText();
			//Store the viewlets data 
			//String Data=driver.findElement(By.xpath("//div[3]/div/ul/li/div")).getText();
			System.out.println("Summary viewlet data is: " +Data);
			
			if(Data.contains(SaveAsViewletName))
			{
				System.out.println("Save As Option is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Save As Option is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				//Click on ViewletMenu
				driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
				Thread.sleep(2000);
				driver.findElement(By.id("Save As Option failed")).click();
			}
			Thread.sleep(2000);
			
			//Click on ViewletMenu
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
			Thread.sleep(2000);
		}
		else
		{
			System.out.println("Save As Option is disabled state");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=489)
	@Parameters({"NewQuery", "Query"})
	@Test(priority=11) //, dependsOnMethods = { "SaveASSummaryViewlet" })
	public void RemoveSummaryViewlet(String NewQuery, String Query, ITestContext context) throws InterruptedException
	{
		WebElement Remove=driver.findElement(By.xpath("//li[contains(.,'Remove Viewlet')]"));
		
		String isEnabled = Remove.getAttribute("class");
		//System.out.println(isEnabled);
		
		if (!isEnabled.equals("disabled"))
		{
			System.out.println("Remove option is Enabled");
			Remove.click();
			Thread.sleep(1000);
			
			//Click on Confirmation ok button
			driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
			Thread.sleep(2000);
			
			//Store the viewlets data
			String Data=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).getText();
			System.out.println("Summary data is: " +Data);
			
			if(Data.contains(NewQuery))
			{
				System.out.println("Viewlet is not removed");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				//Click on ViewletMenu
				driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
				Thread.sleep(2000);
				driver.findElement(By.id("Remove Option failed")).click();
			}
			else
			{
				System.out.println("Viewlet is removed successfully");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
				this.CreateViewlet(Query);
			}
			
			//Click on ViewletMenu
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
			Thread.sleep(2000);
			
		}
		else
		{
			System.out.println("Remove viewlet option is disabled state");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);
	}
	
	@TestRail(testCaseId=490)
	@Parameters({"Query"})
	@Test(priority=12) //, dependsOnMethods = { "RemoveSummaryViewlet" })
	public void DeleteSummaryViewlet(String Query, ITestContext context) throws InterruptedException
	{
		WebElement Delete=driver.findElement(By.xpath("//li[contains(.,'Delete Viewlet')]"));
		
		String isEnabled = Delete.getAttribute("class");
		//System.out.println(isEnabled);
	
		if (!isEnabled.equals("disabled"))
		{
			System.out.println("Delete option is Enabled");
			Delete.click();
			Thread.sleep(1000);
			
			//Click on Confirmation ok button
			driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
			Thread.sleep(1000);
			
			//Store the viewlets data
			//String Data=driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div[2]/div[1]/ul/li/div")).getText();
			String Data=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).getText();
			System.out.println("Summary console data is: " +Data);
			
			if(Data.contains(Query))
			{
				System.out.println("Viewlet is not Deleted");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				//Click on ViewletMenu
				driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
				Thread.sleep(2000);
				driver.findElement(By.id("Delete Option failed")).click();
			}
			else
			{
				System.out.println("Viewlet is Deleted successfully");
				 context.setAttribute("Status",1);
				 context.setAttribute("Comment", "working fine");
				this.CreateViewlet(Query);				
			}
			Thread.sleep(2000);
			
			//Click on ViewletMenu
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div/div/div/div/div/div/ul/li/div/div[2]/div/button[6]/i")).click();
			Thread.sleep(2000);
		}
		else
		{
			System.out.println("Delete viewlet option is disabled state");
			 context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);
	}
	@TestRail(testCaseId=491)
	@Test(priority=13) //,dependsOnMethods = { "DeleteSummaryViewlet" })
	public void ExportSummaryViewlet(ITestContext context) throws InterruptedException
	{
		
		
		WebElement Export=driver.findElement(By.xpath("//li[contains(.,'Export Viewlet')]"));
		
		String isEnabled = Export.getAttribute("class");
		System.out.println(isEnabled);
	
		if (!isEnabled.equals("disabled"))
		{
			System.out.println("Export option is Enabled");
			context.setAttribute("Status",1);
		    context.setAttribute("Comment", "working fine");
		    
			Export.click();
			Thread.sleep(4000);
		}
		else
		{
			System.out.println("Export viewlet option is disabled state");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);
		
		}	
		
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
		
	}
	
	public void CreateViewlet(String Query) throws InterruptedException
	{
		System.out.println("Count is: " +Dashboardscount);
		
		//Click on + button 
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[4]/div[2]/button")).click();
		
		boolean BasicSummary=driver.findElement(By.name("create-summary-viewlet-open-close-option")).isSelected();
		
		if(BasicSummary)
		{
			System.out.println("Basic summary Radio button is already selected");
		}
		else
		{
			driver.findElement(By.name("create-summary-viewlet-open-close-option")).click();
		}
		Thread.sleep(1000);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='create-summary-viewlet']/section/footer/div[2]/button")).click();
		
		//Enter the query into Define query field 
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet-summary']/section/main/div/jkql-input/textarea")).sendKeys(Query);
		Thread.sleep(2000);
		
		//Get viewlet name
		String ViewletName=driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet-summary']/section/main/div[2]/input")).getAttribute("value");
		System.out.println("ViewletName:"+ViewletName);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet-summary']/section/footer/button[2]")).click();
		Thread.sleep(2000);
		
	}
		
	public void CreateDashboard() throws InterruptedException
	{
		// Click on Plus Icon
		driver.findElement(By.xpath("//div[@id='pageContainer-tabs-add']/div/div/span")).click();

		// Give the dashboard Name
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/input")).sendKeys("BasicQuerySummary");

		// uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);

		// select two columns 
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();

		// Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
	}
	
	public void ButtonsAndStatus() throws InterruptedException
	{
		//Close button
		Boolean Close=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[10]/button")).isEnabled();
		
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
		
		//Apply button
		Boolean Create=driver.findElement(By.xpath("//button[contains(.,'Apply')]")).isEnabled();
		
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
		
		//Preview button
		Boolean Apply=driver.findElement(By.xpath("//button[contains(.,'Preview')]")).isEnabled();
		
		if(Apply)
		{
			System.out.println("Apply button is Enabled");
		}
		else
		{
			System.out.println("Apply button is not Enabled");
			driver.findElement(By.id("Apply button disable")).click();
		}
		Thread.sleep(1000);
		
	}
	
	
	public void RightSidePannel() throws InterruptedException
	{
		//Viewlet Name field                          
		Boolean Viewlet=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/h3/span")).isEnabled();
		
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
		Boolean DataType=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[2]/h3/span")).isEnabled();
		
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
		Boolean TimePeriod=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[3]/h3/span")).isEnabled();
		
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
