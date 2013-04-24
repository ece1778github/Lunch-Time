package com.example.lunchtime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgressChart extends Activity{

	private Context context;
	public final static String PLAYER_NAME = "playName";
	private TextView displayName;

	////Level Transition///////////
	private ImageView star1;
	private ImageView star2;
	private ImageView star3;
	private ImageView star4;
	private ImageView star5;
	private ImageView star6;
	private ImageView star7;
	private ImageView star8;
	private ImageView star9;
	private ImageView star10;
	private ImageView star11;
	private ImageView star12;
	private ImageView star13;
	private ImageView star14;
	private ImageView star15;
	private ImageView star16;
	private ImageView star17;
	private ImageView star18;
	private ImageView star19;
	private ImageView star20;
	private ImageView star21;
	private ImageView star22;
	private ImageView star23;
	private ImageView star24;
	private ImageView star25;
	////Level Transition Completed///////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progresschart);

		displayName = (TextView)findViewById(R.id.progresschart_name);

		String name = getIntent().getStringExtra(PLAYER_NAME);
		displayName.setText(MainScreen.playerName);
		////Progress Chart///////////
		 star1 = (ImageView) findViewById(R.id.star_pc1);
		 star2 = (ImageView) findViewById(R.id.star_pc2);
		 star3 = (ImageView) findViewById(R.id.star_pc3);
		 star4 = (ImageView) findViewById(R.id.star_pc4);
		 star5 = (ImageView) findViewById(R.id.star_pc5);
		 star6 = (ImageView) findViewById(R.id.star_pc6);
		 star7 = (ImageView) findViewById(R.id.star_pc7);
		 star8 = (ImageView) findViewById(R.id.star_pc8);
		 star9 = (ImageView) findViewById(R.id.star_pc9);
		 star10 = (ImageView) findViewById(R.id.star_pc10);
		 star11 = (ImageView) findViewById(R.id.star_pc11);
		 star12 = (ImageView) findViewById(R.id.star_pc12);
		 star13 = (ImageView) findViewById(R.id.star_pc13);
		 star14 = (ImageView) findViewById(R.id.star_pc14);
		 star15 = (ImageView) findViewById(R.id.star_pc15);
		 star16 = (ImageView) findViewById(R.id.star_pc16);
		 star17 = (ImageView) findViewById(R.id.star_pc17);
		 star18 = (ImageView) findViewById(R.id.star_pc18);
		 star19 = (ImageView) findViewById(R.id.star_pc19);
		 star20 = (ImageView) findViewById(R.id.star_pc20);
		 star21 = (ImageView) findViewById(R.id.star_pc21);
		 star22 = (ImageView) findViewById(R.id.star_pc22);
		 star23 = (ImageView) findViewById(R.id.star_pc23);
		 star24 = (ImageView) findViewById(R.id.star_pc24);
		 star25 = (ImageView) findViewById(R.id.star_pc25);
		//////////////////////////////
		
		
		
		setStars();
	}


	@Override
	protected void onResume() {
		super.onResume();
		context = this.getApplicationContext();
			Handler handler = new Handler(); 
		    handler.postDelayed(new Runnable() { 
		         public void run() { 

		        			startActivity(new Intent(context, GameMode.class));
		        			finish();
		         } 
		    }, 2000); 	  
	    
	}
	
	private void setStars(){
		
		switch(MainScreen.currentQuestion - 1){
		case 1:
			star1.setImageResource(R.drawable.star_filled);
			break;

		case 2:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			break;

		case 3:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			break;
		case 4:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			break;
		case 5:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			break;
		case 6:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			break;
		case 7:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			break;
		case 8:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			break;
		case 9:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			break;
		case 10:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			break;
		case 11:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			break;
		case 12:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			break;
		case 13:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			break;
		case 14:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			break;
		case 15:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			break;
		case 16:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			break;
		case 17:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			break;
		case 18:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			break;
		case 19:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			star19.setImageResource(R.drawable.star_filled);
			break;
		case 20:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			star19.setImageResource(R.drawable.star_filled);
			star20.setImageResource(R.drawable.star_filled);
			break;
		case 21:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			star19.setImageResource(R.drawable.star_filled);
			star20.setImageResource(R.drawable.star_filled);
			star21.setImageResource(R.drawable.star_filled);
			break;
		case 22:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			star19.setImageResource(R.drawable.star_filled);
			star20.setImageResource(R.drawable.star_filled);
			star21.setImageResource(R.drawable.star_filled);
			star22.setImageResource(R.drawable.star_filled);
			break;
		case 23:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			star19.setImageResource(R.drawable.star_filled);
			star20.setImageResource(R.drawable.star_filled);
			star21.setImageResource(R.drawable.star_filled);
			star22.setImageResource(R.drawable.star_filled);
			star23.setImageResource(R.drawable.star_filled);
			break;
		case 24:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			star19.setImageResource(R.drawable.star_filled);
			star20.setImageResource(R.drawable.star_filled);
			star21.setImageResource(R.drawable.star_filled);
			star22.setImageResource(R.drawable.star_filled);
			star23.setImageResource(R.drawable.star_filled);
			star24.setImageResource(R.drawable.star_filled);
			break;
		case 25:
			star1.setImageResource(R.drawable.star_filled);
			star2.setImageResource(R.drawable.star_filled);
			star3.setImageResource(R.drawable.star_filled);
			star4.setImageResource(R.drawable.star_filled);
			star5.setImageResource(R.drawable.star_filled);
			star6.setImageResource(R.drawable.star_filled);
			star7.setImageResource(R.drawable.star_filled);
			star8.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star9.setImageResource(R.drawable.star_filled);
			star11.setImageResource(R.drawable.star_filled);
			star10.setImageResource(R.drawable.star_filled);
			star12.setImageResource(R.drawable.star_filled);
			star13.setImageResource(R.drawable.star_filled);
			star14.setImageResource(R.drawable.star_filled);
			star15.setImageResource(R.drawable.star_filled);
			star16.setImageResource(R.drawable.star_filled);
			star17.setImageResource(R.drawable.star_filled);
			star18.setImageResource(R.drawable.star_filled);
			star19.setImageResource(R.drawable.star_filled);
			star20.setImageResource(R.drawable.star_filled);
			star21.setImageResource(R.drawable.star_filled);
			star22.setImageResource(R.drawable.star_filled);
			star23.setImageResource(R.drawable.star_filled);
			star24.setImageResource(R.drawable.star_filled);
			star25.setImageResource(R.drawable.star_filled);
			break;
		default:
			break;
		
		}
		
	}
}
