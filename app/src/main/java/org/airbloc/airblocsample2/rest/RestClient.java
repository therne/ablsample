package org.airbloc.airblocsample2.rest;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * REST API를 제공하는 모듈.
 */
public class RestClient {
    private static RestAdapter restAdapter;
    private static OkHttpClient httpClient = new OkHttpClient();
    private static HashMap<Class, Object> caches = new HashMap<>();

    public static void init() {
        // build HTTP (OkHttp) client
        httpClient.interceptors().add(new SessionRequestInterceptor());

        // build REST (Retrofit) client
        restAdapter = createAdapter(Constants.ENDPOINT);
    }

    public static RestAdapter createAdapter(String endpoint) {
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(App.getGson()))
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setClient(new OkClient(httpClient))
                .build();
    }

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 새로운 Retrofit Service를 만들거나, 캐싱된 저장소에서 가져온다.
     * @param clazz Service class
     * @return Retrofit Service
     */
    public static <T> T create(Class<T> clazz) {
        Object cached = caches.get(clazz);
        if (cached != null) return (T) cached;

        T created = restAdapter.create(clazz);
        caches.put(clazz, created);
        return created;
    }
}
