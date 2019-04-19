package com.example.rapidfood.Vendor_files;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidfood.R;
import com.example.rapidfood.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TermConActivity extends AppCompatActivity {

    private TextView mTerms;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_con);
        mFirebaseInstances=new FirebaseInstances();
        mFirebaseFirestore=mFirebaseInstances.getFirebaseFirestore();
        mTerms=findViewById(R.id.termsandcon_Text);
        mFirebaseFirestore.collection("company_data")
                .document("term").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
            if(pTask.isSuccessful()){
                 DocumentSnapshot vDocumentSnapshot=pTask.getResult();
                assert vDocumentSnapshot != null;
                mTerms.setTextColor(getResources().getColor(R.color.grey_800));
                mTerms.setText(vDocumentSnapshot.getString("terms"));
            }
            else{
                Toast.makeText(TermConActivity.this, "Firestore permission required??", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
