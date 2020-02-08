package zplum.kit;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import zplum.plus.ScObject;

public class Norm
{
	public static void main(String[] args)
	{
		test0.echo(10 * Norm.intK1);
		test0.echo(Norm.Chars.charAsterisk);
	}

	public final static int intK1 = 1000;
	public static interface Chars
	{
		public final static char charColon 		= '"';
		public final static char charSlash 		= '/';
		public final static char charBackslash 	= '\\';
		public final static char charAsterisk 	= '*';

		public final static char charSpace 		= ' ';
		public final static char charTabs 		= '\t';

		public final static char charCurlyBraceLeft 	= '{';
		public final static char charCurlyBraceRight 	= '}';

		public final static char charFwSpace 	= '　';

		public final static char charSetBlank[] = {charSpace, charTabs, charFwSpace};
		public final static char charSetConsoleKeep[] = {
				' ', '!', '\'', '"', '#', '$', '%', '&', '*', ',', ';', '`', '(', ')', '<', '>', '[', ']', '{', '}', '|', '\\',
				'~',
				};
	}

	public static class StrFormat
	{
		public static final String codesite = "from %s.%s(%s:%03d)";
		public static final String codesite_extd02 = "TOP)";
	}
	public static class JdkClass{}
	public static class JdkInstant
	{
		public static final SimpleDateFormat format_date = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
		public static final DecimalFormat    format_number = new DecimalFormat();
		public static final DecimalFormat    format_number2 = new DecimalFormat("0E0");
		static {
			format_number.setGroupingUsed(false);
		}
		public static final Comparator<Integer> sortInt  = new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2)
			{
				return o1 - o2;
			}
		};
		public static final Comparator<Integer> sortIntR = new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2)
			{
				return o2 - o1;
			}
		};
		public static final Comparator<String>  sortStr  = new Comparator<String>()  {
			public int compare(String o1, String o2)
			{
				return o1.compareTo(o2);
			}
		};
		public static final Comparator<Object>  sortObj  = new Comparator<Object>()  {
			public int compare(Object o1, Object o2)
			{
				return ScObject.compareTo(o1.toString(), o2.toString());
			}
		};
		public static final Comparator<File>  sortFile  = new Comparator<File>()  {
			public int compare(File o1, File o2)
			{
				return sortStr.compare(o1.getAbsolutePath().toLowerCase(), o2.getAbsolutePath().toLowerCase());
			}
		};
		public static final FileFilter filterFileAllTrue = new FileFilter() {
			public boolean accept(File pathname)
			{
				return true;
			}
		};
	}
}
