package zplum.tools._io_report;

import java.io.PrintStream;
import java.util.Collection;

public abstract class _ReportMega extends _Report
{
	protected PrintStream outs[];

	public _ReportMega(PrintStream... streams)
	{
		super(psConsole_orig_out);
		this.outsSet(streams);
	}

	public void outsSet(PrintStream... streams)
	{
		if(streams.length == 0)
			outs = null;
		else
			outs = streams;

	}
	public void outsSet(Collection<PrintStream> stream)
	{
		this.outsSet(stream.toArray(new PrintStream[stream.size()]));
	}

	public void write(byte[] b, int off, int len)
	{
		if(outs == null)
		{
			super.write_orig(b, off, len);
		} else {
			for(PrintStream out: outs)
				out.write(b, off, len);
		}
	}

	public void close()
	{
		for(PrintStream out: outs)
			out.close();

		super.close();
	}

}
