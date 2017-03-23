package etien.projectandroidevents;

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

public class EventfulAPI {

    String urlSport = "http://api.eventful.com/json/events/search?app_key=L9CqLCjxnqF2Fx6q&id=sports&location=Montreal&date=Future&page_size=50";

    protected static OkHttpClient http = null;

    public EventfulAPI() {}

    public JSONObject getJSON(String url) throws IOException, JSONException {

        if (http == null) {

        }
            http = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        Response response = http.newCall(request).execute();

        String json = response.body().string();

        return new JSONObject(json);
    }

    public List<OneEvent> getAllEvents() throws IOException, JSONException {

        // Parse le JSON
        JSONObject object = getJSON(urlSport);
        int page_size = object.getInt("page_size");
        JSONArray events = object.getJSONObject("events").getJSONArray("event");
        List<OneEvent> allEvents = new ArrayList<OneEvent>();

        for (int i = 0; i < page_size; i++) {
            JSONObject anEvent = events.getJSONObject(i);
            allEvents.add(new OneEvent(
                    anEvent.getString("title"),
                    anEvent.getString("description"),
                    anEvent.getString("start_time"),
                    anEvent.getDouble("longitude"),
                    anEvent.getDouble("latitude")));
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
