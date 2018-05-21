package chow.dan;

public class Chunk {
	public static int LEVELS = 5;

	private static int[] SIZE = { 800, 1600, 2400, 4800, 9600 };
	private static double[][] transcodeTime = { { 90.10135135135136 }, { 96.21621621621622, 171.35135135135135 },
			{ 115.22972972972972, 187.31081081081083, 292.72297297297295 },
			{ 146.52702702702702, 220.5337837837838, 326.4797297297298, 620.2364864864865 } };

	public int index;
	public int level;

	public static double getTranscodeTime(int srcLvl, int dstLvl) {
		return transcodeTime[srcLvl - 1][dstLvl - 1];
	}

	public Chunk(int idx, int lvl) {
		index = idx;
		level = lvl;
	}

	public String getName() {
		return index + "-" + level;
	}

	public int getSize() {
		return SIZE[level - 1];
	}
}
