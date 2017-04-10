package com.wiselteach.geofire_v10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Reference: https://github.com/firebase/geofire-java
//Reference: http://myhexaville.com/2017/02/08/android-firebase-recyclerview-geofire/

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("path/to/geofire");
        GeoFire geoFire = new GeoFire(ref);

        geoFire.setLocation("firebase-hq5", new GeoLocation(24.6014, 73.6742), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    Log.e(TAG, "onComplete:error:" + error);
                } else {
                    Log.d(TAG, "onComplete:success");
                }
            }

        });

        //For Removing Location
        //geoFire.removeLocation("firebase-hq");


        //For Retrieving Location
        geoFire.getLocation("firebase-hq", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    Log.d(TAG, "onLocationResult: latitude:"+location.latitude + " longitude:" + location.longitude);
                } else {
                    Log.d(TAG, "onLocationResult: noLocationFound");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled:error:"+databaseError);
            }
        });




//        Key Entered: The location of a key now matches the query criteria.
//        Key Exited: The location of a key no longer matches the query criteria.
//        Key Moved: The location of a key changed but the location still matches the query criteria.
//        Query Ready: All current data has been loaded from the server and all initial events have been fired.
//        Query Error: There was an error while performing this query, e.g. a violation of security rules.


        // creates a new query around [37.7832, -122.4056] with a radius of 2.6 kilometers
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(24.572, 73.679), 50.0);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d(TAG, "onKeyEntered: latitude:"+location.latitude + " longitude:" + location.longitude);
            }

            @Override
            public void onKeyExited(String key) {
                Log.d(TAG, "onKeyExited: noLocationFound");
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d(TAG, "onKeyMoved: latitude:"+location.latitude + " longitude:" + location.longitude);
            }

            @Override
            public void onGeoQueryReady() {
                Log.d(TAG, "onGeoQueryReady:success");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d(TAG, "onGeoQueryError:error:"+error);
            }
        });

    }
}
