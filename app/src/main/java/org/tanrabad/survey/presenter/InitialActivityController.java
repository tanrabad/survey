package org.tanrabad.survey.presenter;

import org.tanrabad.survey.utils.android.Connection;

public abstract class InitialActivityController {
    private final SyncStatus syncStatus;
    private Connection connection;

    public InitialActivityController(Connection connection, SyncStatus syncStatus) {
        this.connection = connection;
        this.syncStatus = syncStatus;
    }

    public void startInitialData() {
        if (connection.isAvailable()) {
            downloadData();
        } else {
            downloadFail();
        }
    }

    private void downloadFail() {
        if (syncStatus.isPreviousSyncStatusSuccess()) {
            onSuccess();
        } else {
            onFail();
        }
    }

    public abstract void onFail();

    public abstract void onSuccess();

    protected abstract void downloadData();

    public void downloadComplete(boolean isDownloadSuccess) {
        if (isDownloadSuccess) {
            syncStatus.setSyncStatus(true);
            onSuccess();
        } else {
            downloadFail();
        }
    }


    public interface SyncStatus {
        void setSyncStatus(boolean isSuccess);

        boolean isPreviousSyncStatusSuccess();
    }
}
