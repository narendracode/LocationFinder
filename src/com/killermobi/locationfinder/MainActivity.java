package com.killermobi.locationfinder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.killermobi.locationfinder.model.LocationDetail;
import com.killermobi.locationfinder.util.ConnectionDetector;
import android.view.View.OnClickListener;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	List<Address> locations = null;
	EditText zipCodeEditText;
	Button searchLocationButton;
	Geocoder frwdGeocoder = null;
	String postCode=null;
	ConnectionDetector connectionDetector=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectionDetector = new ConnectionDetector(getApplicationContext());
		
		setContentView(R.layout.activity_main);
		zipCodeEditText = (EditText)findViewById(R.id.zipcode);
		searchLocationButton = (Button)findViewById(R.id.search);
		
		frwdGeocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

		searchLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(connectionDetector.isConnectedToInternet()){
				postCode = zipCodeEditText.getText().toString();
				LocationDetail location = getLocationDetail(postCode);
					try{
					if(!location.getZipcode().isEmpty()){
						try {
							List<Address> addresses = frwdGeocoder.getFromLocation(location.getLattitude(), location.getLongitude(), 1);
								Address myAdd = addresses.get(0);
								if(myAdd.getAddressLine(1)!=null){
									location.setCity(myAdd.getAddressLine(1));
								}
								
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						MapDialog mapDialog = new MapDialog(location);
						mapDialog.show(getFragmentManager(), "dialog");
					}
					}catch(NullPointerException e){
						showAlertDialog(MainActivity.this, "Zip Code Error", "Looks like zip code you entered does not exists, please try again.");	
					}
				}
				else{
					showAlertDialog(MainActivity.this, "Internet Connection", "Please connect to internet to access the services.");
				}
				
			}
		});
	}

	
	
	private LocationDetail getLocationDetail(String zipcode){
		LocationDetail locationDetail = new LocationDetail();
		try{
			
			locations = frwdGeocoder.getFromLocationName(zipcode, 5);
			if(locations.size()>0){
				Address address = locations.get(0);
				locationDetail.setCountry(address.getCountryName());
				locationDetail.setLattitude(address.getLatitude());
				locationDetail.setLongitude(address.getLongitude());
				locationDetail.setCity(address.getLocality());
				locationDetail.setZipcode(address.getPostalCode()+"");
				
				}
		}catch(Exception e){
			
		}
		
		
		return locationDetail;
	}//getLocationDetail
	
	public void showAlertDialog(Context context, String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);
		
		// Setting alert dialog icon
		alertDialog.setIcon(R.drawable.ic_network_notavailable);
		
		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
