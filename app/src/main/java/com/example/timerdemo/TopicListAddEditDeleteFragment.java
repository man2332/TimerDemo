package com.example.timerdemo;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_INDEX_POSITION;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_ID;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_NAME;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_TOTAL_MIN;
import static com.example.timerdemo.utils.Constants.DELETE_EXTRA_TOPIC;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopicListAddEditDeleteFragment extends Fragment {

    private TopicViewModel topicViewModel;

    //    @BindView(R.id.topic_name_editText_add)
    EditText topicNameEditTextAddEdit;

    @BindView(R.id.delete_button_topic_list_add)
    Button deleteButtonTopicListAdd;

    Bundle bundle;
    private int id;
    private int elementPosition;

    InputMethodManager imm;

    public TopicListAddEditDeleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic_list_addeditdelete, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        topicNameEditTextAddEdit = getActivity().findViewById(R.id.topic_name_editText_add);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        setHasOptionsMenu(true);

        topicViewModel = ViewModelProviders.of(getActivity()).get(TopicViewModel.class);

        //attempt to getArguments() from bundle, if there are, it's a edit fragment, else its a add new topic
        bundle = getArguments();
        id = bundle != null ? bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID, -1) : -1;
        if (bundle != null && id != -1) {//if there is an ID
            elementPosition = bundle.getInt(ADDEDIT_EXTRA_TOPIC_INDEX_POSITION);//
            topicNameEditTextAddEdit.setText(bundle.getString(ADDEDIT_EXTRA_TOPIC_NAME));
            getActivity().setTitle("Edit topic: ID: " + elementPosition + " TOTAL: " + bundle.getString(ADDEDIT_EXTRA_TOPIC_TOTAL_MIN));
        } else {
            getActivity().setTitle("Add topic");
            deleteButtonTopicListAdd.setVisibility(View.INVISIBLE);
        }
        topicNameEditTextAddEdit.requestFocus();
        topicNameEditTextAddEdit.setSelection(topicNameEditTextAddEdit.getText().length());
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(topicNameEditTextAddEdit, InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
            case android.R.id.home:
                //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                        new TopicListTimersFragment()).commit();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveTopic() {
        String name = topicNameEditTextAddEdit.getText().toString();
        if (name.trim().isEmpty()) {
            Log.d(TAG, "saveTopic: TopicListAddFrag IS EMPTY");
            Toast.makeText(getActivity().getApplicationContext(), "Please insert a title: " + name, Toast.LENGTH_LONG).show();
            return;
        }
        //check if user is editing a topic or adding a new one
        if (bundle != null && bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID, -1) != -1) {
            Toast.makeText(getActivity().getApplication(), "EDITTEED I THINK ", Toast.LENGTH_LONG).show();
            Topic topic = new Topic(name.trim(), bundle.getString(ADDEDIT_EXTRA_TOPIC_TOTAL_MIN));
            topic.setId(bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID));
            Log.d(TAG, "saveTopic: EDIT " + bundle.getInt(ADDEDIT_EXTRA_TOPIC_ID));
            topicViewModel.update(topic);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                    new TopicListTimersFragment()).commit();

        } else {
            Toast.makeText(getActivity().getApplication(), "ADDED I THINK", Toast.LENGTH_LONG).show();
            Topic topic = new Topic(name.trim(), "0");
            topicViewModel.insert(topic);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                    new TopicListTimersFragment()).commit();
        }


    }


    @OnClick(R.id.delete_button_topic_list_add)
    public void onViewClicked(View view) {
        Log.d(TAG, "onViewClicked: ");
        switch (view.getId()) {
            case R.id.delete_button_topic_list_add:
                Log.d(TAG, "onViewClicked: DELETE");
                Bundle deleteBundle = new Bundle();
                deleteBundle.putInt(DELETE_EXTRA_TOPIC, id);
                deleteBundle.putInt(ADDEDIT_EXTRA_TOPIC_INDEX_POSITION, elementPosition);
                Fragment fragment = new TopicListTimersFragment();
                fragment.setArguments(deleteBundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                        fragment).commit();
                break;
            default:
                Toast.makeText(getContext(), "Failed to delete", Toast.LENGTH_LONG).show();
        }
    }


    //when user leaves this view, hide the keyboard
    @Override
    public void onPause() {
        Log.d(TAG, "onPause: TOPICADDEDITDELETEFRAG");
        super.onPause();
//        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(topicNameEditTextAddEdit.getWindowToken(), 0);
    }


}
