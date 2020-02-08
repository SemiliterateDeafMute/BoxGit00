package zplum.tools._fc_runcommand;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class CommandRuner
{
	protected static final String[] cmdTemplate = new String[3];
	protected static final File defaultCmdWorkFolder = null;
	static {
		CommandRuner.cmdTemplate[0] = "bash";
		CommandRuner.cmdTemplate[1] = "-c";
	}

	private static int runCmd(PrintStream outN, PrintStream outE, String[] cmd, String[] cmdParam, File cmdWorkFolder ,MiniCmdMsgPrinter mcmp)
	{
		if(cmdWorkFolder != null)
		{
			if(!cmdWorkFolder.exists() || !cmdWorkFolder.isDirectory())
			{
				outE.println("Warning: File not found, or file is not directory.");
				cmdWorkFolder = defaultCmdWorkFolder;
			}
		}

		try
		{
			try
			{
				Process psCmd = Runtime.getRuntime().exec(cmd, cmdParam, cmdWorkFolder);
				mcmp = new MiniCmdMsgPrinter(psCmd, outN, outE);

				mcmp.start();

				psCmd.waitFor();
				mcmp.join();

				return psCmd.exitValue();

			} catch (IOException e) {
				if (mcmp!=null)
				{
					mcmp.join();
				}
				throw e;
			}
		} catch (InterruptedException | IOException e) {

			e.printStackTrace(outE);
			return 1;
		}
	}
	public static int runCmd0(String cmdContent)
	{
		String[] cmd = cmdTemplate.clone();
		cmd[2] = cmdContent;
		return runCmd(System.out, System.err, cmd, null, null, null);
	}
	public static int runCmd0(PrintStream out, String cmdContent)
	{
		out = (out!=null)?out:System.out;
		String[] cmd = cmdTemplate.clone();
		cmd[2] = cmdContent;
		return runCmd(out, out, cmd, null, null, null);
	}
	public static int runCmd0(PrintStream outN, PrintStream outE, String cmdContent)
	{
		String[] cmd = cmdTemplate.clone();
		cmd[2] = cmdContent;
		return runCmd(outN, outE, cmd, null, null, null);
	}
	public static int runCmd0(PrintStream out, File cmdWorkFolder, String cmdContent)
	{
		out = (out!=null)?out:System.out;
		String[] cmd = cmdTemplate.clone();
		cmd[2] = cmdContent;
		return runCmd(out, out, cmd, null, cmdWorkFolder, null);
	}

	private static int runCmd_withoutShell(PrintStream outN, PrintStream outE, String cmd, String[] cmdParam, File cmdWorkFolder ,MiniCmdMsgPrinter mcmp)
	{
		if(cmdWorkFolder != null)
		{
			if(!cmdWorkFolder.exists() || !cmdWorkFolder.isDirectory())
			{
				outE.println("Warning: File not found, or file is not directory.");
				cmdWorkFolder = defaultCmdWorkFolder;
			}
		}

		try
		{
			try
			{
				Process psCmd = Runtime.getRuntime().exec(cmd, cmdParam, cmdWorkFolder);
				mcmp = new MiniCmdMsgPrinter(psCmd, outN, outE);

				mcmp.start();

				psCmd.waitFor();
				mcmp.join();

				return psCmd.exitValue();

			} catch (IOException e) {
				if (mcmp!=null)
				{
					mcmp.join();
				}
				throw e;
			}
		} catch (InterruptedException | IOException e) {

			e.printStackTrace(outE);
			return 1;
		}
	}
	public static int runCmd_withoutShell(PrintStream out, File cmdWorkFolder, String cmdContent)
	{
		out = out!=null?out:System.out;
		return runCmd_withoutShell(out, out, cmdContent, null, cmdWorkFolder, null);
	}

	private static String runCmdList_AfterTrue(PrintStream outN, PrintStream outE, String[] cmdTemplate, String[] cmdContents, String[] cmdParam, File cmdWorkFolder)
	{
		int flag_chain = 0;
		String cmd[] = cmdTemplate.clone();

		try
		{
			for(int i=0; i<cmdContents.length; i++)
			{
				cmd[2] = cmdContents[i];
				flag_chain = runCmd(outN, outE, cmd, cmdContents, cmdWorkFolder, null);
				if(flag_chain != 0)
				{
					return cmd[2];
				}
			}
		} catch (Exception e) {
			e.printStackTrace(outE);
			return cmd[2];
		}
		return null;
	}
	public static String runCmdList_AfterTrue(PrintStream out, String... cmdContents)
	{
		return runCmdList_AfterTrue(out, out, cmdTemplate, cmdContents, null, null);
	}

	private String[] cmd;
	private ArrayList<String> cmdParam;
	private File cmdWorkFolder;
	private PrintStream outN;
	private PrintStream outE;

	public CommandRuner(PrintStream outN, PrintStream outE)
	{
		this.cmd = cmdTemplate.clone();
		this.cmdParam = new ArrayList<String>();
		this.cmdWorkFolder = null;
		this.outN = outN;
		this.outE = outE;
	}
	public CommandRuner(PrintStream out)
	{
		this(out, out);
	}
	public CommandRuner()
	{
		this(null, null);
	}

	public void setShell(String shell)
	{
		this.cmd[0] = shell;
	}
	public String getShell()
	{
		return this.cmd[0];
	}

	public void setCmdWorkFolder(String pathFolder)
	{
		File file = new File(pathFolder);
		if(file != null)
		{
			if(!file.exists() || !file.isDirectory())
			{
				this.outE.println("Warning: File not found, or file is not directory. Keep former CmdWorkFolder,Don’t change CmdWorkFolder.");
				return;
			}
		}
		this.cmdWorkFolder = file;
	}
	public String getCmdWorkFolder()
	{
		return this.cmdWorkFolder.getPath();
	}

	private int findParam(String paramName)
	{
		int index = 0;
		for(String tmp:this.cmdParam)
		{
			if( tmp.split("=",2)[0].equals(paramName) )
			{
				return index;
			}
			index++;
		}
		return -1;
	}
	private void insterParam(String paramName, String paramContext, boolean isSet)
	{
		int index = this.findParam(paramName);
		if(index == -1)
		{
			this.cmdParam.add(paramName + "=" + paramContext);
		} else if (isSet) {
			this.cmdParam.set(index, paramName + "=" + paramContext);
		}
	}
	private String[] carveParam(String sParam)
	{
		String aParam[];
		int indexChar = sParam.indexOf('=');

		if(indexChar < 0)
		{
			aParam = new String[2];
			aParam[0] = sParam;
			aParam[1] = "";
		} else {
			aParam = sParam.split("=", 2);
		}

		return aParam;
	}

	public void addParam(String paramName, String paramContext)
	{
		this.insterParam(paramName, paramContext, false);
	}
	public void addParam(String sParam)
	{
		String tmp[] = this.carveParam(sParam);
		this.insterParam(tmp[0], tmp[1], false);
	}
	public void addParams(String[] params)
	{
		String aTmp[];
		for(String sTmp:params)
		{
			aTmp = this.carveParam(sTmp);
			this.insterParam(aTmp[0], aTmp[1], false);
		}
	}

	public void setParam(String paramName, String paramContext)
	{
		this.insterParam(paramName, paramContext, true);
	}
	public CommandRuner setParam(String sParam)
	{
		String tmp[] = this.carveParam(sParam);
		this.insterParam(tmp[0], tmp[1], true);
		return this;
	}
	public void setParams(String[] params)
	{
		String aTmp[];
		for(String sTmp:params)
		{
			aTmp = this.carveParam(sTmp);
			this.insterParam(aTmp[0], aTmp[1], true);
		}
	}

	private String[] getCmdParams()
	{
		if(this.cmdParam.size() == 0)
			return null;
		String params[] = new String[this.cmdParam.size()];
		return this.cmdParam.toArray(params);
	}

	public int runCmd(String cmdContent)
	{
		this.cmd[2] = cmdContent;
		return CommandRuner.runCmd(this.outN, this.outE, this.cmd, this.getCmdParams(), this.cmdWorkFolder, null);
	}

	public void runCmdList_AfterTrue(String... cmdContents)
	{
		CommandRuner.runCmdList_AfterTrue(this.outN, this.outE, this.cmd, cmdContents, this.getCmdParams(), cmdWorkFolder);
	}

}

