package zplum.tools._ancestor;

import java.util.ArrayList;
import java.util.Comparator;

import zplum.kit.Norm;

public interface UtilPdkTime
{
	public static enum type
	{
		value, point;
		public boolean isValue()
		{
			return this == value;
		}
		public boolean isPoint()
		{
			return this == point;
		}
	}
	public static enum valueUnit
	{
		days   (Norm.intK1 * 60 * 60 * 24),
		hours  (Norm.intK1 * 60 * 60),
		minutes(Norm.intK1 * 60),
		seconds(Norm.intK1),
		millies(1);
		public final int weight;
		private valueUnit(int weight)
		{
			this.weight = weight;
		}

		public static final valueUnit level[];
		static {
			ArrayList<valueUnit> list = new ArrayList<valueUnit>();
			for(valueUnit tmp: valueUnit.values())
				list.add(tmp);
			list.sort(new Comparator<valueUnit>()
			{
				public int compare(valueUnit o1, valueUnit o2)
				{
					return o1.weight - o2.weight;
				}
			});
			level = list.toArray(new valueUnit[list.size()]);
		}
	}
	public static enum pointUnitWeek
	{
		mon01(1, "mon", "monday"),
		tue02(2, "tue", "tuesday"),
		wed03(3, "wed", "wednesday"),
		thu04(4, "thu", "thursday"),
		fri05(5, "fri", "friday"),
		sat06(6, "sat", "saturday"),
		sun07(7, "sun", "sunday");
		public int munber;
		public String word;
		public String word_abbrv;
		private pointUnitWeek()
		{}
		private pointUnitWeek(int munber, String word_abbrv, String word)
		{
			this.munber = munber;
			this.word = word;
			this.word_abbrv = word_abbrv;
		}

		public static pointUnitWeek valueOf0(int munber)
		{
			for(pointUnitWeek tmp: pointUnitWeek.values())
				if(tmp.munber == munber)
					return tmp;
			return null;
		}
		public static pointUnitWeek valueOf0(String word)
		{
			for(pointUnitWeek tmp: pointUnitWeek.values())
				if(tmp.word.equals(word) || tmp.word_abbrv.equals(word))
					return tmp;
			return null;
		}
	}
	public static enum pointUnitMonth
	{
		jan01( 1, "jan", "january" ),
		feb02( 2, "feb", "february"),
		mar03( 3, "mar", "march"),
		apr04( 4, "apr", "april"),
		may05( 5, "may", "may"),
		jun06( 6, "jun", "june"),
		jul07( 7, "jul", "july"),
		aug08( 8, "aug", "august"),
		sep09( 9, "sep", "september"),
		oct10(10, "oct", "october"),
		nov11(11, "nov", "november"),
		dev12(12, "dev", "december");
		public final int munber;
		public final String word;
		public final String word_abbrv;
		private pointUnitMonth(int munber, String word_abbrv, String word)
		{
			this.munber = munber;
			this.word = word;
			this.word_abbrv = word_abbrv;
		}

		public static pointUnitMonth valueOf0(int munber)
		{
			for(pointUnitMonth tmp: pointUnitMonth.values())
				if(tmp.munber == munber)
					return tmp;
			return null;
		}
		public static pointUnitMonth valueOf0(String word)
		{
			for(pointUnitMonth tmp: pointUnitMonth.values())
				if(tmp.word.equals(word) || tmp.word_abbrv.equals(word))
					return tmp;
			return null;
		}
	}

	public static enum tense
	{
		now,
		nowafter,
		after;
		public boolean isNow()
		{
			return this == now;
		}
		public boolean isAfter()
		{
			return this == after;
		}
		public boolean isNowAndAfter()
		{
			return this == nowafter;
		}
	}
}
