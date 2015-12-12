package dfgden.pxart.com.pxart.internet;


public enum URLList {

    PAGE_BEST_PATTERN("http://30pxart.com/api/Patterns/Best"),
    PAGE_MOSTCOMMENTED_PATTERN("http://30pxart.com/api/Patterns/MostCommented"),
    PAGE_RECENTUNREGISTERED_PATTERN("http://30pxart.com/api/Patterns/RecentUnregistered"),
    PAGE_BESTUNREGISTERED_PATTERN("http://30pxart.com/api/Patterns/BestUnregistered"),
    PAGE_INDEX_PATTERN("http://30pxart.com/api/Patterns/Index"),
    PAGE_NEW_PATTERN("http://30pxart.com/api/patterns/"),
    PAGE_USER("http://30pxart.com/api/users/current"),
    PAGE_BASE64("http://30pxart.com/api/patterns/base64"),
    PAGE_FOLLOWER("http://30pxart.com/api/users/"),
    PAGE_PROVIDERS_URL("http://30pxart.com/api/accounts/loginurls"),
    PAGE_USER_ACCOUNT("http://30pxart.com/api/accounts/token");


    public static final int DEFAULT_SKIP  = 42;
    private String url;

    public String getUrl() {
        return url;
    }

    URLList(String url) {
        this.url = url;
    }
}
