package starwork.terran;

import starwork.economy.constructing.AddOn;
import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import jnibwapi.model.Unit;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType.UnitType;
import ai.core.XVR;
import ai.handling.map.MapExploration;
import ai.handling.map.Position;
import ai.handling.units.UnitActions;
import ai.handling.units.UnitCounter;

public class TerranComsatStation {

	private static final UnitType buildingType = UnitType.Terran_Comsat_Station;
	private static XVR xvr = XVR.getInstance();

	private static int lastTimeScannedSecondEnemyBase = -1;

	public static void act(Unit unit) {
		if (unit.getEnergy() >= 199) {
			tryRandomScan(unit);
		}
	}

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.constructAddOn(
					AddOn.getBuildingWithNoAddOn(UnitType.Terran_Command_Center), buildingType);
			return;
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static boolean shouldBuild() {
		if (UnitCounter.weHaveBuilding(TerranAcademy.getBuildingType())) {
			int bases = TerranCommandCenter.getNumberOfUnitsCompleted();
			int comsats = getNumberOfUnits();

			boolean shouldBuild = bases > comsats;
			if (shouldBuild) {
				return true;
			}
		}
		return false;
	}

	public static Unit getOneNotBusy() {
		for (Unit unit : xvr.getUnitsOfType(buildingType)) {
			if (unit.isCompleted() && unit.isBuildingNotBusy()) {
				return unit;
			}
		}
		return null;
	}

	public static UnitType getBuildingType() {
		return buildingType;
	}

	public static int getNumberOfUnits() {
		return UnitCounter.getNumberOfUnits(buildingType);
	}

	public static int getNumberOfUnitsCompleted() {
		return UnitCounter.getNumberOfUnitsCompleted(buildingType);
	}

	// ==============================================================

	private static void tryRandomScan(Unit comsat) {
		if (shouldScanSecondEnemyBaseLocation()) {
			scanSecondEnemyBaseLocation(comsat);
		} else {
			scanFullyRandomPlace(comsat);
		}
	}

	private static void scanFullyRandomPlace(Unit comsat) {
		scan(comsat, MapExploration.getRandomKnownEnemyBase());
	}

	private static void scanSecondEnemyBaseLocation(Unit comsat) {
		scan(comsat, MapExploration.getNearestEnemyBase());
	}

	private static boolean shouldScanSecondEnemyBaseLocation() {
		return lastTimeScannedSecondEnemyBase == -1
				|| (TimePresident.getTimeSeconds() - lastTimeScannedSecondEnemyBase > 600);
	}

	public static void tryToScanPoint(Position point) {
		if (!UnitCounter.weHaveBuildingFinished(UnitType.Terran_Comsat_Station)) {
			return;
		}

		Unit comsat = getOneWithMostEnergy();
		if (comsat != null) {
			scan(comsat, point);
		}
	}

	private static void scan(Unit comsat, Position point) {
		if (point == null) {
			return;
		}
		UnitActions.useTech(comsat, TechTypes.Scanner_Sweep, point);
	}

	private static Unit getOneWithMostEnergy() {
		int maxEnergy = 49;
		Unit bestObject = null;
		for (Unit unit : xvr.getUnitsOfType(UnitType.Terran_Comsat_Station)) {
			if (unit.getEnergy() > maxEnergy) {
				maxEnergy = unit.getEnergy();
				bestObject = unit;
			}
		}

		return bestObject;
	}

	public static void hiddenUnitDetected(Unit unit) {
		if (UnitCounter.weHaveBuildingFinished(UnitType.Terran_Comsat_Station)) {

			// if you'll discover Protoss Observer, don't waste energy if you
			// don't have too much of it
			if (unit.getType().isObserver()) {
				Unit comsat = getOneWithMostEnergy();
				if (comsat != null && comsat.getEnergy() < 130) {
					return;
				}
			}
			// Debug.message(xvr, "Hidden " + unit.getName());

			// Scout the point where the unit is
			tryToScanPoint(unit);
		}
	}

}
