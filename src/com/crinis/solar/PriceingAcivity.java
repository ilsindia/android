package com.crinis.solar;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PriceingAcivity extends Activity {

	private TextView ac_energy;
	private TextView s_radiation;
//	private TextView e_value;

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		View rootView = inflater.inflate(R.layout.pricingactivity, container,
//				false);
//		getView(rootView);
//		return rootView;
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pricingactivity);
		
		getView();
		ac_energy.setText(String.format("%.2f", getIntent().getStringExtra("ac_annual")));
		s_radiation.setText(String.valueOf((int) (Float.parseFloat(getIntent().getStringExtra("solrad_annual")))) );
	}

	private void getView() {
		ac_energy = (TextView) findViewById(R.id.ac_energy);//(TextView) rootView.findViewById(R.id.ac_energy);
		s_radiation = (TextView)findViewById(R.id.s_radiation);
//		e_value = (TextView) findViewById(R.id.e_value);
	}
}
