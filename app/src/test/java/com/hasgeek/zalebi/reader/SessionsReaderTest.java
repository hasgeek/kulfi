package com.hasgeek.zalebi.reader;

import com.hasgeek.zalebi.model.Session;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.hasgeek.zalebi.BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class SessionsReaderTest extends TestCase {

    private SessionsReader.SessionReadListener sessionReadListener;

    private SessionsReader sessionsReader;
    @Before
    public void setUp()  {
        sessionReadListener = mock(SessionsReader.SessionReadListener.class);
        sessionsReader = new SessionsReader(RuntimeEnvironment.application, sessionReadListener);
    }

    @Test
    public void willReadSessionsFromSDCard(){
        List<Session> sessions = sessionsReader.readSessions();
        assertTrue(sessions.size() == 41);
        verify(sessionReadListener).onSessionReadSuccess(sessions);
    }

    @Test
    public void willFailWhenReadingFromSDCardFails(){
        List<Session> sessions = sessionsReader.readSessions();
        verify(sessionReadListener).onSessionReadFailure();
        verify(sessionReadListener,never()).onSessionReadSuccess(sessions);
    }
}