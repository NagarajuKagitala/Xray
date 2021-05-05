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
public class GlobalRepo_SampleMobile 
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
    
    
    @TestRail(testCaseId=715)
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
    		Thread.sleep(20000);
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Repo opened");
    	}	
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Repo not opened");
			driver.findElement(By.id("Mobile repo failed")).click();
		}
   		
    }
    
   //---------------------Mobile dashboard----------------------------------- 
    @TestRail(testCaseId=716)
   	@Test(priority=2)
   	public static void AbandonedCartsby_Carrier(ITestContext context) throws InterruptedException
    {
   	 //mouse over on the AbandonedCartsby_Carrier pie-chart
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
							
							if (getViewletName.equalsIgnoreCase("Abandoned Carts by Carrier")) {
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

														if (events_count !=null) {
															System.out.println(
																	"AbandonedCartsby_Carrier plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"AbandonedCartsby_Carrier plotting events not working fine. ");
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
   	
   	
    @TestRail(testCaseId=717)
   	@Test(priority=3)
   	public static void Abandoned_Carts_by_Location(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Abandoned Carts by Location")) {
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
																	"Abandoned_Carts_by_Location plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Abandoned_Carts_by_Location plotting events not working fine. ");
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
   	
   	
    @TestRail(testCaseId=718)
   	@Test(priority=4)
   	public static void Order_Amount_and_Items_Sold_by_Application_Version(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Order Amount & Items Sold by Application Version")) {
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
																	"Order_Amount_and_Items_Sold_by_Application_Version plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Order_Amount_and_Items_Sold_by_Application_Version plotting events not working fine. ");
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
   	
   	
   	
    @TestRail(testCaseId=719)
   	@Test(priority=5)
   	public static void AppNavigationin_TimeOrder(ITestContext context)
    {
   	    //get records of table
    	try
		{
		   WebElement we=(WebElement) driver.findElement(By.xpath("//li[2]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains("963f5d39-0510-11e7-8d03-600292390f02"))
		 					{
		 						System.out.println("AppNavigationin_TimeOrder showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("AppNavigationin_TimeOrder not showing result");
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
   	
   	
   	
    @TestRail(testCaseId=720)
   	@Test(priority=6)
   	public static void Slow_Orders(ITestContext context) throws InterruptedException
    {
   	    //mouse over on the Slow_Orders paichart
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
							
							if (getViewletName.equalsIgnoreCase("Slow Orders")) {
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

														if (events_count !=null) {
															System.out.println(
																	"Slow_Orders plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Slow_Orders plotting events not working fine. ");
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
   	
   	
    @TestRail(testCaseId=721)
   	@Test(priority=7)
   	public static void Performance_AvgElapsedTime_by_Carrier(ITestContext context)
    {
   	    //get records of Score-card
    	try
		{
		   WebElement we=(WebElement) driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div[1]/div[2]/div/div[1]/ul")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains("ATT"))
		 					{
		 						System.out.println("Performance_AvgElapsedTime_by_Carrier showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("Performance_AvgElapsedTime_by_Carrier not showing result");
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
   	
   	
   	
    @TestRail(testCaseId=722)
   	@Test(priority=8)
   	public static void ApplicationPerformanceIndexAnalytics_ApdexScores(ITestContext context)
   	{
   	   	    //get records of Score-card
    	try
		{
		   WebElement we=(WebElement) driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div[1]/div[2]/div/div[2]/ul/li[4]/div[2]/div[3]/div/div/div[3]/div[3]/div")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains("AcceptOrder"))
		 					{
		 						System.out.println("ApplicationPerformanceIndexAnalytics_ApdexScores showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("ApplicationPerformanceIndexAnalytics_ApdexScores not showing result");
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
   	
   	
   	
    @TestRail(testCaseId=723)
   	@Test(priority=9)
   	public static void Anomalies_via_Bollinger_Bands(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Anomalies via Bollinger Bands")) {
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

										List<WebElement> inner_we = we.findElements(By.tagName("path"));

										for (WebElement chart_ele : inner_we) {

											List<WebElement> pointer = chart_ele.findElements(By.tagName("g"));

											for (WebElement p : pointer) {
												System.out.println(p.getAttribute("class"));

												if (p.getAttribute("class").contains("amcharts-plot-area")) {

													actions.moveToElement(p).build().perform();

													Thread.sleep(5000);
													WebElement charts_div = view_ele
															.findElement(By.className("amcharts-graph-bullet"));

													try {
														WebElement tooltip = charts_div
																.findElement(By.tagName("circle"));
														String events_count = tooltip.getText();

														System.out.println("Tooltip text: " + events_count);

														if (events_count !=null) {
															System.out.println(
																	"Slow_Orders plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Slow_Orders plotting events not working fine. ");
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
   	
   	
    @TestRail(testCaseId=716)
	@Test(priority=10)
	public static void Crashesby_ScreenName(ITestContext context)
   	{
   	   	    //get records of Score-card
   	   		WebElement Scorecard_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widgeta6ab8e8d-6bb7-bfde-fbd7f2b73764contentscorecard\"]")).findElement(By.tagName("tbody"));
   	 		List<WebElement> list=Scorecard_Table.findElements(By.className("jqgrow"));
   	 		System.out.println("score-card Details: "+ list.size());
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
   	         }
   	}
	
	//---------------------Byes Dashboard---------------------------------------
    @TestRail(testCaseId=726)
	@Test(priority=11)
	public static void  PredictedCustomer_Sentiment(ITestContext context) throws InterruptedException
    {
		//click on bayes dashboard
		driver.findElement(By.xpath("//li[2]/a/span")).click();
		//driver.manage().timeouts().implicitlyWait(20, TimeUnit.MINUTES);
		Thread.sleep(180000);
		//mouse over on the PredictedCustomer_Sentiment pie-chart
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
			//	WebElement dash_tab = e.findElement(By.cssSelector("*[class^='tab-div']"));
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
							
							if (getViewletName.equalsIgnoreCase("Predicted Customer Sentiment ")) {
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

														if (events_count !=null) {
															System.out.println(
																	"PredictedCustomer_Sentiment plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"PredictedCustomer_Sentiment plotting events not working fine. ");
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
	
	
    @TestRail(testCaseId=716)
	@Test(priority=12)
	public static void  LearningRulesfor_CustomerSentiment(ITestContext context) throws InterruptedException
    {
		    //get records 
	   		WebElement CustomerSentiment_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widgetc81cfbd6-afa6-7910-ce69a03c0863contentprojectTable\"]")).findElement(By.tagName("tbody"));
	 		List<WebElement> list=CustomerSentiment_Table.findElements(By.className("jqgrow"));
	 		System.out.println("CustomerSentiment_Table Details: "+ list.size());
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
	         }	 	
    }
	
	
	
    @TestRail(testCaseId=727)
	@Test(priority=13)
	public static void PredictedRiskofLosingcustomerbasedon_AppVersion(ITestContext context) throws InterruptedException
    {
		 		
		//mouse over on the PredictedRiskofLosingcustomerbasedon_AppVersion pie-chart
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
			//	WebElement dash_tab = e.findElement(By.cssSelector("*[class^='tab-div']"));
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
							
							if (getViewletName.equalsIgnoreCase("Predicted Risk of Losing customer based on App Version ")) {
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

														if (events_count !=null) {
															System.out.println(
																	"PredictedRiskofLosingcustomerbasedon_AppVersi on plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"PredictedRiskofLosingcustomerbasedon_AppVersi on plotting events not working fine. ");
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
	
	
	
    @TestRail(testCaseId=728)
	@Test(priority=14)
	public static void PredictedSatisfied_basedon_AppVersion(ITestContext context) throws InterruptedException
	    {			
		        				
				//mouse over on the PredictedSatisfied_basedon_AppVersion pie-chart
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
			//	WebElement dash_tab = e.findElement(By.cssSelector("*[class^='tab-div']"));
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
							
							if (getViewletName.equalsIgnoreCase("Predicted Satisfied based on App Version ")) {
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

														if (events_count !=null) {
															System.out.println(
																	"PredictedSatisfied_basedon_AppVersion plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"PredictedSatisfied_basedon_AppVersion plotting events not working fine. ");
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
	
	
	
    @TestRail(testCaseId=729)
	@Test(priority=15)
	public static void PredictedRisk_ofLosingbasedon_iOSVersion(ITestContext context) throws InterruptedException
	{
			 			
			//mouse over on the PredictedRisk_iOSVersion pie-chart
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
			//	WebElement dash_tab = e.findElement(By.cssSelector("*[class^='tab-div']"));
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
							
							if (getViewletName.equalsIgnoreCase("Predicted Risk of Losing based on iOS Version ")) {
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

														if (events_count !=null) {
															System.out.println(
																	"PredictedRisk_ofLosingbasedon_iOSVersion plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"PredictedRisk_ofLosingbasedon_iOSVersion plotting events not working fine. ");
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
	
	
    @TestRail(testCaseId=730)
	@Test(priority=16)
	public static void PredictedSatisfied_based_on_iOSVersion(ITestContext context) throws InterruptedException
	{
		
		//mouse over on the PredictedSatisfied_based_on_iOSVersion pie-chart
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
			//	WebElement dash_tab = e.findElement(By.cssSelector("*[class^='tab-div']"));
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
							
							if (getViewletName.equalsIgnoreCase("Predicted Satisfied based on iOS Version ")) {
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

														if (events_count !=null) {
															System.out.println(
																	"PredictedSatisfied_based_on_iOSVersion plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"PredictedSatisfied_based_on_iOSVersion plotting events not working fine. ");
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
	
	
	
    @TestRail(testCaseId=731)
	@Test(priority=17)
	public static void PredictedRisk_of_Losing_based_on_PhoneCarrier(ITestContext context) throws InterruptedException
	{
						 							
		//mouse over on the PredictedRisk_of_Losing_based_on_PhoneCarrier pie-chart
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
			//	WebElement dash_tab = e.findElement(By.cssSelector("*[class^='tab-div']"));
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
							
							if (getViewletName.equalsIgnoreCase("Predicted Risk of Losing based on Phone Carrier ")) {
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

														if (events_count !=null) {
															System.out.println(
																	"Predicted Risk of Losing based on Phone Carrier plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Predicted Risk of Losing based on Phone Carrier plotting events not working fine. ");
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
	
	
	
    @TestRail(testCaseId=732)
	@Test(priority=18)
	public static void Predicted_Satisfied_based_on_PhoneCarrier(ITestContext context) throws InterruptedException
    {
					 									
				//mouse over on the Predicted_Satisfied_based_on_PhoneCarrier pie-chart
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
			//	WebElement dash_tab = e.findElement(By.cssSelector("*[class^='tab-div']"));
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
							
							if (getViewletName.equalsIgnoreCase("Predicted Satisfied based on Phone Carrier ")) {
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

														if (events_count !=null) {
															System.out.println(
																	"Predicted Satisfied based on Phone Carrier plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Predicted Satisfied based on Phone Carrier plotting events not working fine. ");
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
	
	
	
    @TestRail(testCaseId=733)
	@Test(priority=19)
	public static void LearningData_for_Customers_at_Risk_of_Losing(ITestContext context) throws InterruptedException
    {
    		   
		//get records of Table
		
		try
		{
		   WebElement we=(WebElement) driver.findElement(By.xpath("//div[2]/ul/li[5]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains("CancelAccount"))
		 					{
		 						System.out.println("LearningData_for_Customers_at_Risk_of_Losing showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("LearningData_for_Customers_at_Risk_of_Losing not showing result");
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
	
	
	
    @TestRail(testCaseId=734)
	@Test(priority=20)
	public static void Learning_Data_for_SatisfiedCustomers(ITestContext context) throws InterruptedException
    {
		        
				//get records of Table
    	try
		{
		   WebElement we=(WebElement) driver.findElement(By.xpath("//div[2]/div[2]/div/div/ul/li[5]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains("PlaceOrder"))
		 					{
		 						System.out.println("Learning_Data_for_SatisfiedCustomers showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("Learning_Data_for_SatisfiedCustomers not showing result");
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
	
	
	
    @TestRail(testCaseId=735)
	@Test(priority=21)
	public static void  PredictedDatax_Risk_Losing_Customer(ITestContext context) throws InterruptedException
    {
		
		//get records of Table
    	try
		{
		   WebElement we=(WebElement) driver.findElement(By.xpath("//div[2]/ul/li[6]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains(" MobileOrders, Orders, RiskLoosingCustomer, T-Mobile, iPhone"))
		 					{
		 						System.out.println("PredictedDatax_Risk_Losing_Customer showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("PredictedDatax_Risk_Losing_Customer not showing result");
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
	
	
	
    @TestRail(testCaseId=736)
	@Test(priority=22)
	public static void PredictedData_Satisfied_Customer(ITestContext context) throws InterruptedException
    {
		        
				//get records of Table
    	try
		{
		   WebElement we=(WebElement) driver.findElement(By.xpath("//li[6]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains("MobileOrders, Orders, SatisfiedCustomer, T-Mobile, TRACKING_ACTIVITY, iPad"))
		 					{
		 						System.out.println("PredictedData_Satisfied_Customer showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("PredictedData_Satisfied_Customer not showing result");
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
	
	
	
    @TestRail(testCaseId=737)
	@Test(priority=23)
	public static void Learning_Rules(ITestContext context) throws InterruptedException
    {
		 
		//get records of Table
    	try
		{
		   WebElement we=(WebElement) driver.findElement(By.cssSelector(".scorecard-body > div")).findElement(By.tagName("tbody"));
			List<WebElement> lst=we.findElements(By.className("jqgrow"));
			System.out.println("ScorecardRowSize: "+ lst.size());
			for(WebElement row : lst)
			{
				 List<WebElement> lstwele=row.findElements(By.tagName("td"));
				 //System.out.println("td size: "+ lstwele.size());
				 for(WebElement td :lstwele)
				 {
					 String data=td.getText();
					 if(!data.isEmpty())
		 				{
		 					String data1=td.getText();
		 					System.out.println(data1);
		 					if(data1.contains("CancelAccount"))
		 					{
		 						System.out.println("Learning_Rules showing result");
		 						context.setAttribute("Status",1);
			 					context.setAttribute("Comment", "working fine");
			 					break;
		 					}
		 					
				
				
				  else {
				 
				              System.out.println("Learning_Rules not showing result");
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
	
    
	@Test(priority=24)
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

   	
   	
   	

