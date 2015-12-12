package dfgden.pxart.com.pxart.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.data.Providers;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.interfaces.ResultUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;


public class AuthorityFragment extends Fragment implements View.OnClickListener, ProgressUpdateDataListener, ResultUpdateDataListener<String> {

    private Button btnVk,btnInstagram,btnFacebook;
    public static final String PROVIDER_VK="provider_vk";
    public static final String PROVIDER_FB="provider_fb";
    public static final String PROVIDER_INSTAGRAM="provider_instagram";
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authority, null);
        btnVk = (Button) view.findViewById(R.id.btnVk);
        btnVk.setOnClickListener(this);
        btnInstagram = (Button) view.findViewById(R.id.btnInstagram);
        btnInstagram.setOnClickListener(this);
        btnFacebook = (Button) view.findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.progressdialog_dataupdate));
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnVk:
                ServiceManager.getInstance().getProviderUrl(PROVIDER_VK, this, this);
                break;
            case R.id.btnInstagram:
                ServiceManager.getInstance().getProviderUrl(PROVIDER_INSTAGRAM,this,this);
                break;
            case R.id.btnFacebook:
                ServiceManager.getInstance().getProviderUrl(PROVIDER_FB,this,this);
                break;
        }
    }

    @Override
    public void startUpdate() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                }
            });

        }
    }

    @Override
    public void stopUpdate() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }
            });

        }
    }

    @Override
    public void crash(final String text) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    @Override
    public void getResult(final String result) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerAuthorityFragment, LoginFragment.getInstance(result)).addToBackStack(null).commit();
                }
            });
        }
    }

    public static AuthorityFragment getInstance(){
        AuthorityFragment authorityFragment = new AuthorityFragment();
        Bundle bundle = new Bundle();
        authorityFragment.setArguments(bundle);
        return authorityFragment;
    }
}
