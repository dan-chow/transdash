package chow.dan.bll;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chow.dan.bll.SegmentData.Segment;
import chow.dan.common.Content;
import chow.dan.dash.DashTranscoder;

public class ContentManager {

	public static int SEG_HIT_RAW = 0;
	public static int SEG_HIT_TRANS = 0;
	public static int SEG_REQUEST = 0;

	private static Log logger = LogFactory.getLog(ContentManager.class);

	public static void clearCounters() {
		ContentManager.SEG_REQUEST = 0;
		ContentManager.SEG_HIT_RAW = 0;
		ContentManager.SEG_HIT_TRANS = 0;
	}

	public static Content get(String uri) throws IOException {
		Content content = getFromCache(uri);
		if (content == null) {
			content = downloadAndCache(uri);
		}
		return content;
	}

	private static Content getFromCache(String uri) {
		return CacheManager.getInstance().get(uri);
	}

	public static Content downloadAndCache(String uri) throws IOException {
		Content content = Downloader.download(uri);
		CacheManager.getInstance().put(uri, content);
		return content;
	}

	public static Content getSegmentWithTrans(String segmentUri) throws IOException, InterruptedException {

		SEG_REQUEST++;

		Content content = getFromCache(segmentUri);
		if (content != null) {
			SEG_HIT_RAW++;
			return content;
		}

		String usefulSegmentUri = usefulSegmentUriOrNull(segmentUri);

		if (usefulSegmentUri != null) {
			logger.warn("trans from " + usefulSegmentUri + " to " + segmentUri);
			SEG_HIT_TRANS++;
			return fakeTranscodeAndCache(usefulSegmentUri, segmentUri);
		}

		return downloadAndCache(segmentUri);

	}

	public static Content getSegmentWithoutTrans(String segmentUri) throws IOException, InterruptedException {
		SEG_REQUEST++;

		Content content = getFromCache(segmentUri);
		if (content != null) {
			SEG_HIT_RAW++;
			return content;
		}

		return content = downloadAndCache(segmentUri);
	}

	private static String usefulSegmentUriOrNull(String segmentUri) {
		String path = FilenameUtils.getPath(segmentUri);

		Segment segment = SegmentManager.getInstance().getByUri(segmentUri);
		if (segment == null)
			return null;

		segment = segment.next;

		while (segment != null) {
			String next = path + segment.name;
			if (CacheManager.getInstance().contains(next))
				return next;

			segment = segment.next;
		}

		return null;
	}

	public static Content transcodeAndCache(String segmentUri, String targetSegmentUri)
			throws IOException, InterruptedException {
		String initSegmentUri = getInitUriForSegment(segmentUri);

		String targetName = FilenameUtils.getName(targetSegmentUri);

		Content initContent = get(initSegmentUri);
		Content segmentContent = getFromCache(segmentUri);
		Content content = DashTranscoder.transcode(initContent, segmentContent, targetName);
		CacheManager.getInstance().put(targetSegmentUri, content);

		return content;
	}

	public static Content fakeTranscodeAndCache(String segmentUri, String targetSegmentUri)
			throws IOException, InterruptedException {

		Content content = DashTranscoder.fakeTranscode(segmentUri, targetSegmentUri);
		CacheManager.getInstance().put(targetSegmentUri, content);

		return content;
	}

	private static String getInitUriForSegment(String segmentUri) {
		return segmentUri.replaceFirst("-[0-9]+.m4s$", "-init.mp4");
	}

}
