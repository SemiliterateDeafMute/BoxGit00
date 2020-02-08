package zplum.plus;

import java.io.Writer;
import java.util.ArrayList;

public class RfStringBufferWriter extends Writer
{
	private StringBuffer buf;
	private boolean isClose = false;
	private ArrayList<RfStringBufferReader> dockStringReaderRf = new ArrayList<RfStringBufferReader>();
    private String lineSeparator = System.getProperty("line.separator");

	public RfStringBufferWriter()
	{
		buf = new StringBuffer();
		lock = buf;
	}

	public RfStringBufferWriter(int initialSize)
    {
		if (initialSize < 0) {
			throw new IllegalArgumentException("Negative buffer size");
		}
		buf = new StringBuffer(initialSize);
		lock = buf;
    }

	public void write(int c)
	{
		if(isClose)
			return;
		buf.append((char) c);
	}

	public void write(char cbuf[], int off, int len)
	{
		if(isClose)
			return;

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
		if(isClose)
			return;
		buf.append(str);
	}

	public void write(String str, int off, int len)
	{
		if(isClose)
			return;
		buf.append(str.substring(off, off + len));
	}

	public RfStringBufferWriter append(CharSequence csq)
	{
		if(isClose)
			return this;

		if (csq == null)
			write("null");
		else
			write(csq.toString());
		return this;
	}

	public RfStringBufferWriter append(CharSequence csq, int start, int end)
	{
		if(isClose)
			return this;

		CharSequence cs = (csq == null ? "null" : csq);
		write(cs.subSequence(start, end).toString());
		return this;
	}

	public RfStringBufferWriter append(char c)
	{
		if(isClose)
			return this;

		write(c);
		return this;
	}

	public String toString()
	{
		return buf.toString();
	}

	public StringBuffer getBuffer()
	{
		return buf;
	}

	public void flush()
	{
	}

	public void close()
	{
		isClose = true;
		for(RfStringBufferReader reader: dockStringReaderRf)
			reader.close();
	}

	public RfStringBufferWriter(StringBuffer buf)
	{
		this.buf = buf;
		lock = buf;
	}
	public void newLine()
	{
		write(lineSeparator);
	}

	public void dockReader(RfStringBufferReader... reader)
	{
		for(RfStringBufferReader readerTmp: reader)
		{
			if(this.buf == readerTmp.getBuffer())
				dockStringReaderRf.add(readerTmp);
			else
				System.err.println("waring: string buffer is not same, dont dock!");
		}
	}

}
