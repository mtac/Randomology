package org.jumpingtree.randomology.utils;

import android.util.Log;

import org.jumpingtree.randomology.RDApplication;
import org.jumpingtree.randomology.config.AppConfiguration;

public class Logger { 
	  
    public enum LogLevel { ERROR, WARNING, INFO, DEBUG, VERBOSE }
      
    public static void log(LogLevel level, String TAG, String text) {
        
        String msg = RDApplication.APP_TAG + " | " + TAG + " | " +  text;
        
        switch (level) {  
        case ERROR: 	Log.e(RDApplication.APP_TAG, msg); break;
        case WARNING: 	Log.w(RDApplication.APP_TAG, msg); break;
        case INFO: 		Log.i(RDApplication.APP_TAG, msg); break;
        case DEBUG: 	if(AppConfiguration.DEBUG){Log.d(RDApplication.APP_TAG, msg);} break;
        case VERBOSE:
        default: 		if(AppConfiguration.DEBUG){Log.v(RDApplication.APP_TAG, msg);} break;
        }    
    } 
      
    public static void log(LogLevel level, String TAG, String text, Throwable e) {

        String msg = RDApplication.APP_TAG + " | " + TAG + " | " +  text;
        
        switch (level) { 
        case ERROR: 	Log.e(RDApplication.APP_TAG, msg, e); break;
        case WARNING: 	Log.w(RDApplication.APP_TAG, msg, e); break;
        case INFO: 		Log.i(RDApplication.APP_TAG, msg, e); break;
        case DEBUG: 	if(AppConfiguration.DEBUG){Log.d(RDApplication.APP_TAG, msg, e);} break;
        case VERBOSE:
        default: 		if(AppConfiguration.DEBUG){Log.v(RDApplication.APP_TAG, msg, e);} break;
        }    
    } 
} 
