package starwork.terran;

import java.util.Collection;
import java.util.Iterator;

import starwork.economy.EconomyMinister;
import starwork.economy.constructing.BuilderSelector;
import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import starwork.exploration.MapExploration;
import starwork.helpers.UnitHelper;
import starwork.main.Starwork;
import starwork.map.AbstractPosition;
import starwork.strategy.BotStrategyManager;
import starwork.units.SelectUnits;
import starwork.units.UnitCounter;
import starwork.utils.RUtilities;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.Chokepoint;

public class TerranSupplyDepot {

	// private static int INITIAL_DEPOT_MIN_DIST_FROM_BASE = 6;
	// private static int INITIAL_DEPOT_MAX_DIST_FROM_BASE = 18;
	private static int DEPOT_FROM_DEPOT_MIN_DISTANCE = 0;
	private static int DEPOT_FROM_DEPOT_MAX_DISTANCE = 7;

	private static final UnitType unitType = UnitType.Terran_Supply_Depot;

	// =========================================================

	public static boolean shouldBuild() {
		boolean weAreBuilding = Constructing.weAreBuilding(unitType);

		int freeSupply = EconomyMinister.getFreeSupply();
		int totalSupply = EconomyMinister.getTotalSupply();
		int depots = TerranSupplyDepot.getNumberOfUnits();
		int barracks = TerranBarracks.getNumberOfUnits();
		int workers = UnitCounter.getNumberOfUnits(UnitHelper.TYPE_WORKER);
		int engineeringBays = TerranEngineeringBay.getNumberOfUnits();

		// =========================================================

		if (totalSupply >= 10 && totalSupply < 200 && freeSupply <= 3) {
			return ShouldBuildCache.cacheShouldBuildInfo(unitType, true);
		}

		if (TerranBunker.getNumberOfUnits() < TerranBunker.GLOBAL_MAX_BUNKERS && TerranBunker.shouldBuild()
				&& !EconomyMinister.canAfford(200)) {
			ShouldBuildCache.cacheShouldBuildInfo(unitType, false);
		}

		// ZERG RUSH
		if (Starwork.getEnemy().getRace().isZerg()) {
			if (barracks == 0) {
				ShouldBuildCache.cacheShouldBuildInfo(unitType, false);
				return false;
			}

			if (barracks >= 1 && depots == 0 && EconomyMinister.canAfford(200)) {
				ShouldBuildCache.cacheShouldBuildInfo(unitType, true);
				return true;
			}

			if ((depots >= 1 || weAreBuilding) && TerranBunker.getNumberOfUnits() == 0) {
				ShouldBuildCache.cacheShouldBuildInfo(unitType, false);
				return false;
			}
		}

		// NON-ZERG RUSH
		else {
			if (depots == 0) {
				ShouldBuildCache.cacheShouldBuildInfo(unitType, true);
				return true;
			}
		}

		if (barracks == 0 && depots == 1) {
			ShouldBuildCache.cacheShouldBuildInfo(unitType, false);
			return false;
		}
		if (barracks == 1 && depots == 1) {
			ShouldBuildCache.cacheShouldBuildInfo(unitType, false);
			return false;
		}

		if (BotStrategyManager.isExpandWithBunkers()) {
			if (depots == 1
					&& ((engineeringBays == 1 && EconomyMinister.canAfford(54)) || (engineeringBays == 0 && EconomyMinister
							.canAfford(194)))) {
				ShouldBuildCache.cacheShouldBuildInfo(unitType, true);
				return true;
			}
		} else {
			if (depots == 1
					&& ((engineeringBays == 1 && EconomyMinister.canAfford(92)) || (engineeringBays == 0 && EconomyMinister
							.canAfford(216)))) {
				ShouldBuildCache.cacheShouldBuildInfo(unitType, true);
				return true;
			}
		}

		if (totalSupply == 200) {
			ShouldBuildCache.cacheShouldBuildInfo(unitType, false);
			return false;
		}

		if (totalSupply < 80 && Constructing.weAreBuilding(unitType)) {
			if (!(totalSupply >= 10 && totalSupply <= 20 && freeSupply == 0)) {
				ShouldBuildCache.cacheShouldBuildInfo(unitType, false);
				return false;
			}
		}

		boolean shouldBuild = ((depots == 0 && totalSupply <= 9 && freeSupply <= 3)
				|| (totalSupply >= 10 && totalSupply <= 17 && freeSupply <= 4 && depots <= 1)
				|| (totalSupply >= 18 && totalSupply <= 25 && freeSupply <= 5)
				|| (totalSupply > 25 && totalSupply <= 45 && freeSupply <= 8) || (totalSupply > 45 && freeSupply <= 10) || (totalSupply > 90
				&& totalSupply < 200 && freeSupply <= 20));

		ShouldBuildCache.cacheShouldBuildInfo(unitType, shouldBuild);
		return shouldBuild;
	}

	// =========================================================

