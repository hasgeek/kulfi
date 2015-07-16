package com.hasgeek.zalebi.reader;

import com.hasgeek.zalebi.model.Session;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.hasgeek.zalebi.BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class SessionsReaderTest extends TestCase {

    private SessionsReader sessionsReader = new SessionsReader(RuntimeEnvironment.application);
    @Before
    public void setUp()  {

    }

    @Test
    public void willReadSessionsFromSDCard(){
        ArrayList<Session> sessions = sessionsReader.readSessions();
        assertTrue(sessions.size() == 41);
    }
}