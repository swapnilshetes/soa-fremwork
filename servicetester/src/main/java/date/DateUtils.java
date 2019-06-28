package date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import core.base;
import utilities.StringOperations;

public class DateUtils extends base {

	public static String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public static String[] DAY_OF_WEEK = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
			"Sunday" };

	public static Date parse(String date) {
		return parse(date, DEFAULT_FORMAT);
	}

	public static Date parse(String date, String format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		org.joda.time.format.DateTimeFormatter df = DateTimeFormat.forPattern(format);
		return DateTime.parse(date, df).toDate();
	}

	public static String formatCurDate() {
		return format(new Date(), DEFAULT_FORMAT);
	}

	public static String format(Date date) {
		return format(date, DEFAULT_FORMAT);
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		return new DateTime(date.getTime()).toString(format);
	}

	public static String format(Long milliseconds, String format) {
		if (milliseconds == null)
			return "";
		return new DateTime(milliseconds).toString(format);
	}

	public static Date plusYeas(Date date, int years) {
		return new DateTime(date.getTime()).plusYears(years).toDate();
	}

	public static Date plusDays(Date date, int days) {
		return new DateTime(date.getTime()).plusDays(days).toDate();
	}

	public Date plusMonths(Date date, int months) {
		return new DateTime(date.getTime()).plusMonths(months).toDate();
	}

	public static int daysBetween(Date start, Date end) {
		LocalDate startLd = new LocalDate(start.getTime());
		LocalDate endLd = new LocalDate(end.getTime());
		return Days.daysBetween(startLd, endLd).getDays();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		String d1 = format(date1, "yyyy-MM-dd");
		String d2 = format(date2, "yyyy-MM-dd");
		return isSameDay(d1, d2);
	}

	public static boolean isSameDay(String date1, String date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return StringUtils.equals(date1, date2);
	}

	public static boolean isSameDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		return isSameDay(date1, date2);
	}

	public static Long getCurTimeNum() {
		return Long.valueOf(format(new Date(),
				StringOperations.isStringEmptyOrNull(prop.getProperty("DATE_DEFAULT_FORMAT")) ? "yyyy-MM-dd'T'HH:mm:ss"
						: prop.getProperty("DATE_DEFAULT_FORMAT")));
	}

	public static int getCurrentSecond() {
		Calendar calendar = Calendar.getInstance();
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		int currentMinute = calendar.get(Calendar.MINUTE);
		int currentSecond = currentHour * 3600 + currentMinute * 60;
		return currentSecond;
	}

	public static boolean isBetween(Date dt, Date startDt, Date endDt) {
		long time = dt.getTime();
		return (startDt.getTime() <= time && time <= endDt.getTime());
	}

	public static boolean isBetween(Date startDt, Date endDt) {
		return isBetween(new Date(), startDt, endDt);
	}

	public static boolean isNotBetween(Date startDt, Date endDt) {
		return !isBetween(startDt, endDt);
	}

	public static boolean isGt(Date date1, Date date2) {
		return (date1.getTime() > date2.getTime());
	}

	public static boolean isLt(Date date1, Date date2) {
		return !isGt(date1, date2);
	}

	public static int dayOfWeek(Date date) {
		return new DateTime(date.getTime()).getDayOfWeek();
	}

	public static String getDayOfWeek(Date date) {
		Integer day = dayOfWeek(date);
		return DAY_OF_WEEK[day - 1];
	}

	public static Date getStartTimeOfMonth() {
		YearMonth yearMonth = YearMonth.now();
		int month = yearMonth.getMonthValue();
		int year = yearMonth.getYear();
		return getBeginTimeOfYearMonth(year, month);
	}

	public static Date getEndTimeOfMonth() {
		YearMonth yearMonth = YearMonth.now();
		int month = yearMonth.getMonthValue();
		int year = yearMonth.getYear();
		return getEndTimeOfYearMonth(year, month);
	}

	public static Date getBeginTimeOfYearMonth(int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		java.time.LocalDate localDate = yearMonth.atDay(1);
		LocalDateTime startOfDay = localDate.atStartOfDay();
		ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.of("Asia/Shanghai"));
		return Date.from(zonedDateTime.toInstant());
	}

	public static Date getEndTimeOfYearMonth(int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		java.time.LocalDate endOfMonth = yearMonth.atEndOfMonth();
		LocalDateTime localDateTime = endOfMonth.atTime(23, 59, 59, 999);
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
		return Date.from(zonedDateTime.toInstant());
	}

	public static String getDateString() {

		return new SimpleDateFormat("yyyy-MM-dd'T'HHmmss").format(new Date());
	}

	public static Long getCurTimeNumPlusDays(int plusDays) {
		return Long.valueOf(format(plusDays(new Date(), plusDays),
				StringOperations.isStringEmptyOrNull(prop.getProperty("DATE_DEFAULT_FORMAT")) ? "yyyy-MM-dd'T'HH:mm:ss"
						: prop.getProperty("DATE_DEFAULT_FORMAT")));
	}

	public static Long getParDateTimeNum(String date) {
		long milliseconds = 0;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = f.parse(date);
			milliseconds = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return milliseconds;
	}

	public static String getRandomDateBetweenTwoDates(long begin, long end) {
		long diff = end - begin + 1;
		Date randDate = new Date(begin + (long) (Math.random() * diff));
		return DateUtils.format(randDate,
				StringOperations.isStringEmptyOrNull(prop.getProperty("DATE_DEFAULT_FORMAT")) ? "yyyy-MM-dd'T'HH:mm:ss"
						: prop.getProperty("DATE_DEFAULT_FORMAT"));

	}

}
