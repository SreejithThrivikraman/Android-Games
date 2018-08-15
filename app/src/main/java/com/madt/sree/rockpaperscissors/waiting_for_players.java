package com.madt.sree.rockpaperscissors;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import static java.nio.charset.StandardCharsets.UTF_8;


public class waiting_for_players extends AppCompatActivity {

    private ConnectionsClient connectionClient;

    // Code to generate and HostName name.
    // HostName name : To be displayed on opponent's device.
    private final String HostName = NameGenerator.generate();

    // connection strategy.
    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    // Tag for connection debugging.
    private String TAG = "Connection_Debugger >>>>>>>> ";


    private String opponentEndpointId;
    private String opponentName;
    private int opponentScore;

    TextView player_1_label;
    TextView player_2_label;



    // callback connection class object.
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i(TAG, "on waiting_for_players :  onConnectionInitiated: accepting connection");
                    connectionClient.acceptConnection(endpointId, payloadCallback);
                    opponentName = connectionInfo.getEndpointName();
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "on waiting_for_players : onConnectionResult: connection successful");

                      //  connectionClient.stopDiscovery();
                       // connectionClient.stopAdvertising();

                        opponentEndpointId = endpointId;
                        player_1_label.setText(opponentEndpointId);
                        player_2_label.setText(opponentEndpointId);


                    } else {
                        Log.i(TAG, "onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(String endpointId)
                {
                    Log.i(TAG, "onDisconnected: disconnected from the opponent");

                }
            };


    // Callbacks for receiving payloads
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {

                  //  receiving the name of the opponent.

                 //   opponentName = GameChoice.valueOf(new String(payload.asBytes(), UTF_8));
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update)
                {
                    // Called with progress information about an active Payload transfer, either incoming or outgoing.

                    // This method is not required as no bulk payload is transfered.
                }
            };










    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_players);
        connectionClient = Nearby.getConnectionsClient(this);

        player_1_label = findViewById(R.id.player_1);
        player_2_label = findViewById(R.id.player_2);

        // call for findPlayer() method
        findPlayer();
    }



    // code to search for new opponents
    public void findPlayer()
    {
        startAdvertising();


    }


    // Broadcasts the device presence using Nearby Connections so other players can find the host.
    private void startAdvertising()

    {

        connectionClient.startAdvertising(
                HostName, getPackageName(), connectionLifecycleCallback, new AdvertisingOptions(STRATEGY));
    }


    // Callback call for connections to other devices



}
