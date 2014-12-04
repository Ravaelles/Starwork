package starwork.terran;

import java.util.ArrayList;

import starwork.economy.constructing.BuilderSelector;
import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import starwork.exploration.MapExploration;
import starwork.presidents.TimePresident;
import starwork.units.UnitCounter;
import starwork.utils.RUtilities;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.Chokepoint;

public class TerranBunker {

	private static final UnitType type = UnitType.Terran_Bunker;

	private static final double MAX_DIST_FROM_CHOKE_POINT_MODIFIER = 1.8;
	public static int GLOBAL_MAX_BUNKERS = 2;
	public static int MAX_STACK = 2;

	private static Position _placeToReinforceWithBunker = null;
	private static int _skipForTurns = 0;

	// =========================================================

	public static boolean shouldBuild() {
		if (TerranBarracks.getNumberOfUnitsCompleted() == 0) {
			return ShouldBuildCache.cacheShouldBuildInfo(type, false);
		}
		int bunkers = UnitCounter.getNumberOfUnits(type);

		// =========================================================

		// STRATEGY: Offensive Bunker
		if (TerranOffensiveBunker.isStrategyActive()) {
			if (GLOBAL_MAX_BUNKERS == 0) {
				return ShouldBuildCache.cacheShouldBuildInfo(type, false);
			}

			if (bunkers == 0) {
				return ShouldBuildCache.cacheShouldBuildInfo(type, true);
			} else if (UnitCounter.getNumberOfBattleUnits() > 5) {
				return ShouldBuildCache.cacheShouldBuildInfo(type, true);
			}
		}

		// =========================================================

		if (_skipForTurns > 0 && TimePresident.getTimeSeconds() > 400) {
			_skipForTurns--;
			return ShouldBuildCache.cacheShouldBuildInfo(type, false);
		}

		// if (TimePresident.getTimeSeconds() > 200 &&
		// UnitCounter.getNumberOfBattleUnits() < 7) {
		// return false;
		// }

		if (bunkers >= GLOBAL_MAX_BUNKERS) {
			return ShouldBuildCache.cacheShouldBuildInfo(type, false);
		}

		// =========================================================

		if (UnitCounter.weHaveBuilding(TerranBarracks.getBuildingType())
				|| BuildingManager.countConstructionProgress(TerranBarracks.getBuildingType()) >= 95) {
			int maxStack = calculateMaxBunkerStack();

			int infantryUnits = UnitCounter.getNumberOfInfantryUnits();

			if (bunkers <= 1) {
				// System.out.println("######## BUNKER! " + bunkers);
				return ShouldBuildCache.cacheShouldBuildInfo(type, true);
			}

			if (bunkers < MAX_STACK && infantryUnits >= bunkers * 3) {
				return ShouldBuildCache.cacheShouldBuildInfo(type, true);
			}

			if (bunkers >= TerranCommandCenter.getNumberOfUnits() * MAX_STACK) {
				return ShouldBuildCache.cacheShouldBuildInfo(type, false);
			}

			// boolean weAreBuilding = Constructing.weAreBuilding(type);
			// if (weAreBuilding) {
			// return ShouldBuildCache.cacheShouldBuildInfo(type, false);
			// }

			// // If main base isn't protected at all, build some bunkers
			// if (shouldBuildNearMainBase()) {
			// ShouldBuildCache.cacheShouldBuildInfo(type, true);
			// return true;
			// }

			if (bunkers <= maxStack && TerranSupplyDepot.calculateExistingDepotsCompleteness() >= 1.35
					&& calculateExistingBunkersStrength() < maxStack) {
				return ShouldBuildCache.cacheShouldBuildInfo(type, true);
			}

			// Select one place to reinforce
			for (Position base : getPlacesToReinforce()) {
				if (UnitCounter.getNumberOfUnits(UnitManager.BASE) == 1) {
					if (shouldBuildFor(base)) {
						return ShouldBuildCache.cacheShouldBuildInfo(type, true);
					}
				}
			}

			// // If reached here, then check if build cannon at next base
			// Position tileForNextBase =
			// TerranCommandCenter.findTileForNextBase(false);
			// if (shouldBuildFor(tileForNextBase)) {
			// ShouldBuildCache.cacheShouldBuildInfo(type, true);
			// return true;
			// }
		}

		return ShouldBuildCache.cacheShouldBuildInfo(type, false);
	}

