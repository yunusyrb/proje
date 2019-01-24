package com.example.dell.proje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Hakkinda extends AppCompatActivity {
    TextView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hakkinda);
        tanimla();
        tiklama();
    }

    public void tanimla(){
        btn= findViewById(R.id.back_button);
    }
    public void gecisYap(){
        Intent intent=new Intent(this,PanoOlustur.class);
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
