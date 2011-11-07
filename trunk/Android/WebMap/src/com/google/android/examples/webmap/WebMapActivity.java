package com.google.android.examples.webmap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebMapActivity extends Activity implements LocationListener {
  
  private static final String MAP_URL = 
	  "http://code.google.com/intl/zh-CN/apis/maps/documentation/javascript/examples/overlay-remove.html";
	  //"http://192.168.2.100/gmap/simple-android-map.html";
	  //"http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";
  //private static final String MAP_URL = "simple-android-map.html";
  private WebView webView;
  private Location mostRecentLocation;

  @Override
  /** Called when the activity is first created. */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    getLocation();
    setupWebView();
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

  }
  /** Sets up the WebView object and loads the URL of the page **/
  private void setupWebView(){
    final String centerURL = "javascript:centerAt(" + "22.5621092,113.9774844"+")";
    //mostRecentLocation.getLatitude() + "," + 
    //mostRecentLocation.getLongitude()+ ")";
    webView = (WebView) findViewById(R.id.webview);
    webView.getSettings().setJavaScriptEnabled(true);
    //Wait for the page to load then send the location information
    webView.setWebViewClient(new WebViewClient(){  
      @Override  
      public void onPageFinished(WebView view, String url)  
      {
        System.out.println("centerURL="+centerURL);
        webView.loadUrl(centerURL);
      }

    });
    webView.loadUrl(MAP_URL);  




  }




  /** The Location Manager manages location providers. This code searches
        for the best provider of data (GPS, WiFi/cell phone tower lookup,
        some other mechanism) and finds the last known location.
   **/
  private void getLocation() {  
	  try{
	    LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    String provider = locationManager.getBestProvider(criteria,true);
	
	    //In order to make sure the device is getting location, request updates.        locationManager.requestLocationUpdates(provider, 1, 0, this);
	    mostRecentLocation = locationManager.getLastKnownLocation(provider);
	  }catch(Exception e){
		  System.out.print("getLocation failed.");
		  e.printStackTrace();
	  }
  }

  /** Sets the mostRecentLocation object to the current location of the device **/
  @Override
  public void onLocationChanged(Location location) {
    mostRecentLocation = location;
  }

  /** The following methods are only necessary because WebMapActivity implements LocationListener **/ 
  @Override
  public void onProviderDisabled(String provider) {
  }

  @Override
  public void onProviderEnabled(String provider) {
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

}