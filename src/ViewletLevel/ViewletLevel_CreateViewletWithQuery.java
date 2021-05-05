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
public class ViewletLevel_CreateViewletWithQuery 
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
			Thread.sleep(10000);
		}
		
		//Create Dashboard
		obj.CreateDashboard(driver, DashboardName);
		Thread.sleep(2000);
		
		//List of dasj=hboards
		WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
	}
	
	@Parameters({"Query", "ViewletName"})
	@TestRail(testCaseId=437)
	@Test(priority=1)
	public void CreateViewletwithquery(String Query, String ViewletName, ITestContext context) throws InterruptedException
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
	
	
	@Parameters({"NewQuery"})
	@TestRail(testCaseId=438)
	@Test(priority=2)
	public void EditQuery(String NewQuery, ITestContext context) throws InterruptedException
	{
		//Click on Edit Query                                                
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button/i")).click();
		
		//Edit the query                                  
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).clear();
		Thread.sleep(2000);
		
		//Enter the new query
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).sendKeys(NewQuery);
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).sendKeys(Keys.ENTER);
		Thread.sleep(4000);
		
		//Store the Activity column heading into string                    
		String Activity=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[3]")).getText();
		System.out.println("Activity is: " +Activity);             
		
		if(Activity.contains("ActivityID"))
		{                             
			System.out.println("Query is updated");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Query is not updated");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query update failed")).click();
		}
		Thread.sleep(2000);
		
	}
	
	@Parameters({"Query"})
	@TestRail(testCaseId=439)
	@Test(priority=3)
	public void UndoIconFunctionalityIntheViewlet(String Query,ITestContext context) throws InterruptedException
	{
		//Click on undo button                                      
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[2]/i")).click();
		Thread.sleep(4000);
		
		//Get the query
		//driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).click();
		String ResetQuery=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).getAttribute("value");
		System.out.println("Updated Query is: " +ResetQuery);
		
		//verification 
		if(ResetQuery.equalsIgnoreCase(Query))
		{
			System.out.println("Undo Query is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Undo Query is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query Reset failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"NewQuery"})
	@TestRail(testCaseId=442)
	@Test(priority=4)
	public void RedoIconFunctionalityIntheViewlet(String NewQuery,ITestContext context) throws InterruptedException
	{
		//Click on Redo button                          
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[3]/i")).click();
		Thread.sleep(4000);
		
		//Get the query
		//driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).click();
		String ResetQuery=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).getAttribute("value");
		System.out.println("Updated Query is: " +ResetQuery);
		
		//verification 
		if(ResetQuery.equalsIgnoreCase(NewQuery))
		{
			System.out.println("Redo Query is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Redo Query is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Query Reset failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@TestRail(testCaseId=440)
	@Test(priority=10)
	public void ChangeChartTypeIcon() throws InterruptedException
	{	
		//Click on change chart type icon 
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[4]")).click();
		Thread.sleep(1000);
		
		WebElement ele1=driver.findElement(By.className("handler-type-select"));
				
		List<WebElement> input_ele=ele1.findElements(By.tagName("label"));
		System.out.println(input_ele.size());
		int index=0;
		for(WebElement e:input_ele)
		{
			System.out.println("Inner html:" +e.getAttribute("innerHTML"));
			//WebElement fin=e.findElement(By.tagName("input"));
			String status=e.getAttribute("innerHTML");
			
			for(int i=0;i<input_ele.size();i++)
			{
				if(input_ele.get(i).isEnabled())
				{
					System.out.println("Status is : " +input_ele.get(i).getText());
					index++;
				}
				
			}
		}
		
		
		//Close the popup
		driver.findElement(By.cssSelector("#handler-type-select-widget-popup > .close-button > .fas")).click();
		Thread.sleep(3000);
		System.out.println("closed: " + index);
		try
		{
			Integer[] list=new Integer[index];
			int k=0;
							
			for(int i=0;i<input_ele.size();i++)
			{
				if(input_ele.get(i).isEnabled())
				{
					list[k]= i;
					k++;
				}
					
			}
				
			System.out.println(list.length);
					
			System.out.println(Arrays.toString(list));
			
			for(int l=0;l<list.length;l++)
			{
				driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[4]")).click();
				Thread.sleep(1000);
				
				System.out.println(list[l].intValue()); 
				driver.findElement(By.cssSelector("label:nth-child("+ (list[l].intValue()+1) +") .color")).click();
				Thread.sleep(4000);
				if(driver.findElement(By.cssSelector("#handler-type-select-widget-popup > .close-button > .fas")).isDisplayed())
				{
					driver.findElement(By.cssSelector("#handler-type-select-widget-popup > .close-button > .fas")).click();
					Thread.sleep(1000);
				}
			}
	
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
	
		
		/*try
		{
		for(int i=2; i<=13; i++)
		{
			//Click on change chart type icon
			driver.findElement(By.xpath("//div[3]/div/ul/li/div[2]/div/div/div[5]/i")).click();
			Thread.sleep(1000);
			
			WebElement ele1=driver.findElement(By.className("viewlet-type-select"));
			
			List<WebElement> dis=ele1.findElements(By.tagName("span"));
			System.out.println(dis.size());
			
			for(WebElement eee : dis)
			{
				WebElement disable=eee.findElement(By.tagName("span"));
				System.out.println(disable.getAttribute("class"));
			}
			
			//Check the chart is enable or not
			Boolean clickable=driver.findElement(By.cssSelector("label:nth-child("+ i +") .viewlet-icon")).isEnabled();
			System.out.println(clickable);
			
						
			if(clickable)
			{
				//Store the chart tooltip data into string
				Actions a=new Actions(driver);
				WebElement ele=driver.findElement(By.cssSelector("label:nth-child("+ i +") .viewlet-icon"));
				a.moveToElement(ele).perform();
				String Chartname=ele.getText();
				System.out.println(Chartname);
				
				//click on Chart type
				driver.findElement(By.cssSelector("label:nth-child("+ i +") .viewlet-icon")).click();
				Thread.sleep(4000);
				
				//Store the query data into string
				String Query=driver.findElement(By.xpath("//div[3]/div/ul/li/div[2]/div/div[2]/input")).getAttribute("value");
				
				if(Query.contains(Chartname))
				{
					System.out.println("Chart is displayed as expected");
				}
				else
				{
					System.out.println("Chart is not displayed as expected");
				}
			}
			else
			{
				//Close the popup
				driver.findElement(By.xpath("//dialog[27]/div")).click();
				Thread.sleep(1000);
				
				System.out.println("Chart is Not disable");
			}
		}
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			System.out.println("Exception Occured");
		}*/
	}
	@TestRail(testCaseId=802)
	@Test(priority=11)
	public void ChartTypes() throws InterruptedException
	{
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[4]")).click();
	    Thread.sleep(2000);
				 
		WebElement ele1=driver.findElement(By.className("handler-type-select"));
		
		List<WebElement> input_ele=ele1.findElements(By.tagName("label"));
		System.out.println(input_ele.size());
		
		for(WebElement ele: input_ele)
		{
			try
			{
				driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[4]")).click();
			    Thread.sleep(2000);
			}
			catch(Exception e)
			{
				System.out.println("Already selected");
			}
			//System.out.println("Inner html" +ele.getAttribute("innerHTML"));
			
			String status=ele.getAttribute("innerHTML");
			
			String Title=ele.getAttribute("title");
			System.out.println("Title is: " +Title);
			
			if(status.contains("disabled"))
			{
				System.out.println(""+ Title +" table is disabled");
			}
			else
			{
				ele.click();
				
				try
				{
					driver.findElement(By.cssSelector("#handler-type-select-widget-popup > .close-button > .fas")).click();
				}
				catch(Exception e)
				{
					
				}
								
				String TreeQuery=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).getAttribute("title");
				System.out.println("Query is: " +TreeQuery); 
				
				if(Title.equalsIgnoreCase("ColumnChart"))
				{
					if(TreeQuery.contains("colchart"))
					{
						System.out.println(""+Title+" Chart is working fine");
					}
					else
					{
						System.out.println(""+Title+" Chart is not working fine");
						driver.findElement(By.id("Chart failed to load")).click();
					}
				}
				else
				{
					if(TreeQuery.toLowerCase().contains(Title.toLowerCase()))
					{
						System.out.println(""+Title+" Chart is working fine");
					}
					else
					{
						System.out.println(""+Title+" Chart is not working fine");
						driver.findElement(By.id("Chart failed to load")).click();
					}
				}
				
			} 
			
			Thread.sleep(1000);
			
		}
	
		
		/*
		 * for(int i=1; i<=14; i++) { //click on chart icon on viewlet
		 * driver.findElement(By.xpath("//div["+ Dashboardscount
		 * +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[4]")).click();
		 * Thread.sleep(2000);
		 * 
		 * //verify enabled chart type WebElement
		 * Table=driver.findElement(By.cssSelector("label:nth-child("+ i
		 * +") > .color-icon > svg")); String wTable=Table.getAttribute("class");
		 * System.out.println("table status is:"+wTable);
		 * 
		 * if(wTable.contains("enabled")) {
		 * System.out.println("table chart is enabled"); String
		 * Title=Table.getAttribute("title"); Title=Title.replaceAll("\\s", "");
		 * System.out.println("chart type title is :" +Title);
		 * 
		 * Table.click(); Thread.sleep(8000); try { driver.findElement(By.
		 * cssSelector("#handler-type-select-widget-popup > .close-button > .fas")).
		 * click(); } catch(Exception e) {
		 * 
		 * }
		 * 
		 * String TreeQuery=driver.findElement(By.xpath("//div["+ Dashboardscount
		 * +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).
		 * getAttribute("title"); System.out.println("Query is: " +TreeQuery);
		 * 
		 * if(Title.equalsIgnoreCase("ColumnChart")) {
		 * if(TreeQuery.contains("colchart")) {
		 * System.out.println(""+Title+" Chart is working fine"); } else {
		 * System.out.println(""+Title+" Chart is not working fine");
		 * driver.findElement(By.id("Chart failed to load")).click(); } } else {
		 * if(TreeQuery.toLowerCase().contains(Title.toLowerCase())) {
		 * System.out.println(""+Title+" Chart is working fine"); } else {
		 * System.out.println(""+Title+" Chart is not working fine");
		 * driver.findElement(By.id("Chart failed to load")).click(); } }
		 * 
		 * } else { System.out.println("chart is disabled"); //close chart type
		 * driver.findElement(By.
		 * cssSelector("#handler-type-select-widget-popup > .close-button > .fas")).
		 * click(); Thread.sleep(2000); } Thread.sleep(1000);
		 * 
		 * }
		 */	
	}
	@TestRail(testCaseId=441)
	@Test(priority=7)
	public void Refresh(ITestContext context) throws InterruptedException
	{
		try
		{
			//click on Refresh icon      
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[7]/i")).click();
			Thread.sleep(6000);
			
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		catch (Exception e)
		{
			System.out.println("Refresh failed");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Refresh failed")).click();
		}		
	}
	
//	@TestRail(testCaseId=442)
//	@Test(priority=8)
//	public void ViewletName(ITestContext context)  
//	{
//		//Store the viewlet name into string
//		String name=driver.findElement(By.xpath("//div[3]/div/ul/li/div")).getText();
//		System.out.println("Get text" +name);
//		String name1=driver.findElement(By.xpath("//div[3]/div/ul/li/div")).getAttribute("value");
//		System.out.println(name1);
//	}
	
	
	@TestRail(testCaseId=443)
	@Test(priority=9)
	public void MinimizeViewlet(ITestContext context) throws InterruptedException
	{
		//Get query name into string   
		Boolean q1=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).isDisplayed();
		System.out.println("Query one status: " +q1);               
		
		//Click on minimize icon
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div/button/i")).click();
		Thread.sleep(2000);                              
		
		//Get query name into string 
		Boolean q2=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div/jkql-input/textarea")).isDisplayed();
		System.out.println("Query two status: " +q2);
		
		if(q1!=q2)
		{
			System.out.println("Viewlet is minimized");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Viewlet is not minimized");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			//expand the viewlet by clicking on +
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div/button/i[2]")).click();
			Thread.sleep(4000);
			driver.findElement(By.id("Viewlet Minimization failed")).click();
		}
		Thread.sleep(1000);
		
		//expand the viewlet by clicking on +
		driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div/button/i[2]")).click();
		Thread.sleep(4000);
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
	
	public void method5() throws InterruptedException
	{
		//Close the popup
		driver.findElement(By.xpath("//dialog[27]/div")).click();
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
