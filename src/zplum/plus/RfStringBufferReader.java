package zplum.plus;

import java.io.IOException;
import java.io.Reader;

public class RfStringBufferReader extends Reader
{
	private StringBuffer buf;

	private boolean isClose = false;
	private int next = 0;
	private int mark = 0;

	public RfStringBufferReader(String s)
    {
    		this();
    		this.buf.append(s);

    }

	private void ensureOpen() throws IOException
	{
	}

	public int read() throws IOException
	{
		synchronized (lock)
		{
			ensureOpen();
			if (next>=buf.length() || isClose)
				return -1;
			return buf.charAt(next++);
		}
	}

	public int read(char cbuf[], int off, int len) throws IOException
	{
		synchronized (lock)
		{
			ensureOpen();
			if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0))
			{
				throw new IndexOutOfBoundsException();
			} else if (len == 0)
			{
				return 0;
			}
			if (next>=buf.length() || isClose)
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
			ensureOpen();
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
			ensureOpen();
			return true;
		}
	}

	public boolean markSupported()
	{
		return true;
	}

	public void mark(int readAheadLimit) throws IOException
	{
		if (readAheadLimit < 0)
		{
			throw new IllegalArgumentException("Read-ahead limit < 0");
		}
		synchronized (lock)
		{
			ensureOpen();
			mark = next;
		}
	}

	public void reset() throws IOException
	{
		synchronized (lock)
		{
			ensureOpen();
			next = mark;
		}
	}

	public void close()
	{
		isClose = true;
	}

	public RfStringBufferReader()
	{
		this.buf = new StringBuffer();

	}
	public RfStringBufferReader(StringBuffer buf)
	{
		this.buf = buf;

	}
	public  StringBuffer getBuffer()
	{
		return this.buf;
	}
}
