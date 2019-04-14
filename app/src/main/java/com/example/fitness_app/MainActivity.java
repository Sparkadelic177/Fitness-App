package com.example.fitness_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness_app.Fragments.CompetitionFragment;
import com.example.fitness_app.Fragments.StepsFramgment;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int NOTIFICATION_ID = 0;
    final private int STEPS_OFFSET = 50;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private final String TAG = "SPLAT";
    private TextView logout;
    private String stepData;
    PendingIntent notifyPendingIntent;
    Intent notifyIntent;

    boolean running = false;
    SensorManager sensorManager;
    Sensor sensor;
    int steps;

    ParseUser user = ParseUser.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //used to change navigation on the main activity
        final FragmentManager fragmentManager = getSupportFragmentManager();

        //setting up alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //Set up the Notification Broadcast Intent
        notifyIntent = new Intent(this, AlarmReceiver.class);


        //set up the Pending Intent for it to fire off
        notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        logout = findViewById(R.id.tvLogout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets a automatic navigation view
        bottomNavigationView.setSelectedItemId(R.id.mSteps);


        //sets up the walk sensors
        setUpSensor();


        //passing the step data from the main activity to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("stepData", stepData);
        Fragment stepsFramgment = new Fragment();
        stepsFramgment.setArguments(bundle);


        //set up click listener to logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    logout();
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = new StepsFramgment();
                if (menuItem.getItemId() == R.id.mSteps) {
                    fragment = new StepsFramgment();
                } else if (menuItem.getItemId() == R.id.mCompetition) {
                    fragment = new CompetitionFragment();
                } else Log.e(TAG, "wrong id? " + menuItem.getItemId());

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

    }

    public void logout() {
        Intent i = new Intent(this, LoginActivity.class);
        ParseUser.logOut();
        startActivity(i);
        finish();
    }

    public void setUpAlarm(){
        //used to set an alarm to stand up every hour will at work
        if(!alarmSet()){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                    AlarmManager.INTERVAL_HOUR, notifyPendingIntent);

            Toast.makeText(this, "The Hour alarm has been set", Toast.LENGTH_LONG).show();
        }

    }

    public boolean alarmSet(){
        //Check if the Alarm is already set, and check the toggle accordingly
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        return alarmUp;
    }

    public void setUpSensor() {
        //sensor triggers an event each time the user takes a step
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //triggers an event each time the user takes a step
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        if (sensor == null) {
            Toast.makeText(this, "sensor was not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "sensor was found", Toast.LENGTH_LONG).show();
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);
    }

    //next three methods are used for android sensor
    @Override
    protected void onPause() {
        super.onPause();
        //passing would take off the listener, no steps counted
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] > Integer.MAX_VALUE) {
            if (BuildConfig.DEBUG) Log.d(TAG, "probably not a real value: " + event.values[0]);
            return;
        } else {
            if (running) steps = (int) event.values[0];
            stepData = Integer.toString(getSteps());
            upDateIfNeeded();
        }

    }

    //updates the amount of steps the user has
    private void upDateIfNeeded() {
        final int lastSavedSteps = Integer.parseInt(user.getString("steps"));
        if (steps > lastSavedSteps + STEPS_OFFSET) {
            user.put("steps", steps);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.d(TAG, "steps are saved: " + lastSavedSteps);
                }
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int getSteps() {
        return steps;
    }

//    creates Channels to send notification
}



