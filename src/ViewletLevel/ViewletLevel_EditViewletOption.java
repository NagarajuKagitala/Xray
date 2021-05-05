package ViewletLevel;

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
import org.openqa.selenium.chrome.ChromeOptions;
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

import Common.CommonForAll;
import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class ViewletLevel_EditViewletOption {
	static WebDriver driver;
	static String Screenshotpath;
	String ViewletName;
	String viewleteditxpath;
	int tabindex=0;
	int Dashboardscount=0;
	StringBuilder GetViewletNames = new StringBuilder();
	CommonForAll obj=new CommonForAll();
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();

		Screenshotpath = Settings.getScreenshotPath();
	}

	// Login page
	@Test
	@Parameters({ "sDriverPath", "sDriver", "DownloadPath", "DashboardName"})
	public void Login(String sDriverPath, String sDriver, String DownloadPath, String DashboardName) throws Exception {

		Settings.read();
		String sURL = Settings.getsURL();
		String sUsername = Settings.getsUsername();
		String sPassword = Settings.getsPassword();

		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverPath);
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.prompt_for_download", "false");
			chromePrefs.put("download.default_directory", DownloadPath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(options);
		} else if (sDriver.equalsIgnoreCase("webdriver.gecko.driver")) {
			System.setProperty(sDriver, sDriverPath);

			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", false);
			driver = new FirefoxDriver(options);

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
		Thread.sleep(4000);

		// Check Landing page
		if (driver.getPageSource().contains("Go to Dashboard")) {
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(15000);
		} else {
			System.out.println("Landing page is not present");
			Thread.sleep(6000);
		}

		obj.CreateDashboard(driver, DashboardName);
		Thread.sleep(2000);
		
        WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
		
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		
	}

	@TestRail(testCaseId = 454)
	@Test(priority = 1)
	public void CreateViewletWithAForm(ITestContext context) throws InterruptedException 
	{
		//Click on Viewlet button
		driver.findElement(By.xpath("//div[@id='app-top-sidebar']/div[3]")).click();
				
		//Check the Create viewlet with JKQL check box
		boolean Form=driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[2]/label/input")).isSelected();
		
		if(Form == true)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.xpath("//dialog[@id='create-viewlet']/section/main/div[2]/label/input")).click();
			Thread.sleep(1000);
		}
		
		driver.findElement(By.id("createViewletBtn")).click();
		Thread.sleep(8000);
		
		//Give the viewlet name     
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).clear();
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).click();
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).sendKeys("ViewletName");
		
		String ViewletName1=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).getAttribute("value");
		System.out.println("Get viewlet name is: " +ViewletName1);
		
		//select some graph
		driver.findElement(By.cssSelector("label:nth-child(1) > .color-icon > svg")).click();
		Thread.sleep(1000);
		
		//Click on Create viewlet button
		CommonElementsofViewlet.CreateButton(driver);
		Thread.sleep(4000);
		
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {

			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
				List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
				System.out.println("Viewlet size:" + ee.size());
				StringBuilder GetViewletName = new StringBuilder();
				for (WebElement innerele : ee) {

					GetViewletName.append(innerele.findElement(By.tagName("input")).getAttribute("value"));
					GetViewletName.append(" , ");
				}
				System.out.println("GetViewletName:" + GetViewletName + " ViewletName: " + ViewletName1);
				// verification
				if (GetViewletName.toString().contains(ViewletName1)) {
					System.out.println("Viewlet is created successfully");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "working fine");
				} else {
					System.out.println("Viewlet is not created");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Failed");
					driver.findElement(By.id("Viewlet Creation failed")).click();
				}
				Thread.sleep(6000);
			}
		}

	}

	@TestRail(testCaseId = 455)
	@Test(priority = 2)
	public void EditOptionInViewletMenu(ITestContext context) throws InterruptedException {

		try {
			int i=0;

			List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {
				i++;
				System.out.println("i: "+ i);
				// boolean str= e.getAttribute("aria-hidden");
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {

					viewleteditxpath="//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i";
					tabindex=i;
					// Click on Viewlet Menu icon
					// Thread.sleep(4000);                       
					driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
					Thread.sleep(2000);
					// Select Edit option
					driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
					Thread.sleep(4000);
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "working fine");
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception, check details: " + ex.getMessage());
			driver.findElement(By.id("Edit Option failed")).click();
		}
	}

	@TestRail(testCaseId = 456)
	@Test(priority = 3)
	public void EditviewletWindowOptions(ITestContext context) throws InterruptedException {
		// --------- Right side panel objects verification -------

		// Viewlet Name field
		try {
		Boolean Viewlet = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).isEnabled();

		Boolean verify=false;
		if (Viewlet) {
			System.out.println("Viewlet Name field is Enabled");
			verify=true;
		} else {
			System.out.println("ViewletName field is not Enabled");
			verify=false;
			driver.findElement(By.id("Viewlet name field is Disable")).click();
		}
		Thread.sleep(1000);

		// Data type field
		Boolean DataType = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[2]/div/span/span/span/span")).isEnabled();

		if (DataType) {
			System.out.println("DataType field is Enabled");
			verify=true;
		} else {
			System.out.println("DataType field is not Enabled");
			verify=false;
			driver.findElement(By.id("Data type field is Disable")).click();
		}
		Thread.sleep(1000);

		// Time Period field
		Boolean TimePeriod = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[3]/div/div/div/div/div/span/span/span/span"))
				.isEnabled();

		if (TimePeriod) {
			System.out.println("Time Period field is Enabled");
			verify=true;
		} else {
			System.out.println("Time Period field is not Enabled");
			verify=false;
			driver.findElement(By.id("Time period field is Disable")).click();
		}
		Thread.sleep(1000);

		// Fields
		Boolean Fields = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[4]/h3/i")).isEnabled();

		if (Fields) {
			System.out.println("Fields is Enabled");
			verify=true;
		} else {
			System.out.println("Fields is not Enabled");
			verify=false;
			driver.findElement(By.id("Fields is Disable")).click();
		}
		Thread.sleep(1000);

		// Group By
		Boolean GroupBy = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/h3/i")).isEnabled();

		if (GroupBy) {
			System.out.println("Group By is Enabled");
			verify=true;
		} else {
			System.out.println("Group By is not Enabled");
			verify=false;
			driver.findElement(By.id("Group by is Disable")).click();
		}
		Thread.sleep(1000);

		// Filters
		Boolean Filters = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[6]/h3/i")).isEnabled();

		if (Filters) {
			System.out.println("Filters is Enabled");
			verify=true;
		} else {
			System.out.println("Filters is not Enabled");
			verify=false;
			driver.findElement(By.id("Filters is Disable")).click();
		}
		Thread.sleep(1000);

		// View let Type
		Boolean ViewletType = driver.findElement(By.xpath("//span[contains(.,'Viewlet Type')]")).isEnabled();

		if (ViewletType) {
			System.out.println("Viewlet Type is Enabled");
			verify=true;
		} else {
			System.out.println("Viewlet Type is not Enabled");
			verify=false;
			driver.findElement(By.id("Viewlet type field is Disable")).click();
		}
		Thread.sleep(1000);

		// ------ Buttons verification ----

		// Close button
		Boolean Close = driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[11]/button")).isEnabled();

		if (Close) {
			System.out.println("Close button is Enabled");
			verify=true;
		} else {
			System.out.println("Close button is not Enabled");
			verify=false;
			driver.findElement(By.id("Close button is Disable")).click();
		}
		Thread.sleep(1000);

		/*// Create button
		Boolean Create = driver.findElement(By.xpath("//div[8]/div/button")).isEnabled();

		if (Create) {
			System.out.println("Create button is Enabled");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Create button is not Enabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Create button is Disable")).click();
		}
		Thread.sleep(1000);*/

		// Apply button
		Boolean Apply = driver.findElement(By.xpath("//button[contains(.,'Apply')]")).isEnabled();

		if (Apply) {
			System.out.println("Apply button is Enabled");
			verify=true;
		} else {
			System.out.println("Apply button is not Enabled");
			verify=false;
			driver.findElement(By.id("Apply button is Disable")).click();
		}
		Thread.sleep(1000);
		
		if(verify)
		{
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Edit viewlet option is working fine");
		}
		else
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Edit viewlet option is not working working properly");
		}
		
		}catch(Exception e)
		{
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception, check details: "+ e.getMessage());
			driver.findElement(By.id("Apply button is Disable")).click();
		}
		Thread.sleep(2000);
	}

	@TestRail(testCaseId = 457)
	@Test(priority = 4)
	public void CloseButton(ITestContext context) throws InterruptedException 
	{
		String ViewletName=driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div/div/input")).getAttribute("value");
		System.out.println("Get viewlet name is: " +ViewletName);
		
		// Click on Close button
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[11]/button")).click();
		Thread.sleep(6000);
		
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {
			
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
				
				List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
				System.out.println("Viewlet size:" + ee.size());
				StringBuilder GetViewletName = new StringBuilder();
				for (WebElement innerele : ee) {

					Thread.sleep(2000);
					GetViewletName.append(innerele.findElement(By.tagName("input")).getAttribute("value"));
					GetViewletName.append(" , ");
				}
				System.out.println("GetViewletName:" + GetViewletName + " ViewletName: " + ViewletName);
				System.out.println("Viewlet name 2: " +ViewletName);
				// verification
				if (GetViewletName.toString().contains(ViewletName)) {
					System.out.println("The close button is working");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "working fine");
				} else {
					System.out.println("The close button is not working fine");
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Failed");
					driver.findElement(By.id("Close button is Disable")).click();
				}
				Thread.sleep(6000);

							
				return;
				
				// Select Edit option
				
			}
			
		}

		
		ITestContext context1 = null;
		this.EditOptionInViewletMenu(context1);
		Thread.sleep(3000);
	}

	@TestRail(testCaseId = 458)
	@Test(priority = 5)
	public void PreviewButton(ITestContext context) throws InterruptedException {
		
		try {
		int i=0;

		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {
			i++;
			System.out.println("i: "+ i);
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {

				
				// Click on Viewlet Menu icon
				// Thread.sleep(4000);
				driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
				Thread.sleep(2000);
				// Select Edit option
				driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
				Thread.sleep(4000);
				
			}
			
		}
		// Change Event to Activity
		//driver.findElement(By.xpath("//div[2]/div[2]/div/span/span/span/span")).click();
		driver.findElement(By.xpath("//div[@id='viewlet-form-settings-wp']/div[2]/div/span/span/span/span")).click();
		Thread.sleep(1000);
		
		
		
		//Change Event to Activity
		WebElement Datatype=driver.findElement(By.className("select2-results"));
		WebElement UL=Datatype.findElement(By.tagName("ul"));
		
		/*String data=Datatype.getText();
		System.out.println("Drop down data:" + data);*/
		
		List<WebElement> ll=UL.findElements(By.tagName("li"));
		System.out.println(ll.size());
		
		for(WebElement list : ll)
		{
			System.out.println(list.getText());
			if(list.getText().equalsIgnoreCase("Activity"))
			{
				list.click();
				break;
			}
		}
		Thread.sleep(4000);

		// Click on Preview
		driver.findElement(By.xpath("//button[contains(.,'Preview')]")).click();
		Thread.sleep(3000);
													
		
		// Store the Activity name into string   
		String variable = driver.findElement(By.xpath("//div/div/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[2]"))
				.getText();
		
		System.out.println("Ver: "+ variable);

		if (variable.equalsIgnoreCase(" ActivityID")) {
			System.out.println("Preview button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");

		} else {
			System.out.println("Preview button is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Preview button is Disable")).click();
		}
		Thread.sleep(1000);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@TestRail(testCaseId = 459)
	@Test(priority = 6)
	public void ApplyButton(ITestContext context) throws InterruptedException {
		try {
		// Click on Apply button
		driver.findElement(By.xpath("//button[contains(.,'Apply')]")).click();
		Thread.sleep(2000);

		// Store the Activity name into string                         
		String Ver = driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div[2]/div/div/div[3]/div[2]/div/table/thead/tr/th[3]")).getText();
		System.out.println(Ver);
		Thread.sleep(2000);

		if (Ver.equalsIgnoreCase(" ActivityID")) {
			System.out.println("Apply button is working fine");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
		} else {
			System.out.println("Apply button is not working");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Apply button is Disable")).click();
		}
		Thread.sleep(1000);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@TestRail(testCaseId = 460)
	@Test(priority = 7)
	public void SaveOption(ITestContext context) throws InterruptedException {
		// Click on Viewlet Menu
		int i=0;

		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {
			i++;
			System.out.println("i: "+ i);
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
				
				// Click on Viewlet Menu icon
				// Thread.sleep(4000);
				tabindex=i;
				System.out.println("i value is: " +i);
				driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
				Thread.sleep(2000);
				// Select Save option
							
				WebElement Save = driver.findElement(By.xpath("//li[contains(.,'Save Viewlet')]"));

				String isEnabled = Save.getAttribute("class");
				// System.out.println(isEnabled);

				if (!isEnabled.equals("disabled")) {
					System.out.println("Save option is Enabled");
					Save.click();
					Thread.sleep(6000);

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
						// Click on Viewlet Menu
						driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
						Thread.sleep(1000);
						driver.findElement(By.id("Save Option failed")).click();
					}

					// Click on Viewlet Menu
					driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
					Thread.sleep(1000);
				} else {
					System.out.println("Save option is disabled state");
				}
				Thread.sleep(5000);
				
			}
			
		}

		
	}

	@Parameters({ "NewViewletName" })
	@TestRail(testCaseId = 461)
	@Test(priority = 8)
	public void SaveAsOption(String NewViewletName, ITestContext context) throws InterruptedException {
		try {
		//driver.findElement(By.xpath(viewleteditxpath)).click();
		//Thread.sleep(2000);
		
		WebElement SaveAs = driver.findElement(By.xpath("//li[contains(.,'Save As Viewlet')]"));

		String isEnabled = SaveAs.getAttribute("class");
		// System.out.println(isEnabled);

		if (!isEnabled.equals("disabled")) {
			System.out.println("Save As option is Enabled");
			SaveAs.click();
			Thread.sleep(1000);

			// Enter the save as viewlet name
			driver.findElement(By.xpath("//dialog[@id='save-as-viewlet']/section/main/div/input")).clear();
			driver.findElement(By.xpath("//dialog[@id='save-as-viewlet']/section/main/div/input")).sendKeys(NewViewletName);

			// Click on Confirmation ok button 
			driver.findElement(By.cssSelector("#save-as-viewlet .primary-btn")).click();
			Thread.sleep(3000);
			
			List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
			// System.out.println(myElements.size());
			for (WebElement e : myElements) {

				// boolean str= e.getAttribute("aria-hidden");
				if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
					List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
					System.out.println("Viewlet size:" + ee.size());
					
					for (WebElement innerele : ee) {

						GetViewletNames.append(innerele.findElement(By.tagName("input")).getAttribute("value"));
						GetViewletNames.append(" , ");
					}
					System.out.println("GetViewletName:" + GetViewletNames + " ViewletName: " + NewViewletName);
					
					System.out.println("tab index values is: " +tabindex);
					// verification
					if (GetViewletNames.toString().contains(NewViewletName)) {
						System.out.println("Save As Option is working fine");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "working fine");
					} else {
						System.out.println("Save As Option is not working");
						context.setAttribute("Status", 5);
						context.setAttribute("Comment", "Failed");
						// Click on Viewlet Menu
						driver.findElement(By.xpath("//div["+tabindex+"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
						Thread.sleep(2000);
						driver.findElement(By.id("Save As Option failed")).click();
					}
					Thread.sleep(6000);
				}
			}
			

			
			// Click on Viewlet Menu
			driver.findElement(By.xpath("//div["+tabindex+"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i")).click();
			Thread.sleep(2000);
		} else {
			System.out.println("Save As Option is disabled state");
		}
		Thread.sleep(1000);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Parameters({ "QueryBefore" })
	@TestRail(testCaseId = 462)
	@Test(priority = 9)
	public void RemoveOption(String QueryBefore, ITestContext context) throws InterruptedException {
		
		//driver.findElement(By.xpath("//div["+tabindex+"]/div[2]/div/div/ul/li/div[2]/div/div[2]/div[9]")).click();
		//Thread.sleep(2000);
		
		//div[6]/div[2]/div/div/ul/li/div[2]/div/div[2]/div[9]
		WebElement Remove = driver.findElement(By.xpath("//li[contains(.,'Remove Viewlet')]"));

		String isEnabled = Remove.getAttribute("class");
		// System.out.println(isEnabled);

		if (!isEnabled.equals("disabled")) {
			System.out.println("Remove option is Enabled");
			Remove.click();
			Thread.sleep(1000);

			// Click on Confirmation ok button
			driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
			Thread.sleep(2000);

			// Store the viewlets data
			String Data="";
			String RightData="";
			try { 
			Data = driver.findElement(By.xpath("//div["+tabindex+"]/div[2]/div/div/ul/li/div[2]/div/div/jkql-input/input")).getText();
			RightData = driver.findElement(By.xpath("//div["+tabindex+"]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).getText();
			}catch(Exception e)
			{
				
			}
			 System.out.println("Data is: " +Data);
			 
			 
			 List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				// System.out.println(myElements.size());
				for (WebElement e : myElements) {

					// boolean str= e.getAttribute("aria-hidden");
					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
						List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
						System.out.println("Viewlet size:" + ee.size());
						StringBuilder GetViewletName = new StringBuilder();
						for (WebElement innerele : ee) {

							GetViewletName.append(innerele.findElement(By.tagName("input")).getAttribute("value"));
							GetViewletName.append(" , ");
						}
						System.out.println("GetViewletName:" + GetViewletName + " ViewletName: " + GetViewletNames);
						// verification
						if (!(GetViewletName.toString().equalsIgnoreCase(GetViewletNames.toString())) || GetViewletName.toString().equals("")) {
							System.out.println("Viewlet is removed successfully");
							context.setAttribute("Status", 1);
							context.setAttribute("Comment", "working fine");
							this.CreateViewletWithAForm(context);
							
						} else {
							System.out.println("Viewlet is not removed");
							context.setAttribute("Status", 5);
							context.setAttribute("Comment", "Failed");
							// Click on Viewlet Menu
							driver.findElement(By.xpath(viewleteditxpath)).click();
							Thread.sleep(2000);
							driver.findElement(By.id("Remove Option failed")).click();
						}
						Thread.sleep(6000);
					}
				}

			// Store the right side
			
				Thread.sleep(3000);
			// Click on Viewlet Menu
				System.out.println("xpath expression is: " +viewleteditxpath);
			driver.findElement(By.xpath(viewleteditxpath)).click();
			Thread.sleep(2000);
		} else {
			System.out.println("Remove viewlet option is disabled state");
		}
		Thread.sleep(1000);
	}

	@Parameters({ "QueryBefore" })
	@TestRail(testCaseId = 463)
	@Test(priority = 10)
	public void DeleteOption(String QueryBefore, ITestContext context) throws InterruptedException {
		try {
		WebElement Delete = driver.findElement(By.xpath("//li[contains(.,'Delete Viewlet')]"));

		String isEnabled = Delete.getAttribute("class");
		// System.out.println(isEnabled);

		if (!isEnabled.equals("disabled")) {
			System.out.println("Delete option is Enabled");
			Delete.click();
			Thread.sleep(1000);

			// Click on Confirmation ok button
			driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
			Thread.sleep(1000);

			 List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				// System.out.println(myElements.size());
				for (WebElement e : myElements) {

					// boolean str= e.getAttribute("aria-hidden");
					if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
						List<WebElement> ee = e.findElements(By.className("viewlet-name-wrapper"));
						System.out.println("Viewlet size:" + ee.size());
						StringBuilder GetViewletName = new StringBuilder();
						for (WebElement innerele : ee) {

							GetViewletName.append(innerele.findElement(By.tagName("input")).getAttribute("value"));
							GetViewletName.append(" , ");
						}
						System.out.println("GetViewletName:" + GetViewletName + " ViewletName: " + GetViewletNames);
						// verification
						if (GetViewletName.toString().equalsIgnoreCase(GetViewletNames.toString())) {
							System.out.println("Viewlet is not Deleted");
							context.setAttribute("Status", 5);
							context.setAttribute("Comment", "Failed");
							// Click on Viewlet Menu
							driver.findElement(By.xpath(viewleteditxpath)).click();
							Thread.sleep(2000);
							driver.findElement(By.id("Delete Option failed")).click();
						} else {
							System.out.println("Viewlet is Deleted successfully");
							context.setAttribute("Status", 1);
							context.setAttribute("Comment", "working fine");
							this.CreateViewletWithAForm(context);
						}
						Thread.sleep(6000);
					}
				}

			
			// Click on Viewlet Menu
			driver.findElement(By.xpath(viewleteditxpath)).click();
			Thread.sleep(2000);

		} else {
			System.out.println("Delete viewlet option is disabled state");
		}
		Thread.sleep(1000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed to delete viewlet, check details: "+ e.getMessage());
		}
	}

	@TestRail(testCaseId = 464)
	@Test(priority = 11)
	public void ExportOption(ITestContext context) throws InterruptedException {
		try {
		
		//this.SaveViewlet();
		//driver.findElement(By.xpath(viewleteditxpath)).click();
		Thread.sleep(2000);
		Thread.sleep(2000);   
		WebElement Export = driver.findElement(By.xpath("//li[contains(.,'Export to CSV')]"));

		String isEnabled = Export.getAttribute("class");
		System.out.println("Status of the export is: " +isEnabled);

		if (isEnabled.equals("Enabled")) {
			System.out.println("Export option is Enabled");
			Export.click();
			Thread.sleep(8000);
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "working fine");
			
		} 
		else if(isEnabled.equals("disabled"))
		{
			System.out.println("Export  is disabled state");
		}
		else {
			System.out.println("Export viewlet option is disabled state");
			context.setAttribute("Status", 1);
			context.setAttribute("Comment", "Passed");
		}
		}
		catch(Exception e)
		{
			System.out.println("Export viewlet option is disabled");
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Export failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"DashboardName"})
	@TestRail(testCaseId = 801)
	@Test(priority=12)
	public void shareviewlet(String DashboardName, ITestContext context) throws InterruptedException
	{
		this.SaveViewlet();
		this.SaveDashboard(DashboardName);
		
		// Click on Viewlet Menu
		driver.findElement(By.xpath(viewleteditxpath)).click();
		Thread.sleep(2000);
				
		//click on Share viewlet
		driver.findElement(By.xpath("//li[contains(.,'Share Viewlet')]")).click();
		Thread.sleep(12000);
		
		//click on share viewlet button
		driver.findElement(By.xpath("//button[contains(.,'Share')]")).click();
		Thread.sleep(3000);
		
		//verification of share viewlet
		WebElement shareviewlet=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div/div[2]/div[2]/button[8]/i"));
		
		String isEnabled = shareviewlet.getAttribute("class"); 
		System.out.println("Status of share viewlet: " +isEnabled);
		
		if (isEnabled.equals("fas fa-chevron-down")) 
		{
			System.out.println("shareviewlet option is Enabled");
			Thread.sleep(2000);
			shareviewlet.click();
			
			//click on Share viewlet
			driver.findElement(By.xpath("//li[contains(.,'Share Viewlet')]")).click();
			Thread.sleep(12000);
		   		    
		    String viewletShare=driver.findElement(By.cssSelector(".info-shared")).getText();
			System.out.println("text:"+viewletShare);
			if(viewletShare.contains("Viewlet Shared:"))
			{
				System.out.println("viewlet is shared");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "working fine");
				
			}
			else
			{
				System.out.println("viewlet not shared");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed");
				driver.findElement(By.id("Sharing failed")).click();
			}
			//stop viewlet sharing
			driver.findElement(By.xpath("//button[contains(.,'Stop Sharing')]")).click();
			Thread.sleep(2000);
		}
		else
		{
			System.out.println("shareviewlet option is disbled"); 
		}
	}

	@Test(priority = 20)
	public void Logout() throws InterruptedException 
	{
		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);

	}
	public void SaveViewlet() throws InterruptedException
	{
		Thread.sleep(3000);
		WebElement Save = driver.findElement(By.xpath("//dialog[4]/ul/li[2]"));

		String isEnabled = Save.getAttribute("class");
		// System.out.println(isEnabled);

		if (!isEnabled.equals("disabled")) {
			System.out.println("Save option is Enabled");
			Save.click();
			Thread.sleep(6000);

			// Store the success message into string
			String Msg = driver.findElement(By.cssSelector(".message")).getText();
			System.out.println("Msg: "+ Msg);

			// Click on Confirmation OK button
			driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();

			if (Msg.contains("successfully")) {
				System.out.println("Save option is working fine");

				
			} else {
				System.out.println("Save option is not working");
									
			}

//			// Click on Viewlet Menu
//			driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[9]/i")).click();
//			Thread.sleep(1000);
		} else {
			System.out.println("Save option is disabled state");
		}
		
				Thread.sleep(5000);
			}
		
	

	public void CreateDashboard() throws InterruptedException {
		// Click on Plus Icon
		driver.findElement(By.xpath("//div[@id='pageContainer-tabs-add']/div/div/span")).click();

		// Give the dashboard Name
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/input")).sendKeys("EditViewlet");

		// uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label[2]/input")).click();
		Thread.sleep(1000);

		// select two columns 
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/ul/li[2]/div")).click();

		// Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
	}
	
	public void SaveDashboard(String DashboardName) throws InterruptedException
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

					}
				}
			
				
			}
			
		}
		Thread.sleep(2000);
		
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
