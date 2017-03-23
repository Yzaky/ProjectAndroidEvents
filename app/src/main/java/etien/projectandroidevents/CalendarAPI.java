package etien.projectandroidevents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }
 /*
    public void createEventWrapper() {

        int hasPermission = checkSelfPermission(Manifest.permission.WRITE_CALENDAR);

        if(hasPermission != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(this)
                    .setMessage("...")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        }

    }
*/
 /*
    public void createEvent() {
        // Construct event details
        long startMillis = 0;
        long endMillis = 0;
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
*/
}

