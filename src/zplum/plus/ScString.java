package zplum.plus;

import java.util.Date;

import zplum.kit.Norm;
import zplum.kit.Project;
import zplum.kit.test0;

public class ScString
{
	private static class define implements Norm.Chars {};
	public static void main0(String[] args)
	{
		int value1, value2;
		value1 = 64 & 193;
		value2 = 129 & 193;

		test0.echo( ((value1&1) - (value2&1)) * -1);
		test0.echo(value1 - value2 );

	}

	public static class getColumn_fast_mark
	{
		private int indexChar;
		private int indexColumn;
		public getColumn_fast_mark()
		{
			this.indexChar = 0;
			this.indexColumn = 0;
		}
		public void clear()
		{
			this.indexChar = 0;
			this.indexColumn = 0;
		}
	}
	public static String getColumn_fast(String content, char split, int indexColumn, getColumn_fast_mark mark)
	{
		int head, tail;

		if(indexColumn - mark.indexColumn == 0)
		{
			head = content.lastIndexOf(split, mark.indexChar);
			tail = mark.indexChar;
			return content.substring(head, tail);
		}
		else if(indexColumn - mark.indexColumn < 0)
		{
			mark.indexChar = 0;
			mark.indexColumn = 0;
		} else {
			indexColumn = indexColumn - mark.indexColumn;
		}

		head = mark.indexChar;
		for(int i=0; i<indexColumn; i++)
		{
			head = content.indexOf(split, head);
			head = head + 1;
			if( head <= 0)
			{
				mark.indexChar = content.length();
				return "";
			}
			mark.indexColumn = mark.indexColumn + 1;
		}

		mark.indexChar = tail = content.indexOf(split, head);
		if(tail == -1)
		{
			tail = content.length();
		}

		return content.substring(head, tail);
	}

	public static String getColumn_fast(String content, char split, int indexColumn)
	{
		return getColumn_fast(content, split, indexColumn, new getColumn_fast_mark());
	}

	public static Integer compareColumn_fast(String i1Content, String j2Content, char split, int... indexColumns)
	{
		if(indexColumns.length == 0)
			Project.exit(new Exception("index is empty."), 1);

		int lim;
		int i1ContentSize = i1Content.length();
		int j2ContentSize = j2Content.length();
		int i1ContentRemain;
		int j2ContentRemain;
		int i1 = -1;
		int j2 = -1;
		int i1Over = 0;
		int j2Over = 0;
		int i1Temp;
		int j2Ttemp;

		for(int p=0, len=indexColumns.length; p<len; p++)
		{
			for(int p2=p+1; p2<len; p2++)
				if(indexColumns[p] == indexColumns[p2])
					indexColumns[p2] = indexColumns[p2-1];
			for(int p2=p+1; p2<len; p2++)
				indexColumns[p2] = indexColumns[p2] - indexColumns[p] - 1;
		}

		for(int x: indexColumns)
		{
			if(x == -1)
				continue;

			if(x >= 0)
			{
				for(i1Over+=x; i1Over>0 && i1!=i1ContentSize; i1Over--)
				{
					i1 = (i1=i1Content.indexOf(split, ++i1))<0?i1ContentSize:i1;
					if(i1 == i1ContentSize)
						break;

				}
				for(j2Over+=x; j2Over>0 && j2!=j2ContentSize; j2Over--)
				{
					j2 = (j2=j2Content.indexOf(split, ++j2))<0?j2ContentSize:j2;
					if(j2 == j2ContentSize)
						break;

				}
			} else {

				for(i1Over+=x; i1Over<0 && i1 != -1; i1Over++)
					i1 = i1Content.lastIndexOf(split, --i1);
				for(j2Over+=x; j2Over<0 && j2 != -1; j2Over++)
					j2 = j2Content.lastIndexOf(split, --j2);
			}
			if(i1Over < 0)
				i1Over++;
			if(j2Over < 0)
				j2Over++;

			i1ContentRemain = i1Over==0?i1ContentSize-i1:0;
			j2ContentRemain = j2Over==0?j2ContentSize-j2:0;
			lim = Math.min(i1ContentRemain, j2ContentRemain) - 1;
			while(lim-- > 0)
			{
				i1Temp = i1Content.charAt(++i1);
				j2Ttemp = j2Content.charAt(++j2);

				if(i1Temp - j2Ttemp != 0)
					return i1Temp - j2Ttemp;
				if(i1Temp == split)
					break;
			}
			if(lim<0 && i1ContentRemain-j2ContentRemain!=0)
			{
				if(++i1 < i1ContentSize && i1Content.charAt(i1)==split)
					continue;
				if(++j2 < j2ContentSize && j2Content.charAt(j2)==split)
					continue;
				return i1ContentRemain-j2ContentRemain;
			}
		}

		return 0;
	}

