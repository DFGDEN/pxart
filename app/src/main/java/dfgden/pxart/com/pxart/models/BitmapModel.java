package dfgden.pxart.com.pxart.models;

import java.io.Serializable;


public class BitmapModel implements Serializable{

    private int fragmentX;
    private int fragmentY;
    private int height;
    private int width;
    private int[] bitmapArray;

    public int getFragmentX() {
        return fragmentX;
    }

    public void setFragmentX(int fragmentX) {
        this.fragmentX = fragmentX;
    }

    public int getFragmentY() {
        return fragmentY;
    }

    public void setFragmentY(int fragmentY) {
        this.fragmentY = fragmentY;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[] getBitmapArray() {
        return bitmapArray;
    }

    public void setBitmapArray(int[] bitmapArray) {
        this.bitmapArray = bitmapArray;
    }
}
