package com.example.dell.proje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Ayarlar extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);
        tanimla();
        tiklama();
    }


    public void tanimla(){
        btn= findViewById(R.id.button_guncelle);
    }
    public void gecisYap(){
        Intent intent=new Intent(this,PanoIcerik.class);
        startActivity(intent);
    }
    public void tiklama(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gecisYap();
            }
        });
    }
}
