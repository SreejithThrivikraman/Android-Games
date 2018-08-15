package com.madt.sree.rockpaperscissors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


//import static java.nio.charset.StandardCharsets.UTF_8;

public class waiting_for_host extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{

    public GoogleApiClient mGoogleApiClient;
    private ConnectionsClient connectionsClient;
    private String TAG = "debugging for discovery";

    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    public String client_name = NameGenerator.generate();

    TextView host_name_label;


    /**
     *    These callbacks are made when :
     *    1. an endpoint that we can connect to is found
     *    2. completes a connection attempt
     */
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback =
            new EndpointDiscoveryCallback()
            {
                @Override
                public void onEndpointFound(
                        String endpointId, DiscoveredEndpointInfo dei)
                {

                    Nearby.getConnectionsClient(getApplicationContext()).requestConnection(
                            client_name,
                            endpointId,
                            mConnectionLifecycleCallback


                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                        }
                    }) ;


                    host_name_label.setText(endpointId);

                    connectionsClient.sendPayload(
                            endpointId, Payload.fromBytes(client_name.getBytes()));
                }

                @Override
                public void onEndpointLost(String endpointId)
                {
                    // A previously discovered endpoint has gone away,
                    // perhaps we might want to do some cleanup here
                    Log.i(TAG, endpointId + " endpoint lost");
                }
            };


    public final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback()
            {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo)
                {
                    Log.i(TAG, endpointId + " connection initiated");



                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result)
                {
                    //player_1_label.setText(endpointId.toString());
                }

                @Override
                public void onDisconnected(String endpointId)
                {
                    Log.i(TAG, endpointId + " disconnected");
                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_host);

        host_name_label = findViewById(R.id.host_name);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Nearby.CONNECTIONS_API).build();
        client_name = getApplicationContext().getPackageName();




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

    private void startDiscovery()

    {

        client_name = getApplicationContext().getPackageName();
        Nearby.Connections.startDiscovery(
                mGoogleApiClient,
                client_name,
                mEndpointDiscoveryCallback,
                new DiscoveryOptions(STRATEGY))
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status)
                            {
                                if (status.isSuccess()) {
                                    Log.i(TAG, "Now looking for advertiser");
                                } else {
                                    Log.i(TAG, "Unable to start discovery");
                                }
                            }
                        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
       startDiscovery();

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
