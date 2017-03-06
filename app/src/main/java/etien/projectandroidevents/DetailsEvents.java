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
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title= null;
                starttime = null;
                description = null;
            } else {
                title= extras.getString("Title");
                starttime = extras.getString("StartTime");
                description = extras.getString("Description");
            }
        } else {
            title= (String) savedInstanceState.getSerializable("Titles");
            starttime= (String) savedInstanceState.getSerializable("StartTime");
            description= (String) savedInstanceState.getSerializable("Description");
        }

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);

        TextView textViewStarttime = (TextView) findViewById(R.id.textViewStarttime);
        textViewStarttime.setText(starttime);

        TextView textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewDescription.setText(description);

    }
}
