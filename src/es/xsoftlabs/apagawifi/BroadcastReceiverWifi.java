package es.xsoftlabs.apagawifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverWifi extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		// Lanzar servicio
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("es.xsoftlabs.apagawifi.ServiceWifi");
		context.startService(serviceIntent);
	}
}
