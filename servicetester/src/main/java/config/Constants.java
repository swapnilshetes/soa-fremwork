package config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.base;

public class Constants extends base {

	// PRIVATE //

	/**
	 * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, and
	 * so on. Thus, the caller should be prevented from constructing objects of this
	 * class, by declaring this private constructor.
	 */
	private Constants() {
		// this prevents even the native class from
		// calling this ctor as well :
		throw new AssertionError();
	}

	public enum XMLtype {

		REQUEST(0), RESPONSE(1);

		private int xmlTypeValue;

		private XMLtype(int xmlTypeValue) {
			this.xmlTypeValue = xmlTypeValue;
		}

	}

	public enum OptType {

		read(0), write(1);

		private int optTypeValue;

		private OptType(int optTypeValue) {
			this.optTypeValue = optTypeValue;
		}

	}

	public enum OptFileType {

		json(0), xlsx(1);

		private int optfileTypeValue;

		private OptFileType(int optfileTypeValue) {
			this.optfileTypeValue = optfileTypeValue;
		}

	}

	public static final int CODE_BAD_REQUEST = 400;
	public static final int CODE_METHOD_NOT_ALLOWD = 405;
	public static final int CODE_UNSUPPORTED_MEDIATYPE = 415;
	public static final int CODE_INTERNAL_ERROR = 500;
	public static final int CODE_OK = 200;
	public static final int CODE_ACCEPTED = 202;
	public static final boolean PASSES = true;
	public static final boolean FAILS = false;
	public static final boolean SUCCESS = true;
	public static final boolean FAILURE = false;
	public static final int NOT_FOUND = -1;
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String PATH_SEPARATOR = System.getProperty("path.separator");
	public static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
	public static final String PROPERTYFILE = "config.properties";
	public static final String APP_REQUEST_FOLDER = "APP_REQUEST_FOLDER";
	public static final String APP_RESPONSE_FOLDER = "APP_RESPONSE_FOLDER";
	public static final String APP_REPORTS_FOLDER = "APP_REPORTS_FOLDER";
	public static final String APP_ACTUAL_RESPONSE_FOLDER = "APP_ACTUAL_RESPONSE_FOLDER";
	
	public static final String APP_TEMPLATEREQUEST_FOLDER = "APP_TEMPLATEREQUEST_FOLDER";
	public static final String APP_TESTDATA_FOLDER = "APP_TESTDATA_FOLDER";
	public static final String APP_RESOURCES = "APP_RESOURCES";
	public static final String SOAP_RESPONSE_XML_FILENAME = "SoapResponse_%s.xml";
	public static final String SOAP_REQUEST_XML_FILENAME = "SoapRequest_%s.xml";
	public static final String SOAP_PARAMETER_EXCEL = "%s.xlsx";
	public static final String SOAP_REQUEST_TEMPLATE_XML = "%s.xml";
	public static final String SOAP_TEST_REPORT_HTML = "%s.html";
	public static final String EXTENT_REPORT_CONFIG = "extent-config.xml";
	public static final String SOAP_PARAMETER_JSON = "%s.json";
	
	public static final String APP_TEMPLATERESPONSE_FOLDER = "APP_TEMPLATERESPONSE_FOLDER";
	public static final String APP_EXPECTED = "APP_EXPECTED";
}
