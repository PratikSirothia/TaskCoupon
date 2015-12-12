package com.task.couponduniyatask;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.task.couponduniyatask.adapter.HomeAdapter;
import com.task.couponduniyatask.model.RestaurantPojo;
import com.task.couponduniyatask.utility.Utils;
import com.task.couponduniyatask.webservice.SyncServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected final static String LOCATION_KEY = "location-key";
    private static final String TAG = "MainActivity";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    private Toolbar toolbar;
    private Context context;
    private RecyclerView mRecyclerView;
    private LinearLayout mLodaingPanel;
    private List<RestaurantPojo> lstRestaurantPojoList;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        initActionBar();
        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();
        initComponents();
        if (Utils.isNetwrokAvial(context)) {
            new GetRestuarentList().execute();
        } else {
            Utils.showLongToast(context, getResources().getString(R.string.error_internet));
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {

            // Update the value of mCurrentLocation from the Bundle and update
            // the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure
                // that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState
                        .getParcelable(LOCATION_KEY);
            }

        }
    }

    private void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.);
        ab.setDisplayHomeAsUpEnabled(false);
    }

    private void initComponents() {
        mRecyclerView = (RecyclerView) findViewById(R.id.home_act_rv);
        mLodaingPanel = (LinearLayout) findViewById(R.id.home_act_loadingPanel);
        lstRestaurantPojoList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(context, lstRestaurantPojoList);
        mRecyclerView.setAdapter(homeAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!Utils.isNull(mGoogleApiClient)) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!Utils.isNull(mGoogleApiClient)) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Utils.showLongToast(context, "Cannot get your location!");

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            Log.d(TAG, "mCurrentLocation=" + mCurrentLocation);
            if (!Utils.isNull(mCurrentLocation) && !lstRestaurantPojoList.isEmpty()) {
                Collections.sort(lstRestaurantPojoList,
                        new Comparator<RestaurantPojo>() {
                            public int compare(RestaurantPojo o1,
                                               RestaurantPojo o2) {
                                if (o1.getDistance() != 0
                                        && o2.getDistance() != 0) {
                                    return o1.getDistance() < o2
                                            .getDistance() ? -1
                                            : 1;
                                } else {
                                    return 1;
                                }
                            }
                        });

                homeAdapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        Utils.showLongToast(context, "Cannot get your location!");
        if (!Utils.isNull(mGoogleApiClient)) {
            mGoogleApiClient.connect();
        }
    }

    private class GetRestuarentList extends AsyncTask<Void, Void, Void> {
        private SyncServer syncServer;
        private String result;
        private JSONObject jsonObject;
        private String uniCode = "&#10042;";

        public GetRestuarentList() {
            syncServer = new SyncServer(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                result = syncServer.getRestaurantData();
//                Log.d(TAG, "result=" + result);
                jsonObject = new JSONObject(result);
                if (!Utils.isNullString(jsonObject.getString("data"))) {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                    for (int rest = 0; rest < jsonArray.length(); rest++) {
                        JSONObject restoObj = jsonArray.getJSONObject(rest);
                        RestaurantPojo restaurantPojo;
                        restaurantPojo = new Gson().fromJson(
                                restoObj.toString(),
                                RestaurantPojo.class);

                        JSONArray catArray = new JSONArray(restoObj.getString("Categories"));
                        String categories = uniCode + " ";
                        for (int cat = 0; cat < catArray.length(); cat++) {
                            JSONObject catObj = catArray.getJSONObject(cat);
                            categories = categories + catObj.getString("Name") + " " + uniCode + " ";
                        }
                        if (!Utils.isNullString(categories)) {
                            int index = categories.lastIndexOf(uniCode);
                            categories = categories.substring(0, index);
                            restaurantPojo.setCateGories(categories);
                        }

                        if (!Utils.isNull(mCurrentLocation)) {
                            float[] result = new float[1];
                            Location.distanceBetween(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), restaurantPojo.getLatitude(), restaurantPojo.getLongitude(), result);
                            restaurantPojo.setDistance(Math.round(result[0]));
                        }

                        lstRestaurantPojoList.add(restaurantPojo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mLodaingPanel.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (!lstRestaurantPojoList.isEmpty()) {

                Collections.sort(lstRestaurantPojoList,
                        new Comparator<RestaurantPojo>() {
                            public int compare(RestaurantPojo o1,
                                               RestaurantPojo o2) {
                                if (o1.getDistance() != 0
                                        && o2.getDistance() != 0) {
                                    return o1.getDistance() < o2
                                            .getDistance() ? -1
                                            : 1;
                                } else {
                                    return 1;
                                }
                            }
                        });

                homeAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLodaingPanel.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}
