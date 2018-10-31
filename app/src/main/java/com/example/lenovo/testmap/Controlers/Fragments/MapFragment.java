package com.example.lenovo.testmap.Controlers.Fragments;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.testmap.Models.Data.GetNearbyPlacesData;
import com.example.lenovo.testmap.Models.Layout.Menu;
import com.example.lenovo.testmap.Models.Parsers.DirectionsParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;

import com.example.lenovo.testmap.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, OnMarkerClickListener,
                                 GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private MapView mMapView;
    private GoogleMap googleMap;

    private View v;

    private Menu menu;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationUpdateState;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    private int PROXIMITY_RADIUS = 5000;

   static public MapFragment newInstance()
    {
        return (new MapFragment());
    }

    public void onLocationChanged(Location location)
    {

    }

    public void onConnectionFailed(ConnectionResult result)
    {

    }

    public void onConnectionSuspended(int cause)
    {

    }
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // inflater and return the layout
    v = inflater.inflate(R.layout.fragment_map, container,
            false);
    mMapView = v.findViewById(R.id.map);
    mMapView.onCreate(savedInstanceState);

    mMapView.onResume();// needed to get the map to display immediately

    try {
       MapsInitializer.initialize(getActivity().getApplicationContext());
    } catch (Exception e) {
        e.printStackTrace();
    }

    mMapView.getMapAsync(this);

            if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createLocationRequest();

    ImageView hotel = v.findViewById(R.id.hotel);
    ImageView resto = v.findViewById(R.id.restaurant);
    ImageView spa = v.findViewById(R.id.spa);
    ImageView bank = v.findViewById(R.id.bank);
    ImageView bus = v.findViewById(R.id.bus);
    ImageView cafe = v.findViewById(R.id.cafe);
    ImageView mall = v.findViewById(R.id.mall);
    ImageView market = v.findViewById(R.id.market);
    ImageView movie = v.findViewById(R.id.movie);
    ImageView pharmacy = v.findViewById(R.id.pharmacy);
            menu = new Menu(hotel, resto, cafe, spa, mall, pharmacy, market, bank, bus, movie);




    return v;
}

@Override
public void onResume() {
    super.onResume();
    mMapView.onResume();

        if (mGoogleApiClient.isConnected() && !mLocationUpdateState) {
            startLocationUpdates();
        }
}

@Override
public void onPause() {
    super.onPause();

     //   LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mMapView.onPause();
}

public void onStart() {
        super.onStart();
        // 2
      mGoogleApiClient.connect();
    }

public void onStop(){
       super.onStop();
        // 3
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
}

    @Override
    public void onConnected(@android.support.annotation.Nullable Bundle bundle) {
        setUpMap();

        if (mLocationUpdateState) {
            startLocationUpdates();
        }

    }

    private void loadPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
