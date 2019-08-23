package com.kodadam.kacorp.rippo2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Lottery extends AppCompatActivity {
    private Button btnTicket;
    private EditText edt1,edt2,edt3,edt4;
    private TextView txtBtcPoints,txtTicket1,txtTicket2,txtTicket3;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFDatabase;
    private FirebaseAuth mAuth;
    private String uID,ticket;
    public String t1,t2,t3,loto;
    private float ticketPrice;
    private boolean b = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mFDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        uID = user.getUid();
        btnTicket = (Button) findViewById(R.id.btnTicket);
        edt1 = (EditText) findViewById(R.id.editText);
        edt2 = (EditText) findViewById(R.id.editText2);
        edt3 = (EditText) findViewById(R.id.editText3);
        edt4 = (EditText) findViewById(R.id.editText4);
        txtBtcPoints = (TextView) findViewById(R.id.txtTicketPoints);
        txtTicket1 = (TextView) findViewById(R.id.txtTicket1);
        txtTicket2 = (TextView) findViewById(R.id.txtTicket2);
        txtTicket3 = (TextView) findViewById(R.id.txtTicket3);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    txtBtcPoints.setText("Your BTCPoints :  "+ds.child(uID).child("BTCPoints").getValue().toString());
                    txtTicket1.setText(ds.child(uID).child("Lottery1").getValue().toString());
                    txtTicket2.setText(ds.child(uID).child("Lottery2").getValue().toString());
                    txtTicket3.setText(ds.child(uID).child("Lottery3").getValue().toString());
                    ticketPrice = Float.parseFloat(ds.child(uID).child("BTCPoints").getValue().toString());
                    t1 = ds.child(uID).child("Lottery1").getValue().toString();
                    t2 = ds.child(uID).child("Lottery2").getValue().toString();
                    t3 = ds.child(uID).child("Lottery3").getValue().toString();
                    loto = ds.child("Lottery").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketPrice >= 1000) {
                    if (edt1.getText() != null && edt2.getText() != null && edt3.getText() != null && edt4.getText() != null) {
                        ticket = "" + edt1.getText() + "-" + edt2.getText() + "-" + edt3.getText() + "-" + edt4.getText();
                        HashMap hashMap = new HashMap<String, String>();
                        if (t1.equals("0-0-0-0")){
                            hashMap.put("Lottery1", "" + ticket);
                            ticketPrice = ticketPrice -1000;
                        }
                        else if (t2.equals("0-0-0-0")){
                            hashMap.put("Lottery2", "" + ticket);
                            ticketPrice = ticketPrice -1000;
                        }
                        else if (t3.equals("0-0-0-0")){
                            hashMap.put("Lottery3", "" + ticket);
                            ticketPrice = ticketPrice -1000;
                        }

                        else{
                            Toast.makeText(Lottery.this, "You don't have empty ticket..", Toast.LENGTH_LONG).show();
                            b = false;
                        }
                        if (b){
                            hashMap.put("BTCPoints", "" + ticketPrice);
                            mDatabase.child("Users").child(uID).updateChildren(hashMap);
                            int forLoto = Integer.parseInt(loto) + 1000;

                            HashMap hashMap2 = new HashMap<String, String>();
                            hashMap2.put("Lottery",""+forLoto);
                            mDatabase.child("Users").updateChildren(hashMap2);
                        }

                    } else
                        Toast.makeText(Lottery.this, "Warning ! Not True Lottery Number..", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(Lottery.this, "Warning ! You Don't Have Enough BTCPoints", Toast.LENGTH_LONG).show();
            }
        });

    }
}
