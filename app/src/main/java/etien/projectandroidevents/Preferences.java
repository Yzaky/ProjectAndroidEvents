package etien.projectandroidevents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Pour les options plus générales
 * Created by francis on 23/03/17.
 */

public class Preferences extends AppCompatActivity {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private LinearLayout prefLayout;
    private AutoCompleteTextView cityField;
    private EditText zipCodeField;
    private EditText radiusField;
    private Switch milesChooser;
    private Button buttonMainActivity;

    List<String> cities = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setTitle(R.string.app_bar_preferences);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        cityField = (AutoCompleteTextView) findViewById(R.id.cityText);
        zipCodeField = (EditText) findViewById(R.id.zipCodeText);
        radiusField = (EditText) findViewById(R.id.radiusText);
        milesChooser = (Switch) findViewById(R.id.milesSwitch);

        cityField.setText(prefs.getString("city", "Montreal"));
        zipCodeField.setText(prefs.getString("zipCode", "H3B 3A5"));
        radiusField.setText(prefs.getString("radius", "20"));
        milesChooser.setChecked(prefs.getBoolean("useMiles", false));

        cityField.setOnEditorActionListener(new myOnEditorActionListener());
        cityField.addTextChangedListener(new TextWatcher() {
            private final String URLBase = "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=(cities)&key=AIzaSyDrQ7esnK_zOh7NRE2rQsMhBtox6kHrPvs&input=";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FetchCities fetchCities = new FetchCities(URLBase + cityField.getText().toString());
                fetchCities.execute();

                adapter = new ArrayAdapter<>(Preferences.this, android.R.layout.simple_dropdown_item_1line, cities);
                cityField.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        zipCodeField.setOnEditorActionListener(new myOnEditorActionListener());

        radiusField.setOnEditorActionListener(new myOnEditorActionListener());

        milesChooser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EventsActivity.listNeedUpdate = true;
            }
        });

        // Bouton pour retourner à l'activité principale
        buttonMainActivity = (Button) findViewById(R.id.buttonMainActivity);
        buttonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        String city = cityField.getText().toString();
        city = Normalizer.normalize(city, Normalizer.Form.NFD);
        city = city.replaceAll("[^\\p{ASCII}]", "");

        editor = prefs.edit();
        editor.putString("city", city);
        editor.putString("zipCode", zipCodeField.getText().toString());
        editor.putString("radius", radiusField.getText().toString());
        editor.putBoolean("useMiles", milesChooser.isChecked());

        editor.commit();
    }

    private class FetchCities extends AsyncTask<String, Object, List<String>>
    {
        private String url;

        private FetchCities(String url)
        {
            this.url=url;
        }

        @Override
        protected List<String> doInBackground(String... params) {
            AutocompleteAPI api = new AutocompleteAPI(url);

            try {
                cities = api.getAllCity();
            } catch (IOException |JSONException e) {
                e.printStackTrace();
            }

            return cities;
        }

        @Override
        protected void onPostExecute(List<String> cities)
        {

        }
    }

    /*
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);

        return true;
    }
    */

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
