package etien.projectandroidevents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Favoris extends AppCompatActivity {

    FeedReaderDbHelper dbHelper;
    Object[] objects;

    int nbRows;
    String[] titles, starttimes, descriptions, images, country_names, region_names, city_names, venue_address;
    String[] longitudes_string, latitudes_string;
    double[] longitudes, latitudes;

    private ListView liste;

    public static boolean updateNeeded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        setTitle(R.string.app_bar_favoris);

        updateNeeded = false;

        dbHelper = new FeedReaderDbHelper(getApplicationContext());

        buildFavList();

        liste = (ListView) findViewById(R.id.listFavoris);
        liste.setAdapter(new SimpleListAdapter());

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailsEvents.class);

                intent.putExtra("Title",titles[position]);
                intent.putExtra("StartTime",starttimes[position]);
                intent.putExtra("Description",descriptions[position]);
                intent.putExtra("Longitude",longitudes[position]);
                intent.putExtra("Latitude",latitudes[position]);
                intent.putExtra("Image",images[position]);
                intent.putExtra("CountryName",country_names[position]);
                intent.putExtra("RegionName",region_names[position]);
                intent.putExtra("CityName",city_names[position]);
                intent.putExtra("VenueAddress",venue_address[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (updateNeeded) {
            buildFavList();
            ((BaseAdapter) liste.getAdapter()).notifyDataSetChanged();
            updateNeeded = false;
        }
    }

    private void buildFavList()
    {
        objects = dbHelper.readFavoris(getApplicationContext());

        nbRows = (int) objects[0];
        titles = (String[]) objects[1];
        starttimes = (String[]) objects[2];
        descriptions = (String[]) objects[3];

        // Il faut convertir ces strings en double
        // pour longitudes et latitudes
        longitudes_string = (String[]) objects[4];
        latitudes_string = (String[]) objects[5];

        images = (String[]) objects[6];
        country_names = (String[]) objects[7];
        region_names = (String[]) objects[8];
        city_names = (String[]) objects[9];
        venue_address = (String[]) objects[10];

        // Convertit les strings en double
        longitudes = new double[nbRows];
        latitudes = new double[nbRows];
        for(int i = 0; i < nbRows; i++){
            longitudes[i] = Double.parseDouble(longitudes_string[i]);
            latitudes[i] = Double.parseDouble(latitudes_string[i]);
        }
    }

    private class SimpleListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return nbRows;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            TextView tv2 = (TextView) convertView.findViewById(android.R.id.text2);

            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            tv2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorComplementaryDark));

            tv.setText(titles[position]);
            tv2.setText(starttimes[position]);

            return convertView;
        }
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
}
