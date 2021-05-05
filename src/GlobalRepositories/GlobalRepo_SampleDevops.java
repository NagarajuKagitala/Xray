package GlobalRepositories;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.codec.language.Nysiis;
//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
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

public class GlobalRepo_SampleDevops 
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
		Thread.sleep(15000);	
    }
    
    @TestRail(testCaseId=581)
    @Test(priority=2)
    public static void Threat_Summary(ITestContext context) throws InterruptedException
    {
       	 //mouse over on the Threat_Summary pie-chart 
    	try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			 System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.cssSelector("#viewlet-efdbe78c-d7f2-2e4f-4119f0ad3c48 .amcharts-bg"));
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
												System.out.println("Threat_Summary pie-chart plotting events working fine. ");
												
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
							//System.out.println("Element not present  " + ex.getMessage());
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
    
    @TestRail(testCaseId=582)
	@Test(priority=3)
	public static void Build_scorecard (ITestContext context) throws InterruptedException
	{
    	WebElement we=(WebElement) driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/ul/li[2]/div[2]/div[3]/div/div/div[3]/div[3]/div")).findElement(By.tagName("tbody"));
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
	 					if(data1.contains("WARNING"))
	 					{
	 						System.out.println("Build_scorecard showing result");
	 						context.setAttribute("Status",1);
		 					context.setAttribute("Comment", "working fine");
		 					break;
	 					}
	 					
			
			
					
					  else {
					  
					  System.out.println("Build_scorecard not showing result");
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
	
	
    @TestRail(testCaseId=583)
	@Test(priority=4)
	public static void  ALL_Events(ITestContext context) throws InterruptedException
	{
    	WebElement All_Event=(WebElement) driver.findElement(By.xpath("//*[@id=\"widget8ec7a6ce-4f48-5f39-2c19cf1305a1contentprojectTable\"]")).findElement(By.tagName("tbody"));
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
	 					if(data1.contains("WARNING"))
	 					{
	 						System.out.println("ALL_Events showing result");
	 						context.setAttribute("Status",1);
		 					context.setAttribute("Comment", "working fine");
		 					break;
	 					}
	 					
			
			
			  else {
			 
			  System.out.println("ALL_Events not showing result");
			  context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
			  driver.findElement(By.xpath("Not showing")).click();
			  
			  
			  }
	 				}    
   			     } 
   			 break;
   	}
		
	}
	
    @TestRail(testCaseId=584)
	@Test(priority=5)
	public static void Event_Stackchart(ITestContext context) throws InterruptedException
    {
		Thread.sleep(5000);
		//move to mouse stackchart tooltip
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widget1da71d43-8cdc-84c2-67ae7a848e4a"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                    System.out.println(w_ele.size());
                    int i=0;
					for (WebElement we : w_ele) {

						try {
							i++;
							//System.out.println("Index:"+i++);
							WebElement inner_we = we.findElement(By.tagName("g"));

								List<WebElement> pointer = inner_we.findElements(By.tagName("path"));

								for (WebElement p : pointer) {

									if (p.getAttribute("class").contains("amcharts-graph-column-front")) {

										actions.moveToElement(p).build().perform();

										Thread.sleep(8000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("Event stack-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(3000);

										// return;
									}
								
							}
								if(i==17)
								{
									break;
								}

						} catch (NoSuchElementException ex) {
							// Element is not present
							//System.out.println("Element not present" + ex.getMessage());
						}

					}
					// Mouse hover
					//Thread.sleep(5000);
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
	
    @TestRail(testCaseId=585)
	@Test(priority=6)
	public static void Comparison_of_Longest_Build_Steps(ITestContext context)
	{
			//get records of table
	   		WebElement TimeOrder_Table=(WebElement) driver.findElement(By.xpath("/html/body/form/div[1]/div[1]/div[3]")).findElement(By.tagName("tbody"));
	 		List<WebElement> list=TimeOrder_Table.findElements(By.className("jqgrow"));
	 		System.out.println("Events Details: "+ list.size());
	 		for(WebElement row : list)
	 		{
	 		    List<WebElement> lstwele=row.findElements(By.tagName("td"));
	 			for(WebElement td :lstwele)
	 			 {
	 				String data=td.getText();
	 				if(!data.isEmpty())
	 				{
	 					String data1=td.getText();
	 					System.out.println(data1);
	 					if(data1.contains("ActvityID"))
	 					{
	 						System.out.println("ALL_Events showing result");
	 						context.setAttribute("Status",1);
		 					context.setAttribute("Comment", "working fine");
		 					break;
	 					}
	 					
			
			
			  else {
			 
			  System.out.println("ALL_Events not showing result");
			  context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
			  driver.findElement(By.xpath("Not showing")).click();
			  
			  
			  }
	 				}

	 			 }	
	 			break;
	         }
	}
	
	
    @TestRail(testCaseId=586)
	@Test(priority=7)
	public static void Step_Severity_Levels(ITestContext context) throws InterruptedException
    {
			try {
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
				// System.out.println(myElements.size());
				for (WebElement e : myElements) {
					// System.out.println(e.getAttribute("aria-hidden"));
					// boolean str= e.getAttribute("aria-hidden");
					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
						// Place a pointer on x-axis to check event details
						// Instantiate Action Class
						Actions actions = new Actions(driver);
						
						//Read viewlet data
						WebElement view_ele=e.findElement(By.id("widgeta5a2565e-3b41-a78d-6315ead44ab2"));
						// Retrieve WebElement
						WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

						List<WebElement> w_ele = ele.findElements(By.tagName("g"));
                        System.out.println(w_ele.size());
                        int i=0;
						for (WebElement we : w_ele) {

							try {
								i++;
								//System.out.println("Index:"+i++);
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
													System.out.println("Step Severity Levels line-chart plotting events working fine. ");
													
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

							} catch (NoSuchElementException ex) {
								// Element is not present
								//System.out.println("Element not present  " + ex.getMessage());
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
	
    @TestRail(testCaseId=587)
	@Test(priority=8)
	public static void Events_By_Hour(ITestContext context) throws InterruptedException
    {
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgeta4483f50-84ba-32ae-30955e60ba66"));
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
												System.out.println("Events By Hour line-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(1000);

										// return;
									}
								
							}

						} catch (NoSuchElementException ex) {
							// Element is not present
							//System.out.println("Element not present" + ex.getMessage());
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

  

    

