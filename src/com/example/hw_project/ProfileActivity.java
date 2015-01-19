package com.example.hw_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity implements OnClickListener{

	private Button share;
	private TextView name, address,email,phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_view);
		
		
		name = (TextView)findViewById(R.id.name_textView1);
		address = (TextView)findViewById(R.id.address_textView2);
		email = (TextView)findViewById(R.id.email_textView3);
		phone = (TextView)findViewById(R.id.phone_textView4);
		
		share = (Button)findViewById(R.id.profile_share);
		share.setOnClickListener(this);
		
		try{
			name.setText(User.Name);
			address.setText(User.Address);
			email.setText(User.Email);
			phone.setText(User.Phone);
		}catch(Exception e){
			
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.profile_share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "Code Warriors' Challenge - 2015");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			break;
		case R.id.profile_edit:
			startActivity(new Intent(ProfileActivity.this, EditActivity.class));
			break;

		default:
			break;
		}
	}

}
