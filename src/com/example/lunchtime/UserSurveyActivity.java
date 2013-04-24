/*package com.example.lunchtime;

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

public class UserSurveyActivity extends MainScreen {

	ArrayList<String> dateData = new ArrayList<String>();
	ArrayList<Integer> questionData = new ArrayList<Integer>();
	ArrayList<String> correctData = new ArrayList<String>();
	ArrayList<String> surveyData = new ArrayList<String>();
	int userID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_survey);

        Intent intent = getIntent();
    	userID = intent.getIntExtra("spinnerUserID", -1);
    	
    	new LoadSurveyDataTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_survey, menu);
		return true;
	}
	
	class LoadSurveyDataTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadData();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {

		    ListView surveyList = (ListView) findViewById(R.id.survey_list);
	    	surveyList.setAdapter(new SurveyAdapter());
		}
		
	}
	
	public void loadData(){
		Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerSurvey WHERE playerID = " + Integer.toString(userID) + " ORDER BY date", null);

		if (result != null){
			while (result.moveToNext()) {
				dateData.add(result.getString(0));
				questionData.add(result.getInt(2));
				correctData.add(result.getString(3));
				surveyData.add(result.getString(4));
			}
		
		}
	}

	class SurveyAdapter extends ArrayAdapter<String> {
		SurveyAdapter() {
    		super(UserSurveyActivity.this, R.layout.time_row, R.id.date_time, dateData);
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row=super.getView(position, convertView, parent);
    	
    		TextView questionTime = (TextView) row.findViewById(R.id.question_time);
    		questionTime.setText("Question " + questionData.get(position).toString() + ", " + correctData.get(position) +", " + surveyData.get(position));
    		
    		TextView dateTime = (TextView) row.findViewById(R.id.date_time);
    		dateTime.setText("Date: " + dateData.get(position));
    		
    		return (row);
    		
    		
    	}
    }

}*/package com.example.lunchtime;

import java.util.ArrayList;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UserSurveyActivity extends MainScreen {

	ArrayList<String> dateData = new ArrayList<String>();
	ArrayList<String> surveyData = new ArrayList<String>();
	ArrayList<Integer> questionData = new ArrayList<Integer>();
	ArrayList<String> correctData = new ArrayList<String>();
	ArrayList<Integer> timeData = new ArrayList<Integer>();
	int userID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Intent intent = getIntent();
    	userID = intent.getIntExtra("spinnerUserID", -1);
    	String userName = intent.getStringExtra("spinnerUserName");
    	
		setTitle(userName);
		setContentView(R.layout.activity_user_survey);
    	dateData.add("0");
    	surveyData.add("0");
    	questionData.add(0);
    	correctData.add("First");
    	timeData.add(0);

		new LoadTimeDataTask().execute();
    	
    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_survey, menu);
		return true;
	}

	class LoadTimeDataTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadTimeData();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {

			new LoadSurveyDataTask().execute();
		}
		
	}
	
	public void loadTimeData(){
		Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerTime WHERE playerID = " + Integer.toString(userID) + " ORDER BY date", null);

		if (result != null){
			while (result.moveToNext()) {
				dateData.add(result.getString(0));
				correctData.add(result.getString(3));
				if(result.getString(3).equals("Incorrect"))
					questionData.add(result.getInt(2)+1);
				else

					questionData.add(result.getInt(2));
				timeData.add(result.getInt(4));
			}
		
		}
	}

	class LoadSurveyDataTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadSurveyData();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {

		    ListView surveyList = (ListView) findViewById(R.id.survey_list);
	    	surveyList.setAdapter(new DataAdapter());
		}
		
	}
	
	public void loadSurveyData(){
		Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerSurvey WHERE playerID = " + Integer.toString(userID) + " ORDER BY date", null);

		int i = 1;
		if (result != null){
			int questionNumber = 0;
			String surveyResponse = "";
			String surveyDate = "";
			boolean sameQuestion;
			while (result.moveToNext()) {
				sameQuestion = true;
				questionNumber = result.getInt(2);
				surveyResponse = result.getString(4);
				surveyDate = result.getString(0);
				
				while (sameQuestion&&i<questionData.size()){
					if(questionNumber > questionData.get(i)){
						surveyData.add("-----");
						i++;
					}
					else if(questionNumber == questionData.get(i)){
						if(i==questionData.size()-1){
							surveyData.add(surveyResponse);
							sameQuestion = false;
							i++;
							
						}
						else if (questionNumber == questionData.get(i+1) && surveyDate.compareTo(dateData.get(i+1)) == 2){
							surveyData.add("-----");

							
							i++;
						}
						else{
							surveyData.add(surveyResponse);
							sameQuestion = false;
							i++;
						}
						
					}			
					else{
						sameQuestion = false;
					}
					//dateSurveyData.add(result.getString(0));
					//questionSurveyData.add(result.getInt(2));
					//surveySurveyData.add(result.getString(4));
					
				}
			}
		
		}
		while (i<questionData.size()) {
			surveyData.add("-----");
			i++;

		}
	}
	
	class DataAdapter extends ArrayAdapter<String> {
		DataAdapter() {
    		super(UserSurveyActivity.this, R.layout.data_row, R.id.question_correct, correctData);
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row=super.getView(position, convertView, parent);
    	
    		if (correctData.get(position) != "First"){
	    		TextView questionText=(TextView) row.findViewById(R.id.question_number);
	    		questionText.setText(questionData.get(position).toString());
	    		TextView correctText=(TextView) row.findViewById(R.id.question_correct);
	    		correctText.setText(correctData.get(position));
	    		TextView timeText = (TextView) row.findViewById(R.id.question_time);
	    		timeText.setText(Integer.toString(timeData.get(position)));
	    		TextView moodText=(TextView) row.findViewById(R.id.question_mood);
	    		moodText.setText(surveyData.get(position));
	    		return(row);
    		}
    		else{
	    		TextView questionText=(TextView) row.findViewById(R.id.question_number);
	    		questionText.setText("Question");
	    		TextView correctText=(TextView) row.findViewById(R.id.question_correct);
	    		correctText.setText("Answer");
	    		TextView timeText = (TextView) row.findViewById(R.id.question_time);
	    		timeText.setText("Time");
	    		TextView moodText=(TextView) row.findViewById(R.id.question_mood);
	    		moodText.setText("Mood");
	    		return(row);
    		}
    		
    		
    	}
    }
	
}
