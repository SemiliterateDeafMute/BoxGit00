package zplum.tools._zp_ball;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import zplum.plus.RfThread;
import zplum.tools._io_report.Report;

@Deprecated

public class ReportBall extends Report.WithFile
{
	public ReportBall(File file) throws IOException
	{
		super(file);
		if(file instanceof FileBall)
		{
			writer = ((FileBall)file).makeWriter();
		} else {

			new RfThread() {
				public void run_script() throws Exception
				{
					synchronized(lock)
					{
						synchronized (file)
						{
							lock.wait();
						}
					}
				}
			}.start();;
		}
	}

	private Writer writer = null;
	public void write(byte[] b, int off, int len)
	{
		if(writer == null)
		{
			super.write(b, off, len);
		} else {
			try {
				writer.write(new String(b, off, len));
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close()
	{
		super.close();
		try {
			if(writer != null)
				writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(writer == null)
		synchronized (lock)
		{
			lock.notifyAll();
		}
	}

	private Object lock = new Object();
}

