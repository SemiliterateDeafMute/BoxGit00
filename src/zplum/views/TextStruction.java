package zplum.views;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;

import zplum.plus.ScString;
import zplum.tools._io_report.Report;

public class TextStruction
{
	public static class ThemeOrigin
	{
		protected static final PrintStream outStc = new Report();
		protected PrintStream out = outStc;
		public void setOut(PrintStream out)
		{
			this.out = (out==null?outStc:out);
		}

		protected String createLine(int indentationDeep)
		{
			if(indentationDeep <= 0)
				return "";
			return ScString.create("| ", indentationDeep - 1) + '|';
		}

		protected String createLineIndentation(int indentationDeep, String indentationText_affix01, String indentationText_affix02)
		{
			return ScString.create("| ", indentationDeep)
					+ indentationText_affix01 + ScString.create('-', indentationDeep) + indentationText_affix02;
		}
		protected String createLineIndentationType02(int indentationDeep_main, int indentationDeep_auxi, String indentationText_affix01, String indentationText_affix02)
		{
			return ScString.create("| ", indentationDeep_main)
					+ ScString.create(' ', indentationDeep_auxi * 2)
					+ indentationText_affix01 + ScString.create('-', indentationDeep_main + indentationDeep_auxi) + indentationText_affix02;
		}
		protected String createLineIndentationType03(int indentationDeep_main, int indentationDeep_auxi, String indentationText_affix01, String indentationText_affix02)
		{
			return ScString.create("| ", indentationDeep_main)
					+ indentationText_affix01 + ScString.create('-', indentationDeep_auxi * 2 + 3) + ScString.create('-', indentationDeep_main + indentationDeep_auxi) + indentationText_affix02;
		}

		protected static interface AffixNode
		{
			static String Leaf[] =      {"-", "-> "};
			static String BrachHead[] = {"+", "-> "};
			static String BrachTail[] = {"@", "-<>"};
		}
		public void printBr(int indentationDeep)
		{
			this.out.print(createLine(indentationDeep));
			this.out.println();
		}
		public void printNodeLeaf(int indentationDeep, String content)
		{
			this.out.print(createLineIndentation(indentationDeep, AffixNode.Leaf[0], AffixNode.Leaf[1]) + content);
			this.out.println();
		}
		public void printNodeBranch(int indentationDeep)
		{
			this.out.print(createLineIndentation(indentationDeep, AffixNode.BrachTail[0], AffixNode.BrachTail[1]));
			this.out.println();
		}
		public void printNodeBranch(int indentationDeep, String content)
		{
			this.out.print(createLineIndentation(indentationDeep, AffixNode.BrachHead[0], AffixNode.BrachHead[1]) + content);
			this.out.println();
		}
		public void printNodeLeaf_style02(int indentationDeep_main, int indentationDeep_auxi, String content)
		{
			this.out.print(createLineIndentationType02(indentationDeep_main, indentationDeep_auxi, AffixNode.Leaf[0], AffixNode.Leaf[1]) + content);
			this.out.println();
		}
		public void printNodeBranch_style02(int indentationDeep_main, int indentationDeep_auxi)
		{
			this.out.print(createLineIndentationType03(indentationDeep_main, indentationDeep_auxi, AffixNode.BrachTail[0], AffixNode.BrachTail[1]));
			this.out.println();
		}

		public void printNodesList(int indentationDeep, String title, Collection<String> contents, boolean isIgnoreWhenListEmpty)
		{
			this.printNodeBranch(indentationDeep++, title);
			if(!(isIgnoreWhenListEmpty && contents.size() == 0))
				for(String content: contents)
					this.printNodeLeaf(indentationDeep, content);
			this.printNodeBranch(--indentationDeep);
		}
		public void printNodesList_style02(int indentationDeep, String title, Collection<String> contents, boolean isIgnoreWhenListEmpty)
		{
			int indentationDeep_auxi = 0;
			this.printNodeBranch(indentationDeep++, title);
			if(!(isIgnoreWhenListEmpty && contents.size() == 0))
				for(String content: contents)
					this.printNodeLeaf_style02(indentationDeep, indentationDeep_auxi++, content);
			this.printNodeBranch_style02(--indentationDeep, --indentationDeep_auxi);
		}

		public void printNodesTree(int indentationDeep, String root, HashMap<String, ? extends Collection<String>> nodes, boolean isIgnoreWhenListEmpty)
		{
			Collection<String> rootMembers = nodes.get(root);
			if(rootMembers == null)
			{
				this.printNodeLeaf(indentationDeep, root);
			} else {
				this.printNodeBranch(indentationDeep, root);
				if(!(isIgnoreWhenListEmpty && rootMembers.size() == 0))
					for(String rootMember: rootMembers)
						this.printNodesTree(indentationDeep + 1, rootMember, nodes, isIgnoreWhenListEmpty);
				this.printNodeBranch(indentationDeep);
			}
		}

	}

}
