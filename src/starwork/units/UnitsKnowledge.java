package starwork.units;

import java.util.ArrayList;

import starwork.helpers.UnitHelper;
import bwapi.Unit;

public class UnitsKnowledge {

	private static ArrayList<Unit> enemyUnits = new ArrayList<>();
	private static ArrayList<Unit> enemyUnitsDestroyed = new ArrayList<>();

	private static ArrayList<Unit> ourUnitsDestroyed = new ArrayList<>();

	// =========================================================

	public static boolean isUnitAlive(Unit unit) {
		if (UnitHelper.isPlayerUnit(unit)) {
			return !ourUnitsDestroyed.contains(unit);
		} else {
			return !enemyUnitsDestroyed.contains(unit);
		}
	}

	public static boolean unitDestroyed(Unit unit) {
		if (UnitHelper.isPlayerUnit(unit)) {
			return ourUnitsDestroyed.contains(unit);
		} else {
			return enemyUnitsDestroyed.contains(unit);
		}
	}

	public static void unitDiscovered(Unit unit) {
		enemyUnits.add(unit);
	}

}
