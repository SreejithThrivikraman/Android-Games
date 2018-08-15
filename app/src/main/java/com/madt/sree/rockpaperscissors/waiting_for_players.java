package com.madt.sree.rockpaperscissors;

import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
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
import com.google.android.gms.nearby.connection.ConnectionsClient;




public class waiting_for_players extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{

    private ConnectionsClient connectionClient;
    public GoogleApiClient mGoogleApiClient;


    // Code to generate and HostName name.
    // HostName name : To be displayed on opponent's device.
    private String HostName = NameGenerator.generate();

    // connection strategy.
    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    // Tag for connection debugging.
    private String TAG = "Connection_Debugger >>>>>>>> ";



    /**
     *    service id. discoverer and advertiser can use this id to
     *    verify each other before connecting
     */

     public static String SERVICE_ID;




    TextView player_1_label;
    TextView player_2_label;





    /**
     *    These callbacks are made when other devices:
     *    1. tries to initiate a connection
     *    2. completes a connection attempt
     *    3. disconnects from the connection
     */
     public final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback()
            {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo)
                {
                    Log.i(TAG, endpointId + " connection initiated");

                   Nearby.getConnectionsClient(getApplicationContext()).acceptConnection(endpointId,mPayloadCallback);



                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result)
                {
                    player_1_label.setText(endpointId.toString());
                }

                @Override
                public void onDisconnected(String endpointId)
                {
                    Log.i(TAG, endpointId + " disconnected");
                }
            };


     // payload : receives data from the client

    private final PayloadCallback mPayloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload)
                {
                  //  opponentChoice = GameChoice.valueOf(new String(payload.asBytes(), UTF_8));
                    player_1_label.setText(endpointId.toString());
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update)
                {

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
        // findPlayer();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Nearby.CONNECTIONS_API).build();

        SERVICE_ID = getApplicationContext().getPackageName();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // Connect to the GoogleApiClient if disconnected
        if (!mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        SERVICE_ID = getApplicationContext().getPackageName();
        startAdvertising();


    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
            Log.e(">> Connection Error <<:","API Connection failed");
    }


/**
 *    Set device to advertising mode by broadcasting to other
 *    devices that are currently in discovery mode.
 */
    private void startAdvertising()
    {
            SERVICE_ID = getApplicationContext().getPackageName();
            Nearby.Connections.startAdvertising(
                    mGoogleApiClient,HostName,
                    SERVICE_ID,
                    mConnectionLifecycleCallback,
                    new AdvertisingOptions(STRATEGY))
                    .setResultCallback(
                    new ResultCallback<Connections.StartAdvertisingResult>()
                    {
                         @Override
                         public void onResult(@NonNull Connections.StartAdvertisingResult result)
                            {
                                if (result.getStatus().isSuccess())
                                {
                                    Log.i(TAG, "Advertising endpoint");
                                }
                                else
                                    {
                                            Log.i(TAG, "unable to start advertising");
                                    }
                            }
                    });
    }




}
