package com.crinis.solar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.crinis.db.DBaseHandler;

public class SplashActivity extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 1500;
	Thread splashTread;
	private boolean stop = false;
	private DBaseHandler dbh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		dbh = new DBaseHandler(SplashActivity.this);

		splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					if (!stop) {
						try {

							SharedPreferences sharedpreferences = getSharedPreferences(
									"solarprefer", Context.MODE_PRIVATE);
							if (sharedpreferences.contains("status")) {
								String status = sharedpreferences.getString(
										"status", "");
								Log.e("",""+status);
								if (status.equalsIgnoreCase("success")) {
									Intent hh = new Intent(SplashActivity.this,
											HomeActivity.class);
									startActivity(hh);
								}
							}else {

									startActivity(new Intent(
											SplashActivity.this,
											LoginActivity.class));
								}
							
						} catch (Exception e) {
							e.printStackTrace();
							startActivity(new Intent(SplashActivity.this,
									LoginActivity.class));
						}

						finish();
					} else
						finish();
				}
			}

		};
		splashTread.start();

	}

	@Override
	public void onBackPressed() {
		if (splashTread.isAlive())
			this.stop = true;

		super.onBackPressed();
	}

}
