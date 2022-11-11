package com.example.sys_mob_zad3.activities;
import androidx.fragment.app.Fragment;

import com.example.sys_mob_zad3.fragments.TaskListFragment;

public class TaskListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}