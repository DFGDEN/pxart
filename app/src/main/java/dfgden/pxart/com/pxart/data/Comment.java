package dfgden.pxart.com.pxart.data;


public class Comment {

    private String id;
    private Author author;
    private String patternId;
    private String text;
    private String creationTime;

    public Comment() {
    }

    public Comment(String id, Author author, String patternId, String text, String creationTime) {
        this.id = id;
        this.author = author;
        this.patternId = patternId;
        this.text = text;
        this.creationTime = creationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

}
