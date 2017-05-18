package etien.projectandroidevents;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class EventfulAPI {
    private SharedPreferences prefs;
    private final String URLBase = "http://api.eventful.com/json/events/search?app_key=L9CqLCjxnqF2Fx6q&page_size=25&date=Future";
    private String URL;

    protected static OkHttpClient http = null;

    public EventfulAPI(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        URL = URLBase;

        LocationManager lm;
        Location location = null;

        int searchOption = prefs.getInt("searchType", R.id.geolocRadioButton);

        if (searchOption == R.id.geolocRadioButton) {
            if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if (location == null) {
                searchOption = R.id.cityRadioButton;
            }
        }

        switch(searchOption) {
            case R.id.geolocRadioButton:
                URL += "&location=" + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
                URL += "&within=" + prefs.getString("radius", "20");
                if (!prefs.getBoolean("useMiles", false)) {
                    URL += "&units=km";
                }
                break;
            case R.id.zipCodeRadioButton:
                URL += "&location=" + prefs.getString("zipCode", "H3B 3A5");
                URL += "&within=" + prefs.getString("radius", "20");
                if (!prefs.getBoolean("useMiles", false)) {
                    URL += "&units=km";
                }
                break;
            default:
                URL += "&location=" + prefs.getString("city", "Montreal");
                break;
        }

        for (Category category : EventsActivity.categories) {
            if (category.isActive()) {
                if (!category.getId().equals("---")) {
                    URL += "&c=" + category.getId();
                }
                break;
            }
        }

    }

    private JSONObject getJSON(String url) throws IOException, JSONException {

        if (http == null) {
            http = new OkHttpClient();
        }

        Request request = new Request.Builder().url(url).build();
        Response response = http.newCall(request).execute();

        String json = response.body().string();

        return new JSONObject(json);
    }

    public List<OneEvent> getAllEvents() throws IOException, JSONException {

        // Parse le JSON
        JSONObject object = getJSON(URL);
        List<OneEvent> allEvents = new ArrayList<>();

        int i = 0;  // Pour le catch lorsqu'il n'y a pas assez d'events correspondant aux critères de recherche

        try {
            JSONArray events = object.getJSONObject("events").getJSONArray("event");

            for (i = 0; i < EventsList.page_size; i++) {
                JSONObject anEvent = events.getJSONObject(i);

                String urlimage = null;

                try {
                    if (anEvent.getJSONObject("image") != null) {
                        urlimage = anEvent.getJSONObject("image").getJSONObject("medium").getString("url");
                    }
                } catch (Exception e) {
                    // Do nothing
                }

                allEvents.add(new OneEvent(
                        anEvent.getString("title"),
                        anEvent.getString("description"),
                        anEvent.getString("start_time"),
                        anEvent.getDouble("longitude"),
                        anEvent.getDouble("latitude"),
                        anEvent.getString("country_name"),
                        anEvent.getString("region_name"),
                        anEvent.getString("city_name"),
                        anEvent.getString("venue_address"),
                        urlimage));
            }
        } catch (Exception e) {
            EventsList.page_size = i;
        }

        Collections.sort(allEvents,new Comparator<OneEvent>(){
            public int compare(OneEvent e1, OneEvent e2){
                String time1 = e1.start_time.substring(0,10).replace("-","");
                String time2 = e2.start_time.substring(0,10).replace("-","");
                Integer int1 = Integer.parseInt(time1);
                Integer int2 = Integer.parseInt(time2);
                return int1 - int2;
            }});

        return allEvents;
    }
}
