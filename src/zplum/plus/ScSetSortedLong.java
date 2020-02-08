package zplum.plus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import zplum.kit.test0;

import java.util.TreeSet;

public class ScSetSortedLong extends AbstractSet<Long>
{
	protected final RfTreeSet<Long> elements = new RfTreeSet<Long>();
	protected final HashMap<Long, Integer> elements_count = new HashMap<Long, Integer>();

	public Iterator<Long> iterator()
	{
		return this.elements.iterator();
	}
	public int size()
	{
		return this.sizeIncludeElementsCount();
	}
	public int szieOnlyElementsGroup()
	{
		return this.elements.size();
	}
	public int sizeIncludeElementsCount()
	{
		int size = 0;
		for(Integer size_add: elements_count.values())
			size += size_add;
		return this.elements.size() + size;
	}

	public boolean isEmpty()
	{
		return this.elements.isEmpty();
	}
	public boolean contains(long e)
	{
		return this.elements.contains(e);
	}
	public synchronized void add(long e)
	{
		int count = 1;
		if(this.elements.contains(e))
			count += this.elements_count.getOrDefault(e, 1);
		this.elements.add(e);
		if(count > 1)
			this.elements_count.put(e, count);
	}
	public synchronized void add(long e, int count)
	{
		if(count <= 0)
			return;
		if(this.elements.contains(e))
			count += this.elements_count.getOrDefault(e, 1);
		this.elements.add(e);
		if(count > 1)
			this.elements_count.put(e, count);
	}
	public synchronized void addAll(long... es)
	{
		for(long e: es)
			this.add(e);
	}
	public synchronized boolean remove(long e)
	{
		if(!elements.remove(e))
			return false;
		this.elements_count.remove(e);
		return true;
	}
	public synchronized void clear()
	{
		this.elements.clear();
		this.elements_count.clear();
	}
	public synchronized void modify(long orig, long dest)
	{
		if(!this.contains(orig))
			return;
		int count = this.count(orig) + this.count(dest);
		this.remove(orig);
		this.elements_count.put(dest, count);
	}

	public synchronized Map.Entry<Long, Integer> pollMin()
	{
		return this.pollSelf(this.elements.first());
	}
	public synchronized Map.Entry<Long, Integer> pollMax()
	{
		return this.pollSelf(this.elements.last());
	}
	public synchronized Map.Entry<Long, Integer> pollSelf(Long e)
	{
		if(e==null)
			return null;
		Integer count = this.elements_count.get(e);
		if(count == null)
			count = 1;
		this.elements.remove(e);
		this.elements_count.remove(e);
		return new AbstractMap.SimpleEntry<Long, Integer>(e, count);
	}
	public Long peekMin()
	{
		return this.elements.first();
	}
	public Long peekMax()
	{
		return this.elements.last();
	}
	public Long peekSelf(Long e)
	{
		return this.elements.contains(e)?e:null;
	}
	public Long peekLess(Long e, boolean inclusive)
	{
		return inclusive?this.elements.floor(e):this.elements.lower(e);
	}
	public Long peekMore(Long e, boolean inclusive)
	{
		return inclusive?this.elements.ceiling(e):this.elements.higher(e);
	}
	public synchronized int count(long e)
	{
		if(!this.elements.contains(e))
			return 0;
		return this.elements_count.getOrDefault(e, 1);
	}
	public synchronized int count(boolean isMin_orMax)
	{
		Long e = (isMin_orMax)?this.elements.first():this.elements.last();
		if(e == null)
			return 0;
		return elements_count.getOrDefault(e, 1);
	}

	public static class WithFactroy extends ScSetSortedLong
	{
		public static class FactroyCtrl
		{
			protected final RfTreeSet<Long> elements;
			protected final HashMap<Long, Integer> elements_count;
			protected boolean isClose = false;
			protected boolean isWorking = false;
			protected long    element_made = 0;
			protected long    element_made_increment = 0;
			protected Long    element_made_break_min = null;
			protected Long    element_made_break_max = null;
			protected int     element_made_times = 0;
			protected Integer element_made_times_break = null;
			protected FactroyCtrl(RfTreeSet<Long> elements, HashMap<Long, Integer> elements_count)
			{
				this.elements = elements;
				this.elements_count = elements_count;
			}

