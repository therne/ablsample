package org.airbloc.airblocsample2.rest.apis;

import org.airbloc.airblocsample2.models.Diary;
import org.airbloc.airblocsample2.models.Goal;
import org.airbloc.airblocsample2.models.Report;
import org.airbloc.airblocsample2.rest.RestCallback;
import org.airbloc.airblocsample2.rest.RestClient;
import org.airbloc.airblocsample2.rest.results.GoalIdResult;
import org.airbloc.airblocsample2.rest.results.GoalListResult;
import org.airbloc.airblocsample2.rest.results.GoalOverviewData;
import org.airbloc.airblocsample2.rest.results.NameSuggestionResult;
import org.airbloc.airblocsample2.rest.results.Result;

import org.airbloc.airblocsample2.rest.results.GoalIdResult;
import org.airbloc.airblocsample2.rest.results.GoalListResult;
import org.airbloc.airblocsample2.rest.results.GoalOverviewData;
import org.airbloc.airblocsample2.rest.results.NameSuggestionResult;
import org.airbloc.airblocsample2.rest.results.Result;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 *
 */
public interface GoalService {
    static GoalService create() {
        return RestClient.create(GoalService.class);
    }

    @POST("/user/me/goal")
    void add(@Body Goal goal,
               RestCallback<GoalIdResult> cb);

    @GET("/goal/{id}")
    void get(@Path("id") String goalId,
                 RestCallback<Goal> cb);

    @DELETE("/goal/{id}")
    void remove(@Path("id") String goalId, RestCallback<Result> cb);

    @GET("/user/me/goals")
    void getGoals(RestCallback<GoalListResult> cb);

    @POST("/goal/{id}")
    @FormUrlEncoded
    void sendResult(@Path("id") String goalId,
                 @Field("percent") int percent,
                 @Field("result") int result,
                 RestCallback<Result> cb);

    @GET("/goal/{id}/collect")
    void getOverview(@Path("id") String goalId, RestCallback<GoalOverviewData> cb);

    @GET("/goal/{id}/farewell")
    void farewell(@Path("id") String goalId, RestCallback<Report> cb);

    @POST("/goal/{id}/diary")
    @FormUrlEncoded
    void diary(@Path("id") String goalId, @Field("content") String text, RestCallback<Diary> cb);

    @GET("/goal/{category}/suggest")
    void suggestName(@Path("category") String category, RestCallback<NameSuggestionResult> cb);
}
