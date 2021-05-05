package EditViewletFilterOptions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommonElementsofEditViewlet 
{
	static By Edit=By.xpath("//li[contains(.,'Edit Viewlet')]");
	static By preview=By.xpath("//button[contains(.,'Preview')]");
	static By cancel=By.cssSelector(".bottom-buttons-wp > .alert-btn"); 
	static By groupby=By.cssSelector(".group-by-block > .title > .fas");
	static By group=By.xpath("//div[@id='viewlet-form-settings-wp']/div[5]/div/div/div/span/span/span/span");
	static By datatype=By.xpath("//div[2]/div/span/span/span/span");
	                            
	public static void Editoption(WebDriver driver)
	{
		driver.findElement(Edit).click();
	}
	public static void PreviewButton(WebDriver driver)
	{
		driver.findElement(preview).click();
	}
	public static void CancelButton(WebDriver driver)
	{
		driver.findElement(cancel).click();
	}
	public static void GroupByPlusIcon(WebDriver driver)
	{
		driver.findElement(groupby).click();
	}
	public static void GroupByOption(WebDriver driver)
	{
		driver.findElement(group).click();
	}
	public static void SelectDatatype(WebDriver driver)
	{
		driver.findElement(datatype).click();
	}

}
