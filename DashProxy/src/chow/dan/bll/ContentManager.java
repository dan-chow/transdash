package chow.dan.bll;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import chow.dan.mpd.MPD;

public class ContentManager {

	private static ConcurrentHashMap<String, SegmentData> uriDataMap = new ConcurrentHashMap<>();

	public static String getMpd(String uri) throws IOException, JAXBException {
		String mpd = getFromCache(uri);
		if (mpd == null) {
			mpd = downloadAndCache(uri);
		}
		return mpd;
	}

	private static String getFromCache(String uri) throws IOException {
		return CacheManager.getInstance().get(uri);
	}

	private static String downloadAndCache(String uri) throws IOException, JAXBException {
		String content = Downloader.Download(uri);
		CacheManager.getInstance().put(uri, content);
		String baseUrl = uri.substring(0, uri.lastIndexOf('/'));
		uriDataMap.put(baseUrl, new SegmentData(parseMpd(content)));
		return content;
	}

	public static MPD parseMpd(String mpdFile) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(MPD.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (MPD) unmarshaller.unmarshal(IOUtils.toInputStream(mpdFile, "UTF-8"));
	}

	public static String getSegment(String uri) throws IOException, JAXBException {
		String content = getFromCache(uri);
		if (content == null) {
			String baseUri = uri.substring(0, uri.lastIndexOf('/') + 1);
			String file = uri.substring(uri.lastIndexOf('/') + 1);
			SegmentData data = uriDataMap.get(baseUri);

			String newUri = baseUri + data.getSegment(file);
			System.out.println(newUri);

			// TODO
			// if (found(data) != null)
			// transform
			// else
			// download

			content = downloadAndCache(uri);
		}
		return content;
	}
}
