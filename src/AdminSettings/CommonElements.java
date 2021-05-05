package AdminSettings;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommonElements 
{
	//static WebDriver driver;
	static By Menubar=By.cssSelector(".fa-bars");
	static By AdminSettings=By.xpath("//li[contains(.,' Admin Settings')]");
	static By cancel=By.cssSelector("#editSet .alert-btn"); 
	static By AdminCancel=By.xpath("//dialog[@id='adminWizard']/section/main/footer/div/button");
	static By Alerts=By.xpath("//li[contains(.,'Alerts')]");
	
	
	
	public static void MenubarIcon(WebDriver driver)
	{
		driver.findElement(Menubar).click();
	}
	
	public static void AdminSettingsOption(WebDriver driver)
	{
		driver.findElement(AdminSettings).click();
	}
	public static void cancel(WebDriver driver)
	{
		driver.findElement(cancel).click();
	}
	public static void AdminCancelButton(WebDriver driver)
	{
		driver.findElement(AdminCancel).click();
	}
	public static void AlertsOption(WebDriver driver)
	{
		driver.findElement(Alerts).click();
	}

}
