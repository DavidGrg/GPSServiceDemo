package com.example.davidg.gpsservicedemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int REQUEST_CODE = 123;

    private Button start, stop;
    private TextView coordinates;

    private BroadcastReceiver broadcastReceiver; //Register them properly to prevent memory leaks.....



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        registerReceiver( broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                coordinates.setText( intent.getStringExtra("Coordinates") );   // the method to be called once the broadcast is received from the service class.
            }
        }, new IntentFilter( "location_updates" ) );



        start = (Button) findViewById( R.id.btnStart );


        stop = (Button) findViewById( R.id.btnStop );


        coordinates = (TextView) findViewById( R.id.tvCoordinates );
        
        if (!runtime_permissions())
            enable_buttons();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver( broadcastReceiver );
        super.onDestroy();
    }

    private void enable_buttons() {
        start.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doSomething();

            }
        } );

        stop.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent in = new Intent( getApplicationContext(), GPSService.class );
                stopService( in );



            }
        });


    }

    private void doSomething() {

        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE );
            //runtime_permissions();
//            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE );
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            Toast.makeText( this, "Request location permission", Toast.LENGTH_SHORT ).show();
            //return;
        } else {
            startMyLocationService();
        }
    }

    private void startMyLocationService() {
        Intent in = new Intent( getApplicationContext(), GPSService.class );
        startService( in );
        System.out.println("Start Pressed");
    }


    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {


            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100 );


            return true;
        }

        return false;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

//        if (requestCode == 100) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//
//                enable_buttons();
//            } else {
//                runtime_permissions();
//            }
//        }


        startMyLocationService();
    }

    @Override
    public void onClick(View v) {


    }
}
