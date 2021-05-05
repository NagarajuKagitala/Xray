import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class test 
{

	WebDriver driver;
	@Test
	 public void Login() throws InterruptedException
	 {
	    System.setProperty("webdriver.chrome.driver", "F:\\Prashant\\automation\\JkoolTest\\Drivers\\chromedriver.exe");
	    driver = new ChromeDriver();
	    
	    driver.get("https://test.jkoolcloud.com/jKool/O_nagaraju/index.jsp");
		driver.manage().window().maximize();

	    
	    driver.findElement(By.id("Uname")).sendKeys("TestNewUser1");
	    driver.findElement(By.id("PWD")).sendKeys("test@123");
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
			Thread.sleep(8000);
		}
	 }
	
	
	@Test(priority=1)
	public void ChangePasswordFromMenu() throws InterruptedException
	{
		//Mouse hour 
		driver.findElement(By.cssSelector(".icon")).click();
		
		//Click on Dash board and Select Save button
		driver.findElement(By.cssSelector(".hasSubMenu:nth-child(5)")).click();
		Thread.sleep(4000);
		driver.findElement(By.cssSelector(".hasSubMenuOpen li:nth-child(2)")).click();
		Thread.sleep(6000);
		
		//chnage in and pwd
		driver.findElement(By.id("OLDPWD")).sendKeys("test@123");
		driver.findElement(By.id("PWD")).sendKeys("user@123");
		driver.findElement(By.id("CONFIRM")).sendKeys("user@123");
		Thread.sleep(1000);
		//click on submit
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(5000);
		
		this.logoutagin();
		Thread.sleep(2000);
		this.loginAgain();
		
	}
	public void logoutagin()
	{
		driver.close();
	}
	public void loginAgain() throws InterruptedException
	{
		System.setProperty("webdriver.chrome.driver", "F:\\Prashant\\automation\\JkoolTest\\Drivers\\chromedriver.exe");
	    driver = new ChromeDriver();
	    
	    driver.get("https://test.jkoolcloud.com/jKool/O_nagaraju/index.jsp");
		driver.manage().window().maximize();
		
		//login with new un and pwd
		driver.findElement(By.id("Uname")).sendKeys("TestNewUser1");
		driver.findElement(By.id("PWD")).sendKeys("test@123");
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
	}
	
	}
	
	