			public synchronized boolean check()
			{
				if(this.isClose)
					return this.isWorking = false;
				if(this.element_made_increment == 0)
					return this.isWorking = false;
				if(this.element_made_times_break != null && this.element_made_times_break < this.element_made_times)
					return this.isWorking = false;
				if(this.element_made_break_max != null   && this.element_made_break_max   < this.element_made)
					return this.isWorking = false;
				if(this.element_made_break_min != null   && this.element_made_break_min   > this.element_made)
					return this.isWorking = false;
				return this.isWorking = true;
			}

			public void close()
			{
				this.isClose = true;
				this.check();
			}
			public synchronized void setNull(boolean isSetNull_element_made_times_break, boolean isSetNull_element_made_break_min, boolean isSetNull_element_made_break_max)
			{
				if(isSetNull_element_made_times_break)
					this.element_made_times_break = null;
				if(isSetNull_element_made_break_min)
					this.element_made_break_min = null;
				if(isSetNull_element_made_break_max)
					this.element_made_break_max = null;

			}
			public synchronized void setValue(Long element_made_initial, Long element_made_increment, Integer element_made_times_break, Long element_made_break_min, Long element_made_break_max)
			{
				if(element_made_initial != null)
					this.element_made = element_made_initial;
				if(element_made_increment != null)
					this.element_made_increment = element_made_increment;
				if(element_made_break_min != null)
					this.element_made_break_min = element_made_break_min;
				if(element_made_break_max != null)
					this.element_made_break_max = element_made_break_max;
				if(element_made_times_break != null && element_made_times_break > 0)
					this.element_made_times_break = element_made_times_break;
				this.check();
			}

			public boolean isWorking()
			{
				return isWorking;
			}
			public long shadowReMade0ElementNow()
			{
				return element_made;
			}
			public synchronized Long[] shadowReMade0Increment1BreakMin2BreakMax()
			{
				Long shadow[] = {
						this.element_made_increment,
						this.element_made_break_min,
						this.element_made_break_max
						};
				return shadow;
			}
			public synchronized Integer[] shadowReMade0TimesNow1TimesBreak()
			{
				Integer shadow[] = {
						this.element_made_times,
						this.element_made_times_break
						};
				return shadow;
			}

			public synchronized Long peek_element_made()
			{
				return (this.isWorking)?this.element_made:null;
			}
			public synchronized Long peekNext_element_made()
			{
				if(!this.isWorking)
					return null;
				if(this.element_made_increment == 0)
					return null;
				if(this.element_made_times_break != null && this.element_made_times_break < this.element_made_times + 1)
					return null;
				Long element_made_tmp = this.element_made + this.element_made_increment;
				if(this.element_made_break_max != null   && this.element_made_break_max   < element_made_tmp)
					return null;
				if(this.element_made_break_min != null   && this.element_made_break_min   > element_made_tmp)
					return null;
				return element_made_tmp;
			}
			public synchronized Long peekPredict_element_made(long e, boolean inclusive, boolean isLess_orMore)
			{
				if(!this.isWorking)
					return null;
				if(this.element_made_increment == 0)
					return null;

				return null;
			}
			public synchronized Long push_element_made()
			{
				if(!this.check())
					return null;
				this.element_made_times += 1;
				this.element_made += this.element_made_increment;
				return (this.check())?this.element_made:null;
			}
			public synchronized boolean checkElementMadeNow_equal(long e)
			{
				if(this.isWorking && this.element_made == e)
					return true;
				return false;
			}
			public synchronized Boolean checkElementMadeNow_isMin()
			{
				if(!this.isWorking)
					return false;
				if(this.elements.isEmpty())
					return true;
				else if(this.element_made == this.elements.first())
					return null;
				return this.element_made < this.elements.first();
			}
			public synchronized Boolean checkElementMadeNow_isMax()
			{
				if(!this.isWorking)
					return false;
				if(this.elements.isEmpty())
					return true;
				else if(this.element_made == this.elements.last())
					return null;
				return this.element_made < this.elements.last();
			}
			public synchronized boolean checkElementMadeNow_isOnly()
			{
				if(!this.isWorking)
					return false;
				return this.elements.contains(this.element_made);
			}
			public int checkElementMadeTimes_elapsed()
			{
				return this.element_made_times;
			}
			public synchronized int checkElementMadeTimes_remaining()
			{
				if(!this.isWorking)
					return 0;
				if(this.element_made_times_break == null)
					return -1;
				int times = this.element_made_times_break - this.element_made_times;
				if(times < 0)
					return 0;
				return times;
			}
		}

