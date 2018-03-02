package chow.dan.bll;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;

public class CacheManager {

	private static volatile CacheManager manager;
	private static CacheAccess<String, String> cacheAccess;

	private CacheManager() {
		cacheAccess = JCS.getInstance("testCache1");
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

	public String get(String key) {
		return cacheAccess.get(key);
	}

	public void put(String key, String value) {
		cacheAccess.put(key, value);
	}

}
