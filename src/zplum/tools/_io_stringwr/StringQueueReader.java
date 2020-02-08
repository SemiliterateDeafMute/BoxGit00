package zplum.tools._io_stringwr;

import java.io.IOException;
import java.io.Reader;

class StringQueueReader extends Reader implements _StringQueueCloseable
{
	private StringBuffer buf = null;
	private int next = 0;

	private StringQueuePoolAllot pool = null;
	private int bufCutSize = _define.bufCutSizeDefault;

	public StringQueueReader()
	{
		buf = new StringBuffer();
		this.pool = new StringQueuePoolAllot(this);
	}
	public StringQueueReader(StringQueuePool pool)
	{
		buf = new StringBuffer();
		this.pool = pool.dock(this);
	}

	private void bufRefresh() throws IOException
	{
		if(next > bufCutSize)
		{
			buf = new StringBuffer(buf.substring(next));
			next = 0;
		}

		while(pool.size()>0 && buf.length()<bufCutSize)
			buf.append(pool.take());
	}

	public int read() throws IOException
	{
		synchronized (lock)
		{
			bufRefresh();
			if (next>=buf.length() && pool.isClose)
				return -1;
			return buf.charAt(next++);
		}
	}
	public int read(char cbuf[], int off, int len) throws IOException
	{
		synchronized (lock)
		{
			bufRefresh();
			if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0))
			{
				throw new IndexOutOfBoundsException();
			} else if (len == 0)
			{
				return 0;
			}
			if (next>=buf.length() && pool.isClose)
				return -1;
			int n = Math.min(buf.length() - next, len);
			buf.getChars(next, next + n, cbuf, off);
			next += n;
			return n;
		}
	}
	public long skip(long ns) throws IOException
	{
		synchronized (lock)
		{
			if (next >= buf.length())
				return 0;
			long n = Math.min(buf.length() - next, ns);
			n = Math.max(-next, n);
			next += n;
			return n;
		}
	}

	public boolean ready() throws IOException
	{
		synchronized (lock)
		{
			return true;
		}
	}

	public void close()
	{
		pool = pool.close(this);
	}
	public void closeErr(int i)
	{
		pool.closeErr(i);
	}

	public StringQueuePool getPool()
	{
		if(pool == null)
			return null;
		return pool.getPoolSelf();
	}

}
