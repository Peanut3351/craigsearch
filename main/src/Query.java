/**
 * Created by lain on 6/18/17.
 */
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class Query {
    Query(String link) {
        this.link = link;
    }

    public void Populate(int region) {
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        System.setProperty("https.agent", "");
        HttpsURLConnection conn;

        String line;
        String tUrl = null; //Stores URL
        String tName = null; //Stores the name of the listing
        String tPrice = null; //Stores the price of the listing

        try {
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", main.getAgent());
            conn.connect();
        } catch (IOException e) {
            throw new RuntimeException(e); //Fuck Java
        }

        boolean inListing = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            while ((line = reader.readLine()) != null) {
                if (line.contains(("result-price")) && inListing == true) { //If a listing was found in the previous loop, grab the price
                    tPrice = line.substring(line.indexOf("$"), line.indexOf("</span>"));
                    results.add(new SearchResult(tName, "<Placeholder>", tPrice, tUrl));
                }
                if (line.contains("result-title")) { //Checks for a title
                    /*
                    Before you say anything about this next block, or even think about touching it
                    Ask yourself, is it worth breaking everything?
                    TL;DR, magic numbers  (╯°□°）╯︵ ┻━┻
                     */
                    if (region == 1) {
                        tUrl = "https://jerseyshore.craigslist.org" + line.substring(17, 37);
                        tName = line.substring(line.indexOf("result-title") + 21, line.indexOf("</a>"));
                        inListing = true;
                    }
                    if (region == 2) {
                        tUrl = "https://raleigh.craigslist.org" + line.substring(17, 37);
                        tName = line.substring(line.indexOf("result-title") + 21, line.indexOf("</a>"));
                        inListing = true;
                    }
                }
                if (line.contains("no result")) {
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e); //Fuck Java
        }
    }

    public ArrayList<SearchResult> getResults() {
        ArrayList<SearchResult> res = new ArrayList<>();
        for (SearchResult s : results)
            res.add(s);
        return res;
    }

    public ArrayList<SearchResult> getResultTitle(String str) {
        ArrayList<SearchResult> res = new ArrayList<>();
        for (SearchResult s : results)
            if (s.getTitle().contains(str))
                res.add(s);
        return res;
    }

    public ArrayList<SearchResult> getResultTitle(ArrayList<String> strings) {
        ArrayList<SearchResult> res = new ArrayList<>();
        for (String str : strings)
            for (SearchResult s : results)
                if (s.getTitle().contains(str))
                    res.add(s);
        return res;
    }

    public ArrayList<SearchResult> getResultPrice(int price) {
        ArrayList<SearchResult> res = new ArrayList<>();
        for (SearchResult s : results)
            if (Integer.parseInt(s.getPrice().substring(s.getPrice().indexOf('$') + 1)) <= price)
                res.add(s);
        return res;
    }

    private String link;
    private ArrayList<SearchResult> results = new ArrayList<>();
}
