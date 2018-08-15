package com.madt.sree.rockpaperscissors;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.seismic.ShakeDetector;

public class DashBoard extends AppCompatActivity implements ShakeDetector.Listener

{

    AnimationDrawable Image_Animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ImageView anim_Images = findViewById(R.id.animation_images);


        anim_Images.setImageDrawable(getResources().getDrawable(R.drawable.animation));
        Image_Animation = (AnimationDrawable) anim_Images.getDrawable();
        Image_Animation.start();

        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector detector = new ShakeDetector(this);
        detector.start(manager);

    }
    // this a mandatory function
    // it's required by the ShakeDetector.Listener class
    @Override public void hearShake()  {


    }

    public void initiate_host_screen(View v)
    {
        Intent host_screen = new Intent(getApplicationContext(),waiting_for_players.class);
        startActivity(host_screen);
    }

    public void initiate_client_screen(View v)
    {
        Intent client_screen = new Intent(getApplicationContext(),waiting_for_host.class);
        startActivity(client_screen);
    }

}
