package org.tanrabad.survey.presenter.authen;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import org.tanrabad.survey.R;
import org.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;

public class ChromeCustomTabs {

    public static final String PACKAGE_NAME = "com.android.chrome";

    /**
     * Check if Chrome CustomTabs are supported.
     * Some devices don't have Chrome or it may not be
     * updated to a version where custom tabs is supported.
     *
     * @param context the context
     * @return whether custom tabs are supported
     */
    public static boolean isSupported(@NonNull final Context context) {
        Intent serviceIntent = new Intent("android.support.customtabs.action.CustomTabsService");
        serviceIntent.setPackage(PACKAGE_NAME);

        CustomTabsServiceConnection serviceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(final ComponentName componentName, final CustomTabsClient customTabsClient) { }

            @Override
            public void onServiceDisconnected(final ComponentName name) { }
        };

        boolean customTabsSupported =
            context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE | Context.BIND_WAIVE_PRIORITY);
        context.unbindService(serviceConnection);

        return customTabsSupported;
    }

    public static void installChrome(@NonNull final Activity context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGE_NAME)));
        }
    }

    public static void showInstallPromptDialog(final Activity activity, String title, String message, String positiveText) {
        AlertDialogPromptMessage prompt = new AlertDialogPromptMessage(activity);
        prompt.setOnConfirm(positiveText, () -> {
            ChromeCustomTabs.installChrome(activity);
            activity.finishAffinity();
        });
        prompt.setOnCancel(activity.getString(R.string.cancel), () -> { });
        prompt.show(title, message);
    }

    public static void showInstallPromptDialog(final Activity activity) {
        showInstallPromptDialog(activity,
            activity.getString(R.string.install_google_chrome),
            activity.getString(R.string.install_google_chrome_descript),
            activity.getString(R.string.install)
        );
    }

}
