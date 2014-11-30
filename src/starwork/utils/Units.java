package starwork.utils;

import java.util.ArrayList;
import java.util.Collection;

import starwork.main.Starwork;
import bwapi.Unit;

public class Units {
	
	private ArrayList<Unit> units = new ArrayList<>();
	private boolean freshObject = true;
	
	// =====================================================================
	
	protected Units() {
	}
	
	// =====================================================================

	public Units addUnits(Collection<Unit> unitsToAdd) {
		unitsToAdd.addAll(unitsToAdd);
		return this;
	}
	
	public Units removeUnits(Collection<Unit> unitsToRemove) {
		unitsToRemove.removeAll(unitsToRemove);
		return this;
	}
	
	// =====================================================================
	
	public Units getOur() {
		return this.addUnits(Starwork.getSelf().getUnits());
	}


}
