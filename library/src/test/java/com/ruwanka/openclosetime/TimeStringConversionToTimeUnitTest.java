package com.ruwanka.openclosetime;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests the time conversion methods
 */
@RunWith(Parameterized.class)
public class TimeStringConversionToTimeUnitTest {

  private Time time;

  private String timeString;

  public TimeStringConversionToTimeUnitTest(String timeString, Time time) {
    this.timeString = timeString;
    this.time = time;
  }

  @Parameters(name = "{index}: timeConversionToString({0} => {1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        { "12:00 pm", new Time(12, 0, 0)},
        { "12:00 am", new Time(24, 0, 0)},
        { "10:23 am", new Time(10, 23, 0)},
        { "10:23 pm", new Time(22, 23, 0)},
        { "08:00 pm", new Time(20, 0, 0)}
    });
  }

  @Test
  public void timeConversionToString() throws Exception {
    // todo override Time class equals and hashcode methods
    assertEquals(time.getHour(), Util.getTimeFromString(timeString).getHour());
    assertEquals(time.getMinutes(), Util.getTimeFromString(timeString).getMinutes());
  }
}