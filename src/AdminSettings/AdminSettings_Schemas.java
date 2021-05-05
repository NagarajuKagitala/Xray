package AdminSettings;



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
import org.openqa.selenium.interactions.Actions;
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
public class AdminSettings_Schemas
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
		
		//Click on Admin settings
		driver.findElement(By.cssSelector(".icon")).click();
		driver.findElement(By.xpath("//div[@id='topMenu']/ul/li[4]")).click();
		Thread.sleep(2000);
			
		}
	
	@Parameters({"Searchdata"})
	@TestRail(testCaseId=391)
	@Test(priority=1)
	public void SchemaSearch(String Searchdata,ITestContext context) throws InterruptedException
	{		
		//Click on Schema tab
		driver.findElement(By.xpath("//nav/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Enter the data into search field
		driver.findElement(By.xpath("//aside[10]/div/input")).clear();
		driver.findElement(By.xpath("//aside[10]/div/input")).sendKeys(Searchdata);
		Thread.sleep(2000);
		
		//Store the results into string
		String SearchResults=driver.findElement(By.xpath("//aside[10]/div[2]/div/div")).getText();
		
		if(SearchResults.contains(Searchdata))
		{
			System.out.println("Search is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		
		//Clear the search field
		for(int i=0; i<=Searchdata.length(); i++)
		{
			driver.findElement(By.xpath("//aside[10]/div/input")).sendKeys(Keys.BACK_SPACE);
		}
		Thread.sleep(1000);
	}
	
	@Parameters({"SchemaName", "ItemName"})
	@TestRail(testCaseId=392)
	@Test(priority=2)
	public void CreateSchema(String SchemaName, String ItemName,ITestContext context) throws InterruptedException
	{
		//Click on Create button
		driver.findElement(By.xpath("//main/footer/div[2]/button")).click();
		
		//Enter the schema name
		driver.findElement(By.id("schemaName")).sendKeys(SchemaName);
		
		Select Itemtype=new Select(driver.findElement(By.xpath("//div[3]/select")));
		Itemtype.selectByVisibleText(ItemName);
		
		//Click on Add fields button
		driver.findElement(By.xpath("//input[@value='Add fields']")).click();
		
		//Click on move all
		driver.findElement(By.xpath("//td[2]/div/div/input")).click();
		
		//Click on save changes
		driver.findElement(By.xpath("//main/footer/div[2]/button[3]")).click();
		
		//click on save changes
		driver.findElement(By.xpath("//main/footer/div[2]/button[2]")).click();
		Thread.sleep(1000);
		
		//Get the data and store into string
		String Data=driver.findElement(By.xpath("//aside[10]/div[2]/div/div")).getText();
		
		if(Data.contains(SchemaName))
		{
			System.out.println("Schema is added successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Schema is not added");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Add Schema is failed")).click();
		}
		Thread.sleep(1000);
	}
	
	
	@Parameters({"SchemaName", "ListOfAttributes"})
	@TestRail(testCaseId=393)
	@Test(priority=3)
	public void EditSchema(String SchemaName, String ListOfAttributes,ITestContext context) throws InterruptedException
	{
		//Enter the data into search field
		driver.findElement(By.xpath("//aside[10]/div/input")).clear();
		driver.findElement(By.xpath("//aside[10]/div/input")).sendKeys(SchemaName);
		Thread.sleep(2000);
		
		//Store the results into string
		String SearchResults=driver.findElement(By.xpath("//aside[10]/div[2]/div/div")).getText();
		
		if(SearchResults.contains(SchemaName))
		{
			//Click on edit icon
			driver.findElement(By.xpath("//td[3]/i")).click();
			Thread.sleep(1000);
			
			//Edit attributes, click pencil icon to edit
			driver.findElement(By.xpath("//td[4]/i")).click();
			Thread.sleep(1000);
			
		    String AttributeValues=ListOfAttributes.toString();
			String[] ListOfAttributesPresent = AttributeValues.split(",");
			
			//Remove the attributes
			for (String RemovableAttributes : ListOfAttributesPresent)
			{
				driver.findElement(By.xpath("//option[@value='"+ RemovableAttributes +"']")).click();
				driver.findElement(By.xpath("//td[2]/div/div[3]/input")).click();
				Thread.sleep(1000);
			}
					
			//Apply changes to attributes
			driver.findElement(By.xpath("//main/footer/div[2]/button[3]")).click();
			Thread.sleep(1000);
			
			String attributes=driver.findElement(By.xpath("//div[2]/div/table/tbody/tr/td[2]/div")).getText();
			
			System.out.println("Attributes: "+ attributes);
			
			if(attributes.contains(AttributeValues))
			{
				System.out.println("Schema is not edited successfully");
				//Save changes
				driver.findElement(By.xpath("//main/footer/div[2]/button[2]")).click();
				Thread.sleep(1000);
				
				//Clear the search field
				driver.findElement(By.xpath("//aside[10]/div/input")).clear();
				Thread.sleep(1000);
				
				driver.findElement(By.xpath("//main/footer/div/button")).click();
				Thread.sleep(1000);
				context.setAttribute("Status",5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("Not created")).click();			}
			else
			{
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
				System.out.println("Schema is edited successfully");
			}
			
			//Save changes
			driver.findElement(By.xpath("//main/footer/div[2]/button[2]")).click();
			Thread.sleep(1000);
			
			//Clear the search field
			driver.findElement(By.xpath("//aside[10]/div/input")).clear();
			Thread.sleep(1000);
			
			//Click on Close button
			driver.findElement(By.xpath("//main/footer/div/button")).click();
			Thread.sleep(1000);
		}
		
	}
	
	@Parameters({"SchemaName", "DashboardName"})
	@TestRail(testCaseId=394)
	@Test(priority=4)
	public void AddSchematoDashboard(String SchemaName, String DashboardName,ITestContext context) throws InterruptedException
	{
		//Create dashboard
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
		
		//dashboard name
		driver.findElement(By.id("dashboardName")).sendKeys(DashboardName);
		
		//Un check initial viewlets checkbox
		driver.findElement(By.id("createInitialViewlets")).click();
		
		//Click on Create button
		driver.findElement(By.id("createDashboardBtn")).click();
		
		//Right click on dashboard
		if(driver.findElement(By.xpath("//li[3]/a/span")).isDisplayed())
		{
			Actions actions = new Actions(driver);
			WebElement elementLocator = driver.findElement(By.xpath("//li[3]/a/span"));
			actions.contextClick(elementLocator).perform();
			Thread.sleep(2000);
		}
		else if(driver.findElement(By.xpath("//li[2]/a/span")).isDisplayed())
		{
			Actions actions = new Actions(driver);
			WebElement elementLocator = driver.findElement(By.xpath("//li[2]/a/span"));
			actions.contextClick(elementLocator).perform();
			Thread.sleep(2000);
		}
		else
		{
			Actions actions = new Actions(driver);
			WebElement elementLocator = driver.findElement(By.xpath("//a/span"));
			actions.contextClick(elementLocator).perform();
			Thread.sleep(2000);	
		}
		
		String Conf=driver.findElement(By.xpath("//ol/li[4]")).getText();
		System.out.println(Conf);
		
		if(Conf.equalsIgnoreCase("Configure"))
		{
			//Click on configure
			driver.findElement(By.xpath("//ol/li[4]")).click();
			Thread.sleep(1000);
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		
		}
		else
		{
			//Click on configure
			driver.findElement(By.xpath("//ol/li[5]")).click();
			Thread.sleep(1000);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
			
		}
		
		//Select schema
		Select drpCountry = new Select(driver.findElement(By.xpath("//div[3]/select")));
		drpCountry.selectByVisibleText(SchemaName);
		Thread.sleep(1000);
		
		driver.findElement(By.xpath("//dialog[20]/section/footer/div[2]/button")).click();
		Thread.sleep(1000);
		
	}
	
	@Parameters({"Query", "SchemaName"})
	@TestRail(testCaseId=395)
	@Test(priority=5)
	public void VerificationOfSchemaAppliedToDashboard(String Query, String SchemaName,ITestContext context) throws InterruptedException
	{
		//Click on Admin settings
		driver.findElement(By.cssSelector(".icon")).click();
		driver.findElement(By.xpath("//div[@id='topMenu']/ul/li[4]")).click();
		Thread.sleep(2000);
		
		//Click on Schema page
		driver.findElement(By.cssSelector(".adminStep:nth-child(5)")).click();
		
		//Search with Schema name
		driver.findElement(By.xpath("//aside[10]/div/input")).clear();
		driver.findElement(By.xpath("//aside[10]/div/input")).sendKeys(SchemaName);
		
		//click on Edit button
		driver.findElement(By.cssSelector(".fa-pencil")).click();
		
		//Get the field names into string
		String Fields=driver.findElement(By.xpath("//div[2]/div/table/tbody/tr/td[2]/div")).getText();
		System.out.println("Field values are: " +Fields);
		
		//Close the popup page
		driver.findElement(By.xpath("//main/footer/div/button[2]")).click();
		driver.findElement(By.xpath("//main/footer/div/button")).click();
		Thread.sleep(1000);
		
		this.CreateSummaryViewlet(Query);
		
		//Click on summary viewlet
		driver.findElement(By.xpath("//div[3]/div[3]/ul/li/div[3]/div/div/div/div/div[2]/div/div[2]/div")).click();
		Thread.sleep(10000);
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
		System.out.println("dashboard count: " + myElements.size());
		for (WebElement e : myElements) 
		{
			//System.out.println(e.getAttribute("aria-hidden"));
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false"))
			{
				try {
				//Get the column names
				WebElement Table=driver.findElement(By.id("consolePanel"));
				
			
				WebElement table_web=Table.findElement(By.tagName("table")).findElement(By.tagName("thead"));
				
				List<WebElement> lst_we=table_web.findElements(By.tagName("tr"));
				
				System.out.println("tr count: "+lst_we.size() );
				
				System.out.println("innerhtml: " +table_web.getAttribute("innerHTML"));
				
				List<WebElement> Row=lst_we.get(0).findElements(By.tagName("th"));
				System.out.println(Row.size());
				
				String ObjectAttributes[]=new String[Row.size()];
				int i=0;
				StringBuilder buffer = new StringBuilder();
				for(WebElement value : Row)
				{
					if(value.getText().isEmpty())
					{
						
					}
					else
					{
						ObjectAttributes[i]=value.getText();
						String Store=value.getText();
						//System.out.println("Attribute values are:" +Store);
						if(!Store.isEmpty()) 
			   	        {
			   	    	 String None= "None";
			   	    	 if(!Store.contains(None))
			   	    	 {
			   	         buffer.append(ObjectAttributes[i]);
			    	     buffer.append(',');
			    	     }
			   	    	 }
					}
				}
				
				String Values=buffer.toString();
				System.out.println(Values);
				
				if(Values.contains(Fields))
				{
					System.out.println("Fields are verified");
					context.setAttribute("Status",1);
					context.setAttribute("Comment", "working fine");
				}
				else
				{
					System.out.println("Fields are not verified");
					driver.findElement(By.id("close_0")).click();
					context.setAttribute("Status",5);
					context.setAttribute("Comment", "Failed");
					driver.findElement(By.id("Fields not matched")).click();
				}
				}
				catch (Exception ex) 
				{
					// TODO: handle exception
					ex.printStackTrace();
				}
				
				driver.findElement(By.id("close_0")).click();
				Thread.sleep(1000);
			}
		}
	}
	
	@Parameters({"SchemaName"})
	@TestRail(testCaseId=396)
	@Test(priority=7)
	public void DeleteSchema(String SchemaName,ITestContext context) throws InterruptedException
	{
		//Click on Admin settings
		driver.findElement(By.cssSelector(".icon")).click();
		driver.findElement(By.xpath("//div[@id='topMenu']/ul/li[4]")).click();
		Thread.sleep(5000);
		
		//Click on Schema tab
		driver.findElement(By.xpath("//nav/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Enter the data into search field
		driver.findElement(By.xpath("//aside[10]/div/input")).clear();
		driver.findElement(By.xpath("//aside[10]/div/input")).sendKeys(SchemaName);
		Thread.sleep(2000);
		
		//Click on delete icon
		driver.findElement(By.xpath("//i[2]")).click();
		Thread.sleep(1000);
		
		//Confirm to delete
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(1000);
		
		/*//Save changes
		driver.findElement(By.xpath("//main/footer/div[2]/button[2]")).click();
		Thread.sleep(1000);
		
		//Clear the search field
		driver.findElement(By.xpath("//aside[10]/div/input")).clear();
		Thread.sleep(1000);*/
		
		//Store the results into string
		String SearchResults=driver.findElement(By.xpath("//aside[10]/div[2]/div/div")).getText();
		System.out.println(SearchResults);          
		
		//Verification
		if(SearchResults.contains(SchemaName))
		{
			System.out.println("Schema is not deleted");
			//Close the settings window
			driver.findElement(By.xpath("//main/footer/div/button")).click();
			Thread.sleep(1000);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Schema failed to delete")).click();
		}
		else
		{
			System.out.println("Schema is deleted successfully");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		
		//Close the settings window
		driver.findElement(By.xpath("//main/footer/div/button")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	
	public void CreateSummaryViewlet(String Query) throws InterruptedException
	{
		//Click on + button
		driver.findElement(By.xpath("//div[3]/div[6]/div[2]/div")).click();
		
		boolean BasicSummary=driver.findElement(By.xpath("//dialog[6]/section/main/div/label/input")).isSelected();
		
		if(BasicSummary)
		{
			System.out.println("Basic summary Radio button is already selected");
		}
		else
		{
			driver.findElement(By.xpath("//dialog[6]/section/main/div/label/input")).click();
		}
		Thread.sleep(1000);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[6]/section/footer/div[2]/button")).click();
		
		//Enter the query into Define query field
		driver.findElement(By.xpath("//dialog[8]/section/main/div/div/div/input")).sendKeys(Query);
				
		//Click on Create button
		driver.findElement(By.xpath("//dialog[8]/section/footer/div[2]/input")).click();
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
