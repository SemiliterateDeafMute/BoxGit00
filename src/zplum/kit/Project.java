package zplum.kit;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Project
{
	public static void testEcho()
	{
		System.out.println("can you see me ? Source .");
	}

	public static final String  configCharset = "utf-8";
	public static final String  configDirLog = Project.getRootDir() + File.separator + "log";
	public static final String  configDirTmp = Project.getRootDir() + File.separator + "tmp";
	public static final int     configTabWidth = 4;
	public static final int     configHalfWidthCharWidth = 1;
	public static final float   configFullWidthCharWidth = (float)5/3;
	public static final int     configTimeZoonOffset = - Integer.valueOf(new SimpleDateFormat("HH").format(new Date(0))) * 60 * 60 * Norm.intK1;
	private static final class define
	{
		private static final String systemNameMac   = "mac";
		private static final String systemNameWin   = "win";
		private static final String systemNameLinux = "linux";
		private static final Exception ensureButNot_SystemIsWin = new Exception("The system must but is not Windows");
		private static final Exception ensureButNot_SystemMacLinux = new Exception("The system must but is not Linux");
	}

	public static void exit()
	{
		Project.exit(null, 1);
	}
	public static void exit(Exception e)
	{
		Project.exit(e, 1);
	}
	public static void exit(Exception e, int status)
	{
		if(e != null)
			e.printStackTrace();
		System.exit(status);
	}

	private static String SystemName = null;
	public static String getSystemName()
	{
		if(SystemName == null)
		{
			SystemName = System.getProperty("os.name").toLowerCase();

			String namesSystem[] = {
					define.systemNameMac,
					define.systemNameWin,
					define.systemNameLinux,
					};
			for(String nameSystem: namesSystem)
			{
				if(SystemName.indexOf(nameSystem) >= 0)
				{
					SystemName = nameSystem;
					break;
				}
			}
		}
		return SystemName;
	}
	public static boolean isSystemWin()
	{
		if(SystemName == null)
			getSystemName();
		return SystemName.equals(define.systemNameWin);
	}
	public static boolean isSystemMacLinux()
	{
		if(SystemName == null)
			getSystemName();
		return SystemName.equals(define.systemNameMac) || SystemName.equals(define.systemNameLinux);
	}
	public static void ensureSystemIsWin()
	{
		if(!Project.isSystemWin())
			Project.exit(define.ensureButNot_SystemIsWin);
	}
	public static void ensureSystemIsSystemMacLinux()
	{
		if(!Project.isSystemWin())
			Project.exit(define.ensureButNot_SystemMacLinux);
	}

	private static String WorkDir = null;
	public static String getRootDir()
	{
		if(WorkDir == null)
			WorkDir = System.getProperty("user.dir");
		return WorkDir;
	}
	private static String JarPath = "";

	public static String getJarPath(Class<?> class_)
	{
		if(JarPath.equals(""))
		{
			URL JarUrl = class_.getProtectionDomain().getCodeSource().getLocation();
			if(JarUrl.getProtocol().equals("rsrc") || !new File(JarUrl.getPath()).isFile())
			{
				JarPath = null;
			} else {
				JarPath = JarUrl.getPath();
			}
		}
		return JarPath;
	}
	public static String getJarPath()
	{
		return getJarPath(Project.class);

	}
	public static String getLogPath()
	{
		return configDirLog;
	}
	public static String getTmpPath()
	{
		return configDirTmp;
	}
}

class Project_DropCode
{
	public static class ZIP20191032
	{
	}
}

