package org.airbloc.airblocsample2.rest.apis;

import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.rest.RestCallback;
import org.airbloc.airblocsample2.rest.RestClient;
import org.airbloc.airblocsample2.rest.results.HydeAnalyzeResult;

import org.airbloc.airblocsample2.rest.results.HydeAnalyzeResult;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 *
 */
public interface HydeService {
    static HydeService create() {
        return RestClient.createAdapter(Constants.HYDE_ENDPOINT)
                .create(HydeService.class);
    }

   @POST("/analyze")
   @FormUrlEncoded
    void analyzePersonality(@Field("result") String result, RestCallback<HydeAnalyzeResult> cb);
}
