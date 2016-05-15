package cz.thehumr.sportactivitydiary.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.database.WorkoutsDatabaseHelper;
import cz.thehumr.sportactivitydiary.model.Workout;

public class WorkoutDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;

    WorkoutsDatabaseHelper databaseHelper;
    Workout workout;
    int id;

    ImageView imageViewWorkoutTypeIcon;
    TextView textViewDuration;
    TextView textViewDistance;
    TextView textViewSpeed;
    TextView textViewDescription;

    TextView textViewTemperature;
    TextView textViewWeatherName;
    TextView textViewWeatherFeelsTemperature;
    TextView textViewWeatherWind;
    ImageView imageViewWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        initViews();

        setupToolbar();

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("workout_id");

        databaseHelper = WorkoutsDatabaseHelper.getInstance(this);
        workout = databaseHelper.getWorkoutById(id);

        switch (workout.getType()){
            case "Cycling":
                imageViewWorkoutTypeIcon.setImageResource(R.drawable.cycling);
                break;
            case "Running":
                imageViewWorkoutTypeIcon.setImageResource(R.drawable.running);
                break;
            case "Walking":
                imageViewWorkoutTypeIcon.setImageResource(R.drawable.walking);
                break;
            default:
                imageViewWorkoutTypeIcon.setImageResource(R.drawable.running);
                break;
        }

        textViewDuration.setText(workout.getDuration());
        textViewDistance.setText(workout.getDistance() + " KM");
        textViewSpeed.setText("-/- KM/H");
        textViewDescription.setText(workout.getDescription());

        textViewWeatherName.setText(workout.getWeather().getName());
        textViewTemperature.setText(workout.getWeather().getTemperature() + " °C");
        textViewWeatherFeelsTemperature.setText(workout.getWeather().getFeelsTemperature() + " °C");
        textViewWeatherWind.setText(workout.getWeather().getWind() + " Kmph");


        new DownloadImageTask(imageViewWeatherIcon)
                .execute(workout.getWeather().getIconURL());

        this.setTitle(workout.getType() + " - " + workout.getDate());

        setupMap();
    }


    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        imageViewWorkoutTypeIcon = (ImageView) findViewById(R.id.img_workout_type);
        textViewDuration = (TextView) findViewById(R.id.txt_duration);
        textViewDistance = (TextView) findViewById(R.id.txt_distance);
        textViewSpeed = (TextView) findViewById(R.id.txt_speed);
        textViewDescription = (TextView) findViewById(R.id.txt_description);

        textViewTemperature = (TextView) findViewById(R.id.txt_weather_temperature);
        textViewWeatherName = (TextView) findViewById(R.id.txt_weather_name);
        textViewWeatherFeelsTemperature = (TextView) findViewById(R.id.txt_weather_feelstemperature);
        textViewWeatherWind = (TextView) findViewById(R.id.txt_weather_wind);
        imageViewWeatherIcon = (ImageView) findViewById(R.id.img_weather_icon);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupMap() {
        CameraPosition cameraPosition = new CameraPosition(new LatLng(workout.getLocation().getLatitude(), workout.getLocation().getLongitude()),9,0,0);

        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .camera(cameraPosition);


        SupportMapFragment mMapFragment = SupportMapFragment.newInstance(options);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mapContainer, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(workout.getLocation().getLatitude(), workout.getLocation().getLongitude())).title("New Marker");
        googleMap.addMarker(marker);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

