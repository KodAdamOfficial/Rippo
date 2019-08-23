package com.kodadam.kacorp.rippo2;

import android.annotation.SuppressLint;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.HashMap;

public class BTCWallet extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseDatabase mFDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    private AdView mAdView;
    TextView txt1;
    EditText edt;
    String uID,putAddress;
    ImageButton imb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btcwallet);
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
        edt = (EditText) findViewById(R.id.edtBTCAddress);
        txt1 = (TextView) findViewById(R.id.txtBTCAmount);
        imb = (ImageButton) findViewById(R.id.imbAddAddress);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    edt.setText(ds.child(uID).child("WalletAddress").getValue().toString());
                    txt1.setText("Your BTC Address: "+ds.child(uID).child("WalletAddress").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap<String,String>();
                putAddress = edt.getText().toString();
                if(edt.getText().toString().length()==34){
                    hashMap.put("WalletAddress",""+putAddress);

                    mDatabase.child("Users").child(uID).updateChildren(hashMap);
                }
                else {
                    Toast.makeText(BTCWallet.this,"Address must be 34 characters..",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
