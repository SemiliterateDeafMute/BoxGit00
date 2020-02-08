package zplum.plus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import zplum.kit.test0;

import java.util.Set;

public class ScHashMap
{
	public static <K,V> HashMap<V,List<K>> reverseKV(HashMap<K,V> map)
	{
		HashMap<V,List<K>> map_reverseKV = new HashMap<V,List<K>>();

		Set<Map.Entry<K,V>> entrys = map.entrySet();
		for(V value: new HashSet<V>(map.values()))
		{
			List<K> collection = new ArrayList<K>();
			for(Map.Entry<K,V> entry: entrys)
				if(entry.getValue().equals(value))
					collection.add(entry.getKey());
			map_reverseKV.put(value, collection);
		}
		return map_reverseKV;
	}

	@SuppressWarnings("unchecked")
	public static <K,V> HashMap<V,List<K>> reverseKV(HashMap<K,? extends List<V>> map, boolean isSingleOrCollection_ReHashMapValue)
	{
		if(isSingleOrCollection_ReHashMapValue)
			return reverseKV((HashMap<K,V>)map);
		HashMap<V,List<K>> map_reverseKV = new HashMap<V,List<K>>();

		Set<V> values = new HashSet<V>();
		for(List<V> collection: new HashSet<List<V>>(map.values()))
			values.addAll(collection);

		Set<?> entrys = map.entrySet();
		Set<Entry<K, List<V>>> entrys_shadow = (Set<Entry<K, List<V>>>)entrys;
		for(V value: values)
		{
			List<K> collection = new ArrayList<K>();
			for(Entry<K, ? extends List<V>> entry: entrys_shadow)
				if(entry.getValue().contains(value))
					collection.add(entry.getKey());
			map_reverseKV.put(value, collection);
		}
		return map_reverseKV;
	}

	public static void main(String[] args)
	{
		HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		map.put(1, ScList.create( ScArray.cerate("a", "b", "c")));
		map.put(2, ScList.create( ScArray.cerate("a", "b", "c")));
		map.put(1, ScList.create( ScArray.cerate("a", "b", "c")));

		HashMap<String, List<Integer>> mapR = reverseKV(map , false);
		test0.echo(mapR);

	}

}
