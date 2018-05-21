package chow.dan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

public class LFUCache extends LoggableCache {
	HashMap<String, Chunk> keyValueMap;
	HashMap<String, Integer> keyFreqMap;
	TreeMap<Integer, LinkedHashSet<String>> freqKeysMap;

	private int size;
	private int capacity;
	int min = -1;

	public LFUCache(int cap) {
		size = 0;
		capacity = cap;

		keyValueMap = new HashMap<>();
		keyFreqMap = new HashMap<>();
		freqKeysMap = new TreeMap<>();
	}

	@Override
	public Chunk get(String key) {
		if (!keyValueMap.containsKey(key))
			return null;

		int freq = keyFreqMap.get(key);
		keyFreqMap.put(key, freq + 1);

		freqKeysMap.get(freq).remove(key);
		if (freqKeysMap.get(freq).isEmpty())
			freqKeysMap.remove(freq);

		if (!freqKeysMap.containsKey(freq + 1))
			freqKeysMap.put(freq + 1, new LinkedHashSet<>());
		freqKeysMap.get(freq + 1).add(key);

		if (logging) {
			System.out.println(keyValueMap);
			System.out.println(keyFreqMap);
			System.out.println(freqKeysMap);
			System.out.println();
		}

		return keyValueMap.get(key);
	}

	@Override
	public void set(String key, Chunk value) {
		if (capacity <= 0)
			return;

		if (keyValueMap.containsKey(key)) {
			keyValueMap.put(key, value);
			get(key);
			return;
		}

		Iterator<Entry<Integer, LinkedHashSet<String>>> iterator = freqKeysMap.entrySet().iterator();
		while (size + value.getSize() > capacity && iterator.hasNext()) {
			Entry<Integer, LinkedHashSet<String>> entry = iterator.next();

			Iterator<String> keysIterator = entry.getValue().iterator();
			while (keysIterator.hasNext()) {
				String keyToEvict = keysIterator.next();

				Chunk chunk = keyValueMap.get(keyToEvict);
				size -= chunk.getSize();

				keyValueMap.remove(keyToEvict);
				keyFreqMap.remove(keyToEvict);

				keysIterator.remove();
				if (freqKeysMap.get(entry.getKey()).isEmpty())
					iterator.remove();

				if (size + value.getSize() <= capacity)
					break;
			}
		}

		size += value.getSize();

		keyValueMap.put(key, value);
		keyFreqMap.put(key, 1);

		if (!freqKeysMap.containsKey(1))
			freqKeysMap.put(1, new LinkedHashSet<>());
		freqKeysMap.get(1).add(key);

		if (logging) {
			System.out.println(keyValueMap);
			System.out.println(keyFreqMap);
			System.out.println(freqKeysMap);
			System.out.println();
		}

	}
}