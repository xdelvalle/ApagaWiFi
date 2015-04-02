package es.xsoftlabs.apagawifi.bd;

import java.util.ArrayList;

import es.xsoftlabs.apagawifi.model.Posicion;

public interface IBdPosiciones {
	
	public void insertPosicion(Posicion pos);
	
	public void deletePosicion(String nombre);
	
	public ArrayList<Posicion> getPosiciones();
}
