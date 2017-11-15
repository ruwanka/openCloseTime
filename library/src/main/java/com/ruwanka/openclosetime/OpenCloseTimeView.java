package com.ruwanka.openclosetime;

import com.jakewharton.rxbinding2.view.RxView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class OpenCloseTimeView extends LinearLayout {

    private static final String TAG = "OpenCloseTimeView";

    private final String[] days = getResources().getStringArray(R.array.days);

    private final PublishSubject<Time> openTime = PublishSubject.create();
    private final PublishSubject<Time> closeTime = PublishSubject.create();

    private FragmentManager fragmentManager;

    public OpenCloseTimeView(Context context) {
        this(context, null);
    }

    public OpenCloseTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpenCloseTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        try {
            fragmentManager = ((Activity) context).getFragmentManager();
            generateDays(context);
        } catch (ClassCastException e) {
            Log.e(TAG, "Can't get fragment manager");
        }
    }

    private void generateDays(final Context context) {
        for (int i = 0; i < 7; i++) {
            final LinearLayout openCloseView =
                    (LinearLayout) View.inflate(context, R.layout.open_close_time_day, null);

            final CheckBox chkDay = openCloseView.findViewById(R.id.chkDay);
            chkDay.setText(days[i]);

            final Button btnOpenAt = openCloseView.findViewById(R.id.btnOpenAt);
            RxView.clicks(btnOpenAt)
                    .flatMap(o -> {
                        // todo based on text value call matching overload
                        return getTimePickerDialog(fragmentManager);
                    })
                    .subscribe(t -> {});

            final Button btnCloseAt = openCloseView.findViewById(R.id.btnCloseAt);
            RxView.clicks(btnCloseAt)
                    .flatMap(o -> {
                        // todo based on text value call matching overload
                        return getTimePickerDialog(fragmentManager);
                    })
                    .subscribe(t -> {});

            addView(openCloseView);
        }
    }

    private Observable<Time> getTimePickerDialog(final FragmentManager manager) {
        return Observable.create(e -> {
            TimePickerDialog timePickerDialog;

            timePickerDialog = TimePickerDialog
                    .newInstance((view, hourOfDay, minute, second) -> {
                        e.onNext(new Time(hourOfDay, minute, second));
                        e.onComplete();
                    }, false);

            timePickerDialog.show(manager, "time_picker");
        });
    }

    private Observable<Time> getTimePickerDialog(final int hour,
                                                          final int minute,
                                                          final int seconds,
                                                          final FragmentManager manager) {
        return Observable.create(e -> {
            TimePickerDialog timePickerDialog;

            timePickerDialog = TimePickerDialog
                    .newInstance((view, hourOfDay, minute1, second) -> {
                        e.onNext(new Time(hourOfDay, minute1, second));
                        e.onComplete();
                    }, hour, minute, seconds, false);

            timePickerDialog.show(manager, "time_picker");
        });
    }
}
