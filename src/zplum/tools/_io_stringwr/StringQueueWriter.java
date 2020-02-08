package zplum.tools._io_stringwr;

import java.io.Writer;

class StringQueueWriter extends Writer implements _StringQueueCloseable
{
	protected StringBuffer buf;
    private String lineSeparator = System.getProperty("line.separator");

	private StringQueuePool pool = null;
	private int bufCutSize = _define.bufCutSizeDefault;

	public StringQueueWriter()
	{
		this.buf = new StringBuffer();
		this.pool = new StringQueuePoolAllot(this);
	}
	public StringQueueWriter(StringQueuePool pool)
	{
		this.buf = new StringBuffer();
		this.pool = pool.dock(this);
	}

	public void write(int c)
	{
		if(pool.isClose)
			return;
		flushAuto();

		buf.append((char) c);
	}
	public void write(char cbuf[], int off, int len)
	{
		if(pool.isClose)
			return;
		flushAuto();

		if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0))
		{
			throw new IndexOutOfBoundsException();
		} else if (len == 0)
		{
			return;
		}
		buf.append(cbuf, off, len);
	}
	public void write(String str)
	{
		if(pool.isClose)
			return;
		flushAuto();

		buf.append(str);
	}
	public void write(String str, int off, int len)
	{
		if(pool.isClose)
			return;
		flushAuto();

		buf.append(str.substring(off, off + len));
	}
	public void newLine()
	{
		if(pool.isClose)
			return;
		flushAuto();

		buf.append(lineSeparator);
	}

	public StringQueueWriter append(CharSequence csq)
	{
		if(pool.isClose)
			return this;

		if (csq == null)
			write("null");
		else
			write(csq.toString());
		return this;
	}
	public StringQueueWriter append(CharSequence csq, int start, int end)
	{
		if(pool.isClose)
			return this;

		CharSequence cs = (csq == null ? "null" : csq);
		write(cs.subSequence(start, end).toString());
		return this;
	}
	public StringQueueWriter append(char c)
	{
		if(pool.isClose)
			return this;

		write(c);
		return this;
	}

	public void flush()
	{
		if(buf.length() <= 0)
			return;

		pool.put(buf);
		buf = new StringBuffer();
	}
	private void flushAuto()
	{
		if(buf.length() >= bufCutSize)
			flush();
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
