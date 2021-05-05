package AdminSettings;

import java.io.File;
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

import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class AdminSettings_AlertsTab 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String Provider="";
	
	static String UserName;
	static String Password;
	static String MailServer;
	static String PortNumber;
	static String MailFrom;
	static String MailTo;
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		UserName =Settings.getUserName();
		Password =Settings.getPassword();
		MailServer =Settings.getMailServer();
		PortNumber =Settings.getPortNumber();
		MailFrom =Settings.getMailFrom();
		MailTo =Settings.getMailTo();
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
		
		//Click on Admin settings
		CommonElements.MenubarIcon(driver);
		CommonElements.AdminSettingsOption(driver);
		//driver.findElement(By.cssSelector(".icon")).click();
		//driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);
		
		//click on Alerts tab
		CommonElements.AlertsOption(driver);
		//driver.findElement(By.xpath("//li[contains(.,'Alerts')]")).click();
		Thread.sleep(4000);
	}
	
	//------------ Create an Alert Tab ----------------
	@Parameters({"AlertName", "ActionName", "UserName", "Password", "MailServer", "MailFrom", "MailTo"})
	@TestRail(testCaseId=382)
	@Test(priority=1)
	public void CreateAlert(String AlertName, String ActionName, String UserName, String Password, String MailServer, String MailFrom, String MailTo, ITestContext context) throws InterruptedException
	{	
		this.Alertstab();
		
		//Alert name
		driver.findElement(By.id("trigger-name")).sendKeys(AlertName);
			
		String TriggerTemp=driver.findElement(By.id("trigger-template")).getText();
		System.out.println("Action dropdown: " +TriggerTemp);
		Thread.sleep(2000);
		
		if(TriggerTemp.contains(""))
		{
			//Click on Create New button
			driver.findElement(By.xpath("//button[contains(.,'Create new')]")).click();
			Thread.sleep(2000);
			
			//Action Provider name
			driver.findElement(By.id("provider-name")).sendKeys(ActionName);
			
			//Send User name
			driver.findElement(By.id("user-name")).clear();
			driver.findElement(By.id("user-name")).sendKeys(UserName);
			
			//send password
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(Password);
			
			//Mail server
			driver.findElement(By.id("server")).sendKeys(MailServer);
			
			//From mail id
			driver.findElement(By.id("mail-from")).sendKeys(MailFrom);
			Thread.sleep(3000);
			
//			Select ToMail=new Select(driver.findElement(By.xpath("//li/input")));
//			Thread.sleep(2000);
//			ToMail.selectByVisibleText(MailTo);
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("//span/input")).sendKeys(MailTo);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ARROW_DOWN);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ENTER);
			Thread.sleep(6000);
			
			//click on Create
			driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[4]")).click();
			Thread.sleep(4000);
		}
		 
		//Mail to option select
