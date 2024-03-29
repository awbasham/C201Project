package c201.sites;

import c201.Article;
import c201.Site;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Tass extends Site {

    public Tass(String name, String url, String rssUrl) {
        super(name, url, rssUrl);
    }

    @Override
    public void fetchArticles() {
        SyndFeed feed = getRssFeed();

        if(isLocalUpToDate(feed)) {
            return;
        }

        int articleCounter = 1;

        System.out.println("Fetching articles...");
        System.out.println("Number of articles from " + getName() + ": " + feed.getEntries().size());

        for(SyndEntry entry: feed.getEntries()) {
            System.out.println("Fetching article (" + articleCounter + "/" + feed.getEntries().size() + ")");
            String articleText = "";

            try {
                Document doc = Jsoup.connect(entry.getLink()).get();
                articleText = doc.selectFirst(".text-content").text();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            if(articleText.length() <= 10) {
                continue;
            }

            if(entry.getDescription() == null) {
                getArticles().add(new Article(entry.getAuthor(), entry.getTitle(), entry.getPublishedDate(),
                        "", articleText, entry.getLink()));
            } else {
                getArticles().add(new Article(entry.getAuthor(), entry.getTitle(), entry.getPublishedDate(),
                        Jsoup.parse(entry.getDescription().getValue()).text(), articleText, entry.getLink()));
            }

            articleCounter++;
        }
    }
}
