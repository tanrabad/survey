package org.tanrabad.survey.utils.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection implements Connection {

    private final Context context;

    public InternetConnection(Context context) {
        this.context = context;
    }

    public static boolean isAvailable(Context context) {
        return new InternetConnection(context).isAvailable();
    }

    @Override
    public boolean isAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
