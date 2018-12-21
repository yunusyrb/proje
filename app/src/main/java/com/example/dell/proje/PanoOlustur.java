package com.example.dell.proje;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PanoOlustur extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
            final AlertDialog.Builder builder=new AlertDialog.Builder(PanoOlustur.this);
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
    Button btn;
    ImageView giderEkleBtn;
    String gideradi;
    String gidertutar;
    EditText giderAdiText, giderTutarText;
    Integer a;
    Integer uniq;

    Context cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pano_olustur);
        cnt = this;
        a = 0;
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

    public void tanimla(){
        btn= findViewById(R.id.olustur);
        giderEkleBtn= findViewById(R.id.gidereklebuton);
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

        giderEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giderAdiText = findViewById(R.id.gideradi);
                giderTutarText = findViewById(R.id.gidertutar);
                gideradi = giderAdiText.getText().toString();
                gidertutar = giderTutarText.getText().toString();
                url = "http://192.168.1.4/wbservis/service/giderekle";
                eklemeMi = true;
                new Post().execute();
            }
        });
    }
    String url = "http://192.168.1.4/wbservis/service/giderekle";
    String veri_string;
    PostClass post = new PostClass();
    ProgressDialog pDialog;
    Boolean eklemeMi = true;
    List postparams = new ArrayList(); //Post edilecek değişkenleri ayarliyoruz.

    class Post extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { // Post tan önce yapılacak işlemler. Yükleniyor yazısını(ProgressDialog) gösterdik.
            pDialog = new ProgressDialog(PanoOlustur.this);
            pDialog.setMessage("Yükleniyor...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false); // ProgressDialog u iptal edilemez hale getirdik.
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) { // Arka Planda yapılacaklar. Yani Post işlemi

            if(eklemeMi == true){
                a++;
                postparams.clear();
                postparams.add(new BasicNameValuePair("gideradi", gideradi));
                postparams.add(new BasicNameValuePair("gidertutar", gidertutar));
                postparams.add(new BasicNameValuePair("gideradmin", "10"));
                postparams.add(new BasicNameValuePair("uniq", ""+a));
            }else{
                postparams.clear();
                postparams.add(new BasicNameValuePair("uniq", ""+uniq));
                postparams.add(new BasicNameValuePair("userid", "10"));
            }

            //Bu değişkenler bu uygulamada hiçbir işe yaramıyor.Sadece göstermek amaçlı



            veri_string = post.httpPost(url,"POST",postparams,20000); //PostClass daki httpPost metodunu çağırdık.Gelen string değerini aldık

            Log.d("HTTP POST CEVAP:",""+veri_string);// gelen veriyi log tuttuk

            return null;
        }

        protected void onPostExecute(Void unused) { //Posttan sonra
            pDialog.dismiss();  //ProgresDialog u kapatıyoruz.
            final LinearLayout lm = (LinearLayout) findViewById(R.id.gideralan);

            // layout parametresi oluşturma
            // aşağıda button için kullanılacak
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            //for ile döngü halinde eklenecek
                // Create LinearLayout
                if( eklemeMi == true){
                    final LinearLayout ll = new LinearLayout(cnt);
                    ll.setOrientation(LinearLayout.HORIZONTAL);

                    // TextView oluşturma
                    TextView product = new TextView(cnt);
                    product.setText(gideradi + "    ");
                    product.setId(a);
                    ll.addView(product);

                    // TextView oluşturma
                    TextView price = new TextView(cnt);
                    price.setText(gidertutar + "  TL " + "     ");
                    price.setId(a);
                    ll.addView(price);

                    // Button oluşturma
                    final Button btn = new Button(cnt);
                    // Button a ID verme
                    //btn.setId("silmebtn"+a);
                    btn.setText("Sil");
                    // Button layout parametreleri ayarlama
                    btn.setLayoutParams(params);

                    final int index = a;
                    // Button için listener oluşturma
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            eklemeMi = false;
                            url = "http://192.168.1.4/wbservis/service/gidersil";
                            uniq = index;
                            //ll.removeView(v);
                            ll.removeAllViews();
                            //ll.removeViewAt();
                            //ll.removeViewAt(index+15);
                            new Post().execute();
                            Toast.makeText(getApplicationContext(), "Tıklanan Button Index :" + index, Toast.LENGTH_LONG).show();
                        }
                    });

                    // LinearLayout a butonu ekleme
                    ll.addView(btn);
                    //Ana LinearLayout oluşturulan view ın eklenmesi
                    lm.addView(ll);
                }
            Toast.makeText(getApplicationContext(),veri_string, Toast.LENGTH_LONG).show(); //Gelen veriyi Toast meaj ile 3 sn boyunca gösterdik
        }
    }
}