		public final FactroyCtrl ctrl;
		public WithFactroy()
		{
			this(true);
		}
		protected WithFactroy(boolean isCtrlInstance)
		{
			this.ctrl = (!isCtrlInstance)?null:new FactroyCtrl(this.elements, this.elements_count);
		}
		protected WithFactroy(long element_made_initial, long element_made_increment, Integer element_made_times_break, Long element_made_break_min, Long element_made_break_max)
		{
			this.ctrl = new FactroyCtrl(this.elements, this.elements_count);
			this.ctrl.setValue(element_made_initial, element_made_increment, element_made_times_break, element_made_break_min, element_made_break_max);
			this.ctrl.setNull(element_made_times_break==null, element_made_break_min==null, element_made_break_max==null);
		}
		public FactroyCtrl ctrl()
		{
			return this.ctrl;
		}

		public Iterator<Long> iterator()
		{
			return this.elements.iterator();
		}
		public int szieOnlyElementsGroup()
		{
			return this.elements.size() + (this.elements.contains(this.ctrl().peek_element_made())?1:0);
		}
		public int sizeIncludeElementsCount()
		{
			return super.sizeIncludeElementsCount() + (this.ctrl().isWorking?1:0);
		}

		public boolean isEmpty()
		{
			if(this.ctrl().isWorking)
				return false;
			return super.isEmpty();
		}
		public boolean contains(long e)
		{
			if(this.ctrl().checkElementMadeNow_equal(e))
				return true;
			return super.contains(e);
		}
		public boolean isEmptyExcludeMade()
		{
			return super.isEmpty();
		}

		public synchronized boolean remove(long e)
		{
			if(this.ctrl().checkElementMadeNow_equal(e))
				this.ctrl().push_element_made();
			return super.remove(e);
		}
		public synchronized void clear()
		{
			this.elements.clear();
			this.elements_count.clear();
			this.ctrl().isClose = false;
			this.ctrl().isWorking = false;
		}

		public synchronized Entry<Long, Integer> pollMin()
		{
			return this.pollSelf(this.peekMin());
		}
		public synchronized Entry<Long, Integer> pollMax()
		{
			return this.pollSelf(this.peekMax());
		}
		public synchronized Map.Entry<Long, Integer> pollSelf(Long e)
		{
			if(e == null)
				return null;
			Integer count = this.elements_count.get(e);
			if(count == null)
				count = this.elements.contains(e)?1:0;
			if(this.ctrl().checkElementMadeNow_equal(e))
				count += 1;
			this.elements.remove(e);
			this.elements_count.remove(e);
			this.ctrl().push_element_made();
			return new AbstractMap.SimpleEntry<Long, Integer>(e, count);
		}
		public synchronized Long peekMin()
		{
			if(this.ctrl().checkElementMadeNow_isMin() == true)
				return this.ctrl().element_made;
			return super.peekMin();
		}
		public synchronized Long peekMax()
		{
			if(this.ctrl().checkElementMadeNow_isMax() == true)
				return this.ctrl().element_made;
			return super.peekMin();
		}
		public synchronized Long peekSelf(Long e)
		{
			return (super.contains(e)||this.ctrl().checkElementMadeNow_equal(e)) ? e : null;
		}
		public synchronized Long peekLess(Long e, boolean inclusive)
		{
			return this.peekLessMore(e, inclusive, true,  false);
		}
		public synchronized Long peekMore(Long e, boolean inclusive)
		{
			return this.peekLessMore(e, inclusive, false, false);
		}
		public synchronized Long peekLessMore(Long e, boolean inclusive, boolean isLess_orMore, boolean isIncludeNextElementMade)
		{
			if(!this.ctrl().isWorking)
				return (isLess_orMore) ? super.peekLess(e, inclusive) : super.peekMore(e, inclusive);
			if(inclusive && this.peekSelf(e)!=null)
				return e;
			isIncludeNextElementMade = false;
			return ScMath.max(
					this.ctrl().peek_element_made(),

					(isIncludeNextElementMade) ? this.ctrl().peekNext_element_made() : null,
					(isLess_orMore)            ? super.peekLess(e, inclusive)      : super.peekMore(e, inclusive)
					);
		}
		public synchronized int count(long e)
		{
			return super.count(e) + (this.ctrl().checkElementMadeNow_equal(e)?1:0);
		}
		public synchronized int count(boolean isMin_orMax)
		{
			Boolean state = (isMin_orMax)?this.ctrl().checkElementMadeNow_isMin():this.ctrl().checkElementMadeNow_isMax();
			if(state == true)
				return 1;
			Long e = (isMin_orMax)?this.elements.first():this.elements.last();
			if(e == null)
				return 0;
			if(state == false)
				return elements_count.getOrDefault(e, 1);
			return elements_count.getOrDefault(e, 1) + 1;
		}

	}
	public static class WithFactroyExtd01 extends WithFactroy
	{
		public static class FactroyCtrl extends WithFactroy.FactroyCtrl
		{
			protected final boolean increment_isPositve_orNegative;
			protected FactroyCtrl(RfTreeSet<Long> elements, HashMap<Long, Integer> elements_count, boolean increment_isPositve_orNegative)
			{
				super(elements, elements_count);
				this.increment_isPositve_orNegative = increment_isPositve_orNegative;
			}

