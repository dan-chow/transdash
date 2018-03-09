package chow.dan.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

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
public class ProxyController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ProxyController.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getParameter("url");

		String trans = request.getParameter("trans");
		boolean shouldTrans = trans.trim().isEmpty() ? true : Boolean.parseBoolean(trans);

		Content content = Content.EMPTY;
		if (url.endsWith("mpd")) {
			content = getAndProcessMpd(url);
		} else if (url.endsWith("mp4")) {
			content = getInitMp4(url);
		} else if (url.endsWith("m4s")) {
			content = shouldTrans ? getSegmentWithTrans(url) : getSegmentWithoutTrans(url);
		}

		response.setHeader("Accept-Ranges", "bytes");
		response.setContentLength(content.getData().length);
		ServletOutputStream os = response.getOutputStream();
		os.write(content.getData());
		os.close();
	}

	private Content getAndProcessMpd(String url) {
		try {
			Content content = ContentManager.get(url);
			MPD mpd = MpdManager.parseMpd(content);
			String baseUrl = FilenameUtils.getPath(url);
			SegmentManager.getInstance().put(baseUrl, new SegmentData(mpd));
			return content;
		} catch (IOException | JAXBException e) {
			logger.warn(e);
			return Content.EMPTY;
		}

	}

	private Content getInitMp4(String url) {
		try {
			return ContentManager.get(url);
		} catch (IOException e) {
			logger.warn(e);
			return Content.EMPTY;
		}
	}

	private Content getSegmentWithTrans(String url) {
		try {
			return ContentManager.getSegmentWithTrans(url);
		} catch (IOException | InterruptedException e) {
			logger.warn(e);
			return new Content(new byte[0]);
		}
	}

	private Content getSegmentWithoutTrans(String url) {
		try {
			return ContentManager.get(url);
		} catch (IOException e) {
			logger.warn(e);
			return new Content(new byte[0]);
		}
	}

}
