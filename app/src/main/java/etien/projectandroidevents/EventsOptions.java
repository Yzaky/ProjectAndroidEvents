package etien.projectandroidevents;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Permet de modifier les options de filtrage pour la liste des events
 * Created by francis on 31/03/17.
 */

public class EventsOptions extends Fragment {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Category[] categories;

    private RadioGroup searchType;
    private TextView cityText;
    private TextView zipCodeText;

    private RadioButton selectedRadio = null;
    private int selectedPosition = -1;

    private ListView categoryListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_options, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        editor = prefs.edit();

        categories = EventsActivity.categories;

        searchType = (RadioGroup) view.findViewById(R.id.searchRadioGroup);
        ((RadioButton) view.findViewById(prefs.getInt("searchType", R.id.geolocRadioButton))).setChecked(true); // retrouve la case à cocher du groupe
        searchType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                EventsActivity.listNeedUpdate = true;
                editor.putInt("searchType", searchType.getCheckedRadioButtonId());
                editor.commit();
            }
        });

        cityText = (TextView) view.findViewById(R.id.cityText);
        cityText.setText(prefs.getString("city", "Montreal"));

        zipCodeText = (TextView) view.findViewById(R.id.zipCodeText);
        zipCodeText.setText(prefs.getString("zipCode", "H3B 3A5"));

        categoryListView = (ListView) view.findViewById(R.id.categoryListView);
        categoryListView.setAdapter(new CategoryListAdapter(categories));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EventsActivity.optionsNeedUpdate) {
            cityText.setText(prefs.getString("city", "Montreal"));
            zipCodeText.setText(prefs.getString("zipCode", "H3B 3A5"));
            EventsActivity.optionsNeedUpdate = false;
        }
    }

    private class CategoryListAdapter extends BaseAdapter
    {
        private final Category[] categories;

        private CategoryListAdapter(Category[] categories) {
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return categories.length;
        }

        @Override
        public Object getItem(int position) {
            return categories[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.category_radio, parent, false);
            }

            final RadioButton currentRadio = (RadioButton) convertView.findViewById(R.id.categoryRadio);
            final Category currentCategory = categories[position];
            final int currentPosition = position;

            if (currentCategory.isActive()) {
                selectedRadio = currentRadio;
                selectedPosition = currentPosition;
            }

            currentRadio.setText(currentCategory.getTitle());
            currentRadio.setChecked(currentCategory.isActive());

            currentRadio.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    if (!currentCategory.isActive())
                    {
                        currentRadio.setChecked(true);
                        currentCategory.setActive(true);
                        editor.putBoolean(currentCategory.getId(), true);

                        if (selectedRadio != null) {
                            selectedRadio.setChecked(false);
                            categories[selectedPosition].setActive(false);
                            editor.putBoolean(categories[selectedPosition].getId(), false);
                        }

                        selectedRadio = currentRadio;
                        selectedPosition = currentPosition;

                        EventsActivity.listNeedUpdate = true;
                        editor.commit();
                    }
                }
            });

            return convertView;
        }
    }
}
