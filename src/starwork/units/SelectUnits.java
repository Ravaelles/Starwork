package starwork.units;

import java.util.Collection;

import starwork.helpers.PositionHelper;
import starwork.main.Starwork;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class SelectUnits {

	public static UnitType OUR_BASE = UnitType.Terran_Command_Center;
	public static UnitType OUR_WORKER = UnitType.Terran_SCV;

	// =====================================================================
	// Basic functionality for object

	private Units units;

	private SelectUnits(Units units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return units.toString();
	}

	@SuppressWarnings("unused")
	private SelectUnits filterOut(Collection<Unit> unitsToRemove) {
		units.removeUnits(unitsToRemove);
		return this;
	}

	private SelectUnits filterOut(Unit unitToRemove) {
		units.removeUnit(unitToRemove);
		return this;
	}

	@SuppressWarnings("unused")
	private SelectUnits filterAllBut(Unit unitToLeave) {
		for (Unit unit : units.list()) {
			if (unitToLeave != unit) {
				units.removeUnit(unit);
			}
		}
		return this;
	}

	// =====================================================================
	// Create base object

	public static SelectUnits our() {
		Units units = new Units();

		units.addUnits(Starwork.getSelf().getUnits());

		return new SelectUnits(units);
	}

	public static SelectUnits enemy() {
		Units units = new Units();

		units.addUnits(Starwork.getEnemy().getUnits());

		return new SelectUnits(units);
	}

	public static SelectUnits neutral() {
		Units units = new Units();

		units.addUnits(Starwork.getNeutral().getUnits());

		return new SelectUnits(units);
	}

	public static SelectUnits minerals() {
		Units units = new Units();

		units.addUnits(Starwork.getNeutral().getUnits());
		SelectUnits selectUnits = new SelectUnits(units);

		return selectUnits.ofType(UnitType.Resource_Mineral_Field);
	}

	public static SelectUnits all() {
		Units units = new Units();

		units.addUnits(Starwork.getSelf().getUnits());

		return new SelectUnits(units);
	}

	// =========================================================
	// Get results

	public Units units() {
		return units;
	}

	// =====================================================================
	// Filter units

	public SelectUnits ofType(UnitType type) {
		for (Unit unit : units.list()) {
			if (unit.getType() != type) {
				filterOut(unit);
			}
		}

		return this;
	}

	public SelectUnits idle() {
		for (Unit unit : units.list()) {
			if (!unit.isIdle()) {
				filterOut(unit);
			}
		}

		return this;
	}

	public SelectUnits buildings() {
		for (Unit unit : units.list()) {
			if (!unit.getType().isBuilding()) {
				filterOut(unit);
			}
		}
		return this;
	}

	// =========================================================
	// Hi-level methods

	public static SelectUnits ourBases() {
		return our().ofType(OUR_BASE);
	}

	public static SelectUnits ourWorkers() {
		return our().ofType(OUR_WORKER);
	}

	// =========================================================
	// Localization-related methods

	public Unit nearestTo(Unit unit) {
		return nearestTo(unit.getPosition());
	}

	public Unit nearestTo(Position position) {
		units.sortByDistanceTo(position, true);
		// return filterAllBut(units.first());
		return units.first();
	}

	public SelectUnits maxDistTo(Unit otherUnit, double maxDist) {
		for (Unit unit : units.list()) {
			// System.out.println("DIST = " +
			// PositionHelper.distanceBetween(unit, otherUnit));
			if (PositionHelper.getDistanceBetween(unit, otherUnit) > maxDist) {
				filterOut(unit);
			}
		}

		return this;
	}

	public Unit firstBase() {
		Units bases = ourBases().units();
		return bases.isEmpty() ? null : bases.first();
	}

}
