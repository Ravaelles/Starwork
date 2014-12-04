package starwork.terran;

import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import jnibwapi.model.Unit;
import jnibwapi.types.UnitType.UnitType;
import ai.core.XVR;
import ai.handling.units.UnitCounter;

public class TerranScienceFacility {

	private static final UnitType buildingType = UnitType.Terran_Science_Facility;
	private static XVR xvr = XVR.getInstance();

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.construct(xvr, buildingType);
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static boolean shouldBuild() {
		int starports = UnitCounter.getNumberOfUnits(TerranStarport.getBuildingType());
		int scienceFacilities = getNumberOfUnits();
		boolean weAreBuilding = Constructing.weAreBuilding(buildingType);

		if (scienceFacilities == 0 && starports >= 2 && !weAreBuilding) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			return true;
		}

		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		return false;
	}

	public static Unit getOneNotBusy() {
		for (Unit unit : xvr.getUnitsOfType(buildingType)) {
			if (unit.isBuildingNotBusy()) {
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

}
