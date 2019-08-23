package com.kodadam.kacorp.rippo2;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Withdraw extends AppCompatActivity {
    private Button btnPayment;
    private Button btnTrade;
    private TextView txtWBTCPoints,txtWBTCAmount,txtWAddress;
    private EditText edtAmount;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFDatabase;
    private FirebaseAuth mAuth;
    private String uID;
    private float hasAmount,withAmount;
    String po,bt;
    float BTCnow;
    private float hasPoint,lastPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mFDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        uID = user.getUid();
        txtWAddress = (TextView) findViewById(R.id.txtWAddress);
        txtWBTCAmount = (TextView) findViewById(R.id.txtWBTCAmount);
        txtWBTCPoints = (TextView) findViewById(R.id.txtWBTCPoints);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        btnTrade = (Button) findViewById(R.id.tradeBtn);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    po = ds.child(uID).child("BTCPoints").getValue().toString();
                    bt = ds.child(uID).child("BTCAmount").getValue().toString();
                    txtWBTCPoints.setText(""+ds.child(uID).child("BTCPoints").getValue().toString());
                    txtWBTCAmount.setText(""+ basamak(Float.parseFloat(ds.child(uID).child("BTCAmount").getValue().toString()))+" BTC");
                    BTCnow = Float.parseFloat(ds.child("BTC").getValue().toString());
                    txtWAddress.setText(""+ds.child(uID).child("WalletAddress").getValue().toString());
                    hasAmount = Float.parseFloat(ds.child(uID).child("BTCAmount").getValue().toString());
                    withAmount = Float.parseFloat(ds.child(uID).child("withdrawAmount").getValue().toString());
                    hasPoint = Float.parseFloat(ds.child(uID).child("BTCPoints").getValue().toString());
                    BTCnow = Float.parseFloat(ds.child("BTC").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtAmount.getText().toString().trim().length() != 0) {
                    if (Float.parseFloat(edtAmount.getText().toString()) <= hasAmount && hasAmount >= 0.002) {
                        float lastAmount;
                        lastAmount = hasAmount - Float.parseFloat(edtAmount.getText().toString());
                        withAmount += Float.parseFloat(edtAmount.getText().toString());
                        HashMap hashMap = new HashMap<String, String>();
                        hashMap.put("withdrawAmount", "" + withAmount);
                        hashMap.put("BTCAmount", "" + basamak(lastAmount));
                        hashMap.put("Payment", "1");
                        mDatabase.child("Users").child(uID).updateChildren(hashMap);
                        Toast.makeText(Withdraw.this, "Payment Request is Successful", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(Withdraw.this,"Payment Error , Please Try Again or Wait..",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Withdraw.this,"Payment Error , Please Try Again or Wait..",Toast.LENGTH_LONG).show();

                }
            }
        });
        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPoint>=100) {
                    lastPoint = hasPoint - hasPoint % 100;
                    float lp = hasPoint%100;
                    HashMap hashMap = new HashMap<String, String>();
                    float amount;
                    amount = BTCnow * (lastPoint / 100);
                    hashMap.put("BTCAmount", "" + (basamak((hasAmount+amount))));
                    hashMap.put("BTCPoints", "" + lp);
                    mDatabase.child("Users").child(uID).updateChildren(hashMap);
                }
                else
                    Toast.makeText(Withdraw.this,"Trade Error , You Don't Have Enough BTCPoints",Toast.LENGTH_LONG).show();
            }
        });
    }
    @SuppressLint("DefaultLocale")
    private String basamak(float sayi){
        String a;
        a = String.format("%7f",sayi);
        return a.replace(',','.');

    }
}
