package dfgden.pxart.com.pxart.interfaces;


public interface ProgressUpdateDataListener {
    void startUpdate();
    void stopUpdate();
    void crash(String text);
}
