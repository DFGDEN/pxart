package dfgden.pxart.com.pxart;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import dfgden.pxart.com.pxart.alarms.AlarmHelper;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;


public class PxartApplication extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ImageLoaderApp");

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).diskCache(new UnlimitedDiskCache(f)).build();
        ImageLoader.getInstance().init(config);
        AlarmHelper.getInstance().init(getApplicationContext());
        ServiceManager.getInstance().init(getApplicationContext());
        PreferenceHelper.getInstance().init(getApplicationContext());
        PreferenceHelper.getInstance().initToken();
        context = getApplicationContext();
    }


}
