package GlobalRepositories;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
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
public class GlobalRepo_SampleMiddleware 
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
	    Thread.sleep(10000);
    }
    
    
    @TestRail(testCaseId=658)
    @Parameters({"sRepositories"})
   	@Test(priority=1)
    public static void SelectRepositories(String sRepositories, ITestContext context) throws InterruptedException
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
    
    //--------------- Data Power Operations Dashboard --------
    @TestRail(testCaseId=659)
   	@Test(priority=11)
   	public static void AutoPilot_Insight_for_DataPower_Events(ITestContext context) throws InterruptedException
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
							
							if (getViewletName.equalsIgnoreCase("AutoPilot Insight for DataPower Events ")) {
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
																	"AutoPilot_Insight_for_DataPower_Events pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"AutoPilot_Insight_for_DataPower_Events pie-chart plotting events not working fine");
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
    @TestRail(testCaseId=660)
    @Test(priority=3)
    public static void AutoPilot_Insight_for_DataPower_Anomalies(ITestContext context ) throws InterruptedException
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
							
							if (getViewletName.equalsIgnoreCase("AutoPilot Insight for DataPower Anomalies")) {
								//System.out.println("HTML:" + ee.getAttribute("innerHTML"));
								WebElement div = e2.findElement(By.className("viewlet-body"));
								WebElement view_ele = div.findElement(By.className("content-wrapper")).findElement(By.tagName("div"));
								// Retrieve WebElement
								WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

								List<WebElement> w_ele = ele.findElements(By.tagName("g"));
								System.out.println(w_ele.size());
								int i = 0;
								for (WebElement we : w_ele) {
									System.out.println(we.getAttribute("innerHTML"));

									try {
										i++;
										System.out.println("index: " + i++);

										List<WebElement> inner_we = we.findElements(By.tagName("g"));
										System.out.println("in");

										for (WebElement chart_ele : inner_we) {	
											System.out.println("chart_ele:"+chart_ele.getAttribute("innerHTML"));

										List<WebElement> pointer = chart_ele.findElements(By.tagName("path"));

											for (WebElement p : pointer) {
												System.out.println("class:"+p.getAttribute("class"));

												if (p.getAttribute("class").contains("amcharts-axis-")) {

													actions.moveToElement(p).build().perform();

													Thread.sleep(2000);
													WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

													try {
														WebElement tooltip = charts_div.findElement(By.className("amcharts-balloon-div"));
														String events_count = tooltip.getText();
														
														System.out.println("Tooltip text: "+ events_count);

														if (events_count != null) {
															System.out.println(
																	"AutoPilot_Insight_for_DataPower_Events pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"AutoPilot_Insight_for_DataPower_Events pie-chart plotting events not working fine");
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
    @TestRail(testCaseId=659)
    @Test(priority=2)
    public void AutoPilot_Insight_for_DataPower_5_Worst_Events(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("AutPilot Insight for DataPower 5 Worst Events")) {
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
																	"AutoPilot_Insight_for_DataPower_5_Worst_Events plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"AutoPilot_Insight_for_DataPower_5_Worst_Events plotting events not working fine. ");
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
    
    @TestRail(testCaseId=661)
    @Test(priority=3)
    public void AutoPilot_Insight_for_DataPower_Alerts(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("AutoPilot Insight for DataPower Alerts")) {
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
																	"AutoPilot_Insight_for_DataPower_Alerts plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"AutoPilot_Insight_for_DataPower_Alerts plotting events not working fine. ");
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
    @TestRail(testCaseId=659)
   	@Test(priority=11)
   	public static void AutoPilot_DataPower_Events_BY_Resource(ITestContext context) throws InterruptedException
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
							
							if (getViewletName.equalsIgnoreCase("AutoPilot for DataPower Events by Resource ")) {
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
																	"AutoPilot_DataPower_Events_BY_Resource pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"AutoPilot_DataPower_Events_BY_Resource pie-chart plotting events not working fine");
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
    
    //------------- Data Power Metrics Dashboard ---------------
    @TestRail(testCaseId=663)
    @Test(priority=4)
    public void Viewlet88(ITestContext context) throws InterruptedException
    {
    	//Click on the data power metrics dashboard
    	driver.findElement(By.xpath("//span[contains(.,'DataPower Metrics')]")).click();
    	Thread.sleep(15000);
    	
    	String event=driver.findElement(By.cssSelector("#viewlet-b02d5b43-2205-6b41-4d55fb42ba34-table_NumberOf")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Events Count"))
    	{
    		System.out.println("Viewlet 88 working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Viewlet 88 events");
    	}
    	else
    	{
    		System.out.println("Viewlet 88 failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Viewlet 88 events");
			driver.findElement(By.id("Viewlet 88 failed")).click();
    	}
    }
    
    @TestRail(testCaseId=664)
    @Test(priority=5)
    public void Viewlet93(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-e766279f-5b95-d1da-02843e220526-table_ResourceName")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" ResourceName"))
    	{
    		System.out.println("Viewlet 93 working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Viewlet 88 events");
    	}
    	else
    	{
    		System.out.println("Viewlet 93 failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Viewlet 88 events");
			driver.findElement(By.id("Viewlet 93 failed")).click();
    	}
    }
    
    //--------- MQ Metrics and Events -------
    @TestRail(testCaseId=671)
   	@Test(priority=11)
   	public static void TODAY_piechart_Summary_AutoPilot_WorkGroup_Policy_Manager(ITestContext context) throws InterruptedException
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
							
							if (getViewletName.equalsIgnoreCase("TODAY-Piechart-AutoPilot WorkGroup_Policy_Manager ")) {
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
																	"TODAY_piechart_Summary_AutoPilot_WorkGroup_Policy_Manager pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"TODAY_piechart_Summary_AutoPilot_WorkGroup_Policy_Manager pie-chart plotting events not working fine");
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
    @TestRail(testCaseId=672)
    @Test(priority=6)
    public void TODAY_Scorecard_Summary_AutoPilot_WorkGroup_Policy_Manager(ITestContext context) throws InterruptedException
    {
    	//Click on MQ metrics and events
    	driver.findElement(By.xpath("//span[contains(.,'MQ Metrics and Events')]")).click();
    	Thread.sleep(15000);
    	
    	String event=driver.findElement(By.cssSelector("#viewlet-830d217e-0cbd-7faa-dc571b7a9425-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("Today scorecard summary working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Today scorecard summary");
    	}
    	else
    	{
    		System.out.println("Today scorecard summary failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Today scorecard summary");
			driver.findElement(By.id("Today scorecard summary failed")).click();
    	}
    }
    @TestRail(testCaseId=673)
    @Test(priority=7)
    public void TODAY_Scorecard_AutoPilot_WorkGroup_Policy_Manager(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-44c13ef5-8127-9fa8-adc9f11268cf-table_ResourceName")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" ResourceName"))
    	{
    		System.out.println("Today scorecard working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Today scorecard");
    	}
    	else
    	{
    		System.out.println("Today scorecard failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Today scorecard");
			driver.findElement(By.id("Today scorecard failed")).click();
    	}
    }
    @TestRail(testCaseId=674)
   	@Test(priority=11)
   	public static void LAST_XXX_Piechart_AutoPilot_WorkGroup_Policy_Manager(ITestContext context) throws InterruptedException
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
							
							if (getViewletName.equalsIgnoreCase("LAST-XXX-Piechart AutoPilot WorkGroup_Policy_Manager ")) {
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
																	"LAST_XXX_Piechart_AutoPilot_WorkGroup_Policy_Manager pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"LAST_XXX_Piechart_AutoPilot_WorkGroup_Policy_Manager pie-chart plotting events not working fine");
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
    @TestRail(testCaseId=675)
    @Test(priority=8)
    public void LAST_XXX_Scorecard_Summary_AutoPilot_WorkGroup_Policy_Manager(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-cb0a8227-843b-050d-1d7bfd250a48-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("last xxx scorecard summary working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "last xxx scorecard summary");
    	}
    	else
    	{
    		System.out.println("last xxx scorecard summary failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "last xxx scorecard summary");
			driver.findElement(By.id("last xxx scorecard summary failed")).click();
    	}
    }
    @TestRail(testCaseId=681)
    @Test(priority=3)
    public void Application_Issues_trading(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Application Issues-Trading")) {
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
																	"Application_Issues_trading plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Application_Issues_trading plotting events not working fine. ");
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
    @TestRail(testCaseId=676)
    @Test(priority=9)
    public void LAST_XXX_Scorecard_AutoPilot_WorkGroup_Policy_Manager(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-d7c30f4c-5dc3-a612-a5d5219d85e9-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("last xxx scorecard working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "last xxx scorecard");
    	}
    	else
    	{
    		System.out.println("last xxx scorecard failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "last xxx scorecard");
			driver.findElement(By.id("last xxx scorecard failed")).click();
    	}
    }
    @TestRail(testCaseId=677)
   	@Test(priority=11)
   	public static void AutoPilot_Events_StateChanges_ETC(ITestContext context) throws InterruptedException
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
							
							if (getViewletName.equalsIgnoreCase("AutoPilot Events-State Cahnge,Notify,Email Task,Etc ")) {
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
																	"AutoPilot_Events_StateChanges_ETC pie-chart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"AutoPilot_Events_StateChanges_ETC pie-chart plotting events not working fine");
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
   
    @TestRail(testCaseId=679)
    @Test(priority=11)
    public void Queue_Anomalies(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector(".amcharts-axis-title:nth-child(3) > tspan")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase("StartTime"))
    	{
    		System.out.println("Queue anomalies working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Queue anomalies");
    	}
    	else
    	{
    		System.out.println("Queue anomalies failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Queue anomalies");
			driver.findElement(By.id("Queue anomalies failed")).click();
    	}
    }
    @TestRail(testCaseId=685)
    @Test(priority=3)
    public void viewlet_188(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Viewlet 188")) {
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
																	"Viewlet_188 plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Viewlet_188 plotting events not working fine. ");
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
    @TestRail(testCaseId=682)
    @Test(priority=13)
    public void Performance_Issues_Forex(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Performance Issues-Forex")) {
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
																	"Performance_Issues_Forex plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Performance_Issues_Forex plotting events not working fine. ");
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
    @TestRail(testCaseId=684)
    @Test(priority=14)
    public void Application_Issues_Fixed_Loan(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Application Issues-Fixed Loan")) {
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
																	"Application_Issues_Fixed_Loan plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Application_Issues_Fixed_Loan plotting events not working fine. ");
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
    @TestRail(testCaseId=683)
    @Test(priority=15)
    public void Application_Issues_OTC(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Application Issues-OTC")) {
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
																	"Application_Issues_OTC plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Application_Issues_OTC plotting events not working fine. ");
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
    @TestRail(testCaseId=680)
    @Test(priority=16)
    public void Missed_SLA_Transactions(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-2731e104-3a1f-192c-df66f01ada96 .amcharts-axis-title:nth-child(5) > tspan:nth-child(1)")).getText();
    	System.out.println("Value is :" +event);
    	String dd1=driver.findElement(By.cssSelector("#viewlet-2731e104-3a1f-192c-df66f01ada96 .amcharts-axis-title:nth-child(2) > tspan:nth-child(1)")).getText();
    	System.out.println("is:" +dd1);
    	
    	if(event.equalsIgnoreCase("ElapsedTime"))
    	{
    		System.out.println("Missed SLA transactions working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Missed SLA transactions");
    	}
    	else
    	{
    		System.out.println("Missed SLA transactions failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Missed SLA transactions");
			driver.findElement(By.id("Missed SLA transactionsfailed")).click();
    	}
    }
    @TestRail(testCaseId=678)
    @Test(priority=16)
    public void Today_MqEvents_StateCahnges(ITestContext context)
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
						
						if (getViewletName.equalsIgnoreCase("TODAY-MQ Events-State Changes ")) {
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
																"Today_MqEvents_StateCahnges pie-chart plotting events working fine. ");
														is_tooltippresent = true;
														context.setAttribute("Status",1);
									 					context.setAttribute("Comment", "working fine");
														
													}
													else
													{
														System.out.println(
																"Today_MqEvents_StateCahnges pie-chart plotting events not working fine");
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
    
    
    //---------- JVM Analysis ---------------------
    @TestRail(testCaseId=686)
    @Test(priority=17)
    public void WebLogic_Scorecard(ITestContext context) throws InterruptedException
    {
    	//Click on JVM Anlysis
    	driver.findElement(By.xpath("//span[contains(.,'JVM Analytics')]")).click();
    	Thread.sleep(15000);
    	
    	
    	String event=driver.findElement(By.cssSelector("#viewlet-ea695bdb-3662-bc6c-7a75555d14d6-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("WebLogic Scorecard working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "WebLogic Scorecard");
    	}
    	else
    	{
    		System.out.println("WebLogic Scorecard failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "WebLogic Scorecard");
			driver.findElement(By.id("WebLogic Scorecard failed")).click();
    	}
    }
    @TestRail(testCaseId=687)
    @Test(priority=18)
    public void WAS_Scorecard(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-dd1c929b-078f-4dc0-256540c0ce82-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("WAS Scorecard working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "WAS Scorecard");
    	}
    	else
    	{
    		System.out.println("WAS Scorecard failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "WAS Scorecard");
			driver.findElement(By.id("WAS Scorecard failed")).click();
    	}
    }
    @TestRail(testCaseId=693)
    @Test(priority=19)
    public void JBoss_Issues(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("JBoss Issues")) {
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
																	"JBoss_Issues plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"JBoss_Issues plotting events not working fine. ");
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
    @TestRail(testCaseId=700)
    @Test(priority=20)
    public void WebLogic_Anomaly_Chart(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-c52fa165-ec2f-0f5d-6c97f3a2612b .amcharts-axis-title:nth-child(3) > tspan:nth-child(1)")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase("StartTime"))
    	{
    		System.out.println("WebLogic Anomaly Chart working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "WebLogic Anomaly Chart");
    	}
    	else
    	{
    		System.out.println("WebLogic Anomaly Chart failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "WebLogic Anomaly Chart");
			driver.findElement(By.id("WebLogic Anomaly Chart failed")).click();
    	}
    }
    @TestRail(testCaseId=694)
    @Test(priority=21)
    public void WAS_Issues(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("WAS Issues")) {
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
																	"WAS_Issues plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"WAS_Issues plotting events not working fine. ");
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
    @TestRail(testCaseId=658)
    @Test(priority=22)
    public void JBoss_Scorecard(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-9aa66fa3-6236-fca9-31d9d650bd1a-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("JBoss Scorecard working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "JBoss Scorecard");
    	}
    	else
    	{
    		System.out.println("JBoss Scorecard failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "JBoss Scorecard");
			driver.findElement(By.id("JBoss Scorecard failed")).click();
    	}
    }
    @TestRail(testCaseId=689)
    @Test(priority=16)
    public void WebLogic_HeapSize(ITestContext context)
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
						
						if (getViewletName.equalsIgnoreCase("WebLogic HeapSize ")) {
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
																"WebLogic_HeapSize pie-chart plotting events working fine. ");
														is_tooltippresent = true;
														context.setAttribute("Status",1);
									 					context.setAttribute("Comment", "working fine");
														
													}
													else
													{
														System.out.println(
																"WebLogic_HeapSize pie-chart plotting events not working fine");
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
    @TestRail(testCaseId=701)
    @Test(priority=23)
    public void WAS_Anomaly_Chart(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-fa3b2488-b50c-4e1f-3cb878b48e8b .amcharts-axis-title:nth-child(3) > tspan:nth-child(1)")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase("StartTime"))
    	{
    		System.out.println("WAS Anomaly Chart working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "WAS Anomaly Chart");
    	}
    	else
    	{
    		System.out.println("WAS Anomaly Chart failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "WAS Anomaly Chart");
			driver.findElement(By.id("WAS Anomaly Chart failed")).click();
    	}
    }
    @TestRail(testCaseId=702)
    @Test(priority=24)
    public void JBoss_Anomaly_Chart(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-37427f40-906b-bee0-f9c3fc382aba .amcharts-axis-title:nth-child(3) > tspan:nth-child(1)")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase("StartTime"))
    	{
    		System.out.println("JBoss Anomaiy Chart working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "JBoss Anomaiy Chart");
    	}
    	else
    	{
    		System.out.println("JBoss Anomaiy Chart failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "JBoss Anomaiy Chart");
			driver.findElement(By.id("JBoss Anomaiy Chart failed")).click();
    	}
    }
    @TestRail(testCaseId=690)
    @Test(priority=25)
    public void WebLogic_CPU_Warnings(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-f5224b61-4491-88d6-630bf316422c-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("WebLogic CPU Warnings working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "WebLogic CPU Warnings");
    	}
    	else
    	{
    		System.out.println("WebLogic CPU Warnings failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "WebLogic CPU Warnings");
			driver.findElement(By.id("WebLogic CPU Warnings failed")).click();
    	}
    }
    
    @TestRail(testCaseId=695)
    @Test(priority=26)
    public void WAS_HeapSize(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("WAS HeapSize")) {
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
																	"WAS_HeapSize plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"WAS_HeapSize plotting events not working fine. ");
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
    @TestRail(testCaseId=696)
    @Test(priority=27)
    public void JBoss_HeapSize(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("JBoss HeapSize")) {
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
																	"JBoss_HeapSize plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"JBoss_HeapSize plotting events not working fine. ");
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
    @TestRail(testCaseId=697)
    @Test(priority=28)
    public void WebLogic_Errors_Stackchart(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("WebLogic Errors Stackchart")) {
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
																	"WebLogic_Errors_Stackchart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"WebLogic_Errors_Stackchart plotting events not working fine. ");
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
    @TestRail(testCaseId=691)
    @Test(priority=29)
    public void WAS_CPU_Warnings(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-55cda99a-9c2e-c30b-97173294a5da-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("WAS CPU Warnings working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "WAS CPU Warnings");
    	}
    	else
    	{
    		System.out.println("WAS CPU Warnings failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "WAS CPU Warnings");
			driver.findElement(By.id("WAS CPU Warnings failed")).click();
    	}
    }
    @TestRail(testCaseId=692)
    @Test(priority=30)
    public void JBoss_CPU_Warnings(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-fe365388-a2aa-164a-52c032b49b1e-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("JBoss CPU Warnings working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "JBoss CPU Warnings");
    	}
    	else
    	{
    		System.out.println("JBoss CPU Warnings failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "JBoss CPU Warnings");
			driver.findElement(By.id("JBoss CPU Warnings failed")).click();
    	}
    }
    @TestRail(testCaseId=698)
    @Test(priority=31)
    public void WAS_Errors_Stackchart(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("WAS Errors Stackchart")) {
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
																	"WAS_Errors_Stackchart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"WAS_Errors_Stackchart plotting events not working fine. ");
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
    @TestRail(testCaseId=699)
    @Test(priority=32)
    public void Viewlet_42(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("Viewlet 42")) {
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
																	"Viewlet_42 plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"Viewlet_42 plotting events not working fine. ");
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
    
    // -------- Data Power Transactions --------@TestRail(testCaseId=680)
    @TestRail(testCaseId=703)
    @Test(priority=16)
    public void DP_Order_Workflow_Breakdown_By_Apllication(ITestContext context)
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
						
						if (getViewletName.equalsIgnoreCase("DP Order Workflow-Breakdown by Apllication ")) {
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
																"DP_Order_Workflow_Breakdown_By_Apllication pie-chart plotting events working fine. ");
														is_tooltippresent = true;
														context.setAttribute("Status",1);
									 					context.setAttribute("Comment", "working fine");
														
													}
													else
													{
														System.out.println(
																"DP_Order_Workflow_Breakdown_By_Apllication pie-chart plotting events not working fine");
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
    
    @TestRail(testCaseId=714)
    @Test(priority=33)
    public void DP_Order_Workflow_Transaction_Volume(ITestContext context) throws InterruptedException
    {
    	//Click on Datapower transaction dashboard
    	driver.findElement(By.xpath("//span[contains(.,'DataPower Transactions')]")).click();
    	Thread.sleep(25000);
    	
    	//String event=driver.findElement(By.cssSelector(".amcharts-category-axis > .amcharts-axis-title:nth-child(6) > tspan")).getText();
    	//System.out.println("Value is :" +event);  
    	String event=driver.findElement(By.cssSelector("#viewlet-cdc22cf9-140e-41ec-1315286a7418 .amcharts-value-axis:nth-child(2) > .amcharts-axis-title:nth-child(6) > tspan:nth-child(1)")).getText();
    	System.out.println("data:" +event);
    	
    	if(event.equalsIgnoreCase("Max(ElapsedTime)"))
    	{
    		System.out.println("DP Order Workflow transaction working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "DP Order Workflow transaction");
    	}
    	else
    	{
    		System.out.println("DP Order Workflow transaction failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "DP Order Workflow transaction");
			driver.findElement(By.id("DP Order Workflow transaction failed")).click();
    	}
    }
    @TestRail(testCaseId=704)
    @Test(priority=34)
    public void Order_Workflow_Scorecard(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-6ac5a911-9ead-e4aa-fb4b2694ceb7-table_Severity")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" Severity"))
    	{
    		System.out.println("Order Workflow scorecard working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "Order Workflow scorecard");
    	}
    	else
    	{
    		System.out.println("Order Workflow scorecard failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "Order Workflow scorecard");
			driver.findElement(By.id("Order Workflow scorecard failed")).click();
    	}
    }
    @TestRail(testCaseId=708)
    @Test(priority=35)
    public void DP_Order_Workflow_Events(ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("DP Order Workflow-Events")) {
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
																	"DP_Order_Workflow_Events plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"DP_Order_Workflow_Events plotting events not working fine. ");
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
    @TestRail(testCaseId=705)
    @Test(priority=16)
    public void DP_Order_Workflow_Severity_Piechart(ITestContext context)
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
						
						if (getViewletName.equalsIgnoreCase("DP Order Workflow-Severity Piechart ")) {
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
																"DP_Order_Workflow_Severity_Piechart pie-chart plotting events working fine. ");
														is_tooltippresent = true;
														context.setAttribute("Status",1);
									 					context.setAttribute("Comment", "working fine");
														
													}
													else
													{
														System.out.println(
																"DP_Order_Workflow_Severity_Piechart pie-chart plotting events not working fine");
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
    @TestRail(testCaseId=706)
    @Test(priority=36)
    public void DP_Transaction_Details(ITestContext context)
    {
    	String event=driver.findElement(By.cssSelector("#viewlet-6ae01bc3-87e6-0a50-264919cba10f-table_SetName")).getText();
    	System.out.println("Value is :" +event);
    	
    	if(event.equalsIgnoreCase(" SetName"))
    	{
    		System.out.println("DP Transation Details working");
    		context.setAttribute("Status",1);
			context.setAttribute("Comment", "DP Transation Details");
    	}
    	else
    	{
    		System.out.println("DP Transation Details failed");
    		context.setAttribute("Status",5);
			context.setAttribute("Comment", "DP Transation Details");
			driver.findElement(By.id("DP Transation Details failed")).click();
    	}
}  
    @TestRail(testCaseId=707)
    @Test(priority=37)
    public void DP_Transactions_Events_Stackchart (ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("DP Transactions-Events Stackchart")) {
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
																	"DP_Transactions_Events_Stackchart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"DP_Transactions_Events_Stackchart plotting events not working fine. ");
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
    @TestRail(testCaseId=713)
    @Test(priority=38)
    public void DP_Transactions_Events_Linechart (ITestContext context) throws InterruptedException
    {
    	//Click on Datapower transaction dashboard
    	driver.findElement(By.xpath("//span[contains(.,'DataPower Transactions')]")).click();
    	Thread.sleep(25000);
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
							
							if (getViewletName.equalsIgnoreCase("DP Transactions - Events Linechart")) {
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
																	"DP_Transactions_Events_Linechart plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"DP_Transactions_Events_Linechart plotting events not working fine");
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
    @TestRail(testCaseId=714)
    @Test(priority=39)
    public void DP_OrderWorkflow_Transaction_Volume (ITestContext context)
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
							
							if (getViewletName.equalsIgnoreCase("DP Order Workflow-Transaction Volume")) {
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
																	"DP_OrderWorkflow_Transaction_Volume plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"DP_OrderWorkflow_Transaction_Volume plotting events not working fine. ");
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
    
    
    
    
    
    /*@TestRail(testCaseId=625)
   	@Test(priority=2)
   	public static void AutoPilot_Insight_for_DataPower_Events(ITestContext context) throws InterruptedException
    {
   	    //mouse over on the paichart
    	try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetb0229db4-b186-79dc-15fade871e0b"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							i++;
							//System.out.println("index: " + i++ );
							
							List<WebElement> inner_we = we.findElements(By.tagName("g"));		
							
							for(WebElement inner_ele:inner_we) {
							
								
								List<WebElement> pointer = inner_ele.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-pie-slice")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("AutoPilot_Insight_for_DataPower_Events pie-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
							}
							if(i==7)
							{
							break;
							}
							//return;
								

						} catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=3)
   	public static void AutoPilot_Insight_for_DataPower_Anomalies(ITestContext context) throws InterruptedException
    {
   		Thread.sleep(30000);
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget976894ad-aa12-71c5-0eada4671399"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							
					         System.out.println("index: " + i++ );
							
							WebElement inner_we = we.findElement(By.tagName("g"));																		  							

							List<WebElement> pointer = inner_we.findElements(By.tagName("circle"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-bullet")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(2000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.className("amcharts-balloon-div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("AutoPilot_Insight_for_DataPower_Anomalies anomly-chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}								

						} 
					catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				   // return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=4)
   	public static void AutoPilot_Insight_for_DataPower_Alerts(ITestContext context) throws InterruptedException
    {
    	try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget992db410-5e04-bfe2-b25c8a981c78"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));																		  							

								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("AutoPilot Insight for DataPower Alerts stack-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

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
		}

    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=5)
   	public static void AutoPilot_for_DataPower_Events_by_Resource(ITestContext context) throws InterruptedException

    {
    	//mouse over on the paichart
    	try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetfd587703-c8f2-8c2c-8323482fc9a7"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							i++;
							//System.out.println("index: " + i++ );
							
							List<WebElement> inner_we = we.findElements(By.tagName("g"));		
							
							for(WebElement inner_ele:inner_we) {
							
								
								List<WebElement> pointer = inner_ele.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-pie-slice")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("AutoPilot_for_DataPower_Events_by_Resource pie-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
							}
							if(i==7)
							{
							break;
							}
							//return;
								

						} catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=6)
   	public static void Viewlet_88(ITestContext context) throws InterruptedException
    {
   		//click on datapower metrics dashboard
   		driver.findElement(By.xpath("//li[2]/a/span")).click();
   		Thread.sleep(3000);
   	   	//get Event count of viewlet
   		String Event_Count=driver.findElement(By.xpath("//div[2]/div[2]/ul/li[2]/div/div[3]/span")).getText();
   		System.out.println("viewlet88 Event_count  :"+Event_Count);
   		
   		//get event count of scorecard
   		String Event_Scorecard=driver.findElement(By.xpath("//td[3]/span")).getText();
   		System.out.println("Event_Scorecard EventCount  :"+Event_Scorecard);
   		
   		//verify event count
        if(Event_Count.contains(Event_Scorecard))
        {
        	System.out.println("event count shown as score-card on Viewlet_88");
        }
        else
        {
        	System.out.println("event count not shown as score-card on Viewlet_88");
        }
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=7)
   	public static void  Viewlet93(ITestContext context) throws InterruptedException
    {

   		//get event count of viewlet
   		String Event_Count=driver.findElement(By.xpath("//div[2]/div[2]/ul/li[3]/div/div[3]/span")).getText();
   		System.out.println("Viewlet93_Event_Count:  "+Event_Count);
   		
   		//verify event count\
   		if(Event_Count.equals("221 Events"))
   		{
        	System.out.println("event count shown as score-card on Viewlet93");
   		}
   		else
   		{
        	System.out.println("event count not shown as score-card on Viewlet93");

   		}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=8)
   	public static void DataPowerMetrics_System_Usage(ITestContext context) throws InterruptedException
    {
   	    
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget54b5d97b-dfbb-9190-a2551d59d989"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));																		  							

								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("DataPowerMetrics_System_Usage column-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

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
		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=9)
   	public static void DataPowerMetrics_MQ_Status(ITestContext context) throws InterruptedException
    {
   	    
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget859a8698-d1ac-1383-0495f5e65c2b"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));																		  							

								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("DataPowerMetrics_MQ_Status column-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

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
		}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=10)
   	public static void DataPowerMetrics_CPU_Usage(ITestContext context) throws InterruptedException
    {
   	   
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget82bf58b3-4b91-07db-3631ac9e3c71"));
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
												System.out.println("DataPowerMetrics_CPU_Usage area-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

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
		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=11)
   	public static void DtaPowerMetrics_Memory(ITestContext context) throws InterruptedException
    {
   	    
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget2b3ebcc3-81e4-3aa8-b1bcab60c372"));
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
												System.out.println("DtaPowerMetrics_Memory area-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

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
		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=12)
   	public static void DataPowerMetrics_EMS_Status(ITestContext context) throws InterruptedException
    {
   	    
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget65a2096f-cf99-90e4-ebb317563ca2"));
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
												System.out.println("DataPowerMetrics_EMS_Status area-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

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
		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=13)
   	public static void DataPowerMetrics_HTTP_Mean_Transaction_Time(ITestContext context) throws InterruptedException
    {
   	
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget2308fb2d-daea-36d0-ddedb163be7e"));
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
												System.out.println("DataPowerMetrics_HTTP_Mean_Transaction_Time area-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

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
		}
    }
   	
   	
    @TestRail(testCaseId=625)
    @Parameters({"WgPolicyMgrMsg"})
   	@Test(priority=14)
    public static void TODAY_Piechart_AutoPilot_WorkGroup_Policy_Manager(String WgPolicyMgrMsg, ITestContext context) throws InterruptedException
    {
   		//click on MQ Metrics and events dashboard
   		driver.findElement(By.xpath("//li[3]/a/span")).click();
   		Thread.sleep(3000);
   	    //mouse over on the paichart
   	    Actions Ac=new Actions(driver); 
        WebElement Paichart_tooltip=driver.findElement(By.cssSelector("#widgetdcb17e39-d3e2-0d6d-0cf275a43bc4content .amcharts-pie-item:nth-child(2) > .amcharts-pie-slice")); 
        Thread.sleep(5000);
 
        Ac.moveToElement(Paichart_tooltip).perform();
        Thread.sleep(3000);
        String str=driver.findElement(By.className("amcharts-balloon-div")).getText();
        //System.out.println(str);
        String str2=str.replaceAll("[\r\n]+",",");
        System.out.println(str2);
        //verify
        if(str2.equals(WgPolicyMgrMsg))
	    {
	    	 System.out.println("tooltip Msg is veryfied");
	    	 
	    	 if(WgPolicyMgrMsg.contains("Events Count"))
		     {
			       System.out.println("piechart showing number of event which grouping by eventname & Sverity");  
		     }
	     }
	     else
	     {
	    	 System.out.println("tooltip Msg is not veryfied");

	     }
    }
    
    
    
    @TestRail(testCaseId=625)
   	@Test(priority=15)
   	public static void TODAY_Scorecard_Summary_AutoPilot_WorkGroup_Policy_Manager(ITestContext context) throws InterruptedException
    {
   	
   		//get event count of viewlet
   		String Event_count=driver.findElement(By.xpath("//div[3]/div[2]/ul/li[2]/div/div[3]/span")).getText();
   		System.out.println("Scorecard_Summary EventCount :"+Event_count);
   		
   		//verify event Count
   		if(Event_count.equals("62537 Events"))
   		{
        	System.out.println("score-card Summary showing event count");
   		}
   		else
   		{
        	System.out.println("score-card Summary not showing event count");
   		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=16)
   	public static void TODAY_Scorecard_AutoPilot_WorkGroup_Policy_Manager(ITestContext context) throws InterruptedException
    {
   	   
   		//get event count of viewlet
        String Event_Count=driver.findElement(By.xpath("//div[3]/div[2]/ul/li[3]/div/div[3]/span")).getText();
        System.out.println("Scorecard Event_Count : "+Event_Count);
        
        //verify Event Count
        if(Event_Count.equals("673 Events"))
        {
        	System.out.println("score-card showing event count");
        }
        else
        {
        	System.out.println("score-card not showing event count");
        }
   		
    }
   	
   	
   	
   	
    @Parameters({"Last_xxx_PaichartMsg"})
   	@Test(priority=17)
    public static void  LAST_XXX_Piechart_AutoPilot_WorkGroup_Policy_Manager(String Last_xxx_PaichartMsg, ITestContext context) throws InterruptedException
    {
   	 //click on MQ Metrics and events dashboard
   		driver.findElement(By.xpath("//li[3]/a/span")).click();
   		Thread.sleep(3000);
   		//click on MQ Metrics and events dashboard
   		driver.findElement(By.xpath("//li[3]/a/span")).click();
   		Thread.sleep(3000);
   	    //mouse over on the paichart
   	    Actions Ac=new Actions(driver); 
        WebElement Paichart_tooltip=driver.findElement(By.cssSelector("#widgetaae5a556-872e-3964-c1b376dcc851content .amcharts-pie-item:nth-child(2) > .amcharts-pie-slice")); 
        Thread.sleep(5000);
 
        Ac.moveToElement(Paichart_tooltip).perform();
        Thread.sleep(3000);
        String str=driver.findElement(By.className("amcharts-balloon-div")).getText();
        //System.out.println(str);
        String str2=str.replaceAll("[\r\n]+",",");
        System.out.println(str2);
        //verify
        if(str2.equals(Last_xxx_PaichartMsg))
	     {
		     System.out.println("piechart showing number of event which grouping by eventname & Sverity");  
	     }
	     else
	     {
		     System.out.println("piechart Not showing number of event which grouping by eventname & Sverity");  
	     }
    }
    
    
    
    @TestRail(testCaseId=625)
    @Test(priority=18)
    public static void  LAST_XXX_Scorecard_Summary_AutoPilot_WorkGroup_Policy_Manager(ITestContext context) throws InterruptedException
        {
       	   
       		//get event count of viewlet
            String Event_Count=driver.findElement(By.xpath("//div[3]/div[2]/ul/li[5]/div/div[3]/span")).getText();
            System.out.println("Scorecard Event_Count : "+Event_Count);
            
            //verify Event Count
            if(Event_Count.equals("41647 Events"))
            {
            	System.out.println("score-card showing event count");
            }
            else
            {
            	System.out.println("score-card not showing event count");
            }
       		
    }
    
    
    
    @Test(priority=19)
    public static void  LAST_XXX_Scorecard_AutoPilot_WorkGroup_Policy_Manager(ITestContext context) throws InterruptedException
        {
       	   
       		//get event count of viewlet
            String Event_Count=driver.findElement(By.xpath("//div[2]/ul/li[6]/div/div[3]/span")).getText();
            System.out.println("Scorecard Event_Count : "+Event_Count);
            
            //verify Event Count
            if(Event_Count.equals("893 Events"))
            {
            	System.out.println("score-card showing event count");
            }
            else
            {
            	System.out.println("score-card not showing event count");
            }
       		
     }
    
    
    @TestRail(testCaseId=625)
    @Parameters({"AutoPilot_Events_piechartMsg"})
   	@Test(priority=20)
    public static void  AutoPilot_Events_State_Change_Notify_Email_Task_etc(String AutoPilot_Events_piechartMsg, ITestContext context) throws InterruptedException
    {
   	 
   		//click on MQ Metrics and events dashboard
   		driver.findElement(By.xpath("//li[3]/a/span")).click();
   		Thread.sleep(3000);
   	    //mouse over on the paichart
   	    Actions Ac=new Actions(driver); 
        WebElement Paichart_tooltip=driver.findElement(By.cssSelector("#widget9f44c024-efd7-5d0e-69476cc8159fcontent .amcharts-pie-item:nth-child(3) > .amcharts-pie-slice")); 
        Thread.sleep(5000);
 
        Ac.moveToElement(Paichart_tooltip).perform();
        Thread.sleep(3000);
        String str=driver.findElement(By.className("amcharts-balloon-div")).getText();
        //System.out.println(str);
        String str2=str.replaceAll("[\r\n]+",",");
        System.out.println(str2);
        //verify
        if(str2.equals(AutoPilot_Events_piechartMsg))
	     {
		     System.out.println("piechart showing number of event which grouping by eventname & Sverity");  
	     }
	     else
	     {
		     System.out.println("piechart Not showing number of event which grouping by eventname & Sverity");  
	     }
    
    }
    
    
    
    @TestRail(testCaseId=625)
   	@Test(priority=21)
   	public static void  TODAY_MQ_Events_State_Changes(ITestContext context) throws InterruptedException
    {
   	   
   	    //mouse over on the paichart
   	    Actions Ac=new Actions(driver); 
        WebElement Paichart_tooltip=driver.findElement(By.cssSelector("#widget06215d30-29dd-b281-f39798ec2aeccontent .amcharts-pie-slice")); 
        Thread.sleep(5000);
 
        Ac.moveToElement(Paichart_tooltip).perform();
        Thread.sleep(3000);
        String str=driver.findElement(By.className("amcharts-balloon-div")).getText();
        //System.out.println(str);
        String str2=str.replaceAll("[\r\n]+",",");
        System.out.println(str2);
        //verify
        if(str2.contains("359"))
	     {
		     System.out.println("piechart showing number of event");  
	     }
	     else
	     {
		     System.out.println("piechart Not showing number of event");  
	     }

    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=22)
   	public static void Queue_Anomalies(ITestContext context) throws InterruptedException
    {
   	    //click on MQ Metrics and events dashboard
   		driver.findElement(By.xpath("//li[3]/a/span")).click();
   		Thread.sleep(20000);
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget7928918b-4610-b287-7d14b4722cb3"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							
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
												System.out.println("Queue_Anomalies anomly-chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
							
//							if(i==7)
//							{
//							break;
//							}
						//	return;
								

						} 
					catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				 //   return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
   		
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=23)
   	public static void Missed_SLA_Transactions(ITestContext context) throws InterruptedException
    {  	
       
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget2731e104-3a1f-192c-df66f01ada96"));
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
												System.out.println(" Events by the Minute line-chart plotting events working fine. ");
												
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
		}
	

   
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=24)
   	public static void  Application_Issues_Trading(ITestContext context) throws InterruptedException
   	    {
   			try {
   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

   				for (WebElement e : myElements) {
   					
   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
   						// Place a pointer on x-axis to check event details
   						// Instantiate Action Class
   						Actions actions = new Actions(driver);
   						
   						//Read viewlet data
   						WebElement view_ele=e.findElement(By.id("widget4b454569-5840-33ff-17b89a84b8bd"));
   						// Retrieve WebElement
   						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

   						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
   	                    System.out.println(w_ele.size());
   						for (WebElement we : w_ele) {

   							try {
   								
   								WebElement inner_we = we.findElement(By.tagName("g"));
   								
   									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

   									for (WebElement p : pointer) {

   										if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

   											actions.moveToElement(p).build().perform();

   											Thread.sleep(3000);
   											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

   											try {
   												WebElement tooltip = charts_div.findElement(By.tagName("div"));
   												String events_count = tooltip.getText();
   												
   												System.out.println("Tooltip text: "+ events_count);

   												if (events_count != null) {
   													System.out.println(" Application Issues - Trading Stack-chart plotting events working fine. ");
   													
   												}
   											} catch (NoSuchElementException se) 
   											{
   												System.out.println("Tooltip div not found");
   											}

   											Thread.sleep(3000);

   											// return;
   										}
   									
   								}
   							} catch (NoSuchElementException ex) {
   								// Element is not present
   								//System.out.println("Element not present  " + ex.getMessage());
   								System.out.println("-");
   							}

   						}
   						// Mouse hover

   						Thread.sleep(3000);
   					}
   				}

   			} catch (Exception e) {
   				// TODO: handle exception
   			     e.printStackTrace();
   			}
   	}
   	
   	
   	
    @TestRail(testCaseId=625)
	@Test(priority=25)
	public static void Performance_Issues_Forex(ITestContext context) throws InterruptedException
    {
   	    
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget85e0f78e-5805-4722-5fd8f02b389e"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));
							
								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(3000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("Performance Issues - Forex Stack-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(3000);

										// return;
									}
								
							}
						} catch (NoSuchElementException ex) {
							// Element is not present
							//System.out.println("Element not present  " + ex.getMessage());
							System.out.println("-");
						}

					}
					// Mouse hover

					Thread.sleep(3000);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		     e.printStackTrace();
		}
    }
	
	
    @TestRail(testCaseId=625)
	@Test(priority=26)
	public static void Application_Issues_OTC(ITestContext context) throws InterruptedException
	{
	   	       
				try {
					List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

					for (WebElement e : myElements) {
						
						if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
							// Place a pointer on x-axis to check event details
							// Instantiate Action Class
							Actions actions = new Actions(driver);
							
							//Read viewlet data
							WebElement view_ele=e.findElement(By.id("widget562841b8-991a-9c0d-f730dd69d3ae"));
							// Retrieve WebElement
							WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

							List<WebElement> w_ele = ele.findElements(By.tagName("g"));
		                    System.out.println(w_ele.size());
							for (WebElement we : w_ele) {

								try {
									
									WebElement inner_we = we.findElement(By.tagName("g"));
									
										List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

										for (WebElement p : pointer) {

											if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

												actions.moveToElement(p).build().perform();

												Thread.sleep(3000);
												WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

												try {
													WebElement tooltip = charts_div.findElement(By.tagName("div"));
													String events_count = tooltip.getText();
													
													System.out.println("Tooltip text: "+ events_count);

													if (events_count != null) {
														System.out.println("Applicaiton Issues - Fixed Loan Stack-chart plotting events working fine. ");
														
													}
												} catch (NoSuchElementException se) 
												{
													System.out.println("Tooltip div not found");
												}

												Thread.sleep(3000);

												// return;
											}
										
									}
								} catch (NoSuchElementException ex) {
									// Element is not present
									//System.out.println("Element not present  " + ex.getMessage());
									System.out.println("-");
								}

							}
							// Mouse hover

							Thread.sleep(3000);
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
				e.printStackTrace();
				}
	}
	
	
    @TestRail(testCaseId=625)
	@Test(priority=27)
	public static void Applicaiton_Issues_Fixed_Loan(ITestContext context) throws InterruptedException
	{   	       
	   			try {
	   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

	   				for (WebElement e : myElements) {
	   					
	   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
	   						// Place a pointer on x-axis to check event details
	   						// Instantiate Action Class
	   						Actions actions = new Actions(driver);
	   						
	   						//Read viewlet data
	   						WebElement view_ele=e.findElement(By.id("widgetb03b8b45-3c52-6422-d923f2fa9060"));
	   						// Retrieve WebElement
	   						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

	   						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
	   	                    System.out.println(w_ele.size());
	   						for (WebElement we : w_ele) {

	   							try {
	   								
	   								WebElement inner_we = we.findElement(By.tagName("g"));
	   								
	   									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

	   									for (WebElement p : pointer) {

	   										if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

	   											actions.moveToElement(p).build().perform();

	   											Thread.sleep(5000);
	   											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

	   											try {
	   												WebElement tooltip = charts_div.findElement(By.tagName("div"));
	   												String events_count = tooltip.getText();
	   												
	   												System.out.println("Tooltip text: "+ events_count);

	   												if (events_count != null) {
	   													System.out.println("Applicaiton Issues - Fixed Loan Stack-chart plotting events working fine. ");
	   													
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
	   			}
	}
	
	
	
    @TestRail(testCaseId=625)
	@Test(priority=28)
	public static void Viewlet_188(ITestContext context) throws InterruptedException
    {   	    
			try {
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

				for (WebElement e : myElements) {
					
					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
						// Place a pointer on x-axis to check event details
						// Instantiate Action Class
						Actions actions = new Actions(driver);
						
						//Read viewlet data
						WebElement view_ele=e.findElement(By.id("widget39c347b0-8b11-2412-84d369ae0be9"));
						// Retrieve WebElement
						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
	                    System.out.println(w_ele.size());
						for (WebElement we : w_ele) {

							try {
								
								WebElement inner_we = we.findElement(By.tagName("g"));
								
									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

									for (WebElement p : pointer) {

										if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

											actions.moveToElement(p).build().perform();

											Thread.sleep(5000);
											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

											try {
												WebElement tooltip = charts_div.findElement(By.tagName("div"));
												String events_count = tooltip.getText();
												
												System.out.println("Tooltip text: "+ events_count);

												if (events_count != null) {
													System.out.println("viewlet-188 Stack-chart plotting events working fine. ");
													
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
			}
	}	 
	
	
    @TestRail(testCaseId=625)
   	@Test(priority=29)
   	public static void WebLogic_Scorecard(ITestContext context) throws InterruptedException
    {
   		//click on JVM Analytics dashboard
   		driver.findElement(By.xpath("//li[4]/a/span")).click();
   		Thread.sleep(3000);
   		
   		//get events of Score card
   		String Event_count=driver.findElement(By.xpath("//div[3]/ul/li/div/div[3]/span")).getText();
   		System.out.println("WebLogic_Scorecard Event :"+Event_count);
   		
   		//verify Event Count
   		if(Event_count.equals("18 Events"))
   		{
   			System.out.println("WebLogic_Scorecard showing event on scorecard");
   		}
   		else
   		{
   			System.out.println("WebLogic_Scorecard not showing event on scorecard");

   		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=30)
   	public static void WAS_Scorecard(ITestContext context) throws InterruptedException
    {
   	     		
   		//get events of Score card
   		String Event_count=driver.findElement(By.xpath("//div[4]/div/ul/li/div/div[3]/span")).getText();
   		System.out.println("WAS_Scorecard Event :"+Event_count);
   		
   		//verify Event Count
   		if(Event_count.equals("199 Events"))
   		{
   			System.out.println("WAS_Scorecard showing event order by severity");
   		}
   		else
   		{
   			System.out.println("WAS_Scorecard not showing event order by severity");

   		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=31)
   	public static void  JBOSS_ScoreCard(ITestContext context) throws InterruptedException
    {   		
   	    //get events of Score card
   		String Event_count=driver.findElement(By.xpath("//div[4]/div[2]/ul/li[2]/div/div[3]/span")).getText();
   		System.out.println("JBOSS_ScoreCard Event :"+Event_count);
   		
   		//verify Event Count
   		if(Event_count.equals("6 Events"))
   		{
   			System.out.println("JBOSS_ScoreCard showing event grouping & order by severity");
   		}
   		else
   		{
   			System.out.println("JBOSS_ScoreCard not showing event grouping & order by severity");

   		}
   		
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=32)
   	public static void WebLogic_HeapSize(ITestContext context) throws InterruptedException
    {   
   	    //mouse over on the WebLogic_HeapSize paichart
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget44b641ab-9484-aad2-98c4dcfec789"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							i++;
							//System.out.println("index: " + i++ );
							
							List<WebElement> inner_we = we.findElements(By.tagName("g"));		
							
							for(WebElement inner_ele:inner_we) {
							
								
								List<WebElement> pointer = inner_ele.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-pie-slice")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("WebLogic_HeapSize pie-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
							}
							if(i==7)
							{
							break;
							}
							//return;
								

						} catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=33)
   	public static void WebLogic_CPU_Warnings(ITestContext context) throws InterruptedException
    {   		
   	    //get events of Score card
   		Thread.sleep(2000);
   		String Event_count=driver.findElement(By.xpath("//div[3]/ul/li[4]/div/div[3]/span")).getText();
   		System.out.println("WebLogic_CPU_Warnings Event :"+Event_count);
   		
   		//verify Event Count
   		if(Event_count.equals("18 Events"))
   		{
   			System.out.println("WebLogic_CPU_Warnings showing event grouping & order by resourcename,severity");
   		}
   		else
   		{
   			System.out.println("WebLogic_CPU_Warnings not showing event grouping & order by resourcename,severity");

   		}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=34)
   	public static void WAS_CPU_Warnings(ITestContext context) throws InterruptedException
    {   		
   	   //get events of Score card
   		Thread.sleep(3000);
   		String Event_count=driver.findElement(By.xpath("//li[5]/div/div[3]/span")).getText();
   		System.out.println("WAS_CPU_Warnings Event :"+Event_count);
   		
   		//verify Event Count
   		if(Event_count.equals("4 Events"))
   		{
   			System.out.println("WAS_CPU_Warnings showing event grouping with resourcename,severity");
   		}
   		else
   		{
   			System.out.println("WAS_CPU_Warnings not showing event grouping with resourcename,severity");

   		}
   		   		
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=35)
   	public static void JBoss_CPU_Warnings(ITestContext context) throws InterruptedException
    {   		
   	    //get events of Score card
   		Thread.sleep(3000);
   		String Event_count=driver.findElement(By.xpath("//div[2]/ul/li[5]/div/div[3]/span")).getText();
   		System.out.println("JBoss_CPU_Warnings Event :"+Event_count);
   		
   		//verify Event Count
   		if(Event_count.equals("16 Events"))
   		{
   			System.out.println("JBoss_CPU_Warnings showing event grouping with resourcename,severity");
   		}
   		else
   		{
   			System.out.println("JBoss_CPU_Warnings not showing event grouping with resourcename,severity");

   		}
   		
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=36)
   	public static void  Jboss_Issues(ITestContext context) throws InterruptedException
    {
   	    //click on JVM Analytics dashboard
   		driver.findElement(By.xpath("//li[4]/a/span")).click();
   		Thread.sleep(3000);

		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget810ac383-a618-61af-b97afd09ebaf"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));
							
								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(3000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println(" Jboss_Issues stack-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(3000);

										// return;
									}
								
							}
						} catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present : " + ex.getMessage());
						}

					}
					// Mouse hover

					Thread.sleep(3000);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	

    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=37)
   	public static void  WAS_Issues(ITestContext context) throws InterruptedException
   	    {
   	   	    

   			try {
   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

   				for (WebElement e : myElements) {
   					
   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
   						// Place a pointer on x-axis to check event details
   						// Instantiate Action Class
   						Actions actions = new Actions(driver);
   						
   						//Read viewlet data
   						WebElement view_ele=e.findElement(By.id("widget91e6527e-5833-1dcb-69a4443699a1"));
   						// Retrieve WebElement
   						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

   						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
   	                    System.out.println(w_ele.size());
   						for (WebElement we : w_ele) {

   							try {
   								
   								WebElement inner_we = we.findElement(By.tagName("g"));
   								
   									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

   									for (WebElement p : pointer) {

   										if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

   											actions.moveToElement(p).build().perform();

   											Thread.sleep(3000);
   											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

   											try {
   												WebElement tooltip = charts_div.findElement(By.tagName("div"));
   												String events_count = tooltip.getText();
   												
   												System.out.println("Tooltip text: "+ events_count);

   												if (events_count != null) {
   													System.out.println(" WAS Issues stack-chart plotting events working fine. ");
   													
   												}
   											} catch (NoSuchElementException se) 
   											{
   												System.out.println("Tooltip div not found");
   											}

   											Thread.sleep(3000);

   											// return;
   										}
   									
   								}
   							} catch (NoSuchElementException ex) {
   								// Element is not present
   								System.out.println("Element not present : " + ex.getMessage());
   							}

   						}
   						// Mouse hover

   						Thread.sleep(3000);
   					}
   				}

   			} catch (Exception e) {
   				// TODO: handle exception
   				e.printStackTrace();
   			}
   	}
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=38)
   	public static void  WAS_HeapSize(ITestContext context) throws InterruptedException
   	   	    {
   	   	   	    
   	   			try {
   	   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

   	   				for (WebElement e : myElements) {
   	   					
   	   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
   	   						// Place a pointer on x-axis to check event details
   	   						// Instantiate Action Class
   	   						Actions actions = new Actions(driver);
   	   						
   	   						//Read viewlet data
   	   						WebElement view_ele=e.findElement(By.id("widgetb344a2e7-633c-119f-dd70b33c5158"));
   	   						// Retrieve WebElement
   	   						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

   	   						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
   	   	                    System.out.println(w_ele.size());
   	   						for (WebElement we : w_ele) {

   	   							try {
   	   								
   	   								WebElement inner_we = we.findElement(By.tagName("g"));
   	   								
   	   									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

   	   									for (WebElement p : pointer) {

   	   										if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

   	   											actions.moveToElement(p).build().perform();

   	   											Thread.sleep(3000);
   	   											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

   	   											try {
   	   												WebElement tooltip = charts_div.findElement(By.tagName("div"));
   	   												String events_count = tooltip.getText();
   	   												
   	   												System.out.println("Tooltip text: "+ events_count);

   	   												if (events_count != null) {
   	   													System.out.println(" WAS Issues stack-chart plotting events working fine. ");
   	   													
   	   												}
   	   											} catch (NoSuchElementException se) 
   	   											{
   	   												System.out.println("Tooltip div not found");
   	   											}

   	   											Thread.sleep(3000);

   	   											// return;
   	   										}
   	   									
   	   								}
   	   							} catch (NoSuchElementException ex) {
   	   								// Element is not present
   	   								//System.out.println("Element not present : " + ex.getMessage());
   	   							}

   	   						}
   	   						// Mouse hover

   	   						Thread.sleep(3000);
   	   					}
   	   				}

   	   			} catch (Exception e) {
   	   				// TODO: handle exception
   	   				e.printStackTrace();
   	   			}
   	   		

   	}
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=39)
   	public static void  JBoss_HeapSize(ITestContext context) throws InterruptedException
   	{
   	   	   	   	    //click on JVM Analytics dashboard
   	   	   	   		driver.findElement(By.xpath("//li[4]/a/span")).click();
   	   	   	   		Thread.sleep(3000);

   	   	   			try {
   	   	   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

   	   	   				for (WebElement e : myElements) {
   	   	   					
   	   	   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
   	   	   						// Place a pointer on x-axis to check event details
   	   	   						// Instantiate Action Class
   	   	   						Actions actions = new Actions(driver);
   	   	   						
   	   	   						//Read viewlet data
   	   	   						WebElement view_ele=e.findElement(By.id("widget3867d6e5-6e9a-2d51-650e52cfaf8e"));
   	   	   						// Retrieve WebElement
   	   	   						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

   	   	   						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
   	   	   	                    System.out.println(w_ele.size());
   	   	   						for (WebElement we : w_ele) {

   	   	   							try {
   	   	   								
   	   	   								WebElement inner_we = we.findElement(By.tagName("g"));
   	   	   								
   	   	   									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

   	   	   									for (WebElement p : pointer) {

   	   	   										if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

   	   	   											actions.moveToElement(p).build().perform();

   	   	   											Thread.sleep(3000);
   	   	   											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

   	   	   											try {
   	   	   												WebElement tooltip = charts_div.findElement(By.tagName("div"));
   	   	   												String events_count = tooltip.getText();
   	   	   												
   	   	   												System.out.println("Tooltip text: "+ events_count);

   	   	   												if (events_count != null) {
   	   	   													System.out.println(" JBoss_HeapSize stack-chart plotting events working fine. ");
   	   	   													
   	   	   												}
   	   	   											} catch (NoSuchElementException se) 
   	   	   											{
   	   	   												System.out.println("Tooltip div not found");
   	   	   											}

   	   	   											Thread.sleep(3000);

   	   	   											// return;
   	   	   										}
   	   	   									
   	   	   								}
   	   	   							} catch (NoSuchElementException ex) {
   	   	   								// Element is not present
   	   	   								System.out.println("Element not present : " + ex.getMessage());
   	   	   							}

   	   	   						}
   	   	   						// Mouse hover

   	   	   						Thread.sleep(3000);
   	   	   					}
   	   	   				}

   	   	   			} catch (Exception e) {
   	   	   				// TODO: handle exception
   	   	   				e.printStackTrace();
   	   	   			}
   	   	   		

   	}
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=40)
   	public static void WebLogic_Errors_Stackchart(ITestContext context) throws InterruptedException
    {
   	            
	   			try {
	   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

	   				for (WebElement e : myElements) {
	   					
	   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
	   						// Place a pointer on x-axis to check event details
	   						// Instantiate Action Class
	   						Actions actions = new Actions(driver);
	   						
	   						//Read viewlet data
	   						WebElement view_ele=e.findElement(By.id("widget1d04d99c-aece-3da9-26c588b4e777"));
	   						// Retrieve WebElement
	   						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

	   						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
	   	                    System.out.println(w_ele.size());
	   						for (WebElement we : w_ele) {

	   							try {
	   								
	   								WebElement inner_we = we.findElement(By.tagName("g"));
	   								
	   									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

	   									for (WebElement p : pointer) {

	   										if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

	   											actions.moveToElement(p).build().perform();

	   											Thread.sleep(3000);
	   											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

	   											try {
	   												WebElement tooltip = charts_div.findElement(By.tagName("div"));
	   												String events_count = tooltip.getText();
	   												
	   												System.out.println("Tooltip text: "+ events_count);

	   												if (events_count != null) {
	   													System.out.println("WebLogic Errors Stackchart plotting events working fine. ");
	   													
	   												}
	   											} catch (NoSuchElementException se) 
	   											{
	   												System.out.println("Tooltip div not found");
	   											}

	   											Thread.sleep(3000);

	   											// return;
	   										}
	   									
	   								}
	   							} catch (NoSuchElementException ex) {
	   								// Element is not present
	   								System.out.println("Element not present : " + ex.getMessage());
	   							}

	   						}
	   						// Mouse hover

	   						Thread.sleep(3000);
	   					}
	   				}

	   			} catch (Exception e) {
	   				// TODO: handle exception
	   				e.printStackTrace();
	   			}
	   		

    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=41)
   	public static void WAS_Errors_Stackchart(ITestContext context) throws InterruptedException
    {
   	       //click on JVM Analytics dashboard
	   		driver.findElement(By.xpath("//li[4]/a/span")).click();
	   		Thread.sleep(3000);

			try {
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

				for (WebElement e : myElements) {
					
					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
						// Place a pointer on x-axis to check event details
						// Instantiate Action Class
						Actions actions = new Actions(driver);
						
						//Read viewlet data
						WebElement view_ele=e.findElement(By.id("widget434aef6e-7892-f455-2c0f5c9d2d8a"));
						// Retrieve WebElement
						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
	                    System.out.println(w_ele.size());
						for (WebElement we : w_ele) {

							try {
								
								WebElement inner_we = we.findElement(By.tagName("g"));
								
									List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

									for (WebElement p : pointer) {

										if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

											actions.moveToElement(p).build().perform();

											Thread.sleep(3000);
											WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

											try {
												WebElement tooltip = charts_div.findElement(By.tagName("div"));
												String events_count = tooltip.getText();
												
												System.out.println("Tooltip text: "+ events_count);

												if (events_count != null) {
													System.out.println("WAS Errors Stackchart plotting events working fine. ");
													
												}
											} catch (NoSuchElementException se) 
											{
												System.out.println("Tooltip div not found");
											}
                                              
											Thread.sleep(3000);

											// return;
											
										}
									
								}
									
							} catch (NoSuchElementException ex) {
								// Element is not present
								//System.out.println("Element not present : " + ex.getMessage());
							}
						}
						// Mouse hover

						Thread.sleep(3000);
						
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=42)
   	public static void Viewlet_42(ITestContext context) throws InterruptedException
    {
   	    
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget1662fb86-f2b3-7623-fd31e6b925cb"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));
							
								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(3000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("Viewlet_42 Stackchart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}
                                          
										Thread.sleep(3000);

										// return;
										
									}								
							}
						} catch (NoSuchElementException ex) {
							// Element is not present
							//System.out.println("Element not present : " + ex.getMessage());
						}
					}
					// Mouse hover

					Thread.sleep(3000);
					
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=43)
   	public static void WebLogic_Anomaly_Chart(ITestContext context) throws InterruptedException
    {
   	    
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetc52fa165-ec2f-0f5d-6c97f3a2612b"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							
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
												System.out.println("Queue_Anomalies anomly-chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
					
						} 
					catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				 //   return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
   			
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=44)
   	public static void WAS_Anomaly_chart(ITestContext context) throws InterruptedException
    {

   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetfa3b2488-b50c-4e1f-3cb878b48e8b"));
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
												System.out.println("WAS_Anomaly_chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
								if(i==50)
								{
								break;
								}
					
						} 
					catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				 //   return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
   			
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=45)
   	public static void JBoss_Anomaly_chart(ITestContext context) throws InterruptedException
    {
   	    //click on JVM Analytics dashboard
   		driver.findElement(By.xpath("//li[4]/a/span")).click();
   		Thread.sleep(20000);

   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget37427f40-906b-bee0-f9c3fc382aba"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							
							
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
												System.out.println("JBoss_Anomaly_chart plotting activities working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
								if(i==48)
								{
								break;
								}
					
						} 
					catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
				 //   return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=46)
   	public static void DPOrderWorkflow_Breakdown_by_Application(ITestContext context) throws InterruptedException
    {
   		//click on Datapower transaction dashboard
   		driver.findElement(By.xpath("//li[5]/a/span")).click();
   		Thread.sleep(3000);
   		
   	    //mouse over on the Breakdown_by_Application paichart
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetc18815f7-dc16-cf96-d3b62d1d752e"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                   // System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							i++;
							//System.out.println("index: " + i++ );
							
							List<WebElement> inner_we = we.findElements(By.tagName("g"));		
							
							for(WebElement inner_ele:inner_we) {
							
								
								List<WebElement> pointer = inner_ele.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-pie-slice")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("DPOrderWorkflow_Breakdown_by_Application pie-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
							}
							if(i==7)
							{
							break;
							}
							//return;
								

						} catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
   		
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=47)
   	public static void OrderWorkflow_Scorecard_II(ITestContext context) throws InterruptedException
    {   		
   	    //get events of Score card
   		Thread.sleep(3000);
   		String Event_count=driver.findElement(By.xpath("//div[5]/div/ul/li[3]/div/div[3]/span")).getText();
   		System.out.println("Scorecard_II Event :"+Event_count);
   		
   		//verify Event Count
   		if(Event_count.equals("160 Events"))
   		{
   			System.out.println("Scorecard_II showing events grouping with severity");
   		}
   		else
   		{
   			System.out.println("Scorecard_II not showing events grouping with severity");
   		}
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=48)
   	public static void DPOrderWorkflow_SeverityPiechart(ITestContext context) throws InterruptedException
    {   	
   	   
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget8829c7aa-46d4-bf5e-53a7c8728169"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                   // System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							i++;
							//System.out.println("index: " + i++ );
							
							List<WebElement> inner_we = we.findElements(By.tagName("g"));		
							
							for(WebElement inner_ele:inner_we) {
							
								
								List<WebElement> pointer = inner_ele.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-pie-slice")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("DPOrderWorkflow_Severity pie-chart plotting events working fine.");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}
							}
							if(i==7)
							{
							break;
							}
							//return;
								

						} catch (NoSuchElementException ex) {
							// Element is not present
							System.out.println("Element not present  " + ex.getMessage());
						}

					}
					// Mouse hover
					
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
   	   
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=49)
   	public static void DPTransaction_Details(ITestContext context) throws InterruptedException
    {   		
   	    //get records of table
   		WebElement DPTransaction_Details=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widget6ae01bc3-87e6-0a50-264919cba10fcontentprojectTable\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=DPTransaction_Details.findElements(By.className("jqgrow"));
 		System.out.println("Activity Details: "+ list.size());
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
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=50)
   	public static void  DP_Transactions_Events_Stackchart(ITestContext context) throws InterruptedException
    {
   	    
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetd5c6c85d-9cc5-9d63-c69450aa9ee8"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));
							
								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(3000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("DP_Transactions_Events_Stackchart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}
                                          
										Thread.sleep(3000);

										// return;
										
									}								
							}
						} catch (NoSuchElementException ex) {
							// Element is not present
							//System.out.println("Element not present : " + ex.getMessage());
						}
					}
					// Mouse hover

					Thread.sleep(3000);
					
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=51)
   	public static void  DP_Order_Workflow_Events(ITestContext context) throws InterruptedException
    {
   	    //click on Datapower transaction dashboard
   		driver.findElement(By.xpath("//li[5]/a/span")).click();
   		Thread.sleep(3000);
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetfbddfa45-99f1-4c2d-7e9f1d20aad2"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
					for (WebElement we : w_ele) {

						try {
							
							WebElement inner_we = we.findElement(By.tagName("g"));
							
								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front ")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(3000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("DP_Order_Workflow_Events Stackchart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}
                                          
										Thread.sleep(3000);

										// return;
										
									}								
							}
						} catch (NoSuchElementException ex) {
							// Element is not present
							//System.out.println("Element not present : " + ex.getMessage());
						}
					}
					// Mouse hover

					Thread.sleep(3000);
					
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
    }

   	
    @TestRail(testCaseId=625)
   	@Test(priority=52)
   	public static void  Payment_Queues_PUT_and_GET_Counts(ITestContext context) throws InterruptedException
    {
   	        
   			try {
   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

   				for (WebElement e : myElements) {
   					
   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
   						// Place a pointer on x-axis to check event details
   						// Instantiate Action Class
   						Actions actions = new Actions(driver);
   						
   						//Read viewlet data
   						WebElement view_ele=e.findElement(By.id("widget1e1a2a58-07ec-6735-fe148fa05fac"));
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
   													System.out.println("Payment Queues PUT and GET Counts line-chart plotting events working fine. ");
   													
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
   			}
   		
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=53)
   	public static void Queue_Depth_Trade_Verification_Output_Queue(ITestContext context) throws InterruptedException
    {
   	        
			try {
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

				for (WebElement e : myElements) {
					
					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
						// Place a pointer on x-axis to check event details
						// Instantiate Action Class
						Actions actions = new Actions(driver);
						
						//Read viewlet data
						WebElement view_ele=e.findElement(By.id("widget2e71853e-1872-9d09-996cac02a0ef"));
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
													System.out.println("Payment Queues PUT and GET Counts area-chart plotting events working fine. ");
													
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
			}
		
    }
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=54)
   	public static void Queue_Depth_Customer_Info_Response_Queue(ITestContext context) throws InterruptedException
    {
   	    
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget505f2ab7-41e1-ed30-39b6a5db29d4"));
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
												System.out.println("Customer Info Response Queue area-chart plotting events working fine. ");
												
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
		}
    } 
   	
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=55)
   	public static void Channel_Stats_for_Named_Channel(ITestContext context) throws InterruptedException
    {
   	        //click on MQ Metrics and events dashboard
			driver.findElement(By.xpath("//li[3]/a/span")).click();
  		    Thread.sleep(3000);
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

			for (WebElement e : myElements) {
				
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget20724e8f-737a-2145-e53c5e1d4ec0"));
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
												System.out.println("Channel Stats for Named Channel(s) area-chart plotting events working fine. ");
												
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
		}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=56)
   	public static void DP_Transactions_Events_Linechart(ITestContext context) throws InterruptedException
    {
   	        
   			try {
   				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

   				for (WebElement e : myElements) {
   					
   					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
   						// Place a pointer on x-axis to check event details
   						// Instantiate Action Class
   						Actions actions = new Actions(driver);
   						
   						//Read viewlet data
   						WebElement view_ele=e.findElement(By.id("widget0ee396c9-bed2-47f8-7524e5743c37"));
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
   													System.out.println("DP Transactions - Events Linechart plotting events working fine. ");
   													
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
   			}
    }
   	
   	
    @TestRail(testCaseId=625)
   	@Test(priority=57)
   	public static void  DP_Order_Workflow_Transaction_Volume(ITestContext context) throws InterruptedException
    {
   	       //click on Datapower transaction dashboard
			driver.findElement(By.xpath("//li[5]/a/span")).click();
			Thread.sleep(3000);
			try {
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));

				for (WebElement e : myElements) {
					
					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
						// Place a pointer on x-axis to check event details
						// Instantiate Action Class
						Actions actions = new Actions(driver);
						
						//Read viewlet data
						WebElement view_ele=e.findElement(By.id("widgetcdc22cf9-140e-41ec-1315286a7418"));
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
													System.out.println("DP_Order_Workflow_Transaction_Volume colchart plotting events working fine. ");
													
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
			}
    }*/
   	
   	@Test(priority=58)
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
