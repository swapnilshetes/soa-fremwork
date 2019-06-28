package servicetester.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xmlunit.XMLUnitException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import com.relevantcodes.extentreports.LogStatus;

import config.Constants;
import core.Main;
import core.base;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.Logger;

public class DataDriverTest extends base {

	String responceFolerPath = Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
			+ prop.getProperty(Constants.APP_RESPONSE_FOLDER) + Constants.FILE_SEPARATOR
			+ prop.getProperty(Constants.APP_ACTUAL_RESPONSE_FOLDER) + Constants.FILE_SEPARATOR;

	String expectedResponceFolerPath = Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
			+ prop.getProperty(Constants.APP_RESPONSE_FOLDER) + Constants.FILE_SEPARATOR
			+ prop.getProperty(Constants.APP_EXPECTED);
	String requestFolerPath = Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
			+ prop.getProperty(Constants.APP_REQUEST_FOLDER) + Constants.FILE_SEPARATOR;
	List<String> excludedNodes;

	public DataDriverTest() {

	}

	@BeforeClass
	public void beforeTest(ITestContext tContext) throws Exception {
		Logger.logInfo(DataDriverTest.class, "---" + tContext.getName() + "start" + "---");

	}

	@DataProvider
	public Object[][] inputDataProvider() throws Exception {
		Object[][] testObjArray = TestHelper.ReadInputData();
		// System.out.println(Arrays.deepToString(testObjArray));
		return testObjArray;
	}

	@BeforeMethod()
	public void beforeMethod(Method method) {
		test = extent.startTest((this.getClass().getSimpleName() + " :: " + method.getName()), method.getName());
		test.assignCategory("Sanity  :: " + "env" + " :: API VERSION - " + "ver"); // Test

	}

	@Test(dataProvider = "inputDataProvider")
	public void soaprequest_response_statuscode_validation(String SrNo, String RequestXML, String ResponsetXML,
			String BaselineXML, String FlagRW, String IgnoredNodesList, String FlagBatch) throws IOException {

		FileInputStream fisData = new FileInputStream(Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
				+ prop.getProperty(Constants.APP_REQUEST_FOLDER) + Constants.FILE_SEPARATOR + RequestXML);

		Response response = RestAssured.given().contentType("text/xml").and().body(IOUtils.toString(fisData, "UTF-8"))
				.when().post(prop.getProperty("WSDL_ENDPOINT")).then().extract().response();

		if (response.statusCode() == 200) {

			test.log(LogStatus.PASS,
					"\n<br><span style='color: #009406;font-size: 1.4em;'>Filen name ::</span><span style='font-size: 0.9em;color: #fb9701;'> "
							+ RequestXML + "</span>"
							+ "\n<br> <span style='color: #009406;font-size: 1.4em;'>Status Code ::</span><span style='font-size: 0.9em;color: #fb9701;'> "
							+ response.statusCode() + "</span>"
							+ "\n<br><span style='color: #009406;font-size: 1.4em;'>Header ::</span> <span style='font-size: 0.9em;color: #fb9701;'>"
							+ response.getHeaders() + "</span>"

			);
		} else {
			test.log(LogStatus.FAIL,
					"\n<br><span style='color: #ff0000;font-size: 1.4em;'>Filen name ::</span><span style='font-size: 0.9em;color: #1e792a;'> "
							+ RequestXML + "</span>"
							+ "\n<br> <span style='color: #ff0000;font-size: 1.4em;'>Status Code ::</span><span style='font-size: 0.9em;color: #1e792a;'> "
							+ response.statusCode() + "</span>"
							+ "\n<br> <span style='color: #ff0000;font-size: 1.4em;'>Header ::</span><span style='font-size: 0.9em;color: #1e792a;'>"
							+ response.getHeaders() + "</span>");
		}

	}

