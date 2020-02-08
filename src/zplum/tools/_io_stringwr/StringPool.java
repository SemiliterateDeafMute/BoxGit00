package zplum.tools._io_stringwr;

public class StringPool extends StringQueuePoolAllot
{
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
