package etien.projectandroidevents;

import android.preference.PreferenceManager;

/**
 * Conteneur des infos concernant une catégorie
 * Created by francis on 07/04/17.
 */

public class Category
{
    private final String id;
    private final String title;
    private boolean active;

    public Category(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}