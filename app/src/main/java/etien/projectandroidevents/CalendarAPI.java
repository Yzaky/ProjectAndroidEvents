package etien.projectandroidevents;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

// TODO : lire https://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en
// TODO : https://github.com/316k/IFT1155-Permissions/blob/master/app/src/main/java/ca/umontreal/iro/hurtubin/permissions/MainActivity.java

public class CalendarAPI extends AppCompatActivity {

    final private int REQUEST_CODE_ADD_EVENT = 1;
    final private int REQUEST_CODE_UPDATE_EVENT = 2;
    final private int REQUEST_CODE_DELETE_EVENT = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        createEventWrapper();
        createEvent();
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


    public void createEvent() {
        // Construct event details
        long startMillis;
        long endMillis;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 9, 14, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();

        // Insert Event
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, "Walk The Dog");
        values.put(CalendarContract.Events.DESCRIPTION, "My dog is bored, so we're going on a really long walk!");
        values.put(CalendarContract.Events.CALENDAR_ID, 3);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }
}

