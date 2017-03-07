package etien.projectandroidevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_events);

        String title, starttime, description;
        double longitude, latitude;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title= null;
                starttime = null;
                description = null;
                longitude = 0;
                latitude = 0;
            } else {
                title= extras.getString("Title");
                starttime = extras.getString("StartTime");
                description = extras.getString("Description");
                longitude = extras.getDouble("Longitude");
                latitude = extras.getDouble("Latitude");
            }
        } else {
            title= (String) savedInstanceState.getSerializable("Titles");
            starttime= (String) savedInstanceState.getSerializable("StartTime");
            description= (String) savedInstanceState.getSerializable("Description");
            longitude= (double) savedInstanceState.getSerializable("Longitude");
            latitude= (double) savedInstanceState.getSerializable("Latitude");

        }

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);

        TextView textViewStarttime = (TextView) findViewById(R.id.textViewStarttime);
        textViewStarttime.setText(starttime);

        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewDescription.setText(description);

        TextView textViewLongitude = (TextView) findViewById(R.id.textViewLongitude);
        textViewLongitude.setText("" + longitude);

        TextView textViewLatitude = (TextView) findViewById(R.id.textViewLatitude);
        textViewLongitude.setText("" + latitude);


    }
}
