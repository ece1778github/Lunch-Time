package com.example.lunchtime;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME="lunchtime.db";
	private static final int SCHEMA=1;
	static final String NAME="name";
	static final String PLAYERTABLE="playerInfo";
	static final String REWARDTABLE="rewardInfo";
	static final String QUESTIONTABLE="questionInfo";
	static final String PLAYERREWARDTABLE="playerRewards";
	static final String TIME="playerTime";
	static final String SURVEY="playerSurvey";
	
	public SQLiteDatabase myDataBase;
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("CREATE TABLE playerInfo (playerID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age INTEGER, gender VARCHAR, level INTEGER);");
			
			db.execSQL("CREATE TABLE rewardInfo (rewardID VARCHAR PRIMARY KEY);");
			db.execSQL("CREATE TABLE questionInfo (questionID INTEGER PRIMARY KEY, type VARCHAR, answer INTEGER, choiceOne INTEGER, choiceTwo INTEGER);");
			
			
			db.execSQL("CREATE TABLE playerRewards (playerID INTEGER, rewardID VARCHAR, " +
					   "FOREIGN KEY(playerID) REFERENCES playerInfo(playerID)" +
					   "FOREIGN KEY(rewardID) REFERENCES playerInfo(rewardID)" +
					   "PRIMARY KEY(playerID, rewardID));");
			
			db.execSQL("CREATE TABLE playerTime (date VARCHAR PRIMARY KEY, playerID INTEGER, questionID INTEGER, correct VARCHAR, answerTime INTEGER, " +
					   "FOREIGN KEY(playerID) REFERENCES playerInfo(playerID)" +
					   "FOREIGN KEY(questionID) REFERENCES playerInfo(questionID));");
			
			
			db.execSQL("CREATE TABLE playerSurvey (date VARCHAR PRIMARY KEY, playerID INTEGER, questionID INTEGER, correct VARCHAR, response VARCHAR, " +
					   "FOREIGN KEY(playerID) REFERENCES playerInfo(playerID)" +
					   "FOREIGN KEY(questionID) REFERENCES playerInfo(questionID));");
			
			
			ContentValues insertValues = new ContentValues();
			
			
			insertValues.put("questionID", 1);
			insertValues.put("type", "Build");
			insertValues.put("answer", 0);
			insertValues.put("choiceOne", 98764321);
			insertValues.put("choiceTwo", 121110);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 2);
			insertValues.put("type", "Build");
			insertValues.put("answer", 0);
			insertValues.put("choiceOne", 987654321);
			insertValues.put("choiceTwo", 1110);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 3);
			insertValues.put("type", "Build");
			insertValues.put("answer", 0);
			insertValues.put("choiceOne", 9865432);
			insertValues.put("choiceTwo", 121110);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 4);
			insertValues.put("type", "Build");
			insertValues.put("answer", 0);
			insertValues.put("choiceOne", 98764321);
			insertValues.put("choiceTwo", 1211);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 5);
			insertValues.put("type", "Build");
			insertValues.put("answer", 0);
			insertValues.put("choiceOne", 976531);
			insertValues.put("choiceTwo", 1210);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();
			
			insertValues.put("questionID", 6);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 800);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 7);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 300);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 8);
			insertValues.put("type", "Button");
			insertValues.put("answer", 100);
			insertValues.put("choiceOne", 300);
			insertValues.put("choiceTwo", 1200);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 9);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 500);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 10);
			insertValues.put("type", "Button");
			insertValues.put("answer", 600);
			insertValues.put("choiceOne", 500);
			insertValues.put("choiceTwo", 1200);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 11);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 315);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 12);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 730);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 13);
			insertValues.put("type", "Button");
			insertValues.put("answer", 230);
			insertValues.put("choiceOne", 130);
			insertValues.put("choiceTwo", 330);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 14);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 345);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 15);
			insertValues.put("type", "Button");
			insertValues.put("answer", 415);
			insertValues.put("choiceOne", 400);
			insertValues.put("choiceTwo", 430);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 16);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 505);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 17);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 810);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 18);
			insertValues.put("type", "Button");
			insertValues.put("answer", 120);
			insertValues.put("choiceOne", 125);
			insertValues.put("choiceTwo", 405);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 19);
			insertValues.put("type", "Button");
			insertValues.put("answer", 240);
			insertValues.put("choiceOne", 810);
			insertValues.put("choiceTwo", 813);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 20);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 750);
			insertValues.put("choiceOne", 355);
			insertValues.put("choiceTwo", 215);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 21);
			insertValues.put("type", "Button");
			insertValues.put("answer", 1205);
			insertValues.put("choiceOne", 1202);
			insertValues.put("choiceTwo", 100);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 22);
			insertValues.put("type", "Button");
			insertValues.put("answer", 356);
			insertValues.put("choiceOne", 1120);
			insertValues.put("choiceTwo", 400);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 23);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 725);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 24);
			insertValues.put("type", "Clock");
			insertValues.put("answer", 232);
			insertValues.put("choiceOne", 0);
			insertValues.put("choiceTwo", 0);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("questionID", 25);
			insertValues.put("type", "Button");
			insertValues.put("answer", 708);
			insertValues.put("choiceOne", 705);
			insertValues.put("choiceTwo", 240);
			db.insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "bread");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "cheese");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "egg");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();
			
			insertValues.put("rewardID", "ham");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "jam");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "lettuce");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "onion");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "peanutbutter");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();

			insertValues.put("rewardID", "tomato");
			db.insert(DatabaseHelper.REWARDTABLE, null, insertValues);
			insertValues.clear();
			
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
		}
	}
	
	
	DatabaseHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, SCHEMA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	    throw new RuntimeException("How did we get here?");
		
	}
}