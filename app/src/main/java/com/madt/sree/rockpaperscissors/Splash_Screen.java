package com.madt.sree.rockpaperscissors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity

{

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);


        Thread mythread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(7000);
                    Intent intent = new Intent(getApplicationContext(),DashBoard.class);
                    startActivity(intent);
                    finish();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

        };


        mythread.start();

    }


}
