package starwork.terran;

import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import jnibwapi.model.Unit;
import jnibwapi.types.UnitType.UnitType;
import ai.core.XVR;
import ai.handling.units.UnitCounter;
import ai.managers.strategy.BotStrategyManager;

public class TerranEngineeringBay {

	private static final UnitType buildingType = UnitType.Terran_Engineering_Bay;
	private static XVR xvr = XVR.getInstance();

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.construct(xvr, buildingType);
			return;
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static boolean shouldBuild() {
		int bays = UnitCounter.getNumberOfUnits(buildingType);
		// int barracks =
		// UnitCounter.getNumberOfUnits(TerranBarracks.getBuildingType());
		int factories = UnitCounter.getNumberOfUnits(TerranFactory.getBuildingType());

		// // Version for expansion with cannons
		// if (BotStrategyManager.isExpandWithBunkers()) {
		// if (bays == 0 && xvr.canAfford(132) &&
		// !Constructing.weAreBuilding(buildingType)) {
		// // if (UnitCounter.getNumberOfBattleUnits() >= 15) {
		// ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
		// return true;
		// // }
		// }
		// }

		// Version for expansion with gateways
		if (BotStrategyManager.isExpandWithBarracks()) {
			if (bays == 0 && factories >= 2 && !Constructing.weAreBuilding(buildingType)) {
				if (UnitCounter.getNumberOfBattleUnits() >= 14) {
					// ProtossGateway.MIN_UNITS_FOR_DIFF_BUILDING - 8) {
					ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
					return true;
				}
			}
		}

		// if (bays == 1 &&
		// UnitCounter.getNumberOfUnits(TerranBarracks.getBuildingType()) >= 4
		// && xvr.canAfford(650) && !Constructing.weAreBuilding(buildingType)) {
		// if (UnitCounter.getNumberOfBattleUnits() >= 18) {
		// ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
		// return true;
		// }
		// }
		//
		// if (bays == 2 && xvr.canAfford(900) &&
		// !Constructing.weAreBuilding(buildingType)) {
		// ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
		// return true;
		// }

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
