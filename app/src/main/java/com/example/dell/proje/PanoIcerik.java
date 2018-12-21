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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PanoIcerik extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(getApplicationContext(),"Profile Tıklandı.", Toast.LENGTH_LONG).show();
        } else if (id == R.id.hakkimizda) {
            Intent intent = new Intent(getApplicationContext(),Hakkinda.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.right_in,R.anim.left_out);

        } else if (id == R.id.nav_cikis) {
            final AlertDialog.Builder builder=new AlertDialog.Builder(PanoIcerik.this);
            builder.setMessage("Çıkış yapmak istediğinizden emin misiniz?");
            builder.setCancelable(true);
            builder.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    //  mFirebaseAuth.signOut();

                    Intent intent = new Intent(getApplicationContext(),GirisYap.class);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.right_in,R.anim.left_out);

                }
            });

            builder.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Hiçbir şey yapma
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(),Ayarlar.class);
            startActivity(intent);
            // overridePendingTransition(R.anim.right_in,R.anim.left_out);

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    ImageView ayar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pano_icerik);
        tanimla();
        tiklama();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void tanimla() {
        ayar =findViewById(R.id.ayarlar);
    }
    public void gecisYap(){
        Intent intent=new Intent(this,Ayarlar.class);
        startActivity(intent);
    }
    public void tiklama(){
        ayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gecisYap();
            }
        });
    }
    String url = "http://192.168.1.4/wbservis/service/yetkiekle";
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
