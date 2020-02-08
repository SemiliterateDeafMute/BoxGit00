package zplum.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import zplum.kit.Norm;
import zplum.kit.Project;
import zplum.plus.ScArray;
import zplum.plus.ScString;
import zplum.tools._ancestor.UtilPdkTime;

public class Abacus
{
	public static final class string implements Norm.Chars
	{
		public static String changeTabsToSpace(String content)
		{
			return changeTabsToSpace(content, Project.configTabWidth);
		}
		public static String changeTabsToSpace(String content, int tab_width)
		{
			return content.replace("\t", ScString.create(' ', tab_width));
		}
		public static String changeSpaceToTabs(String content)
		{
			return changeSpaceToTabs(content, Project.configTabWidth);
		}
		public static String changeSpaceToTabs(String content, int tab_width)
		{
			return content.replace(ScString.create(' ', tab_width), "\t");
		}
		public static String createSpaceIsometricWithoutTabs(String content)
		{
			return createSpaceIsometricWithoutTabs(content, Project.configTabWidth);
		}
		public static String createSpaceIsometricWithoutTabs(String content, int tab_width)
		{
			int tabs, charWidthFull, charWidthHalfWithoutTabs;
			tabs = ScString.count(content, '\t');
			charWidthFull = ScString.countFullWidthChar(content);
			charWidthHalfWithoutTabs = content.length() - charWidthFull - tabs;
			return ScString.create(charSpace, (tabs*tab_width) + charWidthHalfWithoutTabs) + ScString.create(charFwSpace, charWidthFull);
		}
		public static String resetwOnTab_auto(String content, String content_line_regex)
		{
			String lines[] = content.split("\n");
			String contentss[][] = new String[lines.length][];

			int tmpWidth;
			ArrayList<Integer> lines_maxWidth = new ArrayList<Integer>();
			for(int i=0,j=lines.length; i<j; i++)
			{
				contentss[i] = lines[i].split(content_line_regex);
				for(;lines_maxWidth.size() < contentss[i].length;)
					lines_maxWidth.add(0);
				for(int ii=0,jj=contentss[i].length; ii<jj; ii++)
					if(lines_maxWidth.get(ii) < (tmpWidth=ScString.getwOnTabSuffixAnTab(contentss[i][ii])))
						lines_maxWidth.set(ii, tmpWidth);
			}

			StringBuffer strb = new StringBuffer();
			for(int i=0,j=contentss.length;;)
			{
				for(int ii=0,jj=contentss[i].length-1; ii<jj; ii++)
					strb.append(ScString.setwOnTab(contentss[i][ii], lines_maxWidth.get(ii)));
				if(contentss[i].length > 0)
					strb.append(contentss[i][contentss[i].length-1]);
				if(++i >= j)
					break;
				strb.append('\n');
			}
			return strb.toString();
		}
		public static String resetwOnTab_auto(String content, String contentLine_tgtRegex, String contentLine_tgtPrefix, boolean isRemoveTgtPrefix)
		{
			if(contentLine_tgtPrefix == null || contentLine_tgtPrefix == "")
				return string.resetwOnTab_auto(content, contentLine_tgtRegex);

			String lines[] = content.split("\n");
			String contentss[][] = new String[lines.length][];

			int tmpWidth;
			int tmpLengthOfTgtPrefix = contentLine_tgtPrefix.length();
			ArrayList<Integer> lines_maxWidth = new ArrayList<Integer>();
			for(int i=0,j=lines.length; i<j; i++)
			{
				if(lines[i].startsWith(contentLine_tgtPrefix))
				{
					if(isRemoveTgtPrefix)
						lines[i] = lines[i].substring(tmpLengthOfTgtPrefix);
					contentss[i] = lines[i].split(contentLine_tgtRegex);
					for(;lines_maxWidth.size() < contentss[i].length;)
						lines_maxWidth.add(0);
					for(int ii=0,jj=contentss[i].length; ii<jj; ii++)
						if(lines_maxWidth.get(ii) < (tmpWidth=ScString.getwOnTabSuffixAnTab(contentss[i][ii])))
							lines_maxWidth.set(ii, tmpWidth);
				} else {
					contentss[i] = ScArray.cerate(lines[i]);
				}
			}

			StringBuffer strb = new StringBuffer();
			for(int i=0,j=contentss.length;;)
			{
				for(int ii=0,jj=contentss[i].length-1; ii<jj; ii++)
					strb.append(ScString.setwOnTab(contentss[i][ii], lines_maxWidth.get(ii)));
				if(contentss[i].length > 0)
					strb.append(contentss[i][contentss[i].length-1]);
				if(++i >= j)
					break;
				strb.append('\n');
			}
			return strb.toString();
		}

		public static String word_splice(String content, char spliter)
		{
			String words[] = content.split("" + spliter);
			for(int i=0,j=words.length; i<j; i++)
				if(words[i].length() > 0)
					words[i] = Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1);
			StringBuffer strb = new StringBuffer();
			for(String word: words)
				strb.append(word);
			return strb.toString();
		}

