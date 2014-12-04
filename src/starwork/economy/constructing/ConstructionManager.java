package starwork.economy.constructing;

import java.util.HashMap;

import starwork.economy.EconomyMinister;
import starwork.presidents.TimePresident;
import starwork.terran.TerranAcademy;
import starwork.terran.TerranArmory;
import starwork.terran.TerranBarracks;
import starwork.terran.TerranBunker;
import starwork.terran.TerranCommandCenter;
import starwork.terran.TerranComsatStation;
import starwork.terran.TerranControlTower;
import starwork.terran.TerranEngineeringBay;
import starwork.terran.TerranFactory;
import starwork.terran.TerranMachineShop;
import starwork.terran.TerranMissileTurret;
import starwork.terran.TerranRefinery;
import starwork.terran.TerranScienceFacility;
import starwork.terran.TerranStarport;
import starwork.terran.TerranSupplyDepot;
import starwork.units.SelectUnits;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class ConstructionManager {

	// private static final int PROLONGATED_CONSTRUCTION_TIME = 350; // in fps

	private static HashMap<UnitType, Unit> _recentConstructionsInfo = new HashMap<>();
	private static HashMap<UnitType, Position> _recentConstructionsPlaces = new HashMap<>();
	private static HashMap<Unit, UnitType> _recentConstructionsUnitToType = new HashMap<>();
	private static HashMap<Unit, Integer> _recentConstructionsTimes = new HashMap<>();

	private static int _recentConstructionsCounter = 0;
	private static int _actCounter = 0;

	// private static int _lastCheckedForProlongated = -1;

	// ====================================

	public static void act() {
		_actCounter++;
		if (_actCounter >= 3) {
			_actCounter = 0;
		}

		// Store info about constructing given building for 3 acts, then
		// remove all data
		if (_recentConstructionsCounter++ >= 6) {
			resetInfoAboutConstructions();
		}

		// Check only every N frames
		boolean shouldBuildHQ = TerranCommandCenter.shouldBuild();
		boolean canBuildOtherThingThanHQ = !shouldBuildHQ
				|| EconomyMinister.canAfford(550);

		if (_actCounter == 0
				&& (shouldBuildHQ && !EconomyMinister.canAfford(550))) {
			TerranCommandCenter.buildIfNecessary();
		} else if (_actCounter == 1 && canBuildOtherThingThanHQ) {
			TerranAcademy.buildIfNecessary();
			TerranBunker.buildIfNecessary();
			TerranFactory.buildIfNecessary();
			TerranBarracks.buildIfNecessary();
			TerranComsatStation.buildIfNecessary();
			TerranControlTower.buildIfNecessary();
			TerranEngineeringBay.buildIfNecessary();
			TerranRefinery.buildIfNecessary();
		} else if (canBuildOtherThingThanHQ) {
			TerranBarracks.buildIfNecessary();
			TerranBunker.buildIfNecessary();
			TerranMissileTurret.buildIfNecessary();
			TerranSupplyDepot.buildIfNecessary();
			TerranStarport.buildIfNecessary();
			TerranMachineShop.buildIfNecessary();
			TerranArmory.buildIfNecessary();
			TerranScienceFacility.buildIfNecessary();
		}

		// It can happen that damned worker will stuck somewhere (what a retard)
		// if (TimePresident.getTimeSeconds() - _lastCheckedForProlongated >= 8) {
		// checkForProlongatedConstructions();
		// _lastCheckedForProlongated = TimePresident.getTimeSeconds();
		// }
	}

	// private static void checkForProlongatedConstructions() {
	// int now = xvr.getFrames();
	// for (Unit builder : _recentConstructionsTimes.keySet()) {
	// if (!builder.isConstructing()) {
	// continue;
	// }
	//
	// if (now - _recentConstructionsTimes.get(builder) >
	// PROLONGATED_CONSTRUCTION_TIME) {
	// Position buildTile = _recentConstructionsPlaces.get(builder);
	// UnitType building = _recentConstructionsUnitToType.get(builder);
	//
	// // Issue new construction order
	// Constructing.constructBuilding(xvr, building, buildTile);
	//
	// // Cancel previous construction by moving the unit
	// UnitActions.moveTo(builder, SelectUnits.firstBase());
	// }
	// }
	// }

	public static boolean weAreBuilding(UnitType type) {
		if (_recentConstructionsInfo.containsKey(type)) {
			return true;
		}
		for (Unit unit : SelectUnits.our().units().list()) {
			if ((!unit.isCompleted() && unit.getType() == type)
					|| unit.getBuildType() == type) {
				return true;
			}
		}
		return false;
	}

	private static void resetInfoAboutConstructions() {
		_recentConstructionsCounter = 0;
		_recentConstructionsInfo.clear();
	}

	protected static void addInfoAboutConstruction(UnitType building,
			Unit builder, Position buildTile) {
		_recentConstructionsCounter = 0;
		_recentConstructionsInfo.put(building, builder);
		_recentConstructionsPlaces.put(building, buildTile);
		_recentConstructionsUnitToType.put(builder, building);
		_recentConstructionsTimes.put(builder, TimePresident.getTimeFrames());
		ShouldBuildCache.cacheShouldBuildInfo(building, false);
	}

	public static HashMap<UnitType, Position> getRecentConstructionsPlaces() {
		return _recentConstructionsPlaces;
	}

}
