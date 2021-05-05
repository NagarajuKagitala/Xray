package MenuBar;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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
public class MenuBar_UserSettings 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String TeamName;
	static String PreviousDefaultRepository;
	static int Dashboardscount=0;
	static String ViewletName="";
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		TeamName =Settings.getTeamName();
		PreviousDefaultRepository =Settings.getPreviousDefaultRepository();
	}
	
	//Login page
	@Test
	@Parameters({"sDriverPath", "sDriver", "OldDashboardName" })
	public void Login(String sDriverPath, String sDriver, String OldDashboardName) throws Exception {
		
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
		
		//Create dashboard
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=516)
	@Parameters({"Newusername", "userpwd", "usernewpwd"})
	@Test(priority=1)
	public void ChangePasswordFromMenu(String Newusername, String userpwd, String usernewpwd, ITestContext context) throws Exception
	{
		Settings.read();
		String sURL = Settings.getsURL();
		
		// Click on Admin settings
		driver.findElement(By.cssSelector(".icon")).click();
		driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);

		// Click on permissions
		driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
		Thread.sleep(2000);

		// Click on create new users
		driver.findElement(By.xpath("//li[contains(.,'Create New Users')]")).click();
		Thread.sleep(1000);

		// Click on next button
		driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/div[2]/button[2]")).click();
		Thread.sleep(1000);

		// Enter username
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys(Newusername);
		Thread.sleep(1000);

		// Enter firstname
		driver.findElement(By.id("firstName")).sendKeys("Test");
		Thread.sleep(1000);

		// Enter lastname
		driver.findElement(By.id("lastName")).sendKeys("User");
		Thread.sleep(1000);

		// Enter email
		driver.findElement(By.id("email")).sendKeys("user@gmail.com");
		Thread.sleep(1000);

		// Enter password
		driver.findElement(By.id("password")).sendKeys(userpwd);
		Thread.sleep(1000);

		// Enter confirm password
		driver.findElement(By.id("confirmPassword")).sendKeys(userpwd);
		Thread.sleep(4000);

		// Click on next button
		driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/div[2]/button[2]")).click();
		Thread.sleep(4000);

		// Search with team name
		driver.findElement(By.cssSelector(".input-search-field")).sendKeys(TeamName);

		// Select team
		driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[6]/main/aside[3]/div/div/div/table/tbody/tr/td/input")).click();
		Thread.sleep(4000);

		// Click on next button
		driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/div[2]/button[2]")).click();
		Thread.sleep(4000);

		// Click finish button
		driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
		Thread.sleep(8000);
		
		//Close the popup
		driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
		Thread.sleep(2000);

		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
		Thread.sleep(2000);
		
		this.Logout();
		
		//Login with user 
		driver.findElement(By.id("Uname")).sendKeys(Newusername);
		driver.findElement(By.id("PWD")).sendKeys(userpwd);
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
		
		try
		{
			driver.findElement(By.cssSelector("#createDashboard .alert-btn")).click();
		}
		catch (Exception e1)
		{
			System.out.println("create dashboard popup is not present");
		}
		
		//Mouse hour 
		driver.findElement(By.cssSelector(".icon")).click();
		
		//Click on Dash board and Select Save button
		driver.findElement(By.cssSelector(".hasSubMenu:nth-child(4)")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(".hasSubMenuOpen li:nth-child(2)")).click();
		Thread.sleep(6000);
		
		//Change the password
		driver.findElement(By.id("OLDPWD")).sendKeys(userpwd);
		driver.findElement(By.id("PWD")).sendKeys(usernewpwd);
		driver.findElement(By.id("CONFIRM")).sendKeys(usernewpwd);
		Thread.sleep(3000);
		
		//click on Submit button
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(6000);
		
		driver.get(sURL);
		
		//Login with user 
		driver.findElement(By.id("Uname")).sendKeys(Newusername);
		driver.findElement(By.id("PWD")).sendKeys(usernewpwd);
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
		
		try
		{
			driver.findElement(By.cssSelector("#createDashboard .alert-btn")).click();
		}
		catch (Exception e1)
		{
			System.out.println("create dashboard popup is not present");
		}
		Thread.sleep(2000);
		
		//Get the login username
		String LoginUsername=driver.findElement(By.id("loggedinUserName")).getText();
		System.out.println("Login user name is: " +LoginUsername);
		
		if(LoginUsername.equalsIgnoreCase(Newusername))
		{
			System.out.println("Change password is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Change password is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			this.Logout();
			Thread.sleep(2000);
			driver.findElement(By.id("Change password failed")).click();
		}
		
		this.Logout();
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=517)
	@Parameters({"OldDashboardName", "NewDashboardName"})
	@Test(priority=2)
	public void UpdateDashboardNameFromConfigureDashboard(String OldDashboardName, String NewDashboardName, ITestContext context) throws Exception
	{
		Settings.read();
		String sURL = Settings.getsURL();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();

		driver.get(sURL);
		
		//Login with user 
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(15000);
		
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
		
		CreateDashboard(OldDashboardName);
		
		this.ConfigureDashboard();
		Thread.sleep(2000);
		
		//Search with dashboard name 
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).clear();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).sendKeys(OldDashboardName);
		Thread.sleep(3000);
		
		//Click on Edit option
		driver.findElement(By.cssSelector(".edit-icon")).click();
		
		//Update dashboard with new name
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/div/div/div/table/tbody/tr/td/div/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/div/div/div/table/tbody/tr/td/div/input")).sendKeys(NewDashboardName);
		Thread.sleep(2000);
		
		//Save option the dashboard name
		driver.findElement(By.cssSelector(".saveIcon")).click();
		
		for(int i=0; i<=OldDashboardName.length(); i++)
		{
			driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Search with new name
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).sendKeys(NewDashboardName);
		Thread.sleep(4000);
		
		//Store the result value into string
		String DashName=driver.findElement(By.cssSelector(".enabled > td:nth-child(1)")).getText();
		System.out.println("dashboard Name:"+DashName);
		
		for(int i=0; i<=NewDashboardName.length(); i++)
		{
			driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).sendKeys(Keys.BACK_SPACE);
		}
		
		//Verification
		if(DashName.equalsIgnoreCase(NewDashboardName))
		{
			System.out.println("Dash baord name is updated");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Dash baord name is not updated");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Dashboard name update failed")).click();
		}
		Thread.sleep(3000);
	}
	
	
	@TestRail(testCaseId=518)
	@Parameters({"NewDashboardName"})
	@Test(priority=3)
	public void DeleteDashboardFromConfigureDashboard(String NewDashboardName, ITestContext context) throws InterruptedException
	{
		//Search with new name
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).sendKeys(NewDashboardName);
		Thread.sleep(2000);
		
		//Click on delete button
		driver.findElement(By.cssSelector(".del-btn")).click();
		
		//Click on Save button  
		driver.findElement(By.cssSelector("#configure-dashboards .float_right > .primary-btn")).click();
		Thread.sleep(1000);
		
		//Click on Confirmation Yes button
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(5000);
		
		//this.ConfigureDashboard();
		//Mouse hour
		driver.findElement(By.cssSelector(".fa-bars")).click();
		Thread.sleep(2000);

		//Click on Dash board and Select Save button
		//driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
		//Thread.sleep(5000);
		driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[5]/ul/li[2]")).click();
		Thread.sleep(6000);
		

		//Click on Dash board and Select Save button
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[5]")).click();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[5]/ul/li[4]")).click();
		//Thread.sleep(5000);
		
		//Search with new name
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/main/input")).sendKeys(NewDashboardName);
		
		//Store the List of dashboards into string
		String Dashboards=driver.findElement(By.cssSelector(".config-table")).getText();
		System.out.println("dashorddelete"+Dashboards);
		
		for(int k=0; k<=NewDashboardName.length(); k++)
		{
			driver.findElement(By.xpath("//main/input")).sendKeys(Keys.BACK_SPACE);
		}			
		
		if(Dashboards.contains(NewDashboardName))
		{
			System.out.println("Delete Dashboard failed");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			//Click on Close popup
			driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/footer/div/button")).click();
			driver.findElement(By.id("Delete dashboard failed")).click();
		}
		else
		{
			System.out.println("Dashboard is deleted");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		//Click on Close popup
		driver.findElement(By.xpath("//dialog[@id='configure-dashboards']/section/footer/div/button")).click();
		Thread.sleep(4000);
	}
	
	@TestRail(testCaseId=809)
	@Parameters({"OldDashboardName", "Query"})
	@Test(priority=4)
	public void SummaryConsoleCheckboxes(String OldDashboardName, String Query, ITestContext context) throws Exception
	{
		Settings.read();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
		LogoutAgain();
		LoginAgain(sUsername, sPassword);
		
		CreateDashboard(OldDashboardName);
		WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		System.out.println("Number of dashboards are: " +Dashboardscount);
		
		//Create summary viewlet
		CreateSummaryviewlet(Query);
		
		for(int k=0; k<=1; k++)
		{
		
		ConfigureManageSettings();
		
		//Check the On and Off options 
		boolean On=driver.findElement(By.name("manage-user-settings-popup-displaySummaryConsole")).isSelected();
		System.out.println("Summary console On checbox status is:" +On);
		
		if(On)
		{
			//select Off checkbox
			driver.findElement(By.xpath("//span/label[2]/input")).click();
		}
		else
		{
			//Select On checkbox
			driver.findElement(By.name("manage-user-settings-popup-displaySummaryConsole")).click();
		}
		
		//Click on save and Confirmation 
		driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer/button[2]")).click();
		Thread.sleep(4000);
		//close popup
		driver.findElement(By.cssSelector("#manage-user-settings-popup .alert-btn")).click();
		Thread.sleep(4000);  
		
		//Logout
		LogoutAgain();
		Thread.sleep(5000);
		
		//Login
		LoginAgain(sUsername, sPassword);
		
		try
		{
		List<WebElement> list=driver.findElements(By.className("tabs-title"));
		System.out.println(list.size());
		
		for(WebElement e : list)
		{
			System.out.println(e.getText());
			if(e.getText().equalsIgnoreCase(OldDashboardName))
			{
				e.click();
				break;
			}	
		}
		
		}
		catch (Exception e)
		{
			driver.findElement(By.id("failed")).click();
		}
		
		System.out.println("Boolean value later is:" +On);
		if(On)
		{
			System.out.println("off condition");
			OffOptionVerification(context);
		}
		else
		{
			System.out.println("on condition");
			OnOptionVerification(context);
		}
		}
		
	}
	
	@TestRail(testCaseId=810)
	@Test(priority=5)
	public void LandingPageCheckboxes(ITestContext context) throws Exception
	{
		Settings.read();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
		for(int k=0; k<=1; k++)
		{
		ConfigureManageSettings();
		
		//Check the On and Off options
		boolean LandingPageOn=driver.findElement(By.name("manage-user-settings-popup-displayLandingPage")).isSelected();
		System.out.println("Landing page On checkbox status is:" +LandingPageOn);
		
		if(LandingPageOn)
		{
			//select Off checkbox        
			driver.findElement(By.xpath("//div[3]/div[2]/span/label[2]/input")).click();
			System.out.println("Selected off checkbox");
			Thread.sleep(3000);
		}
		else
		{
			//Select On checkbox
			driver.findElement(By.name("manage-user-settings-popup-displayLandingPage")).click();
		}
		
		//Click on save and Confirmation
		driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer/button[2]")).click();
		Thread.sleep(4000);
		//close popup
		driver.findElement(By.cssSelector("#manage-user-settings-popup .alert-btn")).click();
		Thread.sleep(4000);
		
		//Logout
		LogoutAgain();
		
		//Login
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(25000);
		
		if(LandingPageOn)
		{
			boolean viewletbutton=driver.findElement(By.xpath("//div[4]/div[3]")).isDisplayed();
			System.out.println("Boolean value is: " +viewletbutton);
			
			if(viewletbutton==true)
			{
				System.out.println("Landing page Off is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Landing page Off is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.id("Landing page off failed")).click();
			}
		}
		else
		{
			//Get the landing page data               
			String Landingdata=driver.findElement(By.xpath("//div[3]/div/h2")).getText();
			System.out.println("landing page data is: " +Landingdata);
			
			if(Landingdata.equalsIgnoreCase("Go to Dashboard"))
			{
				System.out.println("Landing page On is working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
				driver.findElement(By.xpath("//div[3]/div/div[3]/div")).click();
				Thread.sleep(18000);
			}
			else
			{
				System.out.println("Landing page On is not working");
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				Thread.sleep(18000);
				driver.findElement(By.id("Landing page On failed")).click();
			}
			
		}
		}
	}
	
	@TestRail(testCaseId=811)
	@Parameters({"SchemaName", "ItemType"})
	@Test(priority=6)
	public void CreateSchema(String SchemaName, String ItemType, ITestContext context) throws Exception
	{
		Settings.read();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
		LogoutAgain();
		LoginAgain(sUsername, sPassword);
		
		ConfigureSchema();
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='schemas-wizard-popup']/section/footer/button[2]")).click();
		
		//Schema name
		driver.findElement(By.id("schema-label")).sendKeys(SchemaName);
		
		Select Item=new Select(driver.findElement(By.id("item-type")));
		Item.selectByVisibleText(ItemType);
		
		//Click on Add fields button
		driver.findElement(By.xpath("//div[3]/button")).click();
		Thread.sleep(3000);
		String[] Events= {"ActivityID", "CompCode", "CharSet"};
		
		for(int i=0; i<Events.length; i++)
		{
			System.out.println("Field name is: " +Events[i]);
			//Select item
			driver.findElement(By.xpath("//option[@value='EventName']")).click();
			
			//Select Right button
			driver.findElement(By.xpath("//div[2]/div[2]/div/button[2]")).click();
		}
		
		//Click on Apply changes button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(3000);
		
		//Click on Save button
		driver.findElement(By.xpath("//footer/button[3]")).click();
		Thread.sleep(6000);
		
		//Search with name
		driver.findElement(By.cssSelector(".filter-container > .input-field")).sendKeys(SchemaName);
		
		//Get the schema data into string
		String schemas=driver.findElement(By.cssSelector(".schema-row > td:nth-child(1)")).getText();
		System.out.println("Schema is :" +schemas);
		
		for(int k=0; k<=SchemaName.length(); k++)
		{
			driver.findElement(By.cssSelector(".filter-container > .input-field")).sendKeys(Keys.BACK_SPACE);
		}
		
		if(schemas.contains(SchemaName))
		{
			System.out.println("Schema is created successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Schema is not created");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "failed");
			driver.findElement(By.id("Create schema failed")).click();
		}
	}
	
	@TestRail(testCaseId=812)
	@Parameters({"SchemaName", "NewField"})
	@Test(priority=7)
	public void EditSchema(String SchemaName, String NewField, ITestContext context) throws InterruptedException
	{
		//Search with name
		driver.findElement(By.cssSelector(".filter-container > .input-field")).sendKeys(SchemaName);
		
		//Click on Edit icon
		driver.findElement(By.cssSelector(".fa-pencil-alt:nth-child(1)")).click();
		Thread.sleep(1000);     
		//Again Click on Edit icon
		driver.findElement(By.cssSelector(".fa-pencil-alt:nth-child(1)")).click();
		Thread.sleep(6000);
		
		//Add new item to list of objects
		driver.findElement(By.xpath("//option[contains(.,'"+ NewField +"')]")).click();
		
		//Select Right button
		driver.findElement(By.xpath("//div[2]/div[2]/div/button[2]")).click();
		
		//Click on Apply changes button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(3000);
		
		//Get the field values and store into string
		String Fields=driver.findElement(By.cssSelector("td:nth-child(2) > .teamsUserTableCell")).getText();
		System.out.println("Total fields are: " +Fields);
		
		//Click on Save button
		driver.findElement(By.xpath("//footer/button[3]")).click();
		Thread.sleep(6000);
		
		for(int k=0; k<=SchemaName.length(); k++)
		{
			driver.findElement(By.cssSelector(".filter-container > .input-field")).sendKeys(Keys.BACK_SPACE);
			
		}
		
		if(Fields.contains(NewField))
		{
			System.out.println("Schema updated successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Schema updated failed");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "failed");
			driver.findElement(By.id("Schema update failed")).click();
		}
	
	}
	
	@TestRail(testCaseId=813)
	@Parameters({"SchemaName"})
	@Test(priority=8)
	public void DeleteSchema(String SchemaName, ITestContext context) throws InterruptedException
	{
		//Search with name
		driver.findElement(By.cssSelector(".filter-container > .input-field")).clear();
		driver.findElement(By.cssSelector(".filter-container > .input-field")).sendKeys(SchemaName);
		
		//Click on Delete button
		driver.findElement(By.cssSelector(".fa-trash-alt")).click();
		
		//Click on confirmation
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(3000);
		
		//Get the schemas and store into string
		String Listofschemas=driver.findElement(By.xpath("//dialog[@id='schemas-wizard-popup']/section/main/div[2]/div/div")).getText();
		System.out.println("List of schemas are: " +Listofschemas);
		
		//Click on Close button
		driver.findElement(By.cssSelector("#schemas-wizard-popup > .close-button")).click();
		Thread.sleep(4000);
		
		if(Listofschemas.contains(SchemaName))
		{
			System.out.println("Delete Schema failed");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "failed");
			driver.findElement(By.id("Schema delete failed")).click();
		}
		else
		{
			System.out.println("Delete Schema is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
	}
	
	@TestRail(testCaseId=519)
	@Parameters({"DefaultRepository"})
	@Test(priority=9)
	public void DefaultRepository(String DefaultRepository, ITestContext context) throws Exception
	{
		Settings.read();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
		LogoutAgain();
		LoginAgain(sUsername, sPassword);
		
		/*
		 * //Mouse hour driver.findElement(By.xpath("//button/i")).click();
		 * Thread.sleep(6000);
		 * 
		 * //Click on Dash board and Select Save button
		 * driver.findElement(By.cssSelector(".hasSubMenu:nth-child(5)")).click();
		 * Thread.sleep(8000);
		 * 
		 * // driver.findElement(By.cssSelector(".hasSubMenu li:nth-child(6)")).click();
		 * 
		 * WebDriverWait wait = new WebDriverWait(driver,50);
		 * wait.until(ExpectedConditions.elementToBeClickable(By.
		 * cssSelector(".hasSubMenu li:nth-child(6)")));
		 * driver.findElement(By.cssSelector(".hasSubMenu li:nth-child(6)")).click();
		 * Thread.sleep(6000);
		 */
		
		ConfigureManageSettings();
		Thread.sleep(6000);
		
		driver.findElement(By.id("select2-ui-id-10-container")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span/input")).sendKeys(DefaultRepository);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		  
		//Click on save and Confirmation
		driver.findElement(By.cssSelector("#manage-user-settings-popup footer > .primary-btn")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector("#manage-user-settings-popup > .close-button > .fas")).click();
		Thread.sleep(4000);
		
		this.LogoutAgain();
		
		//Final confirmation ok button
		//driver.findElement(By.id("updatesDone")).click();
		Thread.sleep(3000);
		
		this.LoginAgain(sUsername, sPassword);
		
	try
		{
			driver.findElement(By.cssSelector("#createDashboard .alert-btn")).click();
		}
		catch (Exception e1)
		{
			System.out.println("create dashboard popup is not present");
		}
		Thread.sleep(2000);
		
		String Repo=driver.findElement(By.id("select2-ui-id-10-container")).getText();
		System.out.println("Data is:" +Repo);
		String GetRepository=driver.findElement(By.className("select2-selection__rendered")).getText();
		System.out.println("Repository name:" +GetRepository);
		
		if(DefaultRepository.contains(GetRepository))
		{
			System.out.println("Default repository is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Default repository is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			this.BackToDefaultRepository(sUsername, sPassword);
			driver.findElement(By.id("Repository option failed")).click();
		}
		//no data in global repo. click on popup cancel
		//driver.findElement(By.cssSelector("#createDashboard > .close-button")).click();
		Thread.sleep(2000);
		
		this.BackToDefaultRepository(sUsername, sPassword);
	}
	
	@TestRail(testCaseId=520)
	@Test(priority=10)
	public void PersonalToken(ITestContext context) throws InterruptedException
	{
		//Mouse hour
		driver.findElement(By.cssSelector(".fa-bars")).click();
		Thread.sleep(2000);

		//Click on Dash board and Select Save button
		driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
		Thread.sleep(5000);

		//select personal token option
		driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[5]/ul/li[5]")).click();
		Thread.sleep(4000);
				
		//Click on Generate token button
		driver.findElement(By.id("generate-token-btn")).click();
		Thread.sleep(5000);
		
		//Store the data into string
		String AlertMessage=driver.findElement(By.id("permissionsStatusBar")).getText();
		System.out.println("Results data is: " +AlertMessage);
		
		if(AlertMessage.equalsIgnoreCase("Token was created successfully"))
		{
			System.out.println("tokens is generated");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("tokens is not generated");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.cssSelector("#repository-tokens-popup > .close-button > .fas")).click();
			Thread.sleep(2000);
			driver.findElement(By.id("personal token failed")).click();
		}
		Thread.sleep(5000);
				
		//Close the popup page
		driver.findElement(By.xpath("//dialog[@id='repository-tokens-popup']/button/i")).click();
		Thread.sleep(2000);
	}
	
	@Test(priority=11)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	@Test(priority=12)
	public void CloseBrowser()
	{
		// Closing the browser
		driver.close();
	}
	
	public void ConfigureDashboard() throws InterruptedException 
	{
		//Mouse hour
		driver.findElement(By.cssSelector(".fa-bars")).click();
		Thread.sleep(2000);

		//Click on Dash board and Select Save button
		driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[5]/ul/li[2]")).click();
		Thread.sleep(6000);
	}
	
	public void ConfigureSchema() throws InterruptedException
	{
		//Mouse hour
		driver.findElement(By.cssSelector(".fa-bars")).click();
		Thread.sleep(2000);

		//Click on Dash board and Select Save button
		driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[5]/ul/li[3]")).click();
		Thread.sleep(6000);
		
	}
	
	public void ConfigureManageSettings() throws InterruptedException
	{
		//Mouse hour
		driver.findElement(By.cssSelector(".fa-bars")).click();
		Thread.sleep(2000);

		//Click on Dash board and Select Save button
		driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
		Thread.sleep(6000);
		
		driver.findElement(By.xpath("//label/span")).click();
		Thread.sleep(6000);
	}
	
	public void CreateDashboard(String OldDashboardName) throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.cssSelector(".add-tab-inner > .tabs-title")).click();
		Thread.sleep(5000);
		
		//Give the dashboard Name
		driver.findElement(By.cssSelector(".edit-name")).sendKeys(OldDashboardName);
		
		//uncheck initial viewlet checkbox 
		driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);
		
		//Click on Create button  
		driver.findElement(By.cssSelector("#create-dashboard .primary-btn")).click();
		Thread.sleep(2000);
	}
	
	public void LoginAgain(String sUsername, String sPassword) throws InterruptedException
	{
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(25000);
		
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
	
	public void BackToDefaultRepository(String sUsername, String sPassword) throws InterruptedException
	{
		ConfigureManageSettings();
		
		// Click on Dropdown
		driver.findElement(By.id("select2-ui-id-10-container")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span/input")).sendKeys(PreviousDefaultRepository);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		
		//Click on save and Confirmation
		driver.findElement(By.cssSelector("#manage-user-settings-popup footer > .primary-btn")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector("#manage-user-settings-popup > .close-button > .fas")).click();
		Thread.sleep(4000);
				
		this.Logout();
		
		this.LoginAgain(sUsername, sPassword);
	}
	
	public void LogoutAgain()
	{
		//Click on Logout icon
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		try
		{
			driver.findElement(By.id("logoutSaveBtn")).click();
			Thread.sleep(2000);
		}
		catch (Exception e)
		{
			driver.findElement(By.id("logoutYESBtn")).click();
		}
	}
	
	public void CreateSummaryviewlet(String Query) throws InterruptedException
	{
		//Click on + button  
		driver.findElement(By.xpath("//div[4]/div[3]")).click();
		
		boolean BasicSummary=driver.findElement(By.name("create-viewlet-open-close-option")).isSelected();
		
		if(BasicSummary)   
		{
			System.out.println("Basic summary Radio button is already selected");
		}
		else
		{
			driver.findElement(By.name("create-viewlet-open-close-option")).click();
		}
		Thread.sleep(1000);
		
		//Click on Create button
		driver.findElement(By.id("createViewletBtn")).click();
		
		//Enter the query into Define query field 
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet']/section/main/div/jkql-input/textarea")).sendKeys(Query);
		Thread.sleep(2000);
		
		//Get viewlet name
		ViewletName=driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet']/section/main/div[2]/input")).getAttribute("value");
		System.out.println("ViewletName:"+ViewletName);
		
		//Click on Create button
		driver.findElement(By.xpath("//footer/button[2]")).click();
		Thread.sleep(2000);
		
	}
	
	public void OnOptionVerification(ITestContext context) throws InterruptedException
	{
		Thread.sleep(4000);
		//Summary viewlet data
		String summary=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).getText();
		System.out.println("Summary viewlet data is: " +summary);
				
		if(summary.contains("Count"))
		{
			System.out.println("Summary viewlet On option is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else 
		{
			System.out.println("Summary viewlet On option is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Summary viewlet On failed")).click();
		}
	}
	
	public void OffOptionVerification(ITestContext context) throws InterruptedException
	{
		Thread.sleep(4000);
		
		boolean value=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).isDisplayed();
		System.out.println("Output is: " +value);
		
		/*String summary=driver.findElement(By.id("mCSB_"+ Dashboardscount +"_container")).getText();
		System.out.println("Summary viewlet data is: " +summary);*/
		
		/*//Viewlet data                                       
		String Viewletdata=driver.findElement(By.cssSelector(".ui-layout-pane-hover > .column-container")).getText();
		System.out.println("Viewlet data is: " +Viewletdata);*/
		
		if(value==true)
		{
			System.out.println("Summary viewlet Off option is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Summary viewlet off failed")).click();
		}
		else
		{
			System.out.println("Summary viewlet Off option is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
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
