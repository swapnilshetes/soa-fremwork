package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StringOperations {
	public static boolean isStringEmptyOrNull(String strParam) {

		if (strParam == null || strParam.trim().isEmpty()) {
			return true;
		}

		for (int i = 0; i < strParam.length(); i++) {
			if (!Character.isWhitespace(strParam.charAt(i))) {
				return false;
			}
		}

		return true;

	}

	public static String removeStringExtraSpaces(String strParam) {
		if (!isStringEmptyOrNull(strParam))
			return StringUtils.normalizeSpace(strParam);
		else
			return strParam;
	}

	public static String trimString(String strParam) {
		if (!isStringEmptyOrNull(strParam))
			return strParam.trim();
		else
			return strParam;
	}

	public static ArrayList<String> addPostFixCounterListItem(List<String> tagList) {

		ArrayList<String> tagNameList = new ArrayList<String>();
		Map<String, Integer> counters = new HashMap<String, Integer>();
		for (String tagString : tagList) {
			Integer count = counters.get(tagString);
			boolean toConcat = true;
			if (count == null) {
				count = 0;
				if (Collections.frequency(tagList, tagString) == 1) {
					toConcat = false;
				}
			}
			count += 1;
			counters.put(tagString, count);
			if (toConcat) {
				tagNameList.add(tagString.concat("_").concat("" + count));
			} else {
				tagNameList.add(tagString);
			}
		}

		return tagNameList;
	}

}
