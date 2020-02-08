package zplum.toolz;

import java.io.UnsupportedEncodingException;

public class KitHtml
{
	public static String encodeHtmlSource(byte[] sourceByte)
	{
		String sourceString = new String(sourceByte);
		String charset = "";

		charsetGet:
		{
			int head, tail;
			head = sourceString.indexOf("<head>");
			tail = sourceString.indexOf("</head>");
			if(head >= tail || head < 0 || tail < 0)
				break charsetGet;

			head = sourceString.indexOf("<meta", head);
			if(head < 0 )
				break charsetGet;
			tail = sourceString.indexOf('>', head);
			if(tail < 0 )
				break charsetGet;
			sourceString = sourceString.substring(head, tail);
			sourceString = sourceString.replace("=", " = ");
			sourceString = sourceString.concat(" ").replace("  ", " ");
			sourceString = sourceString.replace("\"", "");

			head = sourceString.indexOf("charset = ") + 10;
			tail = sourceString.indexOf(" ", head);
			if(head >= tail || head < 0 || tail < 0)
				break charsetGet;
			charset = sourceString.substring(head, tail);
		}

		try {
			if (!charset.isEmpty())
				sourceString = new String(sourceByte, charset);
		} catch (UnsupportedEncodingException e) {
			try {
				charset = charset.toLowerCase();
				if(charset.indexOf("gbk") >= 0)
					charset = "gbk";
				sourceString = new String(sourceByte, charset);
			} catch (UnsupportedEncodingException e1) {
				System.err.println("safely err: " + e.getClass());
			}
		}
		return sourceString;
	}

}
