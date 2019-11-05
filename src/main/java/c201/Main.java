package c201;

import c201.analyze.Algorithms;
import c201.sites.FoxNews;
import c201.sites.SputnikNews;
import c201.sites.VeteransToday;
import c201.sites.Tass;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            FoxNews foxNews = new FoxNews("Fox News", "www.foxnews.com", "https://feeds.foxnews.com/foxnews/latest");
            foxNews.fetchArticles();

            SputnikNews sputnikNews = new SputnikNews("Sputnik News", "www.sputniknews.com", "https://sputniknews.com/export/rss2/archive/index.xml");
            sputnikNews.fetchArticles();

            VeteransToday veteransToday = new VeteransToday("Veterans Today", "www.veteranstoday.com", "https://www.veteranstoday.com/feed/");
            veteransToday.fetchArticles();

            Tass tass = new Tass("Tass News", "www.tass.com", "http://tass.com/rss/v2.xml");
            tass.fetchArticles();

            ArrayList<Site> sites = new ArrayList<>();
            sites.add(sputnikNews);
            sites.add(veteransToday);
            sites.add(foxNews);
            sites.add(tass);

            Map<Article, ArrayList<SimilarArticle>> map = Algorithms.lsaCosineSimilarity(sites, 0.6, 50, true, true);
            List<Map.Entry<Article, ArrayList<SimilarArticle>>> list = new LinkedList<>(map.entrySet());
            list.sort(Comparator.comparingInt(t -> t.getValue().size()));

            /*for(int i = 0; i < list.size(); i++) {
                for(int j = 0; j < list.get(i).getValue().size(); j++) {
                    if(list.get(i).getValue().get(j).getFromSite().equals(foxNews)) {
                        System.out.println(list.get(i).getValue().get(j).getTitle());
                        System.out.println(list.get(i).getValue().get(j).getCosineCoeff());
                        System.out.println(list.get(i).getKey().getTitle());
                        System.out.println(list.get(i).getKey().getUrl());
                        System.out.println();
                    }
                }
            }*/

            System.out.println(map.size());
            for(int i = 0; i < list.get(list.size() - 1).getValue().size(); i++) {
                System.out.println(list.get(list.size() - 1).getValue().get(i).getFromSite().getName());
                System.out.println(list.get(list.size() - 1).getValue().get(i).getTitle());
                System.out.println(list.get(list.size() - 1).getValue().get(i).getCosineCoeff());
                System.out.println();
            }
            System.out.println(list.get(list.size() - 1).getKey().getTitle());
            System.out.println(list.get(list.size() - 1).getKey().getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
