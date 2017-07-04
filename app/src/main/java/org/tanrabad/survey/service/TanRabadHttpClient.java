package org.tanrabad.survey.service;

import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.utils.http.FileCertificateAuthority;

import java.io.InputStream;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TanRabadHttpClient {

    private static final int READ_WRITE_TIMEOUT = 10; //second
    private static final int CONNECT_TIMEOUT = 5; //second

    public static OkHttpClient build() {
        try {
            InputStream certificate = TanrabadApp.getInstance()
                .getResources().openRawResource(R.raw.tanrabad);
            FileCertificateAuthority ca = new FileCertificateAuthority(certificate);
            return new OkHttpClient.Builder()
                .readTimeout(READ_WRITE_TIMEOUT, SECONDS)
                .writeTimeout(READ_WRITE_TIMEOUT, SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, SECONDS)
                .sslSocketFactory(ca.getSslContext().getSocketFactory(),
                    (X509TrustManager) ca.getTrustManagers()[0])
                .build();
        } catch (NullPointerException ignoreOnUnitTest) {
            return new OkHttpClient();
        }
    }
}
