import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try {
            SputnikNews sputnikNews = new SputnikNews("Sputnik News", "www.sputniknews.com", "https://sputniknews.com/export/rss2/archive/index.xml");
            sputnikNews.fetchArticles();

            ArrayList<Article> articles = sputnikNews.getArticles();
            /*for(Article article: articles) {
                System.out.println(article.toString()+ "\n");
            }*/

            Utilities.articlesToJsonFile(articles, sputnikNews.getName().replaceAll(" ", ""));

        } catch (Exception e) {
            System.out.println("Error");
        }

        ArrayList<Article> test = Utilities.jsonToArticlesArray("SputnikNews");
        System.out.println(test.get(0).getTitle());
    }
}
