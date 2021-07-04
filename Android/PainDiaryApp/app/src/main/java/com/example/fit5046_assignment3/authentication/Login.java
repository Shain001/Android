package com.example.fit5046_assignment3.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.example.fit5046_assignment3.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private EditText email;
    private EditText password;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    private TextView regBtn;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initiate the veiw
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);

        // initiate fields
        email = loginBinding.email;
        password = loginBinding.password;
        progressBar = loginBinding.progressBar;
        fAuth = FirebaseAuth.getInstance();
        regBtn = loginBinding.singnupJump;
        login = loginBinding.loginBtn;

        // set register page jumping
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });

        // set listener for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch data from input
                String userEmail = email.getText().toString().trim();
                String userPasswd = password.getText().toString().trim();

                // Check Constraint
                if (TextUtils.isEmpty(userEmail)){
                    email.setError("Email Cannot be Blank");
                    return;
                }

                if (TextUtils.isEmpty(userPasswd)){
                    password.setError("Password Cannot be Blank");
                    return;
                }

                if (userPasswd.length() < 6){
                    password.setError("Length of Password has to larger than 6");
                    return;
                }

                // If everything fine, start login
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(userEmail, userPasswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}