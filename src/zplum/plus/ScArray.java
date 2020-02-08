package zplum.plus;

import zplum.kit.Norm;

public class ScArray
{
	@SafeVarargs

	public static <T> T[] cerate(T... objs)
	{
		return objs;
	}
	public static int[] cerateInts(int... objs)
	{
		return objs;
	}

	public static <T> boolean contain(T obj, T[] objs)
	{
		for(T obj_tmp: objs)
			if(obj.equals(obj_tmp))
				return true;
		return false;
	}
	public static boolean contain(char obj, char[] objs)
	{
		for(char obj_tmp: objs)
			if(obj_tmp == obj)
				return true;
		return false;
	}

	public static <T> T last_one(T[] objs)
	{
		return objs[objs.length-1];
	}

	public static class Str
	{
		public static String[] sub(String[] stringsOld, int head)
		{
			return subarray(stringsOld, head, stringsOld.length);
		}
		public static String[] subarray(String[] stringsOld, int head, int tail)
		{
			if(head<0 || tail<0 || tail-head<0 || head>=stringsOld.length)
				head = tail = 0;

			String[] stringsNew = new String[tail - head];
			for(int index=0; head<tail; index++, head++)
				stringsNew[index] = stringsOld[head];
			return stringsNew;
		}
	}

	public static class Int
	{
		public static int[] union(Object... objs)
		{
			int[] watermelon;
			int size;

			size = 0;
			for(Object obj: objs)
			{
				if(obj instanceof Integer)
					size += 1;
				if(obj instanceof int[])
					size += ((int[])obj).length;
				if(obj instanceof Integer[])
					size += ((Integer[])obj).length;
			}

			watermelon = new int[size];
			size = 0;
			for(Object obj: objs)
			{
				if(obj instanceof Integer)
					watermelon[size++] = (int)obj;
				if(obj instanceof int[])
					for(int temp: (int[])obj)
						watermelon[size++] = temp;
				if(obj instanceof Integer[])
					for(int temp: (Integer[])obj)
						watermelon[size++] = temp;
			}

			return watermelon;
		}

		public static int[] sortAsSet(int... values)
		{
			RfListArraySet<Integer> list = new RfListArraySet<Integer>();
			for(int value: values)
				list.add(value);
			list.sort(Norm.JdkInstant.sortInt);

			int index=0;
			values = new int[list.size()];
			for(int value: list)
				values[index++] = value;
			return values;
		}
	}

}
