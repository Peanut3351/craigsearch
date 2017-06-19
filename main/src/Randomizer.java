import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by lain on 6/19/17.
 **/
class Randomizer {
	static String getAgent() {
		int agent = (int)(Math.random() * 4);
		System.out.println("User Agent " + agent);
		return agents[agent];
	}

	static void randSleep() {
		Random rand = new Random();
		long time = rand.nextInt(4000) + 100;
		System.out.println("Waiting " + time + " ms...");
		try {
			TimeUnit.MILLISECONDS.sleep(time);
		} catch(java.lang.Throwable e) {
			System.out.println(e.getMessage());
			for (StackTraceElement s : e.getStackTrace())
				System.out.println(s);
		}
	}

	private static String[] agents = {
		"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36",
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
		"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0",
		"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36 OPR/38.0.2220.41"
	};
}
