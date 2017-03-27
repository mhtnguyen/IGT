package com.igt.mnguyen.codetest.Application;

import android.content.SharedPreferences;

import com.igt.mnguyen.codetest.util.BitmapLruCache;

/**
 * Created by mnguyen on 3/26/2017.
 */

public class IGTApplication extends android.app.Application{
    private static IGTApplication sInstance;
    public SharedPreferences mPref;
    private static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        BitmapLruCache.init(getApplicationContext(),DEFAULT_CACHE_SIZE);
        mPref = this.getApplicationContext().getSharedPreferences("pref_key", MODE_PRIVATE);

    }

    public static IGTApplication getInstance() {
        return sInstance;
    }

}
