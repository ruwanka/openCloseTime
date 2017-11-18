package com.ruwanka.openclosetime;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.*;

/**
 * Tests the time conversion methods
 */
@RunWith(Parameterized.class)
public class TimeConversionToStringUnitTest {

  private Time time;

  private String timeString;

  public TimeConversionToStringUnitTest(Time time, String timeString) {
    this.time = time;
    this.timeString = timeString;
  }

  @Parameters(name = "{index}: timeConversionToString({0} => {1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        { new Time(12, 0, 0), "12:00 pm"},
        { new Time(24, 0, 0), "12:00 am"},
        { new Time(10, 23, 0), "10:23 am"},
        { new Time(22, 23, 0), "10:23 pm"}
    });
  }

  @Test
  public void timeConversionToString() throws Exception {
    assertEquals(timeString, Util.getTimeString(time));
  }
}