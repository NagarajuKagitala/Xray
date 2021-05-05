package AdminSettings;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
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
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
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
public class AdminSettings_BrandingLogos 
{
	static WebDriver driver;
	static String Screenshotpath;
	static String Importlogo;
	static String Message="";
	
	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();
		
		Screenshotpath =Settings.getScreenshotPath();
		Importlogo =Settings.getImportlogo();
	}
	
	//Login page
	@Test
	@Parameters({"sDriverPath", "sDriver" })
	public static void Browser(String sDriverPath, String sDriver) throws Exception {
		
		Settings.read();
		String sURL = Settings.getsURL();
				
		if(sDriver.equalsIgnoreCase("webdriver.chrome.driver"))
		{
			System.setProperty(sDriver, sDriverPath);
			driver= new ChromeDriver();
		}
		else if(sDriver.equalsIgnoreCase("webdriver.gecko.driver"))
		{
			System.setProperty(sDriver, sDriverPath);
			
		//	FirefoxOptions options = new FirefoxOptions();
		//	options.setCapability("marionette", false);
			driver = new FirefoxDriver();
			
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
		driver.get(sURL);	
	}
	
	@Parameters({})
	@Test(priority=1)
	public void Login() throws Exception
	{
		Settings.read();
		String sUsername=Settings.getsUsername();
		String sPassword=Settings.getsPassword();
		
		//Login Credentials
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(4000);
		
        //Check Landing page 
		if(driver.getPageSource().contains("Go to Dashboard"))
		{
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(25000);
		}
		else
		{
			System.out.println("Landing page is not present");
			Thread.sleep(6000);
		}
		
		//Click on Admin settings
		driver.findElement(By.cssSelector(".fa-bars")).click();
		driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);		
	}
	
	//-------------  Logos ------------------
	@Parameters({"Error"})
	@TestRail(testCaseId=351)
	@Test(priority=2)
	public void Faviconlogo(String Error, ITestContext context) throws InterruptedException, AWTException
	{	
		//Click on Favicon Upload button 
		driver.findElement(By.xpath("//td[3]/label")).click();
		Thread.sleep(5000);
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + Importlogo;
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
	    Thread.sleep(1000);
		
	    //Store the Error message into string 
		String Errormessage=driver.findElement(By.xpath("//aside[4]/table/tbody/tr/td[3]/span")).getText();
		System.out.println(Errormessage);
		
		if(Errormessage.equalsIgnoreCase(Error))
		{
			System.out.println("Favicon logo producing the correct error message");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Favicon logo not producing the correct error message");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Favicon Error msg failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"Error"})
	@TestRail(testCaseId=352)
	@Test(priority=3)
	public void Loginlogo(String Error,ITestContext context) throws InterruptedException, AWTException
	{
		//Click on Loginlogo upload button
		driver.findElement(By.xpath("//tr[2]/td[3]/label")).click();
		Thread.sleep(3000);
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + Importlogo;
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
	    Thread.sleep(1000);
		
	    //Store the Error message into string
		String Errormessage=driver.findElement(By.xpath("//tr[2]/td[3]/span")).getText();
		System.out.println(Errormessage);
		
		if(Errormessage.equalsIgnoreCase(Error))
		{
			System.out.println("Login logo producing the correct error message");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Login logo not producing the correct error message");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Login logo Error msg failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"Error"})
	@TestRail(testCaseId=353)
	@Test(priority=4)
	public void Applogo(String Error,ITestContext context) throws InterruptedException, AWTException
	{
		//Click on App logo upload button
		driver.findElement(By.xpath("//tr[3]/td[3]/label")).click();
		Thread.sleep(3000);
		
		//Loading a file from the load file option
		String filepath=System.getProperty("user.dir") + "\\" + Importlogo;
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
	    Thread.sleep(1000);
		
	    //Store the Error message into string
		String Errormessage=driver.findElement(By.xpath("//tr[3]/td[3]/span")).getText();
		System.out.println(Errormessage);
		
		if(Errormessage.equalsIgnoreCase(Error))
		{
			System.out.println("App logo producing the correct error message");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("App logo not producing the correct error message");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("App logo Error msg failed")).click();
		}
		Thread.sleep(2000);
	}
	
	@Parameters({"AppLogoURL", "Title"})
	@TestRail(testCaseId=354)
	@Test(priority=5)
	public void AppLogoLinkURL(String AppLogoURL, String Title,ITestContext context) throws InterruptedException
	{
		/*//App logo url
		driver.findElement(By.cssSelector("tr:nth-child(4) .input-field")).clear(); 
		driver.findElement(By.cssSelector("tr:nth-child(4) .input-field")).sendKeys(AppLogoURL);*/
		
		//Get the url 
		String Applogo=driver.findElement(By.xpath("//td[2]/input")).getAttribute("value");
		System.out.println("Applogo url is: " +Applogo);
		
		//Click on Test url link
		driver.findElement(By.cssSelector(".test-btn")).click();
		Thread.sleep(12000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//switch from Admin settings to link
		driver.switchTo().window(handle[1]);
		
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the page:" +CurrentUrl);
		
		if(Applogo.contains(CurrentUrl))
		{
			System.out.println("App logo link url is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("App logo link url is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");		
			
			driver.close();
			Thread.sleep(1000);
			
			//Back to jkool
			driver.switchTo().window(handle[0]);
			Thread.sleep(1000);
			driver.findElement(By.id("AppLogo Url failed")).click();
		}
		
		/*//Store the license page title into string
		String Licensepage=driver.findElement(By.cssSelector("h1:nth-child(3)")).getText();
		System.out.println(Licensepage);
		Assert.assertEquals(Title, Licensepage);*/
		
		driver.close();
		Thread.sleep(1000);
		
		//Back to jkool
		driver.switchTo().window(handle[0]);
		Thread.sleep(1000);
	}
	
	@Test(priority=6)
	@TestRail(testCaseId=355)
	public void DefaultNastelvaluesCheckbox(ITestContext context) throws InterruptedException
	{
		//Check the checkbox
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/label/span/input")).click();
		
		driver.findElement(By.xpath("//td[3]/label")).click();
		Thread.sleep(4000);
		
		//Verify Favicon Upload button
		boolean Favicon=driver.findElement(By.id("upload-photo")).isEnabled();
		System.out.println(Favicon);
		Assert.assertEquals(Favicon, false);
		Thread.sleep(1000);
		
		//Verify Loginlogo upload button
		boolean Loginlogo=driver.findElement(By.id("upload-photo")).isEnabled();
		System.out.println(Loginlogo);
		Assert.assertEquals(Loginlogo, false);
		Thread.sleep(1000);
		
		//Verify App logo upload button
		boolean Applogo=driver.findElement(By.id("upload-photo")).isEnabled();
		System.out.println(Applogo);
		Assert.assertEquals(Applogo, false);
		Thread.sleep(1000);
		
		//Verify Text field
		boolean Textfield=driver.findElement(By.cssSelector("td > .input-field")).isEnabled();
		System.out.println(Textfield);
		Assert.assertEquals(Textfield, false);
		Thread.sleep(1000);
		
		//UnCheck the checkbox
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[4]/div/label/span/input")).click();
		Thread.sleep(1000);		
	}
	
	//-------------- Login Page -----------
	
	@Test(priority=7)
	@TestRail(testCaseId=356)
	public void LoginPageDefaultValuesCheckbox(ITestContext context) throws InterruptedException
	{
		/*//Click on Branding 
		driver.findElement(By.cssSelector("//nav/ul/li/span")).click();
		Thread.sleep(2000);*/
		
		//Click on Sub option Login page 
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[2]")).click();
		Thread.sleep(2000);
		
		//Click on default value checkbox
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/div/label/span/input")).click();
		Thread.sleep(2000);
		
		//Verify Registration text field
		boolean Registration=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/table/tbody/tr[2]/td[2]/input")).isEnabled(); 
		System.out.println(Registration);
		
		if(Registration==true)
		{
			System.out.println("Registrion field is enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Registrion field is disabled");
		}
		Thread.sleep(1000);
		
		//Verify Registration url
		boolean RegistrationUrl=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/table/tbody/tr[3]/td[2]/input")).isEnabled();
		System.out.println(RegistrationUrl); 
		
		if(RegistrationUrl==true)
		{
			System.out.println("Registrion url field is enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Registrion url field is disabled");
		}
		
		Thread.sleep(1000);
		
		//Verify Terms of service url
		boolean TermsOfService=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/table/tbody/tr[4]/td[2]/input")).isEnabled();
		System.out.println(TermsOfService);
		
		if(TermsOfService==true)
		{
			System.out.println("Terms Of Service url field is enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Terms Of Service url field is disabled");
		}
		
		Thread.sleep(1000);
		
		//Verify Privacy url
		boolean PrivacyURL=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/table/tbody/tr[5]/td[2]/input")).isEnabled();
		System.out.println(PrivacyURL);
		
		if(PrivacyURL==true)
		{
			System.out.println("Privacy url field is enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Privacy URL field is disabled");
		}
		Thread.sleep(1000);
		
		//Verify Copyright text field
		boolean Copyright=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/table/tbody/tr[6]/td[2]/input")).isEnabled();
		System.out.println(Copyright);
		
		if(Copyright==true)
		{
			System.out.println("Copyright url field is enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Copyright URL field is disabled");
		}
		Thread.sleep(1000);
		
		//Verify ForgotPassword url
		boolean ForgotPasswordURL=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/table/tbody/tr[7]/td[2]/input")).isEnabled();
		System.out.println(ForgotPasswordURL);
		
		if(ForgotPasswordURL==true)
		{
			System.out.println("Forgot Password url field is enabled");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Forgot Password URL field is disabled");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);	
		
		//Uncheck the default checkbox
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/div/label/span/input")).click();
		Thread.sleep(2000);
	}
	
	@Test(priority=8)
	@TestRail(testCaseId=357)
	public void RegistrationTextCheckbox(ITestContext context) throws InterruptedException
	{
		//Click on Sub option Login page 
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[2]")).click();
		Thread.sleep(2000);
		
		//Click on default value checkbox
		boolean defaultcheckbox=driver.findElement(By.cssSelector(".align-checkbox")).isSelected();
		if(defaultcheckbox)
		{
			driver.findElement(By.cssSelector(".align-checkbox")).click();
		}
		else
		{
			
		}
		
		/*//Click on Registration text checkbox
		driver.findElement(By.xpath("//aside[6]/table/tbody/tr[2]/td/input")).click();*/
		
		//Verify Registration url
		boolean RegistrationUrl=driver.findElement(By.xpath("//tr[2]/td[2]/input")).isEnabled();
		System.out.println(RegistrationUrl);
		
		if(RegistrationUrl==true)
		{
			System.out.println("Registration text checkbox is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.cssSelector(".align-checkbox")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		else
		{
			System.out.println("Registration text checkbox is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		Thread.sleep(1000);
		
		//Check the checkbox
		driver.findElement(By.cssSelector(".align-checkbox")).click();
		Thread.sleep(2000);
		
	}
	
	
	@Parameters({"sUsername", "sPassword"})
	@Test(priority=9)
	@TestRail(testCaseId=358)
	public void RegistrationText(String sUsername, String sPassword,ITestContext context) throws Exception
	{
		/*//Click on default value checkbox
		boolean defaultcheckbox=driver.findElement(By.cssSelector(".align-checkbox")).isSelected();
		if(defaultcheckbox)
		{
			
		}
		else
		{
			driver.findElement(By.xpath("//tr[2]/td/input")).click();
		}*/
		
		//Store the Registration text field data into string
		String Resitrationdata=driver.findElement(By.xpath("//tr[2]/td[2]/input")).getAttribute("value");
		System.out.println("Registration data is: " +Resitrationdata);
		
		if(!Resitrationdata.equalsIgnoreCase(""))
		{
		
		//Close Admin settings page  
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer/div/button")).click();
		Thread.sleep(2000);
		this.Logout();
		
		//Get the registration data
		String Registration=driver.findElement(By.id("signup")).getText();
		System.out.println("Login page signup data is: " +Registration);
		
		if(Registration.contains(Resitrationdata))
		{
			System.out.println("Registration text is present");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Registration text is not present");
			this.Login();
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Registration text failed")).click();
		}
		
		this.Login();
		Thread.sleep(4000);
		}
		else
		{
			System.out.println("Registration data is empty");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		
	}
	
	@Test(priority=10)
	@TestRail(testCaseId=359)
	public void RegistrationURL(ITestContext context) throws InterruptedException
	{
		
		  //Click on Branding 
		 //driver.findElement(By.xpath("//nav/ul/li/span")).click();
		 
		
		//Click on Sub option Login page
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[2]")).click();
		Thread.sleep(2000);
		
		/*//Registration url
		driver.findElement(By.xpath("//tr[3]/td[2]/input")).clear();
		driver.findElement(By.xpath("//tr[3]/td[2]/input")).sendKeys(AppLogoURL);*/
		
		//Store the Registration URL text into string
		String Registrationurl=driver.findElement(By.xpath("//tr[4]/td[2]/input")).getAttribute("value");
		System.out.println("Registration url is: " +Registrationurl);
		
		if(!Registrationurl.equalsIgnoreCase(""))
		{
		//Click on Test Url
		driver.findElement(By.xpath("//tr[4]/td[3]/span")).click();
		Thread.sleep(10000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//get current url of the page
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the page:" +CurrentUrl);
		
		/*//Store the Registration page title into string
		String Registration=driver.findElement(By.cssSelector("h1:nth-child(3)")).getText();
		System.out.println(Registration);*/
		
		if(Registrationurl.equalsIgnoreCase(CurrentUrl))
		{
			System.out.println("Registration URL field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Registration URL field is not Working");
			
			driver.close();
			
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
			
		}
		driver.close();
		
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
		}
		else
		{
			System.out.println("Registration url field is empty");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		
	}
	
	@Test(priority=11)
	@TestRail(testCaseId=360)
	public void TermsOfServiceURL(ITestContext context) throws InterruptedException
	{
		//Get the URl and Store into string
		String TermsofServiceURl=driver.findElement(By.xpath("//tr[5]/td[2]/input")).getAttribute("value");
		System.out.println("Terms of service url is: " +TermsofServiceURl); 
		
		if(!TermsofServiceURl.equalsIgnoreCase(""))
		{
		//Click on Test URl link
		driver.findElement(By.xpath("//tr[5]/td[3]/span")).click();
		Thread.sleep(10000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//Store the current page url into string
		String url=driver.getCurrentUrl();
		//System.out.println(url);

		if(TermsofServiceURl.equalsIgnoreCase(url))
		{
			System.out.println("Terms of Service URL field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Terms of Service URL field is not Working");
			
			
			driver.close();
			
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("Not created")).click();
			
			
		}
		driver.close();
		
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
		}
		else
		{
			System.out.println("Terms url field is empty");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		
	}
	
	
	@Test(priority=12)
	@TestRail(testCaseId=361)
	public void PrivacyURL(ITestContext context) throws InterruptedException
	{
		//get the Privacy url into string
		String Privacyurl=driver.findElement(By.xpath("//tr[6]/td[2]/input")).getAttribute("value");
		System.out.println("Get privacy url: " +Privacyurl);
		
		if(!Privacyurl.equalsIgnoreCase(""))
		{
		//Click on test Url
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[6]/table/tbody/tr[6]/td[3]/span")).click();
		Thread.sleep(10000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		/*String PageTitle=driver.findElement(By.xpath("//h1")).getText();
		System.out.println("Title of the collectors URL page" +PageTitle);*/
		
		//Store the Privacy url into string
		String url=driver.getCurrentUrl();
		System.out.println("Reidrected page url: " +url);
		
		if(Privacyurl.equalsIgnoreCase(url))
		{
			System.out.println("Privacy URL field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Privacy URL field is not Working");
			
			driver.close();
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");			
			driver.findElement(By.id("Privacy url field failed")).click();
		}
		driver.close();	
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
		Thread.sleep(2000);
		}
		else
		{
			System.out.println("Privacy url is empty");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
	}
	
	@Test(priority=13)
	@TestRail(testCaseId=417)
	public void ForgotPasswordURL(ITestContext context) throws InterruptedException
	{
		//Store the forgot password url into string 
		String ForgaotPassword=driver.findElement(By.xpath("//tr[8]/td[2]/input")).getAttribute("value");
		System.out.println("Forgot password link is: " +ForgaotPassword);
		Thread.sleep(2000);
		
		//Click on Test URL link
		driver.findElement(By.xpath("//tr[8]/td[3]/span")).click();
		Thread.sleep(12000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//Store the Forgot password url into string
		String url=driver.getCurrentUrl();
								
		
		if(ForgaotPassword.equalsIgnoreCase(url))
		{
			System.out.println("Forgot Password URL field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Forgot Password URL field is not Working");
			
			driver.close();
			
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("Forgot password field failed")).click();
		}
		driver.close();
		
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
	}
	
	@Parameters({"sUsername", "sPassword"})
	@Test(priority=14)
	@TestRail(testCaseId=418)
	public void CopyRight(String sUsername, String sPassword, ITestContext context) throws Exception
	{
		//Store the Copy right data into string
		String Copyright=driver.findElement(By.xpath("//tr[7]/td[2]/input")).getAttribute("value");
		System.out.println(Copyright);
		
		//Click on close button
		driver.findElement(By.xpath("//main/footer/div/button")).click();
		Thread.sleep(2000);
		
		//Log out from the application
		this.Logout();
		Thread.sleep(2000);
		
		//Get the copyright data into string
		String Copyrightdata=driver.findElement(By.xpath("//div/div[2]")).getText();
		System.out.println("Copy right data is: " +Copyrightdata);
		
		if(Copyrightdata.contains(Copyright))
		{
			System.out.println("Copy right data is showing exactly");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Copy right data is not showing exactly");
			//Login
			this.Login();
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.id("Copy right data failed")).click();
		}
		
		//Login
		this.Login();
	}
	
	//-------------------- Landing Page -----------
	@Test(priority=15)
	@TestRail(testCaseId=419)
	public void LandingPageDefaultChechbox(ITestContext context) throws InterruptedException
	{
		/*//Click on Branding
		driver.findElement(By.cssSelector(".activeStyle")).click();
		Thread.sleep(1000);*/
		
		//Click on Landing page sub tab
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[3]")).click();
		Thread.sleep(2000);
		
		//Click on Default checkbox
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[7]/div/label/span/input")).click();
		
		//Verify Landing page text1 field
		boolean Landingpage1=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[7]/table/tbody/tr/td[2]/input")).isEnabled();
		System.out.println(Landingpage1);
		Assert.assertEquals(Landingpage1, false);
		Thread.sleep(1000);
		
		//Verify Landing page text2 field
		boolean Landingpage2=driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/aside[7]/table/tbody/tr[2]/td[2]/input")).isEnabled();
		System.out.println(Landingpage2);
		Assert.assertEquals(Landingpage2, false);
		Thread.sleep(1000);
	}
	
	@Parameters({"sUsername", "sPassword"})
	@Test(priority=16)
	@TestRail(testCaseId=420)
	public void LandingPageText(String sUsername, String sPassword,ITestContext context) throws InterruptedException 
	{
		//Click on Landing page sub tab
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[3]")).click();
		Thread.sleep(2000);
		
		//Uncheck the default checkbox
		boolean Defaultchecbox=driver.findElement(By.xpath("//span/input")).isSelected();
		if(Defaultchecbox)
		{
			driver.findElement(By.xpath("//span/input")).click();
		}
		
		//Store the Landing page text into strings
		String Text1=driver.findElement(By.xpath("//td[2]/input")).getAttribute("value");
		System.out.println(Text1);
		String Text2=driver.findElement(By.xpath("//tr[2]/td[2]/input")).getAttribute("value");
		System.out.println(Text2);
		//Thread.sleep(1000);
		
		//close the popup page
		driver.findElement(By.xpath("//main/footer/div/button")).click();
		Thread.sleep(1000);
		
		//Select landing page 
		driver.findElement(By.cssSelector(".fa-bars")).click();
		driver.findElement(By.xpath("//li[contains(.,' Landing Page')]")).click();
		Thread.sleep(20000);
		
		//Get the Login page data
		String Data1=driver.findElement(By.xpath("//p")).getText();
		System.out.println(Data1);
		String Data2=driver.findElement(By.xpath("//p[2]")).getText();
		System.out.println(Data2);
		Thread.sleep(2000);
		
		//Click on go to dashboard option
		driver.findElement(By.cssSelector(".to-dashboard")).click();
		Thread.sleep(10000);
				
		//Verification of fields
		if(Data1.equalsIgnoreCase(Text1) && Data2.equalsIgnoreCase(Text2))
		{
			System.out.println("Given saved data is showing");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Given saved data is not showing");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("Landing page text failed")).click();
		}	
	}
	
	//------------ Index page ------------------
	
	@Test(priority=17)
	@TestRail(testCaseId=421)
	public void IndexPageDefaultChechbox(ITestContext context) throws InterruptedException
	{
		//Click on Admin settings
		driver.findElement(By.cssSelector(".fa-bars")).click();
		driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);
				
		
		//Click on Branding
	/*	driver.findElement(By.cssSelector(".activeStyle")).click();*/
				
		//Click on Index page
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[4]")).click();
		Thread.sleep(1000);
		
		//Click on Default checkbox
		driver.findElement(By.xpath("//span/input")).click();
		
		//Verify help url
		boolean HelpURL=driver.findElement(By.xpath("//tr[2]/td[2]/input")).isEnabled();
		System.out.println(HelpURL);
		
		if(HelpURL==false)
		{
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
		
		//Verify Leave feedback url
		boolean LeaveFeedback=driver.findElement(By.xpath("//tr[3]/td[2]/input")).isEnabled();
		System.out.println(LeaveFeedback);
		if(LeaveFeedback==false)
		{
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
		
		//Verify Collectors url
		boolean Collectors=driver.findElement(By.xpath("//tr[4]/td[2]/input")).isEnabled();
		System.out.println(Collectors);
		
		if(Collectors==false)
		{
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
		
		//Verify License url
		boolean License=driver.findElement(By.xpath("//tr[5]/td[2]/input")).isEnabled();
		System.out.println(License);
		if(License==false)
		{
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}

		//Uncheck the checkbox
		driver.findElement(By.xpath("//span/input")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=18)
	@TestRail(testCaseId=422)
	public void LicenseURLCheckbox(ITestContext context) throws InterruptedException
	{
		//Click on Admin settings
		driver.findElement(By.cssSelector(".fa-bars")).click();
		driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
		Thread.sleep(2000);
		
		//Click on Index page
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[4]")).click();
		Thread.sleep(1000);
		
		//Uncheck the default checkbox
		boolean LicenseDefault=driver.findElement(By.cssSelector(".align-checkbox")).isSelected();
		if(LicenseDefault)
		{
			driver.findElement(By.cssSelector(".align-checkbox")).click();
		}
		
		//Verify License url
		boolean License=driver.findElement(By.xpath("//tr[5]/td[2]/input")).isEnabled();
		System.out.println(License);
		
		if(License==false)
		{
			System.out.println("Check box is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else	
		{
			System.out.println("Check box is not working");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.cssSelector(".align-checkbox")).click();
			driver.findElement(By.id("Checkbox failed")).click();
		}
		Thread.sleep(1000);
		
		//Uncheck the License checkbox
		driver.findElement(By.cssSelector(".align-checkbox")).click();
	}
	
	@Test(priority=19)
	@TestRail(testCaseId=423)
	public void HelpURL(ITestContext context) throws InterruptedException
	{
		/*//Check the License URL
		boolean License=driver.findElement(By.xpath("//tr[5]/td/input")).isEnabled();
		if(License)
		{
			
		}
		else
		{
			driver.findElement(By.xpath("//tr[5]/td/input")).click();
		}*/
		
		//get the help url into string
		String Helpurl=driver.findElement(By.xpath("//tr[2]/td[2]/input")).getAttribute("value");
		System.out.println("Help url is: " +Helpurl);
		
		//Click on test Url
		driver.findElement(By.xpath("//tr[2]/td[3]/span")).click();
		Thread.sleep(10000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//Get the url
		String Currenturl=driver.getCurrentUrl();
		System.out.println("Help url is: " +Currenturl);
		
		if(Currenturl.equalsIgnoreCase("https://customers.nastel.com/hc/en-us/categories/360002083773-Nastel-XRay"))
		{
			System.out.println("Help URL field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Help URL field is not Working");
			driver.close();
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			Thread.sleep(2000);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("Help url field failed")).click();
		}
		
		driver.close();
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
		Thread.sleep(2000);
		
	}
	
	@Test(priority=20)
	@TestRail(testCaseId=424)
	public void LeaveFeedback(ITestContext context) throws InterruptedException
	{
		//get the Leave feed back url into string
		String LeaveFeedbackurl=driver.findElement(By.xpath("//tr[3]/td[2]/input")).getAttribute("value");
		System.out.println("Leave feedback url is: " +LeaveFeedbackurl);
		
		//Click on test Url
		driver.findElement(By.xpath("//tr[3]/td[3]/span")).click();
		Thread.sleep(10000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		//Get the url
		String Currenturl=driver.getCurrentUrl();
		//System.out.println(Currenturl);
		
		/*//get the Heading and store into string
		String Heading=driver.findElement(By.xpath("//h1")).getText();
		System.out.println("Contact us page heading: " +Heading);*/
		
		if(LeaveFeedbackurl.equalsIgnoreCase(Currenturl))
		{
			System.out.println("Leave Feedback URL field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Leave Feedback URL field is not Working");
			
			driver.close();
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("Leave feed back url failed")).click();
		}
		
		driver.close();
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
		
	}
	
	@Parameters({"CollectorsURL"})
	@Test(priority=21)
	@TestRail(testCaseId=425)
	public void CollectorsURL(String CollectorsURL, ITestContext context) throws InterruptedException
	{
				
		//get the Collectors url into string
		String Collectorsurl=driver.findElement(By.xpath("//tr[4]/td[2]/input")).getAttribute("value");
		System.out.println("Collectors url is: " +Collectorsurl);
		
		//Click on test Url
		driver.findElement(By.xpath("//tr[4]/td[3]/span")).click();
		Thread.sleep(10000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		/*String PageTitle=driver.findElement(By.xpath("//h1")).getText();
		System.out.println("Title of the collectors URL page" +PageTitle);*/
		
		//Get the url
		String Currenturl=driver.getCurrentUrl();
		//System.out.println("currentUrl:"+Currenturl);
		
		if(Collectorsurl.equalsIgnoreCase(Currenturl) || CollectorsURL.contains("https://www.nastel.com/#collectors"))
		{
			System.out.println("Collectors URL field is Working fine");
			 context.setAttribute("Status",1);
			 context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Collectors URL field is not Working");
			
			driver.close();
			
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("Collectors url failed")).click();
		}
		driver.close();
		
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
		
	}
	

	@Test(priority=22)
	@TestRail(testCaseId=426)
	public void LicenseURL(ITestContext context) throws InterruptedException
	{
		/*//Send the url into field 
		driver.findElement(By.xpath("//tr[5]/td[2]/input")).clear();
		driver.findElement(By.xpath("//tr[5]/td[2]/input")).sendKeys(AppLogoURL);*/
		
		//get the License url into string
		String LicenseURL=driver.findElement(By.xpath("//tr[5]/td[2]/input")).getAttribute("value");
		System.out.println("License url is: " +LicenseURL);
		
		//Click on test Url
		driver.findElement(By.xpath("//tr[5]/td[3]/span")).click();
		Thread.sleep(10000);
				
		java.util.Set<String> h=driver.getWindowHandles();
		//System.out.print("no of handles"+h.size());
		
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
			
		//Switch to opened window
		driver.switchTo().window(handle[1]);
		
		/*//Get the url
		String Licenseheading=driver.findElement(By.xpath("//h1[2]")).getText();
		//System.out.println(Licenseheading);*/
		
		String url=driver.getCurrentUrl();
		
		/*String PageTitle=driver.findElement(By.xpath("//h1")).getText();
		System.out.println("Title of the License URL page" +PageTitle);*/
		
		if(LicenseURL.equalsIgnoreCase(url))
		{
			System.out.println("License URL field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("License URL field is not Working");
			
			driver.close();
			//Switch back to Admin settings page
			driver.switchTo().window(handle[0]);
			Thread.sleep(2000);
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			
			driver.findElement(By.id("License url failed")).click();
		}
		
		driver.close();
		//Switch back to Admin settings page
		driver.switchTo().window(handle[0]);
		Thread.sleep(2000);
		
	}
	
	//---------------------- Logout ---------------
	
	@Test(priority=23)
	@TestRail(testCaseId=427)
	public void LogoutPageDefaultChechbox(ITestContext context) throws InterruptedException
	{
		
		/*//Click on Branding
		driver.findElement(By.cssSelector(".activeStyle")).click();*/
				
		//Click on Logout page subtab
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[5]")).click();
		Thread.sleep(2000);
		
		//Click on Default checkbox
		driver.findElement(By.xpath("//span/input")).click();
		
		//Verify Logout message text
		boolean Logoutmessage=driver.findElement(By.xpath("//td[2]/input")).isEnabled();
		System.out.println(Logoutmessage);
		
		if(Logoutmessage==false)
		{
			System.out.println("Logout page default page is working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			System.out.println("Logout page default page is not working fine");
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			driver.findElement(By.xpath("//span/input")).click();
			driver.findElement(By.xpath("Not created")).click();
		}
		
		//Click on Default checkbox
		driver.findElement(By.xpath("//span/input")).click();
		Thread.sleep(1000);
	}
	
	@Test(priority=24)
	@TestRail(testCaseId=428)
	public void Logoutmessage(ITestContext context) throws InterruptedException
	{
		//Click on Logout page subtab
		driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/nav/ul/li/div/ul/li[5]")).click();
		Thread.sleep(2000);
							
		//get the Logout message into string
		String LogoutMessage=driver.findElement(By.xpath("//td[2]/input")).getAttribute("value");
		System.out.println(LogoutMessage);
		
		//Close the Admin settings window
		driver.findElement(By.xpath("//main/footer/div/button")).click();
		
		//Click on Logout icon
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		try
		{
			driver.findElement(By.id("logoutYESBtn")).click();
			//Thread.sleep(2000);
		}
		catch (Exception e)
		{
			driver.findElement(By.id("logoutSaveBtn")).click();
			//Thread.sleep(2000);
		}
		
		//System.out.println("page source is: " +driver.getPageSource());
		//Thread.sleep(3000);
		try
		{			
	    Message=driver.findElement(By.id("logout-message")).getText();
		System.out.println("message:" +Message);
		Thread.sleep(5000);
		}
		catch (Exception aa)
		{
			System.out.println("Not get the value");
		}
			
		
		if(LogoutMessage.equalsIgnoreCase(Message))
		{
			System.out.println("Logout message field is Working fine");
			context.setAttribute("Status",1);
			context.setAttribute("Comment", "working fine");
		}
		else
		{
			context.setAttribute("Status",5);
			context.setAttribute("Comment", "Failed");
			System.out.println("Logout message failed");
			driver.findElement(By.xpath("Not created")).click();
		}
		Thread.sleep(1000);
	
	}
	
	
	@Test(priority=25)
	public void Logout() throws InterruptedException
	{
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		
		try
		{
			driver.findElement(By.id("logoutDontSave")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			driver.findElement(By.id("logoutYESBtn")).click();
			Thread.sleep(3000);
		}
	}
	
	@Test(priority=26)
	public void Closebrowser()
	{
		// Closing the browser
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
