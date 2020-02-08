package zplum.tools._ancestor;

public interface UtilJdkFormat
{
	public static enum FpDate
	{
		year   ("yyyy"),
		yearBySortType("yy"),
		month  ("MM"),
		day    ("dd"),
		hour   ("HH"),
		hourByTwelveHour("hh"),
		minute ("mm"),
		second ("ss"),
		millis ("SSS"),

		week("EEEE"),
		countInYear_Day("DDD"),
		countInYear_Week("ww"),
		countInMonth_Day("W"),
		countInMonth_DayOfWeek("F"),

		ADBD("G"),
		AMPM("a"),
		timezoon_TypeR("z"),
		timezoon_TypeG("Z"),

		;
		public final String pattern;
		private FpDate(String pattern)
		{
			this.pattern = pattern;
		}
	}
	public static enum FpNumber
	{
		;
		public final String pattern;
		private FpNumber(String pattern)
		{
			this.pattern = pattern;
		}
	}
}
