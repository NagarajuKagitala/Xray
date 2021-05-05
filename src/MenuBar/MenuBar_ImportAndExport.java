package MenuBar;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
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

import Common.CommonForAll;
import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class MenuBar_ImportAndExport 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String DownloadPath;
	static String ImportData;
	static String ImportViewlet;
	static String ImportDashboard;
	static String ImportSet;
	
	CommonForAll obj=new CommonForAll();
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		DownloadPath =Settings.getDownloadPath();
		ImportData =Settings.getImportData();
		ImportViewlet =Settings.getImportViewlet();
		ImportDashboard =Settings.getImportDashboard();
		ImportSet =Settings.getImportSet();
	}
	
	//Login page
	@Test
	@Parameters({"sDriverPath", "sDriver", "DashboardName"})
	public void Login(String sDriverPath, String sDriver, String DashboardName) throws Exception 
	{
		Settings.read();
		String sURL = Settings.getsURL();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
		String filepath=System.getProperty("user.dir") + "\\" + DownloadPath;
		
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverPath);
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", filepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			driver=new ChromeDriver(options);
		}
		else if(sDriver.equalsIgnoreCase("webdriver.gecko.driver"))
		{
			System.setProperty(sDriver, sDriverPath);
			
			FirefoxOptions options = new FirefoxOptions();
	      	options.setCapability("marionette", true);
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
		
		//Create dashboard
		obj.CreateDashboard(driver, DashboardName);
	}
	
	@TestRail(testCaseId=541)
	@Parameters({"DashboardName"})
	@Test(priority=1)
	public void OpenviewletWithExistingDashboard(String DashboardName, ITestContext context) throws InterruptedException, AWTException
	{
		//Click on Menu and select Import/export 
		CommonElementsofMenu.MenubarIcon(driver);
		CommonElementsofMenu.ImportExportOption(driver);
		Thread.sleep(5000);
		
		CommonElementsofMenu.DataOption(driver);
		Thread.sleep(5000);
		
		
		try
		{
			//Click on Check box
			CommonElementsofMenu.CheckBox(driver);
		}
		
		catch (Exception e)
		{
			System.out.println("Source file is not present");
			
			//Click on Cancel button   
			driver.findElement(By.cssSelector("#importsList .alert-btn")).click();
			Thread.sleep(2000);
			
			//Click on Import button
			driver.findElement(By.xpath("//div[@id='importDataBtnNew']/span")).click();
			Thread.sleep(2000);
			
			//Select the file xls
			//driver.findElement(By.xpath("//li/img")).click();
			
			//Click on choose file option
			driver.findElement(By.xpath("//dialog[@id='importWizard']/section/main/aside/div/span/span/span/input")).click();
			Thread.sleep(2000);
			
			//Loading the file into queue by using robot class
			String filepath=System.getProperty("user.dir") + "\\" + ImportData;
			StringSelection stringSelection = new StringSelection(filepath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		    Robot robot = new Robot();
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(2000);
		    robot.keyPress(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);
		    Thread.sleep(3000);
		    
		    //Click on Next button
		    driver.findElement(By.xpath("//dialog[@id='importWizard']/section/footer/div[2]/button[2]")).click();
		    Thread.sleep(5000);
		    
		    //Click on Start import button
		    driver.findElement(By.xpath("//dialog[@id='importWizard']/section/footer/div[2]/button[3]")).click();
		    Thread.sleep(25000);
		    
		    //Click on finish button
		    driver.findElement(By.xpath("//dialog[@id='importWizard']/section/footer/div[2]/button[4]")).click();
		    Thread.sleep(15000);
		    
		    try
		    {
		    	//Click on confirmation button
		    	driver.findElement(By.xpath("//footer/button")).click();
		    }
		    catch (Exception e1)
		    {
		    	
		    }
		    
//		    //Click on Menu and select Import/export 
//			driver.findElement(By.cssSelector(".icon")).click();
//			driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
//			Thread.sleep(3000);
//			
//			driver.findElement(By.cssSelector(".hasSubMenuOpen li:nth-child(2)")).click();
//			Thread.sleep(5000);
//		    
//			//click on export
//			driver.findElement(By.xpath("//div/div/button[2]"));
//			Thread.sleep(2000);
//			//Click on Check box
//			driver.findElement(By.xpath("//aside/div/div/div/table/tbody/tr/td/input")).click();
		    
		  //Click on Menu and select Import/export 
		    CommonElementsofMenu.MenubarIcon(driver);
			//CommonElementsofMenu.ImportExportOption(driver);
			Thread.sleep(5000);
			
			CommonElementsofMenu.DataOption(driver);
			Thread.sleep(5000);
			
			//Click on Check box
			CommonElementsofMenu.CheckBox(driver);
			Thread.sleep(2000);
		    
		}
		
		//Click on open button
		driver.findElement(By.xpath("//dialog[@id='importsList']/section/footer/div[2]/button[2]")).click();
		Thread.sleep(3000);
		
		//Select viewlet to dispaly image
		//driver.findElement(By.xpath("//dialog[@id='importWizard']/section/main/aside[7]/ul/li/label/div/img")).click();
		//Click on Next
		driver.findElement(By.xpath("//dialog[@id='importWizard']/section/footer/div[2]/button[2]")).click();
		Thread.sleep(8000);         
		
		//Select the dashboard value from left side pannel
		Select Dashboard=new Select(driver.findElement(By.xpath("//dialog[@id='importWizard']/section/main/aside[8]/div/select")));
		Dashboard.selectByVisibleText(DashboardName);            
		Thread.sleep(6000);
		
		//Click on finish button
		driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
		Thread.sleep(35000);
		
		try
		{
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
			// System.out.println(myElements.size());
			
			StringBuilder buffer=new StringBuilder();
			for (WebElement e : myElements) {

			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
			List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
			System.out.println("Viewlet size:" + ee.size());
			String GetViewletName="";
			for(WebElement innerele: ee)
			{
				GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
				System.out.println("Viewlet name:" + GetViewletName);
				buffer.append(GetViewletName);
				buffer.append(",");
			}
			
			String Viewletnames=buffer.toString();
			System.out.println("List of viewlets are: " +Viewletnames);

			// verification
			if (Viewletnames.contains(GetViewletName)) {
			System.out.println("Viewlet is created successfully");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
			} else {
			System.out.println("Viewlet is not created");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Viewlet Creation failed")).click();
			}
			Thread.sleep(2000);
			}
			}
		}
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Open viewlet with existing dashboard failed");
			driver.findElement(By.id("Viewlet open failed")).click();
		}
		Thread.sleep(2000);
		
	}
	
	
	
	@Parameters({"ImportedDashboardName"})
	@TestRail(testCaseId=542)
	@Test(priority=2)
	public void OpenviewletWithNewDashboard(String ImportedDashboardName, ITestContext context) throws InterruptedException, AWTException
	{
		//Click on Menu and select Import/export 
		CommonElementsofMenu.MenubarIcon(driver);
		//CommonElementsofMenu.ImportExportOption(driver);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		CommonElementsofMenu.DataOption(driver);
		Thread.sleep(5000);
				
				
		try
		{
		//Click on Check box
		CommonElementsofMenu.CheckBox(driver);
	    }
		
		catch (Exception e)
		{
			System.out.println("Source file is not present");
			
			//Click on Cancel button
			CommonElementsofMenu.MenubarCancel(driver);
			Thread.sleep(2000);
			
			//Click on Import button
			driver.findElement(By.xpath("//div[@id='importDataBtnNew']/span")).click();
			Thread.sleep(2000);
			
			//Select the file xls
			//driver.findElement(By.xpath("//li/img")).click();
			
			//Click on choose file option
			driver.findElement(By.xpath("//input[@type='file']")).click();
			Thread.sleep(2000);
			
			//Loading the file into queue by using robot class
			String filepath=System.getProperty("user.dir") + "\\" + ImportData;
			StringSelection stringSelection = new StringSelection(filepath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		    Robot robot = new Robot();
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(2000);
		    robot.keyPress(KeyEvent.VK_ENTER);
		    robot.keyRelease(KeyEvent.VK_ENTER);
		    Thread.sleep(2000);
		    
		    //Click on Next button
		    driver.findElement(By.cssSelector("#importWizard .primary-btn:nth-child(2)")).click();
		    
		    //Click on Start import button
		    driver.findElement(By.cssSelector("#importWizard .primary-btn:nth-child(3)")).click();
		    Thread.sleep(12000);
		    
		    //Click on finish button
		    driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
		    Thread.sleep(20000);
		    
		    try
		    {
		    	//Click on confirmation button
		    	driver.findElement(By.id("updatesDone")).click();
		    }
		    catch (Exception e1)
		    {
		    	
		    }
		    
//		    //Click on Menu and select Import/export 
//			driver.findElement(By.cssSelector(".icon")).click();
//			driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
//			Thread.sleep(3000);
//			
//			driver.findElement(By.xpath("//li[3]/ul/li")).click();
//			Thread.sleep(5000);
//		    
//			//Click on Check box
//			driver.findElement(By.xpath("//aside/div/div/div/table/tbody/tr/td/input")).click();
			
			 //Click on Menu and select Import/export 
		    CommonElementsofMenu.MenubarIcon(driver);
			//CommonElementsofMenu.ImportExportOption(driver);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
			CommonElementsofMenu.DataOption(driver);
			Thread.sleep(5000);
			
			//Click on Check box
			CommonElementsofMenu.CheckBox(driver);
			Thread.sleep(2000);
		    
		}
		//Click on open button
		driver.findElement(By.cssSelector("#importsList .primary-btn:nth-child(2)")).click();
		Thread.sleep(3000);
		//Select viewlet to dispaly image
		//driver.findElement(By.xpath("//dialog[@id='importWizard']/section/main/aside[7]/ul/li/label/div/img")).click();
		//Click on Next
		driver.findElement(By.cssSelector("#importWizard .primary-btn:nth-child(2)")).click();
		//Click on second tab
		driver.findElement(By.xpath("//dialog[@id='importWizard']/section/main/aside[8]/div[2]")).click();
		//Give the dashboard name
		driver.findElement(By.xpath("//dialog[@id='importWizard']/section/main/aside[8]/div[2]/input")).sendKeys(ImportedDashboardName);
		//select page layout
		driver.findElement(By.xpath("//dialog[@id='importWizard']/section/main/aside[8]/div[2]/div[2]/label/span")).click();
		//Click on finish button
		driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
		Thread.sleep(30000);
		
		try
		{
		//	driver.findElement(By.id("updatesDone")).click();
			
			//List the dashboards
			WebElement Dashboards=driver.findElement(By.className("tabs-header"));
			List<WebElement> Dashnames=Dashboards.findElements(By.tagName("li"));
			System.out.println("Dashboards count is: " +Dashnames.size());
			
			List<WebElement> title=Dashboards.findElements(By.className("tabs-title"));
			for(WebElement Names : title)
			{
				if(Names.getText().equalsIgnoreCase(ImportedDashboardName))
				{
					System.out.println("Dash board is opened with New Dashboard");
					context.setAttribute("Status",1);
					context.setAttribute("Comment", "Open dash board is working fine");
					break;
				}
			}
		}
		catch (Exception e)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Open viewlet with new dashboard failed");
			
			driver.findElement(By.id("Open viewlet Failed")).click();	
		}
		Thread.sleep(10000);		
	}
	
	
	@TestRail(testCaseId=543)
	@Test(priority=3)
	public void DeleteImportedDataFile(ITestContext context) throws InterruptedException
	{
		//Click on Menu and select Import/export 
		 CommonElementsofMenu.MenubarIcon(driver);
	     //CommonElementsofMenu.ImportExportOption(driver);
		 driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
		 CommonElementsofMenu.DataOption(driver);
		 Thread.sleep(5000);
		
		//Get the deleting data type name
		String Datatypename=driver.findElement(By.xpath("//tbody[@id='setDataTable']/tr/td[2]")).getText();
		//System.out.println("Innerhtml: "+ driver.findElement(By.xpath("//td[2]")).getAttribute("innerHTML"));
		System.out.println("Deleting data type name is: " +Datatypename);
		
		//Click on Check box
		CommonElementsofMenu.CheckBox(driver);
		
		//Delete button
		driver.findElement(By.xpath("//button[contains(.,'Delete')]")).click();
		
		//Click on confirmation yes button
		driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
		Thread.sleep(15000);
		
		//Store the viewlet data
		String ManageImportData=driver.findElement(By.id("importsTable")).getText();
		System.out.println("manage page data is: " +ManageImportData);
		
		//Verification
		if(ManageImportData.contains(Datatypename))
		{
			System.out.println("Source file is not deleted");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Delete data set file is failed");
			//Click on Cancel button
			CommonElementsofMenu.MenubarCancel(driver);
			driver.findElement(By.id("Delete button failed")).click();
		}
		else
		{
			System.out.println("Source file is deleted");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "Delete data file is working fine");
		}
		
		//Click on Cancel button
		Thread.sleep(3000);
		//CommonElementsofMenu.MenubarCancel(driver);
		driver.findElement(By.cssSelector("#importsList .alert-btn")).click();
		Thread.sleep(2000);               
	}
	
	
	@TestRail(testCaseId=544)
	@Test(priority=4)
	public void ImportViewlet(ITestContext context) throws InterruptedException, AWTException
	{
		try
		{
		//Select the viewlet option 
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]/ul/li[2]")).click();
		CommonElementsofMenu.ViewletsOption(driver);
		Thread.sleep(6000);
		
		
		//click on override check box
		Boolean over=driver.findElement(By.id("override-checkbox")).isSelected();
		
		if(over ==true)
		{
			System.out.println("Over ride checkbox is already selected");
		}
		else
		{
			driver.findElement(By.id("override-checkbox")).click();
		}
		Thread.sleep(3000);
		
		//Click on Choose file
		CommonElementsofMenu.ChooseFileButton(driver);
		//CommonElementsofMenu.ChooseFileButton(driver);
		Thread.sleep(3000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + ImportViewlet;
		StringSelection stringSelection = new StringSelection(filepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(4000);
		
		//Click on import button
	    CommonElementsofMenu.ImportButton(driver);
	    Thread.sleep(12000);
	    	    
	    try
		{
	    	//Get the viewlet data
//	    	String Messagedata=driver.findElement(By.cssSelector(".msgDiv")).getText();
//	    	System.out.println("Message data is: " +Messagedata);
//	    	driver.findElement(By.id("updatesDone")).click();
	    	String data=driver.findElement(By.id("setTable")).getText();
	    	System.out.println("Message data is: " +data);
	    	
	    	//verification
	    	
	    	if(data.contains("Viewlet 4"))
	    	{
	    		System.out.println("Import is working fine for viewlet");
	    		context.setAttribute("Status",1);
	    		context.setAttribute("Comment", "Viewlet is imported");
	    	}
	    	else
	    	{
	    		System.out.println("Import is not working fine for viewlet");
	    		driver.findElement(By.id("Import viewlet failed")).click();
	    	}
		}
		catch (Exception e)
		{
			System.out.println("Verification failed or status popup is not present");
			driver.findElement(By.id("Import viewlet failed")).click();
		}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Viewlet importing failed");
		    //driver.findElement(By.cssSelector("#import-export-popup .default-btn")).click();
			CommonElementsofMenu.ImportExportCancelOption(driver);
			driver.findElement(By.id("Import viewlet failed")).click();
			
		}
	    
	    //close the popup
	    CommonElementsofMenu.ImportExportCancelOption(driver);
		Thread.sleep(2000);	
		

	}
	@TestRail(testCaseId=545)
	@Parameters({"sExportedViewletName"})
	@Test(priority=5)
	public void ExportViewlet(String sExportedViewletName, ITestContext context) throws InterruptedException
	{
		try
		{
		//Select the viewlet option
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		CommonElementsofMenu.ViewletsOption(driver);
		Thread.sleep(4000);
		
		//Export Viewlet
		 driver.findElement(By.xpath("//button[contains(.,'Export')]")).click();
		 Thread.sleep(5000);
		 
			/*
			 * //get viewlet name String ele=driver.findElement(By.xpath(
			 * "/html/body/dialog[16]/section/main/aside/div[2]/div")).getText(); //
			 * System.out.println("viewlet names:"+ele);
			 */		 
				 
		 //serch viewlet name
		 driver.findElement(By.cssSelector("div > .info-data")).sendKeys(sExportedViewletName);
		 Thread.sleep(2000);
		 
		 //click on check box
		 driver.findElement(By.cssSelector(".checkbox-wp > input")).click();
		 Thread.sleep(2000);

		 //click on export button
		 CommonElementsofMenu.ExportButton(driver);
		 Thread.sleep(10000);
		 
		 context.setAttribute("Status",1);
         context.setAttribute("Comment", "Viewlet is exported");
		}
		 
//		 List<WebElement> ViewletNames=driver.findElements(By.xpath("/html/body/dialog[16]/section/main/aside/div[2]/div"));
//		 System.out.println(ViewletNames.size());
//		 
//		 String DashBoard[]=new String[ViewletNames.size()];
//		 int i=0;
//		for(WebElement DashBoardlist:ViewletNames)
//		{
//			DashBoard[i]=DashBoardlist.getText();
//			System.out.println(DashBoard[i]);
//			if(DashBoard[i].contains(sExportedViewletName))
//			 {
//				System.out.println("Enter into loop");
//				 Thread.sleep(2000);
//				 driver.findElement(By.xpath("//div[@id='setTable']" + "'" + sExportedViewletName + "'" + "]")).click();
//				 Thread.sleep(4000);
//					 
//				 CommonElementsofMenu.ExportButton(driver);
//				 Thread.sleep(10000);
//				 break;
//			}
//			else	 
//			{
//				System.out.println("This Viewlet is not Required to Export");
//				Thread.sleep(4000);
//			}
//		}
//		context.setAttribute("Status",1);
//		context.setAttribute("Comment", "Viewlet is exported");
		
		catch (Exception e)
		{
			System.out.println("Viewlet Export failed");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Viewlet exporting failed");
			//close the export popup
			CommonElementsofMenu.ImportExportCancelOption(driver);
			Thread.sleep(2000);	
			driver.findElement(By.id("Viewlet export failed")).click();
		}
		//click on cancel popoup
		CommonElementsofMenu.ImportExportCancelOption(driver);
		Thread.sleep(4000);	
		
	}
	
	@TestRail(testCaseId=546)
	@Test(priority=6)
	public void ImportDashboard(ITestContext context) throws InterruptedException, AWTException
	{
		try
		{
		//Select the viewlet option 
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]/ul/li[3]")).click();
		CommonElementsofMenu.DashboardsOption(driver);
		Thread.sleep(5000);
		
		//click on override check box
		Boolean over=driver.findElement(By.id("override-checkbox")).isSelected();
		
		if(over ==true)
		{
			System.out.println("Over ride checkbox is already selected");
		}
		else
		{
			driver.findElement(By.id("override-checkbox")).click();
		}
		Thread.sleep(3000);
		
		//Click on Choose file
		CommonElementsofMenu.ChooseFileButton(driver);
		Thread.sleep(3000);         
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + ImportDashboard;
		StringSelection stringSelection = new StringSelection(filepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(8000);
		
		//Click on import button
	    CommonElementsofMenu.ImportButton(driver);
	    Thread.sleep(20000);                            
	    
	    //Get the dashboard name
	    String Dashname=driver.findElement(By.id("setTable")).getText();
	    System.out.println("Dashbaord name is: " +Dashname);
	    		
		/*
		 * //click on check box driver.findElement(By.xpath(
		 * "//div[@id='setTable']/div/div/table/tbody/tr/td/input")).click();
		 * 
		 * //click on Open button driver.findElement(By.
		 * cssSelector("#import-export-popup .primary-btn:nth-child(3)")).click();
		 * Thread.sleep(8000);
		 */
		
		try
		{
			if(Dashname.contains("Test"))
			{
				System.out.println("Dash board is imported");
				context.setAttribute("Status",1); 
				context.setAttribute("Comment","Dashboard is imported");
			}
			//driver.findElement(By.id("updatesDone")).click();
			
			/*
			 * //List the dashboards WebElement
			 * Dashboards=driver.findElement(By.className("tabs-header")); List<WebElement>
			 * Dashnames=Dashboards.findElements(By.tagName("li"));
			 * System.out.println("Dashboards count is: " +Dashnames.size());
			 * 
			 * for(WebElement Names : Dashnames) { System.out.println("Dash names are: "
			 * +Names.getText()); if(Names.getText().contains(Dashname)) {
			 * System.out.println("Dash board is imported");
			 * context.setAttribute("Status",1); context.setAttribute("Comment",
			 * "Dashboard is imported"); break; } }
			 */
		}
		catch (Exception e)
		{
			System.out.println("Verification failed or status popup is not present");
			driver.findElement(By.id("Dashboard import failed")).click();
			
		}
		}
		
		catch (Exception e1)
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Dashboard is not imported");
			//click on cancle popoup
			CommonElementsofMenu.ImportExportCancelOption(driver);
			Thread.sleep(2000);
			driver.findElement(By.id("Dashboard import failed")).click();
		}
		Thread.sleep(2000);	
		
		//click on cancle popoup
		CommonElementsofMenu.ImportExportCancelOption(driver);
		Thread.sleep(2000);
		
	}
	
	@TestRail(testCaseId=547)
	@Parameters({"sExportedDashboardName"})
	@Test(priority=7)
	public void ExportDashboard(String sExportedDashboardName, ITestContext context) throws InterruptedException
	{
		Thread.sleep(10000);
		try
		{
		//Select the viewlet option
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		CommonElementsofMenu.DashboardsOption(driver);
		Thread.sleep(5000);
		
		//Export tab
		 driver.findElement(By.xpath("//button[contains(.,'Export')]")).click();
		 Thread.sleep(4000);
		 
		 //serch dashboard name
		 driver.findElement(By.cssSelector("div > .info-data")).sendKeys(sExportedDashboardName);
		 Thread.sleep(2000);
		 
		 //click on check box
		 driver.findElement(By.cssSelector(".checkbox-wp > input")).click();
		 Thread.sleep(2000);

		 //click on export button           
		 CommonElementsofMenu.ExportButton(driver);
		 Thread.sleep(10000);
		 
		 context.setAttribute("Status",1);
         context.setAttribute("Comment", "dashbord is exported");
		}
		 
//		 List<WebElement> DashboardNames=driver.findElements(By.id("setTable"));
//		// System.out.println(DashboardNames.size());
//		 
//		 String DashBoard[]=new String[DashboardNames.size()];
//		 int i=0;
//		for(WebElement DashBoardlist:DashboardNames)
//		{
//			DashBoard[i]=DashBoardlist.getText();
//			System.out.println(DashBoard[i]);
//			if(DashBoard[i].equals(sExportedDashboardName))
//			 {
//				 driver.findElement(By.xpath("//label[text()=" + "'" + sExportedDashboardName + "'" + "]")).click();
//				 Thread.sleep(4000);
//					 
//				 driver.findElement(By.id("exportItemButton")).click();
//				 Thread.sleep(10000);
//				 break;
//			}
//			else	 
//			{
//				System.out.println("This DashBoard is not Required to Export");
//				Thread.sleep(4000);
//			}
//		}
		
		
		
		catch(Exception e)
		{
			System.out.println("Exception occured while exporting Dashboard");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Dashboard exporting failed");
			//close the export popup
			CommonElementsofMenu.ImportExportCancelOption(driver);
			Thread.sleep(2000);
			driver.findElement(By.id("Dashboard export failed")).click();
		}
		//close popup
		CommonElementsofMenu.ImportExportCancelOption(driver);
		Thread.sleep(3000);
		
	}
	
	@TestRail(testCaseId=548)
	@Test(priority=8)
	public void ImportSet(ITestContext context) throws InterruptedException, AWTException
	{
		//Select the viewlet option 
		CommonElementsofMenu.MenubarIcon(driver);
		//driver.findElement(By.xpath("//li[contains(.,' Import / Export')]")).click();
	//	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Thread.sleep(3000);
		CommonElementsofMenu.SetsOption(driver);
		//driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]/ul/li[4]")).click();
		Thread.sleep(3000);
		
		//click on override check box
		Boolean over=driver.findElement(By.id("override-checkbox")).isSelected();
		
		if(over ==true)
		{
			System.out.println("Over ride checkbox is already selected");
		}
		else
		{
			driver.findElement(By.id("override-checkbox")).click();
		}
		Thread.sleep(3000);
		
		//Click on Choose file
		CommonElementsofMenu.ChooseFileButton(driver);
		Thread.sleep(3000);
		
		//Loading the file into queue by using robot class
		String filepath=System.getProperty("user.dir") + "\\" + ImportSet;
		StringSelection stringSelection = new StringSelection(filepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	    Robot robot = new Robot();
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(2000);
	    robot.keyPress(KeyEvent.VK_ENTER);
	    robot.keyRelease(KeyEvent.VK_ENTER);
	    Thread.sleep(3000);
		
		//Click on import button
	    CommonElementsofMenu.ImportButton(driver);
	    //driver.findElement(By.cssSelector("#import-export-popup .float_right > .primary-btn:nth-child(1)")).click();
	    Thread.sleep(8000);
	    
	    try
		{
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
		}
		catch (Exception e)
		{
			System.out.println("Success msg popup not present");
		}
		Thread.sleep(8000);
		
		//Click on Cancel button
		/*
		 * driver.findElement(By.cssSelector("#import-export-popup > .close-button")).
		 * click(); Thread.sleep(2000);
		 * 
		 * //Select the viewlet option
		 * driver.findElement(By.cssSelector(".icon")).click();
		 * driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
		 * Thread.sleep(2000);
		 * 
		 * driver.findElement(By.xpath("//li[3]/ul/li[4]")).click();
		 * Thread.sleep(12000);
		 */
		
		//Export set
		/*
		 * WebElement ele=driver.findElement(By.cssSelector(".tablinks:nth-child(2)"));
		 * Actions ac=new Actions(driver); ac.moveToElement(ele); ac.click();
		 * ac.perform();
		 */
		
		driver.findElement(By.xpath("//button[contains(.,'Export')]")).click();
		Thread.sleep(5000);
		 
		 //Get the Sets data into string
		 String Setdata=driver.findElement(By.id("setTable")).getText();
		 System.out.println("Sets are :" +Setdata);
		 
		 if(Setdata.contains("TestSet") || Setdata.contains("event"))
		 {
			 System.out.println("Set is added");
			 context.setAttribute("Status",1);
			 context.setAttribute("Comment", "set is imported");
		 }
		 else
		 {
			 System.out.println("Set is not added");
			 context.setAttribute("Status",5);
			 context.setAttribute("Comment", "Set import failed");
			 //Close the page
			 CommonElementsofMenu.ImportExportCancelOption(driver);
			 driver.findElement(By.id("Import set failed")).click();
		 }
		 //Close the page
		 CommonElementsofMenu.ImportExportCancelOption(driver);
		 Thread.sleep(2000);
		
	}
	
	@TestRail(testCaseId=549)
	@Parameters({"sExportedSetName"})
	@Test(priority=9)
	public void ExportSet(String sExportedSetName, ITestContext context) throws InterruptedException
	{
		try
		{
		 //Select the viewlet option 
			CommonElementsofMenu.MenubarIcon(driver);
			//driver.findElement(By.xpath("//li[contains(.,' Import / Export')]")).click();
		//	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			CommonElementsofMenu.SetsOption(driver);
			Thread.sleep(5000);

			driver.findElement(By.xpath("//button[contains(.,'Export')]")).click();
			Thread.sleep(5000);
		 
		 //Search with set name
		 driver.findElement(By.xpath("//dialog[@id='import-export-popup']/section/main/aside/div/input")).sendKeys(sExportedSetName);
		 Thread.sleep(2000);
		 
		 //click on checkbox
		 driver.findElement(By.cssSelector(".checkbox-wp > input")).click();
		 
		 //Click on export button
		CommonElementsofMenu.ExportButton(driver);
		 Thread.sleep(10000);
		 
		//Close the page
		 CommonElementsofMenu.ImportExportCancelOption(driver);
		 Thread.sleep(2000);
		 
		 context.setAttribute("Status",1);
		 context.setAttribute("Comment", "Set is exported");
		}
		catch (Exception e)
		{
			System.out.println("Exception occured while exporting the set");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Set exporting failed");
			
			//Close the page
			 CommonElementsofMenu.ImportExportCancelOption(driver);
			 Thread.sleep(2000);
			driver.findElement(By.id("Set export failed")).click();
		}
		Thread.sleep(4000);
	}
	
	@Test(priority=20)
	public void Logout() throws InterruptedException
	{
		Thread.sleep(4000);
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}
	
	@Test(priority=21)
	public void CloseBrowser()
	{
		driver.close();
	}
	
	public void CreateDashboard(String DashbaordNameAs) throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//div[@id='pageContainer-tabs-add']/div/div/span")).click();
		
		//Give the dashboard Name
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/input")).sendKeys(DashbaordNameAs);
		
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
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
