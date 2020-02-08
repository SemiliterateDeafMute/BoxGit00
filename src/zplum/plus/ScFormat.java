package zplum.plus;

import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import zplum.kit.Norm;
import zplum.kit.Project;
import zplum.tools._ancestor.UtilJdkFormat;

public class ScFormat implements UtilJdkFormat
{
	protected static class config {};
	protected static class define {};
	public    static class prompt
	{
		public static final Object prompt01 = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
	}

	public static FcDate builderFcDate()
	{
		return new FcDate();
	}
	public static FcNumberChoice builderFcNumberChoice()
	{
		return new FcNumberChoice();
	}
	public static FcNumberDecimal builderFcNumberDecimal()
	{
		return new FcNumberDecimal();
	}

	protected abstract static class Fc<SUBCLASS extends Fc<?>>
	{
		protected StringBuffer pattern = new StringBuffer();
		protected abstract SUBCLASS builder();
		protected abstract SUBCLASS contact(String fp);
		protected abstract Format   build();
	}
	protected abstract static class FcOrig<SUBCLASS extends FcOrig<?>> extends Fc<FcOrig<?>>
	{
		@SuppressWarnings("unchecked")
		protected SUBCLASS builder()
		{
			this.pattern = new StringBuffer();
			return (SUBCLASS) this;
		}
		@SuppressWarnings("unchecked")
		public    SUBCLASS contact(String fp)
		{
			this.pattern.append(fp);
			return (SUBCLASS) this;
		}
		@SuppressWarnings("unchecked")
		public    SUBCLASS contactText(String text)
		{
			this.pattern.append('\'').append(text).append('\'');
			return (SUBCLASS) this;
		}
	}

	public static class FcDate extends FcOrig<FcDate>
	{
		public SimpleDateFormat build()
		{
			if(pattern.length() == 0)
				return Norm.JdkInstant.format_date;
			return new SimpleDateFormat(pattern.toString());
		}
		public FcDate contactYear()
		{
			this.pattern.append(FpDate.year.pattern);
			return this;
		}
		public FcDate contactYearShort()
		{
			this.pattern.append(FpDate.yearBySortType.pattern);
			return this;
		}
		public FcDate contactMonth()
		{
			this.pattern.append(FpDate.month.pattern);
			return this;
		}
		public FcDate contactDay()
		{
			this.pattern.append(FpDate.day.pattern);
			return this;
		}
		public FcDate contactHour()
		{
			this.pattern.append(FpDate.hour.pattern);
			return this;
		}
		public FcDate contactMinute()
		{
			this.pattern.append(FpDate.minute.pattern);
			return this;
		}
		public FcDate contactSecond()
		{
			this.pattern.append(FpDate.second.pattern);
			return this;
		}
		public FcDate contactMillis()
		{
			this.pattern.append(FpDate.millis.pattern);
			return this;
		}
	}

	public abstract static class FcNumber extends FcOrig<FcNumber>
	{
		protected FcNumber()
		{
			Project.exit(new Exception("waiting todo"));
		}
	}
	public static class FcNumberChoice extends FcNumber
	{
		protected ChoiceFormat build()
		{
			if(pattern.length() == 0)
				return null;
			return new ChoiceFormat(this.pattern.toString());
		}

	}
	public static class FcNumberDecimal extends FcNumber
	{
		protected DecimalFormat build()
		{
			if(pattern.length() == 0)
				return null;
			return new DecimalFormat(this.pattern.toString());
		}

	}
}
