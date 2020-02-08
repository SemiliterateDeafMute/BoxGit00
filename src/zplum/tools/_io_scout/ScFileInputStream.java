package zplum.tools._io_scout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import zplum.kit.Project;
import zplum.kit.test0;
import zplum.tools.Abacus;
import zplum.tools._fc_timeobject.Watch;
import zplum.tools._io_report.Report;

@Deprecated

public class ScFileInputStream extends InputStream
{
	protected static class config
	{
		public static final String charset = Project.configCharset;
		public static final long timevalueReLoopDefault = (long)Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.seconds, 5);
		public static final long timevalueReLoopTimeOutDefault = (long)Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.minutes, 5);
	}
	public static void main(String[] args) throws IOException
	{
		test0.echoHere();
		Scout scout = new Scout(new ScFileInputStream.Follow(new File("/Users/apustyphoon/Desktop/1"), Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.seconds, 5), Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.seconds, 60)));
		test0.echo(scout.readLine());
		scout.close();
	}

	protected File fileToRead = null;
	protected FileInputStream isFileToRead = null;
	public ScFileInputStream(File fileToRead) throws FileNotFoundException
	{
		this.fileToRead = fileToRead;
		this.reset();
	}

	public void reset() throws FileNotFoundException
	{
		this.isFileToRead = null;
		if(this.fileToRead.exists() && this.fileToRead.isFile())
			this.isFileToRead = new FileInputStream(this.fileToRead);
	}
	public void createFileToRead(boolean isDelExist) throws IOException
	{
		if(isDelExist && this.fileToRead.exists() && this.fileToRead.isFile())
			this.fileToRead.delete();
		if(!this.fileToRead.exists())
			this.fileToRead.createNewFile();
	}

	public int read() throws IOException
	{
		return this.isFileToRead.read();
	}
	public void close() throws IOException
	{
		this.isFileToRead.close();
	}

	public static class WithRecord extends ScFileInputStream
	{
		protected PrintStream psFileToRecord = null;
		public WithRecord(File fileToRead, PrintStream psFileToRecord) throws IOException
		{
			super(fileToRead);
			this.psFileToRecord = psFileToRecord;
		}
		public WithRecord(File fileToRead, File fileToRecord) throws IOException
		{
			this(fileToRead, new Report.WithFile(fileToRecord));
			if(fileToRecord.getPath().equals(""))
			{
				System.err.println("ScFileBufferReader.WithRecord: FileToReacord_Path is empty, stream close.");
				this.psFileToRecord.close();
			}
		}
		public int read() throws IOException
		{
			int c = super.read();
			if(c >= 0)
				this.psFileToRecord.print((char)c);
			return c;
		}
	}

	protected abstract static class LoopAble extends InputStream
	{
		protected final ScFileInputStream core;

		protected boolean isInsomnia = false;
		protected long timevalueReLoop = config.timevalueReLoopDefault;
		protected long timevalueReLoopTimeOut = config.timevalueReLoopTimeOutDefault;
		protected long timepointReLoopTimeOut_tmp = Watch.current() + this.timevalueReLoopTimeOut;
		protected LoopAble(File fileToRead) throws IOException
		{
			this.core = new ScFileInputStream(fileToRead);
		}
		protected LoopAble(File fileToRead, PrintStream psFileToRecord) throws IOException
		{
			this.core = new ScFileInputStream.WithRecord(fileToRead, psFileToRecord);
		}
		protected LoopAble(File fileToRead, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			this(fileToRead);
			this.timevalueReLoop = timevalueReLoop_millis;
			this.timevalueReLoopTimeOut = timevalueReLoopTimeOut_millis;
			this.timepointReLoopTimeOut_tmp = Watch.current() + this.timevalueReLoopTimeOut;
		}
		protected LoopAble(File fileToRead, PrintStream psFileToRecord, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			this(fileToRead, psFileToRecord);
			this.timevalueReLoop = timevalueReLoop_millis;
			this.timevalueReLoopTimeOut = timevalueReLoopTimeOut_millis;
			this.timepointReLoopTimeOut_tmp = Watch.current() + this.timevalueReLoopTimeOut;
		}
		protected long timepoint_tmp = Watch.current();
		protected void sleep()
		{
			if(this.timevalueReLoopTimeOut == 0)
			{
				this.isInsomnia = true;
			} else if(this.timevalueReLoopTimeOut > 0) {
				this.timepoint_tmp = Watch.current();
				if(Watch.sleep(Math.min(this.timevalueReLoop, this.timepointReLoopTimeOut_tmp - Watch.current())) != 0)
					this.isInsomnia = true;
				if(this.timepointReLoopTimeOut_tmp <= Watch.current())
					this.isInsomnia = true;
			}
		}
		protected void sleep_resetTimeOut()
		{
			this.timepointReLoopTimeOut_tmp += Watch.current() - this.timepoint_tmp;
		}

		static {}
	}

	public static class Follow extends LoopAble
	{
		protected long skip_chars = 0;
		public Follow(File fileToRead, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead, timevalueReLoop_millis, timevalueReLoopTimeOut_millis);
		}
		public Follow(File fileToRead, PrintStream psFileToRecord, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead, psFileToRecord, timevalueReLoop_millis, timevalueReLoopTimeOut_millis);
		}
		public int read() throws IOException
		{
			this.sleep_resetTimeOut();
			for(int c;!test0.trueMelon;)
			{
				if(this.isInsomnia)
					return this.core.read();
				c = this.core.read();
				if(c >= 0)
				{
					this.skip_chars++;
					return c;
				}
				this.sleep();
				this.core.reset();
				this.core.skip(this.skip_chars);
			}

			int c = this.core.read();
			test0.echot(test0.here(), (char)c, c, test0.intMelon++);
			return c;
		}
	}

	protected static class InputAble extends LoopAble
	{
		protected boolean isWait = true;
		protected BufferedReader scout = null;
		public InputAble(File fileToRead, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead);
			this.scout = new BufferedReader( new InputStreamReader(
												new ScFileInputStream.Follow(fileToRead, timevalueReLoop_millis, timevalueReLoopTimeOut_millis),
												config.charset
												));
		}
		public InputAble(File fileToRead, PrintStream psFileToRecord, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead, psFileToRecord);
			this.scout = new BufferedReader( new InputStreamReader(
												new ScFileInputStream.Follow(fileToRead, timevalueReLoop_millis, timevalueReLoopTimeOut_millis),
												config.charset
												));
		}
		public int read() throws IOException
		{
			if(this.isWait)
				this.isInsomnia = (this.scout.readLine()==null);
			if(this.isInsomnia)
				return this.core.read();
			this.isWait = false;
			int c = this.core.read();
			if(c == '\n')
			{
				this.isWait = true;
				this.core.createFileToRead(true);
			}
			return c;
		}
	}

}
