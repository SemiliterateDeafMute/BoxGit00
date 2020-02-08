package zplum.tools._fc_runcommand;

import java.io.PrintStream;

public class CommandRunerChain extends CommandRuner
{
	private Integer flag_chain = null;

	public CommandRunerChain(PrintStream outN, PrintStream outE)
	{
		super(outN, outE);
	}
	public CommandRunerChain(PrintStream out)
	{
		this(out, out);
	}
	public CommandRunerChain()
	{
		this(null, null);
	}

	public CommandRuner reset()
	{
		this.flag_chain = null;
		return this;
	}

	private CommandRuner runCmd_chain(String cmd, Integer key_howtodo)
	{
		int flag = 0;
		if(key_howtodo == null)
		{
			flag |= 0;
		} else switch(key_howtodo) {
			case 0:		flag |= 1;break;
			case 1:		flag |= 2;break;
			default:	flag |= 3;break;
		}

		if(this.flag_chain == null)
		{
			flag |= 0;
		} else switch(this.flag_chain)
		{
			case 0: 	flag |= 1;break;
			default:	flag |= 2;break;
		}

		if(flag < 3)
			this.flag_chain = super.runCmd(cmd);

		return this;
	}

	public CommandRuner runCmd_JustDo(String cmd)
	{
		return this.runCmd_chain(cmd, null);
	}
	public CommandRuner runCmd_JustDo(String cmds[])
	{
		CommandRuner me = this;
		for(String cmd: cmds )
		{
			me = this.runCmd_JustDo(cmd);
		}
		return me;
	}

	public CommandRuner runCmd_AfterTrue(String cmd)
	{
		return this.runCmd_chain(cmd, 0);
	}
	public CommandRuner runCmd_AfterTrue(String cmds[])
	{
		CommandRuner me = this;
		for(String cmd: cmds )
			me = this.runCmd_AfterTrue(cmd);
		return me;
	}

	public String runCmdAfterTrue_stopCmdWhenFalse(String cmds[])
	{
		for(String cmd: cmds)
		{
			this.runCmd_AfterTrue(cmd);
			if(this.flag_chain != 0)
				return cmd;
		}
		return null;
	}

}
