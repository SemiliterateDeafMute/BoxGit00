package zplum.plus;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import zplum.kit.Norm;
import zplum.kit.Project;

public class ScFile
{
	public static void deleteDir(File file) throws IOException
	{
		if(file.isDirectory())
		{
			for(File file_member: file.listFiles())
				ScFile.deleteDir(file_member);
		}
		file.delete();
	}
	public static File createTempFile(String prefix, String suffix) throws IOException
	{
		return File.createTempFile(prefix, suffix, new File(Project.getTmpPath()));
	}

	public static void main(String[] args) throws URISyntaxException
	{
	}
	public static List<File> getDocumentMembers(File document)
	{
		return ScFile.getDocumentMembers(document, null);
	}
	public static List<File> getDocumentMembers(File document, FileFilter filter)
	{
		List<File> list = ScFile.getDocumentMembers(document, filter);
		list.remove(document);
		return list;
	}
	public static List<File> getDocumentSelfAndMembers(File document, FileFilter filter)
	{
		class MethodCore
		{
			void getDocumentMembers(List<File> list, File document)
			{
				list.add(document);
				if(document.isDirectory())
					for(File member: document.listFiles())
						this.getDocumentMembers(list, member);
			}
			void getDocumentMembersWithFilter(List<File> list, File document)
			{
				if(filter.accept(document))
					list.add(document);
				if(document.isDirectory())
					for(File member: document.listFiles())
						this.getDocumentMembersWithFilter(list, member);
			}
		}

		List<File> list = new ArrayList<File>();
		if(filter == null)
			new MethodCore().getDocumentMembers(list, document);
		else
			new MethodCore().getDocumentMembersWithFilter(list, document);

		list.sort(Norm.JdkInstant.sortFile);
		return list;
	}
	public static List<File> getDocumentSelfAndMembers_Verification(File document, FileFilter filter, FileFilter filterToDirectory)
	{
		class MethodCore
		{
			FileFilter filter = null, filterToDirectory = null;
			MethodCore() {}
			MethodCore(FileFilter filter, FileFilter filterToDirectory)
			{
				this.filter = (filter!=null)?filter:Norm.JdkInstant.filterFileAllTrue;
				this.filterToDirectory = (filterToDirectory!=null)?filterToDirectory:Norm.JdkInstant.filterFileAllTrue;
			}
			void getDocumentMembers(List<File> list, File document)
			{
				list.add(document);
				if(document.isDirectory())
					for(File member: document.listFiles())
						this.getDocumentMembers(list, member);
			}
			void getDocumentMembersWithFilter(List<File> list, File document)
			{
				if(this.filter.accept(document))
					list.add(document);
				if(document.isDirectory() && this.filterToDirectory.accept(document))
					for(File member: document.listFiles())
						this.getDocumentMembersWithFilter(list, member);
			}
		}

		List<File> list = new ArrayList<File>();
		if(filter == null && filterToDirectory == null)
			new MethodCore().getDocumentMembers(list, document);
		else
			new MethodCore(filter, filterToDirectory).getDocumentMembersWithFilter(list, document);

		list.sort(Norm.JdkInstant.sortFile);
		return list;
	}

	public static Long getInode(File file) throws IOException
	{
		Project.ensureSystemIsSystemMacLinux();
		return (Long) Files.getAttribute(file.toPath(), "unix:ino");
	}
}

