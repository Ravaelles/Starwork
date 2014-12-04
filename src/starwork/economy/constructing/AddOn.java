package starwork.economy.constructing;

import starwork.units.SelectUnits;
import bwapi.Unit;
import bwapi.UnitType;

public class AddOn {

	public static Unit getBuildingWithNoAddOn(UnitType parentType) {
		for (Unit building : SelectUnits.our().ofType(parentType).units()
				.shuffle().list()) {
			if (building.getAddon() == null) {
				return building;
			}
		}
		return null;
	}

}
