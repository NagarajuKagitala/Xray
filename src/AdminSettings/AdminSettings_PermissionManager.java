package AdminSettings;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Common.LogoutForAll;
import testrail.Settings;
import testrail.TestClass;
import testrail.TestRail;
import testrail.TestRailAPI;

@Listeners(TestClass.class)
public class AdminSettings_PermissionManager {
	static WebDriver driver;
	static String Screenshotpath;
	static String TeamName;
	static String RepoName;

	@BeforeTest
	public void beforeTest() throws Exception {
		System.out.println("BeforeTest");
		Settings.read();

		Screenshotpath = Settings.getScreenshotPath();
		TeamName = Settings.getTeamName();
		RepoName = Settings.getRepoName();
	}

	// Login page
	@Test
	@Parameters({ "sDriverPath", "sDriver" })
	public static void Login(String sDriverPath, String sDriver) throws Exception {

		Settings.read();
		String sURL = Settings.getsURL();
		String sUsername = Settings.getsUsername();
		String sPassword = Settings.getsPassword();

		if (sDriver.equalsIgnoreCase("webdriver.chrome.driver")) {
			System.setProperty(sDriver, sDriverPath);
			driver = new ChromeDriver();
		} else if (sDriver.equalsIgnoreCase("webdriver.gecko.driver")) {
			System.setProperty(sDriver, sDriverPath);

		//	FirefoxOptions options = new FirefoxOptions();
		//	options.setCapability("marionette", false);
			driver = new FirefoxDriver();

		} else if (sDriver.equalsIgnoreCase("webdriver.ie.driver")) {
			System.setProperty(sDriver, sDriverPath);
			driver = new InternetExplorerDriver();
		} else {
			System.setProperty(sDriver, sDriverPath);
			driver = new EdgeDriver();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// Login Credentials
		driver.get(sURL);
		driver.findElement(By.id("Uname")).sendKeys(sUsername);
		driver.findElement(By.id("PWD")).sendKeys(sPassword);
		driver.findElement(By.id("Submit")).click();
		Thread.sleep(4000);

		// Check Landing page
		if (driver.getPageSource().contains("Go to Dashboard")) {
			driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
			Thread.sleep(15000);
		} else {
			System.out.println("Landing page is not present");
			Thread.sleep(6000);
		}

	}

	@Parameters({"UserName", "FirstName", "LastName", "Email", "Password", "ConfirmPassword","TeamName"})
	@Test(priority = 1)
	@TestRail(testCaseId = 374)
	public static void CreateUser(String UserName, String FirstName, String LastName, String Email, String Password, String ConfirmPassword,String TeamName, ITestContext context) throws InterruptedException {
		try {
			// Click on Admin settings
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			WebDriverWait wait = new WebDriverWait(driver, 20);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(2000);

			// Click on create new users
			driver.findElement(By.xpath("//li[contains(.,'Create New Users')]")).click();
			Thread.sleep(1000);

			// Click on next button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/div[2]/button[2]")).click();
			Thread.sleep(1000);

			// Enter username
			driver.findElement(By.id("userName")).clear();
			driver.findElement(By.id("userName")).sendKeys(UserName);
			Thread.sleep(1000);

			// Enter firstname
			driver.findElement(By.id("firstName")).sendKeys(FirstName);
			Thread.sleep(1000);

			// Enter lastname
			driver.findElement(By.id("lastName")).sendKeys(LastName);
			Thread.sleep(1000);

			// Enter email
			driver.findElement(By.id("email")).sendKeys(Email);
			Thread.sleep(1000);

			// Enter password
			driver.findElement(By.id("password")).sendKeys(Password);
			Thread.sleep(2000);

			// Enter confirm password
			driver.findElement(By.id("confirmPassword")).sendKeys(ConfirmPassword);
			Thread.sleep(8000);

			// Click on next button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/div[2]/button[2]")).click();
			Thread.sleep(4000);

			// Search with team name
			driver.findElement(By.cssSelector(".input-search-field")).sendKeys(TeamName);

			// Select team
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[6]/main/aside[3]/div/div/div/table/tbody/tr/td/input")).click();
			Thread.sleep(4000);

			// Click on next button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/div[2]/button[2]")).click();
			Thread.sleep(4000);

			// Click finish button
			driver.findElement(By.xpath("//button[contains(.,'Finish')]")).click();
			Thread.sleep(10000);

			// Verify created user availability
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[3]")).click();
			Thread.sleep(6000);

			// Search user text
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div/input")).clear();
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div/input")).sendKeys(UserName);
			Thread.sleep(5000);

			String verifyname = driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div[2]/div/table/tbody/tr/td")).getText();
			System.out.println("List of users are: " + verifyname); 

			String Totalusers = driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div[2]")).getText();

			if (verifyname.equals(UserName) || Totalusers.contains(UserName)) {

				System.out.println("User created");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "User created successfully under permissions tab");

				// Click on Goback button
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
				Thread.sleep(10000);

				WebElement we;
				we = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")));
				we.click();
				Thread.sleep(2000);

			} else {
				System.out.println("User creation failed");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to create user under permissions tab");

				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
				Thread.sleep(10000);

				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("Failed click")).click();
			}

		} catch (Exception e) {
			// TODO: handle exception
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Got an exception while creating user under permissions tab, check details: " + e.getMessage());
			driver.findElement(By.xpath("Failed click")).click();
			Thread.sleep(2000);
		}
	}

	@Parameters({"UserName", "Password"})
	@Test(priority = 2)
	@TestRail(testCaseId = 375)
	public void CheckOrganizationDetails(String UserName, String Password, ITestContext context) {
		try {
			
			Settings.read();
			String sUsername = Settings.getsUsername();
			String sPassword = Settings.getsPassword();
			
			// Click on Admin settings
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(4000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(2000);

			
			// Read user data from usersrole table
			WebElement wbele = driver.findElement(By.className("usersRoleTable")).findElement(By.tagName("tbody"));

			// Read inner html elements
			List<WebElement> lst = wbele.findElements(By.tagName("tr"));
			// System.out.println(lst.size());
			// looping elements
			for (WebElement e : lst) {
				System.out.println("username: " + e.findElement(By.tagName("td")).getText());
				String str = e.findElement(By.tagName("td")).getText();
				// Check currently added user exits in table

				if (str.equals(UserName)) {
					List<WebElement> lstroles = e.findElements(By.className("toggle-btn"));

					// System.out.println(lstroles.size());
					// Read roles block on the user row
					for (WebElement ele : lstroles) {

						// System.out.println("HTML: "+ ele.getText());

						String userrole = ele.getAttribute("title");
						System.out.println("userrole: " + userrole);

						// Change user to admin role
						if (userrole.equals("Admin")) {
							ele.click();
							// System.out.println("In");

							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
							Thread.sleep(10000);

							driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
							Thread.sleep(2000);
							
							//Click on Logout icon
							driver.findElement(By.cssSelector(".fa-power-off")).click();
							try
							{
								driver.findElement(By.id("logoutSaveBtn")).click();
							}
							catch (Exception e1)
							{
								driver.findElement(By.id("logoutYESBtn")).click();
								Thread.sleep(2000);
							}

							
							} else {
								
								driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
								Thread.sleep(10000);

								driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
								Thread.sleep(2000);
								
								//Click on Logout icon
								driver.findElement(By.cssSelector(".fa-power-off")).click();
								try
								{
									driver.findElement(By.id("logoutSaveBtn")).click();
								}
								catch (Exception e1)
								{
									driver.findElement(By.id("logoutYESBtn")).click();
									Thread.sleep(2000);
								}
							}

							Thread.sleep(3000);

							// Login with new user credentials
							driver.findElement(By.id("Uname")).sendKeys(UserName);
							driver.findElement(By.id("PWD")).sendKeys(Password);
							driver.findElement(By.id("Submit")).click();
							Thread.sleep(4000);

							// Check Landing page
							if (driver.getPageSource().contains("Go to Dashboard")) {
								driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
								Thread.sleep(10000);
								System.out.println("User added successfully with admin permissions");
								
								//Close the dashboard popup if present
								try
								{
									driver.findElement(By.cssSelector("#openDashboard .alert-btn")).click();
								}
								catch (Exception ee1)
								{
									System.out.println("No popup is present");
								}

								// Verifying repository under organization

								// Click on Admin settings
								driver.findElement(By.cssSelector(".icon")).click();
								driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
								Thread.sleep(4000);

								// Click on permissions
								driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
								Thread.sleep(2000);

								// Click on organization tab
								driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
								Thread.sleep(1000);

								// Read user data from usersrole table
								WebElement werepo = driver.findElement(By.className("repoTable")).findElement(By.tagName("tbody"));

								// Read inner html elements
								List<WebElement> lstrepo = werepo.findElements(By.tagName("tr"));
								// System.out.println("repo list size: " + lstrepo.size());

								String strrepotxt = lstrepo.get(0).findElement(By.tagName("td")).getText();
								System.out.println("Repo text: " + strrepotxt);

								List<WebElement> lstreporoles = lstrepo.get(0).findElements(By.className("toggle-btn"));

								// System.out.println(lstreporoles.size());
								// Read roles block on the user row
								for (WebElement wele : lstreporoles) {

									String reporole = wele.getAttribute("title");
									// System.out.println(reporole);

									// Change user to admin role
									if (reporole.equals("Inactive")) {
										wele.click();
										Thread.sleep(1000);

										// Verify repository exits or not
										driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
										Thread.sleep(1000);

										driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
										Thread.sleep(2000);

										// Logout and login using organization user
										driver.findElement(By.cssSelector(".fa-power-off")).click();
										Thread.sleep(2000);

										if (driver.findElement(By.id("logoutSaveBtn")).isDisplayed()) {
											driver.findElement(By.id("logoutSaveBtn")).click();
											Thread.sleep(2000);
											driver.findElement(By.id("updatesDone")).click();
										} else {
											driver.findElement(By.id("logoutYESBtn")).click();
										}

										Thread.sleep(6000);
										driver.findElement(By.id("Uname")).sendKeys("prashant");
										driver.findElement(By.id("PWD")).sendKeys("prashant11");
										driver.findElement(By.id("Submit")).click();
										Thread.sleep(10000);

										// Check Landing page
										if (driver.getPageSource().contains("Go to Dashboard")) {
											driver.findElement(By.cssSelector("div.main-action-container.to-dashboard"))
													.click();
											Thread.sleep(10000);
										} else {
											System.out.println("Landing page is not present");
											Thread.sleep(6000);
										}

										// Search repository
										driver.findElement(By.id("select2-ui-id-1-container")).click();
										Thread.sleep(1000);

										driver.findElement(By.cssSelector(".select2-search__field")).clear();
										driver.findElement(By.cssSelector(".select2-search__field")).sendKeys(strrepotxt);
										Thread.sleep(1000);

										String resulttxt = driver.findElement(By.xpath("//ul[@id='select2-ui-id-1-results']/li")).getText();

										if (resulttxt.equals("No results found")) {
											System.out.println("Repository role updated successfully");

											context.setAttribute("Status", 1);
											context.setAttribute("Comment",
													"Repository role updated successfully under organization tab");

											driver.findElement(By.cssSelector(".select2-search__field")).clear();
											Thread.sleep(2000);
											return;

										} else {
											System.out.println("Repository role updation not working");
											context.setAttribute("Status", 5);
											context.setAttribute("Comment",
													"Failed to update repository role under organization tab");
										}

									}
								}

							} else {
								System.out.println("Landing page is not present");
								Thread.sleep(6000);
							}

						}
					}

				}

			
		} catch (Exception ex) {
			context.setAttribute("Status", 5);
			context.setAttribute("Comment", "Exception occured while updating repository role under organization tab");
			driver.findElement(By.cssSelector("failed click")).click();
		}
	}

	@Parameters({"UserName", "UpdateLastName"})
	@Test(priority = 4)
	@TestRail(testCaseId = 376)
	public void VerifyUserDetails(String UserName, String UpdateLastName, ITestContext context) {
		try {
			// Click on Admin settings
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Click on users tab
			driver.findElement(By.cssSelector(".clearfix > li:nth-child(3)")).click();
			Thread.sleep(1000);

			// Search user
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div/input")).sendKeys(UserName);
			Thread.sleep(2000);

			// Select user to edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div[2]/div/table/tbody/tr/td")).click();
			Thread.sleep(1000);

			// Click on edit button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/input")).click();
			Thread.sleep(4000);

			// Select Manageusers tab
			driver.findElement(By.id("manageUserTeamsTab")).click();
			Thread.sleep(4000);

			// check if any team exists
			String team = driver.findElement(By.xpath("//div[@id='ManageUserTeams']/div[2]/div/table/tbody/tr/td")).getText();
		    System.out.println("team: " + team);

			if (team.equals("No teams found")) {
				// Click on Add button
				driver.findElement(By.xpath("//div[@id='ManageUserTeams']/input")).click();
				Thread.sleep(1000);

				// Select team
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div[2]/div/table/tbody/tr/td/input")).click();
				Thread.sleep(1000);

				// Click on add
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/input")).click();
				Thread.sleep(1000);

			}

			// Edit user details
			driver.findElement(By.id("editUserTab")).click();
			Thread.sleep(4000);

			// Edit lastname
			driver.findElement(By.id("lastName")).clear();
			Thread.sleep(1000);
			driver.findElement(By.id("lastName")).sendKeys(UpdateLastName);
			Thread.sleep(4000);

			// Click on save button
			driver.findElement(By.xpath("//div[@id='EditUser']/form/button[2]")).click();
			Thread.sleep(1000);

			// Search user
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div/input")).clear();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div/input")).sendKeys(UserName);
			Thread.sleep(2000);

			// Select user to edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/div[2]/div/table/tbody/tr/td")).click();
			Thread.sleep(1000);

			// Click on edit button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[2]/div/input")).click();
			Thread.sleep(6000);

			String lastname = driver.findElement(By.id("lastName")).getAttribute("value");
			System.out.println(lastname);
			Thread.sleep(2000);

			if (lastname.equals(UpdateLastName)) {

				System.out.println("User details updated succesfully");

				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "User details are updated successfully on permission manager");

				// Click on Go Back button
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
				Thread.sleep(1000);

				// Click on Go back button
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
				Thread.sleep(1000);

				// Click on cancel button
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				Thread.sleep(3000);

			} else {
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Failed to update user details");
			}

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while updating user details, check details: " + e.getMessage());
			driver.findElement(By.cssSelector("failed click")).click();
		}
	}

	@Parameters({"TeamName"})
	@Test(priority = 5)
	@TestRail(testCaseId = 377)
	public void VerifyTeams(String TeamName,ITestContext context)throws IllegalArgumentException  {
		try {
			System.out.println("in verify team");
			// Click on Admin settings
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Select teams tab
			driver.findElement(By.xpath("//li[contains(.,'Teams')]")).click();
			Thread.sleep(3000);

			// Search teams
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).clear();
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).sendKeys(TeamName);
			Thread.sleep(2000);

			// select team to edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td")).click();
			Thread.sleep(1000);

			// Click on edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/input")).click();
			Thread.sleep(1000);

			// Click on Add button to add repository
			driver.findElement(By.xpath("//input[@value='Add']")).click();
			Thread.sleep(4000);

			// Search with the repo name
			driver.findElement(By.cssSelector(".info-data:nth-child(2)")).sendKeys(RepoName);

			// Select repository to add
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td/input")).click();
			Thread.sleep(3000);

			// Read repository data
			String repo = driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td/input")).getText();
			System.out.println("repo: " + repo);

			// Click on add button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/input")).click();
			Thread.sleep(4000);

			// Search for newly added repository
			driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/input[2]")).clear();
			driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/input[2]")).sendKeys(repo);
			Thread.sleep(1000);

			// Change roles
			boolean val = driver.findElement(By.xpath("//td[2]/div/label")).isSelected();
			String selected_role = "";
			// System.out.println(val);
			if (!val) {
				driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/div[2]/div/table/tbody/tr/td[2]/div/label")).click();
				Thread.sleep(1000);
				selected_role = driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/div[2]/div/table/tbody/tr/td[2]/div/label")).getText();
			} else {
				driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/div[2]/div/table/tbody/tr/td[2]/div/label[2]")).click();
				Thread.sleep(2000);
				selected_role = driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/div[2]/div/table/tbody/tr/td[2]/div/label[2]")).getText();
			}

			// System.out.println("selected role: " + selected_role);

			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
			Thread.sleep(2000);

			// Verify role changes

			// Search teams
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).clear();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).sendKeys(TeamName);
			Thread.sleep(1000);

			// select team to edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td")).click();
			Thread.sleep(1000);

			// Click on edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/input")).click();
			Thread.sleep(2000);

			// Search for newly added repository
			driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/input[2]")).clear();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/input[2]")).sendKeys(repo);
			Thread.sleep(5000);

			// verify role
			WebElement we = driver.findElement(By.className("btn-group"));
			List<WebElement> options = we.findElements(By.tagName("label"));

			for (WebElement wele : options) {
				String class1 = wele.getAttribute("class");
				// System.out.println("class :" + class1);
				if (class1.contains("active")) {
					// System.out.println("Active : " + wele.getText());
					if (selected_role.equals(wele.getText())) {
						System.out.println("Changing team role working fine");
						context.setAttribute("Status", 1);
						context.setAttribute("Comment", "Updating Team Role is working fine");
						// Verify delete
						driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/div[2]/div/table/tbody/tr/td[3]/img")).click();
						Thread.sleep(2000);

						// Search for newly added repository
						driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/input[2]")).clear();
						Thread.sleep(1000);

						driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/input[2]")).sendKeys(repo);
						Thread.sleep(2000);

						String strverify = driver.findElement(By.xpath("//div[@id='ManageTeamRepositories']/div[2]/div/table/tbody/tr/td"))
								.getText();

						if (strverify.equals("No repositories found")) {
							System.out.println("Delete option working fine");

							// click on go back     
							Thread.sleep(3000);
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
							Thread.sleep(2000);

							// Click on forward go back button  
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
							Thread.sleep(2000);

							// Click on cancel button
							driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
							Thread.sleep(2000);

						}

					} else {
						System.out.println("Changing team role not working");
						context.setAttribute("Status", 5);
						context.setAttribute("Comment", "Failed to update Team Role");
					}
					return;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while updating team role details, check details: " + e.getMessage());
			driver.findElement(By.cssSelector("failed click")).click();
		}
	}

	
	@Test(priority = 7)
	@TestRail(testCaseId = 378)
	public void UpdateTeamMember(ITestContext context) {
		try {
			
			Settings.read();
			String sUsername = Settings.getsUsername();
			
			// Click on Admin settings
			Thread.sleep(2000);
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Select teams tab
			driver.findElement(By.xpath("//li[contains(.,'Teams')]")).click();
			Thread.sleep(4000);

			// Search teams
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).clear();
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).sendKeys(TeamName);
			Thread.sleep(2000);

			// select team to edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td")).click();
			Thread.sleep(1000);

			// Click on edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/input")).click();
			Thread.sleep(1000);

			// Select Manage Team Members Tab
			driver.findElement(By.id("manageTeamMembersTab")).click();
			Thread.sleep(3000);

			// Click on add button
			driver.findElement(By.cssSelector("#ManageTeamMembers > .primary-btn")).click();
			Thread.sleep(2000);

			// Select member to add
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td/input")).click();
			Thread.sleep(3000);

			String user = driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td[2]")).getText();
			System.out.println("user: " + user);

			// Click on add button
			driver.findElement(By.xpath("//input[@value='Add']")).click();
			Thread.sleep(4000);

			// Verify if team member is added or not by search
			driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/input[2]")).clear();
			driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/input[2]")).sendKeys(user);
			Thread.sleep(2000);

			String verifyuser = driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/div[2]/div/table/tbody/tr/td")).getText();
			Thread.sleep(4000);

			if (user.equals(verifyuser)) {
				System.out.println("Team member added successfully");

				// Verify roles
				//driver.findElement(By.xpath("//label[contains(.,' Admin')]")).click();
				Thread.sleep(2000);

				String selected_role = driver.findElement(By.xpath("//label[contains(.,' User')]")).getText();
				System.out.println("Selecting role name is: " +selected_role);
				// System.out.println("Selected: " + selected_role);

				// Click on Go Back button
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
				Thread.sleep(2000);

				// Search teams
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).clear();
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).sendKeys(TeamName);
				Thread.sleep(1000);

				// select team to edit
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td")).click();
				Thread.sleep(1000);

				// Click on edit
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/input")).click();
				Thread.sleep(1000);

				// Select Manage Team Members Tab
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/div/button[2]")).click();
				Thread.sleep(2000);

				// Verify if team member is added or not by search
				driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/input[2]")).clear();
				Thread.sleep(1000);

				driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/input[2]")).sendKeys(user);
				Thread.sleep(2000);

				// verify role
				WebElement we = driver.findElement(By.className("btn-group"));
				List<WebElement> options = we.findElements(By.tagName("label"));

				for (WebElement wele : options) {
					String class1 = wele.getAttribute("class");
					// System.out.println("class :" + class1);
					if (class1.contains("active")) {
						// System.out.println("Active : " + wele.getText());
						if (selected_role.equals(wele.getText())) {
							System.out.println("Changing role working fine");
							context.setAttribute("Status", 1);
							context.setAttribute("Comment", "working fine");

						} else {
							context.setAttribute("Status", 5);
							context.setAttribute("Comment", "Failed");
							driver.findElement(By.xpath("Not created")).click();
						}
					}
				}
				
				Thread.sleep(4000);

				// Verify delete option
				driver.findElement(By.xpath("//img[@title='Remove user']")).click();
				Thread.sleep(3000);

				// Click on goback nutton
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
				Thread.sleep(2000);

				// Search teams
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).clear();
				Thread.sleep(1000);

				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/input")).sendKeys(TeamName);
				Thread.sleep(1000);

				// select team to edit
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div[2]/div/table/tbody/tr/td")).click();
				Thread.sleep(1000);

				// Click on edit
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/input")).click();
				Thread.sleep(1000);

				// Select Manage Team Members Tab
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[5]/div/div/div/button[2]")).click();
				Thread.sleep(2000);

				// Verify if team member is added or not by search
				driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/input[2]")).clear();
				Thread.sleep(1000);

				driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/input[2]")).sendKeys(user);
				Thread.sleep(2000);

				// verify if team member deleted or not
				String found_user = driver.findElement(By.xpath("//div[@id='ManageTeamMembers']/div[2]/div/table/tbody/tr/td")).getText();

				if (!found_user.equals(user)) {
					System.out.println("Delete option working fine.");
					context.setAttribute("Status", 1);
					context.setAttribute("Comment", "working fine");

					// click on go back
					Thread.sleep(3000);
					driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
					Thread.sleep(2000);

					// Click on forward go back button
					driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
					Thread.sleep(2000);

					// Click on cancel button
					driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
					Thread.sleep(2000);

				} else {
					context.setAttribute("Status", 5);
					context.setAttribute("Comment", "Failed");
					driver.findElement(By.xpath("Not created")).click();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Test(priority = 8)
	@TestRail(testCaseId = 379)
	public void CheckRepository(ITestContext context) {
		try {
			String sUsername = Settings.getsUsername();
			String sPassword = Settings.getsPassword();
			// Click on Admin settings
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Select repository tab
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
			Thread.sleep(1000);

			// Read repo table data
			WebElement repoele = driver.findElement(By.className("repo-container")).findElement(By.tagName("select"));

			// Loop table data
			List<WebElement> lst_repo = repoele.findElements(By.tagName("optgroup"));

			//System.out.println("Repo size: " + lst_repo.size());
			// Get table first row repository name to verify
			String repo_name = "";
			if (lst_repo.size() > 0) {
				repo_name = lst_repo.get(0).findElement(By.tagName("option")).getText();
			}

			System.out.println("Repo name: " + repo_name);

			// Get repository to edit using search
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).clear();
			Thread.sleep(1000);

			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).sendKeys(repo_name);
			Thread.sleep(5000);

			// Check Repository status to change
			// verify role
			WebElement we = driver.findElement(By.className("btn-group"));
			List<WebElement> options = we.findElements(By.tagName("label"));

			for (WebElement wele : options) {
				String class1 = wele.getAttribute("class");
				//System.out.println("class :" + class1);
				if (class1.contains("active")) {
				 // System.out.println("Active : " + wele.getText());

				  if (wele.getText().equals("Inactive")) {
					  System.out.println("in");
						// Click on go back
						driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
						Thread.sleep(1000);

						// Click on cancel button
						driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
						Thread.sleep(1000);

						// Search repository
						driver.findElement(By.xpath("//span[2]")).click();
						Thread.sleep(1000);

						// Clear search field  
						driver.findElement(By.xpath("//span/span/input")).clear();

						// Enter repo name to search
						driver.findElement(By.xpath("//span/span/input")).sendKeys(repo_name);
						Thread.sleep(1000);

						// Read result data
						String resulttxt = driver.findElement(By.xpath("//span[2]/ul/li")).getText();
						System.out.println("resulttxt:"+resulttxt);
						if (resulttxt.equals("No results found")) {

							System.out.println("Repository Inactive status Working fine");

							// Clear search string
							driver.findElement(By.xpath("//span/span/input")).sendKeys(Keys.BACK_SPACE);
							Thread.sleep(3000);

							// Change repository status to active and check if this repository is visible in
							// repo list

							// Click on Admin settings
							driver.findElement(By.cssSelector(".icon")).click();
							driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
							Thread.sleep(2000);

							// Click on permissions
							driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
							Thread.sleep(1000);

							// Select repository tab
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
							Thread.sleep(1000);

							// Get repository to edit using search
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).clear();
							Thread.sleep(1000);

							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).sendKeys(repo_name);
							Thread.sleep(1000);

							// Change status to active
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div[2]/div/table/tbody/tr/td[2]/div/label")).click();
							Thread.sleep(2000);

							// Click on go back
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
							Thread.sleep(2000);

							// Click on save changes
							driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div[2]/button")).click();
							Thread.sleep(3000);

							// Click on confirmation button
							driver.findElement(By.xpath("//dialog[@id='adminNotification']/input")).click();
							Thread.sleep(2000);

							// Logout and login using organization user
							driver.findElement(By.cssSelector(".fa-power-off")).click();
							Thread.sleep(2000);

							if (driver.findElement(By.id("logoutSaveBtn")).isDisplayed()) {
								driver.findElement(By.id("logoutSaveBtn")).click();
								Thread.sleep(2000);
								//driver.findElement(By.id("updatesDone")).click();
							} else {
								driver.findElement(By.id("logoutYESBtn")).click();
							}

							Thread.sleep(3000);
							driver.findElement(By.id("Uname")).sendKeys(sUsername);
							driver.findElement(By.id("PWD")).sendKeys(sPassword);
							driver.findElement(By.id("Submit")).click();
							Thread.sleep(4000);

							// Check Landing page
							if (driver.getPageSource().contains("Go to Dashboard")) {
								driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
								
							}
							else {
								Thread.sleep(10000);
							}
														
								// Search repository
								driver.findElement(By.xpath("//span[2]")).click();
								Thread.sleep(2000);

								driver.findElement(By.xpath("//span/span/input")).clear();
								Thread.sleep(3000);

								driver.findElement(By.xpath("//span/span/input")).sendKeys(repo_name);
								Thread.sleep(2000);

								String strresult = driver.findElement(By.xpath("//span/span/span/span")).getText();
								List<WebElement> lst = driver.findElements(By.xpath("//span/span/span/span"));
								boolean repo_exists = false;
								for (WebElement e : lst) {

									String str = e.getText().trim();
									if (str.contains("Test_Sample")) {

										repo_exists = true;

									}

								}

								if (repo_exists) {

									System.out.println("Repository Active status working fine");
									context.setAttribute("Status", 1);
									context.setAttribute("Comment", "Repository details are working fine");

									// clear search
									driver.findElement(By.xpath("//span/span/input")).sendKeys(Keys.BACK_SPACE);
									Thread.sleep(2000);

									// Close repo list
									driver.findElement(By.xpath("//span[2]")).click();
									Thread.sleep(1000);

								}

							

						}

					} else {
						// Click on go back
						driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
						Thread.sleep(1000);

						// Click on cancel button
						driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
						Thread.sleep(1000);

						// Search repository
						driver.findElement(By.xpath("//span[2]")).click();
						Thread.sleep(1000);

						driver.findElement(By.xpath("//span/span/input")).clear();
						Thread.sleep(1000);

						driver.findElement(By.xpath("//span/span/input")).sendKeys(repo_name);
						Thread.sleep(1000);

						String strresult = driver.findElement(By.xpath("//span[2]/ul/li")).getText();
						List<WebElement> lst = driver.findElements(By.xpath("//span[2]/ul/li"));
						boolean repo_exists = false;
						for (WebElement e : lst) {

							String str = e.getText().trim();
							if (str.contains("Test_Sampl")) {

								repo_exists = true;

							}

						}

						if (repo_exists) {
							System.out.println("Repository Active status working fine");
							Thread.sleep(3000);
							// Clear search string
							/*
							 * driver.findElement(By.xpath("//span[2]/ul/li")).sendKeys(Keys.BACK_SPACE);
							 * Thread.sleep(2000);
							 */
							// Change repository status to active and check if this repository is visible in
							// repo list

							// Click on Admin settings
							driver.findElement(By.cssSelector(".icon")).click();
							driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
							Thread.sleep(2000);

							// Click on permissions
							driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
							Thread.sleep(1000);

							// Select repository tab
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
							Thread.sleep(1000);

							// Get repository to edit using search
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).clear();
							Thread.sleep(1000);

							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).sendKeys(repo_name);
							Thread.sleep(1000);

							// Change status to active
							driver.findElement(By.cssSelector(".inactive-feature")).click();
							Thread.sleep(2000);

							// Click on go back
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
							Thread.sleep(1000);

							// Click on save changes
							driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div[2]/button")).click();
							Thread.sleep(2000);

							// Click on confirmation button
							try {
								driver.findElement(By.xpath("//dialog[@id='adminNotification']/input")).click();
								Thread.sleep(2000);
							} catch (Exception e) {

							}

							// Logout and login using organization user
							driver.findElement(By.cssSelector(".fa-power-off")).click();
							Thread.sleep(2000);

							if (driver.findElement(By.id("logoutSaveBtn")).isDisplayed()) {
								driver.findElement(By.id("logoutSaveBtn")).click();
								Thread.sleep(2000);
								// driver.findElement(By.id("updatesDone")).click();
							} else {
								driver.findElement(By.id("logoutYESBtn")).click();
							}

							Thread.sleep(3000);
							driver.findElement(By.id("Uname")).sendKeys(sUsername);
							driver.findElement(By.id("PWD")).sendKeys(sPassword);
							driver.findElement(By.id("Submit")).click();
							Thread.sleep(4000);

							// Check Landing page
							if (driver.getPageSource().contains("Go to Dashboard")) {
								
								driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
								Thread.sleep(10000);
							}
							Thread.sleep(10000);
								// Search repository
								driver.findElement(By.xpath("//span[2]")).click();
								Thread.sleep(1000);

								driver.findElement(By.xpath("//span/span/input")).clear();
								Thread.sleep(1000);

								driver.findElement(By.xpath("//span/span/input")).sendKeys(repo_name);
								Thread.sleep(1000);

								String result = driver.findElement(By.xpath("//span[2]/ul/li")).getText();

								if (result.equals("No results found")) {
									System.out.println("Repository Inactive status Working fine");
									context.setAttribute("Status", 1);
									context.setAttribute("Comment", "Repository details are working fine");

									// Clear search string
									driver.findElement(By.xpath("//span/span/input")).sendKeys(Keys.BACK_SPACE);
									Thread.sleep(2000);

									driver.findElement(By.xpath("//span[2]")).click();
									Thread.sleep(1000);

								} else {
									context.setAttribute("Status", 5);
									context.setAttribute("Comment", "Repository details are  not working properly");
								}
							
							
						}
					}

				}
				
				return;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Exception occured while checking Repository details, check details: " + e.getMessage());
		}
	}

	@Test(priority = 10)
	@TestRail(testCaseId = 380)
	public void EditRepository(ITestContext context) {
		try {
			String sUsername = Settings.getsUsername();
			String sPassword = Settings.getsPassword();
			// Click on Admin settings
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Select repository tab
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
			Thread.sleep(3000);
			
			
			// Read repo table data
			WebElement repoele = driver.findElement(By.className("repo-container")).findElement(By.tagName("select"));

			// Loop table data
			List<WebElement> lst_repo = repoele.findElements(By.tagName("optgroup"));

			 System.out.println("Repo size: " + lst_repo.size());
			// Get table first row repository name to verify
			String repo_name = "";
			if (lst_repo.size() > 0) {
				repo_name = lst_repo.get(0).findElement(By.tagName("option")).getText();
				System.out.println("Repo name: " + repo_name);
				
				lst_repo.get(0).findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div[2]/div/table/tbody/tr/td")).click();
				Thread.sleep(1000);

				// Click on edit button
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/input")).click();
				Thread.sleep(2000);

				// Check if token exists
				WebElement ele = driver.findElement(By.className("openViewletTbl")).findElement(By.tagName("table"))
						.findElement(By.tagName("tbody"));

				List<WebElement> lst_token = ele.findElements(By.tagName("tr"));

				//System.out.println("Size: " + lst_token.size());

				int tokenlst_size = lst_token.size();

				if (lst_token.size() > 0) {
					
					String str = lst_token.get(0).getText();
					//System.out.println("str: "+ str);
					if (str.equals("No tokens found")) {

						driver.findElement(By.xpath("//div[@id='ManageRepoTokens']/input")).click();
						Thread.sleep(2000);

						driver.findElement(By.xpath("//div[@id='ManageRepoTokens']/input")).click();
						Thread.sleep(2000);
						
						List<WebElement> lst_token_new = ele.findElements(By.tagName("tr"));

						//System.out.println("lst_token_new: "+lst_token_new.size());

						if (lst_token_new.size() >= lst_token.size()) {
							System.out.println("Generating new tokens working fine");

							String qry_attr = lst_token_new.get(0).findElement(By.xpath("//td[5]/div/label")).getText();
							 //System.out.println("qry_attr: "+qry_attr);

							lst_token_new.get(0).findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[5]/div/label")).click();
							Thread.sleep(2000);

							String modify_attr = lst_token_new.get(0).findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[6]/div/label"))
									.getText();

							 //System.out.println("modify_attr: " +modify_attr);

							lst_token_new.get(0).findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[6]/div/label")).click();
							Thread.sleep(2000);

							// Click on goback button
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
							Thread.sleep(2000);

							// Click on goback button
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
							Thread.sleep(2000);

							// Click on save changes
							driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div[2]/button")).click();
							Thread.sleep(2000);

							// Click on confirmation button
							driver.findElement(By.xpath("//dialog[@id='adminNotification']/input")).click();
							Thread.sleep(2000);

							// Logout and login using organization user
							driver.findElement(By.cssSelector(".fa-power-off")).click();
							Thread.sleep(2000);

							if (driver.findElement(By.id("logoutSaveBtn")).isDisplayed()) {
								driver.findElement(By.id("logoutSaveBtn")).click();
								Thread.sleep(2000);
								// driver.findElement(By.id("updatesDone")).click();
							} else {
								driver.findElement(By.id("logoutYESBtn")).click();
							}

							Thread.sleep(3000);
							driver.findElement(By.id("Uname")).sendKeys(sUsername);
							driver.findElement(By.id("PWD")).sendKeys(sPassword);
							driver.findElement(By.id("Submit")).click();
							Thread.sleep(4000);

							// Check Landing page
							if (driver.getPageSource().contains("Go to Dashboard")) {
								driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
								Thread.sleep(10000);
							}else
							{
								Thread.sleep(10000);
							}

								// Click on Admin settings
								driver.findElement(By.cssSelector(".icon")).click();
								driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
								Thread.sleep(2000);

								// Click on permissions
								driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
								Thread.sleep(1000);

								// Select repository tab
								driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
								Thread.sleep(1000);

								
								// Read repo table data
								WebElement repo_ele = driver.findElement(By.className("repo-container"))
										.findElement(By.tagName("select"));

								// Loop table data
								List<WebElement> lst_editrepo = repo_ele.findElements(By.tagName("optgroup"));

								// System.out.println("Repo size: " + lst_editrepo.size());
								// Get table first row repository name to verify

								if (lst_editrepo.size() > 0) {
									repo_name = lst_editrepo.get(0).findElement(By.tagName("option")).getText();
									
									//System.out.println("rep name: "+ repo_name);
									lst_editrepo.get(0).findElement(By.xpath("//td")).click();
									Thread.sleep(1000);

									// Click on edit button
									driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/input")).click();
									Thread.sleep(2000);

									// Check if token exists
									WebElement wbele = driver.findElement(By.className("openViewletTbl"))
											.findElement(By.tagName("table")).findElement(By.tagName("tbody"));

									List<WebElement> lst_tokenedit = wbele.findElements(By.tagName("tr"));

									String strresult = lst_tokenedit.get(0).getText();
									if (!str.equals("No tokens found")) {
										String str_query_chk = lst_token.get(0)
												.findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[5]/div/label")).getText();

										if (!str_query_chk.equals(qry_attr)) {
											System.out.println("Token updating is working fine");
											context.setAttribute("Status", 1);
											context.setAttribute("Comment", "Repository details are working fine");
											// Click Go Back button
											driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
											Thread.sleep(5000);

											// Click Go Back button
											driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
											Thread.sleep(5000);
										} else {
											context.setAttribute("Status", 5);
											context.setAttribute("Comment", "Failed to update repository tokens");
										}

									}

								

							}
						}
					} else {
						
						String qry_attr = lst_token.get(0).findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[5]/div/label")).getText();
						// System.out.println(qry_attr);

						lst_token.get(0).findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[5]/div/label")).click();
						Thread.sleep(2000);

						String modify_attr = lst_token.get(0).findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[6]/div/label")).getText();
						// System.out.println(modify_attr);

						lst_token.get(0).findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[6]/div/label")).click();
						Thread.sleep(2000);

						// Click on goback button
						driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
						Thread.sleep(2000);

						// Click on goback button
						driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
						Thread.sleep(2000);

						// Click on save changes
						driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div[2]/button")).click();
						Thread.sleep(2000);

						// Click on confirmation button
						try{
						driver.findElement(By.xpath("//dialog[@id='adminNotification']/input")).click();
						Thread.sleep(2000);
						}
						catch(Exception e)
						{
							
						}

						// Logout and login using organization user
						driver.findElement(By.cssSelector(".fa-power-off")).click();
						Thread.sleep(2000);

						if (driver.findElement(By.id("logoutSaveBtn")).isDisplayed()) {
							driver.findElement(By.id("logoutSaveBtn")).click();
							Thread.sleep(2000);
							// driver.findElement(By.id("updatesDone")).click();
						} else {
							driver.findElement(By.id("logoutYESBtn")).click();
						}

						Thread.sleep(3000);
						driver.findElement(By.id("Uname")).sendKeys(sUsername);
						driver.findElement(By.id("PWD")).sendKeys(sPassword);
						driver.findElement(By.id("Submit")).click();
						Thread.sleep(4000);

						// Check Landing page
						if (driver.getPageSource().contains("Go to Dashboard")) {
							driver.findElement(By.cssSelector("div.main-action-container.to-dashboard")).click();
							Thread.sleep(10000);
						}
						else
						{
							Thread.sleep(10000);
						}
						

							// Click on Admin settings
							driver.findElement(By.cssSelector(".icon")).click();
							driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
							Thread.sleep(2000);

							// Click on permissions
							driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
							Thread.sleep(1000);

							// Select repository tab
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
							Thread.sleep(1000);
							
							

							// Read repo table data
							WebElement repo_ele = driver.findElement(By.className("repo-container"))
									.findElement(By.tagName("select"));

							// Loop table data
							List<WebElement> lst_editrepo = repo_ele.findElements(By.tagName("optgroup"));

							// System.out.println("Repo size: " + lst_editrepo.size());
							// Get table first row repository name to verify

							if (lst_editrepo.size() > 0) {
								repo_name = lst_editrepo.get(0).findElement(By.tagName("option")).getText();
								//System.out.println("repository name: "+ repo_name);
								lst_editrepo.get(0).findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div[2]/div/table/tbody/tr/td")).click();
								Thread.sleep(1000);

								// Click on edit button
								driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/input")).click();
								Thread.sleep(2000);

								// Check if token exists
								WebElement wbele = driver.findElement(By.className("openViewletTbl"))
										.findElement(By.tagName("table")).findElement(By.tagName("tbody"));

								List<WebElement> lst_tokenedit = wbele.findElements(By.tagName("tr"));

								System.out.println("lst_tokenedit size: "+ lst_tokenedit.size());

								String strresult = lst_tokenedit.get(0).getText();

								 //System.out.println("strresult: " + strresult);

								if (!strresult.equals("No tokens found")) {
									String str_query_chk = lst_tokenedit.get(0)
											.findElement(By.xpath("//div[@id='ManageRepoTokens']/div[2]/div/table/tbody/tr/td[5]/div/label")).getText();

								

									if (!str_query_chk.equals(qry_attr)) {

										System.out.println("Token updated");
										context.setAttribute("Status", 1);
										context.setAttribute("Comment", "Updating Repository token is working fine");

										// Click Go Back button
										driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
										Thread.sleep(5000);

										// Click Go Back button
										driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
										Thread.sleep(10000);

										// Click on cancel button
										driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
										Thread.sleep(2000);

									} else {
										System.out.println("Token updation is not working");
										context.setAttribute("Status", 5);
										context.setAttribute("Comment", "Failed to update repository tokens");
										// Click Go Back button
										driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
										Thread.sleep(5000);

										// Click Go Back button
										driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
										Thread.sleep(10000);

										// Click on cancel button
										driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
										Thread.sleep(2000);
									}

								}

							

						}

					}

				}
			}

		} catch (

		Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Error occured during checking repository tokens, check details: " + e.getMessage());
		}
	}

	@Test(priority = 11)
	@TestRail(testCaseId = 381)
	public void CheckRepositoryDashboard(ITestContext context) {
		try {
			// Click on Admin settings
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Select repository tab
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
			Thread.sleep(3000);
			
			
			// Read repo table data
			WebElement repoele = driver.findElement(By.className("repo-container")).findElement(By.tagName("select"));

			// Loop table data
			List<WebElement> lst_repo = repoele.findElements(By.tagName("optgroup"));

			// System.out.println("Repo size: " + lst_repo.size());
			// Get table first row repository name to verify
			String repo_name = "";
			if (lst_repo.size() > 0) {
				repo_name = lst_repo.get(0).findElement(By.tagName("option")).getText();
			}
			Thread.sleep(4000);

			 System.out.println("Repo name: " + repo_name);

			// Get repository to edit using search
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).clear();
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).sendKeys(repo_name);
			Thread.sleep(4000);

			// Select repository to edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div[2]/div/table/tbody/tr/td")).click();
			Thread.sleep(2000);

			// Click edit button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/input")).click();
			Thread.sleep(4000);

			// Select manage repository dashboard tab
			driver.findElement(By.id("manageRepoDashboardsTab")).click();
			Thread.sleep(4000);

			// Read dashboard data
			WebElement ele = driver.findElement(By.id("ManageRepoDashboards"))
					.findElement(By.className("openViewletTbl")).findElement(By.tagName("table"))
					.findElement(By.tagName("tbody"));

			// Check how many dashboard are listed in table
			List<WebElement> lst_ele = ele.findElements(By.tagName("tr"));

			// System.out.println("Dashboard size: " + lst_ele.size());

			if (lst_ele.size() > 0) {
				String dashboard_name = lst_ele.get(0).findElement(By.tagName("td")).getText();

				 System.out.println(dashboard_name);

				if (!dashboard_name.equals("No dashboards found")) {
					// Search dashboard
					driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).clear();
					Thread.sleep(1000);

					driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).sendKeys(dashboard_name);
					Thread.sleep(1000);

					String verify_search = lst_ele.get(0).findElement(By.tagName("td")).getText();
					String comment_txt="";

					if (dashboard_name.equals(verify_search)) {
						System.out.println("Search is working fine");
						context.setAttribute("Status", 1);
						comment_txt="Repository dashboard search is working fine";
						//context.setAttribute("Comment", "Repository dashboard search is working fine");

						// Verify delete
						driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/div[2]/div/table/tbody/tr/td[4]/img")).click();
						Thread.sleep(2000);

						// Search dashboard
						driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).clear();
						Thread.sleep(1000);

						driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).sendKeys(dashboard_name);
						Thread.sleep(2000);

						if (!dashboard_name.equals("No dashboards found")) {
							System.out.println("Delete option is working fine.");
							context.setAttribute("Status", 1);
							if(!comment_txt.isEmpty())
							{
								context.setAttribute("Comment", comment_txt +" & Repository dashboard delete option is working fine");
							}
							else
							{
								context.setAttribute("Comment", "Repository dashboard delete option is working fine");
							}
							
							
							// Click Go Back button
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
							Thread.sleep(5000);

							// Click Go Back button
							driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
							Thread.sleep(10000);

							// Click on cancel button
							driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
							Thread.sleep(2000);
						}

					}
				}

			}

		} catch (Exception e) {

			//e.printStackTrace();
			context.setAttribute("Status", 5);
			
			context.setAttribute("Comment", "Got an exception, check details : "+ e.getMessage());

		}
	}

	@Test(priority = 12)
	@TestRail(testCaseId = 435)
	public void VerifyRepDashboard(ITestContext context) {
		try {
			// Click on Admin settings
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@id='main-menu-icon']/div")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Select repository tab
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[5]")).click();
			Thread.sleep(4000);
			
				// Read repo table data
			WebElement repoele = driver.findElement(By.className("repo-container")).findElement(By.tagName("select"));

			// Loop table data
			List<WebElement> lst_repo = repoele.findElements(By.tagName("optgroup"));

			// System.out.println("Repo size: " + lst_repo.size());
			// Get table first row repository name to verify
			String repo_name = "";
			if (lst_repo.size() > 0) {
				repo_name = lst_repo.get(0).findElement(By.tagName("option")).getText();
			}

			// System.out.println("Repo name: " + repo_name);

			// Get repository to edit using search
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).clear();
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div/input")).sendKeys(repo_name);
			Thread.sleep(1000);

			// Select repository to edit
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div[2]/div/table/tbody/tr/td")).click();
			Thread.sleep(4000);

			// Click edit button
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/input")).click();
			Thread.sleep(4000);

			// Select manage repository dashboard tab
			driver.findElement(By.id("manageRepoDashboardsTab")).click();
			Thread.sleep(3000);

			// Read dashboard data
			WebElement ele = driver.findElement(By.id("ManageRepoDashboards"))
					.findElement(By.className("openViewletTbl")).findElement(By.tagName("table"))
					.findElement(By.tagName("tbody"));

			// Check how many dashboard are listed in table
			List<WebElement> lst_ele = ele.findElements(By.tagName("tr"));

			// System.out.println("Dashboard size: " + lst_ele.size());

			if (lst_ele.size() > 0) {
				String dashboard_name = lst_ele.get(0).findElement(By.tagName("td")).getText();

				 System.out.println(dashboard_name);

				if (!dashboard_name.equals("No dashboards found")) {
					// Search dashboard
					driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).clear();
					Thread.sleep(1000);

					driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).sendKeys(dashboard_name);
					Thread.sleep(1000);

					String verify_search = lst_ele.get(0).findElement(By.tagName("td")).getText();

					if (dashboard_name.equals(verify_search)) {
						// Select dashboard to manage edit options
						lst_ele.get(0).findElement(By.tagName("td")).click();
						Thread.sleep(2000);

						driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input[2]")).click();
						Thread.sleep(2000);

						WebElement m_ele = driver.findElement(By.className("form-style-2"));

						List<WebElement> lst_divele = m_ele.findElements(By.className("openViewletTbl"));

						// System.out.println(lst_divele.size());

						for (WebElement tabelelement : lst_divele) {
							WebElement inner_ele = tabelelement.findElement(By.tagName("table"));

							String class_name = inner_ele.getAttribute("class");

							// System.out.println(class_name);
							if (class_name.isEmpty()) {
								WebElement team_row = inner_ele.findElement(By.tagName("tbody"));
								System.out.println("In Available teams");
								List<WebElement> lst_teams = team_row.findElements(By.tagName("tr"));
								System.out.println("Teams size: " + lst_teams.size());

								// System.out.println("Team:" +
								// lst_teams.get(0).findElement(By.tagName("td")).getText());

								String team_member = lst_teams.get(0).findElement(By.tagName("td")).getText();

								// Select team to assign
								lst_teams.get(0).findElement(By.tagName("td")).click();
								Thread.sleep(2000);

								// click on add button
								driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/aside[3]/div/div[5]/input")).click();
								Thread.sleep(2000);

								// click on goback button
								driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
								Thread.sleep(2000);

								// Search dashboard
								driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).clear();
								Thread.sleep(1000);

								driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input")).sendKeys(dashboard_name);
								Thread.sleep(1000);

								// verify dashboard changes
								driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/div[2]/div/table/tbody/tr/td")).click();
								Thread.sleep(1000);

								// Click manage button
								driver.findElement(By.xpath("//div[@id='ManageRepoDashboards']/input[2]")).click();
								Thread.sleep(2000);

								WebElement assignedteam_ele = driver.findElement(By.className("form-style-2"));

								List<WebElement> lst_assignedteam = assignedteam_ele
										.findElements(By.className("openViewletTbl"));

								// WebElement verify_ele=tabelelement.findElement(By.tagName("table"));

								for (WebElement assignedelement : lst_assignedteam) {
									WebElement assigned_table = assignedelement.findElement(By.tagName("table"));

									String classname = assigned_table.getAttribute("class");

									// System.out.println(class_name);
									if (!classname.isEmpty()) {
										WebElement team_assigned = assigned_table.findElement(By.tagName("tbody"));

										List<WebElement> lst_assignedteams = team_assigned
												.findElements(By.tagName("tr"));
										// System.out.println("Assigned Teams size: " + lst_assignedteams.size());

										for (WebElement e : lst_assignedteams) {
											if (team_member.equals(e.findElement(By.tagName("td")).getText())) {
												System.out.println("Team member assigned to dashboard successfully");
												context.setAttribute("Status", 1);
												context.setAttribute("Comment", "working fine");

												// Click Go Back button
												driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
												Thread.sleep(5000);

												// Click Go Back button
												driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button")).click();
												Thread.sleep(5000);

												// Click Go Back button
												driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
												Thread.sleep(10000);

												// Click on cancel button
												driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
												Thread.sleep(2000);

												return;

											} else {
												context.setAttribute("Status", 5);
												context.setAttribute("Comment", "Failed");
											}
										}
									}
								}

							}
						}

					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Test(priority = 13)
	@TestRail(testCaseId = 436)
	public void VerifyPolicyTab(ITestContext context) {
		try {
			driver.findElement(By.cssSelector(".icon")).click();
			driver.findElement(By.xpath("//li[contains(.,' Admin Settings')]")).click();
			Thread.sleep(2000);

			// Click on permissions
			driver.findElement(By.xpath("//li[contains(.,'Organization')]")).click();
			Thread.sleep(1000);

			// Select policy tab
			driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/nav/ul/li[6]")).click();
			Thread.sleep(4000);

			WebElement org_ele = driver.findElement(By.className("organizationData")).findElement(By.tagName("tbody"));

			List<WebElement> lst_org = org_ele.findElements(By.tagName("tr"));
			String strinput = "";
			boolean isdisabled = true;
			// System.out.println();
			for (WebElement ele : lst_org) {
				List<WebElement> wele = ele.findElements(By.tagName("input"));
				for (WebElement input : wele) {
					strinput = input.getAttribute("disabled");
					if (strinput.contains("false")) {
						// System.out.println(input.getAttribute("disabled"));
						isdisabled = false;
						break;

					}
					// System.out.println(input.getAttribute("disabled"));
				}
			}
			if (isdisabled) {
				System.out.println("Policies working fine");
				context.setAttribute("Status", 1);
				context.setAttribute("Comment", "Policy tab is working fine");
				// Click Go Back button
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
				Thread.sleep(10000);

				// Click on cancel button
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				Thread.sleep(2000);
			} else {
				System.out.println("Policies are not working properly");
				context.setAttribute("Status", 5);
				context.setAttribute("Comment", "Policy tab not working properly");
				// Click Go Back button
				driver.findElement(By.xpath("//dialog[@id='permissionsWizard']/section/main/footer/button[2]")).click();
				Thread.sleep(10000);

				// Click on cancel button
				driver.findElement(By.xpath("//dialog[@id='adminWizard']/section/main/footer[2]/div/button")).click();
				Thread.sleep(2000);
			}

		} catch (Exception e) {
			// TODO: handle exception
			// e.printStackTrace();
			context.setAttribute("Status", 5);
			context.setAttribute("Comment",
					"Error occured while checking policy tab, check details: " + e.getMessage());
		}
	}

	@Test(priority = 15)
	public static void Logout() throws Throwable {

		LogoutForAll obj=new LogoutForAll();
		obj.DontSaveLogout(driver);
	}

	@AfterMethod
	public void tearDown(ITestResult result) {

		final String dir = System.getProperty("user.dir");
		String screenshotPath;
		// System.out.println("dir: " + dir);
		if (!result.getMethod().getMethodName().contains("Logout")) {
			if (ITestResult.FAILURE == result.getStatus()) {
				this.capturescreen(driver, result.getMethod().getMethodName(), "FAILURE");
				Reporter.setCurrentTestResult(result);

				Reporter.log("<br/>Failed to execute method: " + result.getMethod().getMethodName() + "<br/>");
				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsFailure/"
						+ result.getMethod().getMethodName() + ".png";

			} else {
				this.capturescreen(driver, result.getMethod().getMethodName(), "SUCCESS");
				Reporter.setCurrentTestResult(result);

				// Attach screenshot to report log
				screenshotPath = dir + "/" + Screenshotpath + "/ScreenshotsSuccess/"
						+ result.getMethod().getMethodName() + ".png";

			}

			String path = "<img src=\" " + screenshotPath + "\" alt=\"\"\"/\" />";
			// To add it in the report
			Reporter.log("<br/>");
			Reporter.log(path);

			try {
				// Update attachment to testrail server
				int testCaseID = 0;
				// int status=(int) result.getTestContext().getAttribute("Status");
				// String comment=(String) result.getTestContext().getAttribute("Comment");
				if (result.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(TestRail.class)) {
					TestRail testCase = result.getMethod().getConstructorOrMethod().getMethod()
							.getAnnotation(TestRail.class);
					// Get the TestCase ID for TestRail
					testCaseID = testCase.testCaseId();

					TestRailAPI api = new TestRailAPI();
					api.Getresults(testCaseID, result.getMethod().getMethodName());

				}
			} catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
			}
		}

	}

	public void capturescreen(WebDriver driver, String screenShotName, String status) {
		try {
			
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			if (status.equals("FAILURE")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png"));
				Reporter.log(Screenshotpath + "/ScreenshotsFailure/" + screenShotName + ".png");
			} else if (status.equals("SUCCESS")) {
				FileHandler.copy(scrFile,
						new File(Screenshotpath + "./ScreenshotsSuccess/" + screenShotName + ".png"));

			}

			System.out.println("Printing screen shot taken for className " + screenShotName);

		} catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
		}
	}

}