;

        } catch(GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

@Override
public void onDestroy() {
    super.onDestroy();
    mMapView.onDestroy();
}

@Override
public void onLowMemory() {
    super.onLowMemory();
    mMapView.onLowMemory();

}

    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
        return false;
    }

    public void onMapReady(GoogleMap map)
    {
        googleMap = map;

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnMarkerClickListener(this);



        menu.getRestoButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchThing("restaurant", "restaurants");
            }
        });

        menu.getHotelButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("hotel", "hotels");
            }
        });

        menu.getBankButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchThing("bank", "banks");
            }
        });

        menu.getBusButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("bus_station", "bus");
            }
        });

        menu.getCafeButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("cafe", "cafe");
            }
        });

        menu.getMallButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("shopping_mall", "malls");
            }
        });

        menu.getMarketButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("store", "stores");
            }
        });

        menu.getMovieButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("movie_theater", "movie theaters");
            }
        });

        menu.getPharmacyButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("pharmacy", "pharmacy");
            }
        });

        menu.getSpaButton().setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                searchThing("spa", "spa");
            }
        });


        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlacePicker();
            }
        });

        }


        public void searchThing(String what, String show)
        {
            android.util.Log.d("onClick", "Button is Clicked");
            googleMap.clear();
            String url = getUrl(mLastLocation.getLatitude(), mLastLocation.getLongitude(), what);
            Object[] DataTransfer = new Object[2];
            DataTransfer[0] = googleMap;
            DataTransfer[1] = url;
            Log.d("onClick", url);
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.execute(DataTransfer);
            Toast.makeText(getActivity(),"Nearby " + show, Toast.LENGTH_LONG).show();
        }


    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        android.util.Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    protected void startLocationUpdates() {
        //1
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED){
            android.support.v4.app.ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        //2
        com.google.android.gms.location.LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
                this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new com.google.android.gms.location.LocationRequest();
        // 2
        mLocationRequest.setInterval(10000);
        // 3
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);

        com.google.android.gms.location.LocationSettingsRequest.Builder builder = new com.google.android.gms.location.LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        com.google.android.gms.common.api.PendingResult<com.google.android.gms.location.LocationSettingsResult> result =
                com.google.android.gms.location.LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new com.google.android.gms.common.api.ResultCallback<com.google.android.gms.location.LocationSettingsResult>() {
            @Override
            public void onResult(@android.support.annotation.NonNull com.google.android.gms.location.LocationSettingsResult result) {
                final com.google.android.gms.common.api.Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    // 4
                    case com.google.android.gms.location.LocationSettingsStatusCodes.SUCCESS:
                        mLocationUpdateState = true;
                        startLocationUpdates();
                        break;
                    // 5
                    case com.google.android.gms.location.LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (android.content.IntentSender.SendIntentException e) {
                        }
                        break;
                    // 6
                    case com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void setUpMap() {
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        googleMap.setMyLocationEnabled(true);

        com.google.android.gms.location.LocationAvailability locationAvailability =
                com.google.android.gms.location.LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLastLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                com.google.android.gms.maps.model.LatLng currentLocation = new com.google.android.gms.maps.model.LatLng(mLastLocation.getLatitude(), mLastLocation
                        .getLongitude());
                //add pin at user's location
                placeMarkerOnMap(currentLocation);
                googleMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            }
        }
    }

    private String getAddress(LatLng latLng ) {
        // 1
        Geocoder geocoder = new Geocoder( getContext() );
        String addressText = "";
        List<android.location.Address> addresses = null;
        Address address = null;
        try {
            // 2
            addresses = geocoder.getFromLocation( latLng.latitude, latLng.longitude, 1 );
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressText += (i == 0)?address.getAddressLine(i):("\n" + address.getAddressLine(i));
                }
            }
        } catch (java.io.IOException e ) {
        }
        return addressText;
    }

    protected  void placeMarkerOnMap(LatLng location) {
        MarkerOptions markerOptions = new MarkerOptions().position(location);

        String titleStr = getAddress(location);
        markerOptions.title(titleStr);

        googleMap.addMarker(markerOptions);
    }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                mLocationUpdateState = true;
                startLocationUpdates();
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                String addressText = place.getName().toString();
                addressText += "\n" + place.getAddress().toString();

                placeMarkerOnMap(place.getLatLng());


                LatLng source = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                String url = getRequestUrl(source, place.getLatLng());
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);
            }
        }
    }

     private String getRequestUrl(com.google.android.gms.maps.model.LatLng origin, com.google.android.gms.maps.model.LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Build the full param
        String param = str_org +"&" + str_dest + "&key=AIzaSyBpO-VvrQ-N8C703-S707bxuLrbsTv0Rh0";
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws java.io.IOException {
        String responseString = "";
        java.io.InputStream inputStream = null;
        java.net.HttpURLConnection httpURLConnection = null;
        try{
            java.net.URL url = new java.net.URL(reqUrl);
            httpURLConnection = (java.net.HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(inputStream);
            java.io.BufferedReader bufferedReader = new java.io.BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }


     public class TaskRequestDirections extends android.os.AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            java.util.ArrayList points = null;

            com.google.android.gms.maps.model.PolylineOptions polylineOptions = null;

            for (java.util.List<java.util.HashMap<String, String>> path : lists) {
                points = new java.util.ArrayList();
                polylineOptions = new com.google.android.gms.maps.model.PolylineOptions();

                for (java.util.HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new com.google.android.gms.maps.model.LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(android.graphics.Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                googleMap.addPolyline(polylineOptions);
            } else {
                android.widget.Toast.makeText(getContext(), "Direction not found!", android.widget.Toast.LENGTH_SHORT).show();
            }

        }
    }





    }
