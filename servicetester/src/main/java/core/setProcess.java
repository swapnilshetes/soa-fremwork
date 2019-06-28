package core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.util.StringUtil;

import com.ibm.wsdl.util.StringUtils;

import config.Constants.OptFileType;
import config.Constants.OptType;
import utilities.FilesUtils;
import utilities.StringOperations;
import wsdl.SoapClient;
import wsdl.SoapRequest;
import xml.Template2Xml;

public class setProcess extends Main {

	public setProcess(OptType oType, OptFileType fType) throws IOException, InterruptedException {

		FilesUtils.createDirectoryifNotExist();
		String url = prop.getProperty("WSDL_URL");
		String ops = prop.getProperty("WSDL_OPERATIONS");
		String arr[] = ops.split(",");

		SoapRequest objReq = new SoapRequest();
		Template2Xml objReqst = new Template2Xml();

		for (int i = 0; i < arr.length; i++) {

			switch (oType) {
			case read:
				String reqTemp = SoapClient.getSoapMessage(url, arr[i].trim());

				// BuildMyString.com generated code. Please enjoy your string responsibly.

				objReq.getColumnName(reqTemp);
				String ResponseString = objReq.replaceTagOfRequestProcess(reqTemp);

				objReq.createResponceXml(arr[i].trim(), ResponseString);

				if (fType.equals(OptFileType.json)) {
					objReq.createJsonWithColumn(arr[i].trim());
					System.out.println(":: Done create Request Process ::");
				} else if (fType.equals(OptFileType.xlsx)) {
					objReq.createXlsxWithColumn(arr[i].trim());
					System.out.println(":: Done create Request Process ::");
				} else {
					System.out.println("Error: Can't do read/write process");
				}
				Thread.sleep(2000);

				break;
			case write:

				switch (fType) {
				case json:
					if (FilesUtils.isFileExist(String.valueOf(arr[i].trim() + ".json"))) {
						HashMap<Integer, Map<String, Object>> excelFileMap = objReqst
								.getDataOfJson(FilesUtils.getPath(String.valueOf(arr[i].trim() + ".json")));

						for (java.util.Map.Entry<Integer, Map<String, Object>> entry : excelFileMap.entrySet()) {
							HashMap<String, Object> sample = (HashMap<String, Object>) entry.getValue();
							objReq.soapRequest(objReqst
									.prepareXML(FilesUtils.getPath(String.valueOf(arr[i].trim() + ".xml")), sample),
									String.valueOf(arr[i].trim()), entry.getKey());
							Thread.sleep(2000);
						}

					}
					System.out.println(":: Json Request process done ::");
					break;
				case xlsx:
					if (FilesUtils.isFileExist(String.valueOf(arr[i].trim() + ".xlsx"))) {
						HashMap<Integer, Map<String, Object>> excelFileMap = objReqst
								.getData(FilesUtils.getPath(String.valueOf(arr[i].trim() + ".xlsx")));

						for (java.util.Map.Entry<Integer, Map<String, Object>> entry : excelFileMap.entrySet()) {
							HashMap<String, Object> sample = (HashMap<String, Object>) entry.getValue();
							objReq.soapRequest(objReqst
									.prepareXML(FilesUtils.getPath(String.valueOf(arr[i].trim() + ".xml")), sample),
									String.valueOf(arr[i].trim()), entry.getKey());
							Thread.sleep(2000);
						}

					}

					/**
					 ** Expected Response creation
					 */
					if (FilesUtils.isFileExist(String.valueOf(arr[i].trim() + ".xlsx"))) {
						HashMap<Integer, Map<String, Object>> excelFileMap = objReqst
								.getData(FilesUtils.getPath(String.valueOf(arr[i].trim() + ".xlsx")));

						for (java.util.Map.Entry<Integer, Map<String, Object>> entry : excelFileMap.entrySet()) {
							HashMap<String, Object> sample = (HashMap<String, Object>) entry.getValue();
							System.out.println("-->" + String.valueOf(sample.get("BaselineXML")));
							if (sample.get("BaselineXML") != null && !StringOperations
									.isStringEmptyOrNull(String.valueOf(sample.get("BaselineXML")))) {
								objReq.soapResponce(
										objReqst.prepareXML(FilesUtils.getPathOfExpectedTemplate(
												sample.get("BaselineXML").toString()), sample),
										String.valueOf(arr[i].trim()), entry.getKey());
							}

						}

					}
					System.out.println(":: Xlsx Request process done ::");
					break;
				default:
					break;
				}
				System.out.println(":: Done write a SOAP responce Process ::");
				break;
			default:
				break;
			}

		}
	}

}
