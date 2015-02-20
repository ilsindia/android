package com.crinis.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class AlertClass {
	 static int itemNo;
	
	public static void Alertmsg(String msg, final Context context) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setMessage(msg);
		ab.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ab.create().show();
	}

}
