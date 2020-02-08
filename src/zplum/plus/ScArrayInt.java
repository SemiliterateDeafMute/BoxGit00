package zplum.plus;

import zplum.kit.Norm;

@Deprecated
public class ScArrayInt
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
