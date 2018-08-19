package com.madt.sree.rockpaperscissors;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.seismic.ShakeDetector;

import java.util.Map;

public class DashBoard extends AppCompatActivity

{

    AnimationDrawable Image_Animation;
    public EditText Dashboard_playerName;
    public String player_name_value = "" ;

    // setup firebase code
    static DatabaseReference myRef;

    public interface ResultCallBack

    {

        void onComplete(Map<String, String> result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // setup firebase variables
        myRef = FirebaseDatabase.getInstance().getReference();

        ImageView anim_Images = findViewById(R.id.animation_images);
        Dashboard_playerName = findViewById(R.id.player_name);


        anim_Images.setImageDrawable(getResources().getDrawable(R.drawable.animation));
        Image_Animation = (AnimationDrawable) anim_Images.getDrawable();
        Image_Animation.start();





    }



    public void initiate_host_screen(View v)
    {
        player_name_value = Dashboard_playerName.getText().toString();

        if(player_name_value.isEmpty())
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage("Please enter the user name to continue !");
            dlgAlert.setTitle("Error Message...");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });

        }

        else
            {
                CreateGame(player_name_value);
                Intent host_screen = new Intent(getApplicationContext(),waiting_for_players.class);
                startActivity(host_screen);
            }


    }

    public void initiate_client_screen(View v)
    {

        player_name_value = Dashboard_playerName.getText().toString();

        if(player_name_value.isEmpty())
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

            dlgAlert.setMessage("Please enter the user name to continue !");
            dlgAlert.setTitle("Error Message...");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });

        }

        else
        {

            Intent client_screen = new Intent(getApplicationContext(),waiting_for_host.class);
            client_screen.putExtra("Player Name", player_name_value);
            startActivity(client_screen);
        }

    }



    //Every play that want to join a game call this function
    public static void CreateGame(String player)
    {

      myRef.child("OnlineUsers").child(player).setValue("Host Player");
    }

}
