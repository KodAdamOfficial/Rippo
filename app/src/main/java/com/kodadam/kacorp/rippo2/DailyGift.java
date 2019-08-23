package com.kodadam.kacorp.rippo2;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Thread.sleep;

public class DailyGift extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseDatabase mFDatabase;
    private FirebaseAuth mAuth;
    int [ ] jack1 = {0,0,0,1,0,0,0,2,0,0,1,0,4,0,3,0,0,1,2,0,0,0,0,0,1,0,0,0,0,1,0,4,0,2,0,5,0,0,0,1,2,0,0,9,0,0,2,0,0,1,0,4,0,0,0,3,0,0,0,0,1,0,0,2,0,0,6,0,0,2,0,0,0,0,3,0,0,0,2,0,0,7,0,0,1,0,0,0,4,0,8,0,1,0,0,0,0,0,0,3};
    int [ ] jackEveryOne = {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9};
    int [ ]  myBanner = {0,1,2,3,4,5,6,7,8,9};
    private TextView txt1,txt2;
    private Button btn;
    private String uID;
    private String btcPointsTemp = "0";
    private String tut1,tut2,tut3,tut4;
    Random r = new Random();
    String btcPoints = "";
    public static final String BPI_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private OkHttpClient okHttpClient = new OkHttpClient();
    String newTime,pastTime;
    Calendar timeDate = Calendar.getInstance();
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_gift);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-5909073519643764/3671650268");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {

            }
        });
        mAuth = FirebaseAuth.getInstance();
        mFDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        uID = user.getUid();
        final int one,two,th,four;
        one = r.nextInt(99);
        two = r.nextInt(99);
        th = r.nextInt(99);
        four = r.nextInt(99);
        txt1 = (TextView) findViewById(R.id.txtEarn);
        txt2 = (TextView) findViewById(R.id.txtEarn2);
        txt1.setVisibility(txt1.INVISIBLE);
        txt2.setVisibility(txt2.INVISIBLE);
        btn = (Button) findViewById(R.id.btnBTCpoints);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){


                    btcPointsTemp = ds.child(uID).child("BTCPoints").getValue().toString();
                    pastTime = ds.child(uID).child("DailyTime").getValue().toString();
                    newTime = ds.child(uID).child("DailyTime").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        for (int i=0;i<5;i++)
        {load();}
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                String pT = null,lT = null;
                String pTFormat = "", lTFormat = "";
                int sayac =0;
                for (String retval: newTime.split(" ")){
                    if(sayac == 1){
                        lT = retval;
                        lTFormat = lT.replace("," ,"");
                    }
                    sayac++;

                }
                sayac =0;
                for (String retval: pastTime.split(" ")){
                    if(sayac == 1){
                        pT = retval;
                        pTFormat = pT.replace(",","");
                    }
                    sayac++;
                }
                int dayDif = Integer.parseInt(lTFormat)-Integer.parseInt(pTFormat);
                if(dayDif >=1 || dayDif<0){
                    tut1 = ""+jack1[one];
                    tut2 = ""+jackEveryOne[two];
                    tut3 = ""+jackEveryOne[th];
                    tut4 = ""+jackEveryOne[four];
                    btcPoints = tut1+tut2+tut3+tut4;
                    final String finalBtcPoints = btcPoints;
                    txt1.setVisibility(txt1.VISIBLE);
                    txt2.setVisibility(txt1.VISIBLE);
                    txt2.setText(""+ finalBtcPoints +" BTC Points");


                    float tempp =Float.parseFloat(btcPointsTemp) + Float.parseFloat(btcPoints);

                    btcPointsTemp = String.valueOf(tempp);
                    HashMap hashMap = new HashMap<String,String>();
                    hashMap.put("DailyTime",""+newTime);
                    hashMap.put("BTCPoints",""+btcPointsTemp);
                    mDatabase.child("Users").child(uID).updateChildren(hashMap);
                }
                else
                    Toast.makeText(DailyGift.this,"Please Wait For Daily Gift",Toast.LENGTH_LONG).show();

            }
        });


    }
    private void load() {
        Request request = new Request.Builder()
                .url(BPI_ENDPOINT)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DailyGift.this, "Error during BPI loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        parseBpiResponse(body);
                    }
                });
            }
        });
    }
    private void parseBpiResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();

            JSONObject jsonObject = new JSONObject(body);
            JSONObject timeObject = jsonObject.getJSONObject("time");
            builder.append(timeObject.getString("updated"));
            newTime = ""+builder.toString();
        } catch (Exception e) {

        }
    }


}
