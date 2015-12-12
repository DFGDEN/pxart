package dfgden.pxart.com.pxart.internet;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import dfgden.pxart.com.pxart.data.Author;
import dfgden.pxart.com.pxart.data.Pattern;
import dfgden.pxart.com.pxart.data.Providers;
import dfgden.pxart.com.pxart.data.User;
import dfgden.pxart.com.pxart.fragments.AuthorityFragment;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.interfaces.ResultUpdateDataListener;


public class ServiceManager {

    private static ServiceManager instance;
    private ServiceHandler serviceHandler;
    private JSONArray jsonArray;
    private Context context;
    private Gson gson;

    private static final String ERROR_NO_CONNECTION = "Ошибка подключения к интернету";
    private static final String ERROR_BAD_GET_DATA = "Ошибка получения данных";
    private static final String ERROR_BAD_DATA = "Неправильный формат данных";
    private static final String ERROR_SAME_NAME = "Ошибка, такой пользаватель уже существует";

    private static final int WAIT_TIME_LONG = 2;
    private static final int WAIT_TIME_SHORT = 0;

    private ServiceManager() {
        this.serviceHandler = new ServiceHandler();
        this.gson = new Gson();
    }

    public static ServiceManager getInstance(){
        if (instance == null) {
            synchronized (ServiceManager.class) {
                if (instance == null) {
                    instance = new ServiceManager();
                }
            }
        }
        return instance;
    }
    public synchronized void init(Context context) {
        this.context = context;

    }

    public  void getProviderUrl(final String provider, final ProgressUpdateDataListener updateDataListener, final ResultUpdateDataListener<String> resultUpdateDataListener ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URLList.PAGE_PROVIDERS_URL.getUrl();
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        String providerUrl = getProviderUrl(jsonStr, provider);
                        if(providerUrl == null){
                            updateDataListener.crash(ERROR_BAD_DATA);
                        } else {
                            resultUpdateDataListener.getResult(providerUrl);
                            updateDataListener.stopUpdate();
                        }
                    }
                }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }
            }
        }).start();
    }


    public  void getToken(final String code,final String provider, final ProgressUpdateDataListener updateDataListener, final ResultUpdateDataListener<User> resultUpdateDataListener ){
        new Thread(new Runnable() {
            @Override
            public void run() {
        String url = URLList.PAGE_USER_ACCOUNT.getUrl()+"?code="+ code+"&provider="+provider;
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        User user = getUser(jsonStr);
                        if(user == null){
                            updateDataListener.crash(ERROR_BAD_DATA);
                        } else {
                            resultUpdateDataListener.getResult(user);
                            updateDataListener.stopUpdate();
                        }
                    }
                }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }
            }
        }).start();
    }

    public  void getImage(final String url, final ProgressUpdateDataListener updateDataListener, final ResultUpdateDataListener<Pattern> resultUpdateDataListener ){
        new Thread(new Runnable() {
            @Override
            public void run() {

                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        Pattern pattern = getPattern(jsonStr);
                        if(pattern == null){
                            updateDataListener.crash(ERROR_BAD_DATA);
                        } else {
                            resultUpdateDataListener.getResult(pattern);
                            updateDataListener.stopUpdate();
                        }
                    }
                } else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }

            }
        }).start();
    }

    public  void putFolowers(final String userName, final ProgressUpdateDataListener updateDataListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URLList.PAGE_FOLLOWER.getUrl()+userName +"/followers";
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_SHORT)){
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.PUT);
                    if (jsonStr == null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        updateDataListener.stopUpdate();
                    }
                }
                else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }
            }
        }).start();
    }

    public  void deleteFolowers(final String userName, final ProgressUpdateDataListener updateDataListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URLList.PAGE_FOLLOWER.getUrl()+userName +"/followers";
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_SHORT)) {
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.DEL);
                        if (jsonStr != null && Integer.parseInt(jsonStr)== HttpStatus.SC_NO_CONTENT) {
                            updateDataListener.stopUpdate();
                        } else {
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    }
                } else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }
            }
        }).start();
    }

