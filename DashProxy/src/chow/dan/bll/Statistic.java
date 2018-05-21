package chow.dan.bll;

import java.util.List;

import com.google.gson.Gson;

public class Statistic {
	private List<Integer> qualities;
	private List<Double> stalls;
	public int totalRequest;
	public int hitRaw;
	public int hitTrans;

	public static Statistic fromJsonString(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, Statistic.class);
	}

	public double avgVideoQuality() {
		if (qualities.size() < 1) {
			return 0.0;
		}

		double sum = 0.0;
		for (int i : qualities) {
			sum += i;
		}

		return sum / qualities.size();
	}

	public double avgQualityVariations() {
		if (qualities.size() <= 1) {
			return 0.0;
		}

		double sum = 0.0;
		for (int i = 1; i < qualities.size(); i++) {
			sum += Math.abs(qualities.get(i) - qualities.get(i - 1));
		}

		return sum / (qualities.size() - 1);
	}

	public int stallFrequency() {
		return stalls.size();
	}

	public double avgStallDuration() {
		if (stalls.size() < 1) {
			return 0.0;
		}

		double sum = 0.0;
		for (double i : stalls) {
			sum += i;
		}
		return sum / stalls.size();
	}
}