	public static int indexOf_byTh(String content, String sub, int countTagt, int indexAreaHead, int indexAreaTail)
	{
		if(countTagt <= 0)
			return -1;
		int count = 0;
		while(true)
		{
			if(indexAreaHead<0 || indexAreaHead>=indexAreaTail)
				return count>=1?-1:-2;
			if(count++ >= countTagt)
				return indexAreaHead-1;
			indexAreaHead = content.indexOf(sub, indexAreaHead);
			indexAreaHead++;
		}

	}
	public static int indexOf_byTh(String content, String sub, int countTagt, int indexAreaTail)
	{
		return indexOf_byTh(content, sub, countTagt, 0, indexAreaTail);
	}

	public static int indexOf_byTh(String content, char sub, int countTagt, int indexAreaHead, int indexAreaTail)
	{
		if(countTagt <= 0)
			return -1;
		int count = 0;
		while(true)
		{
			if(indexAreaHead<0 || indexAreaHead>=indexAreaTail)
				return count>=1?-1:-2;
			if(count++ >= countTagt)
				return indexAreaHead-1;
			indexAreaHead = content.indexOf(sub, indexAreaHead);
			indexAreaHead++;
		}
	}
	public static int indexOf_byTh(String content, char sub, int countTagt, int indexAreaTail)
	{
		return indexOf_byTh(content, sub, countTagt, 0, indexAreaTail);
	}
	public static int indexOf_byTh(String content, char sub, int countTagt)
	{
		int indexAreaHead = 0;
		if(countTagt <= 0)
			return -1;
		int count = 0;
		while(true)
		{
			if(indexAreaHead<0)
				return count>=1?-1:-2;
			if(count++ >= countTagt)
				return indexAreaHead-1;
			indexAreaHead = content.indexOf(sub, indexAreaHead);
			indexAreaHead++;
		}
	}

	public static String around(String stem, String prefix, String suffix)
	{
		return "".concat(prefix).concat(stem).concat(suffix);
	}
	public static String around(String stem, String bthfix)
	{
		return "".concat(bthfix).concat(stem).concat(bthfix);
	}
	public static String around(String stem, char prefix, char suffix)
	{
		return around(stem, String.valueOf(prefix), String.valueOf(suffix));
	}
	public static String around(String stem, char bthfix)
	{
		return around(stem, String.valueOf(bthfix));
	}

	public static boolean isBlank(String content)
	{
		int size = content.length();
		for(char codeChar: content.toCharArray())
			if(codeChar == define.charSpace || codeChar == define.charTabs)
				size--;
		if(size == 0)
			return true;
		return false;
	}

	public static int count(String str, char ch)
	{
		for(int index=-1,count=0;; count++)
			if( (index=str.indexOf(ch, index+1)) <= -1)
				return count;
	}

	public static int countFullWidthChar(String str)
	{
		int count = 0;
		for(int i=0,j=str.length(); i<j; i++)
			if(ScCharacter.isFullWidthChar(str.charAt(i)))
					count++;
		return count;
	}

	public static String create(char filler, int size)
	{
		if(size <= 0)
			return "";
		char cs[] = new char[size];
		for(int i=0; i<size; i++)
			cs[i] = filler;
		return String.copyValueOf(cs);
	}
	public static String create(String filler, int size)
	{
		if(size <= 0 || filler.length() <= 0)
			return "";
		if(filler.length() == 1)
			return create(filler.charAt(0), size);
		char cs[] = new char[size * filler.length()];
		char cs_filler[] = filler.toCharArray();
		for(int i=0,ii=0,j=cs.length,jj=cs_filler.length; i<j;)
			for(ii=0; ii<jj; i++, ii++)
				cs[i] = cs_filler[ii];
		return String.copyValueOf(cs);
	}

	public static String setw(Object content, int width, char filler)
	{
		String str = String.valueOf(content);
		return str + ScString.create(filler, width - str.length());
	}
	public static String setw(Object content, int width )
	{
		return setw(content, width, ' ');
	}

