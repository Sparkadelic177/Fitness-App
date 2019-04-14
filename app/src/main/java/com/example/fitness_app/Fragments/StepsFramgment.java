package com.example.fitness_app.Fragments;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness_app.MainActivity;
import com.example.fitness_app.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

//THis class will show the users steps.

public class StepsFramgment extends Fragment {

    private static final String ANDROID_CHANNEL_ID = "dataCubed.Android";
    TextView tvCounter;
    Button btnWorkLocation;
    Button btnResetLocation;
    Button btnTweet;

    String location = "";
    String stepData;
    AsyncHttpClient client;
    final String TAG = "SPLAT";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stepData = getArguments().getString("stepData");
        }
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocation();
        atWork();


        tvCounter = view.findViewById(R.id.tvCounter);
        btnWorkLocation = view.findViewById(R.id.btnWorkLocation);
        btnResetLocation = view.findViewById(R.id.btnResetLocation);
        ParseUser user = new ParseUser();


        if(user.getString("worklocation") == null){

        }

//       need to get the steps from the main activity();
        tvCounter.setText(stepData);

        tvCounter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                thousandSteps();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnWorkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkLocation();
            }
        });

        btnResetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkLocation();
            }
        });

    }

    //for every step that is a multiple of 1000 it would say congrats
    public void thousandSteps(){
        final int length = tvCounter.getText().toString().length();
        final int divisor = 1000;
        if(length % divisor == 0){
            Toast.makeText(getContext(),"Congratulations You Reach 1000 more Steps", Toast.LENGTH_LONG).show();
        }
    }


    public void getLocation(){

        client = new AsyncHttpClient();
        client.post("https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyBTJDZJhqV_jGVQfLcUrhwcsE8POe07i80", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject coordinates = response.getJSONObject("location");
                    String lat = coordinates.getString("lat");
                    String lng = coordinates.getString("lng");
                    location = lat + lng;
                    Log.d(TAG, "it worked " + lat + " " + lng);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "it didnt got through:" + e.getMessage());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("smile","error could not get the location");
            }
        });
    }

    public void saveWorkLocation(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("workLocation", location);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if( e != null){
                    Log.e(TAG, "Work location was not saved");
                    return;
                }
                btnWorkLocation.setEnabled(false);
                btnWorkLocation.setBackgroundColor(getResources().getColor(R.color.gray));
                btnResetLocation.setEnabled(true);
                Toast.makeText(getContext(), "work location saved: " + location, Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).setUpAlarm();
            }
        });
    }

    //if at work then start a timer for every hour
    public void atWork(){
        Log.d(TAG, "checked if user is at work");
        ParseUser user = ParseUser.getCurrentUser();
        String workLocation = user.getString("workLocation");
        if(workLocation != null){
            if(workLocation.equals(location)){
                //start timer
                ((MainActivity) getActivity()).setUpAlarm();
            }
        }
        else{
            Toast.makeText(getContext(), "You haven't saved your work location", Toast.LENGTH_SHORT).show();
            btnResetLocation.setEnabled(false);
            btnResetLocation.setBackgroundColor(getResources().getColor(R.color.gray));
            btnWorkLocation.setEnabled(true);
        }

    }


}
