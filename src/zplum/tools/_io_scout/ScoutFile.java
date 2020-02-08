package zplum.tools._io_scout;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import zplum.kit.test0;
import zplum.tools.Abacus;
import zplum.tools._fc_timeobject.Watch;
import zplum.tools._io_report.Report;

@Deprecated

public abstract class ScoutFile extends ScoutFile_define implements Closeable,_Scout
{
	protected static class config
	{
		public static final long timevalueReLoopDefault = (long)Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.seconds, 5);
		public static final long timevalueReLoopTimeOutDefault = (long)Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.minutes, 5);
	}
	public static void main(String[] args)
	{
		test0.echo("123\r123");
		test0.echo("123\n123");
		test0.echo("123\n\r123");
	}
	public static void main1(String[] args) throws IOException
	{
		ScoutFile.InputAble scout = new ScoutFile.InputAble(new File("/Users/apustyphoon/Desktop/1"), new Report.WithFile("/Users/apustyphoon/Desktop/1b"),Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.seconds, 5), Abacus.time.valueOfTimeValue(Abacus.time.valueUnit.seconds, 60));
		test0.echoHere();
		scout.readRemainAndPrintToConsole();

		scout.close();
		test0.echo();
	}

	protected final ScFileBufferReader core;
	public ScoutFile(File fileToRead) throws FileNotFoundException
	{
		this.core = new ScFileBufferReader(fileToRead);
	}
	public ScoutFile(File fileToRead, PrintStream psFileToRecord) throws IOException
	{
		this.core = new ScFileBufferReader_WithRecord(fileToRead, psFileToRecord);
	}
	public String readLine() throws IOException
	{
		test0.echoHere();
		return this.core.readLine();
	}
	public void close() throws IOException
	{
		this.core.close();
	}

	protected abstract static class LoopAble extends ScoutFile
	{
		protected boolean isInsomnia = false;
		protected long timevalueReLoop = config.timevalueReLoopDefault;
		protected long timevalueReLoopTimeOut = config.timevalueReLoopTimeOutDefault;
		protected long timepointReLoopTimeOut_tmp = Watch.current() + this.timevalueReLoopTimeOut;
		protected LoopAble(File fileToRead) throws IOException
		{
			super(fileToRead);
		}
		protected LoopAble(File fileToRead, PrintStream psFileToRecord) throws IOException
		{
			super(fileToRead, psFileToRecord);
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
	protected static class Follow extends LoopAble
	{
		protected int lines = 0;
		public Follow(File fileToRead, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead, timevalueReLoop_millis, timevalueReLoopTimeOut_millis);
		}
		public Follow(File fileToRead, PrintStream psFileToRecord, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead, psFileToRecord, timevalueReLoop_millis, timevalueReLoopTimeOut_millis);
		}
		public String readLine() throws IOException
		{
			this.sleep_resetTimeOut();
			for(String line;;)
			{
				if(this.isInsomnia)
					return this.core.readLine();
				line = this.core.readTrueLine();
				if(line != null)
				{
					this.lines++;
					return line;
				}
				this.sleep();
				this.core.reset();
				for(int char_tmp, skip_line=this.lines; skip_line > 0;)
				{
					char_tmp = this.core.brFileToRead.read();
					if(char_tmp == '\n')
					{
						skip_line--;
					} else if(char_tmp < 0) {
						this.lines -= skip_line;
						skip_line = 0;
					}
				}
			}
		}
	}
	protected static class InputAble extends LoopAble
	{
		public InputAble(File fileToRead, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead, timevalueReLoop_millis, timevalueReLoopTimeOut_millis);
		}
		public InputAble(File fileToRead, PrintStream psFileToRecord, long timevalueReLoop_millis, long timevalueReLoopTimeOut_millis) throws IOException
		{
			super(fileToRead, psFileToRecord, timevalueReLoop_millis, timevalueReLoopTimeOut_millis);
		}
		public String readLine() throws IOException
		{
			this.sleep_resetTimeOut();
			this.isInsomnia = false;
			for(String line;;)
			{
				this.core.createFileToRead(false);
				this.core.reset();
				if(this.isInsomnia)
				{
					line = this.core.readLine();
				} else {
					line = this.core.readTrueLine();
					if(line == null)
					{
						this.sleep();
						continue;
					}
				}
				this.core.createFileToRead(true);
				return line;
			}
		}

		public String readRemain() throws IOException
		{
			this.sleep_resetTimeOut();
			this.isInsomnia = false;
			for(StringBuffer strb;;)
			{
				strb = new StringBuffer();
				this.core.createFileToRead(false);
				this.core.reset();
				for(String line;;)
				{
					if(this.isInsomnia)
					{
						line = this.core.readLine();
					} else {
						line = this.core.readTrueLine();
						if(line == null)
							strb = null;
					}
					if(line == null || line.equals("."))
						break;
					strb.append(line);
					strb.append('\n');
				}
				if(strb == null)
				{
					this.sleep();
					continue;
				}
				this.core.createFileToRead(true);
				return strb.toString();
			}
		}
		public void readRemain(ArrayList<String> list) throws IOException
		{
			this.sleep_resetTimeOut();
			this.isInsomnia = false;
			for(boolean isContinue;;)
			{
				list.clear();
				isContinue = false;
				this.core.createFileToRead(false);
				this.core.reset();
				for(String line;;)
				{
					if(this.isInsomnia)
					{
						line = this.core.readLine();
					} else {
						line = this.core.readTrueLine();
						if(line == null)
							isContinue = true;
					}
					if(line == null || line.equals("."))
						break;
					list.add(line);
				}
				if(isContinue)
				{
					this.sleep();
					continue;
				}
				this.core.createFileToRead(true);
				return;
			}
		}
		public void readRemainAndPrint(PrintStream out) throws IOException
		{
			ArrayList<String> list = new ArrayList<String>();
			this.readRemain(list);
			for(String line: list)
				out.println(line);
		}
	}
}

