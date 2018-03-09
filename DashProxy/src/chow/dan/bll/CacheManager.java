package chow.dan.bll;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;

import chow.dan.common.Content;

public class CacheManager {

	private static volatile CacheManager manager;
	private CacheAccess<String, Content> cacheAccess;

	private CacheManager() {
		cacheAccess = JCS.getInstance("testCache1");
		cacheAccess.clear();
	}

	public static CacheManager getInstance() {
		if (manager == null) {
			synchronized (CacheManager.class) {
				if (manager == null) {
					manager = new CacheManager();
				}
			}
		}

		return manager;
	}

	public void clear() {
		cacheAccess.clear();
	}

	public boolean contains(String key) {
		return cacheAccess.get(key) != null;
	}

	public Content get(String key) {
		return cacheAccess.get(key);
	}

	public void put(String key, Content value) {
		cacheAccess.put(key, value);
	}

}
