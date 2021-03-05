package com.electrosim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		View continueButton = this.findViewById(R.id.new_button);
		continueButton.setOnClickListener(this);
		View newButton = this.findViewById(R.id.save_button);
		newButton.setOnClickListener(this);
		View aboutButton = this.findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = this.findViewById(R.id.create_button);
		exitButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;

		case R.id.new_button:
			Intent a = new Intent(this, Help.class);
			startActivity(a);
			break;

		case R.id.save_button:
			Intent b = new Intent(this, GridViewActivity.class);
			startActivity(b);
			break;

		case R.id.create_button:
			Intent c = new Intent(this, CircuitDrawer.class);
			startActivity(c);
			break;

		}
	}

}
