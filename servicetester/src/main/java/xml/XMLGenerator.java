package xml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import config.Constants;
import config.Constants.XMLtype;
import core.base;
import date.DateUtils;
import utilities.FilesUtils;
import utilities.StringOperations;

public class XMLGenerator extends base {
	public static boolean generateXMLFile(String xmlString, XMLtype xmlType, String optName, int index) {
		String fileName = null;
		boolean flagIsCreated = false;
		if (!StringOperations.isStringEmptyOrNull(xmlString)) {
			try {
				// xmlString = StringEscapeUtils.unescapeHtml4(xmlString).trim();
				xmlString = StringOperations.removeStringExtraSpaces(xmlString);

				if (xmlType.equals(XMLtype.RESPONSE)) {

					fileName = Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
							+ prop.getProperty(Constants.APP_RESPONSE_FOLDER) + Constants.FILE_SEPARATOR + prop.getProperty(Constants.APP_ACTUAL_RESPONSE_FOLDER) + Constants.FILE_SEPARATOR 
							+ String.format(Constants.SOAP_RESPONSE_XML_FILENAME,
									String.format("%s_%s", optName, index));
				} else {
					//
					fileName = Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
							+ prop.getProperty(Constants.APP_REQUEST_FOLDER) + Constants.FILE_SEPARATOR + String.format(
									Constants.SOAP_REQUEST_XML_FILENAME, String.format("%s_%s", optName, index));
				}

				FilesUtils.writeToLocal(fileName, xmlString);

			} catch (Exception e) {
				e.printStackTrace();
			}

			Path fileExistPath = Paths.get(fileName);
			flagIsCreated = Files.exists(fileExistPath) ? true : false;
			return flagIsCreated;
		}
		return flagIsCreated;
	}
	
	/**
	 ** Expected Response creation
	 */
	public static boolean generateExpectedXMLFile(String xmlString, XMLtype xmlType, String optName, int index) {
		String fileName = null;
		boolean flagIsCreated = false;
		if (!StringOperations.isStringEmptyOrNull(xmlString)) {
			try {
				// xmlString = StringEscapeUtils.unescapeHtml4(xmlString).trim();
				xmlString = StringOperations.removeStringExtraSpaces(xmlString);

				if (xmlType.equals(XMLtype.RESPONSE)) {

					fileName = Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
							+ prop.getProperty(Constants.APP_RESPONSE_FOLDER) + Constants.FILE_SEPARATOR
							+ prop.getProperty(Constants.APP_EXPECTED)  + Constants.FILE_SEPARATOR + String.format(Constants.SOAP_RESPONSE_XML_FILENAME,
									String.format("%s_%s", optName, index));
				} 
				FilesUtils.writeToLocal(fileName, xmlString);

			} catch (Exception e) {
				e.printStackTrace();
			}

			Path fileExistPath = Paths.get(fileName);
			flagIsCreated = Files.exists(fileExistPath) ? true : false;
			return flagIsCreated;
		}
		return flagIsCreated;
	}
	
	

}