		public static String word_splice_reverse(String content, char spliter)
		{
			if(content.isEmpty())
				return content;
			char cs[] = content.toCharArray();
			StringBuffer strb = new StringBuffer();

			strb.append(Character.toLowerCase(cs[0]));
			for(int i=1,j=cs.length;; i++)
			{
				strb.append(cs[i]);
				if(i+1 >= j)
				{
					break;
				} else if(Character.isUpperCase(cs[i+1])) {
					strb.append(spliter);
					strb.append(Character.toLowerCase(cs[i+1]));
					i = i + 1;
				}
			}
			return strb.toString();
		}
		public static String fileName_caseConsole(String content)
		{
			StringBuffer strb = new StringBuffer();
			for(char char_tmp: content.toCharArray())
			{
				if(ScArray.contain(char_tmp, charSetConsoleKeep))
					strb.append(charBackslash);
				strb.append(char_tmp);
			}
			return strb.toString();
		}
		public static String fileName_caseConsole_reverse(String content)
		{
			StringBuffer strb = new StringBuffer();
			char cs[] = content.toCharArray();
			for(int i=0,j=cs.length; i<j; i++)
			{
				if(cs[i] == charBackslash && i<j && ScArray.contain(cs[i+1], charSetConsoleKeep))
					i++;
				strb.append(cs[i]);
			}
			return strb.toString();
		}

