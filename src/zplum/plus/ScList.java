package zplum.plus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import zplum.tools._ancestor.SolidAble;

public class ScList
{
	@SafeVarargs
	public static <T> List<T> create(T... objs)
	{
		List<T> list = new ArrayList<T>();
		if(objs != null)
			for(T obj: objs)
				list.add(obj);
		return list;
	}
	public static List<Integer> create(int[] objs)
	{
		List<Integer> list = new ArrayList<Integer>();
		if(objs != null)
			for(int obj: objs)
				list.add(obj);
		return list;
	}

	public static boolean isMemberUniqueEz(List<?> list)
	{
		if(list.size() == new HashSet<>(list).size())
			return true;
		return false;
	}
	public static <T> boolean isMemberUnique(List<T> list, Comparator<T> comparator)
	{
		list.sort(comparator);
		for(int i=1, j=list.size(); i<j; i++)
			if(comparator.compare(list.get(i-1), list.get(i)) == 0)
				return false;
		return true;
	}

	public static <E> Comparator<E> createComparator(List<E> orderOfReference)
	{
		return new Comparator<E>() {
			protected int orderOfUncontain = orderOfReference.size();
			protected List<E> orderOfReferenceClone = new ArrayList<E>(orderOfReference);
			public int compare(E o1, E o2)
			{
				int tmp;
				return + ((tmp=orderOfReferenceClone.indexOf(o1))>=0?tmp:orderOfUncontain)
						- ((tmp=orderOfReferenceClone.indexOf(o2))>=0?tmp:orderOfUncontain);
			}
		};
	}
	public static <E> Comparator<E> createComparatorR(List<E> orderOfReference)
	{
		return new Comparator<E>() {
			protected int orderOfUncontain = orderOfReference.size();
			protected List<E> orderOfReferenceClone = new ArrayList<E>(orderOfReference);
			public int compare(E o1, E o2)
			{
				int tmp;
				return - ((tmp=orderOfReferenceClone.indexOf(o1))>=0?tmp:orderOfUncontain)
						+ ((tmp=orderOfReferenceClone.indexOf(o2))>=0?tmp:orderOfUncontain);
			}
		};
	}

	private static abstract class SolidAbleSub extends SolidAble
	{
		protected void setList_checkAllow()
		{
			this.checkAllow(false, new Exception("This Object have already can't set list."));
		}
	}
	public static abstract class vsMember<T> extends SolidAbleSub
	{
		public abstract operation vsScript() throws Exception;
		public vsMember() {}
		public vsMember(List<T> slow, List<T> fast)
		{
			this.slow = slow;
			this.fast = fast;
		}

		protected List<T> slow=null, fast=null;
		protected int slowPointer=0, fastPointer=0;
		public synchronized void setListSlow(List<T> slow)
		{
			this.setList_checkAllow();
			this.slow = slow;
		}
		public synchronized void setListFast(List<T> fast)
		{
			this.setList_checkAllow();
			this.fast = fast;
		}
		public synchronized void setListSlowPointer(int slowPointer)
		{
			this.slowPointer = slowPointer;
		}
		public synchronized void setListFastPointer(int fastPointer)
		{
			this.fastPointer = fastPointer;
		}

		protected static enum operation
		{
			break_,
			breakSlow,
			breakFast,
			continue_;
		}
		public synchronized void execute() throws Exception
		{
			this.solidify();
			markSlow:
			for(slowPointer=0; slowPointer < slow.size(); slowPointer++)
			{
				markFast:
				for(fastPointer=0; slowPointer < slow.size() && fastPointer < fast.size() && slowPointer >= 0 && fastPointer >= 0; fastPointer++)
				{
					switch(vsScript())
					{
						case break_:
						case breakSlow:
							break markSlow;
						case breakFast:
							break markFast;
						case continue_:
							break;
					}
				}
			}
		}
	}
	@Deprecated
	public static abstract class vsMember_orig01<T>
	{
		public abstract int vsScript() throws Exception;

		public static final int breakSolw =	1;
		public static final int breakFast =	2;
		public static final int break_ = 	3;
		public static final int continue_ = 4;

		protected List<T> slow, fast;
		protected int slowPointer, fastPointer;
		public synchronized void execute(List<T> slow, List<T> fast) throws Exception
		{
			markSlow:
			for(slowPointer=0; slowPointer < slow.size(); slowPointer++)
			{
				markFast:
				for(fastPointer=0; slowPointer < slow.size() && fastPointer < fast.size() && slowPointer >= 0 && fastPointer >= 0; fastPointer++)
				{
					switch(vsScript())
					{
						case breakSolw:
							break markSlow;
						case break_:
						case breakFast:
							break markFast;
						case continue_:
							break;
					}
				}
			}
		}
	}
	public static abstract class tsMember<T> extends SolidAbleSub
	{
		public abstract operation tsScript() throws Exception;
		public tsMember() {}
		public tsMember(List<T> list)
		{
			this.list = list;
		}

		protected List<T> list=null;
		protected int pointer=0;
		public synchronized void setList(List<T> list)
		{
			this.setList_checkAllow();
			this.list = list;
		}
		public synchronized void setListPointer(int pointer)
		{
			this.pointer = pointer;
		}

		protected static enum operation
		{
			break_,
			continue_;
		}
		public synchronized void execute() throws Exception
		{
			this.solidify();
			mark:
			for(pointer=0; pointer < list.size(); pointer++)
			{
				switch(tsScript())
				{
					case break_:
						break mark;
					case continue_:
						break;
				}
			}
		}
	}
}
