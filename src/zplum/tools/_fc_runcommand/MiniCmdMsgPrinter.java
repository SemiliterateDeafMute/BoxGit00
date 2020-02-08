package zplum.tools._fc_runcommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class MiniCmdMsgPrinter
{
	Process psCmd;
	Thread threadN;
	Thread threadE;
	PrintStream outE;

	MiniCmdMsgPrinter(Process psCmd, PrintStream outN, PrintStream outE)
	{
		this.psCmd = psCmd;

		this.threadN = this.initThread(psCmd.getInputStream(), ((outN!=null)? outN: System.out));
		this.threadE = this.initThread(psCmd.getErrorStream(), ((outE!=null)? outE: System.err));

		this.outE = (outE!=null)? outE: System.err;
	}
	MiniCmdMsgPrinter(Process psCmd)
	{
		this(psCmd, System.out, System.err);
	}
	MiniCmdMsgPrinter(Process psCmd, PrintStream out)
	{
		this(psCmd, out, out);
	}

	public Thread initThread(InputStream streamIn, PrintStream out)
	{
		Thread thread = new Thread()
		{
			public void run()
			{
				String tmp = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(streamIn));
				try {
					while((tmp=br.readLine()) != null)
					{
						out.println(tmp);
					}
				} catch (IOException e) {
					e.printStackTrace(outE);
				}
			}
		};
		return thread;
	}

	public void start()
	{
		this.threadN.start();
		this.threadE.start();
	}

	public void join() throws InterruptedException
	{
		this.threadN.join();
		this.threadE.join();
	}

}
