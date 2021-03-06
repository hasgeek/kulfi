package com.hasgeek.zalebi.util;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by heisenberg on 27/06/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.hasgeek.zalebi.BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class TimeUtilsTest {
    @Test
    public void displayableTime_returnsProperReadableTime(){
        Assert.assertEquals("09:30 AM", DateTimeUtils.displayableTime("2015-04-16T04:00:00Z"));
        Assert.assertEquals("03:30 PM", DateTimeUtils.displayableTime("2015-04-16T10:00:00Z"));
    }

    @Test
    public void getDuration_returnsDurationBetweenStartTimeAndEndTime(){
        Assert.assertEquals("45 min", DateTimeUtils.getDuration("2015-04-16T04:00:00Z", "2015-04-16T04:45:00Z"));
        Assert.assertEquals("65 min", DateTimeUtils.getDuration("2015-04-16T12:00:00Z", "2015-04-16T13:05:00Z"));
    }

    @Test
    public void displayableDate_returnsReadableDate(){
        Assert.assertEquals("16 April 2015",
                DateTimeUtils.displayableDate("2015-04-16T04:00:00Z"));
        Assert.assertEquals("21 January 2015",
                DateTimeUtils.displayableDate("2015-01-21T12:00:00Z"));
    }
}
