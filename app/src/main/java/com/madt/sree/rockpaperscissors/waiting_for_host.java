package com.madt.sree.rockpaperscissors;


import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.ArrayAdapter;

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

import java.util.List;


// The class for the client devices.

public class waiting_for_host extends AppCompatActivity

{




    private String TAG_CLIENT = "Client module : ";
    private ArrayAdapter adapter;//adapter for listView
    private WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;
    public String name = "";



    public String client_name = NameGenerator.generate();

    TextView host_name_label;
    WroupClient wroupClient;




    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_host);


        wiFiDirectBroadcastReceiver = WiFiP2PInstance.getInstance(this).getBroadcastReceiver();
        host_name_label = findViewById(R.id.host_name);
        host_name_label.setText("");



        wroupClient = WroupClient.getInstance(getApplicationContext());

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





}
