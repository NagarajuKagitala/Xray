import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class ChangeViewletChart {
	WebDriver driver;
	static int Dashboardscount;
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
		this.CreateDashboard();
		Thread.sleep(2000);
		this.createViewlet();
		
		//List of dasj=hboards
				WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
						
				List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
				Dashboardscount=myElements.size();
				System.out.println("Dashboard count is: " +Dashboardscount);
    }
	@Test(priority=1)
	public void ChartTypes() throws InterruptedException
	{
		for(int i=1; i<=14; i++)
		{
			//click on chart icon on viewlet 
			driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[5]/i")).click();
			Thread.sleep(2000);
			
			//verify enabled chart type
		    WebElement Chart=driver.findElement(By.cssSelector("label:nth-child("+ i +") .fa-stack"));
			String ChartDisplay=Chart.getAttribute("class");
			System.out.println("ChartDisplay:"+ChartDisplay);
			
			if(ChartDisplay.contains("enabled"))
			{
				System.out.println("chart is enabled");
				String Title=Chart.getAttribute("title");
				Title=Title.replaceAll("\\s", ""); 
				System.out.println("chart type title is :" +Title);
				
				Chart.click();
				Thread.sleep(8000);
				try
				{
					driver.findElement(By.cssSelector("#viewlet-type-select-widget-popup > .close-button")).click();
				}
				catch(Exception e)
				{
					
				}
								
				String TreeQuery=driver.findElement(By.xpath("//div["+ Dashboardscount +"]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).getAttribute("title");
				System.out.println("Query is: " +TreeQuery);
				
				if(TreeQuery.toLowerCase().contains(Title.toLowerCase()))
				{
					System.out.println(""+Title+" Chart is working fine");
				}
				else
				{
					System.out.println(""+Title+" Chart is not working fine");
					//driver.findElement(By.id("Chart failed to load")).click();
				}
				
//				if(Title.equalsIgnoreCase("ColumnChart"))
//				{
//					if(TreeQuery.contains("colchart"))
//					{
//						System.out.println(""+Title+" Chart is working fine");
//					}
//					else
//					{
//						System.out.println(""+Title+" Chart is not working fine");
//						driver.findElement(By.id("Chart failed to load")).click();
//					}
//				}
//				else
//				{
//					if(TreeQuery.toLowerCase().contains(Title.toLowerCase()))
//					{
//						System.out.println(""+Title+" Chart is working fine");
//					}
//					else
//					{
//						System.out.println(""+Title+" Chart is not working fine");
//						driver.findElement(By.id("Chart failed to load")).click();
//					}
//				}
				
			} 
			else
			{
				System.out.println("chart is disabled");
				//close chart type  
				driver.findElement(By.cssSelector("#viewlet-type-select-widget-popup > .close-button")).click();
				Thread.sleep(2000);
			}
			Thread.sleep(1000);
			
		}		
	}
	public void CreateDashboard() throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
				
		//Give the dashboard Name
		driver.findElement(By.cssSelector("#createDashboard .input-field")).sendKeys("Test");
				
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label/input")).click();
		
		//select two columns
		driver.findElement(By.xpath("//main/div/ul/li[2]/div")).click();
				
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
	}
	public void createViewlet() throws InterruptedException
	{
		//Click on Viewlet button
				driver.findElement(By.xpath("//div[4]/div[3]")).click();
				
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
				
				//Click on Create button
				driver.findElement(By.id("createViewletBtn")).click();
				
				//Enter the query to field
				driver.findElement(By.xpath("//main/div/div/jkql-input/input")).sendKeys("get Event");
				
				//Enter the viewlet name
				driver.findElement(By.xpath("//main/div[2]/input")).clear();
				driver.findElement(By.xpath("//main/div[2]/input")).sendKeys("TestViewlet");
				
				//Click on Create button
				driver.findElement(By.xpath("//dialog[12]/section/footer/div[2]/input")).click();
				Thread.sleep(3000);					
	}
	
    }
