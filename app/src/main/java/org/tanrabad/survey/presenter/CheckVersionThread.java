/*
 * Copyright (c) 2019 NECTEC
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

import android.os.Handler;
import android.os.Message;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.service.GithubReleaseService;
import org.tanrabad.survey.service.json.GithubReleaseJson;

public class CheckVersionThread extends Thread {

    private static final int FOUND_NEWER = 1;
    private static final int ALREADY_LATEST = 0;

    private final Handler handler;

    private boolean pause = false;

    public CheckVersionThread(CheckVersionListener listener) {
        handler = new CheckLatestVersionHandler(listener);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GithubReleaseService service = new GithubReleaseService();
        GithubReleaseJson json = service.getLatest();
        Version latest = new Version(json.tagName, json.prerelease);
        if (pause || latest.isPreRelease)
            return;

        if (latest.compareTo(new Version(BuildConfig.VERSION_NAME)) > 0) {
            Message msg = new Message();
            msg.what = FOUND_NEWER;
            msg.obj = latest;
            handler.sendMessage(msg);
        } else {
            handler.sendEmptyMessage(ALREADY_LATEST);
        }
    }

    public void pause() {
        this.pause = true;
    }

    interface CheckVersionListener {

        void onAlreadyLatest();

        void onFoundNewer(Version version);
    }


    static class CheckLatestVersionHandler extends Handler {

        private final CheckVersionListener listener;

        CheckLatestVersionHandler(CheckVersionListener listener) {
            this.listener = listener;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FOUND_NEWER:
                    listener.onFoundNewer((Version) msg.obj);
                    break;
                case ALREADY_LATEST:
                    listener.onAlreadyLatest();
                    break;
                default:
                    throw new IllegalArgumentException("Not recongnize message");
            }

        }
    }


}
