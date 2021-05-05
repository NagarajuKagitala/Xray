package MenuBar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommonElementsofMenu 
{
	static By Menubar=By.cssSelector(".fa-bars");
	static By view=By.xpath("//li[contains(.,' Viewlet')]");
	static By create=By.xpath("//span[contains(.,'Create')]");
	static By open=By.xpath("//span[contains(.,'Open')]");
	
	static By importexport=By.xpath("//span[contains(.,'Import / Export')]");
	static By data=By.xpath("//li[3]/ul/li");
	static By check=By.xpath("//tbody[@id='setDataTable']/tr/td/input");
	static By cancel=By.xpath("//dialog[@id='importWizard']/section/footer/div/button");
	static By viewlet=By.xpath("//span[contains(.,'Viewlets')]");
	static By dashboards=By.xpath("//span[contains(.,'Dashboards')]");
	static By sets=By.xpath("//span[contains(.,'Sets')]");
	static By Importexportcancel=By.cssSelector("#import-export-popup .default-btn"); 
	static By Import=By.cssSelector("#import-export-popup .float_right > .primary-btn:nth-child(1)");
	static By export=By.cssSelector("#import-export-popup .primary-btn:nth-child(2)");
	static By choosefile=By.xpath("//dialog[@id='import-export-popup']/section/main/aside/div/span/span/span/input");
	
	//Dashboard tab
	static By dashboardlink=By.xpath("//dialog[@id='main-menu']/ul/li[2]");
	
	public static void MenubarIcon(WebDriver driver)
	{
		driver.findElement(Menubar).click();
	}
	public static void Viewlet(WebDriver driver)
	{
		driver.findElement(view).click();
	}
	public static void CreateViewlet(WebDriver driver)
	{
		driver.findElement(create).click();
	}
	public static void OpenOption(WebDriver driver)
	{
		driver.findElement(open).click();
	}
	
	public static void ImportExportOption(WebDriver driver)
	{
		driver.findElement(importexport).click();
	}
	public static void DataOption(WebDriver driver)
	{
		driver.findElement(data).click();
	}
	public static void CheckBox(WebDriver driver)
	{
		driver.findElement(check).click();
	}
	public static void MenubarCancel(WebDriver driver)
	{
		driver.findElement(cancel).click();
	}
	public static void ViewletsOption(WebDriver driver)
	{
		driver.findElement(viewlet).click();
	}
	public static void DashboardsOption(WebDriver driver)
	{
		driver.findElement(dashboards).click();
	}
	public static void SetsOption(WebDriver driver)
	{
		driver.findElement(sets).click();
	}
	public static void ImportExportCancelOption(WebDriver driver)
	{
		driver.findElement(Importexportcancel).click();
	}
	public static void ImportButton(WebDriver driver)
	{
		driver.findElement(Import).click();
	}
	public static void ExportButton(WebDriver driver)
	{
		driver.findElement(export).click();
	}
	public static void ChooseFileButton(WebDriver driver)
	{
		driver.findElement(choosefile).click();
	}
	public static void DashboardLink(WebDriver driver)
	{
		driver.findElement(dashboardlink).click();
	}

}
