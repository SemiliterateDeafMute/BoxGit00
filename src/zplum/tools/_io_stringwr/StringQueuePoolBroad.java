package zplum.tools._io_stringwr;

import java.util.HashSet;
import java.util.Iterator;

class StringQueuePoolBroad extends StringQueuePool
{
	public StringQueuePoolBroad()
	{}
	public StringQueuePoolBroad(StringQueueWriter writer)
	{
		this();
		dock(writer);
	}
	public StringQueuePoolBroad(StringQueueReader reader)
	{
		this();
		dock(reader);
	}

	protected HashSet<StringQueuePool> pools = new HashSet<StringQueuePool>();
	public void put(StringBuffer content)
	{
		for(StringQueuePool pool: pools)
			pool.put(content);
	}
	public void clear()
	{
		for(StringQueuePool pool: pools)
			pool.clear();
	}

	StringQueuePoolAllot dockDo(StringQueueReader reader)
	{
		return new StringQueuePoolAllot(reader, this);

	}

	private boolean runLock_recursive = true;
	protected StringQueuePoolAllot close(_StringQueueCloseable closer)
	{
		dockWriter.remove(closer);

		runLock_recursive = false;
		for (StringQueuePool pool : pools)
		{
			if(pool instanceof StringQueuePoolBroad)
				if(	!((StringQueuePoolBroad)pool).runLock_recursive )
					continue;
			pool.close(closer);
			if(pool.isClose == true)
				pools.remove(pool);
		}
		if(dockWriter.size() == 0)
			close();
		if(pools.size() == 0)
			close();
		runLock_recursive = true;

		return poolNull;
	}
	protected void close(StringQueuePool poolClosed)
	{
		if(!poolClosed.isClose)
			System.err.println("pool has not been closed !!!");
		if( pools.remove(poolClosed) )
			if( pools.size() == 0)
				close();
	}

	public void dock(StringQueuePool poolSlave)
	{
		synchronized (pools)
		{
			pools.add(poolSlave);
			if(poolSlave.poolMasters == null)
				poolSlave.poolMasters = new HashSet<StringQueuePoolBroad>();
			poolSlave.poolMasters.add(this);
		}
	}

	public void close()
	{
		super.close();
		int count = pools.size();
		for(	Iterator<StringQueuePool> iterator = pools.iterator() ;iterator.hasNext();)
		{
			iterator.next().close();
			if(count == pools.size())
				continue;
			count = pools.size();
			iterator = pools.iterator();
		}

	}

}
