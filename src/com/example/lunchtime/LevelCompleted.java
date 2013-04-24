package com.example.lunchtime;



import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelCompleted extends Activity
	implements MediaPlayer.OnCompletionListener {
	
	// variables storing information for one user
	// there is a clear progress button under the admin screen
	// have not implemented time tracking yet

	public final static String PLAYER_NAME = "playName";
	public final static String PLAYER_CURRENTQUESTION = "currentQuestion";
	public final static String REWARDS = "rewards";
	
	
	private MediaPlayer mpYouEarnNewReward;
	private int currentLevel = 0;
	
	private TextView displayName;
	private TextView displayLevel;
	
	private String playerName;
	private int currentQuestion;
	private ArrayList<String> rewardsPass = new ArrayList<String>();
	
	private ImageView item1;
	private ImageView item2;
	private ImageView item3;
	
	View unlockPopup = null ;
	
	private View button2;
	private View button3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_completed);
				
		button2 = (ImageView) findViewById(R.id.keepPlayingButton);
		button3 = (ImageView) findViewById(R.id.quitButton);
		//load user profile
		//load user progress
		displayName = (TextView)findViewById(R.id.level_completed_name);
		displayLevel = (TextView)findViewById(R.id.level_completed_level);
		
		item1 = (ImageView)findViewById(R.id.item1); 
		item2 = (ImageView)findViewById(R.id.item2); 
		item3 = (ImageView)findViewById(R.id.item3); 
		
		item1.setVisibility(View.INVISIBLE);
		item2.setVisibility(View.INVISIBLE);
		item3.setVisibility(View.INVISIBLE);
		
		playerName = getIntent().getStringExtra(PLAYER_NAME);
		displayName.setText("Good Work!" + " " + playerName);
		
		int level = getIntent().getIntExtra(PLAYER_CURRENTQUESTION, 1);
		displayLevel.setText("Level" + " " + Integer.toString(level) + " " + "Completed!");
		
		if(level == 5){
			button2.setBackgroundResource(R.drawable.button_lvlcomp_break);
			button3.setVisibility(View.INVISIBLE);
			button2.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	        		MainScreen.quit = true;
	        		finish();
	            }
	        });
		}
		
		rewardsPass =  getIntent().getStringArrayListExtra(REWARDS);
		int source1;
		int source2;
		int source3;
		if (rewardsPass.size()==1){
			source1 = helper((String)rewardsPass.get(0));
			item1.setBackgroundResource(source1);
			item1.setVisibility(View.VISIBLE);
		}
		else if(rewardsPass.size()==2){
			source1 = helper((String)rewardsPass.get(0));
			item1.setBackgroundResource(source1);
			item1.setVisibility(View.VISIBLE);
			
			source2 = helper((String)rewardsPass.get(1));
			item2.setBackgroundResource(source2);
			item2.setVisibility(View.VISIBLE);
		}
		else if(rewardsPass.size()==3){
			source1 = helper((String)rewardsPass.get(0));
			item1.setBackgroundResource(source1);
			item1.setVisibility(View.VISIBLE);
			
			source2 = helper((String)rewardsPass.get(1));
			item2.setBackgroundResource(source2);
			item2.setVisibility(View.VISIBLE);
			
			source3 = helper((String)rewardsPass.get(2));
			item3.setBackgroundResource(source3);
			item3.setVisibility(View.VISIBLE);
		}
		
		
		 mpYouEarnNewReward   = MediaPlayer.create(this, R.raw.youv_earned_a_new_reward);
		 mpYouEarnNewReward.setOnCompletionListener(this);   
		 mpYouEarnNewReward.start();	    
		
	}
	public void onCompletion(MediaPlayer mp) {
		try {
			mp.stop();
			mp.prepare();
			mp.seekTo(0);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int helper(String nameReward){
		
		if(nameReward.equals("bread")){
			
			return R.drawable.bread_icon;
			
		}
		else if(nameReward.equals("peanutbutter")){
			
			return R.drawable.peanutbutter_icon;
			
		}
		else if(nameReward.equals("jam")){
			
			return R.drawable.jam_icon;
			
		}
		else if(nameReward.equals("lettuce")){
			
			return R.drawable.lettuce_icon;
			
		}
		else if(nameReward.equals("tomato")){
			
			return R.drawable.tomato_icon;
			
		}
		else if(nameReward.equals("onion")){
			
			return R.drawable.onion_icon;
			
		}
		else if(nameReward.equals("ham")){
			
			return R.drawable.ham_icon;
			
		}
		else if(nameReward.equals("egg")){
			
			return R.drawable.egg_icon;
			
		}
		else if(nameReward.equals("cheese")){
			
			return R.drawable.cheese_icon;
			
		}
		else
			return 0;
		
	}
	
	public void goToRewards(View Button){
		
		startActivity(new Intent(this, RewardSectionActivity.class));
		
	}
	public void returnToGame(View Button){
		
		finish();
		
	}
	
	public void quitToMainScreen(View Button){
		MainScreen.quit = true;
		finish();
		
	}

}