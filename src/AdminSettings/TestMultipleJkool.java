package AdminSettings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import org.testng.TestNG;
import org.xml.sax.SAXException;

public class TestMultipleJkool 
{

	public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		
	//PrintStream fileOut = new PrintStream("Log.txt");
	//System.setOut(fileOut);
	
	//System.console();

	TestNG testng = new TestNG(); 
	//testng.setXmlSuites((List <XmlSuite>)(new Parser("MultipleFilesRunning.xml").parse()));
	//testng.setXmlSuites(singletonList());
	//testng.setXmlSuites((List <XmlSuite>)(new Parser("MultipleFilesRunning.xml").parse()));
	//testng.setSuiteThreadPoolSize(1);
	
	List<String> suitefiles=new ArrayList<String>();

	// Add xml file which you have to execute
	suitefiles.add("JkoolMulti.xml");

	// now set xml file for execution
	testng.setTestSuites(suitefiles);
	testng.run();
	
	
	
	
  
    }
}
