package org.airbloc.airblocsample2.rest;

import android.util.Log;
import android.widget.Toast;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.Logger;
import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.rest.results.Result;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.RetrofitError.Kind;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 *
 */
public interface RestCallback<T extends Result> extends Callback<T> {

    void done(T result);

    default void success(T result, Response response) {
        done(result);
    }

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

                // notify error to user using user-readable messages
                try {
                    Result result = error.getKind() == Kind.HTTP ?
                            (Result) error.getBodyAs(Result.class) : null;

                    Toast.makeText(App.getContext(),
                            Errors.getErrorMessage(statusCode, result),
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    // ok, this is conversion error
                    // TODO: 이상하게 500이 오래걸리고, JSON이 잘못 올때 망함.
                    Toast.makeText(App.getContext(),
                            Errors.getErrorMessage(statusCode, new Result()),
                            Toast.LENGTH_LONG).show();

                }
                break;
            }

            // Connectivity Issue
            case NETWORK:
                Toast.makeText(App.getContext(), R.string.error_network, Toast.LENGTH_LONG).show();
                break;

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
