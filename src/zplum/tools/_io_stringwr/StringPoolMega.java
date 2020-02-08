package zplum.tools._io_stringwr;

public class StringPoolMega extends StringQueuePoolBroad
{
	public boolean isEmptyWriter()
	{
		if(this.dockWriter.size() == 0)
			return true;
		return false;
	}

	public static class Writer extends StringQueueWriter
	{
		public Writer()
		{
			super();
		}
		public Writer(StringQueuePool pool)
		{
			super(pool);
		}
	}
	public static class Reader extends StringQueueReader
	{
		public Reader()
		{
			super();
		}
		public Reader(StringQueuePool pool)
		{
			super(pool);
		}
	}
}
