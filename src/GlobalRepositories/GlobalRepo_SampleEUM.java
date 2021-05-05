package GlobalRepositories;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
/*import org.json.JSONArray;
import org.json.JSONObject;*/
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testrail.Settings;
import testrail.TestRail;
import testrail.TestRailAPI;

public class GlobalRepo_SampleEUM 
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
    @TestRail(testCaseId=588)
	@Parameters({ "sRepositories" })
	@Test(priority = 1)
	public static void SelectRepositories(String sRepositories,ITestContext context) throws InterruptedException {
		try
    	{
    		String repodata = ".//*[contains(@id," + "'" + sRepositories + "'" + ")]";
    		// Click on Dropdown
    		driver.findElement(By.cssSelector("span.select2-selection__arrow")).click();
    		driver.findElement(By.xpath(repodata)).click();
    		Thread.sleep(30000);
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
	
    @TestRail(testCaseId=5)
	@Test(priority = 2)
	public static void Event_count_summary(ITestContext context) throws InterruptedException
    {
		Thread.sleep(3000);
		String data = driver.findElement(By.id("widget33448d32-c52c-aab2-767a7a118f2f")).getText();

		if (!data.isEmpty()) {
			System.out.println(data);
		}
    }
	
	
    @TestRail(testCaseId=588)
	@Test(priority = 3)
	public static void Event_count_console(ITestContext context) throws InterruptedException
    {
		Thread.sleep(3000);
		String data=driver.findElement(By.xpath("//div[2]/div/div[2]/div/span")).getText();
		System.out.println("event count:"+data);
		// click on events counts summary viewlet
		driver.findElement(By.xpath("//div/div[2]/div/div[2]/div")).click();
		Thread.sleep(3000);
		// get records on console
		String eventConsole = driver.findElement(By.xpath("//div[4]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
		String eventConsole1 = eventConsole.replace(",", "");
		System.out.println("console event:"+eventConsole1);
		// 1.7k=1680
		String a = "1680";
		// verify displayed counts and console counts
		if (eventConsole1.contains(a)) {
			System.out.println("event count summary showing same result on console");
		} else 
		{
			System.out.println("event count summary not showing same result on console");

		}
		// click on console close icon
		driver.findElement(By.xpath("//div[3]/div[2]/ul/li/span")).click();
		Thread.sleep(3000);
    }
	
    @TestRail(testCaseId=588)
	@Test(priority = 4)
	public static void SnapShot_Summary_count(ITestContext context) throws InterruptedException
    {
		Thread.sleep(3000);
		String data = driver.findElement(By.id("widget7cc1f3b8-2306-7577-3f04019a3693")).getText();

		if (!data.isEmpty()) {
			System.out.println(data);
		}
    }
	
	
    @TestRail(testCaseId=588)
	@Test(priority = 5)
	public static void Snapshot_Console_count(ITestContext context) throws InterruptedException
    {
		String snapshot_count=driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/div/span")).getText();
		System.out.println("snapshot count:"+snapshot_count);
		// click on snapshot countsummary viewlet
		driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/div")).click();
		Thread.sleep(3000);
		// get records on console
		String Snapshot_console = driver.findElement(By.xpath("//div[4]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
		System.out.println("console count:"+Snapshot_console);
		// verify displayed counts and console counts
		if (Snapshot_console.contains(snapshot_count)) {
			System.out.println("snapshot count summary viewlet  showing same result on console ");
		} else 
		{
			System.out.println("snapshot count summary viewlet not showing same result on console ");

		} 
		// click on console close icon
		driver.findElement(By.xpath("//div[3]/div[2]/ul/li/span")).click();
		Thread.sleep(3000);
    }
	
	
    @TestRail(testCaseId=588)
	@Test(priority = 6)
	public static void Activity_count(ITestContext context) throws InterruptedException
    {
		Thread.sleep(3000);
		String data = driver.findElement(By.id("widget9009bc2e-b2c1-0f32-8e08096e0b33")).getText();

		if (!data.isEmpty()) {
			System.out.println(data);
		}
    }
	
	
    @TestRail(testCaseId=588)
	@Test(priority = 7)
	public static void Activity_Console_count(ITestContext context) throws InterruptedException
    {
		// get activity count summary viewlet
		String Activity_count = driver.findElement(By.xpath("//div[3]/div[2]/div/div[2]/div")).getText();
		System.out.println("Activity count:"+Activity_count);
		// 1k=1004
		String b = "1004";
		String Activity_count1 = b;
		// click on activity count summary viewlet
		driver.findElement(By.xpath("//div[3]/div[2]/div/div[2]/div")).click();
		Thread.sleep(3000);
		// get counts on console
		String Activity_console = driver.findElement(By.xpath("//div[4]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
		String Activity_console1 = Activity_console.replace(",", "");
		System.out.println("console Count:"+Activity_console1);
		// verify displayed counts and console
		if (Activity_console1.contains(Activity_count1)) 
		{
			System.out.println("activity count summary viewlet showing same result on console");
		} 
		else 
		{
			System.out.println("activity count summary viewlet not showing same result on console");

		}
		// click on console close icon
		driver.findElement(By.xpath("//div[3]/div[2]/ul/li/span")).click();
		Thread.sleep(3000);

    }
	
    @TestRail(testCaseId=588)
	@Test(priority = 9)
	public static void Event_severity(ITestContext context) throws InterruptedException {
		// mouse over on the paichart
		Actions builder = new Actions(driver);
		WebElement Event_severity_tooltip = driver.findElement(By.cssSelector("#widget844e5a7d-2772-02fe-8d0556b759b9content .amcharts-pie-item:nth-child(2) > .amcharts-pie-slice"));
		Thread.sleep(5000);

		builder.moveToElement(Event_severity_tooltip).perform();
		Thread.sleep(3000);
		String str = driver.findElement(By.className("amcharts-balloon-div")).getText();
		// System.out.println(str);
		String str2 = str.replaceAll("[\r\n]+", ",");
		System.out.println(str2);
		// verify Event_severity tooltip data
		if (str2.contains("Severity") || (str2.contains("Events Count"))) {
			System.out.println("Event_severity piechart showing event group by severity ");
		} else {
			System.out.println("Event_severity piechart Not showing event group by severity ");
		}
	}

    @TestRail(testCaseId=588)
	@Test(priority = 10)
	public static void Event_Scorecard(ITestContext context) throws InterruptedException 
	{
		WebElement we = (WebElement) driver.findElement(By.xpath("//*[@id=\"widgetfcc05d53-d54b-4044-d779db17065econtent\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("event: " + lst.size());
		for (WebElement row : lst) {
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) {
				String data=td.getAttribute("title").replace("&nbsp;", "").replace(" ", "");
				if(!data.isEmpty())
				{
					System.out.println(data);
				}
				
			}

		}

	}

    @TestRail(testCaseId=588)
	@Test(priority = 11)
	public static void All_Events(ITestContext context) 
	{
		WebElement All_Event = (WebElement) driver.findElement(By.xpath("//*[@id=\"gview_widget8150d7f4-22b2-17d7-0a698a544887contentprojectTable\"]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = All_Event.findElements(By.className("jqgrow"));
		System.out.println("ALLevent: " + lst.size());
		for (WebElement row : lst) {
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

    @TestRail(testCaseId=588)
	@Test(priority = 12)
	public static void AnomalyMoniter(ITestContext context) throws InterruptedException 
	{
		// move to mouse on Anomaly chart
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);

					// Read viewlet data
					WebElement view_ele = e.findElement(By.id("widgete41fe1e1-e9c8-8e01-97d74b2cea5d"));
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

										System.out.println("Tooltip text: " + events_count);

										if (events_count != null) {
											System.out.println(" Anomaly Monitor chart plotting events working fine. ");

										}
									} catch (NoSuchElementException se) {
										System.out.println("Tooltip div not found");
									}

									Thread.sleep(5000);

									// return;
								}

							}

						} catch (NoSuchElementException ex) {
							// Element is not present
							// System.out.println("Element not present " + ex.getMessage());
						}

					}
					// Mouse hover

					//Thread.sleep(5000);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
    @TestRail(testCaseId=588)
	@Test(priority = 13)
	public static void EventSeverityTOPOLOGY_CHART_LEVEL(ITestContext context) throws InterruptedException 
	{
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);

					// Read viewlet data
					WebElement view_ele = e.findElement(By.id("widgetd39177d5-5e78-c759-0c490a7b34a3"));
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

										System.out.println("Tooltip text: " + events_count);

										if (events_count != null) {
											System.out.println(
													" Event Severity TOPOLOGY CHART_LEVEL line-chart plotting events working fine. ");

										}
									} catch (NoSuchElementException se) {
										System.out.println("Tooltip div not found");
									}

									Thread.sleep(1000);

									// return;
								}

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

    @TestRail(testCaseId=600)
	@Test(priority = 14)
	public static void Event_Stackchart(ITestContext context) throws InterruptedException 
	{
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);

					// Read viewlet data
					WebElement view_ele = e.findElement(By.id("widgetdf202076-36bc-2c2a-235d519c7b16"));
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

										System.out.println("Tooltip text: " + events_count);

										if (events_count != null) {
											System.out.println("Event stack-chart plotting events working fine. ");

										}
									} catch (NoSuchElementException se) {
										System.out.println("Tooltip div not found");
									}

									Thread.sleep(1000);

									// return;
								}

							}
							if(i==20)
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

    @TestRail(testCaseId=600)
	@Test(priority =15)
	public static void Event_By_the_hour(ITestContext context) {
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);

					// Read viewlet data
					WebElement view_ele = e.findElement(By.id("widget024fcce1-885b-0817-a7b99fe2f76a"));
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

										System.out.println("Tooltip text: " + events_count);

										if (events_count != null) {
											System.out.println(
													" Event By the hour line-chart plotting events working fine. ");

										}
									} catch (NoSuchElementException se) {
										System.out.println("Tooltip div not found");
									}

									Thread.sleep(1000);

									 return;
								}

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

    @TestRail(testCaseId=600)
	@Test(priority = 16)
	public static void The_10_worst_Events(ITestContext context) throws InterruptedException {
		try {
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[id^='dynamicLayout_d-']"));
			for (WebElement e : myElements) {
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					// Place a pointer on x-axis to check event details
					// Instantiate Action Class
					Actions actions = new Actions(driver);

					// Read viewlet data
					WebElement view_ele = e.findElement(By.id("widgeta3942a9f-56e0-e739-23f6b1aab9f6"));
					// Retrieve WebElement
					WebElement ele = view_ele.findElement(By.className("amcharts-chart-div"));

					List<WebElement> w_ele = ele.findElements(By.tagName("g"));
					System.out.println(w_ele.size());
					int i=0;
					for (WebElement we : w_ele) {

						try {
							//i++;
							System.out.println("Index:"+i++);

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

										System.out.println("Tooltip text: " + events_count);

										if (events_count != null) {
											System.out.println("The 10 worst Events bar-chart plotting events working fine.");

										}
									} catch (NoSuchElementException se) {
										System.out.println("Tooltip div not found");
									}

									Thread.sleep(1000);

									// return;
								}

							}
							if(i==19)
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
	
    @TestRail(testCaseId=600)
	@Test(priority = 17)
	public static void EUM_Breakdown_summary(ITestContext context) throws InterruptedException
    {
		// click om Demo dashboard
		driver.findElement(By.xpath("//li[2]/a/span")).click();
		Thread.sleep(15000);
		
		WebElement Events_By_country = (WebElement) driver.findElement(By.id("widget38e2c19b-7335-a821-4ec3fb5ccc72"));
		List<WebElement> list = Events_By_country.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list)
		{
			String data=row.getAttribute("innerText");
			if (!data.isEmpty()) 
			{
				System.out.println(data);
			}
						
		}
    }
	
	
    @TestRail(testCaseId=600)
	@Test(priority = 18)
	public static void EUM_Breakdown_consoleCount(ITestContext context) throws InterruptedException {
		

		WebElement EUM_Breakdown = (WebElement) driver.findElement(By.id("widget38e2c19b-7335-a821-4ec3fb5ccc72"));
		List<WebElement> list = EUM_Breakdown.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list) {

			try {

				List<WebElement> div_ele = row.findElements(By.tagName("div"));

			//	System.out.println("inner div size: " + div_ele.size());
				for (WebElement we : div_ele) {
					String classname = we.getAttribute("class");
				//	System.out.println("classname: " + classname);

					if (classname.equalsIgnoreCase("ag-heading_body")) {

						//System.out.println("innerhtml: " + we.getAttribute("innerHTML"));

						List<WebElement> lst_div = we.findElements(By.className("inner-summary"));

					//	System.out.println("summary size: " + lst_div.size());

					//	System.out.println("innerhtml: " + lst_div.get(1).getAttribute("innerHTML"));
						
						List<WebElement> events = lst_div.get(1).findElements(By.tagName("span"));
						
					//	System.out.println("span size: " + events.size());

						
						for(WebElement inner_events:events)
						{
							if(inner_events.getAttribute("class").equalsIgnoreCase("inner-summary-value"))
							{
								Thread.sleep(10000);
								System.out.println("events count: "+inner_events.getText());
								String Eumevents=inner_events.getText();
					
				String data = row.getAttribute("innerText");
				if (!data.isEmpty()) 
				{
					row.click();
					Thread.sleep(10000);

					String Console_events = driver.findElement(By.xpath("//div[4]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
				//	System.out.println("console_counts: " + Console_events);

					String[] str = Console_events.split("of ");

					System.out.println("Console events:"+str[1]);
					if(Eumevents.equalsIgnoreCase(str[1]))
					{
						System.out.println("EUM_Breakdown events match with console");
					}
					else
					{
						System.out.println("EUM_Breakdown events Not match with console");

					}
					//click on close console
			  		driver.findElement(By.xpath("//div[3]/ul/li/span")).click();   		
			  		Thread.sleep(3000);
				}
							}
									
						}

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}		          
		}
		

   		
	}

    @TestRail(testCaseId=600)
	@Test(priority = 19)
	public static void Activities_By_country(ITestContext context) throws InterruptedException 
	{
		String data = driver.findElement(By.id("widget6b92b429-11c2-7c52-83a0f481be24")).getText();

		if (!data.isEmpty()) {
			System.out.println(data);
		}
		
	}
	
	
    @TestRail(testCaseId=600)
	@Test(priority = 20)
	public static void Activities_By_country_consoleCount(ITestContext context) throws InterruptedException
    {
		WebElement Events_By_country = (WebElement) driver.findElement(By.id("widget6b92b429-11c2-7c52-83a0f481be24"));
		List<WebElement> list = Events_By_country.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list) {

			try {

				List<WebElement> div_ele = row.findElements(By.tagName("div"));

				//System.out.println("inner div size: " + div_ele.size());
				for (WebElement we : div_ele) {
					String classname = we.getAttribute("class");

					if (classname.equalsIgnoreCase("ag-heading_body")) {


						List<WebElement> lst_div = we.findElements(By.className("inner-summary"));

					
						List<WebElement> events = lst_div.get(0).findElements(By.tagName("span"));
						
						
						for(WebElement inner_events:events)
						{
							if(inner_events.getAttribute("class").equalsIgnoreCase("inner-summary-value"))
							{
								Thread.sleep(10000);
								System.out.println("events count: "+inner_events.getText());
								String Eumevents=inner_events.getText();
								
				String data = row.getAttribute("innerText");
				if (!data.isEmpty()) 
				{
					row.click();
					Thread.sleep(10000);

					String Console_events = driver.findElement(By.xpath("//div[4]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
				//	System.out.println("console_counts: " + Console_events);

					String[] str = Console_events.split("of ");

					System.out.println("Console events:"+str[1]);
					if(Eumevents.equalsIgnoreCase(str[1]))
					{
						System.out.println("Activities_By_country events match with console");
					}
					else
					{
						System.out.println("Activities_By_country events Not match with console");

					}
					//click on close console
			  		driver.findElement(By.xpath("//div[3]/ul/li/span")).click();   		
			  		Thread.sleep(3000);
				}
							}
									
						}

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}		          
		}
		
    }
	
	
    @TestRail(testCaseId=600)
	@Test(priority = 21)
	public static void Events_By_country(ITestContext context) throws InterruptedException
    {
		
		WebElement Events_By_country = (WebElement) driver.findElement(By.id("widgetff812d0b-3204-cec3-d4699da3a773"));
		List<WebElement> list = Events_By_country.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list) {
			String data=row.getAttribute("innerText");
			if (!data.isEmpty()) 
			{
				System.out.println(data);
			}
						
		}
    }
	
	
    @TestRail(testCaseId=600)
	@Test(priority = 22)
	public static void Events_By_country_consoleCount(ITestContext context) throws InterruptedException 
	{
		
		WebElement Events_By_country = (WebElement) driver.findElement(By.id("widgetff812d0b-3204-cec3-d4699da3a773"));
		List<WebElement> list = Events_By_country.findElements(By.className("summary-child-div"));
		System.out.println("Events Details: " + list.size());
		for (WebElement row : list) {

			try {

				List<WebElement> div_ele = row.findElements(By.tagName("div"));

				for (WebElement we : div_ele) {
					String classname = we.getAttribute("class");

					if (classname.equalsIgnoreCase("ag-heading_body")) {


						List<WebElement> lst_div = we.findElements(By.className("inner-summary"));

					
						List<WebElement> events = lst_div.get(0).findElements(By.tagName("span"));
						
						for(WebElement inner_events:events)
						{
							if(inner_events.getAttribute("class").equalsIgnoreCase("inner-summary-value"))
							{
								Thread.sleep(15000);
								System.out.println("events count: "+inner_events.getText());
								String Eumevents=inner_events.getText();
								
				String data = row.getAttribute("innerText");
				if (!data.isEmpty()) 
				{
					row.click();
					Thread.sleep(10000);

					String Console_events = driver.findElement(By.xpath("//div[4]/div/div/div[5]/div/table/tbody/tr/td[3]/div")).getText();
				//	System.out.println("console_counts: " + Console_events);

					String[] str = Console_events.split("of ");

					System.out.println("Console events:"+str[1]);
					if(Eumevents.equalsIgnoreCase(str[1]))
					{
						System.out.println("Events_By_country events match with console");
					}
					else
					{
						System.out.println("Events_By_country events Not match with console");

					}
					//click on close console
			  		driver.findElement(By.xpath("//div[3]/ul/li/span")).click();   		
			  		Thread.sleep(3000);
				}
							}
									
						}

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}		          
		}
				
	}
	
	
    @TestRail(testCaseId=609)
	@Test(priority = 23)
	public static void Popular_Browsers(ITestContext context) throws InterruptedException {

		// mouse over on the paichart
    	Thread.sleep(5000);
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
							boolean is_tooltippresent = false;
							for (WebElement e2 : li_elelst) {
								// boolean str= e.getAttribute("aria-hidden");

								WebElement ee = e2.findElement(By.className("viewlet-title-name-div"));
								String getViewletName = ee.findElement(By.tagName("input")).getAttribute("value");
								System.out.println("Viewlet name:" + getViewletName);
								if (getViewletName.equalsIgnoreCase("Popular Browsers")) {
									//System.out.println("HTML:" + ee.getAttribute("innerHTML"));
									WebElement div = e2.findElement(By.className("viewlet-body"));
									WebElement view_ele = div.findElement(By.className("content-wrapper"))
											.findElement(By.tagName("div"));
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

															if (events_count.contains("browser: IE")) {
																System.out.println(
																		"Event_Severity pie-chart plotting events working fine. ");
																is_tooltippresent = true;
																context.setAttribute("Status",1);
											 					context.setAttribute("Comment", "working fine");
															}
															else
															{
																System.out.println(
																		"Event_Severity pie-chart plotting events not working fine. ");	
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

					// .findElement(By.tagName("div")).findElement(By.tagName("ul"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	
    @TestRail(testCaseId=610)
	@Test(priority = 24)
	public static void Event_Scorecard_DemoDashbord(ITestContext context) throws InterruptedException 
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
										if (eventcount.equals("477 Events")) {
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
	
    @TestRail(testCaseId=000)
	@Test(priority = 25)
	public static void Specic_Meassage(ITestContext context) throws InterruptedException 
	{		
		// get records of table
		WebElement we = (WebElement) driver.findElement(By.xpath("//li[2]/div[2]/div[3]/div/div/div[3]/div[3]")).findElement(By.tagName("tbody"));
		List<WebElement> lst = we.findElements(By.className("jqgrow"));
		System.out.println("RowCount: " + lst.size());
		Thread.sleep(3000);
		for (WebElement row : lst)
		{
			List<WebElement> lstwele = row.findElements(By.tagName("td"));
			for (WebElement td : lstwele) 
			{
				String data=td.getText();
				if(!data.isEmpty())
				{
					String data1=td.getText();
 					System.out.println(data1);
 					if(data1.contains("ReceiveOrder"))
 					{
 						System.out.println("Specic_Meassage showing result");
 						context.setAttribute("Status",1);
	 					context.setAttribute("Comment", "working fine");
	 					break;
 					}
 					
		
		           else 
		           {
		 
		               System.out.println("Specic_Meassage not showing result");
		               context.setAttribute("Status",5); context.setAttribute("Comment", "Failed");
		               driver.findElement(By.xpath("Not showing")).click();
		  
		           }
				}
				
			}
			break;
		}

	}
	
	@Test(priority=26)
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
