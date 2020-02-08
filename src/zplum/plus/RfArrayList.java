package zplum.plus;

import java.util.ArrayList;
import java.util.Iterator;

public class RfArrayList<E> extends ArrayList<E>
{
	private static final long serialVersionUID = 6912135985931373421L;

	public synchronized E poll(int index)
	{
		E e = this.get(index);
		this.remove(index);
		return e;
	}
	public int size(Class<?> _class)
	{
		int count = 0;
		Iterator<E> iterator = this.iterator();
		for(;iterator.hasNext();)
			if(_class.equals(iterator.next().getClass()))
				count++;
		return count;
	}
	public int size(Class<?> _class, boolean isIncludeInstance)
	{
		if(isIncludeInstance)
			return this.size(_class);
		if(_class.equals(Object.class))
			return this.size();
		int count = 0;
		Iterator<E> iterator = this.iterator();
		for(;iterator.hasNext();)
			if(_class.isInstance(iterator.next()))
				count++;
		return count;
	}
}
