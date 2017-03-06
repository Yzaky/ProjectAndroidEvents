package etien.projectandroidevents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventfulAPI {

    String urlSport = "http://api.eventful.com/json/events/search?app_key=L9CqLCjxnqF2Fx6q&id=sports&location=Montreal&date=Future";

    protected static OkHttpClient http = null;

    public EventfulAPI() {}

    public JSONObject getJSON(String url) throws IOException, JSONException {

        if (http == null)
            http = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();
        Response response = http.newCall(request).execute();

        String json = response.body().string();

        return new JSONObject(json);
    }

    public OneEvent[] getAllEvents() throws IOException, JSONException {

        // Parse le JSON
        JSONObject object = getJSON(urlSport);
        int page_size = object.getInt("page_size");
        JSONArray events = object.getJSONObject("events").getJSONArray("event");
        OneEvent[] allEvents = new OneEvent[page_size];

        for (int i = 0; i < page_size; i++) {
            JSONObject anEvent = events.getJSONObject(i);
            allEvents[i] = new OneEvent(
                    anEvent.getString("title"),
                    anEvent.getString("description"),
                    anEvent.getString("start_time"));
        }

        return allEvents;
    }
}