public  void getUserInfo(final String urlAuthor, final String urlPattern, final ProgressUpdateDataListener updateDataListener, final ResultUpdateDataListener<Object> resultUpdateDataListener) {
    new Thread(new Runnable() {
        @Override
        public void run() {

            updateDataListener.startUpdate();
            if (checkNetwork(WAIT_TIME_LONG)){
                String jsonAuthor = serviceHandler.makeServiceCall(urlAuthor, ServiceHandler.GET);
                String jsonPattern = serviceHandler.makeServiceCall(urlPattern, ServiceHandler.GET);
                if (jsonAuthor == null || jsonPattern == null){
                    updateDataListener.crash(ERROR_BAD_GET_DATA);
                } else {
                    Author author = getAuthor(jsonAuthor);
                    ArrayList<Pattern> patterns = getPatternList(jsonPattern);
                    if(author == null || patterns == null){
                        updateDataListener.crash(ERROR_BAD_DATA);
                    } else {
                        resultUpdateDataListener.getResult(author);
                        resultUpdateDataListener.getResult(patterns);
                        updateDataListener.stopUpdate();
                    }
                }
            }else {
                updateDataListener.crash(ERROR_NO_CONNECTION);
            }

        }
    }).start();
}

    public  void getPatterns(final String url,  final ProgressUpdateDataListener updateDataListener, final ResultUpdateDataListener<ArrayList<Pattern>> resultUpdateDataListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        ArrayList<Pattern> patterns = getPatternList(jsonStr);
                        if(patterns == null){
                            updateDataListener.crash(ERROR_BAD_DATA);
                        } else {
                            resultUpdateDataListener.getResult(patterns);
                            updateDataListener.stopUpdate();
                        }
                    }
                } else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }

                }

        }).start();
    }

    private static final String KEY_COMMENT ="text";

    public  void postComment(final String url,final String comment,  final ProgressUpdateDataListener updateDataListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    StringEntity se = null;
                    try {
                        JSONObject jo = new JSONObject();
                        jo.put(KEY_COMMENT, comment);
                        se = new StringEntity(jo.toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.PUT,se);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        updateDataListener.stopUpdate();
                    }
                }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }

            }

        }).start();
    }

    public void sendPattern(final Pattern pattern,final ProgressUpdateDataListener updateDataListener,final ResultUpdateDataListener<String> resultUpdateDataListener){
        new Thread(new Runnable() {
            @Override
            public void run() {

                StringEntity se=null;
                try {
                    String jsonTask = gson.toJson(pattern, Pattern.class);
                     se = new StringEntity(jsonTask, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    String jsonStr = serviceHandler.makeServiceCall(URLList.PAGE_BASE64.getUrl(), ServiceHandler.POST,se);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        resultUpdateDataListener.getResult(jsonStr);
                        updateDataListener.stopUpdate();
                        }
                    }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }
                }
        }).start();
    }

    public void putLike(final Pattern pattern, final ProgressUpdateDataListener updateDataListener){

        final String  url = URLList.PAGE_NEW_PATTERN.getUrl()+ pattern.getId()+"/likes";
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_SHORT)){
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.PUT);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        updateDataListener.stopUpdate();
                    }
                }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }

            }
        }).start();
    }

    public void deleteLike(final Pattern pattern, final ProgressUpdateDataListener updateDataListener){

        final String  url = URLList.PAGE_NEW_PATTERN.getUrl()+ pattern.getId()+"/likes";
        new Thread(new Runnable() {
            @Override
            public void run() {

                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_SHORT)){
                    String jsonStr = serviceHandler.makeServiceCall(url, ServiceHandler.DEL);
                    if (jsonStr != null && Integer.parseInt(jsonStr) == HttpStatus.SC_NO_CONTENT){
                        updateDataListener.stopUpdate();
                    } else {
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    }
                }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }

            }
        }).start();
    }

    private static final String KEY_NAME ="name";

    public void setUserName(final String name,final String token, final ProgressUpdateDataListener updateDataListener){

        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jo = null;
                StringEntity se = null;
                try {
                    jo = new JSONObject();

                    jo.put(KEY_NAME, name);
                    se = new StringEntity(jo.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    String jsonStr = serviceHandler.makeServiceCall(URLList.PAGE_USER.getUrl(), ServiceHandler.POST,se,token);
                    if (jsonStr != null && Integer.parseInt(jsonStr)== HttpStatus.SC_NO_CONTENT){
                        updateDataListener.stopUpdate();
                    } else if(jsonStr != null && Integer.parseInt(jsonStr)== HttpStatus.SC_CONFLICT){
                        updateDataListener.crash(ERROR_SAME_NAME);
                    }else {
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    }
                }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }
            }
        }).start();
    }



    public void putPattern(final Pattern pattern,final ProgressUpdateDataListener updateDataListener, final ResultUpdateDataListener<Pattern> resultUpdateDataListener ){

        new Thread(new Runnable() {
            @Override
            public void run() {

                StringEntity se = null;
                try {
                    String jsonTask = gson.toJson(pattern,Pattern.class);
                    se = new StringEntity(jsonTask, "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                updateDataListener.startUpdate();
                if (checkNetwork(WAIT_TIME_LONG)){
                    String jsonStr = serviceHandler.makeServiceCall(URLList.PAGE_NEW_PATTERN.getUrl(), ServiceHandler.PUT,se);
                    if (jsonStr ==null){
                        updateDataListener.crash(ERROR_BAD_GET_DATA);
                    } else {
                        Pattern pattern = getPattern(jsonStr);
                        if(pattern == null){
                            updateDataListener.crash(ERROR_BAD_DATA);
                        } else {
                            resultUpdateDataListener.getResult(pattern);
                            updateDataListener.stopUpdate();
                        }
                    }
                }else {
                    updateDataListener.crash(ERROR_NO_CONNECTION);
                }
            }
        }).start();
    }

    private ArrayList<Pattern> getPatternList(String jsonStr) {
        ArrayList<Pattern> modelArrayList = new ArrayList<>(60);

        try {
            jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                modelArrayList.add(gson.fromJson(jsonObject.toString(), Pattern.class));
            }
            return modelArrayList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private boolean checkNetwork(int time) {
            try {
                for (int i = 0; i < time; i++) {
                    if(isNetworkConnected()){
                        break;
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return isNetworkConnected();

    }

    private User getUser(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            User user = gson.fromJson(jsonObject.toString(),User.class);
            return user;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Pattern getPattern(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Pattern pattern = gson.fromJson(jsonObject.toString(),Pattern.class);
            return pattern;

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

    private String getProviderUrl(String jsonStr,String provider) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Providers providers = gson.fromJson(jsonObject.toString(), Providers.class);
            if (provider.equals(AuthorityFragment.PROVIDER_VK)) {
                return providers.getVk();
            } else {
                if (provider.equals(AuthorityFragment.PROVIDER_FB)) {
                    return providers.getFb();
                } else {
                    if (provider.equals(AuthorityFragment.PROVIDER_INSTAGRAM)) {
                        return providers.getInstagram();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }


    private Author getAuthor(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Author author = gson.fromJson(jsonObject.toString(),Author.class);
            return author;

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
