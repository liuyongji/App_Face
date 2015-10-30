package com.common.util;

import android.widget.ImageView;

import com.face.test.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * UIL 工具类
 * Created by sky on 15/7/26.
 */
public class ImageLoaderUtils {
	
	
	private ImageLoaderConfiguration configuration;
	
	private static DisplayImageOptions options;
	
	

    /**
     * display local image
     * @param uri
     * @param imageView
     * @param options
     */
    public static void displayLocalImage(String uri, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage("file://" + uri, new ImageViewAware(imageView), options, null, null);
    }
    
    
    public static void displayLocalImage(String uri, ImageView imageView) {
    	
    	options = new DisplayImageOptions.Builder()
    	.showImageOnLoading(R.drawable.loading).cacheInMemory(true)
    	.cacheOnDisc(true).build();
    	
        ImageLoader.getInstance().displayImage("file://" + uri, new ImageViewAware(imageView), options, null, null);
    } 

    /**
     * display Drawable image
     * @param uri
     * @param imageView
     * @param options
     */
    public static void displayDrawableImage(String uri, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage("drawable://" + uri, new ImageViewAware(imageView), options, null, null);
    }



}
