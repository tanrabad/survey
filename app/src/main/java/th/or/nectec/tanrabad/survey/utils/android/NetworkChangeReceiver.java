package th.or.nectec.tanrabad.survey.utils.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

    OnNetworkChangedListener listener;

    public NetworkChangeReceiver(OnNetworkChangedListener listener) {
        this.listener = listener;
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
