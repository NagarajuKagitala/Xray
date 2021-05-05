import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.Test;

public class Test_Import {
	WebDriver driver;
	@Test
	public void Login() throws InterruptedException
	{
	    System.setProperty("webdriver.chrome.driver", "F:\\Prashant\\automation\\JkoolTest\\Drivers\\chromedriver.exe");
	    driver = new ChromeDriver();
	    
	    driver.get("https://test.jkoolcloud.com/jKool/O_prashant/index.jsp");
		driver.manage().window().maximize();

	    
	    driver.findElement(By.id("Uname")).sendKeys("prashant");
	    driver.findElement(By.id("PWD")).sendKeys("prashant11");
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
	@Test(priority = 1)
	public void importData() throws InterruptedException, AWTException, IOException
	{
		//Click on Menu and select Import/export 
				driver.findElement(By.cssSelector(".icon")).click();
				driver.findElement(By.xpath("//dialog[@id='main-menu']/ul/li[3]")).click();
				Thread.sleep(5000);
				
				driver.findElement(By.cssSelector(".hasSubMenuOpen li:nth-child(1)")).click();
				Thread.sleep(5000);
				
				try
				{
					//Click on Check box
					driver.findElement(By.xpath("//aside/div/div/div/table/tbody/tr/td/input")).click();
				}
				
				catch (Exception e)
				{
					System.out.println("Source file is not present");
					
					//Click on Cancel button
					driver.findElement(By.cssSelector("#importsList .alert-btn")).click();
					Thread.sleep(2000);
					
					//Click on Import button
					driver.findElement(By.cssSelector("#importDataBtnNew > span")).click();
					Thread.sleep(2000);
					
					//Select the file xls
					//driver.findElement(By.xpath("//li/img")).click();
					
					//Click on choose file option
					driver.findElement(By.xpath("//input[@type='file']")).click();
					Thread.sleep(2000);
					//import
//					String filepath=System.getProperty("F:\\Prashant\\automation\\JkoolTest\\Imported Files\\ImportData.xls");
//					StringSelection stringselection=new StringSelection(filepath);
//					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
					StringSelection sel = new StringSelection("F:\\Prashant\\automation\\JkoolTest\\Imported Files\\ImportData.xls");
					Robot r=new Robot();
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
					r.keyPress(KeyEvent.VK_CONTROL);
					r.keyPress(KeyEvent.VK_V);
					r.keyRelease(KeyEvent.VK_CONTROL);
					r.keyRelease(KeyEvent.VK_V);
					Thread.sleep(2000);
					r.keyPress(KeyEvent.VK_ENTER);
					r.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(3000);
					
					TakesScreenshot srcshot=((TakesScreenshot)driver);
					File srcfile=srcshot.getScreenshotAs(OutputType.FILE);
				//	File destfile=new File("‪C:\\Users\\nastel2\\Desktop\\screenshot\\abc.png");
					FileHandler.copy(srcfile, new File("C:\\Users\\nastel2\\Desktop\\screenshot\\abc.png"));
					
					
	}
	}

}
