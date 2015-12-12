package dfgden.pxart.com.pxart.data;


public class User {

    private String id;
    private String creationTime;
    private String expirationTime;
    private Author user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Author getAuthor() {
        return user;
    }

    public void setAuthor(Author author) {
        this.user = author;
    }

}
