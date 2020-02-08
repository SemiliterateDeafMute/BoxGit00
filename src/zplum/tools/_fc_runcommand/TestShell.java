package zplum.tools._fc_runcommand;

import java.io.File;
import java.io.IOException;

public class TestShell
{
	public static void main0(String[] args) throws IOException
	{
		String str = System.getProperty("user.home");
		System.out.println(str);

		File file = null;
		file = new File("test20180224");
		file.createNewFile();
		System.out.println(file.toPath().toAbsolutePath().toString());

		System.out.println("~");
	}

}
