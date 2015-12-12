package dfgden.pxart.com.pxart.internet;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;


public class ServiceHandler {

    private String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    public final static int DEL = 4;

    public ServiceHandler() {

    }

    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null,null);
    }

    public String makeServiceCall(String url, int method,
                                  StringEntity params) {
        return this.makeServiceCall(url, method, params,null);
    }

    public String makeServiceCall(String url, int method, StringEntity params, String token) {
        try {
            response = null;
            String id = (token == null) ? PreferenceHelper.getInstance().token : token;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            HttpRequestBase request = null;
            switch (method){
                case GET:
                    request = new HttpGet(url);
                    break;
                case POST:
                    request = new HttpPost(url);
                    if (params != null) {
                        ((HttpPost)request).setEntity(params);
                    }
                    break;
                case PUT:
                    request = new HttpPut(url);
                    if (params != null) {
                        ((HttpPut)request).setEntity(params);
                    }
                    break;
                case DEL:
                    request = new HttpDelete(url);
                    break;
                default:
                    throw new IllegalArgumentException("Method " + method + " cannot be performed");
            }

            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            request.setHeader("Authorization", id);
            httpResponse = httpClient.execute(request);

            if (httpResponse.getStatusLine().getStatusCode()  == HttpStatus.SC_OK){
                httpEntity = httpResponse.getEntity();
               return response = EntityUtils.toString(httpEntity);
            }
            if (httpResponse.getStatusLine().getStatusCode()  != HttpStatus.SC_INTERNAL_SERVER_ERROR){
                return httpResponse.getStatusLine().getStatusCode()+"";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

}