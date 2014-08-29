package com.example.todoapplication;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.example.todoapplication.EditItemDialog.EditItemDialogListener;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TodoActivity extends FragmentActivity implements EditItemDialogListener{

	private final int REQUEST_CODE = 18;
	private final boolean USE_DIALOG_FRAGMENT = true;
	private ArrayList<String> todoItemsList;
	private ArrayList<String> todoItemsDateList;
	private ArrayList<String> todoItemsPriorityList;
    private ArrayAdapter<String> todoAdapter;
    private ListView lvItems;
    private EditText etNewItem; 
    private boolean flag = false;
	int year;
	int month;
	int day;
	String monthDayYear;

    CustomTodoListAdapter customTodoListAdapter;
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        
//        todoAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, todoItemsList){
//        	@Override
//        	public View getView(int position, View convertView, ViewGroup parent) {
//        	    View view = super.getView(position, convertView, parent);
//        	    return view;
//        	  }
//        	};
//        lvItems.setAdapter(todoAdapter);

        customTodoListAdapter = new CustomTodoListAdapter(this, todoItemsList, todoItemsDateList, todoItemsPriorityList);
        lvItems.setAdapter(customTodoListAdapter);

        setupOnClickListener();
    }

	public void onAddedItem(View v) {
		String itemText = etNewItem.getText().toString();
		if(!(itemText.isEmpty())){
			etNewItem.setText("");
			setMonthDayYear();
			todoItemsList.add(itemText);
			todoItemsDateList.add(monthDayYear);
			todoItemsPriorityList.add("0");
			customTodoListAdapter.notifyDataSetChanged();
			writeItems();
			
//			todoAdapter.add(itemText);
//			etNewItem.setText("");
//			writeItems();
		}
	}

	private void setupOnClickListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				todoItemsList.remove(pos);
				todoItemsDateList.remove(pos);
				todoItemsPriorityList.remove(pos);
				customTodoListAdapter.notifyDataSetChanged();
				writeItems();
				
				//todoAdapter.notifyDataSetChanged();
				//writeItems();
				return false;
			}
			
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				if(USE_DIALOG_FRAGMENT){
					Bundle bundle = new Bundle();
					bundle.putString("item_name", todoItemsList.get(pos));
					bundle.putInt("item_position", pos);
					bundle.putString("task_month_day_year", todoItemsDateList.get(pos));
					bundle.putString("task_priority_index", todoItemsPriorityList.get(pos));
			        FragmentManager fm = getSupportFragmentManager();
			        EditItemDialog editItemDialog = EditItemDialog.newInstance("Edit Item");
			        editItemDialog.setArguments(bundle);
			        editItemDialog.show(fm, "fragment_edit_name");
				}
				else{
					Intent editItemIntent = new Intent(TodoActivity.this, EditItemActivity.class);
					editItemIntent.putExtra("item_name", todoItemsList.get(pos));
					editItemIntent.putExtra("item_position", pos);
					startActivityForResult(editItemIntent, REQUEST_CODE);
				}
			}
		}
		);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent editItemIntent) {
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		  String editedItemName = editItemIntent.getExtras().getString("item_name");
		  int editedItemPosition = editItemIntent.getExtras().getInt("item_position");

		  if(editedItemName.isEmpty()){
			  todoItemsList.remove(editedItemPosition);
		  }
		  else{
			  flag = true;
			  todoItemsList.set(editedItemPosition, editedItemName);  
		  }
		  todoAdapter.notifyDataSetChanged();
		  writeItems();
	  }
	} 
	
	@Override
	public void onFinishEditDialog(String itemName, int itemPosition, String monthDayYear, String priorityIndex) {
		  String editedItemName = itemName;
		  int editedItemPosition = itemPosition;

		  if(editedItemName.isEmpty()){
			  	todoItemsList.remove(editedItemPosition);
				todoItemsDateList.remove(editedItemPosition);
				todoItemsPriorityList.remove(editedItemPosition);
		  }
		  else{
			  todoItemsList.set(editedItemPosition, editedItemName);
			  todoItemsDateList.set(editedItemPosition, monthDayYear);
			  todoItemsPriorityList.set(editedItemPosition, priorityIndex);
		  }
		  customTodoListAdapter.notifyDataSetChanged();
		  writeItems();
		  
		  //todoAdapter.notifyDataSetChanged();
		  //writeItems();
	}	
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoListFile = new File(filesDir, "todoList.txt");
    	File todoDateFile = new File(filesDir, "todoDate.txt");
    	File todoPriorityFile = new File(filesDir, "todoPriority.txt");
    	try{
    		todoItemsList = new ArrayList<String>(FileUtils.readLines(todoListFile));
    		todoItemsDateList = new ArrayList<String>(FileUtils.readLines(todoDateFile));
    		todoItemsPriorityList = new ArrayList<String>(FileUtils.readLines(todoPriorityFile));
    	}catch (IOException e){
    		todoItemsList = new ArrayList<String>();
    		todoItemsDateList = new ArrayList<String>();
    		todoItemsPriorityList = new ArrayList<String>();
    	}
	}
    
    private void writeItems() {
    	File filesDir = getFilesDir();
    	File todoListFile = new File(filesDir, "todoList.txt");
    	File todoDateFile = new File(filesDir, "todoDate.txt");
    	File todoPriorityFile = new File(filesDir, "todoPriority.txt");
    	try{
    		FileUtils.writeLines(todoListFile, todoItemsList);
    		FileUtils.writeLines(todoDateFile, todoItemsDateList);
    		FileUtils.writeLines(todoPriorityFile, todoItemsPriorityList);
    	}catch (IOException e){
    		e.printStackTrace();
    	}
	}
    
    private void setMonthDayYear()
    {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		monthDayYear = month+"/"+day+"/"+year;
    }
}

