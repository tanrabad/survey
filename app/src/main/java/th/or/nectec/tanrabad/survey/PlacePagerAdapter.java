package th.or.nectec.tanrabad.survey;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PlacePagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String username;

    public PlacePagerAdapter(FragmentManager fm, Context context, String username) {
        super(fm);
        this.context = context;
        this.username = username;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return PlaceListInDatabaseFragment.newInstance();
            case 1:
                return PlaceSurveyListFragment.newInstance(username);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.find_place_by_database);
            case 1:
                return context.getResources().getString(R.string.find_place_by_recent_survey);
            default:
                return null;
        }
    }
}
