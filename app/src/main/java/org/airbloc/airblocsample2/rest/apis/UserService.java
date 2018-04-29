package org.airbloc.airblocsample2.rest.apis;

import org.airbloc.airblocsample2.models.TalkingButtonText;
import org.airbloc.airblocsample2.models.User;
import org.airbloc.airblocsample2.rest.RestCallback;
import org.airbloc.airblocsample2.rest.RestClient;
import org.airbloc.airblocsample2.rest.results.GoalContentResult;
import org.airbloc.airblocsample2.rest.results.HydeResult;
import org.airbloc.airblocsample2.rest.results.IdResult;
import org.airbloc.airblocsample2.rest.results.Result;
import org.airbloc.airblocsample2.rest.results.TokenResult;

import org.airbloc.airblocsample2.rest.results.GoalContentResult;
import org.airbloc.airblocsample2.rest.results.HydeResult;
import org.airbloc.airblocsample2.rest.results.IdResult;
import org.airbloc.airblocsample2.rest.results.Result;
import org.airbloc.airblocsample2.rest.results.TokenResult;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 *
 */
public interface UserService {
    static UserService create() {
        return RestClient.create(UserService.class);
    }

    @POST("/user")
    @FormUrlEncoded
    void login(@Field("accountType") String accountType,
               @Field("userId") String userId,
               @Field("userAccessToken") String accessToken,
               RestCallback<TokenResult> cb);

    @POST("/user/me/feedback")
    @FormUrlEncoded
    void sendFeedback(@Field("type") String type, @Field("message") String question,
                      RestCallback<IdResult> cb);

    @POST("/user/me/refresh")
    @FormUrlEncoded
    void refresh(@Field("refreshToken") String refreshToken,
                 RestCallback<TokenResult> cb);

    @POST("/user/me/gcm")
    @FormUrlEncoded
    void sendGcmKey(@Field("registrationId") String registrationId, RestCallback<Result> cb);

    @GET("/user/me")
    void getMe(RestCallback<User> cb);

    @POST("/user/me/analyze")
    void hydeAnalyze(@Body HydeResult result, RestCallback<Result> cb);

    @GET("/user/me/analyze")
    void hydeAnalyzeResult(RestCallback<HydeResult> cb);

    @GET("/user/me/talk")
    void getTalkingButton(RestCallback<TalkingButtonText> cb);

    @GET("/user/me/contents")
    void getContents(RestCallback<GoalContentResult> cb);
}
