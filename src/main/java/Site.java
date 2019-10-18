import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public SyndFeed getAggregatedRssFeed(String[] urls) {
        try {
            SyndFeed feed = new SyndFeedImpl();
            List entries = new ArrayList();
            feed.setEntries(entries);

            for (String url : urls) {
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed inFeed = input.build(new XmlReader(new URL(url)));

                entries.addAll(inFeed.getEntries());
            }

            return feed;
        } catch(Exception e) {
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
