import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SputnikNews extends Site {

    public SputnikNews(String name, String url, String rssUrl) {
        super(name, url, rssUrl);
    }

    @Override
    public void fetchArticles() {
        SyndFeed feed = getRssFeed();
        int articleCounter = 1;

        System.out.println("Fetching articles...");
        System.out.println("Number of articles from " + getName() + ": " + feed.getEntries().size());

        for(SyndEntry entry: feed.getEntries()) {
            System.out.println("Fetching article #" + articleCounter);
            String articleText = "";

            try {
                Document doc = Jsoup.connect(entry.getLink()).get();
                articleText = doc.selectFirst(".b-article__text").text();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            getArticles().add(new Article(entry.getAuthor(), entry.getTitle(), entry.getPublishedDate(),
                    entry.getDescription().getValue(), articleText, entry.getLink()));
            articleCounter++;
        }
    }
}
