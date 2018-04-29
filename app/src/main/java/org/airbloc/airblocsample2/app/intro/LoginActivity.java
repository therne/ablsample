package org.airbloc.airblocsample2.app.intro;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.airbloc.airblocsample2.Logger;
import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinActivity;
import org.airbloc.airblocsample2.models.User;
import org.airbloc.airblocsample2.rest.ErrorCallback;
import org.airbloc.airblocsample2.rest.Session;
import org.airbloc.airblocsample2.rest.apis.UserService;
import org.airbloc.airblocsample2.rest.results.TokenResult;

import java.util.Arrays;

public class LoginActivity extends SolinActivity {

    private CallbackManager callbackManager;

    ProgressShowDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        progressDialog = new ProgressShowDialog(this);
        callbackManager = CallbackManager.Factory.create();

        // Facebook Login
        findViewById(R.id.loginFacebook).setOnClickListener(v -> {
            if (!checkInternetConnection()) {
                Toast.makeText(this, "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    getFacebookUser(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    progressDialog.cancel();
                }

                @Override
                public void onError(FacebookException error) {
                    Logger.e(error);
                    Toast.makeText(LoginActivity.this, "페이스북 로그인 실패 - " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            });
        });
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFacebookUser(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, (user, response) -> {
            progressDialog.cancel();

            if (response.getError() != null) {
                FacebookRequestError error = response.getError();
                Toast.makeText(LoginActivity.this, "Failed to get User Info- " + error.getErrorMessage(),
                        Toast.LENGTH_LONG).show();
                return;
            }
//            userApi.login("facebook", user.optString("id"), token.getToken(), loginCallback);

            User newUser = new User(user.optString("name"));
            newUser.userId = user.optString("id");
            Session.saveUser(newUser);
            Session.saveTempSession();

            // go to main activity
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
/*
    ErrorCallback<TokenResult> loginCallback = (error, tokens) -> {
        if (error != null) {
            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
            Logger.e(error);
            progressDialog.cancel();
            return;
        }

        // save session
        Session.saveSession(tokens);

        userApi.getMe((ErrorCallback<User>)(err, user) -> {
            progressDialog.cancel();

            if (err != null) {
                Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                Logger.e(err);
                return;
            }

            Session.saveUser(user);

            // go to main activity
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    };*/

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        kakaoSession.removeCallback(kakaoSessionCallback);
    }

//    private class KakaoSessionStatusCallback implements SessionCallback {
//        @Override
//        public void onSessionOpened() {
//            UserManagement.requestMe(new KakaoGetUserCallback());
//        }
//
//        @Override
//        public void onSessionClosed(final KakaoException exception) {
//            if (exception != null) Logger.e(exception);
//        }
//
//        @Override
//        public void onSessionOpening() {
//        }
//    }

//    private class KakaoGetUserCallback extends MeResponseCallback {
//        @Override
//        public void onSuccess(UserProfile userProfile) {
//            progressDialog.show();
//            String accessToken = kakaoSession.getAccessToken();
//            userApi.login("kakao", String.valueOf(userProfile.getId()), accessToken, loginCallback);
//        }
//
//        @Override
//        public void onNotSignedUp() {
//            Logger.e("getKakaoUser.onNotSignedUp occured!");
//        }
//
//        @Override
//        public void onSessionClosedFailure(APIErrorResult apiErrorResult) {
//            Logger.e(apiErrorResult.getErrorMessage());
//        }
//
//        @Override
//        public void onFailure(APIErrorResult apiErrorResult) {
//            Logger.e(apiErrorResult.getErrorMessage());
//            Toast.makeText(LoginActivity.this, "카카오 로그인 실패 : " + apiErrorResult.getErrorMessage(),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
}

