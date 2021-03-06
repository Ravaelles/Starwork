package starwork.army;

import java.util.HashMap;

import bwapi.Unit;
import bwapi.UnitType;

public class SiegeTankManager {

	// private static final int MIN_TIME_TO_UNSIEGE = 20;

	private static UnitType unitType = UnitType.Terran_Siege_Tank_Tank_Mode;

	// =========================================================

	static class TargettingDetails {
		// static boolean _isUnitWhereItShouldBe;
		// static MapPoint _properPlace;
		static Unit _nearestEnemy;
		static Unit _nearestEnemyBuilding;
		static double _nearestEnemyDist;

		/**
		 * If a tank is in this map it means it is considering unsieging, but we
		 * should wait some time before this happens.
		 */
		static HashMap<Unit, Integer> unsiegeIdeasMap = new HashMap<>();
	}

	// =========================================================

	public static void act(Unit unit) {
		// TargettingDetails._properPlace = unit.getProperPlaceToBe();
		// updateProperPlaceToBeForTank(unit);
		// TargettingDetails._isUnitWhereItShouldBe =
		// TargettingDetails._properPlace == null
		// || TargettingDetails._properPlace.distanceTo(unit) <= 3;
		// TargettingDetails._nearestEnemy = xvr.getNearestGroundEnemy(unit);
		// TargettingDetails._nearestEnemyBuilding =
		// MapExploration.getNearestEnemyBuilding(unit);
		// TargettingDetails._nearestEnemyDist = TargettingDetails._nearestEnemy
		// != null ? TargettingDetails._nearestEnemy
		// .distanceTo(unit) : -1;

		// if (unit.isSieged()) {
		// actWhenSieged(unit);
		// } else {
		// actWhenInNormalMode(unit);
		// }
	}

	// =========================================================

