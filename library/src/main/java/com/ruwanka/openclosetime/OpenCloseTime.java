package com.ruwanka.openclosetime;

@SuppressWarnings("WeakerAccess")
public class OpenCloseTime {

  private String day;

  private Time openTime;

  private Time closedTime;

  private boolean active;

  public OpenCloseTime(boolean active, String day, Time openTime, Time closedTime) {
    this.active = active;
    this.day = day;
    this.openTime = openTime;
    this.closedTime = closedTime;
  }

  public OpenCloseTime(String day) {
    this.day = day;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public Time getOpenTime() {
    return openTime;
  }

  public void setOpenTime(Time openTime) {
    this.openTime = openTime;
  }

  public Time getClosedTime() {
    return closedTime;
  }

  public void setClosedTime(Time closedTime) {
    this.closedTime = closedTime;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }


  @Override
  public String toString() {
    return "{"
        + "\"day\":\"" + day + "\""
        + ", \"openTime\":" + openTime
        + ", \"closedTime\":" + closedTime
        + ", \"active\":\"" + active + "\""
        + "}";
  }
}
