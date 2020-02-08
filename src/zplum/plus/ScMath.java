package zplum.plus;

public class ScMath
{
	public static class Bin
	{
		public static int bitSet(int base, int mark)
		{
			return base | mark;
		}

		public static int bitRemove(int base, int mark)
		{
			return base - (base & mark);
		}

		public static int createBitMark(int... digitsOfBit)
		{
			int mark = 0;
			for(int digitOfBit: digitsOfBit)
				mark = mark | 1 << digitOfBit;
			return mark;
		}

		public static boolean isBitContain(int base, int mark)
		{
			return isBitContainOneOf(base, mark);
		}
		public static boolean isBitContainOneOf(int base, int mark)
		{
			return (base & mark) > 0;
		}

		public static boolean isBitContainAllOf(int base, int mark)
		{
			return (base & mark) == mark;
		}

	}

	public static int max(int... values)
	{
		int maxTemp = Integer.MIN_VALUE;
		for(int value: values)
			if(value > maxTemp)
				maxTemp = value;
		return maxTemp;
	}
	public static int min(int... values)
	{
		int minTemp = Integer.MAX_VALUE;
		for(int value: values)
			if(value < minTemp)
				minTemp = value;
		return minTemp;
	}

	public static Long max(Long... values)
	{
		int index = 0;
		Long maxTemp = null;
		for(;index<values.length && maxTemp==null;)
			if(values[index++] != null)
				maxTemp = values[index];
		for(;index<values.length;)
			if(values[index++] >  maxTemp)
				maxTemp = values[index];
		return maxTemp;
	}
	public static Long min(Long... values)
	{
		int index = 0;
		Long minTemp = null;
		for(;index<values.length && minTemp==null;)
			if(values[index++] != null)
				minTemp = values[index];
		for(;index<values.length;)
			if(values[index++] <  minTemp)
				minTemp = values[index];
		return minTemp;
	}

}
