package com.example.buttonanimation;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextViewFranklin extends TextView {

	public MyTextViewFranklin(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyTextViewFranklin(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyTextViewFranklin(Context context) {
		super(context);
		init();
	}

	public void init() {

		Typeface tf = Init.getFont("franklinGothicDemi");
		setTypeface(tf ,1);

	}
}