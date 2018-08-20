package com.madt.sree.rockpaperscissors;



import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abemart.wroup.common.WiFiDirectBroadcastReceiver;
import com.abemart.wroup.common.WiFiP2PError;
import com.abemart.wroup.common.WiFiP2PInstance;
import com.abemart.wroup.common.WroupDevice;
import com.abemart.wroup.common.listeners.ClientConnectedListener;
import com.abemart.wroup.common.listeners.ClientDisconnectedListener;
import com.abemart.wroup.common.listeners.DataReceivedListener;
import com.abemart.wroup.common.listeners.ServiceRegisteredListener;
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


// The class for host device.

public class waiting_for_players extends AppCompatActivity implements MyAdapter.ItemClickListener

{
    Handler wait = new Handler();



    int delay = 1000;
    Runnable runnable;








    private MyAdapter mAdapter;

    public FirebaseDatabase database = FirebaseDatabase.getInstance();

    public DatabaseReference online_user_ref = database.getReference("OnlineUsers");

    private WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;



    // Code to generate and HostName name.
    // HostName name : To be displayed on opponent's device.

    public String HostName = NameGenerator.generate();

    public ArrayList<String> Userlist =  new ArrayList<>();



    // Tag for connection debugging.
    private String TAG = "Server module >>>>>>>> ";

    TextView player_1_label;
    TextView player_2_label;
    WroupService wroupService;
    public  ArrayList<String> players = new ArrayList<>();


    public static class player
    {

        public String name;


        public player()
        {

        }

        public player(String name)
        {
            this.name = name;

        }

    }






    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_players);


        player_1_label = findViewById(R.id.player_1);


        wiFiDirectBroadcastReceiver = WiFiP2PInstance.getInstance(this).getBroadcastReceiver();



        wroupService = WroupService.getInstance(getApplicationContext());

        System.out.println(">>>>>>>>>>>>>> Host Name : " + HostName);

        player_1_label.setText("Server name :" + HostName);


//        players.add("Da");
//        players.add("Pa");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager mlm = (LinearLayoutManager) recyclerView.getLayoutManager();


        // add a "line" between each row
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mlm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        mAdapter = new MyAdapter(this, players);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);











        ValueEventListener valueEventListener = online_user_ref.addValueEventListener(new ValueEventListener()
        {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {


                int i = 0;

                Userlist.clear();
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {

                    Userlist.add(String.valueOf(dsp.getKey()));

                   // players.add(Userlist.get(i));




                }

                runOnUiThread(new Runnable()
                {


                    @Override
                    public void run()
                    {


                        players.clear();

                        for (String s1 : Userlist)
                        {


                            players.add(s1);



                            mAdapter.notifyDataSetChanged();


                        }
                    }
                });








            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }


        });


        // method to start the server service.
        startServerDevice();





    }


    // method to start the server service.
    private void startServerDevice()
    {

        wroupService.registerService(HostName.toString(), new ServiceRegisteredListener() {

            @Override
            public void onSuccessServiceRegistered()
            {
                Log.d(TAG,"Server service registered successfully.");
            }

            @Override
            public void onErrorServiceRegistered(WiFiP2PError wiFiP2PError)
            {
                Toast.makeText(getApplicationContext(), "Error creating group", Toast.LENGTH_SHORT).show();
            }

        });

        wroupService.setClientConnectedListener(new ClientConnectedListener()
        {
            @Override
            public void onClientConnected(WroupDevice wroupDevice)
            {
                // New client connected to the group
                player_2_label.append("\n" + wroupDevice.getCustomName().toString());

                Log.d(TAG,wroupDevice.getCustomName().toString() + " >>>>>>>> connected");

                String name = wroupDevice.getDeviceName();
                Toast.makeText(getApplicationContext(), "Client = " + name, Toast.LENGTH_SHORT).show();


            }
        });



        wroupService.setClientDisconnectedListener(new ClientDisconnectedListener()
        {
            @Override
            public void onClientDisconnected(WroupDevice wroupDevice) {
                // Client disconnected from the group

                Log.d(TAG,wroupDevice.getCustomName().toString() + " disconnected");

            }
        });


    }

    // method to check for the client connection and disconnection.

    // below code is not working :(
    private void checkClients()
    {
        wroupService.setClientConnectedListener(new ClientConnectedListener()
        {
            @Override
            public void onClientConnected(WroupDevice wroupDevice)
            {
                // New client connected to the group
                player_2_label.append("\n" + wroupDevice.getCustomName().toString());

                Log.d(TAG,wroupDevice.getCustomName().toString() + " >>>>>>>> connected");

                String name = wroupDevice.getDeviceName();
                Toast.makeText(getApplicationContext(), "Client = " + name, Toast.LENGTH_SHORT).show();


            }
        });

        // below code is not working :(
        wroupService.setClientDisconnectedListener(new ClientDisconnectedListener()
        {
            @Override
            public void onClientDisconnected(WroupDevice wroupDevice) {
                // Client disconnected from the group

                Log.d(TAG,wroupDevice.getCustomName().toString() + " disconnected");

            }
        });
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        unregisterReceiver(wiFiDirectBroadcastReceiver);

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(wiFiDirectBroadcastReceiver, intentFilter);

       



    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
