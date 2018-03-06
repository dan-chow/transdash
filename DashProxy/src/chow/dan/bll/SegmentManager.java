package chow.dan.bll;

import java.util.concurrent.ConcurrentHashMap;

public class SegmentManager {

	private static volatile SegmentManager manager = new SegmentManager();
	private ConcurrentHashMap<String, SegmentData> uriDataMap;

	public static String getInitUri(String segment) {
		return segment.replaceFirst("-[0-9]+.m4s$", "-init.mp4");
	}

	private SegmentManager() {
		uriDataMap = new ConcurrentHashMap<>();
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

	public void put(String key, SegmentData data) {
		uriDataMap.put(key, data);
	}

	public SegmentData get(String key) {
		return uriDataMap.get(key);
	}
}
