package utilities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import core.base;
import date.DateUtils;
import freemarker.template.utility.DateUtil;

public class MustacheHelper extends base {
	public static String getValueofAutoGenerator(String paramName) {
		// AUTO#STRING#7
		int arrGenLen = 0;
		String returnAutoValue = null;

		if (!StringOperations.isStringEmptyOrNull(paramName)) {
			try {

				if (paramName == null) {
					return returnAutoValue;
				} else {
					String arrGenerator[] = paramName.split("#");
					arrGenLen = arrGenerator.length;

					if (arrGenLen > 0) {
						if (paramName.toLowerCase().contains("string")) {
							// switch statement with int data type
							switch (arrGenLen) {
							case 1:
							case 2:// If Size one
								returnAutoValue = getRamdomString(8);
								break;
							case 3:// If Size Three
								returnAutoValue = getRamdomString(Integer.parseInt(arrGenerator[2]));
								break;
							default:
								break;
							}

						}

						else if (paramName.toLowerCase().contains("alphanumeric")) {
							switch (arrGenLen) {
							case 1:
							case 2:// If Size one
								returnAutoValue = getAlphanumericRamdomString(8);
								break;
							case 3:// If Size Three
								returnAutoValue = getAlphanumericRamdomString(Integer.parseInt(arrGenerator[2]));
								break;
							default:
								returnAutoValue = getAlphanumericRamdomString(8);
								break;
							}

						} else if (paramName.toLowerCase().contains("decimal")) {
							switch (arrGenLen) {
							case 1:
							case 2:// If Size one
								returnAutoValue = getDecimalRandom(5, 40);
								break;
							case 3:// If Size Three
								returnAutoValue = getDecimalRandom(Integer.parseInt(arrGenerator[2]), 40);
								break;
							case 4:// If Size Three
								returnAutoValue = getDecimalRandom(Integer.parseInt(arrGenerator[2]),
										Integer.parseInt(arrGenerator[3]));
								break;
							default:
								returnAutoValue = getDecimalRandom(5, 40);
								break;
							}

						} else if (paramName.toLowerCase().contains("negativedec")) {
							switch (arrGenLen) {
							case 1:
							case 2:// If Size one
								returnAutoValue = getNegativeDecimalRandom(5, 40);
								break;
							case 3:// If Size Three
								returnAutoValue = getNegativeDecimalRandom(Integer.parseInt(arrGenerator[2]), 40);
								break;
							case 4:// If Size Three
								returnAutoValue = getNegativeDecimalRandom(Integer.parseInt(arrGenerator[2]),
										Integer.parseInt(arrGenerator[3]));
								break;
							default:
								returnAutoValue = getNegativeDecimalRandom(5, 40);
								break;
							}

						} else if (paramName.toLowerCase().contains("integer")) {
							// {{AUTO#DATETIME#}}
							switch (arrGenLen) {
							case 1:
							case 2:// If Size one
								returnAutoValue = getRandomDigits(1);
								break;
							case 3:// If Size Three
								returnAutoValue = getRandomDigits(Integer.parseInt(arrGenerator[2]));
								break;

							default:
								returnAutoValue = getRandomDigits(1);
								break;
							}

						} else if (paramName.toLowerCase().contains("datetime")) {
							// AUTO#DATETIME#yyyy-MM-dd'T'HH:mm:ss#30
							switch (arrGenLen) {
							case 1:
							case 2:// If Size one
								returnAutoValue = getDateRandom(null, null);
								break;
							case 3:// If Size Three
								returnAutoValue = getDateRandom(null, String.valueOf(arrGenerator[2]));
								break;

							default:
								returnAutoValue = getDateRandom(null, null);
								break;
							}

						}

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnAutoValue;

	}

	public static String getRamdomString(int strLen) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder(strLen);
		Random random = new Random();
		for (int i = 0; i < strLen; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();

	}

	public static String getAlphanumericRamdomString(int strLen) {
		char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		StringBuilder sb = new StringBuilder(strLen);
		Random random = new Random();
		for (int i = 0; i < strLen; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();

	}

	public static String getDecimalRandom(double min, double max) {
		DecimalFormat df = new DecimalFormat("0.##");
		double x = (Math.random() * ((max - min) + 1)) + min;
		return String.valueOf(df.format(x));
	}

	public static String getNegativeDecimalRandom(double min, double max) {
		DecimalFormat df = new DecimalFormat("0.##");
		double x = (Math.random() * (((max - min) + 50)) - min) * (-1);
		return String.valueOf(df.format(x));
	}

	public static String getDateRandom(Date s, String format1)
			throws NumberFormatException, ParseException, DatatypeConfigurationException {

		// AUTO#DATETIME#yyyy-MM-dd'T'HH:mm:ss#30
		s = s == null ? new Date() : s;
		format1 = StringUtils.isBlank(format1) ? prop.getProperty("DATE_DEFAULT_FORMAT") : format1;
		//GregorianCalendar gCal = new GregorianCalendar();
		//gCal.setTime(s);
		//XMLGregorianCalendar gDateUnformated = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal);
		//System.out.println("Unformatted date: " + gDateUnformated);
		//System.out.println();

		String FORMATER = format1;//

		DateFormat format = new SimpleDateFormat(format1);

		Date date = new Date();
		XMLGregorianCalendar gDateFormatted1 = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(format.format(date));
		return gDateFormatted1.toString();

	}

	public static String getRandomDigits(int n) {
		int m = (int) Math.pow(10, n - 1);
		return String.valueOf(m + new Random().nextInt(9 * m));
	}

	public static Date parse(String date, String format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}

		org.joda.time.format.DateTimeFormatter df = DateTimeFormat.forPattern(format);
		return DateTime.parse(date, df).toDate();
	}

}
