package com.example.lunchtime;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Kitchen extends MainScreen {

	private ImageView iamgeBread;
	private ImageView iamgeLettuce;
	private ImageView iamgeTomato;
	private ImageView iamgeChicken;
	private ImageView iamgeMayo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kitchen_layout);


		iamgeBread = (ImageView) findViewById(R.id.imagebread);
		iamgeLettuce = (ImageView) findViewById(R.id.imagelettuce);
		iamgeTomato = (ImageView) findViewById(R.id.imagetomatos);
		iamgeChicken = (ImageView) findViewById(R.id.imagechicken);
		iamgeMayo = (ImageView) findViewById(R.id.imagemayo);
		

    	iamgeBread.setVisibility(View.INVISIBLE);
    	iamgeLettuce.setVisibility(View.INVISIBLE);
    	iamgeTomato.setVisibility(View.INVISIBLE);
    	iamgeChicken.setVisibility(View.INVISIBLE);
    	iamgeMayo.setVisibility(View.INVISIBLE);
		
		int currentLvl = getLvl();

    	switch (currentLvl) {
        case 1:
        	iamgeBread.setVisibility(View.VISIBLE);
            break;
        case 2:
        	iamgeBread.setVisibility(View.VISIBLE);
        	iamgeLettuce.setVisibility(View.VISIBLE);
            break;
        case 3:
        	iamgeBread.setVisibility(View.VISIBLE);
        	iamgeLettuce.setVisibility(View.VISIBLE);
        	iamgeTomato.setVisibility(View.VISIBLE);
            break;
        case 4:
        	iamgeBread.setVisibility(View.VISIBLE);
        	iamgeLettuce.setVisibility(View.VISIBLE);
        	iamgeTomato.setVisibility(View.VISIBLE);
        	iamgeChicken.setVisibility(View.VISIBLE);
            break;
        case 5:
        	iamgeBread.setVisibility(View.VISIBLE);
        	iamgeLettuce.setVisibility(View.VISIBLE);
        	iamgeTomato.setVisibility(View.VISIBLE);
        	iamgeChicken.setVisibility(View.VISIBLE);
        	iamgeMayo.setVisibility(View.VISIBLE);
            break;
        default:
            break;
        }
	}
	
	
}
