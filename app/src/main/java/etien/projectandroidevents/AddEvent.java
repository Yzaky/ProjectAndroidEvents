package etien.projectandroidevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class AddEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        WebView webViewAddEvent = (WebView) findViewById(R.id.webViewAddEvent);
        webViewAddEvent.loadUrl("http://eventful.com/events/new");
    }
}
