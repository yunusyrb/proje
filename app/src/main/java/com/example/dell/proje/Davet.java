package com.example.dell.proje;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Davet  extends AppCompatActivity {

    String url = "http://192.168.1.5/wbservis/service/davetet";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;
    String hata_mesaji="", davetedilenuye = "";
    Boolean hata = false;
    JSONObject cevap=null;
    Button davet;
    EditText kullaniciadi;
    public String donus_hata_mesaji;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.davet);
        davet = (Button) findViewById(R.id.davet);
        kullaniciadi = (EditText) findViewById(R.id.davetedilen);
        davet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                davetedilenuye = kullaniciadi.getText().toString();
                new Davet.Post().execute();
            }
        });
    }
    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(Davet.this);
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
            params.add(new BasicNameValuePair("session", "14")); // preferences.getString("kullanici", "null");
            params.add(new BasicNameValuePair("panoid", "3")); // preferences.getInt("panoid", -1);
            params.add(new BasicNameValuePair("kullaniciadi", davetedilenuye));


            veri_string = post.httpPost(url,"POST",params,20000); //PostClass daki httpPost metodunu çağırdık.Gelen string değerini aldık

            Log.d("HTTP POST CEVAP:",""+veri_string);// gelen veriyi log tuttuk

            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
            String in;
            in = veri_string;
            try {
                JSONObject jsonObje = new JSONObject(in);
                if(jsonObje.getString("sonuc").equals("1"))
                    Toast.makeText(Davet.this,"Davet gönderildi.", Toast.LENGTH_LONG).show();
                else if(jsonObje.getString("sonuc").equals("2"))
                    Toast.makeText(Davet.this,"Böyle bir kullanıcı bulunmadı.", Toast.LENGTH_LONG).show();
                else if(jsonObje.getString("sonuc").equals("3"))
                    Toast.makeText(Davet.this,"Bu kişiye daha önce davet gönderilmiş.", Toast.LENGTH_LONG).show();
                else if(jsonObje.getString("sonuc").equals("4"))
                    Toast.makeText(Davet.this,"Bu kişi zaten senin panonda.", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

        }
    }
}
