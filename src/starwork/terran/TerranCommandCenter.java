package starwork.terran;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import starwork.utils.CodeProfiler;
import starwork.utils.RUtilities;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;

public class TerranCommandCenter {

	public static final int MAX_WORKERS = 65;

	private static int MAX_DIST_OF_MINERAL_FROM_BASE = 12;
	private static final int ARMY_UNITS_PER_NEW_BASE = 10;
	private static final int MIN_WORKERS = 19;
	public static final int WORKERS_PER_GEYSER = 4;

	private static Position _secondBase = null;
	private static Position _cachedNextBaseTile = null;
	private static int _lastTimeCalculatedTileForBase = -1;

	private static final UnitType buildingType = UnitType.Terran_Command_Center;

	// =========================================================

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.construct(xvr, buildingType);
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static boolean shouldBuild() {
		// if (Constructing.weAreBuilding(buildingType)) {
		// return ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		// }

		int bases = UnitCounter.getNumberOfUnits(buildingType);
		int factories = TerranFactory.getNumberOfUnitsCompleted();
		// int barracks =
		// UnitCounter.getNumberOfUnits(TerranBarracks.getBuildingType());
		// int barracksCompleted =
		// UnitCounter.getNumberOfUnitsCompleted(TerranBarracks
		// .getBuildingType());
		int battleUnits = UnitCounter.getNumberOfBattleUnits();

		// =========================================================

		// STRATEGY: Offensive Bunker
		if (TerranOffensiveBunker.isStrategyActive()) {
			if ((factories < 3 || TerranSupplyDepot.getNumberOfUnits() == 0)
					&& !xvr.canAfford(500)) {
				return ShouldBuildCache.cacheShouldBuildInfo(buildingType,
						false);
			}
		}

		// =========================================================

		boolean factoryFirstConditionOkay = xvr.canAfford(384)
				|| (!TerranFactory.FORCE_FACTORY_BEFORE_SECOND_BASE || TerranFactory
						.getNumberOfUnits() > 0);

		// =========================================================

		if (bases <= 1
				&& factories > 0
				&& (battleUnits >= ArmyCreationManager.MINIMUM_MARINES || xvr
						.canAfford(358))
				&& (TerranBunker.getNumberOfUnitsCompleted() >= TerranBunker.MAX_STACK || xvr
						.canAfford(384)) && factoryFirstConditionOkay) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
		}

