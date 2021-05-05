package Common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommonForAll 
{
	public void CreateDashboard(WebDriver driver, String DashboardName) throws InterruptedException
	{
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
		
		//Give the dashboard Name
		driver.findElement(By.cssSelector(".edit-name")).sendKeys(DashboardName);
				
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//label[2]/input")).click();
		Thread.sleep(1000);
		
		//select two columns
		driver.findElement(By.xpath("//main/div/ul/li[2]/div")).click();
				
		//Click on Create button   
		driver.findElement(By.xpath("//dialog[@id='create-dashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
	}
	
	public void CreateViewlet(WebDriver driver, String Query, String ViewletName) throws InterruptedException
	{
		driver.findElement(By.xpath("//div[@id='app-top-sidebar']/div[3]")).click();
		
		//Check the Create viewlet with JKQL check box
		boolean Jkql=driver.findElement(By.name("create-viewlet-open-close-option")).isSelected();
		                                       
		if(Jkql == true)
		{
			System.out.println("Check box is already selected");
		}
		else
		{
			driver.findElement(By.name("create-viewlet-open-close-option")).click();
			Thread.sleep(1000);
		}
		Thread.sleep(3000);
		
		//Click on Create viewlet  
		driver.findElement(By.id("createViewletBtn")).click();
		
		//Enter the Viewlet query
		driver.findElement(By.xpath("//main/div/jkql-input/textarea")).clear();
		driver.findElement(By.xpath("//main/div/jkql-input/textarea")).sendKeys(Query);
		Thread.sleep(2000);
		
		//Give viewlet name
		driver.findElement(By.cssSelector("#create-jkql-viewlet .viewlet-name")).clear();
		driver.findElement(By.cssSelector("#create-jkql-viewlet .viewlet-name")).sendKeys(ViewletName);
		
		//Click on Create button     
		driver.findElement(By.xpath("//dialog[@id='create-jkql-viewlet']/section/footer/button[2]")).click();
		Thread.sleep(6000);
	}

}
