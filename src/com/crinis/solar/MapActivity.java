package com.crinis.solar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.crinis.util.AlertClass;
import com.crinis.util.ConnectionDetector;
import com.crinis.util.GeocodeJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements OnClickListener {

	// Google Map
	private GoogleMap googleMap;
	private EditText addresstxt;
	private Button map_done;
	private Button btn_show;
	private String location;
	public double latitude;
	public double longitude;
	private ConnectionDetector cd;
	String resutlStr = "";

	// static final ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);
		getViews();
		cd = new ConnectionDetector(this);
		if (cd.isConnectingToInternet()) {
			try {
				if (googleMap == null) {
					googleMap = ((MapFragment) getFragmentManager()
							.findFragmentById(R.id.map)).getMap();
				}
				googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				googleMap.setMyLocationEnabled(true);
				latitude = Double.parseDouble(getIntent().getStringExtra(
						"latitude"));
				longitude = Double.parseDouble(getIntent().getStringExtra(
						"longitude"));
				LatLng points = new LatLng(latitude, longitude);
				Marker TP = googleMap.addMarker(new MarkerOptions()
						.position(points));
				googleMap.moveCamera(CameraUpdateFactory.newLatLng(points));

				// Zoom in the Google Map
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			AlertClass.Alertmsg("Please check your internet connection",
					MapActivity.this);
		}
	}

	private void getViews() {
		addresstxt = (EditText) findViewById(R.id.addresstxt);
		map_done = (Button) findViewById(R.id.map_done);
		btn_show = (Button) findViewById(R.id.btn_show);

		map_done.setOnClickListener(this);
		btn_show.setOnClickListener(this);
	}

	public String geoCode(String location) {

		if (location == null || location.equals("")) {

			resutlStr = "No Place is entered";

			// Toast.makeText(getBaseContext(), resutlStr, Toast.LENGTH_SHORT)
			// .show();
			AlertClass.Alertmsg("Please enter location to search.",
					MapActivity.this);

			return resutlStr;
		}

		String url = "https://maps.googleapis.com/maps/api/geocode/json?";

		try {
			// encoding special characters like space in the user input place
			location = URLEncoder.encode(location, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String address = "address=" + location;

		String sensor = "sensor=false";

		// url , from where the geocoding data is fetched
		url = url + address + "&" + sensor;
		// String modifiedURL= url.toString().replace(" ", "%20");

		// Instantiating DownloadTask to get places from Google Geocoding
		// service
		// in a non-ui thread
		DownloadTask downloadTask = new DownloadTask();

		// Start downloading the geocoding places
		downloadTask.execute(url);
		String af = resutlStr;
		return resutlStr;

	}

	/** A class, to download Places from Geocoding webservice */
	private class DownloadTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);

			} catch (Exception e) {
				Log.e("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {

			ParserTask parserTask = new ParserTask();
			parserTask.execute(result);
		}

	}

	class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;
		private int checktap;
		private MarkerOptions markerOptions;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;
			GeocodeJSONParser parser = new GeocodeJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a an ArrayList */
				places = parser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {

			// Clears all the existing markers

			try {

				googleMap.clear();

				if (list.size() < 1) {
					resutlStr = "No Result Found";
					AlertClass.Alertmsg("No result found for this location.",
							MapActivity.this);
				} else {
					for (int i = 0; i < list.size(); i++) {

						// Creating a marker
						MarkerOptions markerOptions = new MarkerOptions();
						HashMap<String, String> hmPlace = list.get(i);

						double lat = Double.parseDouble(hmPlace.get("lat"));
						double lng = Double.parseDouble(hmPlace.get("lng"));

						String name = hmPlace.get("formatted_address");

						LatLng latLng = new LatLng(lat, lng);
						latitude = lat;
						longitude = lng;
						String title = name;
						addMarker(latLng, name);

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		private void addMarker(LatLng latLng, String name) {
			checktap++;
			if (checktap > 1) {
				// googleMap = super.getMap();

				googleMap.clear();
			}
			markerOptions = new MarkerOptions();

			// Setting the position for the marker
			markerOptions.position(latLng);
			markerOptions.title(name);
			markerOptions.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

			// Placing a marker on the touched position
			googleMap.addMarker(markerOptions);

			googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

		}
	}

	public String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.map_done:

			Intent maplo = new Intent(MapActivity.this, HomeActivity.class);
			// maplo.putExtra("markerAddress", String.valueOf(location));
			maplo.putExtra("latitude", String.valueOf(latitude));

			maplo.putExtra("longitude", String.valueOf(longitude));
			maplo.putExtra("activityparm", "1");
			startActivity(maplo);
			MapActivity.this.finish();
			break;
		case R.id.btn_show:
			if (cd.isConnectingToInternet()) {
				location = addresstxt.getText().toString();

				geoCode(location);
			} else {
				AlertClass.Alertmsg("Please check your internet connection",
						MapActivity.this);
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		Intent maplo = new Intent(MapActivity.this, HomeActivity.class);
		// maplo.putExtra("markerAddress", String.valueOf(location));
		// maplo.putExtra("latitude", String.valueOf(latitude));
		//
		// maplo.putExtra("longitude", String.valueOf(longitude));
		maplo.putExtra("activityparm", "1");
		startActivity(maplo);
		MapActivity.this.finish();
		super.onBackPressed();
	}
	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	// private void initilizeMap() {
	// if (googleMap == null) {
	// googleMap = ((MapFragment) getFragmentManager().findFragmentById(
	// R.id.map)).getMap();
	//
	// // check if map is created successfully or not
	// if (googleMap == null) {
	// Toast.makeText(getApplicationContext(),
	// "Sorry! unable to create maps", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	// }

	// @Override
	// protected void onResume() {
	// super.onResume();
	// initilizeMap();
	// }
}