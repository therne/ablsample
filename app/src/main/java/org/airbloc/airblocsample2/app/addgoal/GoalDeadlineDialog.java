package org.airbloc.airblocsample2.app.addgoal;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinDialog;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * 목표일 입력 다이얼로그
 */
public class GoalDeadlineDialog extends SolinDialog  {
    public interface OnResultCallback {
        void result(Calendar calendar);
    }

    private OnResultCallback callback;
    private EditText yearValue, monthValue, dayValue;

    public GoalDeadlineDialog(Context context, OnResultCallback callback) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goal_deadline);

        yearValue = (EditText) findViewById(R.id.yearValue);
        monthValue = (EditText) findViewById(R.id.monthValue);
        dayValue = (EditText) findViewById(R.id.dayValue);

        DateTime time = new DateTime().plusDays(66);
        yearValue.setText(String.valueOf(time.getYear()));
        monthValue.setText(String.valueOf(time.getMonthOfYear()));
        dayValue.setText(String.valueOf(time.getDayOfMonth()));

        findViewById(R.id.prev).setOnClickListener(v -> prevPage());
        findViewById(R.id.next).setOnClickListener(v -> nextPage());
    }

    /**
     * 이전 페이지를 보여준다.
     */
    private void prevPage() {
        dismiss();
    }

    /**
     * 다음 페이지를 보여준다.
     */
    private void nextPage() {
        try {
            Calendar calendar = Calendar.getInstance();
            int year = Integer.parseInt(yearValue.getText().toString());
            int month = Integer.parseInt(monthValue.getText().toString()) - 1;
            int day = Integer.parseInt(dayValue.getText().toString());
            calendar.set(year, month, day);
            calendar.setLenient(false);
            calendar.getTime(); // for checking error

            // 7일 미만이면 밴
            if (DateTime.now().plusDays(7).isAfter(calendar.getTimeInMillis())) {
                Toast.makeText(getContext(), "기간이 너무 짧은 것 같아요...", Toast.LENGTH_SHORT).show();
                return;
            }

            callback.result(calendar);
            dismiss();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), "날짜를 확인해 주세요!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
