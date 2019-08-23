package com.kodadam.kacorp.rippo2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Giris extends AppCompatActivity {

    private Button btnKyt,btnGiris,btnSifre;
    private EditText editText1;
    private EditText editText2;
    private FirebaseAuth mAuth;
    private ProgressDialog pD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        pD = new ProgressDialog(this);
        editText1 = (EditText) findViewById(R.id.edtGirisEmail);
        editText2 = (EditText) findViewById(R.id.edtGirisSifre);
        mAuth = FirebaseAuth.getInstance();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        btnKyt = (Button) findViewById(R.id.btnGirisKaydol);
        btnKyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Giris.this,Register.class);
                startActivityForResult(myIntent,0);
            }
        });
        btnGiris = (Button) findViewById(R.id.btnGiris);
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pD.setTitle("Login..");
                pD.setMessage("Please Wait..");
                pD.setCanceledOnTouchOutside(false);
                pD.show();
                String email = editText1.getText().toString();
                String password = editText2.getText().toString();
                loginin_user(email, password);

            }
        });
        btnSifre = (Button) findViewById(R.id.button3);
        btnSifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent myIntent = new Intent(Giris.this,ForgetPW.class);
                startActivity(myIntent);


            }
        });



    }
    private void loginin_user (String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pD.dismiss();
                    Intent myIntent = new Intent(Giris.this, anasayfa.class);
                    startActivity(myIntent);
                }
                else{
                    pD.dismiss();
                    Toast.makeText(Giris.this,"Sign Failed..",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
