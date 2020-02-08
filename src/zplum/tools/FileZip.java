package zplum.tools;

import java.io.File;
import java.io.IOException;

import zplum.tools._io_report.Report;
import zplum.tools._io_scout.Scout;

public class FileZip
{
	public static class Write extends Report.WithFileZip
	{
		public Write(File objFile) throws IOException
		{
			super(objFile);
		}
	}

	public static class Read extends Scout.WithFileZip
	{
		public Read(File file) throws IOException
		{
			super(file);
		}
	}
}
