package GlobalRepositories;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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

import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class GlobalRepo_SampleAnomaly 
{
    static WebDriver driver;
    static String Screenshotpath;
    
    @BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
	}
   
    @Parameters({"sDriver", "sDriverPath"})
    @Test
    public static void Login(String sDriver, String sDriverPath) throws Exception
    {
    	Settings.read();
		String sURL = Settings.getsURL();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
    	if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
    	{
    		System.setProperty(sDriver, sDriverPath);
    	    driver=new ChromeDriver();
	    }
    	else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
    	{
    		System.setProperty(sDriver, sDriverPath);
    		driver=new InternetExplorerDriver();
    	}
    	else if(sDriver.equalsIgnoreCase("webdriver.edge.driver"))
    	{
    		System.setProperty(sDriver, sDriverPath);
    		driver= new EdgeDriver();
    	}
    	else
    	{
    		System.setProperty(sDriver, sDriverPath);
    		driver= new FirefoxDriver();
    	}
	
	    driver.get(sURL);
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
	    //Login page
	    driver.findElement(By.id("Uname")).sendKeys(sUsername);
	    driver.findElement(By.id("PWD")).sendKeys(sPassword);
	    Thread.sleep(3000);
	    driver.findElement(By.id("Submit")).click();
	    Thread.sleep(8000);
    }
    
    @Parameters({"sRepositories"})
	@Test(priority=1)
    public static void SelectRepositories(String sRepositories) throws InterruptedException
	{   	
		String repodata = ".//*[contains(@id," + "'" + sRepositories + "'" + ")]";
		// Click on Dropdown
		driver.findElement(By.cssSelector("span.select2-selection__arrow")).click();
		driver.findElement(By.xpath(repodata)).click();
		Thread.sleep(6000);
	}
    
    @TestRail(testCaseId=558)
    @Parameters({"learningQuery"})
	@Test(priority=2)
    public static void Learning_query(String learningQuery, ITestContext context)throws InterruptedException
    {		
		//Get the set name
		String Setname=driver.findElement(By.xpath("//tr[@id='0']/td[2]/span")).getText();
		System.out.println("Set name is: " +Setname);
		
		//click on specific setName
		driver.findElement(By.xpath("//tr[@id='0']/td[2]/span")).click();
		Thread.sleep(2000);
		//need to see data for specific setName
		String SpecificData=driver.findElement(By.xpath("//div[4]/div/div/div[3]/div[3]/div")).getText();
		System.out.println("Set data is: " +SpecificData);
		Thread.sleep(2000);
		
		if(SpecificData.contains(Setname))
		{
			System.out.println("learning query consol data are match");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("learning query consol data are  not match");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			//click on close dashboard btn
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[3]/div[2]/ul/li/span")).click();
			driver.findElement(By.xpath("Not created")).click();

		}
		
		//click on close dashboard btn
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/div[2]/ul/li/span")).click();

     }	 
    
    
     @TestRail(testCaseId=559)
     @Parameters({"learningRules"})
	 @Test(priority=3)
     public static void LearningRules(String learningRules, ITestContext context) throws InterruptedException
     {
		/*//enter the query
        Thread.sleep(8000);
		driver.findElement(By.xpath("//li[2]/div[2]/div/div[2]/input")).clear();
		driver.findElement(By.xpath("//li[2]/div[2]/div/div[2]/input")).sendKeys(learningRules);
        Thread.sleep(3000);
		driver.findElement(By.xpath("//li[2]/div[2]/div/div[2]/input")).sendKeys(Keys.ENTER);
        Thread.sleep(2000);
		//copy text from output pannel
		String LRData1=driver.findElement(By.xpath("//li[2]/div[2]/div/div[2]/input")).getText();
		String LRData2=LRData1.replaceAll("[\r\n]+",",");
        Thread.sleep(2000);
		if(learningRules.toLowerCase().contains(LRData2.toLowerCase()))
		{
			System.out.println("Learning Rules query match with output");
		}
		else
		{
			System.out.println("Learning Rules query not match with output");

		}*/
		
    	//Get the set name
 		String Setname=driver.findElement(By.xpath("(//tr[@id='0']/td[2]/span)[2]")).getText();
 		System.out.println("Set name is: " +Setname);
 		
		//click on specific SetName
		driver.findElement(By.xpath("(//tr[@id='0']/td[2]/span)[2]")).click();
		Thread.sleep(2000);
		String LRConsol=driver.findElement(By.xpath("//div[4]/div/div/div[3]/div[3]/div")).getText();
		//System.out.println(LRConsol);
		if(LRConsol.contains(Setname))
		{
			System.out.println("learning rules console are match");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("learning rules console are not  match");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			//click on close dashboard btn
			driver.findElement(By.xpath("//div[3]/div[2]/ul/li/span")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("Not created")).click();

		}
		
		//click on close dashboard btn
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[3]/div[2]/ul/li/span")).click();
     }
     
     @TestRail(testCaseId=560)
     @Parameters({"eventsquery","MsgData"})
	 @Test(priority=4)
     public static void Events(String eventsquery,String MsgData, ITestContext context) throws InterruptedException
     {
    	 
		/*//Enter the query
		driver.findElement(By.xpath("//li[3]/div[2]/div/div[2]/input")).clear();
		driver.findElement(By.xpath("//li[3]/div[2]/div/div[2]/input")).sendKeys(eventsquery);
		driver.findElement(By.xpath("//li[3]/div[2]/div/div[2]/input")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		//copy text from output pannel
		String EQdata1=driver.findElement(By.xpath("//li[3]/div[2]/div[4]/div/div[3]/div[2]")).getText();
		String EQdata2=EQdata1.replaceAll("[\r\n]+",",");
		if(eventsquery.toLowerCase().contains(EQdata2.toLowerCase()))
		{
			System.out.println("Events query match with output");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Events query not match with output");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			//click on close msg icon
			driver.findElement(By.cssSelector("#errorMsgDialog > .closeButton")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("Not created")).click();

		}*/
		
    	 //Get the Message data
    	 String MessageData=driver.findElement(By.xpath("//tr[@id='0']/td[2]/div[2]")).getText();
    	 System.out.println("Message data is: " +MessageData);
		
		//click on Msg icon
		driver.findElement(By.xpath("//tr[@id='0']/td[2]/div[2]")).click();
		Thread.sleep(2000);
		String Msg=driver.findElement(By.id("errorMsgDialogContainer")).getText();
		System.out.println("Message Text is :"+Msg);
		
		if(Msg.contains(MessageData))
		{
			System.out.println("Msg popup is open");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Msg popup is not open");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
		}
			
		//click on close msg icon
		driver.findElement(By.cssSelector("#errorMsgDialog > .closeButton")).click();
		Thread.sleep(2000);
		
	}
     
    @TestRail(testCaseId=561)
 	@Test(priority=5)
 	public static void Orders_Anomaly(ITestContext context) throws InterruptedException
    {
		 //click on Anomaly dashbarod
		 driver.findElement(By.cssSelector("#ui-id-13 > .tabs-title")).click();
		 Thread.sleep(6000);
		 
		 //Click on Bullet
		 driver.findElement(By.cssSelector(".amcharts-graph-anomaly > .amcharts-graph-bullet")).click();
		 Thread.sleep(4000);
		 
		 boolean Console=driver.findElement(By.xpath("//div[3]/ul/li/a/span")).isDisplayed();
		 System.out.println("Console page is: " +Console);
		 
		 if(Console==true)
		 {
			 System.out.println("Orders anomaly data is opened in the console page");
			 context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		 }
		 else
		 {
			 System.out.println("Orders anomaly data is not opened in the console page");
			 context.setAttribute("Status",5);
			 context.setAttribute("Comment", "Failed");
			 //Close the console page
			 driver.findElement(By.xpath("//div[3]/ul/li/span")).click();
			 driver.findElement(By.id("Orders anomaly failed")).click();
		 }
		 
		 //click on close dashboard btn
		 driver.findElement(By.xpath("//div[3]/ul/li/span")).click();
		 Thread.sleep(4000);
		 
 		/*//get query on orders anomaly
 		String Anomaly_Query=driver.findElement(By.xpath("//div[2]/div/ul/li/div[2]/div/div[2]/input")).getAttribute("value").toString();
 		System.out.println("Query:"+Anomaly_Query);
 		if(Anomaly_Query.contains("ElapsedTime"))
 		{
 			System.out.println(" query gives the Average Elapsed Time on the day");
 		}
 		else
 		{
 			System.out.println(" query not gives the Average Elapsed Time on the day");

 		}
 				
 		//click on Line graph which is highlighted with blue color
 		driver.findElement(By.cssSelector(".amcharts-graph-bullet:nth-child(8)")).click();
 		Thread.sleep(5000);
 		WebElement we=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widgetc7abf05a-2ee0-0c8d-a1cc19926033-drilldowncontentprojectTable\"]/div[3]")).findElement(By.tagName("tbody"));
		List<WebElement> lst=we.findElements(By.className("jqgrow"));
		System.out.println("RowSize: "+ lst.size());
		for(WebElement row : lst)
		{
			 List<WebElement> lstwele=row.findElements(By.tagName("td"));
			 for(WebElement td :lstwele)
			 {
				 String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
	 				if(!data.isEmpty())
	 				{
	 					System.out.println(td.getAttribute("title").replace("&nbsp;", "").replace(" ", ""));
	 				}    
			        
			 }
		}	
		
		//click on console close icon
		driver.findElement(By.xpath("//div[3]/ul/li/span")).click();
		Thread.sleep(3000);	*/	
    }  
 	
    @TestRail(testCaseId=562)
	@Test(priority=6)
	public static void Activities_in_each_hour_withAnomalies(ITestContext context) throws InterruptedException
	{
    	//Store the First interval time
    	String FI=driver.findElement(By.xpath("//div[2]/ul/li[2]/div[2]/div[4]/div/div[3]/div[3]/div/table/tbody/tr[2]/td[2]")).getText();
    	System.out.println("First interval time is: " +FI);
    	
    	String[] arrOfStr = FI.split(",");
		
		
		//System.out.println("Data is1: " +arrOfStr[1]);
		//System.out.println("Data is2: " +arrOfStr[2]);
		
		
		String[] FIT = arrOfStr[1].split(":");
		String[] SIT = arrOfStr[2].split(":");
		
		//System.out.println("Final value" +FIT[0]);
		//System.out.println("Final value" +SIT[0]);
    	
    	int FITime=Integer.parseInt(FIT[0].trim());
    	int SITime=Integer.parseInt(SIT[0].trim());
    	int Result=SITime-FITime;
    	System.out.println("Final resut is: " +Result);
    	
    	if(Result==1)
    	{
    		System.out.println("Activities are displayed with 1hr intervals");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
    	}
    	else
    	{
    		System.out.println("Activities are not displayed with 1hr intervals");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Activities not in each hr failed")).click();
    	}

		/*//store query into a variable
		String ActivityQuery=driver.findElement(By.xpath("//div[2]/ul/li[2]/div[2]/div/div[2]/input")).getAttribute("value").toString();
		System.out.println("ActivityQuery:"+ActivityQuery);
		
		//copy text from the output panel
		String Activityoutput=driver.findElement(By.xpath("//div[2]/ul/li[2]/div[2]/div/div[2]/input")).getText();
		System.out.println(Activityoutput);
		
		if(ActivityQuery.toLowerCase().contains(Activityoutput.toLowerCase()))
		{
			System.out.println("activity query match with output");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("activity query not match with output");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();

		}*/
		Thread.sleep(3000);
	}
	
	@Test(priority=7)
	public static void Logout()
	{
		//click on logout btn
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		//click on yes btn
		driver.findElement(By.id("logoutYESBtn")).click();
		
		//Close the browser
		driver.close();
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

