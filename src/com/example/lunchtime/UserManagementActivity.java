package com.example.lunchtime;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class UserManagementActivity extends Activity {
	
	private TextView userid;

	ArrayList<Integer> userIDs = new ArrayList<Integer>();
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<Integer> ages = new ArrayList<Integer>();
	ArrayList<String> genders = new ArrayList<String>();
	ArrayList<Integer> levels = new ArrayList<Integer>();
	ListView userList;
	private DatabaseHelper db=null;
	String newUserName;
	int newUserAge;
	String newUserGender;
	Integer selectedUser;
	boolean viewData = false;
	boolean editData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_management);
	    db=new DatabaseHelper(getApplicationContext());
	    //userList = (ListView) getViewById(R.id.list);
	    userList = (ListView) findViewById(R.id.userList);
		new LoadUserTask().execute();

		OnItemClickListener clickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == names.size()-1){
					addUser();
				}
				else{
					selectedUser = position;
					selectUser();
				}
			}
		};
		userList.setOnItemClickListener(clickListener);
		
    	
	}
	
	@SuppressLint("NewApi")
	public void addUser(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Create a New User");
		alert.setMessage("Enter User Information");

		LinearLayout alertView = new LinearLayout(this);
		alertView.setOrientation(LinearLayout.VERTICAL);
		alertView.setGravity(Gravity.CENTER);
		
		final EditText userNameText = new EditText(this);
		userNameText.setHint("Name");
		alertView.addView(userNameText);

		final EditText userAgeText = new EditText(this);
		userAgeText.setHint("Age");
		userAgeText.setInputType(InputType.TYPE_CLASS_NUMBER);
		alertView.addView(userAgeText);
		
		RadioButton radioMale = new RadioButton(this);
		radioMale.setText("Male");
		radioMale.setId(201);
		RadioButton radioFemale = new RadioButton(this);
		radioFemale.setText("Female");
		radioFemale.setId(202);

		final RadioGroup userGenderRadio = new RadioGroup(this);
		userGenderRadio.addView(radioMale);
		userGenderRadio.addView(radioFemale);
		
		alertView.addView(userGenderRadio);
		
		alert.setView(alertView);
		
		alert.setNegativeButton("Add", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				newUserName = userNameText.getText().toString();
				String userAge = userAgeText.getText().toString();
				int selectedId = userGenderRadio.getCheckedRadioButtonId();
				if (selectedId == 201){
					newUserGender = "Male";
				}
				else if(selectedId == 202){
					newUserGender = "Female";
				}
				//String userGender = "";//((RadioButton) findViewById(userGenderRadio.getCheckedRadioButtonId())).getText().toString();
				if (newUserName.length() == 0 || userAge.length() == 0 || selectedId == -1){
					Toast.makeText(getApplicationContext(),  "No fields can be left blank", Toast.LENGTH_SHORT).show();
				}
				else{
					newUserAge = Integer.parseInt(userAge);
					new AddUserTask().execute();
				}
		  }
		});
		
		

		alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}
	
	@SuppressLint("NewApi")
	public void editUser(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Edit "+names.get(selectedUser));
		alert.setMessage("Edit User Information");

		LinearLayout alertView = new LinearLayout(this);
		alertView.setOrientation(LinearLayout.VERTICAL);
		alertView.setGravity(Gravity.CENTER);
		
		final EditText userNameText = new EditText(this);
		userNameText.setText(names.get(selectedUser));
		alertView.addView(userNameText);

		final EditText userAgeText = new EditText(this);
		userAgeText.setText(ages.get(selectedUser).toString());
		userAgeText.setInputType(InputType.TYPE_CLASS_NUMBER);
		alertView.addView(userAgeText);
		
		RadioButton radioMale = new RadioButton(this);
		radioMale.setText("Male");
		radioMale.setId(201);
		RadioButton radioFemale = new RadioButton(this);
		radioFemale.setText("Female");
		radioFemale.setId(202);

		final RadioGroup userGenderRadio = new RadioGroup(this);
		userGenderRadio.addView(radioMale);
		userGenderRadio.addView(radioFemale);
		if (genders.get(selectedUser).equals("Male")){
			userGenderRadio.check(201);
		}
		if (genders.get(selectedUser).equals("Female")){
			userGenderRadio.check(202);
		}
		
		alertView.addView(userGenderRadio);
		
		alert.setView(alertView);
		
		alert.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				newUserName = userNameText.getText().toString();
				String userAge = userAgeText.getText().toString();
				int selectedId = userGenderRadio.getCheckedRadioButtonId();
				if (selectedId == 201){
					newUserGender = "Male";
				}
				else if(selectedId == 202){
					newUserGender = "Female";
				}
				if (newUserName.length() == 0 || userAge.length() == 0 || selectedId == -1){
					Toast.makeText(getApplicationContext(),  "No fields can be left blank", Toast.LENGTH_SHORT).show();
				}
				else if(newUserName.equals(names.get(selectedUser)) && Integer.parseInt(userAge) == ages.get(selectedUser) && newUserGender.equals(genders.get(selectedUser))){
					Toast.makeText(getApplicationContext(),  "No fields were changed", Toast.LENGTH_SHORT).show();
					
				}
				else{
					newUserAge = Integer.parseInt(userAge);
					new EditUserTask().execute();
				}
				Intent intent = getIntent();
				finish();
				startActivity(intent);
		  }
		});
		
		

		alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = getIntent();
				finish();
				startActivity(intent);
		    // Canceled.
		  }
		});

		alert.show();
	}
	
	public void selectUser(){
		saveUser();
        FragmentManager fm = getFragmentManager();
        UserManagementDialog alert = new UserManagementDialog();
        alert.show(fm, "fragment_user_alert");
	}
	
	public class UserManagementDialog extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle(names.get(selectedUser));
			alert.setMessage("What would you like to do?");
			
			alert.setPositiveButton("Delete User", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					new DeleteUserTask().execute();
			  }
			});

			alert.setNeutralButton("View Data", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
				  viewData = true;
			  }
			});
			

			alert.setNegativeButton("Edit User", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
				  editData = true;
				  editUser();
			  }
			});
			//alert.setOnDismissListener(onDismissListener)
			

			//alert.show();
	        return alert.create();
	    }
	    
		@Override
		public void onDismiss(DialogInterface dialog) {
			if(viewData)
				viewData();
			else if (!editData){
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		}
	}
	
	public void saveUser(){
		MainScreen.currentUser = userIDs.get(selectedUser).intValue();
		MainScreen.currentQuestion = levels.get(selectedUser).intValue();
		MainScreen.playerName = names.get(selectedUser);
			
		FileOutputStream fos = null;
		try {
			fos = openFileOutput("CurrentUser.txt", Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.write(Integer.toString(MainScreen.currentUser).getBytes());

			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		userid = (TextView) findViewById(R.id.userid);
		userid.setText(Integer.toString(MainScreen.currentUser));
		Toast.makeText(getApplicationContext(),  "Current Player: "+names.get(selectedUser) , Toast.LENGTH_SHORT).show();
	}
	
	public void viewData(){
		viewData = false;
		Intent intent = getIntent();
		finish();
		startActivity(intent);
		intent = new Intent(this, UserSurveyActivity.class);
		intent.putExtra("spinnerUserID", MainScreen.currentUser);
		intent.putExtra("spinnerUserName", MainScreen.playerName);
		startActivity(intent);
		
	}
	
	class LoadUserTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			loadData();

			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {
	    	names.add("Add User");
	    	userList.setAdapter(new IconicAdapter());
		}
		
	}
	
	class AddUserTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			ContentValues insertValues = new ContentValues();
			
			insertValues.put("name", newUserName);
			insertValues.put("age", newUserAge);
			insertValues.put("gender", newUserGender);
			insertValues.put("level", 1);
			db.getWritableDatabase().insert(DatabaseHelper.PLAYERTABLE, null, insertValues);
			insertValues.clear();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {

			Toast.makeText(getApplicationContext(),  "Added User:\nName: " + newUserName 
					+ "\nAge: " + newUserAge
					+ "\nGender: " + newUserGender, Toast.LENGTH_SHORT)
				.show();
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		
	}
	
	class EditUserTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			ContentValues editValues = new ContentValues();
			
			editValues.put("name", newUserName);
			editValues.put("age", newUserAge);
			editValues.put("gender", newUserGender);
			String editId[] = {userIDs.get(selectedUser).toString()};
			
			db.getWritableDatabase().update(DatabaseHelper.PLAYERTABLE, editValues, "playerID = ?", editId);
			
			
			//.insert(DatabaseHelper.PLAYERTABLE, null, insertValues);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused) {

			Toast.makeText(getApplicationContext(),  "Edited User: \nName: " + newUserName 
					+ "\nAge: " + newUserAge
					+ "\nGender: " + newUserGender, Toast.LENGTH_SHORT)
				.show();
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		
	}
	
	class DeleteUserTask extends AsyncTask<Void, String, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			ContentValues insertValues = new ContentValues();
			
			String deleteId[] = {userIDs.get(selectedUser).toString()};
			db.getWritableDatabase().delete(DatabaseHelper.PLAYERTABLE, "playerID = ?", deleteId);
			insertValues.clear();
			return null;
			
		}
		
		@Override
		protected void onPostExecute(Void unused) {

			Toast.makeText(getApplicationContext(),  "Deleted " + names.get(selectedUser),Toast.LENGTH_SHORT).show();
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		
	}
	
	public void loadData(){
		Cursor result = db.getReadableDatabase().rawQuery("SELECT * FROM playerInfo", null);
		if (result != null){
			while (result.moveToNext()) {
				userIDs.add(result.getInt(0));
//				MainScreen.playerName = result.getString(1);				
				names.add(result.getString(1));
				ages.add(result.getInt(2));
				genders.add(result.getString(3));
//				MainScreen.currentQuestion = result.getInt(4);
				levels.add(result.getInt(4));
			}
		
		}
		

/*
		ContentValues insertValues = new ContentValues();
		insertValues.put("questionID", 26);
		insertValues.put("type", "Clock");
		insertValues.put("answer", 300);
		insertValues.put("choiceOne", 0);
		insertValues.put("choiceTwo", 0);
		db.getWritableDatabase().insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
		insertValues.clear();

		insertValues.put("questionID", 27);
		insertValues.put("type", "Clock");
		insertValues.put("answer", 730);
		insertValues.put("choiceOne", 0);
		insertValues.put("choiceTwo", 0);
		db.getWritableDatabase().insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
		insertValues.clear();

		insertValues.put("questionID", 28);
		insertValues.put("type", "Clock");
		insertValues.put("answer", 655);
		insertValues.put("choiceOne", 0);
		insertValues.put("choiceTwo", 0);
		db.getWritableDatabase().insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
		insertValues.clear();

		insertValues.put("questionID", 29);
		insertValues.put("type", "Clock");
		insertValues.put("answer", 225);
		insertValues.put("choiceOne", 0);
		insertValues.put("choiceTwo", 0);
		db.getWritableDatabase().insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
		insertValues.clear();

		insertValues.put("questionID", 30);
		insertValues.put("type", "Clock");
		insertValues.put("answer", 1046);
		insertValues.put("choiceOne", 0);
		insertValues.put("choiceTwo", 0);
		db.getWritableDatabase().insert(DatabaseHelper.QUESTIONTABLE, null, insertValues);
		insertValues.clear();*/
/*
		
		ContentValues insertValues = new ContentValues();
		insertValues.put("date", "2013/03/17-16:37:06");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 1);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 23);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:37:23");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 2);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 15);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:37:34");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 3);
		insertValues.put("correct", "Incorrect");
		insertValues.put("answerTime", 8);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:38:02");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 3);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 28);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:39:05");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 4);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 59);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:39:23");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 5);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 15);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();
		
		insertValues.put("date", "2013/03/17-16:37:35");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 3);
		insertValues.put("correct", "Correct");
		insertValues.put("response", "Sad");
		db.getWritableDatabase().insert(DatabaseHelper.SURVEY, null, insertValues);
		insertValues.clear();
		
		insertValues.put("date", "2013/03/17-16:39:29");
		insertValues.put("playerID", 4);
		insertValues.put("questionID", 5);
		insertValues.put("correct", "Correct");
		insertValues.put("response", "Happy");
		db.getWritableDatabase().insert(DatabaseHelper.SURVEY, null, insertValues);
		insertValues.clear();
		
		insertValues.put("date", "2013/03/17-16:42:06");
		insertValues.put("playerID", 5);
		insertValues.put("questionID", 1);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 23);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:42:23");
		insertValues.put("playerID", 5);
		insertValues.put("questionID", 2);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 15);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:42:34");
		insertValues.put("playerID", 5);
		insertValues.put("questionID", 3);
		insertValues.put("correct", "Correct");
		insertValues.put("answerTime", 8);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();

		insertValues.put("date", "2013/03/17-16:43:22");
		insertValues.put("playerID", 5);
		insertValues.put("questionID", 4);
		insertValues.put("correct", "Incorrect");
		insertValues.put("answerTime", 12);
		db.getWritableDatabase().insert(DatabaseHelper.TIME, null, insertValues);
		insertValues.clear();
		
		insertValues.put("date", "2013/03/17-16:42:44");
		insertValues.put("playerID", 5);
		insertValues.put("questionID", 3);
		insertValues.put("correct", "Correct");
		insertValues.put("response", "Bored");
		db.getWritableDatabase().insert(DatabaseHelper.SURVEY, null, insertValues);
		insertValues.clear();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_management, menu);
		return true;
	}
	
	
	class IconicAdapter extends ArrayAdapter<String> {
    	IconicAdapter() {
    		super(UserManagementActivity.this, R.layout.row, R.id.name, names);
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row=super.getView(position, convertView, parent);
    	
    		if (names.get(position) != "Add User"){
	    		TextView age=(TextView) row.findViewById(R.id.age);
	    		age.setText(String.format("Age:").concat(ages.get(position).toString()));
	    		TextView game=(TextView) row.findViewById(R.id.game);
	    		game.setText(String.format("Gender:").concat(genders.get(position)));
	    		TextView level = (TextView) row.findViewById(R.id.level);
	    		level.setText(String.format("Question:").concat(Integer.toString(levels.get(position)-1)));
	    		if(userIDs.get(position).intValue() == MainScreen.currentUser){
	    			ImageView check = (ImageView) row.findViewById(R.id.checkmark);
	    			check.setVisibility(ImageView.VISIBLE);
	    		}
	    		else{
	    			ImageView check = (ImageView) row.findViewById(R.id.checkmark);
	    			check.setVisibility(ImageView.INVISIBLE);
	    		}
	    		return(row);
    		}
    		else{
	    		TextView age=(TextView) row.findViewById(R.id.age);
	    		age.setText("");
	    		TextView game=(TextView) row.findViewById(R.id.game);
	    		game.setText("");
	    		TextView level = (TextView) row.findViewById(R.id.level);
	    		level.setText("");
    			ImageView check = (ImageView) row.findViewById(R.id.checkmark);
    			check.setVisibility(ImageView.INVISIBLE);
	    		return(row);
    		}
    		
    		
    	}
    }
	



}
