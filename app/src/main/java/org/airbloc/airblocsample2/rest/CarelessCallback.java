package org.airbloc.airblocsample2.rest;

import android.util.Log;

import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.Logger;
import org.airbloc.airblocsample2.rest.results.Result;

import java.util.HashMap;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * 되든 말든 알 게 뭐야.
 */
public interface CarelessCallback<T extends Result> extends RestCallback<T> {

    default void success(T result, Response response) {
        done(result);
    }

    /**
     * 로깅만 하고 끝낸다.
     */
    default void failure(RetrofitError error) {
        HashMap<String, Object> data = new HashMap<>();

        switch (error.getKind()) {

            case CONVERSION:
            case HTTP: {
                int statusCode = error.getResponse().getStatus();

                // log when failed
                if (!Constants.DEBUG) {
                    data.put("url", error.getUrl());
                    data.put("status", "HTTP " + statusCode);
                    data.put("body", error.getResponse().getBody());
                }

                if (error.getResponse() != null && error.getResponse().getBody() != null) {
                    Log.e("RestCallback", String.format("HTTP %d %s => %s", statusCode, error.getUrl(),
                            new String(((TypedByteArray) error.getResponse().getBody()).getBytes())));
                }

                Logger.e("HTTP %d %s", statusCode, error.getUrl());
                Logger.e(error.getMessage());
                break;
            }

            case UNEXPECTED: {
                if (error.getResponse() == null) {
                    data.put("url", error.getUrl());
                    data.put("status", "NONE");
                    data.put("body", error.getMessage());

                    Logger.e(error);
                    return;
                }

                int statusCode = error.getResponse().getStatus();

                // additional data
                data.put("url", error.getUrl());
                data.put("status", "HTTP " + statusCode);
                data.put("body", error.getBody().toString());

                Logger.e("HTTP %d %s", statusCode, error.getUrl());
                Logger.e("Connectivity: " + error.getMessage());
            }
        }
    }
}
