package com.example.hw_project;

import org.apache.http.Header;
import org.json.JSONObject;

import com.example.hw_project.models.User;
import com.example.hw_project.utils.EmailChecker;
import com.example.hw_project.utils.PasswordChecker;
import com.example.hw_project.utils.URLs;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Button register;
	Button login;
	Button logout;
	EditText email, pass;
	String user_email, user_pass;
	String result;
	String jsonresult;
	String jsonstring;
	String json;
	String send_jsonstring;
	ProgressDialog mProgress;
	LinearLayout lin;
	SharedPreferences pref; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pref = getApplicationContext()
				.getSharedPreferences("MyPref", 0); 
		
		email = (EditText) findViewById(R.id.editloginEmail);
		pass = (EditText) findViewById(R.id.editloginPass);
		
		login = (Button) findViewById(R.id.loginoption);
		login.setOnClickListener(this);
		
		register = (Button) findViewById(R.id.registeroption);
		register.setOnClickListener(this);
		
		
		
		if(pref.getBoolean("log_track", false)){
			Toast.makeText(getApplicationContext(),
					"Already Logged In!",
					Toast.LENGTH_LONG).show();
			
			User.Id = pref.getString("user_id","");
			
			Intent intent = new Intent(MainActivity.this,
					ProfileActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
		

	}
	
	private void doLogin(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("email", user_email);
		params.put("password", user_pass);
		client.post (MainActivity.this,URLs.LOGIN_URL,params, new AsyncHttpResponseHandler() {

		    @Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				mProgress.dismiss();
			}

			@Override
		    public void onStart() {
		        // called before request is started
				mProgress = new ProgressDialog(MainActivity.this);
				mProgress.setMessage("Logging  In...");
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
					
					System.out.println("Message: "
							+ msg);

					if (msg.equals("success")) {

						JSONObject user = jsonResult.getJSONObject("user_details");
						System.out.println("Success JSON: " + user.toString());
						// System.out.println("User ID: " + user.getString("id"));
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
								"Login Successful!", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(MainActivity.this,
								ProfileActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);

					}

					else {

						Toast.makeText(getApplicationContext(),
								"Login Failure! Check username/password!",
								Toast.LENGTH_LONG).show();

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
		    	Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
	}



	public void getFormData() {
		user_email = email.getText().toString().trim();
		user_pass = pass.getText().toString().trim();

		if (user_email.length() > 0 && user_pass.length() > 0) {
			if(EmailChecker.isValidEmail(user_email)&&PasswordChecker.isValidPassword(user_pass)){
				//Toast.makeText(MainActivity.this, user_email +":::"+user_pass,Toast.LENGTH_SHORT).show();
				//new LoginTask().execute();
				doLogin();
			}else{
				Toast.makeText(MainActivity.this, "Enter Valid Email!",Toast.LENGTH_SHORT).show();
			}				
			
		} else {

			Toast.makeText(getApplicationContext(), "Fill Required Info!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.loginoption:
			getFormData();
			break;
		case R.id.registeroption:
			Intent i = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

}
