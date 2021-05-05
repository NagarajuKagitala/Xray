import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class ViewletTest {
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
    }
	@Test(priority=1)
	public void TemproryViewlet() throws InterruptedException
	{
		//click on create temprory icon
		driver.findElement(By.xpath("//div[5]/div[2]/div/i")).click();
		Thread.sleep(2000);
		//enter query on temprory viewlet
		WebElement TempQuery=driver.findElement(By.xpath("(//input[@type='text'])[5]"));
		TempQuery.sendKeys("get event");
		Thread.sleep(2000);
		TempQuery.sendKeys(Keys.ENTER);
		Thread.sleep(3000);
	}
	@Test(priority=2)
	public void movetoDashboard() throws InterruptedException
	{
		//get viewlet name
		String ViewletName=driver.findElement(By.xpath("//span[contains(.,'Temporary viewlet')]")).getText();
		System.out.println("ViewletName:"+ViewletName);
		Thread.sleep(2000);
		//click on move to dashboard icon
		driver.findElement(By.cssSelector(".fa-eject")).click();
		Thread.sleep(3000);
		//verification of viewlet
		List<WebElement> myElements = driver.findElements(By.cssSelector("*[class^='tab-div']"));
		// System.out.println(myElements.size());
		StringBuilder buffer=new StringBuilder();
		for (WebElement e : myElements) 
		{
			// boolean str= e.getAttribute("aria-hidden");
			if (e.getAttribute("aria-hidden").toLowerCase().matches("false")) 
			{
				List<WebElement> ee = e.findElements(By.className("viewlet-title-name-div"));
				System.out.println("Viewlet size:" + ee.size());
				String GetViewletName="";
				for(WebElement innerele: ee)
				{
					GetViewletName = innerele.findElement(By.tagName("input")).getAttribute("value");
					//System.out.println("Viewlet name:" + GetViewletName);
					buffer.append(GetViewletName);
					buffer.append(',');
				}

			}
		}
		
		String ListOfViewletnames=buffer.toString();
		System.out.println("List of viewlets are: " +ListOfViewletnames);
		
		if(ListOfViewletnames.contains(ViewletName))
		{
			System.out.println("Viewlet is created successfully");
			
		}
		else
		{
			System.out.println("Viewlet is not created");
			
		}
		Thread.sleep(2000);
	}
	@Test(priority=3)
	public void TearOff() throws InterruptedException
	{
		//click on tear off icon on created viewlet
		driver.findElement(By.xpath("//li[2]/div/div[3]")).click();
		Thread.sleep(5000);
		
		java.util.Set<String> h=driver.getWindowHandles();
		String handle[]=new String[h.size()];
		int i=0;
		for(String s:h)
		{
			handle[i]=s;
			i++;
		}
		//switch from viewlet to dockViewlet
		driver.switchTo().window(handle[1]);
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println("URL of the Dock page:" +CurrentUrl);
		//Back to jkool
		driver.switchTo().window(handle[0]);
		Thread.sleep(2000);		
		
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
