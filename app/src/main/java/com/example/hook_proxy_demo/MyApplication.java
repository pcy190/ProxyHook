package com.example.hook_proxy_demo;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import java.lang.reflect.Field;

public class MyApplication extends Application {

    private static final String TAG = "HAPPY";
    private static final String ACTIVITY_THREAD="android.app.ActivityThread";
    private static final String CURRENT_ACTIVITY_THREAD="sCurrentActivityThread";
    private static final String INSTRUMENT="mInstrumentation";
    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate From Application ");
        try {
            attachContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void attachContext() throws Exception {
        // get current ActivityThread Object
        Class<?> activityThreadClass = Class.forName(ACTIVITY_THREAD);
        Field currentActivityThreadField = activityThreadClass.getDeclaredField(CURRENT_ACTIVITY_THREAD);
        //Field currentActivityThreadField = activityThreadClass.getDeclaredField(CURRENT_ACTIVITY_THREAD);
        currentActivityThreadField.setAccessible(true);
        Object currentActivityThread = currentActivityThreadField.get(null);

        // get original mInstrumentation field
        Field mInstrumentationField = activityThreadClass.getDeclaredField(INSTRUMENT);
        //Field mInstrumentationField = activityThreadClass.getField(INSTRUMENT);
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        // create Proxy
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);

        // set original currentActivityThread as evilInstrumentation(our handler)
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
        Log.d(TAG, "attachContext: Hook finish!");
    }
}


