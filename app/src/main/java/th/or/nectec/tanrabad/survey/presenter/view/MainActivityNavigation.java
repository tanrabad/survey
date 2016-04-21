package th.or.nectec.tanrabad.survey.presenter.view;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import th.or.nectec.tanrabad.entity.Organization;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.AboutActivity;
import th.or.nectec.tanrabad.survey.presenter.AccountUtils;
import th.or.nectec.tanrabad.survey.presenter.LoginActivity;
import th.or.nectec.tanrabad.survey.presenter.PreferenceActivity;
import th.or.nectec.tanrabad.survey.repository.BrokerOrganizationRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class MainActivityNavigation {

    public static void setup(final Activity activity) {
        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        setupHeaderView(navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.trb_watch:
                        Intent trbWatch = new Intent(Intent.ACTION_VIEW, Uri.parse("https://watch.tanrabad.org"));
                        activity.startActivity(trbWatch);
                        break;
                    case R.id.trb_report:
                        Intent trbReport = new Intent(Intent.ACTION_VIEW, Uri.parse("https://report.tanrabad.org"));
                        activity.startActivity(trbReport);
                        break;
                    case R.id.trb_bi:
                        Intent trbBi = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bi.tanrabad.org"));
                        activity.startActivity(trbBi);
                        break;
                    case R.id.manual:
                        Intent manual = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://tanrabad.gitbooks.io/tanrabad-survey/content/index.html"));
                        activity.startActivity(manual);
                        break;
                    case R.id.about:
                        AboutActivity.open(activity);
                        break;
                    case R.id.preferences:
                        PreferenceActivity.open(activity);
                        break;
                    case R.id.logout:
                        AccountUtils.clear();
                        Intent backToLogin = new Intent(activity, LoginActivity.class);
                        activity.startActivity(backToLogin);
                        activity.finish();
                        break;
                }
                return false;
            }
        });


        activity.findViewById(R.id.drawer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private static void setupHeaderView(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);

        final User user = AccountUtils.getUser();
        ImageView avatarImageView = (ImageView) header.findViewById(R.id.avatar_icon);
        avatarImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Alert.lowLevel().show(user.getApiFilter());
                return true;
            }
        });

        TextView userNameTextView = (TextView) header.findViewById(R.id.username);
        userNameTextView.setText(user.getUsername());

        TextView userFullNameTextView = (TextView) header.findViewById(R.id.user_fullname);
        userFullNameTextView.setText(String.format("%s %s", user.getFirstname(), user.getLastname()));

        Organization organization = BrokerOrganizationRepository.getInstance().findById(user.getOrganizationId());
        TextView organizationTextView = (TextView) header.findViewById(R.id.organization);
        organizationTextView.setText(organization.getName());
    }
}
