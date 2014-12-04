package starwork.units;

import java.util.HashMap;
import java.util.Set;

import starwork.helpers.UnitHelper;
import starwork.terran.TerranSupplyDepot;
import bwapi.Unit;
import bwapi.UnitType;

public class UnitCounter {

	private static HashMap<UnitType, Integer> numberOfUnits = new HashMap<>();

	// =========================================================

	public static void recalculateUnits() {
		resetUnits();
		countUnits();
	}

	// =========================================================

	public static int getNumberOfUnits(UnitType type) {
		return numberOfUnits.containsKey(type) ? numberOfUnits.get(type) : 0;
	}

	public static Set<UnitType> getExistingUnitType() {
		return numberOfUnits.keySet();
	}

	public static int getNumberOfUnitsCompleted(UnitType type) {
		int result = 0;
		for (Unit unit : SelectUnits.our().list()) {
			if (unit.isCompleted()) {
				result++;
			}
		}
		return result;
	}

	public static int getNumberOfBattleUnits() {
		return getNumberOfInfantryUnits() + +getNumberOfVehicleUnits()
				+ getNumberOfShipUnits();
	}

	public static int getNumberOfVehicleUnits() {
		return getNumberOfUnits(UnitType.Terran_Vulture)
				+ getNumberOfUnits(UnitType.Terran_Siege_Tank_Siege_Mode)
				+ getNumberOfUnits(UnitType.Terran_Siege_Tank_Tank_Mode)
				+ getNumberOfUnits(UnitType.Terran_Goliath);
	}

	public static int getNumberOfVehicleUnitsCompleted() {
		return getNumberOfUnitsCompleted(UnitType.Terran_Vulture)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Siege_Tank_Siege_Mode)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Siege_Tank_Tank_Mode)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Goliath);
	}

	public static int getNumberOfShipUnits() {
		return getNumberOfUnits(UnitType.Terran_Dropship)
				+ getNumberOfUnits(UnitType.Terran_Wraith)
				+ getNumberOfUnits(UnitType.Terran_Valkyrie)
				+ getNumberOfUnits(UnitType.Terran_Science_Vessel)
				+ getNumberOfUnits(UnitType.Terran_Battlecruiser);
	}

	public static int getNumberOfShipUnitsCompleted() {
		return getNumberOfUnitsCompleted(UnitType.Terran_Dropship)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Wraith)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Valkyrie)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Science_Vessel)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Battlecruiser);
	}

	public static int getNumberOfBattleUnitsCompleted() {
		return getNumberOfInfantryUnitsCompleted()
				+ +getNumberOfVehicleUnitsCompleted()
				+ getNumberOfShipUnitsCompleted();
	}

	public static boolean weHaveBuilding(UnitType unitType) {
		return getNumberOfUnits(unitType) > 0;
	}

	public static boolean weHaveBuildingFinished(UnitType unitType) {
		return getNumberOfUnitsCompleted(unitType) > 0;
	}

	public static int getNumberOfInfantryUnits() {
		return getNumberOfUnits(UnitType.Terran_Marine)
				+ getNumberOfUnits(UnitType.Terran_Firebat)
				+ getNumberOfUnits(UnitType.Terran_Ghost)
				+ getNumberOfUnits(UnitType.Terran_Medic);
	}

	public static int getNumberOfInfantryUnitsCompleted() {
		return getNumberOfUnitsCompleted(UnitType.Terran_Marine)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Firebat)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Ghost)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Medic);
	}

	public static boolean weHaveSupplyDepotFinished() {
		return getNumberOfUnitsCompleted(TerranSupplyDepot.getBuildingType()) > 0;
	}

	public static boolean weHaveSupplyDepot() {
		return getNumberOfUnits(TerranSupplyDepot.getBuildingType()) > 0;
	}

	public static int countAirUnitsNonValkyrie() {
		return getNumberOfShipUnits()
				- getNumberOfUnits(UnitType.Terran_Valkyrie);
	}

	public static int getNumberOfSupplyDepots() {
		return getNumberOfUnits(TerranSupplyDepot.getBuildingType());
	}

	public static int getNumberOfSupplyDepotsCompleted() {
		return getNumberOfUnitsCompleted(TerranSupplyDepot.getBuildingType());
	}

	public static int getNumberOfWorkers() {
		return getNumberOfUnits(UnitHelper.TYPE_WORKER);
	}

	// =========================================================

	private static void resetUnits() {
		numberOfUnits.clear();
	}

	private static void countUnits() {
		for (Unit unit : SelectUnits.our().list()) {
			if (!UnitsKnowledge.isUnitAlive(unit)) {
				continue;
			}
			numberOfUnits.put(
					unit.getType(),
					(numberOfUnits.containsKey(unit) ? numberOfUnits.get(unit
							.getType()) + 1 : 1));
		}
	}

}
