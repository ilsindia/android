package com.crinis.solar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.crinis.db.DBaseHandler;
import com.crinis.util.AlertClass;
import com.crinis.util.ConnectionDetector;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText ed_no;
	private EditText ed_pasword;
	private Button but_login;
	private ConnectionDetector cd;
	private DBaseHandler dbh;
	private String status;
	private String message;
	private String ed_notxt;
	private String ed_paswordtxt;
	String result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity);
		getView();
		settingListner();
		dbh = new DBaseHandler(LoginActivity.this);
		cd = new ConnectionDetector(LoginActivity.this);
	}

	private void settingListner() {
		but_login.setOnClickListener(this);
	}

	private void getView() {
		ed_no = (EditText) findViewById(R.id.ed_no);
		ed_pasword = (EditText) findViewById(R.id.ed_pasword);
		but_login = (Button) findViewById(R.id.but_login);
	}

	public String postData(String phone,String pass) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://crinispower.com/crinis_web_services/web_services/user_login.php");
		HttpResponse response = null;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			// List<NameValuePair> nameValuePairs1 = new
			// ArrayList<NameValuePair>(2);

			JSONObject jo = new JSONObject();
			try {
			//	jo.put("slcode", "aWxlYWRzeW5hcHNlMTA1fENSSU5JU0NPTlNUQU5U");
				jo.put("user_phone", phone);
                jo.put("criniscode", "aWxlYWRzeW5hcHNlMTA1fENSSU5JU0NPTlNUQU5U");
                jo.put("user_password",pass);
                jo.put("cmd", "test_user_login");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			nameValuePairs.add(new BasicNameValuePair("data", Base64
					.encodeToString(jo.toString().getBytes(), Base64.URL_SAFE
							| Base64.NO_WRAP)));

			// String source="name"+":"+uname+" ,";
			// String
			// ret="data"+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
			// return ret;

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);

			InputStream inputStream = response.getEntity().getContent();
			// String responseBody = EntityUtils.toString(response.getEntity());
			// String status = response.getStatusLine().toString();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				// byte[] bytes = Base64.decode(bufferedStrChunk,
				// Base64.DEFAULT);
				// String string = new String(bytes, "UTF-8");
				stringBuilder.append(bufferedStrChunk);
				String encodeRes = stringBuilder.toString();
				try {
					JSONObject json = new JSONObject(encodeRes);
					String responsejosn = json.getString("res");

					byte[] bytes = Base64.decode(responsejosn, Base64.DEFAULT);
					String string1 = new String(bytes, "UTF-8");
					JSONObject json2 = new JSONObject(string1);
					status = json2.getString("status");
					message = json2.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// String currentStr1;
				// String[] currentStr ;
				// String[] separated = encodeRes.split(":");
				// currentStr= separated[1].split("''");
				// for(int k=0;k<currentStr.length;k++){
				// currentStr1 = currentStr[k];
				// }
			}

			return status;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return " Error";
	}

	class SendData extends AsyncTask<Void, Void, String> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LoginActivity.this);
			pd.setMessage("Please wait..");
			pd.setCanceledOnTouchOutside(false);
			pd.show();

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {

			return postData(ed_no.getText().toString(),ed_pasword.getText().toString());
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				System.out.println(result);
				if (pd.isShowing()) {
					pd.dismiss();
				}

				if (result.equalsIgnoreCase("Success")) {
					// dbh.insertReg(uname, pno, latitude, longitude);
					// AlertClass.Alertmsg("Registration Successfull",
					// LoginActivity.this);
					
					SharedPreferences sharedpreferences = getSharedPreferences(
							"solarprefer", Context.MODE_PRIVATE);
					Editor editor = sharedpreferences.edit();
					editor.putString("phone_no", ed_notxt);
					editor.putString("password", ed_paswordtxt);
					editor.putString("status", "success");
					editor.commit();
					
					Intent sub = new Intent(LoginActivity.this,
							HomeActivity.class);
					startActivity(sub);

					LoginActivity.this.finish();
				} else if (result.equalsIgnoreCase("RequestError")) {
					 AlertClass
					 .Alertmsg(
					 "Invalid mobile no. or password.",
					 LoginActivity.this);
					
//					Intent sub = new Intent(LoginActivity.this,
//							HomeActivity.class);
//					startActivity(sub);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.but_login) {
			ed_notxt = ed_no.getText().toString().trim();
			ed_paswordtxt = ed_pasword.getText().toString().trim();
			loginValidation(ed_notxt,ed_paswordtxt);

		}

	}

	public String loginValidation(String ed_notxt,String ed_paswordtxt) {
		
		if (!ed_notxt.equalsIgnoreCase("")
				&& !ed_paswordtxt.equalsIgnoreCase("")) {

		if (ed_notxt.length() >= 10 && ed_notxt.length() <= 13) {
		

				
				

				if (cd.isConnectingToInternet()) {

					new SendData().execute();

				} else {
					AlertClass.Alertmsg(
							"Please check your internet connection.",
							LoginActivity.this);
					result = "Please check your internet connection.";
				}
			
		} else {
			AlertClass.Alertmsg("Please enter valid 10 digit mobile Number ",
					LoginActivity.this);
			result = "Please enter valid 10 digit Mobile Number ";
		}
		
		} else {
			AlertClass.Alertmsg("Please fill all the fields",
					LoginActivity.this);
			result = "Please fill all the fields";
		}
		return result;
	}

}
