package org.airbloc.airblocsample2.app;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.Logger;
import org.airbloc.airblocsample2.app.addgoal.GoalAdder;
import org.airbloc.airblocsample2.app.settings.Settings;
import org.airbloc.airblocsample2.datamgmt.DataStore;
import org.airbloc.airblocsample2.models.Goal;

import org.airbloc.airblocsample2.app.addgoal.GoalAdder;
import org.airbloc.airblocsample2.app.settings.Settings;

import java.util.List;

/**
 * 버전 업데이트를 담당한다.
 */
public class MigrationManager {
    private Context context;
    public static boolean migratedNow = false;

    public MigrationManager(Context context) {
        this.context = context;
    }

    /**
     * 버전을 마이그레이션한다.
     */
    public void migrate() {
        int prevVersion = Settings.getVersion();
        if (prevVersion == Constants.VERSION_CODE) return; // current version. skip.

        Logger.d("Migrating version %d to %d", prevVersion, Constants.VERSION_CODE);

        migratedNow = true;
        Settings.updateVersion();
    }

    private void removeAlarmPre2(AlarmManager alarmManager, Goal goal) {
        // cancel result input alarm
        Intent intent = new Intent(Constants.ACTION_RESULT_INPUT);
        intent.putExtra("goal", goal.toParcel());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);

        // cancel done alarm
        intent = new Intent(Constants.ACTION_DONE);
        intent.putExtra("goal", goal.toParcel());
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);

        // remove possible notifications
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel("Goal_" + goal.getId(), 0);
        notificationManager.cancel("GoalDone_" + goal.getId(), 0);
    }
}
