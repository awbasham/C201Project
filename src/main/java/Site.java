import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;
import java.util.ArrayList;

abstract class Site {
    private String name;
    private String url;
    private String rssUrl;
    private ArrayList<Article> articles;

    public Site(String name, String url, String rssUrl) {
        this.name = name;
        this.url = url;
        this.rssUrl = rssUrl;
        articles = new ArrayList<>();
    }

    abstract public void fetchArticles();

    public SyndFeed getRssFeed() {
        try {
            SyndFeedInput input = new SyndFeedInput();
            return input.build(new XmlReader(new URL(getRssUrl())));
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
            System.exit(0);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }
}
