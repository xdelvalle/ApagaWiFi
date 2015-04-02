package es.xsoftlabs.apagawifi;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import es.xsoftlabs.apagawifi.bd.BdPosicionesSQLite;
import es.xsoftlabs.apagawifi.bd.IBdPosiciones;
import es.xsoftlabs.apagawifi.model.Posicion;

public class PosicionesActivity extends ListActivity {

	private final int ELIMINAR_POSICION = 0;
	private IBdPosiciones db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.posiciones);

		db = new BdPosicionesSQLite(this);

		setListAdapter(new MiAdaptador(this, db.getPosiciones()));

		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(getString(R.string.posicion));
		menu.add(0, ELIMINAR_POSICION, 0, getString(R.string.eliminar));
	}

	@Override
	public boolean onContextItemSelected(MenuItem aItem) {

		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();

		switch (aItem.getItemId()) {
			case ELIMINAR_POSICION:
				Posicion pos = (Posicion) getListView().getAdapter().getItem(menuInfo.position);
				
				db.deletePosicion(pos.getNombre());
				Toast.makeText(this, pos.getNombre() + " " + getString(R.string.eliminada), Toast.LENGTH_LONG).show();
				
				this.finish();
				
				Intent i = new Intent(this, PosicionesActivity.class);
				startActivity(i);
				
				return true;
		}
		return false;
	}
}
