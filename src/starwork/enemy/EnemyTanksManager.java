package starwork.enemy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import starwork.units.SelectUnits;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class EnemyTanksManager {

	private static final double SAFE_DISTANCE_FROM_ENEMY_TANK = 14.5;
	private static final double ALWAYS_MOVE_TO_ENEMY_TANK_IF_CLOSER_THAN = 8;
	private static final double ALWAYS_ATTACK_ENEMY_TANK_IF_CLOSER_THAN = 1.1;
	private static final int MAXIMUM_TANKS_TO_ENGAGE_WITH_NORMAL_UNITS = 3;

	// =========================================================

	private static HashMap<Integer, Unit> enemyTanksDiscovered = new HashMap<>();
	private static HashMap<Integer, Position> enemyTanksPositions = new HashMap<>();

	// =========================================================

	public static boolean tryAvoidingEnemyTanks(Unit unit) {

		// Our own tanks should engage enemy tanks.
		if (unit.getType().isTank()) {
			return false;
		}

		Collection<Position> allKnownEnemyTanks = getEnemyTanksThatAreDangerouslyClose(unit);

		for (Position enemyTank : allKnownEnemyTanks) {
			double distance = enemyTank.distanceTo(unit);
			if (distance < ALWAYS_MOVE_TO_ENEMY_TANK_IF_CLOSER_THAN) {
				if (allKnownEnemyTanks.size() <= MAXIMUM_TANKS_TO_ENGAGE_WITH_NORMAL_UNITS) {
					if (distance < ALWAYS_ATTACK_ENEMY_TANK_IF_CLOSER_THAN) {
						// unit.setAiOrder("Attack tank");
						// UnitActions.attackTo(unit, enemyTank);
						return true;
					} else {
						// UnitActions.moveTo(unit, enemyTank);
						// unit.setAiOrder("Engage enemy tank");
						return true;
					}
				}
			}
		}

		int bonus = unit.getType().isVulture() ? 2 : 0;
		for (Position enemyTank : allKnownEnemyTanks) {
			if (enemyTank.distanceTo(unit) < SAFE_DISTANCE_FROM_ENEMY_TANK + bonus) {
				// UnitActions.moveAwayFrom(unit, enemyTank);
				// unit.setAiOrder("Avoid siege tank");
				return true;
			}
		}

		return false;
	}

	// =========================================================

	private static Collection<Position> getEnemyTanksThatAreDangerouslyClose(Unit unit) {
		ArrayList<Position> tanksInRange = new ArrayList<>();

		for (Object enemyTank : getEnemyTanksKnownIncludingSpeculations()) {
			if (isEnemyTankDangerouslyClose(unit, enemyTank)) {
				Position enemyTankPosition = (Position) enemyTank;
				tanksInRange.add(enemyTankPosition);
			}
		}

		return tanksInRange;
	}

	private static boolean isEnemyTankDangerouslyClose(Unit unit, Object enemy) {

		// Real unit
		if (enemy instanceof Unit) {
			Unit enemyTank = (Unit) enemy;

			if (enemyTank.getType().isTankSieged() || (!enemyTank.isSieged() && !enemyTank.isInterruptible())) {
				if (enemyTank.distanceTo(unit) < SAFE_DISTANCE_FROM_ENEMY_TANK) {
					return true;
				}
			}
		}

		// Speculated unit, with the last known position
		else {
			Position enemyTank = (Position) enemy;

			if (enemyTank.distanceTo(unit) < SAFE_DISTANCE_FROM_ENEMY_TANK) {
				return true;
			}
		}

		return false;
	}

	private static Collection<Object> getEnemyTanksKnownIncludingSpeculations() {
		ArrayList<Object> knownTanks = new ArrayList<>();

		// Add real tanks
		for (Unit tank : SelectUnits.enemy()
				.ofType(UnitType.Terran_Siege_Tank_Siege_Mode, UnitType.Terran_Siege_Tank_Tank_Mode).list()) {
			knownTanks.add(tank);
		}

		// knownTanks.addAll(enemyTanksPositions.values());
		for (Position tankPosition : enemyTanksPositions.values()) {
			knownTanks.add(tankPosition);
		}

		return knownTanks;
	}

	public static void updateTankPosition(Unit enemyTank) {
		enemyTanksDiscovered.put(enemyTank.getID(), enemyTank);
		enemyTanksPositions.put(enemyTank.getID(), new Position(enemyTank));
	}

	public static Collection<Position> getSpeculatedTankPositions() {
		return enemyTanksPositions.values();
	}

	public static void unitDestroyed(int unitID) {
		enemyTanksDiscovered.remove(unitID);
		enemyTanksPositions.remove(unitID);
	}

}
