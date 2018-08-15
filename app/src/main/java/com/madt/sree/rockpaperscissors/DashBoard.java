package com.madt.sree.rockpaperscissors;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DashBoard extends AppCompatActivity

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
