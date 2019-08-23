package com.kodadam.kacorp.rippo2;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    String uID;
    private Button btn;
    private EditText edt1,edt2,edt3;
    String email,sifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        edt1 = (EditText) findViewById(R.id.edtEmail);
        edt2 = (EditText) findViewById(R.id.edtPw);
        edt3 = (EditText) findViewById(R.id.edtName);
        btn = (Button) findViewById(R.id.btnRgstr);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edt1.getText().toString();
                sifre = edt2.getText().toString();
                registering(email,sifre);
            }
        });


    }


    private void registering(String email,String password){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d( "Succes","Registiring is Succesfuly");
                    FirebaseUser user = mAuth.getCurrentUser();
                    uID = user.getUid();
                    addDB(uID);
                    Intent myIntent = new Intent(Register.this,anasayfa.class);
                    startActivity(myIntent);
                }
                else{
                    Toast.makeText(Register.this,"Wrong or Already exist..",Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    private void addDB(String uID){
        String name;

        name = edt3.getText().toString();
        DatabaseReference mdbRef = db.getReference("Users");
        //String key = mdbRef.push().getKey();
        HashMap hashMap = new HashMap<String,String>();
        hashMap.put("UserName",name);
        hashMap.put("E-Mail",edt1.getText().toString());
        hashMap.put("BTCAmount","0");
        hashMap.put("WalletAddress","Write Your BTC Address");
        hashMap.put("BTCPoints","0");
        hashMap.put("Lottery1","0-0-0-0");
        hashMap.put("Lottery2","0-0-0-0");
        hashMap.put("Lottery3","0-0-0-0");
        hashMap.put("DailyTime","0 0");
        hashMap.put("Payment","0");
        hashMap.put("withdrawAmount","0.00");
        mdbRef.child(uID).updateChildren(hashMap);

    }

}
