package starwork.economy.building;

import starwork.helpers.UnitHelper;
import starwork.units.SelectUnits;
import starwork.units.Units;
import bwapi.Unit;

public class BuildingMajor {

	public static void giveOrders() {
		Units buildings = SelectUnits.our().buildings().units();
		for (Unit building : buildings.list()) {
			if (UnitHelper.isBase(building)) {
				BasePrivate.act(building);
			}
		}
	}

}
