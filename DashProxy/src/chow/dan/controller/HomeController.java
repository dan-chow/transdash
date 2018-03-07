package chow.dan.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chow.dan.bll.ContentManager;
import chow.dan.bll.MpdManager;
import chow.dan.bll.SegmentData;
import chow.dan.bll.SegmentManager;
import chow.dan.common.Content;
import chow.dan.mpd.MPD;

@WebServlet("/proxy")
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(HomeController.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getParameter("url");
		logger.warn(url);

		Content content = new Content(new byte[0]);
		if (url.endsWith("mpd")) {
			content = processMpdRequest(url);
		} else if (url.endsWith("mp4")) {
			content = processMp4Request(url);
		} else if (url.endsWith("m4s")) {
			content = processM4sRequest(url);
		} else {
			content = new Content(new byte[0]);
		}

		System.out.println(content.getData().length);

		response.setHeader("Accept-Ranges", "bytes");
		response.setContentLength(content.getData().length);
		ServletOutputStream os = response.getOutputStream();
		os.write(content.getData());
		os.close();
	}

	private Content processMpdRequest(String url) {
		try {
			Content content = ContentManager.get(url);
			MPD mpd = MpdManager.parseMpd(content);
			String baseUrl = FilenameUtils.getPath(url);
			SegmentManager.getInstance().put(baseUrl, new SegmentData(mpd));
			return content;
		} catch (Exception e) {
			logger.warn(e);
			return new Content(new byte[0]);
		}

	}

	private Content processMp4Request(String url) throws IOException {
		return ContentManager.get(url);
	}

	private Content processM4sRequest(String url) {
		try {
			return ContentManager.getSegment(url);
		} catch (IOException | InterruptedException e) {
			logger.warn(e);
			return new Content(new byte[0]);
		}
	}

}
