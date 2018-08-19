package com.madt.sree.rockpaperscissors;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.seismic.ShakeDetector;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements ShakeDetector.Listener {
    final Random rnd = new Random();
    TextView players;
    TextView playerScore;
    String playerId="player1";

    @Override
    public void hearShake() {
        final ImageView img = (ImageView) findViewById(R.id.imgRandom);

        final String str = "img_" + rnd.nextInt(3);
        img.setImageDrawable
                (
                        getResources().getDrawable(getResourceID(str, "drawable",
                                getApplicationContext()))
                );
    }

    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }

    public static class player {

        public String name, selected;
        public int score;
        public boolean status;

        public player() {

        }
        public player(String name, Integer score, String selected, Boolean status) {
            this.name = name;
            this.score = score;
            this.selected = selected;
            this.status = status;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector detector = new ShakeDetector(GameActivity.this);
        detector.start(manager);
        if(playerId == "player1") {

        DatabaseReference ref = database.getReference("Game/player1");
        players = (TextView) findViewById(R.id.textViewP);
        playerScore = (TextView) findViewById(R.id.textViewS);
        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//set player

                    player player = dataSnapshot.getValue(player.class);
                    System.out.println(player.name);
                    System.out.println(player.selected);
                    System.out.println(player.score);
                    players.setText(player.name);
                    playerScore.setText("Score:" + player.score);
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    else if (playerId == "player2"){
            DatabaseReference ref = database.getReference("Game/player2");
            players = (TextView) findViewById(R.id.textViewP);
            playerScore = (TextView) findViewById(R.id.textViewS);
            ref.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//set player

                    player player = dataSnapshot.getValue(player.class);
                    System.out.println(player.name);
                    System.out.println(player.selected);
                    System.out.println(player.score);
                    players.setText(player.name);
                    playerScore.setText("Score:" + player.score);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });


        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Quiz Game", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



}
