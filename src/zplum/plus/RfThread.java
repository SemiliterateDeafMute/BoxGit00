package zplum.plus;

import java.io.PrintStream;

import zplum.tools._io_report.Report;

public abstract class RfThread extends Thread
{
	private boolean hasErr;
	private PrintStream out;
	private PrintStream err;
	public RfThread()
	{
		this.out = new Report();
		this.err = new Report.Err();
	}
	public RfThread(PrintStream out)
	{
		this.out = out;
		this.err = out;
	}

	public RfThread setErr(PrintStream err)
	{
		this.err = err;
		return this;
	}

	private String strLogFinish = null;
	public RfThread setLogFinish(String str)
	{
		strLogFinish = str;
		return this;
	}

	private boolean isExitWhenErr = false;
	public RfThread exitWhenErr(boolean isExitWhenErr)
	{
		this.isExitWhenErr = isExitWhenErr;
		return this;
	}
	public RfThread exitWhenErr()
	{
		this.isExitWhenErr = true;
		return this;
	}

	@Override
	public void run()
	{
		hasErr = false;
		try {
			run_script();
		} catch(Exception e) {
			e.printStackTrace(err);
			hasErr = true;
		}
		if(strLogFinish != null)
			out.println(strLogFinish);
		if(hasErr && isExitWhenErr)
			System.exit(1);
	}
	public abstract void run_script() throws Exception;

}
