package org.airbloc.airblocsample2.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

public class GlideSettings implements com.bumptech.glide.module.GlideModule {
    private static final int DISK_CACHE_SIZE = 200 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE))
                .setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}