class CustomTodoListAdapter extends ArrayAdapter<String>
{
	Context context;
	private ArrayList<String> todoItemsList;
	private ArrayList<String> todoItemsDateList;
	private ArrayList<String> todoItemsPriorityList;
	
	public CustomTodoListAdapter(Context context, ArrayList<String> todoItemsList, ArrayList<String> todoItemsDateList, ArrayList<String> todoItemsPriorityList) {
		super(context, R.layout.single_row_todo_list, R.id.tvTodoItem, todoItemsList);
		this.context = context;
		this.todoItemsList = todoItemsList;
		this.todoItemsDateList = todoItemsDateList;
		this.todoItemsPriorityList = todoItemsPriorityList;
	}
	
    private boolean isTaskPastDue(String MonthDayYear)
    {
		final Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		int currentDay = c.get(Calendar.DAY_OF_MONTH);
		
		String Month = MonthDayYear.substring(0, MonthDayYear.indexOf("/"));
		String DayYear = MonthDayYear.substring(MonthDayYear.indexOf("/") + 1);
		String Day = DayYear.substring(0, DayYear.indexOf("/"));
		String Year = DayYear.substring(DayYear.indexOf("/") + 1);
		
		int taskDueMonth = Integer.parseInt(Month);
		int taskDueDay = Integer.parseInt(Day);
		int taskDueYear = Integer.parseInt(Year);
		
		if(currentYear > taskDueYear){
			return  true;
		}
		
		if(currentYear == taskDueYear && currentMonth > taskDueMonth){
			return  true;
		}

		if(currentYear == taskDueYear && currentMonth == taskDueMonth && currentDay > taskDueDay){
			return  true;
		}
		
		return false;
		
    }

    
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View singleRow = inflater.inflate(R.layout.single_row_todo_list, parent, false);
		
	    TextView todoItemName = (TextView)singleRow.findViewById(R.id.tvTodoItem);
	    TextView todoItemDate = (TextView)singleRow.findViewById(R.id.tvDate);
		
	    todoItemName.setText(todoItemsList.get(position));
	    todoItemDate.setText(todoItemsDateList.get(position));
	    
	    if(todoItemsPriorityList.get(position).equals("1"))
	    {
	    	todoItemName.setTextColor(Color.RED);
	    }
	    
	    if(isTaskPastDue(todoItemsDateList.get(position)))
	    {
	    	todoItemDate.setBackgroundColor(Color.RED);
	    	todoItemDate.setTextColor(Color.WHITE);
	    }
	    

	    return singleRow;
	}
	
}
