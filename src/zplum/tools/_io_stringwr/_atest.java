package zplum.tools._io_stringwr;

import java.io.BufferedReader;

interface _atest
{
	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args)
	{
		StringQueuePoolBroad pool = new StringQueuePoolBroad();
		StringQueueWriter writer = new StringQueueWriter(pool);
		StringQueueReader reader = new StringQueueReader(pool);
		StringQueueReader reader02 = new StringQueueReader(pool);
		StringQueueReader reader03 = new StringQueueReader(pool);
		StringQueueReader reader04 = new StringQueueReader(pool);
		StringQueueReader reader05 = new StringQueueReader(pool);

		writer.close();
	}

	@SuppressWarnings({ "resource", "unused" })
	public static void main0(String[] args)
	{
		StringQueuePoolBroad pool = new StringQueuePoolBroad();
		StringQueueWriter writer = new StringQueueWriter(pool);
		StringQueueReader reader = new StringQueueReader(pool);
		StringQueueReader reader02 = new StringQueueReader(pool);
		BufferedReader streamIn = new BufferedReader(reader);
		BufferedReader streamIn02 = new BufferedReader(reader02);

		writer.close();

	}
}
