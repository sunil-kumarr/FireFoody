package com.example.rapidfood.VendorActivities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.rapidfood.R;
import com.example.rapidfood.Utils.EncryptionHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowQRDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_qr_data);
        TextView vTextView = findViewById(R.id.showData);
        if (getIntent().getSerializableExtra("qr_data") != null) {
            String text = EncryptionHelper.getInstance().getDecryptionString(getIntent().getStringExtra("qr_data"));
            vTextView.setText(text);
        }
    }

}
