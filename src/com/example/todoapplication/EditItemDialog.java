package com.example.todoapplication;


import java.util.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditItemDialog extends DialogFragment{

	private final int START_CALENDAR_FRAG_CODE = 1;
	private int month;
	private int day;
	private int year;
	private EditText etEditItem;
	private Button btnSave;
	private int itemPosition;
	private TextView tvDateFinish;
	private RadioGroup rgTaskPriority;
		

	public EditItemDialog() {
		// Empty constructor required for DialogFragment
	}
	
	public interface EditItemDialogListener{
		void onFinishEditDialog(String itemName, int itemPosition, String monthDayYear, String priorityIndex);
	}
	
	public static EditItemDialog newInstance(String title) {
		EditItemDialog frag = new EditItemDialog();
		Bundle args = new Bundle();
		args.putString("title", "Edit Item");
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_edit_item, container);
		
		etEditItem = (EditText) view.findViewById(R.id.etEditItem);
		btnSave = (Button) view.findViewById(R.id.btnSave);
		tvDateFinish = (TextView) view.findViewById(R.id.tvDateFinish);
		rgTaskPriority = (RadioGroup)view.findViewById(R.id.rgTaskPriority);
		
		String title = getArguments().getString("title", "Enter Name");
		getDialog().setTitle(title);
		
		etEditItem.setText(getArguments().getString("item_name"));
		etEditItem.setSelection(etEditItem.getText().length());
		// Show soft keyboard automatically
		etEditItem.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		itemPosition = getArguments().getInt("item_position", 0);

		int priorityIndex = Integer.parseInt(getArguments().getString("task_priority_index"));
		((RadioButton)rgTaskPriority.getChildAt(priorityIndex)).setChecked(true);

		tvDateFinish.setText(getArguments().getString("task_month_day_year"));
		if(tvDateFinish.getText().toString().isEmpty()){
			SetDateText();
		}
		
		setupOnClickListener();
		
		return view;
	}
	
	// Setting up onclick listener for text view date
	private void setupOnClickListener() {
		tvDateFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowCalendarDialog(v);
			}
		});
		
		btnSave.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				
				int radioButtonID = rgTaskPriority.getCheckedRadioButtonId();
				View radioButton = rgTaskPriority.findViewById(radioButtonID);
				String priorityIndex = rgTaskPriority.indexOfChild(radioButton) + "";
				
				EditItemDialogListener listner = (EditItemDialogListener)getActivity();
				listner.onFinishEditDialog(etEditItem.getText().toString(), itemPosition, tvDateFinish.getText().toString(), priorityIndex);
				dismiss();
			}
			
		});
	}
	
	// Invokes the calendar dialog using set target fragment with appropriate date set 
	public void ShowCalendarDialog(View v)
	{
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        GetMonthDayYear();
        bundle.putInt("Month", month);
        bundle.putInt("Day", day);
        bundle.putInt("Year", year);
        DialogFragment df = new DatePickerFragment();
        df.setArguments(bundle);
        df.setTargetFragment(this, START_CALENDAR_FRAG_CODE);
        df.show(fm, "datePicker");
	}

	// Set the date hint if this is the first time user is editing item
	private void SetDateText() {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		tvDateFinish.setText(month+"/"+day+"/"+year);
	}
	
	// Get month, day, year from text view
	private void GetMonthDayYear() {
		if(tvDateFinish.getText().toString().isEmpty()){
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
		else{
			String MonthDayYear = tvDateFinish.getText().toString();
			String Month = MonthDayYear.substring(0, MonthDayYear.indexOf("/"));
			String DayYear = MonthDayYear.substring(MonthDayYear.indexOf("/") + 1);
			String Day = DayYear.substring(0, DayYear.indexOf("/"));
			String Year = DayYear.substring(DayYear.indexOf("/") + 1);
			
			month = Integer.parseInt(Month) - 1;
			day = Integer.parseInt(Day);
			year = Integer.parseInt(Year);
		}
	}

	// Gets called when calendar dialog returns
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == START_CALENDAR_FRAG_CODE){
	    	String Text = data.getStringExtra("DateFinish");
	    	tvDateFinish.setText(Text);
	    }
	}
}
