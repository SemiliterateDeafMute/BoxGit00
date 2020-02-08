package zplum.plus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import zplum.kit.Project;
import zplum.tools._fc_runcommand.CommandRuner;

@SuppressWarnings("serial")
public class RfProperties extends Properties
{
	public static String configPathName_default = "config";
	public static Class<?> classJarLocator = RfProperties.class;
	public static void locateJarByClass(Class<?> classJarLocator_)
	{
		classJarLocator = classJarLocator_;
	}

	private final String configPathName;
	private final boolean configPathName_isAbsolute;
	public RfProperties()
	{
		configPathName = configPathName_default;
		configPathName_isAbsolute = false;
	}
	public RfProperties(String configPathName_)
	{
		configPathName = configPathName_;
		configPathName_isAbsolute = new File(configPathName_).isAbsolute();
	}

	public void load() throws FileNotFoundException, IOException
	{
		if(configPathName_isAbsolute)
		{
			this.load(new FileInputStream(configPathName));
		} else {

			InputStream streamIn;

			streamIn = classJarLocator.getClassLoader().getResourceAsStream(configPathName);
			if(streamIn != null)
			{
				this.load(streamIn);
				streamIn.close();
			}

			streamIn = null;
			File objFileConfigJarOut = new File(Project.getRootDir(), configPathName);
			if(objFileConfigJarOut.exists())
				streamIn = new FileInputStream(objFileConfigJarOut);
			if(streamIn != null)
			{
				this.load(streamIn);
				streamIn.close();
			}
		}
	}
	public void save(String configPathName_) throws FileNotFoundException, IOException
	{
		this.store(new FileOutputStream(configPathName_, false), "");
	}
	public void save() throws FileNotFoundException, IOException
	{
		this.save(configPathName);
	}
	public void saveToXML(String configPathName_) throws FileNotFoundException, IOException
	{
		this.storeToXML(new FileOutputStream(configPathName_, false), "");
	}
	public void saveToXML() throws FileNotFoundException, IOException
	{
		this.saveToXML(configPathName);
	}

	public boolean saveInJar() throws FileNotFoundException, IOException
	{
		return this.saveInJar(configPathName);
	}

	public boolean saveInJar(String configPathName_) throws FileNotFoundException, IOException
	{
		int intCmdReturn = 0;
		boolean objJarFile_isInvalid = Project.getJarPath(classJarLocator)==null;
		if( !(configPathName_isAbsolute||objJarFile_isInvalid) )
		{
			File objJarFile = new File(Project.getJarPath(classJarLocator));
			File objJarFileParent = new File(objJarFile.getParent());
			File objConfigFile_tmp = new File(objJarFileParent, configPathName_);
			this.store(new FileOutputStream(objConfigFile_tmp, false), "");
			intCmdReturn = CommandRuner.runCmd_withoutShell(null, objJarFileParent, "jar uf " + objJarFile.getName() + " " + configPathName_);
			objConfigFile_tmp.delete();
			if(intCmdReturn == 0)
				return true;
		}

		configPathName_ = new File(configPathName_).getAbsolutePath();
		if(intCmdReturn >= 1)
			System.err.println("PropertiesRfWarning: ConfigFile maybe do not save in jar, property save to " + configPathName_);
		if(configPathName_isAbsolute)
			System.err.println("PropertiesRfWarning: ConfigPathName is absolute, property save to " + configPathName_);
		if(objJarFile_isInvalid)
			System.err.println("PropertiesRfWarning: JarPathName is invalid, property save to " + configPathName_);
		this.save();
		return false;
	}
	@Deprecated
	public void saveInJar_old(String configPathName_) throws FileNotFoundException, IOException
	{
		boolean objJarFile_isInvalid = Project.getJarPath(classJarLocator)==null;
		if(configPathName_isAbsolute || objJarFile_isInvalid)
		{
			if(configPathName_isAbsolute)
				System.err.println("PropertiesRfWarning: configPathName is absolute, property save to " + configPathName_);
			if(objJarFile_isInvalid)
				System.err.println("PropertiesRfWarning: JarPathName is invalid, property save to " + new File(configPathName_).getAbsolutePath());
			this.save();
		}

		File objJarFile = new File(Project.getJarPath(classJarLocator));
		File objJarFileParent = new File(objJarFile.getParent());
		File objConfigFile_tmp = new File(objJarFileParent, configPathName_);
		this.store(new FileOutputStream(objConfigFile_tmp, false), "");
		CommandRuner.runCmd_withoutShell(null, objJarFileParent, "jar uf " + objJarFile.getName() + " " + configPathName_);
		objConfigFile_tmp.delete();
	}

	public InputStream getConfigInJar_streamIn()
	{
		return classJarLocator.getClassLoader().getResourceAsStream(configPathName);
	}
}
