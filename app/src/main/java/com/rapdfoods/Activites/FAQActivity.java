package com.rapdfoods.Activites;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.rapdfoods.Adapters.AdapterListExpand;
import com.rapdfoods.Models.FAQModelClass;
import com.rapdfoods.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    ExpandableListView mListView;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);

         mListView=findViewById(R.id.listview);
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("company_data")
                .document("faq")
                .collection("faq_data")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<FAQModelClass> list_faq = new ArrayList<>();
                        for (DocumentSnapshot x : queryDocumentSnapshots) {
                            if (x.exists()) {
                                FAQModelClass question = new FAQModelClass();
                                question.setQuestion(x.getString("question"));
                                question.setAnswer(x.getString("answer"));
                                list_faq.add(question);
                                Toast.makeText(FAQActivity.this, "", Toast.LENGTH_SHORT).show();
                            }
                        }
                        mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.setAdapter(new AdapterListExpand(FAQActivity.this,list_faq));

                            }
                        });

                    }
                });

    }
}
