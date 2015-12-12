package dfgden.pxart.com.pxart.data;


import java.io.Serializable;

public class Following implements Serializable{

    private Subscriber subscriber;
    private Publisher publisher;
    private String creationTime;
    public Following() {
    }

    public Following(Subscriber subscriber, Publisher publisher, String creationTime) {
        this.subscriber = subscriber;
        this.publisher = publisher;
        this.creationTime = creationTime;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
