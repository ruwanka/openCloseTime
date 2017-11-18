package com.ruwanka.openclosetime;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;

public class OpenCloseTimeView extends LinearLayout {

  private static final String TAG = "OpenCloseTimeView";

  private final String[] days = getResources().getStringArray(R.array.days);

  private List<OpenCloseTime> openCloseTimes = new ArrayList<>(days.length);

  private final PublishSubject<OpenCloseTime> openCloseTimeSubject = PublishSubject.create();

  private FragmentManager fragmentManager;

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  private Drawable btnBg;
  private int bottomMargin;
  private int itemGap;
  private int itemHeight;

  public OpenCloseTimeView(Context context) {
    this(context, null);
  }

  public OpenCloseTimeView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OpenCloseTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setOrientation(VERTICAL);

    TypedArray a = context.obtainStyledAttributes(attrs,
        R.styleable.OpenCloseTimeView,
        defStyleAttr,0);

    if (a.hasValue(R.styleable.OpenCloseTimeView_oct_buttonBackground)) {
      btnBg = ContextCompat.getDrawable(context,
          a.getResourceId(R.styleable.OpenCloseTimeView_oct_buttonBackground, 0));
      bottomMargin = a.getDimensionPixelSize(R.styleable.OpenCloseTimeView_oct_bottomMargin,
          getResources().getDimensionPixelSize(R.dimen.defaultBottomMargin));
      itemGap = a.getDimensionPixelSize(R.styleable.OpenCloseTimeView_oct_itemGap,
          getResources().getDimensionPixelSize(R.dimen.defaultItemGap));
      itemHeight = a.getDimensionPixelSize(R.styleable.OpenCloseTimeView_oct_itemHeight, 0);
    }

    a.recycle();

    try {
      fragmentManager = ((Activity) context).getFragmentManager();
      initOpenCloseTimes();
      generateDays(context);
    } catch (ClassCastException e) {
      Log.e(TAG, "Can't get fragment manager");
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    compositeDisposable.clear();
  }

  private void initOpenCloseTimes() {
    for (int i = 0; i < days.length; i++) {
      openCloseTimes.add(i, new OpenCloseTime(days[i]));
    }
  }

  public void setOpenCloseTimes(List<OpenCloseTime> openCloseTimes) {
    if (openCloseTimes.size() == 7) {
      this.openCloseTimes = openCloseTimes;
      compositeDisposable.clear();
      removeAllViews();
      generateDays(getContext());
    }
  }

  public Observable<OpenCloseTime> observe() {
    return openCloseTimeSubject;
  }

  private void generateDays(final Context context) {
    for (int i = 0; i < 7; i++) {
      final LinearLayout openCloseView = getOpenCloseTimeStrip(context);
      int finalI = i;

      final CheckBox chkDay = getCheckBox(days[i], openCloseView, finalI);

      Disposable chkDisposable = RxCompoundButton.checkedChanges(chkDay)
          .subscribe(checked -> {
            openCloseTimes.get(finalI).setActive(checked);
            openCloseTimeSubject.onNext(openCloseTimes.get(finalI));
          });
      compositeDisposable.add(chkDisposable);

      final Button btnOpenAt = getOpenButton(openCloseView, finalI);
      Disposable openBtnDisposable = RxView.clicks(btnOpenAt)
          .map(o -> btnOpenAt.getText().toString())
          .flatMap(text -> {
            Time time = Util.getTimeFromString(text);
            if (time != null) {
              return getTimePickerDialog(time.getHour(), time.getMinutes(), time.getSeconds(),
                  fragmentManager);
            } else {
              return getTimePickerDialog(fragmentManager);
            }
          })
          .subscribe(t -> {
            btnOpenAt.setText(Util.getTimeString(t));
            openCloseTimes.get(finalI).setOpenTime(t);
            openCloseTimeSubject.onNext(openCloseTimes.get(finalI));
          });
      compositeDisposable.add(openBtnDisposable);

      final Button btnCloseAt = getCloseButton(openCloseView, finalI);
      Disposable closeBtnDisposable = RxView.clicks(btnCloseAt)
          .map(o -> btnCloseAt.getText().toString())
          .flatMap(text -> {
            Time time = Util.getTimeFromString(text);
            if (time != null) {
              return getTimePickerDialog(time.getHour(), time.getMinutes(), time.getSeconds(),
                  fragmentManager);
            } else {
              return getTimePickerDialog(fragmentManager);
            }
          })
          .subscribe(t -> {
            btnCloseAt.setText(Util.getTimeString(t));
            openCloseTimes.get(finalI).setClosedTime(t);
            openCloseTimeSubject.onNext(openCloseTimes.get(finalI));
          });
      compositeDisposable.add(closeBtnDisposable);

      addView(openCloseView);
    }
  }

  @NonNull
  private CheckBox getCheckBox(String day, LinearLayout openCloseView, int finalI) {
    final CheckBox chkDay = openCloseView.findViewById(R.id.chkDay);
    chkDay.setText(day);
    chkDay.setChecked(openCloseTimes.get(finalI).isActive());
    return chkDay;
  }

  @NonNull
  private LinearLayout getOpenCloseTimeStrip(Context context) {
    final LinearLayout openCloseView =
        (LinearLayout) View.inflate(context, R.layout.open_close_time_day, null);
    int height = itemHeight == 0 ? LayoutParams.WRAP_CONTENT : itemHeight;
    LayoutParams params = new LayoutParams(
        LayoutParams.MATCH_PARENT,
        height
    );
    params.bottomMargin = bottomMargin;
    openCloseView.setLayoutParams(params);
    return openCloseView;
  }

  private Button getCloseButton(LinearLayout openCloseView, int index) {
    final Button btnCloseAt = openCloseView.findViewById(R.id.btnCloseAt);
    if (btnBg != null) {
      btnCloseAt.setBackground(btnBg);
    }
    LayoutParams params = new LayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT,
        1f
    );
    params.setMargins(0, 0, itemGap, 0);
    btnCloseAt.setLayoutParams(params);
    Time closedTime = openCloseTimes.get(index).getClosedTime();
    if (closedTime != null) {
      btnCloseAt.setText(Util.getTimeString(closedTime));
    }
    return btnCloseAt;
  }

  private Button getOpenButton(LinearLayout openCloseView, int index) {
    final Button btnOpenAt = openCloseView.findViewById(R.id.btnOpenAt);
    if (btnBg != null) {
      btnOpenAt.setBackground(btnBg);
    }
    LayoutParams params = new LayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT,
        1f
    );
    params.setMargins(itemGap, 0, itemGap, 0);
    btnOpenAt.setLayoutParams(params);
    Time openTime = openCloseTimes.get(index).getOpenTime();
    if (openTime != null) {
      btnOpenAt.setText(Util.getTimeString(openTime));
    }
    return btnOpenAt;
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
          .newInstance((view, hourOfDay, min, second) -> {
            e.onNext(new Time(hourOfDay, min, second));
            e.onComplete();
          }, hour, minute, seconds, false);

      timePickerDialog.show(manager, "time_picker");
    });
  }

}
