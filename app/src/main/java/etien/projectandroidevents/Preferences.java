package etien.projectandroidevents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import  android.widget.TextView.OnEditorActionListener;
/**
 * Created by francis on 23/03/17.
 */

public class Preferences extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private LinearLayout prefLayout;
    private EditText cityField;
    private EditText zipCodeField;
    private EditText radiusField;
    private Switch milesChooser;
    private Switch geolocChooser;

    //ajouter par Philippe pour auto-complete

    private AutoCompleteTextView auto;
    private String[] ville = new String[]{};
    private String txt = "";
    private final String URLBase = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDrQ7esnK_zOh7NRE2rQsMhBtox6kHrPvs&input=";
    private String URL;
    //fin ajout : donnée temporaires pour tester auto-complete

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_layout);

        //ajouter par Philippe pour auto-complete

        auto = (AutoCompleteTextView)findViewById(R.id.cityText);
        auto.setThreshold(1);

      auto.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
              txt = auto.getText().toString();
              URL = URLBase + txt;
              Toast.makeText(getApplicationContext(), URL, Toast.LENGTH_SHORT).show();
          }

          @Override
          public void afterTextChanged(Editable s) {

          }
      });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ville);
        auto.setAdapter(adapter);

        //fin ajout


        prefLayout = (LinearLayout) findViewById(R.id.pref_layout);
        cityField = (EditText) findViewById(R.id.cityText);
        zipCodeField = (EditText) findViewById(R.id.zipCodeText);
        radiusField = (EditText) findViewById(R.id.radiusText);
        milesChooser = (Switch) findViewById(R.id.milesSwitch);
        geolocChooser = (Switch) findViewById(R.id.geolocSwitch);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        cityField.setText(          prefs.getString("city", "Montréal")     );
        zipCodeField.setText(       prefs.getString("zipCode", "H3B 3A5")   );
        radiusField.setText(        prefs.getString("radius", "20")         );
        milesChooser.setChecked(    prefs.getBoolean("useMiles", false)     );
        geolocChooser.setChecked(   prefs.getBoolean("allowGeoloc", false)  );

        prefLayout.invalidate();
    }



    @Override
    protected void onPause() {
        super.onPause();

        editor.putString("city", cityField.getText().toString());
        editor.putString("zipCode", zipCodeField.getText().toString());
        editor.putString("radius", radiusField.getText().toString());
        editor.putBoolean("useMiles", milesChooser.isChecked());
        editor.putBoolean("allowGeoloc", geolocChooser.isChecked());

        editor.commit();
    }
}
