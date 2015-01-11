package org.jumpingtree.randomology.utils;

import android.util.Log;

import org.jumpingtree.randomology.RDApplication;

public class Logger { 
	  
    public enum LogLevel { ERROR, WARNING, INFO, DEBUG, VERBOSE } 
  
    private static int mLevel = LogLevel.VERBOSE.ordinal(); 
      
    public static void setMaxLevel(int level) { 
        mLevel  = level; 
    } 
      
    public static void log(LogLevel level, String TAG, String text) { 
        if (level.ordinal() > mLevel) { return; } 
        
        String msg = RDApplication.APP_TAG + " | " + TAG + " | " +  text;
        
        switch (level) {  
        case ERROR: 	Log.e(RDApplication.APP_TAG, msg); break;
        case WARNING: 	Log.w(RDApplication.APP_TAG, msg); break;
        case INFO: 		Log.i(RDApplication.APP_TAG, msg); break;
        case DEBUG: 	Log.d(RDApplication.APP_TAG, msg); break;
        case VERBOSE:
        default: 		Log.v(RDApplication.APP_TAG, msg); break;
        }    
    } 
      
    public static void log(LogLevel level, String TAG, String text, Throwable e) { 
        if (level.ordinal() > mLevel) { return; }

        String msg = RDApplication.APP_TAG + " | " + TAG + " | " +  text;
        
        switch (level) { 
        case ERROR: 	Log.e(RDApplication.APP_TAG, msg, e); break;
        case WARNING: 	Log.w(RDApplication.APP_TAG, msg, e); break;
        case INFO: 		Log.i(RDApplication.APP_TAG, msg, e); break;
        case DEBUG: 	Log.d(RDApplication.APP_TAG, msg, e); break;
        case VERBOSE:
        default: 		Log.v(RDApplication.APP_TAG, msg, e); break;
        }    
    } 
} 
