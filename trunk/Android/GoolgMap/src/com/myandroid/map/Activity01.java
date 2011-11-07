package com.myandroid.map;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Activity01 extends MapActivity {
	private MapView mMapView;
	private MapController mMapController;
	private MyLocationOverlay myPosition;

	private final static int ZOOM_IN=Menu.FIRST;
	private final static int ZOOM_OUT=Menu.FIRST+1;
	private TextView tv;
	private String loc;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv=(TextView)findViewById(R.id.tv);
        
        //��õ�ͼ�Ķ�λ������
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        mMapView=(MapView)findViewById(R.id.MapView01);
        
        //�趨��ͼģʽ����ͨ�����ǡ��ֵ�
        mMapView.setTraffic(true);
       //mMapView.setSatellite(true);
     //   mMapView.setStreetView(true);
        
        //�õ���ͼ��ͼ�Ŀ�����
        mMapController =mMapView.getController();
        
        //�趨��ͼ��ͼ���������
        mMapView.setEnabled(true);
        mMapView.setClickable(true);
        mMapView.displayZoomControls(false);
       // mGeoPoint=new GeoPoint((int)30.659259*1000000,(int)104.065762*1000000);
        
        //�趨��ͼ�����ķŴ���
        mMapController.setZoom(17);
        
        //�����û���д��overlay����������һ�����Ƕ���
        myPosition=new MyLocationOverlay();
        
        //��õ�ͼ��ͼ�ĸ��Ƕ��󼯺ϣ�������Ͻ���������ͼ�ϣ������һЩ�¼�������Ӧ
        List<Overlay> list=mMapView.getOverlays();
        
        //�������Զ���ĸ��Ƕ�����뵽���������
        list.add(myPosition);   
        
        //��ͼ�����̣���׼���Ķ��壬�������ָ���providers       
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);	//��ȷ�ٶ�
        criteria.setAltitudeRequired(false);			//����
        criteria.setBearingRequired(false);				//����
        criteria.setCostAllowed(false);					//�Ƿ�Ʒ�
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        
        //�����������ϱ�׼��provider������  
        String provider=locationManager.getBestProvider(criteria, true);
        
        //������provider�����Ķ�λ��Ϣ
        Location location=locationManager.getLastKnownLocation(provider);
        
        //���¶�λ��Ϣ
        updateWithNewLocation(location);
        
        //��λ����������λ����
        locationManager.requestLocationUpdates(provider, 3000, 0, locationListener);
    }

    
    
    
    public void updateWithNewLocation(Location location)
    {
    	String latLongString;
    	//TextView tv=(TextView)findViewById(R.id.tv);
    	String  addressString="can not find the address\n";
    	if(location!=null)
    	{
    		myPosition.setLocation(location);
    		
    		// �õ���λ��Ϣ�ľ��ȡ�γ������
    		Double  geoLat=location.getLatitude()*1E6;
    		Double  geoLng=location.getLongitude()*1E6;
    		
    		//�Ѿ��ȡ�γ�����ݷ�װ��һ��GeoPoint��������Ǿ�γ������
    		GeoPoint point=new GeoPoint(geoLat.intValue(),geoLng.intValue());
    		
    		//��ͼ��λ���þ�γ������
    		mMapController.animateTo(point);
    		
    		//��øõ�ľ��Ⱥ�γ����Ϣ
    		double lat=location.getLatitude();
    		double lng=location.getLongitude();
    		latLongString="jingdu��"+lat+"  weidu"+lng;
    		
    		double latitude=location.getLatitude();
    		double longitude=location.getLongitude();
    		
    		//���ݸ��������Ի��������ػ�����һ������������
    		Geocoder gc=new Geocoder(this,Locale.CHINA);
    		
    		try
    		{
    			//��һ�����Ⱥ�γ�Ȼ����һ����������ĵ���������Ϣ
    			List<Address> addresses=gc.getFromLocation(latitude, longitude, 1);
    			StringBuffer sb=new StringBuffer();
    			if(addresses.size()>0)
    			{
    			

    				//��õ���λ����ϸ��Ϣ
    				Address address = addresses.get(0);
    				for(int i=1;i<address.getMaxAddressLineIndex();i++)
	    			{
    					System.out.println("begin to read the "+i+" line");
    					sb.append(address.getCountryName()+address.getAddressLine(i)+address.getThoroughfare()+address.getFeatureName() );
	    				
						//sb.append(address.getAddressLine(i));//.append("\n");			//��һ�С��й������ڶ��С��㶫ʡ�����и�������
	    				//sb.append(address.getLocality());//.append("\n");				//��һ�С������С����ڶ��С������С�
	    				//sb.append(address.getPostalCode());//.append("\n");			//��һ�С�null�����ڶ��С�null��
	    				//sb.append(address.getCountryName());							//��һ�С�null�����ڶ��С�null��
    					//sb.append(address.getThoroughfare()    );						//ũ��·
    					
    					addressString=sb.toString();
	    				Toast.makeText(Activity01.this, addressString, Toast.LENGTH_LONG).show();
	    				System.out.println(addressString);
	    			}
    			}
    			
    		}
    		catch(IOException e)
    		{
    			
    		}
    	}
    	else
    	{
    		latLongString="can find the coods\n";
    		
    	}
    	tv.setText("�㵱ǰ���������£�\n"+latLongString+"\n"+addressString);
    	System.out.println("your coords:\n"+latLongString+"\n"+addressString);
    	loc=addressString;
    	
    	
    }
    
    private final LocationListener locationListener=new LocationListener(){
    	public void onLocationChanged(Location location)
    	{
    		updateWithNewLocation(location);
    	}
    	
    	public void onProviderDisabled(String provider)
    	{
    		updateWithNewLocation(null);
    	}
    	public void onProviderEnabled(String provider)
    	{
    		
    	}
    	public void onStatusChanged(String provider,int status,Bundle extras){}  	
    };
    
    protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, ZOOM_IN, Menu.NONE, "�Ŵ�");
		menu.add(0, ZOOM_OUT, Menu.NONE, "��С");
		return true;
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		switch(item.getItemId())
		{
			case(ZOOM_IN):
			  mMapController.zoomIn();
			  return true;
			case(ZOOM_OUT):
				mMapController.zoomOut();
			    return true;
		}
		return true;
	}


    
class MyLocationOverlay extends Overlay
    {
				Location mlocation;
				public void setLocation(Location location)
				{
					mlocation=location;
				}
			
					@Override
					public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
							long when) {
						
						// TODO Auto-generated method stub
						 super.draw(canvas, mapView, shadow);
						 Paint  paint=new Paint();
						 //����һ����Ļ�������
						 Point myScreenCoords=new Point();
						GeoPoint tmpGeoPoint =  new GeoPoint((int)(mlocation.getLatitude()*1E6),(int)(mlocation.getLongitude()*1E6));
						//�ѵ�������ŵ���Ļ������
						mapView.getProjection().toPixels(tmpGeoPoint, myScreenCoords);
						
						paint.setStrokeWidth(1);//�����������
						paint.setARGB(255, 255, 0, 0);//ɫ��͸���Ⱥ���ɫ����
						paint.setStyle(Paint.Style.STROKE);//�������
						
						//����ָ��λ�õ�Сͼ��
						 Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.home);
						 
						 //��Сͼ����ʾ����Ļ������
						 canvas.drawBitmap(bmp, myScreenCoords.x,myScreenCoords.y, paint);
						 
						 //�ѵ��������ı���Ϣ��ʾ����Ļ������
						 canvas.drawText(loc, myScreenCoords.x, myScreenCoords.y, paint);
						 return true;
					}
    	
    }

	
	
}