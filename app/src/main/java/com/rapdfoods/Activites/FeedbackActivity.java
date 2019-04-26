package com.rapdfoods.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.rapdfoods.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {


    private EditText EdtMain,EdtSub;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);
        EdtMain=findViewById(R.id.feedback_text);
        EdtSub=findViewById(R.id.feedback_about);
        ImageView imageView=findViewById(R.id.send_feedback);
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=String.valueOf(EdtMain.getText());
                String about=String.valueOf(EdtSub.getText());
                if(text.length()>10 ) {
                    Map<String, Object> feed = new HashMap<>();
                    feed.put("improve", text);
                    feed.put("how", about);
                    firebaseFirestore.collection("feedback")
                            .document()
                            .set(feed);
                    Snackbar.make(findViewById(R.id.snackbar_show),"Thank you so much.",Snackbar.LENGTH_LONG).show();
                    EdtMain.setText("");
                    EdtSub.setText("");
                }
                else{
                    EdtMain.setError("Invalid");
                }
            }
        });


    }
}