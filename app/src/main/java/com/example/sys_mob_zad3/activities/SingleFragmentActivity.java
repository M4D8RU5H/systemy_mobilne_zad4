package com.example.sys_mob_zad3.activities;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sys_mob_zad3.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (fragment == null) {
            fragment = createFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public Fragment getFragment() {
        return fragment;
    }
}
