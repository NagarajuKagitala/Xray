package GlobalRepositories;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

//import org.apache.commons.io.FileUtils;
//import org.apache.xml.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestRail;
import testrail.TestRailAPI;

public class GlobalRepo_SmapleMFT 
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
    public static void SelectRepositories(String sRepositories ) throws InterruptedException
    {
       	String repodata = ".//*[contains(@id," + "'" + sRepositories + "'" + ")]";
   		// Click on Dropdown
   		driver.findElement(By.cssSelector("span.select2-selection__arrow")).click();
   		driver.findElement(By.xpath(repodata)).click();
   		Thread.sleep(6000);	
    }
    
    
    @TestRail(testCaseId=626)
   	@Test(priority=2)
   	public static void MFTSupport_FileSearch(ITestContext context) throws InterruptedException
    {
   		//get records of table
 		WebElement MFTSupport_FileSearch=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widget4d5dee62-8b3c-7f97-bb46a4b7810fcontentprojectTable\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> lst=MFTSupport_FileSearch.findElements(By.className("jqgrow"));
 		System.out.println("MFTFile Table: "+ lst.size());
 		for(WebElement row : lst)
 		{
 		    List<WebElement> lstwele=row.findElements(By.tagName("td"));
 		    System.out.println(row.getText());

         }
    }
   	
   	
   	
    @TestRail(testCaseId=626)
   	@Test(priority=3)
   	public static void File_Transfer_Tracking_Details(ITestContext context) throws InterruptedException
    {
   		//click on MFT Dashboard
   		driver.findElement(By.xpath("//span[contains(.,'MFT Dashboard')]")).click();
   		Thread.sleep(2000);
   		
   		//click on cancel btn on popup
   		driver.findElement(By.cssSelector("div:nth-child(1) > .primary-btn:nth-child(2)")).click();
   		Thread.sleep(2000);
   		
   		//Get records of table
   		WebElement FileTransfer_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widget5d7b1124-fc7a-8029-18180a8ba12ccontentprojectTable\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> lst=FileTransfer_Table.findElements(By.className("jqgrow"));
 		System.out.println("FileTransfer Table: "+ lst.size());
 		for(WebElement row : lst)
 		{
 			List<WebElement> lstwele=row.findElements(By.tagName("td"));
 		    System.out.println(row.getText());
// 		    List<WebElement> lstwele=row.findElements(By.tagName("td"));
// 			for(WebElement td :lstwele)
// 			 {
//  	          Stream<WebElement> Data=((Collection<WebElement>) td).stream().filter(value -> value != null);
//  	             System.out.println(Data);
// 				 			        			     
// 	          }

         }  
   		
    }
   	
   	
    @TestRail(testCaseId=626)
   	@Test(priority=4)
   	public static void MFT_Environment_ResourceSummary(ITestContext context) throws InterruptedException
    {
   	    //click on MFT Environment dashboard
   		driver.findElement(By.xpath("//li[3]/a/span")).click();
   		Thread.sleep(5000);
   		
   		WebElement ResourceSummary=(WebElement) driver.findElement(By.id("widget04a8ca65-8872-7f7b-bba58a31a238"));
 		List<WebElement> list=ResourceSummary.findElements(By.className("summary-child-div"));
 		System.out.println("Activity Details: "+ list.size());
 		for(WebElement row : list)
 		 {
 			
 			       String data=row.getAttribute("innerText");
		           if(!data.isEmpty())
		           {
			        System.out.println(row.getAttribute("innerText"));
		           }             
		 }
    }
   	
   	
    @TestRail(testCaseId=626)
   	@Test(priority=4)
   	public static void  MFT_Environment_FileSummary(ITestContext context) throws InterruptedException
    {
   	      		
   		WebElement FileSummary=(WebElement) driver.findElement(By.id("widget0def7e60-c3de-9e06-039caad6d1ce"));
 		List<WebElement> list=FileSummary.findElements(By.className("summary-child-div"));
 		System.out.println("event Details: "+ list.size());
 		for(WebElement row : list)
 		 {		
 			    String data=row.getAttribute("innerText");
			    if(!data.isEmpty())
			    {
				System.out.println(row.getAttribute("innerText"));
			    }         
	     }
    }
   	
   	
    @TestRail(testCaseId=626)
   	@Test(priority=4)
   	public static void  MFT_Environment_MFT_Objectives(ITestContext context) throws InterruptedException
    {
   		
   		WebElement FileSummary=(WebElement) driver.findElement(By.id("widget52eaf96e-719a-c9de-dddc16f96cd1"));
 		List<WebElement> list=FileSummary.findElements(By.className("summary-child-div"));
 		System.out.println("Activity Details: "+ list.size());
 		String data=FileSummary.getText();
 		System.out.println(data);
		
    }
   	
   	
   	
    @TestRail(testCaseId=626)
   	@Test(priority=4)
   	public static void MFT_Environment_MFT_By_Source_Agent(ITestContext context) throws InterruptedException
    {      
   	    //get Event
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget857cdf7a-d123-47ef-86f98c118b84content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	
    @TestRail(testCaseId=626)
   	@Test(priority=5)
   	public static void  MFT_Environment_File_Status_By_Source(ITestContext context) throws InterruptedException
    {
    		
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget3d5bf877-f6c9-7160-112af111a167content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
    @TestRail(testCaseId=626)
   	@Test(priority=6)
   	public static void MFT_Environment_File_Status_By_Destination(ITestContext context) throws InterruptedException
    {  		
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widgetd6c77ead-d283-1748-96b4bf88943ccontent\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	
   	@Test(priority=7)
   	public static void  MFT_Environment_MFT_By_Destination(ITestContext context) throws InterruptedException
    {   	   
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widgetd81a684a-05d4-94de-70359d85b86econtent\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=8)
   	public static void MFT_Environment_MFT_Event_Summary(ITestContext context) throws InterruptedException
    {
   		
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widgete882a988-5cc2-2c22-5e4a258f1b32content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
   		
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=9)
   	public static void MFT_Environment_MFT_Performance_Scorecard(ITestContext context) throws InterruptedException
    {
   	    
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget28c0b965-53fb-0b52-b728d3f978dccontent\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
   		
   		
    }
   	
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=10)
   	public static void MFT_Environment_Test(ITestContext context) throws InterruptedException
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
					WebElement view_ele=e.findElement(By.id("widget617bb2e3-2a68-eacc-4601b9f62189"));
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
												System.out.println("MFT_Environment_Test pie-chart plotting events working fine. ");
												
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
   	
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=11)
   	public static void MFT_Tracking_MFT_Summary(ITestContext context) throws InterruptedException
    {
   	    //click on MFT Tracking dashboard
   		driver.findElement(By.xpath("//li[4]/a/span")).click();
   		Thread.sleep(5000);
   		
   		WebElement MFT_Summary=(WebElement) driver.findElement(By.id("widget09bc7466-b137-ff08-dfaf99677222"));
 		List<WebElement> list=MFT_Summary.findElements(By.className("summary-child-div"));
 		System.out.println("Activity Details: "+ list.size());
 		for(WebElement row : list)
 		 {
 			    String data=row.getAttribute("innerText");
 			    if(!data.isEmpty())
				{
					System.out.println(row.getAttribute("innerText"));
				}
         }
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=11)
   	public void Events_Activity_Details(ITestContext context) throws InterruptedException
    {		
   		//get records of table
   		WebElement Activity_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widget6260e7f0-d620-b79c-7a4488d1f087contentcontentcontentprojectTable\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=Activity_Table.findElements(By.className("jqgrow"));
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
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=12)
   	public static void MFT_By_Destination(ITestContext context) throws InterruptedException
    {
   	   
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget71ef07a1-f991-dfdf-81ff90f069efcontent\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=13)
   	public static void MFT_By_Source_Agent(ITestContext context) throws InterruptedException
   	    {
   	   	   
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widgetd493fd74-6cdb-e2e4-833902d173a8content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
   	    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=14)
   	public static void File_Status_By_Destination(ITestContext context) throws InterruptedException
    {
   	 
   	    //mouse over on the barchart
   		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);
					
					//Read viewlet data
					WebElement view_ele=e.findElement(By.id("widgetfbf4a195-db5c-f4c1-8af29a165a24"));
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

										Thread.sleep(5000);
										WebElement charts_div = view_ele.findElement(By.className("amcharts-chart-div"));

										try {
											WebElement tooltip = charts_div.findElement(By.tagName("div"));
											String events_count = tooltip.getText();
											
											System.out.println("Tooltip text: "+ events_count);

											if (events_count != null) {
												System.out.println("File Status By Destination bar-chart plotting events working fine. ");
												
											}
										} catch (NoSuchElementException se) 
										{
											System.out.println("Tooltip div not found");
										}

										Thread.sleep(5000);

										// return;
									}
								
							}
								if(i==16)
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
   	
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=15)
   	public static void File_Status_By_Source(ITestContext context) throws InterruptedException
    {
   	    
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget6e2a3d7d-9d0b-4381-151d1e0a2701content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=16)
   	public static void Failed_Transfers(ITestContext context) throws InterruptedException
    {
   	   
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget9fb3e1e1-37dc-915a-a51bd874ceb2content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	@TestRail(testCaseId=626)
    @Parameters({"MFT_PerformanceMsg"})
   	@Test(priority=17)
    public static void MFT_Performance(String MFT_PerformanceMsg, ITestContext context) throws InterruptedException
    {
   	    
   	    //mouse over on the paichart
   	    Actions Ac=new Actions(driver); 
        WebElement Paichart_tooltip=driver.findElement(By.cssSelector("#widget31b33ac2-e211-b4eb-5afcbb409779content .amcharts-pie-slice")); 
        Thread.sleep(5000);
 
        Ac.moveToElement(Paichart_tooltip).perform();
        Thread.sleep(3000);
        String str=driver.findElement(By.className("amcharts-balloon-div")).getText();
        //System.out.println(str);
        String str2=str.replaceAll("[\r\n]+",",");
        System.out.println(str2);
        
        //verify
        if(str2.equals(MFT_PerformanceMsg))
	    {
	    	 System.out.println("tooltip Msg is veryfied");
	     }
	     else
	     {
	    	 System.out.println("tooltip Msg is not veryfied");

	     }
    }
    
    
   	@TestRail(testCaseId=626)
   	@Test(priority=18)
   	public static void SuccessfulTransfers2(ITestContext context) throws InterruptedException
    {
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget10ea1bc3-e3ab-93ba-eec653f1fa64content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=18)
   	public static void MFT_Event_Summary(ITestContext context) throws InterruptedException
    {
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget0143791a-dc9e-85bc-f9209f1a5156content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	@TestRail(testCaseId=626)
    @Parameters({"Recent_TransfersMSG"})
   	@Test(priority=19)
    public static void Recent_Transfers(String Recent_TransfersMSG, ITestContext context) throws InterruptedException
    {
   		
   	    //mouse over on the Recent_Transfers paichart
   	    Actions Ac=new Actions(driver); 
        WebElement Paichart_tooltip=driver.findElement(By.cssSelector("#widget8eb6ea02-6777-f99a-5ef5c878b8eccontent .amcharts-pie-slice")); 
        Thread.sleep(5000);
 
        Ac.moveToElement(Paichart_tooltip).perform();
        Thread.sleep(3000);
        String str=driver.findElement(By.className("amcharts-balloon-div")).getText();
        //System.out.println(str);
        String str2=str.replaceAll("[\r\n]+",",");
        System.out.println(str2);
         //verify
        if(str2.equals(Recent_TransfersMSG))
	    {
	    	 System.out.println("Recent_Transfers tooltip Msg is veryfied");
	     }
	     else
	     {
	    	 System.out.println("Recent_Transfers tooltip Msg is not veryfied");

	     }
    } 
    
    
    
   	@TestRail(testCaseId=626)
   	@Test(priority=20)
   	public static void MFT_sets(ITestContext context) throws InterruptedException
    {
   	    //click on MFT Tracking dashboard
   		driver.findElement(By.xpath("//li[4]/a/span")).click();
   		Thread.sleep(5000);
   	 List<WebElement> tr_collection = driver.findElements(By.xpath("//*[@id=\"gview_widget2eaee745-f444-74d2-ba4352ad0764contentprojectTable\"]"));
     int row_num=1,col_num=1;
     for (WebElement trElement : tr_collection) 
     {
       List<WebElement> td_collection = trElement.findElements(By.className("jqgrow")); 
      int n = td_collection.size();
      for(int i=0;i<td_collection.size();i++)
       System.out.println(td_collection.get(i).getText());
      //System.out.println("NUMBER OF COLUMNS="+td_collection.size());
      col_num=1;
      for (WebElement tdElement  :td_collection)
      {
       System.out.println(tdElement.getText());
       col_num++;
      }
      row_num++;
     }
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=21)
   	public static void List_of_Successful_transfers(ITestContext context) throws InterruptedException
    {
   		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widget6260e7f0-d620-b79c-7a4488d1f087content\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}
		}
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=22)
   	public static void Ad_Hoc_Job_Inqiries(ITestContext context) throws InterruptedException
    {
   	
   	    //get records of table
   		WebElement Event_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widget202ec6b1-3ee6-d600-0f13650e6ab4contentprojectTable\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=Event_Table.findElements(By.className("jqgrow"));
 		System.out.println("Event_Table row : "+ list.size());
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
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=23)
   	public static void List_Received_Xfer_Files(ITestContext context) throws InterruptedException
   	{
   		//click on IB2Bi_Tracking dashboard
   		driver.findElement(By.xpath("//li[5]/a/span")).click();
   		Thread.sleep(3000);
   		
   		WebElement Event_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"widgetefcce599-01c1-6a48-0d082fbe8b30content\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=Event_Table.findElements(By.className("jqgrow"));
 		System.out.println("Event_Table row : "+ list.size());
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
   	
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=23)
   	public static void Activities_Missed_Business_Objectives(ITestContext context) throws InterruptedException
    {
   		
   		WebElement Event_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"widget1f15965b-200e-e29f-ffba49943cc3content\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=Event_Table.findElements(By.className("jqgrow"));
 		System.out.println("Event_Table row : "+ list.size());
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
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=24)
   	public static void Tracking_List_File_Xfers_by_Producer_Sender(ITestContext context) throws InterruptedException
    {
   		WebElement Event_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"widgetd9cbb1a4-0307-6797-c3256d576ee1content\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=Event_Table.findElements(By.className("jqgrow"));
 		System.out.println("Event_Table row : "+ list.size());
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
   	
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=24)
   	public static void Tracking_Xfer_Load_by_Application(ITestContext context) throws InterruptedException
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
					WebElement view_ele=e.findElement(By.id("widget44422e01-0bd2-9a62-ccba38bf89ee"));
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
												System.out.println("Tracking_Xfer_Load_by_Application pie-chart plotting events working fine. ");
												
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
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=25)
   	public static void Total_MFT_Summary(ITestContext context) throws InterruptedException
    {
   	    //click on MFT Business dashboard
   		driver.findElement(By.xpath("//li[6]/a/span")).click();
   		Thread.sleep(5000);
   		WebElement MFT_Summary=(WebElement) driver.findElement(By.id("widgete809b5aa-5dc0-45fb-133af1a6094e"));
 		List<WebElement> list=MFT_Summary.findElements(By.className("summary-child-div"));
 		System.out.println("Activity Details: "+ list.size());
 		for(WebElement row : list)
 		 {
 			
 			       String data=row.getAttribute("innerText");
		           if(!data.isEmpty())
		           {
			        System.out.println(row.getAttribute("innerText"));
		           }             
		 }
    }
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=25)
   	public static void MFT_Business_MFT_Performance_Scorecard(ITestContext context) throws InterruptedException
    {  		
   		WebElement Event_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"widget25cb04a4-b7d4-7252-cc3fb31a3dcfcontent\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=Event_Table.findElements(By.className("jqgrow"));
 		System.out.println("Event_Table row : "+ list.size());
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
   	
   	
   	@TestRail(testCaseId=626)
   	@Test(priority=26)
   	public static void Locate_Files(ITestContext context) throws InterruptedException
    {
   		WebElement Event_Table=(WebElement) driver.findElement(By.xpath("//*[@id=\"widget9f37dd84-efe4-e83f-1b42cecd588bcontent\"]")).findElement(By.tagName("tbody"));
 		List<WebElement> list=Event_Table.findElements(By.className("jqgrow"));
 		System.out.println("Event_Table row : "+ list.size());
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
   	
   	
   	
   	@Test(priority=27)
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

	


    
