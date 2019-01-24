package com.example.dell.proje;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pano_adi_olustur extends AppCompatActivity {
    Button panoadibtn;
    EditText panoadi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pano_adi_olustur);
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
                Intent intent=new Intent(this,GirisYap.class);
                startActivity(intent);

                break;
        }
        return true;
    }


    public void tanimla(){
        panoadibtn= findViewById(R.id.panoadibtn);
        panoadi=findViewById(R.id.panoadi);

    }
    public void gecisYap(){
        Intent intent=new Intent(this,PanoOlustur.class);
        startActivity(intent);
    }
    public void tiklama(){
        panoadibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gecisYap();
               new Post().execute();
            }
        });
    }
    String url = Config.URL+"panoekle";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;

    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(pano_adi_olustur.this);
            pDialog.setMessage("Yükleniyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();


        }

        protected Void doInBackground(Void... unused) { // Arka Planda yapılacaklar. Yani Post işlemi

            List params = new ArrayList(); //Post edilecek değişkenleri ayarliyoruz.
            //Bu değişkenler bu uygulamada hiçbir işe yaramıyor.Sadece göstermek amaçlı
            // params.add(new BasicNameValuePair("panoID", "3"));
            //params.add(new BasicNameValuePair("kullaniciID", "3"));
            params.add(new BasicNameValuePair("panoadi",panoadi.getText().toString()));
            params.add(new BasicNameValuePair("panoadmin",String.valueOf(GirisYap.sharedPref.getInt("kullanici_id",0))));


            veri_string = post.httpPost(url,"POST",params,50000); //PostClass daki httpPost metodunu çağırdık.Gelen string değerini aldık

            Log.d("HTTP POST CEVAP:",""+veri_string);// gelen veriyi log tuttuk

            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
            String in;
            in = veri_string;
            try {
                JSONObject jsonObje = new JSONObject(veri_string);
                String id  = jsonObje.getString("panoId");
                GirisYap.sharedPref.edit().putInt("panoID",Integer.parseInt(id)).commit();
                System.out.println("Pano Oluşturuldu "+id);
                gecisYap();

            } catch (JSONException e) {
                System.out.println("Giriş Hatası "+e.getMessage());
                Toast.makeText(getApplicationContext(), "Kullanıcı bulunamadı", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

        }
    }

}
