package zplum.plus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class RfPrintStream extends PrintStream
{
	private RfPrintStream(OutputStream streamOut)
	{
		super(streamOut);
	}
	public RfPrintStream(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException
    {
		super(file, csn);
	}

	public boolean isClose()
	{
		return super.out == null;
	}

	static{};

	public static PrintStream outMake(File file, String charset) throws IOException
	{
		RfPrintStream tempPs = mapPathToPs.get(file.getAbsolutePath());
		if(tempPs==null || tempPs.isClose() || !file.exists())
		{
			file.getParentFile().mkdirs();
			tempPs = new RfPrintStream(file, charset);

			mapPathToPs.put(file.getAbsolutePath(), tempPs);
		}
		return tempPs;
	}
	public static PrintStream outCheck(String pathFile) throws IOException
	{
		return mapPathToPs.get(pathFile);
	}

	private static HashMap<String, RfPrintStream> mapPathToPs = new HashMap<String, RfPrintStream>();
}
