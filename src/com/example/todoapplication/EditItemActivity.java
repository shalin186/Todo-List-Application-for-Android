package com.example.todoapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends Activity {

	private EditText etEditItem;
	private Button btnSave;
	private int itemPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		etEditItem = (EditText) findViewById(R.id.etEditItem);
		btnSave = (Button) findViewById(R.id.btnSave);
		
		itemPosition = getIntent().getIntExtra("item_position", 0);
		etEditItem.setText(getIntent().getStringExtra("item_name"));
		etEditItem.setSelection(etEditItem.getText().length());
		
		btnSave.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				Intent todoActivityIntent = new Intent();
				todoActivityIntent.putExtra("item_name", etEditItem.getText().toString());
				todoActivityIntent.putExtra("item_position", itemPosition);
				setResult(RESULT_OK, todoActivityIntent);
				finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
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