			public synchronized boolean check()
			{
				if(this.increment_isPositve_orNegative ? this.element_made_increment<0 : this.element_made_increment>0 )
					return this.isWorking = false;
				return super.check();
			}
			public synchronized boolean checkOnlyIncrement()
			{
				if(this.element_made_increment == 0)
					return this.isWorking = false;
				if(this.increment_isPositve_orNegative ? this.element_made_increment<0 : this.element_made_increment>0 )
					return this.isWorking = false;
				return true;
			}
			public long shadowOnlyIncrement()
			{
				return this.element_made_increment;
			}
			public synchronized void setValueReMade_increment(long element_made_increment)
			{
				this.element_made_increment = element_made_increment;
				this.checkOnlyIncrement();
			}
			public synchronized void addValueReMade_increment(long element_made_increment_add)
			{
				if(element_made_increment_add == 0)
					return;
				this.element_made_increment += element_made_increment_add;
				this.checkOnlyIncrement();
			}

			public synchronized Long peekNext_element_made()
			{
				if(this.increment_isPositve_orNegative ? this.element_made_increment<0 : this.element_made_increment>0 )
					return null;
				return super.peekNext_element_made();
			}
			public synchronized Long push_element_made()
			{
				if(!this.checkOnlyIncrement())
					return null;
				return super.push_element_made();
			}
		}

		public final FactroyCtrl ctrl;
		public WithFactroyExtd01(boolean increment_isPositve_orNegative)
		{
			this(true, increment_isPositve_orNegative);
		}
		protected WithFactroyExtd01(boolean isCtrlInstance, boolean increment_isPositve_orNegative)
		{
			super(false);
			this.ctrl = (!isCtrlInstance)?null:new FactroyCtrl(this.elements, this.elements_count, increment_isPositve_orNegative);
		}
		protected WithFactroyExtd01(boolean increment_isPositve_orNegative, long element_made_initial, long element_made_increment, Integer element_made_times_break, Long element_made_break_min, Long element_made_break_max)
		{
			super(false);
			this.ctrl = new FactroyCtrl(this.elements, this.elements_count, increment_isPositve_orNegative);
			this.ctrl.setValue(element_made_initial, element_made_increment, element_made_times_break, element_made_break_min, element_made_break_max);
			this.ctrl.setNull(element_made_times_break==null, element_made_break_min==null, element_made_break_max==null);
		}
		public FactroyCtrl ctrl()
		{
			return this.ctrl;
		}
	}

