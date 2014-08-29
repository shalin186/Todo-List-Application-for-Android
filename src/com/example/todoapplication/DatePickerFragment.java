package com.example.todoapplication;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private final int START_CALENDAR_FRAG_CODE = 1;
	
	@Override
	public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
		// Set the date sent by user as the default date
		
		int month;
		int day;
		int year;

		final Calendar c = Calendar.getInstance();
		int defaultYear = c.get(Calendar.YEAR);
		int defaultMonth = c.get(Calendar.MONTH) + 1;
		int defaultDay = c.get(Calendar.DAY_OF_MONTH);

		Bundle bundle = this.getArguments();
		month = bundle.getInt("Month", defaultMonth);
		day = bundle.getInt("Day", defaultDay);
        year = bundle.getInt("Year", defaultYear);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		month = month + 1;
		
	    Intent EditItemDialogIntent = new Intent();
	    EditItemDialogIntent.putExtra("DateFinish", month+"/"+day+"/"+year);
	    getTargetFragment().onActivityResult(getTargetRequestCode(), START_CALENDAR_FRAG_CODE, EditItemDialogIntent);
	}
}