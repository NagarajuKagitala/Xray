import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class dashbordOption {
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
	public void saveDashboard() throws InterruptedException
	{
			
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText("Test"));
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
						
						String isEnabled = save.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("save option is enabled");
							save.click();
							Thread.sleep(4000);
							
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
						}

					}
				}
			
				
			}
			
		}
		Thread.sleep(2000);
	}
	@Test(priority=2)
	public void SetDefultDashborad() throws InterruptedException
	{
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText("Test"));
				Actions ac1=new Actions(driver);
				ac1.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement setDefault=driver.findElement(By.xpath("//li[contains(.,'Set As Default')]"));
						
						String isEnabled = setDefault.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Set As Default enabled");
							setDefault.click();
							Thread.sleep(1000);
							
							//verification of setDefault	
						}
						else
						{
							System.out.println("Set As Default disabled");
						}
					}
						else
						{
							System.out.println("Set As Default not working");
						}
				}
			}
		}
		}
		
	@Test(priority=3)	
	public void closeTabOftheRight() throws InterruptedException
	{
	  this.CreateDashboard2();
	  List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText("Test"));
				Actions ac=new Actions(driver);
				ac.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement rightTab=driver.findElement(By.xpath("//li[contains(.,'Close tabs to the right')]"));
						
						String isEnabled = rightTab.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Close tabs to the right is enabled");
							rightTab.click();
							Thread.sleep(4000);
							
							// Store the success message into string
							String Msg = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg);
							
							// Click on Confirmation save button button
							driver.findElement(By.xpath("//footer/button[2]")).click();
							Thread.sleep(2000);
						}
						else
						{
							System.out.println("Close tabs to the right is enabled");
						}
					}
						else
						{
							System.out.println("Close tabs to the right is not working");
						}
					}
				}
			}
		
	}
	@Test(priority=4)
	public void closeTabOftheLeft() throws InterruptedException
	{
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText("Test"));
				Actions ac=new Actions(driver);
				ac.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement leftTab=driver.findElement(By.xpath("//li[contains(.,'Close tabs to the left')]"));
					
						String isEnabled = leftTab.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Close tabs to the left is enabled");
							leftTab.click();
							Thread.sleep(5000);
							
						// Store the success message into string
							String Msg = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg);
						
							// Click on Confirmation save button button
							driver.findElement(By.xpath("//footer/button[2]")).click();
							Thread.sleep(2000);
						}
						else
						{
							System.out.println("Close tabs to the left is disabled");
						}
					}
						else
						{
							System.out.println("Close tabs to the left is not working");
						}
					}
				}
			}
		List<WebElement> Afterdash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size After delete dashboard:"+Afterdash.size());
	}
	@Test(priority=5)
	public void closeAllTab() throws InterruptedException
	{
		List<WebElement> dash=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size:"+dash.size());
		for(WebElement e:dash)
		{
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				//click on dashboard
				WebElement elem=driver.findElement(By.linkText("Test"));
				Actions ac=new Actions(driver);
				ac.contextClick(elem).perform();
				Thread.sleep(3000);
				
				//save dashbaord
				List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
				for (WebElement e1 : myElements) 
				{
					if (e1.getAttribute("aria-hidden").toLowerCase().matches("false"))
					{
						WebElement OtherTab=driver.findElement(By.xpath("//li[contains(.,'Close other tabs')]"));
						
						String isEnabled = OtherTab.getAttribute("class");
						System.out.println(isEnabled);
						
						if (!isEnabled.equals("disabled"))
						{
							System.out.println("Close other tab is enabled");
							OtherTab.click();
							Thread.sleep(5000);
							
							// Store the success message into string
							String Msg = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg);														
							// Click on Confirmation save button button
							driver.findElement(By.xpath("//footer/button[2]")).click();
							Thread.sleep(2000);
							String Msg2 = driver.findElement(By.cssSelector(".message")).getText();
							System.out.println("Msg: "+ Msg2);
							driver.findElement(By.xpath("//footer/button[2]")).click();
						}
						else
						{
							System.out.println("Close other tab is disabled");
						}
					}
						else
						{
							System.out.println("Close other tab is not working");
						}
					}
				}
			}
		List<WebElement> allTab=driver.findElements(By.cssSelector("*[class^='tab-div']"));
		System.out.println("Size All tab:"+allTab.size());
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
	public void CreateDashboard2() throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
				
		//Give the dashboard Name
		driver.findElement(By.cssSelector("#createDashboard .input-field")).sendKeys("Abc");
				
		//uncheck initial viewlet checkbox
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/main/div/label/input")).click();
		
		//select two columns
		driver.findElement(By.xpath("//main/div/ul/li[2]/div")).click();
				
		//Click on Create button
		driver.findElement(By.xpath("//dialog[@id='createDashboard']/section/footer/div[2]/button")).click();
		Thread.sleep(2000);
	}
}
