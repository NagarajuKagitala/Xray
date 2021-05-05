package GlobalRepositories;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
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
public class GlobalRepo_SmapleOrderTracking 
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
    
    
    @TestRail(testCaseId=738)
    @Parameters({"sRepositories"})
   	@Test(priority=1)
    public static void SelectRepositories(String sRepositories ,ITestContext context) throws InterruptedException
    {
    	try
    	{
    		String repodata = ".//*[contains(@id," + "'" + sRepositories + "'" + ")]";
    		// Click on Dropdown
    		driver.findElement(By.cssSelector("span.select2-selection__arrow")).click();
    		driver.findElement(By.xpath(repodata)).click();
    		Thread.sleep(8000);
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Repo opened");
    	}	
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Repo not opened");
			driver.findElement(By.id("Devops repo failed")).click();
		}
    }
    
    
    @TestRail(testCaseId=886)
   	@Test(priority=2)
   	public static void Summary_of_the_Order_Process_Flow_for_Latest_Week(ITestContext context) throws InterruptedException
    {
    	String LatestWeek=driver.findElement(By.xpath("//div[@id='mCSB_1_container']/div/div")).getText();
    	System.out.println("Vlues is :" +LatestWeek);
    	
    	if(LatestWeek.contains("AcceptOrder")||LatestWeek.contains("Orders"))
    	{
    		System.out.println("Latest Week activities working fine");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Latest week activities");
    	}
    	else
    	{
    		System.out.println("Latest Week activities not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Latest week activities");
			driver.findElement(By.id("Latest week activities failed")).click();
    	}
   		/*Thread.sleep(5000);
   	    //get records of Summary viewlet	
   		WebElement MFT_Summary=(WebElement) driver.findElement(By.id("widget9e99a14e-1126-88f1-fa9f92e24ba9"));
 		List<WebElement> list=MFT_Summary.findElements(By.className("summary-child-div"));
 		System.out.println("Events Details: "+ list.size());
 		for(WebElement row : list)
 		 {
 			
 			       String data=row.getAttribute("innerText");
		           if(!data.isEmpty())
		           {
			        System.out.println("content text:"+row.getAttribute("innerText"));
		           }             
		 }*/
    }
    @TestRail(testCaseId=887)
   	@Test(priority=3)
   	public static void test_it_Summary(ITestContext context) throws InterruptedException
       {
    	
    	WebElement MFT_Summary=(WebElement) driver.findElement(By.id("//div[@id='mCSB_1_container']/div/div[2]"));
 		List<WebElement> list=MFT_Summary.findElements(By.className("summary-child-div"));
 		System.out.println("Events Details: "+ list.size());
 		for(WebElement row : list)
 		 {
 			
 			       String data=row.getAttribute("innerText");
		           if(!data.isEmpty())
		           {
			        System.out.println("content text:"+row.getAttribute("innerText"));
		           }             
		 }
		
		/*
		 * String data =
		 * driver.findElement(By.xpath("//div[@id='mCSB_1_container']/div/div[2]")).
		 * getText();
		 * 
		 * if (data.contains("ALL")) { System.out.println(data);
		 * System.out.println("test_it_Summary working fine");
		 * context.setAttribute("Status",1); context.setAttribute("Comment",
		 * "working fine"); } else {
		 * System.out.println("test_it_Summary not working fine");
		 * context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
		 * driver.findElement(By.xpath("Not showing")).click(); }
		 */
   		
       }
   	
   	
    @TestRail(testCaseId=740)
   	@Test(priority=4)
   	public static void ActivtyScorecard_Latest_Week(ITestContext context)
    {
       //get activity of scorecard
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
								if (getViewletName.equalsIgnoreCase("Activty Scorecard: Latest Week ")) {
								//	System.out.println("HTML:" + ee.getAttribute("innerHTML"));
									String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
									System.out.println("eventcount:" + eventcount);
									if (!eventcount.isEmpty()) {
										String[] str = eventcount.split(" ");
										System.out.println("events: " + str[0]);
										int eventcounts = Integer.parseInt(str[0]);
										if (eventcount.equals("96 Actvities")) {
											System.out.println("ActivtyScorecard_Latest_Week scorcard working fine");
											context.setAttribute("Status",1);
						 					context.setAttribute("Comment", "working fine");
										}
										else
										{
											System.out.println("ActivtyScorecard_Latest_Week scorcard  not working fine");
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
   	
   	
    @TestRail(testCaseId=741)
   	@Test(priority=5)
   	public static void Orders_for_the_Last_Hour_that_missed_their_SLA(ITestContext context)
    {
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
							
							if (getViewletName.equalsIgnoreCase("Orders for the Last Hour that missed their SLA")) {
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
																	"Orders_for_the_Last_Hour_that_missed_their_SLA plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Orders_for_the_Last_Hour_that_missed_their_SLA plotting events not working fine. ");
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
   	
   	
    @TestRail(testCaseId=742)
   	@Test(priority=6)
   	public static void Credit_Validation_Exceptions(ITestContext context)
    {
    	String Activity=driver.findElement(By.xpath("//th[3]")).getText();
    	System.out.println("Activity is :" +Activity);
    	
    	if(Activity.equalsIgnoreCase(" ActivityID"))
    	{
    		System.out.println("Credit validation exception is working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Credit validation exceptions");
    	}
    	else
    	{
    		System.out.println("Credit validation exception is not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Credit validation exception failed");
			driver.findElement(By.id("Credit validation exception failed")).click();
    	}
    	
   	   /* //get records of table
   		WebElement TimeOrder_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widget283340d3-e29e-bcbb-0d5dc86dd410contentprojectTable\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=TimeOrder_Table.findElements(By.className("jqgrow"));
 		System.out.println("Events Details: "+ list.size());
 		for(WebElement row : list)
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
         }*/
    }
   	
   	
    @TestRail(testCaseId=743)
   	@Test(priority=7)
   	public static void Events_for_Latest_Hour_by_Location(ITestContext context) throws InterruptedException
    {
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
							
							if (getViewletName.equalsIgnoreCase("Events for Latest Hour by Location")) {
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
																	"Events_for_Latest_Hour_by_Location plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Events_for_Latest_Hour_by_Location plotting events not working fine. ");
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
   	
   	
    @TestRail(testCaseId=744)
   	@Test(priority=8)
   	public static void Application_Performance_Index_Analytics(ITestContext context)
    {
    	String Application=driver.findElement(By.xpath("//li[4]/div[2]/div[3]/div/div/div[3]/div[2]/div/table/thead/tr/th[2]")).getText();
    	System.out.println("name is: " +Application);
    	
    	if(Application.equalsIgnoreCase(" ActivityName"))
    	{
    		System.out.println("Application performance index analysis working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Application performance index analysis");
    	}
    	else
    	{
    		System.out.println("Application performance index analysis not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Application performance index analysis");
			driver.findElement(By.id("Application performance index analysis failed")).click();
    	}
    	
   	    /*//get records of Score-card
   		WebElement TimeOrder_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widgete39f6b6c-a821-13e0-cc45f58f0c63contentscorecard\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=TimeOrder_Table.findElements(By.className("jqgrow"));
 		System.out.println("Events Details: "+ list.size());
 		for(WebElement row : list)
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
         }*/
    }
   	
   	
    @TestRail(testCaseId=745)
   	@Test(priority=9)
   	public static void SLA_Violation_Scorecard(ITestContext context) throws InterruptedException
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
								if (getViewletName.equalsIgnoreCase("SLA violation Scorecard")) {
								//	System.out.println("HTML:" + ee.getAttribute("innerHTML"));
									String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
									System.out.println("eventcount:" + eventcount);
									if (!eventcount.isEmpty()) {
										String[] str = eventcount.split(" ");
										System.out.println("events: " + str[0]);
										int eventcounts = Integer.parseInt(str[0]);
										if (eventcount.equals("11 Activities")) {
											System.out.println("SLA_Violation_Scorecard scorcard working fine");
											context.setAttribute("Status",1);
						 					context.setAttribute("Comment", "working fine");
										}
										else
										{
											System.out.println("SLA_Violation_Scorecard scorcard  not working fine");
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
   	
   	
    @TestRail(testCaseId=746)
   	@Test(priority=10)
   	public static void Activity_Distribution(ITestContext context) throws InterruptedException
    {
   	    //mouse over on the Activity_Distribution piechart
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
							
							if (getViewletName.equalsIgnoreCase("Activity Distribution")) {
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
																	"Activity_Distribution pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"Activity_Distribution pie-chart plotting events not working fine");
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
   	
   	
    @TestRail(testCaseId=747)
   	@Test(priority=11)
   	public static void Elapsed_Time_for_Order_Events(ITestContext context) throws InterruptedException
    {
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
							
							if (getViewletName.equalsIgnoreCase("Elapsed Time for Order Events")) {
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
																	"Elapsed_Time_for_Order_Events plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Elapsed_Time_for_Order_Events plotting events not working fine. ");
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
   	
   	
    @TestRail(testCaseId=748)
   	@Test(priority=12)
   	public static void Activities_by_Application(ITestContext context) throws InterruptedException
    {
   	   //mouse over on the Activities_by_Application piechart
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
							
							if (getViewletName.equalsIgnoreCase("Activities by Application ")) {
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
																	"Activities_by_Application pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"Activities_by_Application pie-chart plotting events not working fine");
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
   	
   	
    @TestRail(testCaseId=749)
   	@Test(priority=13)
   	public static void  Exponential_Moving_Average_for_ElapsedTime(ITestContext context)
    {
    	String EMSE=driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div/div[2]/div/div[1]/ul/li[6]/div[2]/div[3]/div[2]/svg/g/g/g/rect")).getText();
    	System.out.println("EmA Elapsed :" +EMSE);
    	
    	if(EMSE.equalsIgnoreCase("EMA(ElapsedTime)"))
    	{
    		System.out.println("EMA is working fine");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "EMS Elapsed time");
    	}
    	else
    	{
    		System.out.println("EMA Elapsed time is not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "WMA Elapsed time");
			driver.findElement(By.id("EMA Elapsed is failed")).click();
    	}
   		/*try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetaf6bcf29-83d9-fa66-3f43ec99d8b9"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							
							//i++;
					         System.out.println("index: " + i++ );
							
							WebElement inner_we = we.findElement(By.tagName("g"));																		  							

							List<WebElement> pointer = inner_we.findElements(By.tagName("circle"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-bullet")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.className("amcharts-balloon-div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("Exponential_Moving_Average_for_ElapsedTime line-chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
								if(i==43)
								{
								break;
								}
					
						} 
					catch (NoSuchElementException ex) {
							// Element is not present
						//	System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				 //   return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
    }
   	
   	
    @TestRail(testCaseId=750)
   	@Test(priority=14)
   	public static void StdDev_Elapsed_Time_for_Activities(ITestContext context)
    {
    	String StdDev=driver.findElement(By.cssSelector("#viewlet-f3d78794-7944-1fd6-5b4a5f002513 .amcharts-axis-title:nth-child(4) > tspan:nth-child(1)")).getText();
    	System.out.println("Name is :" +StdDev);
    	
    	if(StdDev.equalsIgnoreCase("Activities Count"))
    	{
    		System.out.println("StdDev is working fine");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "StdDev is working");
    	}
    	else
    	{
    		System.out.println("StdDev is not working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment","StdDev is not working");
			driver.findElement(By.id("StdDev is failed")).click();
    	}
   		/*try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetf3d78794-7944-1fd6-5b4a5f002513content"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));
							
								List<WebElement> pointer = inner_we.findElements(By.tagName("circle"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-bullet")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("StdDev Elapsed Time for Activities Linechart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(10000);

										// return;
									}
								
							}
						} catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover

					Thread.sleep(5000);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
    }
   	
   	
    @TestRail(testCaseId=751)
   	@Test(priority=15)
   	public static void Anomaly_Monitor(ITestContext context) throws InterruptedException
    {
    	String Anomaly=driver.findElement(By.cssSelector("#viewlet-ec583aa5-8ecf-a94c-5a753237d1b7 .widget-footer-div > svg")).getText();
    	System.out.println("vallue is :" +Anomaly);
    	
    	if(Anomaly.contains("Events Count"))
    	{
    		System.out.println("Anomaly is working fine");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Anomaly is working");
    	}
    	else
    	{
    		System.out.println("Anomaly is not working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment","Anomaly is not working");
			driver.findElement(By.id("Anomaly is failed")).click();
    	}
    	
    	
   		/*Thread.sleep(20000);
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetec583aa5-8ecf-a94c-5a753237d1b7"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							
							 i++;
					         //System.out.println("index: " + i++ );
							
							WebElement inner_we = we.findElement(By.tagName("g"));																		  							

							List<WebElement> pointer = inner_we.findElements(By.tagName("circle"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-bullet")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.className("amcharts-balloon-div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("Anomaly_Monitor Anomaly_chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
								if(i==51)
								{
								break;
								}
					
						} 
					catch (NoSuchElementException ex) {
							// Element is not present
						//	System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				 //   return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
    }
   	
   	
    @TestRail(testCaseId=752)
   	@Test(priority=16)
   	public static void Appdex_Zones(ITestContext context)
    {
    	String App=driver.findElement(By.cssSelector(".amcharts-category-axis > .amcharts-axis-title:nth-child(4) > tspan")).getText();
    	System.out.println("Value is :" +App);
    	
    	if(App.equalsIgnoreCase("StartTime"))
    	{
    		System.out.println("Appdex zones is working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Appdex zones is working");
    	}
    	else
    	{
    		System.out.println("Appdex zones is not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Appdex zones is not working");
			driver.findElement(By.id("Appdex zones failed")).click();
    	}
    	
   		/*try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget95dd4e41-0b61-9823-a018cc899aef"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							
							i++;
					       //  System.out.println("index: " + i++ );
							
							WebElement inner_we = we.findElement(By.tagName("g"));																		  							

							List<WebElement> pointer = inner_we.findElements(By.tagName("circle"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-bullet")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.className("amcharts-balloon-div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("Appdex_Zones line-chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
								if(i==46)
								{
								break;
								}
					
						} 
					catch (NoSuchElementException ex) {
							// Element is not present
						//	System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				 //   return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
    }
    @TestRail(testCaseId=844)
   	@Test(priority=17)
   	public static void Function_Analysis_viewlet(ITestContext context)
    {
    	String Application=driver.findElement(By.xpath("//li[8]/div[2]/div[3]/div/div/div[3]/div[2]/div/table/thead/tr/th[2]")).getText();
    	System.out.println("name is: " +Application);
    	
    	if(Application.equalsIgnoreCase(" COUNTRY_NAME"))
    	{
    		System.out.println("Function_Analysis_viewlet working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "passed");
    	}
    	else
    	{
    		System.out.println("Function_Analysis_viewlet not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "failed");
			driver.findElement(By.id("Function_Analysis_viewlet failed")).click();
    	}
    }
    @TestRail(testCaseId=845)
   	@Test(priority=18)
   	public static void Histogram_of_Recent_Events_viewlet(ITestContext context)
    {
    	String App=driver.findElement(By.cssSelector(".amcharts-axis-title:nth-child(21) > tspan")).getText();
    	System.out.println("Value is :" +App);
    	
    	if(App.equalsIgnoreCase("ElapsedTime"))
    	{
    		System.out.println("Histogram_of_Recent_Events_viewlet is working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Histogram_of_Recent_Events_viewlet is working");
    	}
    	else
    	{
    		System.out.println("Histogram_of_Recent_Events_viewlet is not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Histogram_of_Recent_Events_viewlets is not working");
			driver.findElement(By.id("Histogram_of_Recent_Events_viewlet failed")).click();
    	}
    }
    @TestRail(testCaseId=846)
   	@Test(priority=19)
   	public static void Revenue_over_Time_viewlet(ITestContext context)
    {
    	String App=driver.findElement(By.cssSelector("#viewlet-8c98ff98-f821-37a6-d9784a78dd54 .amcharts-axis-title:nth-child(3) > tspan:nth-child(1)")).getText();
    	System.out.println("Value is :" +App);
    	
    	if(App.equalsIgnoreCase("EndTime"))
    	{
    		System.out.println("Revenue_over_Time_viewlet is working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "passed");
    	}
    	else
    	{
    		System.out.println("Revenue_over_Time_viewlet is not working");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "failed");
			driver.findElement(By.id("Revenue_over_Time_viewlet failed")).click();
    	}
    }
   	
   	@Test(priority=20)
   	public static void Logout()
   	{
   			//click on logout btn
   			driver.findElement(By.cssSelector(".fa-power-off")).click();
   			//click on yes btn
   			driver.findElement(By.id("logoutYESBtn")).click();
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
