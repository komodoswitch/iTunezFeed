package com.keran.marinov.itunezfeed.common;

import android.util.Log;


public class L 
{
	
	
	public static void log(String LOG_TAG,boolean LOG_ENABLED,String msg)
	{
		if(LOG_ENABLED)
			Log.e( LOG_TAG, msg+ "==============");
	}
	
	public static void logi(String TAG,boolean LOG_ENABLED,String msg)
	{
		if(LOG_ENABLED)
			Log.i( TAG, msg+ "==============");
	}

	public static void loge(String TAG,boolean LOG_ENABLED,String msg)
	{
		if(LOG_ENABLED)
			Log.e( TAG, msg+ "==============");
	}
	
	public static void logw(String TAG,boolean LOG_ENABLED,String msg)
	{
		if(LOG_ENABLED)
			Log.w( TAG, msg+ "==============");
	}
}
