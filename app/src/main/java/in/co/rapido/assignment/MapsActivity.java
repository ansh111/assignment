package in.co.rapido.assignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.rapido.assignment.model.Route;
import in.co.rapido.assignment.model.Step;
import in.co.rapido.assignment.utility.Utility;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MapsContract.View, View.OnClickListener {


    private static final int STARTING_PLACE_REQUEST_CODE = 1001;
    private static final int DESTINATION_PLACE_REQUEST_CODE = 1002;
    private GoogleMap mMap;
    private final String TAG = MapsActivity.class.getSimpleName();
    private LatLng mLatLng;
    private BottomSheetBehavior mBottomSheetBehavior;
    private EditText mStart;
    private EditText mDestination;
    private MapsActivityPresenter mapsActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapsActivityPresenter = new MapsActivityPresenter();
        View bottomSheet = findViewById(R.id.bottom_sheet1);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mapsActivityPresenter.setView((MapsContract.View) this);
        mStart = (EditText) findViewById(R.id.starting_point);
        mStart.setOnClickListener(this);
        mDestination = (EditText) findViewById(R.id.destionation_point);
        mDestination.setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationPermissionUtil.checkLocationPermission(this);
            return;
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LocationPermissionUtil.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        // locationManager.requestLocationUpdates(provider, 400, 1, this);
                        mMap.setMyLocationEnabled(true);
                        mMap.setPadding(0, 90, 0, 0);
                    }

                } else {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                return;
            }

        }

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void sendLocations(final List<Route> routeList) {
        final Map<String ,List<Step>> steps = new HashMap<>();
        for (Route route : routeList) {
            List<LatLng> decodedPath = Utility.decodePoly(route.getOverviewPolyline().getPoints());
            PolylineOptions line = new PolylineOptions().color(Color.BLUE).width(10f).geodesic(true).addAll(decodedPath);
            Polyline polyline = mMap.addPolyline(line);
            polyline.setClickable(true);
            steps.put(polyline.getId(),route.getLegs().get(0).getSteps());
            mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {

                @Override
                public void onPolylineClick(Polyline polyline) {
                    if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        for(Map.Entry<String,List<Step>> entry : steps.entrySet()) {
                            if(polyline.getId().equals(entry.getKey()))
                            showBottomSheetFragment(entry.getValue());
                        }
                        // mButton.setText(R.string.collapse_button1);
                    } else {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        //   mButton1.setText(R.string.button1);
                    }
                }
            });
            //  mMap.addPolyline(line);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(route.getLegs().get(0).getStartLocation().getLat(), route.getLegs().get(0).getStartLocation().getLng()), 10f));
            mMap.addMarker(new MarkerOptions().position(new LatLng(route.getLegs().get(0).getStartLocation().getLat(), route.getLegs().get(0).getStartLocation().getLng())).icon(BitmapDescriptorFactory.defaultMarker()));
            mMap.addMarker(new MarkerOptions().position(new LatLng(route.getLegs().get(0).getEndLocation().getLat(), route.getLegs().get(0).getEndLocation().getLng())).icon(BitmapDescriptorFactory.defaultMarker()));
        }
    }


    private void findStartingPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, STARTING_PLACE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    private void findDestinationPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, DESTINATION_PLACE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == STARTING_PLACE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if(null !=place && null !=place.getAddress())
                    mStart.setText(place.getAddress());
            }

        } else if (requestCode == DESTINATION_PLACE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if(null !=place && null !=place.getAddress())
                     mDestination.setText(place.getAddress());
            }
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.starting_point) {
            findStartingPlace(v);
        } else if (v.getId() == R.id.destionation_point) {
            findDestinationPlace(v);
        } else if (v.getId() == R.id.search) {
            if (mMap != null)
                mMap.clear();
            if (mStart.getText().toString() == null) {
                Toast.makeText(this, "Please enter start location", Toast.LENGTH_SHORT).show();
                return;
            } else if (mDestination.getText().toString() == null) {
                Toast.makeText(this, "Please enter end location", Toast.LENGTH_SHORT).show();
                return;
            }
            if (null != mapsActivityPresenter)
                mapsActivityPresenter.fetchDirections(mStart.getText().toString(), mDestination.getText().toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocationPermissionUtil.checkLocationPermission(this)) {
            if (null != mMap)
                mMap.setMyLocationEnabled(true);
        }
    }

    private void showBottomSheetFragment(List<Step> value) {

        DirectionStepsFragment directionStepsFragment = new DirectionStepsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(getString(R.string.steps_list), (ArrayList<? extends Parcelable>) value);
        directionStepsFragment.setArguments(bundle);
        directionStepsFragment.show(getSupportFragmentManager(), "BottomSheet Fragment");
    }

}
