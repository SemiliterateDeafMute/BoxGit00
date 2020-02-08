package zplum.tools._io_report;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class ReportMega1 extends ReportMega
{
	public ReportMega1()
	{
		super();
	}
	public ReportMega1(PrintStream... pipes)
	{
		super(pipes);
		this.outsRefresh();
	}

	@Override
	public int pipeSet(PrintStream stream)
	{
		int index = super.pipeSet(stream);
		this.outsRefresh();
		return index;
	}
	@Override
	public int pipeSet(File file) throws IOException
	{
		int index = super.pipeSet(file);
		this.outsRefresh();
		return index;
	}
	@Override
	public int pipeSet(String pathFile) throws IOException
	{
		int index = super.pipeSet(pathFile);
		this.outsRefresh();
		return index;
	}

	@Override
	public int[] pipeSet(PrintStream... stream)
	{
		int[] index = super.pipeSet(stream);
		this.outsRefresh();
		return index;
	}
	@Override
	public int[] pipeSet(File... file) throws IOException
	{
		int[] index = super.pipeSet(file);
		this.outsRefresh();
		return index;
	}
	@Override
	public int[] pipeSet(String... pathFile) throws IOException
	{
		int[] index = super.pipeSet(pathFile);
		this.outsRefresh();
		return index;
	}
}
