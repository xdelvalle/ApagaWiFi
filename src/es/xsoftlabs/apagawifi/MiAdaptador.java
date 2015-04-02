package es.xsoftlabs.apagawifi;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.xsoftlabs.apagawifi.model.Posicion;

public class MiAdaptador extends BaseAdapter{
	private int[] colors = new int[] { 0x4e4e4e4e, 0x00000000 };
	private final Activity actividad;
	private final ArrayList<Posicion> lista;

	public MiAdaptador(Activity actividad, ArrayList<Posicion> lista) {
		super();
		this.actividad = actividad;
		this.lista = lista;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();

		View view = inflater.inflate(R.layout.elemento_lista, null, true);
		
		TextView textView = (TextView) view.findViewById(R.id.nombre);
		textView.setText(lista.get(position).getNombre());
		
		TextView textView2 = (TextView) view.findViewById(R.id.latlon);
		textView2.setText("Lat: " + lista.get(position).getLatitude() + " Lon: " + lista.get(position).getLongitude());
		
		ImageView imageView = (ImageView) view.findViewById(R.id.icono);
		imageView.setImageResource(R.drawable.wifi2);

		int colorPos = position % colors.length;
		view.setBackgroundColor(colors[colorPos]);
		
		return view;
	}

	public int getCount() {
		return lista.size();
	}

	public Object getItem(int arg0) {
		return lista.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
}
