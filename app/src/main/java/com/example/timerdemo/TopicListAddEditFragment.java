package com.example.timerdemo;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_GOAL;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_ID;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_NAME;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_TOTAL_MIN;
import static com.example.timerdemo.utils.Constants.DELETE_EXTRA_TOPIC;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopicListAddEditFragment extends Fragment {

    private TopicViewModel topicViewModel;

    //    @BindView(R.id.topic_name_editText_add)
    EditText topicNameEditTextAddEdit;
    //    @BindView(R.id.topic_goal_editText_add)
    EditText topicGoalEditTextAddEdit;
    //    @BindView(R.id.number_picker_hour_topic_list_add)
    NumberPicker numberPickerHour;

    @BindView(R.id.delete_button_topic_list_add)
    Button deleteButtonTopicListAdd;
    @BindView(R.id.create_button_topic_list_add)
    Button createButtonTopicListAdd;

    @BindView(R.id.mon_checkbox)
    CheckBox monCheckbox;
    @BindView(R.id.tue_checkbox)
    CheckBox tueCheckbox;
    @BindView(R.id.wed_checkbox)
    CheckBox wedCheckbox;
    @BindView(R.id.thu_checkbox)
    CheckBox thuCheckbox;
    @BindView(R.id.fri_checkbox)
    CheckBox friCheckbox;
    @BindView(R.id.sat_checkbox)
    CheckBox satCheckbox;
    @BindView(R.id.sun_checkbox)
    CheckBox sunCheckbox;

    Bundle bundle;
    private int id;

    public TopicListAddEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic_list_add, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topicNameEditTextAddEdit = getActivity().findViewById(R.id.topic_name_editText_add);
        topicGoalEditTextAddEdit = getActivity().findViewById(R.id.topic_goal_editText_add);
        numberPickerHour = getActivity().findViewById(R.id.number_picker_hour_topic_list_add);
        numberPickerHour.setMaxValue(24);
        numberPickerHour.setMinValue(0);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        topicViewModel = ViewModelProviders.of(getActivity()).get(TopicViewModel.class);
        setHasOptionsMenu(true);

        bundle = getArguments();
        id = bundle != null? bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID, -1) : -1;
        if (bundle != null && id != -1) {//if there is an ID
            topicNameEditTextAddEdit.setText(bundle.getString(ADDEDIT_EXTRA_TOPIC_NAME));
            topicGoalEditTextAddEdit.setText(bundle.getString(ADDEDIT_EXTRA_TOPIC_GOAL));
            getActivity().setTitle("Edit topic: TOTAL: " + bundle.getString(ADDEDIT_EXTRA_TOPIC_TOTAL_MIN));
        } else {
            getActivity().setTitle("Add topic");
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_topic_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_topic:
                saveTopic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTopic() {
        String name = topicNameEditTextAddEdit.getText().toString();
        String goal = topicGoalEditTextAddEdit.getText().toString();
        if (name.trim().isEmpty() || goal.trim().isEmpty()) {
            Log.d(TAG, "saveTopic: TopicListAddFrag IS EMPTY");
            Toast.makeText(getActivity().getApplicationContext(), "Please insert a title: " + name, Toast.LENGTH_LONG).show();
            return;
        }

        if (bundle != null && bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID, -1) != -1) {
            Toast.makeText(getActivity().getApplication(), "EDITTEED I THINK", Toast.LENGTH_LONG).show();
            Topic topic = new Topic(name,
                    bundle.getString(ADDEDIT_EXTRA_TOPIC_TOTAL_MIN),
                    goal);
            topic.setId(bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID));
            Log.d(TAG, "saveTopic: EDIT " + bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID));
            topicViewModel.update(topic);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                    new TopicListFragment()).commit();

        } else {
            Toast.makeText(getActivity().getApplication(), "ADDED I THINK", Toast.LENGTH_LONG).show();
            Topic topic = new Topic(name, "0", goal);
            topicViewModel.insert(topic);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                    new TopicListFragment()).commit();
        }


    }


    @OnClick(R.id.delete_button_topic_list_add)
    public void onViewClicked(View view) {
        Log.d(TAG, "onViewClicked: ");
        switch(view.getId()){
            case R.id.delete_button_topic_list_add:
                Log.d(TAG, "onViewClicked: DELETE");
                Bundle deleteBundle = new Bundle();
                deleteBundle.putInt(DELETE_EXTRA_TOPIC, id);
                Fragment fragment = new TopicListFragment();
                fragment.setArguments(deleteBundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                        fragment).commit();
                break;
            default:
                Toast.makeText(getContext(),"Failed to delete", Toast.LENGTH_LONG).show();
        }
    }
}
