package com.crinis.solar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crinis.gps.GPSTracker;
import com.crinis.util.AlertClass;
import com.crinis.util.ConnectionDetector;

public class SolarAnalysisActivity extends Fragment implements OnClickListener {

	private ConnectionDetector cd;
	private GPSTracker gps;
	private String latitude = "";
	private String longitude = "";
	private EditText Dcsys;
	private EditText Azimuth;
	private Spinner ModTyp;
	private EditText Sysloss;
	private EditText Tilt;
	private TextView Latitude;
	private TextView Longitude;
	private Button but_done;
	private Button but_editloc;
	private Context context;
	private Spinner Arytyp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.solaranalysisactivity,
				container, false);
		context = getActivity().getApplicationContext();
		getView(rootView);
		setadper();
		but_done.setOnClickListener(this);
		but_editloc.setOnClickListener(this);
		cd = new ConnectionDetector(getActivity().getApplicationContext());

		if (cd.isConnectingToInternet()) {
			gps = new GPSTracker(getActivity());

			// check if GPS enabled
			if (gps.canGetLocation()) {

				latitude = String.valueOf(gps.getLatitude());
				longitude = String.valueOf(gps.getLongitude());
				// \n is for new line
				Latitude.setText(latitude);
				Longitude.setText(longitude);
				Tilt.setText(String.valueOf((int) (Float.parseFloat(latitude))));
				// Toast.makeText(
				// getActivity().getApplicationContext(),
				// "Your Location is - \nLat: " + latitude + "\nLong: "
				// + longitude, Toast.LENGTH_LONG).show();
			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();
			}
		}

		return rootView;
	}

	@Override
	public void onResume() {
		//
		// String latitude = getArguments().getString("latitude");
		// // String longitude = ;
		// if (getArguments().getString("latitude")!=null &&
		// getArguments().getString("latitude")!=null) {
		// Latitude.setText(latitude);
		// Longitude.setText(longitude);
		// }
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		super.onResume();
		try {
			Bundle args = this.getArguments();
			if (args != null
					&& (!args.getString("latitude").equalsIgnoreCase("0.0") || !args
							.getString("longitude").equalsIgnoreCase("0.0"))) {

				latitude = args.getString("latitude");
				longitude = args.getString("longitude");
				Tilt.setText(String.valueOf((int) (Float.parseFloat(latitude))));
				Latitude.setText(latitude);
				Longitude.setText(longitude);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setadper() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.module_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ModTyp.setAdapter(adapter);

		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				getActivity(), R.array.type_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		Arytyp.setAdapter(adapter1);

	}

	private void getView(View rootView) {
		// TODO Auto-generated method stub
		Dcsys = (EditText) rootView.findViewById(R.id.Dcsys);
		Azimuth = (EditText) rootView.findViewById(R.id.Azimuth);
		ModTyp = (Spinner) rootView.findViewById(R.id.ModTyp);
		Arytyp = (Spinner) rootView.findViewById(R.id.Arytyp);
		Sysloss = (EditText) rootView.findViewById(R.id.Sysloss);
		Tilt = (EditText) rootView.findViewById(R.id.Tilt);
		Latitude = (TextView) rootView.findViewById(R.id.Latitude);
		Longitude = (TextView) rootView.findViewById(R.id.Longitude);
		but_done = (Button) rootView.findViewById(R.id.but_done);
		but_editloc = (Button) rootView.findViewById(R.id.but_editloc);

		Sysloss.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Dcsys.setSelection(0);
			}
		});
		Tilt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Sysloss.setSelection(0);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_editloc:
			if (cd.isConnectingToInternet()) {
				Intent ma = new Intent(getActivity(), MapActivity.class);
				ma.putExtra("latitude", Latitude.getText().toString());

				ma.putExtra("longitude", Longitude.getText().toString());
				startActivity(ma);
				getActivity().finish();
			} else {
				AlertClass.Alertmsg("Please check your internet connection",
						getActivity());
			}
			break;
		case R.id.but_done:
			if (cd.isConnectingToInternet()) {
				String Dcsystxt = Dcsys.getText().toString().trim();

				String Azimuthtxt = Azimuth.getText().toString().trim();
				String Syslosstxt = Sysloss.getText().toString().trim();

				String Tilttxt = Tilt.getText().toString().trim();
				String ModTyptxt = String.valueOf(ModTyp
						.getSelectedItemPosition());
				if (ModTyptxt.equalsIgnoreCase("0")) {
					ModTyptxt = "1";
				} else if (ModTyptxt.equalsIgnoreCase("1")) {
					ModTyptxt = "0";
				}

				String Arytyptxt = String.valueOf(Arytyp
						.getSelectedItemPosition());
				if (!Dcsystxt.equalsIgnoreCase("")
						&& !Azimuthtxt.equalsIgnoreCase("")
						&& !Syslosstxt.equalsIgnoreCase("")
						&& !Tilttxt.equalsIgnoreCase("")
						&& !ModTyptxt.equalsIgnoreCase("")
						&& !Arytyptxt.equalsIgnoreCase("")) {
					if (!latitude.equalsIgnoreCase("")
							&& !longitude.equalsIgnoreCase("")
							&& !latitude.equalsIgnoreCase("0.0")
							&& !longitude.equalsIgnoreCase("0.0")) {
						new SendData(
								getActivity().getApplicationContext(),
								"https://developer.nrel.gov/api/pvwatts/v5.json?api_key=REXh2uHlTS9fqTySMS2kMXnO7RynLSIgGhSgBbZ1&lat="
										+ latitude
										+ "&lon="
										+ longitude
										+ "&system_capacity=4&azimuth="
										+ Azimuthtxt
										+ "&tilt="
										+ Tilttxt
										+ "&array_type="
										+ Arytyptxt
										+ "&module_type="
										+ ModTyptxt
										+ "&losses="
										+ Syslosstxt
										+ "&dataset=intl&radius=0").execute();

					} else {
						AlertClass
								.Alertmsg(
										"Please enable GPS to get latitude and longitude then reload the activity.",
										getActivity());
					}
				} else {
					AlertClass.Alertmsg("Please fill value in all fields.",
							getActivity());
				}
			} else {
				AlertClass.Alertmsg("Please check your internet connection",
						getActivity());
			}
			break;
		default:
			break;
		}
	}

	class SendData extends AsyncTask<Void, Void, String> {
		ProgressDialog pd;
		String url = "";
		private Context context;

		public SendData(Context applicationContext, String url) {
			this.context = applicationContext;
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait..");
			pd.setCanceledOnTouchOutside(false);
			pd.show();

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String jsonStr = "";
			try {
				ServiceHandler sh = new ServiceHandler();
				// url =
				// "https://developer.nrel.gov/api/pvwatts/v5.json?api_key=REXh2uHlTS9fqTySMS2kMXnO7RynLSIgGhSgBbZ1&location=Denver+CO&lat=40&lon=-105&system_capacity=4&azimuth=180&tilt=40&array_type=1&module_type=1&losses=10";
				// Making a request to url and getting response
				// url =
				// "http://developer.nrel.gov/api/pvwatts/v5.json?api_key=MAF0jwFcU8e4pHtQvEyDNYTD3RR6by1HQxmlotlq&lat=42&lon=71&system_capacity=4&azimuth=180&tilt=20&array_type=1&module_type=1&losses=14&dataset=intl&radius=0";
				jsonStr = sh.makeServiceCall(this.url, ServiceHandler.GET);

				Log.d("Response: ", "> " + jsonStr);
			} catch (Exception e) {

				e.printStackTrace();

				jsonStr = "illigalArgumentException";
				return jsonStr;
			}

			return jsonStr;
		}

		@Override
		protected void onPostExecute(final String result) {
			try {
				System.out.println(result);
				if (pd.isShowing()) {
					pd.dismiss();
				}
				if (result.equalsIgnoreCase("illigalArgumentException")) {
					AlertClass
							.Alertmsg(
									"Data is not available for this location. Please select another location.",
									getActivity());

				}
				// else if(){
				//
				// }

				else {

					// Creating the instance of PopupMenu
					PopupMenu popup = new PopupMenu(getActivity()
							.getApplicationContext(), but_done);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(R.menu.solar_production,
							popup.getMenu());

					// registering popup with OnMenuItemClickListener
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						public boolean onMenuItemClick(MenuItem item) {

							JSONObject json;
							try {
								json = new JSONObject(result);
								JSONObject outputs = json
										.getJSONObject("outputs");

								if (item.getItemId() == R.id.action_production) {

									String ac_annual = outputs
											.getString("ac_annual");
									Log.e("", "ac_annual " + ac_annual);// 6745.57568359375
									String solrad_annual = outputs
											.getString("solrad_annual");
									Log.e("", "solrad_annual " + solrad_annual);// 6.040134429931641
									Intent action_production = new Intent(
											getActivity(),
											PriceingAcivity.class);
									action_production.putExtra("ac_annual",
											ac_annual);
									action_production.putExtra("solrad_annual",
											solrad_annual);
									startActivity(action_production);
									// Toast.makeText(SolarAnalysisActivity.this,"You Clicked : "
									// +
									// item.getTitle(),Toast.LENGTH_SHORT).show();
								} else if (item.getItemId() == R.id.action_graph) {
									JSONArray ac_monthly = outputs
											.getJSONArray("ac_monthly");
									ArrayList<String> list = new ArrayList<String>();
									for (int i = 0; i < ac_monthly.length(); i++) {
										list.add(ac_monthly.getString(i)
												.toString());
									}
									Intent action_production = new Intent(
											getActivity(),
											SolarProduction.class);
									action_production.putStringArrayListExtra(
											"ac_monthly", list);
									startActivity(action_production);
								}

							} catch (JSONException e) {

								AlertClass
										.Alertmsg(
												"No result found for entered data. Please change values and try again.",
												getActivity());
								e.printStackTrace();
							}
							return true;
						}
					});

					try {
						Field[] fields = popup.getClass().getDeclaredFields();
						for (Field field : fields) {
							if ("mPopup".equals(field.getName())) {
								field.setAccessible(true);
								Object menuPopupHelper = field.get(popup);
								Class<?> classPopupHelper = Class
										.forName(menuPopupHelper.getClass()
												.getName());
								Method setForceIcons = classPopupHelper
										.getMethod("setForceShowIcon",
												boolean.class);
								setForceIcons.invoke(menuPopupHelper, true);
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					popup.show();

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			super.onPostExecute(result);
		}
	}

}
