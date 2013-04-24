package com.example.lunchtime;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdminScreen extends MainScreen {

	private TextView currentLvl;
	private TextView expScore;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_layout);

		currentLvl = (TextView) findViewById(R.id.level);

		expScore = (TextView) findViewById(R.id.experience);	
		
		currentLvl.setText(Integer.toString(getLvl()));
		expScore.setText(Double.toString(getLvl()));
	}
	

	public void clearProgress(View Button){
		
//		updateLvl(0);
//		updateExp(0);
		currentLvl.setText(Integer.toString(getLvl()));
		expScore.setText(Double.toString(getLvl()));
	}
}
