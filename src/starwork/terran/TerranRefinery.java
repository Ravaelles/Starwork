package starwork.terran;

import starwork.economy.EconomyMinister;
import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import starwork.helpers.UnitHelper;
import starwork.strategy.BotStrategyManager;
import starwork.units.UnitCounter;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class TerranRefinery {

	private static final UnitType buildingType = UnitType.Terran_Refinery;

	// =========================================================

	public static boolean shouldBuild() {
		int minGateways = BotStrategyManager.isExpandWithBunkers() ? 3 : 4;
		int barracks = UnitCounter.getNumberOfUnits(UnitHelper.TYPE_BARRACKS);
		int refineries = UnitCounter.getNumberOfUnits(buildingType);
		int battleUnits = UnitCounter.getNumberOfBattleUnits();
		boolean weHaveAcademy = UnitCounter.weHaveBuilding(TerranAcademy.getBuildingType());

		// =========================================================

		if (refineries == 0) {
			if (xvr.getSuppliesUsed() >= 18) {
				return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			}

			// boolean isEnoughInfantry = (battleUnits >= 6 || battleUnits >=
			// ArmyCreationManager.MINIMUM_MARINES);
			// boolean isAnotherBaseAndFreeMinerals =
			// TerranCommandCenter.getNumberOfUnits() > 1
			// || xvr.canAfford(468)
			// || (TerranBarracks.getNumberOfUnitsCompleted() == 0
			// && TerranBarracks.getNumberOfUnits() > 0 && xvr.canAfford(134));
			// if (isEnoughInfantry || isAnotherBaseAndFreeMinerals) {
			// return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			// }
		}

		if (UnitCounter.getNumberOfUnitsCompleted(TerranEngineeringBay.getBuildingType()) == 0
				&& UnitCounter.getNumberOfUnits(TerranBunker.getBuildingType()) == 0) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
			return false;
		}

		if (!Constructing.weAreBuilding(buildingType)
				&& UnitCounter.getNumberOfUnits(buildingType) < UnitCounter.getNumberOfUnitsCompleted(UnitHelper.BASE)
				&& weHaveAcademy) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			return true;
		}

		if (!Constructing.weAreBuilding(buildingType)
				&& (weHaveAcademy || barracks >= minGateways || xvr.canAfford(700))
				&& UnitCounter.getNumberOfUnits(buildingType) < UnitCounter.getNumberOfUnitsCompleted(UnitHelper.BASE)) {
			if (battleUnits >= TerranBarracks.MIN_UNITS_FOR_DIFF_BUILDING) {
				ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
				return true;
			}
		}

		if (UnitCounter.getNumberOfUnits(buildingType) < UnitCounter.getNumberOfUnitsCompleted(UnitHelper.BASE)
				&& EconomyMinister.canAfford(750)) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			return true;
		}

		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		return false;
	}

	// =========================================================

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.construct(xvr, buildingType);
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
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

	public static Position findTileForRefinery() {
		Unit nearestGeyser = xvr.getUnitNearestFromList(SelectUnits.firstBase(), xvr.getGeysersUnits());
		if (nearestGeyser != null
				&& xvr.getUnitsOfGivenTypeInRadius(UnitHelper.BASE, 15, nearestGeyser, true).isEmpty()) {
			return null;
		}

		if (nearestGeyser != null) {
			return new Position(nearestGeyser.getX() - 64, nearestGeyser.getY() - 32);
		} else {
			return null;
		}
	}

}
