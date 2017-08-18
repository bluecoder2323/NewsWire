package com.developinggeek.thebetterlawyernewsapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developinggeek.thebetterlawyernewsapp.Fragments.InternationalFragment;
import com.developinggeek.thebetterlawyernewsapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity
{

    private TextInputLayout edt_email , edt_pass;
    private Button btn_login , btn_register;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_register = (Button)findViewById(R.id.login_btn_register);
        edt_email = (TextInputLayout) findViewById(R.id.login_email);
        edt_pass = (TextInputLayout)findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(LoginActivity.this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String email = edt_email.getEditText().getText().toString();
                String password = edt_pass.getEditText().getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
                {
                    mProgress.setTitle("Logging in...");
                    mProgress.setMessage("Please wait while se check your credentials");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    login_user(email , password);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Enter all the text fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void login_user(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    mProgress.dismiss();

                    Intent mainIntent = new Intent(LoginActivity.this , MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                }
                else
                {
                    mProgress.dismiss();

                    Toast.makeText(LoginActivity.this, "Could not Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
