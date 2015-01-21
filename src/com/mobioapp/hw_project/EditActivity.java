package com.mobioapp.hw_project;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.mobioapp.hw_project.utils.PasswordChecker;
import com.mobioapp.hw_project.utils.URLs;

public class EditActivity extends Activity implements OnClickListener {

	EditText name, address, phone, pass,email;
	ProgressDialog mProgress;
	String user_name, user_address, user_phone, user_password;
	String result;
	Button update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit);
		
		update = (Button)findViewById(R.id.profile_update);
		update.setOnClickListener(this);
		
		name = (EditText) findViewById(R.id.edit_name);
		address = (EditText) findViewById(R.id.edit_address);
		phone = (EditText) findViewById(R.id.edit_phone);
		pass = (EditText) findViewById(R.id.edit_pass);
		email = (EditText) findViewById(R.id.edit_email);
		
		name.setText(User.Name);
		address.setText(User.Address);
		phone.setText(User.Phone);
		email.setText(User.Email);
		
	}

	public void getFormData() {

		user_name = name.getText().toString();
		user_address = address.getText().toString();
		// user_email = email.getText().toString();
		user_phone = phone.getText().toString();
		user_password = pass.getText().toString();

		if (user_name.length() > 0 && user_address.length() > 0
				&& user_password.length() > 0 && user_phone.length() > 0) {

			// String pass1 = password.getText().toString();
			if (!PasswordChecker.isValidPassword(user_password)) {
				pass.setError("Password length must be 6 digits");
				return;
			}

			//new EditTask().execute();
			doEdit();
		} else {

			Toast.makeText(getApplicationContext(), "Fill Required Info!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.profile_update:
			getFormData();
			break;

		default:
			break;
		}
	}
	
	private void doEdit(){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("id", User.Id);
		params.put("name", user_name);
		params.put("address", user_address);
		params.put("password", user_password);
		params.put("phone_no", user_phone);
		client.post (EditActivity.this,URLs.EDIT_URL,params, new AsyncHttpResponseHandler() {

		    @Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				mProgress.dismiss();
			}

			@Override
		    public void onStart() {
		        // called before request is started
				mProgress = new ProgressDialog(EditActivity.this);
				mProgress.setMessage("Updating...");
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

						Toast.makeText(getApplicationContext(),
								"Update Successfull!", Toast.LENGTH_SHORT).show();

						finish();

					}

					else {

						Toast.makeText(getApplicationContext(), "Update Failure!",
								Toast.LENGTH_SHORT).show();

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
		    	Toast.makeText(EditActivity.this, "Error!", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
	}

}
