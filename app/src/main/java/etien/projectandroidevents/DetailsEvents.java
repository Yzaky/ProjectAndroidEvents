package etien.projectandroidevents;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

// DOC : lire https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
// DOC : https://github.com/316k/IFT1155-Permissions/blob/master/app/src/main/java/ca/umontreal/iro/hurtubin/permissions/MainActivity.java


public class DetailsEvents extends AppCompatActivity {

    String title, starttime, description;
    double longitude2, latitude2;
    long startMillis;
    long endMillis;
    int annee, mois, jour, heure, minute, seconde, heureplustard, minuteplustard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_events);

        final double longitude, latitude;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title = null;
                starttime = null;
                description = null;
                longitude = 0;
                latitude = 0;
            } else {
                title = extras.getString("Title");
                starttime = extras.getString("StartTime");
                description = extras.getString("Description");
                longitude = extras.getDouble("Longitude");
                latitude = extras.getDouble("Latitude");
            }
        } else {
            title = savedInstanceState.getString("Title");
            starttime = savedInstanceState.getString("StartTime");
            description = savedInstanceState.getString("Description");
            latitude = savedInstanceState.getDouble("Latitude");
            longitude = savedInstanceState.getDouble("Longitude");
        }
        longitude2 = longitude;
        latitude2 = latitude;

        // déclaration des éléments du layout
        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView textViewStarttime = (TextView) findViewById(R.id.textViewStarttime);
        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        Button buttonGoogleMaps = (Button) findViewById(R.id.buttonGoogleMaps);
        Button buttonAddCalendrier = (Button) findViewById(R.id.buttonAddCalendrier);

        // affichage des éléments du layont
        textViewStarttime.setText(starttime);
        textViewTitle.setText(title);
        textViewDescription.setText(description);

        // calculer date et heure
        calculerDateHeure();

        // Bouton google maps
        buttonGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.google.ca/maps/@" + latitude + "," + longitude + ",15z");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        // Bouton calendrier
        buttonAddCalendrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Crée un événement
                createEvent();

                // Démarre le calendrier
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                startActivity(intent);
            }
        });

    }

    // Sauvergarder les données lorsqu'on incline le cellulaire
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("Title", title);
        outState.putString("StartTime", starttime);
        outState.putString("Description", description);
        outState.putDouble("Latitude", latitude2);
        outState.putDouble("Longitude", longitude2);
        super.onSaveInstanceState(outState);
    }

    // Menu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    // Crée un événement
    public void createEvent() {
        // Debug
        Log.i("DetailsEvents",starttime);
        Log.i("DetailsEvents","" + annee + " " + mois + " " + jour  + " " + heure  + " " + minute  + " " + seconde);
        Log.i("DetailsEvents","" + annee + " " + mois + " " + jour  + " " + heure  + " " + minute  + " " + seconde);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, 3);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }

    // Calculer date et heure
    void calculerDateHeure(){

        // Obtenir les données temporelles de l'événement
        annee = Integer.parseInt(starttime.substring(0,4));
        mois = Integer.parseInt(starttime.substring(5,7));
        jour = Integer.parseInt(starttime.substring(8,10));
        heure = Integer.parseInt(starttime.substring(11,13));
        minute = Integer.parseInt(starttime.substring(14,16));
        seconde = Integer.parseInt(starttime.substring(17,19));

        // Temps de l'événement
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(annee, mois, jour, heure, minute, seconde);
        startMillis = beginTime.getTimeInMillis();

        // Construct event details
        minuteplustard = minute;
        heureplustard = heure + 1;
        if(heure == 23) {
            minuteplustard = 59;
            heureplustard = 23;
        }
        Calendar endTime = Calendar.getInstance();
        endTime.set(annee, mois, jour, heureplustard, minuteplustard, seconde);
        endMillis = endTime.getTimeInMillis();

    }
}
