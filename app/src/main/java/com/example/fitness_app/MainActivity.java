package com.example.fitness_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness_app.Fragments.CompetitionFragment;
import com.example.fitness_app.Fragments.StepsFramgment;
import com.parse.ParseUser;



public class MainActivity extends AppCompatActivity implements SensorEventListener{

    final String TAG = "SPLAT";
    TextView logout;

    boolean running = false;
    SensorManager sensorManager;
    Sensor sensor;
    String steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpSensor();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        logout = findViewById(R.id.tvLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logout();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.mSteps);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = new StepsFramgment();
                if(menuItem.getItemId() == R.id.mSteps){
                    fragment = new StepsFramgment();
                }else if(menuItem.getItemId() == R.id.mCompetition){
                    fragment = new CompetitionFragment();
                }else Log.e(TAG,"wrong id? " + menuItem.getItemId());

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
    }

    public void logout(){
        Intent i = new Intent(this, LoginActivity.class);
        ParseUser.logOut();
        startActivity(i);
        finish();
    }

    public void setUpSensor(){
        //sensor triggers an event each time the user takes a step
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //triggers an event each time the user takes a step
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        if(sensor == null){
            Toast.makeText(this,"sensor was not found",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"sensor was found",Toast.LENGTH_LONG).show();
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
        if (running)  steps = String.valueOf(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public String getSteps(){
        return steps;
    }

    public void sendNotification(View view) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());

        //Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.ic_walk;
        mBuilder.setContentTitle("Standing Time");
        mBuilder.setContentText("Its time to stand up and move those legs");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }


}




