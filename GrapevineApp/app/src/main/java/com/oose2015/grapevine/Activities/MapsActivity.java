package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.PlaceAutocompleteAdapter;
import com.oose2015.grapevine.R;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker m;

    @Override
    /**
     * Called the first time Google maps is opened
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.searchLocation);
        textView.setAdapter(new PlaceAutocompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Log.d("debug: ", "google maps");
        mMap.setMyLocationEnabled(true);
    }

    /**
     * Called when User searches for a location
     * @param view the View which is the box to type in location
     */
    public void onSearch(View view) {
        AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.searchLocation);
        String loc = location.getText().toString();
        List<Address> addressList = null;
        if (loc != null || !loc.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(loc, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                m = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    /**
     * Called when user confirms their location
     * @param view the View which is the confirm button the user presses
     */
    public void onConfirm(View view) {
        if (m != null) {
            Intent intent = new Intent();
            intent.putExtra(Keys.IntentKeys.KEY_LATITUDE, m.getPosition().latitude);
            intent.putExtra(Keys.IntentKeys.KEY_LONGITUDE, m.getPosition().longitude);
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            Toast.makeText(Grapevine.getGrapevineContext(), "You must enter a valid location!", Toast.LENGTH_SHORT);
        }
    }

}