class ScoutFile_define
{
	protected static class ScFileBufferReader implements Closeable
	{
		protected File fileToRead = null;
		protected BufferedReader brFileToRead = null;
		protected StringBuffer   brFileToRead_strbRemain = null;
		public ScFileBufferReader(File fileToRead) throws FileNotFoundException
		{
			this.fileToRead = fileToRead;
			this.reset();
		}
		public void close() throws IOException
		{
			this.brFileToRead.close();
		}
		public void reset() throws FileNotFoundException
		{
			this.brFileToRead = null;
			if(this.fileToRead.exists() && this.fileToRead.isFile())
				this.brFileToRead = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileToRead)));
			this.brFileToRead_strbRemain = null;

		}
		public void createFileToRead(boolean isDelExist) throws IOException
		{
			if(isDelExist && this.fileToRead.exists() && this.fileToRead.isFile())
				this.fileToRead.delete();
			if(!this.fileToRead.exists())
				this.fileToRead.createNewFile();
		}
		public String readLine() throws IOException
		{
			if(this.brFileToRead_strbRemain == null)
				return this.brFileToRead.readLine();
			String str = this.brFileToRead_strbRemain.toString();
			this.brFileToRead_strbRemain = null;
			return str;
		}
		public String readTrueLine() throws IOException
		{
			if(this.brFileToRead == null)
				return null;
			StringBuffer strb = new StringBuffer();
			for(int c;;)
			{
				c = this.brFileToRead.read();
				if(c < 0)
				{
					if(strb.length() != 0)
						this.brFileToRead_strbRemain = strb;
					return null;
				} else {
					if(c == '\n')
						return strb.toString();
					strb.append((char)c);
				}
			}
		}
	}
	protected static class ScFileBufferReader_WithRecord extends ScFileBufferReader
	{
		protected PrintStream psFileToRecord = null;
		public ScFileBufferReader_WithRecord(File fileToRead, PrintStream psFileToRecord) throws IOException
		{
			super(fileToRead);
			this.psFileToRecord = psFileToRecord;
		}
		public ScFileBufferReader_WithRecord(File fileToRead, File fileToRecord) throws IOException
		{
			this(fileToRead, new Report.WithFile(fileToRecord));
			if(fileToRecord.getPath().equals(""))
			{
				System.err.println("ScFileBufferReader.WithRecord: FileToReacord_Path is empty, stream close.");
				this.psFileToRecord.close();
			}
		}
		public String readLine() throws IOException
		{
			String line = super.readLine();
			if(line != null)
				this.psFileToRecord.println(line);
			return line;
		}
		public String readTrueLine() throws IOException
		{
			String line = super.readTrueLine();
			if(line != null)
				this.psFileToRecord.println(line);
			return line;
		}
	}

}

