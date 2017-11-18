package com.ruwanka.openclosetimesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.ruwanka.openclosetime.OpenCloseTime;
import com.ruwanka.openclosetime.OpenCloseTimeView;
import com.ruwanka.openclosetime.Time;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private final static String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    OpenCloseTimeView timeView = findViewById(R.id.openCloseTimeView);

    timeView.setOpenCloseTimes(getOpenCloseTimes());

    timeView.observe()
        .subscribe(openCloseTime -> Log.d(TAG, openCloseTime.toString()));

  }

  private List<OpenCloseTime> getOpenCloseTimes() {
    List<OpenCloseTime> times = new ArrayList<>(7);
    Time openTime = new Time(9, 0, 0);
    Time closeTime = new Time(16, 30, 0);
    times.add(new OpenCloseTime(true, "Monday", openTime, closeTime));
    times.add(new OpenCloseTime(true, "Tuesday", openTime, closeTime));
    times.add(new OpenCloseTime(true, "Wednesday", openTime, closeTime));
    times.add(new OpenCloseTime(true, "Thursday", openTime, closeTime));
    times.add(new OpenCloseTime(true, "Friday", openTime, closeTime));
    times.add(new OpenCloseTime(false, "Saturday", openTime, closeTime));
    times.add(new OpenCloseTime(false, "Sunday", openTime, closeTime));
    return times;
  }
}
