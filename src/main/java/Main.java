import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try {
            SputnikNews sputnikNews = new SputnikNews("Sputnik News", "www.sputniknews.com", "https://sputniknews.com/export/rss2/archive/index.xml");
            sputnikNews.fetchArticles();

            ArrayList<Article> articles = sputnikNews.getArticles();
            for(Article article: articles) {
                System.out.println(article.toString()+ "\n");
            }

            System.out.println(articles.get(0).getArticleText());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
