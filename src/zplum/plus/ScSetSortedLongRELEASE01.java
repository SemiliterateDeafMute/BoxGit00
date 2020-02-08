package zplum.plus;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;

import zplum.kit.test0;

public class ScSetSortedLongRELEASE01 extends AbstractSet<Long>
{
	protected final RfTreeSet<Long> elements = new RfTreeSet<Long>();
	protected final HashMap<Long, Integer> element_count = new HashMap<Long, Integer>();

	public Iterator<Long> iterator()
	{
		return this.elements.iterator();
	}
	public int size()
	{
		return this.elements.size();
	}
	public int sizeIncludeSameElements()
	{
		int size = 0;
		for(Integer size_add: element_count.values())
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
			count += this.element_count.getOrDefault(e, 1);
		this.elements.add(e);
		if(count > 1)
			this.element_count.put(e, count);
	}
	public synchronized void add(long e, int count)
	{
		if(count <= 0)
			return;
		if(this.elements.contains(e))
			count += this.element_count.getOrDefault(e, 1);
		this.elements.add(e);
		if(count > 1)
			this.element_count.put(e, count);
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
		this.element_count.remove(e);
		return true;
	}
	public synchronized void clear()
	{
		this.elements.clear();
		this.element_count.clear();
	}
	public synchronized void modify(long orig, long dest)
	{
		if(!this.contains(orig))
			return;
		int count = this.count(orig) + this.count(dest);
		this.remove(orig);
		this.element_count.put(dest, count);
	}
	public synchronized void modifyMin(long dest)
	{
		Long orig = this.peekMin();
		if(orig == null)
			return;
		int count = this.count(orig) + this.count(dest);
		this.remove(orig);
		this.element_count.put(dest, count);
	}
	public synchronized void modifyMax(long dest)
	{
		Long orig = this.peekMax();
		if(orig == null)
			return;
		int count = this.count(orig) + this.count(dest);
		this.remove(orig);
		this.element_count.put(dest, count);
	}

	public synchronized int count(long e)
	{
		if(!this.elements.contains(e))
			return 0;
		return this.element_count.getOrDefault(e, 1);
	}
	public synchronized int countMin()
	{
		Long e = this.elements.first();
		if(e == null)
			return 0;
		return element_count.getOrDefault(e, 1);
	}
	public synchronized int countMax()
	{
		Long e = this.elements.last();
		if(e == null)
			return 0;
		return element_count.getOrDefault(e, 1);
	}

