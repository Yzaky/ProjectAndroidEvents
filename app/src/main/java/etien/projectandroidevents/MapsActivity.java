package etien.projectandroidevents;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double[] lat;
    public double[] lng;
    public String[] title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        try {
            lat = intent.getDoubleArrayExtra("Latitudes");
            lng = intent.getDoubleArrayExtra("Longitudes");
            title = intent.getStringArrayExtra("Title");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Couldn't locate events", Toast.LENGTH_LONG).show();
    }
}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final double userLat, userLng;
        double meanLat, meanLng;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Geocoder coder = new Geocoder(getApplicationContext());
        LatLng userLatlng = null;
        List<Address> addresslist;
        String strAddress;

        LocationManager lm;
        Location location = null;

        int searchOption = prefs.getInt("searchType", R.id.geolocRadioButton);

        if (searchOption == R.id.geolocRadioButton) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            if (location == null) {
                Toast.makeText(this, getResources().getString(R.string.geoloc_impossible), Toast.LENGTH_LONG).show();
                searchOption = R.id.cityRadioButton;
            }
        }

        switch (searchOption) {
            case R.id.geolocRadioButton:
                strAddress = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
                break;
            case R.id.zipCodeRadioButton:
                strAddress = prefs.getString("zipCode", "H3B 3A5");
                break;
            default:
                strAddress = prefs.getString("city", "Montreal");
                break;
        }

        try {
            addresslist = coder.getFromLocationName(strAddress, 1);
            if (addresslist != null && addresslist.size() > 0) {
                userLat = addresslist.get(0).getLatitude();
                userLng = addresslist.get(0).getLongitude();
                userLatlng = new LatLng(userLat, userLng);
                mMap.addMarker(new MarkerOptions()
                        .position(userLatlng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatlng, 11));
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLat, userLng), 14));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lat.length > 0)
        {
            meanLat = 0;
            meanLng = 0;
            int count = 0;

            for(int i = 0; i < lat.length; i++)
            {
                if (lat[i] == 0 && lng[i] == 0)
                    break;

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat[i], lng[i]))
                        .title(title[i]));

                meanLat += lat[i];
                meanLng += lng[i];
                count++;
            }

            if (userLatlng == null) {
                final LatLng latlng = new LatLng(meanLat/count, meanLng/count);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11));
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
                    }
                });
            }
        }
        else
        {
            Toast.makeText(this.getApplicationContext(),"Error binding Lat/Lng",Toast.LENGTH_LONG).show();
        }
    }
}