	public static String setwOnTab(Object content, int widthOnTab, int tab_width)
	{
		if(widthOnTab <= 1)
			return String.valueOf(content) + '\t';

		int intv;

		String str = String.valueOf(content);
		intv = str.length();
		for(;intv > 0 ;intv--)
			if(str.charAt(intv-1) != '\t')
				break;
		str = str.substring(0, intv);

		intv = (int)Math.floor( (double)getw(str, tab_width) / (double)tab_width);
		intv = widthOnTab - intv;
		intv = (intv<=0)?1:intv;
		return str + ScString.create('\t', intv);
	}
	public static String setwOnTab(Object content, int widthOnTab )
	{
		return setwOnTab(content, widthOnTab, Project.configTabWidth);
	}
	public static String setwOnTabAfterPreText(Object content, int widthOnTab, int tab_width, int pretext_width)
	{
		if(pretext_width == 0)
			return ScString.setwOnTab(content, widthOnTab);
		else
			return ScString.setwOnTab(content, widthOnTab-((pretext_width%tab_width+content.toString().length()%tab_width<tab_width)?0 :1));
	}
	public static String setwOnTabAfterPreText(Object content, int widthOnTab, int pretext_width)
	{
		return ScString.setwOnTabAfterPreText(content, widthOnTab, Project.configTabWidth, pretext_width);
	}

	public static String valueOf(StackTraceElement ste)
	{
		return String.format(Norm.StrFormat.codesite, ste.getClassName(), ste.getMethodName(), ste.getFileName(), ste.getLineNumber());
	}
	public static String valueOf(Date date)
	{
		return Norm.JdkInstant.format_date.format(date);
	}

	public static String valueOf(Object object)
	{
		if(object == null)
			return "null";
		if(object.getClass().isArray())
		{
			Object objects[] = (Object[])object;
			StringBuffer strb = new StringBuffer().append('[');
			for(int i=0, j=objects.length-1;; i++)
				if(i < j)
					strb.append(ScString.valueOf(objects[i])).append(", ");
				else if(i == j)
					strb.append(ScString.valueOf(objects[i]));
				else
					break;
			return strb.append(']').toString();
		}
		return object.toString();
	}
	@Deprecated
	@SuppressWarnings("unused")
	private static String valueOf_ORIG(Object[] objects)
	{
		StringBuffer strb = new StringBuffer();
		strb.append('[');
		for(int i=0, j=objects.length-1;; i++)
		{
			if(i < j)
				strb.append(String.valueOf(objects[i])).append(", ");
			else if(i == j)
				strb.append(String.valueOf(objects[i]));
			else
				break;
		}
		strb.append(']');
		return strb.toString();
	}

	public static int getw(String content)
	{
		return getw(content, Project.configTabWidth);
	}
	public static int getw(String content, int tab_width)
	{
		double width = 0;
		for(char c: content.toCharArray())
		{
			if(c == '\t')
				width = tab_width * ((int)Math.floor(width/tab_width)+1);
			else if(ScCharacter.isFullWidthChar(c))
				width += Project.configFullWidthCharWidth;
			else
				width += 1;
		}
		return (int) Math.ceil(width);
	}

	public static int getwOnTab(String content)
	{
		return getwOnTab(content, Project.configTabWidth);
	}
	public static int getwOnTab(String content, int tab_width)
	{
		return  (int)Math.ceil( (double)getw(content, tab_width) / (double)tab_width);
	}
	public static int getwOnTabSuffixAnTab(String content)
	{
		return getwOnTabSuffixAnTab(content, Project.configTabWidth);
	}
	public static int getwOnTabSuffixAnTab(String content, int tab_width)
	{
		return  (int)Math.ceil( (double)getw(content, tab_width) / (double)tab_width)
					+ (content.length()%tab_width==0?1:0);
	}

	public static String concat(String... contents)
	{
		StringBuffer strb = new StringBuffer();
		for(String content: contents)
			strb.append(content);
		return strb.toString();
	}

	public static String concatWithSpace(char spacer, String... contents)
	{
		StringBuffer strb = new StringBuffer();
		for(int i=0,j=contents.length-1; i<j; i++)
			strb.append(contents[i]).append(spacer);
		if(contents.length > 1)
			strb.append(contents[contents.length - 1]);
		return strb.toString();
	}

	@Deprecated

