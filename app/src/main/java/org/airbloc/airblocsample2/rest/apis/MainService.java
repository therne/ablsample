package org.airbloc.airblocsample2.rest.apis;

import org.airbloc.airblocsample2.rest.RestCallback;
import org.airbloc.airblocsample2.rest.RestClient;
import org.airbloc.airblocsample2.rest.results.VersionResult;

import org.airbloc.airblocsample2.rest.results.VersionResult;

import retrofit.http.GET;

/**
 *
 */
public interface MainService {
    static MainService create() {
        return RestClient.create(MainService.class);
    }

    @GET("/version")
    void getVersion(RestCallback<VersionResult> cb);
}
