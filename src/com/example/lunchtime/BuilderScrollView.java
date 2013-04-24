package com.example.lunchtime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class BuilderScrollView extends HorizontalScrollView {
	float scrollX = -1;
	float scrollY = -1;
	

    public BuilderScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BuilderScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BuilderScrollView(Context context) {
		super(context);
	}

    private boolean insideView = true;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN){
			insideView = true;
			scrollX = x;
			scrollY = y;
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP){
			scrollX = -1;
			scrollY = -1;
		}
		
    	if (insideView){
    		if (y>this.getHeight()){
            	insideView = false;
            	return false;
    		}
    		else{
        		return super.onTouchEvent(event);
    		}
    	}
    	else{
    		return false;
    	}
    	
    }
}
