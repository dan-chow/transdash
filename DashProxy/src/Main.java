import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jndi.toolkit.url.Uri;

import chow.dan.mpd.MPD;
import chow.dan.mpd.MPD.Period;
import chow.dan.mpd.MPD.Period.AdaptationSet.Representation;
import chow.dan.mpd.MPD.Period.AdaptationSet.Representation.SegmentList.SegmentURL;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {
	public static void main(String[] args) throws IOException, JAXBException {
		OkHttpClient httpClient = new OkHttpClient();
		Headers.Builder headers = new Headers.Builder();
		Uri uri = new Uri("http://114.212.84.179:8080/video/result.mpd");
		Request.Builder request = new Request.Builder().url(uri.toString()).headers(headers.build());
		Response response = httpClient.newCall(request.build()).execute();

		JAXBContext context = JAXBContext.newInstance(MPD.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		MPD mpd = (MPD) unmarshaller.unmarshal(response.body().byteStream());

		System.out.println();
		Period period = mpd.getPeriod();
		for (Representation representation : period.getAdaptationSet().getRepresentation()) {
			for (SegmentURL segmentURL : representation.getSegmentList().getSegmentURL()) {
				System.out.println(segmentURL.getMedia());
			}
		}
	}
}
