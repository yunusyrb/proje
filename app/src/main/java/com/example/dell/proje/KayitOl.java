package com.example.dell.proje;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class KayitOl extends AppCompatActivity implements View.OnClickListener{

    Button BtnKyt;
    TextView giris;
    EditText kullaniciAdi,email,sifre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kayit_ol);

        tanimla();
        tiklama();
        //BtnKyt.setOnClickListener(KayitOl.this);
    }
    public void tanimla(){
        BtnKyt = findViewById(R.id.btnkayitol);
        giris= findViewById(R.id.giris);
        kullaniciAdi= findViewById(R.id.kullaniciadi);
        email=findViewById(R.id.email);
        sifre=findViewById(R.id.sifre);
    }
    public void gecisYap(){
        //Intent intent=new Intent(this,GirisYap.class);
        //startActivity[intent];
        startActivity(new Intent(this,GirisYap.class));
    }
    public void tiklama(){
        BtnKyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullaniciadi=kullaniciAdi.getText().toString();
                mail = email.getText().toString();
                Sifre = sifre.getText().toString();

                new Post().execute();


            }
        });
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gecisYap();
            }
        });
    }
    String url = Config.URL+"kayit";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;
    String mail,Sifre,kullaniciadi;

    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(KayitOl.this);
            pDialog.setMessage("Yükleniyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();

        }

        protected Void doInBackground(Void... unused) { // Arka Planda yapılacaklar. Yani Post işlemi

            List params = new ArrayList(); //Post edilecek değişkenleri ayarliyoruz.
            //Bu değişkenler bu uygulamada hiçbir işe yaramıyor.Sadece göstermek amaçlı
            params.add(new BasicNameValuePair("adsoyad", kullaniciAdi.getText().toString()));
            params.add(new BasicNameValuePair("email", email.getText().toString()));
            params.add(new BasicNameValuePair("sifre", sifre.getText().toString()));


            veri_string = post.httpPost(url,"POST",params,20000); //PostClass daki httpPost metodunu çağırdık.Gelen string değerini aldık

            Log.d("HTTP POST CEVAP:",""+veri_string);// gelen veriyi log tuttuk

            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
            Toast.makeText(getApplicationContext(),veri_string, Toast.LENGTH_LONG).show(); //Gelen veriyi Toast meaj ile 3 sn boyunca gösterdik
            gecisYap();
        }
    }

    public void onClick(View v){
        new Post().execute();



    }
}
