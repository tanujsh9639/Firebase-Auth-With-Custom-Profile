package com.tanujsh.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword , editTextCity,editTextName,editTextRoll,editTextCourse;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        editTextCity=(EditText)findViewById(R.id.editTextCity);
        editTextRoll=(EditText)findViewById(R.id.editTextRoll);
        editTextCourse=(EditText)findViewById(R.id.editTextCourse);
        editTextName=(EditText)findViewById(R.id.editTextName);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

    }
private void registerUser(){
    String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();
    String name =editTextName.getText().toString().trim();
    String city =editTextCity.getText().toString().trim();
    String course =editTextCourse.getText().toString().trim();
    String roll =editTextRoll.getText().toString().trim();
    if (email.isEmpty()) {
        editTextEmail.setError("Email is required");
        editTextEmail.requestFocus();
        return;
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        editTextEmail.setError("Please enter a valid email");
        editTextEmail.requestFocus();
        return;
    }

    if (password.isEmpty()) {
        editTextPassword.setError("Password is required");
        editTextPassword.requestFocus();
        return;
    }

    if (password.length() < 6) {
        editTextPassword.setError("Minimum length of password should be 6");
        editTextPassword.requestFocus();
        return;
    }

    progressBar.setVisibility(View.VISIBLE);

    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {
                //finish();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
          user user=new user(name,city,course,roll);

          FirebaseDatabase.getInstance().getReference("Users")
                  .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                  .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  if(task.isSuccessful())
                  {
                      Toast.makeText(SignUpActivity.this,"User REgistered",Toast.LENGTH_LONG);
                      progressBar.setVisibility(View.GONE);
                  }
              }
          });
            } else {

                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
    });
}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}