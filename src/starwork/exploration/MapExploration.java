package starwork.exploration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import starwork.enemy.EnemyTanksManager;
import starwork.main.Starwork;
import starwork.map.AbstractPosition;
import starwork.terran.TerranCommandCenter;
import starwork.units.SelectUnits;
import starwork.utils.RUtilities;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BaseLocation;
import bwta.Chokepoint;
import bwta.Region;

public class MapExploration {

	private static final int MAXIMUM_CHOKE_POINT_DISTANCE_FROM_BASE = 30;

	private static ArrayList<Chokepoint> chokePointsProcessed = new ArrayList<Chokepoint>();

	protected static TreeSet<BaseLocation> baseLocationsDiscovered = new TreeSet<BaseLocation>();
	protected static HashMap<Integer, Unit> enemyBasesDiscovered = new HashMap<Integer, Unit>();
	protected static HashMap<Integer, Unit> enemyBuildingsDiscovered = new HashMap<Integer, Unit>();
	protected static HashMap<Integer, Unit> enemyUnitsDiscovered = new HashMap<Integer, Unit>();
	protected static ArrayList<Unit> _hiddenEnemyUnits = new ArrayList<Unit>();

	private static boolean _disabledChokepointsNearMainBase = false;
	private static bwta.BaseLocation calculatedEnemyBaseLocation = null;

	// =========================================================

	public static synchronized void enemyUnitDiscovered(Unit enemyUnit) {
		UnitType type = enemyUnit.getType();

		// Discovered BASE
		if (type.isBase()) {
			TerranCommandCenter.findTileForNextBase(true);
			synchronized (enemyBasesDiscovered) {
				enemyBasesDiscovered.put(enemyUnit.getID(), enemyUnit);
			}
		}

		// Discovered BUILDING
		if (type.isBuilding()) {
			synchronized (enemyBuildingsDiscovered) {
				enemyBuildingsDiscovered.put(enemyUnit.getID(), enemyUnit);
			}
		}

		// Discovered OTHER UNIT
		else {
			synchronized (enemyUnitsDiscovered) {
				enemyUnitsDiscovered.put(enemyUnit.getID(), enemyUnit);
			}

			// TANK
			if (type.isTank()) {
				EnemyTanksManager.updateTankPosition(enemyUnit);
			}
		}
	}

	public static BaseLocation getMostDistantBaseLocation(Unit unit) {
		if (unit == null) {
			return null;
		}

		double mostFarDistance = 2;
		BaseLocation nearestObject = null;

		ArrayList<BaseLocation> baseLocations = new ArrayList<>();
		baseLocations.addAll(BaseLocation.getAllBaseLocations());
		baseLocations.remove(getOurBaseLocation());
		Collections.shuffle(baseLocations);

		boolean onlyStartLocations = baseLocationsDiscovered.size() < getNumberOfStartLocations(baseLocations);

		for (BaseLocation object : baseLocations) {
			if (onlyStartLocations && !object.isStartLocation()) {
				continue;
			}

			double distance = unit.distanceTo(object);
			if (distance > mostFarDistance) {
				mostFarDistance = distance;
				nearestObject = object;
			}
		}

		return nearestObject;
	}

	public static BaseLocation getNearestBaseLocation(Unit unit) {
		if (unit == null) {
			return null;
		}

		double mostNearDistance = 99999;
		BaseLocation nearestObject = null;

		ArrayList<BaseLocation> baseLocations = new ArrayList<>();
		baseLocations.addAll(BaseLocation.getAllBaseLocations());
		baseLocations.remove(getOurBaseLocation());
		Collections.shuffle(baseLocations);

		for (BaseLocation object : baseLocations) {
			double distance = unit.distanceTo(object);
			if (distance < mostNearDistance) {
				mostNearDistance = distance;
				nearestObject = object;
			}
		}

		return nearestObject;
	}

	public static int getNumberOfStartLocations(Collection<BaseLocation> baseLocations) {
		int result = 0;
		for (BaseLocation object : baseLocations) {
			if (object.isStartLocation()) {
				result++;
			}
		}
		return result;
	}

