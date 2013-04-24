package com.example.lunchtime;

import java.util.ArrayList;


import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserTimeActivity extends MainScreen {

	ArrayList<String> dateData = new ArrayList<String>();
	ArrayList<Integer> questionData = new ArrayList<Integer>();
	ArrayList<String> correctData = new ArrayList<String>();
	ArrayList<Integer> timeData = new ArrayList<Integer>();
	int userID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_time);

        Intent intent = getIntent();
    	userID = intent.getIntExtra("spinnerUserID", -1);

		new LoadTimeDataTask().execute();
    	
    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_time, menu);
		return true;
	}

	class LoadTimeDataTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadData();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {

		    ListView timeList = (ListView) findViewById(R.id.time_list);
	    	timeList.setAdapter(new TimeAdapter());
		}
		
	}
	
	public void loadData(){
		Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerTime WHERE playerID = " + Integer.toString(userID) + " ORDER BY date", null);

		if (result != null){
			while (result.moveToNext()) {
				dateData.add(result.getString(0));
				questionData.add(result.getInt(2));
				correctData.add(result.getString(3));
				timeData.add(result.getInt(4));
			}
		
		}
	}

	class TimeAdapter extends ArrayAdapter<String> {
		TimeAdapter() {
    		super(UserTimeActivity.this, R.layout.time_row, R.id.date_time, dateData);
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row=super.getView(position, convertView, parent);
    	
    		TextView questionTime = (TextView) row.findViewById(R.id.question_time);
    		questionTime.setText("Question " + questionData.get(position).toString() + ", " + correctData.get(position) +", " + timeData.get(position).toString() + "s");
    		
    		TextView dateTime = (TextView) row.findViewById(R.id.date_time);
    		dateTime.setText("Date: " + dateData.get(position) );//+ ", ID:" + Integer.toString(userID));
    		
    		return (row);
    		
    		
    	}
    }
	
}
