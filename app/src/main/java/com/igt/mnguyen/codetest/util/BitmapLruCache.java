package com.igt.mnguyen.codetest.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapLruCache {

    private static BitmapLruCache instance;
    private LruCache<String, Bitmap> lru;

    public BitmapLruCache(int defaultCacheSize) {
        lru = new LruCache<String, Bitmap>(defaultCacheSize);

    }

    public static BitmapLruCache getInstance() {

        if (instance == null) {

            throw new IllegalStateException("The BitmapLruCache must be initialized.");
        }

        return instance;

    }

    public static void init(Context context, int cacheSize) {
        if (instance == null) {
            instance = new BitmapLruCache(cacheSize);
        }
    }

    public LruCache<String, Bitmap> getLru() {
        return lru;
    }

}
