package com.killermobi.locationfinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.killermobi.locationfinder.model.LocationDetail;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class MapDialog extends DialogFragment{
	private View rootView;
	private Button closeMapButton;
	private LocationDetail locationDetail=null;
	private GoogleMap googleMap;
	
	public MapDialog(LocationDetail locationDetail){
		this.locationDetail = locationDetail;
	}//MapDialog
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getDialog().setTitle("Map for Zip Code:"+locationDetail.getZipcode());
		rootView = inflater.inflate(R.layout.layout_map_dialog, container);
		closeMapButton = (Button)rootView.findViewById(R.id.closeDialogButton);
		
		String cityInfo;
		if(locationDetail.getCity()!=null){
			cityInfo = locationDetail.getCity()+","+locationDetail.getCountry();
		}else{
			cityInfo = locationDetail.getCountry();
		}
		
		googleMap =  ((MapFragment)this.getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
		googleMap.clear();
		googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(locationDetail.getLattitude(),locationDetail.getLongitude()) , 14.0f) );
		googleMap.addMarker(new MarkerOptions().position(new LatLng(locationDetail.getLattitude(),locationDetail.getLongitude())).title(cityInfo).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		
		closeMapButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
			
		});//OnnClickListener
		return rootView;
	}//onCreateView
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MapFragment mapFragment = (MapFragment)this.getActivity().getFragmentManager().findFragmentById(R.id.map);
	     if(mapFragment!=null){
	    	 this.getFragmentManager().beginTransaction().remove(mapFragment).commit();
	     }
	}//onDestroyView

}
