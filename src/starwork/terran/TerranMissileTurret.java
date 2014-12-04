package starwork.terran;

import starwork.economy.constructing.Constructing;
import starwork.economy.constructing.ShouldBuildCache;
import starwork.units.SelectUnits;
import starwork.units.UnitCounter;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class TerranMissileTurret {

	private static final UnitType type = UnitType.Terran_Missile_Turret;

	private static final int MIN_DIST_OF_TURRET_FROM_BUNKER = 1;
	private static final int MAX_DIST_OF_TURRET_FROM_BUNKER = 15;

	// ==========================================

	public static void buildIfNecessary() {
		if (shouldBuild()) {
			ShouldBuildCache.cacheShouldBuildInfo(type, true);
			Constructing.construct(xvr, type);
		}
		ShouldBuildCache.cacheShouldBuildInfo(type, false);
	}

	public static boolean shouldBuild() {
		if (!UnitCounter.weHaveBuildingFinished(UnitType.Terran_Engineering_Bay) || Constructing.weAreBuilding(type)) {
			ShouldBuildCache.cacheShouldBuildInfo(type, false);
			return false;
		}

		int bunkers = TerranBunker.getNumberOfUnitsCompleted();
		int turrets = getNumberOfUnits();
		// System.out.println("2 --> " + bunkers + " / " + turrets);

		if (bunkers > 0 && bunkers > turrets) {
			Position buildTile = findTileForTurret();
			if (buildTile != null) {
				ShouldBuildCache.cacheShouldBuildInfo(type, true);
				return true;
			}
		}

		ShouldBuildCache.cacheShouldBuildInfo(type, false);
		return false;
	}

	public static Position findTileForTurret() {

		// Every bunker needs to have one Turret nearby (acting as a detector)
		for (Unit bunker : SelectUnits.our().ofType(TerranBunker.getBuildingType()).list()) {
			if (xvr.countUnitsOfGivenTypeInRadius(type, MAX_DIST_OF_TURRET_FROM_BUNKER, bunker, true) == 0) {
				Position tileForTurret = Constructing.getLegitTileToBuildNear(type, bunker,
						MIN_DIST_OF_TURRET_FROM_BUNKER, MAX_DIST_OF_TURRET_FROM_BUNKER);
				// System.out.println("###tile## ForTurret = " + tileForTurret +
				// " / bunker: "
				// + bunker.toStringLocation());
				return tileForTurret;
			}
		}

		return null;
	}

	public static UnitType getBuildingType() {
		return type;
	}

	public static int getNumberOfUnits() {
		return UnitCounter.getNumberOfUnits(type);
	}

	public static int getNumberOfUnitsCompleted() {
		return UnitCounter.getNumberOfUnitsCompleted(type);
	}

}
