package es.xsoftlabs.apagawifi.bd;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import es.xsoftlabs.apagawifi.model.Posicion;

public class BdPosicionesSQLite extends SQLiteOpenHelper implements IBdPosiciones {

	public BdPosicionesSQLite(Context context) {
		super(context, "posiciones", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE posiciones (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, latitud DOUBLE, longitud DOUBLE)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) { }
	
	@Override
	public void insertPosicion(Posicion pos) {
		SQLiteDatabase db = getWritableDatabase();
		if(db != null) {
			db.execSQL("INSERT INTO posiciones VALUES (null, '" + pos.getNombre() + "', " + pos.getLatitude() + ", " + pos.getLongitude() + ")");
			db.close();
		}
	}

	@Override
	public void deletePosicion(String nombre) {
		SQLiteDatabase db = getWritableDatabase();
		if(db != null) {
			db.delete("posiciones", "nombre=?", new String[] {nombre});
			db.close();
		}
	}

	@Override
	public ArrayList<Posicion> getPosiciones() {
		ArrayList<Posicion> listaPosiciones = new ArrayList<Posicion>();
		Posicion pos;
		
		SQLiteDatabase db = getWritableDatabase();
		if(db != null) {
			Cursor cursor = db.query("posiciones", null, null, null, null, null, "nombre");
			cursor.moveToFirst();
			
			while(!cursor.isAfterLast()) {
				pos = new Posicion(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
				listaPosiciones.add(pos);
				
				cursor.moveToNext();
			}
			
			db.close();
			
			return listaPosiciones;
		}
		else
			return null;
	}
}
