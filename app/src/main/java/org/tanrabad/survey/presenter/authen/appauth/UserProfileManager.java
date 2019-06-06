/*
 * Copyright (c) 2018 NECTEC
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

package org.tanrabad.survey.presenter.authen.appauth;

import android.content.Context;
import android.content.SharedPreferences;
import com.bluelinelabs.logansquare.LoganSquare;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;
import org.tanrabad.survey.presenter.authen.UserProfile;

public class UserProfileManager {

    private static final AtomicReference<WeakReference<UserProfileManager>> INSTANCE_REF =
        new AtomicReference<>(new WeakReference<>(null));

    private static final String STORE_NAME = "UserProfile";
    private static final String KEY_STATE = "profile";

    private SharedPreferences pref;
    private AtomicReference<UserProfile> profile;

    public static UserProfileManager getInstance(Context context) {
        UserProfileManager manager = INSTANCE_REF.get().get();
        if (manager == null) {
            manager = new UserProfileManager(context.getApplicationContext());
            INSTANCE_REF.set(new WeakReference<>(manager));
        }
        return manager;
    }

    private UserProfileManager(Context context) {
        pref = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        profile = new AtomicReference<>();
    }

    public void save(UserProfile profile) {
        this.profile.set(profile);
        try {
            pref.edit().putString(KEY_STATE, LoganSquare.serialize(profile)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        profile.set(null);
        pref.edit().clear().apply();
    }

    UserProfile getProfile() {
        if (profile.get() != null)
            return profile.get();

        String save = pref.getString(KEY_STATE, null);
        if (save != null) {
            try {
                profile.compareAndSet(null, UserProfile.fromJson(save));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return profile.get();
    }

}
