package etien.projectandroidevents;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by Francis on 2017-04-14.
 * Lorsqu'on termine de modifier un champ modifiable, retire le focus et fait disparaître le clavier.
 */

public class myOnEditorActionListener implements TextView.OnEditorActionListener {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        EventsActivity.optionsNeedUpdate = true;
        EventsActivity.listNeedUpdate = true;
        return true;
    }
}
