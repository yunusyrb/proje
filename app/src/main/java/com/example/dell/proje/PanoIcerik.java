package com.example.dell.proje;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PanoIcerik extends AppCompatActivity  {

    Button davetgonder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pano_icerik);
        tanimla();
        tiklama();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.profil:
                startActivity(new Intent(this,Ayarlar.class));
                break;
            case R.id.bildirim:
                startActivity(new Intent(this,Ayarlar.class));
                break;
            case R.id.hakkimizda:
                startActivity(new Intent(this,Hakkinda.class));
                break;
            case R.id.cikis:
                cikisYap();
                break;
        }
        return true;
    }

    public void cikisYap(){
        Intent intent=new Intent(this,GirisYap.class);
        startActivity(intent);
        PanoIcerik.this.finish();
    }

    public void tanimla() {
        davetgonder = findViewById(R.id.davetgonder);
    }
    public void tiklama() {
        davetgonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PanoIcerik.this,Davet.class));
            }
        });
    }


    String url = "http://192.168.1.5/wbservis/service/yetkiekle";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;

    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(PanoIcerik.this);
            pDialog.setMessage("Yükleniyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) { // Arka Planda yapılacaklar. Yani Post işlemi

            List params = new ArrayList(); //Post edilecek değişkenleri ayarliyoruz.
            //Bu değişkenler bu uygulamada hiçbir işe yaramıyor.Sadece göstermek amaçlı
            params.add(new BasicNameValuePair("panoID", "3"));
            params.add(new BasicNameValuePair("kullaniciID", "3"));


            veri_string = post.httpPost(url,"POST",params,20000); //PostClass daki httpPost metodunu çağırdık.Gelen string değerini aldık

            Log.d("HTTP POST CEVAP:",""+veri_string);// gelen veriyi log tuttuk

            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
            Toast.makeText(getApplicationContext(),veri_string, Toast.LENGTH_LONG).show(); //Gelen veriyi Toast meaj ile 3 sn boyunca gösterdik
        }
    }
}
