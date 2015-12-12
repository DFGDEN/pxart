package dfgden.pxart.com.pxart.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.activities.MainActivity;
import dfgden.pxart.com.pxart.alarms.AlarmHelper;
import dfgden.pxart.com.pxart.data.User;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.interfaces.ResultUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;
import dfgden.pxart.com.pxart.utils.TimeUtil;

/**
 * Created by DFGDEN on 29.11.2015.
 */
public class LoginFragment extends Fragment {

    private static final String VK_URL = "http://30pxart.com/Account/Login?provider=vk&code=";
    private static final String FB_URL = "http://30pxart.com/Account/Login?provider=fb&code=";
    private static final String INSTAGRAM_URL = "http://30pxart.com/Account/Login?provider=instagram&code=";

    private static final String VK="vk";
    private static final String FB="fb";
    private static final String INSTAGRAM="instagram";

    public static final String PROVIDER_URL = "provider_url";

    private String provider;

    private WebView webView;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        webView = (WebView) view.findViewById(R.id.web);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        android.webkit.CookieManager.getInstance().removeAllCookie();
        webView.loadUrl(getArguments().getString(PROVIDER_URL));
        webView.setWebViewClient(new VkWebViewClient());
        webView.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    class VkWebViewClient extends WebViewClient implements ProgressUpdateDataListener, ResultUpdateDataListener<User> {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.setVisibility(View.VISIBLE);
            if (extractPattern(url)){
                webView.stopLoading();
                String code= url.substring(url.lastIndexOf("=",url.length()-5)+1);
                if(provider.equals(FB)){
                    code= code.substring(0,code.lastIndexOf("#_=_"));
                }

                ServiceManager.getInstance().getToken(code, provider, this, this);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.setVisibility(View.GONE);
        }

        public boolean extractPattern(String string){
                    if(string.contains(VK_URL)){
                        provider = VK;
                        return true;
                    } else if(string.contains(FB_URL)){
                        provider = FB;
                        return true;
                    } else if(string.contains(INSTAGRAM_URL)){
                        provider = INSTAGRAM;
                        return true;
                    }
            return false;

        }

        @Override
        public void startUpdate() {

        }

        @Override
        public void stopUpdate() {

        }

        @Override
        public void crash(final String text) {
            if (getActivity() != null ) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void getResult(final User result) {
            if (getActivity() != null ) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getAuthor().getName()==null){
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerAuthorityFragment, NicknameFragment.getInstance(result.getId(),result.getExpirationTime())).addToBackStack(null).commit();
                        } else {
                            PreferenceHelper.getInstance().putString(PreferenceHelper.TOKEN, result.getId());
                            PreferenceHelper.getInstance().putString(PreferenceHelper.USER_NAME, result.getAuthor().getName());
                            AlarmHelper.getInstance().setAlarm(TimeUtil.convertTime(result.getExpirationTime()));
                            Intent intent = new Intent();
                            getActivity().setResult(MainActivity.REQUEST_CODE2, intent);
                            getActivity().finish();
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    public static LoginFragment getInstance(String url) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PROVIDER_URL, url);
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

}

