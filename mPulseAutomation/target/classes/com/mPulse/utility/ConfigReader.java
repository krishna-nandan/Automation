package com.mPulse.utility;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigReader {

	static Logger logger = Logger.getLogger(ConfigReader.class);
	
	private static String readConfig(String fileName, String config) {
		Properties property = new Properties();
		try {
			property.load(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return property.getProperty(config);
	}
	
	public static String getConfig(String config) {
		
		if (readConfig("common.properties", "ENV").equalsIgnoreCase("qa2")) {
			String configValue = readConfig("qa2.properties", config);
			System.out.println("Config of "+config+" - "+configValue);
			return configValue;
		}
		if (readConfig("common.properties", "ENV").equalsIgnoreCase("uat")) {
			return readConfig("uat.properties", config);
		}
		if (readConfig("common.properties", "ENV").equalsIgnoreCase("stage")) {
			return readConfig("stage.properties", config);
		}
		if (readConfig("common.properties", "ENV").equalsIgnoreCase("prod")) {
			return readConfig("prod.properties", config);
		}
		else return null;		
	}
}