//		Select Trigger=new Select(driver.findElement(By.xpath("//li/input")));
//		Trigger.selectByVisibleText(MailTo);
//		Thread.sleep(2000);
		driver.findElement(By.xpath("//span/input")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span/input")).sendKeys(MailTo);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ARROW_DOWN);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ENTER);
		
		//Click on Finish button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[3]")).click();
		Thread.sleep(4000);
		
		String Alertsdata=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div[2]/div")).getText();
		System.out.println("Alert names are: " +Alertsdata);
		
		if(Alertsdata.contains(AlertName))
		{
			System.out.println("Alert is added successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Alert is not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		
		Thread.sleep(2000);
	}
	@TestRail(testCaseId=383)
	@Test(priority=2)
	public void ActionTypeCheckboxInAlertTab(ITestContext context) throws InterruptedException
	{
		this.Alertstab();
		
		//Action type check box verification
		Boolean Action=driver.findElement(By.name("action-type")).isSelected();
		
		if(Action == true)
		{
			System.out.println("Action Type checkbox is already selected");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Action Type checkbox is not selected");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("ActionType check box not selected")).click();
		}
		Thread.sleep(1000);
	}
	@TestRail(testCaseId=384)
	@Test(priority=3)
	public void BrowseAvailablePlaceholderLinkInAlertTab(ITestContext context) throws InterruptedException
	{
		//Click on the Browse available placeholder link
		driver.findElement(By.linkText("Browse available placeholders")).click();
		Thread.sleep(1000);
		
		//Store the place holder name into string
		String Placeholdername=driver.findElement(By.xpath("//dialog[@id='placeholders-popup']/section/div/div/div/table/thead/tr/th")).getText();
		System.out.println(Placeholdername);
		
		if(Placeholdername.equalsIgnoreCase("Placeholder"))
		{
			System.out.println("Place holder page is opened");
			 context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
			
		}
		else
		{
			System.out.println("Place holder page is not opened");
			//Close the popup page
			if(driver.findElement(By.cssSelector("#placeholders-popup > .close-button")).isDisplayed())
			{
				driver.findElement(By.cssSelector("#placeholders-popup > .close-button")).click();
			}
			Thread.sleep(1000);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Place holder page not opened")).click();
		}
		
		//Close the Available placeholders popup page 
		if(driver.findElement(By.cssSelector("#placeholders-popup > .close-button")).isDisplayed())
		{
			driver.findElement(By.cssSelector("#placeholders-popup > .close-button")).click();
		}
		Thread.sleep(1000);
	}
	@TestRail(testCaseId=385)
	@Test(priority=4)
	public void SetDefaultValuesInAlertTab(ITestContext context)
	{
		//Click on Set default values button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div[3]/div[4]/button")).click();
	}
	
	@TestRail(testCaseId=386)
	@Test(priority=5)
	public void TestAndFinishButtonsVerificationBeforEnterTheName(ITestContext context) throws InterruptedException
	{
		//Verify Test button                
		boolean Test=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[2]")).isEnabled();
		System.out.println(Test);
		
		if(Test == true)
		{
			System.out.println("Test button is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Test button is Disable");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
			
		}
		Thread.sleep(1000);
		
		//Verify Finish button
		boolean Finish=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[3]")).isEnabled();
		System.out.println(Finish);
		
		if(Finish == true)
		{
			System.out.println("Finish button is Enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Finish button failed")).click();
		}
		else
		{
			System.out.println("Finish button is Disable");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);		
	}
	
	@Parameters({"AlertName1", "MailTo"})
	@TestRail(testCaseId=387)
	@Test(priority=6)
	public void TestAndFinishButtonsVerificationAfterEnterTheName(String AlertName1, String MailTo,ITestContext context) throws InterruptedException
	{   
		//Alert name
		driver.findElement(By.id("trigger-name")).sendKeys(AlertName1);
		
		//Mail to option select
		/*
		 * Select Trigger=new Select(driver.findElement(By.id("trigger-mail-to")));
		 * Trigger.selectByVisibleText(MailTo); Thread.sleep(3000);
		 */
		
		driver.findElement(By.xpath("//span/input")).sendKeys(MailTo);
		driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);
		
		//Verify Test button
		boolean Test=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[2]")).isEnabled();
		System.out.println(Test);
		
		if(Test == true)
		{
			System.out.println("Test button is Enabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
			
		}
		else
		{
			System.out.println("Test button is Disable");
			driver.findElement(By.id("Test button failed")).click();
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
		
		//Verify Finish button
		boolean Finish=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[3]")).isEnabled();
		System.out.println(Finish);
		
		if(Finish == true)
		{
			System.out.println("Finish button is Enabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
			
		}
		else
		{
			System.out.println("Finish button is Disable");
			driver.findElement(By.id("Finish button failed")).click();
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(4000);
	}
	
	//-----------------  Create Action tab -------------------------
	@Parameters({"ActionNameinCreatePage"})
	@TestRail(testCaseId=388)
	@Test(priority=7)
	public void TestButtonFunctionalityVerification(String ActionNameinCreatePage, ITestContext context) throws InterruptedException
	{
		//Click on Test option for went to create an action tab
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div[2]/div[2]/button")).click();
				
		//Action Provider name
		driver.findElement(By.id("provider-name")).sendKeys(ActionNameinCreatePage);
		
		//Send User name
		driver.findElement(By.id("user-name")).clear();
		driver.findElement(By.id("user-name")).sendKeys(UserName);
		
		//send password
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(Password);
		
		//Mail server
		driver.findElement(By.id("server")).sendKeys(MailServer);
		
		//Port number
		driver.findElement(By.id("port")).clear();
		driver.findElement(By.id("port")).sendKeys(PortNumber);
		
		//From mail id
		driver.findElement(By.id("mail-from")).sendKeys(MailFrom);
		
		/*
		 * Select ToMail=new Select(driver.findElement(By.id("mail-to")));
		 * ToMail.selectByVisibleText(MailTo); Thread.sleep(2000);
		 */
		
		driver.findElement(By.xpath("//span/input")).sendKeys(MailTo);
		driver.findElement(By.xpath("//span/input")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		//click on Test button        
		driver.findElement(By.xpath("//button[contains(.,'Test')]")).click();
		Thread.sleep(15000);
		
		String MessageData=driver.findElement(By.cssSelector(".message")).getText();
		System.out.println(MessageData);
		
		if(MessageData.contains("Failed"))
		{
			System.out.println("Mail is not sent");
			//Close the popup
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
	
			//Fail the test case
			driver.findElement(By.id("Test button functionality failed")).click();
			
		}
		else
		{
			System.out.println("Mail is sent and Test button is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");	
			//Close the popup
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
			Thread.sleep(1000);
		}
	}
	
	@Parameters({"AlertName"})
	@TestRail(testCaseId=389)
	@Test(priority=8)
	public void CreateButtonFunctinalityVerification(String AlertName,ITestContext context) throws InterruptedException
	{
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[4]")).click();
		Thread.sleep(6000);
		
		//Click on Finish button
		driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
		
		driver.findElement(By.cssSelector(".info-data:nth-child(2)")).clear();
		driver.findElement(By.cssSelector(".info-data:nth-child(2)")).sendKeys(AlertName);
		
		//Verification of manage alerts page
		String ListOfAlerts=driver.findElement(By.xpath("//aside/div[2]/div")).getText();
		System.out.println("List of Alerts are:" +ListOfAlerts);
		
		
		if(ListOfAlerts.contains(AlertName))
		{
			System.out.println("Create button is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
			
		}
		else
		{
			System.out.println("Create button is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Create button failed")).click();
		}
		Thread.sleep(1000);
	}
	
	//----------------- Manage Actions Tab ---------------------
	@Parameters({"ActionNameinCreatePage"})
	@TestRail(testCaseId=390)
	@Test(priority=9)
	public void SerachInManageActionsTab(String ActionNameinCreatePage,ITestContext context) throws InterruptedException
	{
		//Click on Manage Actions tab
		driver.findElement(By.xpath("//li[contains(.,'Manage actions')]")).click();
		Thread.sleep(2000);
		
		try
		{
		//click on confirmation
		boolean Confirmation=driver.findElement(By.xpath("//footer/button[2]")).isDisplayed();
		System.out.println(Confirmation);
		
		if(Confirmation)
		{
			driver.findElement(By.xpath("//footer/button[2]")).click();
		}
		Thread.sleep(2000);
		}
		catch (Exception e)
		{
			System.out.println("No popup is present");
		}
		
		//Enter the data to search field
		driver.findElement(By.cssSelector(".info-data:nth-child(2)")).clear();
		driver.findElement(By.cssSelector(".info-data:nth-child(2)")).sendKeys(ActionNameinCreatePage);
		
		//Store the search results into string
		String Results=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div[2]/div")).getText();
		
		if(Results.contains(ActionNameinCreatePage))
		{
			System.out.println("Search in manage Actions tab is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Search in manage Actions tab is not working fine");
			
			for(int i=0; i<=ActionNameinCreatePage.length(); i++)
			{
				driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(Keys.BACK_SPACE);
			}
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Search failed in Manage Actions")).click();
		}
		
		//Clear the search data
		for(int i=0; i<=ActionNameinCreatePage.length(); i++)
		{
			driver.findElement(By.cssSelector(".info-data:nth-child(2)")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"ActionNameinCreatePage"})
	@TestRail(testCaseId=429)
	@Test(priority=10)
	public void EditAction(String ActionNameinCreatePage,ITestContext context) throws InterruptedException
	{
		//Enter the data to search field
		driver.findElement(By.cssSelector(".info-data:nth-child(2)")).clear();
		driver.findElement(By.cssSelector(".info-data:nth-child(2)")).sendKeys(ActionNameinCreatePage);
		
		//Click on Edit ICon   
		driver.findElement(By.cssSelector(".fa-pencil-alt:nth-child(1)")).click();
		
		boolean Actionname=driver.findElement(By.id("provider-name")).isEnabled();
		
		if(Actionname == true)
		{
			System.out.println("Action is Editable");
			
			//Click on Cancel button
			driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[2]")).click();
			Thread.sleep(2000);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			//Failed condition
			driver.findElement(By.id("Action Edit failed")).click();
			
		}
		else
		{
			System.out.println("Action is not Editable");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);
		
		//Click on Cancel button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[2]")).click();
		Thread.sleep(2000);
		
		/*//Click Ok button in confirmation
		if(driver.findElement(By.xpath("//div[29]/div[3]/button")).isDisplayed())
		{
			driver.findElement(By.xpath("//div[29]/div[3]/button")).click();
		}*/
		Thread.sleep(2000);	
	}
	
	@Parameters({"ActionNameinCreatePage"})
	@TestRail(testCaseId=430)
	@Test(priority=11)
	public void CopyAction(String ActionNameinCreatePage,ITestContext context) throws InterruptedException
	{
		//Enter the data to search field
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(ActionNameinCreatePage);
		
		//Click on Copy Icon
		driver.findElement(By.cssSelector(".fa-copy")).click();
		
		//Get provider name
		Provider=driver.findElement(By.id("provider-name")).getAttribute("value");
		System.out.println("Copy as provider names is: " +Provider);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[4]")).click();
		Thread.sleep(8000);
				
		//Store the Total Actions into string          
		String TotalActions=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div[2]/div")).getText();
		System.out.println("Total action names are: " +TotalActions);
		
		if(TotalActions.contains(Provider))
		{
			System.out.println("Copy button is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Copy button is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Copy option failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"ActionNameinCreatePage"})
	@TestRail(testCaseId=431)
	@Test(priority=12)
	public void DeleteAction(String ActionNameinCreatePage,ITestContext context) throws InterruptedException
	{
				
		//Search with the Copy name
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(Provider);
		
		//Click on Delete button
		driver.findElement(By.cssSelector(".fa-trash-alt")).click();
		
		//click on confirmation 
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(1000);
		
		for(int i=0; i<=Provider.length(); i++)
		{
			driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(1000);
		
		//Store the Total Actions into string 
		String TotalActions=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div[2]/div")).getText();
		
		if(TotalActions.contains(Provider))
		{
			System.out.println("Action is not deleted");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("Action Delete failed")).click();
		}
		else
		{
			System.out.println("Action is deleted");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(2000);
	}
	
	
	//-------------- Manage Alerts Tab --------------------
	@Parameters({"AlertName"})
	@TestRail(testCaseId=432)
	@Test(priority=13)
	public void SearchInManageAlertsTab(String AlertName,ITestContext context) throws InterruptedException
	{
		
		//Click on Manage Alerts tab   
		driver.findElement(By.xpath("//li[contains(.,'Manage alerts')]")).click();
		
		
		try
		{
			//click on confirmation
			boolean Confirmation=driver.findElement(By.xpath("//footer/button[2]")).isDisplayed();
			System.out.println(Confirmation);
			
			if(Confirmation)
			{
				driver.findElement(By.xpath("//footer/button[2]")).click();
			}
			Thread.sleep(2000);
		
		}
		catch (Exception e)
		{
			System.out.println("No popup is present");
		}
		Thread.sleep(4000);
		
		//Enter the data to search field
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(AlertName);
		
		//Store the search results into string
		String Results=driver.findElement(By.xpath("//aside/div[2]/div")).getText();
		
		for(int i=0; i<=AlertName.length(); i++)
		{
			driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		
		if(Results.contains(AlertName))
		{
			System.out.println("Search in managet alerts tab is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Search in manage alerts tab is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Saerch failed in Manage Alerts")).click();
		}
		Thread.sleep(2000);
		
	}
	
	@Parameters({"AlertName"})
	@TestRail(testCaseId=433)
	@Test(priority=14)
	public void EditAlert(String AlertName,ITestContext context) throws InterruptedException
	{
		//Enter the data to search field
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(AlertName);
		
		//Click on Edit button
		driver.findElement(By.cssSelector(".fa-pencil-alt:nth-child(1)")).click();
		Thread.sleep(4000);
		
		//click on set name checkbox
		WebElement Checkbox=driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div[2]/div/table/tbody/tr/td/input"));
		
		try
		{
		if(Checkbox.isSelected())
		{
			System.out.println("Checkbox is already selected");
		}
		else
		{
			Checkbox.click();
			Thread.sleep(1000);
		}
		}
		catch (Exception e)
		{
			System.out.println("Exception occured at checkbox");
		}
		Thread.sleep(2000);
		
		//Click on Next button  
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[3]")).click();
		
		//Click on Next button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[3]")).click();
		Thread.sleep(2000);
		
		//Action name
		boolean Name=driver.findElement(By.id("trigger-name")).isEnabled();
		System.out.println(Name);
		
		if(Name==false)
		{
			System.out.println("Alert name is not editable");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Alert name is editable");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		
		Thread.sleep(2000);
		
		//Goto back
		for(int i=0; i<=1; i++)
		{
			driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button")).click();
			Thread.sleep(1000); 
		}
		//Click on Cancel
		driver.findElement(By.xpath("//main/footer/div[2]/button")).click();
		Thread.sleep(2000);		
		
	}
	
	@Parameters({"AlertName"})
	@TestRail(testCaseId=434)
	@Test(priority=15)
	public void DeleteAlert(String AlertName,ITestContext context) throws InterruptedException
	{
		//Enter the data to search field
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(AlertName);
		
		/*//Save the deleted alert name into string for verification
		String AlertName=driver.findElement(By.xpath("//div[2]/div/table/tbody/tr/td[2]")).getText();
		System.out.println(AlertName);*/
		
		//Click on Delete option
		driver.findElement(By.cssSelector(".fa-trash-alt")).click();
		Thread.sleep(3000);
		
		//Click on confirmation ok button
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(2000);
		
		//Save the deleted alert name into string for verification
		String AlertNameAfterDelete=driver.findElement(By.xpath("//aside/div[2]/div")).getText();
		System.out.println("List of alerts: " +AlertNameAfterDelete);
		
		for(int i=0; i<=AlertName.length(); i++)
		{
			driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/aside/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(1000);
		
		if(AlertNameAfterDelete.contains(AlertName))
		{
			System.out.println("Alert is not deleted");
			//Close the popup
			driver.findElement(By.xpath("//main/footer/div/button")).click();
			Thread.sleep(2000);
			
			//Final close 
			CommonElements.AdminCancelButton(driver);
			//driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			Thread.sleep(1000);
			
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
			
		}
		
		else
		{
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
			System.out.println("Alert is deleted successfully");
		}
		
		//Close the popup
		driver.findElement(By.xpath("//main/footer/div/button")).click();
		Thread.sleep(2000);
		
		//Final close 
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
	
	public void Alertstab() throws InterruptedException
	{	
		Thread.sleep(4000);
		//Click on Create button 
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button")).click();
		Thread.sleep(4000);
		
		//click on set name checkbox 
		driver.findElement(By.cssSelector("tr:nth-child(1) .error")).click();
		
		//Click on Next button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[3]")).click();
		
		//Click on Next button
		driver.findElement(By.xpath("//dialog[@id='trigger-wizard']/section/main/footer/div[2]/button[3]")).click();
		
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
