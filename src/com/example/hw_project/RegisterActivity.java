package com.example.hw_project;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import org.apache.james.mime4j.stream.NameValuePair;

public class RegisterActivity extends Activity {

	Button submit;
	EditText name, desc, location, fb, web, email, phone, password, image1;
	String user_name, user_desc, user_location, user_fb, user_web, user_email,
			user_phone, user_password, user_image;
	String result;
	String jsonresult;
	String jsonstring;
	String json;
	String send_jsonstring;
	ProgressDialog mProgress;
	String user;
	List<NameValuePair> nameValuePairs;
	boolean server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		mProgress = new ProgressDialog(RegisterActivity.this);
		initializeComponent();

	}

	private void initializeComponent() {
		name = (EditText) findViewById(R.id.editgName);

		location = (EditText) findViewById(R.id.editglocation);

		email = (EditText) findViewById(R.id.editgemail);
		phone = (EditText) findViewById(R.id.editgphone);
		password = (EditText) findViewById(R.id.editgpass);
		submit = (Button) findViewById(R.id.gsubmit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getFormData();

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

					Toast.makeText(getApplicationContext(),
							"Registration Successful!", Toast.LENGTH_SHORT)
							.show();

					final Intent intent = new Intent(RegisterActivity.this,
							MainActivity.class);

					Thread thread = new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep(2000); // As I am using LENGTH_LONG
													// in Toast
								startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					thread.start();

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
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress.setMessage("Registering...");
			mProgress.show();
		}

		@Override
		protected String doInBackground(String... params) {

			try {

				List<NameValuePair> reg_data = new ArrayList<NameValuePair>();
				reg_data.add(new BasicNameValuePair("name", user_name));
				reg_data.add(new BasicNameValuePair("desc", user_desc));
				reg_data.add(new BasicNameValuePair("location", user_location));
				reg_data.add(new BasicNameValuePair("email", user_email));
				reg_data.add(new BasicNameValuePair("password", user_password));
				reg_data.add(new BasicNameValuePair("phone", user_phone));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://mobioapp.net/apps/bdcyclists/public/group_signup_json");
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

		user_name = name.getText().toString();
		user_location = location.getText().toString();
		user_email = email.getText().toString();
		user_phone = phone.getText().toString();
		user_password = password.getText().toString();

		if (user_name.length() > 0 && user_location.length() > 0
				&& user_email.length() > 0 && user_password.length() > 0
				&& user_phone.length() > 0) {

			// String email1 = email.getText().toString();
			if (!isValidEmail(user_email)) {
				email.setError("Invalid Email");
				return;
			}

			// String pass1 = password.getText().toString();
			if (!isValidPassword(user_password)) {
				password.setError("Password length must be 6 digits");
				return;
			}

			new RegisterTask().execute();
		} else {

			Toast.makeText(getApplicationContext(), "Fill Required Info!",
					Toast.LENGTH_LONG).show();
		}
	}

	private boolean isValidPassword(String pass1) {
		if (pass1 != null && pass1.length() > 5) {
			return true;
		}
		return false;
	}

	private boolean isValidEmail(String email1) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email1);
		return matcher.matches();
	}

}
