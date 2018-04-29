package org.airbloc.airblocsample2.app.addgoal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinDialog;
import org.airbloc.airblocsample2.models.NameSuggestion;
import org.airbloc.airblocsample2.rest.apis.GoalService;
import org.airbloc.airblocsample2.views.FontCache;

import org.airbloc.airblocsample2.rest.apis.GoalService;

/**
 * 목표 이름 입력 다이얼로그
 */
public class GoalNameDialog extends SolinDialog {
    public interface OnResultCallback {
        void result(String name);
    }

    private OnResultCallback callback;
    private String category;
    private EditText nameField;

    GoalService goalService = GoalService.create();

    public GoalNameDialog(Context context, String category, OnResultCallback callback) {
        super(context, R.style.GoalAddDialog);
        this.callback = callback;
        this.category = category;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goal_name);

        nameField  = (EditText) findViewById(R.id.goalName);
        loadSuggestions();

        String title, subtitle, placeholder;
        switch (category) {
            case "sport":
                title = "당신이 하고 싶은 운동은\n무엇인가요?";
                subtitle = "운동 목표를 입력해주세요.";
                placeholder = "하루에 줄넘기 100개 하기, 살 빼기";
                break;

            case "habit_breaker":
                title = "당신이 고치고 싶은 습관은\n무엇인가요?";
                subtitle = "고치고 싶은 습관을 입력해주세요.";
                placeholder = "담배 끊기";
                break;

            case "learn":
                title = "당신이 익히고 싶은 활동은\n무엇인가요?";
                subtitle = "악기, 기술 등 익히고 싶은 걸 입력해주세요.";
                placeholder = "기타 배우기";
                break;

            case "challenge":
                title = "당신이 도전하고 싶은 목표는\n무엇인가요?";
                subtitle = "벽을 뛰어넘고 싶은 목표를 입력해주세요!";
                placeholder = "A학점 만들기";
                break;

            case "habit_maker":
                title = "당신이 들이고 싶은 습관은\n무엇인가요?";
                subtitle = "들였으면 하는 좋은 습관을 입력해주세요.";
                placeholder = "하루에 물 열잔 마시기";
                break;

            default:
                title = "당신이 세우려는 목표는\n무엇인가요?";
                subtitle = "목표를 입력해주세요.";
                placeholder = "중국어 공부하기";
        }

        ((TextView) findViewById(R.id.title)).setText(title);
        ((TextView) findViewById(R.id.subtitle)).setText(subtitle);
        nameField.setHint("예) " + placeholder);
        nameField.setTypeface(FontCache.get("NotoSans-Regular.otf"));

        findViewById(R.id.next).setOnClickListener(v -> {
            if (nameField.getText().length() == 0) {
                Toast.makeText(getContext(), "목표를 입력해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            callback.result(nameField.getText().toString());
            dismiss();
        });
    }

    /**
     * 목표 텍스트 추천을 로딩한다.
     */
    private void loadSuggestions() {
        LinearLayout container = findViewById(R.id.suggestionContainer);

        goalService.suggestName(category, result -> {
            for (NameSuggestion suggestion : result.list) {
                View row = View.inflate(getContext(), R.layout.item_goalname_suggest, null);
                TextView name = row.findViewById(R.id.goalSuggestName);
                TextView userCount = row.findViewById(R.id.goalSuggestingUsers);

                name.setText(suggestion.goal);
                userCount.setText("+ " + suggestion.people + "명");

                row.setOnClickListener(v -> nameField.setText(suggestion.goal));
                container.addView(row);
            }
        });
    }
}
