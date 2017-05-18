package etien.projectandroidevents;

import android.content.res.Resources;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import etien.projectandroidevents.R;

/**
 * Gère les tabs d'EventsActivity
 * Created by francis on 31/03/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final String[] titles;

    public ViewPagerAdapter(FragmentManager fm, Resources res) {
        super(fm);
        titles = new String[] {res.getString(R.string.options), res.getString(R.string.v_enements)};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EventsOptions();
            case 1:
                return new EventsList();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
