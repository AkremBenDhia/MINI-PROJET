package com.BagLocator.BagLocatorApp;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MAP extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap gMap;
    FrameLayout map ;
    EditText latitude, longitude, distance,Etat;
    Button updateButton;
    Double firebaseLatitude;
    Double firebaseLongitude;
    int firebaseLDR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification","My notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        map = findViewById(R.id.map);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        updateButton=findViewById(R.id.updateButton);
        distance=findViewById(R.id.Distance);
        distance=findViewById(R.id.Distance);
        Etat=findViewById(R.id.Etat);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public static double distance(double lat1,double lat2, double lon1, double lon2) {
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;
        return (c * r);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("GPS").child("f_latitude");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firebaseLatitude = (Double) dataSnapshot.getValue();
               String f = String.valueOf(firebaseLatitude);

                latitude.setText(f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("GPS").child("f_longitude");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firebaseLongitude = (Double) dataSnapshot.getValue();
                String longit=String.valueOf(firebaseLongitude);
                longitude.setText(longit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference reference5= FirebaseDatabase.getInstance().getReference().child("LUMIERE").child("VALLUMIERE");
        reference5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object val =dataSnapshot.getValue();

                Etat.setText(val.toString());
               String val2= val.toString();
                firebaseLDR = Integer.valueOf(val2);
                if(firebaseLDR>10){

                    Etat.setText("SAC OUVERT");
                    Etat.setTextColor(getResources().getColor(R.color.white));
                    Etat.setBackgroundColor(getResources().getColor(R.color.red));




                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MAP.this,"My Notification");

                    builder.setContentTitle("Alerte d'ouverture " );
                    builder.setContentText("SAC OUVERT !");

                    builder.setSmallIcon(R.drawable.open);
                    builder.setAutoCancel(true);


                    Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    builder.setSound(path);

                    NotificationManagerCompat managerCompat= NotificationManagerCompat.from(MAP.this);
                    managerCompat.notify(1,builder.build() );

                }else {
                    Etat.setText("SAC FERMER");
                    Etat.setTextColor(getResources().getColor(R.color.white));
                    Etat.setBackgroundColor(getResources().getColor(R.color.black));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = latitude.getText().toString();
                int lo=lat.length();
                String res = String.valueOf(lo);


                Intent intent = getIntent();
               Double latitudeChoix= intent.getDoubleExtra("latitude",0);
                Double longitudeChoix= intent.getDoubleExtra("longitude",0);
                Double distanceChoix= intent.getDoubleExtra("distance",0);
                String convDistance=String.valueOf(distanceChoix);

                gMap = googleMap;
                LatLng mapTunisia= new LatLng(firebaseLatitude, firebaseLongitude);
                gMap.addMarker(new MarkerOptions().position(mapTunisia).title("Mon sac"));
                gMap.moveCamera(CameraUpdateFactory.newLatLng(mapTunisia));


                double x = distance(firebaseLatitude,latitudeChoix,firebaseLongitude,longitudeChoix);

                double convertedDistance=Math.floor(x * 100) / 100;
                String destString = String.valueOf(convertedDistance);
                distance.setText(destString+" km");

                if(distanceChoix<x){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MAP.this,"My Notification");

                    builder.setContentTitle("ALERTE DISTANCE " );
                    builder.setContentText("Distance dépasse : "+convDistance+" km");

                    builder.setSmallIcon(R.drawable.distanc_icon);
                    builder.setAutoCancel(true);


                    Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(path);

                    NotificationManagerCompat managerCompat= NotificationManagerCompat.from(MAP.this);
                    managerCompat.notify(1,builder.build() );
                }




            }
        });




    }
