package zplum.tools._io_stringwr;

import java.util.HashSet;

abstract class StringQueuePool
{
	StringQueuePool()
	{
		init();
	}
	protected void init()
	{
		poolNull = new StringQueuePoolNull(this);
	}

	public abstract void put(StringBuffer content);
	public abstract void clear();

	protected StringQueuePoolNull poolNull;
	protected HashSet<StringQueuePoolBroad> poolMasters = null;
	protected HashSet<StringQueueWriter> dockWriter = new HashSet<StringQueueWriter>();
	protected HashSet<StringQueueReader> dockReader = new HashSet<StringQueueReader>();
	final StringQueuePool dock(StringQueueWriter writer)
	{
		synchronized(dockWriter)
		{
			if(!dockCheck(writer))
				return _define.StringQueuePoolNull;
			dockWriter.add(writer);
			return this;
		}
	}
	final StringQueuePoolAllot dock(StringQueueReader reader)
	{
		synchronized(dockReader)
		{
			if(!dockCheck(reader))
				return  _define.StringQueuePoolNull;
			return dockDo(reader);
		}
	}
	abstract StringQueuePoolAllot dockDo(StringQueueReader reader);
	boolean dockCheck(_StringQueueCloseable closer)
	{
		if(!isSolid)
			if(closer.getPool() == null)
				return true;
		return false;
	}

	protected abstract StringQueuePool close(_StringQueueCloseable closer);

	protected boolean isClose = false;
	protected boolean isSolid = false;
	public void solid()
	{
		this.isSolid = true;
	}
	public void close()
	{
		this.isClose = true;
		this.isSolid = true;
		if(poolMasters == null)
			return;

		for(StringQueuePoolBroad poolMulti: poolMasters)
		{
			poolMasters.remove(poolMulti);
			poolMulti.close(this);
		}
	}
	public void closeNow()
	{
		close();
		this.clear();
	}
	public void closeErr(int exception)
	{
		closeNow();
		this.isException |= exception;
	}

	protected int isException = 0;
	public int isException()
	{
		return isException;
	}
	public StringQueuePool getPoolSelf()
	{
		return this;
	}
}