	public static void buildIfNecessary() {
		if (EconomyMinister.canAfford(92)) {
			if (shouldBuild()) {
				Constructing.construct(unitType);
			}
		}
	}

	private static Unit getRandomSupplyDepot() {
		Collection<Unit> depots = getSupplyDepots();
		return (Unit) RUtilities.getRandomElement(depots);
	}

	public static double calculateExistingDepotsCompleteness() {
		double result = 0;

		for (Unit depot : SelectUnits.our().ofType(unitType).list()) {
			result += (double) depot.getHP() / 500;
		}

		return result;
	}

	public static Position findTileForDepot() {
		Unit builder = BuilderSelector.getRandomWorker();

		if (UnitCounter.weHaveSupplyDepot()) {
			return findTileForNextDepot(builder);
		}

		// It's the first Depot
		else {
			return findTileForFirstDepot(builder, SelectUnits.firstBase());
		}
	}

	private static Position findTileForNextDepot(Unit builder) {

		// Or build near random depot.
		Unit supplyDepot = null;
		// ArrayList<Unit> depotsNearMainBase =
		// EconomyMinister.getUnitsOfGivenTypeInRadius(unitType, 14,
		// SelectUnits.firstBase(), true);
		Collection<Unit> depotsNearMainBase = SelectUnits.our().ofType(unitType).inRadius(14, SelectUnits.firstBase())
				.list();
		if (!depotsNearMainBase.isEmpty()) {
			supplyDepot = (Unit) RUtilities.getRandomElement(depotsNearMainBase);
		}
		if (supplyDepot == null) {
			supplyDepot = getRandomSupplyDepot();
		}

		Position tile = findTileForSupplyDepotNearby(supplyDepot, DEPOT_FROM_DEPOT_MIN_DISTANCE,
				DEPOT_FROM_DEPOT_MAX_DISTANCE);
		if (tile != null) {
			return tile;
		} else {
			return Constructing.findTileForStandardBuilding(unitType);
		}

	}

	private static Position findTileForSupplyDepotNearby(AbstractPosition point, int minDist, int maxDist) {
		return findLegitTileForDepot(point, BuilderSelector.getRandomWorker());
	}

	private static Position findLegitTileForDepot(AbstractPosition buildNearToHere, Unit builder) {
		int tileX = buildNearToHere.getTx();
		int tileY = buildNearToHere.getTy();

		int currentDist = DEPOT_FROM_DEPOT_MIN_DISTANCE;
		while (currentDist <= DEPOT_FROM_DEPOT_MAX_DISTANCE) {
			for (int i = tileX - currentDist; i <= tileX + currentDist; i++) {
				if (i % 3 != 0 || i % 9 == 0) {
					continue;
				}
				for (int j = tileY - currentDist; j <= tileY + currentDist; j++) {
					if (j % 2 != 0 || j % 6 == 0) {
						continue;
					}
					int x = i * 32;
					int y = j * 32;
					Position position = new Position(x, y);
					if (Constructing.canBuildHere(builder, unitType, i, j)
							&& SelectUnits.our().ofType(unitType).inRadius(DEPOT_FROM_DEPOT_MIN_DISTANCE - 1, position)
									.units().isEmpty()) {
						if (!Constructing.isTooNearMineralsOrGeyser(unitType, position)) {

							// Damn, try NOT to build in the middle of narrow
							// choke point.
							if (!Constructing.isTooCloseToAnyChokepoint(position)) {

								// Distance to the base must be big enough
								if (position.distanceTo(SelectUnits.firstBase().translate(5, 2)) >= 3) {
									return position;
								}
							}
						}
					}
					if (j % 4 == 0) {
						j += 2;
					}
				}
			}

			currentDist++;
		}
		return null;
	}

	private static Collection<Unit> getSupplyDepots() {
		Collection<Unit> depots = SelectUnits.our().ofType(unitType).list();
		for (Iterator<Unit> iterator = depots.iterator(); iterator.hasNext();) {
			Unit unit = iterator.next();
			if (!unit.isCompleted()) {
				iterator.remove();
			}
		}
		return depots;
	}

	private static Position findTileForFirstDepot(Unit builder, Unit base) {
		if (base == null) {
			return null;
		}

		// Find point being in the middle of way base<->nearest choke point.
		Chokepoint choke = MapExploration.getNearestChokepointFor(base);
		Position location = new Position((2 * base.getX() + choke.getCenterX()) / 3,
				(2 * base.getY() + choke.getCenterY()) / 3);
		// System.out.println();
		// System.out.println(choke.toStringLocation());
		// System.out.println(location.toStringLocation());

		return Constructing.getLegitTileToBuildNear(builder, unitType, location, 0, 100);
	}

	public static UnitType getBuildingType() {
		return unitType;
	}

	public static int getNumberOfUnits() {
		return UnitCounter.getNumberOfUnits(unitType);
	}

	public static int getNumberOfUnitsCompleted() {
		return UnitCounter.getNumberOfUnitsCompleted(unitType);
	}

}
