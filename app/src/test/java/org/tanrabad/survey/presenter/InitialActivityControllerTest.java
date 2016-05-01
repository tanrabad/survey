package org.tanrabad.survey.presenter;

import org.junit.Test;
import org.tanrabad.survey.utils.android.Connection;

import static org.mockito.Mockito.*;
import static org.tanrabad.survey.presenter.InitialActivityController.SyncStatus;

public class InitialActivityControllerTest {
    private final Connection connection = mock(Connection.class);
    private final SyncStatus syncStatus = mock(SyncStatus.class);
    boolean isDownloadSuccess = false;

    private InitialActivityController initialActivityController =
            spy(new InitialActivityController(connection, syncStatus) {

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
