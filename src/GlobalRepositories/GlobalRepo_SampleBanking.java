package GlobalRepositories;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
public class GlobalRepo_SampleBanking 
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
	    Thread.sleep(15000);
    }
    @TestRail(testCaseId=563)
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
			driver.findElement(By.id("Banking repo failed")).click();
		}	
    }
    
    @TestRail(testCaseId=868)
	@Test(priority=14)
	public static void Business_View_Summary(ITestContext context) throws InterruptedException
    {
    	//clcik on business view dashboard
    	driver.findElement(By.xpath("//span[contains(.,'Business View')]")).click();
    	Thread.sleep(15000);
    	
		String data = driver.findElement(By.xpath("//div[@id='mCSB_2_container']/div/div")).getText();

		if (data.contains("ALL")||data.contains("PARTIAL")||data.contains("NONE")) 
		{
			System.out.println(data);
			System.out.println("Business_View_Summary working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Business_View_Summary not working fine");
			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
		}
		
    }
	
    
	@TestRail(testCaseId=869)
	@Test(priority=15)
	public static void  Business_View_Summary_Counts(ITestContext context) throws InterruptedException
	{
		WebElement Business_View_Summary = (WebElement) driver.findElement(By.xpath("//div[@id='mCSB_2_container']/div/div"));
		List<WebElement> list = Business_View_Summary.findElements(By.className("summary-child-div"));
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
					
					if(str[1].contains(event))

					{
						System.out.println("Business_View_Summary_Counts working fine");
						context.setAttribute("Status",1);
						context.setAttribute("Comment", "working fine");
					}
					else
					{
						System.out.println("Business_View_Summary_Counts not working fine");
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
	
	@TestRail(testCaseId=870)
	@Test(priority=16)
	public static void complete_activities(ITestContext context) throws InterruptedException
    {
	  	   Thread.sleep(3000);
	  	 String data = driver.findElement(By.xpath("//div[2]/div/ul/li/div/div/div/div/div[3]/div[2]/div/div[2]/div/span")).getText();
	  	 System.out.println("complete_activities data:"+data);

	  	if (data.equalsIgnoreCase("3.7K")) 
		{
			System.out.println(data);
			System.out.println("complete_activities working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("complete_activities not working fine");
			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
		}
			
    }
	
	@TestRail(testCaseId=871)
	@Test(priority=17)
	public static void complete_activities_ConsoleCounts(ITestContext context) throws InterruptedException
    {
	  	 Thread.sleep(2000);
		 //get records of completed summary viewlet
		 String completedsummary=driver.findElement(By.xpath("//div[2]/div/ul/li/div/div/div/div/div[3]/div[2]/div/div[2]/div/span")).getText();
		 System.out.println("Activites count:"+completedsummary);
		 Thread.sleep(1000);
		 //click on completed summary viewlet
		 driver.findElement(By.xpath("//div[2]/div/ul/li/div/div/div/div/div[3]/div[2]/div/div[2]/div/span")).click();
		 Thread.sleep(2000);
		 //verify value on console
	  	   String completedActCount=driver.findElement(By.xpath("//div[2]/li/div/div[3]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
	       //System.out.println(completedActCount);
	  	   
	  	   String[] str = completedActCount.split("of ");
			System.out.println("Console events:"+str[1]);
	       Thread.sleep(2000);
	       
	       if(str[1].equalsIgnoreCase("3,702"))

			{
				System.out.println("complete_activities_ConsoleCounts working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("complete_activities_ConsoleCounts not working fine");
				context.setAttribute("Status",5); 
				context.setAttribute("Comment", "Failed");
	            driver.findElement(By.xpath("Not showing")).click();
			}
	       
	       //click on close cosole close btn
		   driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
		   Thread.sleep(2000);
	  
		 
    }
	
	@TestRail(testCaseId=872)
	@Test(priority=18)
	public static void SLA_Violations(ITestContext context) throws InterruptedException
    {
		     Thread.sleep(3000);
		  	 String SLA_Violations_count = driver.findElement(By.xpath("//div[2]/div/ul/li/div/div/div/div/div[2]/div[2]/div/div[2]/div/span")).getText();
		  	 System.out.println("SLA_Violations_count:"+SLA_Violations_count);

		  	if (SLA_Violations_count.contains("498")) 
			{
				System.out.println("SLA_Violations working fine");
				context.setAttribute("Status",1);
				context.setAttribute("Comment", "working fine");
			}
			else
			{
				System.out.println("SLA_Violations not working fine");
				context.setAttribute("Status",5); 
				context.setAttribute("Comment", "Failed");
	            driver.findElement(By.xpath("Not showing")).click();
			}
    }
	
	@TestRail(testCaseId=873)
	@Test(priority=19)
	public static void SLA_Violations_consoleCounts(ITestContext context) throws InterruptedException
    {
		   //get records of violations summary viewlet
	   	   String SLAcount=driver.findElement(By.xpath("//div[2]/div/ul/li/div/div/div/div/div[2]/div[2]/div/div[2]/div/span")).getText();
	   	   System.out.println("SLA Count:"+SLAcount);
	   	   //click on SLA violations summary viewlet
	   	   driver.findElement(By.xpath("//div[2]/div/ul/li/div/div/div/div/div[2]/div[2]/div/div[2]/div/span")).click();
	   	   Thread.sleep(3000);
	   	   //verify value on console
	   	   String slaActCount=driver.findElement(By.xpath("//div[2]/li/div/div[3]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
	   	   System.out.println(slaActCount);
	   	  String[] str = slaActCount.split("of ");
		  System.out.println("Console events:"+str[1]);

	   	   Thread.sleep(2000);
	   	   
	   	  if(str[1].equals(SLAcount))

		  {
			  System.out.println("SLA_Violations_consoleCounts working fine");
			  context.setAttribute("Status",1);
			  context.setAttribute("Comment", "working fine");
		  }
		  else
		  {
			  System.out.println("SLA_Violations_consoleCounts not working fine");
			  context.setAttribute("Status",5); 
			  context.setAttribute("Comment", "Failed");
              driver.findElement(By.xpath("Not showing")).click();
		  }

	       //click on close cosole close btn
		   driver.findElement(By.xpath("//span[contains(.,'Close Viewlet')]")).click();
	   	   Thread.sleep(2000); 	   
   	}
	
	@TestRail(testCaseId=570)
	@Test(priority=8)
	public static void Viewlet_3(ITestContext context)
	{
		//get query of viewlet_3
		String Viewlet3query=driver.findElement(By.xpath("//div[2]/input")).getAttribute("value").toString();
		System.out.println(Viewlet3query);
		//get value of output console
		String outputConsole1=driver.findElement(By.cssSelector(".ui-jqgrid-hdiv")).getText();
		String outputConsole2=outputConsole1.trim();
		System.out.println(outputConsole2);
		//verify query and output value
		if(Viewlet3query.contains(outputConsole2))
		{
			System.out.println("viewlet3 query match with output");
		}
		else
		{
			System.out.println("viewlet3 query not match with output");

		}
	}
	//-------------------Trasury market trade dahboard--------------------------------------------
	@TestRail(testCaseId=874)
	@Test(priority=3)
	public static void Treasury_market_summary_viewlet(ITestContext context) throws InterruptedException
	{
		//click on treasury market dasboard
//   		driver.findElement(By.xpath("//span[contains(.,'Treasury Markets Trade cycle')]")).click();
//   		Thread.sleep(15000);
   		
   		String data = driver.findElement(By.xpath("//div[@id='mCSB_1_container']/div/div[3]")).getText();

		if (data.contains("ALL")||data.contains("PARTIAL")||data.contains("NONE")) 
		{
			System.out.println(data);
			System.out.println("Treasury_market_summary_viewlet working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Treasury_market_summary_viewlet not working fine");
			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
		}
	}
	
	@TestRail(testCaseId=875)
	@Test(priority=4)
	public static void Treasury_market_SLA_Violations(ITestContext context) throws InterruptedException
    {	
		 String data = driver.findElement(By.xpath("//div[2]/div/div[2]/div/span")).getText();
	  	 System.out.println("data:"+data);

	  	if (data.equalsIgnoreCase("498")) 
		{
			System.out.println("Treasury_market_SLA_Violations working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Treasury_market_SLA_Violations not working fine");
			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
		}			
	}
	
	@TestRail(testCaseId=876)
	@Test(priority=5)
	public static void Treasury_market_Message_Volume(ITestContext context) throws InterruptedException
    {
		 String data = driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/div/span")).getText();
	  	 System.out.println("data:"+data);

	  	if (data.equalsIgnoreCase("0")) 
		{
			System.out.println("Treasury_market_Message_Volume working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Treasury_market_Message_Volume not working fine");
			context.setAttribute("Status",5); 
			context.setAttribute("Comment", "Failed");
            driver.findElement(By.xpath("Not showing")).click();
		}			
    }
	@TestRail(testCaseId=877)
	@Test(priority=6)
	public static void counterparty_SLA(ITestContext context) throws InterruptedException
	{
	    			
	 		//move to mouse tooltip 
	   		try
	   		{
	   		WebElement elem = driver.findElement(By.xpath("//div[@id='pageContainer']/div/div/ul"));

			List<WebElement> myElements = elem.findElements(By.tagName("li"));
			System.out.println(myElements.size());
			for (WebElement e : myElements) {
				System.out.println(e.getAttribute("aria-selected"));
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
								
								if (getViewletName.equalsIgnoreCase("Counterparty SLA")) {
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

															if (events_count != null) {
																System.out.println(
																		"counterparty_SLA plotting events working fine. ");
																is_tooltippresent = true;
																context.setAttribute("Status",1);
											 					context.setAttribute("Comment", "working fine");
															}
															else
															{
																System.out.println(
																		"counterparty_SLA plotting events not working fine. ");
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
				}
			}
			
	   	
		} catch (Exception e) {
			e.printStackTrace();
	 }
	    	
	 }
	
	 @TestRail(testCaseId=574)
	 @Test(priority=7)
	 public static void ReconcileSummary(ITestContext context) throws InterruptedException
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
						List<WebElement> dash_tab = driver.findElements(By.cssSelector("*[class^='tab-div']"));
						
						for(WebElement tab_active:dash_tab)
						{
							String tabstyle_class = tab_active.getAttribute("aria-hidden");
							
							if(tabstyle_class.toLowerCase().matches("false"))
							{
								
						
						WebElement middle_ele = tab_active.findElement(By.className("middle-panel"));

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
									if (getViewletName.equalsIgnoreCase("Reconcile Summary")) {
									//	System.out.println("HTML:" + ee.getAttribute("innerHTML"));
										String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
										System.out.println("eventcount:" + eventcount);
										if (!eventcount.isEmpty()) {
											String[] str = eventcount.split(" ");
											System.out.println("events: " + str[0]);
											int eventcounts = Integer.parseInt(str[0]);
											if (eventcount.equals("6285 Activities")) {
												System.out.println("Reconcile Summary for scorcrad working fine");
												context.setAttribute("Status",1);
							 					context.setAttribute("Comment", "working fine");
											}
											else
											{
												System.out.println("Reconcile Summary for scorcrad not working fine");
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
					}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	 

	    }
	    
	    
	    @TestRail(testCaseId=619)
	   	@Test(priority=8)
	   	public static void All_Events(ITestContext context)
	   	{
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
		 					if(data1.contains("9d6f35c2-21e4-11e6-b8d3-00259035f22d"))
		 					{
		 						System.out.println("All_Events showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				       else {
				 
				                System.out.println("All_Events not showing result");
				                context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
				                driver.findElement(By.xpath("Not showing")).click();
				  
				  
				  }  			     
	   	 		 } 
	   			        			     
	   			 }
	   			 break;
	   		  }
			

	} 
	 
	 
	@TestRail(testCaseId=575)
	@Test(priority=9)
	public static void Transaction_Tracking_Summary(ITestContext context) throws InterruptedException
    {
		try
		{
		WebElement we=(WebElement) driver.findElement(By.xpath("//div[2]/ul/li[3]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
		List<WebElement> lst=we.findElements(By.className("jqgrow"));
		System.out.println("TrackingSummaryRowSize: "+ lst.size());
		for(WebElement row : lst)
		{
			 List<WebElement> lstwele=row.findElements(By.tagName("td"));
			// System.out.println("td size: "+ lstwele.size());
			 for(WebElement td :lstwele)
			 {
				 Thread.sleep(2000);
				 String data=td.getText();
				 if(!data.isEmpty())
	 				{
	 					String data1=td.getText();
	 					System.out.println(data1);
	 					if(data1.contains("36"))
	 					{
	 						System.out.println("Transaction_Tracking_Summary showing result");
	 						context.setAttribute("Status",1);
		 					context.setAttribute("Comment", "working fine");
		 					break;
	 					}
	 					
			
			
			  else {
			 
			  System.out.println("Transaction_Tracking_Summary not showing result");
			  context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
			  driver.findElement(By.xpath("Not showing")).click();
			  
			  
			  }
	 					//break;	
	 				}
				// break;
					
			        
			 }
			break; 
		}
		
		
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
    }
	
	@TestRail(testCaseId=576)
    @Test(priority=10)
    public static void  Payment_in_Progress(ITestContext context) throws InterruptedException
	{
    	
		//get query 
		String Paymentquery=driver.findElement(By.xpath("//li[3]/div[2]/div/div/jkql-input/input")).getAttribute("value").toString();
		System.out.println(Paymentquery);
		Thread.sleep(2000);
		//get columns of Payment_in_Progress viewlet
		String columns=driver.findElement(By.xpath("//li[3]/div[2]/div[3]/div/div/div[3]/div[2]/div/table/thead/tr/th[3]")).getText();
		String Tablecolumns=columns.replaceAll("[\r\n]+",",");
		System.out.println(Tablecolumns);
		if(Paymentquery.contains("'SETTLEMENT_DATE'"))
		{
			System.out.println("output console showing same column");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("output console not showing same column");
			context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
	         driver.findElement(By.xpath("Not showing")).click();

		}			
   }   
    
   @TestRail(testCaseId=577)
   @Parameters({"AD_HOCK_Transaction_Analysis_Query"})
   @Test(priority=11)
   public static void AD_HOCK_Transaction_Analysis(String AD_HOCK_Transaction_Analysis_Query, ITestContext context) throws InterruptedException
	{
	 
	   WebElement we=(WebElement) driver.findElement(By.xpath("//li[5]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
		List<WebElement> lst=we.findElements(By.className("jqgrow"));
		System.out.println("TableRowSize: "+ lst.size());
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
	 				if(data1.contains("Send DupConf to TRAM"))
	 				{
	 					//System.out.println(td.getText().replace("&nbsp;", "").replace(" ", "")); 
	 					System.out.println("AD_HOCK_Transaction_Analysis showing result");
	 					context.setAttribute("Status",1);
	 					context.setAttribute("Comment", "working fine");
	 					break;
	 					
	 				}  
	 				else
	 				{
	 					System.out.println("AD_HOCK_Transaction_Analysis not showing result");
			            context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
			            driver.findElement(By.xpath("Not showing")).click();
	 				}
	 		 }
			 
		}
			 break;
		}

    }
    
    @TestRail(testCaseId=578)
	@Test(priority=12)
	public static void Busines_step(ITestContext context) throws InterruptedException
	{
		
		 WebElement we=(WebElement) driver.findElement(By.xpath("//li[4]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("Busines_TableRowSize: "+ lst.size());
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
		 				
		 				if(data1.contains("Banking_Sets _definition-jKoolDataExport"))
		 				{
		 					//System.out.println(td.getAttribute("title").replace("&nbsp;", "").replace(" ", ""));
		 					System.out.println("Busines_step showing result");
		 					context.setAttribute("Status",1);
		 					context.setAttribute("Comment", "working fine");
		 					break;
		 				}  
		 				else
		 				{
		 					System.out.println("Busines_step not showing result");
				            context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
				            driver.findElement(By.xpath("Not showing")).click();
		 				}
				 }
			}	
				 break;
			}
		
	} 
	
    @TestRail(testCaseId=579)
    @Test(priority=13)
    public static void AD_HOCK_Transaction_Analysis2(ITestContext context) throws InterruptedException
    {
    			
 		//move to mouse barchart tooltip 
   		try
   		{
   		WebElement elem = driver.findElement(By.xpath("//div[@id='pageContainer']/div/div/ul"));

		List<WebElement> myElements = elem.findElements(By.tagName("li"));
		System.out.println(myElements.size());
		for (WebElement e : myElements) {
			System.out.println(e.getAttribute("aria-selected"));
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
							
							if (getViewletName.equalsIgnoreCase("AD-HOCK Transcaction Analysis 2")) {
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

														if (events_count != null) {
															System.out.println(
																	"AD_HOCK_Transaction_Analysis2 plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"AD_HOCK_Transaction_Analysis2 plotting events not working fine. ");
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
			}
		}
		
   	
	} catch (Exception e) {
		e.printStackTrace();
	}
    	
    }
    //----------------App Support dashboard--------------------------------
    @TestRail(testCaseId=838)
    @Test(priority=20)
    public static void failed_transaction(ITestContext context) throws InterruptedException
    {
    	//click on app support dashboard
    	driver.findElement(By.xpath("//span[contains(.,'AppSupport')]")).click();
    	Thread.sleep(20000);
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
					List<WebElement> dash_tab = driver.findElements(By.cssSelector("*[class^='tab-div']"));
					
					for(WebElement tab_active:dash_tab)
					{
						String tabstyle_class = tab_active.getAttribute("aria-hidden");
						
						if(tabstyle_class.toLowerCase().matches("false"))
						{
							
					
					WebElement middle_ele = tab_active.findElement(By.className("middle-panel"));

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
								if (getViewletName.equalsIgnoreCase("Failed Transactions ")) {
									System.out.println("HTML:" + ee.getAttribute("innerHTML"));
									String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
									System.out.println("eventcount:" + eventcount);
									if (!eventcount.isEmpty()) {
										String[] str = eventcount.split(" ");
										System.out.println("events: " + str[0]);
										int eventcounts = Integer.parseInt(str[0]);
										if (eventcount.equals("522 Events")) {
											System.out.println("failed_transaction scorcard working fine");
											context.setAttribute("Status",1);
						 					context.setAttribute("Comment", "working fine");
										}
										else
										{
											System.out.println("failed_transaction scorcard  not working fine");
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
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
 

    }
    @TestRail(testCaseId=839)
    @Test(priority=21)
    public static void send_recive_message_countByapp_recon(ITestContext context) throws InterruptedException
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
					List<WebElement> dash_tab = driver.findElements(By.cssSelector("*[class^='tab-div']"));
					
					for(WebElement tab_active:dash_tab)
					{
						String tabstyle_class = tab_active.getAttribute("aria-hidden");
						
						if(tabstyle_class.toLowerCase().matches("false"))
						{
							
					
					WebElement middle_ele = tab_active.findElement(By.className("middle-panel"));

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
								if (getViewletName.equalsIgnoreCase("Send & Receive Message Count by Application-RECON ")) {
								//	System.out.println("HTML:" + ee.getAttribute("innerHTML"));
									String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
									System.out.println("eventcount:" + eventcount);
									if (!eventcount.isEmpty()) {
										String[] str = eventcount.split(" ");
										System.out.println("events: " + str[0]);
										int eventcounts = Integer.parseInt(str[0]);
										if (eventcount.equals("8350 Events")) {
											System.out.println("send_recive_message_countByapp_recon scorcard working fine");
											context.setAttribute("Status",1);
						 					context.setAttribute("Comment", "working fine");
										}
										else
										{
											System.out.println("send_recive_message_countByapp_recon scorcard  not working fine");
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
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    @TestRail(testCaseId=840)
    @Test(priority=22)
    public static void list_ofLong_running_operation(ITestContext context) throws InterruptedException
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
    						List<WebElement> dash_tab = driver.findElements(By.cssSelector("*[class^='tab-div']"));
    						
    						for(WebElement tab_active:dash_tab)
    						{
    							String tabstyle_class = tab_active.getAttribute("aria-hidden");
    							
    							if(tabstyle_class.toLowerCase().matches("false"))
    							{
    								
    						
    						WebElement middle_ele = tab_active.findElement(By.className("middle-panel"));

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
    									if (getViewletName.equalsIgnoreCase("List of Long Running Operations ")) {
    									//	System.out.println("HTML:" + ee.getAttribute("innerHTML"));
    										String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
    										System.out.println("eventcount:" + eventcount);
    										if (!eventcount.isEmpty()) {
    											String[] str = eventcount.split(" ");
    											System.out.println("events: " + str[0]);
    											int eventcounts = Integer.parseInt(str[0]);
    											if (eventcount.equals("4139 Events")) {
    												System.out.println("list_ofLong_running_operation scorcard working fine");
    												context.setAttribute("Status",1);
    							 					context.setAttribute("Comment", "working fine");
    											}
    											else
    											{
    												System.out.println("list_ofLong_running_operation scorcard  not working fine");
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
    					}
    				}
    	    	
    				
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    	 

    }  
    
	@Test(priority=23)
	public static void Logout()
	{
		//click on logout btn
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		//click on yes btn
		driver.findElement(By.id("logoutYESBtn")).click();
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
    

