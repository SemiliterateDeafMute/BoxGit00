package zplum.tools._fc_runcommand;

public class CommandKit
{
	public static String backslash(String content)
	{
		int c;
		int j = 0;
		char[] cache = new char[content.length() * 2];

		for(int i=0; i<content.length(); i++)
		{
			c = content.charAt(i);
			switch(c)
			{
				case ' ':
				case '&':
				case '|':
				case '\\':
				{
					cache[j++] = '\\';
					break;
				}
			}
			cache[j++] = (char)c;
		}

		return String.valueOf(cache).trim();
	}

}
