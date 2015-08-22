package com.hasgeek.zalebi.app;

import android.app.Application;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycleApplication;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

@RunWith(RobolectricTestRunner.class)
@Config(constants = com.hasgeek.zalebi.BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class TestTalkfunnelApplication extends Application
        implements TestLifecycleApplication {

    @Test
    public void startEverTestSugarAppAsFirst() {

    }

    @Override
    public void beforeTest(Method method) {
        Log.v("test", "beforeTest");
    }

    @Override
    public void prepareTest(Object test) {
        Log.v("test", "prepareTest");
    }

    @Override
    public void afterTest(Method method) {
        Log.v("test", "afterTest");
    }
}