	public synchronized Long pollMin()
	{
		Long e = this.elements.pollFirst();
		this.element_count.remove(e);
		return e;
	}
	public synchronized Long pollMax()
	{
		Long e = this.elements.pollLast();
		this.element_count.remove(e);
		return e;
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

	public SortedSet<Long> tailSet(long fromElement, boolean inclusive)
	{
		return this.elements.tailSet(fromElement, inclusive);
	}
	public SortedSet<Long> headSet(long toElement, boolean inclusive)
	{
		return this.elements.headSet(toElement, inclusive);
	}

	public static class WithFactroy extends ScSetSortedLongRELEASE01
	{
		protected boolean isWorking = false;
		protected long    element_made = 0;
		protected long    element_made_increment = 0;
		protected Long    element_made_break_min = null;
		protected Long    element_made_break_max = null;
		protected int     elemaut_made_times = 0;
		protected Integer elemaut_made_times_break = null;

		public WithFactroy() {};
		protected WithFactroy(long element_made_initial, long elemaut_made_increment, Integer elemaut_made_times_break, Long element_made_break_min, Long element_made_break_max)
		{
			this.reset(element_made_initial, elemaut_made_increment, elemaut_made_times_break, element_made_break_min, element_made_break_max);
		}

		public synchronized boolean check()
		{
			if(this.element_made_increment == 0)
				return this.isWorking = false;
			if(this.elemaut_made_times_break != null && this.elemaut_made_times_break < this.elemaut_made_times)
				return this.isWorking = false;
			if(this.element_made_break_max != null   && this.element_made_break_max   < this.element_made)
				return this.isWorking = false;
			if(this.element_made_break_min != null   && this.element_made_break_min   > this.element_made)
				return this.isWorking = false;
			return this.isWorking = true;
		}
		public synchronized void reset(long element_made_initial, long elemaut_made_increment, Integer elemaut_made_times_break, Long element_made_break_min, Long element_made_break_max)
		{
			this.element_made = element_made_initial;
			this.element_made_increment = elemaut_made_increment;
			this.element_made_break_min = element_made_break_min;
			this.element_made_break_max = element_made_break_max;
			this.elemaut_made_times_break = (elemaut_made_times_break==null)?null:(elemaut_made_times_break<0)?null:elemaut_made_times_break;
			this.check();
		}
		public synchronized void elementMade_set(long element)
		{
			this.element_made = element;
			this.check();
		}
		public synchronized void elementMade_add(long element_add)
		{
			this.elementMade_set(this.element_made + element_add);
		}
		public synchronized void elementMadeIncrement_set(long increment)
		{
			this.element_made_increment = increment;
			this.check();
		}
		public synchronized void elementMadeIncrement_add(long increment_add)
		{
			this.elementMadeIncrement_set(this.element_made_increment + increment_add);
		}
		public synchronized void elementMadeTimesBreak_set(Integer times_break)
		{
			this.elemaut_made_times_break = times_break;
			this.check();
		}
		public synchronized void elementMadeTimesBreak_add(Integer times_break_add)
		{
			this.elementMadeTimesBreak_set(this.elemaut_made_times_break + times_break_add);
		}
		public synchronized Long elementMade_peek()
		{
			return (this.isWorking)?this.element_made:null;
		}
		public synchronized Long elementMade_peekNext()
		{
			if(!this.isWorking)
				return null;
			if(this.element_made_increment == 0)
				return null;
			if(this.elemaut_made_times_break != null && this.elemaut_made_times_break < this.elemaut_made_times + 1)
				return null;
			Long element_made_tmp = this.element_made + this.element_made_increment;
			if(this.element_made_break_max != null   && this.element_made_break_max   < element_made_tmp)
				return null;
			if(this.element_made_break_min != null   && this.element_made_break_min   > element_made_tmp)
				return null;
			return element_made_tmp;
		}

		public synchronized Long elementMade_push()
		{
			if(!this.isWorking)
				return null;
			this.elemaut_made_times += 1;
			this.element_made += this.element_made_increment;
			this.check();
			return (this.isWorking)?this.element_made:null;
		}
		public synchronized Long elementMade_push(long element_made_increment_)
		{
			if(!this.isWorking)
				return null;
			if(element_made_increment_ == 0)
				return null;
			this.elemaut_made_times += 1;
			this.element_made += element_made_increment_;
			this.check();
			return (this.isWorking)?this.element_made:null;
		}
		public boolean elementMade_equal(long e)
		{
			if(this.isWorking && this.element_made == e)
				return true;
			return false;
		}
		public synchronized Boolean elementMade_IsMin()
		{
			if(!this.isWorking)
				return false;
			if(this.elements.isEmpty())
				return true;
			else if(this.element_made == this.elements.first())
				return null;
			return this.element_made < this.elements.first();
		}
		public synchronized Boolean elementMade_IsMax()
		{
			if(!this.isWorking)
				return false;
			if(this.elements.isEmpty())
				return true;
			else if(this.element_made == this.elements.last())
				return null;
			return this.element_made > this.elements.last();
		}

		public synchronized boolean isWorking()
		{
			return this.isWorking;
		}
		public int elementMadeTimes()
		{
			return this.elemaut_made_times;
		}
		public Integer elementMadeTimesBreak()
		{
			return this.elemaut_made_times_break;
		}
		public long elementMadeIncrement()
		{
			return this.element_made_increment;
		}

		public int elementMadeTimes_elapsed()
		{
			return this.elemaut_made_times;
		}
		public synchronized int elementMadeTimes_remaining()
		{
			if(!this.isWorking)
				return 0;
			if(this.elemaut_made_times_break == null)
				return -1;
			int times = this.elemaut_made_times_break - this.elemaut_made_times;
			if(times < 0)
				return 0;
			return times;
		}

		public boolean isEmpty()
		{
			if(this.isWorking)
				return false;
			return super.isEmpty();
		}
		public boolean contains(long e)
		{
			if(this.elementMade_equal(e))
				return true;
			return super.contains(e);
		}
		public synchronized boolean remove(long e)
		{
			if(this.elementMade_equal(e))
				this.elementMade_push();
			return super.remove(e);
		}
		public synchronized boolean remove(long e, long element_made_increment_)
		{
			if(this.elementMade_equal(e))
				this.elementMade_push(element_made_increment_);
			return super.remove(e);
		}
		public synchronized void clear()
		{
			this.elements.clear();
			this.element_count.clear();
			this.isWorking = false;
		}

		public synchronized int count(long e)
		{
			return super.count(e) + (this.elementMade_equal(e)?1:0);
		}
		public synchronized int countMin()
		{
			Boolean state = this.elementMade_IsMin();
			if(state == true)
				return 1;
			Long e = this.elements.first();
			if(e == null)
				return 0;
			if(state == false)
				return element_count.getOrDefault(e, 1);
			return element_count.getOrDefault(e, 1) + 1;
		}
		public synchronized int countMax()
		{
			Boolean state = this.elementMade_IsMax();
			if(state == true)
				return 1;
			Long e = this.elements.last();
			if(e == null)
				return 0;
			if(state == false)
				return element_count.getOrDefault(e, 1);
			return element_count.getOrDefault(e, 1) + 1;
		}

		public synchronized Long peekMin()
		{
			if(this.elementMade_IsMin() == true)
				return this.element_made;
			return super.peekMin();
		}
		public synchronized Long peekMax()
		{
			if(this.elementMade_IsMax() == true)
				return this.element_made;
			return super.peekMax();
		}
		public synchronized Long pollMin()
		{
			Long e = this.peekMin();
			if(e != null)
				this.remove((long)e);
			return e;
		}
		public synchronized Long pollMax()
		{
			Long e = this.peekMax();
			if(e != null)
				this.remove((long)e);
			return e;
		}
		public Long peekSelf(Long e)
		{
			return super.contains(e)&&this.elementMade_equal(e) ? e : null;
		}
		public Long peekLess(Long e, boolean inclusive)
		{
			if(inclusive && this.peekSelf(e)!=null)
				return e;
			return ScMath.max(this.elementMade_peek(), super.peekLess(e, inclusive));
		}
		public Long peekMore(Long e, boolean inclusive)
		{
			if(inclusive && this.peekSelf(e)!=null)
				return e;
			return ScMath.min(this.elementMade_peek(), super.peekMore(e, inclusive));
		}
		public SortedSet<Long> tailSet(long fromElement, boolean inclusive)
		{
			SortedSet<Long> set = super.tailSet(fromElement, inclusive);
			Long e = this.elementMade_peek();
			if(e != null)
				set.add(e);
			return set;
		}
		public SortedSet<Long> headSet(long toElement, boolean inclusive)
		{
			SortedSet<Long> set = super.headSet(toElement, inclusive);
			Long e = this.elementMade_peek();
			if(e != null)
				set.add(e);
			return set;
		}

		public Long peekLess(Long e, boolean inclusive, boolean isIncludeNextAutoValue)
		{
			if(inclusive && this.peekSelf(e)!=null)
				return e;
			return ScMath.max(this.elementMade_peek(), this.elementMade_peekNext(), super.peekLess(e, inclusive));
		}
		public Long peekMore(Long e, boolean inclusive, boolean isIncludeNextAutoValue)
		{
			if(inclusive && this.peekSelf(e)!=null)
				return e;
			return ScMath.min(this.elementMade_peek(), this.elementMade_peekNext(), super.peekMore(e, inclusive));
		}

	}

	public static void main1(String[] args)
	{
		ScSetSortedLongRELEASE01.WithFactroy sortedset = new ScSetSortedLongRELEASE01.WithFactroy(0, 10, null, null, 20L);
		sortedset.addAll(5L, 15L);

		for(Long tmp;;)
		{
			tmp = sortedset.pollMin();
			test0.echo(tmp);
			if(tmp == null)
				break;
		}
	}
}

