package etien.projectandroidevents;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class EventsList extends Fragment {

    private Button googleEvents;
    ProgressBar progressBar;
    ListView list;

    public static int page_size;
    private final int NUM_EVENTS = 25;

    private String[] titles;
    private String[] descriptions;
    private String[] start_times;
    private double[] longitudes;
    private double[] latitudes;
    private String[] images;
    private String[] country_names;
    private String[] region_names;
    private String[] city_names;
    private String[] venue_address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_events, container, false);

        list = (ListView) view.findViewById(R.id.list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        googleEvents=(Button) view.findViewById(R.id.mapButton);
        googleEvents.setVisibility(View.INVISIBLE);
        googleEvents.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                intent.putExtra("Latitudes", latitudes);
                intent.putExtra("Longitudes", longitudes);
                intent.putExtra("Title",titles);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            progressBar.setVisibility(VISIBLE);

            FetchArticle fetcher = new FetchArticle();
            fetcher.execute();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            googleEvents.setVisibility(VISIBLE);
            titles = (String[]) savedInstanceState.getSerializable("titles");
            descriptions = (String[]) savedInstanceState.getSerializable("descriptions");
            start_times = (String[]) savedInstanceState.getSerializable("start_times");
            longitudes = (double[]) savedInstanceState.getSerializable("longitudes");
            latitudes = (double[]) savedInstanceState.getSerializable("latitudes");
            images = (String[]) savedInstanceState.getSerializable("images");
            country_names = (String[]) savedInstanceState.getSerializable("country_names");
            region_names = (String[]) savedInstanceState.getSerializable("region_names");
            city_names = (String[]) savedInstanceState.getSerializable("city_names");
            venue_address = (String[]) savedInstanceState.getSerializable("venue_address");

            list.setAdapter(new SimpleListAdapter());
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailsEvents.class);

                intent.putExtra("Title",titles[position]);
                intent.putExtra("StartTime",start_times[position]);
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

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        intent.putExtra("Latitudes", latitudes);
        intent.putExtra("Longitudes", longitudes);
        intent.putExtra("Title",titles);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && EventsActivity.listNeedUpdate) {
            progressBar.setVisibility(VISIBLE);
            FetchArticle fetcher = new FetchArticle();
            fetcher.execute();

            EventsActivity.listNeedUpdate = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EventsActivity.listNeedUpdate) {
            progressBar.setVisibility(VISIBLE);
            FetchArticle fetcher = new FetchArticle();
            fetcher.execute();

            EventsActivity.listNeedUpdate = false;
        }
    }

    private class SimpleListAdapter extends BaseAdapter
    {
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
                convertView = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            TextView tv2 = (TextView) convertView.findViewById(android.R.id.text2);

            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            tv.setText(titles[position % page_size]);

            tv2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorComplementaryDark));
            tv2.setText(start_times[position % page_size]);

            return convertView;
        }
    }

    private class FetchArticle extends AsyncTask<String, Object, List<OneEvent>> {

        @Override
        protected List<OneEvent> doInBackground(String... params) {

            // Fetcher un event
            page_size = NUM_EVENTS;
            EventfulAPI api = new EventfulAPI(getContext());
            List<OneEvent> events = new ArrayList<>();

            try {
                events = api.getAllEvents();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return events;
        }

        @Override
        protected void onPostExecute(List<OneEvent> events) {
            page_size = events.size();

            titles = new String[NUM_EVENTS];
            descriptions = new String[NUM_EVENTS];
            start_times = new String[NUM_EVENTS];
            longitudes = new double[NUM_EVENTS];
            latitudes = new double[NUM_EVENTS];
            images = new String[NUM_EVENTS];
            country_names = new String[NUM_EVENTS];
            region_names = new String[NUM_EVENTS];
            city_names = new String[NUM_EVENTS];
            venue_address = new String[NUM_EVENTS];

            for (int i = 0; i < page_size; i++) {
                titles[i] = events.get(i).title;
                descriptions[i] = events.get(i).description;
                start_times[i] = events.get(i).start_time;
                longitudes[i] = events.get(i).longitude;
                latitudes[i] = events.get(i).latitude;
                images[i] = events.get(i).image_url;
                country_names[i] = events.get(i).country_name;
                region_names[i] = events.get(i).region_name;
                city_names[i] = events.get(i).city_name;
                venue_address[i] = events.get(i).venue_address;
            }

            list.setAdapter(new SimpleListAdapter());
            progressBar.setVisibility(View.GONE);
            googleEvents.setVisibility(VISIBLE);
            // Set seek value
            ((EventsActivity) getActivity()).setSeekValue(latitudes,longitudes);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putSerializable("titles",titles);
        outState.putSerializable("descriptions",descriptions);
        outState.putSerializable("start_times",start_times);
        outState.putSerializable("longitudes",longitudes);
        outState.putSerializable("latitudes",latitudes);
        outState.putSerializable("images",images);
        outState.putSerializable("country_names",country_names);
        outState.putSerializable("region_names",region_names);
        outState.putSerializable("city_names",city_names);
        outState.putSerializable("venue_address",venue_address);
    }
}

