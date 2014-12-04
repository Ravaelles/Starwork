package starwork.units;

import java.util.Collection;

import starwork.helpers.UnitHelper;
import starwork.main.Starwork;
import starwork.map.AbstractPosition;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class SelectUnits {

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

		for (Unit unit : Starwork.getSelf().getUnits()) {
			if (unit.isAlive()) {
				units.addUnit(unit);
			}
		}

		return new SelectUnits(units);
	}

	public static SelectUnits enemy() {
		Units units = new Units();

		for (Unit unit : Starwork.getEnemy().getUnits()) {
			if (unit.isAlive()) {
				units.addUnit(unit);
			}
		}

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

	public static SelectUnits from(Units units) {
		SelectUnits selectUnits = new SelectUnits(units);
		return selectUnits;
	}

	public static SelectUnits from(Collection<Unit> unitsCollection) {
		Units units = new Units();
		units.addUnits(unitsCollection);

		SelectUnits selectUnits = new SelectUnits(units);
		return selectUnits;
	}

	// public static SelectUnits all() {
	// Units units = new Units();
	//
	// units.addUnits(Starwork.getSelf().getUnits());
	//
	// return new SelectUnits(units);
	// }

	// =========================================================
	// Get results

	public Units units() {
		return units;
	}

	public Collection<Unit> list() {
		return units().list();
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

	public SelectUnits ofType(UnitType type1Allowed, UnitType type2Allowed) {
		for (Unit unit : units.list()) {
			if (unit.getType() != type1Allowed && unit.getType() != type2Allowed) {
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
		return our().ofType(UnitHelper.TYPE_BASE);
	}

	public static SelectUnits ourWorkers() {
		return our().ofType(UnitHelper.TYPE_WORKER);
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

	public SelectUnits inRadius(double maxDist, AbstractPosition position) {
		for (Unit unit : units.list()) {
			if (position.distanceTo(unit) > maxDist) {
				filterOut(unit);
			}
		}

		return this;
	}

	public static Unit firstBase() {
		Units bases = ourBases().units();
		return bases.isEmpty() ? null : bases.first();
	}

}
