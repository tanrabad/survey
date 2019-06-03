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

package org.tanrabad.survey.presenter.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.Organization;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.job.UploadJobRunner;
import org.tanrabad.survey.presenter.AboutActivity;
import org.tanrabad.survey.presenter.AccountUtils;
import org.tanrabad.survey.presenter.PreferenceActivity;
import org.tanrabad.survey.presenter.authen.ChromeCustomTabs;
import org.tanrabad.survey.presenter.authen.appauth.AppAuthPresenter;
import org.tanrabad.survey.repository.BrokerOrganizationRepository;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.InternetConnection;

public final class MainActivityNavigation {

    private MainActivityNavigation() {
    }

    public static void setup(Activity activity) {
        NavigationView navigationView = activity.findViewById(R.id.navigation);
        if (navigationView == null) {
            TanrabadApp.log(new IllegalArgumentException("NavigationView of MainActivity is Null"));
            return;
        }
        navigationView.setItemIconTintList(null);
        setupHeaderView(navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationViewSelected(activity));
        setupDrawerButton(activity);
    }

    private static void setupHeaderView(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);

        final User user = AccountUtils.getUser();

        TextView userNameTextView = header.findViewById(R.id.username);
        userNameTextView.setText(user.getUsername());

        TextView userFullNameTextView = header.findViewById(R.id.user_fullname);
        userFullNameTextView.setText(String.format("%s %s", user.getFirstname(), user.getLastname()));

        Organization organization = BrokerOrganizationRepository.getInstance().findById(user.getOrganizationId());
        TextView organizationTextView = header.findViewById(R.id.organization);
        organizationTextView.setText(organization.getName());
    }

    private static void setupDrawerButton(final Activity activity) {
        activity.findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        User user = AccountUtils.getUser();
        TextView userFullNameTextView = (TextView) activity.findViewById(R.id.user_fullname);
        userFullNameTextView.setText(String.format("%s %s", user.getFirstname(), user.getLastname()));

        Organization organization = BrokerOrganizationRepository.getInstance().findById(user.getOrganizationId());
        TextView organizationTextView = (TextView) activity.findViewById(R.id.organization);
        organizationTextView.setText(organization.getName());
    }

    private static class NavigationViewSelected implements OnNavigationItemSelectedListener {

        private final Activity activity;

        public NavigationViewSelected(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.trb_watch:
                    openWeb(Uri.parse("https://watch.tanrabad.org"));
                    break;
                case R.id.trb_report:
                    openWeb(Uri.parse("https://report.tanrabad.org"));
                    break;
                case R.id.trb_bi:
                    openWeb(Uri.parse("https://bi.tanrabad.org"));
                    break;
                case R.id.manual:
                    openWeb(Uri.parse("https://tanrabad.gitbooks.io/survey-manual/content/index.html"));
                    break;
                case R.id.about:
                    AboutActivity.open(activity);
                    break;
                case R.id.preferences:
                    PreferenceActivity.open(activity);
                    break;
                case R.id.logout:
                    if (!InternetConnection.isAvailable(activity)) {
                        Alert.highLevel().show(R.string.please_connect_internet_before_logout);
                        return false;
                    }
                    AppAuthPresenter auth = new AppAuthPresenter(activity);
                    UploadJobRunner uploadJob = new UploadJobRunner();
                    uploadJob.addJobs(new UploadJobRunner.Builder().getJobs());
                    uploadJob.setOnSyncFinishListener(() -> {
                        if (ChromeCustomTabs.isSupported(activity)) {
                            auth.logout();
                        } else {
                            ChromeCustomTabs.showInstallPromptDialog(activity);
                        }
                    });
                    uploadJob.start();
                    break;
            }
            return false;
        }

        private void openWeb(Uri parse) {
            Intent intent = new Intent(Intent.ACTION_VIEW, parse);
            activity.startActivity(intent);
        }
    }
}
