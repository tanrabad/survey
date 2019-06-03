package org.tanrabad.survey.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TanRabadHttpClient {

    private static final int READ_WRITE_TIMEOUT = 30; //second
    private static final int CONNECT_TIMEOUT = 15; //second

    public static OkHttpClient build() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .readTimeout(READ_WRITE_TIMEOUT, SECONDS)
            .writeTimeout(READ_WRITE_TIMEOUT, SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, SECONDS)
            .cache(TanrabadApp.getCache());

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }
}
