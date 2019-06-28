package wsdl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import config.Constants;
import config.Constants.XMLtype;
import core.base;
import utilities.StringOperations;
import xml.XMLGenerator;

public class SoapRequest extends base {

	static String responceString = "";
	List<String> tagList;
	ArrayList<String> arrTagListModofied;

	public void soapRequest(String xmlData, String optName, int index) {
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			XMLGenerator.generateXMLFile(xmlData, XMLtype.REQUEST, optName, index);
			SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(xmlData), prop.getProperty("WSDL_URL"));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapResponse.writeTo(out);
			System.out.println(soapResponse.toString());
			XMLGenerator.generateXMLFile(out.toString(), XMLtype.RESPONSE, optName, index);
			soapConnection.close();
		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			e.printStackTrace();
		}
	}

	/**
	 ** Expected Response creation
	 */
	public void soapResponce(String xmlData, String optName, int index) {
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			XMLGenerator.generateExpectedXMLFile(xmlData, XMLtype.RESPONSE, optName, index);
			soapConnection.close();
		} catch (Exception e) {
			System.err.println(
					"\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
			e.printStackTrace();
		}
	}

	private static SOAPMessage createSOAPRequest(String xmlData) throws Exception {

		InputStream is = new ByteArrayInputStream(xmlData.getBytes());
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);
		return soapMessage;
	}

	public List<String> getColumnName(String resString) {
		this.tagList = new ArrayList<String>();
		String regTags = "\\?<\\/([^>]+)>"; // Tag 1
		// String re2 = "([^<]*)"; // Variable Name 1
		// String re3 = re1;

		Pattern p = Pattern.compile(regTags, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		java.util.regex.Matcher m = p.matcher(resString);
		while (m.find()) {
			String tag1 = m.group(1);
			this.tagList.add(tag1.toString().trim());
		}

		return tagList;
	}

	public String replaceTagOfRequestProcess(String resString) {
		arrTagListModofied = StringOperations.addPostFixCounterListItem(tagList);

		for (int i = 0; i < this.tagList.size(); i++) {

			resString = resString.replaceFirst("\\?</" + this.tagList.get(i).toString().trim() + ">",
					"{{" + arrTagListModofied.get(i).toString().trim() + "}}</" + this.tagList.get(i).toString().trim()
							+ ">");

		}

		return resString.toString();
	}

	public void createResponceXml(String xmlfile, String responceString) throws IOException {

		File file = new File(Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
				+ prop.getProperty(Constants.APP_TEMPLATEREQUEST_FOLDER) + Constants.FILE_SEPARATOR
				+ String.format(Constants.SOAP_REQUEST_TEMPLATE_XML, xmlfile));
		if (file.createNewFile()) {
			System.out.println(xmlfile + ":: File is created!");
		} else {
			System.out.println(xmlfile + ":: File already exists.");
		}

		// Write Content
		FileWriter writer = new FileWriter(file);
		writer.write(responceString);
		writer.close();

	}

	/**
	 * @param xlsxfile
	 * @throws IOException
	 */
	public void createXlsxWithColumn(String xlsxfile) throws IOException {

		File fileObj = new File(
				Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR + prop.getProperty(Constants.APP_TESTDATA_FOLDER)
						+ Constants.FILE_SEPARATOR + String.format(Constants.SOAP_PARAMETER_EXCEL, xlsxfile));
		if (fileObj.createNewFile()) {

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet(xlsxfile);

			Row headerRow = sheet.createRow(0);

			String arrPreHeader[] = prop.getProperty("PREDDEIFIND_HEADER_LIST").split(",");
			for (int iHeader = 0; iHeader < arrPreHeader.length; iHeader++) {
				Cell cell = headerRow.createCell(iHeader);
				cell.setCellValue(String.valueOf(arrPreHeader[iHeader]));
			}

			for (int i = 0; i < this.arrTagListModofied.size(); i++) {

				Cell cell = headerRow.createCell(arrPreHeader.length + i);
				cell.setCellValue(this.arrTagListModofied.get(i));

			}

			XSSFDataFormat fmt = workbook.createDataFormat();
			XSSFCellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat("Text"));
			sheet.setDefaultColumnStyle(0, textStyle);

			FileOutputStream fos = new FileOutputStream(fileObj);

			workbook.write(fos);

			fos.close();
			workbook.close();
		}
	}

	/**
	 * @param jsonfile
	 * @throws IOException
	 */
	public void createJsonWithColumn(String jsonfile) throws IOException {

		JSONObject prepareResponceJson = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < this.tagList.size(); i++) {
			prepareResponceJson.put(this.tagList.get(i), "");

		}
		arr.add(prepareResponceJson);
		// Write JSON file

		// Writing to a file
		File jsonFile = new File(
				Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR + prop.getProperty(Constants.APP_TESTDATA_FOLDER)
						+ Constants.FILE_SEPARATOR + String.format(Constants.SOAP_PARAMETER_JSON, jsonfile));

		jsonFile.createNewFile();
		FileWriter fileWriter = new FileWriter(jsonFile);
		fileWriter.write(JSON.toJSONString(arr));
		fileWriter.flush();
		fileWriter.close();

	}
}
