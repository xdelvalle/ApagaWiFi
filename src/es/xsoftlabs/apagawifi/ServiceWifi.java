package es.xsoftlabs.apagawifi;

import java.util.ArrayList;

import utils.Harvesine;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import es.xsoftlabs.apagawifi.bd.BdPosicionesSQLite;
import es.xsoftlabs.apagawifi.bd.IBdPosiciones;
import es.xsoftlabs.apagawifi.model.Posicion;

public class ServiceWifi extends Service implements LocationListener{

	private WifiManager wifiManager;
	private LocationManager locationManager;
	public static Location location;
	private SharedPreferences prefs;
	private NotificationManager notificationManager;
	private static int ID_NOTIFICACION_CREAR = 111;
	
	private boolean activarServicio;
	private boolean notificaciones;
	private int distancia;
	
	private static final int PERIODO_PROCESO = 3 * 60 * 1000;
	private long ultimoCambio;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		activarServicio = prefs.getBoolean("activar_apagaWifi", true);
		notificaciones = prefs.getBoolean("notificaciones", true);
		distancia = Integer.parseInt(prefs.getString("distancia", "300"));
		
		location = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 30000L, 10F, this);
		
		wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		
		notificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(this);
		notificationManager.cancel(ID_NOTIFICACION_CREAR);
	}

	@Override
	public void onLocationChanged(Location loc) {
		
		long ahora = System.currentTimeMillis();

		if(ultimoCambio + PERIODO_PROCESO > ahora) 
			return;
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		activarServicio = prefs.getBoolean("activar_apagaWifi", true);
		notificaciones = prefs.getBoolean("notificaciones", true);
		distancia = Integer.parseInt(prefs.getString("distancia", "300"));
		
		if(activarServicio) {
			location = loc;

			Posicion actual = new Posicion("actual", loc.getLatitude(), loc.getLongitude());
			
			IBdPosiciones bd = new BdPosicionesSQLite(this);
			ArrayList<Posicion> listaPosiciones = bd.getPosiciones();
			
			boolean dentro = false;
			
			for(Posicion posicion : listaPosiciones) {
				double dist = Harvesine.getInstance().getDistanceInMeters(actual, posicion);
	
				if(dist < distancia) {
					dentro = true;
					break;
				}
				else 
					dentro = false;
			}
			
			if(!listaPosiciones.isEmpty()) {
				if(dentro) {
					if(!wifiManager.isWifiEnabled()) { 
						wifiManager.setWifiEnabled(true);
						
						ultimoCambio = ahora;
						
						if(notificaciones) {
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).
									setSmallIcon(R.drawable.wifi2).setContentTitle("ApagaWIFI").setContentText(getString(R.string.activar_wifi));
							
							Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							mBuilder.setSound(alarmSound);
							
							notificationManager.notify(ID_NOTIFICACION_CREAR, mBuilder.build()); 
						}
					}
				}
				else {
					if(wifiManager.isWifiEnabled()) {
						wifiManager.setWifiEnabled(false);
						
						ultimoCambio = ahora;
						
						if(notificaciones) {
							NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).
									setSmallIcon(R.drawable.wifi2).setContentTitle("ApagaWIFI").setContentText(getString(R.string.desactivar_wifi));
							
							Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							mBuilder.setSound(alarmSound);
							
							notificationManager.notify(ID_NOTIFICACION_CREAR, mBuilder.build()); 
						}
					}
				}
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) { }

	@Override
	public void onProviderEnabled(String provider) { }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { }
}
