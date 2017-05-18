package etien.projectandroidevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventsMap extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mGoogleMap;
    View view;
    public double[] lat;
    public double[] lng;
    public String[] title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_maps, container, false);

        Intent intent = getActivity().getIntent();
        try{
            lat = intent.getDoubleArrayExtra("Latitudes");
            lng = intent.getDoubleArrayExtra("Longitudes");
            title = intent.getStringArrayExtra("Title");
        }

        catch(Exception e)
        {
            Toast.makeText(getActivity(),"Couldn't locate events",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view,savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.mMapView);
        if(mapView !=null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        ///////////////////////////////////////////////////////
        // Regarde ce code Youssef
        ///////////////////////////////////////////////////////

        // Get seek value
        Object[] objects = ((EventsActivity) getActivity()).getSeekValue();
        double[] lat = (double[]) objects[0];
        double[] lng = (double[]) objects[1];

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(lat.length == lng.length)
        {
            for(int i=0;i<lat.length;i++)
            {
                Log.i("latLng",lat[i]+" "+lng[i]+" ");  // 0.0
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat[i],lng[i])).title(title[i]));
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.5017,-73.5673),14));
        }
        else
        {
            Toast.makeText(getActivity(),"Error binding Lat/Lng", Toast.LENGTH_LONG).show();
        }
    }
}