package com.madt.sree.rockpaperscissors;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.common.WiFiDirectBroadcastReceiver;
import com.abemart.wroup.common.WiFiP2PError;
import com.abemart.wroup.common.WiFiP2PInstance;
import com.abemart.wroup.common.WroupServiceDevice;
import com.abemart.wroup.common.listeners.ServiceDiscoveredListener;
import com.abemart.wroup.common.messages.MessageWrapper;
import com.abemart.wroup.service.WroupService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


// The class for the client devices.

public class waiting_for_host extends AppCompatActivity implements ChildEventListener

{


    FirebaseDatabase database;
    DatabaseReference root,initialise;


    private String TAG_CLIENT = "Client module : ";
    private ArrayAdapter adapter;//adapter for listView
    private WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;
    public String name = "";
    Handler wait = new Handler();
    public String client_player = "";

    List<String> player_array_list = new ArrayList<String>();

    int delay = 1000;
    Runnable runnable;


    // firebase codes for child listner

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {

    }


    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onCancelled(DatabaseError databaseError)
    {

    }


    // firebase codes for child listner

    public static class Game {

        public Boolean status;

public Game() {

}
        public Game(Boolean status) {
            this.status = status;
            // ...
        }

    }

    @Override
    protected void onResume() {


        wait.postDelayed( runnable = new Runnable() {
            public void run() {

                Log.d("check","in running state");
                root.orderByChild("status").addListenerForSingleValueEvent(new ValueEventListener()  {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                            //System.out.println(snapshot.getValue().toString());

                            Game game = snapshot.getValue(Game.class);
                            System.out.println(game.status);

                            //start intent for rock/paper/scissor game here whenever value is true

                        if(game.status == true)
                        {
                            System.out.println("Starting intent.....");
                            Intent intent = new Intent(waiting_for_host.this, GameActivity.class);
                            startActivity(intent);
                        }

                            //the code will run every 1 second until the intent is changed i.e until the admin changes the value of Game to true

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getMessage());
                    }

                });
                wait.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        wait.removeCallbacks(runnable);
        super.onPause();
    }


    public String client_name = NameGenerator.generate();

    TextView host_name_label;
    WroupClient wroupClient;




    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_host);
        database = FirebaseDatabase.getInstance();

        root = database.getReference("Game");
        Log.d("check","in oncfeate state");

        initialise = database.getReference("OnlineUsers");
        Log.d("check","Created client name");

        wiFiDirectBroadcastReceiver = WiFiP2PInstance.getInstance(this).getBroadcastReceiver();
        host_name_label = findViewById(R.id.host_name);
        host_name_label.setText("");



        wroupClient = WroupClient.getInstance(getApplicationContext());

        Bundle bundle  = getIntent().getExtras();
        client_player  = bundle.getString("Player Name");





        // method to start the client service.
        startClientDevice();





    }


    private void startClientDevice()
    {
        wroupClient.discoverServices(5000L, new ServiceDiscoveredListener() {

            @Override
            public void onNewServiceDeviceDiscovered(WroupServiceDevice serviceDevice)
            {
                // Found new server Device.






                name = serviceDevice.getTxtRecordMap().get(WroupService.SERVICE_GROUP_NAME);
                host_name_label.append(name);

                // notification message to server.
                MessageWrapper message = new MessageWrapper();
                message.setMessage(client_name);
                message.setMessageType(MessageWrapper.MessageType.CONNECTION_MESSAGE);

                wroupClient.sendMessage(serviceDevice,message);



                CreateGame(client_player);
                Toast.makeText(getApplicationContext(),"Discovered Server : " + name ,Toast.LENGTH_SHORT).show();


                // Apply the logic to add the client name to firebase here.  //
                // Apply the logic to add the client name to firebase here.  //


            }

            @Override
            public void onFinishServiceDeviceDiscovered(List<WroupServiceDevice> serviceDevices) {
                // The list of services discovered in the time indicated

                Log.d(TAG_CLIENT," Total no .of Server Devices = " + serviceDevices.size());




                Toast.makeText(getApplicationContext(),"Message sent to server " + name ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(WiFiP2PError wiFiP2PError)
            {
                // An error occurred during the searching
                Log.e(TAG_CLIENT," Error in discovering servers.");

            }
        });
    }

    //Every play that want to join a game call this function
    public void CreateGame(String player)
    {

        initialise.child(player).setValue("Client Player");
    }





}