		if (xvr.canAfford(550) && battleUnits >= 18) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
		}

		if (TimePresident.getTimeSeconds() >= 390 && bases <= 1
				&& !Constructing.weAreBuilding(UnitManager.BASE)
				&& battleUnits >= ArmyCreationManager.MINIMUM_MARINES) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
		}

		if (bases >= 2 && battleUnits <= bases * 7 && !xvr.canAfford(420)) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		}

		if (battleUnits < StrategyManager.getMinBattleUnits() + 2
				&& !xvr.canAfford(500)) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		}

		if (xvr.canAfford(600)) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
		}

		// // Initially, we must wait to have at least 3 barracks to build first
		// // base.
		if (bases == 1) {
			// if (barracksCompleted <= 2 && !xvr.canAfford(500)) {
			// return ShouldBuildCache.cacheShouldBuildInfo(buildingType,
			// false);
			// }
		}

		// More than one base
		else {

			// But if we already have another base...
			if ((bases * ARMY_UNITS_PER_NEW_BASE > battleUnits && !xvr
					.canAfford(500))) {
				return ShouldBuildCache.cacheShouldBuildInfo(buildingType,
						false);
			}
		}

		return ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static void act() {
		for (Unit base : xvr.getUnitsOfTypeCompleted(buildingType)) {
			TerranCommandCenter.act(base);
		}
	}

	public static void act(Unit base) {

		// Calculate number of workers at nearby geyser, if there's too many
		// of them, send some of them away
		checkNumberOfGasWorkersAt(base);

		// If we don't have enough workers at our base
		checkNumberOfWorkersOverallAtBase(base);

		// =========================================
		// ======== SSCAI FIX: Remove 0 minerals ===
		checkIfRemoveZeroMineralsCrystal(base);
	}

	// =========================================================

	private static boolean shouldBuildWorkers(Unit base) {
		int workers = UnitCounter.getNumberOfUnits(UnitManager.WORKER);
		int depots = UnitCounter.getNumberOfUnits(TerranSupplyDepot
				.getBuildingType());
		boolean weAreBuildingDepot = Constructing
				.weAreBuilding(TerranSupplyDepot.getBuildingType());

		// =========================================================
		// ==== SPECIFIC STRATEGIES ================================
		// =========================================================

		// STRATEGY: Anti-Zerg Rush
		if (XVR.isEnemyZerg()) {
			if (workers >= 7
					&& TerranBunker.getNumberOfUnits() == 0
					&& !Constructing.weAreBuilding(TerranBunker
							.getBuildingType())) {
				return false;
			}
		}

		// STRATEGY: Offensive Bunker
		if (TerranOffensiveBunker.isStrategyActive()) {

			// Initially build barracks, not worker
			if (workers == 4 && TerranBarracks.getNumberOfUnits() > 0) {
				return true;
			}

			// Priority for everything else e.g. force building Factory early
			if (workers >= 10 && TerranFactory.getNumberOfUnits() == 0) {
				return false;
			}

			// Priority for units training
			if (workers >= 4 && UnitCounter.getNumberOfBattleUnits() < 2
					&& !xvr.canAfford(130)) {
				return false;
			}

			// Priority for barracks
			if (TerranBarracks.shouldBuild() && !xvr.canAfford(210)) {
				return false;
			}

			// Priority for bunker
			if ((TerranBunker.shouldBuild() || TerranBunker.getNumberOfUnits() == 0)
					&& !xvr.canAfford(150)) {
				return false;
			}
		}

		// =========================================================
		// ==== End of SPECIFIC STRATEGIES =========================
		// =========================================================

		if (workers >= MAX_WORKERS) {
			return false;
		}

		// =========================================================

		if (TerranBunker.getNumberOfUnits() < TerranBunker.GLOBAL_MAX_BUNKERS
				&& TerranBunker.shouldBuild()
				&& (!xvr.canAfford(134) && workers >= 13)) {
			return false;
		}

		// =========================================================

		// Quick FIRST DEPOT
		// if (BotStrategyManager.isExpandWithbunkers()) {
		if (workers == 9 && (depots == 0 || !weAreBuildingDepot)
				&& !xvr.canAfford(150)) {
			return false;

		}
		// }

		// Quick FIRST BUNKER
		if (BotStrategyManager.isExpandWithBunkers()) {
			if (depots == 2) {
				if (!Constructing.weAreBuilding(TerranBunker.getBuildingType())
						&& !xvr.canAfford(200) && workers >= 20) {
					return false;
				} else {
					return xvr.canAfford(184);
				}
			}
		}

		if (workers < MIN_WORKERS && xvr.canAfford(58)) {
			return true;
		}

		int bases = UnitCounter.getNumberOfUnits(UnitManager.BASE);
		int bunkers = UnitCounter.getNumberOfUnits(TerranBunker
				.getBuildingType());

		if (workers > bases * 25) {
			return false;
		}

		if (BotStrategyManager.isExpandWithBunkers()) {
			if (workers >= MIN_WORKERS
					&& bunkers < TerranBunker.GLOBAL_MAX_BUNKERS) {
				return false;
			}
		}

		// if (UnitCounter.getNumberOfBattleUnits() < 3 * workers) {
		// return false;
		// }

		int workersNearBase = getNumberOfWorkersNearBase(base);
		double existingToOptimalRatio = (double) workersNearBase
				/ getOptimalMineralGatherersAtBase(base);

		// If we have only one base and already some workers, promote more
		// gateways
		int barracks = UnitCounter.getNumberOfUnits(TerranBarracks
				.getBuildingType());
		if (UnitCounter.getNumberOfUnits(buildingType) == 1
				&& barracks < TerranBarracks.MAX_BARRACKS) {
			if (existingToOptimalRatio > 0.5 + 0.12 * barracks) {
				return false;
			}
		}

		// Check if need to build some building. If so then check if we can
		// afford to train worker, we don't want to steal resources.
		int[] shouldBuildAnyBuilding = Constructing.shouldBuildAnyBuilding();
		if (shouldBuildAnyBuilding != null) {

			// If we can't afford extra 50 minerals, disallow building of
			// worker. BUT if we don't have even 40% of mineral gatherers then
			// allow.
			if (!xvr.canAfford(shouldBuildAnyBuilding[0] + 100)
					&& existingToOptimalRatio > 0.4) {
				return false;
			}
		}

		return (existingToOptimalRatio < 1 ? true : false);
	}

	// =========================================================

	private static void checkNumberOfWorkersOverallAtBase(Unit base) {
		if (shouldBuildWorkers(base)) {
			if (base.getTrainingQueueSize() == 0) {
				xvr.buildUnit(base, UnitManager.WORKER);
			}
		}

		// We have more than optimal number workers at base
		else {
			trySendingWorkersToOtherBaseFrom(base);
		}
	}

	private static void trySendingWorkersToOtherBaseFrom(Unit base) {
		double mineralWorkersToOptimalRatio = defineNumberOfWorkersToOptimalNumberRatioFor(base);

		for (Unit otherBase : getBases()) {
			boolean baseReady = (otherBase.isCompleted() || BuildingManager
					.countConstructionProgress(buildingType) >= 86);

			if (!otherBase.equals(base) && baseReady
					&& otherBase.distanceTo(base) <= 35) {
				double otherBaseRatio = defineNumberOfWorkersToOptimalNumberRatioFor(otherBase);
				// System.out.println("THEY/WE: " + mineralWorkersToOptimalRatio
				// + " / "
				// + otherBaseRatio);
				if (mineralWorkersToOptimalRatio - 0.13 >= otherBaseRatio) {
					sendOneWorkerFromTo(base, otherBase);
					return;
				}
			}
		}
	}

	private static void sendOneWorkerFromTo(Unit base, Unit otherBase) {
		Unit chosenWorker = null;
		for (Unit worker : getWorkersNearBase(base)) {
			if (worker.isGatheringMinerals() && !worker.isCarryingMinerals()) {
				chosenWorker = worker;
				break;
			}
		}

		if (chosenWorker != null) {
			UnitActions.moveTo(chosenWorker, otherBase);
		}
	}

	private static double defineNumberOfWorkersToOptimalNumberRatioFor(Unit base) {
		// int gasGatherersForBase = getNumberOfGasGatherersForBase(base);
		// int numRequiredWorkers = WORKERS_PER_GEYSER - gasGatherersForBase;
		int optimalMineralWorkersAtBase = getOptimalMineralGatherersAtBase(base)
				- WORKERS_PER_GEYSER;
		double mineralWorkersToOptimalRatio = (double) getWorkersNearBase(base)
				.size() / optimalMineralWorkersAtBase;
		return mineralWorkersToOptimalRatio;
	}

	private static void checkNumberOfGasWorkersAt(Unit base) {
		if (!TerranCommandCenter.isExistingCompletedAssimilatorNearBase(base)) {
			return;
		}

		int gasGatherersForBase = getNumberOfGasGatherersForBase(base);
		if (gasGatherersForBase > WORKERS_PER_GEYSER) {
			int overLimitWorkers = gasGatherersForBase - WORKERS_PER_GEYSER;

			// Check whether the geyser isn't depleted
			if (xvr.getUnitOfTypeNearestTo(TerranRefinery.getBuildingType(),
					base).getResources() < 40) {
				overLimitWorkers = gasGatherersForBase - 1;
			}

			// We can send workers only if there's another base
			if (overLimitWorkers > 0) {
				haveOverLimitGasWorkers(base, overLimitWorkers);
			}
		} else {
			int numRequiredWorkers = WORKERS_PER_GEYSER - gasGatherersForBase;
			int optimalMineralWorkersAtBase = getOptimalMineralGatherersAtBase(base)
					- WORKERS_PER_GEYSER;
			double mineralWorkersToOptimalRatio = (double) getNumberOfMineralGatherersForBase(base)
					/ optimalMineralWorkersAtBase;
			if (mineralWorkersToOptimalRatio < 0.5) {
				numRequiredWorkers--;
			}
			if (mineralWorkersToOptimalRatio < 0.6) {
				numRequiredWorkers--;
			}
			if (mineralWorkersToOptimalRatio < 0.7) {
				numRequiredWorkers--;
			}
			if (numRequiredWorkers <= 0) {
				numRequiredWorkers = 1;
			}

			ArrayList<Unit> gatherers = getMineralWorkersNearBase(base);
			for (int i = 0; i < numRequiredWorkers && i < gatherers.size(); i++) {
				Unit gathererToAssign = gatherers.get(i);
				WorkerManager.gatherResources(gathererToAssign, base);
			}
		}
	}

	private static void haveOverLimitGasWorkers(Unit base, int overLimitWorkers) {
		ArrayList<Unit> gatherers = getGasWorkersNearBase(base);
		// for (int i = 0; i < overLimitWorkers && i < gatherers.size();
		// i++) {
		// UnitActions.moveTo(gatherers.get(i), base);
		// }
		ArrayList<Unit> mineralsInNeihgborhood = xvr
				.getUnitsOfGivenTypeInRadius(UnitType.Resource_Mineral_Field,
						25, base, false);
		if (!mineralsInNeihgborhood.isEmpty()) {
			for (Unit worker : gatherers) {
				// WorkerManager
				// .forceGatherMinerals(
				// worker,
				// (Unit) RUtilities
				// .getRandomListElement(mineralsInNeihgborhood));
				WorkerManager.gatherResources(worker, base);
			}
		}
	}

	private static void checkIfRemoveZeroMineralsCrystal(Unit base) {
		final int SEARCH_IN_RADIUS = 30;
		final int ACT_IF_AT_LEAST_N_WORKERS = 20;

		// Create list of mineral gatheres near base. It's essential not to use
		// defined method, as we need to significantly increase seek range.
		if (UnitCounter.getNumberOfUnits(UnitManager.WORKER) < ACT_IF_AT_LEAST_N_WORKERS) {
			return;
		}

		ArrayList<Unit> workers = xvr.getWorkers();
		ArrayList<Unit> mineralWorkersNearBase = new ArrayList<>();
		for (Unit worker : workers) {
			if (worker.isGatheringMinerals() && !worker.isConstructing()
					&& xvr.getDistanceBetween(base, worker) <= SEARCH_IN_RADIUS) {
				mineralWorkersNearBase.add(worker);
			}
		}

		// If we have at least X mineral workers near base, send one of them to
		// gather this lonely mineral-obstacle
		if (mineralWorkersNearBase.size() >= ACT_IF_AT_LEAST_N_WORKERS) {
			ArrayList<Unit> mineralsAroundTheBase = xvr
					.getUnitsOfGivenTypeInRadius(
							UnitType.Resource_Mineral_Field, SEARCH_IN_RADIUS,
							base, false);

			for (Unit mineral : mineralsAroundTheBase) {
				if (mineral.getResources() == 0 && !mineral.isBeingGathered()) {
					int max = 1;
					Unit unitToUse = mineralWorkersNearBase.get(RUtilities
							.rand(0, max));
					WorkerManager.forceGatherMinerals(unitToUse, mineral);
				}
			}
		}
	}

	/** Find building tile for new base. */
	public static Position findTileForNextBase(boolean forceNewSolution) {
		if (SelectUnits.firstBase() == null) {
			return null;
		}

		// Try to get cached value
		if (_cachedNextBaseTile != null && !forceNewSolution) {
			return _cachedNextBaseTile;
		}

		// Make sure you're not calculating base location all the time
		if (forceNewSolution) {
			int now = TimePresident.getTimeSeconds();
			if (_lastTimeCalculatedTileForBase != -1
					&& now - _lastTimeCalculatedTileForBase <= 3) {
				return _cachedNextBaseTile;
			}
			_lastTimeCalculatedTileForBase = now;
		}

		// ===============================
		BaseLocation nearestFreeBaseLocation = getNearestFreeBaseLocation();
		if (nearestFreeBaseLocation != null) {
			Position point = nearestFreeBaseLocation;

			CodeProfiler.startMeasuring("New base");
			_cachedNextBaseTile = Constructing.getLegitTileToBuildNear(
					BuilderSelector.getRandomWorker(), buildingType, point, 0,
					10);
			CodeProfiler.endMeasuring("New base");
		} else {
			System.out.println("Error! No place for next base!");
			_cachedNextBaseTile = null;
		}

		return _cachedNextBaseTile;
	}

	private static BaseLocation getNearestFreeBaseLocation() {
		Unit expansionCenter = SelectUnits.firstBase();
		// if (!xvr.getLastBase().equals(SelectUnits.firstBase())) {
		// expansionCenter = xvr.getLastBase();
		// }
		if (expansionCenter == null) {
			return null;
		}
		Map map = xvr.getBwapi().getMap();
		BaseLocation nearestFreeBaseLocation = null;
		double nearestDistance = 999999;
		for (BaseLocation location : xvr.getBwapi().getMap().getBaseLocations()) {

			// If there's already a base there don't build. Check for both our
			// and enemy bases.
			if (existsBaseNear(location)) {
				continue;
			}

			// Check if the new base is connected to the main base by land.
			// Region newBaseRegion = xvr.getBwapi().getMap()
			// .getRegion(location.getX(), location.getY());
			// if (!map.isConnected(location, expansionCenter)) {
			// continue;
			// }

			// Look for for the closest base and remember it.
			double distance = map.getGroundDistance(location, expansionCenter) / 32;
			if (distance < 0) { // -1 means there's no path
				continue;
			}

			// double distance = xvr.getDistanceBetween(location.getX(),
			// location.getY(), mainBase.getX(), mainBase.getY());
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearestFreeBaseLocation = location;
			}
		}

		return nearestFreeBaseLocation;
	}

	public static boolean existsBaseNear(Position point) {
		return existsBaseNear(point.getX(), point.getY());
	}

	public static boolean existsBaseNear(int x, int y) {
		for (Unit unit : xvr.getUnitsInRadius(x, y, 14)) {
			if (unit.getType().isBase()) {
				if (!unit.isEnemy() && !unit.isExists()) {
					return false;
				} else {
					return true;
				}
			}
		}

		return false;
	}

	private static int getNumberOfWorkersNearBase(Unit base) {
		return xvr.countUnitsOfGivenTypeInRadius(UnitManager.WORKER, 15,
				base.getX(), base.getY(), true);
	}

	public static int getOptimalMineralGatherersAtBase(Unit base) {
		int numberOfMineralsNearbyBase = xvr.countMineralsInRadiusOf(12,
				base.getX(), base.getY());
		return (int) (2.59 * numberOfMineralsNearbyBase) + WORKERS_PER_GEYSER;
	}

	public static Unit getNearestBaseForUnit(Position point) {
		double nearestDistance = 9999999;
		Unit nearestBase = null;

		for (Unit base : xvr.getUnitsOfTypeCompleted(buildingType)) {
			double distance = xvr.getDistanceBetween(base, point);
			if (distance < nearestDistance) {
				distance = nearestDistance;
				nearestBase = base;
			}
		}

		return nearestBase;
	}

	public static Unit getNearestMineralGathererForUnit(Unit base) {
		double nearestDistance = 9999999;
		Unit nearestBase = null;

		for (Unit scv : xvr.getWorkers()) {
			if (scv.isGatheringMinerals()) {
				double distance = xvr.getDistanceBetween(scv, base);
				if (distance < nearestDistance) {
					distance = nearestDistance;
					nearestBase = scv;
				}
			}
		}

		return nearestBase;
	}

	public static int getNumberOfGasGatherersForBase(Unit base) {
		int result = 0;
		int MAX_DISTANCE = 10;

		for (Unit worker : xvr.getWorkers()) {
			if (worker.isGatheringGas()) {
				double distance = xvr.getDistanceBetween(worker, base);
				if (distance < MAX_DISTANCE) {
					result++;
				}
			}
		}

		return result;
	}

	public static int getNumberOfMineralGatherersForBase(Unit base) {
		int result = 0;
		int MAX_DISTANCE = 12;

		for (Unit worker : xvr.getWorkers()) {
			if (worker.isGatheringMinerals()) {
				double distance = xvr.getDistanceBetween(worker, base);
				if (distance < MAX_DISTANCE) {
					result++;
				}
			}
		}

		return result;
	}

	public static ArrayList<Unit> getMineralsNearBase(Unit base) {
		return getMineralsNearBase(base, MAX_DIST_OF_MINERAL_FROM_BASE);
	}

	public static ArrayList<Unit> getMineralsNearBase(Unit base, int maxDist) {
		HashMap<Unit, Double> minerals = new HashMap<Unit, Double>();

		for (Unit mineral : xvr.getMineralsUnits()) {
			double distance = xvr.getDistanceBetween(mineral, base);
			// double distance = xvr.getBwapi().getMap()
			// .getGroundDistance(mineral, base) / 32;
			if (distance < 0) {
				continue;
			}

			if (distance <= maxDist && mineral.getResources() > 1) {
				minerals.put(mineral, distance);
			}
		}

		ArrayList<Unit> sortedList = new ArrayList<Unit>();
		sortedList.addAll(RUtilities.sortByValue(minerals, true).keySet());
		return sortedList;
	}

	public static ArrayList<Unit> getWorkersNearBase(Unit nearestBase) {
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (Unit worker : xvr.getWorkers()) {
			if (xvr.getDistanceBetween(nearestBase, worker) < 20) {
				units.add(worker);
			}
		}
		return units;
	}

	public static ArrayList<Unit> getMineralWorkersNearBase(Unit nearestBase) {
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (Unit worker : xvr.getWorkers()) {
			if (worker.isGatheringMinerals()
					&& xvr.getDistanceBetween(nearestBase, worker) < 20) {
				units.add(worker);
			}
		}
		return units;
	}

	public static ArrayList<Unit> getGasWorkersNearBase(Unit nearestBase) {
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (Unit worker : xvr.getWorkers()) {
			if (worker.isGatheringGas()
					&& xvr.getDistanceBetween(nearestBase, worker) < 12) {
				units.add(worker);
			}
		}
		return units;
	}

	public static void initialMineralGathering() {
		ArrayList<Unit> minerals = getMineralsNearBase(SelectUnits.firstBase());

		int counter = 0;
		for (Unit unit : getWorkersNearBase(SelectUnits.firstBase())) {
			WorkerManager.forceGatherMinerals(unit, minerals.get(counter));

			counter++;
		}
	}

	public static ArrayList<Unit> getBases() {
		return xvr.getUnitsOfType(buildingType);
	}

	public static UnitType getBuildingType() {
		return buildingType;
	}

	public static boolean isExistingCompletedAssimilatorNearBase(
			Unit nearestBase) {
		ArrayList<Unit> inRadius = xvr.getUnitsOfGivenTypeInRadius(
				TerranRefinery.getBuildingType(), 12, nearestBase, true);

		if (!inRadius.isEmpty() && inRadius.get(0).isCompleted()
				&& inRadius.get(0).getResources() > 50) {
			return true;
		} else {
			return false;
		}
	}

	public static Unit getExistingCompletedAssimilatorNearBase(Unit nearestBase) {
		ArrayList<Unit> inRadius = xvr.getUnitsOfGivenTypeInRadius(
				TerranRefinery.getBuildingType(), 12, nearestBase, true);

		if (!inRadius.isEmpty() && inRadius.get(0).isCompleted()
				&& inRadius.get(0).getResources() > 50) {
			return inRadius.get(0);
		} else {
			return null;
		}
	}

	public static Unit getRandomBase() {
		ArrayList<Unit> bases = getBases();
		if (bases.isEmpty()) {
			return null;
		}
		return (Unit) RUtilities.randomElement(bases);
	}

	public static Position getSecondBaseLocation() {
		if (_secondBase != null) {
			return _secondBase;
		} else {
			_secondBase = TerranCommandCenter.findTileForNextBase(true);
			return _secondBase;
		}
	}

	public static void updateNextBaseToExpand() {
		findTileForNextBase(true);
	}

	public static int getNumberOfUnits() {
		return UnitCounter.getNumberOfUnits(buildingType);
	}

	public static int getNumberOfUnitsCompleted() {
		return UnitCounter.getNumberOfUnitsCompleted(buildingType);
	}

	public static Position get_cachedNextBaseTile() {
		return _cachedNextBaseTile;
	}

}
