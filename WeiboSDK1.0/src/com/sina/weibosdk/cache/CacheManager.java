package com.sina.weibosdk.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.WeiboSDKConfig;
import com.sina.weibosdk.log.LogUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

@SuppressWarnings("unused")
public class CacheManager {

    private static CacheManager mSelf = null;
    private Context mContext;
    
    private CacheManager(Context ctx) {
    	mContext = ctx;
    }

    public synchronized static CacheManager getInstance(Context ctx) {
        if (mSelf == null) {
            mSelf = new CacheManager(ctx);
        }
        return mSelf;
    }

    public Bitmap getImage(CacheStrategy cs) {
		Bitmap b = null;
		String memKey = cs.getCacheMemKey();
		String filepath = cs.getCacheFilePath();
		
		if(!TextUtils.isEmpty(memKey)) {
			b = getBitmapFromMemCache(memKey);
		}
		if(b != null && !b.isRecycled()) {
			return b;
		}
		if(!TextUtils.isEmpty(filepath)) {
			b = getBitmapFromFileCache(filepath);
			addBitmapToCache(memKey, b);
		}
    	return b;
    }
    
    /*
     * Cache-related fields and methods.
     * 
     * We use a hard and a soft cache. A soft reference cache is too
     * aggressively cleared by the Garbage Collector.
     */
    private static final int HARD_CACHE_CAPACITY = WeiboSDKConfig.getInstance(
    		).getInt(WeiboSDKConfig.KEY_MEM_CACHE_SIZE);

    // Hard cache, with a fixed maximum capacity and a life duration
    private final HashMap<String, Bitmap> sHardBitmapCache = 
    		new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
        /**
		 * 
		 */
		private static final long serialVersionUID = -1062468789016692404L;
		@Override
        protected boolean removeEldestEntry(
                LinkedHashMap.Entry<String, Bitmap> eldest) {
            if (size() > HARD_CACHE_CAPACITY) {
                // Entries push-out of hard reference cache are transferred to
                // soft reference cache
                sSoftBitmapCache.put(eldest.getKey(),
                        new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            } else {
                return false;
            }
        }
    };
//    private final HashMap<String, Bitmap> sHardBitmapCache = new HashMap<String, Bitmap>(
//            HARD_CACHE_CAPACITY);

    // Soft cache for bitmaps kicked out of hard cache
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = 
    		new ConcurrentHashMap<String, SoftReference<Bitmap>>(
            HARD_CACHE_CAPACITY / 2);



    /**
     * Adds this bitmap to the cache.
     * 
     * @param bitmap
     *            The newly downloaded bitmap.
     */
    public void addBitmapToCache(String memKey, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (sHardBitmapCache) {
                sHardBitmapCache.put(memKey, bitmap);
            }
        }
    }

    private Bitmap getBitmapFromFileCache(String filepath) {
    	Bitmap b = null;
    	try {
	    	b = BitmapFactory.decodeFile(filepath);
	    	
    	} catch(Exception e) {
    		Util.loge(e.getMessage(), e);
    	}
    	return b;
    }
    
    
    /**
     * @param key
     *            The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    private Bitmap getBitmapFromMemCache(String memKey) {
        // First try the hard reference cache
        synchronized (sHardBitmapCache) {
            final Bitmap bitmap = sHardBitmapCache.get(memKey);
            if (bitmap != null && !bitmap.isRecycled()) {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
                sHardBitmapCache.remove(memKey);
                sHardBitmapCache.put(memKey, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(memKey);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null && !bitmap.isRecycled()) {
                // Bitmap found in soft cache
                return bitmap;
            } else {
                // Soft reference has been Garbage Collected
                sSoftBitmapCache.remove(memKey);
            }
        }

        return null;
    }

    
    
    public void clearCache() {
        // sHardBitmapCache.clear();
        synchronized (sHardBitmapCache) {
            Iterator<Bitmap> softIter = sHardBitmapCache.values().iterator();
            while (softIter.hasNext()) {
                Bitmap bitmap = softIter.next();
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            sHardBitmapCache.clear();
        }
        synchronized (sSoftBitmapCache) {
            Iterator<SoftReference<Bitmap>> softIter = sSoftBitmapCache.values().iterator();
            while (softIter.hasNext()) {
                Bitmap bitmap = softIter.next().get();
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            sSoftBitmapCache.clear();
        }
    }

    /**
     * Clears the image cache used internally to improve performance. Note that
     * for memory efficiency reasons, the cache will automatically be cleared
     * after a certain inactivity delay.
     */
    public void clearCache(Vector<String> needToClear) {
        if (null == needToClear) {
            return;
        }
        // sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
        synchronized (sHardBitmapCache) {
            Bitmap bm = null;
            String key = null;
            Iterator<String> iter = needToClear.iterator();
            while (iter.hasNext()) {
                key = iter.next();
                bm = sHardBitmapCache.get(key);
                if (null != bm && !bm.isRecycled()) {
                    bm.recycle();
                }
                sHardBitmapCache.remove(key);
            }
        }
        needToClear.clear();
        synchronized (sSoftBitmapCache) {
            Iterator<SoftReference<Bitmap>> softIter = sSoftBitmapCache
                    .values().iterator();
            while (softIter.hasNext()) {
                Bitmap bitmap = softIter.next().get();
                if (null != bitmap && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            sSoftBitmapCache.clear();
        }
    }

    public void clearCache(String needToClear) {
        if (null == needToClear) {
            return;
        }
        synchronized (sHardBitmapCache) {
            Bitmap bm = null;
            bm = sHardBitmapCache.get(needToClear);
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
            sHardBitmapCache.remove(needToClear);
        }
    }
    
    
    


}

