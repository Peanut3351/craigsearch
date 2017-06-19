/**
 * Created by lain on 6/18/17.
 **/
class SearchResult {

    SearchResult(String t, int p, String l) {
        title = t;
        price = p;
        link = l;
    }

    String getTitle() {
        return title;
    }
    int getPrice() { return price; }
    String getLink() {
        return link;
    }

    private String title;
    private int price;
    private String link;
}