		static {};
	}
	public static final class number
	{
		public static String caseBin(int number)
		{
			return Integer.toBinaryString(number);
		}
		public static String caseOct(int number)
		{
			return Integer.toOctalString(number);
		}
		public static String caseHex(int number)
		{
			return Integer.toHexString(number);
		}
		public static Integer valueOfInteger(String number, int radix)
		{
			switch(radix)
			{
				case 2:
				case 8:
				case 16:
				case 10:
					break;
				default:
					radix = -1;
			}
			if(radix <= 1)
				return null;
			return Integer.valueOf(number, radix);
		}
		public static String caseFixed(double number)
		{
			String content = Norm.JdkInstant.format_number.format(number);
			if(content.indexOf('.') < 0)
				content = content + ".0";
			return content;
		}
		public static String caseFloat(double number)
		{
			int e = Integer.valueOf(Norm.JdkInstant.format_number2.format(number).split("E")[1]);
			StringBuffer strb = new StringBuffer();
			if(number < 0)
				strb.append('-');
			String shadow0Integer1Decimal[] = Norm.JdkInstant.format_number.format(Math.abs(number)).split("\\.");
			String cache = new Long(
								new Double(
										"0." +
										shadow0Integer1Decimal[0] +
										(shadow0Integer1Decimal.length>1? shadow0Integer1Decimal[1]: "")
										).toString().replace("0.", "")
								).toString();
			if(cache.length() == 1)
				cache = cache + ".0";
			else
				cache = cache.charAt(0) + "." + cache.substring(1);
			strb.append(cache);
			strb.append('E').append(e);
			return strb.toString();
		}
		@Deprecated
		public static String caseScientific_ORIG(double number)
		{
			String shadow0Integer1Decimal[] = Norm.JdkInstant.format_number.format(Math.abs(number)).split("\\.");
			int e = Integer.valueOf(Norm.JdkInstant.format_number2.format(number).split("E")[1]);
			StringBuffer strb = new StringBuffer();
			if(number < 0)
				strb.append('-');
			String content_add = "";
			if(e >= 0)
			{
				strb.append(shadow0Integer1Decimal[0].charAt(0));
				content_add = "0" +
						(e>0 ?shadow0Integer1Decimal[0].substring(1) :"") +
						(shadow0Integer1Decimal.length>1 ? shadow0Integer1Decimal[1] :"");
				if(Integer.valueOf(content_add) == 0)
					content_add = "0";
			} else {
				content_add = "" +
						Integer.valueOf(shadow0Integer1Decimal[1]);
				strb.append(content_add.charAt(0));
				if(content_add.length() == 1)
					content_add = "0";
				else
					content_add = content_add.substring(1);
			}
			strb.append('.').append(content_add).append('E').append(e);
			return strb.toString();
		}
		public static String caseFloatTypeMine(double number)
		{
			int e = Integer.valueOf(Norm.JdkInstant.format_number2.format(number).split("E")[1]);
			StringBuffer strb = new StringBuffer();
			strb.append(number>=0? 'P': 'N');
			strb.append(e>=0?      'P': 'N');

			String shadow0Integer1Decimal[] = Norm.JdkInstant.format_number.format(Math.abs(number)).split("\\.");
			strb.append(new Long(
							new Double(
									"0." +
									shadow0Integer1Decimal[0] +
									(shadow0Integer1Decimal.length>1? shadow0Integer1Decimal[1]: "")
									).toString().replace("0.", "")
							).toString()
					);
			strb.append('E').append(Math.abs(e));
			return strb.toString();
		}
		public static Double valueOfDouble(String number)
		{
			return new Double(number);
		}
		public static Double valueOfDoubleByTypeMine(String number)
		{
			String mark = null;
			String marks[] = ScArray.cerate("PP", "PN", "NP", "NN");
			number = number.toUpperCase();
			for(int i=0,j=marks.length; i<j && mark==null; i++)
				if(number.indexOf(marks[i]) > 0)
					mark = marks[i];
			if(mark == null)
				return null;
			number.replace(mark, "");
			if(mark.charAt(1) == 'N')
				number.replace("E", "E-");
			if(mark.charAt(1) == 'N')
				return new Double(number) * -1;
			return new Double(number);
		}
	}
	public static final class time implements UtilPdkTime
	{
		public static double caseTimeValueUnit(time.valueUnit unit, long time_value_millis)
		{
			return ((double)time_value_millis)/unit.weight;
		}
		public static Long[] caseTimeValueUnitUion0millies(time.valueUnit unit, long time_value_millis)
		{
			ArrayList<Long> uion = new ArrayList<Long>();
			int i=0;
			boolean tmp_boolean = true;
			double time_millis_abs = Math.abs(time_value_millis);
			if(unit == null)
				unit = time.valueUnit.level[time.valueUnit.level.length - 1];
			while(tmp_boolean && time_millis_abs >= time.valueUnit.level[i].weight)
				uion.add((long)Math.floor(((tmp_boolean=time.valueUnit.level[i].weight<unit.weight)?time_value_millis%time.valueUnit.level[i+1].weight:time_value_millis)/time.valueUnit.level[i++].weight));
			return uion.toArray(new Long[uion.size()]);
		}
		public static long valueOfTimeValue(time.valueUnit unit, double time_value_unit)
		{
			return (long)time_value_unit * unit.weight;
		}
		public static Long valueOfTimeValue(Long time_value_unituion[])
		{
			if(time_value_unituion==null || time_value_unituion.length == 0 || time_value_unituion.length > time.valueUnit.level.length)
				return null;
			for(int i=1,j=time_value_unituion.length; i<j; i++)
				time_value_unituion[0] += time_value_unituion[i] * time.valueUnit.level[i].weight;
			return new Long(time_value_unituion[0]);
		}

		private static HashMap<String, SimpleDateFormat> mapOf_PatternTo_ForMatDate = new HashMap<String, SimpleDateFormat>();
		static {
			mapOf_PatternTo_ForMatDate.put(Norm.JdkInstant.format_date.toPattern(), Norm.JdkInstant.format_date);
		}
		private static SimpleDateFormat formatDateMake(String pattern)
		{
			if(pattern == null)
				pattern = Norm.JdkInstant.format_date.toPattern();
			SimpleDateFormat formatDate = mapOf_PatternTo_ForMatDate.get(pattern);
			if(formatDate == null)
				mapOf_PatternTo_ForMatDate.put(pattern, formatDate = new SimpleDateFormat(pattern));
			return formatDate;
		}
		public static String caseTimePointText(String pattern, long time_point_millis)
		{
			return formatDateMake(pattern).format(new Date(time_point_millis));
		}
		public static Long valueOfTimePoint(String pattern, String time_point_text)
		{
			try {
				return formatDateMake(pattern).parse(time_point_text).getTime();
			} catch (ParseException e) {

			}
			return null;
		}

	}

	public static String getTimeNow()
	{
		return Norm.JdkInstant.format_date.format(new Date());
	}
	public static String getTimeNow(long time)
	{
		return Norm.JdkInstant.format_date.format(new Date(time));
	}
	public static String getCodeSiteHere()
	{
		return ScString.valueOf(new Throwable().getStackTrace()[1]);
	}
	public static String getCodeSiteHere(int stack_trace_level)
	{
		stack_trace_level = stack_trace_level + 1;
		stack_trace_level = (stack_trace_level==0)?-1:stack_trace_level;
		try {
			return ScString.valueOf(new Throwable().getStackTrace()[stack_trace_level]);
		} catch (ArrayIndexOutOfBoundsException e) {

			StackTraceElement sts[] = new Throwable().getStackTrace();

			return ScString.valueOf(sts[1]) + Norm.StrFormat.codesite_extd02;
		}
	}
	public static String[] getCodeSiteTrace()
	{
		ArrayList<String> strs = new ArrayList<String>();
		StackTraceElement sts[] = new Throwable().getStackTrace();
		for(int i=1, j=sts.length; i<j; i++)
			strs.add(ScString.valueOf(sts[i]));
		return strs.toArray(new String[strs.size()]);
	}
	public static String[] getCodeSiteTrace(int stack_trace_level)
	{
		stack_trace_level = stack_trace_level + 1;
		ArrayList<String> strs = new ArrayList<String>();
		StackTraceElement sts[] = new Throwable().getStackTrace();

		if(stack_trace_level <= 0 || stack_trace_level >= sts.length)
		{
			stack_trace_level = 2;
			strs.add(ScString.valueOf(sts[1]) + Norm.StrFormat.codesite_extd02);
		}
		for(int i=stack_trace_level, j=sts.length; i<j; i++)
			strs.add(ScString.valueOf(sts[i]));
		return strs.toArray(new String[strs.size()]);
	}
	public static StackTraceElement getStackTraceHere()
	{
		return new Throwable().getStackTrace()[1];
	}

}

