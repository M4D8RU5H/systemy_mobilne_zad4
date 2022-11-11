package com.example.sys_mob_zad3.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sys_mob_zad3.R;
import com.example.sys_mob_zad3.TaskStorage;
import com.example.sys_mob_zad3.activities.MainActivity;
import com.example.sys_mob_zad3.models.Task;

import java.util.List;

public class TaskListFragment extends Fragment {

    public static final String KEY_EXTRA_TASK_ID = "ask-id";
    private RecyclerView recyclerView;

    public TaskListFragment() {
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void updateView () {
        TaskStorage storage = TaskStorage.getInstance();
        List<Task> tasks = storage.getTasks();

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            recyclerView.setAdapter(new TaskAdapter(tasks));
            return;
        }

        adapter.notifyDataSetChanged();
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView date;
        private Task task = null;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
            });

            name = itemView.findViewById(R.id.task_item_date);
            date = itemView.findViewById(R.id.task_item_name);
        }

        void bind (Task task) {
            this.task = task;
            name.setText(task.getName());
            date.setText(task.getDate().toString());
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private final List<Task> taskList;

        public TaskAdapter (List<Task> tasks) {
            taskList = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TaskHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            holder.bind(taskList.get(position));
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}