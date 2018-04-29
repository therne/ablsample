package org.airbloc.airblocsample2.rest;

import org.airbloc.airblocsample2.rest.results.Result;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 수동으로 에러처리한다.
 */
public interface ErrorCallback<T extends Result> extends RestCallback<T> {

    void done(RetrofitError err, T result);
    default void done(T result) {
        done(null, result);
    }

    default void success(T result, Response response) {
        done(null, result);
    }

    default void failure(RetrofitError error) {
        done(error, null);
    }
}
