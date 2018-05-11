package chow.dan.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

import chow.dan.bll.CacheManager;
import chow.dan.bll.ContentManager;
import chow.dan.bll.PostData;
import chow.dan.bll.Statistic;

@WebServlet("/control")
public class TestController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(TestController.class);

	private static HashMap<String, List<Statistic>> map = new HashMap<>();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PostData postData = getPostData(req);

		String testName = postData.getTestName();
		String op = postData.getOp();

		if ("start".equals(op)) {
			prepareForTest(testName);
			logger.warn("start");
			resp.getWriter().write("start ok");
		} else if ("precache".equals(op)) {
			CacheManager.getInstance().clear();
			preCache();
			ContentManager.clearCounters();
			logger.warn("precache");
			resp.getWriter().write("precache ok");
		} else if ("upload".equals(op)) {
			saveStatistic(testName, postData.getStatistic());
			logger.warn("upload");
			resp.getWriter().write("upload ok");
		} else if ("stop".equals(op)) {
			writeToFile(testName);
			cleanUpTest(testName);
			logger.warn("stop");
			resp.getWriter().write("stop ok");
		}

		resp.setContentType("text/plain");
	}

	private PostData getPostData(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();

		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append('\n');
		}

		reader.close();
		logger.warn(sb.toString());
		return new Gson().fromJson(sb.toString(), PostData.class);
	}

	private void prepareForTest(String testName) {
		ContentManager.clearCounters();
		map.put(testName, new ArrayList<>());
	}

	private void preCache() throws IOException {
		ProxyController.getAndProcessMpd("http://114.212.84.179:8080/videos/result.mpd");
		ProxyController.getInitMp4("http://114.212.84.179:8080/videos/5-init.mp4");
		for (int i = 1; i <= 74; i++) {
			String url = "http://114.212.84.179:8080/videos/5-" + i + ".m4s";
			ProxyController.getSegmentWithoutTrans(url);
		}
	}

	private void saveStatistic(String testName, Statistic statistic) throws IOException {

		statistic.totalRequest = ContentManager.SEG_REQUEST;
		statistic.hitRaw = ContentManager.SEG_HIT_RAW;
		statistic.hitTrans = ContentManager.SEG_HIT_TRANS;

		map.get(testName).add(statistic);
		ContentManager.clearCounters();
	}

	private void writeToFile(String testName) throws IOException {
		List<Statistic> statistics = map.get(testName);

		File file = new File(testName + ".csv");

		String header = "avg quality,avg quality variation,stall frequency,avg stall time,total request,hit raw,hit trans\n";
		FileUtils.write(file, header, "UTF-8", true);

		String line = null;
		for (Statistic statistic : statistics) {
			line = statistic.avgVideoQuality() + "," + statistic.avgQualityVariations() + ","
					+ statistic.stallFrequency() + "," + statistic.avgStallDuration() + "," + statistic.totalRequest
					+ "," + statistic.hitRaw + "," + statistic.hitTrans + "\n";
			FileUtils.write(file, line, "UTF-8", true);
		}
	}

	private void cleanUpTest(String testName) {
		map.remove(testName);
		ContentManager.clearCounters();
	}
}
