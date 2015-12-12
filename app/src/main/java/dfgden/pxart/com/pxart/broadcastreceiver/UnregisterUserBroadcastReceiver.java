package dfgden.pxart.com.pxart.broadcastreceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;


public class UnregisterUserBroadcastReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        PreferenceHelper.getInstance().putString(PreferenceHelper.USER_NAME, "");
        PreferenceHelper.getInstance().putString(PreferenceHelper.TOKEN, "");
        PreferenceHelper.getInstance().initToken();
        Toast.makeText(context, R.string.unregisteruserbroadcastreceiver_overtime, Toast.LENGTH_LONG).show();
    }


}
