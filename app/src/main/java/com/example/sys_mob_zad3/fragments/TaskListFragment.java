package com.example.sys_mob_zad3.fragments;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sys_mob_zad3.R;
import com.example.sys_mob_zad3.TaskStorage;
import com.example.sys_mob_zad3.activities.MainActivity;
import com.example.sys_mob_zad3.enums.Category;
import com.example.sys_mob_zad3.models.Task;

import java.util.List;

public class TaskListFragment extends Fragment {

    public static final String KEY_EXTRA_TASK_ID = "task-id";
    public static final String SUBTITLE_VISIBLE_KEY = "visible";
    private static final int MAX_NAME_LENGTH = 40;
    private RecyclerView recyclerView;
    private boolean subtitleVisible;

    public TaskListFragment() {
        //empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_KEY, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBTITLE_VISIBLE_KEY, subtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);

        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                return true;

            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateView () {
        TaskStorage storage = TaskStorage.getInstance();
        List<Task> tasks = storage.getTasks();

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            recyclerView.setAdapter(new TaskAdapter(tasks));
            return;
        }

        adapter.notifyDataSetChanged();
        updateSubtitle();
    }

    public void updateSubtitle() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int todoTasksCount = 0;

        for (Task task : tasks) {
            if (!task.isDone()) {
                todoTasksCount++;
            }
        }

        String subtitle = getString(R.string.subtitle_format, todoTasksCount);
        if (!subtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView date;
        private final ImageView iconImageView;
        private final CheckBox checkBox;
        private Task task = null;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
            });

            name = itemView.findViewById(R.id.task_item_name);
            date = itemView.findViewById(R.id.task_item_date);
            iconImageView = itemView.findViewById(R.id.task_item_icon);
            checkBox = itemView.findViewById(R.id.task_item_checkbox);
        }

        void bind (Task task) {
            this.task = task;

            if (task.getName().length() <= MAX_NAME_LENGTH) {
                name.setText(task.getName());
            } else {
                name.setText(task.getName().substring(0, MAX_NAME_LENGTH - 4) + "...");
            }

            date.setText(task.getDate().toString());

            if (task.getCategory() == Category.HOME) {
                iconImageView.setImageResource(R.drawable.ic_home);
            } else {
                iconImageView.setImageResource(R.drawable.ic_university);
            }

            if (task.isDone()) {
                name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                name.setPaintFlags(name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            checkBox.setChecked(task.isDone());
        }

        public CheckBox getCheckBox() { return checkBox; }
        public TextView getNameTextView() { return name; }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private final List<Task> tasks;

        public TaskAdapter (List<Task> tasks) {
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TaskHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            holder.bind(tasks.get(position));

            CheckBox checkBox = holder.getCheckBox();
            TextView name = holder.getNameTextView();

            checkBox.setChecked(tasks.get(position).isDone());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked);

                if (isChecked) {
                    name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    name.setPaintFlags(name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
                updateSubtitle();
            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}