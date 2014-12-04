package starwork.economy.constructing;

import java.util.HashMap;
import java.util.Set;

import bwapi.UnitType;

public class ShouldBuildCache {

	private static HashMap<UnitType, Boolean> shouldBuildMap = new HashMap<UnitType, Boolean>();

	public static boolean cacheShouldBuildInfo(UnitType buildingType,
			boolean shouldBuild) {
		if (shouldBuild) {
			shouldBuildMap.put(buildingType, true);
		} else {
			shouldBuildMap.remove(buildingType);
		}

		return shouldBuild;
	}

	public static boolean getCachedValueOfShouldBuild(UnitType buildingType) {
		return shouldBuildMap.get(buildingType);
	}

	public static Set<UnitType> getBuildingsThatShouldBeBuild() {
		return shouldBuildMap.keySet();
	}

}
