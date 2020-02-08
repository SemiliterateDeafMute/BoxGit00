package zplum.tools._io_scout;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import zplum.tools._io_report.Report;

public interface _Scout
{
	public static PrintStream out = new Report();

	public String readLine() throws IOException;
	public default String readRemain() throws IOException
	{
		StringBuffer strb = new StringBuffer();
		for(String line; (line=this.readLine()) != null ;)
		{
			strb.append(line);
			strb.append('\n');
		}
		return strb.toString();
	}
	public default void readRemain(ArrayList<String> list) throws IOException
	{
		for(String line; (line=this.readLine()) != null ;)
			list.add(line);
	}
	public default void readRemainAndPrint(PrintStream out) throws IOException
	{
		for(String line; (line=this.readLine()) != null ;)
			out.println(line);
	}
	public default void readRemainAndPrintToConsole() throws IOException
	{
		this.readRemainAndPrint(_Scout.out);
	}
	public default void readRemainAndPrintToFile(File file) throws IOException
	{
		this.readRemainAndPrint(new Report.WithFile(file));
	}

}
