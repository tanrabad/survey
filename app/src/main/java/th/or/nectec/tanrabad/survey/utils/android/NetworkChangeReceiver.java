package th.or.nectec.tanrabad.survey.utils.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private OnNetworkChangedListener listener;

    public NetworkChangeReceiver(OnNetworkChangedListener listener) {
        this.listener = listener;
    }

    public static IntentFilter getIntentFilter() {
        return new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (InternetConnection.isAvailable(context)) {
            if (listener != null)
                listener.onNetworkChanged(true);
        } else {
            if (listener != null)
                listener.onNetworkChanged(false);
        }
    }

    public interface OnNetworkChangedListener {
        void onNetworkChanged(boolean isConnected);
    }
}
