package com.BagLocator.BagLocatorApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Reglage_Client extends AppCompatActivity {
    EditText latitude1,longitude1,distance;
    Button validerButton;
    ImageView user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglage_client);

        latitude1=findViewById(R.id.setLatitude);
        longitude1=findViewById(R.id.setLongitude);

        distance=findViewById(R.id.setDistance);
        validerButton= findViewById(R.id.validerButton);
        user=findViewById(R.id.user);



        validerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = latitude1.getText().toString();
                String longitude = longitude1.getText().toString();
                String dist = distance.getText().toString();

                    Double convLat= Double.valueOf(latitude);
                    Double convLong= Double.valueOf(longitude);
                    Double distance= Double.valueOf(dist);

                    Intent intent= new Intent(Reglage_Client.this, MAP.class);
                    intent.putExtra("latitude",convLat);
                    intent.putExtra("longitude",convLong);
                    intent.putExtra("distance",distance);
                    startActivity(intent);

            }
        });



    }
}
