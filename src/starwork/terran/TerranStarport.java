package starwork.terran;

import java.util.ArrayList;

import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import jnibwapi.model.Unit;
import jnibwapi.types.UnitType.UnitType;
import ai.core.XVR;
import ai.handling.units.UnitCounter;
import ai.managers.economy.TechnologyManager;

public class TerranStarport {

	public static UnitType BATTLECRUISER = UnitType.Terran_Battlecruiser;
	public static UnitType DROPSHIP = UnitType.Terran_Dropship;
	public static UnitType VALKYRIE = UnitType.Terran_Valkyrie;
	public static UnitType WRAITH = UnitType.Terran_Wraith;
	public static UnitType SCIENCE_VESSEL = UnitType.Terran_Science_Vessel;

	private static final int MIN_VESSELS = 1;
	// private static final int MINIMUM_BATTLECRUISERS = 2;
	private static final int MAX_WRAITHS = 2;
	// private static final int MINIMUM_VALKYRIES = 3;
	// private static final int VALKYRIES_PER_OTHER_AIR_UNIT = 2;

	private static final UnitType buildingType = UnitType.Terran_Starport;
	private static XVR xvr = XVR.getInstance();

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			Constructing.construct(xvr, buildingType);
		}
	}

	public static boolean shouldBuild() {
		int starports = UnitCounter.getNumberOfUnits(buildingType);

		if (TerranFactory.getNumberOfUnitsCompleted() >= 3 && starports == 0
				&& TerranSiegeTank.getNumberOfUnits() >= 5
				&& !TechnologyManager.isSiegeModeResearchPossible()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			return true;
		}

		if (starports == 1 && xvr.canAfford(600, 400)) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			return true;
		}

		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
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

	public static ArrayList<Unit> getAllObjects() {
		return xvr.getUnitsOfType(buildingType);
	}

	// ==========================
	// Unit creating

	public static void act(Unit facility) {
		if (facility == null) {
			return;
		}

		int[] buildingQueueDetails = Constructing.shouldBuildAnyBuilding();
		int freeMinerals = xvr.getMinerals();
		int freeGas = xvr.getGas();
		if (buildingQueueDetails != null) {
			freeMinerals -= buildingQueueDetails[0];
			freeGas -= buildingQueueDetails[1];
		}

		if (buildingQueueDetails == null || (freeMinerals >= 200 && freeGas >= 400)) {
			if (facility.getTrainingQueueSize() == 0) {
				xvr.buildUnit(facility, defineUnitToBuild(freeMinerals, freeGas));
			}
		}
	}

	// =========================================================

	private static UnitType defineUnitToBuild(int freeMinerals, int freeGas) {
		// boolean arbiterAllowed =
		// UnitCounter.weHaveBuilding(UnitType.Protoss_Arbiter_Tribunal);
		boolean valkyrieAllowed = UnitCounter.weHaveBuilding(UnitType.Terran_Control_Tower);
		boolean scienceVesselAllowed = UnitCounter
				.weHaveBuilding(UnitType.Terran_Science_Facility);

		// // BATTLECRUISER
		// if (arbiterAllowed && xvr.countUnitsOfType(BATTLECRUISER) <
		// MINIMUM_BATTLECRUISERS) {
		// return BATTLECRUISER;
		// }

		// SCIENCE VESSEL
		if (scienceVesselAllowed && TerranScienceVessel.getNumberOfUnits() < MIN_VESSELS) {
			return SCIENCE_VESSEL;
		}

		// VALKYRIE
		if (valkyrieAllowed
				&& UnitCounter.getNumberOfUnitsCompleted(UnitType.Terran_Valkyrie) <= 3) {
			return VALKYRIE;
		}

		// WRAITH
		if (UnitCounter.getNumberOfUnits(WRAITH) > MAX_WRAITHS) {
			return WRAITH;
		}

		return null;

		// // VALKYRIE
		// if (UnitCounter.getNumberOfUnits(VALKYRIE) < MINIMUM_VALKYRIES
		// || (UnitCounter.countAirUnitsNonValkyrie() *
		// VALKYRIES_PER_OTHER_AIR_UNIT < UnitCounter
		// .getNumberOfUnits(VALKYRIE))) {
		// return VALKYRIE;
		// }

		// return null;
	}

	public static int getNumberOfUnits() {
		return UnitCounter.getNumberOfUnits(buildingType);
	}

	public static int getNumberOfUnitsCompleted() {
		return UnitCounter.getNumberOfUnitsCompleted(buildingType);
	}

}
