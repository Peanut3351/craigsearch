/**
 * Created by lain on 6/18/17.
 */
public class SearchResult {

    public SearchResult(String t, String d, String p, String l) {
        title = t;
        description = d;
        price = p;
        link = l;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String title;
    private String description;
    private String price;
    private String link;
}
