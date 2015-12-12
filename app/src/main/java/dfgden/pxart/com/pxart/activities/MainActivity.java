package dfgden.pxart.com.pxart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.alarms.AlarmHelper;
import dfgden.pxart.com.pxart.fragments.BaseTitlePatternFragment;
import dfgden.pxart.com.pxart.fragments.CommentsFragment;
import dfgden.pxart.com.pxart.fragments.UserFragment;
import dfgden.pxart.com.pxart.internet.URLList;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    public static final String KEY_BUNDLE_PATTERN_ID = "pattern_id";
    public static final int REQUEST_CODE1 = 1;
    public static final int REQUEST_CODE2 = 2;

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.mainactivity_logo);
        toolbar.setLogo(R.drawable.logo_header);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PaintActivity.class);
                startActivityForResult(intent, REQUEST_CODE1);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        drawer.setDrawerListener(toggle);
       prepareToggleButton();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeNavigationMenu();
        PreferenceHelper.getInstance().onTokenChange(new PreferenceHelper.OnTokenChangeListener() {
            @Override
            public void onTokenChange() {
                changeNavigationMenu();
            }
        });

        if (getSupportFragmentManager().findFragmentByTag("start") == null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, BaseTitlePatternFragment.getInstance(URLList.PAGE_NEW_PATTERN.getUrl()), "start").addToBackStack(null).commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                }
                hideSoftKeyboard();
                fab.setVisibility(getSupportFragmentManager().getBackStackEntryCount() > 1 ? View.GONE : View.VISIBLE);
                toggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() > 1 ? false : true);
            }
        });
    }

    private void prepareToggleButton() {
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        toggle.setHomeAsUpIndicator(getDrawerToggleDelegate().getThemeUpIndicator());
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() > 1 ? false : true);
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE1:
                if (resultCode == 1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, CommentsFragment.getInstance(data.getStringExtra(KEY_BUNDLE_PATTERN_ID))).addToBackStack(null).commit();
                }
                break;
            case REQUEST_CODE2:
                if (resultCode == 2) {
                    PreferenceHelper.getInstance().initToken();
                    changeNavigationMenu();
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, UserFragment.getInstance(PreferenceHelper.getInstance().getString(PreferenceHelper.USER_NAME))).addToBackStack(null).commit();
                }
                break;
        }
    }

    private void changeNavigationMenu() {
        if (!PreferenceHelper.getInstance().token.isEmpty()) {
            navigationView.getMenu().getItem(0).setVisible(true);
            navigationView.getMenu().getItem(0).setTitle(PreferenceHelper.getInstance().getString(PreferenceHelper.USER_NAME));
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(3).setVisible(true);
        } else {
            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(true);
            navigationView.getMenu().getItem(3).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_remove_save_pattern:
                PreferenceHelper.getInstance().putPaintModel(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_new_pattern:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, BaseTitlePatternFragment.getInstance(URLList.PAGE_NEW_PATTERN.getUrl())).addToBackStack(null).commit();
                break;
            case R.id.nav_best_pattern:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, BaseTitlePatternFragment.getInstance(URLList.PAGE_BEST_PATTERN.getUrl())).addToBackStack(null).commit();
                break;
            case R.id.nav_most_commented:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, BaseTitlePatternFragment.getInstance(URLList.PAGE_MOSTCOMMENTED_PATTERN.getUrl())).addToBackStack(null).commit();
                break;
            case R.id.nav_new_unregregistered:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, BaseTitlePatternFragment.getInstance(URLList.PAGE_RECENTUNREGISTERED_PATTERN.getUrl())).addToBackStack(null).commit();
                break;
            case R.id.nav_best_unregregistered:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, BaseTitlePatternFragment.getInstance(URLList.PAGE_BESTUNREGISTERED_PATTERN.getUrl())).addToBackStack(null).commit();
                break;
            case R.id.nav_login:
                Intent intent = new Intent(MainActivity.this, AuthorityActivity.class);
                startActivityForResult(intent, REQUEST_CODE2);
                break;
            case R.id.nav_leave:
                PreferenceHelper.getInstance().putString(PreferenceHelper.TOKEN, "");
                PreferenceHelper.getInstance().initToken();
                AlarmHelper.getInstance().removeAlarm();
                changeNavigationMenu();
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom).replace(R.id.container, UserFragment.getInstance(PreferenceHelper.getInstance().getString(PreferenceHelper.USER_NAME))).addToBackStack(null).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
