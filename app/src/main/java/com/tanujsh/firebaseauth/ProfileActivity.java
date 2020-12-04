package com.tanujsh.firebaseauth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ProfileActivity extends AppCompatActivity {

   private FirebaseUser cUser;
   private DatabaseReference reference;
private Button logout;
   private String userID;
    ProgressBar progressBar;



    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        loadUserInformation();

logout=findViewById(R.id.Buttonlogout);
 logout.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         FirebaseAuth.getInstance().signOut();
         startActivity(new Intent(ProfileActivity.this,MainActivity.class));
     }
 });





    }

     @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void loadUserInformation() {

        cUser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=cUser.getUid();
        final TextView tname=(TextView)findViewById(R.id.editTextName);
        final TextView tcity=(TextView)findViewById(R.id.editTextCity);
        final TextView tcourse=(TextView)findViewById(R.id.editTextCourse);
        final TextView troll=(TextView)findViewById(R.id.editTextRoll);
reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        user userProfile =snapshot.getValue(user.class);
        if ( userProfile!=null)
        {
            String name= userProfile.name;
            String city= userProfile.city;
            String course= userProfile.course;
            String roll= userProfile.roll;
            tname.setText("Welcome"+" "+name);
            tcity.setText("City :"+" "+city);
            tcourse.setText("Course :"+" "+course);
            troll.setText("Roll No: "+" "+roll);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
Toast.makeText(ProfileActivity.this,"Kuch Gadbad hai",Toast.LENGTH_LONG).show();
    }
});
    }

}