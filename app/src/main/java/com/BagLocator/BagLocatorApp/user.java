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

public class user extends AppCompatActivity {
SQLiteDatabase db;
EditText nom,mdp,confirmMdp;
Button update_btn;
    String mdp_intent;
    String mdp_new;

    String nom_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nom=findViewById(R.id.nom);
        mdp=findViewById(R.id.mdp);
        confirmMdp=findViewById(R.id.confirmMdp);
        update_btn=findViewById(R.id.btn);

        db=openOrCreateDatabase("bagTrackDatabse",MODE_PRIVATE, null);
        Intent intent = getIntent();
         mdp_intent=intent.getStringExtra("mdp");
        Cursor user = db.rawQuery("select * from USER where mdp=?",new String[]{mdp_intent});
        user.moveToFirst();
        nom.setText( user.getString(1));


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nom_value = nom.getText().toString();
                String mdp_old = mdp.getText().toString();
                 mdp_new = confirmMdp.getText().toString();

                if(nom_value.isEmpty()){
                    nom.setError("vide");
                }if(mdp_old.isEmpty()){
                    mdp.setError("vide");
                }if(mdp_new.isEmpty()){
                    confirmMdp.setError("vide");}
                if(!(mdp_old.equals(mdp_intent))){
                    mdp.setError("Incorrect");}
                else{
                    db.execSQL("UPDATE USER SET nom=?,mdp=? where mdp=?",new String[]{mdp_new,nom_value,mdp_intent});

                    AlertDialog alertDialog =  new AlertDialog.Builder(user.this).setTitle("Update réussit").setMessage("Allez au login ?").setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(user.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

                }

                }



        });







        /*
        Cursor users = db.rawQuery("select * from USER where mdp=?", new String[]{mdp});
        if(users.getCount()>0){ new AlertDialog.Builder(user.this).setTitle("Client name :"+users.getString(1)).show();
        */
        }


}