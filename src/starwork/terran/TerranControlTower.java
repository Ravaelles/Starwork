package starwork.terran;

import starwork.economy.constructing.AddOn;
import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import jnibwapi.model.Unit;
import jnibwapi.types.UnitType.UnitType;
import ai.core.XVR;
import ai.handling.units.UnitCounter;

public class TerranControlTower {

	private static final UnitType buildingType = UnitType.Terran_Control_Tower;
	private static XVR xvr = XVR.getInstance();

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.constructAddOn(
					AddOn.getBuildingWithNoAddOn(TerranStarport.getBuildingType()), buildingType);
			return;
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static boolean shouldBuild() {
		if (UnitCounter.weHaveBuilding(TerranStarport.getBuildingType())) {
			int starports = TerranStarport.getNumberOfUnitsCompleted();
			int addOns = getNumberOfUnits();

			boolean shouldBuild = starports > addOns;
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

}
