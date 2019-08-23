package com.kodadam.kacorp.rippo2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class anasayfa extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDL;
    private ActionBarDrawerToggle mABDT;
    public static final String BPI_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ProgressDialog progressDialog;
    private TextView txt;
    private ImageButton btn;
    Date timeDate;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        mDL = (DrawerLayout) findViewById(R.id.drawer);
        mABDT = new ActionBarDrawerToggle(this,mDL,R.string.Open,R.string.Close);
        mDL.addDrawerListener(mABDT);
        mABDT.syncState();
        txt = (TextView) findViewById(R.id.txtBTC);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("BPI Loading");
        progressDialog.setMessage("Wait ...");
        load();
        NavigationView navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.wallet:
                Intent myIntent = new Intent(anasayfa.this,BTCWallet.class);
                startActivity(myIntent);
                break;

            case R.id.dailygift:
                Intent myIntent4 = new Intent(anasayfa.this,DailyGift.class);
                startActivity(myIntent4);
                break;
            case R.id.wtchads:
                Intent myIntent5 = new Intent(anasayfa.this,EarnBTCPoints.class);
                startActivity(myIntent5);
                break;
            case R.id.lottery:
                Intent myIntent6 = new Intent(anasayfa.this,Lottery.class);
                startActivity(myIntent6);
                break;
            case R.id.withdraw:
                Intent myIntent7 = new Intent(anasayfa.this,Withdraw.class);
                startActivity(myIntent7);
                break;
            case R.id.info:
                Intent myIntent8 = new Intent(anasayfa.this,InfoActivity.class);
                startActivity(myIntent8);
        }

        return true;
    }

    private void load() {
        Request request = new Request.Builder()
                .url(BPI_ENDPOINT)
                .build();

        progressDialog.show();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(anasayfa.this, "Error during BPI loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
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
            builder.append(timeObject.getString("updated")).append("\n\n");

            JSONObject bpiObject = jsonObject.getJSONObject("bpi");
            JSONObject usdObject = bpiObject.getJSONObject("USD");
            builder.append("$  ").append(usdObject.getString("rate")).append("      USD").append("\n");

            JSONObject gbpObject = bpiObject.getJSONObject("GBP");
            builder.append("£  ").append(gbpObject.getString("rate")).append("      GBP").append("\n");

            JSONObject euroObject = bpiObject.getJSONObject("EUR");
            builder.append("€  ").append(euroObject.getString("rate")).append("      EUR ").append("\n");

            txt.setText(builder.toString());

        } catch (Exception e) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }



}
