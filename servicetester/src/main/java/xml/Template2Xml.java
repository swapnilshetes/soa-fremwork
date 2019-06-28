package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

import utilities.FilesUtils;

public class Template2Xml {

	public String prepareXML(String filePath, HashMap<String, Object> sample) throws IOException {

		String template = FilesUtils.ReadFile(filePath);
		StringWriter writer = new StringWriter();
		if (template != "") {
			MustacheFactory mf = new DefaultMustacheFactory();
			Mustache mustache = mf.compile(new StringReader(template), "template");
			mustache.execute(writer, sample);
		}
		return writer.getBuffer().toString().trim();
	}

	public HashMap<Integer, Map<String, Object>> getData(String filePath) {

		HashMap<Integer, Map<String, Object>> excelFileMap = new HashMap<Integer, Map<String, Object>>();
		try {

			FileInputStream file = new FileInputStream(new File(filePath));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();

			int rowNum = sheet.getLastRowNum() + 1;
			int colNum = sheet.getRow(0).getLastCellNum();
			// System.out.println("rowNum" + rowNum + " colNum" + colNum);

			Map<Integer, String> colMapByName = new HashMap<Integer, String>();

			for (int j = 0; j < colNum; j++) {
				colMapByName.put(j, sheet.getRow(0).getCell(j).toString());
			}

			for (int rowCnt = 1; rowCnt < rowNum; rowCnt++) {

				XSSFCell xCell = (XSSFCell) sheet.getRow(rowCnt).getCell(0);

				if (xCell == null || xCell.getCellTypeEnum() == CellType.BLANK) {
					break;
				}

				Map<String, Object> dataMap = new HashMap<String, Object>();

				for (int cellItr = 0; cellItr < colNum; cellItr++) {

					XSSFCell cell = (XSSFCell) sheet.getRow(rowCnt).getCell(cellItr);

					if (cell == null || cell.getCellTypeEnum() == null) {
						continue;
					}

					DataFormatter formatter = new DataFormatter();
					switch (cell.getCellTypeEnum()) {

					case NUMERIC:
						dataMap.put(colMapByName.get(cellItr).toString(), formatter.formatCellValue(cell));
						break;
					case STRING:
						dataMap.put(colMapByName.get(cellItr).toString(), cell.getStringCellValue());
						break;

					case BLANK:
						dataMap.put(colMapByName.get(cellItr).toString(), "");
						break;
					case BOOLEAN:
						dataMap.put(colMapByName.get(cellItr).toString(), formatter.formatCellValue(cell));
						break;
					case _NONE:
						break;

					default:
						if (cell.getCellTypeEnum() == null)
							break;
					}
				}
				excelFileMap.put(rowCnt, dataMap);

			}
			file.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return excelFileMap;
	}

	public HashMap<Integer, Map<String, Object>> getDataOfJson(String filePath) throws IOException {
		HashMap<Integer, Map<String, Object>> excelFileMap = new HashMap<Integer, Map<String, Object>>();

		File a = FileUtils.getFile(filePath);
		String str = FileUtils.readFileToString(a);

		JSONArray jsonArray = JSONArray.parseArray(str);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = (JSONObject) jsonArray.get(i);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : obj.entrySet()) {
				dataMap.put(entry.getKey().toString(), entry.getValue().toString());
			}
			excelFileMap.put(i, dataMap);
		}
		return excelFileMap;
	}
}
