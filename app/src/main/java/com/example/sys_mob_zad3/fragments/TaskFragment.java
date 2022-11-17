package com.example.sys_mob_zad3.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sys_mob_zad3.R;
import com.example.sys_mob_zad3.TaskStorage;
import com.example.sys_mob_zad3.enums.Category;
import com.example.sys_mob_zad3.models.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFragment extends Fragment {
    private Task task = new Task();
    private EditText dateField;

    private static final String ARG_TASK_ID = "todoapp-task-id";
    private final Calendar calendar = Calendar.getInstance();

    public TaskFragment() {
        //empty constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        UUID id = (UUID) getArguments().getSerializable(ARG_TASK_ID);

        Task taskNew = TaskStorage.getInstance().getTask(id);
        assert taskNew != null;
        this.task = taskNew;
    }

    public static TaskFragment newInstance (UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID, id);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        EditText name = view.findViewById(R.id.task_name);
        dateField = view.findViewById(R.id.task_date);
        CheckBox checkBox = view.findViewById(R.id.task_done);
        Spinner categorySpinner = view.findViewById(R.id.task_category);

        name.setText(task.getName());
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty
            }

            @Override
            public void onTextChanged(CharSequence name, int start, int before, int count) {
                task.setName(name.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //empty
            }
        });

        dateField.setText(task.getDate().toString());
        DatePickerDialog.OnDateSetListener date = (view12, year, month, day) -> {
          calendar.set(Calendar.YEAR, year);
          calendar.set(Calendar.MONTH, month);
          calendar.set(Calendar.DAY_OF_MONTH, day);
          setupDateFieldValue(calendar.getTime());
          task.setDate(calendar.getTime());
        };
        dateField.setOnClickListener(view1 -> {
            new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                    .show();
            setupDateFieldValue(task.getDate());
        });

        categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, Category.values()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                task.setCategory(Category.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        categorySpinner.setSelection(task.getCategory().ordinal());

        checkBox.setChecked(task.isDone());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
        });

        return view;
    }

    private void setupDateFieldValue(Date date) {
        Locale locale = new Locale("pl", "PL");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
        dateField.setText(dateFormat.format(date));
    }
}