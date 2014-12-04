package starwork.terran;

import java.util.ArrayList;
import java.util.Collection;

import jnibwapi.model.Unit;
import jnibwapi.types.UnitType.UnitType;
import ai.core.XVR;
import ai.handling.map.Position;
import ai.handling.units.UnitActions;
import ai.handling.units.UnitCounter;

public class TerranMedic {

	private static XVR xvr = XVR.getInstance();

	private static UnitType unitType = UnitType.Terran_Medic;

	public static UnitType getUnitType() {
		return unitType;
	}

	// =========================================================

	public static void act(Unit unit) {
		Position goTo = null;
		Position unitPlace = getNearestInfantryPreferablyOutsideBunker(unit);
		if (unitPlace != null) {
			goTo = unitPlace;
		}

		// If there's someone to protect, go there
		if (goTo != null) {
			double distance = goTo.distanceTo(unit);

			// If distance is big, just go
			if (distance > 3) {
				UnitActions.moveTo(unit, goTo);
			} else {
				UnitActions.moveTo(unit, goTo);
			}
		}

		// ==============================
		// Manually check for units to heal
		ArrayList<Unit> possibleToHeal = xvr.getUnitsInRadius(unit, 50,
				xvr.getUnitsPossibleToHeal());
		for (Unit otherUnit : possibleToHeal) {
			if (otherUnit.isWounded()) {
				xvr.getBwapi().rightClick(unit, otherUnit);
				return;
			}
		}
	}

	// =========================================================

	private static Position getNearestInfantryPreferablyOutsideBunker(Unit unit) {

		// Define list of all infantry units that we could possibly follow
		Collection<Unit> allInfantry = xvr.getUnitsOurOfTypes(UnitType.Terran_Marine,
				UnitType.Terran_Firebat);
		ArrayList<Unit> nearestInfantry = xvr.getUnitsInRadius(unit, 300, allInfantry);

		// Try to go there, where's a marine/firebat not in a bunker
		for (Unit infantry : nearestInfantry) {
			if (infantry.isCompleted() && !infantry.isLoaded()) {
				return infantry;
			}
		}

		// Units in bunkers will do fine...
		for (Unit infantry : nearestInfantry) {
			return infantry;
		}

		return TerranCommandCenter.getSecondBaseLocation();
	}

	public static int getNumberOfUnits() {
		return UnitCounter.getNumberOfUnits(unitType);
	}

	public static int getNumberOfUnitsCompleted() {
		return UnitCounter.getNumberOfUnitsCompleted(unitType);
	}

}
