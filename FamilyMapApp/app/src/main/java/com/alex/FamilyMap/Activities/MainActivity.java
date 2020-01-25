package com.alex.FamilyMap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.R;
import com.alex.FamilyMap.Fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {
    DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        if (dataCache.isDataCacheEmpty()) {
            fragment = new LoginFragment();
            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}
