package zplum.tools._io_report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import zplum.plus.RfListArraySet;
import zplum.plus.RfPrintStream;

public abstract class ReportMega extends _ReportMega {
	protected HashSet<ReportMega> owners = new HashSet<ReportMega>();
	protected RfListArraySet<PrintStream> pipes = new RfListArraySet<PrintStream>();
	private HashMap<OutputStream, PrintStream> outs_regedit = new HashMap<OutputStream, PrintStream>();

	boolean runLock_recursive = true;

	protected ReportMega()
	{
		super();
	}
	protected ReportMega(PrintStream... pipes)
	{
		this();
		for(PrintStream pipe: pipes)
		{
			this.pipes.add(pipe);
			if(pipe instanceof ReportMega)
				((ReportMega) pipe).owners.add(this);
		}

	}

	@Override
	public void close()
	{
		this.out = _Report.psCloseWatermelon;

		for (PrintStream out : this.pipes)
			out.close();
		super.close();
	}

	private void outsMake(List<PrintStream> pipes)
	{
		this.outs_regedit.clear();
		for(PrintStream pipe : pipes)
		{
			if(pipe instanceof ReportMega)
			{
				this.outs_regedit.putAll(((ReportMega) pipe).outs_regedit);
				continue;
			}
			if(pipe instanceof Report)
			{
				this.outs_regedit.put(((Report) pipe).getOut(), pipe);
				continue;
			}
			this.outs_regedit.put(pipe, pipe);
		}
		this.outsSet(this.outs_regedit.values());

	}

	private List<PrintStream> outsUnion(List<PrintStream> pipes)
	{
		if(pipes == null)
			return null;

		boolean unknowClass = true;
		int sizeLast_outs;
		int sizeLast_outs_2 = this.outs_regedit.size();
		for(PrintStream pipe : pipes)
		{
			sizeLast_outs = this.outs_regedit.size();
			if(unknowClass && pipe instanceof ReportMega)
			{
				this.outs_regedit.putAll(((ReportMega) pipe).outs_regedit);
				unknowClass = false;
			}
			if(unknowClass && pipe instanceof Report)
			{
				this.outs_regedit.put(((Report) pipe).getOut(), pipe);
				unknowClass = false;
			}
			if(unknowClass)
				this.outs_regedit.put(pipe, pipe);
			if(sizeLast_outs == this.outs_regedit.size())
				pipes.remove(pipe);
		}

		if(sizeLast_outs_2 != this.outs_regedit.size())
			this.outsSet(this.outs_regedit.values());
		if(pipes.size() == 0)
			return null;
		return pipes;

	}

	protected void outsRefresh(List<PrintStream> pipes)
	{
		Collection<PrintStream> contentLast_outs = this.outs_regedit.values();
		this.outs_regedit.clear();
		this.outsMake(pipes);

		if(contentLast_outs.size()==this.outs_regedit.size() && !new ArrayList<PrintStream>(contentLast_outs).addAll(this.outs_regedit.values()) )
			return;

		this.runLock_recursive = false;
		for (ReportMega owner : this.owners)
			if(owner.runLock_recursive)
				owner.outsRefresh();
		this.runLock_recursive = true;

	}
	protected void outsRefresh()
	{
		this.outsRefresh(this.pipes);
	}

	protected void outsUpdate(List<PrintStream> pipes)
	{
		List<PrintStream> pipesComplement = this.outsUnion(pipes);

		if(pipesComplement == null)
			return;

		this.runLock_recursive = false;
		for (ReportMega owner : this.owners)
			if(owner.runLock_recursive && pipesComplement!=null)
				owner.outsUpdate(pipesComplement);
		this.runLock_recursive = true;
	}

	public int pipeIndex(PrintStream stream)
	{
		return this.pipes.indexOf(stream);
	}
	public int pipeSet(PrintStream stream)
	{
		int index = pipeIndex(stream);
		if(index >= 0)
			return index;
		this.pipes.add(stream);
		if(stream instanceof ReportMega)
			((ReportMega) stream).owners.add(this);
		return this.pipes.indexOf(stream);
	}
	public int pipeIndex(File file)
	{
		PrintStream stream;
		try {
			stream = RfPrintStream.outCheck(file.getAbsolutePath());
		} catch (IOException e) {
			stream = null;
		}
		return this.pipeIndex(stream);
	}
	public int pipeSet(File file) throws IOException
	{
		PrintStream stream = outMake(file);
		return this.pipeSet(stream);
	}
	public int pipeIndex(String pathFile)
	{
		PrintStream stream;
		try {
			stream = RfPrintStream.outCheck(pathFile);
		} catch (IOException e) {
			stream = null;
		}
		return this.pipeIndex(stream);
	}
	public int pipeSet(String pathFile) throws IOException
	{
		PrintStream stream = outMake(new File(pathFile));
		return this.pipeSet(stream);
	}

	public int[] pipeSet(PrintStream... streams)
	{
		int loog = streams.length;
		if(loog == 0)
			return null;

		int indexs[] = new int[streams.length];
		for(int i=0; i<loog; i++)
			indexs[i] = this.pipeSet(streams[i]);
		return indexs;
	}
	public int[] pipeSet(File... files) throws IOException
	{
		int loog = files.length;
		if(loog == 0)
			return null;

		int indexs[] = new int[files.length];
		for(int i=0; i<loog; i++)
			indexs[i] = this.pipeSet(files[i]);
		return indexs;
	}
	public int[] pipeSet(String... pathFiles) throws IOException
	{
		int loog = pathFiles.length;
		if(loog == 0)
			return null;

		int indexs[] = new int[pathFiles.length];
		for(int i=0; i<loog; i++)
			indexs[i] = this.pipeSet(pathFiles[i]);
		return indexs;
	}

}

