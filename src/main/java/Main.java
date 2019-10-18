
public class Main {

    public static void main(String[] args) {
        try {
            SputnikNews sputnikNews = new SputnikNews("Sputnik News", "www.sputniknews.com", "https://sputniknews.com/export/rss2/archive/index.xml");
            sputnikNews.fetchArticles();

            VeteransToday veteransToday = new VeteransToday("Veterans Today", "www.veteranstoday.com", "https://www.veteranstoday.com/feed/");
            veteransToday.fetchArticles();

            Utilities.articlesToJsonFile(veteransToday.getArticles(), veteransToday.getName().replaceAll(" ", ""));
            Utilities.articlesToJsonFile(sputnikNews.getArticles(), sputnikNews.getName().replaceAll(" ", ""));

        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
