package chow.dan.bll;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import chow.dan.common.Content;
import chow.dan.mpd.MPD;

public class MpdManager {
	public static MPD parseMpd(Content content) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(MPD.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (MPD) unmarshaller.unmarshal(new ByteArrayInputStream(content.getData()));
	}
}