	// private static void actWhenInNormalMode(Unit unit) {
	// if (shouldSiege(unit) && notTooManySiegedUnitHere(unit) &&
	// didntJustUnsiege(unit)) {
	// unit.siege();
	// return;
	// }
	//
	// if (mustSiege(unit)) {
	// unit.siege();
	// return;
	// }
	//
	// int enemiesVeryClose = xvr.countUnitsEnemyInRadius(unit, 4);
	// int enemiesInSight = xvr.countUnitsEnemyInRadius(unit, 13);
	//
	// boolean isEnemyInSight = enemiesInSight >= 2;
	// boolean isEnemyVeryClose = enemiesVeryClose != 0;
	// if (isEnemyInSight && !isEnemyVeryClose) {
	// unit.siege();
	// if (isEnemyInSight) {
	// unit.setAiOrder("Enemy in sight: Siege");
	// }
	// if (!isEnemyVeryClose) {
	// unit.setAiOrder("Enemy close: Siege");
	// }
	// return;
	// }
	//
	// UnitActions.attackTo(unit,
	// ArmyRendezvousManager.getRendezvousPointForTanks());
	// unit.setAiOrder("Go");
	// }
	//
	// private static boolean mustSiege(Unit unit) {
	//
	// // If there's enemy building in range, siege.
	// if (TargettingDetails._nearestEnemyBuilding != null
	// && TargettingDetails._nearestEnemyBuilding.distanceTo(unit) <= 10.4) {
	// unit.setAiOrder("Enemy building: Siege");
	// return true;
	// }
	//
	// // If there's enemy Siege Tank in range, siege.
	// Collection<Unit> enemyArmyUnits = xvr.getEnemyArmyUnits();
	// if
	// (!xvr.getUnitsOfGivenTypeInRadius(UnitTypes.Terran_Siege_Tank_Siege_Mode,
	// 11.1, unit, enemyArmyUnits)
	// .isEmpty()
	// ||
	// !xvr.getUnitsOfGivenTypeInRadius(UnitTypes.Terran_Siege_Tank_Tank_Mode,
	// 10.9, unit, enemyArmyUnits)
	// .isEmpty()) {
	// unit.setAiOrder("Enemy tank: Siege");
	// return true;
	// }
	//
	// return false;
	// }
	//
	// private static boolean didntJustUnsiege(Unit unit) {
	// return unit.getLastTimeSieged() + 5 <= xvr.getTimeSeconds();
	// }
	//
	// private static void actWhenSieged(Unit unit) {
	// unit.setLastTimeSieged(xvr.getTimeSeconds());
	//
	// Unit nearestEnemy = xvr.getNearestGroundEnemy(unit);
	// double nearestEnemyDist = nearestEnemy != null ?
	// nearestEnemy.distanceTo(unit) : -1;
	//
	// boolean enemyVeryClose = nearestEnemyDist >= 0 && nearestEnemyDist <=
	// 2.5;
	// int enemiesVeryClose = xvr.countUnitsEnemyInRadius(unit, 4);
	//
	// boolean enemyAlmostInSight = nearestEnemyDist >= 0 && nearestEnemyDist <=
	// 14;
	// boolean neighborhoodDangerous = enemyVeryClose && unit.getStrengthRatio()
	// < 1.8;
	// // boolean chancesRatherBad = unit.getStrengthRatio() < 1.1;
	// if (neighborhoodDangerous && enemyVeryClose && (enemiesVeryClose >= 2 ||
	// nearestEnemy.getMaxHP() > 50)) {
	// unit.setAiOrder("Unsiege: Urgent");
	// unit.unsiege();
	// return;
	// }
	//
	// // If tank from various reasons shouldn't be here, unsiege.
	// if (!enemyAlmostInSight && !unit.isStartingAttack() && !shouldSiege(unit)
	// && !mustSiege(unit)) {
	// infoTankIsConsideringUnsieging(unit);
	// }
	//
	// if (isUnsiegingIdeaTimerExpired(unit)) {
	// unit.setAiOrder("Unsiege: OK");
	// if (!mustSiege(unit) && !shouldSiege(unit)) {
	// unit.unsiege();
	// }
	// }
	// }
	//
	// private static boolean isUnsiegingIdeaTimerExpired(Unit unit) {
	// if (TargettingDetails.unsiegeIdeasMap.containsKey(unit)) {
	// if (xvr.getTimeSeconds() - TargettingDetails.unsiegeIdeasMap.get(unit) >=
	// MIN_TIME_TO_UNSIEGE) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public static void infoTankIsConsideringUnsieging(Unit unit) {
	// if (!TargettingDetails.unsiegeIdeasMap.containsKey(unit)) {
	// unit.setAiOrder("Consider unsieging");
	// TargettingDetails.unsiegeIdeasMap.put(unit, xvr.getTimeSeconds());
	// }
	// }
	//
	// private static boolean shouldSiege(Unit unit) {
	// boolean isEnemyNearShootRange = (TargettingDetails._nearestEnemyDist > 0
	// && TargettingDetails._nearestEnemyDist <=
	// (TargettingDetails._nearestEnemy
	// .getType().isBuilding() ? 10.6 : 13));
	//
	// // Check if should siege, based on unit proper place to be (e.g. near
	// // the bunker), but consider the neighborhood, if it's safe etc.
	// if (isTankWhereItShouldBe(unit) && notTooManySiegedInArea(unit) ||
	// isEnemyNearShootRange) {
	// if (canSiegeInThisPlace(unit) && isNeighborhoodSafeToSiege(unit)
	// && (!isNearMainBase(unit) || isNearBunker(unit))) {
	// unit.setAiOrder("Should Siege");
	// return true;
	// }
	// }
	//
	// // If there's an enemy in the range of shoot and there are some other
	// // units around this tank, then siege.
	// int oursNearby = xvr.countUnitsOursInRadius(unit, 7);
	// if (isEnemyNearShootRange && oursNearby >= 5) {
	// unit.setAiOrder("Enemy & support: Siege");
	// return true;
	// }
	//
	// // If there's enemy building in range, siege.
	// if (TargettingDetails._nearestEnemyBuilding != null
	// && TargettingDetails._nearestEnemyBuilding.distanceTo(unit) <= 10.5 &&
	// oursNearby >= 2) {
	// unit.setAiOrder("Enemy building: Siege");
	// return true;
	// }
	//
	// return false;
	// }
	//
	// private static boolean isNearMainBase(Unit unit) {
	// return unit.distanceTo(SelectUnits.firstBase()) > 28;
	// }
	//
	// private static boolean isNearBunker(Unit unit) {
	// Unit nearestBunker =
	// xvr.getUnitOfTypeNearestTo(TerranBunker.getBuildingType(), unit);
	// return nearestBunker != null && nearestBunker.distanceTo(unit) < 4.4;
	// }
	//
	// private static boolean isTankWhereItShouldBe(Unit unit) {
	// MapPoint rendezvous = ArmyRendezvousManager.getRendezvousPointForTanks();
	// if (rendezvous != null) {
	// return unit.distanceTo(rendezvous) < 4.7;
	// } else {
	// return true;
	// }
	// }
	//
	// private static boolean notTooManySiegedInArea(Unit unit) {
	// // return xvr.countUnitsOfGivenTypeInRadius(type, tileRadius, point,
	// // onlyMyUnits);
	// return true;
	// }
	//
	// private static boolean notTooManySiegedUnitHere(Unit unit) {
	// Unit nearBuilding = xvr.getUnitNearestFromList(unit,
	// xvr.getUnitsBuildings());
	// boolean isNearBuilding = nearBuilding != null &&
	// nearBuilding.distanceTo(unit) <= 7;
	//
	// ChokePoint nearChoke = MapExploration.getNearestChokePointFor(unit);
	// boolean isNearChoke = nearChoke != null &&
	// unit.distanceToChokePoint(nearChoke) <= 3;
	//
	// if (isNearBuilding || isNearChoke) {
	// return
	// xvr.countUnitsOfGivenTypeInRadius(UnitTypes.Terran_Siege_Tank_Siege_Mode,
	// 2.7, unit, true) <= 2;
	// } else {
	// return false;
	// }
	// }
	//
	// private static boolean isNeighborhoodSafeToSiege(Unit unit) {
	// if (xvr.countUnitsEnemyInRadius(unit, 4) <= 0) {
	// return true;
	// }
	// return false;
	// }
	//
	// private static boolean canSiegeInThisPlace(Unit unit) {
	// ChokePoint nearestChoke = MapExploration.getNearestChokePointFor(unit);
	//
	// // Don't siege in the choke point near base, or you'll... lose.
	// if (nearestChoke.getRadiusInTiles() <= 3 &&
	// unit.distanceToChokePoint(nearestChoke) <= 3) {
	// Unit nearestBase = xvr.getUnitOfTypeNearestTo(UnitManager.BASE, unit);
	// if (nearestBase != null && nearestBase.distanceTo(unit) <= 20) {
	// return false;
	// }
	// }
	//
	// return true;
	// }
	//
	// public static UnitTypes getUnitType() {
	// return unitType;
	// }

}
