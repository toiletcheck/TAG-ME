package com.tcc.gian_luca.test;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.ResponseBody;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Home extends FragmentActivity implements GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RatingDialog.OnFragmentInteractionListener {

    //private static final long UPDATE_INTERVAL = ;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private JSONObject obj;
    private Location mLastLocation;
    private double mLatitudeDouble;
    private double mLongitudeDouble;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private HashMap<Marker, Integer> Marker2ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       // x_y_reader();


        setUpMapIfNeeded();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://geoportal1.stadt-koeln.de")
                .build();

        TCC service = retrofit.create(TCC.class);


        Call<ResponseBody>result = service.listHotSpot();



            result.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response) {
                    Log.d("TCC","Recieved");
                    try {
                        String json = response.body().string();
                        Log.d("TCC", json);

                        obj =  new JSONObject(json);
                        renderMarkers();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                public void renderMarkers () {
                    if (obj != null) {
                        try {
                            JSONArray features = obj.getJSONArray("features");
                            Marker2ID = new HashMap<Marker, Integer>();

                            for (int i = 0; i < features.length(); i++) {

                                JSONObject jobj = features.getJSONObject(i);
                                double x = jobj.getJSONObject("geometry").getDouble("y");
                                double y = jobj.getJSONObject("geometry").getDouble("x");
                                String n = jobj.getJSONObject("attributes").getString("BETREIBER");
                                Integer Id = jobj.getJSONObject("attributes").getInt("OBJECTID");


                                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(x, y)).title(n));
                                Marker2ID.put(marker, Id);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                }


                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });


    }
/*
    private void x_y_reader() {
        mLocationClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */





    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);


    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeDouble = mLastLocation.getLatitude();
            mLongitudeDouble = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        DialogFragment newFragment = RatingDialog.newInstance(marker.getTitle(), 0, Marker2ID.get(marker));
        newFragment.show(getSupportFragmentManager(), "dialog");

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
