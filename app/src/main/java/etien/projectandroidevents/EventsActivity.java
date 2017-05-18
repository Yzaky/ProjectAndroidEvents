package etien.projectandroidevents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.text.Normalizer;

/**
 * Created by francis on 31/03/17.
 * Activité contenant les onglets pour lister les événements et sélectionner les options de filtrage.
 */

public class EventsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public static Category[] categories;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    static boolean listNeedUpdate;
    static boolean optionsNeedUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        setTitle(R.string.app_bar_eventsactivity);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        categories = getCategories();

        listNeedUpdate = false;
        optionsNeedUpdate = false;

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getResources());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    ////////////////////////////////////////////
    // Pour les maps

    public double[] _latitudes;
    public double[] _longitudes;

    public void setSeekValue(double[] latitudes, double[] longitudes){
        this._latitudes = latitudes;
        this._longitudes = longitudes;
    }
    public Object[] getSeekValue(){
        return new Object[]{this._latitudes,this._longitudes};
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// Handle item selection
        switch (item.getItemId()) {
            case R.id.preferences:
                startActivity(new Intent(getApplicationContext(), Preferences.class));
                return true;
            case R.id.about:
                startActivity(new Intent(getApplicationContext(), About.class));
                return true;
            case R.id.help:
                startActivity(new Intent(getApplicationContext(), Help.class));
                return true;
            case R.id.action_add_event:
                String url = "http://eventful.com/events/new";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Category[] getCategories()
    {
        Resources res = getResources();

        Category[] categories = new Category[] {
                new Category("---",                     res.getString(R.string.category_all)    ),
                new Category("art",                     res.getString(R.string.category1)       ),
                new Category("business",                res.getString(R.string.category2)       ),
                new Category("comedy",                  res.getString(R.string.category3)       ),
                new Category("music",                   res.getString(R.string.category4)       ),
                new Category("conference",              res.getString(R.string.category5)       ),
                new Category("learning_education",      res.getString(R.string.category6)       ),
                new Category("festivals_parades",       res.getString(R.string.category7)       ),
                new Category("movies_film",             res.getString(R.string.category8)       ),
                new Category("food",                    res.getString(R.string.category9)       ),
                new Category("fundraisers",             res.getString(R.string.category10)      ),
                new Category("support",                 res.getString(R.string.category11)      ),
                new Category("holiday",                 res.getString(R.string.category12)      ),
                new Category("family_fun_kids",         res.getString(R.string.category13)      ),
                new Category("books",                   res.getString(R.string.category14)      ),
                new Category("attractions",             res.getString(R.string.category15)      ),
                new Category("community",               res.getString(R.string.category16)      ),
                new Category("singles_social",          res.getString(R.string.category17)      ),
                new Category("clubs_associations",      res.getString(R.string.category18)      ),
                new Category("outdoors_recreation",     res.getString(R.string.category19)      ),
                new Category("performing_arts",         res.getString(R.string.category20)      ),
                new Category("animals",                 res.getString(R.string.category21)      ),
                new Category("politics_activism",       res.getString(R.string.category22)      ),
                new Category("religion_spirituality",   res.getString(R.string.category23)      ),
                new Category("sales",                   res.getString(R.string.category24)      ),
                new Category("science",                 res.getString(R.string.category25)      ),
                new Category("sports",                  res.getString(R.string.category26)      ),
                new Category("technology",              res.getString(R.string.category27)      ),
                new Category("schools_alumni",          res.getString(R.string.category28)      ),
                new Category("others",                  res.getString(R.string.category_other)  )
        };

        boolean oneActive = false;
        for (Category category : categories) {
            /*
            editor.putBoolean(category.getId(), false);
            editor.commit();
            */
            category.setActive(prefs.getBoolean(category.getId(), false));

            if (category.isActive())
                oneActive = true;
        }

        if (!oneActive) {
            categories[0].setActive(true);
            editor.putBoolean(categories[0].getId(), true);
            editor.commit();
        }

        for (int i = 1; i < categories.length - 2; i++) {
            for (int j = i + 1; j < categories.length - 1; j++) {
                if (Normalizer.normalize(categories[i].getTitle(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").compareTo(
                        Normalizer.normalize(categories[j].getTitle(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")) > 0) {
                    Category temp = categories[i];
                    categories[i] = categories[j];
                    categories[j] = temp;
                }
            }
        }

        return categories;
    }
}
