package starwork.economy.constructing;

import java.util.ArrayList;

import starwork.exploration.MapExploration;
import starwork.helpers.PositionHelper;
import starwork.main.Starwork;
import starwork.presidents.TimePresident;
import starwork.terran.TerranAcademy;
import starwork.terran.TerranBarracks;
import starwork.terran.TerranBunker;
import starwork.terran.TerranCommandCenter;
import starwork.terran.TerranComsatStation;
import starwork.terran.TerranEngineeringBay;
import starwork.terran.TerranFactory;
import starwork.terran.TerranMissileTurret;
import starwork.terran.TerranRefinery;
import starwork.terran.TerranSupplyDepot;
import starwork.units.SelectUnits;
import starwork.units.UnitCounter;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class Constructing {

	private static int _skipCheckingForTurns = 0;

	// ============================

	public static void construct(UnitType building) {

		// Define tile where to build according to type of building.
		Position buildTile = getTileAccordingToBuildingType(building);
		// System.out.println("buildTile FOR: " + building + " = " + buildTile);
		// Debug.message("buildTile FOR: " + building + " = " + buildTile);

		// Check if build tile is okay.
		if (buildTile != null) {
			// if (building.getType().isBase()) {
			// handleBaseConstruction(building, buildTile);
			// } else {
			constructBuilding(building, buildTile);
			// }
		}
	}

	private static void build(Unit builder, Position buildTile, UnitType buildingType) {
		boolean canProceed = false;

		// Disallow multiple building of all buildings, except barracks,
		// bunkers.
		int builders = ifWeAreBuildingItCountHowManyWorkersIsBuildingIt(buildingType);
		if (buildingType.isFactory()) {
			canProceed = builders <= 1;
		} else if (buildingType.isBarracks()) {
			int barracks = TerranBarracks.getNumberOfUnits();
			if (barracks != 1) {
				canProceed = builders == 0;
			}
			if (barracks == 1) {
				canProceed = builders <= TerranBarracks.MAX_BARRACKS - 1;
			}
		} else {
			canProceed = !weAreBuilding(buildingType);
		}

		// If there aren't multiple orders to build one building given, we can
		// proceed
		if (canProceed) {
			// Starwork.getGame().build(builder, buildTile.getTx(),
			// buildTile.getTy(), building);
			TilePosition tilePosition = new TilePosition(buildTile.getX(), buildTile.getY());
			builder.build(tilePosition, buildingType);
			ConstructionManager.addInfoAboutConstruction(buildingType, builder, buildTile);
		}
	}

	// ============================

	public static Position findTileForStandardBuilding(UnitType typeToBuild) {

		// There is a nasty bug: when we're losing badly Terran Barracks are
		// slowing down game terribly; try to limit search range.
		int MAX_RANGE = 80;
		if (TimePresident.getTimeSeconds() > 400 && typeToBuild == UnitType.Terran_Barracks) {
			MAX_RANGE = 20;
		}

		Unit base = SelectUnits.firstBase();
		if (base == null) {
			return null;
		}

		Position tile = Constructing.getLegitTileToBuildNear(BuilderSelector.getRandomWorker(), typeToBuild,
				base.translate(5, 2), 5, MAX_RANGE);

		return tile;
	}

	private static Position getTileAccordingToBuildingType(UnitType building) {
		Position buildTile = null;
		boolean disableReportOfNoPlaceFound = false;

		// Supply Depot
		if (TerranSupplyDepot.getBuildingType() == building) {
			buildTile = TerranSupplyDepot.findTileForDepot();
		}

		// Bunker
		else if (TerranBunker.getBuildingType() == building) {
			buildTile = TerranBunker.findTileForBunker();
		}

		// Missile Turret
		else if (TerranMissileTurret.getBuildingType() == building) {
			buildTile = TerranMissileTurret.findTileForTurret();
		}

		// Refinery
		else if (TerranRefinery.getBuildingType() == building) {
			buildTile = TerranRefinery.findTileForRefinery();
			// System.out.println("       buildTile = " +
			// buildTile.toStringLocation());
			disableReportOfNoPlaceFound = true;
		}

		// Base
		else if (TerranCommandCenter.getBuildingType() == building) {
			buildTile = TerranCommandCenter.findTileForNextBase(false);
		}

		// Standard building
		else {
			if (_skipCheckingForTurns > 0) {
				_skipCheckingForTurns--;
				return null;
			}
			buildTile = findTileForStandardBuilding(building);
		}

		if (buildTile == null && !disableReportOfNoPlaceFound) {
			System.out.println("# No tile found for: " + building.c_str());
		}

		return buildTile;
	}

	/**
	 * @return if we need to build some building it will return non-null value,
	 *         being int array containing three elements: first is total amount
	 *         of minerals required all buildings that we need to build, while
	 *         second is total amount of gas required and third returns total
	 *         number of building types that we want to build. If we don't need
	 *         to build anything right now it returns null
	 * */
	public static int[] shouldBuildAnyBuilding() {
		int mineralsRequired = 0;
		int gasRequired = 0;
		int buildingsToBuildTypesNumber = 0;
		if (TerranCommandCenter.shouldBuild()) {
			mineralsRequired += 400;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}
		if (TerranFactory.shouldBuild()) {
			mineralsRequired += 200;
			gasRequired += 200;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}
		if (TerranBarracks.shouldBuild()) {
			mineralsRequired += 150;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}
		// if (TerranBunker.shouldBuild()
		// && UnitCounter.getNumberOfUnits(UnitType.Protoss_Bunker) < 2) {
		// mineralsRequired += 100;
		// buildingsToBuildTypesNumber++;
		// }
		if (TerranRefinery.shouldBuild()) {
			mineralsRequired += 100;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}
		if (TerranSupplyDepot.shouldBuild()) {
			mineralsRequired += 100;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}
		if (TerranEngineeringBay.shouldBuild()) {
			mineralsRequired += 150;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}
		if (TerranAcademy.shouldBuild()) {
			mineralsRequired += 150;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}
		if (TerranComsatStation.shouldBuild()) {
			mineralsRequired += 50;
			gasRequired += 100;
			buildingsToBuildTypesNumber++;
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		}

		if (buildingsToBuildTypesNumber > 0) {
			return new int[] { mineralsRequired + 8, gasRequired, buildingsToBuildTypesNumber };
		} else {
			return null;
		}
	}

	public static Position findBuildTile(Unit builder, UnitType type, Position place) {
		return findBuildTile(builder, type, place.getX(), place.getY());
	}

	public static Position findBuildTile(Unit builder, UnitType type, int x, int y) {
		Position tileToBuild = findTileForStandardBuilding(type);

		if (tileToBuild == null) {
			System.err.println("Unable to find tile for new " + type.c_str());
		}
		return tileToBuild;
	}

	public static Position getLegitTileToBuildNear(UnitType type, Position nearTo, int minimumDist, int maximumDist) {
		Unit worker = BuilderSelector.getRandomWorker();
		if (worker == null || type == null) {
			return null;
		}
		return getLegitTileToBuildNear(worker, type, nearTo, minimumDist, maximumDist);
	}

	public static Position getLegitTileToBuildNear(Unit worker, UnitType type, Position nearTo, int minimumDist,
			int maximumDist) {
		if (worker == null || type == null) {
			return null;
		}
		return getLegitTileToBuildNear(worker, type, nearTo, minimumDist, maximumDist);
	}

	public static Position getLegitTileToBuildNear(Unit worker, UnitType type, TilePosition tile, int minimumDist,
			int maximumDist, boolean requiresPower) {
		if (worker == null || type == null) {
			return null;
		}
		return getLegitTileToBuildNear(worker, type, tile, minimumDist, maximumDist);
	}

	public static Position getLegitTileToBuildNear(Unit builder, UnitType type, TilePosition tile, int minimumDist,
			int maximumDist) {
		boolean isBase = type.isBase();
		boolean isDepot = type.isSupplyDepot();
		// boolean checkExplored = type.isBunker();

		// boolean skipCheckingIsFreeFromUnits = type.isBase();
		boolean skipCheckingIsFreeFromUnits = false;
		boolean skipCheckingRegion = TimePresident.getTimeSeconds() > 250 || isBase || type.isBunker()
				|| type.isMissileTurret() || type.isAddon();

		int currentDist = minimumDist;
		while (currentDist <= maximumDist) {
			for (int i = tile.getTx() - currentDist; i <= tile.getTx() + currentDist; i++) {
				if (isDepot && (i % 3 != 0 || i % 9 == 0)) {
					continue;
				}
				for (int j = tile.getTy() - currentDist; j <= tile.getTy() + currentDist; j++) {
					if (isDepot && (j % 2 != 0 || j % 6 == 0)) {
						continue;
					}
					int x = i * 32;
					int y = j * 32;
					Position place = new Position(x, y);
					// bwapi.canBuildHere(builderID, i, j, buildingTypeID,
					// false)
					if (canBuildAt(place, type)) {
						// && isBuildTileFullyBuildableFor(builderID, i, j,
						// buildingTypeID)
						Unit builderUnit = BuilderSelector.getRandomWorker();
						if (builderUnit != null
								&& (skipCheckingIsFreeFromUnits || isBuildTileFreeFromUnits(builderUnit, i, j))) {
							if ((isBase || !isTooNearMineralsOrGeyser(type, place))
									&& (isBase || isEnoughPlaceToOtherBuildings(place, type))
									&& (isBase || !isOverlappingNextBase(place, type))
									&& (isBase || !isTooCloseToAnyChokePoint(place)
											&& (isBase || skipCheckingRegion || isInAllowedRegions(place)))) {

								// if (type.isPhotonCannon()) {
								// System.out.println("@@@@@@@ "
								// + xvr.getDistanceBetween(choke, place) +
								// "/"
								// + choke.getRadius());
								// }
								return place;
							}
						}
					}
				}
			}

			currentDist++;
		}

		return null;
	}

	private static boolean isInAllowedRegions(Position place) {
		Region buildTileRegion = xvr.getMap().getRegion(place);
		if (buildTileRegion.equals(SelectUnits.firstBase().getRegion())
				|| buildTileRegion.equals(TerranCommandCenter.getSecondBaseLocation().getRegion())) {
			return true;
		}
		return false;
	}

	public static boolean isTooCloseToAnyChokePoint(Position place) {
		ChokePoint nearestChoke = MapExploration.getNearestChokePointFor(place);
		int chokeTiles = (int) (nearestChoke.getRadius() / 32);

		if (chokeTiles >= 6) {
			return false;
		} else {
			return place.distanceToChokePoint(nearestChoke) <= 3.3;
		}

		// for (ChokePoint choke : MapExploration.getChokePoints()) {
		// if (choke.getRadius() < 210
		// && (xvr.getDistanceBetween(choke, place) - choke.getRadius() / 32) <=
		// MIN_DIST_FROM_CHOKE_POINT) {
		// return true;
		// }
		// }
	}

	private static boolean isOverlappingNextBase(Position place, UnitType type) {
		if (!type.isBase() && UnitCounter.getNumberOfUnits(TerranSupplyDepot.getBuildingType()) >= 1) {
			return xvr.getDistanceSimple(place, TerranCommandCenter.findTileForNextBase(false).translate(62, 48)) <= 6;
		} else {
			return false;
		}
	}

	private static boolean isEnoughPlaceToOtherBuildings(Position place, UnitType type) {
		if (type.isBase() || type.isOnGeyser()) {
			return true;
		}
		boolean isDepot = type.isSupplyDepot();

		// ==============================
		// Define building dimensions
		int wHalf = type.getTileWidth() + (type.canHaveAddOn() ? 2 : 0);
		int hHalf = type.getTileHeight();
		int maxDimension = wHalf > hHalf ? wHalf : hHalf;

		// ==============================
		// Define center of the building
		Position center = new Position(place.getX() + wHalf, place.getY() + hHalf);

		// Define buildings that are near this build tile
		ArrayList<Unit> buildingsNearby = xvr.getUnitsInRadius(center, 10, xvr.getUnitsBuildings());

		// If this building can have an Add-On, it is essential we keep place
		// for it.
		int spaceBonus = 0;
		if (type.canHaveAddOn()) {
			// spaceBonus += 2;
			center = center.translate(64, 0);
		}

		// For each building nearby define if it's not too close to this build
		// tile. If so, reject this build tile.
		for (Unit unit : buildingsNearby) {
			if (unit.isLifted()) {
				continue;
			}

			// Supply Depots can be really close to each other, but only if
			// there're few of them
			if (isDepot && type.isSupplyDepot()
					&& xvr.countUnitsOfGivenTypeInRadius(UnitType.Terran_Supply_Depot, 5, place, true) <= 2
					&& xvr.countUnitsOfGivenTypeInRadius(UnitType.Terran_Supply_Depot, 9, place, true) <= 3) {
				continue;
			}

			// Also: don't build in the place where there COULD BE Add-On for a
			// different, already existing building
			int dx = 0;
			int bonus = spaceBonus;
			UnitType unitType = unit.getType();
			if (type.canHaveAddOn() && !unit.hasAddOn()) {
				// bonus++;
				dx = 64;
			}
			if (unitType.isBase()) {
				dx += 32;
				bonus += 4;
			}

			// If this building is too close to our build tile, indicate this
			// fact.
			if (type.isBuilding() && !unit.isLifted()
					&& unit.translate(dx, 0).distanceTo(center) <= maxDimension + 1 + bonus) {
				return false;
			}
		}
		return true;
	}

	public static boolean isTooNearMineralsOrGeyser(UnitType type, Position point) {
		int minDistBonus = type.canHaveAddOn() ? 2 : 0;

		// Check if isn't too near to geyser
		Unit nearestGeyser = xvr.getUnitNearestFromList(point, xvr.getGeysersUnits());
		double distToGeyser = xvr.getDistanceBetween(nearestGeyser, point);
		Unit nearestBase = xvr.getUnitOfTypeNearestTo(UnitManager.BASE, point);
		if (distToGeyser <= 7 + minDistBonus) {
			double distBaseToGeyser = xvr.getDistanceBetween(nearestBase, nearestGeyser);
			if (distBaseToGeyser >= distToGeyser + minDistBonus) {
				return false;
			}
		}

		// ==================================
		// Check if isn't too near to mineral
		Unit nearestMineral = xvr.getUnitNearestFromList(point, xvr.getMineralsUnits());
		double distToMineral = xvr.getDistanceBetween(nearestMineral, point);
		if (distToMineral <= 7 + minDistBonus) {
			return true;
		}

		if (distToMineral <= 10 + minDistBonus) {
			if (nearestBase.distanceTo(point) <= 4 + minDistBonus) {
				return false;
			}

			double distBaseToMineral = xvr.getDistanceBetween(nearestBase, nearestMineral);
			if (distToMineral < distBaseToMineral + minDistBonus) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBuildTileFullyBuildableFor(int builderID, int i, int j, int buildingTypeID) {
		UnitType buildingType = UnitType.getUnitTypeByID(buildingTypeID);
		int wHalf = buildingType.getTileWidth() / 2;
		int hHalf = buildingType.getTileHeight() / 2;
		for (int tx = i - wHalf; tx < i + wHalf; tx++) {
			for (int ty = j - hHalf; ty < j + hHalf; ty++) {
				if (!Starwork.getGame().isBuildable(tx, ty, true)) {
					return false;
				}
			}
		}

		// if (UnitCounter.weHaveBuildingFinished(UnitType.Protoss_Pylon)) {
		// Position tileForNextBase = ProtossNexus.getTileForNextBase(false);
		// if (tileForNextBase != null
		// && xvr.getDistanceBetween(tileForNextBase,
		// new Position(i * 32, j * 32)) < 3) {
		// return false;
		// }
		// }

		return true;
	}

	public static boolean isBuildTileFreeFromUnits(Unit builder, int tileX, int tileY) {
		Position point = new Position(tileX * 32, tileY * 32);

		// Check if units are blocking this tile
		boolean unitsInWay = false;
		for (Unit u : bwapi.getAllUnits()) {
			if (u == builderID) {
				continue;
			}
			if (xvr.getDistanceBetween(u, point) <= 3) {
				// for (Unit unit : xvr.getUnitsInRadius(point, 4,
				// Starwork.getGame().getMyUnits())) {
				// UnitActions.moveAwayFromUnitIfPossible(unit, point, 6);
				// }
				unitsInWay = true;
			}
		}
		if (!unitsInWay) {
			return true;
		}

		return false;
	}

	private static boolean canBuildAt(Position position, UnitType type) {
		Unit randomWorker = BuilderSelector.getRandomWorker();
		if (randomWorker == null || position == null) {
			return false;
		}

		// Buildings that can have an add-on, must have additional space on
		// their right
		if (type.canBuildAddon() && !type.isBase()) {
			if (!Starwork.getGame().canBuildHere(randomWorker, PositionHelper.convertPositionToTile(position, 2, 0),
					type)) {
				return false;
			}
		}

		TilePosition tilePosition = new TilePosition(position);
		return Starwork.getGame().canBuildHere(randomWorker, tilePosition, type, false);
	}

	protected static boolean constructBuilding(UnitType building, Position buildTile) {
		if (buildTile == null) {
			return false;
		}

		Unit workerUnit = BuilderSelector.getOptimalBuilder(buildTile, building);
		if (workerUnit != null) {

			// if we found a good build position, and we aren't already
			// constructing this building order our worker to build it
			// && (!xvr.weAreBuilding(building))
			if (buildTile != null) {
				// Debug.messageBuild(building);
				build(workerUnit, buildTile, building);

				// // If it's base then build pylon for new base
				// if (UnitType.getUnitTypeByID(building).isBase()) {
				// forceConstructionOfPylonNear(buildTile);
				// }
				return true;
			}
		}
		return false;
	}

	public static int ifWeAreBuildingItCountHowManyWorkersIsBuildingIt(UnitType type) {
		int result = 0;

		// if (_recentConstructionsInfo.containsKey(type)) {
		// result++;
		// }
		for (Unit unit : SelectUnits.ourWorkers().list()) {
			if (unit.getBuildType() == type) {
				result++;
			}
		}
		return result;
	}

	public static boolean weAreBuilding(UnitType type) {
		return ConstructionManager.weAreBuilding(type);
	}

	public static boolean canBuildHere(Unit builder, UnitType buildingType, TilePosition position) {
		return Starwork.getGame().canBuildHere(builder, position, buildingType, false);
	}

	public static void constructAddOn(Unit buildingWithNoAddOn, UnitType buildingType) {
		if (buildingWithNoAddOn == null || buildingType == null) {
			return;
		}
		buildingWithNoAddOn.buildAddon(buildingType);
	}

}
