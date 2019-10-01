abstract class Site {
    private String name;
    private String url;

    public Site(String name) {
        this.name = name;
    }

    abstract public void fetchArticles();
}
