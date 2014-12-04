package starwork.terran;

import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import starwork.main.Starwork;
import starwork.presidents.TimePresident;
import starwork.units.SelectUnits;
import starwork.units.UnitCounter;
import bwapi.Unit;
import bwapi.UnitType;

public class TerranAcademy {

	private static final UnitType buildingType = UnitType.Terran_Academy;

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			Constructing.construct(buildingType);
		}
		ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static boolean shouldBuild() {
		boolean weAreBuilding = Constructing.weAreBuilding(buildingType);

		if (Starwork.getEnemy().getRace().isTerran()) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		}

		if (weAreBuilding) {
			return ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
		}

		int academies = getNumberOfUnits();
		if (academies == 0 && TimePresident.getTimeSeconds() >= 275) {
			int barracks = TerranBarracks.getNumberOfUnitsCompleted();

			if (barracks >= TerranBarracks.MAX_BARRACKS && !weAreBuilding && UnitCounter.getNumberOfBattleUnits() >= 5) {
				return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			}

			if (TerranRefinery.getNumberOfUnitsCompleted() == 1 || TerranFactory.getNumberOfUnits() == 1) {
				return ShouldBuildCache.cacheShouldBuildInfo(buildingType, true);
			}
		}

		return ShouldBuildCache.cacheShouldBuildInfo(buildingType, false);
	}

	public static Unit getOneNotBusy() {
		for (Unit unit : SelectUnits.our().ofType(buildingType).list()) {
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
