package c201;

import java.util.Date;

public class Article {
    private String author;
    private Date datePublished;
    private String title;
    private String description;
    private String articleText;
    private String url;

    public Article() {
        this("", "", null, "", "", "");
    }

    public Article(String author, String title, Date datePublished, String description, String articleText, String url) {
        this.author = author;
        this.title = title;
        this.datePublished = datePublished;
        this.description = description;
        this.articleText = articleText;
        this.url = url;
    }

    @Override
    public String toString() {
        return title + "\n" + author + "\n" + datePublished + "\n" + description + "\n" + url;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getArticleText() {
        return articleText;
    }

    public String getUrl() {
        return url;
    }
}
