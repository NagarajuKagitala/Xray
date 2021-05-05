package GlobalRepositories;

import java.io.File;
import java.util.List;

import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
public class GlobalRepo_SampleIOTSports 
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
	    Thread.sleep(20000);
    }
    
    @TestRail(testCaseId=611)
    @Parameters({"sRepositories"})
   	@Test(priority=1)
    public static void SelectRepositories(String sRepositories,ITestContext context ) throws InterruptedException
    {
    	try
    	{
    		String repodata = ".//*[contains(@id," + "'" + sRepositories + "'" + ")]";
    		// Click on Dropdown
    		driver.findElement(By.cssSelector("span.select2-selection__arrow")).click();
    		driver.findElement(By.xpath(repodata)).click();
    		Thread.sleep(10000);
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Repo opened");
    	}	
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Repo not opened");
			driver.findElement(By.id("IOT_sports repo failed")).click();
		}		
    }
    @TestRail(testCaseId=878)
    @Test(priority=2)
    public static void Team_Events_Summary(ITestContext context) throws InterruptedException
    {
    	WebElement Events_By_country = (WebElement) driver.findElement(By.xpath("//div[@id='mCSB_1_container']/div/div"));
		List<WebElement> list = Events_By_country.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list) {
			String data=row.getText();
			if (data.contains("INFO")||data.contains("WARNING")) 
			{
				System.out.println(data);
				System.out.println("Team_Events_Summary working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Team_Events_Summary not working fine");
				context.setAttribute("Status",5); 
				context.setAttribute("Comment", "Failed");
	            driver.findElement(By.xpath("Not showing")).click();
			}
						
		}
    }
    
    
    @TestRail(testCaseId=879)
    @Test(priority=3)
    public static void Team_Events_Summary_Console(ITestContext context) throws InterruptedException
    {
    	WebElement Statistics_Summary = (WebElement) driver.findElement(By.xpath("//div[@id='mCSB_1_container']/div/div"));
		List<WebElement> list = Statistics_Summary.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list) {

			try {

				List<WebElement> div_ele = row.findElements(By.tagName("div"));

				for (WebElement we : div_ele) {
					String classname = we.getAttribute("class");

					if (classname.equalsIgnoreCase("ag-heading_body")) {
						Thread.sleep(5000);
				
						List<WebElement> events = we.findElements(By.tagName("span"));
						//System.out.println("events_size"+events.size());
						
						if(events.size()>0)
						{
							String event=events.get(0).getText();
							System.out.println("event count:"+event);
							
				
										
				String data = row.getText();
				if (!data.isEmpty()) 
				{
					row.click();
					Thread.sleep(5000);

					String Console_events = driver.findElement(By.xpath("//div[2]/li/div/div[3]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
					//System.out.println("console_counts: " + Console_events);

					String[] str = Console_events.split("of ");

					System.out.println("Console events:"+str[1]);
					
					if(event.equalsIgnoreCase(str[1]))
					{
						System.out.println("Team_Events_Summary event match with console");
						System.out.println("Passes_Summary working fine");
						context.setAttribute("Status",1);
						context.setAttribute("Comment", "working fine");
					}
					else
					{
						System.out.println("Team_Events_Summary Not event match with console");
						context.setAttribute("Status",5); 
						context.setAttribute("Comment", "Failed");
			            driver.findElement(By.xpath("Not showing")).click();
					}
					
					//click on close console
			  		driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
			  		Thread.sleep(3000);
										
				}
				
				}
						
					}
						
						}
						
				}				
		catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
			}		          
		}		
    }
    
    @TestRail(testCaseId=880)
	@Test(priority=4)
	public static void Passes_Summary(ITestContext context) throws InterruptedException
    {
		 //get event count on passes summary viewlet
		 
		 String data = driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/div/span")).getText();

			if (data.equalsIgnoreCase("268")) 
			{
				System.out.println(data);
				System.out.println("Passes_Summary working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("Passes_Summary not working fine");
				context.setAttribute("Status",5); 
				context.setAttribute("Comment", "Failed");
	            driver.findElement(By.xpath("Not showing")).click();
			}
   	}
    @TestRail(testCaseId=881)
    @Test(priority=5)
   	public static void Passes_Summary_console(ITestContext context) throws InterruptedException
   	{
   		//get activity count
   		String Activity_Count=driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/div/span")).getText();
   		System.out.println(Activity_Count);
   		
   		//click on activity count summary viewlet
   		driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/div/span")).click();
   		Thread.sleep(3000);
   		
   		//get records on console
   		String ActvityCount_Console=driver.findElement(By.xpath("//div[2]/li/div/div[3]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
   		System.out.println(ActvityCount_Console);
   		
   		String[] str = ActvityCount_Console.split("of ");
		System.out.println("Console events:"+str[1]);
        Thread.sleep(2000);
   		
   		//verify Activity count summary and console count
   		if(str[1].equalsIgnoreCase(Activity_Count))
   		{
   			System.out.println("Passes_Summary count showing same records on console");
   			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
   		}
   		else
   		{
   			System.out.println("Passes_Summary count viewlet not showing same records on console");
   			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
   		}
   		
   	    //click on console close icon
		driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
		Thread.sleep(3000);
   	}
		@TestRail(testCaseId=882)
		@Test(priority=6)
		public static void Activity_Summary_viewlet(ITestContext context) throws InterruptedException
	    {
			 //get event count on passes summary viewlet
			 
			 String data = driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/div/span")).getText();

				if (data.equalsIgnoreCase("268")) 
				{
					System.out.println(data);
					System.out.println("Activity_Summary_viewlet working fine");
					context.setAttribute("Status",1);
					context.setAttribute("Comment", "working fine");
				}
				else
				{
					System.out.println("Activity_Summary_viewlet not working fine");
					context.setAttribute("Status",5); 
					context.setAttribute("Comment", "Failed");
		            driver.findElement(By.xpath("Not showing")).click();
				}
	   	}
   	
    
    @TestRail(testCaseId=883)
   	@Test(priority=7)
   	public static void Activity_Count_Summary(ITestContext context) throws InterruptedException
   	{
   		//get activity count
   		String Activity_Count=driver.findElement(By.xpath("//div[3]/div[2]/div/div[2]/div/span")).getText();
   		System.out.println(Activity_Count);
   		
   		//click on activity count summary viewlet
   		driver.findElement(By.xpath("//div[3]/div[2]/div/div[2]/div/span")).click();
   		Thread.sleep(3000);
   		
   		//get records on console
   		String ActvityCount_Console=driver.findElement(By.xpath("//div[2]/li/div/div[3]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
   		System.out.println(ActvityCount_Console);
   		
   		String[] str = ActvityCount_Console.split("of ");
		System.out.println("Console events:"+str[1]);
        Thread.sleep(2000);
   		
   		//verify Activity count summary and console count
   		if(str[1].equalsIgnoreCase(Activity_Count))
   		{
   			System.out.println("Activity count summary viewlet showing same records on console");
   			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
   		}
   		else
   		{
   			System.out.println("Activity count summary viewlet not showing same records on console");
   			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
   		}
   		
   	    //click on console close icon
		driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
		Thread.sleep(3000);
   	}
       
	
    @TestRail(testCaseId=884)
    @Test(priority=8)
    public static void Statistics_Summary(ITestContext context) throws InterruptedException
    {
    	String data = driver.findElement(By.xpath("//div[@id='mCSB_1_container']/div/div[4]")).getText();

		if (data.contains("Measurements")||data.contains("Session")||data.contains("Shots")) 
		{
			System.out.println(data);
			System.out.println("Statistics_Summary working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Statistics_Summary not working fine");
			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
		}
    }
    
    
    @TestRail(testCaseId=885)
    @Test(priority=9)
    public static void Statistics_Summary_console(ITestContext context) throws InterruptedException
    {   			
		WebElement Statistics_Summary = (WebElement) driver.findElement(By.id("//div[@id='mCSB_1_container']/div/div[4]"));
		List<WebElement> list = Statistics_Summary.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list) {

			try {

				List<WebElement> div_ele = row.findElements(By.tagName("div"));

				for (WebElement we : div_ele) {
					String classname = we.getAttribute("class");

					if (classname.equalsIgnoreCase("ag-heading_body")) {
						Thread.sleep(5000);
				
						List<WebElement> events = we.findElements(By.tagName("span"));
						//System.out.println("events_size"+events.size());
						
						if(events.size()>0)
						{
							String event=events.get(0).getText();
							System.out.println("event count:"+event);
							
				
										
				String data1 = row.getText();
				if (!data1.isEmpty()) 
				{
					row.click();
					Thread.sleep(5000);

					String Console_events = driver.findElement(By.xpath("//div[2]/li/div/div[3]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
					//System.out.println("console_counts: " + Console_events);

					String[] str = Console_events.split("of ");

					System.out.println("Console events:"+str[1]);
					
					if(event.contains(str[1]))
					{
						System.out.println("Statistics_Summary event match with console");
						context.setAttribute("Status",1);
						context.setAttribute("Comment", "working fine");
					}
					else
					{
						System.out.println("Statistics_Summary Not event match with console");
						context.setAttribute("Status",5); 
						context.setAttribute("Comment", "Failed");
			            driver.findElement(By.xpath("Not showing")).click();
					}
					
					//click on close console
			  		driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
			  		Thread.sleep(3000);
										
				}
				
				}
						
					}
						
						}
						
				}				
		        catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
			}		          
		}		

   	} 
    @TestRail(testCaseId=618)
    @Test(priority=10)
    public static void Event_Scorecard(ITestContext context) throws InterruptedException
    {
    	try {

			WebElement elem = driver.findElement(By.xpath("//div[@id='pageContainer']/div/div/ul"));

			List<WebElement> myElements = elem.findElements(By.tagName("li"));
			System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-selected").toLowerCase().matches("true")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);

					// Read Dashboard data
					WebElement dash_tab = driver.findElement(By.cssSelector("*[class^='tab-div']"));

					WebElement middle_ele = dash_tab.findElement(By.className("middle-panel"));

					List<WebElement> div_ele = middle_ele.findElements(By.className("inner-column "));

					for (WebElement inner_ele : div_ele) {
					//	System.out.println("style: " + inner_ele.getAttribute("style"));

						String style_class = inner_ele.getAttribute("style");
						if (style_class.contains("display: block")) {

							List<WebElement> li_elelst = inner_ele.findElements(By.tagName("li"));
							System.out.println("li_elelst: " + li_elelst.size());
							StringBuilder buffer = new StringBuilder();
							for (WebElement e2 : li_elelst) {
								// boolean str= e.getAttribute("aria-hidden");

								WebElement ee = e2.findElement(By.className("viewlet-title-name-div"));
								String getViewletName = ee.findElement(By.tagName("input")).getAttribute("value");
								System.out.println("Viewlet name:" + getViewletName);
								if (getViewletName.equalsIgnoreCase("Event Scorecard ")) {
								//	System.out.println("HTML:" + ee.getAttribute("innerHTML"));
									String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
									System.out.println("eventcount:" + eventcount);
									if (!eventcount.isEmpty()) {
										String[] str = eventcount.split(" ");
										System.out.println("events: " + str[0]);
										int eventcounts = Integer.parseInt(str[0]);
										if (eventcount.equals("112 Events")) {
											System.out.println("Event count for scorcrad working fine");
											context.setAttribute("Status",1);
						 					context.setAttribute("Comment", "working fine");
										}
										else
										{
											System.out.println("Event count for scorcrad not working fine");
											context.setAttribute("Status",5); 
											context.setAttribute("Comment", "Failed");
								            driver.findElement(By.xpath("Not showing")).click();
										}
									}
								}

							}
						}

					}

					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
 

    }
    
    
    @TestRail(testCaseId=619)
   	@Test(priority=11)
   	public static void All_Events(ITestContext context)
   	{
    	//li[3]/div[2]/div[3]/div/div/div[3]/div[2]                     //li[3]/div[2]/div[3]/div/div/div[3]/div[3]
   		  WebElement All_Event=(WebElement) driver.findElement(By.xpath("//li[3]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
   		  List<WebElement> lst=All_Event.findElements(By.className("jqgrow"));
   		  System.out.println("ALLevent: "+ lst.size());
   		  for(WebElement row : lst)
   		  {
   			 List<WebElement> lstwele=row.findElements(By.tagName("td"));
   			 for(WebElement td :lstwele)
   			 {
   				 	    	 
   				 String data=td.getText();
				 if(!data.isEmpty())
	 				{
	 					String data1=td.getText();
	 					System.out.println(data1);
	 					if(data1.contains("RECEIVE"))
	 					{
	 						System.out.println("All_Events showing result");
	 						context.setAttribute("Status",1);
		 					context.setAttribute("Comment", "working fine");
		 					
	 					}
			
			       else {
			 
			                System.out.println("All_Events not showing result");
			                context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
			                driver.findElement(By.xpath("Not showing")).click();
			  
			  
			  }  			     
   	 		 }
				 break;
   			        			     
   			 }
   			 break;
   		  }
        
    }    
   	
    @TestRail(testCaseId=620)
   	@Test(priority=12)
   	public static void Event_Stackchart(ITestContext context) throws InterruptedException
   	{
         
        //move to mouse stackchart tooltip
    	try
   		{
   		WebElement elem = driver.findElement(By.xpath("//div[@id='pageContainer']/div/div/ul"));

		List<WebElement> myElements = elem.findElements(By.tagName("li"));
		System.out.println(myElements.size());
		for (WebElement e : myElements) {
			if (e.getAttribute("aria-selected").toLowerCase().matches("true")) {
				// Place a pointer on x-axis to check event details
				// Instantiate Action Class
				Actions actions = new Actions(driver);

				// Read Dashboard data
				WebElement dash_tab = driver.findElement(By.cssSelector("*[class^='tab-div']"));

				WebElement middle_ele = dash_tab.findElement(By.className("middle-panel"));

				List<WebElement> div_ele = middle_ele.findElements(By.className("inner-column "));

				for (WebElement inner_ele : div_ele) {
					System.out.println("style: " + inner_ele.getAttribute("style"));

					String style_class = inner_ele.getAttribute("style");
					if (style_class.contains("display: block")) {

						List<WebElement> li_elelst = inner_ele.findElements(By.tagName("li"));
						System.out.println("li_elelst: " + li_elelst.size());
						StringBuilder buffer = new StringBuilder();
						boolean is_tooltippresent = false;
						for (WebElement e2 : li_elelst) {
							// boolean str= e.getAttribute("aria-hidden");

							WebElement ee = e2.findElement(By.className("viewlet-title-name-div"));
							String getViewletName = ee.findElement(By.tagName("input")).getAttribute("value");
							System.out.println("Viewlet name:" + getViewletName);
							
							if (getViewletName.equalsIgnoreCase("Event StackChart")) {
								System.out.println("HTML:" + ee.getAttribute("innerHTML"));
								WebElement div = e2.findElement(By.className("viewlet-body"));
								WebElement view_ele = div.findElement(By.className("content-wrapper")).findElement(By.tagName("div"));
								// Retrieve WebElement
								WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

								List<WebElement> w_ele = ele.findElements(By.tagName("g"));
								System.out.println(w_ele.size());
								int i = 0;
								for (WebElement we : w_ele) {

									try {
										i++;
										System.out.println("index: " + i++);

										List<WebElement> inner_we = we.findElements(By.tagName("g"));
										

										for (WebElement chart_ele : inner_we) {

											List<WebElement> pointer = chart_ele.findElements(By.tagName("path"));

											for (WebElement p : pointer) {
												

												if (p.getAttribute("class").contains("amcharts-graph-column-front amcharts-graph-column-element")) {

													actions.moveToElement(p).build().perform();

													Thread.sleep(5000);
													WebElement charts_div = view_ele
															.findElement(By.className("amcharts-chart-div"));

													try {
														WebElement tooltip = charts_div
																.findElement(By.tagName("div"));
														String events_count = tooltip.getText();

														System.out.println("Tooltip text: " + events_count);

														if (events_count !=null) {
															System.out.println(
																	"Event_Stackchart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Event_Stackchart plotting events not working fine. ");
											                context.setAttribute("Status",5);
											                context.setAttribute("Comment", "Failed");
											                driver.findElement(By.xpath("Not showing")).click();	
														}
													} catch (NoSuchElementException se) {
														System.out.println("Tooltip div not found");
													}

													Thread.sleep(1000);

													// return;
												}

												if (is_tooltippresent) {
													break;
												}
											}

											if (is_tooltippresent) {
												break;
											}

										}

										if (is_tooltippresent) {
											break;
										}

									} catch (NoSuchElementException ex) {
										// Element is not present
										System.out.println("Element not present  " + ex.getMessage());
									}
								}
							}
						}
						
					}

				}
				

			}
		}
   	
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
   	
    @TestRail(testCaseId=621)
   	@Test(priority=13)
   	public static void Event_Severity(ITestContext context) throws InterruptedException
   	{
   		//mouse over on the paichart
   		try
   		{
   		WebElement elem = driver.findElement(By.xpath("//div[@id='pageContainer']/div/div/ul"));

		List<WebElement> myElements = elem.findElements(By.tagName("li"));
		System.out.println(myElements.size());
		for (WebElement e : myElements) {
			if (e.getAttribute("aria-selected").toLowerCase().matches("true")) {
				// Place a pointer on x-axis to check event details
				// Instantiate Action Class
				Actions actions = new Actions(driver);

				// Read Dashboard data
				WebElement dash_tab = driver.findElement(By.cssSelector("*[class^='tab-div']"));

				WebElement middle_ele = dash_tab.findElement(By.className("middle-panel"));

				List<WebElement> div_ele = middle_ele.findElements(By.className("inner-column "));

				for (WebElement inner_ele : div_ele) {
					System.out.println("style: " + inner_ele.getAttribute("style"));

					String style_class = inner_ele.getAttribute("style");
					if (style_class.contains("display: block")) {

						List<WebElement> li_elelst = inner_ele.findElements(By.tagName("li"));
						System.out.println("li_elelst: " + li_elelst.size());
						StringBuilder buffer = new StringBuilder();
						boolean is_tooltippresent = false;
						for (WebElement e2 : li_elelst) {
							// boolean str= e.getAttribute("aria-hidden");

							WebElement ee = e2.findElement(By.className("viewlet-title-name-div"));
							String getViewletName = ee.findElement(By.tagName("input")).getAttribute("value");
							System.out.println("Viewlet name:" + getViewletName);
							
							if (getViewletName.equalsIgnoreCase("Event Severity ")) {
								//System.out.println("HTML:" + ee.getAttribute("innerHTML"));
								WebElement div = e2.findElement(By.className("viewlet-body"));
								WebElement view_ele = div.findElement(By.className("content-wrapper")).findElement(By.tagName("div"));
								// Retrieve WebElement
								WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

								List<WebElement> w_ele = ele.findElements(By.tagName("g"));
								System.out.println(w_ele.size());
								int i = 0;
								for (WebElement we : w_ele) {

									try {
										i++;
										System.out.println("index: " + i++);

										List<WebElement> inner_we = we.findElements(By.tagName("g"));

										for (WebElement chart_ele : inner_we) {

											List<WebElement> pointer = chart_ele.findElements(By.tagName("path"));

											for (WebElement p : pointer) {

												if (p.getAttribute("class").contains("amcharts-pie-slice")) {

													actions.moveToElement(p).build().perform();

													Thread.sleep(5000);
													WebElement charts_div = view_ele
															.findElement(By.className("amcharts-chart-div"));

													try {
														WebElement tooltip = charts_div
																.findElement(By.tagName("div"));
														String events_count = tooltip.getText();

														System.out.println("Tooltip text: " + events_count);

														if (events_count != null) {
															System.out.println(
																	"Event_Severity pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"Event_Severity pie-chart plotting events not working fine");
															context.setAttribute("Status",5); 
															context.setAttribute("Comment", "Failed");
											                driver.findElement(By.xpath("Not showing")).click();
														}
													} catch (NoSuchElementException se) {
														System.out.println("Tooltip div not found");
													}

													Thread.sleep(1000);

													// return;
												}

												if (is_tooltippresent) {
													break;
												}
											}

											if (is_tooltippresent) {
												break;
											}

										}

										if (is_tooltippresent) {
											break;
										}

									} catch (NoSuchElementException ex) {
										// Element is not present
										System.out.println("Element not present  " + ex.getMessage());
									}
								}
							}
						}
						
					}

				}
				

				
			}
		}
   	
	} catch (Exception e) {
		e.printStackTrace();
	}
        
   	}
   	
   	
    @TestRail(testCaseId=622)
   	@Test(priority=14)
   	public static void Event_Severity_Levels(ITestContext context) throws InterruptedException
    {
    	
    	try
   		{
   		WebElement elem = driver.findElement(By.xpath("//div[@id='pageContainer']/div/div/ul"));

		List<WebElement> myElements = elem.findElements(By.tagName("li"));
		System.out.println(myElements.size());
		for (WebElement e : myElements) {
			if (e.getAttribute("aria-selected").toLowerCase().matches("true")) {
				// Place a pointer on x-axis to check event details
				// Instantiate Action Class
				Actions actions = new Actions(driver);

				// Read Dashboard data
				List<WebElement> dash_tab = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				
				for(WebElement tab_active:dash_tab)
				{
					String tabstyle_class = tab_active.getAttribute("aria-hidden");
					
					if(tabstyle_class.toLowerCase().matches("false"))
					{

				WebElement middle_ele = tab_active.findElement(By.className("middle-panel"));

				List<WebElement> div_ele = middle_ele.findElements(By.className("inner-column "));

				for (WebElement inner_ele : div_ele) {
					System.out.println("style: " + inner_ele.getAttribute("style"));

					String style_class = inner_ele.getAttribute("style");
					if (style_class.contains("display: block")) {

						List<WebElement> li_elelst = inner_ele.findElements(By.tagName("li"));
						System.out.println("li_elelst: " + li_elelst.size());
						StringBuilder buffer = new StringBuilder();
						boolean is_tooltippresent = false;
						for (WebElement e2 : li_elelst) {
							// boolean str= e.getAttribute("aria-hidden");

							WebElement ee = e2.findElement(By.className("viewlet-title-name-div"));
							String getViewletName = ee.findElement(By.tagName("input")).getAttribute("value");
							System.out.println("Viewlet name:" + getViewletName);
							
							if (getViewletName.equalsIgnoreCase("Event Severity Levels")) {
								System.out.println("HTML:" + ee.getAttribute("innerHTML"));
								WebElement div = e2.findElement(By.className("viewlet-body"));
								WebElement view_ele = div.findElement(By.className("content-wrapper")).findElement(By.tagName("div"));
								// Retrieve WebElement
								WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

								List<WebElement> w_ele = ele.findElements(By.tagName("g"));
								System.out.println(w_ele.size());
								int i = 0;
								for (WebElement we : w_ele) {

									try {
										i++;
										System.out.println("index: " + i++);

										List<WebElement> inner_we = we.findElements(By.tagName("g"));

										for (WebElement chart_ele : inner_we) {

											List<WebElement> pointer = chart_ele.findElements(By.tagName("circle"));

											for (WebElement p : pointer) {

												if (p.getAttribute("class").contains("amcharts-graph-bullet")) {

													actions.moveToElement(p).build().perform();

													Thread.sleep(5000);
													WebElement charts_div = view_ele
															.findElement(By.className("amcharts-plot-area"));

													try {
														WebElement tooltip = charts_div
																.findElement(By.tagName("div"));
														String events_count = tooltip.getText();

														System.out.println("Tooltip text: " + events_count);

														if (events_count !=null) {
															System.out.println(
																	"Event_Severity_Levels plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Event_Severity_Levels plotting events not working fine. ");
											                context.setAttribute("Status",5);
											                context.setAttribute("Comment", "Failed");
											                driver.findElement(By.xpath("Not showing")).click();	
														}
													} catch (NoSuchElementException se) {
														System.out.println("Tooltip div not found");
													}

													Thread.sleep(1000);

													// return;
												}

												if (is_tooltippresent) {
													break;
												}
											}
											

											if (is_tooltippresent) {
												break;
											}

									

										if (is_tooltippresent) {
											break;
										}
										}	
										

									} catch (NoSuchElementException ex) {
										// Element is not present
										System.out.println("Element not present  " + ex.getMessage());
									}
								}
							}
						}
						
					}

				}
				

			}
		}
			}
		}
   	
	} catch (Exception e) {
		e.printStackTrace();
	}

    }
   	
   	
    @TestRail(testCaseId=623)
   	@Test(priority=15)
   	public static void  Events_by_the_Minute(ITestContext context)
    {
    	try
   		{
   		WebElement elem = driver.findElement(By.xpath("//div[@id='pageContainer']/div/div/ul"));

		List<WebElement> myElements = elem.findElements(By.tagName("li"));
		System.out.println(myElements.size());
		for (WebElement e : myElements) {
			if (e.getAttribute("aria-selected").toLowerCase().matches("true")) {
				// Place a pointer on x-axis to check event details
				// Instantiate Action Class
				Actions actions = new Actions(driver);

				// Read Dashboard data
				WebElement dash_tab = driver.findElement(By.cssSelector("*[class^='tab-div']"));

				WebElement middle_ele = dash_tab.findElement(By.className("middle-panel"));

				List<WebElement> div_ele = middle_ele.findElements(By.className("inner-column "));

				for (WebElement inner_ele : div_ele) {
					System.out.println("style: " + inner_ele.getAttribute("style"));

					String style_class = inner_ele.getAttribute("style");
					if (style_class.contains("display: block")) {

						List<WebElement> li_elelst = inner_ele.findElements(By.tagName("li"));
						System.out.println("li_elelst: " + li_elelst.size());
						StringBuilder buffer = new StringBuilder();
						boolean is_tooltippresent = false;
						for (WebElement e2 : li_elelst) {
							// boolean str= e.getAttribute("aria-hidden");

							WebElement ee = e2.findElement(By.className("viewlet-title-name-div"));
							String getViewletName = ee.findElement(By.tagName("input")).getAttribute("value");
							System.out.println("Viewlet name:" + getViewletName);
							
							if (getViewletName.equalsIgnoreCase("Events by the Minute ")) {
								System.out.println("HTML:" + ee.getAttribute("innerHTML"));
								WebElement div = e2.findElement(By.className("viewlet-body"));
								WebElement view_ele = div.findElement(By.className("content-wrapper")).findElement(By.tagName("div"));
								// Retrieve WebElement
								WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

								List<WebElement> w_ele = ele.findElements(By.tagName("g"));
								System.out.println(w_ele.size());
								int i = 0;
								for (WebElement we : w_ele) {

									try {
										i++;
										System.out.println("index: " + i++);

										List<WebElement> inner_we = we.findElements(By.tagName("g"));

										for (WebElement chart_ele : inner_we) {

											List<WebElement> pointer = chart_ele.findElements(By.tagName("circle"));

											for (WebElement p : pointer) {

												if (p.getAttribute("class").contains("amcharts-graph-bullet")) {

													actions.moveToElement(p).build().perform();

													Thread.sleep(5000);
													WebElement charts_div = view_ele
															.findElement(By.className("amcharts-chart-div"));

													try {
														WebElement tooltip = charts_div
																.findElement(By.tagName("div"));
														String events_count = tooltip.getText();

														System.out.println("Tooltip text: " + events_count);

														if (events_count != null) {
															System.out.println(
																	"Events_by_the_Minute plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"Events_by_the_Minute plotting events not working fine");
															context.setAttribute("Status",5); 
															context.setAttribute("Comment", "Failed");
											                driver.findElement(By.xpath("Not showing")).click();
														}
													} catch (NoSuchElementException se) {
														System.out.println("Tooltip div not found");
													}

													Thread.sleep(1000);

													// return;
												}

												if (is_tooltippresent) {
													break;
												}
											}

											if (is_tooltippresent) {
												break;
											}

										}

										if (is_tooltippresent) {
											break;
										}

									} catch (NoSuchElementException ex) {
										// Element is not present
										System.out.println("Element not present  " + ex.getMessage());
									}
								}
							}
						}
						
					}

				}
				

			}
		}
   	
	} catch (Exception e) {
		e.printStackTrace();
	}
	
    }
   	
   	
   	
   	@Test(priority=16)
   	public static void Logout()
   	{
   		//click on logout btn
   		driver.findElement(By.cssSelector(".fa-power-off")).click();
   		//click on yes btn
   		driver.findElement(By.id("logoutYESBtn")).click();
   			
   		//close browser
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
    
      

