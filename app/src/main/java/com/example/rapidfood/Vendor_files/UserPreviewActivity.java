package com.example.rapidfood.Vendor_files;

import android.os.Bundle;

import com.example.rapidfood.Fragments.HomeFragment;
import com.example.rapidfood.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UserPreviewActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment vFragment = new HomeFragment();
        mFragmentTransaction.replace(R.id.user_preview_holder, vFragment, "frag_preview");
        mFragmentTransaction.commit();
    }
}
