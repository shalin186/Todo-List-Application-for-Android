package com.example.todoapplication;

import android.app.Activity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoActivity extends Activity {

	private final int REQUEST_CODE = 18;
	private ArrayList<String> todoItems;
    private ArrayAdapter<String> todoAdapter;
    private ListView lvItems;
    private EditText etNewItem; 
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        todoAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, todoItems);
        lvItems.setAdapter(todoAdapter);
        setupOnClickListener();
    }

	public void onAddedItem(View v) {
		String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);
		etNewItem.setText("");
		writeItems();
	}

	private void setupOnClickListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return false;
			}
			
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				Intent editItemIntent = new Intent(TodoActivity.this, EditItemActivity.class);
				editItemIntent.putExtra("item_name", todoItems.get(pos));
				editItemIntent.putExtra("item_position", pos);
				startActivityForResult(editItemIntent, REQUEST_CODE);
			}
		}
		);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent editItemIntent) {
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
		  String editedItemName = editItemIntent.getExtras().getString("item_name");
		  int editedItemPosition = editItemIntent.getExtras().getInt("item_position");
		  todoItems.set(editedItemPosition, editedItemName);
		  todoAdapter.notifyDataSetChanged();
		  writeItems();
	  }
	} 
	
    private void readItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try{
    		todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    	}catch (IOException e){
    		todoItems = new ArrayList<String>();
    	}
	}
    
    private void writeItems() {
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try{
    		FileUtils.writeLines(todoFile, todoItems);
    	}catch (IOException e){
    		e.printStackTrace();
    	}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
