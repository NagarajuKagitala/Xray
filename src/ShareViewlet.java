import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class ShareViewlet {
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
		this.CreateDashboard();
		Thread.sleep(2000);
		this.createViewlet();
    }
	@Test(priority=1)
	public void shareviewlet() throws InterruptedException
	{
		this.SaveViewlet();
		Thread.sleep(2000);
		
		//click on Share viewlet
		driver.findElement(By.xpath("//li[contains(.,'Share Viewlet')]")).click();
		Thread.sleep(4000);
		
		//click on share viewlet button
		driver.findElement(By.xpath("//button[contains(.,'Share')]")).click();
		Thread.sleep(2000);
		
		//verification of share viewlet
		WebElement shareviewlet=driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[8]/i"));
		
		String isEnabled = shareviewlet.getAttribute("class");
		//System.out.println(isEnabled);
		
		if (isEnabled.equals("fa fa-share-alt")) 
		{
			System.out.println("shareviewlet option is Enabled");
			shareviewlet.click();
		    Thread.sleep(4000);
		String viewletShare=driver.findElement(By.cssSelector(".info-shared")).getText();
		System.out.println("text:"+viewletShare);
		
		if(viewletShare.equals("Viewlet Shared"))
		{
			System.out.println("viewlet is shared");
		}
		else
		{
			System.out.println("viewlet is shared");
		}
		//stop viewlet sharing
		driver.findElement(By.xpath("//button[contains(.,'Stop Sharing')]")).click();
		Thread.sleep(1000);
		}
		else
		{
			System.out.println("shareviewlet option is disbled"); 
		}
	}
	public void SaveViewlet() throws InterruptedException
	{
		Thread.sleep(3000);
		//save viewlet
		int i=0;

		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		for (WebElement e : myElements) {
			i++;
			System.out.println("i: "+ i);
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) {
				
				// Click on Viewlet Menu icon
				// Thread.sleep(4000);//div[8]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[9]/i
				driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[9]/i")).click();
				Thread.sleep(2000);
				// Select Save option
							
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

					// Click on Viewlet Menu
					driver.findElement(By.xpath("//div["+  i +"]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[9]/i")).click();
					Thread.sleep(1000);
				} else {
					System.out.println("Save option is disabled state");
				}
				Thread.sleep(5000);
				
			}
			
		}

		
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
				driver.findElement(By.xpath("//main/div/div/jkql-input/input")).sendKeys("get Activity");
				
				//Enter the viewlet name
				driver.findElement(By.xpath("//main/div[2]/input")).clear();
				driver.findElement(By.xpath("//main/div[2]/input")).sendKeys("TestViewlet");
				
				//Click on Create button
				driver.findElement(By.xpath("//dialog[12]/section/footer/div[2]/input")).click();
				Thread.sleep(3000);				
		
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
    }
