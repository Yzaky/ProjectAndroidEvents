package etien.projectandroidevents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsEvents extends AppCompatActivity {

    String title, starttime, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_events);

        final double longitude, latitude;
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
            title= (String) savedInstanceState.getString("Title");
            starttime= (String) savedInstanceState.getString("StartTime");
            description= (String) savedInstanceState.getString("Description");
            latitude= (double) savedInstanceState.getDouble("Latitude");
            longitude= (double) savedInstanceState.getDouble("Longitude");
        }
        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);

        TextView textViewStarttime = (TextView) findViewById(R.id.textViewStarttime);
        textViewStarttime.setText(starttime);

        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewDescription.setText(description);

        Button buttonGoogleMaps = (Button) findViewById(R.id.buttonGoogleMaps);
        buttonGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.google.ca/maps/@" + latitude + "," + longitude + ",15z");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("Title", title);
        outState.putString("StartTime", starttime);
        outState.putString("Description", description);
        super.onSaveInstanceState(outState);
    }
}
