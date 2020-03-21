package com.mPulse.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;

public class RunCustomTestNG {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
	    TestNG testng = new TestNG();
	    List<String> suites = new ArrayList<String>();
	    
	    for (String suite : getRunXmls()) {
	    	suites.add(suite);
	    }
	    
	    testng.setTestSuites(suites);
	    testng.setUseDefaultListeners(false);
	    testng.run();
	}
	
	public static List<String> getRunXmls() throws FileNotFoundException, IOException {
		String runXMLs = ConfigReader.getConfig("runXMLs");
		
		System.out.println("xml file is " + runXMLs);
		List<String> listOfRunXmls = new ArrayList<String>();
		
		for (String runXML : runXMLs.split(",")) {
			listOfRunXmls.add(".//src//test//resources//" + runXML.trim());
		}

		return listOfRunXmls;
	}

}
