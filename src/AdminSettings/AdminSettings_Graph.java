package AdminSettings;

import java.io.File;
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
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class AdminSettings_Graph {

	static WebDriver driver;
	static String Screenshotpath;

	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();

		Screenshotpath = Settings.getScreenshotPath();
	}

	// Login page
	@Test
	@Parameters({ "sDriverPath", "sDriver" })
	public static void Login(String sDriverPath, String sDriver) throws Exception {

		Settings.read();
		String sURL = Settings.getsURL();
		String sUsername = Settings.getsUsername();
		String sPassword = Settings.getsPassword();

		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverPath);
			driver = new ChromeDriver();
		} else if (sDriver.equalsIgnoreCase("webdriver.gecko.driver")) {
			System.setProperty(sDriver, sDriverPath);

		//	FirefoxOptions options = new FirefoxOptions();
		//	options.setCapability("marionette", false);
			driver = new FirefoxDriver();

		} else if (sDriver.equalsIgnoreCase("webdriver.ie.driver")) {
			System.setProperty(sDriver, sDriverPath);
			driver = new InternetExplorerDriver();
		} else {
			System.setProperty(sDriver, sDriverPath);
			driver = new EdgeDriver();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// Login Credentials
		driver.get(sURL);
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(25000);

		// Check Landing page
		if (driver.getPageSource().contains("Go to Dashboard")) {
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(25000);
		} else {
			System.out.println("Landing page is not present");
			Thread.sleep(25000);
		}

	}

	@TestRail(testCaseId = 401)
	@Test(priority = 1)
	public void OpenGraphSettings(ITestContext context) {
		try {
			// Click on Admin settings
			CommonElements.MenubarIcon(driver);
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);
			//click on colours tab
			driver.findElement(By.xpath("//label[4]/span")).click();
			Thread.sleep(3000);
			
			//Get the page severity color header and store into string
			String Header=driver.findElement(By.xpath("//div[4]/div[2]/div/strong")).getText();
			System.out.println("Header is: " +Header);
			
			if(Header.contains("Colors for severity"))
			{
				System.out.println("Graph page is opened");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Graph settings working fine");
			}
			else
			{
				System.out.println("Graph page is not opened");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Error while opening graph settings");
				driver.findElement(By.xpath("Graph page not opened")).click();
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			driver.findElement(By.xpath("Graph page not opened")).click();
		}
	}

	@TestRail(testCaseId = 402)
	@Test(priority = 2)
	public void CheckSeveritySettings(ITestContext context) {
							
		try {
			//driver.findElement(By.xpath("//label[4]/span/span")).click();
			
			// Check severity one by one 
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div/div/div")).click();
			Thread.sleep(2000); 
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
			Thread.sleep(4000);
			
			for(int i=2; i<=11; i++)
			{
				//System.out.println("i value: " +i);
				driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div["+ i +"]/div/div")).click();
				//close popup
				driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
				Thread.sleep(4000);
			}
			
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Severities are opened");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Severities are not opened");
			driver.findElement(By.id("failed")).click();
		}
	}

	// Method to change severity color from theme options
	@TestRail(testCaseId = 403)
	@Test(priority = 3)
	public void ChangeSeveritySettings(ITestContext context) {
		
		//driver.findElement(By.xpath("//label[4]/span/span")).click();
		try {
			// Change severity color from theme colors, selected error code
			//System.out.println("in method3");
			//driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[6]/div/div")).click();
			//Thread.sleep(2000);

			//Pick color
			//driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[6]/div/div[2]/span/table/tbody/tr[2]/td[6]")).click();
			//Thread.sleep(2000);
			
			//driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div/label[4]/span/span")).click();
			//Thread.sleep(2000);

			// Get color code for selected
			//driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[6]/div/div[2]/div[2]/span")).click();
			//Thread.sleep(2000);

			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[8]/div/div")).click();
			Thread.sleep(2000);
			
			WebElement target=driver.findElement(By.xpath("//span/table/tbody/tr[8]/td[10]"));
			//Mouse over the color name
			Actions a=new Actions(driver);
			a.moveToElement(target).perform();
			
			//get the result
			String TargetColor=driver.findElement(By.xpath("//div[8]/div/div[2]/div[3]/span")).getText();
			System.out.println("Target color is: "+TargetColor);
			
			//select one color from theme
			target.click();
			Thread.sleep(2000);
			
			//Open any color box
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[8]/div/div")).click();
			Thread.sleep(2000);
			
			String selectedcolor = driver.findElement(By.xpath("//div[8]/div/div[2]/div[2]/span")).getText();
			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(2000);
			
			if(TargetColor.equalsIgnoreCase(selectedcolor))
			{
				System.out.println("Theme color selected");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
			}
			else 
			{
				System.out.println("Severity color didn't updated");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
				Thread.sleep(4000);
				driver.findElement(By.id("failed")).click();
			}
			
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
			Thread.sleep(4000);
			
			/*
			 * // Save changes driver.findElement(By.xpath(
			 * "//dialog[@id='manage-user-settings-popup']/section/footer/button[2]")).click
			 * (); Thread.sleep(4000);
			 */
		
			/*// List all dynamic tab data
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
			System.out.println(myElements.size());
			for (WebElement e : myElements) {
				System.out.println(e.getAttribute("aria-hidden"));
				// boolean str= e.getAttribute("aria-hidden");
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					WebElement we_pie = e.findElement(By.className("middle-columns"));
					
					  List<WebElement> lst=we_pie.findElements(By.className("inner-column"));
					  
					  System.out.println("list size: "+ lst.size());
					 

					WebElement we = e.findElement(By.className("amcharts-pie-item")).findElement(By.tagName("path"));
					System.out.println(we.getAttribute("fill"));
					if (we.getAttribute("fill").matches(selectedcolor)) {
						System.out.println("Severity color updated successfully");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");

					} else {
						System.out.println("Severity color didn't updated");
						context.setAttribute("Status", 5);
						context.setAttribute("Comment", "Failed");
						driver.findElement(By.id("failed")).click();
					}
				}
			} */

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			driver.findElement(By.id("failed")).click();
		}
	}

	// Method to change severity color from theme options
	@TestRail(testCaseId = 404)
	@Test(priority = 4)
	@Parameters({ "sDriverPath", "sDriver" })
	public void ChangeSeveritybyStandardcolor(String sDriverPath,String sDriver,  ITestContext context) {
		
		try {
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[8]/div/div")).click();
			Thread.sleep(2000);
			
			WebElement target=driver.findElement(By.xpath("//span/table/tbody/tr[10]/td[10]"));
			//Mouse over the color name
			Actions a=new Actions(driver);
			a.moveToElement(target).perform();
			
			//get the result
			String TargetColor=driver.findElement(By.xpath("//div[8]/div/div[2]/div[3]/span")).getText();
			System.out.println("Target color is: "+TargetColor);
			
			//select one color from standard colors
			target.click();
			Thread.sleep(2000);
			
			//Open any color box
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[8]/div/div")).click();
			Thread.sleep(2000);
			
			String selectedcolor = driver.findElement(By.xpath("//div[8]/div/div[2]/div[2]/span")).getText();
			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(2000);
			
			if(TargetColor.equalsIgnoreCase(selectedcolor))
			{
				System.out.println("Standard color selected");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
			}
			else 
			{
				System.out.println("Standard color didn't updated");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
				Thread.sleep(4000);
				driver.findElement(By.id("failed")).click();
			}
			
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
			Thread.sleep(4000);

			/*try
			{
				// Click ok button
				driver.findElement(By.xpath("//input[@value='OK']")).click();
				Thread.sleep(2000);
			}
			catch (Exception e)
			{
				System.out.println("No confirmation popup");
			}*/
			
			// Create dashboard to verify changes
			// Create dashboard to verify changes
			//driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
			//Thread.sleep(4000);
			
			/*driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[2]")).click();
			Thread.sleep(2000);

			// Select create dashboard
			driver.findElement(By.cssSelector(".hasSubMenuOpen li:nth-child(1)")).click();
			Thread.sleep(1000);*/
			
			// List all dynamic tab data
			/*	List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
			System.out.println(myElements.size());
			for (WebElement e : myElements) {
				System.out.println(e.getAttribute("aria-hidden"));
				// boolean str= e.getAttribute("aria-hidden");
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					WebElement we = e.findElement(By.className("amcharts-pie-item")).findElement(By.tagName("path"));
					System.out.println(we.getAttribute("fill"));
					if (we.getAttribute("fill").matches(selectedcolor)) {
						System.out.println("Severity color updated successfully");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");
					} else {
						System.out.println("Severity color didn't updated");

						context.setAttribute("Status", 5);
						context.setAttribute("Comment", "Failed");
						driver.findElement(By.id("failed")).click();
					}
				} 
			}*/

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			driver.findElement(By.id("failed")).click();
		}
	}

	// Method to change severity color from theme options
	@TestRail(testCaseId = 405)
	@Test(priority = 4)
	@Parameters({ "sDriverPath", "sDriver" })
	public void ChangeSeveritybyWebcolor(String sDriverPath,String sDriver,ITestContext context) {
		try {
			
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[8]/div/div")).click();
			Thread.sleep(2000);
			
			//click on web color
			driver.findElement(By.linkText("Web Colors")).click();
			Thread.sleep(2000);
			
			WebElement target=driver.findElement(By.xpath("//table[7]/tbody/tr/td[11]"));
			//Mouse over the color name
			Actions a=new Actions(driver);
			a.moveToElement(target).perform();
			
			//get the result
			String TargetColor=driver.findElement(By.xpath("//div[8]/div/div[2]/div[3]/span")).getText();
			System.out.println("Target color is: "+TargetColor);
			
			//select one color from standard colors
			target.click();
			Thread.sleep(2000);
			
			//Open any color box
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/main/div/div[2]/div[4]/div[2]/div[2]/div[8]/div/div")).click();
			Thread.sleep(2000);
			
			//click on theme color link
			driver.findElement(By.linkText("Theme Colors")).click();
			Thread.sleep(4000);
			
			String selectedcolor = driver.findElement(By.xpath("//div[8]/div/div[2]/div[2]/span")).getText();
			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(2000);
			
			if(TargetColor.equalsIgnoreCase(selectedcolor))
			{
				System.out.println("Web color selected");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
			}
			else 
			{
				System.out.println("Web color didn't updated");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
				Thread.sleep(4000);
				//Click on Restore default values
				driver.findElement(By.xpath("//div[4]/div/div/div/button")).click();
				Thread.sleep(3000);
				
				// Save changes
				driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer/button[2]")).click();
				Thread.sleep(2000);
				driver.findElement(By.id("failed")).click();
			}
			
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer")).click();
			Thread.sleep(4000);
			
			//Click on Restore default values
			driver.findElement(By.xpath("//div[4]/div/div/div/button")).click();
			Thread.sleep(3000);
			
			// Save changes
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer/button[2]")).click();
			Thread.sleep(6000);
			
			//click on cancel button
			driver.findElement(By.xpath("//dialog[@id='manage-user-settings-popup']/section/footer/button")).click();
			Thread.sleep(4000);
			

		/*	try
			{
				// Click ok button
				driver.findElement(By.xpath("//input[@value='OK']")).click();
				Thread.sleep(2000);
			}
			catch (Exception e)
			{
				System.out.println("No confirmation popup");
			}
			
			driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
			Thread.sleep(4000); */
			
			/*driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[2]")).click();
			Thread.sleep(2000);

			// Select create dashboard
			driver.findElement(By.cssSelector(".hasSubMenuOpen li:nth-child(1)")).click();
			Thread.sleep(1000);

			// Enter dashboard name
			driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/main/div/input")).sendKeys("VerifySeverityColor");
			Thread.sleep(1000);

			// Select template            
			driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/main/div/ul/li/div")).click();
			Thread.sleep(1000);

			// Uncheck create intitial viewlet
			driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/main/div/label/input")).click();
			Thread.sleep(1000);

			// Click create button
			driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/footer/div[2]/button")).click();
			Thread.sleep(1000);

			// Create viewlet to verify using query
			driver.findElement(By.xpath("//div[@id='app-top-sidebar']/div[3]/span")).click();
			Thread.sleep(1000);

			// Select create viewlet option
			driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div/label/input")).click();
			Thread.sleep(1000);

			// click on create btn
			driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/footer/div[2]/button")).click();
			Thread.sleep(1000);

			// Passing query string
			driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet']/section/main/div/jkql-input/textarea")).sendKeys("Get number of Event where Severity ='ERROR' group by Severity order by Severity show as piechart");
			Thread.sleep(3000);

			// click on cretae btn
			driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet']/section/footer/button[2]")).click();
			Thread.sleep(4000);
			
			// List all dynamic tab data
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
			System.out.println(myElements.size());
			for (WebElement e : myElements) {
				System.out.println(e.getAttribute("aria-hidden"));
				// boolean str= e.getAttribute("aria-hidden");
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {*/
					/*WebElement we = e.findElement(By.className("amcharts-pie-item")).findElement(By.tagName("path"));
					System.out.println(we.getAttribute("fill"));
					if (we.getAttribute("fill").matches(selectedcolor)) {
						System.out.println("Severity color by applying web color updated successfully");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");
					} else {
						System.out.println("Severity color didn't updated");
						context.setAttribute("Status", 5);
						context.setAttribute("Comment", "Failed");
						driver.findElement(By.id("failed")).click();
					}
				}
			}*/

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			driver.findElement(By.id("failed")).click();
		}
	}

	@TestRail(testCaseId = 406)
	@Test(priority = 7)
	public void VerifyHistorySeverity(ITestContext context) {
		try {
			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			String prevselectedcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();
			System.out.println("prevselectedcolor color code: " + prevselectedcolor);
			Thread.sleep(1000);

			// Mouseover on submit button
			Actions action = new Actions(driver);
			WebElement we = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/span/table/tbody/tr[6]/td[6]"));
			action.moveToElement(we).perform();

			Thread.sleep(2000);

			// Get color code for selected
			// driver.findElement(By.xpath("//aside[4]/div/div[7]/div/div/div")).click();
			// Thread.sleep(2000);

			String selectedcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[3]/span")).getText();
			System.out.println("selector color is: " + selectedcolor);

			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(1000);

			// click on history link
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div/a[2]")).click();
			Thread.sleep(1000);

			// Verify histroty
			String hstryverifycolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();
			System.out.println("History color is: " + hstryverifycolor);

			String hstryverifselectedycolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[3]/span")).getText();
			System.out.println("history verify color is: " + hstryverifselectedycolor);

			if (hstryverifycolor.equals(prevselectedcolor) && hstryverifselectedycolor.equals(selectedcolor)) {
				System.out.println("History verified");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			} else {
				System.out.println("History not verified");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				driver.findElement(By.id("failed")).click();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			driver.findElement(By.id("failed")).click();
		}

	}

	@TestRail(testCaseId = 407)
	@Test(priority = 6)
	public void VerifyBackSeverity(ITestContext context) {
		try {

			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			String prevselectedcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();
			System.out.println("prevselectedcolor color code: " + prevselectedcolor);
			Thread.sleep(1000);

			// Mouseover on submit button
			Actions action = new Actions(driver);
			WebElement we = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/span/table/tbody/tr[6]/td[6]"));
			action.moveToElement(we).perform();

			Thread.sleep(2000);

			// Get color code for selected
			// driver.findElement(By.xpath("//aside[4]/div/div[7]/div/div/div")).click();
			// Thread.sleep(2000);

			String selectedcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[3]/span")).getText();

			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(1000);

			// click on history link
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div/a[2]")).click();
			Thread.sleep(1000);

			/*
			 * //Verify histroty String
			 * hstryverifycolor=driver.findElement(By.xpath("//div[2]/div[2]/span")).getText
			 * ();
			 * 
			 * String
			 * hstryverifselectedycolor=driver.findElement(By.xpath("//div[2]/div[3]/span"))
			 * .getText();
			 * 
			 * if(hstryverifycolor.equals(prevselectedcolor) &&
			 * hstryverifselectedycolor.equals(selectedcolor)) {
			 * System.out.println("History verified"); }
			 */

			// Click back link
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div/a")).click();
			Thread.sleep(5000);

			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();

		} catch (Exception ex) {
			ex.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			driver.findElement(By.id("failed")).click();

		}

	}

	@TestRail(testCaseId = 408)
	@Test(priority = 8)
	public void VerifyCanselSeverity(ITestContext context) {
		try {

			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			// Select color
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/span/table/tbody/tr[6]/td[6]")).click();
			Thread.sleep(2000);

			// Get color code for selected
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(2000);

			String selectedcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();
			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(1000);

			// Click cancel
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			Thread.sleep(1000);

			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			String presentcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();
			System.out.println("presentcolor color code: " + presentcolor);
			Thread.sleep(1000);

			if (!selectedcolor.equals(presentcolor)) {
				System.out.println("Cancel option working fine");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			} else {
				System.out.println("Cancel option not working fine");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				driver.findElement(By.id("failed")).click();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			driver.findElement(By.id("failed")).click();
		}

	}

	@TestRail(testCaseId = 409)
	@Test(priority = 9)
	public void VerifyResetSeverity(ITestContext context) {
		try {

			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			String presentcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();

			System.out.println("present color: " + presentcolor);

			// Select color
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/span/table/tbody/tr[2]/td[6]")).click();
			Thread.sleep(2000);

			// Get color code for selected
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(2000);

			String selectedcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();

			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(1000);

			// Click cancel
			driver.findElement(By.xpath("//aside[4]/button")).click();
			Thread.sleep(1000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			String color = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();

			System.out.println("after color: " + color);
			Thread.sleep(1000);

			if (color.equals("#4878E8")) {
				System.out.println("Reset option working fine");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			} else {
				System.out.println("Reset not working");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				driver.findElement(By.id("failed")).click();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			driver.findElement(By.id("failed")).click();
		}

	}

	@TestRail(testCaseId = 410)
	@Test(priority = 10)
	public void CancelGraphSettings(ITestContext context) {
		try {

			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			// Click cancel
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			Thread.sleep(1000);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");

		} catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("failed")).click();
		}
	}

	@TestRail(testCaseId = 411)
	@Test(priority = 11)
	public void CheckSeverityHexValue(ITestContext context) {
		try {

			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			String presentcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[2]/span")).getText();

			if (!presentcolor.isEmpty()) {
				System.out.println("Hex value for severity exits");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			} else {
				System.out.println("Hex value for severity not exits");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				driver.findElement(By.id("failed")).click();
			}

		} catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			driver.findElement(By.id("failed")).click();
		}
	}

	@TestRail(testCaseId = 412)
	@Test(priority = 12)
	public void CheckSeveritySelectedHexValue(ITestContext context) {
		try {

			// Click on Admin settings
			driver.findElement(By.xpath("//button/i")).click();
			driver.findElement(By.xpath("//li[contains(.,' User Settings')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//span[contains(.,'Manage Settings')]")).click();
			Thread.sleep(6000);

			// Select severity
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div")).click();
			Thread.sleep(1000);

			// Mouseover on submit button
			Actions action = new Actions(driver);
			WebElement we = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/span/table/tbody/tr[8]/td[10]"));
			action.moveToElement(we).perform();

			Thread.sleep(2000);

			// Get color code for selected
			// driver.findElement(By.xpath("//aside[4]/div/div[7]/div/div/div")).click();
			// Thread.sleep(2000);

			String selectedcolor = driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/div[9]/div/div/div[2]/div[3]/span")).getText();

			System.out.println("Selected color code: " + selectedcolor);
			Thread.sleep(1000);

			if (!selectedcolor.isEmpty()) {
				System.out.println("Hex value for selected severity exits");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			} else {
				System.out.println("Hex value for selected severity not exits");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				driver.findElement(By.id("failed")).click();
			}

		} catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
			driver.findElement(By.id("failed")).click();
		}
	}

	@Test(priority = 20)
	public void Logout() throws InterruptedException 
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}

	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		// System.out.println("dir: " + dir);
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
				// Update attachment to testrail server
				int testCaseID = 0;
				// int status=(int) result.getTestContext().getAttribute("Status");
				// String comment=(String) result.getTestContext().getAttribute("Comment");
				if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class)) {
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod()
							.getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();

					TestRailAPI api = new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());

				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
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
