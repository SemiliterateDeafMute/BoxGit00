package zplum.plus;

import zplum.kit.test0;

public class ScCharacter
{
	public static void main(String[] args)
	{
		char ch;
		ch = '';
		ch = '﹌';
		ch = '，';
		test0.echo(isFullWidthChar_way1(ch));
		test0.echo(isFullWidthChar_way2(ch));

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
		Character.UnicodeScript sc = Character.UnicodeScript.of(ch);
		test0.echo(ub);
		test0.echo(sc);
	}

	public static boolean isFullWidthChar(char ch)
	{
		return isFullWidthChar_way1(ch);

	}
	public static boolean isFullWidthChar_way1(char ch)
	{
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
		if( false

			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
			|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
			|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT

			|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
			|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
			|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
			|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
			|| ub == Character.UnicodeBlock.VERTICAL_FORMS
			)
			return true;
		return false;
	}
	public static boolean isFullWidthChar_way2(char ch)
	{
		Character.UnicodeScript sc = Character.UnicodeScript.of(ch);
		if(
			sc == Character.UnicodeScript.HAN)
			return true;
		return false;
	}

}
