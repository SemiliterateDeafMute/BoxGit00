package zplum.tools._io_report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.GZIPOutputStream;

import zplum.kit.Project;
import zplum.plus.RfListArraySet;

public class Report extends _Report
{
	public Report()
	{
		super();
	}

	public Report(OutputStream streamOut)
	{
		super(streamOut);
		if(streamOut instanceof Report)
			super.out = ((Report) streamOut).out;
	}

	@Override
	public void write(byte b[], int off, int len)
	{
		this.write_orig(b, off, len);
	}

public static final class Err extends Report
{
	public Err()
	{
		super(_Report.psConsole_orig_err);
	}
	public void close()
	{
		super.out = _Report.psConsole_orig_out; super.close();
	}
}

public static final class Close extends Report
{
	public Close()
	{
		super(_Report.psCloseWatermelon);
	}
}

public static class WithFile extends Report
{
	private WithFile(PrintStream streamOut)
	{
		super(streamOut);
	}
	public WithFile(File file) throws IOException
	{
		this(outMake(file));
	}
	public WithFile(String filePath) throws IOException
	{
		this(new File(filePath));
	}

	public static void print(File file, String content) throws IOException
	{
		PrintStream out = new WithFile(file);
		out.print(content);
		out.close();
	}
}

public static class WithFileZip extends Report
{
	private WithFileZip(OutputStream streamOut)
	{
		super(streamOut);
	}
	public WithFileZip(File objFile) throws IOException
	{
		this(new GZIPOutputStream(outMake(objFile)));
	}
	public WithFileZip(String strFile) throws IOException
	{
		this(new File(strFile));
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		this.close();
	}
}

public static class WithFileToTee extends WithFile
{
	public WithFileToTee(File file) throws IOException
	{
		super(file);
		if(_Report.psConsole_orig_out.equals(super.out))
			super.out = _Report.psCloseWatermelon;
	}
	public WithFileToTee(String filePath) throws IOException
	{
		this(new File(filePath));
	}

	public void write(byte b[], int off, int len)
	{
		super.write(b, off, len);
		_Report.psConsole.write(b, off, len);
	}
}

public static class WithStringBuffer extends _Report
{
	protected StringBuffer strb = null;
	public WithStringBuffer(StringBuffer strb) throws IOException
	{
		this.strb = strb;
	}
	public void write(byte b[], int off, int len)
	{
		try {
			this.strb.append(new String(b, off, len, config.charset));
		} catch (UnsupportedEncodingException e) {
			Project.exit(e);
		}
	}
	@Deprecated
	public void writeOld(byte b[], int off, int len)
	{
		char[] cs = new char[b.length];
		for(int i=0,j=b.length; i<j; i++)
			cs[i] = (char)b[i];
		this.strb.append(cs, off, len);
	}
	public StringBuffer getBuffer()
	{
		return this.strb;
	}
}

@Deprecated
public final static class SystemReport extends _Report
{
	private static RfListArraySet<PrintStream> listPs = new RfListArraySet<PrintStream>();
	private static HashMap<String, Integer> mapPathToIndex = new HashMap<String, Integer>();
	private static ReportMega2 outs0 = new ReportMega2();

	@Override
	public void write(byte b[], int off, int len)
	{
		this.write_orig(b, off, len);
	}

	public int pipeSet(PrintStream stream)
	{
		if(stream instanceof SystemReport)
			return -1;
		SystemReport.listPs.add(stream);
		return listPs.indexOf(stream);
	}
	public int pipeSet(File file) throws IOException
	{
		Integer index = pipeSet(outMake(file));
		mapPathToIndex.put(file.getAbsolutePath(), index);
		return index;
	}
	public int pipeSet(String pathFile) throws IOException
	{
		return pipeSet(new File(pathFile));
	}

	public int pipeSetAdChange(PrintStream stream)
	{
		int i = pipeSet(stream);
		pipeChange(i);
		return i;
	}
	public int pipeSetAdChange(File file) throws IOException
	{
		int i = pipeSet(file);
		pipeChange(i);
		return i;
	}
	public int pipeSetAdChange(String pathFile) throws IOException
	{
		int i = pipeSet(pathFile);
		pipeChange(i);
		return i;
	}

	public int pipeIndex(PrintStream stream)
	{
		return listPs.indexOf(stream);
	}
	public int pipeIndex(File file)
	{
		return mapPathToIndex.getOrDefault(file.getAbsolutePath(), -1);
	}
	public int pipeIndex(String pathFile)
	{
		return mapPathToIndex.getOrDefault(pathFile, -1);
	}

	public void pipeChange(int index)
	{
		if(index < 0 || index >= listPs.size())
			super.out = _Report.psConsole_orig_out;
		else
			super.out = listPs.get(index);
	}
	public void pipeChange(int... indexs)
	{
		HashSet<PrintStream> streams = new HashSet<PrintStream>();
		for(int index: indexs)
		{
			if(index < 0 || index >= listPs.size())
				streams.add(_Report.psConsole_orig_out);
			else
				streams.add(listPs.get(index));
		}

		switch(streams.size())
		{
			case 0:
				super.out = _Report.psConsole_orig_out;
				break;
			case 1:
				super.out = streams.iterator().next();
				break;
			default:
				super.out = outs0;
				outs0.outsSet(streams.toArray(new PrintStream[streams.size()]));
				break;
		}
	}

	public PrintStream pipe(int index)
	{
		if(index < 0 || index >= listPs.size())
			return _Report.psConsole_orig_out;
		else
			return listPs.get(index);
	}
	public PrintStream pipe(int... indexs)
	{
		HashSet<PrintStream> streams = new HashSet<PrintStream>();
		for(int index: indexs)
		{
			if(index < 0 || index >= listPs.size())
				streams.add(_Report.psConsole_orig_out);
			else
				streams.add(listPs.get(index));
		}

		switch(streams.size())
		{
			case 0:
				return _Report.psConsole_orig_out;
			case 1:
				return streams.iterator().next();
			default:
				outs0.outsSet(streams.toArray(new PrintStream[streams.size()]));
				return outs0;
		}
	}

	public void pipeReset()
	{
		super.out = _Report.psConsole_orig_out;
	}

}

}

