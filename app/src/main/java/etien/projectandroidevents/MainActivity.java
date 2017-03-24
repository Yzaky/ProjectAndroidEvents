package etien.projectandroidevents;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ADD_EVENT = 1;
    final private int REQUEST_CODE_UPDATE_EVENT = 2;
    final private int REQUEST_CODE_DELETE_EVENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //jk
        setContentView(R.layout.activity_main);

        Button buttonListEvents = (Button) findViewById(R.id.buttonListEvents);
        buttonListEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListEvents.class);
                startActivity(intent);
            }
        });
        Button buttonCalendar = (Button) findViewById(R.id.buttonCalendar);
        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long startMillis = System.currentTimeMillis();

                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                startActivity(intent);
            }
        });
        Button buttonGeolocalisation = (Button) findViewById(R.id.buttonGeolocalisation);
        buttonGeolocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Geolocalisation.class);
                startActivity(intent);
            }
        });
        createEventWrapper();
    }

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
                //showAbout();
                return true;
            case R.id.help:
                //showHelp();
                return true;
            case R.id.action_add_event:
                startActivity(new Intent(getApplicationContext(), AddEvent.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void createEventWrapper() {

        int hasPermission = checkSelfPermission(Manifest.permission.WRITE_CALENDAR);

        if(hasPermission != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(this)
                    .setMessage("Afin d'ajouter un événement à votre calendrier, vous devez autoriser l'application à écrire un événement au calendrier.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_CODE_ADD_EVENT);
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ADD_EVENT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission accordée", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission refusée", Toast.LENGTH_LONG).show();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }

    }
}
