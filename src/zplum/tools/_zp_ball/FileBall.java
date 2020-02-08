package zplum.tools._zp_ball;

import java.io.File;
import java.io.IOException;

import zplum.kit.test0;
import zplum.tools._io_stringwr.StringPoolMega;

@Deprecated

@SuppressWarnings("serial")
public class FileBall extends File
{
	StringPoolMega pools = new StringPoolMega();
	public FileBall(String str) throws IOException
	{
		super(str);
		initWindUp();
	}
	public FileBall(File file) throws IOException
	{
		super(file.getPath());
		initWindUp();
	}
	private void initWindUp() throws IOException
	{
		this.createNewFile();

	}

	StringPoolMega.Writer makeWriter()
	{
		synchronized (lock)
		{
			if(pools.isEmptyWriter())
			{
				try {
					if(!unlock())
						lock.wait();
					return new StringPoolMega.Writer(pools);
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}

			return null;
		}

	}
	StringPoolMega.Reader makeReader()
	{
		return new StringPoolMega.Reader(pools);
	}

	private Object lock = new Object();
	private boolean unlock()
	{
		if(!key.equals(keyWord))
			return false;
		synchronized(lock)
		{
			lock.notifyAll();
		}
		return true;
	}

	private static String keyWord = "abc";
	private String key = "";
	@SuppressWarnings("unused")
	private void unlockTest()
	{
		if(this.isFile() && this.canRead() && this.canWrite() && this.exists())
			test0.echo(key.equals(keyWord) + "\t" + unlock() + "\t" + key);
		else
			test0.echo("no pass\t" + key);
	}
	public boolean isFile()
	{
		key = "a";
		return super.isFile();
	}
	public boolean canRead()
	{
		switch (key)
		{
			case "a":
				key = "ab";
				break;
			default:
				key = "";
		}
		return super.canRead();
	}
	public boolean canWrite()
	{
		switch (key)
		{
			case "ab":
				key = "abc";
				break;
			default:
				key = "";
		}
		return super.canWrite();
	}
	public boolean exists()
	{
		unlock();
		return super.exists();
	}

	Object lock2 = new Object();
	boolean isNotReady = true;

}
