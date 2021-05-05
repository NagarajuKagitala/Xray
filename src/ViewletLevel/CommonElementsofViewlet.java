package ViewletLevel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CommonElementsofViewlet 
{
	static By Create=By.xpath("//div[@id='viewlet-form-settings-wp']/div[11]/div/button");
	
	public static void CreateButton(WebDriver driver)
	{
		driver.findElement(Create).click();
	}

}
