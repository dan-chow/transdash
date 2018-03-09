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
		String op = postData.getOp();
		String testName = postData.getTestName();
		if ("start".equals(op)) {
			prepareForTest(testName);
			logger.warn("start");
		} else if ("upload".equals(op)) {
			saveStatistic(testName, postData.getStatistic());
		} else if ("end".equals(op)) {
			writeToFile(testName);
			cleanUpTest(testName);
			logger.warn("end");
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
		CacheManager.getInstance().clear();
		map.put(testName, new ArrayList<>());
	}

	private void saveStatistic(String testName, Statistic statistic) throws IOException {
		map.get(testName).add(statistic);
	}

	private void writeToFile(String testName) throws IOException {
		List<Statistic> statistics = map.get(testName);

		StringBuilder avgVideoQuality = new StringBuilder("avgVideoQuality");
		StringBuilder avgQualityVariations = new StringBuilder("avgQualityVariations");
		StringBuilder stallFrequency = new StringBuilder("stallFrequency");
		StringBuilder avgStallDuration = new StringBuilder("avgStallDuration");
		for (Statistic statistic : statistics) {
			avgVideoQuality.append(" ").append(statistic.avgVideoQuality());
			avgQualityVariations.append(" ").append(statistic.avgQualityVariations());
			stallFrequency.append(" ").append(statistic.stallFrequency());
			avgStallDuration.append(" ").append(statistic.avgStallDuration());
		}

		File file = new File(testName);
		FileUtils.write(file, avgVideoQuality, "UTF-8", true);
		FileUtils.write(file, avgQualityVariations, "UTF-8", true);
		FileUtils.write(file, stallFrequency, "UTF-8", true);
		FileUtils.write(file, avgStallDuration, "UTF-8", true);
	}

	private void cleanUpTest(String testName) {
		map.remove(testName);
	}
}
