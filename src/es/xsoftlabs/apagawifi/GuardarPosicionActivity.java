package es.xsoftlabs.apagawifi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import es.xsoftlabs.apagawifi.bd.BdPosicionesSQLite;
import es.xsoftlabs.apagawifi.bd.IBdPosiciones;
import es.xsoftlabs.apagawifi.model.Posicion;

public class GuardarPosicionActivity extends Activity {
	
	private EditText nombre;
	private Posicion pos = new Posicion();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guardar);

		nombre = (EditText)findViewById(R.id.editText);
	}
	
	public void guardarLocalizacion(View view) {
	
		Bundle bundle = this.getIntent().getExtras();
		
		if(bundle != null) {
			pos.setNombre(nombre.getText().toString());
			pos.setLatitude(bundle.getDouble("latitude"));
			pos.setLongitude(bundle.getDouble("longitude"));
			
			IBdPosiciones db = new BdPosicionesSQLite(this);
			db.insertPosicion(pos);
			
			Intent i = new Intent(this, PosicionesActivity.class);
			startActivity(i);
			
			this.finish();
		}
		else
			Toast.makeText(this, "Error! No se ha podido determinar la posición..", Toast.LENGTH_LONG).show();
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
