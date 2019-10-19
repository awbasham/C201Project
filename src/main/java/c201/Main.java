package c201;

import c201.analyze.Histogram;
import c201.analyze.OpenNLP;
import c201.sites.SputnikNews;
import c201.sites.VeteransToday;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        try {
            SputnikNews sputnikNews = new SputnikNews("Sputnik News", "www.sputniknews.com", "https://sputniknews.com/export/rss2/archive/index.xml");
            sputnikNews.fetchArticles();

            VeteransToday veteransToday = new VeteransToday("Veterans Today", "www.veteranstoday.com", "https://www.veteranstoday.com/feed/");
            veteransToday.fetchArticles();

            Utilities.articlesToJsonFile(veteransToday.getArticles(), veteransToday.getName().replaceAll(" ", ""));
            Utilities.articlesToJsonFile(sputnikNews.getArticles(), sputnikNews.getName().replaceAll(" ", ""));

            //Experimental code with OpenNLP and Histogram
            OpenNLP openNLP = new OpenNLP();
            Histogram histogram = new Histogram();

            for(Article article : sputnikNews.getArticles()) {
                ArrayList<String> arr = openNLP.getNounGroups(article.getDescription());
                for(String string : arr) {
                    histogram.add(string);
                }
            }

            System.out.println(histogram.sortAscending().toString());

        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
