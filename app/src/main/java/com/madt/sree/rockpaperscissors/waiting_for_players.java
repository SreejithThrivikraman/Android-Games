package com.madt.sree.rockpaperscissors;



import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;


// The class for host device.

public class waiting_for_players extends AppCompatActivity

{

    private WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;

    // Code to generate and HostName name.
    // HostName name : To be displayed on opponent's device.

    public String HostName = NameGenerator.generate();



    // Tag for connection debugging.
    private String TAG = "Server module >>>>>>>> ";

    TextView player_1_label;
    TextView player_2_label;
    WroupService wroupService;





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

        ArrayList<String> movies = new ArrayList<>();
        movies.add("3 Idiots");
        movies.add("Deadpool 2");
        movies.add("Gangs of Wassaypur");
        movies.add("Go Goa Gone");
        movies.add("Garam Masala");
        movies.add("Black Panther");








        // method to start the server service.
        startServerDevice();

        // method to check for the client connection and disconnection.
      //  checkClients();




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





}
