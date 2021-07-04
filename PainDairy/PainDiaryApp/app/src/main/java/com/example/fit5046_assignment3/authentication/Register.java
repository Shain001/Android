package com.example.fit5046_assignment3.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fit5046_assignment3.MainActivity;
import com.example.fit5046_assignment3.R;
import com.example.fit5046_assignment3.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding regBinding;
    private EditText name;
    private EditText email;
    private EditText passwd;
    private EditText passwd2;
    private Button regBtn;
    private TextView loginBtn;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initiate the view
        super.onCreate(savedInstanceState);
        regBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = regBinding.getRoot();
        setContentView(view);

        // initiate fields

        name = regBinding.name;
        email = regBinding.email;
        passwd = regBinding.password;
        regBtn = regBinding.registerBtn;
        loginBtn = regBinding.loginJump;
        progressBar = regBinding.progressBar;
        passwd2 = regBinding.password2;

        fAuth = FirebaseAuth.getInstance();

        // set listener for login jumping button

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        // set listener for register button
        regBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                // Fetch data from input
                String userEmail = email.getText().toString().trim();
                String userPasswd = passwd.getText().toString().trim();
                String userName = name.getText().toString().trim();
                String userPasswd2 = passwd2.getText().toString().trim();


                // Check Constraint
                if (userEmail.equals("")){
                    email.setError("Email Cannot be Blank");
                    return;
                }

                if (userPasswd.equals("")){
                    passwd.setError("Password Cannot be Blank");
                    return;
                }

                if (userPasswd2.equals("")){
                    passwd2.setError("Password Cannot be Blank");
                    return;
                }

                if (userName.equals("")){
                    passwd.setError("Name Cannot be Blank");
                    return;
                }

                if (userPasswd.length() < 6){
                    passwd.setError("Length of Password has to larger than 6");
                    return;
                }

                if (!userPasswd.equals(userPasswd2)){
                    passwd2.setError("Two Passwords are different");
                    return;
                }

                // Check if the user has already login
                if (fAuth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

                // If everything fine, start registering
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(userEmail, userPasswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Register Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}