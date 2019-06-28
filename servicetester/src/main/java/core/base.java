package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import config.Constants;
import date.DateUtils;
import utilities.Logger;

public class base {

	public static Properties prop = null;
	public static ExtentReports extent;
	public static ExtentTest test;

	public base() {
		prop = loadProperties();
		loadExtentConfiguration();

	}

	public static Properties loadProperties() {

		Properties prop = new Properties();
		try {
			Logger.logInfo(base.class, "BASE START POINT");
			FileInputStream ipStream = new FileInputStream(
					Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR + Constants.PROPERTYFILE);
			prop.load(ipStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;

	}

	public void loadExtentConfiguration() {
		extent = new ExtentReports(Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
				+ prop.getProperty(Constants.APP_REPORTS_FOLDER) + Constants.FILE_SEPARATOR
				+ String.format(Constants.SOAP_TEST_REPORT_HTML, DateUtils.getDateString()), false);

		extent.loadConfig(new File(
				Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR + prop.getProperty(Constants.APP_RESOURCES)
						+ Constants.FILE_SEPARATOR + Constants.EXTENT_REPORT_CONFIG));
		// It will provide Execution Machine Information
		extent.addSystemInfo("Environment", "QA-Sanity");
	}
}
