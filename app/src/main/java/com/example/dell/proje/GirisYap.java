package com.example.dell.proje;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GirisYap extends AppCompatActivity {
    Button btn;
    EditText email,sifre;
    TextView kayıtolbuton;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris_yap);
        tanimla();
        tiklama();
    }

    public void tanimla(){
        btn= findViewById(R.id.girisbuton);
        email=findViewById(R.id.email);
        sifre=findViewById(R.id.sifre);
        kayıtolbuton=findViewById(R.id.sifremiunuttum);
    }
    public void gecisYap(){

        Intent intent=new Intent(this,PanoOlustur.class);
        startActivity(intent);
    }
    public void gecisYap2(){
        Intent intent=new Intent(this,KayitOl.class);
        startActivity(intent);
    }
    public void tiklama(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gecisYap();

                mail = email.getText().toString();
               Sifre = sifre.getText().toString();

                /*Calendar c = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                tarih = format.format(c.getTime());*/

                if(mail.matches("")){
                    hata_mesaji += "Üye No yada E-Mail Alanı Boş Olamaz\n";
                    hata = true;
                }
                /*if(!Fonksiyonlar.isEmailValid(mail)){//Mail format kontrol
                    hata_mesaji += "Yanlış e-mail formatı\n";
                    hata=true;
                }*/

                int sifre_karakter = sifre.length();
                if(sifre_karakter<6 || sifre_karakter==0){
                    hata_mesaji += "Şifre 6 Karakterden Az Olamaz\n";
                    hata=true;
                }

                if(hata){//hata varsa AlertDialog ile kullanıcıyı uyarıyoruz.
                    AlertDialog alertDialog = new AlertDialog.Builder(GirisYap.this).create();
                    alertDialog.setTitle("Hata");
                    alertDialog.setMessage(hata_mesaji);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(RESULT_OK,"Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           sifre.setText("");
                            hata_mesaji = "";
                            hata = false;
                        }
                    });
                    alertDialog.show();
                }else{//Hata yoksa Asynctask classı çağırıyoruz.İşlemlere orda devam ediyoruz

                    new Post().execute();

                }
            }
        });
        kayıtolbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gecisYap2();
            }
        });
    }
    String url = "http://192.168.1.4/wbservis/service/giris";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;
    String mail,Sifre,sonuc,tarih;
    String hata_mesaji="";
    Boolean hata = false;
    JSONObject cevap=null;
    public String donus_hata_mesaji;

    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(GirisYap.this);
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
            params.add(new BasicNameValuePair("email", email.getText().toString()));
            params.add(new BasicNameValuePair("sifre", sifre.getText().toString()));


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
                String id  = jsonObje.getString("id");
                int realId;
                realId = Integer.parseInt(id);
                if(realId >= 0){
                    //session'a atılmalı
                    gecisYap();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Kullanıcı bulunamadı", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

        }
    }

    public void onClick(View v){
        new GirisYap.Post().execute();



    }
}
