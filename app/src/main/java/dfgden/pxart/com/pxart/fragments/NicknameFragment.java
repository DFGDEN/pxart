package dfgden.pxart.com.pxart.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.activities.MainActivity;
import dfgden.pxart.com.pxart.alarms.AlarmHelper;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;
import dfgden.pxart.com.pxart.utils.TimeUtil;


public class NicknameFragment extends Fragment implements View.OnClickListener, ProgressUpdateDataListener {

    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_REG_TIME = "key_regtime";

    private String token;
    private String expirationTime;
    private Button btnConfirmNick;
    private EditText edtNick;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nickname, null);
        btnConfirmNick = (Button) view.findViewById(R.id.btnConfirmNick);
        btnConfirmNick.setOnClickListener(this);
        edtNick = (EditText) view.findViewById(R.id.edtNick);
        TextInputLayout tilEdtNick=(TextInputLayout) view.findViewById(R.id.tilEdtNick);
        tilEdtNick.setHint(getString(R.string.nicknamefragment_intutlogin));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.progressdialog_dataupdate));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();


    }
    private void getData(){
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(KEY_TOKEN)&& bundle.containsKey(KEY_REG_TIME)){
            token= bundle.getString(KEY_TOKEN);
            expirationTime= bundle.getString(KEY_REG_TIME);
        }
    }

    public static NicknameFragment getInstance(String token,String time) {
        NicknameFragment nicknameFragment = new NicknameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TOKEN, token);
        bundle.putString(KEY_REG_TIME, time);
        nicknameFragment.setArguments(bundle);
        return nicknameFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirmNick:
                ServiceManager.getInstance().setUserName(edtNick.getText().toString(),token,this);

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
        PreferenceHelper.getInstance().putString(PreferenceHelper.TOKEN,token);
        PreferenceHelper.getInstance().putString(PreferenceHelper.USER_NAME,edtNick.getText().toString());
        AlarmHelper.getInstance().setAlarm(TimeUtil.convertTime(expirationTime));
        Intent intent = new Intent();
        getActivity().setResult(MainActivity.REQUEST_CODE2, intent);
        getActivity().finish();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void crash(final String text) {
        if (getActivity() != null ) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }
}
