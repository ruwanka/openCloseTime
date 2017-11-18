package com.ruwanka.openclosetimesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.ruwanka.openclosetime.OpenCloseTimeView;

public class MainActivity extends AppCompatActivity {

  private final static String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    OpenCloseTimeView timeView = findViewById(R.id.openCloseTimeView);
    timeView.observe()
        .subscribe(openCloseTime -> Log.d(TAG, openCloseTime.toString()));

  }
}
