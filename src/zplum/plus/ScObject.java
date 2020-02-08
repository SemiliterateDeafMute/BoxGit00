package zplum.plus;

public class ScObject
{
	public static boolean equals(Object this_one, Object other)
	{
		if(this_one == null)
			if(other == null)
				return true;
		if(this_one != null)
			if(this_one.equals(other))
				return true;
		return false;
	}

	public static boolean equals(Object this_one, Object... others)
	{
		if(this_one == null)
			for(Object other: others)
				if(other == null)
					return true;
		if(this_one != null)
			for(Object other: others)
				if(other != null)
					if(other.equals(this_one))
						return true;
		return false;
	}

	public static <E> int compareTo(Comparable<E> this_one, E other)
	{
		return this_one.compareTo(other);
	}
}
