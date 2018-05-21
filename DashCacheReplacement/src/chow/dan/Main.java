package chow.dan;

public class Main {

	public static void main(String[] args) {

		String testName = "dash";
		int capacity = 1000000;

		int req = 0, hit = 0, trans = 0, miss = 0, down = 0;
		Cache cache = null;

		if ("lfu".equals(testName.toLowerCase())) {
			cache = new LFUCache(capacity);
		} else if ("dash".equals(testName.toLowerCase())) {
			cache = new DASHCache(capacity);
		}

		for (int n = 0; n < 1000; n++) {
			int lvl = RandomBitrate.getRandomBitrate();
			for (int idx = 1; idx <= 100; idx++) {
				Chunk chunk = new Chunk(idx, lvl);
				req++;

				if (cache.get(chunk.getName()) != null) {
					hit++;
				} else {
					boolean higherFound = false;
					for (int higherLvl = lvl + 1; higherLvl <= Chunk.LEVELS; higherLvl++) {

						Chunk higher = cache.get(idx + "-" + higherLvl);
						if (higher != null) {

							higherFound = true;
							trans++;
							cache.set(chunk.getName(), chunk);
							break;
						}
					}
					if (!higherFound) {
						miss++;
						down += chunk.getSize();
						cache.set(chunk.getName(), chunk);
					}
				}
			}
		}

		System.out.println(req);
		System.out.println(hit);
		System.out.println(trans);
		System.out.println(miss);
		System.out.println(down);
		System.out.println();
	}
}
