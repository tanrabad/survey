/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tanrabad.survey.presenter;

import org.tanrabad.survey.utils.android.Connection;

public abstract class AbsInitialActivityController {
    private final SyncStatus syncStatus;
    private Connection connection;

    public AbsInitialActivityController(Connection connection, SyncStatus syncStatus) {
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
