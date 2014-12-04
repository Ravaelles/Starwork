package starwork.economy.building;

import starwork.units.SelectUnits;
import starwork.units.Units;
import bwapi.Unit;

public class BuildingMajor {

	public static void giveOrders() {
		Units buildings = SelectUnits.our().buildings().units();
		for (Unit building : buildings.list()) {
			if (building.isBase()) {
				BasePrivate.act(building);
			}
		}
	}

}
