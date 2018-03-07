package chow.dan.bll;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chow.dan.bll.SegmentData.Segment;
import chow.dan.common.Content;
import chow.dan.dash.DashTranscoder;

public class ContentManager {

	private static Log logger = LogFactory.getLog(ContentManager.class);

	public static Content get(String uri) throws IOException {
		Content content = getFromCache(uri);
		if (content == null) {
			content = downloadAndCache(uri);
		} else {
			logger.warn("hit:" + uri);
		}
		return content;
	}

	private static Content getFromCache(String uri) {
		return CacheManager.getInstance().get(uri);
	}

	private static Content downloadAndCache(String uri) throws IOException {
		logger.warn("download:" + uri);

		Content content = Downloader.download(uri);
		CacheManager.getInstance().put(uri, content);
		return content;
	}

	public static Content getSegment(String segmentUri) throws IOException, InterruptedException {
		Content content = getFromCache(segmentUri);

		if (content != null) {
			return content;
		}

		String usefulSegmentUri = usefulSegmentUriOrNull(segmentUri);
		if (usefulSegmentUri == null) {
			return downloadAndCache(segmentUri);
		}

		content = transcodeAndCache(usefulSegmentUri, segmentUri);
		CacheManager.getInstance().put(segmentUri, content);

		return content;
	}

	private static String usefulSegmentUriOrNull(String segmentUri) {
		String path = FilenameUtils.getPath(segmentUri);

		Segment segment = SegmentManager.getInstance().getByUri(segmentUri);
		if (segment == null)
			return null;

		segment = segment.next;

		while (segment != null) {
			String next = FilenameUtils.concat(path, segment.name);
			if (CacheManager.getInstance().contains(next))
				return next;

			segment = segment.next;
		}

		return null;
	}

	private static Content transcodeAndCache(String segmentUri, String targetSegmentUri)
			throws IOException, InterruptedException {

		logger.warn("transcode:" + segmentUri + " to " + targetSegmentUri);
		String initSegmentUri = getInitUriForSegment(segmentUri);

		String targetName = FilenameUtils.getName(targetSegmentUri);

		Content initContent = get(initSegmentUri);
		Content segmentContent = getSegment(segmentUri);
		Content content = DashTranscoder.transcode(initContent, segmentContent, targetName);
		CacheManager.getInstance().put(targetSegmentUri, content);

		return content;
	}

	private static String getInitUriForSegment(String segmentUri) {
		return segmentUri.replaceFirst("-[0-9]+.m4s$", "-init.mp4");
	}

}
