package starwork.terran;

import starwork.economy.constructing.AddOn;
import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import jnibwapi.model.Unit;
import jnibwapi.types.UnitType.UnitType;
import ai.core.XVR;
import ai.handling.units.UnitCounter;
import ai.strategies.TerranOffensiveBunker;

public class TerranMachineShop {

	private static final UnitType buildingType = UnitType.Terran_Machine_Shop;
	private static XVR xvr = XVR.getInstance();

	// =========================================================

	public static boolean shouldBuild() {
		if (UnitCounter.weHaveBuilding(TerranFactory.getBuildingType())) {
			if (!TerranOffensiveBunker.isStrategyActive() && TerranVulture.getNumberOfUnits() == 0) {
				ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
				return false;
			}

			int factories = TerranFactory.getNumberOfUnitsCompleted();
			int addOns = getNumberOfUnits();

			boolean shouldBuild = factories > addOns;
			if (shouldBuild) {
				ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
				return true;
			}
		}

		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		return false;
	}

	// =========================================================

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.constructAddOn(
					AddOn.getBuildingWithNoAddOn(TerranFactory.getBuildingType()), buildingType);
			return;
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
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
