package zplum.tools._io_stringwr;

import java.util.concurrent.LinkedBlockingQueue;

class StringQueuePoolAllot extends StringQueuePool
{
	public StringQueuePoolAllot()
	{}
	public StringQueuePoolAllot(StringQueueWriter writer)
	{
		dock(writer);
	}
	public StringQueuePoolAllot(StringQueueReader reader)
	{
		dock(reader);
	}
	public StringQueuePoolAllot(StringQueueReader reader, StringQueuePoolBroad master)
	{
		dock(reader);
		master.dock(this);

	}

	private LinkedBlockingQueue<StringBuffer> pool = new LinkedBlockingQueue<StringBuffer>();
	public void put(StringBuffer content)
	{
		try
		{
			this.pool.put(content);
		} catch (InterruptedException e) {

			System.err.println("Waring!!! Check the RowCount!!! (Catch the InterruptedException.)");
			Thread.currentThread().interrupt();

			System.exit(1);
		}
	}
	public StringBuffer take()
	{
		try
		{
			return pool.take();
		} catch (InterruptedException e) {

			System.err.println("Waring!!! Check the RowCount!!! (Catch the InterruptedException.)");
			Thread.currentThread().interrupt();

			System.exit(1);
		}
		return new StringBuffer();
	}
	public int size()
	{
		return pool.size();
	}
	public void clear()
	{
		this.pool.clear();
	}

	StringQueuePoolAllot dockDo(StringQueueReader reader)
	{
		dockReader.add(reader);
		return this;
	}

	protected StringQueuePoolAllot close(_StringQueueCloseable closer)
	{
		if(dockWriter.remove(closer))
			if(dockWriter.size() == 0)
				close();
		if(dockReader.remove(closer))
			if(dockReader.size() == 0)
				closeNow();
		return poolNull;
	}
}
