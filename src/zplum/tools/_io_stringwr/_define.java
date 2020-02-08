package zplum.tools._io_stringwr;

import java.io.Closeable;

interface _define
{
	static int bufCutSizeDefault = 10 * 1024 * 1024;
	static StringQueuePoolNull StringQueuePoolNull = new StringQueuePoolNull(null);
}

interface _StringQueueCloseable extends Closeable
{
	public StringQueuePool getPool();
}

class StringQueuePoolNull extends StringQueuePoolAllot
{
	private StringQueuePool orig;
	StringQueuePoolNull(StringQueuePool orig)
	{
		this.orig = orig;
		super.isClose = true;
		super.isSolid = true;
		super.clear();
		super.close();
	}
	@Override
	protected void init()
	{}

	@Override
	public StringQueuePool getPoolSelf()
	{
		return orig;
	}
	@Override
	public int size()
	{
		return 0;
	}
	@Override
	protected StringQueuePoolAllot close(_StringQueueCloseable closer)
	{
		return this;
	}
}
