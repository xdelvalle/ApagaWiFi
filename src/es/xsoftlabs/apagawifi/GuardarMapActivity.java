package es.xsoftlabs.apagawifi;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import es.xsoftlabs.apagawifi.bd.BdPosicionesSQLite;
import es.xsoftlabs.apagawifi.bd.IBdPosiciones;
import es.xsoftlabs.apagawifi.model.Posicion;

public class GuardarMapActivity extends FragmentActivity implements LocationListener, LocationSource, OnMapClickListener{
	
	private OnLocationChangedListener locationListener;
	private LocationManager locationManager;
	private GoogleMap mapa;
	private IBdPosiciones db = new BdPosicionesSQLite(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guardar_map);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		if (locationManager != null) {
			boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (gpsEnabled)
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000L, 10F, this);
			else if (networkEnabled)
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000L, 10F, this);
			else
				Toast.makeText(this, getString(R.string.no_provider), Toast.LENGTH_SHORT).show();
		} 
		
		configurarMapa();
	}

	private void configurarMapa() {
		
		ArrayList<Posicion> lista = db.getPosiciones();
		
		if(mapa == null)
			mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		mapa.setMyLocationEnabled(true);
		mapa.setLocationSource(this);
		mapa.setOnMapClickListener(this);
		
		if(lista.size() > 0) {
			for(Posicion point : lista){
				mapa.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude())));
			}
		}
	}
	
	@Override
	public void onMapClick(LatLng point) {
		mapa.addMarker(new MarkerOptions().position(new LatLng(point.latitude, point.longitude)));

		Intent intent = new Intent(this, GuardarPosicionActivity.class);
		Bundle b = new Bundle();
		b.putDouble("latitude", point.latitude);
		b.putDouble("longitude", point.longitude);
		intent.putExtras(b);
		
		startActivity(intent);
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		locationListener = listener;
	}

	@Override
	public void deactivate() {
		locationListener = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		configurarMapa();
		
		if(locationListener != null) {
			locationListener.onLocationChanged(location);
			mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if(locationManager != null)
			locationManager.removeUpdates(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		if (locationManager != null) {
			boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (gpsEnabled)
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000L, 10F, this);
			else if (networkEnabled)
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000L, 10F, this);
			else
				Toast.makeText(this, getString(R.string.no_provider), Toast.LENGTH_SHORT).show();
		} 
		
		configurarMapa();
	}
}
