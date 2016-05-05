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

import org.junit.Test;
import org.tanrabad.survey.utils.android.Connection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.tanrabad.survey.presenter.AbsInitialActivityController.SyncStatus;

public class AbsInitialActivityControllerTest {
    private final Connection connection = mock(Connection.class);
    private final SyncStatus syncStatus = mock(SyncStatus.class);
    boolean isDownloadSuccess = false;

    private AbsInitialActivityController initialActivityController =
            spy(new AbsInitialActivityController(connection, syncStatus) {

                @Override
                public void onFail() {

                }

                @Override
                public void onSuccess() {

                }

                @Override
                protected void downloadData() {
                    downloadComplete(isDownloadSuccess);
                }
            });

    @Test
    public void testDownloadFirstTimeWhenNoInternetMustFail() throws Exception {
        when(connection.isAvailable()).thenReturn(false);
        when(syncStatus.isPreviousSyncStatusSuccess()).thenReturn(false);

        initialActivityController.startInitialData();

        verify(connection).isAvailable();
        verify(syncStatus).isPreviousSyncStatusSuccess();
        verify(initialActivityController).onFail();
    }

    @Test
    public void testSuccessWhenNoInternetAndPreviousSyncIsSuccess() throws Exception {
        when(connection.isAvailable()).thenReturn(false);
        when(syncStatus.isPreviousSyncStatusSuccess()).thenReturn(true);

        initialActivityController.startInitialData();

        verify(connection).isAvailable();
        verify(syncStatus).isPreviousSyncStatusSuccess();
        verify(initialActivityController).onSuccess();
    }

    @Test
    public void testDownloadSuccess() throws Exception {
        when(connection.isAvailable()).thenReturn(true);
        when(syncStatus.isPreviousSyncStatusSuccess()).thenReturn(true);


        isDownloadSuccess = true;
        initialActivityController.startInitialData();

        verify(connection).isAvailable();
        verify(initialActivityController).downloadData();
        verify(syncStatus).setSyncStatus(true);
        verify(initialActivityController).onSuccess();
        verify(initialActivityController, never()).onFail();
    }

    @Test
    public void testDownloadFailButPreviousSyncSuccess() throws Exception {
        when(connection.isAvailable()).thenReturn(true);
        when(syncStatus.isPreviousSyncStatusSuccess()).thenReturn(true);

        isDownloadSuccess = false;
        initialActivityController.startInitialData();

        verify(connection).isAvailable();
        verify(syncStatus).isPreviousSyncStatusSuccess();
        verify(initialActivityController).onSuccess();
        verify(initialActivityController, never()).onFail();
    }

    @Test
    public void testDownloadFailButPreviousSyncFail() throws Exception {
        when(connection.isAvailable()).thenReturn(true);
        when(syncStatus.isPreviousSyncStatusSuccess()).thenReturn(false);

        isDownloadSuccess = false;
        initialActivityController.startInitialData();

        verify(connection).isAvailable();
        verify(syncStatus).isPreviousSyncStatusSuccess();
        verify(initialActivityController).onFail();
        verify(initialActivityController, never()).onSuccess();
    }
}
