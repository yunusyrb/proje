package com.example.dell.proje;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Ayarlar extends AppCompatActivity {

    Button btn;
    EditText ad,mail,sifre;
    private final String url = Config.URL+"update";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;
    String strmail,strsifre,strkullanici;
    String hata_mesaji="";
    Boolean hata = false;
    JSONObject cevap=null;
    public String donus_hata_mesaji;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);
        tanimla();
        tiklama();
    }
    public void tanimla(){

        btn= findViewById(R.id.button_guncelle);
        ad=findViewById(R.id.edit_kullanici_adi);
        mail=findViewById(R.id.edit_kullanici_mail);
        sifre=findViewById(R.id.edit_kullanici_sifre);

        ad.setText(GirisYap.sharedPref.getString("ad",""));
        sifre.setText(GirisYap.sharedPref.getString("sifre",""));
        mail.setText(GirisYap.sharedPref.getString("email", ""));
    }
    public void gecisYap(){
        Intent intent=new Intent(this,PanoIcerik.class);
        startActivity(intent);
    }
    public void tiklama(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strkullanici=ad.getText().toString();
                strmail=mail.getText().toString();
                strsifre=sifre.getText().toString();
                new Post().execute();
               //gecisYap();
            }
        });
    }

    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(Ayarlar.this);
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
            SharedPreferences sharedPreferences = GirisYap.sharedPref;
            int kullanici_id=sharedPreferences.getInt("kullanici_id",0);
            params.add(new BasicNameValuePair("id", String.valueOf(kullanici_id)));
            params.add(new BasicNameValuePair("ad", strkullanici));
            params.add(new BasicNameValuePair("email", strmail));
            params.add(new BasicNameValuePair("sifre", strsifre));


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
                Boolean status  = jsonObje.getBoolean("status");

                if(status){
                    SharedPreferences.Editor editor = GirisYap.sharedPref.edit();
                    editor.putString("ad",ad.getText().toString());
                    editor.putString("email",strmail);
                    editor.putString("sifre",strsifre);
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Kullanıcı Güncellendi", Toast.LENGTH_LONG).show();
                    //gecisYap();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Kullanıcı Güncellenemedi", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Kullanıcı bulunamadı", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

        }
    }
}
