package c201.sites;

import c201.Article;
import c201.Site;
import c201.Utilities;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FoxNews extends Site {
    public FoxNews(String name, String url, String rssUrl) {
        super(name, url, rssUrl);
    }

    @Override
    public void fetchArticles() {
        String[] urls = new String[]{"http://feeds.foxnews.com/foxnews/opinion", "http://feeds.foxnews.com/foxnews/politics",
                "http://feeds.foxnews.com/foxnews/science", "http://feeds.foxnews.com/foxnews/section/lifestyle", "http://feeds.foxnews.com/foxnews/national", "http://feeds.foxnews.com/foxnews/world"};

        SyndFeed feed = getAggregatedRssFeed(urls);

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
                doc.select("img").remove();
                articleText = doc.selectFirst(".article-body").text();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            if(articleText.length() <= 10) {
                continue;
            }

            getArticles().add(new Article(entry.getAuthor(), entry.getTitle(), entry.getPublishedDate(),
                    Jsoup.parse(entry.getDescription().getValue()).text(), articleText, entry.getLink()));
            articleCounter++;
        }
        Utilities.articlesToJsonFile(getArticles(), getName().replaceAll(" ", ""));
    }
}
