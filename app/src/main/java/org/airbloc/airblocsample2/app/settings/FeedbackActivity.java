
package org.airbloc.airblocsample2.app.settings;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinActivity;
import org.airbloc.airblocsample2.datamgmt.DataStore;
import org.airbloc.airblocsample2.models.Feedback;
import org.airbloc.airblocsample2.rest.apis.UserService;
import org.airbloc.airblocsample2.utils.Utils;

import org.airbloc.airblocsample2.rest.apis.UserService;
import org.airbloc.airblocsample2.utils.Utils;

import java.util.List;

public class FeedbackActivity extends SolinActivity {

    private LinearLayout feedbackContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacks);
        setUpToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        setTitle(""); // space for custom text

        feedbackContainer = findView(R.id.feedbackContainer);

        findView(R.id.newFeedback).setOnClickListener(v -> addFeedback());
        loadFeedbacks();
    }

    private void addFeedback() {
        new FeedbackDialog(this, (type, feedback) -> {
            // send to server
            UserService.create().sendFeedback(type, feedback, result -> {
                Toast.makeText(this, "피드백을 전송했습니다! 감사합니다 :)", Toast.LENGTH_SHORT).show();
                DataStore.save(new Feedback(result.id, type, feedback));
                loadFeedbacks();
            });
        }).show();
    }

    private void loadFeedbacks() {
        feedbackContainer.removeAllViews();

        List<Feedback> feedbacks = DataStore.getAll(Feedback.class);
        if (feedbacks.isEmpty()) findView(R.id.noFeedback).setVisibility(View.VISIBLE);
        else {
            findView(R.id.noFeedback).setVisibility(View.GONE);

            for (Feedback feedback : feedbacks) {
                View row = View.inflate(this, R.layout.item_feedback, null);
                TextView message = (TextView) row.findViewById(R.id.feedbackMessage),
                        category = (TextView) row.findViewById(R.id.feedbackCategory),
                        when = (TextView) row.findViewById(R.id.feedbackDate),
                        replyStatus = (TextView) row.findViewById(R.id.feedbackStatus),
                        reply = (TextView) row.findViewById(R.id.feedbackAnswer);

                ImageView replyStatusIcon = (ImageView) row.findViewById(R.id.feedbackStatusIcon);

                message.setText(feedback.question);
                category.setText(feedback.type.equals("error") ? "오류신고" : "건의사항");
                when.setText(Utils.formatRelative(feedback.date));
                replyStatus.setText(feedback.answer == null
                        ? "아직 답변이 도착하지 않았습니다."
                        : "답변이 도착했습니다!");
                replyStatusIcon.setImageResource(feedback.answer == null
                        ? R.drawable.not_yet
                        : R.drawable.arrived);
                if (feedback.answer == null) {
                    reply.setVisibility(View.GONE);
                }
                else reply.setText(feedback.answer);

                feedbackContainer.addView(row);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