	public static BaseLocation getOurBaseLocation() {
		Collection<BaseLocation> baseLocations = BaseLocation.getAllBaseLocations();
		Unit ourBase = SelectUnits.firstBase();

		for (BaseLocation object : baseLocations) {
			if (object.isStartLocation()) {
				double distance = object.distanceTo(ourBase);
				if (distance < 5) {
					return object;
				}
			}
		}

		return null;
	}

	// private static BaseLocation nonInitialScouting(Unit explorer) {
	// XVR xvr = XVR.getInstance();
	// BaseLocation goTo = null;
	//
	// // Filter out visited bases.
	// ArrayList<BaseLocation> possibleBases = new ArrayList<BaseLocation>();
	// possibleBases.addAll(xvr.getBwapi().getMap().getStartLocations());
	// possibleBases.removeAll(baseLocationsDiscovered);
	// possibleBases.remove(getOurBaseLocation());
	//
	// // If there is any unvisited base- go there. If no- go to the random
	// // base.
	//
	// if (possibleBases.isEmpty()) {
	// goTo = (BaseLocation) RUtilities.getRandomListElement(xvr
	// .getBwapi().getMap().getBaseLocations());
	// } else {
	// goTo = (BaseLocation) RUtilities
	// .getRandomListElement(possibleBases);
	// }
	//
	// return goTo;
	// }

	public static Chokepoint getNearestChokepointFor(AbstractPosition point) {
		return getNearestChokepointFor(point.getX(), point.getY());
	}

	public static Chokepoint getNearestChokepointFor(int x, int y) {
		double nearestDistance = 999999;
		Chokepoint nearestObject = null;

		for (Chokepoint chokepoint : chokePointsProcessed) {
			double distance = chokepoint.distanceTo(x, y) - chokepoint.getWidth() / 33;
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearestObject = chokepoint;
			}
		}

