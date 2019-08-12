package com.example.hook_proxy_demo;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

public class EvilInstrumentation extends Instrumentation {
    //private static final String TAG = "EvilInstrumentation";
    private static final String TAG="HAPPY";

    // ActivityThread Instrumentation Object
    private Instrumentation mBase;

    EvilInstrumentation(Instrumentation base) {
        mBase = base;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        // before Hook
        Log.d(TAG, "\nNow Hook startActivity, paramters are:" + "\ntoken = [" + token + "], " +
                "\ntarget = [" + target + "], \nintent = [" + intent +
                "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");

        // Invoke original execStartActivity method by reflect
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class, Bundle.class);
            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(mBase, who,
                    contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            Log.d(TAG, "execStartActivity: Fail to exec by hook");
            throw new RuntimeException("Do not support! please adapt it manually.");
        }
    }
}
