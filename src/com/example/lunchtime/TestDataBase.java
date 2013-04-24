package com.example.lunchtime;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

public class TestDataBase extends MainScreen {
	private TextView userid3;
	private TextView setQNum;
	private TextView setCorrectness;
	private TextView questionNumber;
	private TextView setTime;
	private TextView emotion;
	private TextView rewards;
	
	private int currentQuestion;
	private String correctness;
	private int timeForResponse;
	private String emotionState;
	private String rewardData;

	private DatabaseHelper db=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_db);
		
		userid3 		= (TextView)findViewById(R.id.userid3);
		questionNumber 	= (TextView)findViewById(R.id.questionNumber);
		setQNum 		= (TextView)findViewById(R.id.setQNum);
		setCorrectness	= (TextView)findViewById(R.id.setCorrectness);
		questionNumber 	= (TextView)findViewById(R.id.questionNumber);
		setTime 		= (TextView)findViewById(R.id.setTime);
		emotion 		= (TextView)findViewById(R.id.emotion);
		rewards 		= (TextView)findViewById(R.id.rewards);

	    db=new DatabaseHelper(getApplicationContext());
	}
	@Override
	protected void onResume() {
		super.onResume();
		//load user profile
		//load user progress
		 if(userid3!=null)
			 userid3.setText(Integer.toString(MainScreen.currentUser));

		/******************/
//		db=new DatabaseHelper(getApplicationContext());
		
		
	}

	public void getQuestion(View view){
		
		new GetUserQuestionTask().execute();
	}
	
	public void setQuestion(View view){
		
		String qNum = setQNum.getText().toString();
		currentQuestion = Integer.parseInt(qNum);
		new SetUserQuestionTask().execute();
	}
	
	public void addTimeData(View view){

		String correctEntry = setCorrectness.getText().toString();
		String timeEntry = setTime.getText().toString();
		
		correctness = correctEntry;
		timeForResponse = Integer.parseInt(timeEntry); 
		new AddUserTimeData().execute();
		
	}
	
	public void addEmotion(View view){

		String emotionEntry = emotion.getText().toString();
		emotionState = emotionEntry;
		new AddUserSurveyData().execute();
	}
	
	
	public void addReward(View view){
		
		String rewardEntry = rewards.getText().toString();
		rewardData = rewardEntry;
		new AddPlayerRewardData().execute();
	}
	
	//////////////////////////////////////////////////////////////
	//retrieves the level (question number) of the current user
	class GetUserQuestionTask extends AsyncTask<Void, String, Void> {
	    @Override
	    protected Void doInBackground(Void... unused) {
	        Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerInfo WHERE playerID = " + Integer.toString(currentUser), null);
	        
	        if (result != null)
	            while (result.moveToNext())
	                currentQuestion = result.getInt(4);
	        
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void unused) {
			questionNumber.setText(Integer.toString(currentQuestion));
	    }
	    
	}

	//sets the level (question number) of the current user
	class SetUserQuestionTask extends AsyncTask<Void, String, Void> {
	    @Override
	    protected Void doInBackground(Void... unused) {
	        ContentValues editValues = new ContentValues();
	        
	        editValues.put("level", currentQuestion);
	        String editId[] = {Integer.toString(currentUser)};
	        
	        db.getWritableDatabase().update(DatabaseHelper.PLAYERTABLE, editValues, "playerID = ?", editId);
	        
	        
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void unused) {
	    }
	    
	}

	//retrieves the date and time
	String getDate(){
	    Time date = new Time();
	    date.setToNow();
	    String currentDate = date.format("%Y/%m/%d-%H:%M:%S");
	    return currentDate;
	}

	//adds a time value to the database
	//increment currentQuestion after this function and the next
	//limit?
	class AddUserTimeData extends AsyncTask<Void, String, Void> {
	    @Override
	    protected Void doInBackground(Void... unused) {
	        
	        ContentValues insertValues = new ContentValues();
	        insertValues.put("date", getDate());
	        insertValues.put("playerID", currentUser);
	        insertValues.put("questionID", currentQuestion);
	        insertValues.put("correct", correctness);//put "Correct" or "Incorrect"
	        insertValues.put("answerTime", timeForResponse);//put time as an integer
	        db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
	        
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void unused) {
	    }
	    
	}

	//adds a survey value to the database
	class AddUserSurveyData extends AsyncTask<Void, String, Void> {
	    @Override
	    protected Void doInBackground(Void... unused) {
	        
	        ContentValues insertValues = new ContentValues();
	        insertValues.put("date", getDate());
	        insertValues.put("playerID", currentUser);
	        insertValues.put("questionID", currentQuestion);
	        insertValues.put("correct", "Start");//put "Correct" or "Incorrect"
	        insertValues.put("response", emotionState);//put "Happy", "Bored" or "Sad"
	        db.getWritableDatabase().insert(DatabaseHelper.SURVEY, null, insertValues);
	        
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void unused) {
	    }
	    
	}

	//adds a reward to the player's inventory
	class AddPlayerRewardData extends AsyncTask<Void, String, Void> {
	    @Override
	    protected Void doInBackground(Void ... unused) {
	        
	        ContentValues insertValues = new ContentValues();
	        insertValues.put("playerID", currentUser);
	        insertValues.put("rewardID", rewardData);//put reward name(see list in reward database
	        db.getWritableDatabase().insert(DatabaseHelper.PLAYERREWARDTABLE, null, insertValues);
	        
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void unused) {
	    }
	    
	}
}
