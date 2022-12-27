package com.BagLocator.BagLocatorApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
Button boutonStart;
TextView inscrire;
EditText mdp;
SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=openOrCreateDatabase("bagTrackDatabse",MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS USER (id integer primary key autoincrement, nom varchar(50), mdp varchar(50))");

        inscrire=findViewById(R.id.inscrirewORD);
        mdp=findViewById(R.id.mdp);

        inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Inscription.class);
                startActivity(intent);

            }
        });
        boutonStart= findViewById(R.id.bouton);
        boutonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mdpValue = mdp.getText().toString();

                if (mdpValue.isEmpty()) {
                    mdp.setError("vide");
                }
                else{
                    Cursor users = db.rawQuery("select * from USER where mdp=?", new String[]{mdpValue});
                    if (users.getCount() == 0) {
                        new AlertDialog.Builder(MainActivity.this).setTitle("Utilisateur inexistant").setMessage("Créer un compte ?").setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MainActivity.this, Inscription.class);
                            startActivity(intent);
                        }
                        }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                        }).show();
                    }
                    if (users.getCount() > 0) {
                    Intent intent = new Intent(MainActivity.this, Reglage_Client.class);
                    intent.putExtra("mdp",mdpValue);
                    startActivity(intent);
                }
            }
            }
        });


    }
}