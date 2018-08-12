package com.madt.sree.rockpaperscissors;

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
}
