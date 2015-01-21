package com.mobioapp.hw_project;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobioapp.hw_project.models.User;
import com.mobioapp.hw_project.utils.EmailChecker;
import com.mobioapp.hw_project.utils.PasswordChecker;
import com.mobioapp.hw_project.utils.URLs;

public class RegisterActivity extends Activity implements OnClickListener {

	Button submit;
	EditText name, location,email, phone, password;
	String user_name,  user_location, user_email,
			user_phone, user_password;
	String result;
	String jsonresult;
	String jsonstring;
	String json;
	String send_jsonstring;
	ProgressDialog mProgress;
	String user;
	List<NameValuePair> nameValuePairs;
	boolean server;
	SharedPreferences pref; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		pref = getApplicationContext()
				.getSharedPreferences("MyPref", 0); 
		
		name = (EditText) findViewById(R.id.editgName);

		location = (EditText) findViewById(R.id.editglocation);

		email = (EditText) findViewById(R.id.editgemail);
		phone = (EditText) findViewById(R.id.editgphone);
		password = (EditText) findViewById(R.id.editgpass);
		submit = (Button) findViewById(R.id.reg_submit);
		submit.setOnClickListener(this);

	}

	public void getFormData() {

		user_name = name.getText().toString();
		user_location = location.getText().toString();
		user_email = email.getText().toString();
		user_phone = phone.getText().toString();
		user_password = password.getText().toString();

		if (user_name.length() > 0 && user_location.length() > 0
				&& user_email.length() > 0 && user_password.length() > 0
				&& user_phone.length() > 0) {

			// String email1 = email.getText().toString();
			if (!EmailChecker.isValidEmail(user_email)) {
				email.setError("Invalid Email");
				return;
			}

			// String pass1 = password.getText().toString();
			if (!PasswordChecker.isValidPassword(user_password)) {
				password.setError("Password length must be 6 digits");
				return;
			}

			//new RegisterTask().execute();
			doRegister();
		} else {

			Toast.makeText(getApplicationContext(), "Fill Required Info!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reg_submit:
			getFormData();
			break;

		default:
			break;
		}
	}

	private void doRegister(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("name", user_name);
		params.put("address", user_location);
		params.put("email", user_email);
		params.put("password", user_password);
		params.put("phone_no", user_phone);
		client.post (RegisterActivity.this,URLs.REGISTRATION_URL,params, new AsyncHttpResponseHandler() {

		    @Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				mProgress.dismiss();
			}

			@Override
		    public void onStart() {
		        // called before request is started
				mProgress = new ProgressDialog(RegisterActivity.this);
				mProgress.setMessage("Registering...");
				mProgress.show();
		    }

				
		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		        // called when response HTTP status is "200 OK"
		    	try {
					JSONObject jsonResult = new JSONObject(new String(response));

					System.out.println("Return JSON Object: "
							+ jsonResult.toString());

					String msg = jsonResult.getString("message");

					if (msg.equals("success")) {

						JSONObject user = jsonResult.getJSONObject("user_details");
						System.out.println("Success JSON: " + user.toString());
						
						User.Id = user.getString("id");
						User.Name = user.getString("name");
						User.Address = user.getString("address");					
						User.Email = user.getString("email");
						User.Phone = user.getString("phone_no");
						
						
						Editor editor = pref.edit();
						editor.putString("user_id", user.getString("id"));
						// Storing
						editor.putBoolean("log_track", true); // string
						editor.commit(); // commit changes

						Toast.makeText(getApplicationContext(),
								"Registration Successfull!", Toast.LENGTH_SHORT)
								.show();

						Intent intent = new Intent(RegisterActivity.this,
								ProfileActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);


					

					}

					else {

						Toast.makeText(getApplicationContext(),
								"Registration Failure!", Toast.LENGTH_SHORT).show();

					}

				} catch (Exception e) {

					Toast.makeText(getApplicationContext(), "Error!",
							Toast.LENGTH_SHORT).show();
					System.out.println("JSON Parse Error: " + result);
				}
		    	
		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    	Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
	}


}
