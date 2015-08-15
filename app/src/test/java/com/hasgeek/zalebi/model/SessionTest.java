package com.hasgeek.zalebi.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.hasgeek.zalebi.BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class SessionTest {

    Session session;

    @Before
    public void setup(){
        session = new Session();
        session.setTitle("Going functional with JS");
        session.setSpeaker("Hemanth.HM");
        session.setStart("2015-09-18T04:15:00Z");
        session.setEnd("2015-09-18T05:00:00Z");
        session.setSessionId(27L);
    }
    @Test
    public void shouldSaveIfSessionNotExists(){
//        when(Session.find(Session.class,"session_id = ?","27")).thenReturn(new ArrayList<Session>());
//        verify(session, times(1)).save();
        session.saveOrUpdate();

    }
    @Test
    public void shouldUpdateIfSessionIdExists(){

    }
}