	// =========================================================

	public static Unit getFirstBunker() {
		return (Unit) RUtilities.getFirstElement(xvr.getUnitsOfType(type));
	}

	// =========================================================

	private static double calculateExistingBunkersStrength() {
		double result = 0;
		UnitType unitType = UnitType.getUnitTypeByUnitType(type);
		int maxHitPoints = unitType.getMaxHitPoints();

		for (Unit cannon : xvr.getUnitsOfType(type)) {
			double cannonTotalHP = (double) (cannon.getHP()) / maxHitPoints;
			if (!cannon.isCompleted()) {
				cannonTotalHP = Math.sqrt(cannonTotalHP);
			}
			result += cannonTotalHP;
		}

		return result;
	}

	private static boolean shouldBuildFor(Position base) {
		if (base == null) {
			return false;
		}

		// Build just at second base
		if (base.equals(SelectUnits.firstBase())) {
			return false;
		}

		// Build at first base
		// if (UnitCounter.getNumberOfUnits(UnitManager.BASE) >= 2) {
		// if (base.equals(SelectUnits.firstBase())) {
		// return false;
		// }
		// }

		// Get the nearest choke point to base
		Chokepoint chokePoint = MapExploration.getImportantChokepointNear(base);

		// // If this is new base, try to force building of cannon here.
		// if (!base.equals(SelectUnits.firstBase())) {
		// _placeToReinforceWithCannon = base;
		// return true;
		// }

		// If in the neighborhood of choke point there's too many cannons, don't
		// build next one.
		if (shouldBuildFor(chokePoint)) {
			_placeToReinforceWithBunker = chokePoint;
			return true;
		} else {
			return false;
		}
	}

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(type, true);
			for (Position base : getPlacesToReinforce()) {
				tryToBuildFor(base);
			}
		}
		ShouldBuildCache.cacheShouldBuildInfo(type, false);
	}

	private static ArrayList<Position> getPlacesToReinforce() {
		ArrayList<Position> placesToReinforce = new ArrayList<>();

		// Second base should be one huge defensive bunker.
		placesToReinforce.add(TerranCommandCenter.getSecondBaseLocation());

		// Add bases from newest, to the oldest (I guess?)
		ArrayList<Unit> bases = TerranCommandCenter.getBases();
		for (int i = bases.size() - 1; i >= 0; i--) {
			Unit base = bases.get(i);
			Chokepoint chokePoint = MapExploration.getImportantChokepointNear(base);
			placesToReinforce.add(chokePoint);
		}

		return placesToReinforce;
	}

	private static void tryToBuildFor(Position base) {
		if (shouldBuildFor(base)) {
			Constructing.construct(xvr, type);
		}
	}

	private static boolean shouldBuildFor(Chokepoint chokePoint) {
		// return findTileForCannon() != null;
		if (chokePoint.isDisabled()) {
			return false;
		}

		int numberOfDefensiveBuildingsNearby = calculateBunkersNearby(chokePoint);

		int bonus = 0;
		// if
		// (xvr.getDistanceBetween(TerranCommandCenter.getSecondBaseLocation(),
		// chokePoint) < 14) {
		// bonus = 1;
		// }

		// If there isn't too many cannons defending this choke point
		if (numberOfDefensiveBuildingsNearby < calculateMaxBunkerStack() + bonus) {
			return true;
		}

		// No, there's too many defensive buildings. Don't build next one.
		else {
			return false;
		}
	}

	public static int calculateMaxBunkerStack() {
		return MAX_STACK;
		// return BotStrategyManager.isExpandWithBunkers() ? MAX_STACK :
		// (UnitCounter
		// .getNumberOfBattleUnits() >= 8 ? 1 : MAX_STACK);
	}

	private static int calculateBunkersNearby(Position Position) {
		int radius;

		Chokepoint choke = null;
		if (Position instanceof Chokepoint) {
			choke = (Chokepoint) Position;
			radius = (int) choke.getRadius() / 32;
		} else {
			radius = 8;
		}

		int searchInDistance = (int) (1.5 * MAX_DIST_FROM_CHOKE_POINT_MODIFIER * radius);
		if (searchInDistance < 9) {
			searchInDistance = 9;
		}

		ArrayList<Unit> cannonsNearby = xvr.getUnitsOfGivenTypeInRadius(type, searchInDistance, Position, true);

		double result = 0;
		double maxCannonHP = 200;
		for (Unit cannon : cannonsNearby) {
			// if (!cannon.isCompleted()) {
			// result -= 1;
			// }
			result += cannon.getHP() / maxCannonHP;
		}

		return (int) result;
	}

	private static Position findProperBuildTile(Position Position) {

		// Define approximate tile for cannon
		Position initialBuildTile = Position;

		// Define random worker, for technical reasons
		Unit workerUnit = BuilderSelector.getRandomWorker();

		// ================================
		// Define minimum and maximum distance from a choke point for a bunker
		int minimumDistance = 5;
		int numberOfBunkersNearby = calculateBunkersNearby(Position);
		if (Position instanceof Chokepoint) {
			Chokepoint choke = (Chokepoint) Position;
			if (choke.getRadius() / 32 >= 8) {
				minimumDistance = 3;
			}
		}
		int maximumDistance = minimumDistance + (10 / Math.max(1, numberOfBunkersNearby));

		// ================================
		// Find proper build tile
		Unit nearBunker = xvr.getUnitOfTypeNearestTo(type, initialBuildTile, true);
		Position properBuildTile = null;
		if (nearBunker != null && nearBunker.distanceTo(initialBuildTile) <= maximumDistance) {
			properBuildTile = Constructing.getLegitTileToBuildNear(workerUnit, type, nearBunker, 0, maximumDistance);
		} else {
			properBuildTile = Constructing.getLegitTileToBuildNear(workerUnit, type, initialBuildTile, minimumDistance,
					maximumDistance);
		}

		return properBuildTile;
	}

	public static Position findTileForBunker() {
		Position tileForBunker = null;

		// Offensive bunker
		int bunkers = getNumberOfUnits();
		if (TerranOffensiveBunker.isStrategyActive() && bunkers < GLOBAL_MAX_BUNKERS) {
			if (bunkers == 0) {
				return TerranOffensiveBunker.getTerranOffensiveBunkerPosition();
			} else {
				return TerranOffensiveBunker.getTerranSecondOffensiveBunkerPosition();
			}
		}

		// Protected main base
		// if (shouldBuildNearMainBase()) {
		// tileForCannon = findBuildTileNearMainBase();
		// } else {
		if (getNumberOfUnits() < MAX_STACK) {
			if (XVR.isEnemyZerg() && getNumberOfUnits() == 0) {
				tileForBunker = findTileAtBase(SelectUnits.firstBase());
			} else {
				tileForBunker = findTileAtBase(TerranCommandCenter.getSecondBaseLocation());
			}
		} else {

			// return findProperBuildTile(_chokePointToReinforce, true);
			if (_placeToReinforceWithBunker == null) {
				_placeToReinforceWithBunker = MapExploration.getNearestChokepointFor(getInitialPlaceToReinforce());
			}

			// Try to find normal tile.
			tileForBunker = findProperBuildTile(_placeToReinforceWithBunker);
		}

		// }
		if (tileForBunker != null) {
			return tileForBunker;
		}

		// ===================
		// If we're here it can mean we should build bunkers at position of the
		// next base
		Position tileForNextBase = TerranCommandCenter.findTileForNextBase(false);
		if (shouldBuildFor(tileForNextBase)) {
			tileForBunker = findProperBuildTile(tileForNextBase);
			if (tileForBunker != null) {
				return tileForBunker;
			}
		}

		_skipForTurns = 30;

		return null;
	}

	public static Position findTileAtBase(Position base) {
		if (base == null) {
			return null;
		}

		// Change first base to second base.
		// base = TerranCommandCenter.getSecondBaseLocation();
		// if (base == null) {
		// return null;
		// }

		// Find point being in the middle of way second base<->nearest choke
		// point.
		// Chokepoint choke = MapExploration.getNearestChokepointFor(base);
		Chokepoint choke = MapExploration.getImportantChokepointNear(base);
		if (choke == null) {
			return null;
		}

		// Position location = new Position(
		// (base.getX() + 2 * choke.getX()) / 3,
		// (base.getY() + 2 * choke.getY()) / 3);
		Position location = Position.getMiddlePointBetween(base, choke);

		// Find place for bunker between choke point and the second base.
		// return Constructing.getLegitTileToBuildNear(xvr.getRandomWorker(),
		// type, location, 0, 100);

		Position properBuildTile = null;

		int maximumDistance = 100;
		Unit nearBunker = xvr.getUnitOfTypeNearestTo(type, location, true);
		if (getNumberOfUnits() == 1) {
			ArrayList<Unit> unitsOfType = xvr.getUnitsOfType(type);
			if (!unitsOfType.isEmpty()) {
				nearBunker = unitsOfType.get(0);
			}
		}

		// if (nearBunker == null) {
		// System.out.println("No bunker near");
		// } else {
		// System.out.println("nearBunker = " + nearBunker.toStringLocation() +
		// " / compl:"
		// + nearBunker.isCompleted());
		// }

		if (nearBunker != null && nearBunker.distanceTo(location) <= maximumDistance) {
			Position secondBunkerTile = Position.getPointBetween(nearBunker, base, -2);
			properBuildTile = Constructing.getLegitTileToBuildNear(type, secondBunkerTile, 0, maximumDistance);
		} else {
			properBuildTile = Constructing.getLegitTileToBuildNear(type, location, 0, maximumDistance);
		}

		// System.out.println("TILE = " + properBuildTile.toStringLocation());

		return properBuildTile;
	}

	// private static Position findBuildTileNearMainBase() {
	//
	// // ===================
	// // If main base isn't protected at all, build some cannons
	// Unit firstBase = SelectUnits.firstBase();
	// Position point = firstBase;
	//
	// Position tileForCannon =
	// Constructing.getLegitTileToBuildNear(xvr.getRandomWorker(), type,
	// point, 0, 10);
	//
	// // Debug.message(xvr, "## Build cannon for main base ##");
	// // System.out.println(" ################################ ");
	// //
	// System.out.println(" ################################ PROTECTED THE BASE");
	// // System.out.println(" ################################ ");
	// // System.out.println(tileForCannon);
	// // System.out.println();
	//
	// if (tileForCannon != null) {
	// return tileForCannon;
	// }
	// return null;
	// }

	private static Position getInitialPlaceToReinforce() {
		if (TerranOffensiveBunker.isStrategyActive()) {
			return TerranOffensiveBunker.getTerranOffensiveBunkerPosition();
		}

		return TerranCommandCenter.getSecondBaseLocation();
	}

	public static UnitType getBuildingType() {
		return type;
	}

	public static int getNumberOfUnits() {
		return UnitCounter.getNumberOfUnits(type);
	}

	public static int getNumberOfUnitsCompleted() {
		return UnitCounter.getNumberOfUnitsCompleted(type);
	}

}
