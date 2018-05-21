package chow.dan.bll;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FilenameUtils;

import chow.dan.bll.SegmentData.Segment;

public class SegmentManager {

	private static volatile SegmentManager manager = new SegmentManager();
	private ConcurrentHashMap<String, SegmentData> uriDataMap;

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

	public void clear() {
		uriDataMap.clear();
	}

	public void put(String key, SegmentData data) {
		uriDataMap.put(key, data);
	}

	public SegmentData get(String key) {
		return uriDataMap.get(key);
	}

	public Segment getByUri(String segmentUri) {
		String path = FilenameUtils.getPath(segmentUri);
		String name = FilenameUtils.getName(segmentUri);

		SegmentData data = SegmentManager.getInstance().get(path);
		if (data == null)
			return null;

		return data.getSegment(name);
	}
}
