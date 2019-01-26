package com.example.dell.proje;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PanoIcerik extends AppCompatActivity  {

    Button davetgonder,yenigider;

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
                SharedPreferences.Editor editor = GirisYap.sharedPref.edit();
                editor.putBoolean("isSession",false);
                editor.putBoolean("tabloEklendi",false);
                editor.putString("email","");
                editor.putString("sifre","");
                editor.commit();
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
    String panoID;
    public void tanimla() {
        davetgonder = findViewById(R.id.davetgonder);
        yenigider = findViewById(R.id.yenigider);
        panoID = String.valueOf(GirisYap.sharedPref.getInt("panoID", 0));
        Toast.makeText(getApplicationContext(), panoID, Toast.LENGTH_LONG).show();
        new Post().execute();
    }
    public void tiklama() {
        davetgonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PanoIcerik.this,Davet.class));
            }
        });
        yenigider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PanoIcerik.this,PanoOlustur.class));
            }
        });
    }
    String url = Config.URL+"gideroku";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;


    class Post extends AsyncTask<String, String, String> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(PanoIcerik.this);
            pDialog.setMessage("Yükleniyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();


        }

        protected String doInBackground(String... unused) { // Arka Planda yapılacaklar. Yani Post işlemi

            List params = new ArrayList(); //Post edilecek değişkenleri ayarliyoruz.

            params.add(new BasicNameValuePair("panoID", panoID));
            veri_string = post.httpPost(url,"POST",params,20000);
            //PostClass daki httpPost metodunu çağırdık.Gelen string değerini aldık


            return null;
        }

        protected void onPostExecute(String unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
            veri_string = "{data:"+veri_string+"}";
            Log.d("frgd:",""+veri_string);// gelen veriyi log tuttuk
            TextView gidertext=findViewById(R.id.gidertext);
            TextView gidertutar=findViewById(R.id.tutartext);
            gidertutar.setText("Tutar");
            try {
                JSONObject jo = new JSONObject(veri_string);
                JSONArray arr = (JSONArray) jo.get("data");
                TextView tutarTxt = findViewById(R.id.toplamtutar);
                TextView panoadi = findViewById(R.id.panoadi);
                int tutar = 0;
                if (arr.length()>0)
                {
                    for (int i = 0; i <arr.length() ; i++) {
                        JSONObject j = arr.getJSONObject(i);
                        gidertext.setText(gidertext.getText()+"\n"+j.get("gideradi"));
                        gidertutar.setText(gidertutar.getText()+"\n"+j.get("gidertutar")+"₺");
                        tutar +=Integer.parseInt(j.get("gidertutar").toString());
                        panoadi.setText(j.get("panoadi").toString());
                    }


                }
                tutarTxt.setText(String.valueOf(tutar));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
