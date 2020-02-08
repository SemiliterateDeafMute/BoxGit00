package zplum.tools._io_scout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import zplum.kit.Project;
import zplum.plus.RfStringBufferReader;

public class Scout extends BufferedReader implements _Scout
{
	protected static final class config
	{
		public static final String charset = Project.configCharset;
	}

	private Scout(Reader in)
	{
		super(in);
	}
	public Scout(InputStream streamIn) throws UnsupportedEncodingException
	{
		this(new InputStreamReader(streamIn, config.charset));
	}
	public Scout(URL objUrl) throws IOException
	{
		this(objUrl.openStream()) ;
	}
	public Scout(File objFile) throws IOException
	{
		this(new FileInputStream(objFile));
	}
	public Scout(String objOrUrlFile) throws MalformedURLException, IOException
	{
		this(isrMake(objOrUrlFile));
	}
	protected static InputStreamReader isrMake(String orUrlFile) throws MalformedURLException, IOException
	{
		if(orUrlFile.startsWith("http:/") || orUrlFile.startsWith("https:/"))
			return new InputStreamReader( new URL(orUrlFile).openStream() );
		else
			return new InputStreamReader( new FileInputStream( new File(orUrlFile) ) );
	}

	public static void recon(String objOrUrlFile) throws MalformedURLException, IOException
	{
		Scout scout = new Scout(objOrUrlFile);
		scout.readRemainAndPrintToConsole();
		scout.close();
	}
	public static String reconToString(String objOrUrlFile) throws MalformedURLException, IOException
	{
		Scout scout = new Scout(objOrUrlFile);
		String str = scout.readRemain();
		scout.close();
		return str;
	}
	public static String reconToString(File file) throws IOException
	{
		Scout scout = new Scout(file);
		String str = scout.readRemain();
		scout.close();
		return str;
	}

public static class Plus extends Scout
{
	protected String rowThis = null;
	protected String rowNext = null;

	protected Plus(Reader in) throws IOException
	{
		super(in);
		this.rowNext = super.readLine();
	}
	protected Plus(InputStream streamIn) throws IOException
	{
		this(new InputStreamReader(streamIn));
	}
	public Plus(URL objUrl) throws IOException
	{
		this(objUrl.openStream()) ;
	}
	public Plus(File objFile) throws IOException
	{
		this(new FileInputStream(objFile));
	}
	public Plus(String objOrUrlFile) throws MalformedURLException, IOException
	{
		this(isrMake(objOrUrlFile));
	}

    public String readLine() throws IOException
    {
    	this.rowThis = this.rowNext;
    	this.rowNext = super.readLine();
    	return this.rowThis;
    }
	public boolean isFinsh()
	{
		return this.rowNext == null;
	}
	public boolean isNoFinsh()
	{
		return this.rowNext != null;
	}
}

public static class WithFileZip extends Scout
{
	public WithFileZip(File objFile) throws IOException
	{
		super( new GZIPInputStream( new FileInputStream(objFile) ) );
	}
}

public static class WithStringBuffer extends Plus
{
	public WithStringBuffer(StringBuffer strb) throws IOException
	{
		super(new RfStringBufferReader(strb));
	}
}

}

