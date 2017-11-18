package com.ruwanka.openclosetime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Util {

  static Time getTimeFromString(String timeString) {
    Pattern pattern = Pattern.compile("([0-1][0-9]):([0-5][0-9]) (am|pm)");
    Matcher matcher = pattern.matcher(timeString);
    if (matcher.find() && matcher.groupCount() == 3) {
      int hr = Integer.valueOf(matcher.group(1));
      int min = Integer.valueOf(matcher.group(2));
      if ((matcher.group(3).equals("pm") && hr != 12)
          || (matcher.group(3).equals("am") && hr == 12)) {
        hr = Integer.valueOf(matcher.group(1)) + 12;
      }
      return new Time(hr, min, 0);
    }
    return null;
  }

  static String getTimeString(Time time) {
    boolean am = true;
    int hr;
    if (time.getHour() == 12) {
      am = false;
    } if (time.getHour() > 12 && time.getHour() != 24) {
      am = false;
      hr = time.getHour() - 12;
    } else if (time.getHour() == 24) {
      hr = 12;
    } else {
      hr = time.getHour();
    }
    return Util.appendLeadingZero(hr) + ":" + Util.appendLeadingZero(time.getMinutes()) + (am ? " am" : " pm");
  }

  private static String appendLeadingZero(int value) {
    if (value < 10) {
      return "0" + String.valueOf(value);
    }
    return String.valueOf(value);
  }

}
