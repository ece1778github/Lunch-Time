package com.example.lunchtime;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class UserDataActivity extends MainScreen {
	ArrayList<Integer> userIDs = new ArrayList<Integer>();
	ArrayList<String> names = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_data);
	    db=new DatabaseHelper(getApplicationContext());
		new LoadUserTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_data, menu);
		return true;
	}
	
	class LoadUserTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadData();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			
			populateSpinner();

		}
		
	}
	
	private void populateSpinner(){
		Spinner userSpinner = (Spinner) findViewById(R.id.user_spinner);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(aa);
		
	}
	
	public void loadData(){
		Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerInfo", null);
		if (result != null){
			while (result.moveToNext()) {
				userIDs.add(result.getInt(0));
				names.add(result.getString(1));
			}
		
		}
	}
	
	public void viewData(View view){
		Spinner userSpinner = (Spinner) findViewById(R.id.user_spinner);
	    int spinnerPosition = userSpinner.getSelectedItemPosition();
        int spinnerUserID = userIDs.get(spinnerPosition);
        String spinnerUserNames = names.get(spinnerPosition);
		Intent intent = new Intent(this, UserSurveyActivity.class);
		intent.putExtra("spinnerUserID", spinnerUserID);
		intent.putExtra("spinnerUserName", spinnerUserNames);
		startActivity(intent);
	}

}
