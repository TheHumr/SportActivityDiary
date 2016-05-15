package cz.thehumr.sportactivitydiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cz.thehumr.sportactivitydiary.model.Location;
import cz.thehumr.sportactivitydiary.model.Weather;
import cz.thehumr.sportactivitydiary.model.Workout;

/**
 * Created by Ondřej on 07.03.2016.
 */
public class WorkoutsDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "workoutsDatabase";
    private static final int DATABASE_VERSION = 11;

    // Table Names
    private static final String TABLE_WORKOUTS = "workouts";
    private static final String TABLE_LOCATIONS = "locations";
    private static final String TABLE_WEATHERS = "weathers";

    // Post Table Columns
    private static final String KEY_WORKOUT_ID = "workout_id";
    private static final String KEY_WORKOUT_TYPE = "type";
    private static final String KEY_WORKOUT_DATE = "date";
    private static final String KEY_WORKOUT_TIME = "time";
    private static final String KEY_WORKOUT_DURATION = "duration";
    private static final String KEY_WORKOUT_DISTANCE = "distance";
    private static final String KEY_WORKOUT_DESCRIPTION = "description";
    private static final String KEY_WORKOUT_SPEED = "speed";
    private static final String KEY_WORKOUT_FEELING = "feeling";
    private static final String KEY_WORKOUT_LOCATION_ID = "location_id";
    private static final String KEY_WORKOUT_WEATHER_ID = "weather_id";


    private static final String KEY_LOCATION_ID = "location_id";
    private static final String KEY_LOCATION_LATITUDE = "latitude";
    private static final String KEY_LOCATION_LONGITUDE = "longitude";
    private static final String KEY_LOCATION_NAME = "location_name";

    private static final String KEY_WEATHER_ID = "weather_id";
    private static final String KEY_WEATHER_NAME = "name";
    private static final String KEY_WEATHER_TEMPERATURE = "temperature";
    private static final String KEY_WEATHER_FEELSTEMPERATURE = "feels_temperature";
    private static final String KEY_WEATHER_WIND = "wind";
    private static final String KEY_WEATHER_ICON_URL = "iconURL";

    private static WorkoutsDatabaseHelper sInstance;

    public static synchronized WorkoutsDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WorkoutsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public WorkoutsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + TABLE_WORKOUTS +
                "(" +
                    KEY_WORKOUT_ID + " INTEGER PRIMARY KEY," +
                    KEY_WORKOUT_TYPE + " TEXT," +
                    KEY_WORKOUT_DATE + " NUMERIC," +
                    KEY_WORKOUT_TIME + " NUMERIC," +
                    KEY_WORKOUT_DURATION + " NUMERIC," +
                    KEY_WORKOUT_DISTANCE + " REAL," +
                    KEY_WORKOUT_DESCRIPTION + " TEXT," +
                    KEY_WORKOUT_FEELING + " REAL," +
                    KEY_WORKOUT_LOCATION_ID + " INTEGER," +
                    KEY_WORKOUT_WEATHER_ID + " INTEGER," +
                    "FOREIGN KEY(" + KEY_WORKOUT_LOCATION_ID +") REFERENCES " + TABLE_LOCATIONS + "(" + KEY_LOCATION_ID + ")," +
                    "FOREIGN KEY(" + KEY_WORKOUT_WEATHER_ID +") REFERENCES " + TABLE_WEATHERS + "(" + KEY_WEATHER_ID + ")" +
                ")";

        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS +
                "(" +
                KEY_LOCATION_ID + " INTEGER PRIMARY KEY," +
                KEY_LOCATION_LATITUDE + " REAL," +
                KEY_LOCATION_LONGITUDE + " REAL, " +
                KEY_LOCATION_NAME + " TEXT " +
                ")";

        String CREATE_WEATHERS_TABLE = "CREATE TABLE " + TABLE_WEATHERS +
                "(" +
                KEY_WEATHER_ID + " INTEGER PRIMARY KEY," +
                KEY_WEATHER_NAME + " TEXT," +
                KEY_WEATHER_TEMPERATURE + " REAL, " +
                KEY_WEATHER_FEELSTEMPERATURE + " REAL, " +
                KEY_WEATHER_WIND + " REAL, " +
                KEY_WEATHER_ICON_URL + " TEXT " +
                ")";

        db.execSQL(CREATE_LOCATIONS_TABLE);
        db.execSQL(CREATE_WEATHERS_TABLE);
        db.execSQL(CREATE_WORKOUTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHERS);
            onCreate(db);
        }
    }

    public void addWorkout(Workout workout){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues locationVaules = new ContentValues();
        locationVaules.put(KEY_LOCATION_LATITUDE, workout.getLocation().getLatitude());
        locationVaules.put(KEY_LOCATION_LONGITUDE, workout.getLocation().getLongitude());
        locationVaules.put(KEY_LOCATION_NAME, workout.getLocation().getName());
        db.insertOrThrow(TABLE_LOCATIONS, null, locationVaules);

        final String MY_QUERY = "SELECT MAX(location_id) FROM " + TABLE_LOCATIONS;
        Cursor cur = db.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int loc_id = cur.getInt(0);
        cur.close();

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(KEY_WEATHER_NAME, workout.getWeather().getName());
        weatherValues.put(KEY_WEATHER_TEMPERATURE, workout.getWeather().getTemperature());
        weatherValues.put(KEY_WEATHER_FEELSTEMPERATURE, workout.getWeather().getFeelsTemperature());
        weatherValues.put(KEY_WEATHER_WIND, workout.getWeather().getWind());
        weatherValues.put(KEY_WEATHER_ICON_URL, workout.getWeather().getIconURL());
        db.insertOrThrow(TABLE_WEATHERS, null, weatherValues);

        final String MY_QUERY2 = "SELECT MAX(weather_id) FROM " + TABLE_WEATHERS;
        Cursor cur2 = db.rawQuery(MY_QUERY2, null);
        cur2.moveToFirst();
        int weather_id = cur2.getInt(0);
        cur2.close();

        ContentValues workoutValues = new ContentValues();
        workoutValues.put(KEY_WORKOUT_TYPE, workout.getType());
        workoutValues.put(KEY_WORKOUT_DATE, workout.getDate());
        workoutValues.put(KEY_WORKOUT_TIME, workout.getTime());
        workoutValues.put(KEY_WORKOUT_DURATION, workout.getDuration());
        workoutValues.put(KEY_WORKOUT_DISTANCE, workout.getDistance());
        workoutValues.put(KEY_WORKOUT_DESCRIPTION, workout.getDescription());
        workoutValues.put(KEY_WORKOUT_FEELING, workout.getFeeling());
        workoutValues.put(KEY_WORKOUT_LOCATION_ID, loc_id);
        workoutValues.put(KEY_WORKOUT_WEATHER_ID, weather_id);

        db.insertOrThrow(TABLE_WORKOUTS, null, workoutValues);
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public List<Workout> getAllWorkouts(){
        List<Workout> workouts = new ArrayList<>();
        String WORKOUTS_SELECT_QUERY = "select * from " + TABLE_WORKOUTS + " w JOIN " + TABLE_LOCATIONS + " l ON w.location_id = l.location_id";
        //String WORKOUTS_SELECT_QUERY = String.format("select * from %s", TABLE_WORKOUTS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(WORKOUTS_SELECT_QUERY, null);
        if (cursor.moveToFirst()){
            do {
                Workout workout = new Workout();
                workout.setId(cursor.getInt(cursor.getColumnIndex(KEY_WORKOUT_ID)));
                workout.setType(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_TYPE)));
                workout.setDate(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_DATE)));
                workout.setTime(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_TIME)));
                workout.setDuration(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_DURATION)));
                workout.setDistance(cursor.getDouble(cursor.getColumnIndex(KEY_WORKOUT_DISTANCE)));
                workout.setDescription(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_DESCRIPTION)));
                workout.setFeeling(cursor.getDouble(cursor.getColumnIndex(KEY_WORKOUT_FEELING)));

                Location location = new Location();
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE)));
                workout.setLocation(location);

                workouts.add(workout);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return workouts;
    }

    public Workout getWorkoutById(int id){
        Workout workout = new Workout();
        String WORKOUT_SELECT_QUERY = "select * from " + TABLE_WORKOUTS + " w " +
                "JOIN " + TABLE_LOCATIONS + " l ON w.location_id = l.location_id " +
                "JOIN " + TABLE_WEATHERS + " we ON w.weather_id = we.weather_id " +
                "where w.workout_id = " + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(WORKOUT_SELECT_QUERY, null);
        if(cursor.moveToFirst()){
            workout.setType(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_TYPE)));
            workout.setDate(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_DATE)));
            workout.setTime(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_TIME)));
            workout.setDuration(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_DURATION)));
            workout.setDistance(cursor.getDouble(cursor.getColumnIndex(KEY_WORKOUT_DISTANCE)));
            workout.setDescription(cursor.getString(cursor.getColumnIndex(KEY_WORKOUT_DESCRIPTION)));
            workout.setFeeling(cursor.getDouble(cursor.getColumnIndex(KEY_WORKOUT_FEELING)));

            Location location = new Location();
            location.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE)));
            location.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE)));
            workout.setLocation(location);

            Weather weather = new Weather();
            weather.setName(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_NAME)));
            weather.setTemperature(cursor.getDouble(cursor.getColumnIndex(KEY_WEATHER_TEMPERATURE)));
            weather.setFeelsTemperature(cursor.getDouble(cursor.getColumnIndex(KEY_WEATHER_FEELSTEMPERATURE)));
            weather.setWind(cursor.getDouble(cursor.getColumnIndex(KEY_WEATHER_WIND)));
            weather.setIconURL(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_ICON_URL)));
            workout.setWeather(weather);

        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return workout;
    }

    public boolean deleteWorkout(int id){
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_WORKOUTS, KEY_WORKOUT_ID + "=" + id, null) > 0;
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        String LOCATIONS_SELECT_QUERY = "select " + KEY_LOCATION_NAME + ", " + KEY_LOCATION_LATITUDE + ", " + KEY_LOCATION_LONGITUDE + ", count(" + KEY_LOCATION_ID + ") AS pocet from " + TABLE_LOCATIONS + " GROUP BY " + " " + KEY_LOCATION_NAME + " ORDER BY pocet desc";
        //String WORKOUTS_SELECT_QUERY = String.format("select * from %s", TABLE_WORKOUTS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LOCATIONS_SELECT_QUERY, null);
        if (cursor.moveToFirst()){
            do {
                Location location = new Location();
                location.setName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE)));
                location.setNumOfWorkouts(cursor.getInt(cursor.getColumnIndex("pocet")));
                locations.add(location);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return locations;
    }

    public Location getLocationByName(String name){
        Location location = new Location();
        String LOCATION_SELECT_QUERY = "select * from " + TABLE_LOCATIONS + " where " + KEY_LOCATION_NAME + " = '" + name + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LOCATION_SELECT_QUERY, null);
        if(cursor.moveToFirst()){
            location.setName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
            location.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE)));
            location.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE)));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return location;
    }

    public Location getLastLocation(){
        Location location = new Location();
        String QUERY = "select * from " + TABLE_LOCATIONS + " where " + KEY_LOCATION_ID + " = (select MAX(" + KEY_LOCATION_ID + ") from " + TABLE_LOCATIONS + ")";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.moveToFirst()){
            location.setName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
            location.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE)));
            location.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE)));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return location;
    }

    public int getCountOfWorkoutsByType(String type, String dateFrom){
        int count = 0;
        String QUERY = "select count(" + KEY_WORKOUT_ID + ") as countOfWorkouts from " + TABLE_WORKOUTS + " where " + KEY_WORKOUT_TYPE + " = '" + type + "' and " + KEY_WORKOUT_DATE + " >= '" + dateFrom + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.moveToFirst()){
            count = cursor.getInt(cursor.getColumnIndex("countOfWorkouts"));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return count;
    }

    public double getSumOfDistanceByType(String type, String dateFrom){
        double sum = 0;
        String QUERY = "select sum(" + KEY_WORKOUT_DISTANCE + ") as sumOfDistance from " + TABLE_WORKOUTS + " where " + KEY_WORKOUT_TYPE + " = '" + type + "' and " + KEY_WORKOUT_DATE + " >= '" + dateFrom + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.moveToFirst()){
            sum = cursor.getInt(cursor.getColumnIndex("sumOfDistance"));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return sum;
    }


}
