package dfgden.pxart.com.pxart.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import dfgden.pxart.com.pxart.data.Pattern;
import dfgden.pxart.com.pxart.data.User;
import dfgden.pxart.com.pxart.models.BitmapModel;

public class PreferenceHelper {

    public interface OnTokenChangeListener {
        void onTokenChange();
    }
    private OnTokenChangeListener onTokenChangeListener;

    public static final String TOKEN = "token";
    public static final String USER_NAME = "user_name";
    public static final String BITMAP_MODEL = "bitmap_model";
    public static final String PATTERNS = "patterns";

    private static PreferenceHelper instance;

    private Context context;

    private SharedPreferences preferences;

    public String token;

    private Gson gson;

    private PreferenceHelper() {
        this.gson = new Gson();
    }

    public static PreferenceHelper getInstance() {
        if (instance ==null) {
            instance = new PreferenceHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void onTokenChange(OnTokenChangeListener onTokenChangeListener){
        this.onTokenChangeListener = onTokenChangeListener;
    }

    public void initToken() {
       token=getString(TOKEN);
        if (onTokenChangeListener !=null){
            onTokenChangeListener.onTokenChange();
        }
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void putString(String key,String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putPaintModel(BitmapModel bitmapModel) {
       putString(BITMAP_MODEL, gson.toJson(bitmapModel, BitmapModel.class));
    }

    public BitmapModel getPaintModel() {
        String bitmapString = getString(BITMAP_MODEL);
        BitmapModel bitmapMode=null;
        if (!bitmapString.isEmpty()){
            bitmapMode = gson.fromJson(getString(BITMAP_MODEL),BitmapModel.class);
        }
       return bitmapMode;
    }

    public void putPatterns(ArrayList<Pattern> patterns) {
        putString(PATTERNS, gson.toJson(patterns, new TypeToken<ArrayList<Pattern>>() {}.getType()));
    }

    public ArrayList<Pattern> getPatterns() {
        String patternsString = getString(PATTERNS);
        ArrayList<Pattern> patterns=null;
        if (!patternsString.isEmpty()){
            patterns = gson.fromJson(getString(PATTERNS),  new TypeToken<ArrayList<Pattern>>() {}.getType());
        }
        return patterns;
    }

}