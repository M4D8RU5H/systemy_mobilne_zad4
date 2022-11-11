package com.example.sys_mob_zad3.activities;

import androidx.fragment.app.Fragment;

import com.example.sys_mob_zad3.fragments.TaskFragment;
import com.example.sys_mob_zad3.fragments.TaskListFragment;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID);
        return TaskFragment.newInstance(id);
    }
}