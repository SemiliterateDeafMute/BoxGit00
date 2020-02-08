package zplum.toolz;

import java.io.PrintStream;

import zplum.plus.ScString;
import zplum.tools._fc_runcommand.CommandRuner;

public class KitScript
{
	private static class std
	{
		static String str_empty = "";
		static String str_space = " ";
		static String str_comma = ",";
		static String str_quote = "\"";
		static String str_newline = "\n";
	}

	private static <T> String transferArrayToCsv(T objs[], String aroundSymbolLeft, String aroundSymbolRight, String indentSymbol, int indentLoog, String separateSymbol)
	{
		if(objs==null || objs.length == 0)
			return std.str_empty;
		indentSymbol = (indentSymbol==null)?"":indentSymbol;
		separateSymbol = (separateSymbol==null)?"":separateSymbol;
		aroundSymbolLeft = (aroundSymbolLeft==null)?"":aroundSymbolLeft;
		aroundSymbolRight = (aroundSymbolRight==null)?"":aroundSymbolRight;

		String head = std.str_empty;
		for(int i=0; i<indentLoog; i++)
			head = head.concat(indentSymbol);
		String tail = std.str_comma.concat(separateSymbol);

		String content = separateSymbol;
		for(T obj: objs)
			content = content.concat(head).concat(ScString.around(obj.toString(), aroundSymbolLeft, aroundSymbolRight)).concat(tail);
		content = content.concat(head);
		return content;
	}
	public static <T> String transferArrayToCsv(T objs[])
	{
		return KitScript.transferArrayToCsv(objs, std.str_empty, std.str_empty, std.str_empty, 0, std.str_space);
	}
	public static <T> String transferArrayToCsv(T objs[], int indentLoog)
	{
		return KitScript.transferArrayToCsv(objs, std.str_empty, std.str_empty, std.str_space, indentLoog, std.str_newline);
	}
	public static <T> String transferArrayToCsv(T objs[], String indentSymbol, int indentLoog)
	{
		return KitScript.transferArrayToCsv(objs, std.str_empty, std.str_empty, indentSymbol, indentLoog, std.str_newline);
	}
	public static String transferArrayStrToCsvStr(String objs[])
	{
		return KitScript.transferArrayToCsv(objs, std.str_quote, std.str_quote, std.str_quote, 0, std.str_space);
	}
	public static String transferArrayStrToCsvStr(String objs[], int indentLoog)
	{
		return KitScript.transferArrayToCsv(objs, std.str_quote, std.str_quote, std.str_space, indentLoog, std.str_newline);
	}
	public static String transferArrayStrToCsvStr(String objs[], String indentSymbol, int indentLoog)
	{
		return KitScript.transferArrayToCsv(objs, std.str_quote, std.str_quote, indentSymbol, indentLoog, std.str_newline);
	}

	public static void runit_python(String script_content)
	{
		KitScript.runit_python_way0(script_content);
	}
	public static void runit_python(String script_outline, Object... script_outline_args)
	{
		KitScript.runit_python_way0(String.format(script_outline, script_outline_args));
	}

	public static void runit_python_byJython(String script_content)
	{
		KitScript.runit_python_way1(script_content);
	}
	public static int runit_python(String script_content, boolean isKeepExecWhenHaveError)
	{
		return KitScript.runit_python_way2(script_content, isKeepExecWhenHaveError);
	}

	private static boolean isForce_runitPythonWay2 = false;
	private static void runit_python_way0(String script_content, PrintStream... streamOuts)
	{
		if(KitScript.isForce_runitPythonWay2)
			{ KitScript.runit_python_way2(script_content, false, streamOuts); return; }

		 KitScript.runit_python_way2(script_content, false, streamOuts);
	}
	private static void runit_python_way1(String script_content, PrintStream... streamOuts)
	{
	}
	private static PrintStream melonValue[] = new PrintStream[0];
	private static int runit_python_way2(String script_content, boolean isKeepExecWhenHaveError, PrintStream... streamOuts)
	{
		script_content = script_content.replace("\"", "\\\"");
		String command;
		command = String.format(runnerCmdOutline_python, script_content);

		int status;
		if(streamOuts == null)
			streamOuts = melonValue;
		switch(streamOuts.length)
		{
			case 0:
				status = CommandRuner.runCmd0(command);
				break;
			case 1:
				status = CommandRuner.runCmd0(streamOuts[0], command);
				break;
			case 2:
			default:
				status = CommandRuner.runCmd0(streamOuts[0], streamOuts[1], command);
				break;
		}
		if( status != 0 && !isKeepExecWhenHaveError)
			System.exit(status);
		return status;
	}
	@SuppressWarnings("unused")
	private static final String z  = "echo \"%s\"";
	private static final String runnerCmdOutline_python_default = "python -c \"%s\"";
	private static String runnerCmdOutline_python = runnerCmdOutline_python_default;
	public static void setRunner_python(String runnerCmdName_new)
	{
		KitScript.isForce_runitPythonWay2 = (runnerCmdName_new!=null);
		String runnerCmdName_old = "python";
		if(!KitScript.isForce_runitPythonWay2)
		{
			runnerCmdName_new = "";
			runnerCmdName_old = "～_～";
		}
		KitScript.runnerCmdOutline_python = KitScript.runnerCmdOutline_python_default.replace(runnerCmdName_old, runnerCmdName_new);
	}
}
