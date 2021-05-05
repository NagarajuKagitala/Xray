import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Analyze_Viewlet {
	WebDriver driver;
	static int Dashboardscount;
	
	@Test
	public void Login() throws InterruptedException
	{
	    System.setProperty("webdriver.chrome.driver", "Drivers/chromedriver.exe");
	    driver = new ChromeDriver();
	    
	    driver.get("https://test.jkoolcloud.com/jKool");
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
		Thread.sleep(2000);
		
		
		//List of dasj=hboards
		WebElement Header=driver.findElement(By.className("dashboard-header")).findElement(By.className("tabs-wrapper")).findElement(By.tagName("ul"));
				
		List<WebElement> myElements = Header.findElements(By.className("dashboard-tab"));
		Dashboardscount=myElements.size();
		System.out.println("Dashboard count is: " +Dashboardscount);	
		
		
    }
	@Test(priority=1)
	public void analyzeViewlet() throws InterruptedException
	{
		try {
		//click on multiple select checkbox
//		driver.findElement(By.xpath("//div[4]/div[2]/div/div[2]/ul/li/div[2]/div[3]/div/div/div[3]/div[2]/div/table/thead/tr/th/div/input")).click();
//		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[4]/div[2]/div/div[2]/ul/li/div[2]/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[2]/td/input")).click();
		driver.findElement(By.xpath("//div[4]/div[2]/div/div[2]/ul/li/div[2]/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[3]/td/input")).click();
		Thread.sleep(3000);
		
		//click on analyze option
		driver.findElement(By.cssSelector("#viewlet-context-menu li:nth-child(2)")).click();
		Thread.sleep(2000);
		
		//click on show elapsed time on analyze viewlet pop-up
		driver.findElement(By.cssSelector(".analyzePopupCreate > div > .largeSizeFont > input")).click();
		Thread.sleep(2000);
		
		//click on next
		driver.findElement(By.cssSelector(".next-btn")).click();
		//again click on next
		driver.findElement(By.cssSelector(".next-btn")).click();
		
		//click on all severity level
		driver.findElement(By.cssSelector(".leftMargin:nth-child(5) input")).click();
		
		//click on create 
		driver.findElement(By.cssSelector(".create-btn")).click();
		Thread.sleep(3000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@Test(priority=2)
	public void verifyCalnderDate() throws InterruptedException, ParseException
	{
       
		//click on first date range
		driver.findElement(By.xpath("//div[3]/div/div/div/input")).click();
		//select date on calender
		driver.findElement(By.linkText("5")).click();
		//click on done
		driver.findElement(By.cssSelector(".ui-datepicker-close")).click();
		
		String first_date_value = driver.findElement(By.xpath("//div[3]/div/div/div/input")).getAttribute("value");
		//System.out.println("First Selection Value = " +first_date_value);
		
			
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
	    SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM");
	    Date date1 = format1.parse(first_date_value);
	    String Date1=(format2.format(date1));
	    System.out.println("Date1:"+Date1);
	    
	    
		//click on second date range
		driver.findElement(By.xpath("//div/input[2]")).click();
		//select date on calender
		driver.findElement(By.linkText("16")).click();
		//click on done
		driver.findElement(By.cssSelector(".ui-datepicker-close")).click();
		
		String Second_date_value = driver.findElement(By.xpath("//div/input[2]")).getAttribute("value");
		//System.out.println("Second Selection Value = " +Second_date_value);
		
		
	    Date date2 = format1.parse(Second_date_value);
	    String Date2=(format2.format(date2));
	    System.out.println("Date2:"+Date2);
		
		//click on show button
		driver.findElement(By.xpath("//button[contains(.,'Show')]")).click();
		Thread.sleep(4000);
		
		//get data
		String data=driver.findElement(By.className("amcharts-stock-div")).getText();
		//System.out.println("data:"+data);
		
		//&& data.contains((CharSequence) date2)
		if(data.contains(Date1) && data.contains(Date2))
		{
			System.out.println("viewlet showing selected date");
		}
		else
		{
			System.out.println("viewlet not showing selected date");
		}
		Thread.sleep(2000);
        
	}
	@Test(priority=3)
	public void Today() throws InterruptedException, ParseException
	{
		//click on today bbutton
		driver.findElement(By.xpath("//button[contains(.,'Today')]")).click();
		Thread.sleep(5000);
		
		//copy calender value
		//first date value
		String first_date_value = driver.findElement(By.xpath("//div[3]/div/div/div/input")).getAttribute("value");
		//System.out.println("First Selection Value = " +first_date_value);
			
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
	    SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM");
	    Date date1 = format1.parse(first_date_value);
	    String Date1=(format2.format(date1));
	    System.out.println("Date1:"+Date1);
		
	    //second date value
	    String Second_date_value = driver.findElement(By.xpath("//div/input[2]")).getAttribute("value");
		//System.out.println("Second Selection Value = " +Second_date_value);
		
		
	    Date date2 = format1.parse(Second_date_value);
	    String Date2=(format2.format(date2));
	    System.out.println("Date2:"+Date2);
	    
	    //get data
	  	String data=driver.findElement(By.className("amcharts-stock-div")).getText();
	   // System.out.println("data:"+data);
	    
	    //verify
	    if(data.contains(Date1) && data.contains(Date2))
		{
			System.out.println("viewlet showing Today date");
		}
		else
		{
			System.out.println("viewlet not showing Today date");
		}
	    Thread.sleep(2000);
	    
	}
	@Test(priority=4)
	public void Days10() throws InterruptedException, ParseException
	{
		//click on 10 days button
		driver.findElement(By.xpath("//button[contains(.,'10 days')]")).click();
		Thread.sleep(5000);
		
		//copy calender value
		//first date value
		String first_date_value = driver.findElement(By.xpath("//div[3]/div/div/div/input")).getAttribute("value");
		//System.out.println("First Selection Value = " +first_date_value);
					
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM");
	    Date date1 = format1.parse(first_date_value);
	    String Date1=(format2.format(date1));
	    System.out.println("Date1:"+Date1);
				
	    //second date value
		String Second_date_value = driver.findElement(By.xpath("//div/input[2]")).getAttribute("value");
		//System.out.println("Second Selection Value = " +Second_date_value);
				
				
	    Date date2 = format1.parse(Second_date_value);
		String Date2=(format2.format(date2));
		System.out.println("Date2:"+Date2);
			    
	    //get data
		String data=driver.findElement(By.className("amcharts-stock-div")).getText();
	    //System.out.println("data:"+data);
			    
	    //verify
		if(data.contains(Date1) && data.contains(Date2))
		{
		    System.out.println("viewlet showing 10days date");
		}
		else
		{
			System.out.println("viewlet not showing 10days date");
		}
		Thread.sleep(2000);
	}
	@Test(priority=5)
	public void month1() throws InterruptedException, ParseException
	{
		//click on 1month button
		driver.findElement(By.xpath("//button[contains(.,'1 month')]")).click();
		Thread.sleep(5000);
		
		//copy calender value
		//first date value
		String first_date_value = driver.findElement(By.xpath("//div[3]/div/div/div/input")).getAttribute("value");
		//System.out.println("First Selection Value = " +first_date_value);
							
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM");
		Date date1 = format1.parse(first_date_value);
		String Date1=(format2.format(date1));
		System.out.println("Date1:"+Date1);
						
	    //second date value
		String Second_date_value = driver.findElement(By.xpath("//div/input[2]")).getAttribute("value");
	    //System.out.println("Second Selection Value = " +Second_date_value);
						
						
	    Date date2 = format1.parse(Second_date_value);
		String Date2=(format2.format(date2));
		System.out.println("Date2:"+Date2);
					    
	    //get data
		String data=driver.findElement(By.className("amcharts-stock-div")).getText();
		//System.out.println("data:"+data);
					    
	    //verify
		if(data.contains(Date1) && data.contains(Date2))
		{
			 System.out.println("viewlet showing 1months date");
		}
		else
		{
			 System.out.println("viewlet not showing 1months date");
		}
		Thread.sleep(2000);
	}
	@Test(priority=6)
	public void year1() throws InterruptedException, ParseException
	{
		//click on 1month button
		driver.findElement(By.xpath("//button[contains(.,'1 year')]")).click();
		Thread.sleep(5000);
		
		//copy calender value
		//first date value
		String first_date_value = driver.findElement(By.xpath("//div[3]/div/div/div/input")).getAttribute("value");
		System.out.println("First Selection Value = " +first_date_value);
							
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM");
		Date date1 = format1.parse(first_date_value);
		String Date1=(format2.format(date1));
		System.out.println("Date1:"+Date1);
						
	    //second date value
		String Second_date_value = driver.findElement(By.xpath("//div/input[2]")).getAttribute("value");
	    //System.out.println("Second Selection Value = " +Second_date_value);
						
						
	    Date date2 = format1.parse(Second_date_value);
		String Date2=(format2.format(date2));
		System.out.println("Date2:"+Date2);
					    
	    //get data
		String data=driver.findElement(By.className("amcharts-stock-div")).getText();
		//System.out.println("data:"+data);
					    
	    //verify
		if(data.contains(Date1) && data.contains(Date2))
		{
			 System.out.println("viewlet showing 1Year date");
		}
		else
		{
			 System.out.println("viewlet not showing 1Year date");
		}
		Thread.sleep(2000);
		//close analyze viewlet
		driver.findElement(By.xpath("//div[3]/div/div/ul/li/span")).click();
		Thread.sleep(2000);
	}
	@Test(priority=7)
	public void editAnalyzeViewlet() throws InterruptedException
	{
		String viewleteditxpath;
		int tabindex=0;
		driver.findElement(By.xpath("//div[4]/div[2]/div/div[2]/ul/li/div[2]/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[2]/td/input")).click();
		driver.findElement(By.xpath("//div[4]/div[2]/div/div[2]/ul/li/div[2]/div[3]/div/div/div[3]/div[3]/div/table/tbody/tr[3]/td/input")).click();
		Thread.sleep(3000);
		
		//click on analyze option
		driver.findElement(By.cssSelector("#viewlet-context-menu li:nth-child(2)")).click();
		Thread.sleep(2000);
		
		//click on show elapsed time on analyze viewlet pop-up
		driver.findElement(By.cssSelector(".analyzePopupCreate > div > .largeSizeFont > input")).click();
		Thread.sleep(2000);
		
		//click on next
		driver.findElement(By.cssSelector(".next-btn")).click();
		//again click on next
		driver.findElement(By.cssSelector(".next-btn")).click();
		
		//click on all severity level
		driver.findElement(By.cssSelector(".leftMargin:nth-child(5) input")).click();
		
		//click on create 
		driver.findElement(By.cssSelector(".create-btn")).click();
		Thread.sleep(3000);
		
		//click on edit viewlet 
		driver.findElement(By.xpath("//div[2]/li/div/div/div[2]/div[6]/i")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//li[contains(.,'Edit Viewlet')]")).click();
		Thread.sleep(2000);
		
		
		//checkox 
		boolean checkbox=driver.findElement((By.cssSelector(".analyzePopupCreate > div > .largeSizeFont > input"))).isSelected();
		if(checkbox==true)
		{
			System.out.println("checkbox is already selectrd");
		}
		else
		{
			System.out.println("checkbox is not selected");
			driver.findElement((By.cssSelector(".analyzePopupCreate > div > .largeSizeFont > input"))).click();
		}
		//click on next
		driver.findElement(By.cssSelector(".next-btn")).click();
		
		//again click on next
		driver.findElement(By.cssSelector(".next-btn")).click();
		
		//click on only volume
		driver.findElement(By.xpath("(//input[@name='analyze-wizard-analyze-mode'])[3]")).click();
		
		//click on update
		driver.findElement(By.cssSelector(".create-btn")).click();
		Thread.sleep(3000);
		
		//verify
		//get data
		String data=driver.findElement(By.className("amcharts-stock-div")).getText();
		//System.out.println("data:"+data);
		
		if(data.contains("Event")||data.contains("Activity"))
		{
			System.out.println("edit viewlet working fine");
		}
		else
		{
			System.out.println("edit viewlet not working");
		}
	}
	
	public void CreateDashboard() throws InterruptedException
	{
		//Click on Plus Icon
		driver.findElement(By.xpath("//span[contains(.,'+')]")).click();
				
		//Give the dashboard Name
		driver.findElement(By.cssSelector("#createDashboard .input-field")).sendKeys("AnalyzeDashboard");
				
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
		driver.findElement(By.xpath("//main/div/div/jkql-input/input")).sendKeys("get Event show as Table");
		
		//Enter the viewlet name
		driver.findElement(By.xpath("//main/div[2]/input")).clear();
		driver.findElement(By.xpath("//main/div[2]/input")).sendKeys("AnalyzeViewlet");
		
		//Click on Create button
		driver.findElement(By.xpath("//dialog[12]/section/footer/div[2]/input")).click();
		Thread.sleep(15000);					
	}

}