	@Test(dataProvider = "inputDataProvider")
	public void compare_responce_expected_with_actual_soapresponce_first(String SrNo, String RequestXML,
			String ResponsetXML, String BaselineXML, String FlagRW, String IgnoredNodesList, String FlagBatch)
			throws FileNotFoundException {
		String expected_path = expectedResponceFolerPath + Constants.FILE_SEPARATOR + ResponsetXML;
		String actual_path = responceFolerPath + ResponsetXML;

		try {

			Diff myDiffSimilar = DiffBuilder.compare(Input.fromFile(expected_path))
					.withTest(Input.fromFile(actual_path))
					.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)).checkForSimilar().build();
			Assert.assertFalse(myDiffSimilar.hasDifferences(), "XML similar " + myDiffSimilar.toString());
			test.log(LogStatus.PASS,
					"XML similar ::" + myDiffSimilar.toString() + "<br> Expected File : <a target='_blank' href='"
							+ expected_path + "'> Link </a> " + " Actual File : <a target='_blank' href='" + actual_path
							+ "'> Link </a>");
		} catch (AssertionError ar) {

			test.log(LogStatus.FAIL,
					"XML not Similer ::" + ar.getMessage() + "<br> Expected File : <a target='_blank' href='"
							+ expected_path + "'> Link </a> " + " Actual File : <a target='_blank' href='" + actual_path
							+ "'> Link </a>");
		} catch (XMLUnitException ex) {

			test.log(LogStatus.FAIL,
					"XML not Similer ::" + ex.getMessage() + "<br> Expected File : <a target='_blank' href='"
							+ expected_path + "'> Link </a> " + " Actual File : <a target='_blank' href='" + actual_path
							+ "'> Link </a>");

		} catch (Exception e) {

			test.log(LogStatus.FAIL,
					"XML not Similer ::" + e.getMessage() + "<br> Expected File : <a target='_blank' href='"
							+ expected_path + "'> Link </a> " + " Actual File : <a target='_blank' href='" + actual_path
							+ "'> Link </a>");
		}
	}

	@Test(dataProvider = "inputDataProvider")
	public void soaprequest_response_with_ignore_nodes_validation(String SrNo, String RequestXML, String ResponseXML,
			String BaselineXML, String FlagRW, String IgnoredNodesList, String FlagBatch) throws FileNotFoundException {

		String expected_path = expectedResponceFolerPath + Constants.FILE_SEPARATOR + ResponseXML;
		String actual_path = responceFolerPath + ResponseXML;

		try {
			// .withNodeFilter(node -> !node.getNodeName().equals("listLatLonOut"))
			Diff myDiffSimilar = DiffBuilder.compare(Input.fromFile(expected_path))
					.withTest(Input.fromFile(actual_path))
					.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
					.withNodeFilter(
							x -> !TestHelper.getExudedNodes(IgnoredNodesList).contains(x.getNodeName().toString()))
					.checkForSimilar().build();

			assertFalse(myDiffSimilar.hasDifferences(),
					"XML similar " + myDiffSimilar.toString() + "<br> Expected File : <a target='_blank' href='"
							+ expected_path + "'> Link </a> " + " Actual File : <a target='_blank' href='" + actual_path
							+ "'> Link </a>");
			test.log(LogStatus.PASS,
					"XML is Similer ::" + myDiffSimilar.toString() + "<br>  <a target='_blank' href='" + expected_path
							+ "'> Expected </a> | " + " <a target='_blank' href='" + actual_path + "'> Actual </a>");
		} catch (AssertionError ar) {

			test.log(LogStatus.FAIL,
					"XML not Similer ::" + ar.getMessage() + "<br><a target='_blank' href='" + expected_path
							+ "'> Expected </a> | " + "<a target='_blank' href='" + actual_path + "'> Actual </a>");
		} catch (Exception ex) {

			test.log(LogStatus.FAIL,
					"XML not Similer ::" + ex.getMessage() + "<br> Expected File : <a target='_blank' href='"
							+ expected_path + "'> Link </a> " + " Actual File : <a target='_blank' href='" + actual_path
							+ "'> Link </a>");

		}
	}

	// Test Case Reporting Ends Here
	@AfterMethod()
	public void afterMethod() {
		extent.endTest(test);
		extent.flush();
	}

	@AfterTest()
	public void afterTest(ITestContext tContext) {
		Logger.logInfo(DataDriverTest.class, "---" + tContext.getName() + "end" + "---");

	}

	@AfterSuite()
	public void afterSuite() {
		extent.close(); // close the Test Suite
	}

	public static String escape(String s) {
		StringBuilder builder = new StringBuilder();
		boolean previousWasASpace = false;
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				if (previousWasASpace) {
					builder.append("&nbsp;");
					previousWasASpace = false;
					continue;
				}
				previousWasASpace = true;
			} else {
				previousWasASpace = false;
			}
			switch (c) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '"':
				builder.append("&quot;");
				break;
			case '\n':
				builder.append("<br>");
				break;
			// We need Tab support here, because we print StackTraces as HTML
			case '\t':
				builder.append("&nbsp; &nbsp; &nbsp;");
				break;
			default:
				if (c < 128) {
					builder.append(c);
				} else {
					builder.append("&#").append((int) c).append(";");
				}
			}
		}
		return builder.toString();
	}
}