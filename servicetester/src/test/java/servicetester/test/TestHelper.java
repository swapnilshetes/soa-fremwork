package servicetester.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Constants;
import core.base;
import utilities.StringOperations;

public class TestHelper extends base {
	public static Object[][] ReadInputData() throws IOException {
		int ColNum = 0;
		String[][] tempArr = null;
		Object[][] finalArr = null;
		Map<String, Object> map = new HashMap<String, Object>();
		List<String[]> rowList = new ArrayList<String[]>();
		int counterArrRow = 0;
		XSSFSheet ExcelWSheet = null;
		XSSFWorkbook ExcelWBook = null;

		Iterator<File> itExcelInput = FileUtils
				.iterateFiles(
						new File(Constants.CURRENT_DIRECTORY + Constants.FILE_SEPARATOR
								+ prop.getProperty(Constants.APP_TESTDATA_FOLDER) + Constants.FILE_SEPARATOR),
						null, false);

		try {
			while (itExcelInput.hasNext()) {

				FileInputStream fisDataFile = new FileInputStream(new File(((File) itExcelInput.next()).getPath()));
				ExcelWBook = new XSSFWorkbook(fisDataFile);
				ExcelWSheet = ExcelWBook.getSheetAt(0);
				XSSFRow Row = ExcelWSheet.getRow(0); // get my Row which start // from 0
				int RowNum = ExcelWSheet.getPhysicalNumberOfRows();// count my
				// number of
				// Rows
				ColNum = 7;// Row.getLastCellNum(); // get last ColNum
				tempArr = new String[RowNum - 1][ColNum]; // pass my count data in
				DataFormatter formatter = new DataFormatter();
				for (int iRowCount = 0; iRowCount < RowNum - 1; iRowCount++) // Loop work for Rows
				{
					XSSFRow row = ExcelWSheet.getRow(iRowCount + 1);

					for (int iColCount = 0; iColCount < ColNum; iColCount++) // Loop work for colNum
					{
						if (row == null)
							tempArr[iRowCount][iColCount] = "";
						else {
							XSSFCell cell = row.getCell(iColCount);
							if (cell == null)
								tempArr[iRowCount][iColCount] = ""; // if it get Null value it pass
							// no data
							else {
								String value = formatter.formatCellValue(cell);
								tempArr[iRowCount][iColCount] = value;

							}
						}

					}
					rowList.add((String[]) tempArr[iRowCount]);
					map.put(String.valueOf(counterArrRow), tempArr[iRowCount]);
					counterArrRow++;

				}

			}
			int dynamicSize = rowList.size();
			finalArr = new Object[dynamicSize][ColNum];
			int fRowCounter = 0;
			for (String[] row : rowList) {

				for (int yColounter = 0; yColounter < row.length; yColounter++) {
					finalArr[fRowCounter][yColounter] = row[yColounter].toString();
				}
				fRowCounter++;
			} // prints:
		}

		catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}

		catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
		return finalArr;

	}

	public static List<String> getExudedNodes(String customNodes) {
		List<String> excludedNodes;
		excludedNodes = new ArrayList<String>();
		if (customNodes != "") {
			String ignoreParam[] = StringOperations.trimString(customNodes).split(",");
			for (int i = 0; i < ignoreParam.length; i++) {
				excludedNodes.add(ignoreParam[i].toString().trim());
			}
		}
		return excludedNodes;

	}

}
