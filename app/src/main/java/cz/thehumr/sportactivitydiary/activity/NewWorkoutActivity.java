package cz.thehumr.sportactivitydiary.activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.database.WorkoutsDatabaseHelper;
import cz.thehumr.sportactivitydiary.fragment.DatePickerFragment;
import cz.thehumr.sportactivitydiary.fragment.TimePickerFragment;
import cz.thehumr.sportactivitydiary.model.Location;
import cz.thehumr.sportactivitydiary.model.Weather;
import cz.thehumr.sportactivitydiary.model.Workout;
import cz.thehumr.sportactivitydiary.service.WeatherApi;

public class NewWorkoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

{
    Toolbar toolbar;

    private Spinner spinnerActivityType;
    private EditText datepicker;
    private EditText timepicker;
    private EditText editTextDistance;
    private EditText editTextDuration;
    private EditText editTextDescription;
    private RatingBar ratingBarFeeling;
    private ScrollView scrollView;
    private ImageView transparentImageView;
    private Spinner spinnerLocations;

    private Location location;
    private android.location.Location currentLocation;

    private TextView txtSelectNewLocation;

    final WorkoutsDatabaseHelper databaseHelper = WorkoutsDatabaseHelper.getInstance(this);

    boolean newLocation;
    private Location lastLocation;

    private GoogleApiClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);

        location = new Location();
        newLocation = false;

        initViews();
        setupToolbar();
        setupSpinnerAdapter();
        //setupMap();

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        String dateString = dateFormat.format(cal.getTime());
        datepicker.setText(dateString);
        timepicker.setText("00:00");

        setupListeners();

        mLocationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationClient.connect();


    }

    private void setupMap() {
        CameraPosition cameraPosition;
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

        LatLng latLng;
        if (currentLocation != null){
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            Location location = databaseHelper.getLastLocation();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            this.location = location;
        }
        cameraPosition = new CameraPosition(latLng,9,0,0);

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .camera(cameraPosition)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .zoomGesturesEnabled(true)
                .zoomControlsEnabled(true)
                .tiltGesturesEnabled(true);


        SupportMapFragment mMapFragment = SupportMapFragment.newInstance(options);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mapContainer, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);


        transparentImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        spinnerActivityType = (Spinner) findViewById(R.id.spinner_activity_type);
        datepicker = (EditText) findViewById(R.id.editText_datepicker);
        timepicker = (EditText) findViewById(R.id.editText_timepicker);
        editTextDistance = (EditText) findViewById(R.id.editText_distance);
        editTextDuration = (EditText) findViewById(R.id.editText_duration);
        editTextDescription = (EditText) findViewById(R.id.editText_description);
        ratingBarFeeling = (RatingBar) findViewById(R.id.ratingBar);
        scrollView = (ScrollView) findViewById(R.id.scrlView_new_workout);
        txtSelectNewLocation = (TextView) findViewById(R.id.txt_select_new_location);
        spinnerLocations = (Spinner) findViewById(R.id.spinner_locations);

        transparentImageView = (ImageView) findViewById(R.id.transparent_image);

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListeners() {
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        txtSelectNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newLocation) {
                    spinnerLocations.setEnabled(false);
                    setupMap();
                    newLocation = true;
                }
            }
        });
    }

    private void setupSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapterActivityType = ArrayAdapter.createFromResource(this, R.array.activities_array, android.R.layout.simple_spinner_item);
        adapterActivityType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivityType.setAdapter(adapterActivityType);
        spinnerActivityType.setOnItemSelectedListener(this);

        List<Location> locations = databaseHelper.getAllLocations();
        final List<String> locationsNames = new ArrayList<>();
        for(Location l: locations){
            locationsNames.add(0, l.getName());
        }

        ArrayAdapter<String> adapterLocations = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationsNames);
        adapterLocations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(adapterLocations);
        spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = databaseHelper.getLocationByName(locationsNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void showDatePickerDialog(){
        DialogFragment datePickerFragment = DatePickerFragment.newInstance(101);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(){
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng latLng;
        if (currentLocation != null){
            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            this.location = new Location(latLng.latitude, latLng.longitude, getAddressName(latLng));
        } else {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        MarkerOptions defaultMarker = new MarkerOptions().position(latLng);
        googleMap.addMarker(defaultMarker);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude)).title("New Marker");
                googleMap.clear();
                googleMap.addMarker(marker);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


                location.setName(getAddressName(latLng));

                Toast.makeText(NewWorkoutActivity.this, "Selected location: " + location.getName(),Toast.LENGTH_LONG).show();

            }
        });
    }

    public String getAddressName(LatLng latLng){
        Geocoder geocoder = new Geocoder(NewWorkoutActivity.this);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String addressName = "";
        Address address = addressList.get(0);
        if (address.getFeatureName() != null) {
            addressName = address.getFeatureName();
        }
        if (address.getLocality() != null) {
            if (!"".equals(addressName)){
                addressName = addressName + ", ";
            }
            addressName = addressName + address.getLocality();
        }
        if (address.getSubLocality() != null) {
            if (!"".equals(addressName)){
                addressName = addressName + ", ";
            }
            addressName = addressName + address.getSubLocality();
        }

        return addressName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_workout) {
            final Workout workout = new Workout();

            workout.setType((String) spinnerActivityType.getSelectedItem());
            workout.setDistance(Double.parseDouble(editTextDistance.getText().toString()));
            workout.setDate(datepicker.getText().toString());
            workout.setTime(timepicker.getText().toString());
            workout.setDuration(editTextDuration.getText().toString());
            workout.setDescription(editTextDescription.getText().toString());
            workout.setFeeling(ratingBarFeeling.getRating());
            workout.setLocation(location);

            new AsyncTask<String, Void, Weather>(){

                @Override
                protected Weather doInBackground(String... params) {
                    return WeatherApi.getWeatherByLocation(params[0], params[1], params[2]);
                }

                @Override
                protected void onPostExecute(Weather weather) {
                    workout.setWeather(weather);
                    databaseHelper.addWorkout(workout);
                    Toast.makeText(NewWorkoutActivity.this, "Workout sccesfully added", Toast.LENGTH_LONG).show();
                }



            }.execute("" + location.getLatitude(), "" + location.getLongitude(), datepicker.getText().toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
