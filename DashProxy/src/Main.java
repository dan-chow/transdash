import java.io.IOException;

import javax.xml.bind.JAXBException;

public class Main {
	public static void main(String[] args) throws IOException, JAXBException, InterruptedException {
		// String mpdFile =
		// ContentManager.getMpd("http://114.212.84.179:8080/video/result.mpd");

		// SegmentData dashData = new SegmentData(mpdFile);

		String uri = "114.212.84.179:8080/video/result.mpd";
		System.out.println(uri.substring(uri.lastIndexOf('/') + 1));
	}
}