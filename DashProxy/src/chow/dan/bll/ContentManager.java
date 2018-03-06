package chow.dan.bll;

import java.io.IOException;

import chow.dan.bll.SegmentData.Segment;
import chow.dan.common.Content;
import chow.dan.dash.DashTranscoder;

public class ContentManager {

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

	private static Content downloadAndCache(String uri) throws IOException {
		Content content = Downloader.download(uri);
		CacheManager.getInstance().put(uri, content);
		return content;
	}

	public static Content getSegment(String segmentUri) throws IOException, InterruptedException {
		Content content = getFromCache(segmentUri);

		if (content != null) {
			return content;
		}

		String baseUri = segmentUri.substring(0, segmentUri.lastIndexOf('/') + 1);
		String targetFile = segmentUri.substring(segmentUri.lastIndexOf('/') + 1);

		SegmentData data = SegmentManager.getInstance().get(baseUri);
		Segment segment = data.getSegment(targetFile);

		String usefulSegmentUri = usefulSegmentUriOrNull(baseUri, segment);
		if (usefulSegmentUri == null) {
			return downloadAndCache(segmentUri);
		}

		content = transcodeAndCache(usefulSegmentUri, targetFile);
		CacheManager.getInstance().put(segmentUri, content);
		return content;
	}

	private static String usefulSegmentUriOrNull(String baseUri, Segment current) {
		current = current.next;

		while (current != null) {
			if (CacheManager.getInstance().contains(baseUri + current))
				return baseUri + current;

			current = current.next;
		}

		return null;
	}

	private static Content transcodeAndCache(String segmentUri, String target)
			throws IOException, InterruptedException {
		String init = SegmentManager.getInitUri(segmentUri);

		Content initContent = get(init);
		Content segmentContent = getSegment(segmentUri);
		Content content = DashTranscoder.transcode(initContent, segmentContent, target);

		return content;
	}

}
