import java.security.acl.Group;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class Date_andTime {
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

	/*
	 * @Test(priority = 1) public void unspecified() throws InterruptedException {
	 * //click on set date and time driver.findElement(By.xpath(
	 * "//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[6]/i")).click();
	 * Thread.sleep(1000);
	 * 
	 * 
	 * List<WebElement>
	 * dropbox=driver.findElements(By.id("select2-ui-id-6-container"));
	 * 
	 * for(WebElement d:dropbox) { String text=d.getText();
	 * System.out.println("text:"+text);
	 * 
	 * if(text.equals("Unspecified")) { System.out.println("Unspecified selected");
	 * //click on save driver.findElement(By.
	 * cssSelector("#viewlet-date-range-select-popup .float_right > .default-btn")).
	 * click(); Thread.sleep(2000); } else { //click on close driver.findElement(By.
	 * cssSelector("#viewlet-date-range-select-popup .float_left > .default-btn")).
	 * click(); Thread.sleep(1000); } }
	 * 
	 * }
	 */
/*	@Test(priority = 2)
	public void predefined() throws InterruptedException
	{
		//click on set date and time
		driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[6]/i")).click();
		Thread.sleep(1000);
		
			
		
			String data = ".//*[contains(@id," + "'" + "Predefined" + "'" + ")]";
			driver.findElement(By.id("select2-ui-id-6-container")).click();
			driver.findElement(By.xpath(data)).click();
			Thread.sleep(1000);
			
			String[] Group= {"This Hour","Last Hour","This Week","Last Week","This Month","Last Month","This Year","Last Year"};
			for(int i=0;i<Group.length;i++)
			{
				//System.out.println(Group.length);
				//select second drop down value
				String Seconddata = ".//*[contains(@id," + "'" + Group[i] + "'" + ")]";
				driver.findElement(By.id("select2-ui-id-7-container")).click();
				driver.findElement(By.xpath(Seconddata)).click();
				Thread.sleep(1000);
				
				//click on save 
				driver.findElement(By.cssSelector("#viewlet-date-range-select-popup .float_right > .default-btn")).click();
				Thread.sleep(3000);
				//verify
				String Query=driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).getAttribute("title");
				System.out.println(Query);
				Thread.sleep(2000);
				
				System.out.println("group value:"+ Group[i]);
				
				if(Query.contains(Group[i]))
				{
					System.out.println("change predefined working fine");
				}
				else
				{
					System.out.println("change predefined not working fine");
				}
				
				//click on save 
				driver.findElement(By.cssSelector("#viewlet-date-range-select-popup .float_right > .default-btn")).click();
				Thread.sleep(3000);
    }
		
	}*/
/*	@Test(priority = 2)	
	public void custom() throws InterruptedException
	{
		//click on set date and time
		driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[6]/i")).click();
		Thread.sleep(1000);
		
		String data = ".//*[contains(@id," + "'" + "Custom" + "'" + ")]";
		driver.findElement(By.id("select2-ui-id-6-container")).click();
		driver.findElement(By.xpath(data)).click();
		Thread.sleep(1000);
		
		String[] group= {"Earliest","Latest"};
		String[] units= {"Minute","Hour","Day","Week","Month","Year"};
		for(int i=0;i<group.length;i++)
		{
			String Seconddata = ".//*[contains(@id," + "'" + group[i] + "'" + ")]";
			driver.findElement(By.id("select2-ui-id-8-container")).click();
			driver.findElement(By.xpath(Seconddata)).click();
			System.out.println("in");
			Thread.sleep(2000);
			for(int j=0;j<units.length;j++)
			{
				String unitddata = ".//*[contains(@id," + "'" + units[j] + "'" + ")]";
				driver.findElement(By.id("select2-ui-id-9-container")).click();
				driver.findElement(By.xpath(unitddata)).click();
				Thread.sleep(2000);
				
				//click on save 
				driver.findElement(By.cssSelector("#viewlet-date-range-select-popup .float_right > .default-btn")).click();
				Thread.sleep(3000);
				//verify
				String Query=driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).getAttribute("title");
				System.out.println(Query);
				Thread.sleep(2000);
				System.out.println("unit value:"+units[j]);
				//verify
				if(Query.contains(units[j]))
				{
					System.out.println("custom working fine");
				}
				else
				{
					System.out.println("custom is not working");
				}
				//click on set date and time
				driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[6]/i")).click();
				Thread.sleep(2000);
			}
		}
		
	}*/
	@Test(priority = 2)
	public void DateRange() throws InterruptedException
	{
		//click on set date and time
		driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div[2]/div[6]/i")).click();
		Thread.sleep(1000);
		
		String data = ".//*[contains(@id," + "'" + "Date range" + "'" + ")]";
		driver.findElement(By.id("select2-ui-id-6-container")).click();
		driver.findElement(By.xpath(data)).click();
		Thread.sleep(1000);
		
		//click on first calender box
		driver.findElement(By.xpath("//dialog[@id='viewlet-date-range-select-popup']/section/main/div/div/div/div/div[4]/div/input")).click();
		//click on calender date
		driver.findElement(By.xpath("//tr[5]/td[4]/a")).click();
		
		driver.findElement(By.cssSelector(".ui-datepicker-close")).click();
		Thread.sleep(3000);
		//<input class="input-field datepickerIcon hasDatepicker" data-bind="datetimepicker: from, datetimepickerOptions: {lowestUnit: 'second' , timeFormat: 'HH:mm:ss'}" id="dp1580377712735" title="">
		
		
		String firstDate=driver.findElement(By.xpath("//*[@id=\"viewlet-date-range-select-popup\"]/section/main/div/div/div/div/div[4]/div[1]")).getText();
		System.out.println("firstDate:"+firstDate);
		Thread.sleep(2000);
		//click on second claender box
		driver.findElement(By.xpath("//dialog[@id='viewlet-date-range-select-popup']/section/main/div/div/div/div/div[4]/div[2]/input")).click();
		//click on calnder date
		driver.findElement(By.xpath("//tr[5]/td[5]/a")).click();
		
		driver.findElement(By.cssSelector(".ui-datepicker-close")).click();
		Thread.sleep(2000);
		
//		String seconddate=driver.findElement(By.xpath("//*[@id=\"dp1580471901615\"]")).getAttribute("class");
//    	System.out.println("seconddate:"+seconddate);
//		Thread.sleep(2000);
		//click on save 
		driver.findElement(By.cssSelector("#viewlet-date-range-select-popup .float_right > .default-btn")).click();
		Thread.sleep(2000);
		//verify
		String Query=driver.findElement(By.xpath("//div[2]/div[2]/div/div[2]/ul/li/div[2]/div/div/jkql-input/input")).getAttribute("title");
		System.out.println(Query);
				
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
				driver.findElement(By.xpath("//main/div/div/jkql-input/input")).sendKeys("get number of Event Show As Table");
				
				//Enter the viewlet name
				driver.findElement(By.xpath("//main/div[2]/input")).clear();
				driver.findElement(By.xpath("//main/div[2]/input")).sendKeys("TestViewlet");
				
				//Click on Create button
				driver.findElement(By.xpath("//dialog[12]/section/footer/div[2]/input")).click();
				Thread.sleep(3000);					
	}

    }
