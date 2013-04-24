package com.example.lunchtime;

import android.os.Bundle;
import android.content.Intent;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AdminSettingsActivity extends MainScreen {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_settings);
		
		Time date = new Time();
		date.setToNow();
		TextView timeText = (TextView) findViewById(R.id.temptimeview);
		timeText.setText(date.format("%Y/%m/%d-%H:%M:%S"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_admin_settings, menu);
		return true;
	}
	
	
	public void goToUserManagement(View Button){
		startActivity(new Intent(this, UserManagementActivity.class));
		
	}
	
	public void goToUserData(View Button){
		startActivity(new Intent(this, UserDataActivity.class));
		
	}

}
