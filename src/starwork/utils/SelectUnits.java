package starwork.utils;

import starwork.main.Starwork;

public class SelectUnits {
	
	private Units units;
	
	private SelectUnits(Units units) {
		this.units = units;
	}

	// =====================================================================
	// Create base object
	
	public static Units selectOur() {
		Units units = new Units();
		
		units.addUnits(Starwork.getSelf().getUnits());
		
		return units;
	}
	
	public static Units selectEnemy() {
		Units units = new Units();
		
		units.addUnits(Starwork.getEnemy().getUnits());
		
		return units;
	}
	
	public static Units selectNeutral() {
		Units units = new Units();
		
		units.addUnits(Starwork.getNeutral().getUnits());
		
		return units;
	}
	
	public static Units selectAllUnits() {
		Units units = new Units();
		
		units.addUnits(Starwork.getSelf().getUnits());
		
		return units;
	}

}
