package chow.dan;

import java.util.Random;

public class RandomBitrate {
	private static Random random = new Random(10086);

	public static int getRandomBitrate() {
		// int r = random.nextInt(20);
		// if (r < 1)
		// return 1;
		// if (r < 5)
		// return 2;
		// if (r < 15)
		// return 3;
		// if (r < 19)
		// return 4;
		// return 5;
		int r = random.nextInt(5);
		return r + 1;
	}

}