	public static String concatWithSpace_ORIG(char filler, int width, int pretext_width, String... contents)
	{
		StringBuffer strb = new StringBuffer();
		if(filler == '\t')
		{
			if(contents.length > 0)
				strb.append(ScString.setwOnTabAfterPreText(contents[0], width, pretext_width));
			for(int i=1,j=contents.length-1; i<j; i++)
				strb.append(ScString.setwOnTab(contents[i], 1));
		} else {
			for(int i=0,j=contents.length-1; i<j; i++)
				strb.append(ScString.setw(
								contents[i],
								width>contents[i].length()?width:1,
								filler
								));
		}
		if(contents.length > 1)
			strb.append(contents[contents.length - 1]);
		return strb.toString();
	}

	public static String concatWithSpace(char filler, int width, String... contents)
	{
		StringBuffer strb = new StringBuffer();
		for(int i=0,j=contents.length-1; i<j; i++)
			strb.append(ScString.setw(
							contents[i],
							width>contents[i].length()?width:1,
							filler
							));
		if(contents.length > 1)
			strb.append(contents[contents.length - 1]);
		return strb.toString();
	}

	public static String concatWithSpaceOnTab(int widthOnTab, String... contents)
	{
		return concatWithSpaceOnTab(widthOnTab, Project.configTabWidth, 0, contents);
	}
	public static String concatWithSpaceOnTab(int[] widthsOnTab, String... contents)
	{
		return concatWithSpaceOnTab(widthsOnTab, Project.configTabWidth, 0, contents);
	}
	public static String concatWithSpaceOnTab(int widthOnTab, int pretext_width, String... contents)
	{
		return concatWithSpaceOnTab(widthOnTab, Project.configTabWidth, pretext_width, contents);
	}
	public static String concatWithSpaceOnTab(int[] widthsOnTab, int pretext_width, String... contents)
	{
		return concatWithSpaceOnTab(widthsOnTab, Project.configTabWidth, pretext_width, contents);
	}
	public static String concatWithSpaceOnTab(int widthOnTab, int tab_width, int pretext_width, String... contents)
	{
		if(contents == null)
			return null;
		StringBuffer strb = new StringBuffer();
		if(contents.length > 1)
			strb.append(ScString.setwOnTabAfterPreText(contents[0], widthOnTab, pretext_width));
		for(int i=1,j=contents.length-1; i<j; i++)
			strb.append(ScString.setwOnTab(contents[i], widthOnTab));
		if(contents.length > 0)
			strb.append(contents[contents.length-1]);
		return strb.toString();
	}
	public static String concatWithSpaceOnTab(int widthsOnTab[], int tab_width, int pretext_width, String... contents)
	{
		if(contents == null)
			return null;
		if(widthsOnTab == null)
			return concatWithSpaceOnTab(1, tab_width, pretext_width, contents);
		if(widthsOnTab.length == 1)
			return concatWithSpaceOnTab(widthsOnTab[0], tab_width, pretext_width, contents);

		StringBuffer strb = new StringBuffer();
		int index = 1;
		int indexOfTail_width = widthsOnTab.length<contents.length-1? widthsOnTab.length: contents.length-1;
		int indexOfTail_content = contents.length-1;
		if(contents.length > 1)
			strb.append(ScString.setwOnTabAfterPreText(contents[0], widthsOnTab[0], pretext_width));
		for(;index < indexOfTail_width; index++)
			strb.append(ScString.setwOnTab(contents[index], widthsOnTab[index]));
		for(;index < indexOfTail_content; index++)
			strb.append(ScString.setwOnTab(contents[index], 1));
		if(contents.length > 0)
			strb.append(String.valueOf(contents[contents.length-1]));
		return strb.toString();
	}

	public static String trimTail(String content)
	{
		char chars[] = content.toCharArray();

		int tail = chars.length - 1;
		for(; tail>=0; tail--)
			if(!ScArray.contain(chars[tail], define.charSetBlank))
				break;
		return content.substring(0, tail+1);
	}

	public static String fuse(String content, char fuseChar)
	{
		String fuseCharSingle = "" + fuseChar;
		String fuseCharDouble = "" + fuseChar + fuseChar;
		for(;content.contains(fuseCharDouble);)
			content = content.replace(fuseCharDouble, fuseCharSingle);
		return content;
	}

	public static boolean equals(String str1, String str2)
	{
		return str1.equals(str2);
	}
}

