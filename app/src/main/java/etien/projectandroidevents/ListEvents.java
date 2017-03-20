package etien.projectandroidevents;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListEvents extends AppCompatActivity {

    ListView liste;
    ProgressBar progressBar;

    int page_size = 50;
    private String[] titles = new String[page_size];
    private String[] descriptions = new String[page_size];
    private String[] start_times = new String[page_size];
    private double[] longitudes = new double[page_size];
    private double[] latitudes = new double[page_size];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        FetchArticle fetcher = new FetchArticle();
        fetcher.execute();

        liste = (ListView) findViewById(R.id.list);

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailsEvents.class);

                intent.putExtra("Title",titles[position]);
                intent.putExtra("StartTime",start_times[position]);
                intent.putExtra("Description",descriptions[position]);
                intent.putExtra("Longitude",longitudes[position]);
                intent.putExtra("Latitude",latitudes[position]);

                startActivity(intent);
            }
        });
    }



    public class SimpleListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return page_size;
        }

        @Override
        public Object getItem(int position) {
            return 0;
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

            tv.setText(titles[position % page_size]);
            tv2.setText(start_times[position % page_size]);

            return convertView;
        }
    }

    public class FetchArticle extends AsyncTask<String, Object, List<OneEvent>> {

        @Override
        protected List<OneEvent> doInBackground(String... params) {

            // Fetcher un event

            EventfulAPI api = new EventfulAPI();

            List<OneEvent> events = new ArrayList<OneEvent>();

            try {
                events = api.getAllEvents();

            } catch (IOException |JSONException e) {
                e.printStackTrace();
            }

            return events;
        }

        @Override
        protected void onPostExecute(List<OneEvent> events) {

            for(int i=0; i<page_size; i++) {
                titles[i] = events.get(i).title;
                descriptions[i] = events.get(i).description;
                start_times[i] = events.get(i).start_time;
                longitudes[i] = events.get(i).longitude;
                latitudes[i] = events.get(i).latitude;
            }

            liste.setAdapter(new SimpleListAdapter());
            progressBar.setVisibility(View.GONE);

        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }
}
