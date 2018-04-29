
package org.airbloc.airblocsample2.app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.TextView;

import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinActivity;
import org.airbloc.airblocsample2.app.intro.LoginActivity;
import org.airbloc.airblocsample2.app.intro.WelcomeActivity;
import org.airbloc.airblocsample2.datamgmt.DataCache;
import org.airbloc.airblocsample2.datamgmt.DataStore;
import org.airbloc.airblocsample2.rest.Session;

public class SettingsActivity extends SolinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        setTitle(""); // space for custom text

        // push settings
        TextView pushEnabled = findView(R.id.settingMenuPushState);
        pushEnabled.setText(Settings.isPushEnabled() ? "켜짐" : "꺼짐");

        findViewById(R.id.settingMenuTutorial).setOnClickListener(v -> {
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.putExtra("showingAgain", true);
            startActivity(intent);
        });

        findView(R.id.settingMenuPush).setOnClickListener(v -> {
            if (Settings.isPushEnabled()) {
                new AlertDialog.Builder(this)
                        .setMessage("푸시 알림을 끄면 SOLIN이 제공하는 동기부여 기능, 일침 기능 및 일일 달성량 체크 알림을 받으실 수 없으며 " +
                                "사용자님의 목표 달성에 영향을 초래할 수 있습니다. 그래도 끄시겠습니까?")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            Settings.setPushEnabled(false);
                            pushEnabled.setText("꺼짐");
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            } else {
                Settings.setPushEnabled(true);
                pushEnabled.setText("켜짐");
            }
        });

        // Logout
        findView(R.id.settingMenuLogout).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage("정말로 로그아웃하시겠습니까?")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    logout();
                })
                .setNegativeButton(android.R.string.no, (dialog1, which1) -> {})
                .show());

        // About
        findViewById(R.id.settingMenuDevInfo).setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));

        // Terms
        findViewById(R.id.settingMenuPrivacyPolicy).setOnClickListener(v ->
                startActivity(new Intent(this, TermsActivity.class)));
        findViewById(R.id.settingMenuTerms).setOnClickListener(v ->
                startActivity(new Intent(this, TermsActivity.class)));

        findViewById(R.id.settingMenuHelp).setOnClickListener(v ->
                startActivity(new Intent(this, HelpActivity.class)));
    }

    private void logout() {
        Session.clearSession();
        DataStore.deleteAll();
        DataCache.clear();
//        new ResultInputAlarmer(this).removeAll();
//      new NaggingStartAlarmer(this).removeAll();

        // Facebook / Kakao Session clear
//        com.kakao.auth.Session kakaoSession = com.kakao.auth.Session.getCurrentSession();
//        if (kakaoSession != null && kakaoSession.isOpened()) {
//            UserManagement.requestLogout(new LogoutResponseCallback() {
//                @Override
//                public void onSuccess(long l) {
//                    backToLogin();
//                }
//
//                @Override
//                public void onFailure(APIErrorResult apiErrorResult) {
//                    Logger.e(apiErrorResult.getErrorMessage());
//                    Toast.makeText(SettingsActivity.this, "카카오 로그아웃 실패 : " + apiErrorResult.getErrorMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            });
//
//        } else {
//            com.facebook.Session fbSession = com.facebook.Session.getActiveSession();
//            if (fbSession != null) fbSession.closeAndClearTokenInformation();
            backToLogin();
//        }
    }

    private void backToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
