package com.example.sys_mob_zad3.activities;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.sys_mob_zad3.R;


public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    private Fragment fragment;
    private int fragmentsListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fragmentsListSize = getSupportFragmentManager().getFragments().size();

        if (fragmentsListSize == 0) {
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
