package org.airbloc.airblocsample2.rest;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.rest.results.TokenResult;

import org.airbloc.airblocsample2.rest.results.TokenResult;

import java.io.IOException;

/**
 * Copyright (C) 2015 Vista. All rights are reserved.
 *
 * Automatically loads access token to the request and
 * will refresh session and retry the request if the session is expired.
 */
public class SessionRequestInterceptor implements Interceptor {

    private static final String REFRESH_URL = Constants.ENDPOINT + "/user/";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (Session.isLoggedIn()) {
            // add access token
            request = request.newBuilder()
                    .addHeader("accesstoken", Session.getAccessToken())
                    .build();
        }

        Response response = chain.proceed(request);

        // is the session expired?
        if (response.code() == 403 || response.code() == 401) {

            // get the token
            Request tokenRequest = new Request.Builder()
                    .url(REFRESH_URL + Session.getMe().userId + "/refresh")
                    .addHeader("Access-Token", Session.getAccessToken())
                    .post(new FormEncodingBuilder()
                            .add("refreshToken", Session.getRefreshToken())
                            .build())
                    .build();

            Response tokenResponse = RestClient.getHttpClient().newCall(tokenRequest).execute();
            if (!tokenResponse.isSuccessful()) return response; // RestCallback will log error.

            // refresh the session
            TokenResult result = App.getGson()
                    .fromJson(tokenResponse.body().string(), TokenResult.class);
            Session.refreshSession(result.accessToken);

            // retry old request with new session
            Request newRequest = request.newBuilder()
                    .addHeader("Access-Token", Session.getAccessToken())
                    .build();

            // go with brand new fresh request
            return chain.proceed(newRequest);
        }

        // otherwise just go with it
        return response;
    }
}
