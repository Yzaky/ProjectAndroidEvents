package etien.projectandroidevents;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

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
    private final String[] VILLE = new String[]{"Montreal","Ottawa","Toronto"};
    //fin ajout : donnée temporaires pour tester auto-complete

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_layout);

        //ajouter par Philippe pour auto-complete
        auto = (AutoCompleteTextView)findViewById(R.id.cityText);
        auto.setThreshold(2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, VILLE);
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
