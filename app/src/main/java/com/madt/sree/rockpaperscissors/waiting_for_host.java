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
import com.google.android.gms.nearby.Nearby;
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

//import static java.nio.charset.StandardCharsets.UTF_8;

public class waiting_for_host extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
      //  Connections.ConnectionRequestListener,
        Connections.MessageListener,
      //  Connections.EndpointDiscoveryListener,
        View.OnClickListener
{







    private ConnectionsClient connectionsClient;
    private String TAG = "debugging for discovery";
    private final String ClientName = NameGenerator.generate();
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    public String host_name;
    public GoogleApiClient mGoogleApiCLsient;

    TextView host_name_label;



    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_host);

        host_name_label = findViewById(R.id.host_name);

        connectionsClient = Nearby.getConnectionsClient(this);
        // method to find host
        // find_host();

        mGoogleApiCLsient = new GoogleApiClient.Builder(this).addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( Nearby.CONNECTIONS_API )
                .build();


    }


    // finding host_device
    public void find_host()
    {
        startDiscovery();
    }




    public void  startDiscovery()
    {
        connectionsClient.startDiscovery(
                getPackageName(), endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY));
    }


    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i(TAG, "onEndpointFound: endpoint found, connecting");
                    connectionsClient.requestConnection(ClientName, endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {}
            };

    // Callbacks for connections to other devices
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i(TAG, "on Host : onConnectionInitiated: accepting connection");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    host_name = connectionInfo.getEndpointName();
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "on Host : onConnectionResult: connection successful");

//                        connectionsClient.stopDiscovery();
//                        connectionsClient.stopAdvertising();

                        host_name_label.setText(endpointId);

                    } else {
                        Log.i(TAG, "onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i(TAG, "onDisconnected: disconnected from the opponent");

                }
            };


    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                   // opponentChoice = GameChoice.valueOf(new String(payload.asBytes(), UTF_8));
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update)

                {

                }
            };
//****************************************************************************************************************


//    @Override
//    public void onEndpointLost(String s)
//    {
//
//    }

    @Override
    public void onClick(View view)
    {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }



    @Override
    public void onMessageReceived(String s, byte[] bytes, boolean b)
    {

    }

    @Override
    public void onDisconnected(String s)
    {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiCLsient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mGoogleApiCLsient != null && mGoogleApiCLsient.isConnected() ) {
            mGoogleApiCLsient.disconnect();
        }
    }




}