	public static class WithFactroyExtd02 extends WithFactroy
	{
		public static class FactroyCtrl extends WithFactroy.FactroyCtrl
		{
			protected final boolean increment_isPositve_orNegative;
			protected FactroyCtrl(RfTreeSet<Long> elements, HashMap<Long, Integer> elements_count, boolean increment_isPositve_orNegative)
			{
				super(elements, elements_count);
				this.increment_isPositve_orNegative = increment_isPositve_orNegative;
			}
			public synchronized void setNull(boolean isSetNull_element_made_times_break, boolean isSetNull_element_made_break_min, boolean isSetNull_element_made_break_max)
			{
				if(isSetNull_element_made_times_break)
					this.element_made_times_break = null;

			}
			public synchronized void setValue(Long element_made_initial, Long element_made_increment, Integer element_made_times_break, Long element_made_break_min, Long element_made_break_max)
			{
				if(element_made_initial != null)
					this.element_made = element_made_initial;
				if(element_made_increment != null)
					this.element_made_increment = element_made_increment;
				if(element_made_times_break != null)
					this.element_made_times_break = element_made_times_break;
				this.check();
			}
			public synchronized Long[] shadowReMade0Increment1BreakMin2BreakMax()
			{
				Long shadow[] = {
						this.element_made_increment,
						null,
						null
						};
				return shadow;
			}

			public synchronized boolean check()
			{
				if(this.increment_isPositve_orNegative ? this.element_made_increment<0 : this.element_made_increment>0 )
					return this.isWorking = false;

				if(this.isClose)
					return this.isWorking = false;
				if(this.element_made_increment == 0)
					return this.isWorking = false;
				if(this.element_made_times_break != null && this.element_made_times_break < this.element_made_times)
					return this.isWorking = false;
				return this.isWorking = true;
			}
			public synchronized boolean checkOnlyIncrement()
			{
				if(this.element_made_increment == 0)
					return this.isWorking = false;
				if(this.increment_isPositve_orNegative ? this.element_made_increment<0 : this.element_made_increment>0 )
					return this.isWorking = false;
				return true;
			}
			public long shadowOnlyIncrement()
			{
				return this.element_made_increment;
			}
			public synchronized void setValueReMade_increment(long element_made_increment)
			{
				this.element_made_increment = element_made_increment;
				this.checkOnlyIncrement();
			}
			public synchronized void addValueReMade_increment(long element_made_increment_add)
			{
				if(element_made_increment_add == 0)
					return;
				this.element_made_increment += element_made_increment_add;
				this.checkOnlyIncrement();
			}

			public synchronized Long peekNext_element_made()
			{
				if(this.increment_isPositve_orNegative ? this.element_made_increment<0 : this.element_made_increment>0 )
					return null;

				if(!this.isWorking)
					return null;
				if(this.element_made_increment == 0)
					return null;
				if(this.element_made_times_break != null && this.element_made_times_break < this.element_made_times + 1)
					return null;
				Long element_made_tmp = this.element_made + this.element_made_increment;
				return element_made_tmp;
			}
			public synchronized Long push_element_made()
			{
				if(!this.checkOnlyIncrement())
					return null;
				return super.push_element_made();
			}
		}

		public final FactroyCtrl ctrl;
		public WithFactroyExtd02(boolean increment_isPositve_orNegative)
		{
			this(true, increment_isPositve_orNegative);
		}
		protected WithFactroyExtd02(boolean isCtrlInstance, boolean increment_isPositve_orNegative)
		{
			super(false);
			this.ctrl = (!isCtrlInstance)?null:new FactroyCtrl(this.elements, this.elements_count, increment_isPositve_orNegative);
		}
		protected WithFactroyExtd02(boolean increment_isPositve_orNegative, long element_made_initial, long element_made_increment, Integer element_made_times_break, Long element_made_break_min, Long element_made_break_max)
		{
			super(false);
			this.ctrl = new FactroyCtrl(this.elements, this.elements_count, increment_isPositve_orNegative);
			this.ctrl.setValue(element_made_initial, element_made_increment, element_made_times_break, element_made_break_min, element_made_break_max);
			this.ctrl.setNull(element_made_times_break==null, element_made_break_min==null, element_made_break_max==null);
		}
		public FactroyCtrl ctrl()
		{
			return this.ctrl;
		}
	}

