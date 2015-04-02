package utils;

import es.xsoftlabs.apagawifi.model.Posicion;

public class Harvesine {
	private final double EARTH_RADIUS = 6371;

	public static Harvesine instance = new Harvesine();
	
	private Harvesine() { } 
	
	public double getDistanceInKm(Posicion actual, Posicion guardada) {
		double latitud  = toRadians(guardada.getLatitude() - actual.getLatitude());
		double longitud = toRadians(guardada.getLongitude() - actual.getLongitude());
		
		double a = Math.pow(Math.sin(latitud / 2), 2) + 
				Math.cos(toRadians(actual.getLatitude())) * Math.cos(toRadians(guardada.getLatitude())) * Math.pow(Math.sin(longitud / 2) , 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		
		return EARTH_RADIUS * c;
	}
	
	public double getDistanceInMeters(Posicion actual, Posicion guardada) {
		return getDistanceInKm(actual, guardada) * 1000;
	}
	
	private double toRadians(double input) {
		return input * (Math.PI / 180);
	}

	public static Harvesine getInstance() {
		return instance;
	}
}
