package zplum.tools._fc_runthread;

import java.util.LinkedList;

public class EasyMapOO<K,V>
{
	private static class MapElem<K,V>
	{
		private K key;
		private V value;

		private MapElem(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
	}
	private LinkedList<MapElem<K,V>> list = new LinkedList<MapElem<K,V>>();

	public synchronized void put(K key, V value)
	{
		int index;
		boolean flag;
		int size = this.list.size();
		boolean flagDoCreate = true;

		for(index=0, flag=true; index<size; index++)
		{
			if(list.get(index).value == value)
			{
				if(flag)
				{
					list.get(index).key = key;
				} else {
					list.remove(index);
				}
				flag &= false;
			}
		}
		flagDoCreate &= flag;

		for(index=0, flag=true; index<size; index++)
		{
			if(list.get(index).key == key)
			{
				if(flag)
				{
					list.get(index).value = value;
				} else {
					list.remove(index);
				}
				flag &= false;
			}
		}
		flagDoCreate &= flag;

		if(flagDoCreate)
		{
			list.add(new MapElem<K,V>(key,value));
		}

	}

	public K lastKey()
	{
		return list.getLast().key;
	}

	public K firstKey()
	{
		return list.getFirst().key;
	}

	public int size()
	{
		return list.size();
	}

	public V get(K key)
	{
		for(int index=0; index<list.size(); index++)
		{
			if(list.get(index).key == key)
			{
				return list.get(index).value;
			}
		}
		return null;
	}

	public K get_Invert(V value)
	{
		for(int index=0; index<list.size(); index++)
		{
			if(list.get(index).value == value)
			{
				return list.get(index).key;
			}
		}
		return null;
	}

}
