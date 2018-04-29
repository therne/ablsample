package org.airbloc.airblocsample2.app.addgoal;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinDialog;
import org.airbloc.airblocsample2.models.GoalUnit;

/**
 * 하루 목표치 단위 / 수치 입력 다이얼로그
 */
public class GoalAmountDialog extends SolinDialog {
    public interface OnResultCallback {
        void result(GoalUnit unit, float value);
    }

    private OnResultCallback callback;
    private GoalUnit goalUnit;
    private String category;
    private boolean isUnitInput = true, isHabitBreaker = false;

    public GoalAmountDialog(Context context, String category, OnResultCallback callback) {
        super(context, R.style.Base_Theme_AppCompat_Light_Dialog);
        this.callback = callback;
        this.category = category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goal_amount);

        String title;
        switch (category) {
            case "habit_breaker":
                title = "현재 습관을 얼만큼 하고 계신지 선택해주세요.";
                isHabitBreaker = true;
                break;

            case "learn":
                title = "익히기 위해 어떻게 하실건지 선택해주세요.";
                break;

            case "challenge":
                title = "도전을 위해 어떻게 하실건지 선택해주세요.";
                break;

            case "habit_maker":
                title = "습관을 들이기 위해 어떻게 하실건지 선택해주세요.";
                ((TextView) findViewById(R.id.title)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.5f);
                break;

            default:
                title = "목표를 위해서 어떻게 하실건지 선택해 주세요.";
        }

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText(title);

        TextView unitTime = (TextView) findViewById(R.id.unitTime),
                unitAmount = (TextView) findViewById(R.id.unitAmount),
                unitTextAmount = (TextView) findViewById(R.id.amountValueText),
                unitTextTime = (TextView) findViewById(R.id.timeValueText);

        unitTime.setOnClickListener(v -> {
            isUnitInput = false;
            findView(R.id.unitInputView).setVisibility(View.GONE);
            findViewById(R.id.valueInputView).setVisibility(View.VISIBLE);

            goalUnit = GoalUnit.TIME;

            findView(R.id.amountView).setVisibility(View.GONE);
            findView(R.id.timeView).setVisibility(View.VISIBLE);

            unitTextAmount.setText(isHabitBreaker ? "개 하는 습관이 있음" : "개 수행하기");
            if (isHabitBreaker) titleText.setText("습관을 하루에 얼만큼 하고 계셨나요?");

        });
        unitAmount.setOnClickListener(v -> {
            isUnitInput = false;
            findView(R.id.unitInputView).setVisibility(View.GONE);
            findViewById(R.id.valueInputView).setVisibility(View.VISIBLE);

            goalUnit = GoalUnit.AMOUNT;

            findView(R.id.amountView).setVisibility(View.VISIBLE);
            findView(R.id.timeView).setVisibility(View.GONE);

            unitTextTime.setText(isHabitBreaker ? "분씩 했었음" : "분씩 투자하기");
            if (isHabitBreaker) titleText.setText("습관을 하루에 얼만큼 하고 계셨나요?");
        });

        findViewById(R.id.prev).setOnClickListener(v -> prevPage());
        findViewById(R.id.next).setOnClickListener(v -> nextPage());
    }

    /**
     * 이전 페이지를 보여준다.
     */
    private void prevPage() {
        if (isUnitInput) dismiss();
        else {
            findView(R.id.unitInputView).setVisibility(View.VISIBLE);
            findViewById(R.id.valueInputView).setVisibility(View.GONE);
            isUnitInput = true;
        }
    }

    /**
     * 다음 페이지를 보여준다.
     */
    private void nextPage() {
        try {
            if (!isUnitInput) {
                if (goalUnit == GoalUnit.AMOUNT) {
                    EditText goalValue = (EditText) findViewById(R.id.amountValue);
                    int amount = Integer.valueOf(goalValue.getText().toString());
                    if (amount == 0) {
                        Toast.makeText(getContext(), "설마 한개도 안하시려구요...?", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    callback.result(goalUnit, amount);

                } else {
                    EditText hourText = findView(R.id.timeValueHour),
                        minuteText = findView(R.id.timeValueMin);

                    int hour = Integer.valueOf(hourText.getText().toString()),
                        min = Integer.valueOf(minuteText.getText().toString());

                    if (hour == 0 && min == 0 || min >= 60) {
                        Toast.makeText(getContext(), "목표량을 제대로 입력하셨는지 확인해주세요!", Toast.LENGTH_SHORT).show();
                        return;

                    } else if (hour > 10) {
                        Toast.makeText(getContext(), "너무 많은 목표량은 좋지 않아요...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    callback.result(goalUnit, (float) hour + (min / 60));
                }
                dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
