package com.ruwanka.openclosetime;

@SuppressWarnings("WeakerAccess")
public class Time {

  private int hour;

  private int minutes;

  private int seconds;

  public Time(int hour, int minutes, int seconds) {
    this.hour = hour;
    this.minutes = minutes;
    this.seconds = seconds;
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public int getMinutes() {
    return minutes;
  }

  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

  public int getSeconds() {
    return seconds;
  }

  public void setSeconds(int seconds) {
    this.seconds = seconds;
  }


  @Override
  public String toString() {
    return "{"
        + "\"hour\":\"" + hour + "\""
        + ", \"minutes\":\"" + minutes + "\""
        + ", \"seconds\":\"" + seconds + "\""
        + "}";
  }
}
