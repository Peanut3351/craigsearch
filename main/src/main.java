import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by Connor on 6/16/2017.
 */

public class main {

	static String link = null;
	static String templink = null;
	static ArrayList<String> list = new ArrayList<String>(1);
	static URL url = null;
	static int page = 0;

	public static void main(String args[]) {
		JFrame window = new JFrame("Craigslist Crawler");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int region = 0;
		Scanner x = new Scanner(System.in);
		System.out.print("First search term: ");
		String a = x.nextLine();
		System.out.print("Second search term: ");
		String b = x.nextLine();
		System.out.print("Input region:\n(1) Jersey Shore\n(2) Raleigh\n");
		int c = x.nextInt();
		System.out.print("Input category:\n(1) Computer Systems\n(2) Auto Parts\n(3) Cars by owner\n(4) All\n");
		int d = x.nextInt();

        if (c == 1) {
            link = "https://jerseyshore.craigslist.org/search/";
            region = 1;
        }
        if (c == 2) {
            link = "https://raleigh.craigslist.org/search/";
            region = 2;
        }

		if (d == 1) {
			link += "sys";
		}
		if (d == 2) {
			link += "pta";
		}
		if (d == 3) {
			link += "cto";
		}
		if (d == 4) {
			link += "sss";
		}


		//templink = link;
		//crawl(a, b, region);
        Query query = new Query(link);
        query.Populate(region);

        ArrayList<SearchResult> results = new ArrayList<>();
        results = query.getResults();

		for (SearchResult s : results)
			System.out.println(s.getTitle() + " - " + s.getPrice() + ":" + s.getLink());

		x.close();
	}


    public static void crawl(String a, String b, int region) {
		if (region != 1 && region != 2) {
			System.out.println("You did not select a valid region. Exiting.");
			return;
		}
		getLinkByTitle(a, b, region);
		FileWriter scribe = null;
		try {
			scribe = new FileWriter("results.txt");
		} catch (IOException e) {
		}
		try {
			for (String str : list) {
				scribe.write(str);
				scribe.write("\r\n");
			}
		} catch (IOException e) {
		}
		try {
			scribe.close();
		} catch (IOException e3) {
		}
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i));
	}

	public static void getResults(int region) {

    }

	public static void getLinkByTitle(String a, String b, int region) {
		try {
			url = new URL(templink);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		System.setProperty("https.agent", "");
		HttpsURLConnection c;
		String line;
		String tUrl = null; //Stores URL
		String tName = null; //Stores the name of the listing
		String tPrice = null; //Stores the price of the listing
		int listing = 0;
		try {
			c = (HttpsURLConnection) url.openConnection();
			c.setRequestProperty("User-Agent", getAgent());
			c.connect();
		} catch (IOException e) {
			throw new RuntimeException(e); //Fuck Java
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"))) {
			while ((line = reader.readLine()) != null) {
				if (line.contains(("result-price")) && listing == 1) { //If a listing was found in the previous loop, grab the price
					tPrice = line.substring(line.indexOf("$"), line.indexOf("</span>"));
					list.add(tUrl + " - " + tName + " - " + tPrice); //Create the entry in the ArrayList with the listing
					listing = 0;
				}
				if (line.contains("result-title")) { //Checks for a title
					if (line.contains(a) || line.contains(b)) { //Checks for keywords in the title
						if (region == 1) {
							tUrl = "https://jerseyshore.craigslist.org" + line.substring(17, 37);
							tName = line.substring(line.indexOf("result-title") + 21, line.indexOf("</a>"));
							listing = 1;
						}
						if (region == 2) {
							tUrl = "https://raleigh.craigslist.org" + line.substring(17, 37);
							tName = line.substring(line.indexOf("result-title") + 21, line.indexOf("</a>"));
							listing = 1;
						}
					}
				}
				if (line.contains("no result")) {
					return;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e); //Fuck Java
		}
		page += 120;
		templink = link + "?s=" + page;
		getLinkByTitle(a, b, region);
	}

	public static void getLinkByDesc(String a, String b, int region) {
		try {
			url = new URL(templink);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		System.setProperty("https.agent", "");
		HttpsURLConnection c;
		String line;
		String tempUrl = null;
		String tUrl = null; //Stores URL
		String tName = null; //Stores the name of the listing
		String tPrice = null; //Stores the price of the listing
		int listing = 0;
		try {
			c = (HttpsURLConnection) url.openConnection();
			c.setRequestProperty("User-Agent", getAgent());
			c.connect();
		} catch (IOException e) {
			throw new RuntimeException(e); //Fuck Java
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"))) {
			while ((line = reader.readLine()) != null) {
				if (line.contains("result-title")) { //Checks for a title
					if (region == 1) {
						tempUrl = "https://jerseyshore.craigslist.org" + line.substring(17, 37);
						break;
					}
					if (region == 2) {
						tempUrl = "https://raleigh.craigslist.org" + line.substring(17, 37);
						break;
					}
				}
				if (line.contains("no result")) {
					return;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e); //Fuck Java
		}
		URL url2;
		try {
			url2 = new URL(tempUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		HttpsURLConnection d;
		try {
			d = (HttpsURLConnection) url2.openConnection();
			d.setRequestProperty("User-Agent", getAgent());
			d.connect();
		} catch (IOException e) {
			throw new RuntimeException(e); //Fuck Java
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(d.getInputStream(), "UTF-8"))) {
			while ((line = reader.readLine()) != null) {
			}
			if (line.contains("postingbody")) { //Finds the body of the post
				BufferedReader postreader = new BufferedReader(new InputStreamReader(d.getInputStream(), "UTF-8"));
				while ((line = postreader.readLine()) != null) {
				}
			}
			if (line.contains("no result")) {
				return;
			}
		} catch (IOException e) {
			throw new RuntimeException(e); //Fuck Java
		}
		page += 120;
		templink = link + "?s=" + page;
		getLinkByTitle(a, b, region);
	}

	public static String getAgent() {
		Random rand = new Random();
		long randomtime = rand.nextInt(4000) + 100;
		try {
			TimeUnit.MILLISECONDS.sleep(randomtime);
			System.out.println("Waited " + randomtime + " milliseconds");
		} catch (InterruptedException e) {
			e.printStackTrace();  //Automatically fuck Java (thanks IntelliJ)
		}
		int agent = (int) (Math.random() * 4 + 1);
		if (agent == 1) {
			System.out.println("User agent 1");
			return "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
		}
		if (agent == 2) {
			System.out.println("User agent 2");
			return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
		}
		if (agent == 3) {
			System.out.println("User agent 3");
			return "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0";
		} else {
			System.out.println("User agent 4");
			return "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36 OPR/38.0.2220.41";
		}
	}
}

