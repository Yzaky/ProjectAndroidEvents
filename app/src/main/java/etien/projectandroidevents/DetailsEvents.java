package etien.projectandroidevents;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.TimeZone;

// DOC : lire https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
// DOC : https://github.com/316k/IFT1155-Permissions/blob/master/app/src/main/java/ca/umontreal/iro/hurtubin/permissions/MainActivity.java


public class DetailsEvents extends AppCompatActivity {

    String title, starttime, description, image;
    String country_name, region_name, city_name, venue_address;
    double longitude2, latitude2;
    long startMillis, endMillis;
    int annee, mois, jour, heure, minute, seconde, heureplustard, minuteplustard;
    private static boolean isBookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_events);

        setTitle(R.string.app_bar_detailsevents);

        final double longitude, latitude;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                title = null;
                starttime = null;
                description = null;
                longitude = 0;
                latitude = 0;
                image = null;
                country_name = null;
                region_name = null;
                city_name = null;
                venue_address = null;
            } else {
                title = extras.getString("Title");
                starttime = extras.getString("StartTime");
                description = extras.getString("Description");
                longitude = extras.getDouble("Longitude");
                latitude = extras.getDouble("Latitude");
                image = extras.getString("Image");
                country_name = extras.getString("CountryName");
                region_name = extras.getString("RegionName");
                city_name = extras.getString("CityName");
                venue_address = extras.getString("VenueAddress");
            }
        } else {
            title = savedInstanceState.getString("Title");
            starttime = savedInstanceState.getString("StartTime");
            description = savedInstanceState.getString("Description");
            latitude = savedInstanceState.getDouble("Latitude");
            longitude = savedInstanceState.getDouble("Longitude");
            image = savedInstanceState.getString("Image");
            country_name = savedInstanceState.getString("CountryName");
            region_name = savedInstanceState.getString("RegionName");
            city_name = savedInstanceState.getString("CityName");
            venue_address = savedInstanceState.getString("VenueAddress");
        }
        longitude2 = longitude;
        latitude2 = latitude;

        // déclaration des éléments du layout
        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView textViewStarttime = (TextView) findViewById(R.id.textViewStarttime);
        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        ImageButton buttonGoogleMaps = (ImageButton) findViewById(R.id.imageButtonMaps);
        ImageButton buttonAddCalendrier = (ImageButton) findViewById(R.id.imageButtonAgenda);
        final ImageButton buttonFavoris = (ImageButton) findViewById(R.id.imageButtonFavoris);
        Button buttonMainActivity = (Button) findViewById(R.id.buttonMainActivity);
        ImageView img = (ImageView) findViewById(R.id.imageViewEvent);

        // affiche des éléments du layont
        textViewStarttime.setText(starttime);
        textViewTitle.setText(title);

        // affiche l'image
        Picasso.Builder b = new Picasso.Builder(getApplicationContext());
        b.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                Log.d("Images","erreur: " + e.getMessage());
            }
        });
        if(image == null){
            img.setImageResource(R.drawable.logopingouin);
        } else {
            b.build().load(image).into(img);
        }

        // affiche la description
        if (description.equals("null") || description.isEmpty()) {
            textViewDescription.setText(R.string.descriptionNonDisponible);
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                textViewDescription.setText(Html.fromHtml(description));
            } else {
                textViewDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
            }
        }

        // calculer date et heure
        calculerDateHeure();

        // Bouton google maps
        buttonGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<"+latitude+">,<"+longitude+">?q=<<"+latitude+">,<"+longitude+">"));
                startActivity(intent);
            }
        });
        // Bouton pour retourner à l'activité principale
        buttonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        // Verifie si le favori existe
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        isBookmarked = dbHelper.isBookmarked(getApplicationContext(), title);


        if(isBookmarked){
            buttonFavoris.setImageResource(R.drawable.minus_sign);
        } else {
            buttonFavoris.setImageResource(R.drawable.plus_sign3);
        }

        // Bouton favoris
        buttonFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());

                if(isBookmarked) {
                    Toast.makeText(getApplicationContext(), R.string.enleverFavoris, Toast.LENGTH_SHORT).show();
                    buttonFavoris.setImageResource(R.drawable.plus_sign3);
                    dbHelper.deleteUnFavoris(getApplicationContext(),title);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.ajouterFavoris, Toast.LENGTH_SHORT).show();
                    buttonFavoris.setImageResource(R.drawable.minus_sign);
                    dbHelper.ajoutUnFavoris(getApplicationContext(),
                            title, description, starttime, "" + longitude, "" + latitude,
                            country_name, region_name, city_name, venue_address, image);
                }

                buttonFavoris.invalidate();
                isBookmarked = !isBookmarked;
                Favoris.updateNeeded = true;

                /*
                // Démarre l'activité des favoris
                Intent intent = new Intent(getApplicationContext(), Favoris.class);
                startActivity(intent);
                */
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
        outState.putString("CountryName",country_name);
        outState.putString("RegionName",region_name);
        outState.putString("CityName",city_name);
        outState.putString("VenueAddress",venue_address);

        super.onSaveInstanceState(outState);
    }

    /////////////////////////////////////////////////////
    // Menu
    /////////////////////////////////////////////////////

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);

        return true;
    }

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

    ////////////////////////////////////////////
    // Agenda
    ////////////////////////////////////////////

    // Crée un événement
    public void createEvent() {
        // Debug
        Log.i("DetailsEvents", starttime);
        Log.i("DetailsEvents", "" + annee + " " + mois + " " + jour + " " + heure + " " + minute + " " + seconde);
        Log.i("DetailsEvents", "" + annee + " " + mois + " " + jour + " " + heure + " " + minute + " " + seconde);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, 3);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
            return;
        }

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
