package com.example.rapidfood.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        TextView userDetails =findViewById(R.id.userDetails);
        Button signout = (Button) findViewById(R.id.signout);
        if(mAuth.getCurrentUser()!=null){
            Toast.makeText(this, "SignedIn", Toast.LENGTH_SHORT).show();
            userDetails.setText("USER: "+mAuth.getCurrentUser().getUid());
        }
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,Authentication.class));
                finish();
            }
        });
    }
}