		return nearestObject;
	}

	public static ArrayList<Chokepoint> getNearestChokepointsFor(Position point) {
		HashMap<Chokepoint, Double> chokes = new HashMap<Chokepoint, Double>();
		if (point == null) {
			return new ArrayList<Chokepoint>();
		}

		for (Chokepoint choke : chokePointsProcessed) {
			double distance = point.distanceTo(choke) - choke.getWidth() / 32;
			chokes.put(choke, distance);
		}

		ArrayList<Chokepoint> result = new ArrayList<Chokepoint>();
		int counter = 0;
		int limitTo = 2;
		Map<Chokepoint, Double> byValue = RUtilities.sortByValue(chokes, true);
		for (Chokepoint chokePoint : byValue.keySet()) {
			if (byValue.get(chokePoint) < MAXIMUM_CHOKE_POINT_DISTANCE_FROM_BASE || counter < limitTo) {
				result.add(chokePoint);
			} else {
				if (counter >= limitTo) {
					break;
				}
			}
			counter++;
		}
		return result;
	}

	public static Point getBaseNearEnemy() {

		// Either an existing enemy base...
		if (!enemyBasesDiscovered.isEmpty() && RUtilities.rand(0, 100) < 50) {
			Unit randomEnemyBase = getRandomKnownEnemyBase();
			return new Point(randomEnemyBase.getX(), randomEnemyBase.getY());
		}
		// Or fully random base
		else {
			BaseLocation baseLocation = getRandomBaseLocation();
			return new Point(baseLocation.getX(), baseLocation.getY());
		}
	}

	public static int getNumberOfKnownEnemyBases() {
		return enemyBasesDiscovered.size();
	}

	public static Unit getRandomKnownEnemyBase() {
		if (enemyBasesDiscovered.isEmpty()) {
			return null;
		} else {
			return (Unit) RUtilities.getRandomElement(enemyBasesDiscovered.values());
		}
	}

	public static BaseLocation getRandomBaseLocation() {
		ArrayList<BaseLocation> list = new ArrayList<BaseLocation>();
		for (BaseLocation base : BaseLocation.getAllBaseLocations()) {
			list.add(base);
		}
		if (list.isEmpty()) {
			return null;
		} else {
			return (BaseLocation) RUtilities.getRandomListElement(list);
		}
	}

	public static Unit getNearestEnemyBase() {
		if (enemyBasesDiscovered.isEmpty()) {
			return null;
		} else {
			Unit ourBase = SelectUnits.firstBase();

			Unit closestBase = null;
			double closestDistance = 99999;

			for (Unit base : enemyBasesDiscovered.values()) {
				double distance = base.distanceTo(ourBase);
				if (closestDistance > distance) {
					closestBase = base;
					closestDistance = distance;
				}
			}

			// return new Point(closestBase.getX(), closestBase.getY());
			return closestBase;
		}
	}

	public static Unit getRandomEnemyBuilding() {
		if (enemyBuildingsDiscovered.isEmpty()) {
			return null;
		} else {
			return (Unit) RUtilities.getRandomElement(enemyBuildingsDiscovered.values());
		}
	}

	public static Unit getNearestEnemyBuilding() {
		return getNearestEnemyBuilding(SelectUnits.firstBase());
	}

	public static Unit getNearestEnemyBuilding(AbstractPosition nearTo) {
		if (enemyBuildingsDiscovered.isEmpty()) {
			return null;
		} else {
			Unit closestBuilding = null;
			double closestDistance = 99999;

			for (Unit building : SelectUnits.enemy().buildings().list()) {
				if (building.getType().isOnGeyser() && !building.isLifted()
						&& (!building.isAlive() || building.isInvincible() || building.getPlayer().isNeutral())) {
					continue;
				}

				double distance = building.distanceTo(nearTo);
				if (closestDistance > distance && closestDistance != 0) {
					closestBuilding = building;
					closestDistance = distance;
				}
			}

			// return new Point(closestBase.getX(), closestBase.getY());
			return closestBuilding;
		}
	}

	public static Position getNearestUnknownPointFor(int x, int y, boolean mustBeWalkable) {
		int tileX = x / 32;
		int tileY = y / 32;

		int currentDist = 3;
		int maximumDist = 90;
		while (currentDist < maximumDist) {
			for (int attempt = 0; attempt < currentDist; attempt++) {
				int i = tileX + currentDist - RUtilities.rand(0, 2 * currentDist);
				int j = tileY + currentDist - RUtilities.rand(0, 2 * currentDist);
				if (!Starwork.getGame().isExplored(i, j) && (!mustBeWalkable || (Starwork.getGame().isWalkable(i, j)))) {
					return new Position(i * 32, j * 32);
				}
			}
			currentDist += RUtilities.rand(1, maximumDist - currentDist);
		}

		// If we reach here it means we didn't find proper unexplored point.
		// Just go to explored.
		currentDist = 10;
		maximumDist = 90;
		while (currentDist < maximumDist) {
			for (int attempt = 0; attempt < currentDist; attempt++) {
				int i = tileX + currentDist - RUtilities.rand(0, 2 * currentDist);
				int j = tileY + currentDist - RUtilities.rand(0, 2 * currentDist);
				if (!mustBeWalkable || (Starwork.getGame().isWalkable(i, j))) {
					return new Position(i * 32, j * 32);
				}
			}
			currentDist += RUtilities.rand(1, maximumDist - currentDist);
		}

		return null;
	}

	public static Collection<Unit> getEnemyBuildingsDiscovered() {
		return enemyBuildingsDiscovered.values();
	}

	public static Collection<Unit> getEnemyUnitsDiscovered() {
		return enemyUnitsDiscovered.values();
	}

	public static Chokepoint getRandomChokepoint() {
		return (Chokepoint) RUtilities.getRandomListElement(chokePointsProcessed);
	}

	public static synchronized void removeNonExistingEnemyUnits() {
		// for (Iterator<Unit> iterator = enemyBuildingsDiscovered.iterator();
		// iterator
		// .hasNext();) {
		// for (Entry<Integer, Unit> entry :
		// enemyBuildingsDiscovered.entrySet()) {
		// Unit unit = entry.getValue();
		// if (unit == null || !unit.isExists() || unit.getHitPoints() < 1) {
		// // System.out.println("REMOVE " + unit.toStringShort());
		// enemyBuildingsDiscovered.remove(unit.getID());
		// }
		// }
		//
		// for (Iterator<Unit> iterator = enemyBasesDiscovered.iterator();
		// iterator
		// .hasNext();) {
		// Unit unit = (Unit) iterator.next();
		// if (unit == null || !unit.isExists() || unit.getHitPoints() < 1) {
		// iterator.remove();
		// }
		// }
		//
		// for (Iterator<Unit> iterator = enemyUnitsDiscovered.iterator();
		// iterator
		// .hasNext();) {
		// Unit unit = (Unit) iterator.next();
		// if (unit == null || !unit.isExists() || unit.getHitPoints() < 1) {
		// iterator.remove();
		// }
		// }

		synchronized (enemyBasesDiscovered) {
			removeNonExistingUnitsFrom(enemyBasesDiscovered);
		}
		synchronized (enemyBuildingsDiscovered) {
			removeNonExistingUnitsFrom(enemyBuildingsDiscovered);
		}
		synchronized (enemyUnitsDiscovered) {
			removeNonExistingUnitsFrom(enemyUnitsDiscovered);
		}
	}

	private static synchronized void removeNonExistingUnitsFrom(HashMap<Integer, Unit> mapping) {
		for (Entry<Integer, Unit> entry : mapping.entrySet()) {
			Unit unit = entry.getValue();
			if (unit == null || !unit.isAlive() || unit.getHP() < 1) {
				mapping.remove(unit.getID());
			}
		}
	}

	public static boolean enemyUnitDestroyed(int unitID) {
		boolean result = false;
		if (enemyBasesDiscovered.remove(unitID) != null) {
			result = true;
		}
		if (enemyBuildingsDiscovered.remove(unitID) != null) {
			result = true;
		}
		if (enemyUnitsDiscovered.remove(unitID) != null) {
			result = true;
		}
		return result;
	}

	public static ArrayList<? extends AbstractPosition> getBaseLocationsNear(Position point, int tileRadius) {
		ArrayList<BaseLocation> bases = new ArrayList<BaseLocation>();
		if (point == null) {
			return bases;
		}

		for (BaseLocation object : BaseLocation.getAllBaseLocations()) {
			if (object == null) {
				continue;
			}
			double distance = point.distanceTo(object);
			if (distance <= tileRadius) {
				bases.add(object);
			}
		}

		return bases;
	}

	public static void updateInfoAboutHiddenUnits() {
		_hiddenEnemyUnits.clear();
		for (Unit unit : SelectUnits.enemy().list()) {
			if (unit.isCloaked() || unit.isBurrowed() || !unit.isDetected()) {
				_hiddenEnemyUnits.add(unit);
			}
		}
	}

	public static ArrayList<Unit> getEnemyUnitsHidden() {
		return _hiddenEnemyUnits;
	}

	public static Unit getHiddenEnemyUnitNearbyTo(Unit unit) {
		if (_hiddenEnemyUnits.isEmpty()) {
			return null;
		} else {
			Unit nearestHiddenEnemy = SelectUnits.from(_hiddenEnemyUnits).nearestTo(unit);
			if (unit.distanceTo(nearestHiddenEnemy) < 12) {
				return nearestHiddenEnemy;
			}
			return null;
		}
	}

	public static void processInitialChokepoints() {
		// int initialChokes = Starwork.getGame().getChokepoints().size();
		// int mapWidth = xvr.getBwapi().getMap().getWidth();
		// int mapHeight = xvr.getBwapi().getMap().getHeight();
		//
		// final int MIN_DIST_FROM_BORDER = 6;
		//
		// // Store initial choke points
		// for (Chokepoint choke : bwapi.getMap().getChokepoints()) {
		//
		// // Filter out gigantic choke points
		// if (choke.getWidth() / 32 <= 15) {
		//
		// // Check whether this choke isn't too close to map borders
		// if (choke.getTx() <= MIN_DIST_FROM_BORDER || choke.getTx() >=
		// (mapWidth - MIN_DIST_FROM_BORDER)
		// || choke.getTy() <= MIN_DIST_FROM_BORDER || choke.getTy() >=
		// (mapHeight - MIN_DIST_FROM_BORDER)) {
		// continue;
		// } else {
		// chokePointsProcessed.add(choke);
		// }
		// }
		// }
		// int percentSkipped = 100 * (initialChokes -
		// chokePointsProcessed.size()) / initialChokes;
		// if (percentSkipped > 0) {
		// System.out.println("Skipped " + percentSkipped +
		// "% of initial choke points");
		// }
	}

	public static Chokepoint getImportantChokepointNear(AbstractPosition position) {
		ArrayList<Chokepoint> nearestChokepoints = getChokepointsForRegion(Region.getRegionFor(position), position,
				true);

		if (!nearestChokepoints.isEmpty()) {
			Position secondBase = TerranCommandCenter.getSecondBaseLocation();

			// We're at second base
			if (secondBase != null && secondBase.distanceTo(position) < 13) {
				Chokepoint chokeNearMainBase = MapExploration.getImportantChokepointNear(SelectUnits.firstBase());
				nearestChokepoints.remove(chokeNearMainBase);
				return nearestChokepoints.isEmpty() ? chokeNearMainBase : nearestChokepoints.get(0);
			} else {
				return nearestChokepoints.get(0);
			}
		} else {
			return getNearestChokepointFor(position);
		}

	}

	private static ArrayList<Chokepoint> getChokepointsForRegion(Region region, final AbstractPosition position,
			boolean disallowDisabled) {
		ArrayList<Chokepoint> result = new ArrayList<Chokepoint>();
		if (region == null) {
			return result;
		}
		for (Chokepoint choke : chokePointsProcessed) {
			if ((!disallowDisabled || !choke.isDisabled()) && choke.belongsToRegion(region)) {
				result.add(choke);
			}
		}

		Collections.sort(result, new Comparator<Chokepoint>() {
			@Override
			public int compare(Chokepoint o1, Chokepoint o2) {
				return Double.compare(o1.distanceTo(position) - o1.getWidth() / 32,
						o2.distanceTo(position) - o2.getWidth() / 32);
			}
		});

		return result;
	}

	public static ArrayList<Chokepoint> getChokepoints() {
		return chokePointsProcessed;
	}

	public static Collection<Chokepoint> getChokepointsNear(Position near, int tileRadius) {
		ArrayList<Chokepoint> chokes = new ArrayList<>();
		if (near == null) {
			return chokes;
		}

		for (Chokepoint chokepoint : chokePointsProcessed) {
			if (chokepoint == null) {
				continue;
			}
			double distance = near.distanceTo(chokepoint);
			if (distance <= tileRadius) {
				chokes.add(chokepoint);
			}
		}

		return chokes;
	}

	public static void disableChokepointsNearFirstBase() {
		if (!_disabledChokepointsNearMainBase) {
			// Region baseRegion =
			// xvr.getBwapi().getMap().getRegion(SelectUnits.firstBase());
			// Collection<Chokepoint> chokes = baseRegion.getChokepoints();
			// for (Chokepoint choke : chokes) {
			// if (baseRegion.getChokepoints().contains(choke)) {
			// // chokePointsProcessed.remove(choke);
			// System.out.println("Disabling choke point: " + choke);
			// choke.setDisabled(true);
			// }
			// }

			_disabledChokepointsNearMainBase = true;
		}
	}

	public static ArrayList<Chokepoint> getChokepointsProcessed() {
		return chokePointsProcessed;
	}

	public static void setChokepointsProcessed(ArrayList<Chokepoint> chokePointsProcessed) {
		MapExploration.chokePointsProcessed = chokePointsProcessed;
	}

	public static TreeSet<BaseLocation> getBaseLocationsDiscovered() {
		return baseLocationsDiscovered;
	}

	public static void setBaseLocationsDiscovered(TreeSet<BaseLocation> baseLocationsDiscovered) {
		MapExploration.baseLocationsDiscovered = baseLocationsDiscovered;
	}

	public static HashMap<Integer, Unit> getEnemyBasesDiscovered() {
		return enemyBasesDiscovered;
	}

	public static void setEnemyBasesDiscovered(HashMap<Integer, Unit> enemyBasesDiscovered) {
		MapExploration.enemyBasesDiscovered = enemyBasesDiscovered;
	}

	public static void setEnemyBuildingsDiscovered(HashMap<Integer, Unit> enemyBuildingsDiscovered) {
		MapExploration.enemyBuildingsDiscovered = enemyBuildingsDiscovered;
	}

	public static void setEnemyUnitsDiscovered(HashMap<Integer, Unit> enemyUnitsDiscovered) {
		MapExploration.enemyUnitsDiscovered = enemyUnitsDiscovered;
	}

	public static void setCalculatedEnemyBaseLocation(BaseLocation base) {
		calculatedEnemyBaseLocation = base;
	}

	public static BaseLocation getCalculatedEnemyBaseLocation() {
		return calculatedEnemyBaseLocation;
	}

}
