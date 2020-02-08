package zplum.tools._io_report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;

import zplum.kit.Project;
import zplum.plus.RfPrintStream;
import zplum.plus.ScObject;

public abstract class _Report extends PrintStream
{
	protected static final class config
	{
		public static final String charset = Project.configCharset;
		public static final String nameFileDefault = "_java_print_file.txtv";
		public static final boolean total_isMonitorNewInstance = false;
	}
	protected static PrintStream psCloseWatermelon;
	protected static final PrintStream psConsole_orig_out = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out), 128), true);
	protected static final PrintStream psConsole_orig_err = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.err), 128), true);
	protected static final PrintStream psConsole = psConsole_orig_out;
	static {
		try {
			File watermelon;
			watermelon = Files.createTempFile("", "").toFile();
			psCloseWatermelon = new PrintStream(watermelon);
			watermelon.delete();
			psCloseWatermelon.close();
		} catch (IOException e) {
			System.err.println("Something wrong about watermelon ?!");
			psCloseWatermelon = null;
		}
	}

	public _Report()
	{
		this(_Report.psConsole_orig_out);

		if(_Report.total_isMonitorNewInstance)
			_Report.total_include(this);
	}

	protected _Report(OutputStream streamOut)
	{
		super(streamOut);

		if(_Report.total_isMonitorNewInstance)
			_Report.total_include(this);
	}
	protected void finalize() throws Throwable
	{
		this.close();
	}

	protected static PrintStream outMake(File file) throws IOException
	{
		if(file.getPath().equals(""))
		{
			System.err.println("ReportWarning: FilePath is empty, stream forward to Console");
			return _Report.psConsole_orig_out;
		}
		if(file.isDirectory())
		{
			file = new File(file.getAbsolutePath() + File.separator + _Report.config.nameFileDefault);
			System.err.println("ReportWarning: File originally is a directorie, File forward to " + file.getAbsolutePath());
		}
		return RfPrintStream.outMake(file, _Report.config.charset);
	}

	protected OutputStream getOut()
	{
		if(super.out instanceof _Report)
			return ((_Report) super.out).getOut();
		return super.out;
	}

	public void write_orig(byte b[], int off, int len)
	{
		super.write(b, off, len);
	}
	public abstract void write(byte b[], int off, int len);

	public void close()
	{
		this.radio_silence_break();
		if(_Report.psConsole_orig_out.equals(super.out))
			super.out = _Report.psCloseWatermelon;
		super.close();

		if(_Report.total_isMonitorNewInstance_hasbeentrue)
			_Report.total_exclude(this);
	}

	public void printToConsole(Object content)
	{
		_Report.psConsole.print(content);
	}
	public void printlnToConsole(Object content)
	{
		_Report.psConsole.println(content);
	}
	public void printlnToConsole()
	{
		_Report.psConsole.println();
	}

	private OutputStream psEmconWatermelon = null;

	public final void emcon(int level)
	{
		switch(level)
		{
			case 1:
				this.radio_silence_break();
				break;
			case 2:
				if(ScObject.equals(super.out, _Report.psConsole_orig_out))
					this.radio_silence();
				if(!ScObject.equals(this.psEmconWatermelon, _Report.psConsole_orig_out))
					this.radio_silence_break();
				break;
			case 3:
				if(ScObject.equals(super.out, _Report.psConsole_orig_out, _Report.psConsole_orig_err))
					this.radio_silence();
				if(!ScObject.equals(this.psEmconWatermelon, _Report.psConsole_orig_out, _Report.psConsole_orig_err))
					this.radio_silence_break();
				break;
			case 4:
				this.radio_silence();
				break;
		}
	}

	public final void radio_silence()
	{
		if(psEmconWatermelon == null)
		{
			psEmconWatermelon = super.out;
			super.out = psCloseWatermelon;
		}
	}

	public final void radio_silence_break()
	{
		if(psEmconWatermelon == null)
			return;

		super.out = psEmconWatermelon;
		psEmconWatermelon = null;
	}

	private static boolean total_isMonitorNewInstance = _Report.config.total_isMonitorNewInstance;
	private static boolean total_isMonitorNewInstance_hasbeentrue = _Report.config.total_isMonitorNewInstance||false;
	private static final ArrayList<_Report> total_list = new ArrayList<_Report>();
	public static final void total_monitor(boolean isMonitorNewInstance)
	{
		total_isMonitorNewInstance = isMonitorNewInstance;
		total_isMonitorNewInstance_hasbeentrue = isMonitorNewInstance||false;
	}
	public static final void total_include(_Report report)
	{
		total_list.add(report);
	}
	public static final void total_exclude(_Report report)
	{
		total_list.remove(report);
	}
	public static final void total_emcon(int level)
	{
		for(_Report report: total_list)
			report.emcon(level);
	}

}

