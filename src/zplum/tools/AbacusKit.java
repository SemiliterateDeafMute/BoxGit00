package zplum.tools;

import zplum.tools._fc_timeobject.Watch;

public class AbacusKit
{
	public static class Count
	{
		public String title;
		public int number=0;
		public void increment()
		{
			this.number += 1;
		}
	}

	@Deprecated
	public static class Mark
	{
		public int intv = 0;
		public Mark()
		{
			this.intv = 0;
		}
		public Mark(int i)
		{
			this.intv = i;
		}
		public String toString()
		{
			return String.valueOf(this.intv);
		}
	}

	public static class Ticker extends Watch.kit_base.Ticker
	{
		public Ticker(long duration_millis)
		{
			super(duration_millis);
		}
	}

	public static class Chromo extends Watch.kit_base.Chromo {}

	public static class Cuckoo extends Watch.kit_base.Cuckoo {}
}
