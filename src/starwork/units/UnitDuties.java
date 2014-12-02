package starwork.units;

import java.util.HashMap;

import bwapi.Unit;

public class UnitDuties {

	private static HashMap<Unit, UnitDuty> duties = new HashMap<>();
	private static HashMap<Unit, Units> unitAssignments = new HashMap<>();

	// =========================================================

	public static UnitDuty getUnitDuty(Unit unit) {
		return duties.get(unit);
	}

	public static void setUnitDuty(Unit unit, UnitDuty duty) {
		duties.put(unit, duty);
	}

	// =========================================================

	public static Units getUnitAssignment(Unit unit) {
		return unitAssignments.get(unit);
	}

	public static void setUnitAssignment(Unit unit, Units assignment) {
		unitAssignments.put(unit, assignment);
	}

}
