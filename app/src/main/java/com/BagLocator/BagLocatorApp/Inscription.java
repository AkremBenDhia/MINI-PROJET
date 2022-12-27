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

public class Inscription extends AppCompatActivity {
EditText nom,mdp,confirmMdp;
Button inscrire_btn;
SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        db=openOrCreateDatabase("bagTrackDatabse",MODE_PRIVATE, null);

        nom=findViewById(R.id.nom);
        mdp=findViewById(R.id.mdp);
        confirmMdp=findViewById(R.id.confirmMdp);
        inscrire_btn=findViewById(R.id.btn);

        inscrire_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String nom_value = nom.getText().toString();
                String mdp_value = mdp.getText().toString();
                String confirm_mdp_value = confirmMdp.getText().toString();

                if(nom_value.isEmpty()){
                    nom.setError("vide");
                }if(mdp_value.isEmpty()){
                    mdp.setError("vide");
                }if(confirm_mdp_value.isEmpty()){
                    confirmMdp.setError("vide");
                }if(!(confirm_mdp_value.equals(mdp_value)))
                {confirmMdp.setError("erreur");}else
                {
                Cursor users = db.rawQuery("select * from USER where nom=? and mdp=?", new String[]{nom_value,mdp_value});
                if(users.getCount()>0){
                    AlertDialog alertDialog =  new AlertDialog.Builder(Inscription.this).setTitle("Client existe").show();
                }else{
                    db.execSQL("insert into USER(nom,mdp) values (?,?)", new String[]{nom_value,mdp_value});
                    AlertDialog alertDialog =  new AlertDialog.Builder(Inscription.this).setTitle("Inscription réussite").setMessage("Allez au login ?").setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Inscription.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }
                }

            }
        });



    }
}