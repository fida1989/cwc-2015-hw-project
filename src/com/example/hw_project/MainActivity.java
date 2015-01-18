package com.example.hw_project;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProgress = new ProgressDialog(MainActivity.this);
		initializeComponent();

	}

	private void initializeComponent() {
		// TODO Auto-generated method stub
		email = (EditText) findViewById(R.id.editloginEmail);
		pass = (EditText) findViewById(R.id.editloginPass);
		login = (Button) findViewById(R.id.loginoption);
		// logout = (Button) findViewById(R.id.logout);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFormData();
			}
		});
		register = (Button) findViewById(R.id.registeroption);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// register.setBackgroundColor(Color.CYAN);

				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);

			}
		});

	}

	public class RegisterTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mProgress.dismiss();
			try {
				JSONObject jsonResult = new JSONObject(result);

				System.out.println("Return JSON Object: "
						+ jsonResult.toString());

				String msg = jsonResult.getString("message");

				if (msg.equals("success")) {

					JSONObject user = jsonResult.getJSONObject("details");
					System.out.println("Success JSON: " + user.toString());
					// System.out.println("User ID: " + user.getString("id"));

					SharedPreferences pref = getApplicationContext()
							.getSharedPreferences("MyPref", 0); // 0 - for
																// private mode
					Editor editor = pref.edit();
					editor.putString("user_id", user.getString("id"));
					// Storing
					editor.putBoolean("log_track", true); // string
					editor.commit(); // commit changes

					Toast.makeText(getApplicationContext(),
							"Login Successful!", Toast.LENGTH_SHORT).show();

					/*
					 * Intent i = new Intent(LoginActivity.this,
					 * MainActivity.class); startActivity(i);
					 */
					//finish();
					email.setText("");
					pass.setText("");

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
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress.setMessage("Loging...");
			mProgress.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// name, email, phone, password
			/*
			 * json = createJSON(user_name, user_email, user_password,
			 * user_phone);
			 */

			try {

				// System.out.println(send_jsonstring);

				List<NameValuePair> reg_data = new ArrayList<NameValuePair>();
				reg_data.add(new BasicNameValuePair("email", user_email));

				reg_data.add(new BasicNameValuePair("password", user_pass));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://mobioapp.net/apps/bdcyclists/public/login_json");
				httppost.setEntity(new UrlEncodedFormEntity(reg_data));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				String htmlResponse = EntityUtils.toString(entity);
				result = htmlResponse;
				System.out.println("JSON Try: " + result);

			} catch (Exception e) {
				result = e.toString();
				System.out.println("JSON Catch: " + result);

			}

			return result;
		}

	}

	public void getFormData() {
		// name, email, phone, password
		user_email = email.getText().toString();
		user_pass = pass.getText().toString();

		if (user_email.length() > 0 && user_pass.length() > 0) {

			new RegisterTask().execute();
		} else {

			Toast.makeText(getApplicationContext(), "Fill Required Info!",
					Toast.LENGTH_SHORT).show();
		}
	}

}
