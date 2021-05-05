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
import org.testng.annotations.Listeners;

import testrail.TestClass;

@Listeners(TestClass.class)
public class GlobalRepo_SampleKafkaTracing 
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
				driver.findElement(By.id("Kafka_Tracing repo failed")).click();
			}	
  }
  //------------Kafka message analysis dashboard----------------------
  @Test(priority=2)
  public static void size_Breakdown(ITestContext context ) throws InterruptedException
  {
	  //test start with default dashboard
	  
	  //get Query data
	  String Query_data=driver.findElement(By.xpath("//div[3]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).getAttribute("title");
	  System.out.println("table data:"+Query_data);
	  
	  //get col name
	  String col_data=driver.findElement(By.xpath("//div[3]/div/div/div[3]/div[2]")).getText();
	  System.out.println("col data:"+col_data);
			 
	  //verify table data
	  if(col_data.contains("Value Size") || (col_data.contains("Events Count")))
	  {
		System.out.println("size beakdown showing records");
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "working fine");
	  }
	  else
	  {
		  System.out.println("size beakdown not showing records");
		  context.setAttribute("Status",5);
			context.setAttribute("Comment","not working fine");
			driver.findElement(By.id("Size breakdown failed")).click();
	  }
  }
  @Test(priority=3)
  public static void Keysize_Breakdown(ITestContext context ) throws InterruptedException
  {
	  //get Query data
	  String Query_data=driver.findElement(By.xpath("//div[3]/div[2]/div/div[2]/ul/li[2]/div[2]/div/div/jkql-input/input")).getAttribute("title");
	  System.out.println("table data:"+Query_data);
	  
	  //get col name
	  String col_data=driver.findElement(By.xpath("//li[2]/div[2]/div[3]/div/div/div[3]/div[2]")).getText();
	  System.out.println("col data:"+col_data);
			 
	  //verify table data
	  if(col_data.contains("KeySize") || (col_data.contains("Events Count")))
	  {
		System.out.println("Keysize beakdown showing records");
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "working fine");
	  }
	  else
	  {
		  System.out.println("Keysize beakdown not showing records");
		  context.setAttribute("Status",5);
			context.setAttribute("Comment","not working fine");
			driver.findElement(By.id("KeySize breakdown failed")).click();
	  }
  }
  //---------------Kafka Topic dashboard-----------------------
  @Test(priority=4)
  public static void Topic_breakdown(ITestContext context ) throws InterruptedException
  {
	  //click on kafka topic dashbaord
	  driver.findElement(By.xpath("//span[contains(.,'Kafka Topics')]")).click();
	  Thread.sleep(10000);
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
							                                     
							if (getViewletName.equalsIgnoreCase("Topic Breakdown")) {
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
																	"Topic_breakdown plotting events working fine. ");
															is_tooltippresent = true;
															
														}
														else
														{
															System.out.println(
																	"Topic_breakdown plotting events not working fine. ");
											               	
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
  //-----------------Kafka Messgae rate dashboard---------------------------------
  @Test(priority=5)
  public static void publish_rate(ITestContext context ) throws InterruptedException
  {
	//click on kafka message rate dashboard
	  driver.findElement(By.xpath("//span[contains(.,'Kafka Message Rates')]")).click();
	  Thread.sleep(10000);
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
							
							if (getViewletName.equalsIgnoreCase("Publish Rate ")) {
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
																	"publish_rate plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
															
														}
														else
														{
															System.out.println(
																	"publish_rate plotting events not working fine");
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
  @Test(priority=6)
  public static void kafka_message_rate_SizeBreakdown(ITestContext context ) throws InterruptedException
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
							
							if (getViewletName.equalsIgnoreCase("Kafka Message Rates_Size Breakdown")) {
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
																	"kafka_message_rate_SizeBreakdown plotting events working fine. ");
															is_tooltippresent = true;
															context.setAttribute("Status",1);
										 					context.setAttribute("Comment", "working fine");
														}
														else
														{
															System.out.println(
																	"kafka_message_rate_SizeBreakdown plotting events not working fine. ");
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
  //---------------------Kafka partitions dashboard-------------------------------
  @Test(priority=7)
  public static void kafka_partituons_partion_alnalysis(ITestContext context) throws InterruptedException
  {
	  //click on kafka partion dashboard
	  driver.findElement(By.xpath("//span[contains(.,'Kafka Partitions')]")).click();
	  Thread.sleep(10000);
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
								if (getViewletName.equalsIgnoreCase("Kafka Partitions_Partion Analysis")) {
								//	System.out.println("HTML:" + ee.getAttribute("innerHTML"));
									String eventcount = e2.findElement(By.className("jqgrow").tagName("div")).getText();
									System.out.println("eventcount:" + eventcount);
									if (!eventcount.isEmpty()) {
										String[] str = eventcount.split(" ");
										System.out.println("events: " + str[0]);
										int eventcounts = Integer.parseInt(str[0]);
										if (eventcount.equals("411 Events")) {
											System.out.println("Event count for kafka_partituons_partion_alnalysis working fine");
											context.setAttribute("Status",1);
						 					context.setAttribute("Comment", "working fine");
										}
										else
										{
											System.out.println("Event count for kafka_partituons_partion_alnalysis not working fine");
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
  @Test(priority=8)
  public static void kafaka_partitions_Keysize_Breakdown(ITestContext context ) throws InterruptedException
  {
	  //get Query data
	  String Query_data=driver.findElement(By.xpath("//div[4]/div[2]/div/div[2]/ul/li[2]/div[2]/div/div/jkql-input/input")).getAttribute("title");
	  System.out.println("table data:"+Query_data);
	  
	  //get col name
	  String col_data=driver.findElement(By.xpath("//div[4]/div[2]/div/div[2]/ul/li[2]/div[2]/div[3]/div/div/div[3]/div[2]")).getText();
	  System.out.println("col data:"+col_data);
			 
	  //verify table data
	  if(col_data.contains("KeySize") || (col_data.contains("Events Count")))
	  {
		System.out.println("kafaka_partitions_Keysize_Breakdown showing records");
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "working fine");
	  }
	  else
	  {
		  System.out.println("kafaka_partitions_Keysize_Breakdown not showing records");
		  context.setAttribute("Status",5);
			context.setAttribute("Comment","not working fine");
			driver.findElement(By.id("Kafka Partitions KeySize breakdown failed")).click();
	  }
  }
  @Test(priority=9)
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
