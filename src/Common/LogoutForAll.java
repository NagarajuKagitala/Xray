package Common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LogoutForAll 
{
	public void SaveLogout(WebDriver driver) throws InterruptedException
	{
		driver.findElement(By.cssSelector(".fa-power-off")).click();
		
		try
		{
			driver.findElement(By.id("logoutSaveBtn")).click();
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			driver.findElement(By.id("logoutYESBtn")).click();
			Thread.sleep(3000);
		}
		
		//Close the browser
		driver.close();
	}
	
	public void DontSaveLogout(WebDriver driver) throws InterruptedException
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
		
		//Close the browser
		driver.close();
	}

}