	static class testbed
	{
		public static void main(String[] args) throws MalformedURLException, IOException
		{
			test0.echoHere();

			ScSetSortedLong.WithFactroy sortset = new ScSetSortedLong.WithFactroy();

			sortset.ctrl.setValue(51L, +5L, null, null, null);

			test0.exit();

			int e = 100;

			int element_made_now1 = 57;
			int element_made_increment = -5;
			int times = (e-element_made_now1) / element_made_increment;
			test0.echo(times);
			test0.echo(element_made_increment * times + element_made_now1);
			test0.exit();

			TreeSet<Long> set = new TreeSet<Long>();
			set.add(1L);
			set.add(5L);
			set.add(9L);

			test0.echo(set.lower(5L));
			test0.echo(set.floor(5L));

			test0.echo(set.lower(-1L));
			test0.echo(set.floor(-1L));

		}

		public static class Object00 implements java.lang.Comparable<Object00>
		{
			static int intv = 100;
			int value = (intv+=100);
			@Override
			public int compareTo(Object00 o)
			{
				return this.value - o.value;
			}
		}
		public static class Object01 implements Comparable<Object01>
		{
			static int intv = 100;
			int value = (intv+=100);
			public int peek()
			{
				test0.echoHere(test0.intMelon++);
				return value;
			}
			public int compareTo(Object01 o)
			{
				return this.peek() > o.peek() ? -1 : 0;
			}
		}
		public static void main2(String[] args)
		{
			Object01 obj1, obj2, obj3;
			obj1 = new Object01();
			obj2 = new Object01();
			obj3 = new Object01();

			test0.intMelon = 1;
			TreeSet<Object01> set = new TreeSet<Object01>();
			set.add(obj1);
			set.add(obj2);
			set.add(obj3);
			set.add(new Object01());
			set.add(new Object01());
			set.add(new Object01());
			set.add(new Object01());
			set.add(new Object01());
			set.add(new Object01());
			set.add(new Object01());
			for(Object01 obj: set)
				test0.echo(obj.value);
			test0.br();

			test0.intMelon = 1;
			ArrayList<Object01> list = new ArrayList<Object01>();
			list.add(obj1);
			list.add(obj2);
			list.add(obj3);
			list.add(new Object01());
			list.add(new Object01());
			list.add(new Object01());
			list.add(new Object01());
			list.add(new Object01());
			list.add(new Object01());
			list.add(new Object01());
			list.sort(new Comparator<Object01>()
			{
				public int compare(Object01 o1, Object01 o2)
				{
					return o1.peek() > o2.peek() ? 1 : 0;

				}
			});
			for(Object01 obj: list)
				test0.echo(obj.value);
			test0.br();
		}

		@SuppressWarnings("unused")
		public static void main0(String[] args)
		{
			TreeSet<Object00> set = new TreeSet<Object00>();
			Object00 obj1, obj2, obj3;
			obj1 = new Object00();
			obj2 = new Object00();
			obj3 = new Object00();
			set.add(obj1);
			set.add(obj2);
			set.add(obj3);
			for(Object00 obj: set)
				test0.echo(obj.value);
			obj2.value = 0;
			test0.br();
			for(Object00 obj: set)
				test0.echo(obj.value);

			LinkedList<Object00> list = new LinkedList<Object00>();
			list.add(obj1);
			list.add(obj3);
			list.add(obj2);
			list.sort(new Comparator<Object00>()
			{
				public int compare(Object00 o1, Object00 o2)
				{
					return o1.value > o2.value ? 1 : 0;

				}
			});
			test0.br();
			for(Object00 obj: list)
				test0.echo(obj.value);

			AbstractMap.SimpleEntry<Long, Long> a = new AbstractMap.SimpleEntry<Long, Long>(1L, 1L);
		}
		public static void main1(String[] args)
		{
			ScSetSortedLong.WithFactroy sortedset = new ScSetSortedLong.WithFactroy(0, 10, null, null, 20L);
			sortedset.addAll(5L, 15L);

			for(Object tmp;;)
			{
				tmp = sortedset.pollMin();
				test0.echo(tmp);
				if(tmp == null)
					break;
			}
		}
	}

}

