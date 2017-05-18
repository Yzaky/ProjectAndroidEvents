package etien.projectandroidevents;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AutocompleteAPI
{
    private String urlAutoComplete = "";
    protected static OkHttpClient http = null;

    public AutocompleteAPI(String url)
    {
        urlAutoComplete = url;
    }

    private JSONObject getJSON(String url) throws IOException, JSONException
    {
        if (http == null)
        {
            http = new OkHttpClient();
        }

        Request request = new Request.Builder().url(url).build();
        Response response = http.newCall(request).execute();

        String json = response.body().string();

        return new JSONObject(json);
    }

    public List<String> getAllCity() throws IOException, JSONException
    {
        JSONObject object = getJSON(urlAutoComplete);
        JSONArray villes = object.getJSONArray("predictions");
        List<String> allCity = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            JSONObject jsonObject = villes.getJSONObject(i);
            allCity.add(jsonObject.getString("description"));
        }

        return allCity;
    }
}
