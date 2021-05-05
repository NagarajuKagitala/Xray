package DashBoardOption;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.CommonForAll;
import Common.LogoutForAll;
import MenuBar.CommonElementsofMenu;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class dashbordOption {
	static WebDriver driver;
	static String Screenshotpath;
	int Dashboardscount=0;
	CommonForAll obj=new CommonForAll();

	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();

		Screenshotpath = Settings.getScreenshotPath();
	}
	// Login page
	@Test
	@Parameters({ "sDriverPath", "sDriver"})
	public void Login(String sDriverPath, String sDriver) throws Exception {

		   Settings.read();
		   String sURL = Settings.getsURL();
		   String sUsername=Settings.getsUsername();
		   String sPassword=Settings.getsPassword();
		
		  if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		  {
			  System.setProperty(sDriver, sDriverPath);
			  driver= new ChromeDriver();
		  }
		  else if(sDriver.equalsIgnoreCase("webdriver.gecko.driver"))
		  {
			  System.setProperty(sDriver, sDriverPath);
			
			  FirefoxOptions options = new FirefoxOptions();
			  options.setCapability("marionette", false);
			  driver = new FirefoxDriver(options);
			
		  }
		  else if(sDriver.equalsIgnoreCase("webdriver.ie.driver"))
		  {
		    System.setProperty(sDriver, sDriverPath);
		    driver= new InternetExplorerDriver();
		  }
		  else
		  {
		    System.setProperty(sDriver, sDriverPath);
		    driver= new EdgeDriver();
		  }
		   driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		   driver.manage().window().maximize();
		
		  //Login Credentials
		  driver.get(sURL);
		  driver.findElement(By.id("Uname")).sendKeys(sUsername);
		  driver.findElement(By.id("PWD")).sendKeys(sPassword);
		  driver.findElement(By.id("Submit")).click();
		  Thread.sleep(4000);

			// Check Landing page
			if (driver.getPageSource().contains("Go to Dashboard")) {
				driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
				Thread.sleep(15000);
			} else {
				System.out.println("Landing page is not present");
				Thread.sleep(6000);
			}
	}
	
	@Parameters({"DashboardName", "Query", "ViewletName"})
	@TestRail(testCaseId=803)
	@Test(priority=1)
	public void CreateDashboardandViewlet(String DashboardName, String Query, String ViewletName, ITestContext context) throws InterruptedException
	{
		obj.CreateDashboard(driver, DashboardName);
				
		Thread.sleep(2000);
		obj.CreateViewlet(driver, Query, ViewletName);
		
		try
		{
		List<WebElement> list=driver.findElements(By.className("tabs-title"));
		System.out.println(list.size());
		
		for(WebElement e : list)
		{
			System.out.println(e.getText());
			if(e.getText().equalsIgnoreCase(DashboardName))
			{
				System.out.println("Dashboard is created successfully");
				break;
			}	
		}
		context.setAttribute("Status",1);
		context.setAttribute("Comment", "working fine");
		}
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("failed")).click();
		}
	}
	
	@Parameters({"DashboardName"})
	@TestRail(testCaseId=804)
	@Test(priority=2)
	public void saveDashboard(String DashboardName, ITestContext context) throws InterruptedException
	{
			
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText(DashboardName));
				Actions ac=new Actions(driver);
				ac.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement save=driver.findElement(By.xpath("//li[contains(.,'Save')]"));
						
							save.click();
							Thread.sleep(4000);
							
							// Store the success message into string
							String Msg = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg);

							// Click on Confirmation OK button
							driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();

							if (Msg.contains("successfully")) {
								System.out.println("Save option is working fine");
								context.setAttribute("Status", 1);
								context.setAttribute("Comment", "working fine");

								
							} else {
								System.out.println("Save option is not working");
								context.setAttribute("Status", 5);
								context.setAttribute("Comment", "Failed");
													
							}

					}
				}
			
				
			}
			
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"DashboardName", "SaveAsDashboardname"})
	@TestRail(testCaseId=805)
	@Test(priority=3)
	public void SaveAs(String DashboardName, String SaveAsDashboardname, ITestContext context) throws InterruptedException
	{
		WebElement elem=driver.findElement(By.linkText(DashboardName));
		Actions ac=new Actions(driver);
		ac.contextClick(elem).perform();
		Thread.sleep(3000);
		
		//select save as option
		driver.findElement(By.xpath("//li[contains(.,'Save As')]")).click();
		Thread.sleep(6000);
		
		//Give the save as name
		driver.findElement(By.cssSelector("#save-as-dashboard .input-field")).sendKeys(SaveAsDashboardname);
		
		driver.findElement(By.id("saveAsDashboardBtn")).click();
		Thread.sleep(10000);
		
		//List out the dashboards
		WebElement dash1=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
	    StringBuilder buffer=new StringBuilder();
		List<WebElement> li=dash1.findElements(By.tagName("li"));
		System.out.println("li size is: " +li.size());
		
		for(WebElement a : li)
		{
			WebElement anc=a.findElement(By.tagName("a")).findElement(By.tagName("span"));
			System.out.println("text is: " +anc.getText());
			String Names=anc.getText();
			buffer.append(Names);
			buffer.append(',');
		}
		
		String DashboardList=buffer.toString();
		System.out.println("List of dashboards are : " +DashboardList);
		
		if(DashboardList.contains(SaveAsDashboardname))
		{
			System.out.println("Save As option is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Save As option is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
		}
		
	}
	
	@Parameters({"SaveAsDashboardname", "SchemaName"})
	@TestRail(testCaseId=806)
	@Test(priority=4)
	public void Configure(String SaveAsDashboardname, String SchemaName, ITestContext context) throws InterruptedException
	{
		//Mouse hour 
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.cssSelector(".fa-bars")).click();
		Thread.sleep(4000);

		//Click on Dash board and Select Save button
		driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[5]/ul/li[3]")).click();
		Thread.sleep(4000);
		
		//Click on create schema button
		driver.findElement(By.xpath("//dialog[@id='schemas-wizard-popup']/section/footer/button[2]")).click();
		Thread.sleep(4000);
		
		//Give the schema name
		driver.findElement(By.id("schema-label")).sendKeys(SchemaName);
		
		//Select the type
		Select type=new Select(driver.findElement(By.id("item-type")));
		type.selectByVisibleText("Activity");
		
		//Click on Add fields button
		driver.findElement(By.xpath("//button[contains(.,'Add fields')]")).click();
		Thread.sleep(6000);
		
		//Select Activity name
		driver.findElement(By.xpath("//option[@value='ActivityName']")).click();
		
		driver.findElement(By.xpath("//div[2]/div[2]/div/button[2]")).click();
		
		//click on Apply changes
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		
		//Click on save button
		driver.findElement(By.xpath("//footer/button[3]")).click();
		Thread.sleep(4000);
		
		//Click on cancel button   
		driver.findElement(By.cssSelector("#schemas-wizard-popup > .close-button")).click();
		
		//Open the required viewlet
		driver.findElement(By.linkText(""+SaveAsDashboardname+"")).click();
		Thread.sleep(10000);
		
		//Select the dashboard
		WebElement elem=driver.findElement(By.linkText(""+SaveAsDashboardname+""));
		Actions ac=new Actions(driver);
		ac.contextClick(elem).perform();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//li[contains(.,'Configure')]")).click();
		Thread.sleep(2000);
		
		//select the schema
		Select Schema=new Select(driver.findElement(By.id("dboardViewSchema")));
		Schema.selectByVisibleText(SchemaName);
		
		//Click on Apply
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(12000);
		
        WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		
			
		//Get the column name                        
		String Fieldname=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[3]")).getText();
		System.out.println("Field name is:"+Fieldname);
		
		if(Fieldname.contains("ActivityName"))
		{
			System.out.println("Configure schema is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Configure schema is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
		}
	}
	
	@Parameters({"DashboardName", "sDriverPath", "sDriver"})
	@TestRail(testCaseId=807)
	@Test(priority=5)
	public void SetDefultDashborad(String DashboardName, String sDriverPath, String sDriver, ITestContext context) throws Exception
	{
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText(DashboardName));
				Actions ac1=new Actions(driver);
				ac1.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement setDefault=driver.findElement(By.xpath("//li[contains(.,'Set As Default')]"));
						
						String isEnabled = setDefault.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Set As Default enabled");
							setDefault.click();
							Thread.sleep(1000);
							
							//Logout
							Logout();
							Thread.sleep(4000);
							
							//Login again
							Login(sDriverPath, sDriver);
							
							List<WebElement> myviewletElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
							// System.out.println(myElements.size());
							StringBuilder buffer=new StringBuilder();
							for (WebElement ele : myviewletElements) 
							{
								// boolean str= e.getAttribute("aria-hidden");
								if (ele.getAttribute("aria-hidden").toLowerCase().matches("false")) 
								{
									List<WebElement> viewletsize = ele.findElements(By.className("viewlet-name-wrapper"));
									System.out.println("Viewlet size:" + viewletsize.size());
									String GetViewletName="";
									for(WebElement innerele: viewletsize)
									{
										GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
										//System.out.println("Viewlet name:" + GetViewletName);
										buffer.append(GetViewletName);
										buffer.append(',');
									}

								}
							}
							
							String ListOfViewletnames=buffer.toString();
							System.out.println("List of viewlets are: " +ListOfViewletnames);
							
							if(ListOfViewletnames.contains("TestViewlet"))
							{
								System.out.println("Set as Default dashboard option is working fine");
								context.setAttribute("Status", 1);
								context.setAttribute("Comment", "working fine");
								
							}
							else
							{
								System.out.println("Set as Default dashboard option is not working");
								context.setAttribute("Status", 5);
								context.setAttribute("Comment", "Failed");
								
							}
							Thread.sleep(2000);
						}
						else
						{
							System.out.println("Set As Default disabled");
						}
					}
						else
						{
							System.out.println("Set As Default not working");
							
						}
				}
			}
		}
		}
	
	@Parameters({"DashboardName"})
	@TestRail(testCaseId=808)	
	@Test(priority=6)	
	public void closeTabOftheRight(String DashboardName, ITestContext context) throws InterruptedException
	{
	  //this.CreateDashboard2();
	  
	  WebElement dash1=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
	    StringBuilder buffer=new StringBuilder();
		List<WebElement> li=dash1.findElements(By.tagName("li"));
		System.out.println("li size is: " +li.size());
		
		for(WebElement a : li)
		{
			WebElement anc=a.findElement(By.tagName("a")).findElement(By.tagName("span"));
			System.out.println("text is: " +anc.getText());
			String BeforeNames=anc.getText();
			buffer.append(BeforeNames);
			buffer.append(',');
		}
		
		String ListBeforeCloseRighttab=buffer.toString();
		System.out.println("List of dashboards before close right side dashboard: " +ListBeforeCloseRighttab);
		
		//click on dashboard
		WebElement elem=driver.findElement(By.linkText(""+DashboardName+""));
		Actions ac=new Actions(driver);
		ac.contextClick(elem).perform();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//li[contains(.,'Close tabs to the right')]")).click();
		Thread.sleep(4000);
		
		// Click on Confirmation save button button
		try
		{
			driver.findElement(By.xpath("//dialog[47]/section/footer/button[2]")).click();
		}
		catch (Exception e2)
		{
			System.out.println("No need to save dashboard");
		}
		Thread.sleep(8000);
		
		WebElement dash2=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		StringBuilder buffer1=new StringBuilder();
		List<WebElement> li2=dash2.findElements(By.tagName("li"));
		System.out.println("li size is: " +li2.size());
		
		for(WebElement a2 : li2)
		{
			WebElement anc2=a2.findElement(By.tagName("a")).findElement(By.tagName("span"));
			System.out.println("text is: " +anc2.getText());
			String AfterDelete=anc2.getText();
			buffer1.append(AfterDelete);
			buffer1.append(',');	
		}
		
		String ListAfterCloseRighttab=buffer1.toString();
		System.out.println("List of dashboards After close right side dashboard: " +ListAfterCloseRighttab);
		
		if(ListBeforeCloseRighttab.equalsIgnoreCase(ListAfterCloseRighttab))
		{
			System.out.println("Close right tab is not working ");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Close right tabs failed")).click();
		}
		else
		{
			System.out.println("Close right tab is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		}
	  
	 /* List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
	  System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText(DashboardName));
				Actions ac=new Actions(driver);
				ac.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement rightTab=driver.findElement(By.xpath("//li[contains(.,'Close tabs to the right')]"));
												
						String isEnabled = rightTab.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Close tabs to the right is enabled");
							rightTab.click();
							Thread.sleep(4000);
							
							// Store the success message into string
							String Msg = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg);
							
							// Click on Confirmation save button button
							try
							{
								driver.findElement(By.xpath("//footer/button[2]")).click();
							}
							catch (Exception e2)
							{
								System.out.println("No need to save dashboard");
							}
							
							WebElement dash2=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
							StringBuilder buffer1=new StringBuilder();
							List<WebElement> li2=dash2.findElements(By.tagName("li"));
							System.out.println("li size is: " +li2.size());
							
							for(WebElement a2 : li2)
							{
								WebElement anc2=a2.findElement(By.tagName("a")).findElement(By.tagName("span"));
								System.out.println("text is: " +anc2.getText());
								String AfterDelete=anc2.getText();
								buffer1.append(AfterDelete);
								buffer1.append(',');	
							}
							
							String ListAfterCloseRighttab=buffer1.toString();
							System.out.println("List of dashboards After close right side dashboard: " +ListAfterCloseRighttab);
							
							if(ListBeforeCloseRighttab.equalsIgnoreCase(ListAfterCloseRighttab))
							{
								System.out.println("Close right tab is not working ");
							}
							else
							{
								System.out.println("Close right tab is working fine");
								break;
							}
						}
						else
						{
							System.out.println("Close tabs to the right is enabled");
						}
					}
						else
						{
							System.out.println("Close tabs to the right is not working");
						}
					}
				}
			}	*/
	}
	
	
	

	/*@Test(priority=7)
	public void closeTabOftheLeft() throws InterruptedException
	{
		this.CreateDashboardLeft();
		
		//Right click on Dashboard
		WebElement Save=driver.findElement(By.linkText("DashLeft"));
		Actions action=new Actions(driver);
		action.contextClick(Save).perform();
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//li[contains(.,'Save')]")).click();
		Thread.sleep(8000);
		
		//Click on Confirmation button
		driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		Thread.sleep(4000);
		
		WebElement drag=driver.findElement(By.linkText(DashboardName));
		WebElement drop=driver.findElement(By.linkText("DashLeft"));
		Actions ac1=new Actions(driver);
		ac1.dragAndDrop(drag, drop).build().perform();
		Thread.sleep(3000);
		
		//Click on Test Dashboard
		driver.findElement(By.linkText(DashboardName)).click();
		
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText(DashboardName));
				Actions ac=new Actions(driver);
				ac.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement leftTab=driver.findElement(By.xpath("//li[contains(.,'Close tabs to the left')]"));
					
						String isEnabled = leftTab.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Close tabs to the left is enabled");
							leftTab.click();
							Thread.sleep(5000);
							
						// Store the success message into string
							String Msg = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg);
						
							// Click on Confirmation save button button
							driver.findElement(By.xpath("//footer/button[2]")).click();
							Thread.sleep(2000);
						}
						else
						{
							System.out.println("Close tabs to the left is disabled");
						}
					}
						else
						{
							System.out.println("Close tabs to the left is not working");
						}
					}
				}
			}
		List<WebElement> Afterdash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size After delete dashboard:"+Afterdash.size());
	}
	
	@Test(priority=6)
	public void closeAllTab() throws InterruptedException
	{
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText(DashboardName));
				Actions ac=new Actions(driver);
				ac.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement OtherTab=driver.findElement(By.xpath("//li[contains(.,'Close other tabs')]"));
						
						String isEnabled = OtherTab.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Close other tab is enabled");
							OtherTab.click();
							Thread.sleep(5000);
							
							// Store the success message into string
							String Msg = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg);														
							// Click on Confirmation save button button
							driver.findElement(By.xpath("//footer/button[2]")).click();
							Thread.sleep(2000);
							String Msg2 = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg2);
							driver.findElement(By.xpath("//footer/button[2]")).click();
						}
						else
						{
							System.out.println("Close other tab is disabled");
						}
					}
						else
						{
							System.out.println("Close other tab is not working");
						}
					}
				}
			}
		List<WebElement> allTab=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size All tab:"+allTab.size());
	}*/
	
	public void Logout() throws InterruptedException
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	/*public void Login() throws Exception
	{
		 Settings.read();
		 String sURL = Settings.getsURL();
		 String sUsername=Settings.getsUsername();
		 String sPassword=Settings.getsPassword();
	    System.setProperty("webdriver.chrome.driver", "Drivers/chromedriver.exe");
	    driver = new ChromeDriver();
	    
	    driver.get(sURL);
		driver.manage().window().maximize();

	    
	    driver.findElement(By.id("Uname")).sendKeys(sUsername);
	    driver.findElement(By.id("PWD")).sendKeys(sPassword);
	    driver.findElement(By.id("Submit")).click();
	    Thread.sleep(4000);
	    
	    //Check Landing page 
		if(driver.getPageSource().contains("Go to Dashboard"))
		{
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(15000);
		}
		else
		{
			System.out.println("Landing page is not present");
			Thread.sleep(6000);
		}
	}*/
	
	public void createViewlet() throws InterruptedException
	{
		//Click on Viewlet button
		driver.findElement(By.xpath("//div[@id='app-top-sidebar']/div[3]/span")).click();
		
		//Check the Create viewlet with JKQL check box
		boolean Jkql=driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div/label/input")).isSelected();
		
		if(Jkql == true)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div/label/input")).click();
			Thread.sleep(1000);
		}
		
		//Click on Create button
		driver.findElement(By.id("createViewletBtn")).click();
		
		//Enter the query to field
		driver.findElement(By.xpath("//dialog[@id='createJkqlViewlet']/section/main/div/div/jkql-input/input")).sendKeys("get Activity");
		
		//Enter the viewlet name
		driver.findElement(By.xpath("//dialog[@id='createJkqlViewlet']/section/main/div[2]/input")).clear();
		driver.findElement(By.xpath("//dialog[@id='createJkqlViewlet']/section/main/div[2]/input")).sendKeys("TestViewlet");
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createJkqlViewlet']/section/footer/div[2]/input")).click();
		Thread.sleep(30000);				
		
	}
	public void CreateDashboard(String DashboardName) throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//div[@id='pageContainer-tabs-add']/div/div/span")).click();
				
		//Give the dashboard Name
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/input")).sendKeys(DashboardName);
				
		// uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);

		// select two columns 
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();

		// Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
	}
	public void CreateDashboard2() throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
				
		//Give the dashboard Name
		driver.findElement(By.cssSelector(".edit-name")).sendKeys("Abc");
				
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//label[2]/input")).click();
		
		//select two columns
		driver.findElement(By.xpath("//main/div/ul/li[2]/div")).click();
				
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
	}
	
	public void CreateDashboardLeft() throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
				
		//Give the dashboard Name
		driver.findElement(By.cssSelector(".edit-name")).sendKeys("DashLeft");
				
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//label[2]/input")).click();
		
		//select two columns
		driver.findElement(By.xpath("//main/div/ul/li[2]/div")).click();
				
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
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
