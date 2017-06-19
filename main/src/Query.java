import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lain on 6/18/17.
 **/
class Query {
    Query(int region, int category) {
        String link = regions[region];
        link += "/search/";
        link += categories[category];
        this.link = link;
	    this.region = region;
    }

    void Populate() {
        try {
	        System.setProperty("https.agent", "");

            URL url = new URL(link);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setRequestProperty("User-Agent", Randomizer.getAgent());
            conn.connect();

            if (!parsePage(conn)) return;
	        Randomizer.randSleep();

            for (int i = 120; i <= 4800; i += 120) {
            	url = new URL(link + "?s=" + i);
	            conn = (HttpsURLConnection)url.openConnection();
	            conn.setRequestProperty("User-Agent", Randomizer.getAgent());
	            conn.connect();

				if (!parsePage(conn)) return;
	            Randomizer.randSleep();
            }

        } catch (java.lang.Throwable e) {
            System.out.println(e.getMessage());
            for (StackTraceElement s : e.getStackTrace())
            	System.out.println(s);
        }
    }

    private boolean parsePage(HttpsURLConnection conn) {
	    String line;
	    String tUrl = null;
	    String tName = null;
	    int tPrice;

	    boolean inListing = false;

	    try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    while ((line = reader.readLine()) != null) {
			    if (line.contains("result-price") && inListing) { //If a listing was found in the previous loop, grab the price
				    tPrice = Integer.parseInt(line.substring(line.indexOf("$") + 1, line.indexOf("</span>")));
				    results.add(new SearchResult(tName, tPrice, tUrl));
				    inListing = false;
			    }
			    if (line.contains("result-title")) { //Checks for a title
				    tUrl = regions[region] + line.substring(17, 37);
				    tName = line.substring(line.indexOf("result-title") + 21, line.indexOf("</a>"));
				    inListing = true;
			    }

			    if (line.contains("no result")) return false;
			    if (line.contains("an end is a beginning")) return false;
		    }
	    } catch (java.lang.Throwable e) {
		    System.out.println(e.getMessage());
		    for (StackTraceElement s : e.getStackTrace())
			    System.out.println(s);
	    }

	    return true;
    }

    ArrayList<SearchResult> getResults() {
        return results;
    }

    private String link;
    private int region;
    private ArrayList<SearchResult> results = new ArrayList<>();

    private static String[] regions = {
        "https://jerseyshore.craigslist.org",
        "https://raleigh.craigslist.org"
    };

    private static String[] categories = {
        "sys",
        "pta",
        "cto",
        "sss"
    };
}
