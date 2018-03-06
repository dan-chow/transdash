package chow.dan.controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import chow.dan.bll.ContentManager;
import chow.dan.bll.MpdManager;
import chow.dan.bll.SegmentData;
import chow.dan.bll.SegmentManager;
import chow.dan.common.Content;

@WebServlet("/hello")
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO
		String uri = "http://114.212.84.179:8080/video/result.mpd";
		Content mpdContent;
		try {
			mpdContent = ContentManager.get(uri);
			// TODO FilenameUtils
			String baseUri = uri.substring(0, uri.lastIndexOf('/'));
			SegmentManager.getInstance().put(baseUri, new SegmentData(MpdManager.parseMpd(mpdContent)));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
