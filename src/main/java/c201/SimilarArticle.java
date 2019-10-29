package c201;

import java.util.Date;

public class SimilarArticle extends Article {
    private double cosineCoeff;
    private Site fromSite;

    public SimilarArticle(String author, String title, Date datePublished, String description, String articleText, String url, double cosineCoeff, Site fromSite) {
        super(author, title, datePublished, description, articleText, url);
        this.cosineCoeff = cosineCoeff;
        this.fromSite = fromSite;
    }

    public SimilarArticle(Article article, double cosineCoeff, Site fromSite) {
        this(article.getAuthor(), article.getTitle(), article.getDatePublished(),
                article.getDescription(), article.getArticleText(), article.getUrl(), cosineCoeff, fromSite);
    }

    public double getCosineCoeff() {
        return cosineCoeff;
    }

    public Site getFromSite() {
        return fromSite;
    }
}
