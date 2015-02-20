package com.crinis.solar;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CrinisSiteActivity  extends Fragment {
	 private ProgressDialog progressDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		  View rootView = inflater.inflate(R.layout.crinissiteactivity, container, false);
		  
		  String url = "http://crinispower.com/";

	//	  WebView wv = new WebView(CrinisSiteActivity.this); 
		  // or 
		   WebView wv = (WebView)rootView.findViewById(R.id.webView);
		   wv.getSettings().setJavaScriptEnabled(true);
//           wv.setWebViewClient(new SwAWebClient());
//		  wv.loadUrl(url);
		  
	     
	
	
		  wv.setWebViewClient(new WebViewClient() {
       
		public boolean shouldOverrideUrlLoading(WebView view, String url) {              
            view.loadUrl(url);
            return true;
        }
        public void onLoadResource (WebView view, String url) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please Wait..");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }
        public void onPageFinished(WebView view, String url) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }); 
    wv.loadUrl("http://crinispower.com/");

	
	
    
    return rootView;
}
	
	
	
//	  class SwAWebClient extends WebViewClient {
//		  
//	        @Override
//	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//	            return false;
//	        }
//	         
//	    }
}
