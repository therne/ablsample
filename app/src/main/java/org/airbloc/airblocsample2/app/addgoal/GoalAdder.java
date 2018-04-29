package org.airbloc.airblocsample2.app.addgoal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.datamgmt.DataStore;
import org.airbloc.airblocsample2.models.Goal;
import org.airbloc.airblocsample2.models.GoalTarget;
import org.airbloc.airblocsample2.models.GoalUnit;
import org.airbloc.airblocsample2.rest.apis.GoalService;

import java.util.Date;

/**
 * 목표를 추가한다.
 */
public class GoalAdder {
    Context context;

    public GoalAdder(Context context) {
        this.context = context;
    }

    public void showAddGoalDialog(OnGoalAdded callback) {
        new CategoryDialog(context, category ->
                new GoalNameDialog(context, category, name ->
                        new GoalAmountDialog(context, category, (unit, value) ->
                                new GoalDeadlineDialog(context, (calendar) ->
                                        saveGoal(name, category, unit, value,
                                                calendar.getTime(), callback)).show()
                        ).show()
                ).show()
        ).show();
    }

    private void saveGoal(String name, String category, GoalUnit unit, float value, Date endDate,
                          OnGoalAdded callback) {
        // create goal
        Goal goal = new Goal(name, category, endDate);
        goal.target = new GoalTarget(value, unit);
        goal.id = "originally_server_id";
        DataStore.save(goal);

        callback.onGoalAdded(goal);
    }

    public void schedule(Goal goal) {
    }


    public interface OnGoalAdded {
        void onGoalAdded(Goal goal);
    }
}
