package dfgden.pxart.com.pxart.customviews;import android.content.Context;import android.content.res.Resources;import android.graphics.AvoidXfermode;import android.graphics.Bitmap;import android.graphics.Canvas;import android.graphics.Color;import android.graphics.DashPathEffect;import android.graphics.Paint;import android.graphics.PorterDuff;import android.graphics.PorterDuffXfermode;import android.graphics.Rect;import android.util.AttributeSet;import android.util.DisplayMetrics;import android.util.Log;import android.view.GestureDetector;import android.view.MotionEvent;import android.view.ScaleGestureDetector;import android.view.View;import android.widget.Scroller;import java.sql.Array;import java.util.ArrayList;import java.util.Arrays;public class PaintView extends View {    private ArrayList<Point> pointBuffer;    private ArrayList<FillPoint> fillPointList;    private ArrayList<ArrayList<Point>> buffer = new ArrayList<>();    private int eventNumber = 0;    private final static int bufferSize = 11; //10    private ScaleGestureDetector mScaleDetector;    private float mScaleFactor = 4*1.f;    private static final float BASE_HEIGHT_CELL = 5f;    private static final float BASE_WIDTH_CELL = 5f;    private static final int COUNT_HEIGHT_CELL = 30;    private static final int COUNT_WIDTH_CELL = 30;    private static final int COUNT_FIELD_FRAGMENT_X = 3;    private static final int COUNT_FIELD_FRAGMENT_Y = 3;    private boolean isScrolling;    private Paint fieldPaint;    private Context context;    private Canvas mCanvas;    private Bitmap mBitmap;    private Paint mPaint;    private Rect rect;    private int countCellWidth;    private int countCellHeight;    private float width_cell;    private float height_cell;    private int color;    private boolean isFilling;    private boolean isDraw;    private int heightFieldOfPaint;    private int widthFieldOfPaint;    private int maxX;    private int maxY;    int countPaintFragmentX;    int countPaintFragmentY;    private int[][] baseColorArray;    private boolean paintFieldTrigger;    private final Scroller scroller;    private final GestureDetector gestureDetector;    public interface ColorTouchListener {        void getColor(int color);    }    private ColorTouchListener colorTouchListener;    public PaintView(Context context, AttributeSet attrs) {        super(context, attrs);        setBackgroundColor(Color.WHITE);        this.countCellWidth =COUNT_WIDTH_CELL;        this.countCellHeight = COUNT_HEIGHT_CELL;        this.countPaintFragmentX = COUNT_FIELD_FRAGMENT_X;        this.countPaintFragmentY = COUNT_FIELD_FRAGMENT_Y;        this.color = Color.RED;        this.fillPointList = new ArrayList<>();        this.context = context;        this.width_cell = BASE_WIDTH_CELL;        this.height_cell = BASE_HEIGHT_CELL;        initRect();        initPaint();        scroller = new Scroller(context);        gestureDetector = new GestureDetector(context, new MyGestureListener());        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());        getSizeFieldOfPaint();        initMainField();        initColorArray();    }    @Override    protected void onSizeChanged(int w, int h, int oldw, int oldh) {        super.onSizeChanged(w, h, oldw, oldh);    }    @Override    protected void onDraw(Canvas canvas) {        super.onDraw(canvas);        if (!paintFieldTrigger) {            createPaintSmallField();            paintFieldTrigger = true;        }        canvas.scale(mScaleFactor, mScaleFactor);        canvas.drawBitmap(mBitmap, 0, 0, new Paint(Paint.DITHER_FLAG));        createPaintBigField();    }    @Override    public boolean onTouchEvent(MotionEvent event) {        float x = (getScrollX() + event.getX() < 0)  ? 0 : getScrollX() + event.getX();        float y = (getScrollY() + event.getY() < 0)  ? 0 : getScrollY() + event.getY();            switch (event.getAction()) {                case MotionEvent.ACTION_DOWN:                    touch_start(x, y);                    break;                case MotionEvent.ACTION_MOVE:                    touch_move(x, y);                    break;                case MotionEvent.ACTION_UP:                    touch_up();                    break;            }        if (isScrolling) {            gestureDetector.onTouchEvent(event);            mScaleDetector.onTouchEvent(event);        }        return true;    }    @Override    public void computeScroll() {        if (scroller.computeScrollOffset()) {            int oldX = getScrollX();            int oldY = getScrollY();            int x = scroller.getCurrX();            int y = scroller.getCurrY();            scrollTo(x, y);            if (oldX != getScrollX() || oldY != getScrollY()) {                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);            }            postInvalidate();        }    }    private void initMainField() {        this.maxX = this.countCellWidth * (int) convertDpToPixel(width_cell) * countPaintFragmentX;        this.maxY = this.countCellHeight * (int) convertDpToPixel(height_cell) * countPaintFragmentY;        mBitmap = Bitmap.createBitmap(maxX, maxY, Bitmap.Config.ARGB_8888);        mCanvas = new Canvas(mBitmap);    }    private void getSizeFieldOfPaint() {        this.heightFieldOfPaint = (int) (this.countCellHeight * convertDpToPixel(height_cell));        this.widthFieldOfPaint = (int) (this.countCellWidth * convertDpToPixel(width_cell));    }    private void initPaint() {        this.mPaint = new Paint();        this.mPaint.setStyle(Paint.Style.FILL);        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));        this.fieldPaint = new Paint();        this.fieldPaint.setColor(Color.BLACK);        this.fieldPaint.setStyle(Paint.Style.STROKE);    }    private void initColorArray() {        this.baseColorArray = new int[countCellWidth][countCellHeight];        for (int i = 0; i < baseColorArray.length; i++) {            Arrays.fill(baseColorArray[i], Color.WHITE);        }    }    private void initRect() {        rect = new Rect(0, 0, (int) convertDpToPixel(width_cell) - 1, (int) convertDpToPixel(height_cell) - 1);    }    public void setColor(int color) {        this.color = color;    }    private int getTouchBasePositionX(float x) {        int baseX = (int) (x / mScaleFactor) % widthFieldOfPaint;        int cellX = baseX / (widthFieldOfPaint / countCellWidth);        return cellX;    }    private int getTouchBasePositionY(float y) {        int baseY = (int) (y / mScaleFactor) % heightFieldOfPaint;        int cellY = baseY / (heightFieldOfPaint / countCellHeight);        return cellY;    }    private void drawImage(float x, float y) {        int positionInTableX = getTouchBasePositionX(x);        int positionInTableY = getTouchBasePositionY(y);        if (colorTouchListener != null) {            colorTouchListener.getColor(baseColorArray[positionInTableX][positionInTableY]);        }        if (isDraw && baseColorArray[positionInTableX][positionInTableY] != color) {            if (isFilling) {                int oldColor = baseColorArray[positionInTableX][positionInTableY];                fillCells(positionInTableX, positionInTableY, oldColor);                for (FillPoint pointList : fillPointList) {                    addToPointBuffer(pointList.getX(), pointList.getY(), oldColor, color);                    sketchCell(pointList.getX(), pointList.getY(), color);                }                fillPointList.clear();            } else {                addToPointBuffer(positionInTableX, positionInTableY, baseColorArray[positionInTableX][positionInTableY], color);                sketchCell(positionInTableX, positionInTableY, color);            }        }    }    private void sketchCell(int cellX, int cellY, int color) {        mPaint.setColor(color);        baseColorArray[cellX][cellY] = color;        for (int w = 0; w < countPaintFragmentX; w++) {            for (int h = 0; h < countPaintFragmentY; h++) {                int offsetX = cellX * (widthFieldOfPaint / countCellWidth) + w * widthFieldOfPaint +1;                int offsetY = cellY * (heightFieldOfPaint / countCellHeight) + h * heightFieldOfPaint+1 ;                if (offsetX < maxX && offsetY < maxY) {                    rect.offsetTo(offsetX, offsetY);                    mCanvas.drawRect(rect, mPaint);                    invalidate();                }            }        }    }    private void createPaintSmallField() {        fieldPaint.setStrokeWidth(1f);        for (int x = 0; x <= countCellWidth * countPaintFragmentX; x++)            mCanvas.drawLine((float) x * widthFieldOfPaint / countCellWidth, 0, (float) x * widthFieldOfPaint / countCellWidth, maxY, fieldPaint);        for (int y = 0; y <= countCellHeight * countPaintFragmentY; y++)            mCanvas.drawLine(0, (float) y * heightFieldOfPaint / countCellHeight, maxX, (float) y * heightFieldOfPaint / countCellHeight, fieldPaint);    }    private void createPaintBigField() {        fieldPaint.setStrokeWidth(2f);        for (int x = 0; x <= countPaintFragmentX; x++)            mCanvas.drawLine((float) x * widthFieldOfPaint, 0, (float) x * widthFieldOfPaint, maxY, fieldPaint);        for (int y = 0; y <= countPaintFragmentY; y++)            mCanvas.drawLine(0, (float) y * heightFieldOfPaint, maxX, (float) y * heightFieldOfPaint, fieldPaint);    }    private void touch_start(float x, float y) {        createNewPointBuffer();        drawImage(x, y);    }    private void touch_move(float x, float y) {        drawImage(x, y);    }    private void touch_up() {        if (isDraw) {            addBuffer();        }    }    private float convertDpToPixel(float dp) {        Resources resources = context.getResources();        DisplayMetrics metrics = resources.getDisplayMetrics();        return dp * (metrics.densityDpi / 160f);    }    private void createNewPointBuffer() {        pointBuffer = new ArrayList<>();    }    private void addToPointBuffer(int x, int y, int beforeColor, int currentColor) {        pointBuffer.add(new Point(x, y, beforeColor, currentColor));    }    private void addBuffer() {        buffer.add(eventNumber, pointBuffer);        eventNumber = eventNumber >= bufferSize ? bufferSize : eventNumber + 1;        if (eventNumber <= buffer.size()) {            for (int i = eventNumber; i < buffer.size(); i++) {                buffer.remove(i);            }        }        if (buffer.size() >= bufferSize) {            eventNumber = eventNumber - 1;            buffer.remove(0);        }    }    private void fillCells(int x, int y, int col) {        if (x >= 0 && y >= 0 && x < countCellWidth && y < countCellHeight && baseColorArray[x][y] == col) {            baseColorArray[x][y] = color;            fillPointList.add(new FillPoint(x, y));            fillCells(x + 1, y, col);            fillCells(x - 1, y, col);            fillCells(x, y + 1, col);            fillCells(x, y - 1, col);        }    }    public int getColor() {        return mPaint.getColor();    }    public void setIsDraw(boolean isDraw) {        this.isDraw = isDraw;    }    public void setIsScrolling(boolean isScrolling) {        this.isScrolling = isScrolling;    }    public void setProperty(int countCellWidth, int countCellHeight, int countPaintFragmentX, int countPaintFragmentY) {        this.countCellWidth = countCellWidth;        this.countCellHeight = countCellHeight;        this.countPaintFragmentX = countPaintFragmentX;        this.countPaintFragmentY = countPaintFragmentY;        this.mBitmap = null;        this.mCanvas = null;        getSizeFieldOfPaint();        initMainField();        initColorArray();        buffer.clear();        this.eventNumber = 0;        paintFieldTrigger = false;        invalidate();    }    public int getCountCellWidth() {        return countCellWidth;    }    public int getCountCellHeight() {        return countCellHeight;    }    public int getCountPaintFragmentX() {        return countPaintFragmentX;    }    public int getCountPaintFragmentY() {        return countPaintFragmentY;    }    public void loadPattern(int[]  array,int countCellWidth, int countCellHeight, int countPaintFragmentX, int countPaintFragmentY){        this.countCellWidth = countCellWidth;        this.countCellHeight = countCellHeight;        this.countPaintFragmentX = countPaintFragmentX;        this.countPaintFragmentY = countPaintFragmentY;        this.mBitmap = null;        this.mCanvas = null;        getSizeFieldOfPaint();        initMainField();        initColorArray();        paintFieldTrigger = false;        for (int i = 0; i < countCellWidth; i++) {            for (int j = 0; j < countCellHeight; j++) {                sketchCell(j,i,array[i*countCellHeight+j]);            }        }    }    public void clearField(){        initMainField();        initColorArray();        buffer.clear();        this.eventNumber = 0;        paintFieldTrigger = false;        invalidate();    }    public int[] getColorArray(){        int[] bitmapArray = new int[countCellWidth*countCellHeight];        for (int i = 0; i < countCellHeight ; i++) {            for (int j = 0; j < countCellWidth; j++) {                bitmapArray[j+ i*countCellWidth] = baseColorArray[j][i];            }        }        return bitmapArray;    }    public Bitmap getBitmap(){        return Bitmap.createBitmap(getColorArray(),countCellWidth,countCellHeight,Bitmap.Config.ARGB_8888);    }    public void setBitmap(Bitmap bitmap){        int h = bitmap.getHeight();        int w = bitmap.getWidth();        int[] bitmapArray = new int[h*w];        for (int i = 0; i < h; i++) {            for (int j = 0; j < w; j++) {                bitmapArray[i*w+j] = bitmap.getPixel(j,i);            }        }        loadPattern(bitmapArray,h,w,2,2);    }    public void setOnColorTouchListener(ColorTouchListener colorTouchListener) {        this.colorTouchListener = colorTouchListener;    }    public void bufferBack() {        if (buffer.size() > 0) {            eventNumber = eventNumber <= 0 ? 0 : eventNumber - 1;            for (int i = 0; i < buffer.get(eventNumber).size(); i++) {                sketchCell(buffer.get(eventNumber).get(i).getX(), buffer.get(eventNumber).get(i).getY(), buffer.get(eventNumber).get(i).getBeforeColor());            }        }    }    public void bufferNext() {        if (buffer.size() > 0 && eventNumber < buffer.size()) {            for (int i = 0; i < buffer.get(eventNumber).size(); i++) {                sketchCell(buffer.get(eventNumber).get(i).getX(), buffer.get(eventNumber).get(i).getY(), buffer.get(eventNumber).get(i).getCurrentColor());            }            eventNumber = eventNumber >= buffer.size() ? buffer.size() - 1 : eventNumber + 1;        }    }    public void setIsFilling(boolean isFilling) {        this.isFilling = isFilling;    }    private class FillPoint {        private int x, y;        public FillPoint(int x, int y) {            this.x = x;            this.y = y;        }        public int getX() {            return x;        }        public int getY() {            return y;        }    }    private class Point {        private int x, y, beforeColor, currentColor;        public Point(int x, int y, int beforeColor, int currentColor) {            this.x = x;            this.y = y;            this.beforeColor = beforeColor;            this.currentColor = currentColor;        }        public int getX() {            return x;        }        public int getY() {            return y;        }        public int getBeforeColor() {            return beforeColor;        }        public int getCurrentColor() {            return currentColor;        }    }    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {        @Override        public boolean onScroll(MotionEvent e1, MotionEvent e2,                                float distanceX, float distanceY) {            scrollBy((int) distanceX, (int) distanceY);            return true;        }        @Override        public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {            scroller.fling(getScrollX(), getScrollY(),                    -(int) vX, -(int) vY, 0, (int) (maxX * mScaleFactor) - getWidth(), 0, (int) (maxY * mScaleFactor) - getHeight());            return true;        }        @Override        public boolean onDown(MotionEvent e) {            if (!scroller.isFinished()) {                scroller.forceFinished(true);            }            return true;        }    }    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {        @Override        public boolean onScale(ScaleGestureDetector detector) {            mScaleFactor *= detector.getScaleFactor();            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 8.0f));            return true;        }    }}