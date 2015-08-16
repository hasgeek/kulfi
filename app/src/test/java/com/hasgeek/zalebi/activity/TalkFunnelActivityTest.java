package com.hasgeek.zalebi.activity;

import com.hasgeek.zalebi.activity.TalkFunnelActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.hasgeek.zalebi.BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class TalkFunnelActivityTest {

    @Test
    public void testMainActivity() throws Exception {
//        assertTrue(Robolectric.buildActivity(TalkFunnelActivity.class).create().get() != null);
    }
}