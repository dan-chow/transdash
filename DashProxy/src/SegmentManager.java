import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;

public class SegmentManager {
	// Segment

	private static volatile SegmentManager manager;
	private static CacheAccess<String, String> cacheAccess;

	private SegmentManager() {
		cacheAccess = JCS.getInstance("testCache1");
	}

	public static SegmentManager getInstance() {
		if (manager == null) {
			synchronized (SegmentManager.class) {
				if (manager == null) {
					manager = new SegmentManager();
				}
			}
		}

		return manager;
	}

	public static String getFromCache(String key) {
		String file = cacheAccess.get(key);
		return "";

	}

	public static String downloadAndCache() {
		return "";
	}

}
