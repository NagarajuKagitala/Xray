package GlobalRepositories;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestClass;

@Listeners(TestClass.class)
public class GlobalRepo_Covid19 
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
    @Test(priority=2)
    public static void SummaryViewlet() throws InterruptedException
    {
    	//get summary
    	Thread.sleep(3000);
    	String data=driver.findElement(By.cssSelector(".inner-container")).getText();
    	System.out.println(data);
    	
    }
    @Test(priority=4)
    public static void covid19Overview()throws InterruptedException
    {
    	try
		{                                                      
		WebElement we=(WebElement) driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/ul")).findElement(By.tagName("tbody"));
		List<WebElement> lst=we.findElements(By.className("jqgrow"));
		System.out.println("covid19OverviewRowSize: "+ lst.size());
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
	 					if(data1.contains("US"))
	 					{
	 						System.out.println("covid19Overview showing result");
	 						
		 					break;
	 					}
	 					
			
			
			  else {
			 
			  System.out.println("covid19Overview not showing result");
			  
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
    @Test(priority=5)
    public static void top_10_countries_by_confirm_cases()
    {
    	 WebElement we=(WebElement) driver.findElement(By.xpath("/html/body/form/div[1]/div[2]/div[2]/div/div[2]/div/div[1]/ul/li[3]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
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
 	 					System.out.println("top_10_countries_by_confirm_cases showing result");
 	 				}
 	 				else
 	 				{
 	 					System.out.println("top_10_countries_by_confirm_cases not showing result");
 			            
 	 				}
 	 		 }
 			 
 		}
 			// break;
 		}
	@Test(priority=6)
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
    
    }
