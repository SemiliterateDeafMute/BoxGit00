package zplum.plus;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import zplum.kit.test0;

@Deprecated

public class ScSetSorted<E> extends AbstractSet<E>
{
	private final TreeSet<E> elements = new TreeSet<E>();
	private final HashMap<E, Integer> element_count = new HashMap<E, Integer>();

	@Override
	public Iterator<E> iterator()
	{
		return null;
	}
	@Override
	public int size()
	{
		return 0;
	}
	public int size0()
	{
		return 0;
	}

	public boolean isEmpty(E e)
	{
		return this.elements.isEmpty();
	}
	public boolean contains(Object e)
	{
		return this.elements.contains(e);
	}
	public synchronized boolean add(E e)
	{
		int count = 1;
		if(this.elements.contains(e))
			count += this.element_count.getOrDefault(e, 1);
		this.elements.add(e);
		if(count > 1)
			this.element_count.put(e, count);
		return true;
	}
	@SuppressWarnings("unchecked")
	public synchronized void addAll(E... es)
	{
		for(E e: es)
			this.add(e);
	}
	public synchronized boolean remove(Object e)
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
	public synchronized void modify(E orig, E dest)
	{
		if(!this.contains(orig))
			return;
		int count = this.count(orig) + this.count(dest);
		this.remove(orig);
		this.element_count.put(dest, count);
	}

	public synchronized int count(E e)
	{
		if(!this.elements.contains(e))
			return 0;
		return this.element_count.getOrDefault(e, 1);
	}
	public synchronized int countMin()
	{
		E elements = this.elements.first();
		if(elements == null)
			return 0;
		return element_count.getOrDefault(elements, 1);
	}
	public synchronized int countMax()
	{
		E elements = this.elements.last();
		if(elements == null)
			return 0;
		return element_count.getOrDefault(elements, 1);
	}

	public synchronized E pollMin()
	{
		E e = this.elements.pollFirst();
		this.element_count.remove(e);
		return e;
	}
	public synchronized E pollMax()
	{
		E e = this.elements.pollLast();
		this.element_count.remove(e);
		return e;
	}
	public E peekMin()
	{
		return this.elements.first();
	}
	public E peekMax()
	{
		return this.elements.last();
	}
	public E peekSelf(E e)
	{
		return this.elements.contains(e)?e:null;
	}
	public E peekLess(E e, boolean inclusive)
	{
		return inclusive?this.elements.floor(e):this.elements.lower(e);
	}
	public E peekMore(E e, boolean inclusive)
	{
		return inclusive?this.elements.ceiling(e):this.elements.higher(e);
	}

	public SortedSet<E> tailSet(E fromElement, boolean inclusive)
	{
		return this.elements.tailSet(fromElement, inclusive);
	}
	public SortedSet<E> headSet(E toElement, boolean inclusive)
	{
		return this.elements.headSet(toElement, inclusive);
	}

	public static class TestObject implements Comparable<TestObject>
	{
		public String title;
		public int value = 0;

		public TestObject(String title, int value)
		{
			this.title = title;
			this.value = value;
		}
		public int compareTo(TestObject o)
		{
			return this.value - o.value;
		}
	}

	public static void main(String[] args)
	{
		TestObject obj1, obj2, obj3;
		obj1 = new TestObject("object01", 100);
		obj2 = new TestObject("object02", 1200);
		obj3 = new TestObject("object03", 300);

		ConcurrentSkipListSet<TestObject> list = new ConcurrentSkipListSet<TestObject>();
		list.add(obj1);
		list.add(obj2);
		list.add(obj3);

		for(TestObject obj: list)
		{
			test0.echo(obj.title);
		}

		Object obj = new Object();
		test0.echo(obj1.compareTo((TestObject)obj));

	}

	public static void main1(String[] args)
	{
		ScSetSorted<Long> list = new ScSetSorted<Long>();
		list.addAll(1L, 5L ,9L, 5L);
		test0.echo(list.peekLess(5L, true));
		test0.echo(list.peekMore(5L, true));
		test0.echo(list.peekLess(5L, false));
		test0.echo(list.peekMore(5L, false));

		test0.br();
		test0.echo(list.count(5L));
		list.add(5L);
		test0.echo(list.count(5L));

	}
}

