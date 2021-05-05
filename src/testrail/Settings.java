package testrail;

import java.io.FileInputStream;
import java.util.Properties;

public class Settings {
	private static Properties propertiesSetting = null;

	private static String settingTestrailAPI;

	private static String settingUsername;
	private static String settingPassword;

	private static String settingProjectId;
	
	private static String settingTestRunId;

	private static String settingURL;
	
	

	private static String settingENV;
	
	private static String sURL;
	private static String sUsername;
	private static String sPassword;
	private static String ScreenshotPath;
	private static String DownloadPath;
	private static String ImportData;
	private static String ImportViewlet;
	private static String ImportDashboard;
	private static String ImportSet;
	private static String Importlogo;
	
	private static String TeamName;
	private static String RepoName;
	
	private static String UserName;
	private static String Password;
	private static String MailServer;
	private static String PortNumber;
	private static String MailFrom;
	private static String MailTo;
	
	private static String PreviousDefaultRepository;
	private static String TestRunID;


	
	public static  void read() throws Exception {
		if (propertiesSetting == null) {
			propertiesSetting = new Properties();
			propertiesSetting.load(new FileInputStream("File.properties"));

			settingTestrailAPI = propertiesSetting.getProperty("TESTRAILAPI");
			settingUsername = propertiesSetting.getProperty("USERNAME");
			settingPassword = propertiesSetting.getProperty("PASSWORD");
			settingProjectId = propertiesSetting.getProperty("PROJECTID");
			settingURL = propertiesSetting.getProperty("URL");
			settingENV = propertiesSetting.getProperty("ENV");
			
			sURL= propertiesSetting.getProperty("sURL");
			sUsername= propertiesSetting.getProperty("sUsername");
			sPassword= propertiesSetting.getProperty("sPassword");
			ScreenshotPath= propertiesSetting.getProperty("ScreenshotPath");
			DownloadPath= propertiesSetting.getProperty("DownloadPath");
			ImportData= propertiesSetting.getProperty("ImportData");
			ImportViewlet= propertiesSetting.getProperty("ImportViewlet");
			ImportDashboard= propertiesSetting.getProperty("ImportDashboard");
			ImportSet= propertiesSetting.getProperty("ImportSet");
			Importlogo=propertiesSetting.getProperty("Importlogo");
			settingTestRunId=propertiesSetting.getProperty("TestRunID");
			
			TeamName=propertiesSetting.getProperty("TeamName");
			RepoName=propertiesSetting.getProperty("RepoName");
			
			UserName=propertiesSetting.getProperty("UserName");
			Password=propertiesSetting.getProperty("Password");
			MailServer=propertiesSetting.getProperty("MailServer");
			PortNumber=propertiesSetting.getProperty("PortNumber");
			MailFrom=propertiesSetting.getProperty("MailFrom");
			MailTo=propertiesSetting.getProperty("MailTo");
			
			PreviousDefaultRepository=propertiesSetting.getProperty("PreviousDefaultRepository");
			TestRunID=propertiesSetting.getProperty("TestRunID");
		}
	}
	
	public static String getTestRunID() {
		return TestRunID;
	}


	public static void setTestRunID(String testRunID) {
		TestRunID = testRunID;
	}
	
	public static String getPreviousDefaultRepository() {
		return PreviousDefaultRepository;
	}


	public static void setPreviousDefaultRepository(String previousDefaultRepository) {
		PreviousDefaultRepository = previousDefaultRepository;
	}
	
	public static String getMailTo() {
		return MailTo;
	}

	public static void setMailTo(String mailTo) {
		MailTo = mailTo;
	}

	public static String getMailFrom() {
		return MailFrom;
	}

	public static void setMailFrom(String mailFrom) {
		MailFrom = mailFrom;
	}

	public static String getPortNumber() {
		return PortNumber;
	}

	public static void setPortNumber(String portNumber) {
		PortNumber = portNumber;
	}

	public static String getMailServer() {
		return MailServer;
	}

	public static void setMailServer(String mailServer) {
		MailServer = mailServer;
	}
	
	public static String getPassword() {
		return Password;
	}

	public static void setPassword(String password) {
		Password = password;
	}
	
	public static String getUserName() {
		return UserName;
	}

	public static void setUserName(String userName) {
		UserName = userName;
	}
	
	public static String getSettingTestRunId() {
		return settingTestRunId;
	}

	public static void setSettingTestRunId(String settingTestRunId) {
		Settings.settingTestRunId = settingTestRunId;
	}
	
	public static String getRepoName() {
		return RepoName;
	}

	public static void setRepoName(String repoName) {
		RepoName = repoName;
	}

	public static String getTeamName() {
		return TeamName;
	}

	public static void setTeamName(String teamName) {
		TeamName = teamName;
	}
	
	public static String getImportlogo() {
		return Importlogo;
	}


	public static void setImportlogo(String importlogo) {
		Importlogo = importlogo;
	}
	
	public static String getImportDashboard() {
		return ImportDashboard;
	}


	public static void setImportDashboard(String importDashboard) {
		ImportDashboard = importDashboard;
	}


	public static String getImportSet() {
		return ImportSet;
	}


	public static void setImportSet(String importSet) {
		ImportSet = importSet;
	}


	public static String getImportViewlet() {
		return ImportViewlet;
	}


	public static void setImportViewlet(String importViewlet) {
		ImportViewlet = importViewlet;
	}

	public static String getImportData() {
		return ImportData;
	}


	public static void setImportData(String importData) {
		ImportData = importData;
	}


	public static String getsURL() {
		return sURL;
	}

	public static void setsURL(String sURL) {
		Settings.sURL = sURL;
	}

	public static String getsUsername() {
		return sUsername;
	}

	public static void setsUsername(String sUsername) {
		Settings.sUsername = sUsername;
	}

	public static String getsPassword() {
		return sPassword;
	}

	public static void setsPassword(String sPassword) {
		Settings.sPassword = sPassword;
	}

	public static Properties getPropertiesSetting() {
		return propertiesSetting;
	}

	public static void setPropertiesSetting(Properties propertiesSetting) {
		Settings.propertiesSetting = propertiesSetting;
	}

	public static String getSettingTestrailAPI() {
		return settingTestrailAPI;
	}

	public static void setSettingTestrailAPI(String settingTestrailAPI) {
		Settings.settingTestrailAPI = settingTestrailAPI;
	}

	public static String getSettingUsername() {
		return settingUsername;
	}

	public static void setSettingUsername(String settingUsername) {
		Settings.settingUsername = settingUsername;
	}

	public static String getSettingPassword() {
		return settingPassword;
	}

	public static void setSettingPassword(String settingPassword) {
		Settings.settingPassword = settingPassword;
	}

	public static String getSettingProjectId() {
		return settingProjectId;
	}

	public static void setSettingProjectId(String settingProjectId) {
		Settings.settingProjectId = settingProjectId;
	}

	public static String getSettingURL() {
		return settingURL;
	}

	public static void setSettingURL(String settingURL) {
		Settings.settingURL = settingURL;
	}

	public static String getSettingENV() {
		return settingENV;
	}

	public static void setSettingENV(String settingENV) {
		Settings.settingENV = settingENV;
	}

	public static String getScreenshotPath() {
		return ScreenshotPath;
	}

	public static void setScreenshotPath(String screenshotPath) {
		ScreenshotPath = screenshotPath;
	}

	public static String getDownloadPath() {
		return DownloadPath;
	}

	public static void setDownloadPath(String downloadPath) {
		DownloadPath = downloadPath;
	}

	

	

	

	

	


}