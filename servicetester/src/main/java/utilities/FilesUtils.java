package utilities;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;

import config.Constants;
import core.base;

public class FilesUtils extends base {
	private static Pattern NAMESPACE_PATTERN = Pattern.compile("xmlns:(ns\\d+)=\"(.*?)\"");

	public static void createDirectoryifNotExist() {

		List<String> FOLDER_LIST = Collections.unmodifiableList(new ArrayList<String>() {
			{
				add(prop.getProperty(Constants.APP_REQUEST_FOLDER));
				add(prop.getProperty(Constants.APP_RESPONSE_FOLDER));
				add(prop.getProperty(Constants.APP_REPORTS_FOLDER));
				add(prop.getProperty(Constants.APP_TESTDATA_FOLDER));
				add(prop.getProperty(Constants.APP_TEMPLATEREQUEST_FOLDER));

			}
		});

		String dirPath = null;
		List<String> listFoldersList = FOLDER_LIST;
		for (String folderName : listFoldersList) {
			dirPath = Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR + folderName;
			File dirCreate = new File(dirPath);
			if (!dirCreate.exists()) {
				dirCreate.mkdirs();
			}
		}

	}

	public static void writeToLocal(String filePath, String fileName, String textContent) {
		File directory = new File(filePath);
		try {

			if (!directory.exists()) {
				directory.mkdirs();
			}
			File file = new File(directory, fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(textContent.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String ReadFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				if (tempString.contains("{{AUTO#")) {
					String autoGenerator = tempString.substring(tempString.indexOf("{{") + 2, tempString.indexOf("}}"));
					String oldString = "{{" + autoGenerator + "}}";
					tempString = tempString.replace(oldString, MustacheHelper.getValueofAutoGenerator(autoGenerator));
				}
				laststr += tempString;

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}

	public static String readFileByStream(InputStream inputStream) {
		StringBuilder resultStr = new StringBuilder();
		BufferedReader reader = null;
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				resultStr.append(temp);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return resultStr.toString();
	}

	public static void writeToLocal(String filePath, String textContent) {
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(textContent.toCharArray());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Set<Map.Entry<Object, Object>> loadLocalProperty(String pathName) {
		FilesUtils filesUtils = new FilesUtils();
		return filesUtils.loadPropertyFile(pathName);
	}

	private Set<Map.Entry<Object, Object>> loadPropertyFile(String pathName) {
		Set<Map.Entry<Object, Object>> set = new HashSet<Entry<Object, Object>>();
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(pathName);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			Properties properties = new Properties();
			properties.load(inputStreamReader);
			set = properties.entrySet();
		} catch (IOException e) {
			System.out.println("load local property file failed");
			e.printStackTrace();
		}
		return set;
	}

	public static String getPath(String fileName) {

		String extension = getFileExtension(fileName);
		String folderName = extension.toLowerCase().equals("xlsx") || extension.toLowerCase().equals("json")
				? prop.getProperty(Constants.APP_TESTDATA_FOLDER)
				: prop.getProperty(Constants.APP_TEMPLATEREQUEST_FOLDER);
		Path pathFolder = Paths.get(Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR + folderName
				+ Constants.FILE_SEPARATOR + fileName);
		return pathFolder.toString();
	}
	/**
	 ** Expected Response creation
	 */
	public static String getPathOfExpectedTemplate(String fileName) {

		String extension = getFileExtension(fileName);
		String folderName = extension.toLowerCase().equals("xlsx") || extension.toLowerCase().equals("json")
				? prop.getProperty(Constants.APP_TESTDATA_FOLDER)
				: prop.getProperty(Constants.APP_RESPONSE_FOLDER) + Constants.FILE_SEPARATOR + prop.getProperty(Constants.APP_TEMPLATERESPONSE_FOLDER);
		Path pathFolder = Paths.get(Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR + folderName
				+ Constants.FILE_SEPARATOR + fileName);
		return pathFolder.toString();
	}

	private static String getFileExtension(String fileName) {

		int lastIndexOf = fileName.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return fileName.substring(lastIndexOf + 1);
	}

	public static boolean isFileExist(String fileName) {

		File file = new File(getPath(fileName));
		if (!file.exists()) {
			return false;
		}
		return true;
	}

	public static List compareXML(Reader source, Reader target) throws SAXException, IOException {
		// creating Diff instance to compare two XML files
		Diff xmlDiff = new Diff(source, target);

		// for getting detailed differences between two xml files
		DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff);

		return detailXmlDiff.getAllDifferences();
	}

	public static void printDifferences(List differences) {
		int totalDifferences = differences.size();
		System.out.println("===============================");
		System.out.println("Total differences : " + totalDifferences);
		System.out.println("================================");

		for (Object difference : differences) {
			System.out.println(difference);
		}
	}

}