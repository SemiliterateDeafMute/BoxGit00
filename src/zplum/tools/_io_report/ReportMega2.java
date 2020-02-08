package zplum.tools._io_report;

import java.io.PrintStream;

import zplum.plus.RfListArraySet;

public class ReportMega2 extends ReportMega
{
	public ReportMega2()
	{
		super();
	}
	public ReportMega2(PrintStream... pipes)
	{
		super(pipes);
	}

	private RfListArraySet<PrintStream> pipesCache = new RfListArraySet<PrintStream>();
	private ReportMega1 outsCache = new ReportMega1();

	public void pipeChange(int index)
	{
		this.pipesCache.clear();
		if(index < 0 || index >= this.pipes.size())
			this.pipesCache.add(_Report.psConsole_orig_out);
		else
			this.pipesCache.add(this.pipes.get(index));

		this.outsRefresh(this.pipesCache);
	}
	public void pipeChange(int... indexs)
	{
		if(indexs.length == 0)
			return;

		this.pipesCache.clear();
		for(int index: indexs)
		{
			if(index < 0 || index >= this.pipes.size())
				this.pipesCache.add(_Report.psConsole_orig_out);
			else
				this.pipesCache.add(this.pipes.get(index));
		}
		this.outsRefresh(this.pipesCache);
	}

	public PrintStream pipe(int index)
	{
		if(index < 0 || index >= this.pipes.size())
			return _Report.psConsole_orig_out;
		else
			return this.pipes.get(index);
	}
	public PrintStream pipe(int... indexs)
	{
		if(indexs.length == 0)
			return _Report.psCloseWatermelon;

		this.pipesCache.clear();
		for(int index: indexs)
		{
			if(index < 0 || index >= this.pipes.size())
				this.pipesCache.add(_Report.psConsole_orig_out);
			else
				this.pipesCache.add(this.pipes.get(index));
		}
		this.outsCache.outsSet(this.pipesCache);
		return this.outsCache;
	}
}
