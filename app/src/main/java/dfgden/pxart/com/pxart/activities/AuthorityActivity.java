package dfgden.pxart.com.pxart.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.fragments.AuthorityFragment;

public class AuthorityActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerAuthorityFragment, AuthorityFragment.getInstance()).addToBackStack(null).commit();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                }
            }
        });
    }
}
