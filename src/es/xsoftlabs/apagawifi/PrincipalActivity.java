package es.xsoftlabs.apagawifi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PrincipalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
		
		// Lanzar servicio
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("es.xsoftlabs.apagawifi.ServiceWifi");
		startService(serviceIntent);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.about:
				Intent acercaDe = new Intent(this, AcercaDeActivity.class);
				startActivity(acercaDe);
				return true;
			case R.id.config:
				Intent prefs = new Intent(this, PreferenciasActivity.class);
				startActivity(prefs);
				return true;
		}
		return false;
	}
	
	public void guardaPosicion(View view) {
		//Intent i = new Intent(this, GuardarPosicionActivity.class);
		Intent i = new Intent(this, GuardarMapActivity.class);
		startActivity(i);
	}
	
	public void listadoPosicionesGuardadas(View view) {
		Intent i = new Intent(this, PosicionesActivity.class);
		startActivity(i);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
}
